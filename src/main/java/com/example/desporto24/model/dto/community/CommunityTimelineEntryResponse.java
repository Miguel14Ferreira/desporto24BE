package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunityTimelineEntryResponse(
        Long id, Long communityId, String kind, String username, String body, Instant createdAt,
        Long replyToMessageId, String replyToSender, String replyToText) {
    public CommunityTimelineEntryResponse(Long id, Long communityId, String kind,
                                          String username, String body, Instant createdAt) {
        this(id, communityId, kind, username, body, createdAt, null, null, null);
    }
}
