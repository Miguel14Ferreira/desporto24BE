package com.example.desporto24.service;

import com.example.desporto24.model.community.CommunitySeason;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Explicit season attribution for newly recorded community rounds.
 * The legacy no-season-id path may still use the single community active season.
 * Never silently attach a round to another community, closed season or sport.
 */
final class CommunityRoundSeasonPolicy {
    private CommunityRoundSeasonPolicy() {}

    /**
     * Select an active season for the recorded sport. Prefer a sport-tagged
     * season to a legacy shared season; never guess between multiple matches.
     * This also supports communities as they migrate to per-sport seasons.
     */
    static CommunitySeason selectForRound(
            java.util.List<CommunitySeason> activeSeasons, String roundSport) {
        if (activeSeasons == null || activeSeasons.isEmpty()) return null;
        if (activeSeasons.size() == 1) return activeSeasons.get(0);

        if (roundSport == null || roundSport.isBlank()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Seleciona a modalidade e a época da jornada");
        }
        java.util.List<CommunitySeason> tagged = activeSeasons.stream()
                .filter(s -> s.getSport() != null && !s.getSport().isBlank()
                        && CommunityRoundSport.belongsToSeason(s.getSport(), roundSport))
                .toList();
        if (tagged.size() == 1) return tagged.get(0);
        if (tagged.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Existem várias épocas ativas para esta modalidade");
        }
        java.util.List<CommunitySeason> shared = activeSeasons.stream()
                .filter(s -> s.getSport() == null || s.getSport().isBlank())
                .toList();
        if (shared.size() == 1) return shared.get(0);
        throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Não existe uma época ativa única para esta modalidade");
    }

    static CommunitySeason validate(Long communityId, CommunitySeason season,
                                    String roundSport, boolean explicit) {
        if (explicit && season == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Época não encontrada nesta comunidade");
        }
        // Protect both explicit selections and legacy automatic attribution:
        // an archived or foreign season must never receive a new round.
        if (season != null) {
            if (season.getCommunity() == null
                    || !communityId.equals(season.getCommunity().getId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Época não encontrada nesta comunidade");
            }
            if (!season.isActive()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Não é possível associar uma jornada a uma época encerrada");
            }
        }
        if (season != null && !CommunityRoundSport.belongsToSeason(
                season.getSport(), roundSport)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Esta época está associada a outra modalidade");
        }
        return season;
    }
}
