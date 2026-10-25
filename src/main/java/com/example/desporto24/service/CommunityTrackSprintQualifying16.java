package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Article 3.2.050 UCI (16-starter variant). Only an explicit recorded flying
 * 200m result with 16 unequivocal times may seed the first eight duels.
 * Ties need commissaires' last-100m/draw decision; do not decide them here.
 */
final class CommunityTrackSprintQualifying16 {
    private CommunityTrackSprintQualifying16() {}

    static List<String> seeds(CommunityRoundResponse qualifying) {
        return seeds(qualifying, 16);
    }

    /** Both UCI 16- and 28-rider formats use the same qualifying 200m
     * evidence; no times or tie-breaking outcomes are inferred.
     */
    static List<String> seeds(CommunityRoundResponse qualifying, int starters) {
        if (starters != 16 && starters != 28)
            throw new IllegalArgumentException("Formato de Sprint não suportado");
        if (qualifying == null || !"Ciclismo de Pista".equals(qualifying.sport())
                || !"TRACK_FLYING_200".equals(qualifying.eventType())
                || qualifying.entries() == null)
            throw new IllegalArgumentException("Escolhe uma qualificação de 200 m de Ciclismo de Pista");
        record Entry(String username, BigDecimal time) {}
        Set<String> unique = new HashSet<>();
        List<Entry> valid = new ArrayList<>();
        for (var e : qualifying.entries()) {
            if (e.username() == null || e.username().isBlank())
                throw new IllegalArgumentException("Há um participante da qualificação sem conta");
            String username = e.username().trim().toLowerCase(Locale.ROOT);
            if (!unique.add(username))
                throw new IllegalArgumentException("Um ciclista está duplicado nos 200 m");
            if (e.raceDisqualified()) continue;
            if (e.elapsedSeconds() == null || e.elapsedSeconds().signum() <= 0)
                continue; // No valid time: no qualifying seed.
            valid.add(new Entry(username, e.elapsedSeconds()));
        }
        valid.sort(Comparator.comparing(Entry::time).thenComparing(Entry::username));
        if (valid.size() < starters)
            throw new IllegalArgumentException("A qualificação exige " + starters + " tempos de 200 m válidos");
        // Need unambiguous ordering AND cutoff. UCI article 3.2.027 requires
        // last-100m timing or drawing lots; this feature does not assume either.
        for (int i=1; i<Math.min(starters + 1,valid.size()); i++) {
            if (valid.get(i).time().compareTo(valid.get(i-1).time()) == 0)
                throw new IllegalArgumentException("Empate nos 200 m: regista primeiro o desempate oficial");
        }
        return valid.subList(0,starters).stream().map(Entry::username).toList();
    }
}
