package com.example.desporto24.model.dto.community;

import java.util.List;

/**
 * Race results are the already recorded community rounds; linking never
 * duplicates them or modifies the original sporting history.
 */
public record CommunityTournamentRaceOverviewResponse(
        Long tournamentId,
        List<Integer> pointsByPlace,
        List<CommunityRoundResponse> linkedRaces,
        List<CommunityRoundResponse> availableRaces,
        List<CommunityRaceStandingResponse> standings
) {}
