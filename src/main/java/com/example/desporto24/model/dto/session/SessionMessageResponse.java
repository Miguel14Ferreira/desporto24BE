package com.example.desporto24.model.dto.session;

import java.time.Instant;

public record SessionMessageResponse(
        Long id,
        Long sessionId,
        String senderUsername,
        String body,
        Instant createdAt,
        Long replyToMessageId,
        String replyToSender,
        String replyToText
) {
    public SessionMessageResponse(Long id, Long sessionId, String senderUsername,
                                  String body, Instant createdAt) {
        this(id, sessionId, senderUsername, body, createdAt, null, null, null);
    }
}
