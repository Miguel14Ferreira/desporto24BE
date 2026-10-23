package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityRoundEventTypeTest {
    @Test void requiresAnExplicitChoiceForMultiEventSports() {
        assertEquals("RACE", CommunityRoundEventType.validate("BMX", "RACE"));
        assertEquals("FREESTYLE", CommunityRoundEventType.validate("Snowboard", "FREESTYLE"));
        assertEquals("TRACK", CommunityRoundEventType.validate("Atletismo", "TRACK"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("BMX", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Snowboard", "TRACK"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Atletismo", "FREESTYLE"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Futebol", "TRACK"));
        assertNull(CommunityRoundEventType.validate("Futebol", null));
    }

    @Test void neutralEventsRejectFootballPointsButAllowParticipation() {
        var participant = new com.example.desporto24.model.dto.community.CommunityRoundEntryRequest(
                "jogador", null,
                com.example.desporto24.enumeration.CommunityMatchResult.DRAW,
                0, 0, false, false);
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "BMX", "FREESTYLE", java.util.List.of(participant)));
        var scored = new com.example.desporto24.model.dto.community.CommunityRoundEntryRequest(
                "jogador", null,
                com.example.desporto24.enumeration.CommunityMatchResult.WIN,
                2, 0, false, false);
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries(
                        "Atletismo", "JUMP", java.util.List.of(scored)));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                "Futebol", null, java.util.List.of(scored)));
    }

    @Test void neverConfusesFreestyleOrFieldEventsWithRaceScoring() {
        assertTrue(CommunityRoundEventType.isRaceEvent("BMX", "RACE"));
        assertTrue(CommunityRoundEventType.isRaceEvent("BMX", null)); // historic
        assertFalse(CommunityRoundEventType.isRaceEvent("BMX", "FREESTYLE"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Snowboard", "RACE"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Snowboard", "FREESTYLE"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Snowboard", null));
        assertTrue(CommunityRoundEventType.isRaceEvent("Atletismo", "TRACK"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Atletismo", "JUMP"));
        assertFalse(CommunityRoundEventType.isRaceEvent("Atletismo", "THROW"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Karting", null));
        assertFalse(CommunityRoundEventType.isRaceEvent("Futebol", null));
        assertTrue(CommunityRoundEventType.isNeutralEvent("BMX", "FREESTYLE"));
    }
}
