package com.example.desporto24.storage;

import com.example.desporto24.configuration.MediaStorageProperties;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

/**
 * Direct-chat attachments must never enter MediaStorage, whose S3 bucket and
 * /media/** local directory are public. All downloads go via authorized HTTP.
 * For S3 deployments a DEDICATED private bucket is mandatory (fail closed).
 */
@Component
@RequiredArgsConstructor
public class PrivateChatFileStorage {
    public static final long MAX_BYTES = 5L * 1024L * 1024L;
    private static final String PREFIX = "private-chat/";
    private final MediaStorageProperties settings;
    private volatile S3Client s3Client;
    // Avoid an R2 network request for every composer refresh. A failed check
    // remains unavailable and can be retried after a short bounded interval.
    private volatile Boolean privateBucketReady;
    private volatile long privateBucketCheckedAtNanos;
    private static final long BUCKET_CHECK_NANOS = java.util.concurrent.TimeUnit.SECONDS.toNanos(30);

    public record StoredFile(String key, String name, String mime, long size) {}
    private record ValidFile(byte[] bytes, String name, String mime, String extension) {}

    public boolean isConfigured() {
        if ("s3".equalsIgnoreCase(settings.getProvider())) {
            if (!hasPrivateR2Settings()) return false;
            long now = System.nanoTime();
            Boolean cached = privateBucketReady;
            if (cached != null && now - privateBucketCheckedAtNanos < BUCKET_CHECK_NANOS) {
                return cached;
            }
            synchronized (this) {
                now = System.nanoTime();
                cached = privateBucketReady;
                if (cached != null && now - privateBucketCheckedAtNanos < BUCKET_CHECK_NANOS) {
                    return cached;
                }
                boolean ready;
                try {
                    // R2 implements S3 HeadBucket. A configured name alone does
                    // not prove that the bucket exists or credentials can access it.
                    client().headBucket(HeadBucketRequest.builder()
                            .bucket(privateBucket()).build());
                    ready = true;
                } catch (RuntimeException unavailable) {
                    ready = false; // Fail closed; never fall back to the public feed bucket.
                }
                privateBucketReady = ready;
                privateBucketCheckedAtNanos = System.nanoTime();
                return ready;
            }
        }
        if ("local".equalsIgnoreCase(settings.getProvider())) {
            Path publicRoot = Path.of(settings.getLocalRoot()).toAbsolutePath().normalize();
            Path privateRoot = Path.of(settings.getPrivateRoot()).toAbsolutePath().normalize();
            return !privateRoot.startsWith(publicRoot) && !publicRoot.startsWith(privateRoot);
        }
        return false;
    }

    private boolean hasPrivateR2Settings() {
        MediaStorageProperties.S3 s3 = settings.getS3();
        String bucket = settings.getPrivateBucket();
        if (bucket == null || bucket.isBlank() || bucket.equals(s3.getBucket())
                || s3.getEndpoint() == null || s3.getEndpoint().isBlank()
                || settings.getPrivateAccessKey() == null || settings.getPrivateAccessKey().isBlank()
                || settings.getPrivateSecretKey() == null || settings.getPrivateSecretKey().isBlank()) return false;
        try {
            URI uri = URI.create(s3.getEndpoint());
            return "https".equalsIgnoreCase(uri.getScheme())
                    && uri.getHost() != null
                    && uri.getHost().toLowerCase(Locale.ROOT).endsWith(".r2.cloudflarestorage.com");
        } catch (IllegalArgumentException invalidEndpoint) {
            return false;
        }
    }

    public StoredFile store(MultipartFile file) {
        if (!isConfigured()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Private attachment storage is unavailable");
        }
        // Reject malicious content before writing anywhere.
        ValidFile validated = validate(file);
        String key = PREFIX + UUID.randomUUID() + validated.extension();
        if (usesS3()) {
            client().putObject(PutObjectRequest.builder().bucket(privateBucket())
                    .key(key).contentType(validated.mime()).build(),
                    RequestBody.fromBytes(validated.bytes()));
        } else {
            Path path = privatePath(key);
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, validated.bytes());
            } catch (IOException exception) {
                throw new IllegalStateException("Could not store private chat attachment", exception);
            }
        }
        return new StoredFile(key, validated.name(), validated.mime(), validated.bytes().length);
    }

    public byte[] read(String key) {
        validKey(key);
        if (!isConfigured()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Private attachment storage is unavailable");
        }
        if (usesS3()) {
            return client().getObjectAsBytes(GetObjectRequest.builder()
                    .bucket(privateBucket()).key(key).build()).asByteArray();
        }
        try {
            return Files.readAllBytes(privatePath(key));
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found");
        }
    }

    public void delete(String key) {
        validKey(key);
        if (usesS3()) {
            client().deleteObject(DeleteObjectRequest.builder()
                    .bucket(privateBucket()).key(key).build());
            return;
        }
        try {
            Files.deleteIfExists(privatePath(key));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not remove private chat attachment", exception);
        }
    }

    private static ValidFile validate(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getSize() > MAX_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Attachment must be non-empty and at most 5 MB");
        }
        String name = file.getOriginalFilename() == null ? "anexo" : file.getOriginalFilename()
                .replace('\\', '/').replaceAll("[\\p{Cntrl}]", "");
        name = name.substring(name.lastIndexOf('/') + 1).trim();
        if (name.isEmpty()) name = "anexo";
        if (name.length() > 160) name = name.substring(name.length() - 160);
        try {
            byte[] bytes = file.getBytes();
            if (bytes.length == 0 || bytes.length > MAX_BYTES) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid attachment size");
            }
            String detected = new Tika().detect(bytes);
            if ("application/pdf".equals(detected)) {
                String declared = file.getContentType();
                if (bytes.length < 5 || bytes[0] != '%' || bytes[1] != 'P'
                        || bytes[2] != 'D' || bytes[3] != 'F' || bytes[4] != '-') {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid PDF");
                }
                if (declared != null && !declared.isBlank()
                        && !"application/pdf".equalsIgnoreCase(declared)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PDF MIME mismatch");
                }
                return new ValidFile(bytes, name, detected, ".pdf");
            }
            // Browser voice notes are Opus recordings. Validate both the
            // declared format and recognizable audio headers before upload.
            // Files are always served as attachment from an authenticated URL.
            String declared = file.getContentType() == null ? ""
                    : file.getContentType().split(";")[0].trim().toLowerCase(Locale.ROOT);
            if ("audio/webm".equals(declared)) {
                if (!isOpusWebm(bytes)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid WebM/Opus audio");
                }
                return new ValidFile(bytes, name, "audio/webm", ".webm");
            }
            if ("audio/ogg".equals(declared)) {
                if (!isOpusOgg(bytes)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Ogg/Opus audio");
                }
                return new ValidFile(bytes, name, "audio/ogg", ".ogg");
            }
            // Shared image validation detects real bytes, checks declared MIME,
            // verifies GIF frames and limits GIF dimensions to 1024x1024.
            MediaStorageSupport.ValidatedMedia image = MediaStorageSupport.validateCommunityPhoto(file);
            return new ValidFile(image.bytes(), name, image.mimeType(), image.extension());
        } catch (IOException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or unsupported attachment", ex);
        }
    }

    private static boolean isOpusWebm(byte[] data) {
        if (data.length < 32 || (data[0] & 0xff) != 0x1a
                || (data[1] & 0xff) != 0x45 || (data[2] & 0xff) != 0xdf
                || (data[3] & 0xff) != 0xa3) return false;
        // Require an EBML WebM DocType and an Opus audio track, rather than
        // trusting a user-controlled filename or Content-Type alone.
        String prefix = new String(data, 0, Math.min(data.length, 65536),
                StandardCharsets.ISO_8859_1);
        if (!prefix.substring(0, Math.min(prefix.length(), 1024)).contains("webm")
                || !prefix.contains("A_OPUS")) return false;
        // EBML TrackType = 2 (audio), encoded as ID 0x83, size 0x81, value 0x02.
        for (int i = 4; i + 2 < Math.min(data.length, 65536); i++) {
            if ((data[i] & 0xff) == 0x83 && (data[i+1] & 0xff) == 0x81
                    && (data[i+2] & 0xff) == 0x02) return true;
        }
        return false;
    }

    private static boolean isOpusOgg(byte[] data) {
        if (data.length < 48 || data[0] != 'O' || data[1] != 'g'
                || data[2] != 'g' || data[3] != 'S') return false;
        String header = new String(data, 0, Math.min(data.length, 512),
                StandardCharsets.ISO_8859_1);
        return header.contains("OpusHead");
    }

    private boolean usesS3() {
        if ("s3".equalsIgnoreCase(settings.getProvider())) return true;
        if ("local".equalsIgnoreCase(settings.getProvider())) return false;
        throw new IllegalStateException("Unsupported attachment storage provider");
    }

    private String privateBucket() {
        String bucket = settings.getPrivateBucket();
        if (bucket == null || bucket.isBlank()
                || bucket.equals(settings.getS3().getBucket())) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Private attachment bucket is not configured");
        }
        return bucket;
    }

    private static void validKey(String key) {
        if (key == null || !key.matches("private-chat/[a-f0-9-]{36}\\.(jpg|png|webp|gif|pdf|webm|ogg)")) {
            throw new IllegalArgumentException("Invalid private attachment key");
        }
    }

    private Path privatePath(String key) {
        validKey(key);
        Path publicRoot = Path.of(settings.getLocalRoot()).toAbsolutePath().normalize();
        Path privateRoot = Path.of(settings.getPrivateRoot()).toAbsolutePath().normalize();
        if (privateRoot.startsWith(publicRoot) || publicRoot.startsWith(privateRoot)) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Private attachment directory overlaps public media");
        }
        return privateRoot.resolve(key).normalize();
    }

    private S3Client client() {
        // Check configuration before creating an external client.
        privateBucket();
        S3Client current = s3Client;
        if (current != null) return current;
        synchronized (this) {
            if (s3Client == null) {
                MediaStorageProperties.S3 s = settings.getS3();
                S3ClientBuilder builder = S3Client.builder().region(Region.of(s.getRegion()));
                if (s.getEndpoint() != null && !s.getEndpoint().isBlank()) {
                    URI endpoint = URI.create(s.getEndpoint());
                    if (endpoint.getHost() == null
                            || endpoint.getHost().toLowerCase(Locale.ROOT).endsWith(".r2.dev")) {
                        throw new IllegalStateException("Use a private S3 API endpoint");
                    }
                    builder.endpointOverride(endpoint)
                            .serviceConfiguration(S3Configuration.builder()
                                    .pathStyleAccessEnabled(true).build());
                }
                builder.credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(settings.getPrivateAccessKey(),
                                settings.getPrivateSecretKey())));
                s3Client = builder.build();
            }
            return s3Client;
        }
    }
}
