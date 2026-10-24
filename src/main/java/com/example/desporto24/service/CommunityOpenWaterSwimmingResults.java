package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Community-entered individual open-water 5 km/10 km results. Distance is
 * required in eventDiscipline. Times are recorded to tenths of a second.
 * No inferred finish positions, classification points, penalties or official
 * World Aquatics adjudication. Multi-round knockout and relays are excluded.
 */
final class CommunityOpenWaterSwimmingResults {
    private CommunityOpenWaterSwimmingResults() {}

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        if (!"Natação em Águas Abertas".equals(sport) || entries == null || entries.isEmpty())
            throw new IllegalArgumentException(
                    "Natação em Águas Abertas exige pelo menos um nadador");
        Set<String> usernames = new HashSet<>();
        for (CommunityRoundEntryRequest e : entries) {
            if (e == null || e.username() == null || e.username().isBlank()
                    || !usernames.add(e.username().trim().toLowerCase(Locale.ROOT))
                    || e.result() != CommunityMatchResult.DRAW
                    || e.team() != null && !e.team().isBlank()
                    || e.finishPosition() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null) {
                throw new IllegalArgumentException(
                        "Águas Abertas regista participantes e tempos, sem posições automáticas, equipas ou estatísticas de outros desportos");
            }
            BigDecimal time = e.elapsedSeconds();
            if (time != null && (time.signum() <= 0 || time.scale() > 1
                    || time.precision() - time.scale() > 8)) {
                throw new IllegalArgumentException(
                        "Tempo de Águas Abertas: segundos positivos com até uma casa decimal");
            }
        }
    }
}
