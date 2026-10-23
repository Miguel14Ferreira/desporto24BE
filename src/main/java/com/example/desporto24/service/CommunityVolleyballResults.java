package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunitySetScore;
import java.util.ArrayList;
import java.util.List;

/**
 * Completed volleyball/beach volleyball matches and separately configured footvolley
 * matches (18-point single, 18/18/15 best-of-three, 15-point single).
 * This is not a forfeit/incomplete-team rulebook, and it does
 * not feed the generic football W/D/L standings. Scores belong to sides A/B.
 */
final class CommunityVolleyballResults {
    private CommunityVolleyballResults() {}

    static boolean isVolleyballEvent(String eventType) {
        return "VOLLEYBALL_FINAL".equals(eventType) || "BEACH_VOLLEY_FINAL".equals(eventType)
                || "FOOTVOLLEY_SINGLE_18".equals(eventType)
                || "FOOTVOLLEY_BEST_OF_3".equals(eventType)
                || "FOOTVOLLEY_SINGLE_15".equals(eventType);
    }

    static String validateAndEncode(String sport, String eventType,
            String matchFormat, List<CommunitySetScore> scores,
            List<CommunityRoundEntryRequest> entries) {
        boolean footvolley = "FOOTVOLLEY_SINGLE_18".equals(eventType)
                || "FOOTVOLLEY_BEST_OF_3".equals(eventType)
                || "FOOTVOLLEY_SINGLE_15".equals(eventType);
        if (!("VOLLEYBALL_FINAL".equals(eventType) && "Voleibol".equals(sport)
                || "BEACH_VOLLEY_FINAL".equals(eventType) && "Voleibol de Praia".equals(sport)
                || footvolley && "Futevólei".equals(sport)))
            throw new IllegalArgumentException("Prova de Voleibol incompatível com a modalidade");
        if (matchFormat != null && !matchFormat.isBlank())
            throw new IllegalArgumentException("Voleibol não utiliza o formato singulares/pares de raquete");
        final boolean beach = "BEACH_VOLLEY_FINAL".equals(eventType);
        final boolean footvolleySingle = "FOOTVOLLEY_SINGLE_18".equals(eventType)
                || "FOOTVOLLEY_SINGLE_15".equals(eventType);
        final int needed = footvolleySingle ? 1 : beach || footvolley ? 2 : 3;
        final int maxSets = footvolleySingle ? 1 : beach || footvolley ? 3 : 5;
        if (scores == null || scores.size() < needed || scores.size() > maxSets)
            throw new IllegalArgumentException("O confronto exige os sets completos até à vitória");

        int wonA = 0, wonB = 0;
        List<String> encoded = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            CommunitySetScore set = scores.get(i);
            // Footvolley competition formats: single 18 or 15; best-of-three 18/18/15.
            // Beach volleyball is 21/21/15; indoor is 25/25/25/25/15.
            // At least a 2-point lead; no artificial deuce cap.
            int target = "FOOTVOLLEY_SINGLE_15".equals(eventType) ? 15
                    : footvolley ? i == 2 ? 15 : 18
                    : i == maxSets - 1 ? 15 : beach ? 21 : 25;
            if (set == null || set.sideA() == null || set.sideB() == null
                    || set.sideA() < 0 || set.sideB() < 0
                    || set.sideA() > 999 || set.sideB() > 999)
                throw new IllegalArgumentException("Set com pontuação inválida");
            int high = Math.max(set.sideA(), set.sideB());
            int low = Math.min(set.sideA(), set.sideB());
            // The winning side must reach the target FIRST (or win by 2 after deuce).
            if (high < target || high - low < 2
                    || (low < target - 1 && high != target)
                    || (low >= target - 1 && high != low + 2))
                throw new IllegalArgumentException("Set não cumpre a pontuação e diferença de dois pontos");
            if (set.sideA() > set.sideB()) wonA++; else wonB++;
            encoded.add(set.sideA() + ":" + set.sideB());
            // A match must stop as soon as either side has won enough sets.
            if ((wonA == needed || wonB == needed) && i != scores.size() - 1)
                throw new IllegalArgumentException("Não registes sets depois da vitória final");
        }
        if (wonA != needed && wonB != needed)
            throw new IllegalArgumentException("Os sets ainda não determinam o vencedor do jogo");

        int a = 0, b = 0;
        for (CommunityRoundEntryRequest e : entries) {
            if ("A".equals(e.team())) a++;
            else if ("B".equals(e.team())) b++;
            else throw new IllegalArgumentException("Associa cada participante à equipa A ou B");
            if (e.result() != CommunityMatchResult.DRAW
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Voleibol guarda apenas participantes e pontos por equipa/set, não estatísticas de futebol");
        }
        // Indoor registration can be partial; footvolley and beach volleyball use pairs.
        if (beach || footvolley ? a != 2 || b != 2 : a < 1 || b < 1)
            throw new IllegalArgumentException(footvolley
                    ? "Futevólei exige dois participantes registados por lado"
                    : beach ? "Voleibol de Praia exige dois participantes registados por lado"
                    : "Regista pelo menos um participante em cada equipa");
        return String.join(",", encoded); // Existing set_scores VARCHAR(255), no migration.
    }
}
