package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Organizer-confirmed IBJJF-style *Gi* individual match summaries.
 *
 * Submission is a confirmed victory, without inferred technique or score.
 * POINTS only represents a clear victory on the FINAL point totals.
 * Ties requiring advantages, penalties, referee decision, disqualification,
 * stoppage or injury need distinct schemas; no fabricated tie-breaks.
 * Neither result participates in generic season W/D/L calculations.
 */
final class CommunityBjjResults {
    private CommunityBjjResults() {}

    static boolean isBjjEvent(String type) {
        return "BJJ_IBJJF_GI_SUBMISSION".equals(type)
                || "BJJ_IBJJF_GI_POINTS".equals(type);
    }

    static boolean isPointsEvent(String type) {
        return "BJJ_IBJJF_GI_POINTS".equals(type);
    }

    static void validate(String sport, String type, List<CommunityRoundEntryRequest> entries) {
        if (!"Jiu-Jitsu Brasileiro".equals(sport) || !isBjjEvent(type)
                || entries == null || entries.size() != 2) {
            throw new IllegalArgumentException("Jiu-Jitsu Brasileiro exige dois atletas e um formato válido");
        }
        CommunityRoundEntryRequest a = entries.stream()
                .filter(e -> "A".equals(e.team())).findFirst().orElse(null);
        CommunityRoundEntryRequest b = entries.stream()
                .filter(e -> "B".equals(e.team())).findFirst().orElse(null);
        if (a == null || b == null || a.username().equalsIgnoreCase(b.username())
                || !((a.result() == CommunityMatchResult.WIN
                      && b.result() == CommunityMatchResult.LOSS)
                    || (a.result() == CommunityMatchResult.LOSS
                      && b.result() == CommunityMatchResult.WIN))) {
            throw new IllegalArgumentException(
                    "Seleciona um atleta A e outro B, com uma vitória e uma derrota confirmadas");
        }

        for (CommunityRoundEntryRequest e : entries) {
            if (e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.waveScore1() != null || e.waveScore2() != null) {
                throw new IllegalArgumentException(
                        "Jiu-Jitsu Brasileiro não admite métricas de outros desportos");
            }
            BigDecimal score = e.performanceValue();
            if (isPointsEvent(type)) {
                if (score == null || score.signum() < 0
                        || score.compareTo(new BigDecimal("999999")) > 0
                        || score.stripTrailingZeros().scale() > 0
                        || score.compareTo(BigDecimal.ONE) == 0) {
                    throw new IllegalArgumentException(
                            "Regista apenas os pontos inteiros finais confirmados (0 ou pelo menos 2)");
                }
            } else if (score != null) {
                throw new IllegalArgumentException(
                        "Uma finalização não exige uma soma de pontos inventada");
            }
        }

        if (isPointsEvent(type)) {
            int cmp = a.performanceValue().compareTo(b.performanceValue());
            if (cmp == 0
                    || cmp > 0 && a.result() != CommunityMatchResult.WIN
                    || cmp < 0 && b.result() != CommunityMatchResult.WIN)
                throw new IllegalArgumentException(
                        "Vitória por pontos exige maior pontuação final; empates requerem vantagens/penalizações ou decisão arbitral");
        }
    }
}
