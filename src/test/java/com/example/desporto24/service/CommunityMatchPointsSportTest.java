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
    void acceptsCaseSpacingAndAccentsForLegacyCommunitySportNames() {
        assertTrue(CommunityMatchPointsSport.supports("  futebol  "));
        assertTrue(CommunityMatchPointsSport.supports("VOLEIBOL DE PRAIA"));
        assertTrue(CommunityMatchPointsSport.supports("Hoquei em Patins"));
    }

    @Test
    void legacyApiOmitsFootballTableForKartingAndRaceOnlyCommunities() {
        assertFalse(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of("Karting"), null));
        assertFalse(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of("Karting", "Ciclismo de Estrada", "Atletismo"), null));
        assertFalse(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of("Futebol", "Karting"), "Karting"));
        assertFalse(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of("Futebol", "Ténis"), "Ténis"));
    }

    @Test
    void legacyApiRetainsWdlForFootballAndMixedCommunityTeamSeasons() {
        assertTrue(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of("Futebol"), null));
        assertTrue(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of("Karting", "Futebol"), null));
        assertTrue(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of("Karting", "Futebol"), "Futebol"));
        assertTrue(CommunityMatchPointsSport.showLegacyMatchStandings(
                java.util.List.of(), null));
        assertTrue(CommunityMatchPointsSport.showLegacyMatchStandings(null, null));
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
