package com.example.desporto24.model.dto.community;

public record CommunityTournamentTeamStandingRowResponse(
        int position,
        Long teamId,
        String teamName,
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
