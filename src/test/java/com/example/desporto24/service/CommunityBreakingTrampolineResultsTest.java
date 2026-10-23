package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityBreakingTrampolineResultsTest {
    private static CommunityRoundEntryRequest participant(String id, CommunityMatchResult outcome,
                                                           String score, String team,
                                                           int goals) {
        return new CommunityRoundEntryRequest(id, team, outcome, goals, 0, false, false,
                null, null, null, null, score == null ? null : new BigDecimal(score),
                null, null);
    }

    @Test void eventCodesDoNotLeakIntoOtherSportsOrLegacyRounds() {
        assertEquals("BREAKING_BATTLE", CommunityRoundEventType.validate(
                "Breaking", "BREAKING_BATTLE"));
        assertEquals("TRAMPOLINE_INDIVIDUAL", CommunityRoundEventType.validate(
                "Trampolim", "TRAMPOLINE_INDIVIDUAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Breaking", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Trampolim", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Skate", "BREAKING_BATTLE"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Ginástica Artística", "TRAMPOLINE_INDIVIDUAL"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Breaking", "BREAKING_BATTLE"));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Trampolim", "TRAMPOLINE_INDIVIDUAL"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Breaking", "BREAKING_BATTLE"));
    }

    @Test void breakingBattleRecordsOnlyTheJudgesFinalDecisionForExactlyTwoParticipants() {
        var winner = participant("one", CommunityMatchResult.WIN, null, null, 0);
        var loser = participant("two", CommunityMatchResult.LOSS, null, null, 0);
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Breaking", "BREAKING_BATTLE", List.of(winner, loser)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Breaking", "BREAKING_BATTLE", List.of(winner)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Breaking", "BREAKING_BATTLE",
                        List.of(winner, participant("two", CommunityMatchResult.WIN, null, null, 0))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Breaking", "BREAKING_BATTLE",
                        List.of(winner, participant("two", CommunityMatchResult.DRAW, null, null, 0))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Breaking", "BREAKING_BATTLE",
                        List.of(winner, loser, participant("three", CommunityMatchResult.LOSS, null, null, 0))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Breaking", "BREAKING_BATTLE",
                        List.of(participant("one", CommunityMatchResult.WIN, "20", null, 0), loser)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Breaking", "BREAKING_BATTLE",
                        List.of(participant("one", CommunityMatchResult.WIN, null, "A", 0), loser)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Breaking", "BREAKING_BATTLE",
                        List.of(participant("one", CommunityMatchResult.WIN, null, null, 1), loser)));
    }

    @Test void trampolineRecordsFinalScoreOnlyAndRejectsUnrelatedStats() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Trampolim",
                "TRAMPOLINE_INDIVIDUAL", List.of(
                        participant("one", CommunityMatchResult.DRAW, "54.25", null, 0),
                        participant("two", CommunityMatchResult.DRAW, null, null, 0))));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Trampolim",
                "TRAMPOLINE_INDIVIDUAL", List.of(
                        participant("one", CommunityMatchResult.DRAW, "0", null, 0))));
        for (String invalid : List.of("-0.01", "10000.00", "55.234")) {
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityRoundEventType.validateEntries("Trampolim", "TRAMPOLINE_INDIVIDUAL",
                            List.of(participant("one", CommunityMatchResult.DRAW, invalid, null, 0))));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Trampolim", "TRAMPOLINE_INDIVIDUAL",
                        List.of(participant("one", CommunityMatchResult.WIN, "54", null, 0))));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validateEntries("Futebol", null,
                        List.of(participant("one", CommunityMatchResult.DRAW, "54", null, 0))));
    }
}
