package com.example.desporto24.service;

import java.util.Set;
import java.text.Normalizer;
import java.util.Locale;

/**
 * Team-result sports eligible for configurable generic W/D/L season points.
 * Racquet matches, races, timed events and judged performances retain their
 * existing specialised scoring contracts.
 */
final class CommunityMatchPointsSport {
    private CommunityMatchPointsSport() {}

    private static final Set<String> TEAM_SPORTS = Set.of(
            "Andebol", "Basebol", "Basquetebol", "Basquetebol 3x3", "Corfebol",
            "Cricket", "Curling", "Flag Football", "Futebol", "Futebol Americano",
            "Futevólei", "Futsal", "Hóquei", "Hóquei em Campo", "Hóquei em Patins",
            "Hóquei no Gelo", "Lacrosse", "Polo Aquático", "Rugby", "Rugby Sevens",
            "Softbol", "Voleibol", "Voleibol de Praia");

    private static final Set<String> NORMALIZED_TEAM_SPORTS = TEAM_SPORTS.stream()
            .map(CommunityMatchPointsSport::normalize).collect(java.util.stream.Collectors.toUnmodifiableSet());

    static boolean supports(String sport) {
        return sport != null && NORMALIZED_TEAM_SPORTS.contains(normalize(sport));
    }

    private static String normalize(String sport) {
        return Normalizer.normalize(sport.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
    }
}
