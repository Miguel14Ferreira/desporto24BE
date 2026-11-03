package com.example.desporto24.enumeration;

/**
 * Null on historical tournaments means legacy numeric win/draw/loss scoring.
 * Only supported implementations should be added here.
 */
public enum TournamentScoringMode {
    CHESS_HALF_POINTS,
    RACE_POINTS,
    SWIM_BEST_TIME,
    CANOE_BEST_TIME,
    FIELD_BEST_MARK,
    TRACK_BEST_TIME,
    RELAY_BEST_TIME
}
