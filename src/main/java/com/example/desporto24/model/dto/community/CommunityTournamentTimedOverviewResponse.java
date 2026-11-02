package com.example.desporto24.model.dto.community;

import java.util.List;

/** Every included round shares one exact sport, event type and discipline. */
public record CommunityTournamentTimedOverviewResponse(
        Long tournamentId, String eventType, String eventDiscipline,
        List<CommunityRoundResponse> linkedRounds,
        List<CommunityRoundResponse> availableRounds,
        List<CommunityTimedStandingResponse> standings
) {}
