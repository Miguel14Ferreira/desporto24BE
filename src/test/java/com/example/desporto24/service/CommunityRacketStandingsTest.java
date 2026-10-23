package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryResponse;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunitySetScore;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommunityRacketStandingsTest {
    private static CommunityRoundEntryResponse player(long id, String name,
            String side, CommunityMatchResult outcome) {
        return new CommunityRoundEntryResponse(id, "user" + id, name, null,
                side, outcome, 0, 0, false, false);
    }

    private static CommunityRoundResponse match(long id, String sport, String format,
            List<CommunitySetScore> sets, CommunityRoundEntryResponse... players) {
        return new CommunityRoundResponse(id, "Jornada " + id, Instant.parse("2026-10-20T18:00:00Z"),
                List.of(players), sport, format, sets);
    }

    @Test void separatesRacketSportsAndSinglesFromDoubles() {
        var a = player(10L, "Maria", "A", CommunityMatchResult.WIN);
        var b = player(20L, "Ana", "B", CommunityMatchResult.LOSS);
        var r = match(1, "Ténis", "SINGLES",
                List.of(new CommunitySetScore(6, 3), new CommunitySetScore(6, 4)), a, b);
        var padel = match(2, "Padel", "DOUBLES",
                List.of(new CommunitySetScore(6, 4), new CommunitySetScore(6, 2)),
                player(10, "Maria", "A", CommunityMatchResult.WIN),
                player(30, "João", "A", CommunityMatchResult.WIN),
                player(20, "Ana", "B", CommunityMatchResult.LOSS),
                player(40, "Pedro", "B", CommunityMatchResult.LOSS));
        var tables = CommunityRacketStandings.calculate(List.of(r, padel));
        assertEquals(2, tables.size());
        var singles = tables.stream().filter(group -> group.sport().equals("Ténis")).findFirst().orElseThrow();
        assertEquals("SINGLES", singles.matchFormat());
        assertEquals(2, singles.standings().size());
        var leader = singles.standings().get(0);
        assertEquals(10L, leader.profileId());
        assertEquals(1, leader.matches());
        assertEquals(1, leader.wins());
        assertEquals(2, leader.setsWon());
        assertEquals(0, leader.setsLost());
        assertEquals(2, leader.setDifference());
        assertEquals(12, leader.gamesOrPointsWon());
        assertEquals(7, leader.gamesOrPointsLost());
        assertEquals(5, leader.gamesOrPointsDifference());
        assertEquals(4, tables.stream().filter(g -> g.sport().equals("Padel"))
                .findFirst().orElseThrow().standings().size());
    }

    @Test void tieBreakBySetsThenPointsAndSharedRanksOnSportsTie() {
        var r1 = match(1, "Ténis", "SINGLES", List.of(
                new CommunitySetScore(6, 3), new CommunitySetScore(6, 3)),
                player(1, "One", "A", CommunityMatchResult.WIN),
                player(2, "Two", "B", CommunityMatchResult.LOSS));
        var r2 = match(2, "Ténis", "SINGLES", List.of(
                new CommunitySetScore(6, 4), new CommunitySetScore(6, 4)),
                player(3, "Three", "A", CommunityMatchResult.WIN),
                player(4, "Four", "B", CommunityMatchResult.LOSS));
        var rows = CommunityRacketStandings.calculate(List.of(r2, r1)).get(0).standings();
        assertEquals(1L, rows.get(0).profileId());
        assertEquals(1, rows.get(0).position());
        assertEquals(2, rows.get(1).position());
        var equal = match(3, "Ténis", "DOUBLES", List.of(
                new CommunitySetScore(6, 4), new CommunitySetScore(6, 4)),
                player(7, "Equal One", "A", CommunityMatchResult.WIN),
                player(8, "Equal Two", "A", CommunityMatchResult.WIN),
                player(9, "Opp One", "B", CommunityMatchResult.LOSS),
                player(10, "Opp Two", "B", CommunityMatchResult.LOSS));
        var doubles = CommunityRacketStandings.calculate(List.of(equal)).get(0).standings();
        assertEquals(1, doubles.get(0).position());
        assertEquals(1, doubles.get(1).position());
    }

    @Test void onlyCountsCurrentSeasonInputAndSkipsUnstructuredOrInvalidRounds() {
        var valid = match(1, "Badminton", "SINGLES",
                List.of(new CommunitySetScore(21, 19), new CommunitySetScore(21, 18)),
                player(1, "One", "A", CommunityMatchResult.WIN),
                player(2, "Two", "B", CommunityMatchResult.LOSS));
        var older = match(2, "Badminton", null,
                List.of(new CommunitySetScore(21, 19)),
                player(3, "Legacy", "A", CommunityMatchResult.WIN),
                player(4, "Legacy 2", "B", CommunityMatchResult.LOSS));
        var football = match(3, "Futebol", "SINGLES",
                List.of(new CommunitySetScore(6, 4)),
                player(5, "Five", "A", CommunityMatchResult.WIN),
                player(6, "Six", "B", CommunityMatchResult.LOSS));
        var invalid = match(4, "Badminton", "SINGLES",
                List.of(new CommunitySetScore(21, 21)),
                player(7, "Seven", "A", CommunityMatchResult.WIN),
                player(8, "Eight", "B", CommunityMatchResult.LOSS));
        var tables = CommunityRacketStandings.calculate(List.of(valid, older, football, invalid));
        assertEquals(1, tables.size());
        assertEquals(2, tables.get(0).standings().size());
        assertTrue(CommunityRacketStandings.calculate(List.of()).isEmpty());
    }

    @Test void usesStableProfileIdentifiersRatherThanCombiningIdenticalDisplayNames() {
        var match = match(1, "Squash", "SINGLES",
                List.of(new CommunitySetScore(11, 7)),
                player(17, "João", "A", CommunityMatchResult.WIN),
                player(18, "João", "B", CommunityMatchResult.LOSS));
        var rows = CommunityRacketStandings.calculate(List.of(match)).get(0).standings();
        assertEquals(2, rows.size());
        assertNotEquals(rows.get(0).profileId(), rows.get(1).profileId());
    }
}
