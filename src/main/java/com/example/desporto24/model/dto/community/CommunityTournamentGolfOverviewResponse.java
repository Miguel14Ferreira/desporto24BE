package com.example.desporto24.model.dto.community;

import java.util.List;

/** Provisional single-round gross stroke-play tournament, without final snapshot. */
public record CommunityTournamentGolfOverviewResponse(
        Long tournamentId, String eventType,
        List<CommunityRoundResponse> linkedRounds,
        List<CommunityRoundResponse> availableRounds,
        List<CommunityGolfRoundStandingResponse> standings
) {}
