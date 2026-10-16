package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.TournamentMatchStatus;

public record CommunityTournamentMatchResponse(
        Long id,
        int roundNumber,
        Long homeProfileId,
        String homeUsername,
        Long awayProfileId,
        String awayUsername,
        TournamentMatchStatus status,
        Integer homeScore,
        Integer awayScore
) {
}
