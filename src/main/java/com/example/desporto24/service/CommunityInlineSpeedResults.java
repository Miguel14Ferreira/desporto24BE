package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Organizer-recorded times for the 200 m inline-speed track dual time trial
 * and first-round timed 500 m +D track / one-lap road sprint heats.
 * Timed skaters are not automatically certified as completed heats/finals:
 * pairing, qualifying rounds, timing approval, penalties and disqualification
 * are outside the current community round request contract.
 */
final class CommunityInlineSpeedResults {
    private CommunityInlineSpeedResults() {}

    /** Distinct event codes prevent comparing unrelated distances or circuits. */
    static boolean isRecordedTimeEvent(String eventType) {
        return "INLINE_TRACK_200_TT".equals(eventType)
                || "INLINE_TRACK_500D_FIRST_ROUND".equals(eventType)
                || "INLINE_ROAD_LAP_FIRST_ROUND".equals(eventType);
    }

    private static final List<String> PLACED_HEATS = List.of(
            "INLINE_TRACK_500D_QUARTER_HEAT",
            "INLINE_TRACK_500D_SEMI_HEAT",
            "INLINE_TRACK_500D_FINAL_A",
            "INLINE_TRACK_500D_FINAL_B",
            "INLINE_ROAD_LAP_QUARTER_HEAT",
            "INLINE_ROAD_LAP_SEMI_HEAT",
            "INLINE_ROAD_LAP_FINAL_A",
            "INLINE_ROAD_LAP_FINAL_B");

    static boolean isPlacedHeat(String eventType) {
        return eventType != null && PLACED_HEATS.contains(eventType);
    }

    static boolean isInlineEvent(String eventType) {
        return isRecordedTimeEvent(eventType) || isPlacedHeat(eventType);
    }

    /**
     * Each record represents ONE heat, never the full sprint tournament.
     * Placings are entered and confirmed by the organizer, not computed from
     * a single elapsed time. Jury decisions and cross-heat promotion need a
     * separately linked competition structure and are not inferred here.
     */
    static void validatePlacedHeat(String sport, String eventType,
            List<CommunityRoundEntryRequest> entries) {
        if (!"Patinagem de Velocidade".equals(sport) || !isPlacedHeat(eventType))
            throw new IllegalArgumentException("Manga de velocidade inválida");
        boolean finalHeat = eventType.endsWith("_FINAL_A") || eventType.endsWith("_FINAL_B");
        if (entries == null || entries.size() < 2 || entries.size() > (finalHeat ? 4 : 6))
            throw new IllegalArgumentException("A manga exige entre 2 e "
                    + (finalHeat ? 4 : 6) + " patinadores");
        Set<String> skaters = new HashSet<>();
        Set<Integer> positions = new HashSet<>();
        for (CommunityRoundEntryRequest entry : entries) {
            if (entry == null || entry.username() == null || entry.username().isBlank()
                    || !skaters.add(entry.username().trim().toLowerCase(Locale.ROOT))
                    || entry.result() != CommunityMatchResult.DRAW
                    || entry.team() != null && !entry.team().isBlank()
                    || entry.goals() != null && entry.goals() != 0
                    || entry.assists() != null && entry.assists() != 0
                    || entry.mvp() || entry.cleanSheet()
                    || entry.bestLapSeconds() != null || entry.lapsCompleted() != null
                    || entry.performanceValue() != null
                    || entry.waveScore1() != null || entry.waveScore2() != null
                    || entry.finishPosition() == null || entry.finishPosition() < 1
                    || entry.finishPosition() > entries.size()
                    || !positions.add(entry.finishPosition()))
                throw new IllegalArgumentException(
                        "Regista uma posição única de chegada para cada patinador da manga");
            BigDecimal time = entry.elapsedSeconds();
            if (time != null && (time.signum() <= 0 || time.scale() > 2
                    || time.precision() - time.scale() > 8))
                throw new IllegalArgumentException(
                        "Tempo da manga: segundos positivos, até duas casas decimais");
        }
        // Unique positions within [1, numberOfEntries] must be consecutive.
        if (positions.size() != entries.size())
            throw new IllegalArgumentException("Manga sem posições completas");
    }

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        if (!"Patinagem de Velocidade".equals(sport) || entries == null || entries.isEmpty())
            throw new IllegalArgumentException(
                    "Patinagem de Velocidade exige participantes");
        Set<String> skaters = new HashSet<>();
        for (CommunityRoundEntryRequest entry : entries) {
            if (entry == null || entry.username() == null
                    || entry.username().isBlank()
                    || !skaters.add(entry.username().trim().toLowerCase(Locale.ROOT))
                    || entry.result() != CommunityMatchResult.DRAW
                    || entry.team() != null && !entry.team().isBlank()
                    || entry.finishPosition() != null
                    || entry.bestLapSeconds() != null || entry.lapsCompleted() != null
                    || entry.goals() != null && entry.goals() != 0
                    || entry.assists() != null && entry.assists() != 0
                    || entry.mvp() || entry.cleanSheet()
                    || entry.performanceValue() != null
                    || entry.waveScore1() != null || entry.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Patinagem de Velocidade apenas admite atletas e tempos registados, sem classificações oficiais ou métricas de outras modalidades");
            BigDecimal time = entry.elapsedSeconds();
            if (time != null && (time.signum() <= 0 || time.scale() > 2
                    || time.precision() - time.scale() > 8))
                throw new IllegalArgumentException(
                        "Tempo da prova de Patinagem: segundos positivos com até dois decimais");
        }
    }
}
