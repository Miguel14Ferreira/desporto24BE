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

    @Test
    void neverAcceptsVideoThroughImageOnlyStorage() {
        var fakeVideo = new MockMultipartFile("photo", "example.mp4", "video/mp4",
                new byte[] {0,0,0,16,'f','t','y','p','i','s','o','m',0,0,0,0});
        assertThrows(IllegalArgumentException.class,
                () -> storage().store("profiles/2/photo", fakeVideo));
    }

    @Test
    void rejectsFakeAndOversizedVideos() {
        assertThrows(IllegalArgumentException.class, () -> storage().storeVideo("posts/1/video",
                new MockMultipartFile("video", "fake.mp4", "video/mp4",
                        "not actually a video".getBytes())));
        assertThrows(IllegalArgumentException.class, () -> storage().storeVideo("posts/1/video",
                new MockMultipartFile("video", "large.mp4", "video/mp4",
                        new byte[(20 * 1024 * 1024) + 1])));
    }

    @Test
    void storesMp4VideoWithCorrectExtension() {
        byte[] header = new byte[] {
                0, 0, 0, 24, 'f', 't', 'y', 'p',
                'i', 's', 'o', 'm', 0, 0, 0, 0,
                'i', 's', 'o', 'm', 'm', 'p', '4', '2'
        };
        var file = new MockMultipartFile("video", "clip.bin", "video/mp4", header);
        var uri = storage().storeVideo("posts/1/clip", file);
        assertEquals("/media/posts/1/clip.mp4", uri.toString());
        assertTrue(Files.exists(tempDir.resolve("posts/1/clip.mp4")));
    }

    @Test
    void storesWebmVideoWithCorrectExtension() {
        byte[] header = new byte[] {
                0x1A, 0x45, (byte) 0xDF, (byte) 0xA3, (byte) 0x9F,
                0x42, (byte) 0x86, (byte) 0x81, 0x01,
                0x42, (byte) 0xF7, (byte) 0x81, 0x01,
                0x42, (byte) 0xF2, (byte) 0x81, 0x04,
                0x42, (byte) 0xF3, (byte) 0x81, 0x08,
                0x42, (byte) 0x82, (byte) 0x84, 'w', 'e', 'b', 'm'
        };
        var file = new MockMultipartFile("video", "clip.bin", "video/webm", header);
        var uri = storage().storeVideo("posts/1/clip", file);
        assertEquals("/media/posts/1/clip.webm", uri.toString());
        assertTrue(Files.exists(tempDir.resolve("posts/1/clip.webm")));
    }

    @Test
    void preservesAnimatedGifFramesWhenSavingCommunityAvatar() throws Exception {
        byte[] animatedGif = Base64.getDecoder().decode(
                "R0lGODlhAgACAIEAAP8AAAAAAAAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh+QQAFAAAACwAAAAAAgACAAAIBgABCAQQEAAh+QQBFAABACwAAAAAAgACAIEAgAAAAAAAAAAAAAAIBgABCAQQEAA7");
        var file = new MockMultipartFile("photo", "animation.gif", "image/gif", animatedGif);

        var uri = storage().storeCommunityPhoto("communities/10/avatar", file);

        assertEquals("/media/communities/10/avatar.gif", uri.toString());
        assertArrayEquals(animatedGif, Files.readAllBytes(tempDir.resolve("communities/10/avatar.gif")));
        try (var input = javax.imageio.ImageIO.createImageInputStream(
                tempDir.resolve("communities/10/avatar.gif").toFile())) {
            var readers = javax.imageio.ImageIO.getImageReadersByFormatName("gif");
            var reader = readers.next();
            try {
                reader.setInput(input);
                assertEquals(2, reader.getNumImages(true));
            } finally {
                reader.dispose();
            }
        }
    }

    @Test
    void preventsGifUploadsInUnrelatedImageStorage() {
        byte[] gif = Base64.getDecoder().decode("R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==");
        var file = new MockMultipartFile("photo", "avatar.gif", "image/gif", gif);
        assertThrows(IllegalArgumentException.class,
                () -> storage().store("posts/1/photo", file));
    }

    @Test
    void rejectsSpoofedAndOversizedCommunityGifAvatars() {
        var spoofed = new MockMultipartFile("photo", "not-gif.gif", "image/gif",
                "this is not a GIF".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class,
                () -> storage().storeCommunityPhoto("communities/1/photo", spoofed));
        var big = new MockMultipartFile("photo", "huge.gif", "image/gif",
                new byte[5 * 1024 * 1024 + 1]);
        assertThrows(IllegalArgumentException.class,
                () -> storage().storeCommunityPhoto("communities/1/photo", big));
    }

    private LocalMediaStorage storage() {
        MediaStorageProperties properties = new MediaStorageProperties();
        properties.setLocalRoot(tempDir.toString());
        properties.setPublicBaseUrl("");
        return new LocalMediaStorage(properties);
    }
}
