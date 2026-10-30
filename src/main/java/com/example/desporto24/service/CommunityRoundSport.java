package com.example.desporto24.service;

import com.example.desporto24.model.community.Community;
import java.util.List;

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
        if (requested == null || requested.isBlank()) {
            return sports.size() == 1 ? sports.get(0) : null;
        }
        String trimmed = requested.trim();
        return sports.stream().filter(s -> s.equalsIgnoreCase(trimmed)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "A modalidade da jornada tem de pertencer às modalidades da comunidade"));
    }
}
