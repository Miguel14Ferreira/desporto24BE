package com.example.desporto24.service;

import com.example.desporto24.model.Session;
import java.util.Locale;

/** Minimum to be considered ready. Occupancy (maxParticipants) remains independent. */
public final class SessionMinimumPlayers {
    private SessionMinimumPlayers() {}

    public static int suggested(String sport, int capacity) {
        int maximum = Math.max(2, capacity);
        String name = sport == null ? "" : sport.trim().toLowerCase(Locale.ROOT);
        // Small-sided games remain possible; a 4-person futsal game is not ready.
        int proposed = name.contains("futebol") || name.contains("futsal")
                || name.contains("basquetebol") || name.contains("basketball") ? 6
                : name.contains("padel") || name.contains("pádel") ? 4 : 2;
        return Math.min(proposed, maximum);
    }

    public static int required(Session session) {
        int capacity = session.getMaxParticipants() == null ? 2 : session.getMaxParticipants();
        Integer custom = session.getMinParticipants();
        return custom != null && custom >= 2 && custom <= capacity
                ? custom : suggested(session.getSport(), capacity);
    }

    public static int validate(Integer requested, String sport, Integer capacity) {
        if (capacity == null || capacity < 2 || capacity > 100)
            throw new IllegalArgumentException("Lotação inválida");
        int minimum = requested == null ? suggested(sport, capacity) : requested;
        if (minimum < 2 || minimum > capacity)
            throw new IllegalArgumentException("O mínimo de jogadores deve estar entre 2 e a lotação");
        return minimum;
    }
}
