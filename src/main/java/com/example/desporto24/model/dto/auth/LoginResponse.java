package com.example.desporto24.model.dto.auth;

import com.example.desporto24.model.dto.ProfileDTO;
import java.time.Instant;

public record LoginResponse(
        boolean mfaRequired,
        String challengeId,
        Instant challengeExpiresAt,
        String token,
        ProfileDTO profile
) {}
