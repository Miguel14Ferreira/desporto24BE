package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.community.*;
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
import java.util.List;
import java.util.Objects;

/**
 * Provisional golf championship with exactly one organizer-confirmed round.
 * No multi-round aggregation, handicap or immutable completion snapshot.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentGolfService {
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityRoundRepository rounds;
    private final CommunityTournamentRaceRoundRepository links;
    private final CommunityCompetitionService competition;

    @Transactional(readOnly = true)
    public CommunityTournamentGolfOverviewResponse overview(Long communityId, Long tournamentId, String username) {
        checkMember(communityId, username);
        return calculate(communityId, typed(communityId, tournamentId, false), username);
    }

    public CommunityTournamentGolfOverviewResponse link(Long communityId, Long tournamentId,
            Long roundId, String username) {
        checkManager(communityId, username);
        CommunityTournament tournament = typed(communityId, tournamentId, true);
        requireOpen(tournament);
        CommunityRound source = rounds.findById(roundId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jornada não encontrada"));
        if (!Objects.equals(source.getCommunity().getId(), communityId)
                || !eligible(tournament, source.getSport(), source.getEventType()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Jornada de golfe de outra comunidade ou de outro número de buracos");
        var saved = links.findByTournamentIdOrderByIdAsc(tournamentId);
        if (!saved.isEmpty()) {
            if (saved.size() == 1 && Objects.equals(saved.get(0).getRound().getId(), roundId))
                return calculate(communityId, tournament, username);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este torneio de golfe admite apenas uma jornada; remove a atual primeiro");
        }
        CommunityRoundResponse confirmed = allRounds(communityId, username).stream()
                .filter(r -> Objects.equals(roundId, r.id())).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Resultados da jornada indisponíveis"));
        if (!eligible(tournament, confirmed.sport(), confirmed.eventType()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Prova incompatível");
        try {
            validate(confirmed);
        } catch (IllegalArgumentException invalid) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A jornada exige resultados completos de pelo menos dois jogadores", invalid);
        }
        links.save(CommunityTournamentRaceRound.builder().tournament(tournament).round(source).build());
        if (tournament.getStatus() == TournamentStatus.DRAFT) {
            tournament.setStatus(TournamentStatus.IN_PROGRESS);
            tournament.setStartedAt(Instant.now());
            tournaments.save(tournament);
        }
        return calculate(communityId, tournament, username);
    }

    public CommunityTournamentGolfOverviewResponse unlink(Long communityId, Long tournamentId,
            Long roundId, String username) {
        checkManager(communityId, username);
        CommunityTournament tournament = typed(communityId, tournamentId, true);
        requireOpen(tournament);
        links.findByTournamentIdAndRoundId(tournamentId, roundId).ifPresent(links::delete);
        links.flush();
        if (links.findByTournamentIdOrderByIdAsc(tournamentId).isEmpty()) {
            tournament.setStatus(TournamentStatus.DRAFT);
            tournament.setStartedAt(null);
            tournaments.save(tournament);
        }
        return calculate(communityId, tournament, username);
    }

    private CommunityTournamentGolfOverviewResponse calculate(Long communityId,
            CommunityTournament tournament, String username) {
        var saved = links.findByTournamentIdOrderByIdAsc(tournament.getId());
        if (saved.size() > 1)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Torneio com jornadas a mais");
        var all = allRounds(communityId, username);
        List<CommunityRoundResponse> linked = List.of();
        List<CommunityGolfRoundStandingResponse> standings = List.of();
        if (!saved.isEmpty()) {
            Long id = saved.get(0).getRound().getId();
            CommunityRoundResponse round = all.stream().filter(r -> Objects.equals(r.id(), id))
                    .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                            "Jornada associada indisponível"));
            if (!eligible(tournament, round.sport(), round.eventType()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Jornada associada alterada");
            try {
                standings = validate(round);
            } catch (IllegalArgumentException invalid) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Resultados da jornada associada inválidos", invalid);
            }
            linked = List.of(round);
        }
        List<CommunityRoundResponse> available = saved.isEmpty() ? all.stream()
                .filter(r -> eligible(tournament, r.sport(), r.eventType()))
                .filter(r -> {
                    try { validate(r); return true; }
                    catch (IllegalArgumentException invalid) { return false; }
                }).limit(100).toList() : List.of();
        return new CommunityTournamentGolfOverviewResponse(tournament.getId(),
                tournament.getEventType(), linked, available, standings);
    }

    private List<CommunityGolfRoundStandingResponse> validate(CommunityRoundResponse round) {
        var ranked = CommunityGolfTournamentRoundAdapter.calculate(round);
        if (ranked.size() < 2)
            throw new IllegalArgumentException("São necessários pelo menos dois jogadores");
        return ranked.stream().map(s -> new CommunityGolfRoundStandingResponse(
                s.position(), s.playerId(), s.grossStrokes())).toList();
    }

    private List<CommunityRoundResponse> allRounds(Long communityId, String username) {
        return competition.overview(communityId, username, null).rounds();
    }

    private boolean eligible(CommunityTournament tournament, String sport, String eventType) {
        return "Golfe".equals(sport) && Objects.equals(tournament.getEventType(), eventType);
    }

    private CommunityTournament typed(Long communityId, Long tournamentId, boolean lock) {
        CommunityTournament tournament = (lock
                ? tournaments.findByIdForUpdate(tournamentId) : tournaments.findById(tournamentId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado"));
        if (!Objects.equals(tournament.getCommunity().getId(), communityId)
                || tournament.getScoringMode() != TournamentScoringMode.GOLF_GROSS_STROKES
                || tournament.getEntryType() != TournamentEntryType.INDIVIDUAL
                || tournament.getFormat() != TournamentFormat.LEAGUE
                || !"Golfe".equals(tournament.getSport())
                || !CommunityGolfStrokeResults.isGolfStrokeEvent(tournament.getEventType())
                || tournament.getEventDiscipline() != null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio de golfe não encontrado");
        return tournament;
    }

    private void requireOpen(CommunityTournament tournament) {
        if (tournament.getStatus() == TournamentStatus.COMPLETED)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Torneio concluído");
    }

    private void checkMember(Long communityId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador inválido"));
        if (!members.existsByCommunityIdAndProfileId(communityId, actor.getId()))
            throw new AccessDeniedException("Não pertences à comunidade");
    }

    private void checkManager(Long communityId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador inválido"));
        var membership = members.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences à comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN)
            throw new AccessDeniedException("Só administradores podem gerir o torneio");
    }
}
