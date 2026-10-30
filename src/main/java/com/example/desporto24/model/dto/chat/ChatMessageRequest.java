package com.example.desporto24.model.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ChatMessageRequest(
        @NotBlank
        @Size(max = 2000)
        String text,
        @Positive Long replyToMessageId
) {
    // Preserve older callers; a normal message has no quoted parent.
    public ChatMessageRequest(String text) {
        this(text, null);
    }
}
