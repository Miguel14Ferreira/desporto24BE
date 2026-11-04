package com.example.desporto24.model.dto.community;

import java.util.List;

/**
 * W/D/L standings for one explicitly selected community sport.
 * In this initial contract scoring still uses the community's shared
 * win/draw/loss settings; do not mistake it for a per-sport ruleset.
 */
public record CommunitySportStandingGroupResponse(
        String sport,
        List<CommunityStandingResponse> standings
) {
}
