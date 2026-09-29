package com.example.desporto24.model.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record GoogleCredentialRequest(
        @NotBlank String credential
) {
}
