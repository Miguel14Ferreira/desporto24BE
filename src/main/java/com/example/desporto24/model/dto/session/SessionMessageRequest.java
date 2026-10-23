package com.example.desporto24.model.dto.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SessionMessageRequest(
        @NotBlank
        @Size(max = 2000)
        String body,
        @Positive Long replyToMessageId
) {
    public SessionMessageRequest(String body) { this(body, null); }
}
