package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunityInviteResponse(
        Long id,
        Long communityId,
        String communityName,
        String invitedByUsername,
        Instant createdAt
) {
}
