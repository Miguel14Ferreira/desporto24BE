package com.example.desporto24.model.dto.social;

import java.time.Instant;

public record StoryResponse(
        Long id, String authorUsername, String displayName, String avatarUrl,
        Long communityId, String communityName, String communityPhotoUrl,
        String caption, String mediaUrl, boolean video,
        Instant createdAt, Instant expiresAt, boolean canDelete) {
}
