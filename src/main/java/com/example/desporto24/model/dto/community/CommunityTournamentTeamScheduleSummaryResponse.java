package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.TournamentStatus;

public record CommunityTournamentTeamScheduleSummaryResponse(
        Long tournamentId, TournamentStatus status, int teamCount,
        int roundCount, int matchCount) {
}
