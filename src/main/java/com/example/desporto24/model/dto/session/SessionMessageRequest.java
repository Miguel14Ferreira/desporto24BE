package com.example.desporto24.model.dto.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SessionMessageRequest(
        @NotBlank
        @Size(max = 2000)
        String body
) {
}
