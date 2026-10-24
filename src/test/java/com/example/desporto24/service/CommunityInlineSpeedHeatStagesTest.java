package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityInlineSpeedHeatStagesTest {
    private static final List<String> TRACK = List.of(
            "INLINE_TRACK_500D_QUARTER_HEAT", "INLINE_TRACK_500D_SEMI_HEAT",
            "INLINE_TRACK_500D_FINAL_A", "INLINE_TRACK_500D_FINAL_B");
    private static final List<String> ROAD = List.of(
            "INLINE_ROAD_LAP_QUARTER_HEAT", "INLINE_ROAD_LAP_SEMI_HEAT",
            "INLINE_ROAD_LAP_FINAL_A", "INLINE_ROAD_LAP_FINAL_B");

    private CommunityRoundEntryRequest entry(String username, Integer pos, String sec) {
        return new CommunityRoundEntryRequest(username, null, CommunityMatchResult.DRAW,
                0, 0, false, false, pos,
                sec == null ? null : new BigDecimal(sec), null, null);
    }

    @Test void supportsEachTrackAndRoadStageWithoutChangingLegacyTimedOptions() {
        for (String code : List.of(
                "INLINE_TRACK_500D_QUARTER_HEAT", "INLINE_TRACK_500D_SEMI_HEAT",
                "INLINE_TRACK_500D_FINAL_A", "INLINE_TRACK_500D_FINAL_B",
                "INLINE_ROAD_LAP_QUARTER_HEAT", "INLINE_ROAD_LAP_SEMI_HEAT",
                "INLINE_ROAD_LAP_FINAL_A", "INLINE_ROAD_LAP_FINAL_B")) {
            assertTrue(CommunityInlineSpeedResults.isPlacedHeat(code));
            assertTrue(CommunityInlineSpeedResults.isInlineEvent(code));
            assertEquals(code, CommunityRoundEventType.validate("Patinagem de Velocidade", code));
            assertTrue(CommunityRoundEventType.isRaceEvent("Patinagem de Velocidade", code));
            assertFalse(CommunityRoundEventType.isNeutralEvent("Patinagem de Velocidade", code));
            assertFalse(CommunityRoundEventType.isChampionshipRaceEvent("Patinagem de Velocidade", code));
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                    "Patinagem de Velocidade", code,
                    List.of(entry("ana", 2, "45.37"), entry("bruno", 1, "45.20"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validate("Patinagem Artística", code));
        }
        assertFalse(CommunityInlineSpeedResults.isPlacedHeat(null));
        assertFalse(CommunityInlineSpeedResults.isInlineEvent(null));
        assertFalse(CommunityInlineSpeedResults.isPlacedHeat("INLINE_TRACK_200_TT"));
        assertTrue(CommunityInlineSpeedResults.isInlineEvent("INLINE_TRACK_200_TT"));
        assertTrue(CommunityInlineSpeedResults.isInlineEvent("INLINE_TRACK_500D_FIRST_ROUND"));
        assertTrue(CommunityInlineSpeedResults.isInlineEvent("INLINE_ROAD_LAP_FIRST_ROUND"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate(
                        "Patinagem de Velocidade", "INLINE_TRACK_500D_FINAL_C"));
    }

    @Test void requiresConfirmedUniqueCompletePositionsAndNoFictitiousSportsMetrics() {
        for (String code : List.of(TRACK.get(0), ROAD.get(1), TRACK.get(2), ROAD.get(3))) {
            for (List<CommunityRoundEntryRequest> entries : List.of(
                    List.of(entry("ana", 1, "45.20")),
                    List.of(entry("ana", null, "45.20"), entry("bruno", 1, "45.37")),
                    List.of(entry("ana", 1, "45.20"), entry("bruno", 1, "45.37")),
                    List.of(entry("ana", 1, "45.20"), entry("bruno", 3, "45.37")),
                    List.of(entry("ana", 1, "45.20"), entry(" ANA ", 2, "45.37")),
                    List.of(entry("ana", 1, "45.201"), entry("bruno", 2, "45.37")),
                    List.of(entry("ana", 1, "0"), entry("bruno", 2, "45.37"))
            )) {
                assertThrows(IllegalArgumentException.class,
                        () -> CommunityRoundEventType.validateEntries(
                                "Patinagem de Velocidade", code, entries));
            }
        }
    }

    @Test void permitsFourSkaterFinalsButNotOverfilledFinalOrHeat() {
        var four = List.of(entry("a", 1, null), entry("b", 3, "50.01"),
                entry("c", 2, null), entry("d", 4, "52.20"));
        var five = List.of(entry("a", 1, null), entry("b", 2, null),
                entry("c", 3, null), entry("d", 4, null), entry("e", 5, null));
        for (String code : TRACK) {
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                    "Patinagem de Velocidade", code, four));
            if (code.endsWith("_FINAL_A") || code.endsWith("_FINAL_B"))
                assertThrows(IllegalArgumentException.class,
                        () -> CommunityRoundEventType.validateEntries(
                                "Patinagem de Velocidade", code, five));
            else
                assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                        "Patinagem de Velocidade", code, five));
        }
    }
}
