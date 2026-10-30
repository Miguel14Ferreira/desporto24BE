package com.example.desporto24.model.dto.chat;

import jakarta.validation.constraints.NotBlank;
public record ChatReactionRequest(@NotBlank String emoji) {}
