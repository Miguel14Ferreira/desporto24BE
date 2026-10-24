package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommunityRaceStandingsTest {
    private CommunityRoundEntryResponse driver(
            long record, long player, String name, Integer place, int penalty) {
        return new CommunityRoundEntryResponse(null, null, name, null, null,
                CommunityMatchResult.DRAW, 0, 0, false, false, place, null, null, null,
                record, player, penalty, penalty > 0 ? "Limite de pista" : null);
    }

    private CommunityRoundResponse race(long id, String sport, CommunityRoundEntryResponse... drivers) {
        return new CommunityRoundResponse(id, "Prova " + id,
                Instant.parse("2026-10-20T18:00:00Z"), List.of(drivers),
                sport, null, List.of());
    }

    @Test void validatesPointsSchedulesWithoutFederationDefaults() {
        assertEquals(List.of(25, 18, 15, 12, 10), CommunityRaceStandings.parsePoints("25,18,15,12,10"));
        assertEquals(List.of(1), CommunityRaceStandings.parsePoints("1"));
        for (String invalid : List.of("", "1,2", "25,-1", "25,", "1001,4", "a,b", "1.5,0")) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRaceStandings.parsePoints(invalid), invalid);
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRaceStandings.parsePoints("1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1"));
    }

    @Test void pointsAndPenaltyApplyPerRaceWithoutChangingFinishingOrder() {
        var races = List.of(
                race(1, "Karting", driver(1, 11, "Maria", 1, 5),
                        driver(2, 22, "João", 2, 0)),
                race(2, "Karting", driver(3, 11, "Maria", 2, 0),
                        driver(4, 22, "João", 1, 0)));
        var rule = new CommunityRaceRuleResponse("Karting", List.of(25, 18, 15));
        var group = CommunityRaceStandings.calculate(races, List.of(rule)).get(0);
        assertEquals("Karting", group.sport());
        assertEquals(2, group.standings().size());
        var joao = group.standings().get(0);
        var maria = group.standings().get(1);
        assertEquals(22L, joao.playerId());
        assertEquals(43, joao.championshipPoints());
        assertEquals(11L, maria.playerId());
        assertEquals(38, maria.championshipPoints());
        assertEquals(5, maria.penaltyPoints());
        assertEquals(1, maria.bestFinish());
        assertEquals(2, maria.races());
        assertEquals(1, maria.wins());
    }

    @Test void scoresNeverCrossSportsAndNoRuleNeverGeneratesPoints() {
        var rounds = List.of(
                race(1, "Karting", driver(1, 11, "Mesmo nome", 1, 0)),
                race(2, "Automobilismo", driver(2, 22, "Mesmo nome", 1, 0)),
                race(3, "Futebol", driver(3, 33, "Futebolista", 1, 0)));
        var groups = CommunityRaceStandings.calculate(rounds, List.of(
                new CommunityRaceRuleResponse("Karting", List.of(10, 5))));
        assertEquals(2, groups.size());
        var kart = groups.stream().filter(g -> g.sport().equals("Karting")).findFirst().orElseThrow();
        var auto = groups.stream().filter(g -> g.sport().equals("Automobilismo")).findFirst().orElseThrow();
        assertEquals(10, kart.standings().get(0).championshipPoints());
        assertNull(auto.standings().get(0).championshipPoints());
        assertNull(auto.rule());
    }

    @Test void ignoresUnplacedAndDuplicateFinishPositionsAndRespectsStablePlayerId() {
        var rounds = List.of(
                race(1, "Karting", driver(1, 11, "Igual", 1, 0),
                        driver(2, 22, "Igual", 2, 0)),
                race(2, "Karting", driver(3, 11, "Igual", 1, 0),
                        driver(4, 33, "Outro", 1, 0)),
                race(3, "Karting", driver(5, 44, "Não classificado", null, 0)));
        var standings = CommunityRaceStandings.calculate(rounds, List.of()).get(0).standings();
        assertEquals(2, standings.size());
        assertEquals(11L, standings.get(0).playerId());
        assertEquals(22L, standings.get(1).playerId());
        assertEquals(1, standings.get(0).races());
        assertNull(standings.get(0).championshipPoints());
    }

    @Test void roadCyclingStandingsNeverMixTimeTrialsIntoRacePoints() {
        var road = new CommunityRoundResponse(70L, "Prova em linha",
                Instant.parse("2026-10-20T18:00:00Z"),
                List.of(driver(1, 11, "Ana", 1, 0), driver(2, 22, "Rui", 2, 0)),
                "Ciclismo de Estrada", null, List.of(), "ROAD_RACE");
        var tt = new CommunityRoundResponse(71L, "Contrarrelógio",
                Instant.parse("2026-10-21T18:00:00Z"),
                List.of(driver(3, 11, "Ana", 2, 0), driver(4, 22, "Rui", 1, 0)),
                "Ciclismo de Estrada", null, List.of(), "TIME_TRIAL");
        var group = CommunityRaceStandings.calculate(List.of(road, tt),
                List.of(new CommunityRaceRuleResponse("Ciclismo de Estrada", List.of(25, 18))))
                .get(0);
        assertEquals(25, group.standings().get(0).championshipPoints());
        assertEquals(1, group.standings().get(0).races());
        assertEquals(11L, group.standings().get(0).playerId());
    }

    @Test void penaltiesCannotTakePerRaceScoreBelowZero() {
        var round = race(1, "Karting", driver(1, 11, "A", 1, 1000),
                driver(2, 22, "B", 2, 0));
        var group = CommunityRaceStandings.calculate(List.of(round),
                List.of(new CommunityRaceRuleResponse("Karting", List.of(10, 5)))).get(0);
        var first = group.standings().stream().filter(s -> s.playerId() == 11L).findFirst().orElseThrow();
        assertEquals(0, first.championshipPoints());
        assertEquals(1000, first.penaltyPoints());
        assertEquals(1, first.wins());
    }
}
