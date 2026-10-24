package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunityTimelineEntryResponse(
        Long id, Long communityId, String kind, String username, String body, Instant createdAt,
        Long replyToMessageId, String replyToSender, String replyToText,
        String imageUrl, String videoUrl) {
    public CommunityTimelineEntryResponse(Long id, Long communityId, String kind,
                                          String username, String body, Instant createdAt,
                                          Long replyToMessageId, String replyToSender, String replyToText) {
        this(id, communityId, kind, username, body, createdAt,
                replyToMessageId, replyToSender, replyToText, null, null);
    }

    public CommunityTimelineEntryResponse(Long id, Long communityId, String kind,
                                          String username, String body, Instant createdAt) {
        this(id, communityId, kind, username, body, createdAt, null, null, null, null, null);
    }
}
