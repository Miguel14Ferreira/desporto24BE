package com.example.desporto24.model.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record MfaResendRequest(
        @NotBlank
        String challengeId
) {
}
