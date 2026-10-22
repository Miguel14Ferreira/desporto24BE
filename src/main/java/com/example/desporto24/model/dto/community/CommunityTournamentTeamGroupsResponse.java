package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityTournamentTeamGroupsResponse(
        Long tournamentId, boolean knockoutStarted,
        List<CommunityTournamentTeamGroupResponse> groups) {
}
