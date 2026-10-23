package com.example.desporto24.service;

import java.util.List;
import java.util.Map;

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
