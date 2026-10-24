package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentScoringMode;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentRaceRound;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An explicit Karting championship reuses existing community rounds and the
 * community race standings algorithm. No duplicate results or made-up duels.
 * This is intentionally limited to Karting until other race event contracts
 * are verified, rather than automatically enabling all 121 disciplines.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentRaceService {
    public static final String DEFAULT_POINTS = "25,18,15,12,10,8,6,4,2,1";
    private static final int MAX_ROUNDS = 100;

    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityRoundRepository rounds;
    private final CommunityTournamentRaceRoundRepository links;
    private final CommunityCompetitionService competition;

    @Transactional(readOnly = true)
    public CommunityTournamentRaceOverviewResponse overview(
            Long communityId, Long tournamentId, String username) {
        checkMember(communityId, username);
        CommunityTournament tournament = getRaceTournament(communityId, tournamentId, false);
        return calculate(communityId, tournament, username);
    }

    public CommunityTournamentRaceOverviewResponse link(
            Long communityId, Long tournamentId, Long roundId, String username) {
        checkAdmin(communityId, username);
        CommunityTournament tournament = getRaceTournament(communityId, tournamentId, true);
        requireEditable(tournament);
        CommunityRound round = rounds.findById(roundId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Corrida não encontrada"));
        if (!round.getCommunity().getId().equals(communityId)
                || !"karting".equalsIgnoreCase(Objects.toString(round.getSport(), "").trim())
                || !CommunityRoundEventType.isRaceEvent(round.getSport(), round.getEventType())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Escolhe uma jornada de corrida de Karting desta comunidade");
        }
        if (links.existsByTournamentIdAndRoundId(tournamentId, roundId)) {
            return calculate(communityId, tournament, username); // idempotent click
        }
        if (links.findByTournamentIdOrderByIdAsc(tournamentId).size() >= MAX_ROUNDS) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "O campeonato suporta até 100 corridas");
        }
        // Reuse the existing read model, including penalties, identity claims and
        // timing. A race with no real finishes must not count as a championship result.
        CommunityRoundResponse race = competition.overview(communityId, username, null).rounds()
                .stream().filter(item -> roundId.equals(item.id())).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Corrida não encontrada"));
        Set<Integer> positions = new HashSet<>();
        if (race.entries() == null || race.entries().size() < 2
                || race.entries().stream().filter(e -> e.finishPosition() != null || e.raceDisqualified()).count() < 2
                || race.entries().stream().anyMatch(e -> e.finishPosition() != null
                        && (e.finishPosition() < 1 || !positions.add(e.finishPosition())))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A corrida precisa de pelo menos dois participantes com resultados válidos");
        }
        links.save(CommunityTournamentRaceRound.builder().tournament(tournament).round(round).build());
        if (tournament.getStatus() == TournamentStatus.DRAFT) {
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            tournaments.save(tournament);
        }
        return calculate(communityId, tournament, username);
    }

    public CommunityTournamentRaceOverviewResponse unlink(
            Long communityId, Long tournamentId, Long roundId, String username) {
        checkAdmin(communityId, username);
        CommunityTournament tournament = getRaceTournament(communityId, tournamentId, true);
        requireEditable(tournament);
        links.findByTournamentIdAndRoundId(tournamentId, roundId).ifPresent(links::delete);
        links.flush();
        if (links.findByTournamentIdOrderByIdAsc(tournamentId).isEmpty()) {
            tournament.setStatus(TournamentStatus.DRAFT);
            tournament.setStartedAt(null);
            tournaments.save(tournament);
        }
        return calculate(communityId, tournament, username);
    }

    public CommunityTournamentRaceOverviewResponse changePoints(
            Long communityId, Long tournamentId, String username, String csv) {
        checkAdmin(communityId, username);
        CommunityTournament tournament = getRaceTournament(communityId, tournamentId, true);
        if (tournament.getStatus() != TournamentStatus.DRAFT
                || !links.findByTournamentIdOrderByIdAsc(tournamentId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A pontuação fica fixa ao associar a primeira corrida");
        }
        List<Integer> points;
        try {
            points = CommunityRaceStandings.parsePoints(csv);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        tournament.setRacePointsByPlace(points.stream()
                .map(String::valueOf).collect(Collectors.joining(",")));
        tournaments.save(tournament);
        return calculate(communityId, tournament, username);
    }

    private CommunityTournamentRaceOverviewResponse calculate(
            Long communityId, CommunityTournament tournament, String username) {
        var stored = links.findByTournamentIdOrderByIdAsc(tournament.getId());
        if (stored.size() > MAX_ROUNDS)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Demasiadas corridas associadas");
        // The existing community overview applies the same identity, timing,
        // DSQ and penalty semantics as the jornadas screen.
        var all = competition.overview(communityId, username, null).rounds();
        Map<Long, CommunityRoundResponse> byId = all.stream()
                .collect(Collectors.toMap(CommunityRoundResponse::id, r -> r, (a, b) -> a));
        Set<Long> selectedIds = stored.stream().map(link -> link.getRound().getId())
                .collect(Collectors.toSet());
        List<CommunityRoundResponse> selected = stored.stream()
                .map(link -> byId.get(link.getRound().getId()))
                .filter(Objects::nonNull).toList();
        List<CommunityRoundResponse> available = all.stream()
                .filter(r -> "karting".equalsIgnoreCase(Objects.toString(r.sport(), "").trim()))
                .filter(r -> CommunityRoundEventType.isRaceEvent(r.sport(), r.eventType()))
                .filter(r -> !selectedIds.contains(r.id()))
                .limit(100).toList();
        List<Integer> points = CommunityRaceStandings.parsePoints(
                tournament.getRacePointsByPlace() == null
                        ? DEFAULT_POINTS : tournament.getRacePointsByPlace());
        List<CommunityRaceStandingGroupResponse> grouped = CommunityRaceStandings.calculate(
                selected, List.of(new CommunityRaceRuleResponse("Karting", points)));
        List<CommunityRaceStandingResponse> table = grouped.isEmpty()
                ? List.of() : grouped.get(0).standings();
        return new CommunityTournamentRaceOverviewResponse(
                tournament.getId(), points, selected, available, table);
    }

    private CommunityTournament getRaceTournament(Long communityId, Long id, boolean lock) {
        CommunityTournament tournament = (lock
                ? tournaments.findByIdForUpdate(id) : tournaments.findById(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campeonato não encontrado"));
        if (!tournament.getCommunity().getId().equals(communityId)
                || tournament.getScoringMode() != TournamentScoringMode.RACE_POINTS
                || tournament.getEntryType() != TournamentEntryType.INDIVIDUAL
                || tournament.getFormat() != TournamentFormat.LEAGUE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campeonato de Karting não encontrado");
        }
        return tournament;
    }

    private void requireEditable(CommunityTournament tournament) {
        if (tournament.getStatus() == TournamentStatus.COMPLETED)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Campeonato já concluído");
    }

    private void checkMember(Long communityId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador inválido"));
        if (!members.existsByCommunityIdAndProfileId(communityId, actor.getId()))
            throw new AccessDeniedException("Não pertences a esta comunidade");
    }

    private void checkAdmin(Long communityId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador inválido"));
        var membership = members.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN)
            throw new AccessDeniedException("Apenas Owners e Admins podem gerir o campeonato");
    }
}
