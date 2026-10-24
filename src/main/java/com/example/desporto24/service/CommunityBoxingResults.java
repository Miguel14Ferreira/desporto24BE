package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.util.List;

/**
 * Individual World Boxing-style contest decision recorded AFTER official confirmation.
 * WP means win on points decided by judges, NOT automatically calculated from
 * a summed score. KO and RSC are separate official ways to win.
 * No invented round cards, judge counts, disqualification, injury detail,
 * abandonment, stoppage times, official rankings or season points.
 */
final class CommunityBoxingResults {
    private CommunityBoxingResults() {}

    static boolean isBoxingEvent(String type) {
        return "BOXING_WB_POINTS".equals(type)
                || "BOXING_WB_KO".equals(type)
                || "BOXING_WB_RSC".equals(type);
    }

    static void validate(String sport, String type, List<CommunityRoundEntryRequest> entries) {
        if (!"Boxe".equals(sport) || !isBoxingEvent(type)
                || entries == null || entries.size() != 2) {
            throw new IllegalArgumentException("Boxe exige exatamente dois atletas e um resultado válido");
        }
        CommunityRoundEntryRequest red = entries.stream()
                .filter(e -> "RED".equals(e.team())).findFirst().orElse(null);
        CommunityRoundEntryRequest blue = entries.stream()
                .filter(e -> "BLUE".equals(e.team())).findFirst().orElse(null);
        if (red == null || blue == null
                || red.username() == null || blue.username() == null
                || red.username().trim().equalsIgnoreCase(blue.username().trim())
                || !((red.result() == CommunityMatchResult.WIN
                      && blue.result() == CommunityMatchResult.LOSS)
                    || (red.result() == CommunityMatchResult.LOSS
                      && blue.result() == CommunityMatchResult.WIN))) {
            throw new IllegalArgumentException(
                    "Boxe exige vermelho e azul, atletas diferentes, um vencedor e um derrotado");
        }
        for (CommunityRoundEntryRequest e : entries) {
            if (e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null) {
                throw new IllegalArgumentException(
                        "Boxe regista apenas a decisão oficial, sem somas de pontos nem métricas de outros desportos");
            }
        }
    }
}
