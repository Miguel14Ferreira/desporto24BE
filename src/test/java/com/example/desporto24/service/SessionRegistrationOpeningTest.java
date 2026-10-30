package com.example.desporto24.service;

import com.example.desporto24.enumeration.RegistrationOpeningMode;
import com.example.desporto24.model.Session;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class SessionRegistrationOpeningTest {
    private final Instant now = Instant.parse("2026-10-25T12:00:00Z");

    @Test void legacySessionsRemainOpen() {
        assertTrue(SessionRegistrationOpening.isOpen(new Session(), now));
    }

    @Test void manualCallupRemainsClosedWithoutOrganizer() {
        Session session = new Session();
        session.setRegistrationOpeningMode(RegistrationOpeningMode.MANUAL);
        assertFalse(SessionRegistrationOpening.isOpen(session, now));
    }

    @Test void scheduledCallupOpensExactlyOnServerTime() {
        Session session = new Session();
        session.setRegistrationOpeningMode(RegistrationOpeningMode.SCHEDULED);
        session.setRegistrationOpensAt(now);
        assertFalse(SessionRegistrationOpening.isOpen(session, now.minusNanos(1)));
        assertTrue(SessionRegistrationOpening.isOpen(session, now));
        assertTrue(SessionRegistrationOpening.isOpen(session, now.plusSeconds(1)));
    }

    @Test void brokenScheduledConfigurationFailsClosed() {
        Session session = new Session();
        session.setRegistrationOpeningMode(RegistrationOpeningMode.SCHEDULED);
        assertFalse(SessionRegistrationOpening.isOpen(session, now));
    }

    @Test void immediateCallupAllowsJoining() {
        Session session = new Session();
        session.setRegistrationOpeningMode(RegistrationOpeningMode.IMMEDIATE);
        assertTrue(SessionRegistrationOpening.isOpen(session, now));
    }
}
