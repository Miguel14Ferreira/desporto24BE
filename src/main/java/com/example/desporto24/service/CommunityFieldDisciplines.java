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

    // Individual senior rhythmic apparatus, distinct from group all-around.
    private static final List<String> RHYTHMIC_APPARATUS = List.of(
            "HOOP", "BALL", "CLUBS", "RIBBON");

    private static final List<String> APPARATUS = List.of(
            "FLOOR", "VAULT", "POMMEL_HORSE", "RINGS",
            "PARALLEL_BARS", "HORIZONTAL_BAR", "UNEVEN_BARS", "BALANCE_BEAM");

    static String validate(String sport, String eventType, String requested) {
        List<String> allowed = "Atletismo".equals(sport) ? ATHLETICS.get(eventType)
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
