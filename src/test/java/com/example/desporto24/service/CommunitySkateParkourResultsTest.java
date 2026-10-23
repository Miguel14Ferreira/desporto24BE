package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommunitySkateParkourResultsTest {
    private BigDecimal decimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    private CommunityRoundEntryRequest record(String value, String elapsed, Integer place,
                                              Integer laps, String bestLap) {
        return new CommunityRoundEntryRequest("skater", null, CommunityMatchResult.DRAW,
                0, 0, false, false, place, decimal(elapsed), decimal(bestLap), laps,
                decimal(value), null, null);
    }

    private void accepted(String sport, String type, CommunityRoundEntryRequest entry) {
        assertDoesNotThrow(() ->
                CommunityRaceMetrics.validate(sport, type, List.of(entry)));
        assertDoesNotThrow(() ->
                CommunityRoundEventType.validateEntries(sport, type, List.of(entry)));
    }

    private void rejected(String sport, String type, CommunityRoundEntryRequest entry) {
        assertThrows(IllegalArgumentException.class, () -> {
            CommunityRaceMetrics.validate(sport, type, List.of(entry));
            CommunityRoundEventType.validateEntries(sport, type, List.of(entry));
        });
    }

    @Test void sportEventTypesStayIndependent() {
        assertEquals("SKATE_PARK", CommunityRoundEventType.validate("Skate", "SKATE_PARK"));
        assertEquals("SKATE_STREET", CommunityRoundEventType.validate("Skate", "SKATE_STREET"));
        assertEquals("PARKOUR_SPEED", CommunityRoundEventType.validate("Parkour", "PARKOUR_SPEED"));
        assertEquals("PARKOUR_FREESTYLE",
                CommunityRoundEventType.validate("Parkour", "PARKOUR_FREESTYLE"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Skate", "PARKOUR_SPEED"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Parkour", "SKATE_PARK"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Skate", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Parkour", null));
        assertFalse(CommunityRoundEventType.isRaceEvent("Skate", "SKATE_STREET"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Parkour", "PARKOUR_SPEED"));
        assertFalse(CommunityRoundEventType.isNeutralEvent("Parkour", "PARKOUR_SPEED"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Parkour", "PARKOUR_FREESTYLE"));
    }

    @Test void skateParkScoresAreManualAndBounded() {
        accepted("Skate", "SKATE_PARK", record("0", null, null, null, null));
        accepted("Skate", "SKATE_PARK", record("99.875", null, null, null, null));
        accepted("Skate", "SKATE_PARK", record(null, null, null, null, null));
        rejected("Skate", "SKATE_PARK", record("-0.001", null, null, null, null));
        rejected("Skate", "SKATE_PARK", record("100.001", null, null, null, null));
        rejected("Skate", "SKATE_PARK", record("80.0001", null, null, null, null));
        rejected("Skate", "SKATE_PARK", record("90", "5.100", null, null, null));
    }

    @Test void streetAcceptsOrganizerTotalWithoutCalculatingRunsAndTricks() {
        accepted("Skate", "SKATE_STREET", record("275.500", null, null, null, null));
        accepted("Skate", "SKATE_STREET", record("300", null, null, null, null));
        accepted("Skate", "SKATE_STREET", record(null, null, null, null, null));
        rejected("Skate", "SKATE_STREET", record("300.001", null, null, null, null));
        rejected("Skate", "SKATE_STREET", record("-1", null, null, null, null));
        rejected("Skate", "SKATE_STREET", record("245.2345", null, null, null, null));
    }

    @Test void parkourSpeedRecordsElapsedSecondsWithoutTrackLaps() {
        accepted("Parkour", "PARKOUR_SPEED", record(null, "24.568", 1, null, null));
        accepted("Parkour", "PARKOUR_SPEED", record(null, null, null, null, null));
        rejected("Parkour", "PARKOUR_SPEED", record("10", "24", 1, null, null));
        rejected("Parkour", "PARKOUR_SPEED", record(null, "24", 1, 2, null));
        rejected("Parkour", "PARKOUR_SPEED", record(null, "24", 1, null, "23"));
        rejected("Parkour", "PARKOUR_SPEED", record(null, "0", 1, null, null));
        rejected("Parkour", "PARKOUR_SPEED", record(null, "24", 0, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRaceMetrics.validate("Parkour", "PARKOUR_SPEED",
                        List.of(record(null, "10", 1, null, null),
                                record(null, "12", 1, null, null))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Parkour", "PARKOUR_SPEED",
                        List.of(new CommunityRoundEntryRequest("athlete", "A",
                                CommunityMatchResult.WIN, 0, 0, false, false,
                                1, decimal("24.5"), null, null, null, null, null))));
    }

    @Test void parkourFreestyleStoresOnlyAnEnteredFinalScore() {
        accepted("Parkour", "PARKOUR_FREESTYLE", record("75.250", null, null, null, null));
        accepted("Parkour", "PARKOUR_FREESTYLE", record(null, null, null, null, null));
        rejected("Parkour", "PARKOUR_FREESTYLE", record("-1", null, null, null, null));
        rejected("Parkour", "PARKOUR_FREESTYLE", record("101", null, null, null, null));
        rejected("Parkour", "PARKOUR_FREESTYLE", record("60", "35", null, null, null));
        rejected("Futebol", null, record("65", null, null, null, null));
    }
}
