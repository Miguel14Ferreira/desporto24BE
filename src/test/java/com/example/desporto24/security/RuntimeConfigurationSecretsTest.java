package com.example.desporto24.security;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RuntimeConfigurationSecretsTest {

    private static final List<String> PROFILE_FILES = List.of(
            "application-dev.properties",
            "application-prod.properties",
            "application-test.properties"
    );

    private static final List<String> SENSITIVE_KEYS = List.of(
            "POSTGRES_USERNAME",
            "POSTGRES_PASSWORD",
            "JWT_SECRET"
    );

    @Test
    void profileFilesDoNotContainLiteralRuntimeSecrets() throws IOException {
        for (String profileFile : PROFILE_FILES) {
            Properties properties = load(profileFile);

            for (String key : SENSITIVE_KEYS) {
                String value = properties.getProperty(key);
                assertTrue(
                        value == null || value.isBlank() || value.startsWith("${"),
                        () -> profileFile + " must not contain a literal value for " + key
                );
            }
        }
    }

    private Properties load(String resource) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (input == null) {
                throw new IllegalStateException("Missing resource: " + resource);
            }
            properties.load(input);
        }
        return properties;
    }
}
