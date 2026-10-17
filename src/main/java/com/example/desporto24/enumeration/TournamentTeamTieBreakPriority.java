package com.example.desporto24.enumeration;

/**
 * Priority among the existing sporting statistics after tournament points.
 * All three remain applicable; this rule only determines which is considered first.
 */
public enum TournamentTeamTieBreakPriority {
    GOAL_DIFFERENCE_FIRST,
    GOALS_SCORED_FIRST,
    WINS_FIRST
}
