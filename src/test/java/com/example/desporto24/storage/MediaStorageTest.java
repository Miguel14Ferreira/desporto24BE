package com.example.desporto24.storage;

import com.example.desporto24.configuration.MediaStorageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class MediaStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void rejectsEmptyFile() {
        LocalMediaStorage storage = storage();

        var file = new MockMultipartFile(
                "photo", "empty.png", "image/png", new byte[0]);

        assertThrows(IllegalArgumentException.class,
                () -> storage.store("sessions/1/test", file));
    }

    @Test
    void rejectsFilesLargerThanFiveMegabytes() {
        LocalMediaStorage storage = storage();
        byte[] bytes = new byte[(5 * 1024 * 1024) + 1];

        var file = new MockMultipartFile(
                "photo", "large.png", "image/png", bytes);

        assertThrows(IllegalArgumentException.class,
                () -> storage.store("sessions/1/test", file));
    }

    @Test
    void rejectsTextPretendingToBePng() {
        LocalMediaStorage storage = storage();

        var file = new MockMultipartFile(
                "photo",
                "fake.png",
                "image/png",
                "this is plain text".getBytes()
        );

        assertThrows(IllegalArgumentException.class,
                () -> storage.store("sessions/1/test", file));
    }

    @Test
    void storesValidPngUsingDetectedExtension() {
        LocalMediaStorage storage = storage();
        byte[] png = Base64.getDecoder().decode(
                "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
        );
        var file = new MockMultipartFile(
                "photo", "anything.bin", "image/png", png);

        var uri = storage.store("sessions/42/photo", file);

        assertEquals("/media/sessions/42/photo.png", uri.toString());
        assertTrue(Files.exists(tempDir.resolve("sessions/42/photo.png")));
    }

    private LocalMediaStorage storage() {
        MediaStorageProperties properties = new MediaStorageProperties();
        properties.setLocalRoot(tempDir.toString());
        properties.setPublicBaseUrl("");
        return new LocalMediaStorage(properties);
    }
}
