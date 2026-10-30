package com.example.desporto24.model.dto.community;

public record CommunityRaceStandingResponse(
        int position,
        Long playerId,
        Long profileId,
        String username,
        String fullName,
        String profilePicUrl,
        int races,
        int wins,
        int podiums,
        Integer bestFinish,
        Integer championshipPoints,
        int penaltyPoints
) {}
