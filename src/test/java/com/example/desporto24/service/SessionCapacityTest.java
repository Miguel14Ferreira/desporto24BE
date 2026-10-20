package com.example.desporto24.service;

import com.example.desporto24.model.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionCapacityTest {

    @Test
    void legacyNullExternalCountMeansZero() {
        Session session = new Session();
        session.setMaxParticipants(10);
        session.setExternalConfirmedParticipants(null);

        assertEquals(1, SessionCapacity.totalConfirmed(session, 1));
        assertEquals(9, SessionCapacity.remainingPlaces(session, 1));
    }

    @Test
    void externalCountAndActiveProfilesShareTheSameCapacity() {
        Session session = new Session();
        session.setMaxParticipants(10);
        session.setExternalConfirmedParticipants(7);

        assertEquals(8, SessionCapacity.totalConfirmed(session, 1));
        assertEquals(2, SessionCapacity.remainingPlaces(session, 1));
        assertEquals(0, SessionCapacity.remainingPlaces(session, 3));
        assertEquals(0, SessionCapacity.remainingPlaces(session, 4));
    }

    @Test
    void negativeCountsAreRejected() {
        Session session = new Session();
        session.setMaxParticipants(10);
        assertThrows(IllegalArgumentException.class, () -> SessionCapacity.totalConfirmed(session, -1));
        session.setExternalConfirmedParticipants(-1);
        assertThrows(IllegalArgumentException.class, () -> SessionCapacity.totalConfirmed(session, 1));
    }
}
