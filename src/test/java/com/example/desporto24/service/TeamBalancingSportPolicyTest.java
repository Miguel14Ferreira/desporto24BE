package com.example.desporto24.service;

import com.example.desporto24.constant.SportsCatalog;
import com.example.desporto24.enumeration.SessionPlayFormat;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TeamBalancingSportPolicyTest {

    @Test
    void supportsVariableEvenTeamNumbersRatherThanAssumingTenFootballers() {
        for (int size : new int[]{4, 6, 8, 10, 12, 14, 18, 22}) {
            assertTrue(TeamBalancingSportPolicy.isEligible("Futebol", size),
                    "Football community match with " + size + " participants");
        }
        assertTrue(TeamBalancingSportPolicy.isEligible("Voleibol", 12));
        assertTrue(TeamBalancingSportPolicy.isEligible("Basquetebol", 10));
        assertTrue(TeamBalancingSportPolicy.isEligible("Futsal", 10));
    }

    @Test
    void hidesTeamBalancerForIndividualOrAmbiguousSinglesAndDoublesSports() {
        for (String sport : new String[]{"Atletismo", "Corrida", "Ciclismo",
                "Natação", "Boxe", "Judo", "Ténis", "Badminton", "Padel",
                "Teqball", "Golfe", "Xadrez", "Muay Thai"}) {
            assertTrue(SportsCatalog.ALLOWED.contains(sport), sport + " must be catalogued");
            assertFalse(TeamBalancingSportPolicy.isEligible(sport, 10), sport);
        }
        assertFalse(TeamBalancingSportPolicy.isEligible("Not a sport", 10));
    }

    @Test
    void respectsExplicitFormatAndAvoidsUnevenSidesOrExternalUnlinkedSeats() {
        assertFalse(TeamBalancingSportPolicy.isEligible("Futebol", 9));
        assertFalse(TeamBalancingSportPolicy.isEligible("Futebol", 2));
        assertTrue(TeamBalancingSportPolicy.isEligible("Basquetebol 3x3", 6));
        assertFalse(TeamBalancingSportPolicy.isEligible("Basquetebol 3x3", 10));
        assertTrue(TeamBalancingSportPolicy.isEligible("Rugby Sevens", 14));
        assertFalse(TeamBalancingSportPolicy.isEligible("Rugby Sevens", 12));
        assertTrue(TeamBalancingSportPolicy.isEligible("Voleibol de Praia", 4));
        assertFalse(TeamBalancingSportPolicy.isEligible("Voleibol de Praia", 6));

        Session session = new Session();
        session.setSport("Futebol");
        session.setMaxParticipants(12);
        assertFalse(TeamBalancingSportPolicy.isEligible(session), "independent session");
        session.setCommunity(mock(Community.class));
        assertTrue(TeamBalancingSportPolicy.isEligible(session));
        session.setExternalConfirmedParticipants(1);
        assertFalse(TeamBalancingSportPolicy.isEligible(session),
                "external confirmed players cannot be assigned to a Jynlo team");
    }
    @Test
    void explicitDoublesSupportsFourPlayersNotSixOrSingles() {
        for (String sport : new String[]{"Padel", "Ténis", "Badminton",
                "Pickleball", "Ténis de praia", "Ténis de mesa"}) {
            assertTrue(SportsCatalog.ALLOWED.contains(sport));
            assertEquals(SessionPlayFormat.PAIRS,
                    SessionPlayFormatPolicy.resolve(sport, SessionPlayFormat.PAIRS));
            Session session = new Session();
            session.setSport(sport);
            session.setCommunity(mock(Community.class));
            session.setPlayFormat(SessionPlayFormat.PAIRS);
            session.setMaxParticipants(4);
            assertTrue(TeamBalancingSportPolicy.isEligible(session), sport);
            session.setMaxParticipants(6);
            assertFalse(TeamBalancingSportPolicy.isEligible(session),
                    "More than four doubles players need explicit rotations or reserves");
            session.setPlayFormat(SessionPlayFormat.INDIVIDUAL);
            session.setMaxParticipants(4);
            assertFalse(TeamBalancingSportPolicy.isEligible(session));
        }
    }

    @Test
    void rejectsIncompatibleFormatsAndPreservesLegacyCommunityFootball() {
        assertThrows(IllegalArgumentException.class,
                () -> SessionPlayFormatPolicy.resolve("Natação", SessionPlayFormat.PAIRS));
        assertThrows(IllegalArgumentException.class,
                () -> SessionPlayFormatPolicy.resolve("Futebol", SessionPlayFormat.INDIVIDUAL));
        assertThrows(IllegalArgumentException.class,
                () -> SessionPlayFormatPolicy.resolve("Padel", SessionPlayFormat.TEAMS));
        assertEquals(SessionPlayFormat.TEAMS, SessionPlayFormatPolicy.resolve("Futebol", null));
        assertEquals(SessionPlayFormat.INDIVIDUAL, SessionPlayFormatPolicy.resolve("Padel", null));
        Session legacy = new Session();
        legacy.setSport("Futebol");
        legacy.setCommunity(mock(Community.class));
        legacy.setMaxParticipants(10);
        assertTrue(TeamBalancingSportPolicy.isEligible(legacy));
    }
}
