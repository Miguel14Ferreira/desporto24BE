package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;

import java.time.Instant;
import java.util.List;

public record CommunityResponse(
        Long id,
        String name,
        String description,
        String sport,
        CommunityVisibility visibility,
        boolean rankingsEnabled,
        String ownerUsername,
        CommunityRole myRole,
        int memberCount,
        Instant createdAt,
        List<CommunityMemberResponse> members
) {
}
