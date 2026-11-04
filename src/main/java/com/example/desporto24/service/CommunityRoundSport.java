package com.example.desporto24.service;

import com.example.desporto24.model.community.Community;
import java.util.List;
import java.text.Normalizer;
import java.util.Locale;

/**
 * A round can use one of the community's declared disciplines.
 * Legacy community-wide rounds keep null when the discipline is ambiguous.
 */
final class CommunityRoundSport {
    private CommunityRoundSport() {}

    static String resolve(Community community, String requested) {
        List<String> sports = community.getSports() != null && !community.getSports().isEmpty()
                ? community.getSports()
                : community.getSport() == null ? List.of() : List.of(community.getSport());
        // Imported and edited communities can contain repeated labels with
        // different casing or accents. Count distinct sports, not raw entries,
        // without guessing a sport when genuinely several are configured.
        java.util.Map<String, String> distinctSports = new java.util.LinkedHashMap<>();
        for (String sport : sports) {
            if (sport != null && !sport.isBlank()) {
                distinctSports.putIfAbsent(normalize(sport), sport.trim());
            }
        }
        if (requested == null || requested.isBlank()) {
            return distinctSports.size() == 1
                    ? distinctSports.values().iterator().next() : null;
        }
        String normalizedRequested = normalize(requested);
        String selectedSport = distinctSports.get(normalizedRequested);
        if (selectedSport == null) {
            throw new IllegalArgumentException(
                    "A modalidade da jornada tem de pertencer às modalidades da comunidade");
        }
        return selectedSport;
    }

    /** Null denotes a historic community-wide season, which accepts every declared sport. */
    static boolean belongsToSeason(String seasonSport, String roundSport) {
        return seasonSport == null || seasonSport.isBlank()
                || (roundSport != null && normalize(seasonSport).equals(normalize(roundSport)));
    }

    private static String normalize(String sport) {
        return Normalizer.normalize(sport.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT);
    }
}
