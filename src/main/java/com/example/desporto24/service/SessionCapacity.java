package com.example.desporto24.service;

import com.example.desporto24.model.Session;

/**
 * Seat accounting: active Jynlo registrations plus confirmed people outside the app.
 * The organiser is an active Jynlo participant and must not be counted twice.
 */
public final class SessionCapacity {

    private SessionCapacity() {
    }

    public static int totalConfirmed(Session session, long activeParticipants) {
        if (activeParticipants < 0) {
            throw new IllegalArgumentException("Active participant count cannot be negative");
        }
        int external = session.getExternalConfirmedParticipants() == null
                ? 0 : session.getExternalConfirmedParticipants();
        if (external < 0) {
            throw new IllegalArgumentException("External confirmed participant count cannot be negative");
        }
        return Math.toIntExact(Math.addExact(activeParticipants, external));
    }

    public static int remainingPlaces(Session session, long activeParticipants) {
        int maximum = session.getMaxParticipants() == null ? 0 : session.getMaxParticipants();
        return Math.max(0, maximum - totalConfirmed(session, activeParticipants));
    }
}
