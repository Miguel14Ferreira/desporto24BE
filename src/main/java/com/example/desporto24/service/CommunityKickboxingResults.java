package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * WAKO individual kickboxing: organizer-confirmed *completed* bout only.
 * PF may store two final whole-number scoreboard totals; a strict higher
 * total wins. Other five disciplines record an adjudicated WIN/LOSS without
 * misrepresenting independent judges' cards as one arithmetic aggregate.
 * Does not infer penalties, wins by stoppage, extra rounds, injury,
 * disqualification, official judges' voting or season championship points.
 */
final class CommunityKickboxingResults {
    private static final Set<String> EVENTS = Set.of(
            "WAKO_POINT_FIGHTING", "WAKO_LIGHT_CONTACT", "WAKO_KICK_LIGHT",
            "WAKO_FULL_CONTACT", "WAKO_LOW_KICK", "WAKO_K1");

    private CommunityKickboxingResults() {}

    static boolean isKickboxingEvent(String type) { return type != null && EVENTS.contains(type); }

    static boolean isPointFighting(String type) { return "WAKO_POINT_FIGHTING".equals(type); }

    static void validate(String sport, String type, List<CommunityRoundEntryRequest> entries) {
        if (!"Kickboxing".equals(sport) || !isKickboxingEvent(type)
                || entries == null || entries.size() != 2)
            throw new IllegalArgumentException("Kickboxing exige dois atletas e uma disciplina WAKO válida");
        CommunityRoundEntryRequest red = entries.stream()
                .filter(e -> "RED".equals(e.team())).findFirst().orElse(null);
        CommunityRoundEntryRequest blue = entries.stream()
                .filter(e -> "BLUE".equals(e.team())).findFirst().orElse(null);
        if (red == null || blue == null || red.username() == null || blue.username() == null
                || red.username().isBlank() || blue.username().isBlank()
                || red.username().trim().equalsIgnoreCase(blue.username().trim())
                || !((red.result() == CommunityMatchResult.WIN
                      && blue.result() == CommunityMatchResult.LOSS)
                    || (red.result() == CommunityMatchResult.LOSS
                      && blue.result() == CommunityMatchResult.WIN)))
            throw new IllegalArgumentException(
                    "Kickboxing exige vermelho e azul, dois atletas diferentes, um vencedor e um derrotado");

        for (CommunityRoundEntryRequest e : entries) {
            if (e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException("Kickboxing não aceita estatísticas de outros desportos");
            BigDecimal points = e.performanceValue();
            if (isPointFighting(type)) {
                if (points == null || points.signum() < 0
                        || points.compareTo(new BigDecimal("999999")) > 0
                        || points.stripTrailingZeros().scale() > 0)
                    throw new IllegalArgumentException("Point Fighting exige total inteiro final confirmado por atleta");
            } else if (points != null) {
                throw new IllegalArgumentException(
                        "Os restantes estilos WAKO usam decisão confirmada, não soma inventada de cartões dos juízes");
            }
        }
        if (isPointFighting(type)) {
            int cmp = red.performanceValue().compareTo(blue.performanceValue());
            if (cmp == 0 || (cmp > 0 && red.result() != CommunityMatchResult.WIN)
                    || (cmp < 0 && blue.result() != CommunityMatchResult.WIN))
                throw new IllegalArgumentException(
                        "Point Fighting só suporta vitória inequívoca pelos pontos finais; empate exige contrato próprio");
        }
    }
}
