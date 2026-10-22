package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityTournamentTeamGroupResponse(
        int groupNumber, List<CommunityTournamentTeamStandingRowResponse> standings) {
}
