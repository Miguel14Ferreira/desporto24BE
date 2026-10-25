package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Regression: optional community standings must not gate World Skate heat records. */
class CommunityInlineSpeedStandaloneRoundsTest {
    @Test void firstRoundAndKnockoutHeatsAreIndependentOfCommunityStandings() {
        for (String prefix : new String[] {"INLINE_TRACK_500D_", "INLINE_ROAD_LAP_"}) {
            for (String suffix : new String[] {"FIRST_ROUND", "QUARTER_HEAT",
                    "SEMI_HEAT", "FINAL_A"}) {
                assertTrue(CommunityCompetitionService.isStandingsIndependentSpeedHeat(
                        "Patinagem de Velocidade", prefix + suffix));
            }
        }
    }

    @Test void historyAndOtherSportsDoNotInheritTheException() {
        for (String value : new String[] {"INLINE_TRACK_200_TT",
                "INLINE_TRACK_500D_FINAL_B", "INLINE_ROAD_LAP_FINAL_B",
                "TRACK_SPRINT_HEAT", "CHESS_GAME"}) {
            assertFalse(CommunityCompetitionService.isStandingsIndependentSpeedHeat(
                    "Patinagem de Velocidade", value));
        }
        assertFalse(CommunityCompetitionService.isStandingsIndependentSpeedHeat(
                "Futebol", "INLINE_TRACK_500D_FIRST_ROUND"));
        assertFalse(CommunityCompetitionService.isStandingsIndependentSpeedHeat(
                "Patinagem", "INLINE_ROAD_LAP_FIRST_ROUND"));
        assertFalse(CommunityCompetitionService.isStandingsIndependentSpeedHeat(
                "Patinagem de Velocidade", null));
    }
}
