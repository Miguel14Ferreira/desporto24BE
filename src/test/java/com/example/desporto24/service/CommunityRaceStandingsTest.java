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

    @Test void trackScratchPositionsEarnCommunityPointsButTrackTimeTrialsDoNot() {
        var scratch = new CommunityRoundResponse(100L, "Scratch", Instant.now(),
                List.of(driver(1, 11, "Ana", 1, 0), driver(2, 22, "Rui", 2, 0)),
                "Ciclismo de Pista", null, List.of(), "TRACK_SCRATCH");
        var timed = new CommunityRoundResponse(101L, "Contrarrelógio", Instant.now(),
                List.of(driver(3, 11, "Ana", 2, 0), driver(4, 22, "Rui", 1, 0)),
                "Ciclismo de Pista", null, List.of(), "TRACK_TIME_TRIAL");
        var flying = new CommunityRoundResponse(102L, "200m lançados", Instant.now(),
                List.of(driver(5, 11, "Ana", 3, 0), driver(6, 22, "Rui", 1, 0)),
                "Ciclismo de Pista", null, List.of(), "TRACK_FLYING_200");
        var group = CommunityRaceStandings.calculate(List.of(scratch, timed, flying),
                List.of(new CommunityRaceRuleResponse("Ciclismo de Pista", List.of(25, 18)))).get(0);
        assertEquals(11L, group.standings().get(0).playerId());
        assertEquals(25, group.standings().get(0).championshipPoints());
        assertEquals(1, group.standings().get(0).races());
        assertEquals(18, group.standings().get(1).championshipPoints());
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

    @Test void newPoolResultsAreIsolatedFromLegacySwimmingRaceStandings() {
        var newPool = new CommunityRoundResponse(10L, "100 m livres",
                Instant.parse("2026-10-24T10:00:00Z"),
                List.of(driver(11, 101, "Ana", 1, 0)),
                "Natação", null, List.of(), "SWIM_LCM", "FREE_100");
        var oldRace = race(11, "Natação", driver(12, 102, "Histórico", 1, 0));
        var rule = new CommunityRaceRuleResponse("Natação", List.of(25, 18));
        var groups = CommunityRaceStandings.calculate(List.of(newPool, oldRace), List.of(rule));
        assertEquals(1, groups.size());
        assertEquals("Natação", groups.get(0).sport());
        assertEquals(1, groups.get(0).standings().size());
        assertEquals(102L, groups.get(0).standings().get(0).playerId());
        assertEquals(25, groups.get(0).standings().get(0).championshipPoints());
        assertTrue(CommunityRaceStandings.calculate(List.of(newPool), List.of(rule)).isEmpty());
    }

    @Test void openWaterFiveAndTenKilometresNeverJoinLegacyOrPoolStandings() {
        var openFive = new CommunityRoundResponse(15L, "5 km",
                Instant.parse("2026-10-24T10:00:00Z"),
                List.of(driver(101, 101, "Ana", null, 0)),
                "Natação em Águas Abertas", null, List.of(), "OWS_INDIVIDUAL", "OWS_5K");
        var openTen = new CommunityRoundResponse(16L, "10 km",
                Instant.parse("2026-10-24T12:00:00Z"),
                List.of(driver(102, 102, "Bruno", null, 0)),
                "Natação em Águas Abertas", null, List.of(), "OWS_INDIVIDUAL", "OWS_10K");
        var historic = race(17, "Natação em Águas Abertas",
                driver(103, 103, "Histórico", 1, 0));
        var rule = new CommunityRaceRuleResponse(
                "Natação em Águas Abertas", List.of(25, 18));
        assertTrue(CommunityRaceStandings.calculate(List.of(openFive, openTen), List.of(rule)).isEmpty());
        var groups = CommunityRaceStandings.calculate(
                List.of(openFive, historic, openTen), List.of(rule));
        assertEquals(1, groups.size());
        assertEquals("Natação em Águas Abertas", groups.get(0).sport());
        assertEquals(1, groups.get(0).standings().size());
        assertEquals(103L, groups.get(0).standings().get(0).playerId());
        assertEquals(25, groups.get(0).standings().get(0).championshipPoints());
    }

    @Test void typedOrienteeringRoutesNeverJoinGenericChampionshipStandings() {
        var sprint = new CommunityRoundResponse(201L, "Sprint A",
                Instant.parse("2026-10-24T10:00:00Z"),
                List.of(driver(101, 11, "Ana", null, 0)),
                "Orientação", null, List.of(), "FOOT_ORIENTEERING", "ORIENT_SPRINT");
        var longCourse = new CommunityRoundResponse(202L, "Percurso longo",
                Instant.parse("2026-10-24T11:00:00Z"),
                List.of(driver(102, 22, "Bruno", null, 0)),
                "Orientação", null, List.of(), "FOOT_ORIENTEERING", "ORIENT_LONG");
        var historical = race(203, "Orientação", driver(103, 33, "Histórico", 1, 0));
        var rule = new CommunityRaceRuleResponse("Orientação", List.of(25, 18));
        assertTrue(CommunityRaceStandings.calculate(List.of(sprint, longCourse), List.of(rule)).isEmpty());
        var standings = CommunityRaceStandings.calculate(
                List.of(sprint, historical, longCourse), List.of(rule));
        assertEquals(1, standings.size());
        assertEquals("Orientação", standings.get(0).sport());
        assertEquals(1, standings.get(0).standings().size());
        assertEquals(33L, standings.get(0).standings().get(0).playerId());
    }

    @Test void timedInlineTrackSkatingDoesNotProduceCertifiedChampionshipStandings() {
        var timeTrial = new CommunityRoundResponse(230L, "200m pista",
                Instant.parse("2026-10-24T10:00:00Z"),
                List.of(driver(101, 101, "Ana", null, 0)),
                "Patinagem de Velocidade", null, List.of(), "INLINE_TRACK_200_TT");
        var historical = race(231, "Patinagem de Velocidade",
                driver(102, 102, "Histórico", 1, 0));
        var rule = new CommunityRaceRuleResponse("Patinagem de Velocidade", List.of(25, 18));
        assertTrue(CommunityRaceStandings.calculate(List.of(timeTrial), List.of(rule)).isEmpty());
        var groups = CommunityRaceStandings.calculate(List.of(timeTrial, historical), List.of(rule));
        assertEquals(1, groups.size());
        assertEquals(1, groups.get(0).standings().size());
        assertEquals(102L, groups.get(0).standings().get(0).playerId());
        assertEquals(25, groups.get(0).standings().get(0).championshipPoints());
    }
}
