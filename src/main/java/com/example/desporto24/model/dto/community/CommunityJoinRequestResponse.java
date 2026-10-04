package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;

import java.time.Instant;

public record CommunityJoinRequestResponse(
        Long id,
        Long communityId,
        String communityName,
        String requesterUsername,
        CommunityInviteStatus status,
        Instant createdAt
) {
}
