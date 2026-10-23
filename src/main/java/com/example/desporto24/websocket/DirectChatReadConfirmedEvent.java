package com.example.desporto24.websocket;

/**
 * Emitted only after a server-authorized direct-chat read cursor is persisted.
 * A non-durable delivery hint, not a new read state or an acknowledgement
 * from the recipient device.
 */
public record DirectChatReadConfirmedEvent(
        String readerUsername, String senderUsername, long lastReadMessageId) {}
