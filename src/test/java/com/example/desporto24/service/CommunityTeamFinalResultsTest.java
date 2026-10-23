package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityTeamFinalScore;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTeamFinalResultsTest {
    private CommunityRoundEntryRequest athlete(String name, String team) {
        return new CommunityRoundEntryRequest(name, team, CommunityMatchResult.DRAW,
                0, 0, false, false);
    }
    private final List<CommunityRoundEntryRequest> players =
            List.of(athlete("a", "A"), athlete("b", "B"));
    private CommunityTeamFinalScore score(int a, int b) {
        return new CommunityTeamFinalScore(a, b);
    }

    @Test void handballUsesGoalsAndCanEndLevel() {
        assertEquals("HANDBALL_FINAL", CommunityRoundEventType.validate("Andebol", "HANDBALL_FINAL"));
        assertEquals(score(28, 28), CommunityTeamFinalResults.validate(
                "Andebol", "HANDBALL_FINAL", players, score(28, 28)));
        assertEquals(score(31, 29), CommunityTeamFinalResults.validate(
                "Andebol", "HANDBALL_FINAL", players, score(31, 29)));
    }
    @Test void rugbyAndSevensUseOnlyConfirmedFinalPoints() {
        for (String[] pair : List.of(new String[]{"Rugby", "RUGBY_FINAL"},
                new String[]{"Rugby Sevens", "RUGBY_SEVENS_FINAL"})) {
            assertEquals(pair[1], CommunityRoundEventType.validate(pair[0], pair[1]));
            assertEquals(score(24, 24), CommunityTeamFinalResults.validate(
                    pair[0], pair[1], players, score(24, 24)));
            assertEquals(score(19, 12), CommunityTeamFinalResults.validate(
                    pair[0], pair[1], players, score(19, 12)));
        }
    }
    @Test void fourGoalTeamSportsUseDedicatedEventTypesAndAllowDrawnScores() {
        for (String[] pair : List.of(
                new String[]{"Futsal", "FUTSAL_FINAL"},
                new String[]{"Hóquei em Campo", "FIELD_HOCKEY_FINAL"},
                new String[]{"Hóquei em Patins", "ROLLER_HOCKEY_FINAL"},
                new String[]{"Polo Aquático", "WATER_POLO_FINAL"})) {
            assertEquals(pair[1], CommunityRoundEventType.validate(pair[0], pair[1]));
            assertEquals(score(0, 0), CommunityTeamFinalResults.validate(
                    pair[0], pair[1], players, score(0, 0)));
            assertEquals(score(5, 3), CommunityTeamFinalResults.validate(
                    pair[0], pair[1], players, score(5, 3)));
            assertEquals(score(8, 8), CommunityTeamFinalResults.validate(
                    pair[0], pair[1], players, score(8, 8)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(pair[0], "HANDBALL_FINAL", players, score(1, 1)));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Futsal", "FUTSAL_FINAL", players, score(1000, 0)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Futsal", "FUTSAL_FINAL",
                    List.of(athlete("a", "A")), score(2, 1)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Hóquei no Gelo", "FIELD_HOCKEY_FINAL"));
    }
    @Test void iceHockeySeparatesRegulationOvertimeAndShootoutOfficialScores() {
        for (String type : List.of("ICE_HOCKEY_REG", "ICE_HOCKEY_OT", "ICE_HOCKEY_SO")) {
            assertTrue(type.length() <= 20, "Existing event_type storage is VARCHAR(20)");
            assertEquals(type, CommunityRoundEventType.validate("Hóquei no Gelo", type));
            assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Hóquei no Gelo", type));
        }
        assertEquals(score(2, 2), CommunityTeamFinalResults.validate(
                "Hóquei no Gelo", "ICE_HOCKEY_REG", players, score(2, 2)));
        assertEquals(score(4, 2), CommunityTeamFinalResults.validate(
                "Hóquei no Gelo", "ICE_HOCKEY_REG", players, score(4, 2)));
        for (String decided : List.of("ICE_HOCKEY_OT", "ICE_HOCKEY_SO")) {
            assertEquals(score(3, 2), CommunityTeamFinalResults.validate(
                    "Hóquei no Gelo", decided, players, score(3, 2)));
            assertEquals(score(0, 1), CommunityTeamFinalResults.validate(
                    "Hóquei no Gelo", decided, players, score(0, 1)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(
                            "Hóquei no Gelo", decided, players, score(2, 2)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(
                            "Hóquei no Gelo", decided, players, score(5, 2)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(
                            "Hóquei no Gelo", decided, List.of(athlete("a", "A")), score(3, 2)));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Hóquei em Patins",
                        "ICE_HOCKEY_REG", players, score(2, 2)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Hóquei", "ICE_HOCKEY_OT"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Hóquei no Gelo", "FIELD_HOCKEY_FINAL"));
    }
    @Test void baseballAndSoftballUseRunsNotFootballGoalsOrSeasonPoints() {
        for (String[] pair : List.of(
                new String[]{"Basebol", "BASEBALL_RUNS"},
                new String[]{"Softbol", "SOFTBALL_RUNS"})) {
            String sport = pair[0], type = pair[1];
            assertTrue(type.length() <= 20, "Existing event_type VARCHAR(20)");
            assertEquals(type, CommunityRoundEventType.validate(sport, type));
            assertTrue(CommunityRoundEventType.isNeutralEvent(sport, type));
            assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
            for (CommunityTeamFinalScore runs : List.of(score(0, 0),
                    score(7, 4), score(2, 11), score(5, 5))) {
                assertEquals(runs, CommunityTeamFinalResults.validate(
                        sport, type, players, runs));
            }
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type, players, score(-1, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type, players, score(1000, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type, List.of(athlete("a","A")), score(3, 2)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type,
                            List.of(new CommunityRoundEntryRequest("a","A",CommunityMatchResult.WIN,
                                    0,0,false,false),athlete("b","B")), score(3,2)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityRoundEventType.validate(sport, "FUTSAL_FINAL"));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Basebol", "SOFTBALL_RUNS"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Softbol", "BASEBALL_RUNS"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Basebol", "SOFTBALL_RUNS",players,score(4,3)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Softbol", "BASEBALL_RUNS",players,score(4,3)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Futebol", "BASEBALL_RUNS",players,score(1,0)));
    }
    @Test void doesNotAcceptCrossSportOrFootballMetricOrUnrelatedScores() {
        assertThrows(IllegalArgumentException.class, () -> CommunityTeamFinalResults.validate(
                "Rugby", "HANDBALL_FINAL", players, score(20, 19)));
        assertThrows(IllegalArgumentException.class, () -> CommunityTeamFinalResults.validate(
                "Futebol", null, players, score(2, 0)));
        assertThrows(IllegalArgumentException.class, () -> CommunityTeamFinalResults.validate(
                "Andebol", "HANDBALL_FINAL", players, score(-1, 10)));
        assertThrows(IllegalArgumentException.class, () -> CommunityTeamFinalResults.validate(
                "Andebol", "HANDBALL_FINAL", players, score(1000, 5)));
        assertThrows(IllegalArgumentException.class, () -> CommunityTeamFinalResults.validate(
                "Andebol", "HANDBALL_FINAL", List.of(athlete("a", "A")), score(12, 9)));
        assertEquals(score(0, 0), CommunityTeamFinalResults.decode(0, 0));
        assertNull(CommunityTeamFinalResults.decode(null, null));
    }
}
