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
