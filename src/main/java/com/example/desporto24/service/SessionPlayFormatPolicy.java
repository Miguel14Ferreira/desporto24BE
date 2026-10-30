package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionPlayFormat;
import java.util.Set;

/** Safe, explicit list of catalogued sports that can host doubles play. */
public final class SessionPlayFormatPolicy {
    private static final Set<String> PAIR_CAPABLE = Set.of(
            "Padel", "Ténis", "Ténis de praia", "Ténis de mesa",
            "Badminton", "Pickleball"
    );

    private SessionPlayFormatPolicy() {}

    public static boolean canPlayPairs(String sport) {
        return PAIR_CAPABLE.contains(sport);
    }

    public static SessionPlayFormat resolve(String sport, SessionPlayFormat selected) {
        if (selected == null) {
            return TeamBalancingSportPolicy.isKnownTeamSport(sport)
                    ? SessionPlayFormat.TEAMS : SessionPlayFormat.INDIVIDUAL;
        }
        if (selected == SessionPlayFormat.PAIRS && canPlayPairs(sport)) {
            return selected;
        }
        if (selected == SessionPlayFormat.TEAMS
                && TeamBalancingSportPolicy.isKnownTeamSport(sport)) {
            return selected;
        }
        if (selected == SessionPlayFormat.INDIVIDUAL
                && !TeamBalancingSportPolicy.isKnownTeamSport(sport)) {
            return selected;
        }
        throw new IllegalArgumentException("Formato incompatível com a modalidade");
    }
}
