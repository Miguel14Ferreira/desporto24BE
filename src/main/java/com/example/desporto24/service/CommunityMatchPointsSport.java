package com.example.desporto24.service;

import java.util.Set;

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

    static boolean supports(String sport) {
        return sport != null && TEAM_SPORTS.contains(sport);
    }
}
