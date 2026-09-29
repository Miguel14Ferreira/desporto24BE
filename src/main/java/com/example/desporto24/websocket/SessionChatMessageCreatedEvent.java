package com.example.desporto24.websocket;

import com.example.desporto24.model.dto.session.SessionMessageResponse;

import java.util.Set;

public record SessionChatMessageCreatedEvent(
        SessionMessageResponse message,
        Set<String> recipients
) {
}
