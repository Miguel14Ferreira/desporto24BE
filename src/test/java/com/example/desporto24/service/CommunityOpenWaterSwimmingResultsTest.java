package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityOpenWaterSwimmingResultsTest {
    private CommunityRoundEntryRequest participant(String name, String seconds,
            Integer place, CommunityMatchResult result, String team, int goals) {
        return new CommunityRoundEntryRequest(name, team, result, goals, 0,
                false, false, place,
                seconds == null ? null : new BigDecimal(seconds), null, null);
    }

    private CommunityRoundEntryRequest timed(String name, String seconds) {
        return participant(name, seconds, null, CommunityMatchResult.DRAW, null, 0);
    }

    @Test void registersOnlyIndividualFiveAndTenKilometres() {
        assertEquals("OWS_INDIVIDUAL",
                CommunityRoundEventType.validate("Natação em Águas Abertas", "OWS_INDIVIDUAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Natação em Águas Abertas", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Natação", "OWS_INDIVIDUAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Natação Artística", "OWS_INDIVIDUAL"));
        for (String distance : List.of("OWS_5K", "OWS_10K")) {
            assertEquals(distance, CommunityFieldDisciplines.validate(
                    "Natação em Águas Abertas", "OWS_INDIVIDUAL", distance));
        }
        for (String unsupported : List.of("OWS_3K_KNOCKOUT", "OWS_RELAY", "OWS_7_5K", "FREE_200")) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityFieldDisciplines.validate(
                            "Natação em Águas Abertas", "OWS_INDIVIDUAL", unsupported));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityFieldDisciplines.validate(
                        "Natação em Águas Abertas", "OWS_INDIVIDUAL", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityFieldDisciplines.validate(
                        "Natação", "SWIM_LCM", "OWS_5K"));
        assertTrue(CommunityRoundEventType.isRaceEvent(
                "Natação em Águas Abertas", "OWS_INDIVIDUAL"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Natação em Águas Abertas", null));
        assertFalse(CommunityRoundEventType.isRaceEvent("Natação Artística", "OWS_INDIVIDUAL"));
    }

    @Test void acceptsRecordedTenthsAndUnrecordedParticipantsWithoutRanks() {
        var entries = List.of(timed("ana", "3603.8"), timed("bruno", "3702.0"),
                timed("carla", null));
        assertDoesNotThrow(() -> CommunityRaceMetrics.validate(
                "Natação em Águas Abertas", "OWS_INDIVIDUAL", entries));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Natação em Águas Abertas", "OWS_INDIVIDUAL", entries));
    }

    @Test void rejectsUnverifiedPlacingsOtherSportsOrInvalidTimes() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Natação em Águas Abertas", "OWS_INDIVIDUAL", List.of()));
        for (var invalid : List.of(
                timed("ana", "3603.81"), timed("ana", "0"), timed("ana", "-2.1"),
                participant("ana", "3603.8", 1, CommunityMatchResult.DRAW, null, 0),
                participant("ana", "3603.8", null, CommunityMatchResult.WIN, null, 0),
                participant("ana", "3603.8", null, CommunityMatchResult.LOSS, null, 0),
                participant("ana", "3603.8", null, CommunityMatchResult.DRAW, "A", 0),
                participant("ana", "3603.8", null, CommunityMatchResult.DRAW, null, 1))) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries(
                            "Natação em Águas Abertas", "OWS_INDIVIDUAL", List.of(invalid)));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Natação em Águas Abertas", "OWS_INDIVIDUAL",
                        List.of(timed("ana", "3603.8"), timed(" ANA ", "3605.0"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Natação", "OWS_INDIVIDUAL", List.of(timed("ana", "3603.8"))));
    }
}
