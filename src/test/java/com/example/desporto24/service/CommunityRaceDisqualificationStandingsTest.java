package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRaceDisqualificationStandingsTest {
    private CommunityRoundEntryResponse player(
            long entryId, long playerId, int place, boolean disqualified) {
        return new CommunityRoundEntryResponse(null, null, "Piloto " + playerId, null, null,
                CommunityMatchResult.DRAW, 0, 0, false, false, place,
                new BigDecimal("80.000"), null, 10, entryId, playerId, 0, null,
                BigDecimal.ZERO, new BigDecimal("80.000"), disqualified,
                disqualified ? "Regra da prova" : null);
    }

    private CommunityRoundResponse round(long id, CommunityRoundEntryResponse... entries) {
        return new CommunityRoundResponse(id, "Corrida " + id,
                Instant.parse("2026-10-20T19:00:00Z"), List.of(entries),
                "Karting", null, List.of());
    }

    @Test void disqualifiedRaceStillCountsParticipationButNoWinPodiumOrPoints() {
        var rows = CommunityRaceStandings.calculate(List.of(
                round(1, player(11, 101, 1, true), player(12, 102, 2, false)),
                round(2, player(21, 101, 2, false), player(22, 102, 1, false))),
                List.of(new CommunityRaceRuleResponse("Karting", List.of(25, 18)))).get(0).standings();
        var first = rows.stream().filter(row -> row.playerId().equals(101L)).findFirst().orElseThrow();
        assertEquals(2, first.races());
        assertEquals(0, first.wins());
        assertEquals(1, first.podiums());
        assertEquals(2, first.bestFinish());
        assertEquals(18, first.championshipPoints());
        var second = rows.stream().filter(row -> row.playerId().equals(102L)).findFirst().orElseThrow();
        assertEquals(43, second.championshipPoints());
        assertEquals(1, second.wins());
    }

    @Test void solelyDisqualifiedParticipantHasNoSportingFinishButRemainsVisible() {
        var rows = CommunityRaceStandings.calculate(
                List.of(round(1, player(11, 101, 1, true))), List.of()).get(0).standings();
        assertEquals(1, rows.size());
        assertEquals(1, rows.get(0).races());
        assertEquals(0, rows.get(0).wins());
        assertEquals(0, rows.get(0).podiums());
        assertNull(rows.get(0).bestFinish());
        assertNull(rows.get(0).championshipPoints());
    }

    @Test void timePenaltyDoesNotRecomputeFinishPositionOrAwardOtherDriverWin() {
        var penalized = new CommunityRoundEntryResponse(null, null, "Rápido", null, null,
                CommunityMatchResult.DRAW, 0, 0, false, false, 1,
                new BigDecimal("80.000"), null, null, 12L, 102L, 0, null,
                new BigDecimal("10.000"), new BigDecimal("90.000"), false,
                "Penalização de tempo");
        var standings = CommunityRaceStandings.calculate(
                List.of(round(1, penalized, player(13, 103, 2, false))),
                List.of(new CommunityRaceRuleResponse("Karting", List.of(25,18)))).get(0).standings();
        var top = standings.stream().filter(r -> r.playerId().equals(102L)).findFirst().orElseThrow();
        assertEquals(1, top.bestFinish());
        assertEquals(1, top.wins());
        assertEquals(25, top.championshipPoints());
    }
}
