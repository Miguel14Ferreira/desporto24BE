package com.example.desporto24.model.dto.chat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record InboxRequest(
        @NotNull @Size(max = 150) List<@NotNull @Valid Conversation conversation> conversations) {
    public record Conversation(
            @NotNull String kind,
            @NotNull String id) {}
}
