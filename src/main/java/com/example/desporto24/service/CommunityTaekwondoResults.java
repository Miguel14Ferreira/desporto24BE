package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

/**
 * World Taekwondo-style individual *completed* event summaries, never live
 * adjudication. Kyorugi records referee-confirmed round wins (2:0 or 2:1),
 * not an unsafe sum of technique scores from different rounds.
 * Poomsae records organizer-confirmed FINAL judge marks (0..10), without
 * inventing judges' raw cards, accuracy/presentation splits or tie-breaks.
 *
 * Exceptional kyorugi outcomes (RSC, DSQ, WDR, match interruptions etc.),
 * poomsae pairs/teams and championships need their own distinct contracts.
 */
final class CommunityTaekwondoResults {
    private CommunityTaekwondoResults() {}

    static boolean isKyorugi(String type) {
        return "TKD_KYORUGI_BEST3".equals(type);
    }

    static boolean isPoomsae(String type) {
        return "TKD_POOMSAE_RECOGNIZED".equals(type)
                || "TKD_POOMSAE_FREESTYLE".equals(type);
    }

    static boolean isTaekwondoEvent(String type) {
        return isKyorugi(type) || isPoomsae(type);
    }

    static void validate(String sport, String type, List<CommunityRoundEntryRequest> entries) {
        if (!"Taekwondo".equals(sport) || !isTaekwondoEvent(type)
                || entries == null || entries.isEmpty())
            throw new IllegalArgumentException("Formato de Taekwondo inválido");
        if (isKyorugi(type)) {
            if (entries.size() != 2)
                throw new IllegalArgumentException("Kyorugi exige dois adversários: Chung e Hong");
            CommunityRoundEntryRequest blue = entries.stream()
                    .filter(e -> "CHUNG".equals(e.team())).findFirst().orElse(null);
            CommunityRoundEntryRequest red = entries.stream()
                    .filter(e -> "HONG".equals(e.team())).findFirst().orElse(null);
            if (blue == null || red == null || blue == red
                    || !(blue.result() == CommunityMatchResult.WIN
                            && red.result() == CommunityMatchResult.LOSS
                        || blue.result() == CommunityMatchResult.LOSS
                            && red.result() == CommunityMatchResult.WIN))
                throw new IllegalArgumentException(
                        "Kyorugi exige um atleta Chung, um Hong e um vencedor");
        } else if (entries.stream().map(CommunityRoundEntryRequest::username)
                .distinct().count() != entries.size()) {
            throw new IllegalArgumentException("Poomsae não admite atletas duplicados na prova");
        }

        for (CommunityRoundEntryRequest entry : entries) {
            if (entry.goals() != null && entry.goals() != 0
                    || entry.assists() != null && entry.assists() != 0
                    || entry.mvp() || entry.cleanSheet()
                    || entry.finishPosition() != null || entry.elapsedSeconds() != null
                    || entry.bestLapSeconds() != null || entry.lapsCompleted() != null
                    || entry.waveScore1() != null || entry.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Taekwondo não admite métricas de futebol, corrida ou ondas");
            BigDecimal value = entry.performanceValue();
            if (isKyorugi(type)) {
                if (value == null || value.stripTrailingZeros().scale() > 0
                        || value.compareTo(BigDecimal.ZERO) < 0
                        || value.compareTo(BigDecimal.valueOf(2)) > 0
                        || entry.result() == CommunityMatchResult.WIN
                            && value.compareTo(BigDecimal.valueOf(2)) != 0
                        || entry.result() == CommunityMatchResult.LOSS
                            && value.compareTo(BigDecimal.valueOf(2)) >= 0)
                    throw new IllegalArgumentException(
                            "Kyorugi concluído exige 2 rondas ganhas pelo vencedor e 0 ou 1 pelo derrotado");
            } else {
                if (entry.result() != CommunityMatchResult.DRAW
                        || entry.team() != null && !entry.team().isBlank()
                        || value == null || value.scale() > 3
                        || value.compareTo(BigDecimal.ZERO) < 0
                        || value.compareTo(BigDecimal.TEN) > 0)
                    throw new IllegalArgumentException(
                            "Poomsae individual exige nota final confirmada de 0 a 10, até 3 decimais");
            }
        }
    }
}
