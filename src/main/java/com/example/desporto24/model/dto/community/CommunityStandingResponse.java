package com.example.desporto24.model.dto.community;

public record CommunityStandingResponse(
        int position,
        Long profileId,
        String username,
        String fullName,
        String profilePicUrl,
        int games,
        int wins,
        int draws,
        int losses,
        int points,
        int goals,
        int assists,
        int mvps,
        int cleanSheets,
        double winRate,
        String currentStreak,
        int bestWinStreak
) {
}
