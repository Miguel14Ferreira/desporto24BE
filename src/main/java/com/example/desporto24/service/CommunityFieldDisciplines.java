package com.example.desporto24.service;

import java.util.List;
import java.util.Map;

/** A discipline is descriptive metadata, not an official points or attempt rule. */
final class CommunityFieldDisciplines {
    private CommunityFieldDisciplines() {}

    private static final Map<String, List<String>> ATHLETICS = Map.of(
            "JUMP", List.of("LONG_JUMP", "TRIPLE_JUMP", "HIGH_JUMP", "POLE_VAULT"),
            "THROW", List.of("SHOT_PUT", "DISCUS", "JAVELIN", "HAMMER")
    );

    // Bodyboard heats are divided by stance; never rank prone vs drop knee together.
    private static final List<String> BODYBOARD_STANCES = List.of("PRONE", "DROP_KNEE");

    // Conservative 50-metre pool, individual events only. No relays, short course or open water.
    private static final List<String> SWIM_LCM = List.of(
            "FREE_50", "FREE_100", "FREE_200", "BACK_100",
            "BREAST_100", "FLY_100", "IM_200");

    // Descriptive stage for an independently recorded Sprint heat.
    // A complete bracket or best-of-three match outcome requires a separate relation.
    private static final List<String> TRACK_SPRINT_PHASES = List.of(
            "SPRINT_THIRTY_SECOND_FINAL", "SPRINT_SIXTEENTH_FINAL", "SPRINT_EIGHTH_FINAL", "SPRINT_QUARTER_FINAL", "SPRINT_SEMI_FINAL",
            "SPRINT_BRONZE_FINAL", "SPRINT_GOLD_FINAL");

    private static final List<String> TRACK_SPRINT_REPECHAGES = List.of(
            "SPRINT_REPECHAGE_1_32", "SPRINT_REPECHAGE_1_16", "SPRINT_REPECHAGE_1_8");

    private static final List<String> TRACK_SPRINT_CLASSIFICATION = List.of("SPRINT_PLACES_5_8");

    // Only separately timed individual distances; knockout sprint and relays need
    // dedicated multi-stage and team contracts, so they are not offered here.
    private static final List<String> OPEN_WATER = List.of("OWS_5K", "OWS_10K");

    // Descriptive course formats; controls, relay legs and knockout stages are not modeled.
    private static final List<String> FOOT_ORIENTEERING =
            List.of("ORIENT_SPRINT", "ORIENT_MIDDLE", "ORIENT_LONG");

    // Individual senior rhythmic apparatus, distinct from group all-around.
    private static final List<String> RHYTHMIC_APPARATUS = List.of(
            "HOOP", "BALL", "CLUBS", "RIBBON");

    private static final List<String> APPARATUS = List.of(
            "FLOOR", "VAULT", "POMMEL_HORSE", "RINGS",
            "PARALLEL_BARS", "HORIZONTAL_BAR", "UNEVEN_BARS", "BALANCE_BEAM");

    static String validate(String sport, String eventType, String requested) {
        List<String> allowed = "Ciclismo de Pista".equals(sport)
                && "TRACK_SPRINT_HEAT".equals(eventType) ? TRACK_SPRINT_PHASES
                : "Ciclismo de Pista".equals(sport)
                && "TRACK_SPRINT_REPECHAGE".equals(eventType) ? TRACK_SPRINT_REPECHAGES
                : "Ciclismo de Pista".equals(sport)
                && "TRACK_SPRINT_5_8".equals(eventType) ? TRACK_SPRINT_CLASSIFICATION
                : "Orientação".equals(sport)
                && "FOOT_ORIENTEERING".equals(eventType) ? FOOT_ORIENTEERING
                : "Natação em Águas Abertas".equals(sport)
                && "OWS_INDIVIDUAL".equals(eventType) ? OPEN_WATER
                : "Natação".equals(sport) && "SWIM_LCM".equals(eventType)
                ? SWIM_LCM
                : "Atletismo".equals(sport) ? ATHLETICS.get(eventType)
                : "Ginástica Artística".equals(sport) && "APPARATUS".equals(eventType)
                ? APPARATUS
                : "Bodyboard".equals(sport) && "BODYBOARD_HEAT".equals(eventType)
                ? BODYBOARD_STANCES
                : "Ginástica Rítmica".equals(sport) && "RHYTHMIC_APPARATUS".equals(eventType)
                ? RHYTHMIC_APPARATUS : null;
        if (allowed == null) {
            if (requested != null && !requested.isBlank()) {
                throw new IllegalArgumentException(
                        "Esta modalidade ou tipo de prova não suporta subdisciplinas");
            }
            return null;
        }
        if (requested == null || requested.isBlank()) {
            throw new IllegalArgumentException("Seleciona a disciplina ou aparelho da prova");
        }
        String code = requested.trim();
        if (!allowed.contains(code)) {
            throw new IllegalArgumentException(
                    "Disciplina inválida para o tipo de prova selecionado");
        }
        return code;
    }
}
