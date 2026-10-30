package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.util.List;

/**
 * Organizer-recorded final outcomes for individual IFMA Full Contact Muay Thai.
 *
 * Official May 2026 IFMA Rules & Regulations, Rule 30: WP, KO,
 * RSC Safety (RSCS), RSC Injury (RSCI) are distinct decisions.
 *
 * The application records ALREADY CONFIRMED decisions only, never
 * computes judge scores, medical findings, knockout causes, penalty
 * points, or standings. Technical/semi-contact, Wai Kru and Mai Muay
 * are intentionally outside this contract.
 */
final class CommunityMuayThaiResults {
    private CommunityMuayThaiResults() {}

    static boolean isMuayThaiEvent(String type) {
        return "IFMA_FULL_WP".equals(type)
                || "IFMA_FULL_KO".equals(type)
                || "IFMA_FULL_RSCS".equals(type)
                || "IFMA_FULL_RSCI".equals(type);
    }

    static void validate(String sport, String type, List<CommunityRoundEntryRequest> entries) {
        if (!"Muay Thai".equals(sport) || !isMuayThaiEvent(type)
                || entries == null || entries.size() != 2)
            throw new IllegalArgumentException(
                    "Muay Thai IFMA Full Contact exige exatamente dois atletas e um resultado válido");
        CommunityRoundEntryRequest red = entries.stream()
                .filter(e -> "RED".equals(e.team())).findFirst().orElse(null);
        CommunityRoundEntryRequest blue = entries.stream()
                .filter(e -> "BLUE".equals(e.team())).findFirst().orElse(null);
        if (red == null || blue == null
                || red.username() == null || blue.username() == null
                || red.username().isBlank() || blue.username().isBlank()
                || red.username().trim().equalsIgnoreCase(blue.username().trim())
                || !((red.result() == CommunityMatchResult.WIN
                      && blue.result() == CommunityMatchResult.LOSS)
                    || (red.result() == CommunityMatchResult.LOSS
                      && blue.result() == CommunityMatchResult.WIN)))
            throw new IllegalArgumentException(
                    "Muay Thai exige cantos vermelho e azul, dois atletas distintos e um vencedor confirmado");
        for (CommunityRoundEntryRequest e : entries) {
            if (e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null
                    || e.elapsedSeconds() != null || e.bestLapSeconds() != null
                    || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Muay Thai regista apenas decisões oficiais confirmadas, sem totais inventados ou métricas de outros desportos");
        }
    }
}
