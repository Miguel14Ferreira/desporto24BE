package com.example.desporto24.model.dto.session;

import java.time.Instant;

public record SessionMessageResponse(
        Long id,
        Long sessionId,
        String senderUsername,
        String body,
        Instant createdAt
) {
}
