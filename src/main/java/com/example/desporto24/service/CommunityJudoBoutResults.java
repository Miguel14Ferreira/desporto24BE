package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.util.List;

/**
 * Winner and organizer-confirmed IJF-style contest decision, not a scorecard.
 * Type identifies the winning outcome; White/Blue are judogi sides, not teams.
 * No inferred technique totals, shido scores, Golden Score clock or standings.
 */
final class CommunityJudoBoutResults {
    private CommunityJudoBoutResults() {}

    static boolean isJudoBout(String eventType) {
        return "JUDO_IPPON".equals(eventType)
                || "JUDO_AWASETE_IPPON".equals(eventType)
                || "JUDO_WAZA_ARI".equals(eventType)
                || "JUDO_YUKO".equals(eventType)
                || "JUDO_HANSOKU_MAKE".equals(eventType);
    }

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        if (!"Judo".equals(sport) || entries == null || entries.size() != 2)
            throw new IllegalArgumentException("Judo exige exatamente dois judocas");
        CommunityRoundEntryRequest white = entries.stream()
                .filter(e -> "WHITE".equals(e.team())).findFirst().orElse(null);
        CommunityRoundEntryRequest blue = entries.stream()
                .filter(e -> "BLUE".equals(e.team())).findFirst().orElse(null);
        if (white == null || blue == null)
            throw new IllegalArgumentException("Seleciona um judoca de branco e um de azul");
        if (!(white.result() == CommunityMatchResult.WIN
                    && blue.result() == CommunityMatchResult.LOSS
                || white.result() == CommunityMatchResult.LOSS
                    && blue.result() == CommunityMatchResult.WIN))
            throw new IllegalArgumentException("Judo exige um vencedor e um derrotado");
        for (CommunityRoundEntryRequest e : entries) {
            if (e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null || e.waveScore1() != null
                    || e.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Judo só admite judogi branco/azul, vencedor e forma de vitória");
        }
    }
}
