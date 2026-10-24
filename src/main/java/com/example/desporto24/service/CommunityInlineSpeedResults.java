package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Organizer-recorded times for the 200 m inline-speed track dual time trial.
 * Timed skaters are not automatically certified as completed heats/finals:
 * pairing, qualifying rounds, timing approval, penalties and disqualification
 * are outside the current community round request contract.
 */
final class CommunityInlineSpeedResults {
    private CommunityInlineSpeedResults() {}

    static void validate(String sport, List<CommunityRoundEntryRequest> entries) {
        if (!"Patinagem de Velocidade".equals(sport) || entries == null || entries.isEmpty())
            throw new IllegalArgumentException(
                    "Patinagem de Velocidade exige participantes");
        Set<String> skaters = new HashSet<>();
        for (CommunityRoundEntryRequest entry : entries) {
            if (entry == null || entry.username() == null
                    || entry.username().isBlank()
                    || !skaters.add(entry.username().trim().toLowerCase(Locale.ROOT))
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
                        "Patinagem de Velocidade apenas admite atletas e tempos registados, sem classificações oficiais ou métricas de outras modalidades");
            BigDecimal time = entry.elapsedSeconds();
            if (time != null && (time.signum() <= 0 || time.scale() > 2
                    || time.precision() - time.scale() > 8))
                throw new IllegalArgumentException(
                        "Tempo da prova de Patinagem: segundos positivos com até dois decimais");
        }
    }
}
