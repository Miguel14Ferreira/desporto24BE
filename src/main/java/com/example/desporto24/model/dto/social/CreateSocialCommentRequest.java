package com.example.desporto24.model.dto.social;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSocialCommentRequest(
        @NotBlank
        @Size(max = 500)
        String content
) {
}
