package com.example.desporto24.service;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityGolfTournamentStandingsTest {
    private CommunityGolfTournamentStandings.Score score(long player, String strokes) {
        return new CommunityGolfTournamentStandings.Score(player, new BigDecimal(strokes));
    }

    @Test void ordersNineHoleTotalsAndPreservesCompetitionTies() {
        var result = CommunityGolfTournamentStandings.calculate(9, List.of(
                score(4, "54"), score(3, "45"), score(2, "36"), score(1, "36")));
        assertEquals(List.of(
                new CommunityGolfTournamentStandings.Standing(1, 1, 36),
                new CommunityGolfTournamentStandings.Standing(1, 2, 36),
                new CommunityGolfTournamentStandings.Standing(3, 3, 45),
                new CommunityGolfTournamentStandings.Standing(4, 4, 54)), result);
    }

    @Test void acceptsEighteenHoleGrossTotalWithStoredDecimalScale() {
        assertEquals(List.of(new CommunityGolfTournamentStandings.Standing(1, 7, 72)),
                CommunityGolfTournamentStandings.calculate(18, List.of(score(7, "72.0000"))));
    }

    @Test void rejectsIncompleteOrMalformedScores() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(9, null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(9, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(10, List.of(score(1, "36"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(18, List.of(score(1, "17"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(9, List.of(score(1, "36.5"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(9, List.of(score(1, "1000000"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(9, List.of(score(1, "36"), score(1, "37"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityGolfTournamentStandings.calculate(9, List.of(score(0, "36"))));
    }
}
