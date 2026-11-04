package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityCompetitionOverviewResponse(
        CommunityCompetitionSettingsResponse settings,
        List<CommunitySeasonResponse> seasons,
        Long selectedSeasonId,
        Long activeSeasonId,
        List<CommunityRoundResponse> rounds,
        List<CommunityStandingResponse> standings,
        List<CommunityRacketStandingGroupResponse> racketStandings,
        List<CommunityRaceStandingGroupResponse> raceStandings,
        List<CommunitySportStandingGroupResponse> sportStandings
) {
    /** Keep existing callers and API contracts compatible. */
    public CommunityCompetitionOverviewResponse(
            CommunityCompetitionSettingsResponse settings,
            List<CommunitySeasonResponse> seasons,
            Long selectedSeasonId,
            Long activeSeasonId,
            List<CommunityRoundResponse> rounds,
            List<CommunityStandingResponse> standings,
            List<CommunityRacketStandingGroupResponse> racketStandings,
            List<CommunityRaceStandingGroupResponse> raceStandings) {
        this(settings, seasons, selectedSeasonId, activeSeasonId,
                rounds, standings, racketStandings, raceStandings, List.of());
    }

    public CommunityCompetitionOverviewResponse(
            CommunityCompetitionSettingsResponse settings,
            List<CommunitySeasonResponse> seasons,
            Long selectedSeasonId,
            Long activeSeasonId,
            List<CommunityRoundResponse> rounds,
            List<CommunityStandingResponse> standings,
            List<CommunityRacketStandingGroupResponse> racketStandings) {
        this(settings, seasons, selectedSeasonId, activeSeasonId,
                rounds, standings, racketStandings, List.of());
    }
    public CommunityCompetitionOverviewResponse(
            CommunityCompetitionSettingsResponse settings,
            List<CommunitySeasonResponse> seasons,
            Long selectedSeasonId,
            Long activeSeasonId,
            List<CommunityRoundResponse> rounds,
            List<CommunityStandingResponse> standings) {
        this(settings, seasons, selectedSeasonId, activeSeasonId,
                rounds, standings, List.of());
    }
}
