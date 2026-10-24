package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityOrienteeringResultsTest {
    private CommunityRoundEntryRequest runner(String name, String seconds,
            CommunityMatchResult result, Integer place, String team, Integer goals) {
        return new CommunityRoundEntryRequest(name, team, result, goals, 0, false, false,
                place, seconds == null ? null : new BigDecimal(seconds), null, null);
    }
    private CommunityRoundEntryRequest elapsed(String name, String seconds) {
        return runner(name, seconds, CommunityMatchResult.DRAW, null, null, 0);
    }

    @Test void usesThreeStandardFootFormatsWithoutInventingCourseDistance() {
        assertEquals("FOOT_ORIENTEERING",
                CommunityRoundEventType.validate("Orientação", "FOOT_ORIENTEERING"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Orientação", null));
        for (String format : List.of("ORIENT_SPRINT", "ORIENT_MIDDLE", "ORIENT_LONG"))
            assertEquals(format, CommunityFieldDisciplines.validate(
                    "Orientação", "FOOT_ORIENTEERING", format));
        for (String invalid : List.of("ORIENT_RELAY", "ORIENT_KNOCKOUT", "OWS_5K", "ORIENT_5KM"))
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityFieldDisciplines.validate(
                            "Orientação", "FOOT_ORIENTEERING", invalid));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityFieldDisciplines.validate(
                        "Orientação", "FOOT_ORIENTEERING", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Corrida", "FOOT_ORIENTEERING"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Orientação", "FOOT_ORIENTEERING"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Orientação", null));
        assertFalse(CommunityRoundEventType.isNeutralEvent("Orientação", "FOOT_ORIENTEERING"));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent(
                "Orientação", "FOOT_ORIENTEERING"));
    }

    @Test void savesObservedWholeSecondTimesAndUnrecordedParticipants() {
        var entries = List.of(elapsed("ana", "735"), elapsed("bruno", "801"), elapsed("rui", null));
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate(
                "Orientação", "FOOT_ORIENTEERING", entries));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Orientação", "FOOT_ORIENTEERING", entries));
    }

    @Test void rejectsPlaceholdersThatWouldCreateFalseResults() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Orientação", "FOOT_ORIENTEERING", List.of()));
        for (var invalid : List.of(
                elapsed("ana", "750.1"), elapsed("ana", "-5"), elapsed("ana", "0"),
                runner("ana", "750", CommunityMatchResult.WIN, null, null, 0),
                runner("ana", "750", CommunityMatchResult.LOSS, null, null, 0),
                runner("ana", "750", CommunityMatchResult.DRAW, 1, null, 0),
                runner("ana", "750", CommunityMatchResult.DRAW, null, "Team A", 0),
                runner("ana", "750", CommunityMatchResult.DRAW, null, null, 1))) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            "Orientação", "FOOT_ORIENTEERING", List.of(invalid)));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Orientação", "FOOT_ORIENTEERING",
                        List.of(elapsed("ana", "735"), elapsed(" ANA ", "801"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Natação", "FOOT_ORIENTEERING",
                        List.of(elapsed("ana", "735"))));
    }
}
