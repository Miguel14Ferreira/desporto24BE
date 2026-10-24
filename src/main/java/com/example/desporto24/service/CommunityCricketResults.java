package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityCricketDetails;
import com.example.desporto24.model.dto.community.CommunityTeamFinalScore;

/**
 * Completed uninterrupted regular T20 innings ONLY.
 * Does not calculate DLS, Super Overs, wickets won, net run rate, or table points.
 */
final class CommunityCricketResults {
    private CommunityCricketResults() {}

    static CommunityCricketDetails validate(String sport, String eventType,
            CommunityTeamFinalScore runs, CommunityCricketDetails details) {
        if (!"CRICKET_T20".equals(eventType)) {
            if (details != null)
                throw new IllegalArgumentException("Detalhes de Cricket incompatíveis com esta prova");
            return null;
        }
        if (!"Cricket".equals(sport) || runs == null || details == null)
            throw new IllegalArgumentException("Cricket T20 exige duas innings completas");
        validateSide(details.wicketsA(), details.legalBallsA());
        validateSide(details.wicketsB(), details.legalBallsB());
        if (details.wicketsA() != 10 && details.legalBallsA() != 120)
            throw new IllegalArgumentException(
                    "Primeira innings T20 incompleta: 20 overs ou all out");
        if (details.wicketsB() != 10 && details.legalBallsB() != 120
                && runs.sideB() <= runs.sideA())
            throw new IllegalArgumentException(
                    "Segunda innings T20 incompleta: chase conseguido, 20 overs ou all out");
        return details;
    }

    private static void validateSide(Integer wickets, Integer balls) {
        if (wickets == null || balls == null || wickets < 0 || wickets > 10
                || balls < 1 || balls > 120)
            throw new IllegalArgumentException(
                    "T20: wickets de 0 a 10 e bolas legais de 1 a 120");
    }

    static CommunityCricketDetails decode(Integer wicketsA, Integer ballsA,
            Integer wicketsB, Integer ballsB) {
        if (wicketsA == null && ballsA == null && wicketsB == null && ballsB == null)
            return null; // Historical and non-cricket rows unchanged.
        if (wicketsA == null || ballsA == null || wicketsB == null || ballsB == null)
            throw new IllegalStateException("Detalhes incompletos de Cricket");
        return new CommunityCricketDetails(wicketsA, ballsA, wicketsB, ballsB);
    }
}
