package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.dto.community.CommunityTournamentPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
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

        CommunityTournament tournament = CommunityTournament.builder()
                .community(community)
                .name(request.name().trim())
                .winPoints(request.winPoints() == null ? 3 : request.winPoints())
                .drawPoints(request.drawPoints() == null ? 1 : request.drawPoints())
                .lossPoints(request.lossPoints() == null ? 0 : request.lossPoints())
                .createdAt(Instant.now())
                .build();
        // Only the LEAGUE/DRAFT foundation is exposed; starting and scoring come later.
        return response(tournamentRepository.save(tournament));
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

    private CommunityTournamentResponse response(CommunityTournament tournament) {
        return new CommunityTournamentResponse(
                tournament.getId(), tournament.getName(), tournament.getFormat(), tournament.getStatus(),
                tournament.getWinPoints(), tournament.getDrawPoints(), tournament.getLossPoints(),
                tournament.getCreatedAt(), tournament.getStartedAt(), tournament.getCompletedAt()
        );
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
