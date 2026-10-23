package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRacketStandingsRulesTest {
    private CommunityRoundEntryResponse player(long id, String team, boolean won) {
        return new CommunityRoundEntryResponse(id, "u" + id, "Jogador " + id, null,
                team, won ? CommunityMatchResult.WIN : CommunityMatchResult.LOSS,
                0, 0, false, false);
    }
    private CommunityRoundResponse round(long id, String sport, String format,
            long winner, long loser, int gamesForWinner, int gamesForLoser) {
        return new CommunityRoundResponse(id, "Jornada", Instant.parse("2026-10-20T18:00:00Z"),
                List.of(player(winner, "A", true), player(loser, "B", false)),
                sport, format, List.of(new CommunitySetScore(gamesForWinner, gamesForLoser)));
    }
    @Test void pointsOnlyApplyWhenExplicitRuleIsSavedForMatchingSportAndFormat() {
        var results = List.of(
                round(1, "Ténis", "SINGLES", 1, 2, 6, 4),
                round(2, "Ténis", "SINGLES", 2, 1, 6, 4),
                round(3, "Padel", "SINGLES", 3, 4, 6, 4));
        var rule = new CommunityRacketRuleResponse("Ténis", "SINGLES", 3, 1, "SETS_FIRST");
        var rankings = CommunityRacketStandings.calculate(results, List.of(rule));
        var tennis = rankings.stream().filter(g -> g.sport().equals("Ténis")).findFirst().orElseThrow();
        assertEquals(1, tennis.standings().get(0).position());
        assertEquals(1, tennis.standings().get(1).position());
        assertEquals(4, tennis.standings().get(0).championshipPoints());
        assertEquals(4, tennis.standings().get(1).championshipPoints());
        var padel = rankings.stream().filter(g -> g.sport().equals("Padel")).findFirst().orElseThrow();
        assertNull(padel.rule());
        assertNull(padel.standings().get(0).championshipPoints());
    }
    @Test void configuredLossPointsAlterOrderWithoutChangingRecordedMatchStatistics() {
        var results = List.of(
                round(1, "Ténis", "SINGLES", 1, 3, 6, 2),
                round(2, "Ténis", "SINGLES", 1, 4, 6, 3),
                round(3, "Ténis", "SINGLES", 2, 1, 6, 4),
                round(4, "Ténis", "SINGLES", 2, 3, 6, 4),
                round(5, "Ténis", "SINGLES", 2, 4, 6, 4));
        var win2loss1 = new CommunityRacketRuleResponse("Ténis", "SINGLES", 2, 1, "SETS_FIRST");
        var table = CommunityRacketStandings.calculate(results, List.of(win2loss1)).get(0).standings();
        assertEquals(2L, table.get(0).profileId());
        assertEquals(6, table.get(0).championshipPoints());
        assertEquals(1L, table.get(1).profileId());
        assertEquals(5, table.get(1).championshipPoints());
        assertEquals(2, table.get(1).wins());
        assertEquals(1, table.get(1).losses());
    }
    @Test void scoreFirstPrefersBigScoreDifferenceInEqualPointTies() {
        var results = List.of(
                round(1, "Squash", "SINGLES", 1, 3, 11, 9),
                round(2, "Squash", "SINGLES", 2, 4, 11, 1));
        var rule = new CommunityRacketRuleResponse("Squash", "SINGLES", 2, 0, "SCORE_FIRST");
        var rows = CommunityRacketStandings.calculate(results, List.of(rule)).get(0).standings();
        assertEquals(2L, rows.get(0).profileId());
        assertEquals(1L, rows.get(1).profileId());
        assertEquals(2, rows.get(0).championshipPoints());
        assertEquals(1, rows.get(0).position());
        assertEquals(2, rows.get(1).position());
    }
}
