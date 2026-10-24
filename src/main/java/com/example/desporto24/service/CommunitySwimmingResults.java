package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Organizer-recorded individual 50 m pool (long-course) swimming times.
 * Stroke/distance are mandatory separate event metadata, never inferred from time.
 * No official rankings, heats, lane assignment, DSQ or World Aquatics points.
 */
final class CommunitySwimmingResults {
    private CommunitySwimmingResults() {}

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        if (!"Natação".equals(sport) || entries == null || entries.isEmpty())
            throw new IllegalArgumentException("Natação em piscina exige participantes");
        Set<String> usernames = new HashSet<>();
        for (CommunityRoundEntryRequest e : entries) {
            if (e == null || e.username() == null || e.username().isBlank()
                    || !usernames.add(e.username().trim().toLowerCase(Locale.ROOT))
                    || e.result() != CommunityMatchResult.DRAW
                    || e.team() != null && !e.team().isBlank()
                    || e.finishPosition() != null || e.bestLapSeconds() != null
                    || e.lapsCompleted() != null
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Natação em piscina admite apenas participantes e tempos individuais, sem posições ou estatísticas de outros desportos");
            BigDecimal time = e.elapsedSeconds();
            if (time != null && (time.signum() <= 0 || time.scale() > 2
                    || time.precision() - time.scale() > 8))
                throw new IllegalArgumentException(
                        "Tempo de Natação inválido: segundos positivos com até duas casas decimais");
        }
    }
}
