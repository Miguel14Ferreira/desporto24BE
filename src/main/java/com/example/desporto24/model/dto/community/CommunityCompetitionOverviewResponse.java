package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityCompetitionOverviewResponse(
        CommunityCompetitionSettingsResponse settings,
        List<CommunityRoundResponse> rounds,
        List<CommunityStandingResponse> standings
) {
}
