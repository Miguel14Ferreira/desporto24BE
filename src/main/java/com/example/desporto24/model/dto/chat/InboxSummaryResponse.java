package com.example.desporto24.model.dto.chat;

import java.time.Instant;

public record InboxSummaryResponse(
        String kind,
        String id,
        Long latestMessageId,
        String latestText,
        String latestSender,
        Instant latestAt,
        long unreadCount) {}
