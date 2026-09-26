package com.example.desporto24.storage;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

final class MediaStorageSupport {

    static final long MAX_BYTES = 5L * 1024L * 1024L;
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

    record ValidatedMedia(byte[] bytes, String mimeType, String extension) {
    }
}
