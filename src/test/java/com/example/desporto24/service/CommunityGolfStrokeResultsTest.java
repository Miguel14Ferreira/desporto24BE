package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityGolfStrokeResultsTest {
    private CommunityRoundEntryRequest strokes(String player, String value) {
        return new CommunityRoundEntryRequest(player, null, CommunityMatchResult.DRAW,
                0, 0, false, false, null, null, null, null,
                value == null ? null : new BigDecimal(value));
    }

    @Test void onlyClosedCatalogueGolfSupportsExplicit9And18HoleGrossStrokePlay() {
        assertEquals("GOLF_STROKE_9", CommunityRoundEventType.validate("Golfe", "GOLF_STROKE_9"));
        assertEquals("GOLF_STROKE_18", CommunityRoundEventType.validate("Golfe", "GOLF_STROKE_18"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Golfe", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Cricket", "GOLF_STROKE_9"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Golfe", "GOLF_STABLEFORD"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Golfe", "GOLF_STROKE_18"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Golfe", "GOLF_STROKE_18"));
    }

    @Test void acceptsOrganizerConfirmedGrossTotalsAndTies() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Golfe",
                "GOLF_STROKE_18", List.of(strokes("one", "70"), strokes("two", "71"),
                        strokes("three", "70"))));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Golfe",
                "GOLF_STROKE_9", List.of(strokes("one", "35"), strokes("two", "35"))));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Golfe",
                "GOLF_STROKE_18", List.of(strokes("one", "18"))));
    }

    @Test void rejectsMissingPartialDecimalOrImpossibleGrossTotals() {
        for (String bad : new String[] {null,"0","8","17.5","-2","9999999"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Golfe",
                            "GOLF_STROKE_18", List.of(strokes("one", bad))), "invalid "+bad);
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Golfe", "GOLF_STROKE_9",
                        List.of(strokes("one", "8"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Futebol", "GOLF_STROKE_9",
                        List.of(strokes("one", "34"))));
    }

    @Test void rejectsUnrelatedTeamFootballOrRaceMetrics() {
        var football = new CommunityRoundEntryRequest("one", "A", CommunityMatchResult.WIN,
                2, 0, true, false, null, null, null, null, new BigDecimal("70"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Golfe",
                        "GOLF_STROKE_18", List.of(football)));
        var race = new CommunityRoundEntryRequest("one", null, CommunityMatchResult.DRAW,
                0, 0, false, false, 1, null, null, null, new BigDecimal("70"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Golfe",
                        "GOLF_STROKE_18", List.of(race)));
    }
}
