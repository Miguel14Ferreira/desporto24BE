package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Community-entered individual K1 times only: no K2/K4 crews, heats or official points. */
final class CommunityCanoeSprintResults {
    private CommunityCanoeSprintResults() {}

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        if (!"Canoagem Velocidade".equals(sport) || entries == null || entries.isEmpty())
            throw new IllegalArgumentException("K1 exige participantes individuais");
        Set<String> usernames = new HashSet<>();
        for (CommunityRoundEntryRequest entry : entries) {
            if (entry == null || entry.username() == null || entry.username().isBlank()
                    || !usernames.add(entry.username().trim().toLowerCase(Locale.ROOT))
                    || entry.result() != CommunityMatchResult.DRAW
                    || entry.team() != null && !entry.team().isBlank()
                    || entry.finishPosition() != null || entry.bestLapSeconds() != null
                    || entry.lapsCompleted() != null
                    || entry.goals() != null && entry.goals() != 0
                    || entry.assists() != null && entry.assists() != 0
                    || entry.mvp() || entry.cleanSheet()
                    || entry.performanceValue() != null
                    || entry.waveScore1() != null || entry.waveScore2() != null)
                throw new IllegalArgumentException(
                        "K1 só aceita atletas e tempos individuais, sem vitórias de futebol ou posições inventadas");
            BigDecimal time = entry.elapsedSeconds();
            if (time != null && (time.signum() <= 0 || time.stripTrailingZeros().scale() > 3
                    || time.precision() - time.scale() > 8))
                throw new IllegalArgumentException(
                        "Tempo K1 inválido: segundos positivos com até três casas decimais");
        }
    }
}
