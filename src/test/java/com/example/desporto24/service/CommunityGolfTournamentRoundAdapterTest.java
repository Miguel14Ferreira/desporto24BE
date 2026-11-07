package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryResponse;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityGolfTournamentRoundAdapterTest {
    private CommunityRoundEntryResponse entry(long player, String total) {
        return new CommunityRoundEntryResponse(null, null, "Player " + player, null,
                null, CommunityMatchResult.DRAW, 0, 0, false, false,
                null, null, null, null, null, player, 0, null,
                null, null, false, null, new BigDecimal(total));
    }

    private CommunityRoundResponse round(String sport, String event,
            List<CommunityRoundEntryResponse> entries) {
        return new CommunityRoundResponse(1L, "Round", null, entries,
                sport, null, List.of(), event);
    }

    @Test void ranksStoredNineHoleScoresWithoutConvertingToFootballPoints() {
        var standings = CommunityGolfTournamentRoundAdapter.calculate(round(
                "Golfe", "GOLF_STROKE_9", List.of(entry(3, "45.0000"),
                        entry(2, "36.0000"), entry(1, "36.0000"))));
        assertEquals(List.of(
                new CommunityGolfTournamentStandings.Standing(1, 1, 36),
                new CommunityGolfTournamentStandings.Standing(1, 2, 36),
                new CommunityGolfTournamentStandings.Standing(3, 3, 45)), standings);
    }

    @Test void rejectsStoredFootballAndRaceMetricsInsteadOfRankingThem() {
        var football = new CommunityRoundEntryResponse(null, null, "Player 1", null,
                "A", CommunityMatchResult.WIN, 2, 0, true, false,
                null, null, null, null, null, 1L, 0, null,
                null, null, false, null, new BigDecimal("36"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentRoundAdapter.calculate(
                        round("Golfe", "GOLF_STROKE_9", List.of(football))));
        var race = new CommunityRoundEntryResponse(null, null, "Player 2", null,
                null, CommunityMatchResult.DRAW, 0, 0, false, false,
                1, null, null, null, null, 2L, 0, null,
                null, null, false, null, new BigDecimal("36"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentRoundAdapter.calculate(
                        round("Golfe", "GOLF_STROKE_9", List.of(race))));
    }

    @Test void rejectsOtherSportsAndIncompleteStoredRounds() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentRoundAdapter.calculate(
                        round("Futebol", "GOLF_STROKE_9", List.of(entry(1, "36")))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentRoundAdapter.calculate(
                        round("Golfe", "GOLF_STROKE_18", List.of(entry(1, "17")))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentRoundAdapter.calculate(
                        round("Golfe", "GOLF_STROKE_9", List.of())));
    }
}
