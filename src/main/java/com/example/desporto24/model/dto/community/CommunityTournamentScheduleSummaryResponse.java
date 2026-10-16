package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.TournamentStatus;

public record CommunityTournamentScheduleSummaryResponse(
        Long tournamentId,
        TournamentStatus status,
        int participantCount,
        int roundCount,
        int matchCount
) {
}
