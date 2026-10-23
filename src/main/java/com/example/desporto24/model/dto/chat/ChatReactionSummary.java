package com.example.desporto24.model.dto.chat;
import java.util.Map;

/** Counts only; never publishes identities of other reacting members. */
public record ChatReactionSummary(Long messageId, Map<String, Long> counts, String myEmoji) {}
