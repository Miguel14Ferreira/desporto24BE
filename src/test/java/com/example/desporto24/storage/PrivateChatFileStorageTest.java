package com.example.desporto24.storage;

import com.example.desporto24.configuration.MediaStorageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class PrivateChatFileStorageTest {
    @TempDir Path tmp;

    private PrivateChatFileStorage local(String privateRoot) {
        MediaStorageProperties settings = new MediaStorageProperties();
        settings.setProvider("local");
        settings.setLocalRoot(tmp.resolve("public").toString());
        settings.setPrivateRoot(privateRoot);
        return new PrivateChatFileStorage(settings);
    }

    @Test
    void pdfIsStoredOutsidePublicMediaAndCanBeReadOnlyThroughStorageApi() throws Exception {
        var storage = local(tmp.resolve("private").toString());
        var file = new MockMultipartFile("file", "partida.pdf", "application/pdf",
                ("%PDF-1.4\n1 0 obj\n<<>>\nendobj\n%%EOF\n")
                        .getBytes(StandardCharsets.US_ASCII));
        var saved = storage.store(file);
        assertTrue(saved.key().startsWith("private-chat/"));
        assertEquals("application/pdf", saved.mime());
        assertEquals("partida.pdf", saved.name());
        assertArrayEquals(file.getBytes(), storage.read(saved.key()));
        storage.delete(saved.key());
        assertThrows(ResponseStatusException.class, () -> storage.read(saved.key()));
    }

    @Test
    void refusesPrivateDirectoryUnderPublicMediaRoot() {
        var storage = local(tmp.resolve("public").resolve("private").toString());
        var file = new MockMultipartFile("file", "partida.pdf", "application/pdf",
                "%PDF-1.4\ntrailer\n%%EOF".getBytes(StandardCharsets.US_ASCII));
        assertThrows(ResponseStatusException.class, () -> storage.store(file));
    }

    @Test
    void rejectsSpoofedPdfAndUnsupportedFilesBeforeWriting() {
        var storage = local(tmp.resolve("private").toString());
        var fakePdf = new MockMultipartFile("file", "fake.pdf", "application/pdf",
                "not a pdf".getBytes(StandardCharsets.UTF_8));
        var script = new MockMultipartFile("file", "malware.html", "text/html",
                "<script>alert(1)</script>".getBytes(StandardCharsets.UTF_8));
        assertThrows(ResponseStatusException.class, () -> storage.store(fakePdf));
        assertThrows(ResponseStatusException.class, () -> storage.store(script));
    }

    @Test
    void failsClosedWhenProviderIsS3ButOnlyPublicBucketExists() {
        MediaStorageProperties settings = new MediaStorageProperties();
        settings.setProvider("s3");
        settings.getS3().setBucket("public-feed");
        settings.setPrivateBucket("public-feed");
        var storage = new PrivateChatFileStorage(settings);
        var file = new MockMultipartFile("file", "jogo.pdf", "application/pdf",
                "%PDF-1.4\ntrailer\n%%EOF".getBytes(StandardCharsets.US_ASCII));
        var ex = assertThrows(ResponseStatusException.class, () -> storage.store(file));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatusCode());
    }
}
