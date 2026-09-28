package com.example.desporto24.model.dto.auth;

public record MfaSetupResponse(
        String secret,
        String qrCodeDataUrl,
        String otpauthUri
) {
}
