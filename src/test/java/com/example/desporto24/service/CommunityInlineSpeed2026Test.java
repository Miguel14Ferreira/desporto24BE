package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommunityInlineSpeed2026Test {
    private static CommunityInlineSpeed2026.Skater timed(long id, double seconds) {
        return new CommunityInlineSpeed2026.Skater(id, BigDecimal.valueOf(seconds), null, false);
    }
    private static CommunityInlineSpeed2026.Skater placed(long id, int position) {
        return new CommunityInlineSpeed2026.Skater(id, null, position, false);
    }
    private static CommunityInlineSpeed2026.Heat first(long id, String type,
                                                       List<CommunityInlineSpeed2026.Skater> skaters) {
        return new CommunityInlineSpeed2026.Heat(id, type, 1, skaters);
    }
    @Test void twentyEntrantsQualifySixteenInTrack() {
        var skaters = new ArrayList<CommunityInlineSpeed2026.Skater>();
        for (int i = 1; i <= 20; i++) skaters.add(timed(i, 10 + i));
        var decision = CommunityInlineSpeed2026.propose(
                List.of(first(1, "INLINE_TRACK_500D_FIRST_ROUND", skaters)), "FIRST", "TRACK_500D");
        assertTrue(decision.ready());
        assertEquals("QUARTER", decision.targetStage());
        assertEquals(16, decision.candidatePlayerIds().size());
        assertEquals(1L, decision.candidatePlayerIds().get(0));
    }
    @Test void fewerThanSixteenBypassQuarterFinals() {
        var skaters = new ArrayList<CommunityInlineSpeed2026.Skater>();
        for (int i = 1; i <= 12; i++) skaters.add(timed(i, i + 30));
        var result = CommunityInlineSpeed2026.propose(
                List.of(first(2, "INLINE_TRACK_500D_FIRST_ROUND", skaters)), "FIRST", "TRACK_500D");
        assertEquals("SEMI", result.targetStage());
        assertEquals(8, result.candidatePlayerIds().size());
    }
    @Test void equalSixteenthAndSeventeenthNeedSeventeenAtQuarterFinal() {
        var skaters = new ArrayList<CommunityInlineSpeed2026.Skater>();
        for (int i = 1; i <= 18; i++) skaters.add(timed(i, i == 17 ? 25 : i + 9));
        // Rank 16 and rank 17 both 26.
        var result = CommunityInlineSpeed2026.propose(
                List.of(first(3, "INLINE_TRACK_500D_FIRST_ROUND", skaters)), "FIRST", "TRACK_500D");
        assertEquals(17, result.candidatePlayerIds().size());
    }
    @Test void allFourQuartersPromoteExactlyTopTwoFromEach() {
        var heats = new ArrayList<CommunityInlineSpeed2026.Heat>();
        for (int i = 1; i <= 4; i++) heats.add(new CommunityInlineSpeed2026.Heat(i,
                "INLINE_TRACK_500D_QUARTER_HEAT", i,
                List.of(placed(i * 10 + 1, 1), placed(i * 10 + 2, 2),
                        placed(i * 10 + 3, 3), placed(i * 10 + 4, 4))));
        var result = CommunityInlineSpeed2026.propose(heats, "QUARTER", "TRACK_500D");
        assertTrue(result.ready());
        assertEquals("SEMI", result.targetStage());
        assertEquals(List.of(11L, 12L, 21L, 22L, 31L, 32L, 41L, 42L),
                result.candidatePlayerIds());
    }
    @Test void twoSemisProduceSingleFinalOfFour() {
        var heats = List.of(
                new CommunityInlineSpeed2026.Heat(1, "INLINE_ROAD_LAP_SEMI_HEAT", 1,
                        List.of(placed(1, 1), placed(2, 2), placed(3, 3))),
                new CommunityInlineSpeed2026.Heat(2, "INLINE_ROAD_LAP_SEMI_HEAT", 2,
                        List.of(placed(4, 1), placed(5, 2), placed(6, 3))));
        var result = CommunityInlineSpeed2026.propose(heats, "SEMI", "ROAD_LAP");
        assertTrue(result.ready());
        assertEquals("FINAL", result.targetStage());
        assertEquals(List.of(1L, 2L, 4L, 5L), result.candidatePlayerIds());
    }
    @Test void duplicatesAndMissingHeatsBlockProgression() {
        var same = List.of(
                new CommunityInlineSpeed2026.Heat(1, "INLINE_TRACK_500D_SEMI_HEAT", 1,
                        List.of(placed(1, 1), placed(2, 2))),
                new CommunityInlineSpeed2026.Heat(2, "INLINE_TRACK_500D_SEMI_HEAT", 2,
                        List.of(placed(1, 1), placed(3, 2))));
        assertFalse(CommunityInlineSpeed2026.propose(same, "SEMI", "TRACK_500D").ready());
        assertFalse(CommunityInlineSpeed2026.propose(same.subList(0, 1), "SEMI", "TRACK_500D").ready());
    }
    @Test void firstRoundNeverAllowsMoreThanSixSkatersPerHeat() {
        var seven = new ArrayList<CommunityRoundEntryRequest>();
        for (int i = 1; i <= 7; i++) {
            seven.add(new CommunityRoundEntryRequest("skater" + i, null,
                    CommunityMatchResult.DRAW, null, null, false, false,
                    null, BigDecimal.valueOf(30 + i, 1), null, null));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityInlineSpeedResults.validate("Patinagem de Velocidade",
                        "INLINE_TRACK_500D_FIRST_ROUND", seven));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityInlineSpeedResults.validate("Patinagem de Velocidade",
                        "INLINE_ROAD_LAP_FIRST_ROUND", seven));
        assertDoesNotThrow(() -> CommunityInlineSpeedResults.validate(
                "Patinagem de Velocidade", "INLINE_TRACK_500D_FIRST_ROUND",
                seven.subList(0, 6)));
        // The 200m individual time trial does not have the sprint heat limit.
        assertDoesNotThrow(() -> CommunityInlineSpeedResults.validate(
                "Patinagem de Velocidade", "INLINE_TRACK_200_TT", seven));
    }

    @Test void historicalFinalBHasNoOfficial2026Stage() {
        assertNull(CommunityInlineSpeed2026.stage("INLINE_TRACK_500D_FINAL_B"));
        assertNull(CommunityInlineSpeed2026.stage("INLINE_ROAD_LAP_FINAL_B"));
        assertEquals("FINAL", CommunityInlineSpeed2026.stage("INLINE_TRACK_500D_FINAL_A"));
    }
    @Test void timingEditsInvalidateSourceFingerprint() {
        var type = "INLINE_TRACK_500D_FIRST_ROUND";
        var first = List.of(first(10, type, List.of(timed(1, 20))));
        var edited = List.of(first(10, type, List.of(timed(1, 20.01))));
        assertNotEquals(CommunityInlineSpeed2026.fingerprint(first),
                CommunityInlineSpeed2026.fingerprint(edited));
    }
}
