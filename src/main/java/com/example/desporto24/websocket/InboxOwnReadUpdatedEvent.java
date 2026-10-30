package com.example.desporto24.websocket;

/**
 * A server-committed read cursor for the reader's own other tabs/devices.
 * No message content, recipient list, or client-provided socket command.
 */
public record InboxOwnReadUpdatedEvent(
        String username, String kind, String conversationId, long lastReadMessageId) {}
