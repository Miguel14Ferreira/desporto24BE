package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Foot orienteering elapsed time only, entered by the community organizer.
 * Neither correct control punching nor comparable courses are represented.
 * Accordingly, no invented rankings, finish positions or championship points.
 */
final class CommunityOrienteeringResults {
    private CommunityOrienteeringResults() {}

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        if (!"Orientação".equals(sport) || entries == null || entries.isEmpty())
            throw new IllegalArgumentException("Orientação exige participantes");
        Set<String> usernames = new HashSet<>();
        for (CommunityRoundEntryRequest entry : entries) {
            if (entry == null || entry.username() == null || entry.username().isBlank()
                    || !usernames.add(entry.username().trim().toLowerCase(Locale.ROOT))
                    || entry.result() != CommunityMatchResult.DRAW
                    || entry.team() != null && !entry.team().isBlank()
                    || entry.finishPosition() != null
                    || entry.bestLapSeconds() != null || entry.lapsCompleted() != null
                    || entry.goals() != null && entry.goals() != 0
                    || entry.assists() != null && entry.assists() != 0
                    || entry.mvp() || entry.cleanSheet()
                    || entry.performanceValue() != null
                    || entry.waveScore1() != null || entry.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Orientação regista apenas participantes e tempos, sem posições verificadas ou métricas de outros desportos");
            BigDecimal elapsed = entry.elapsedSeconds();
            if (elapsed != null && (elapsed.signum() <= 0 || elapsed.scale() > 0
                    || elapsed.precision() > 8))
                throw new IllegalArgumentException(
                        "Tempo de Orientação: segundos inteiros positivos até oito dígitos");
        }
    }
}
