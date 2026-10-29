package com.example.desporto24.service;

import com.example.desporto24.model.Session;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SessionMinimumPlayersTest {

    @Test void footballRequiresPlayableMinimumWithoutMakingCapacityMandatory() {
        assertEquals(6, SessionMinimumPlayers.suggested("Futebol", 10));
        assertEquals(6, SessionMinimumPlayers.suggested("Futsal", 10));
        assertEquals(4, SessionMinimumPlayers.suggested("Futsal", 4));
        assertEquals(4, SessionMinimumPlayers.suggested("Padel", 4));
        assertEquals(2, SessionMinimumPlayers.suggested("Ténis", 2));
    }

    @Test void organiserCanSetMinimumIndependentlyOfCapacity() {
        assertEquals(8, SessionMinimumPlayers.validate(8, "Futebol", 10));
        assertThrows(IllegalArgumentException.class,
                () -> SessionMinimumPlayers.validate(11, "Futsal", 10));
        assertThrows(IllegalArgumentException.class,
                () -> SessionMinimumPlayers.validate(1, "Futsal", 10));
    }

    @Test void existingSessionsWithNoStoredValueRemainValid() {
        Session old = Session.builder().sport("Futsal").maxParticipants(10).build();
        assertEquals(6, SessionMinimumPlayers.required(old));
        old.setMinParticipants(8);
        assertEquals(8, SessionMinimumPlayers.required(old));
    }
}
