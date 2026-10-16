package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityTournamentMatch;
import com.example.desporto24.model.dto.community.CommunityTournamentMatchResponse;
import com.example.desporto24.model.dto.community.RecordCommunityTournamentMatchResultRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentResultService {
    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityTournamentMatchRepository matchRepository;

    /**
     * First result marks the match COMPLETED; subsequent writes may correct its scores
     * while the tournament is IN_PROGRESS. Both paths serialize on the tournament row.
     * The first completion timestamp remains stable on corrections.
     */
    public CommunityTournamentMatchResponse recordResult(
            Long communityId, Long tournamentId, Long matchId,
            String username, RecordCommunityTournamentMatchResultRequest request) {
        if (request == null || !validScore(request.homeScore()) || !validScore(request.awayScore())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Indica resultados entre 0 e 999 para ambas as equipas");
        }
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        var membership = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Só Owner/Admin pode registar resultados");
        }

        // Same lock/order as schedule creation. Concurrent updates serialize.
        var tournament = tournamentRepository.findByIdForUpdate(tournamentId)
                .orElseThrow(() -> notFound());
        if (!tournament.getCommunity().getId().equals(communityId)) {
            throw notFound();
        }
        if (tournament.getStatus() != TournamentStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só é possível registar resultados de um torneio em curso");
        }

        CommunityTournamentMatch match = matchRepository.findById(matchId)
                .orElseThrow(() -> notFound());
        if (!match.getTournament().getId().equals(tournamentId)) {
            throw notFound();
        }

        if (match.getStatus() == TournamentMatchStatus.COMPLETED
                && request.homeScore().equals(match.getHomeScore())
                && request.awayScore().equals(match.getAwayScore())) {
            return response(match);
        }
        if (match.getStatus() != TournamentMatchStatus.SCHEDULED
                && match.getStatus() != TournamentMatchStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estado do jogo inválido");
        }
        boolean firstCompletion = match.getStatus() == TournamentMatchStatus.SCHEDULED;
        match.setHomeScore(request.homeScore());
        match.setAwayScore(request.awayScore());
        match.setStatus(TournamentMatchStatus.COMPLETED);
        if (firstCompletion) {
            match.setCompletedAt(Instant.now());
        }
        return response(matchRepository.save(match));
    }

    private boolean validScore(Integer score) {
        return score != null && score >= 0 && score <= 999;
    }

    private CommunityTournamentMatchResponse response(CommunityTournamentMatch match) {
        var home = match.getHomeParticipant().getProfile();
        var away = match.getAwayParticipant().getProfile();
        return new CommunityTournamentMatchResponse(
                match.getId(), match.getRoundNumber(),
                home.getId(), home.getUsername(),
                away.getId(), away.getUsername(),
                match.getStatus(), match.getHomeScore(), match.getAwayScore());
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado");
    }
}
