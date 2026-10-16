package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentParticipant;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantsPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentService {
    private static final int PAGE_SIZE = 20;

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityTournamentParticipantRepository participantRepository;
    private final CommunityTournamentMatchRepository matchRepository;
    private final ProfileRepository profileRepository;

    public CommunityTournamentResponse create(Long communityId, String username,
                                               CreateCommunityTournamentRequest request) {
        Profile actor = profile(username);
        Community community = community(communityId);
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owners e Admins podem criar torneios");
        }

        String selectedSport = request.sport();
        if (selectedSport != null) {
            selectedSport = selectedSport.trim();
            if (selectedSport.isEmpty() || selectedSport.length() > 100) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Escolhe uma modalidade válida para o torneio");
            }
        } else {
            // Old clients keep working. Snapshot the primary legacy modality on creation.
            selectedSport = community.getSport();
            if (selectedSport != null) {
                selectedSport = selectedSport.trim();
                if (selectedSport.isEmpty()) selectedSport = null;
            }
        }

        CommunityTournament tournament = CommunityTournament.builder()
                .community(community)
                .name(request.name().trim())
                .sport(selectedSport)
                .winPoints(request.winPoints() == null ? 3 : request.winPoints())
                .drawPoints(request.drawPoints() == null ? 1 : request.drawPoints())
                .lossPoints(request.lossPoints() == null ? 0 : request.lossPoints())
                .createdAt(Instant.now())
                .build();
        // Only the LEAGUE/DRAFT foundation is exposed; starting and scoring come later.
        return response(tournamentRepository.save(tournament));
    }


    /**
     * Remove an unfinished experimental tournament, its fixtures and its opt-in roster.
     * Never delete a competition which has recorded sporting results or is completed.
     * Scope and manager rights are checked before any destructive operation.
     */
    public void delete(Long communityId, Long tournamentId, String username) {
        Profile actor = profile(username);
        var member = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (member.getRole() != CommunityRole.OWNER && member.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owners e Admins podem eliminar torneios");
        }
        CommunityTournament tournament = tournament(communityId, tournamentId);
        if (tournament.getStatus() == TournamentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível eliminar uma competição concluída");
        }

        var matches = matchRepository.findByTournamentIdOrderByRoundNumberAscIdAsc(tournamentId);
        if (matches.stream().anyMatch(match ->
                match.getStatus() == TournamentMatchStatus.COMPLETED
                        || match.getHomeScore() != null || match.getAwayScore() != null)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível eliminar uma competição com resultados registados");
        }

        // Foreign keys connect matches to enrollments and both to the tournament.
        // Flush in dependency order to keep the delete transactional even on PostgreSQL.
        matchRepository.deleteAll(matches);
        matchRepository.flush();
        participantRepository.deleteAll(participantRepository.findByTournamentIdOrderByIdAsc(tournamentId));
        participantRepository.flush();
        tournamentRepository.delete(tournament);
    }

    @Transactional(readOnly = true)
    public CommunityTournamentPageResponse list(Long communityId, String username, int page) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Página inválida");
        }
        Profile actor = profile(username);
        Community community = community(communityId);
        if (community.getVisibility() == CommunityVisibility.PRIVATE
                && !memberRepository.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não tens acesso a esta comunidade");
        }
        var pageable = PageRequest.of(page, PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")));
        var tournaments = tournamentRepository.findByCommunityId(communityId, pageable);
        return new CommunityTournamentPageResponse(
                tournaments.getContent().stream().map(this::response).toList(),
                page,
                tournaments.hasNext()
        );
    }


    // Registration is explicit: organizers cannot enroll other players without consent.
    // These writes are only allowed before a tournament starts.
    public CommunityTournamentParticipantResponse join(Long communityId, Long tournamentId, String username) {
        Profile actor = profile(username);
        requireMember(communityId, actor);
        CommunityTournament tournament = tournament(communityId, tournamentId);
        requireDraft(tournament);
        return participantRepository.findByTournamentIdAndProfileId(tournamentId, actor.getId())
                .map(this::participantResponse)
                .orElseGet(() -> participantResponse(participantRepository.save(
                        CommunityTournamentParticipant.builder()
                                .tournament(tournament)
                                .profile(actor)
                                .joinedAt(Instant.now())
                                .status(TournamentParticipantStatus.PENDING)
                                .build()
                )));
    }

    /**
     * Confirm only a player's own opt-in registration; managers cannot register
     * other people directly. Confirmation is repeatable and restricted to DRAFT.
     */
    public CommunityTournamentParticipantResponse confirmParticipant(
            Long communityId, Long tournamentId, Long profileId, String username) {
        if (profileId == null || profileId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participante inválido");
        }
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        CommunityTournament tournament = tournament(communityId, tournamentId);
        requireDraft(tournament);

        CommunityTournamentParticipant enrollment = participantRepository
                .findByTournamentIdAndProfileId(tournamentId, profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido de participação não encontrado"));
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, profileId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O jogador já não pertence à comunidade");
        }
        if (enrollment.getStatus() == TournamentParticipantStatus.CONFIRMED) {
            return participantResponse(enrollment);
        }
        enrollment.setStatus(TournamentParticipantStatus.CONFIRMED);
        return participantResponse(participantRepository.save(enrollment));
    }

    public void leave(Long communityId, Long tournamentId, String username) {
        Profile actor = profile(username);
        requireMember(communityId, actor);
        CommunityTournament tournament = tournament(communityId, tournamentId);
        requireDraft(tournament);
        participantRepository.findByTournamentIdAndProfileId(tournamentId, actor.getId())
                .ifPresent(participantRepository::delete);
    }

    public void removeParticipant(Long communityId, Long tournamentId, Long profileId, String username) {
        if (profileId == null || profileId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participante inválido");
        }
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        CommunityTournament tournament = tournament(communityId, tournamentId);
        requireDraft(tournament);
        participantRepository.findByTournamentIdAndProfileId(tournamentId, profileId)
                .ifPresent(participantRepository::delete);
    }

    @Transactional(readOnly = true)
    public CommunityTournamentParticipantsPageResponse participants(Long communityId, Long tournamentId,
                                                                     String username, int page) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Página inválida");
        }
        Profile actor = profile(username);
        // Rosters are accessible only to community members, even when a group is public.
        requireMember(communityId, actor);
        tournamentReadOnly(communityId, tournamentId);
        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"));
        var result = participantRepository.findByTournamentId(tournamentId, pageable);
        return new CommunityTournamentParticipantsPageResponse(
                result.getContent().stream().map(this::participantResponse).toList(),
                page, result.hasNext()
        );
    }

    private CommunityTournamentParticipantResponse participantResponse(CommunityTournamentParticipant participant) {
        Profile profile = participant.getProfile();
        String displayName = profile.getNickname();
        if (displayName == null || displayName.isBlank()) {
            displayName = profile.getFullName();
        }
        if (displayName == null || displayName.isBlank()) {
            displayName = profile.getUsername();
        }
        return new CommunityTournamentParticipantResponse(
                profile.getId(), profile.getUsername(), displayName, participant.getJoinedAt(),
                participant.getStatus()
        );
    }

    private void requireMember(Long communityId, Profile actor) {
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
    }

    private void requireAdmin(Long communityId, Profile actor) {
        var membership = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owners e Admins podem gerir participantes");
        }
    }

    private CommunityTournament tournament(Long communityId, Long tournamentId) {
        var tournament = tournamentRepository.findByIdForUpdate(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado"));
        if (!tournament.getCommunity().getId().equals(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
        }
        return tournament;
    }

    private CommunityTournament tournamentReadOnly(Long communityId, Long tournamentId) {
        var tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado"));
        if (!tournament.getCommunity().getId().equals(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
        }
        return tournament;
    }

    private void requireDraft(CommunityTournament tournament) {
        if (tournament.getStatus() != TournamentStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Os participantes só podem ser alterados antes do início do torneio");
        }
    }

    private CommunityTournamentResponse response(CommunityTournament tournament) {
        return CommunityTournamentResponse.from(tournament);
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    private Community community(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
    }
}
