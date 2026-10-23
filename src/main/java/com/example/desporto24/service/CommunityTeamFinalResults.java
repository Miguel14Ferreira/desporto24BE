package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityTeamFinalScore;
import java.util.List;

/**
 * Organizer-confirmed full-time team totals, without inferring goal scorers,
 * tries/conversions, extra time, official roster size or league standings.
 * Draws are allowed: knockout tie-break details are not recorded here.
 */
final class CommunityTeamFinalResults {
    private CommunityTeamFinalResults() {}

    static boolean isTeamFinalEvent(String type) {
        return "HANDBALL_FINAL".equals(type) || "RUGBY_FINAL".equals(type)
                || "RUGBY_SEVENS_FINAL".equals(type);
    }

    static CommunityTeamFinalScore validate(String sport, String eventType,
            List<CommunityRoundEntryRequest> entries, CommunityTeamFinalScore score) {
        if (!isTeamFinalEvent(eventType)) {
            if (score != null)
                throw new IllegalArgumentException("Marcador coletivo incompatível com a prova");
            return null;
        }
        boolean allowed = "Andebol".equals(sport) && "HANDBALL_FINAL".equals(eventType)
                || "Rugby".equals(sport) && "RUGBY_FINAL".equals(eventType)
                || "Rugby Sevens".equals(sport) && "RUGBY_SEVENS_FINAL".equals(eventType);
        if (!allowed) throw new IllegalArgumentException("A prova não corresponde à modalidade");
        // Storage/API guard, not an official sporting score ceiling.
        if (score == null || score.sideA() == null || score.sideB() == null
                || score.sideA() < 0 || score.sideB() < 0
                || score.sideA() > 999 || score.sideB() > 999)
            throw new IllegalArgumentException("Regista dois resultados finais inteiros de 0 a 999");
        int a = 0, b = 0;
        for (CommunityRoundEntryRequest e : entries) {
            if ("A".equals(e.team())) a++;
            else if ("B".equals(e.team())) b++;
            else throw new IllegalArgumentException("Associa cada jogador à equipa A ou B");
            if (e.result() != CommunityMatchResult.DRAW
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException(
                        "O resultado pertence à equipa, sem golos individuais de futebol");
        }
        if (a == 0 || b == 0)
            throw new IllegalArgumentException("Regista participantes nas duas equipas");
        return score;
    }

    static CommunityTeamFinalScore decode(Integer a, Integer b) {
        if (a == null && b == null) return null;
        if (a == null || b == null) throw new IllegalStateException("Marcador coletivo incompleto");
        return new CommunityTeamFinalScore(a, b);
    }
}
