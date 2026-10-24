package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Explicit organizer-confirmed WKF-2026-style individual result summary.
 * Kumite points use two final integer scores. At tied points, SENSHU or
 * HANTEI must be confirmed by the official; it is never inferred here.
 * Kata is a duel decided by judges' majority vote, NOT an aggregate mark.
 * This does not adjudicate a contest, certify per-technique scores,
 * record team kata, sanctions, round-robin draws, or season standings.
 */
final class CommunityKarateResults {
    private CommunityKarateResults() {}

    static boolean isKarateEvent(String eventType) {
        return "KARATE_KUMITE_POINTS".equals(eventType)
                || "KARATE_KUMITE_SENSHU".equals(eventType)
                || "KARATE_KUMITE_HANTEI".equals(eventType)
                || "KARATE_KATA_JUDGES".equals(eventType);
    }

    static boolean isKumite(String type) {
        return isKarateEvent(type) && !"KARATE_KATA_JUDGES".equals(type);
    }

    static void validate(String sport, String type, List<CommunityRoundEntryRequest> entries) {
        if (!"Karaté".equals(sport) || !isKarateEvent(type)
                || entries == null || entries.size() != 2)
            throw new IllegalArgumentException("Karaté exige exatamente dois atletas e um formato válido");
        CommunityRoundEntryRequest aka = entries.stream()
                .filter(e -> "AKA".equals(e.team())).findFirst().orElse(null);
        CommunityRoundEntryRequest ao = entries.stream()
                .filter(e -> "AO".equals(e.team())).findFirst().orElse(null);
        if (aka == null || ao == null
                || !((aka.result() == CommunityMatchResult.WIN
                        && ao.result() == CommunityMatchResult.LOSS)
                    || (aka.result() == CommunityMatchResult.LOSS
                        && ao.result() == CommunityMatchResult.WIN)))
            throw new IllegalArgumentException("Karaté exige AKA (vermelho), AO (azul) e um vencedor");
        for (CommunityRoundEntryRequest e : entries) {
            if (e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Karaté não admite estatísticas de futebol, corrida ou ondas");
            BigDecimal score = e.performanceValue();
            if (isKumite(type)) {
                if (score == null || score.signum() < 0
                        || score.compareTo(new BigDecimal("999999")) > 0
                        || score.stripTrailingZeros().scale() > 0)
                    throw new IllegalArgumentException(
                            "Kumite exige os dois totais inteiros confirmados de pontos");
            } else if (score != null) {
                throw new IllegalArgumentException(
                        "Kata é decidido por maioria dos votos dos juízes, não por uma soma de notas");
            }
        }
        if (isKumite(type)) {
            int cmp = aka.performanceValue().compareTo(ao.performanceValue());
            if ("KARATE_KUMITE_POINTS".equals(type)) {
                if (cmp == 0
                        || cmp > 0 && aka.result() != CommunityMatchResult.WIN
                        || cmp < 0 && ao.result() != CommunityMatchResult.WIN)
                    throw new IllegalArgumentException(
                            "Em Kumite por pontos vence quem tem o maior total confirmado");
            } else if (cmp != 0) {
                throw new IllegalArgumentException(
                        "Uma decisão por SENSHU ou HANTEI só se aplica a totais empatados");
            } else if ("KARATE_KUMITE_SENSHU".equals(type)
                    && aka.performanceValue().signum() == 0) {
                throw new IllegalArgumentException(
                        "SENSHU exige um primeiro ponto sem resposta, não pode existir com 0–0");
            }
        }
    }
}
