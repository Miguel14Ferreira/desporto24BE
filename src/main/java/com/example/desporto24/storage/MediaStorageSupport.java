package com.example.desporto24.storage;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

final class MediaStorageSupport {

    static final long MAX_BYTES = 5L * 1024L * 1024L;
    static final long MAX_VIDEO_BYTES = 20L * 1024L * 1024L;
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private static final Tika TIKA = new Tika();

    private MediaStorageSupport() {
    }

    static ValidatedMedia validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file must not be empty");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("Image file must not exceed 5 MB");
        }

        try {
            byte[] bytes = file.getBytes();
            String detected = TIKA.detect(bytes);
            String extension = EXTENSIONS.get(detected);
            if (extension == null) {
                throw new IllegalArgumentException("Unsupported image type");
            }

            String declared = file.getContentType();
            if (declared != null && !declared.isBlank() && !detected.equalsIgnoreCase(declared)) {
                throw new IllegalArgumentException("Declared image type does not match file contents");
            }

            return new ValidatedMedia(bytes, detected, extension);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read image file", e);
        }
    }

    static ValidatedMedia validateVideo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file must not be empty");
        }
        if (file.getSize() > MAX_VIDEO_BYTES) {
            throw new IllegalArgumentException("Video file must not exceed 20 MB");
        }
        try {
            byte[] bytes = file.getBytes();
            // Strict container signature + declared MIME. Apache Tika alone cannot
            // reliably classify all MP4 and WebM headers without a complete video.
            String detected;
            String extension;
            if (isMp4(bytes)) {
                detected = "video/mp4";
                extension = ".mp4";
            } else if (isWebm(bytes)) {
                detected = "video/webm";
                extension = ".webm";
            } else {
                throw new IllegalArgumentException("Unsupported video type: MP4 or WebM required");
            }
            if (!detected.equalsIgnoreCase(file.getContentType())) {
                throw new IllegalArgumentException("Declared video type does not match file contents");
            }
            return new ValidatedMedia(bytes, detected, extension);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read video file", e);
        }
    }

    private static boolean isMp4(byte[] bytes) {
        if (bytes.length < 16 || !matches(bytes, 4, "ftyp")) {
            return false;
        }
        int boxLength = ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16)
                | ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
        if (boxLength < 16 || boxLength > bytes.length) {
            return false;
        }
        String brand = new String(bytes, 8, 4, java.nio.charset.StandardCharsets.US_ASCII);
        return java.util.Set.of("isom", "iso2", "iso4", "iso5", "iso6",
                "mp41", "mp42", "avc1", "M4V ", "dash", "MSNV").contains(brand);
    }

    private static boolean isWebm(byte[] bytes) {
        if (bytes.length < 16 || (bytes[0] & 0xff) != 0x1a
                || (bytes[1] & 0xff) != 0x45 || (bytes[2] & 0xff) != 0xdf
                || (bytes[3] & 0xff) != 0xa3) {
            return false;
        }
        // EBML DocType element 42 82 with an encoded 4-byte value "webm".
        for (int i = 4; i + 6 < Math.min(bytes.length, 4096); i++) {
            if ((bytes[i] & 0xff) == 0x42 && (bytes[i + 1] & 0xff) == 0x82
                    && (bytes[i + 2] & 0xff) == 0x84
                    && matches(bytes, i + 3, "webm")) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(byte[] bytes, int start, String ascii) {
        if (start < 0 || start + ascii.length() > bytes.length) {
            return false;
        }
        for (int i = 0; i < ascii.length(); i++) {
            if (bytes[start + i] != (byte) ascii.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    record ValidatedMedia(byte[] bytes, String mimeType, String extension) {
    }
}
