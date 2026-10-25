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
        String replyToText,
        String attachmentName,
        String attachmentMimeType,
        Long attachmentSize
) {
    public SessionMessageResponse(Long id, Long sessionId, String senderUsername,
                                  String body, Instant createdAt,
                                  Long replyToMessageId, String replyToSender, String replyToText) {
        this(id, sessionId, senderUsername, body, createdAt,
                replyToMessageId, replyToSender, replyToText, null, null, null);
    }

    public SessionMessageResponse(Long id, Long sessionId, String senderUsername,
                                  String body, Instant createdAt) {
        this(id, sessionId, senderUsername, body, createdAt, null, null, null);
    }
}
