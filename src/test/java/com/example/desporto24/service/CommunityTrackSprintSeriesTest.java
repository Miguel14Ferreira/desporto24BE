package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintSeriesTest {
    private static CommunityRoundEntryRequest entry(String name, CommunityMatchResult result) {
        return new CommunityRoundEntryRequest(name, null, result, 0, 0, false, false);
    }
    private static List<CommunityRoundEntryRequest> riders() {
        return List.of(entry("ana", CommunityMatchResult.WIN), entry("rui", CommunityMatchResult.LOSS));
    }
    private static CommunityTrackSprintSeries.Heat heat(String phase, int number, String winner) {
        return new CommunityTrackSprintSeries.Heat(phase, number, "ana", "rui", winner);
    }
    private static void valid(String phase, String key, int number, CommunityTrackSprintSeries.Heat... prior) {
        CommunityTrackSprintSeries.validate("TRACK_SPRINT_HEAT", phase, key, number, List.of(prior), riders());
    }

    @Test void unrelatedAndOldRecordsStayIndependent() {
        assertDoesNotThrow(() -> CommunityTrackSprintSeries.validate(
                "TRACK_SCRATCH", null, null, null, List.of(), riders()));
        assertDoesNotThrow(() -> CommunityTrackSprintSeries.validate(
                "TRACK_SPRINT_HEAT", "SPRINT_SEMI_FINAL", null, null, List.of(), riders()));
        assertThrows(IllegalArgumentException.class, () -> CommunityTrackSprintSeries.validate(
                "TRACK_SCRATCH", null, "S1", 1, List.of(), riders()));
        assertThrows(IllegalArgumentException.class, () -> CommunityTrackSprintSeries.validate(
                "TRACK_SPRINT_HEAT", "SPRINT_SEMI_FINAL", "S1", null, List.of(), riders()));
    }

    @Test void semifinalEndsAtTwoWinsWithoutUnneededThirdHeat() {
        var one = heat("SPRINT_SEMI_FINAL", 1, "ana");
        var two = heat("SPRINT_SEMI_FINAL", 2, "ana");
        valid("SPRINT_SEMI_FINAL", "Semi 1", 1);
        valid("SPRINT_SEMI_FINAL", "Semi 1", 2, one);
        assertEquals("ana", CommunityTrackSprintSeries.seriesWinner(
                "SPRINT_SEMI_FINAL", List.of(one, two)));
        assertThrows(IllegalArgumentException.class,
                () -> valid("SPRINT_SEMI_FINAL", "Semi 1", 3, one, two));
    }

    @Test void thirdHeatIsOnlyAllowedAtOneOneAndResolvesSeries() {
        var one = heat("SPRINT_GOLD_FINAL", 1, "ana");
        var two = heat("SPRINT_GOLD_FINAL", 2, "rui");
        valid("SPRINT_GOLD_FINAL", "Gold 1", 3, one, two);
        assertNull(CommunityTrackSprintSeries.seriesWinner(
                "SPRINT_GOLD_FINAL", List.of(one, two)));
        assertEquals("ana", CommunityTrackSprintSeries.seriesWinner(
                "SPRINT_GOLD_FINAL", List.of(one, two, heat("SPRINT_GOLD_FINAL", 3, "ana"))));
        assertThrows(IllegalArgumentException.class,
                () -> valid("SPRINT_GOLD_FINAL", "Gold 1", 2, one, two));
    }

    @Test void quarterfinalIsAlsoBestOfThreePerUci2026() {
        var one = heat("SPRINT_QUARTER_FINAL", 1, "rui");
        var two = heat("SPRINT_QUARTER_FINAL", 2, "rui");
        valid("SPRINT_QUARTER_FINAL", "Quarter A", 1);
        valid("SPRINT_QUARTER_FINAL", "Quarter A", 2, one);
        assertNull(CommunityTrackSprintSeries.seriesWinner("SPRINT_QUARTER_FINAL", List.of(one)));
        assertEquals("rui", CommunityTrackSprintSeries.seriesWinner(
                "SPRINT_QUARTER_FINAL", List.of(one, two)));
        assertThrows(IllegalArgumentException.class,
                () -> valid("SPRINT_QUARTER_FINAL", "Quarter A", 3, one, two));
        var split = heat("SPRINT_QUARTER_FINAL", 2, "ana");
        valid("SPRINT_QUARTER_FINAL", "Quarter B", 3, one, split);
        assertEquals("ana", CommunityTrackSprintSeries.seriesWinner(
                "SPRINT_QUARTER_FINAL", List.of(one, split,
                        heat("SPRINT_QUARTER_FINAL", 3, "ana"))));
    }

    @Test void rejectMixedPairPhasesAndNonconsecutiveHeats() {
        var one = heat("SPRINT_SEMI_FINAL", 1, "ana");
        assertThrows(IllegalArgumentException.class,
                () -> valid("SPRINT_SEMI_FINAL", "Semi 1", 3, one));
        assertThrows(IllegalArgumentException.class,
                () -> valid("SPRINT_BRONZE_FINAL", "Semi 1", 2, one));
        assertThrows(IllegalArgumentException.class,
                () -> valid("SPRINT_SEMI_FINAL", "Semi 1", 2,
                        new CommunityTrackSprintSeries.Heat("SPRINT_SEMI_FINAL", 1, "ana", "bob", "ana")));
        assertThrows(IllegalArgumentException.class,
                () -> valid("SPRINT_SEMI_FINAL", "a/b", 1));
    }
}
