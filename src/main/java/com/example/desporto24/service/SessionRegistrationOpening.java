package com.example.desporto24.service;

import com.example.desporto24.enumeration.RegistrationOpeningMode;
import com.example.desporto24.model.Session;
import java.time.Instant;

/** No scheduler needed: opening is evaluated against server time at each join. */
public final class SessionRegistrationOpening {
    private SessionRegistrationOpening() { }

    public static boolean isOpen(Session session, Instant now) {
        RegistrationOpeningMode mode = session.getRegistrationOpeningMode();
        if (mode == null || mode == RegistrationOpeningMode.IMMEDIATE) return true;
        if (mode == RegistrationOpeningMode.MANUAL) return false;
        return session.getRegistrationOpensAt() != null
                && !now.isBefore(session.getRegistrationOpensAt());
    }
}
