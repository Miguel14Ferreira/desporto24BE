package com.example.desporto24.model.dto.community;

import java.math.BigDecimal;

/** Provisional best mark in metres; not official athletics tie-break rules. */
public record CommunityMeasuredStandingResponse(
        int position, Long communityPlayerId, Long profileId,
        String username, String fullName, BigDecimal bestMetres, int validMarks
) {}
