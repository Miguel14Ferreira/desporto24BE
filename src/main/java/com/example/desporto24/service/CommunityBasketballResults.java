package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityBasketballScore;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.util.List;

/** Records only the organizer-confirmed FINAL basketball score (5v5 or 3x3).
 * Does not calculate baskets, FIBA tie-breaks or generic football season points.
 */
final class CommunityBasketballResults {
    private CommunityBasketballResults() {}

    static boolean isBasketballEvent(String type) {
        return "BASKETBALL_FINAL".equals(type) || "BASKETBALL_3X3_FINAL".equals(type);
    }

    static CommunityBasketballScore validate(String sport, String eventType,
            List<CommunityRoundEntryRequest> entries, CommunityBasketballScore score) {
        if (!isBasketballEvent(eventType)) {
            if (score != null)
                throw new IllegalArgumentException(
                        "Pontos de Basquetebol só são permitidos nas provas correspondentes");
            return null;
        }
        if (!("Basquetebol".equals(sport) && "BASKETBALL_FINAL".equals(eventType)
                || "Basquetebol 3x3".equals(sport) && "BASKETBALL_3X3_FINAL".equals(eventType)))
            throw new IllegalArgumentException("O tipo de prova não corresponde à modalidade");
        if (score == null || score.sideA() == null || score.sideB() == null
                || score.sideA() < 0 || score.sideB() < 0
                || score.sideA() > 999 || score.sideB() > 999
                || score.sideA().equals(score.sideB()))
            throw new IllegalArgumentException(
                    "Indica os dois marcadores finais em pontos (0 a 999), sem empate");
        int a = 0, b = 0;
        for (CommunityRoundEntryRequest e : entries) {
            if ("A".equals(e.team())) a++;
            else if ("B".equals(e.team())) b++;
            else throw new IllegalArgumentException("Associa cada participante ao lado A ou B");
            if (e.result() != CommunityMatchResult.DRAW
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null) {
                throw new IllegalArgumentException(
                        "O marcador pertence às equipas; não são permitidas estatísticas individuais de futebol");
            }
        }
        // Registered participants are not asserted to be the official on-court roster.
        if (a < 1 || b < 1)
            throw new IllegalArgumentException("As duas equipas precisam de participantes associados");
        return score;
    }

    static CommunityBasketballScore decode(Integer sideA, Integer sideB) {
        if (sideA == null && sideB == null) return null;
        if (sideA == null || sideB == null)
            throw new IllegalStateException("Marcador de Basquetebol incompleto");
        return new CommunityBasketballScore(sideA, sideB);
    }
}
