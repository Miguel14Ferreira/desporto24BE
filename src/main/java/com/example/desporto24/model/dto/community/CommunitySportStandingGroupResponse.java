package com.example.desporto24.model.dto.community;

import java.util.List;

/**
 * W/D/L standings for one identified community sport.
 * Default scoring inherits the community's existing points; an explicit
 * season-specific rule overrides it without changing legacy aggregates.
 */
public record CommunitySportStandingGroupResponse(
        String sport,
        List<CommunityStandingResponse> standings,
        CommunitySportPointsRuleResponse scoring
) {
    public CommunitySportStandingGroupResponse(
            String sport, List<CommunityStandingResponse> standings) {
        this(sport, standings, null);
    }
}
