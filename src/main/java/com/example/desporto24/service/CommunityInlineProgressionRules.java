package com.example.desporto24.service;

import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityRoundEntry;
import java.util.*;

/**
 * Checks a human-confirmed connection between completed sprint heats.
 * Matching participants does not prove a World Skate jury decision.
 */
final class CommunityInlineProgressionRules {
    private CommunityInlineProgressionRules() {}

    static boolean allowedTransition(String source, String target) {
        if (source == null || target == null) return false;
        String family = source.startsWith("INLINE_TRACK_500D_") ? "INLINE_TRACK_500D_"
                : source.startsWith("INLINE_ROAD_LAP_") ? "INLINE_ROAD_LAP_"
                : null;
        if (family == null || !target.startsWith(family)) return false;
        return switch (source.substring(family.length())) {
            case "FIRST_ROUND" -> target.equals(family + "QUARTER_HEAT")
                    || target.equals(family + "SEMI_HEAT");
            case "QUARTER_HEAT" -> target.equals(family + "SEMI_HEAT");
            case "SEMI_HEAT" -> target.equals(family + "FINAL_A");
            default -> false;
        };
    }

    static void validate(CommunityRound target, List<CommunityRound> sources,
            List<CommunityRoundEntry> targetEntries,
            Map<Long, List<CommunityRoundEntry>> entriesBySource) {
        if (target == null || !CommunityInlineSpeedResults.isPlacedHeat(target.getEventType())
                || target.getEventType().endsWith("_FINAL_B"))
            throw new IllegalArgumentException("Escolhe uma manga de destino válida (sem Final B em 2026)");
        if (sources == null || sources.isEmpty() || sources.size() > 20)
            throw new IllegalArgumentException("Seleciona as mangas de origem");
        Set<Long> sourceIds = new HashSet<>();
        Map<Long, CommunityRoundEntry> originsByPlayer = new HashMap<>();
        for (CommunityRound source : sources) {
            if (source == null || !sourceIds.add(source.getId()) || source.getId().equals(target.getId())
                    || !source.getCommunity().getId().equals(target.getCommunity().getId())
                    || !Objects.equals(source.getSeason() == null ? null : source.getSeason().getId(),
                            target.getSeason() == null ? null : target.getSeason().getId())
                    || source.getPlayedAt().isAfter(target.getPlayedAt())
                    || !allowedTransition(source.getEventType(), target.getEventType())) {
                throw new IllegalArgumentException("Manga de origem incompatível com a fase, época ou comunidade");
            }
            for (CommunityRoundEntry entry : entriesBySource.getOrDefault(source.getId(), List.of())) {
                if (entry.isRaceDisqualified())
                    continue;
                Long id = entry.getCommunityPlayer().getId();
                if (originsByPlayer.putIfAbsent(id, entry) != null)
                    throw new IllegalArgumentException("O mesmo patinador surge em várias mangas de origem");
            }
        }
        if (targetEntries == null || targetEntries.isEmpty())
            throw new IllegalArgumentException("Manga de destino sem participantes");
        for (CommunityRoundEntry targetEntry : targetEntries) {
            if (!originsByPlayer.containsKey(targetEntry.getCommunityPlayer().getId()))
                throw new IllegalArgumentException(
                        "Os patinadores do destino têm de existir nas mangas de origem sem desclassificação");
        }
    }
}
