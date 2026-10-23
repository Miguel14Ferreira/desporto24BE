package com.example.desporto24.model.dto.chat;

import java.time.Instant;

/** A restricted result from a single authorized chat conversation. */
public record ChatSearchHit(
        Long id, String sender, String text, Instant createdAt,
        Long replyToMessageId, String replyToSender, String replyToText) {
}
