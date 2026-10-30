package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityInlineSpeedResultsTest {
    private CommunityRoundEntryRequest skater(String username, String seconds,
            CommunityMatchResult result, Integer place, String team, int goals) {
        return new CommunityRoundEntryRequest(username, team, result, goals, 0, false, false,
                place, seconds == null ? null : new BigDecimal(seconds), null, null);
    }
    private CommunityRoundEntryRequest timed(String username, String seconds) {
        return skater(username, seconds, CommunityMatchResult.DRAW, null, null, 0);
    }
    @Test void existingInlineSpeedSportUsesExplicit200mTrackEventNotIce() {
        assertEquals("INLINE_TRACK_200_TT", CommunityRoundEventType.validate(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Patinagem de Velocidade", null));
        for (String unrelated : List.of("Patinagem", "Patinagem Artística", "Hóquei em Patins")) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validate(unrelated, "INLINE_TRACK_200_TT"));
        }
        for (String unsupported : List.of("INLINE_TRACK_500_SPRINT", "ICE_SPEED_500", "INLINE_TRACK_POINTS"))
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validate("Patinagem de Velocidade", unsupported));
        assertTrue(CommunityRoundEventType.isRaceEvent("Patinagem de Velocidade", null));
        assertTrue(CommunityRoundEventType.isRaceEvent(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT"));
        assertFalse(CommunityRoundEventType.isNeutralEvent(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT"));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT"));
        assertNull(CommunityFieldDisciplines.validate(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityFieldDisciplines.validate(
                        "Patinagem de Velocidade", "INLINE_TRACK_200_TT", "ICE_500"));
    }
    @Test void acceptsHundredthSecondTimesAndUntimedParticipants() {
        var entries = List.of(timed("ana", "18.43"), timed("bruno", "19.00"), timed("carla", null));
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT", entries));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT", entries));
    }
    @Test void rejectsInventedPlacingsCrossSportStatsAndSubHundredths() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Patinagem de Velocidade", "INLINE_TRACK_200_TT", List.of()));
        for (var entry : List.of(
                timed("ana", "18.432"), timed("ana", "0"), timed("ana", "-1"),
                skater("ana", "18.43", CommunityMatchResult.WIN, null, null, 0),
                skater("ana", "18.43", CommunityMatchResult.LOSS, null, null, 0),
                skater("ana", "18.43", CommunityMatchResult.DRAW, 1, null, 0),
                skater("ana", "18.43", CommunityMatchResult.DRAW, null, "A", 0),
                skater("ana", "18.43", CommunityMatchResult.DRAW, null, null, 1)))
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            "Patinagem de Velocidade", "INLINE_TRACK_200_TT", List.of(entry)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Patinagem de Velocidade", "INLINE_TRACK_200_TT",
                        List.of(timed("ana", "18.43"), timed(" ANA ", "18.82"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Patinagem", "INLINE_TRACK_200_TT", List.of(timed("ana", "18.43"))));
    }

    @Test void firstRoundHeatsOnTrackAndRoadKeepSeparateTimedContracts() {
        var entered = List.of(timed("ana", "45.37"), timed("bruno", "45.98"),
                timed("sem-tempo", null));
        for (String type : List.of("INLINE_TRACK_500D_FIRST_ROUND",
                "INLINE_ROAD_LAP_FIRST_ROUND")) {
            assertTrue(CommunityInlineSpeedResults.isRecordedTimeEvent(type));
            assertEquals(type, CommunityRoundEventType.validate(
                    "Patinagem de Velocidade", type));
            assertTrue(CommunityRoundEventType.isRaceEvent("Patinagem de Velocidade", type));
            assertFalse(CommunityRoundEventType.isNeutralEvent("Patinagem de Velocidade", type));
            assertFalse(CommunityRoundEventType.isChampionshipRaceEvent(
                    "Patinagem de Velocidade", type));
            assertDoesNotThrow(() -> CommunityRaceMetrics.validate(
                    "Patinagem de Velocidade", type, entered));
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                    "Patinagem de Velocidade", type, entered));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            "Patinagem de Velocidade", type,
                            List.of(skater("ana", "45.37", CommunityMatchResult.DRAW, 1, null, 0))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            "Patinagem de Velocidade", type,
                            List.of(timed("ana", "45.374"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            "Patinagem de Velocidade", type,
                            List.of(timed("ana", "45.37"), timed(" ANA ", "47.00"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validate("Patinagem Artística", type));
        }
        assertFalse(CommunityInlineSpeedResults.isRecordedTimeEvent("INLINE_TRACK_POINTS"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate(
                        "Patinagem de Velocidade", "INLINE_TRACK_500D_FINAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate(
                        "Patinagem de Velocidade", "INLINE_ROAD_LAP_FINAL"));
        assertTrue(CommunityInlineSpeedResults.isRecordedTimeEvent("INLINE_TRACK_200_TT"));
    }
}
