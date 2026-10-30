package com.example.desporto24.model.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InboxReadRequest(
        @NotBlank String kind,
        @NotBlank String id,
        @NotNull @Positive Long lastSeenMessageId) {}
