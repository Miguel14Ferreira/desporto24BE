package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityMatchPointsSportTest {
    @Test
    void acceptsDifferentTeamSportsForSharedWinDrawLossRules() {
        for (String sport : new String[] {
                "Futebol", "Futsal", "Basquetebol", "Basquetebol 3x3",
                "Andebol", "Voleibol", "Rugby", "Cricket", "Hóquei em Patins"
        }) {
            assertTrue(CommunityMatchPointsSport.supports(sport), sport);
        }
    }

    @Test
    void neverOffersTeamPointsForIncompatibleResultFormats() {
        for (String sport : new String[] {
                "Karting", "Ciclismo", "Natação", "Atletismo",
                "Ténis", "Padel", "Xadrez", "Golfe", "Surf", "Ginástica",
                "Yoga", "Unknown sport"
        }) {
            assertFalse(CommunityMatchPointsSport.supports(sport), sport);
        }
        assertFalse(CommunityMatchPointsSport.supports(null));
    }
}
