package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunityTimelineEntryResponse(
        Long id, Long communityId, String kind, String username, String body, Instant createdAt) {
}
