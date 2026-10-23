package com.example.desporto24.model.dto.community;

import java.util.List;

/** A ranking only within one discipline and one match format. */
public record CommunityRacketStandingGroupResponse(
        String sport,
        String matchFormat,
        List<CommunityRacketStandingResponse> standings,
        CommunityRacketRuleResponse rule
) {
    public CommunityRacketStandingGroupResponse(String sport, String matchFormat,
            List<CommunityRacketStandingResponse> standings) {
        this(sport, matchFormat, standings, null);
    }
}
