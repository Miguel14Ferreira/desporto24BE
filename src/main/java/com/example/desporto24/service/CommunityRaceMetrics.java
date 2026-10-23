package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Preliminary race timing schema, never the points/scoring rules of a federation. */
final class CommunityRaceMetrics {
    private CommunityRaceMetrics() {}

    private static final Set<String> RACE_SPORTS = Set.of(
            "Automobilismo", "BMX", "Canoagem", "Canoagem Slalom",
            "Canoagem Velocidade", "Ciclismo", "Ciclismo de Estrada",
            "Ciclismo de Pista", "Ciclocrosse", "Columbofilia",
            "Corrida", "Corrida de obstáculos", "Esqui Alpino",
            "Esqui de Fundo", "Esqui de Montanha", "Karting",
            "Motociclismo", "Motocross", "Motonáutica", "Mountain Bike",
            "Natação", "Natação em Águas Abertas", "Orientação",
            "Patinagem de Velocidade", "Pentatlo Moderno", "Remo",
            "Trail Running", "Triatlo", "Vela"
    ).stream().map(CommunityRaceMetrics::normalize)
            .collect(java.util.stream.Collectors.toUnmodifiableSet());

    static boolean isRaceSport(String sport) {
        return sport != null && RACE_SPORTS.contains(normalize(sport));
    }

    private static String normalize(String text) {
        return Normalizer.normalize(text.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        boolean race = isRaceSport(sport);
        Set<Integer> positions = new HashSet<>();
        for (CommunityRoundEntryRequest entry : entries) {
            boolean supplied = entry.finishPosition() != null
                    || entry.elapsedSeconds() != null
                    || entry.bestLapSeconds() != null
                    || entry.lapsCompleted() != null;
            if (!race && supplied) {
                throw new IllegalArgumentException(
                        "As métricas de corrida só podem ser usadas numa jornada de prova");
            }
            if (!race) continue;
            if (entry.goals() != null && entry.goals() != 0 ||
                    entry.assists() != null && entry.assists() != 0 ||
                    entry.mvp() || entry.cleanSheet()) {
                throw new IllegalArgumentException(
                        "Uma prova não pode ter golos, assistências ou prémios de futebol");
            }
            if (entry.finishPosition() != null) {
                if (entry.finishPosition() < 1 || !positions.add(entry.finishPosition())) {
                    throw new IllegalArgumentException(
                            "As posições de chegada têm de ser positivas e únicas na prova");
                }
            }
            if (entry.lapsCompleted() != null && entry.lapsCompleted() < 0) {
                throw new IllegalArgumentException("O número de voltas não pode ser negativo");
            }
            validateSeconds(entry.elapsedSeconds(), "Tempo total");
            validateSeconds(entry.bestLapSeconds(), "Melhor volta");
        }
    }

    private static void validateSeconds(BigDecimal seconds, String label) {
        if (seconds == null) return;
        if (seconds.signum() <= 0 || seconds.scale() > 3
                || seconds.precision() - seconds.scale() > 8) {
            throw new IllegalArgumentException(
                    label + ": indica um número de segundos positivo com até 3 decimais");
        }
    }
}
