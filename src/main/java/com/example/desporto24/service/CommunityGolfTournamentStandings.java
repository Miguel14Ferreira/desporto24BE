package com.example.desporto24.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pure gross stroke-play ranking for one completed 9- or 18-hole round.
 * Input totals are organizer-confirmed; no handicap, countback or season
 * aggregation is inferred. Not yet wired into tournament creation.
 */
final class CommunityGolfTournamentStandings {
    private CommunityGolfTournamentStandings() {}

    record Score(long playerId, BigDecimal grossStrokes) {}
    record Standing(int position, long playerId, long grossStrokes) {}

    static List<Standing> calculate(int holes, List<Score> scores) {
        if ((holes != 9 && holes != 18) || scores == null || scores.isEmpty())
            throw new IllegalArgumentException("Prova de golfe inválida");
        Set<Long> players = new HashSet<>();
        List<Standing> sorted = new ArrayList<>();
        for (Score score : scores) {
            if (score == null || score.playerId() <= 0 || !players.add(score.playerId())
                    || score.grossStrokes() == null)
                throw new IllegalArgumentException("Participante de golfe inválido");
            BigDecimal gross = score.grossStrokes().stripTrailingZeros();
            if (gross.scale() > 0 || gross.compareTo(BigDecimal.valueOf(holes)) < 0
                    || gross.compareTo(BigDecimal.valueOf(999999)) > 0)
                throw new IllegalArgumentException("Total de pancadas inválido");
            sorted.add(new Standing(0, score.playerId(), gross.longValueExact()));
        }
        sorted.sort(Comparator.comparingLong(Standing::grossStrokes)
                .thenComparingLong(Standing::playerId));
        List<Standing> ranked = new ArrayList<>(sorted.size());
        long previous = -1;
        int position = 0;
        for (int i = 0; i < sorted.size(); i++) {
            Standing current = sorted.get(i);
            if (i == 0 || current.grossStrokes() != previous) position = i + 1;
            ranked.add(new Standing(position, current.playerId(), current.grossStrokes()));
            previous = current.grossStrokes();
        }
        return List.copyOf(ranked);
    }
}
