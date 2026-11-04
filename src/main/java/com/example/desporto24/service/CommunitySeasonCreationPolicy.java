package com.example.desporto24.service;

import com.example.desporto24.model.community.CommunitySeason;
import java.util.List;

/**
 * Legacy shared seasons remain exclusive. A community can instead maintain
 * one active season per sport, without overlapping shared and tagged seasons.
 */
final class CommunitySeasonCreationPolicy {
    private CommunitySeasonCreationPolicy() {}

    static void check(List<CommunitySeason> activeSeasons, String requestedSport) {
        if (activeSeasons == null || activeSeasons.isEmpty()) return;
        if (requestedSport == null || requestedSport.isBlank()) {
            throw new IllegalStateException(
                    "Encerra as épocas ativas antes de criares uma época partilhada");
        }
        for (CommunitySeason active : activeSeasons) {
            if (active.getSport() == null || active.getSport().isBlank()) {
                throw new IllegalStateException(
                        "Encerra a época partilhada antes de criares épocas por modalidade");
            }
            if (CommunityRoundSport.belongsToSeason(active.getSport(), requestedSport)) {
                throw new IllegalStateException(
                        "Já existe uma época ativa para esta modalidade");
            }
        }
    }
}
