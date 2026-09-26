package com.example.desporto24.security;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RuntimeSecretsTest {

    @Test
    void readsRequiredSecretFromEnvironmentMap() {
        assertEquals("secret-value", RuntimeSecrets.require(Map.of("SECRET_NAME", "secret-value"), "SECRET_NAME"));
    }

    @Test
    void rejectsMissingRequiredSecret() {
        IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> RuntimeSecrets.require(Map.of(), "SECRET_NAME")
        );
        assertEquals("SECRET_NAME must be configured", error.getMessage());
    }
}
