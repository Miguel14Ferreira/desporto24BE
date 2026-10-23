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
            "Snowboard", List.of("RACE", "FREESTYLE"),
            "Surf", List.of("HEAT"),
            "Bodyboard", List.of("BODYBOARD_HEAT"),
            "Ginástica Artística", List.of("APPARATUS")
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
        boolean neutral = isNeutralEvent(sport, type);
        for (CommunityRoundEntryRequest e : entries) {
            java.math.BigDecimal value = e.performanceValue();
            if (!neutral) {
                if (value != null || e.waveScore1() != null || e.waveScore2() != null)
                    throw new IllegalArgumentException(
                            "Esta modalidade não suporta estas marcas ou notas");
                continue;
            }
            if ("HEAT".equals(type) || "BODYBOARD_HEAT".equals(type)) {
                if (value != null)
                    throw new IllegalArgumentException("Baterias de ondas usam notas por onda");
                validateWave(e.waveScore1());
                validateWave(e.waveScore2());
            } else {
                if (e.waveScore1() != null || e.waveScore2() != null)
                    throw new IllegalArgumentException("Notas por onda só são permitidas nas baterias suportadas");
                if (value != null) {
                    if (value.scale() > 3 || value.precision() - value.scale() > 6
                            || value.signum() < 0)
                        throw new IllegalArgumentException(
                                "A marca ou nota deve ser positiva, até 3 casas decimais");
                    if ("FREESTYLE".equals(type) || "APPARATUS".equals(type)) {
                        if (value.compareTo(new java.math.BigDecimal("100")) > 0)
                            throw new IllegalArgumentException(
                                    "A nota final registada deve estar entre 0 e 100");
                    } else if (value.signum() <= 0) {
                        throw new IllegalArgumentException(
                                "A marca em metros deve ser superior a zero");
                    }
                }
            }
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

    private static void validateWave(java.math.BigDecimal value) {
        if (value == null) return;
        if (value.signum() < 0 || value.scale() > 3
                || value.compareTo(java.math.BigDecimal.TEN) > 0)
            throw new IllegalArgumentException("Nota de onda: 0 a 10, até 3 casas decimais");
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
