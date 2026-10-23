package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityGroupRoutineScore;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * One shared score per real performing group, never duplicated into athlete statistics.
 * Not a federation score calculator and never used for generic W/D/L standings.
 */
final class CommunityGroupRoutineResults {
    private CommunityGroupRoutineResults() {}
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final TypeReference<List<CommunityGroupRoutineScore>> TYPE =
            new TypeReference<>() {};

    static boolean isGroupEvent(String type) {
        return Set.of("RHYTHMIC_GROUP_5", "RHYTHMIC_GROUP_3_2",
                "ARTISTIC_DUET_TECH", "ARTISTIC_DUET_FREE",
                "ARTISTIC_TEAM_TECH", "ARTISTIC_TEAM_FREE").contains(type == null ? "" : type);
    }

    private static String groupKey(String name) {
        if (name == null || name.isBlank() || name.strip().length() > 60)
            throw new IllegalArgumentException("Indica o nome de cada dueto, equipa ou conjunto (até 60 caracteres)");
        return name.strip().toLowerCase(Locale.ROOT);
    }

    static String validateAndEncode(String sport, String eventType,
            List<CommunityRoundEntryRequest> entries,
            List<CommunityGroupRoutineScore> scores) {
        if (!isGroupEvent(eventType)) {
            if (scores != null && !scores.isEmpty())
                throw new IllegalArgumentException("Esta modalidade não suporta resultados por grupo");
            return null;
        }
        if (scores == null || scores.isEmpty() || scores.size() > 32)
            throw new IllegalArgumentException("Regista de 1 a 32 grupos com uma nota por grupo");
        int min = eventType.startsWith("ARTISTIC_DUET") ? 2
                : eventType.startsWith("RHYTHMIC_GROUP") ? 5 : 4;
        int max = eventType.startsWith("ARTISTIC_DUET") ? 2
                : eventType.startsWith("RHYTHMIC_GROUP") ? 5 : 8;
        Map<String, Long> memberCounts = new LinkedHashMap<>();
        for (CommunityRoundEntryRequest e : entries) {
            String key = groupKey(e.team());
            memberCounts.merge(key, 1L, Long::sum);
            if (e.result() != CommunityMatchResult.DRAW
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException("A nota é do grupo; cada atleta só tem participação e grupo");
        }
        if (memberCounts.size() > 32 || memberCounts.values().stream()
                .anyMatch(n -> n < min || n > max))
            throw new IllegalArgumentException(
                    "Número de participantes inválido por grupo: " + min + " a " + max);
        Set<String> scoredGroups = new HashSet<>();
        List<CommunityGroupRoutineScore> clean = new ArrayList<>();
        for (CommunityGroupRoutineScore item : scores) {
            if (item == null) throw new IllegalArgumentException("Grupo inválido");
            String key = groupKey(item.groupName());
            if (!memberCounts.containsKey(key) || !scoredGroups.add(key))
                throw new IllegalArgumentException("Os grupos devem ter um único resultado e participantes associados");
            BigDecimal value = item.score();
            int fraction = eventType.startsWith("RHYTHMIC") ? 3 : 4;
            if (value != null && (value.signum() < 0
                    || value.precision() - value.scale() > 6
                    || value.stripTrailingZeros().scale() > fraction))
                throw new IllegalArgumentException("Nota inválida ou precisão excessiva para a modalidade");
            clean.add(new CommunityGroupRoutineScore(item.groupName().strip(), value));
        }
        if (!scoredGroups.equals(memberCounts.keySet()))
            throw new IllegalArgumentException("Cada dueto ou equipa deve ter exatamente um resultado");
        try {
            return JSON.writeValueAsString(clean);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível guardar os resultados por grupo", e);
        }
    }

    static List<CommunityGroupRoutineScore> decode(String stored) {
        if (stored == null || stored.isBlank()) return List.of();
        try {
            return JSON.readValue(stored, TYPE);
        } catch (Exception e) {
            throw new IllegalStateException("Resultados de grupos inválidos no histórico", e);
        }
    }
}
