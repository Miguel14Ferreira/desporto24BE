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

    @Test void roadCyclingRequiresExplicitEventTypeWithoutReclassifyingLegacyHistory() {
        assertEquals("ROAD_RACE", CommunityRoundEventType.validate("Ciclismo de Estrada", "ROAD_RACE"));
        assertEquals("TIME_TRIAL", CommunityRoundEventType.validate("Ciclismo de Estrada", "TIME_TRIAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Ciclismo de Estrada", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Ciclismo de Estrada", "TRACK"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Ciclismo de Estrada", "ROAD_RACE"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Ciclismo de Estrada", "TIME_TRIAL"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Ciclismo de Estrada", null));
        assertTrue(CommunityRoundEventType.isChampionshipRaceEvent("Ciclismo de Estrada", "ROAD_RACE"));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent("Ciclismo de Estrada", "TIME_TRIAL"));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent("Ciclismo de Estrada", null));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent("Mountain Bike", "ROAD_RACE"));
    }

    @Test void trackCyclingSeparatesScratchFromTimedEventsAndKeepsLegacyAmbiguous() {
        assertEquals("TRACK_SCRATCH", CommunityRoundEventType.validate("Ciclismo de Pista", "TRACK_SCRATCH"));
        assertEquals("TRACK_TIME_TRIAL", CommunityRoundEventType.validate("Ciclismo de Pista", "TRACK_TIME_TRIAL"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Ciclismo de Pista", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Ciclismo de Pista", "TIME_TRIAL"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Ciclismo de Pista", null)); // historical
        assertTrue(CommunityRoundEventType.isRaceEvent("Ciclismo de Pista", "TRACK_TIME_TRIAL"));
        assertTrue(CommunityRoundEventType.isRaceEvent("Ciclismo de Pista", "TRACK_SCRATCH"));
        assertTrue(CommunityRoundEventType.isChampionshipRaceEvent("Ciclismo de Pista", "TRACK_SCRATCH"));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent("Ciclismo de Pista", "TRACK_TIME_TRIAL"));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent("Ciclismo de Pista", null));
        assertFalse(CommunityRoundEventType.isChampionshipRaceEvent("Ciclismo de Estrada", "TRACK_SCRATCH"));
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
