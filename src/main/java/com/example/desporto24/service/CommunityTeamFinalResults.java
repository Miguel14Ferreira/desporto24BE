package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityTeamFinalScore;
import java.util.List;

/**
 * Organizer-confirmed team totals (goals, rugby points, or baseball/softball
 * runs), without inventing
 * scorers, conversions, official rosters or league standings. Ice hockey
 * distinguishes regulation time, overtime, and shootout in the event type;
 * its official final score includes only the decisive shootout goal.
 */
final class CommunityTeamFinalResults {
    private CommunityTeamFinalResults() {}

    static boolean isTeamFinalEvent(String type) {
        return "HANDBALL_FINAL".equals(type) || "RUGBY_FINAL".equals(type)
                || "RUGBY_SEVENS_FINAL".equals(type)
                || "FUTSAL_FINAL".equals(type)
                || "FIELD_HOCKEY_FINAL".equals(type)
                || "ROLLER_HOCKEY_FINAL".equals(type)
                || "WATER_POLO_FINAL".equals(type)
                || "ICE_HOCKEY_REG".equals(type)
                || "ICE_HOCKEY_OT".equals(type)
                || "ICE_HOCKEY_SO".equals(type)
                || "BASEBALL_RUNS".equals(type)
                || "SOFTBALL_RUNS".equals(type)
                || "KORFBALL_REG".equals(type)
                || "KORFBALL_GOLDEN".equals(type)
                || "LACROSSE_M_REG".equals(type)
                || "LACROSSE_M_OT".equals(type);
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
                || "Rugby Sevens".equals(sport) && "RUGBY_SEVENS_FINAL".equals(eventType)
                || "Futsal".equals(sport) && "FUTSAL_FINAL".equals(eventType)
                || "Hóquei em Campo".equals(sport) && "FIELD_HOCKEY_FINAL".equals(eventType)
                || "Hóquei em Patins".equals(sport) && "ROLLER_HOCKEY_FINAL".equals(eventType)
                || "Polo Aquático".equals(sport) && "WATER_POLO_FINAL".equals(eventType)
                || "Basebol".equals(sport) && "BASEBALL_RUNS".equals(eventType)
                || "Softbol".equals(sport) && "SOFTBALL_RUNS".equals(eventType)
                || "Corfebol".equals(sport) && (
                        "KORFBALL_REG".equals(eventType) || "KORFBALL_GOLDEN".equals(eventType))
                || "Lacrosse".equals(sport) && (
                        "LACROSSE_M_REG".equals(eventType) || "LACROSSE_M_OT".equals(eventType))
                || "Hóquei no Gelo".equals(sport) && (
                        "ICE_HOCKEY_REG".equals(eventType)
                        || "ICE_HOCKEY_OT".equals(eventType)
                        || "ICE_HOCKEY_SO".equals(eventType));
        if (!allowed) throw new IllegalArgumentException("A prova não corresponde à modalidade");
        // These are confirmed runs totals, not runs per inning. A tied total is
        // recorded without claiming that an official winner has been decided.
        // Storage/API guard, not an official sporting score ceiling.
        if (score == null || score.sideA() == null || score.sideB() == null
                || score.sideA() < 0 || score.sideB() < 0
                || score.sideA() > 999 || score.sideB() > 999)
            throw new IllegalArgumentException("Regista dois resultados finais inteiros de 0 a 999");
        // Sudden-decision results: IKF golden goal, men's field lacrosse
        // overtime and IIHF ice hockey OT/SO all end with one decisive goal.
        // Regulation scores can be level, without implying a final winner.
        if (("KORFBALL_GOLDEN".equals(eventType) || "LACROSSE_M_OT".equals(eventType)
                || "ICE_HOCKEY_OT".equals(eventType) || "ICE_HOCKEY_SO".equals(eventType))
                && Math.abs(score.sideA() - score.sideB()) != 1)
            throw new IllegalArgumentException(
                    "O desempate por golo decisivo exige um vencedor por exatamente um golo");
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
