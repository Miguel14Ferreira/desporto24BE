package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityStrengthTrainingResultsTest {
    private CommunityRoundEntryRequest member(String username, CommunityMatchResult result,
            String team, BigDecimal fakeScore, int goals) {
        return new CommunityRoundEntryRequest(username, team, result, goals, 0,
                false, false, null, null, null, null, fakeScore);
    }

    @Test void registersOnlyExistingStrengthTrainingWithoutInventedCompetition() {
        assertEquals("STRENGTH_TRAINING",
                CommunityRoundEventType.validate("Musculação", "STRENGTH_TRAINING"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Musculação", "STRENGTH_TRAINING"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Musculação", "STRENGTH_TRAINING"));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Musculação", "STRENGTH_TRAINING",
                List.of(member("ana", CommunityMatchResult.DRAW, null, null, 0))));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Musculação", "STRENGTH_TRAINING",
                List.of(member("ana", CommunityMatchResult.DRAW, null, null, 0),
                        member("bruno", CommunityMatchResult.DRAW, null, null, 0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Futebol", "STRENGTH_TRAINING"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Musculação", null));
    }

    @Test void rejectsCompetitionResultsOtherSportMetricsOrDuplicateParticipants() {
        var valid = member("ana", CommunityMatchResult.DRAW, null, null, 0);
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Musculação", "STRENGTH_TRAINING", List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Yoga", "STRENGTH_TRAINING", List.of(valid)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Musculação", "STRENGTH_TRAINING",
                        List.of(member("ana", CommunityMatchResult.WIN, null, null, 0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Musculação", "STRENGTH_TRAINING",
                        List.of(member("ana", CommunityMatchResult.LOSS, null, null, 0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Musculação", "STRENGTH_TRAINING",
                        List.of(member("ana", CommunityMatchResult.DRAW, "A", null, 0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Musculação", "STRENGTH_TRAINING",
                        List.of(member("ana", CommunityMatchResult.DRAW, null, null, 3))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Musculação", "STRENGTH_TRAINING",
                        List.of(member("ana", CommunityMatchResult.DRAW, null, BigDecimal.ONE, 0))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Musculação", "STRENGTH_TRAINING",
                        List.of(valid, member(" ANA ", CommunityMatchResult.DRAW, null, null, 0))));
    }
}
