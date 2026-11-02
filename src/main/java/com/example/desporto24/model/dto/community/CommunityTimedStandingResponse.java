package com.example.desporto24.model.dto.community;

import java.math.BigDecimal;

/** Provisional ranking by best recorded time, not federation points. */
public record CommunityTimedStandingResponse(
        int position, Long communityPlayerId, Long profileId,
        String username, String fullName, BigDecimal bestSeconds, int validAttempts
) {}
