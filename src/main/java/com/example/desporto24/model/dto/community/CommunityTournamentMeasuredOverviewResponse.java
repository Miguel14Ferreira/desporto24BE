package com.example.desporto24.model.dto.community;

import java.util.List;

/** All associated athletics rounds share exactly the same event and discipline. */
public record CommunityTournamentMeasuredOverviewResponse(
        Long tournamentId, String eventType, String eventDiscipline,
        List<CommunityRoundResponse> linkedRounds,
        List<CommunityRoundResponse> availableRounds,
        List<CommunityMeasuredStandingResponse> standings
) {}
