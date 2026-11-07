package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityRoundEntryResponse;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapts already persisted community gross stroke-play results to the
 * tournament ranking engine. A round is accepted only in its entirety.
 * This does not link rounds or publish tournaments.
 */
final class CommunityGolfTournamentRoundAdapter {
    private CommunityGolfTournamentRoundAdapter() {}

    static List<CommunityGolfTournamentStandings.Standing> calculate(CommunityRoundResponse round) {
        if (round == null || !"Golfe".equals(round.sport())
                || !CommunityGolfStrokeResults.isGolfStrokeEvent(round.eventType())
                || round.entries() == null || round.entries().isEmpty())
            throw new IllegalArgumentException("Jornada de golfe inválida");
        int holes = "GOLF_STROKE_9".equals(round.eventType()) ? 9 : 18;
        List<CommunityGolfTournamentStandings.Score> scores = new ArrayList<>();
        for (CommunityRoundEntryResponse entry : round.entries()) {
            if (entry == null || entry.communityPlayerId() == null
                    || entry.raceDisqualified() || entry.performanceValue() == null)
                throw new IllegalArgumentException("Resultados de golfe incompletos");
            scores.add(new CommunityGolfTournamentStandings.Score(
                    entry.communityPlayerId(), entry.performanceValue()));
        }
        return CommunityGolfTournamentStandings.calculate(holes, scores);
    }
}
