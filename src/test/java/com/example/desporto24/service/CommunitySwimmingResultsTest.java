package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunitySwimmingResultsTest {
    private CommunityRoundEntryRequest swimmer(String name, String seconds,
            CommunityMatchResult result, Integer place, String team, int goals) {
        return new CommunityRoundEntryRequest(name, team, result, goals, 0, false, false,
                place, seconds == null ? null : new BigDecimal(seconds), null, null);
    }
    private CommunityRoundEntryRequest timed(String name, String seconds) {
        return swimmer(name, seconds, CommunityMatchResult.DRAW, null, null, 0);
    }

    @Test void confirmsSevenDistinctIndividual50mPoolEvents() {
        assertEquals("SWIM_LCM", CommunityRoundEventType.validate("Natação", "SWIM_LCM"));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType.validate("Natação", null));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType.validate("Natação Artística", "SWIM_LCM"));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType.validate("Natação em Águas Abertas", "SWIM_LCM"));
        for (String code : List.of("FREE_50", "FREE_100", "FREE_200", "BACK_100",
                "BREAST_100", "FLY_100", "IM_200")) {
            assertEquals(code, CommunityFieldDisciplines.validate("Natação", "SWIM_LCM", code));
        }
        for (String invalid : List.of("FREE_25", "BACK_50", "IM_100", "RELAY_100", "FREE_1500")) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityFieldDisciplines.validate("Natação", "SWIM_LCM", invalid), invalid);
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityFieldDisciplines.validate("Natação", "SWIM_LCM", null));
        assertFalse(CommunityRoundEventType.isNeutralEvent("Natação", "SWIM_LCM"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Natação", "SWIM_LCM"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Natação", null)); // untouched legacy
    }

    @Test void recordsRealHundredthTimesAndOptionalUnrecordedParticipants() {
        var entries = List.of(timed("ana", "28.47"), timed("bruno", "29.11"), timed("carla", null));
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate("Natação", "SWIM_LCM", entries));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Natação", "SWIM_LCM", entries));
    }

    @Test void rejectsInventedResultsAndOtherSportMeasurements() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Natação", "SWIM_LCM", List.of()));
        for (var invalid : List.of(
                swimmer("ana", "27.42", CommunityMatchResult.WIN, null, null, 0),
                swimmer("ana", "27.42", CommunityMatchResult.LOSS, null, null, 0),
                swimmer("ana", "27.42", CommunityMatchResult.DRAW, 1, null, 0),
                swimmer("ana", "27.42", CommunityMatchResult.DRAW, null, "A", 0),
                swimmer("ana", "27.42", CommunityMatchResult.DRAW, null, null, 2),
                timed("ana", "27.423"),
                timed("ana", "-1"),
                timed("ana", "0"))) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Natação", "SWIM_LCM", List.of(invalid)));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Natação", "SWIM_LCM",
                        List.of(timed("ana", "28.42"), timed(" ANA ", "29.40"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Natação em Águas Abertas", "SWIM_LCM",
                        List.of(timed("ana", "28.42"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRaceMetrics.validate("Futebol", "SWIM_LCM",
                        List.of(timed("ana", "28.42"))));
    }
}
