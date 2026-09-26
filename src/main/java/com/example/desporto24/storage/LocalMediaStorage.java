package com.example.desporto24.storage;

import com.example.desporto24.configuration.MediaStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "desporto.media.provider", havingValue = "local", matchIfMissing = true)
public class LocalMediaStorage implements MediaStorage {

    private final MediaStorageProperties properties;

    @Override
    public URI store(String key, MultipartFile file) {
        MediaStorageSupport.ValidatedMedia media = MediaStorageSupport.validate(file);
        String objectKey = key + media.extension();
        Path root = Path.of(properties.getLocalRoot()).toAbsolutePath().normalize();
        Path target = root.resolve(objectKey).normalize();

        if (!target.startsWith(root)) {
            throw new IllegalArgumentException("Invalid media key");
        }

        try {
            Files.createDirectories(target.getParent());
            Files.write(target, media.bytes());
        } catch (IOException e) {
            throw new IllegalStateException("Could not store media file", e);
        }

        String base = properties.getPublicBaseUrl();
        if (base == null || base.isBlank()) {
            return URI.create("/media/" + objectKey);
        }
        return URI.create(stripTrailingSlash(base) + "/" + objectKey);
    }

    @Override
    public void delete(String key) {
        Path root = Path.of(properties.getLocalRoot()).toAbsolutePath().normalize();
        Path target = root.resolve(key).normalize();
        if (!target.startsWith(root)) {
            throw new IllegalArgumentException("Invalid media key");
        }
        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new IllegalStateException("Could not delete media file", e);
        }
    }

    private String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
