package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityTeamFinalScore;
import java.util.List;

/**
 * Organizer-confirmed team totals (goals, rugby/football points, or baseball/
 * softball runs), without inferring touchdowns, conversions, individual
 * scorers, official rosters or league standings. Ice hockey
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
                || "LACROSSE_M_OT".equals(type)
                || "AMFOOTBALL_SCORE".equals(type)
                || "FLAGFOOTBALL_SCORE".equals(type)
                || "CURLING_REG".equals(type)
                || "CURLING_EXTRA".equals(type)
                || "PETANQUE_13".equals(type)
                || "PETANQUE_11".equals(type)
                || "BOWLING_TENPIN".equals(type)
                || "POOL_9_RACKS".equals(type)
                || "FENCING_POOL_5".equals(type)
                || "FENCING_DE_15".equals(type);
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
                || "Futebol Americano".equals(sport) && "AMFOOTBALL_SCORE".equals(eventType)
                || "Flag Football".equals(sport) && "FLAGFOOTBALL_SCORE".equals(eventType)
                || "Curling".equals(sport) && (
                        "CURLING_REG".equals(eventType) || "CURLING_EXTRA".equals(eventType))
                || "Petanca".equals(sport) && (
                        "PETANQUE_13".equals(eventType) || "PETANQUE_11".equals(eventType))
                || "Bowling".equals(sport) && "BOWLING_TENPIN".equals(eventType)
                || "Bilhar".equals(sport) && "POOL_9_RACKS".equals(eventType)
                || "Esgrima".equals(sport) && (
                        "FENCING_POOL_5".equals(eventType) || "FENCING_DE_15".equals(eventType))
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
        // World Curling: tied regulation requires extra ends. An extra-end
        // result denotes a decided game, but the winning margin is NOT capped
        // at one (several stones can score in an end).
        if ("CURLING_EXTRA".equals(eventType) && score.sideA().equals(score.sideB()))
            throw new IllegalArgumentException("Após extra ends, o Curling exige um vencedor");
        // IBF tenpin: each individual game's score is in [0, 300].
        // These totals are organizer-confirmed; no invented ten frames/strikes.
        if ("BOWLING_TENPIN".equals(eventType)
                && (score.sideA() > 300 || score.sideB() > 300))
            throw new IllegalArgumentException(
                    "Bowling de dez pinos exige dois totais inteiros entre 0 e 300");
        // WPA 9-ball singles: racks won per side in a *completed* match.
        // Competition-specific race length is not inferred from the totals.
        if ("POOL_9_RACKS".equals(eventType)
                && (score.sideA().equals(score.sideB())
                || Math.max(score.sideA(), score.sideB()) == 0))
            throw new IllegalArgumentException(
                    "Bilhar de nove bolas exige vencedor por racks num jogo concluído");
        // FIE individual pool and direct elimination: bouts may finish below
        // 5/15 hits after the time limit. Equal-hit priority decisions cannot
        // be represented without a separate priority-winner field, so reject
        // tied totals rather than inventing an official winner.
        int fencingMax = "FENCING_POOL_5".equals(eventType) ? 5
                : "FENCING_DE_15".equals(eventType) ? 15 : 0;
        if (fencingMax > 0 && (score.sideA() > fencingMax
                || score.sideB() > fencingMax || score.sideA().equals(score.sideB())))
            throw new IllegalArgumentException(
                    "Esgrima: regista 1 vencedor por toques, dentro do limite da prova; decisões por prioridade não são suportadas");
        // FIPJP: the selected untimed game targets 13, or 11 in permitted
        // league/qualifying formats. Timed/shortened games need a separate type.
        int petanqueTarget = "PETANQUE_13".equals(eventType) ? 13
                : "PETANQUE_11".equals(eventType) ? 11 : 0;
        if (petanqueTarget > 0 && (
                Math.max(score.sideA(), score.sideB()) != petanqueTarget
                || Math.min(score.sideA(), score.sideB()) >= petanqueTarget))
            throw new IllegalArgumentException(
                    "Petanca: uma equipa tem de atingir exatamente os pontos do formato selecionado");
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
        // This explicitly selected match format is 1v1, rather than
        // claiming doubles, teams, other bowling styles or billiards games.
        if (("BOWLING_TENPIN".equals(eventType) || "POOL_9_RACKS".equals(eventType)
                || fencingMax > 0)
                && (a != 1 || b != 1))
            throw new IllegalArgumentException(
                    "Este confronto individual exige exatamente um jogador em cada lado");
        // FIPJP permits singles (1v1), doubles (2v2), or triples (3v3).
        if (petanqueTarget > 0 && (a != b || a > 3))
            throw new IllegalArgumentException(
                    "Petanca exige singulares, pares ou triplos com equipas da mesma dimensão");
        return score;
    }

    static CommunityTeamFinalScore decode(Integer a, Integer b) {
        if (a == null && b == null) return null;
        if (a == null || b == null) throw new IllegalStateException("Marcador coletivo incompleto");
        return new CommunityTeamFinalScore(a, b);
    }
}
