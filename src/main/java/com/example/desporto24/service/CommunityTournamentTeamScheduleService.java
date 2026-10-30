package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentTeam;
import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamMatchResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamMatchesPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamScheduleSummaryResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamPlayerRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Real-team configurable one/two-leg round-robin fixtures. The individual participant tables, regular
 * seasons and historical community standings are completely independent.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentTeamScheduleService {
    private static final int MAX_TEAMS = 32;
    private static final int PAGE_SIZE = 50;

    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamRepository teams;
    private final CommunityTournamentTeamPlayerRepository players;
    private final CommunityTournamentTeamMatchRepository matches;
    private final CommunityTournamentTeamKnockoutService knockout;
    private final CommunityTournamentTeamGroupsService groups;

    public CommunityTournamentTeamScheduleSummaryResponse generate(
            Long communityId, Long tournamentId, String username) {
        Profile actor = actor(username);
        var membership = members.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Só organizadores podem publicar o calendário");
        }
        // Uses the same tournament row lock as every team/roster mutation.
        CommunityTournament tournament = tournaments.findByIdForUpdate(tournamentId)
                .orElseThrow(() -> notFound());
        checkScope(communityId, tournament);
        if (tournament.getStatus() != TournamentStatus.DRAFT
                || matches.existsByTournamentId(tournamentId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O calendário já foi publicado ou o torneio não permite esta operação");
        }
        List<CommunityTournamentTeam> rosterTeams = teams.findByTournamentIdOrderByIdAsc(tournamentId);
        if (rosterTeams.size() < 2 || rosterTeams.size() > MAX_TEAMS) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "É necessário ter entre 2 e 32 equipas para gerar jornadas");
        }
        var registered = players.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId);
        Map<Long, Integer> counts = new HashMap<>();
        for (var player : registered) {
            if (!members.existsByCommunityIdAndProfileId(communityId, player.getProfile().getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Existem jogadores que já não pertencem à comunidade");
            }
            counts.merge(player.getTeam().getId(), 1, Integer::sum);
        }
        // Legacy fixed-size tournaments keep the original complete-roster rule.
        // New flexible tournaments require at least one member in each team.
        Integer fixedSize = tournament.getPlayersPerTeam();
        for (var team : rosterTeams) {
            int assigned = counts.getOrDefault(team.getId(), 0);
            if (fixedSize == null ? assigned < 1 || assigned > 30 : assigned != fixedSize) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        fixedSize == null
                                ? "Cada equipa precisa de pelo menos um jogador antes de publicar"
                                : "Completa todos os plantéis antes de gerar as jornadas");
            }
        }

        if (tournament.getFormat() == TournamentFormat.GROUP_KNOCKOUT) {
            var groupFixtures = groups.draw(tournament, rosterTeams);
            matches.saveAll(groupFixtures);
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            tournaments.save(tournament);
            int maxGroupSize = (rosterTeams.size() + tournament.getGroupCount() - 1)
                    / tournament.getGroupCount();
            int rounds = maxGroupSize % 2 == 0 ? maxGroupSize - 1 : maxGroupSize;
            return new CommunityTournamentTeamScheduleSummaryResponse(
                    tournamentId, TournamentStatus.IN_PROGRESS, rosterTeams.size(),
                    rounds, groupFixtures.size());
        }
        if (tournament.getFormat() == TournamentFormat.KNOCKOUT) {
            var opening = knockout.firstRound(tournament, rosterTeams);
            matches.saveAll(opening);
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            tournaments.save(tournament);
            int totalRounds = 32 - Integer.numberOfLeadingZeros(rosterTeams.size() - 1);
            return new CommunityTournamentTeamScheduleSummaryResponse(
                    tournamentId, TournamentStatus.IN_PROGRESS, rosterTeams.size(),
                    totalRounds, opening.size());
        }
        if (tournament.getFormat() != TournamentFormat.LEAGUE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Formato de competição inválido");
        }

        // Berger circle, with a bye for odd counts. The second leg mirrors every
        // first-leg fixture in the corresponding round, reversing home and away.
        int legs = tournament.getRoundRobinLegs();
        if (legs < 1 || legs > 2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de voltas inválido");
        }
        List<CommunityTournamentTeam> seats = new ArrayList<>(rosterTeams);
        if (seats.size() % 2 != 0) seats.add(null);
        int rounds = seats.size() - 1;
        List<CommunityTournamentTeamMatch> fixtures = new ArrayList<>();
        for (int round = 0; round < rounds; round++) {
            for (int position = 0; position < seats.size() / 2; position++) {
                var first = seats.get(position);
                var second = seats.get(seats.size() - 1 - position);
                if (first == null || second == null) continue;
                boolean swap = (round + position) % 2 == 1;
                fixtures.add(CommunityTournamentTeamMatch.builder()
                        .tournament(tournament)
                        .homeTeam(swap ? second : first)
                        .awayTeam(swap ? first : second)
                        .roundNumber(round + 1).build());
            }
            var last = seats.remove(seats.size() - 1);
            seats.add(1, last);
        }
        if (legs == 2) {
            List<CommunityTournamentTeamMatch> firstLeg = List.copyOf(fixtures);
            for (var game : firstLeg) {
                fixtures.add(CommunityTournamentTeamMatch.builder()
                        .tournament(tournament)
                        .homeTeam(game.getAwayTeam())
                        .awayTeam(game.getHomeTeam())
                        .roundNumber(game.getRoundNumber() + rounds).build());
            }
        }
        matches.saveAll(fixtures);
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournament.setStartedAt(Instant.now());
        tournaments.save(tournament);
        return new CommunityTournamentTeamScheduleSummaryResponse(
                tournamentId, TournamentStatus.IN_PROGRESS, rosterTeams.size(),
                rounds * legs, fixtures.size());
    }

    @Transactional(readOnly = true)
    public CommunityTournamentTeamMatchesPageResponse matches(
            Long communityId, Long tournamentId, String username, int page) {
        if (page < 0 || page > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Página inválida");
        }
        Profile actor = actor(username);
        if (!members.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
        CommunityTournament tournament = tournaments.findById(tournamentId)
                .orElseThrow(() -> notFound());
        checkScope(communityId, tournament);
        // Groups are published first; knockout fixtures are appended as results arrive.
        // For legacy formats preserve sorting by sporting round.
        var sort = tournament.getFormat() == TournamentFormat.GROUP_KNOCKOUT
                ? Sort.by(Sort.Direction.ASC, "id")
                : Sort.by(Sort.Direction.ASC, "roundNumber").and(Sort.by("id"));
        int pageSize = tournament.getFormat() == TournamentFormat.GROUP_KNOCKOUT ? 250 : PAGE_SIZE;
        var slice = matches.findByTournamentId(tournamentId, PageRequest.of(page, pageSize, sort));
        return new CommunityTournamentTeamMatchesPageResponse(
                slice.getContent().stream().map(game -> new CommunityTournamentTeamMatchResponse(
                        game.getId(), game.getRoundNumber(), game.getHomeTeam().getId(),
                        game.getHomeTeam().getName(), game.getAwayTeam().getId(),
                        game.getAwayTeam().getName(), game.getStatus(),
                        game.getHomeScore(), game.getAwayScore(), game.getGroupNumber())).toList(),
                page, slice.hasNext());
    }

    private Profile actor(String username) {
        return profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    private void checkScope(Long communityId, CommunityTournament tournament) {
        if (!tournament.getCommunity().getId().equals(communityId)) throw notFound();
        if (tournament.getEntryType() != TournamentEntryType.TEAM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta competição é individual e não admite confrontos entre equipas");
        }
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Competição não encontrada");
    }
}
