package com.example.desporto24.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MfaSetupConfirmRequest(
        @NotBlank
        @Pattern(regexp = "\\d{6}", message = "O código deve conter 6 dígitos")
        String code
) {
}
