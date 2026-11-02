package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityRelayOverviewResponse(
        Long tournamentId, String eventDiscipline,
        List<CommunityTournamentTeamResponse> teams,
        List<CommunityRelayHeatResponse> heats,
        List<CommunityRelayStandingResponse> standings
) {}
