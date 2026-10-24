package com.example.desporto24.model.dto.auth;

import java.time.Instant;

/** Public API contains no bearer values, token hashes or raw User-Agent. */
public record DeviceSessionResponse(
        String id, String device, Instant createdAt, Instant expiresAt,
        boolean current
) {}
