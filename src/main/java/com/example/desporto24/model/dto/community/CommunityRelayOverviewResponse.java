package com.example.desporto24.model.dto.community;
import java.util.List;
public record CommunityRelayOverviewResponse(
        Long tournamentId, String eventDiscipline,
        List<CommunityTournamentTeamResponse> teams,
        List<CommunityRelayHeatResponse> heats,
        List<CommunityRelayStandingResponse> standings,
        boolean finalPublished,
        List<CommunityRelayFinalistResponse> finalists,
        List<CommunityRelayStandingResponse> qualifyingStandings,
        List<CommunityRelayStandingResponse> finalStandings,
        List<CommunityRelayDecisionResponse> decisions
) {
    public CommunityRelayOverviewResponse(Long tournamentId,String eventDiscipline,
            List<CommunityTournamentTeamResponse> teams,
            List<CommunityRelayHeatResponse> heats,List<CommunityRelayStandingResponse> standings) {
        this(tournamentId,eventDiscipline,teams,heats,standings,false,List.of(),standings,List.of(),List.of());
    }
}
