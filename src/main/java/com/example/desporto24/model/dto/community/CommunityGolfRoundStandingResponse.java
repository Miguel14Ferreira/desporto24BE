package com.example.desporto24.model.dto.community;

/** Provisional gross stroke-play result; tied totals share a position. */
public record CommunityGolfRoundStandingResponse(
        int position, long communityPlayerId, long grossStrokes
) {}
