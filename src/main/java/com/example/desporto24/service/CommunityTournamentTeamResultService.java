package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamMatchResponse;
import com.example.desporto24.model.dto.community.RecordCommunityTournamentMatchResultRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

/** Results for team fixtures, entirely separate from individual tournament fixtures. */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentTeamResultService {
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamMatchRepository matches;

    public CommunityTournamentTeamMatchResponse record(
            Long communityId, Long tournamentId, Long matchId,
            String username, RecordCommunityTournamentMatchResultRequest request) {
        if (request == null || !valid(request.homeScore()) || !valid(request.awayScore())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Indica resultados entre 0 e 999 para ambas as equipas");
        }
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        var membership = members.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER
                && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Só Owner/Admin pode registar resultados");
        }

        // Serialize writes on the tournament row, as in individual competitions.
        var tournament = tournaments.findByIdForUpdate(tournamentId)
                .orElseThrow(this::notFound);
        if (!tournament.getCommunity().getId().equals(communityId)) throw notFound();
        if (tournament.getEntryType() != TournamentEntryType.TEAM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este endpoint apenas regista resultados entre equipas");
        }
        if (tournament.getStatus() != TournamentStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só é possível registar resultados de um torneio em curso");
        }

        CommunityTournamentTeamMatch match = matches.findById(matchId)
                .orElseThrow(this::notFound);
        if (!match.getTournament().getId().equals(tournamentId)) throw notFound();
        if (match.getStatus() == TournamentMatchStatus.COMPLETED
                && request.homeScore().equals(match.getHomeScore())
                && request.awayScore().equals(match.getAwayScore())) {
            return response(match);
        }
        if (match.getStatus() != TournamentMatchStatus.SCHEDULED
                && match.getStatus() != TournamentMatchStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estado do confronto inválido");
        }
        boolean firstCompletion = match.getStatus() == TournamentMatchStatus.SCHEDULED;
        match.setHomeScore(request.homeScore());
        match.setAwayScore(request.awayScore());
        match.setStatus(TournamentMatchStatus.COMPLETED);
        if (firstCompletion) match.setCompletedAt(Instant.now());
        return response(matches.save(match));
    }

    private boolean valid(Integer score) {
        return score != null && score >= 0 && score <= 999;
    }

    private CommunityTournamentTeamMatchResponse response(CommunityTournamentTeamMatch match) {
        return new CommunityTournamentTeamMatchResponse(
                match.getId(), match.getRoundNumber(),
                match.getHomeTeam().getId(), match.getHomeTeam().getName(),
                match.getAwayTeam().getId(), match.getAwayTeam().getName(),
                match.getStatus(), match.getHomeScore(), match.getAwayScore());
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Confronto não encontrado");
    }
}
