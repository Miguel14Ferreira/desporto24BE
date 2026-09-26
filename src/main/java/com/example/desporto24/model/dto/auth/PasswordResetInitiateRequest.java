package com.example.desporto24.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetInitiateRequest(
        @NotBlank
        @Email
        String email
) {
}
