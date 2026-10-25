package com.example.desporto24.storage;

import com.example.desporto24.configuration.MediaStorageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
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
    void acceptsOpusVoiceNotesAsPrivateWebmAndOgg() throws Exception {
        var storage = local(tmp.resolve("private").toString());
        byte[] webm = new byte[]{
            0x1a, 0x45, (byte) 0xdf, (byte) 0xa3, (byte) 0x9f,
            0x42, (byte) 0x82, (byte) 0x84, 'w', 'e', 'b', 'm',
            (byte) 0x83, (byte) 0x81, 0x02, 'A', '_', 'O', 'P', 'U', 'S',
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        var voice = new MockMultipartFile("file", "voz.webm", "audio/webm", webm);
        var saved = storage.store(voice);
        assertEquals("audio/webm", saved.mime());
        assertTrue(saved.key().endsWith(".webm"));
        assertArrayEquals(webm, storage.read(saved.key()));
        storage.delete(saved.key());

        byte[] ogg = new byte[65];
        System.arraycopy("OggS".getBytes(StandardCharsets.US_ASCII), 0, ogg, 0, 4);
        System.arraycopy("OpusHead".getBytes(StandardCharsets.US_ASCII), 0, ogg, 32, 8);
        var opus = new MockMultipartFile("file", "voz.ogg", "audio/ogg", ogg);
        var savedOgg = storage.store(opus);
        assertEquals("audio/ogg", savedOgg.mime());
        assertArrayEquals(ogg, storage.read(savedOgg.key()));
    }

    @Test
    void rejectsAudioSpoofedAsHtmlOrVideoAndTraversalKeys() {
        var storage = local(tmp.resolve("private").toString());
        var html = new MockMultipartFile("file", "voice.webm", "audio/webm",
                "<script>attack</script>".getBytes(StandardCharsets.UTF_8));
        var video = new MockMultipartFile("file", "voice.webm", "audio/webm",
                new byte[]{0x1a,0x45,(byte)0xdf,(byte)0xa3,0,'w','e','b','m',
                    (byte)0x83,(byte)0x81,1,'A','_','V','P','9',0,0,0,0,0});
        assertThrows(ResponseStatusException.class, () -> storage.store(html));
        assertThrows(ResponseStatusException.class, () -> storage.store(video));
        assertThrows(IllegalArgumentException.class,
                () -> storage.read("../private-chat/44444444-4444-4444-4444-444444444444.webm"));
    }

    private MediaStorageProperties r2Settings() {
        MediaStorageProperties settings = new MediaStorageProperties();
        settings.setProvider("s3");
        settings.setPrivateBucket("jynlo-chat-private");
        settings.getS3().setBucket("jynlo-public-feed");
        settings.getS3().setEndpoint("https://fake-account-id.r2.cloudflarestorage.com");
        settings.getS3().setRegion("auto");
        settings.setPrivateAccessKey("private-test-access-key");
        settings.setPrivateSecretKey("private-test-secret-key");
        // Deliberately different public credentials to prove isolation.
        settings.getS3().setAccessKey("public-test-access-key");
        settings.getS3().setSecretKey("public-test-secret-key");
        return settings;
    }

    @Test
    void doesNotEnablePrivateChatForBucketNameAlone() {
        MediaStorageProperties settings = r2Settings();
        settings.setPrivateSecretKey("");
        var storage = new PrivateChatFileStorage(settings);
        assertFalse(storage.isConfigured());
        var file = new MockMultipartFile("file", "jogo.pdf", "application/pdf",
                "%PDF-1.4\\n%%EOF".getBytes(StandardCharsets.US_ASCII));
        var error = assertThrows(ResponseStatusException.class, () -> storage.store(file));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
    }

    @Test
    void rejectsCopiedPublicFeedAccessKeyWithoutContactingR2() {
        MediaStorageProperties settings = r2Settings();
        settings.setPrivateAccessKey(settings.getS3().getAccessKey());
        var storage = new PrivateChatFileStorage(settings);
        S3Client client = mock(S3Client.class);
        ReflectionTestUtils.setField(storage, "s3Client", client);

        assertFalse(storage.isConfigured());
        verifyNoInteractions(client);
        var file = new MockMultipartFile("file", "jogo.pdf", "application/pdf",
                "%PDF-1.4\\n%%EOF".getBytes(StandardCharsets.US_ASCII));
        var error = assertThrows(ResponseStatusException.class, () -> storage.store(file));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
        verifyNoInteractions(client);
    }

    @Test
    void rejectsCopiedPublicFeedSecretKeyWithoutContactingR2() {
        MediaStorageProperties settings = r2Settings();
        settings.setPrivateSecretKey(settings.getS3().getSecretKey());
        var storage = new PrivateChatFileStorage(settings);
        S3Client client = mock(S3Client.class);
        ReflectionTestUtils.setField(storage, "s3Client", client);

        assertFalse(storage.isConfigured());
        verifyNoInteractions(client);
        var file = new MockMultipartFile("file", "jogo.pdf", "application/pdf",
                "%PDF-1.4\\n%%EOF".getBytes(StandardCharsets.US_ASCII));
        var error = assertThrows(ResponseStatusException.class, () -> storage.store(file));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
        verifyNoInteractions(client);
    }

    @Test
    void refusesBucketSuffixedEndpointBeforeContactingR2() {
        MediaStorageProperties settings = r2Settings();
        // The Cloudflare bucket Settings page displays a URL WITH /bucket.
        // The AWS SDK must be given the account-root API URL instead.
        settings.getS3().setEndpoint(
                "https://fake-account-id.r2.cloudflarestorage.com/jynlo-chat-private");
        var storage = new PrivateChatFileStorage(settings);
        S3Client client = mock(S3Client.class);
        ReflectionTestUtils.setField(storage, "s3Client", client);

        assertFalse(storage.isConfigured());
        verifyNoInteractions(client);
    }

    @Test
    void permitsAccountRootR2EndpointWithTrailingSlash() {
        MediaStorageProperties settings = r2Settings();
        settings.getS3().setEndpoint("https://fake-account-id.r2.cloudflarestorage.com/");
        var storage = new PrivateChatFileStorage(settings);
        S3Client client = mock(S3Client.class);
        ReflectionTestUtils.setField(storage, "s3Client", client);
        when(client.headBucket(any(HeadBucketRequest.class)))
                .thenReturn(HeadBucketResponse.builder().build());

        assertTrue(storage.isConfigured());
        verify(client).headBucket(argThat((HeadBucketRequest req) ->
                "jynlo-chat-private".equals(req.bucket())));
    }

    @Test
    void confirmsBucketAccessWithHeadRequestAndCachesTheResult() {
        var storage = new PrivateChatFileStorage(r2Settings());
        S3Client client = mock(S3Client.class);
        ReflectionTestUtils.setField(storage, "s3Client", client);
        when(client.headBucket(any(HeadBucketRequest.class)))
                .thenReturn(HeadBucketResponse.builder().build());
        assertTrue(storage.isConfigured());
        assertTrue(storage.isConfigured());
        verify(client, times(1)).headBucket(any(HeadBucketRequest.class));
    }

    @Test
    void refusesUploadsWhenPrivateBucketIsMissingOrCredentialsDenied() {
        var storage = new PrivateChatFileStorage(r2Settings());
        S3Client client = mock(S3Client.class);
        ReflectionTestUtils.setField(storage, "s3Client", client);
        when(client.headBucket(any(HeadBucketRequest.class)))
                .thenThrow(S3Exception.builder().statusCode(403).message("Forbidden").build());
        assertFalse(storage.isConfigured());
        assertFalse(storage.isConfigured());
        verify(client, times(1)).headBucket(any(HeadBucketRequest.class));
        var file = new MockMultipartFile("file", "jogo.pdf", "application/pdf",
                "%PDF-1.4\\n%%EOF".getBytes(StandardCharsets.US_ASCII));
        var error = assertThrows(ResponseStatusException.class, () -> storage.store(file));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
        verify(client, never()).putObject(any(software.amazon.awssdk.services.s3.model.PutObjectRequest.class),
                any(software.amazon.awssdk.core.sync.RequestBody.class));
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
