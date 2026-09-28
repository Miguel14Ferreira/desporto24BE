package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityMatchResult;

public record CommunityRoundEntryResponse(
        Long profileId,
        String username,
        String fullName,
        String profilePicUrl,
        String team,
        CommunityMatchResult result,
        int goals,
        int assists,
        boolean mvp,
        boolean cleanSheet
) {
}
