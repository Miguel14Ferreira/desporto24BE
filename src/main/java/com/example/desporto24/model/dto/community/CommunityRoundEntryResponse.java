package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityMatchResult;
import java.math.BigDecimal;

public record CommunityRoundEntryResponse(
        Long profileId,
        String username,
        String fullName,
        String profilePicUrl,
        String team,
        CommunityMatchResult result,
        int goals,
        int assists,
        boolean mvp,
        boolean cleanSheet,
        Integer finishPosition,
        BigDecimal elapsedSeconds,
        BigDecimal bestLapSeconds,
        Integer lapsCompleted,
        Long entryId,
        Long communityPlayerId,
        int racePenaltyPoints,
        String racePenaltyReason
) {
    public CommunityRoundEntryResponse(Long profileId, String username,
            String fullName, String profilePicUrl, String team,
            CommunityMatchResult result, int goals, int assists,
            boolean mvp, boolean cleanSheet,
            Integer finishPosition, BigDecimal elapsedSeconds,
            BigDecimal bestLapSeconds, Integer lapsCompleted) {
        this(profileId, username, fullName, profilePicUrl, team,
                result, goals, assists, mvp, cleanSheet,
                finishPosition, elapsedSeconds, bestLapSeconds, lapsCompleted,
                null, null, 0, null);
    }
    public CommunityRoundEntryResponse(Long profileId, String username,
            String fullName, String profilePicUrl, String team,
            CommunityMatchResult result, int goals, int assists,
            boolean mvp, boolean cleanSheet) {
        this(profileId, username, fullName, profilePicUrl, team,
                result, goals, assists, mvp, cleanSheet,
                null, null, null, null);
    }
}
