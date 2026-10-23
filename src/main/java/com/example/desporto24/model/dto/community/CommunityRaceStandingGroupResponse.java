package com.example.desporto24.model.dto.community;

import java.util.List;

public record CommunityRaceStandingGroupResponse(
        String sport,
        CommunityRaceRuleResponse rule,
        List<CommunityRaceStandingResponse> standings
) {}
