package com.example.desporto24.encryption;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AesEnvironmentKeyTest {

    @Test
    void resolvesAesBaseKeyFromEnvironment() {
        assertEquals("abc123", AES.resolveBaseKey(Map.of("AES_ENCRYPTION_KEY", "abc123")));
    }

    @Test
    void rejectsMissingAesEnvironmentKey() {
        IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> AES.resolveBaseKey(Map.of())
        );
        assertEquals("AES_ENCRYPTION_KEY must be configured", error.getMessage());
    }
}
