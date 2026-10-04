package com.example.desporto24.model.dto.session;

import java.util.List;

public record SessionTeamSuggestionResponse(
        List<SessionTeamPlayerResponse> teamA,
        List<SessionTeamPlayerResponse> teamB,
        double teamAScore,
        double teamBScore,
        int playersWithStats,
        int totalPlayers
) {
}
