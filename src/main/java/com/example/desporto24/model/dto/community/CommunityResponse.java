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
        String photoUrl,
        CommunityVisibility visibility,
        boolean rankingsEnabled,
        String ownerUsername,
        CommunityRole myRole,
        int memberCount,
        Instant createdAt,
        List<CommunityMemberResponse> members,
        List<String> sports
) {
    // Preserve existing direct construction and older consumers.
    public CommunityResponse(Long id, String name, String description, String sport,
                             String photoUrl, CommunityVisibility visibility,
                             boolean rankingsEnabled, String ownerUsername,
                             CommunityRole myRole, int memberCount,
                             Instant createdAt, List<CommunityMemberResponse> members) {
        this(id, name, description, sport, photoUrl, visibility,
                rankingsEnabled, ownerUsername, myRole, memberCount, createdAt,
                members, sport == null ? List.of() : List.of(sport));
    }
}
