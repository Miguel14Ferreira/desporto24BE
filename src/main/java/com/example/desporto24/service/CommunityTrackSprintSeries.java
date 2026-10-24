package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Opt-in, season-scoped Sprint series. Each Jornada remains a single heat.
 * Never guesses grouping from the Jornada title or historical entries.
 * Bracket progression must be recorded separately by the organiser.
 */
final class CommunityTrackSprintSeries {
    private CommunityTrackSprintSeries() {}

    record Heat(String phase, int number, String riderA, String riderB, String winner) {}

    static Heat fromRound(CommunityRoundResponse round) {
        if (!"TRACK_SPRINT_HEAT".equals(round.eventType()) || round.entries().size() != 2)
            throw new IllegalArgumentException("Registo de manga Sprint inválido");
        var a = round.entries().get(0);
        var b = round.entries().get(1);
        if (a.username() == null || b.username() == null)
            throw new IllegalArgumentException("Os ciclistas da série devem ter contas identificadas");
        String winner = a.result() == CommunityMatchResult.WIN ? a.username()
                : b.result() == CommunityMatchResult.WIN ? b.username() : null;
        if (winner == null) throw new IllegalArgumentException("A manga não tem vencedor");
        return new Heat(round.eventDiscipline(), round.sprintHeatNumber(),
                a.username(), b.username(), winner);
    }

    static void validate(String eventType, String phase, String seriesKey, Integer heatNumber,
            List<Heat> previous, List<CommunityRoundEntryRequest> upcoming) {
        boolean sprint = "TRACK_SPRINT_HEAT".equals(eventType);
        if (!sprint) {
            if (seriesKey != null || heatNumber != null)
                throw new IllegalArgumentException("A ligação de séries só é permitida no Sprint de Pista");
            return;
        }
        if (seriesKey == null && heatNumber == null) return; // legacy independent heat
        if (seriesKey == null || !seriesKey.matches("[A-Za-z0-9][A-Za-z0-9 _.\\-]{0,39}")
                || heatNumber == null)
            throw new IllegalArgumentException("Indica uma série (até 40 caracteres) e o número da manga");
        int maxHeats = "SPRINT_QUARTER_FINAL".equals(phase) ? 1 : 3;
        if (heatNumber < 1 || heatNumber > maxHeats)
            throw new IllegalArgumentException("Número de manga incompatível com a fase Sprint");
        if (upcoming.size() != 2)
            throw new IllegalArgumentException("A série exige exatamente dois ciclistas");
        Set<String> pair = Set.of(id(upcoming.get(0).username()), id(upcoming.get(1).username()));
        if (pair.size() != 2)
            throw new IllegalArgumentException("Os ciclistas devem ser distintos");
        if (previous.size() >= maxHeats || heatNumber != previous.size() + 1)
            throw new IllegalArgumentException("A série exige mangas consecutivas e sem duplicados");
        Map<String,Integer> wins = new HashMap<>();
        for (int i = 0; i < previous.size(); i++) {
            Heat old = previous.get(i);
            if (!phase.equals(old.phase()) || old.number() != i + 1 ||
                    !pair.equals(Set.of(id(old.riderA()), id(old.riderB()))) ||
                    !pair.contains(id(old.winner())))
                throw new IllegalArgumentException("A série tem fase, ordem ou ciclistas incompatíveis");
            wins.merge(id(old.winner()), 1, Integer::sum);
        }
        if (wins.values().stream().anyMatch(w -> w >= (maxHeats == 1 ? 1 : 2)))
            throw new IllegalArgumentException("A série já tem vencedor; não podes acrescentar mangas");
    }

    static String seriesWinner(String phase, List<Heat> heats) {
        if (heats.isEmpty()) return null;
        int required = "SPRINT_QUARTER_FINAL".equals(phase) ? 1 : 2;
        Map<String,Integer> wins = new HashMap<>();
        for (Heat heat : heats) {
            int count = wins.merge(id(heat.winner()), 1, Integer::sum);
            if (count == required) return heat.winner();
        }
        return null;
    }

    private static String id(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("O ciclista da série não está identificado");
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
