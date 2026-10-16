package com.example.desporto24.model.dto.community;

public record CommunityTournamentStandingRowResponse(
        int position,
        Long profileId,
        String username,
        String displayName,
        int played,
        int won,
        int drawn,
        int lost,
        int goalsFor,
        int goalsAgainst,
        int goalDifference,
        long points
) {
}
