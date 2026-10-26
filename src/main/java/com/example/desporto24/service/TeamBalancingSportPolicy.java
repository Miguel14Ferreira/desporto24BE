package com.example.desporto24.service;

import com.example.desporto24.constant.SportsCatalog;
import com.example.desporto24.model.Session;
import com.example.desporto24.enumeration.SessionPlayFormat;

import java.util.Set;

/**
 * Explicit, conservative team-balance eligibility for session sports.
 * Never infer team play from the number of participants alone: individual and
 * mixed singles/doubles modalities need a separately recorded team format.
 * Session capacity is configured by the organiser, not hardcoded to 10.
 */
public final class TeamBalancingSportPolicy {
    private static final Set<String> TEAM_SPORTS = Set.of(
            "Andebol", "Basebol", "Basquetebol", "Basquetebol 3x3", "Corfebol",
            "Cricket", "Curling", "Flag Football", "Futebol", "Futebol Americano",
            "Futsal", "Hóquei", "Hóquei em Campo", "Hóquei em Patins",
            "Hóquei no Gelo", "Lacrosse", "Polo Aquático", "Rugby",
            "Rugby Sevens", "Softbol", "Voleibol", "Voleibol de Praia"
    );

    private TeamBalancingSportPolicy() {}

    public static boolean isKnownTeamSport(String sport) {
        return TEAM_SPORTS.contains(sport);
    }

    public static boolean isEligible(Session session) {
        if (session == null || session.getCommunity() == null ||
                session.getMaxParticipants() == null ||
                (session.getExternalConfirmedParticipants() != null &&
                        session.getExternalConfirmedParticipants() > 0)) {
            return false;
        }
        SessionPlayFormat mode = session.getPlayFormat();
        if (mode == SessionPlayFormat.INDIVIDUAL) return false;
        if (mode == SessionPlayFormat.PAIRS) {
            return SessionPlayFormatPolicy.canPlayPairs(session.getSport())
                    && session.getMaxParticipants() == 4;
        }
        // Null is a legacy record: retain conservative team-only behavior.
        return isEligible(session.getSport(), session.getMaxParticipants());
    }

    public static boolean isEligible(String sport, int confirmedCapacity) {
        if (sport == null || !SportsCatalog.ALLOWED.contains(sport) ||
                !TEAM_SPORTS.contains(sport) || confirmedCapacity < 4 ||
                (confirmedCapacity % 2) != 0) {
            return false;
        }
        // Named formats are not silently reinterpreted as a different game.
        return switch (sport) {
            case "Basquetebol 3x3" -> confirmedCapacity == 6;
            case "Rugby Sevens" -> confirmedCapacity == 14;
            case "Voleibol de Praia" -> confirmedCapacity == 4;
            default -> true;
        };
    }
}
