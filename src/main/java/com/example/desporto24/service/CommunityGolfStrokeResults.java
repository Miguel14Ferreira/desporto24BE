package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Individual *gross* stroke play per completed nine or eighteen hole round.
 * Values are organizer-confirmed final strokes, including penalty strokes.
 * No card certification, per-hole scores, handicap, Stableford, official
 * tie-breaks or season table are inferred.
 */
final class CommunityGolfStrokeResults {
    private CommunityGolfStrokeResults() {}

    static boolean isGolfStrokeEvent(String type) {
        return "GOLF_STROKE_9".equals(type) || "GOLF_STROKE_18".equals(type);
    }

    static void validate(String sport, String type, List<CommunityRoundEntryRequest> entries) {
        if (!"Golfe".equals(sport) || !isGolfStrokeEvent(type) || entries.isEmpty())
            throw new IllegalArgumentException("Formato de Golfe inválido");
        int holes = "GOLF_STROKE_9".equals(type) ? 9 : 18;
        for (CommunityRoundEntryRequest e : entries) {
            BigDecimal strokes = e.performanceValue();
            if (strokes == null
                    || strokes.compareTo(BigDecimal.valueOf(holes)) < 0
                    || strokes.compareTo(new BigDecimal("999999")) > 0
                    || strokes.stripTrailingZeros().scale() > 0) {
                throw new IllegalArgumentException(
                        "Golfe stroke play exige um total inteiro confirmado de pancadas por jogador");
            }
            if (e.result() != CommunityMatchResult.DRAW
                    || e.team() != null && !e.team().isBlank()
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.waveScore1() != null || e.waveScore2() != null) {
                throw new IllegalArgumentException(
                        "Golfe stroke play não admite equipas, W/D/L nem métricas de outros desportos");
            }
        }
    }
}
