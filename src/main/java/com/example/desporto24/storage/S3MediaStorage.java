package com.example.desporto24.storage;

import com.example.desporto24.configuration.MediaStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

@Slf4j
@Component
@ConditionalOnProperty(name = "desporto.media.provider", havingValue = "s3")
public class S3MediaStorage implements MediaStorage {

    private final MediaStorageProperties properties;
    private volatile S3Client s3Client;

    public S3MediaStorage(MediaStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public URI store(String key, MultipartFile file) {
        return storeValidated(key, MediaStorageSupport.validate(file));
    }

    @Override
    public URI storeVideo(String key, MultipartFile file) {
        return storeValidated(key, MediaStorageSupport.validateVideo(file));
    }

    private URI storeValidated(String key, MediaStorageSupport.ValidatedMedia media) {
        String objectKey = key + media.extension();
        MediaStorageProperties.S3 s3 = properties.getS3();

        client().putObject(
                PutObjectRequest.builder()
                        .bucket(required(s3.getBucket(), "desporto.media.s3.bucket"))
                        .key(objectKey)
                        .contentType(media.mimeType())
                        .build(),
                RequestBody.fromBytes(media.bytes())
        );

        String publicBaseUrl = properties.getPublicBaseUrl();
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return URI.create(stripTrailingSlash(publicBaseUrl) + "/" + objectKey);
        }

        return URI.create("s3://" + s3.getBucket() + "/" + objectKey);
    }

    @Override
    public void delete(String key) {
        MediaStorageProperties.S3 s3 = properties.getS3();
        client().deleteObject(DeleteObjectRequest.builder()
                .bucket(required(s3.getBucket(), "desporto.media.s3.bucket"))
                .key(key)
                .build());
    }

    private S3Client client() {
        S3Client current = s3Client;
        if (current != null) {
            return current;
        }

        synchronized (this) {
            if (s3Client == null) {
                s3Client = buildClient(properties);
            }
            return s3Client;
        }
    }

    private static S3Client buildClient(MediaStorageProperties properties) {
        MediaStorageProperties.S3 s3 = properties.getS3();

        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(s3.getRegion()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(s3.getEndpoint() != null
                                && !s3.getEndpoint().isBlank())
                        .build());

        if (s3.getEndpoint() != null && !s3.getEndpoint().isBlank()) {
            URI endpoint = URI.create(s3.getEndpoint());
            String host = endpoint.getHost() == null ? "" : endpoint.getHost().toLowerCase();

            if (host.endsWith(".r2.dev")) {
                throw new IllegalStateException(
                        "desporto.media.s3.endpoint must be the R2 S3 API endpoint (*.r2.cloudflarestorage.com), not the public r2.dev URL"
                );
            }

            log.info("Configuring S3 media storage for bucket {} via {}",
                    s3.getBucket(), host);
            builder.endpointOverride(endpoint);
        }

        if (s3.getAccessKey() != null && !s3.getAccessKey().isBlank()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            s3.getAccessKey(),
                            required(s3.getSecretKey(), "desporto.media.s3.secret-key")
                    )
            ));
        }

        return builder.build();
    }

    private static String required(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " is required for S3 media storage");
        }
        return value;
    }

    private static String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
