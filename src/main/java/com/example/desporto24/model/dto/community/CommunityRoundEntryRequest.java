package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityMatchResult;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record CommunityRoundEntryRequest(
        @NotBlank String username,
        String team,
        @NotNull CommunityMatchResult result,
        @Min(0) Integer goals,
        @Min(0) Integer assists,
        boolean mvp,
        boolean cleanSheet,
        @Min(1) Integer finishPosition,
        @DecimalMin("0.001") @Digits(integer = 8, fraction = 3) BigDecimal elapsedSeconds,
        @DecimalMin("0.001") @Digits(integer = 8, fraction = 3) BigDecimal bestLapSeconds,
        @Min(0) Integer lapsCompleted,
        @Digits(integer = 6, fraction = 3) BigDecimal performanceValue,
        @Digits(integer = 2, fraction = 3) BigDecimal waveScore1,
        @Digits(integer = 2, fraction = 3) BigDecimal waveScore2
) {
    // Preserve the formerly canonical 12-argument constructor for Java callers.
    public CommunityRoundEntryRequest(String username, String team,
            CommunityMatchResult result, Integer goals, Integer assists,
            boolean mvp, boolean cleanSheet, Integer finishPosition,
            BigDecimal elapsedSeconds, BigDecimal bestLapSeconds,
            Integer lapsCompleted, BigDecimal performanceValue) {
        this(username, team, result, goals, assists, mvp, cleanSheet,
                finishPosition, elapsedSeconds, bestLapSeconds, lapsCompleted,
                performanceValue, null, null);
    }
    // Preserve previous callers and JSON payloads which only know W/D/L.
    public CommunityRoundEntryRequest(String username, String team,
            CommunityMatchResult result, Integer goals, Integer assists,
            boolean mvp, boolean cleanSheet) {
        this(username, team, result, goals, assists, mvp, cleanSheet,
                null, null, null, null, null, null, null);
    }
    public CommunityRoundEntryRequest(String username, String team,
            CommunityMatchResult result, Integer goals, Integer assists,
            boolean mvp, boolean cleanSheet, Integer finishPosition,
            BigDecimal elapsedSeconds, BigDecimal bestLapSeconds,
            Integer lapsCompleted) {
        this(username, team, result, goals, assists, mvp, cleanSheet,
                finishPosition, elapsedSeconds, bestLapSeconds, lapsCompleted,
                null, null, null);
    }
}
