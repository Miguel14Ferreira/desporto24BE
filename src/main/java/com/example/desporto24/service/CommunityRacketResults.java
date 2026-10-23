package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunitySetScore;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Racket results are intentionally independent of community W/D/L scoring.
 * Only completed singles/doubles matches are accepted; no fake tie-breaks or
 * federation-specific point awards.
 */
final class CommunityRacketResults {
    private CommunityRacketResults() {}

    private static final Set<String> SPORTS = Set.of(
            "Ténis", "Padel", "Badminton", "Squash",
            "Pickleball", "Ténis de mesa", "Ténis de praia"
    ).stream().map(CommunityRacketResults::normalize)
            .collect(java.util.stream.Collectors.toUnmodifiableSet());

    static boolean isSupported(String sport) {
        return sport != null && SPORTS.contains(normalize(sport));
    }

    private static String normalize(String sport) {
        return Normalizer.normalize(sport.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }

    static String validateAndEncode(String sport, String matchFormat,
            List<CommunitySetScore> scores, List<CommunityRoundEntryRequest> entries) {
        boolean supplied = matchFormat != null || scores != null;
        if (!supplied) return null; // Legacy W/D/L rows retain previous behavior.
        if (!isSupported(sport)) {
            throw new IllegalArgumentException(
                    "Resultados por sets só estão disponíveis para modalidades de raquete suportadas");
        }
        if (!"SINGLES".equals(matchFormat) && !"DOUBLES".equals(matchFormat)) {
            throw new IllegalArgumentException("Seleciona singulares ou pares");
        }
        if (scores == null || scores.isEmpty() || scores.size() > 5) {
            throw new IllegalArgumentException("Indica entre 1 e 5 sets completos");
        }
        int playersPerSide = "SINGLES".equals(matchFormat) ? 1 : 2;
        if (entries.size() != playersPerSide * 2) {
            throw new IllegalArgumentException(
                    "O número de jogadores não corresponde ao formato do confronto");
        }

        int sideA = 0;
        int sideB = 0;
        int wonA = 0;
        int wonB = 0;
        List<String> encoded = new ArrayList<>();
        for (CommunitySetScore score : scores) {
            if (score == null || score.sideA() == null || score.sideB() == null
                    || score.sideA() < 0 || score.sideB() < 0
                    || score.sideA() > 99 || score.sideB() > 99
                    || score.sideA().equals(score.sideB())) {
                throw new IllegalArgumentException("Cada set deve ter um vencedor e pontuações válidas");
            }
            if (score.sideA() > score.sideB()) wonA++; else wonB++;
            encoded.add(score.sideA() + ":" + score.sideB());
        }
        if (wonA == wonB) {
            throw new IllegalArgumentException("O confronto tem de ter um vencedor pelos sets");
        }
        boolean aWins = wonA > wonB;
        for (CommunityRoundEntryRequest entry : entries) {
            if ("A".equals(entry.team())) {
                sideA++;
            } else if ("B".equals(entry.team())) {
                sideB++;
            } else {
                throw new IllegalArgumentException("Associa cada jogador ao lado A ou B");
            }
            CommunityMatchResult expected = ("A".equals(entry.team()) == aWins)
                    ? CommunityMatchResult.WIN : CommunityMatchResult.LOSS;
            if (entry.result() != expected) {
                throw new IllegalArgumentException("A vitória/derrota não corresponde aos sets");
            }
            if (entry.goals() != null && entry.goals() != 0
                    || entry.assists() != null && entry.assists() != 0
                    || entry.mvp() || entry.cleanSheet()) {
                throw new IllegalArgumentException(
                        "Resultados por sets não podem conter estatísticas de futebol");
            }
        }
        if (sideA != playersPerSide || sideB != playersPerSide) {
            throw new IllegalArgumentException("Cada lado deve ter o número correto de jogadores");
        }
        return String.join(",", encoded);
    }

    static List<CommunitySetScore> decode(String stored) {
        if (stored == null || stored.isBlank()) return List.of();
        List<CommunitySetScore> scores = new ArrayList<>();
        for (String item : stored.split(",")) {
            String[] sides = item.split(":", -1);
            if (sides.length != 2) throw new IllegalStateException("Dados dos sets inválidos");
            scores.add(new CommunitySetScore(Integer.parseInt(sides[0]),
                    Integer.parseInt(sides[1])));
        }
        return List.copyOf(scores);
    }
}
