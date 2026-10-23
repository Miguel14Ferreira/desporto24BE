package com.example.desporto24.service;

import java.util.List;
import java.util.Map;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;

/**
 * Structured event variants for sports that contain incompatible event types.
 * An event type describes the contest, not its scoring or federation rules.
 * Historical records keep null; new records for these sports require a choice.
 */
final class CommunityRoundEventType {
    private CommunityRoundEventType() {}

    private static final Map<String, List<String>> TYPES = Map.of(
            "BMX", List.of("RACE", "FREESTYLE"),
            "Atletismo", List.of("TRACK", "JUMP", "THROW"),
            "Snowboard", List.of("RACE", "FREESTYLE")
    );

    static String validate(String sport, String requested) {
        List<String> allowed = TYPES.get(sport);
        if (allowed == null) {
            if (requested != null && !requested.isBlank())
                throw new IllegalArgumentException(
                        "Este desporto ainda não tem tipos de prova definidos");
            return null;
        }
        if (requested == null || requested.isBlank()) {
            throw new IllegalArgumentException("Seleciona o tipo de prova para " + sport);
        }
        String type = requested.trim();
        if (!allowed.contains(type))
            throw new IllegalArgumentException(
                    "Tipo de prova inválido para a modalidade selecionada");
        return type;
    }

    /**
     * Field/freestyle event results have not yet been modeled; never allow
     * arbitrary WDL, football stats or teams to silently become a ranking.
     */
    static void validateEntries(String sport, String type,
            List<CommunityRoundEntryRequest> entries) {
        if (!isNeutralEvent(sport, type)) return;
        for (CommunityRoundEntryRequest e : entries) {
            if (e.result() != CommunityMatchResult.DRAW
                    || (e.team() != null && !e.team().isBlank())
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()) {
                throw new IllegalArgumentException(
                        "Este tipo de prova ainda só permite registar participantes, sem resultados calculados");
            }
        }
    }

    static boolean isRaceEvent(String sport, String eventType) {
        if (sport == null) return false;
        if ("Atletismo".equals(sport)) return "TRACK".equals(eventType);
        if ("Snowboard".equals(sport)) return "RACE".equals(eventType);
        if ("BMX".equals(sport)) return eventType == null || "RACE".equals(eventType);
        return CommunityRaceMetrics.isRaceSport(sport) && eventType == null;
    }

    static boolean supportsRaceChampionship(String sport) {
        return CommunityRaceMetrics.isRaceSport(sport)
                || "Atletismo".equals(sport) || "Snowboard".equals(sport);
    }

    static boolean isNeutralEvent(String sport, String eventType) {
        return eventType != null && !isRaceEvent(sport, eventType);
    }
}
