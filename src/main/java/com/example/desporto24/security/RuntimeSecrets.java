package com.example.desporto24.security;

import java.util.Map;

public final class RuntimeSecrets {

    private RuntimeSecrets() {
    }

    public static String require(String name) {
        return require(System.getenv(), name);
    }

    static String require(Map<String, String> environment, String name) {
        String value = environment.get(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " must be configured");
        }
        return value;
    }
}
