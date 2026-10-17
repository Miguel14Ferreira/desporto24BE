package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

/**
 * Two-sided consent: OWNER/ADMIN suggests an identity, the target member accepts or rejects it.
 * No imported result, player name, or round entry is ever changed.
 */
@Service
@RequiredArgsConstructor
public class CommunityHistoricalPlayerLinkService {
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityPlayerRepository playerRepository;
    private final CommunityHistoricalPlayerLinkRepository linkRepository;
    private final CommunityRoundEntryRepository entryRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<HistoricalPlayerCandidateResponse> candidates(Long communityId, String username) {
        manager(communityId, username);
        Map<Long, CommunityHistoricalPlayerLink> links = new HashMap<>();
        for (CommunityHistoricalPlayerLink link : linkRepository.findByCommunityIdAndStatus(communityId, "ACCEPTED")) {
            links.put(link.getPlayer().getId(), link);
        }
        for (CommunityHistoricalPlayerLink link : linkRepository.findByCommunityIdAndStatus(communityId, "PENDING")) {
            links.put(link.getPlayer().getId(), link);
        }
        return playerRepository.findByCommunityIdOrderByDisplayNameAsc(communityId).stream()
                .map(player -> {
                    CommunityHistoricalPlayerLink link = links.get(player.getId());
                    Profile profile = link == null ? player.getProfile() : link.getProfile();
                    return new HistoricalPlayerCandidateResponse(
                            player.getId(), player.getDisplayName(),
                            profile == null ? null : profile.getUsername(),
                            link == null ? player.getProfile() == null ? "UNLINKED" : "ACCEPTED" : link.getStatus());
                }).toList();
    }

    @Transactional
    public HistoricalPlayerLinkResponse propose(Long communityId, String username,
                                                 ProposeHistoricalPlayerLinkRequest request) {
        manager(communityId, username);
        if (request == null || request.playerId() == null || request.targetUsername() == null
                || request.targetUsername().isBlank()) throw new IllegalArgumentException("Seleciona jogador e utilizador");
        CommunityPlayer player = playerRepository.findById(request.playerId())
                .filter(found -> found.getCommunity().getId().equals(communityId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador histórico não encontrado"));
        if (player.getProfile() != null) throw new IllegalStateException("Este jogador já está associado a um perfil");
        Profile target = profileRepository.findUserByUsernameIgnoreCase(request.targetUsername().trim())
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, target.getId())) {
            throw new IllegalArgumentException("O perfil tem de pertencer à comunidade");
        }
        CommunityHistoricalPlayerLink link = linkRepository.findByCommunityIdAndPlayerId(communityId, player.getId())
                .orElse(null);
        if (link != null && "ACCEPTED".equals(link.getStatus())) {
            throw new IllegalStateException("Este histórico já pertence a um perfil confirmado");
        }
        if (link != null && "PENDING".equals(link.getStatus())) {
            throw new IllegalStateException("Já existe uma associação pendente para este histórico");
        }
        if (link == null) link = CommunityHistoricalPlayerLink.builder()
                .community(player.getCommunity()).player(player).build();
        link.setProfile(target);
        link.setStatus("PENDING");
        link.setProposedByUsername(username);
        link.setCreatedAt(Instant.now());
        link.setConfirmedAt(null);
        return response(linkRepository.save(link));
    }

    @Transactional(readOnly = true)
    public List<HistoricalPlayerLinkResponse> pendingForMe(Long communityId, String username) {
        Profile target = member(communityId, username);
        return linkRepository.findByCommunityIdAndProfileIdAndStatus(communityId, target.getId(), "PENDING")
                .stream().map(this::response).toList();
    }

    @Transactional
    public HistoricalPlayerLinkResponse decide(Long communityId, Long linkId, String username, boolean accept) {
        Profile target = member(communityId, username);
        CommunityHistoricalPlayerLink link = linkRepository.findByIdAndCommunityId(linkId, communityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associação não encontrada"));
        if (!link.getProfile().getId().equals(target.getId())) {
            throw new AccessDeniedException("Só o jogador pode confirmar ou recusar esta associação");
        }
        if (!"PENDING".equals(link.getStatus())) throw new IllegalStateException("Esta proposta já foi tratada");
        if (accept) {
            // A profile may have several historical aliases, but never two participations in one round.
            Set<Long> historicalRoundIds = new HashSet<>();
            List<CommunityRoundEntry> entries = entryRepository
                    .findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(communityId);
            for (CommunityRoundEntry entry : entries) {
                if (entry.getCommunityPlayer().getId().equals(link.getPlayer().getId())) {
                    historicalRoundIds.add(entry.getRound().getId());
                }
            }
            Map<Long, Long> confirmedLinks = new HashMap<>();
            for (CommunityHistoricalPlayerLink other : linkRepository.findByCommunityIdAndStatus(communityId, "ACCEPTED")) {
                confirmedLinks.put(other.getPlayer().getId(), other.getProfile().getId());
            }
            for (CommunityRoundEntry entry : entries) {
                if (entry.getCommunityPlayer().getId().equals(link.getPlayer().getId()) ||
                        !historicalRoundIds.contains(entry.getRound().getId())) continue;
                Profile existing = entry.getProfile() != null ? entry.getProfile()
                        : entry.getCommunityPlayer().getProfile();
                Long profileId = existing == null ? confirmedLinks.get(entry.getCommunityPlayer().getId())
                        : existing.getId();
                if (target.getId().equals(profileId)) {
                    throw new IllegalStateException(
                            "Este perfil já tem uma participação na mesma jornada; resolve o conflito antes de associar");
                }
            }
        }
        link.setStatus(accept ? "ACCEPTED" : "REJECTED");
        link.setConfirmedAt(accept ? Instant.now() : null);
        return response(linkRepository.save(link));
    }

    private HistoricalPlayerLinkResponse response(CommunityHistoricalPlayerLink link) {
        return new HistoricalPlayerLinkResponse(link.getId(), link.getPlayer().getId(),
                link.getPlayer().getDisplayName(), link.getProfile().getUsername(), link.getStatus());
    }

    private Profile member(Long communityId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador não encontrado"));
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
        return actor;
    }

    private void manager(Long communityId, String username) {
        Profile actor = member(communityId, username);
        CommunityMember membership = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas gestores podem propor associações");
        }
        if (!communityRepository.existsById(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunidade não encontrada");
        }
    }
}
