package com.example.desporto24.model.dto.chat;

/**
 * The peer's last confirmed visible message ID in this one-to-one chat.
 * Zero means no confirmed read receipt; not an indicator of WebSocket delivery.
 */
public record DirectReadReceiptResponse(String peerUsername, long lastReadMessageId) {}
