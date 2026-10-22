package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentTeam;
import com.example.desporto24.model.community.CommunityTournamentTeamPlayer;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Draft team roster administration; it never creates individual tournament enrollments. */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentTeamService {
    private static final int MAX_TEAMS = 32;

    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityTournamentTeamRepository teamRepository;
    private final CommunityTournamentTeamPlayerRepository playerRepository;

    public CommunityTournamentTeamResponse createTeam(
            Long communityId, Long tournamentId, String username, String name) {
        Profile actor = actor(username);
        requireManager(communityId, actor);
        CommunityTournament tournament = lockedTeamTournament(communityId, tournamentId);
        requireDraft(tournament);

        if (name == null || name.trim().isEmpty() || name.trim().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de equipa inválido");
        }
        String cleaned = name.trim().replaceAll("\\s+", " ");
        String normalized = cleaned.toLowerCase(Locale.ROOT);
        if (teamRepository.countByTournamentId(tournamentId) >= MAX_TEAMS) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Esta versão suporta até 32 equipas");
        }
        if (teamRepository.existsByTournamentIdAndNormalizedName(tournamentId, normalized)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta equipa já existe no torneio");
        }
        var team = teamRepository.save(CommunityTournamentTeam.builder()
                .tournament(tournament).name(cleaned).normalizedName(normalized)
                .createdAt(Instant.now()).build());
        return toResponse(team, tournament.getPlayersPerTeam(), List.of());
    }

    public CommunityTournamentTeamResponse assignPlayer(Long communityId, Long tournamentId,
            Long teamId, String username, Long profileId) {
        Profile actor = actor(username);
        requireManager(communityId, actor);
        CommunityTournament tournament = lockedTeamTournament(communityId, tournamentId);
        requireDraft(tournament);
        CommunityTournamentTeam team = team(tournamentId, teamId);
        if (profileId == null || profileId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jogador inválido");
        }
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, profileId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só podes incluir membros atuais desta comunidade");
        }
        if (playerRepository.existsByTournamentIdAndProfileId(tournamentId, profileId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O jogador já pertence a uma equipa desta competição");
        }
        if (playerRepository.countByTeamId(teamId) >= tournament.getPlayersPerTeam()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A equipa já atingiu o número definido de jogadores");
        }
        var player = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));
        playerRepository.save(CommunityTournamentTeamPlayer.builder()
                .tournament(tournament).team(team).profile(player).assignedAt(Instant.now()).build());
        return toResponse(team, tournament.getPlayersPerTeam(),
                playerRepository.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId).stream()
                        .filter(p -> p.getTeam().getId().equals(teamId)).toList());
    }

    /**
     * An invite link offers a place to the authenticated community member who opens it.
     * No roster assignment is performed merely by opening the link.
     * All capacity/uniqueness checks run under the tournament row lock.
     */
    public CommunityTournamentTeamResponse joinInvitedTeam(
            Long communityId, Long tournamentId, Long teamId, String username) {
        Profile player = actor(username);
        requireMember(communityId, player);
        CommunityTournament tournament = lockedTeamTournament(communityId, tournamentId);
        requireDraft(tournament);
        CommunityTournamentTeam target = team(tournamentId, teamId);

        var existing = playerRepository.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId)
                .stream().filter(entry -> entry.getProfile().getId().equals(player.getId()))
                .findFirst();
        if (existing.isPresent()) {
            if (!existing.get().getTeam().getId().equals(teamId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Já pertences a outra equipa nesta competição");
            }
            return toResponse(target, tournament.getPlayersPerTeam(),
                    playerRepository.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId)
                        .stream().filter(entry -> entry.getTeam().getId().equals(teamId)).toList());
        }

        if (playerRepository.countByTeamId(teamId) >= tournament.getPlayersPerTeam()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A equipa já está completa");
        }
        playerRepository.save(CommunityTournamentTeamPlayer.builder()
                .tournament(tournament).team(target).profile(player)
                .assignedAt(Instant.now()).build());
        return toResponse(target, tournament.getPlayersPerTeam(),
                playerRepository.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId)
                        .stream().filter(entry -> entry.getTeam().getId().equals(teamId)).toList());
    }

    public void removePlayer(Long communityId, Long tournamentId, Long teamId,
            String username, Long profileId) {
        Profile actor = actor(username);
        requireManager(communityId, actor);
        requireDraft(lockedTeamTournament(communityId, tournamentId));
        team(tournamentId, teamId);
        playerRepository.findByTeamIdAndProfileId(teamId, profileId)
                .ifPresent(playerRepository::delete);
    }

    public void deleteTeam(Long communityId, Long tournamentId, Long teamId, String username) {
        Profile actor = actor(username);
        requireManager(communityId, actor);
        requireDraft(lockedTeamTournament(communityId, tournamentId));
        CommunityTournamentTeam team = team(tournamentId, teamId);
        if (playerRepository.countByTeamId(teamId) != 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Retira primeiro os jogadores da equipa");
        }
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public List<CommunityTournamentTeamResponse> list(Long communityId, Long tournamentId, String username) {
        Profile actor = actor(username);
        requireMember(communityId, actor);
        CommunityTournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> notFound());
        verifyScopeAndType(communityId, tournament);
        var teams = teamRepository.findByTournamentIdOrderByIdAsc(tournamentId);
        // One batch for the whole tournament instead of N+1 roster queries.
        var byTeam = playerRepository.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId)
                .stream().collect(Collectors.groupingBy(p -> p.getTeam().getId()));
        return teams.stream().map(t -> toResponse(t, tournament.getPlayersPerTeam(),
                byTeam.getOrDefault(t.getId(), List.of()))).toList();
    }

    private CommunityTournamentTeamResponse toResponse(CommunityTournamentTeam team, int size,
            List<CommunityTournamentTeamPlayer> roster) {
        return new CommunityTournamentTeamResponse(team.getId(), team.getName(), size, team.getCreatedAt(),
                roster.stream().map(p -> {
                    var profile = p.getProfile();
                    String display = profile.getNickname();
                    if (display == null || display.isBlank()) display = profile.getFullName();
                    if (display == null || display.isBlank()) display = profile.getUsername();
                    return new CommunityTournamentTeamResponse.Player(profile.getId(),
                            profile.getUsername(), display);
                }).toList());
    }

    private CommunityTournamentTeam team(Long tournamentId, Long teamId) {
        var team = teamRepository.findById(teamId).orElseThrow(() -> notFound());
        if (!team.getTournament().getId().equals(tournamentId)) throw notFound();
        return team;
    }

    private CommunityTournament lockedTeamTournament(Long communityId, Long tournamentId) {
        var t = tournamentRepository.findByIdForUpdate(tournamentId).orElseThrow(() -> notFound());
        verifyScopeAndType(communityId, t);
        return t;
    }

    private void verifyScopeAndType(Long communityId, CommunityTournament tournament) {
        if (!tournament.getCommunity().getId().equals(communityId)) throw notFound();
        if (tournament.getEntryType() != TournamentEntryType.TEAM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este torneio é individual e não suporta equipas");
        }
    }

    private void requireDraft(CommunityTournament tournament) {
        if (tournament.getStatus() != TournamentStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Os plantéis só podem ser alterados antes do torneio começar");
        }
    }

    private Profile actor(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    private void requireMember(Long cid, Profile actor) {
        if (!memberRepository.existsByCommunityIdAndProfileId(cid, actor.getId()))
            throw new AccessDeniedException("Não pertences a esta comunidade");
    }

    private void requireManager(Long cid, Profile actor) {
        var m = memberRepository.findByCommunityIdAndProfileId(cid, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (m.getRole() != CommunityRole.ADMIN && m.getRole() != CommunityRole.OWNER)
            throw new AccessDeniedException("Apenas organizadores podem gerir equipas");
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipa ou competição não encontrada");
    }
}
