package com.example.desporto24.model.dto.social;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSocialPostRequest(
        @NotBlank
        @Size(max = 1000)
        String content
) {
}
