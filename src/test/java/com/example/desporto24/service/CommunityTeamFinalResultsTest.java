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
    @Test void korfballAndMensFieldLacrosseSeparateRegulationAndDecisiveGoal() {
        for (String[] format : List.of(
                new String[]{"Corfebol", "KORFBALL_REG", "KORFBALL_GOLDEN"},
                new String[]{"Lacrosse", "LACROSSE_M_REG", "LACROSSE_M_OT"})) {
            String sport = format[0], regulation = format[1], decision = format[2];
            for (String type : List.of(regulation, decision)) {
                assertTrue(type.length() <= 20, "Existing event_type VARCHAR(20)");
                assertEquals(type, CommunityRoundEventType.validate(sport, type));
                assertTrue(CommunityRoundEventType.isNeutralEvent(sport, type));
                assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
            }
            assertEquals(score(11, 11), CommunityTeamFinalResults.validate(
                    sport, regulation, players, score(11, 11)));
            assertEquals(score(15, 12), CommunityTeamFinalResults.validate(
                    sport, regulation, players, score(15, 12)));
            assertEquals(score(11, 12), CommunityTeamFinalResults.validate(
                    sport, decision, players, score(11, 12)));
            assertEquals(score(16, 15), CommunityTeamFinalResults.validate(
                    sport, decision, players, score(16, 15)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, decision, players, score(3, 3)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, decision, players, score(5, 3)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, regulation, players, score(-1, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, regulation, players, score(1000, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, regulation,
                        List.of(athlete("a", "A")), score(1, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, regulation,
                        List.of(new CommunityRoundEntryRequest("a", "A", CommunityMatchResult.WIN,
                                        1, 0, false, false), athlete("b", "B")), score(1, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityRoundEventType.validate(sport, "FUTSAL_FINAL"));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Corfebol", "LACROSSE_M_OT"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Lacrosse", "KORFBALL_GOLDEN"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Futebol", "KORFBALL_REG", players, score(3, 1)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Hóquei", "LACROSSE_M_REG", players, score(3, 1)));
    }
    @Test void americanAndFlagFootballUseConfirmedPointsNotFabricatedTouchdowns() {
        for (String[] pair : List.of(
                new String[]{"Futebol Americano", "AMFOOTBALL_SCORE"},
                new String[]{"Flag Football", "FLAGFOOTBALL_SCORE"})) {
            String sport = pair[0], type = pair[1];
            assertTrue(type.length() <= 20, "Existing event_type is VARCHAR(20)");
            assertEquals(type, CommunityRoundEventType.validate(sport, type));
            assertTrue(CommunityRoundEventType.isNeutralEvent(sport, type));
            assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
            for (CommunityTeamFinalScore points : List.of(
                    score(0, 0), score(7, 6), score(21, 14), score(32, 32), score(35, 41))) {
                assertEquals(points, CommunityTeamFinalResults.validate(
                        sport, type, players, points));
            }
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type, players, score(-1, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type, players, score(1000, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type, List.of(athlete("a", "A")), score(7, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate(sport, type,
                            List.of(new CommunityRoundEntryRequest("a", "A", CommunityMatchResult.WIN,
                                    1, 0, false, false), athlete("b", "B")), score(7, 0)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityRoundEventType.validate(sport, "FUTSAL_FINAL"));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Flag Football", "AMFOOTBALL_SCORE"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Futebol Americano", "FLAGFOOTBALL_SCORE"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Rugby", "FLAGFOOTBALL_SCORE", players, score(7, 0)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Futebol", "AMFOOTBALL_SCORE", players, score(7, 0)));
    }
    @Test void curlingSeparatesRegulationAndExtraEndsWithoutInventedEndBreakdown() {
        for (String type : List.of("CURLING_REG", "CURLING_EXTRA")) {
            assertTrue(type.length() <= 20);
            assertEquals(type, CommunityRoundEventType.validate("Curling", type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Curling", type));
            assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
            assertEquals(score(8, 6), CommunityTeamFinalResults.validate(
                    "Curling", type, players, score(8, 6)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate("Curling", type, players, score(-1, 6)));
        }
        assertEquals(score(5, 5), CommunityTeamFinalResults.validate(
                "Curling", "CURLING_REG", players, score(5, 5)));
        assertEquals(score(9, 6), CommunityTeamFinalResults.validate(
                "Curling", "CURLING_EXTRA", players, score(9, 6)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Curling", "CURLING_EXTRA", players, score(5, 5)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Curling", "PETANQUE_13"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Curling", "CURLING_REG",
                        List.of(athlete("one", "A")), score(6, 2)));
    }
    @Test void petanqueSupportsStandardAndQualifierTargetsAndExactTeamSizes() {
        for (int target : List.of(11, 13)) {
            String type = "PETANQUE_" + target;
            assertTrue(type.length() <= 20);
            assertEquals(type, CommunityRoundEventType.validate("Petanca", type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Petanca", type));
            assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
            for (int other : List.of(0, 7, target - 1)) {
                assertEquals(score(target, other), CommunityTeamFinalResults.validate(
                        "Petanca", type, players, score(target, other)));
                assertEquals(score(other, target), CommunityTeamFinalResults.validate(
                        "Petanca", type, players, score(other, target)));
            }
            for (CommunityTeamFinalScore bad : List.of(score(0, 0), score(7, 6),
                    score(target, target), score(target - 1, target - 1),
                    score(target + 1, 0), score(-1, target))) {
                assertThrows(IllegalArgumentException.class, () ->
                        CommunityTeamFinalResults.validate("Petanca", type, players, bad));
            }
            for (int count : List.of(2, 3)) {
                List<CommunityRoundEntryRequest> sides = new java.util.ArrayList<>();
                for (int i = 0; i < count; i++) {
                    sides.add(athlete("a" + i, "A"));
                    sides.add(athlete("b" + i, "B"));
                }
                assertEquals(score(target, 4), CommunityTeamFinalResults.validate(
                        "Petanca", type, sides, score(target, 4)));
            }
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate("Petanca", type,
                            List.of(athlete("a","A"),athlete("b","A"),athlete("c","B")),
                            score(target, 3)));
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate("Petanca", type,
                            List.of(athlete("a","A"),athlete("b","A"),athlete("c","A"),
                                    athlete("d","A"),athlete("e","B"),athlete("f","B"),
                                    athlete("g","B"),athlete("h","B")),score(target, 3)));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Petanca", "CURLING_REG"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Futebol", "PETANQUE_13", players, score(13, 0)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Petanca", "CURLING_EXTRA", players, score(9, 6)));
    }
    @Test void tenpinBowlingHeadToHeadOnlyRecordsOneConfirmedGamePerPlayer() {
        String type = "BOWLING_TENPIN";
        assertTrue(type.length() <= 20);
        assertEquals(type, CommunityRoundEventType.validate("Bowling", type));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Bowling", type));
        assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
        for (CommunityTeamFinalScore finalScore : List.of(score(0, 0),
                score(300, 300), score(300, 180), score(221, 190), score(145, 200))) {
            assertEquals(finalScore, CommunityTeamFinalResults.validate(
                    "Bowling", type, players, finalScore));
        }
        for (CommunityTeamFinalScore bad : List.of(score(301, 0), score(0, 301),
                score(-1, 0), score(1000, 1))) {
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate("Bowling", type, players, bad));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Bowling", type,
                        List.of(athlete("a", "A"), athlete("b", "A"), athlete("c", "B")),
                        score(145, 140)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Bowling", type,
                        List.of(new CommunityRoundEntryRequest("a", "A", CommunityMatchResult.WIN,
                                1, 0, false, false), athlete("b", "B")), score(120, 110)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Bowling", "POOL_9_RACKS"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Bilhar", type, players, score(123, 122)));
    }
    @Test void nineBallPoolRecordsCompletedSinglesRackTotalsNotInventedRaceLength() {
        String type = "POOL_9_RACKS";
        assertTrue(type.length() <= 20);
        assertEquals(type, CommunityRoundEventType.validate("Bilhar", type));
        assertTrue(CommunityRoundEventType.isNeutralEvent("Bilhar", type));
        assertTrue(CommunityTeamFinalResults.isTeamFinalEvent(type));
        for (CommunityTeamFinalScore finalScore : List.of(score(1, 0),
                score(9, 6), score(7, 10), score(11, 10), score(5, 2))) {
            assertEquals(finalScore, CommunityTeamFinalResults.validate(
                    "Bilhar", type, players, finalScore));
        }
        for (CommunityTeamFinalScore bad : List.of(score(0, 0), score(1, 1),
                score(4, 4), score(-1, 3), score(1, 1000))) {
            assertThrows(IllegalArgumentException.class, () ->
                    CommunityTeamFinalResults.validate("Bilhar", type, players, bad));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Bilhar", type,
                        List.of(athlete("a", "A"), athlete("b", "A"), athlete("c", "B")),
                        score(5, 3)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityRoundEventType.validate("Bilhar", "BOWLING_TENPIN"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Bowling", type, players, score(5, 3)));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityTeamFinalResults.validate("Futebol", type, players, score(5, 3)));
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
