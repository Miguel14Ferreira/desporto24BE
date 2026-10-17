package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.TournamentMatchStatus;

public record CommunityTournamentTeamMatchResponse(
        Long id, int roundNumber, Long homeTeamId, String homeTeamName,
        Long awayTeamId, String awayTeamName, TournamentMatchStatus status) {
}
