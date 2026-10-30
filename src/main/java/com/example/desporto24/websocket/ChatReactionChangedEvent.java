package com.example.desporto24.websocket;
import java.util.Map;
import java.util.Set;

/** Emitted after persisted state change. Includes no other users' identities. */
public record ChatReactionChangedEvent(String kind, String conversationId, Long messageId,
                                       Map<String, Long> counts, String actorUsername,
                                       String emoji, Set<String> recipients) {}
