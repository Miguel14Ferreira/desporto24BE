package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Finalizes a real-team round-robin under the same lock as result updates. */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentTeamFinalizationService {
    private static final int MAX_TEAMS = 32;
    private static final int MAX_MATCHES = MAX_TEAMS * (MAX_TEAMS - 1);

    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamMatchRepository matches;
    private final CommunityTournamentTeamStandingsService standingsService;

    public CommunityTournamentResponse complete(Long communityId, Long tournamentId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        var member = members.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (member.getRole() != CommunityRole.OWNER && member.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas organizadores podem encerrar competições");
        }
        var tournament = tournaments.findByIdForUpdate(tournamentId)
                .orElseThrow(this::notFound);
        if (!tournament.getCommunity().getId().equals(communityId)) throw notFound();
        if (tournament.getEntryType() != TournamentEntryType.TEAM
                || tournament.getFormat() != TournamentFormat.LEAGUE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este encerramento exige um campeonato por equipas");
        }
        if (tournament.getStatus() == TournamentStatus.COMPLETED) {
            return CommunityTournamentResponse.from(tournament);
        }
        if (tournament.getStatus() != TournamentStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O campeonato ainda não começou");
        }

        var table = standingsService.standings(communityId, tournamentId, username);
        List<CommunityTournamentTeamStandingRowResponse> rows = table.standings();
        int count = rows.size();
        if (count < 2 || count > MAX_TEAMS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de equipas inválido");
        }
        int legs = tournament.getRoundRobinLegs();
        if (legs < 1 || legs > 2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de voltas inválido");
        }
        long expected = (long) count * (count - 1) / 2 * legs;
        if (table.totalMatches() != expected || table.completedMatches() != expected
                || rows.stream().anyMatch(row -> row.played() != (count - 1) * legs)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Todos os confrontos do campeonato têm de estar concluídos");
        }

        // A repeated pair must not stand in for a missing match between two other teams.
        var scheduled = matches.findByTournamentId(tournamentId,
                PageRequest.of(0, MAX_MATCHES + 1, Sort.by("id")));
        if (scheduled.hasNext() || scheduled.getNumberOfElements() != expected) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Calendário incompleto");
        }
        Set<Long> registeredTeams = new HashSet<>();
        for (var row : rows) {
            if (!registeredTeams.add(row.teamId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipas duplicadas");
            }
        }
        Set<String> pairings = new HashSet<>();
        for (var game : scheduled.getContent()) {
            Long home = game.getHomeTeam().getId();
            Long away = game.getAwayTeam().getId();
            if (home == null || away == null || home.equals(away)
                    || !registeredTeams.contains(home) || !registeredTeams.contains(away)
                    || game.getStatus() != TournamentMatchStatus.COMPLETED
                    || game.getHomeScore() == null || game.getAwayScore() == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Confronto inconsistente");
            }
            String pairing = legs == 2
                    ? home + ":" + away
                    : Math.min(home, away) + ":" + Math.max(home, away);
            if (!pairings.add(pairing)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Confrontos duplicados");
            }
        }
        if (pairings.size() != expected) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Faltam confrontos entre equipas");
        }

        var leader = rows.get(0);
        boolean tied = rows.size() > 1 && sameSportingRank(leader, rows.get(1));
        tournament.setChampionTied(tied);
        tournament.setChampionTeamId(tied ? null : leader.teamId());
        tournament.setChampionTeamName(tied ? null : leader.teamName());
        tournament.setChampionPoints(tied ? null : leader.points());
        tournament.setCompletedAt(Instant.now());
        tournament.setStatus(TournamentStatus.COMPLETED);
        return CommunityTournamentResponse.from(tournaments.save(tournament));
    }

    private boolean sameSportingRank(CommunityTournamentTeamStandingRowResponse a,
                                     CommunityTournamentTeamStandingRowResponse b) {
        return a.points() == b.points()
                && a.goalDifference() == b.goalDifference()
                && a.goalsFor() == b.goalsFor()
                && a.won() == b.won();
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Campeonato não encontrado");
    }
}
