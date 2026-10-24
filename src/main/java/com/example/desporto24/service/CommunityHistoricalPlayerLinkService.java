package com.example.desporto24.service;

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

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.*;

/**
 * Community members directly claim their own imported name with explicit confirmation.
 * Older unconfirmed manager proposals never block a member's new self-claim.
 * No imported result, player name, or round entry is ever changed.
 */
@Service
@RequiredArgsConstructor
public class CommunityHistoricalPlayerLinkService {
    private final CommunityMemberRepository memberRepository;
    private final CommunityPlayerRepository playerRepository;
    private final CommunityHistoricalPlayerLinkRepository linkRepository;
    private final CommunityRoundEntryRepository entryRepository;
    private final ProfileRepository profileRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<HistoricalPlayerCandidateResponse> candidates(Long communityId, String username) {
        Profile viewer = member(communityId, username);
        Map<Long, CommunityHistoricalPlayerLink> links = new HashMap<>();
        for (CommunityHistoricalPlayerLink link : linkRepository.findByCommunityIdAndStatus(communityId, "ACCEPTED")) {
            links.put(link.getPlayer().getId(), link);
        }
        return playerRepository.findHistoricalPlayersWithResults(communityId).stream()
                .filter(player -> player.getProfile() == null)
                .map(player -> {
                    CommunityHistoricalPlayerLink link = links.get(player.getId());
                    Profile owner = link == null ? null : link.getProfile();
                    return new HistoricalPlayerCandidateResponse(
                            player.getId(), player.getDisplayName(),
                            owner != null && owner.getId().equals(viewer.getId()) ? owner.getUsername() : null,
                            link == null ? "UNLINKED" : link.getStatus());
                }).toList();
    }


    /**
     * A member claims an unlinked historical name for their own authenticated account,
     * without requesting approval from the community manager.
     */
    @Transactional
    public HistoricalPlayerLinkResponse claimForMe(Long communityId, String username, Long playerId) {
        Profile owner = member(communityId, username);
        if (playerId == null) throw new IllegalArgumentException("Seleciona um nome histórico");
        // Lock the authenticated profile first: two different aliases cannot be claimed in
        // overlapping rounds by this profile at the same time.
        entityManager.lock(owner, LockModeType.PESSIMISTIC_WRITE);
        // The link might not exist yet, so lock the source player before checking it.
        CommunityPlayer player = playerRepository.findByIdForUpdate(playerId)
                .filter(found -> found.getCommunity().getId().equals(communityId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nome histórico não encontrado"));
        if (player.getProfile() != null) {
            throw new IllegalStateException("Este jogador já tem um perfil associado");
        }
        CommunityHistoricalPlayerLink link = linkRepository.findByCommunityIdAndPlayerId(communityId, playerId)
                .orElse(null);
        if (link != null && "ACCEPTED".equals(link.getStatus())) {
            if (link.getProfile().getId().equals(owner.getId())) {
                return response(link); // idempotent retry
            }
            throw new IllegalStateException("Este nome histórico já pertence a outro membro");
        }
        // A former manager suggestion was never accepted by the member. It must not
        // reserve a historical name. Reuse its link row to respect the unique key,
        // but never change the original player or any recorded round entry.
        validateNoOverlappingRounds(communityId, playerId, owner.getId());
        if (link == null) link = CommunityHistoricalPlayerLink.builder()
                .community(player.getCommunity()).player(player).build();
        link.setProfile(owner);
        link.setStatus("ACCEPTED");
        link.setProposedByUsername(username); // unchanged DB column; self-claimed by authenticated user
        link.setCreatedAt(Instant.now());
        link.setConfirmedAt(Instant.now());
        return response(linkRepository.save(link));
    }

    @Transactional(readOnly = true)
    public List<HistoricalPlayerLinkResponse> myClaims(Long communityId, String username) {
        Profile owner = member(communityId, username);
        return linkRepository.findByCommunityIdAndProfileIdAndStatus(communityId, owner.getId(), "ACCEPTED")
                .stream().map(this::response).toList();
    }

    @Transactional
    public void releaseMyClaim(Long communityId, Long linkId, String username) {
        Profile owner = member(communityId, username);
        CommunityHistoricalPlayerLink link = linkRepository.findByIdAndCommunityId(linkId, communityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associação não encontrada"));
        if (!link.getProfile().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Só o titular pode remover esta associação");
        }
        if (!"ACCEPTED".equals(link.getStatus())) {
            throw new IllegalStateException("Esta associação já não está ativa");
        }
        // Keep the original imported player and entries: only the association is released.
        link.setStatus("REJECTED");
        link.setConfirmedAt(null);
        linkRepository.save(link);
    }

    private void validateNoOverlappingRounds(Long communityId, Long playerId, Long profileId) {
        // A profile may have several aliases, but never two participations in one round.
        Set<Long> historicalRoundIds = new HashSet<>();
        List<CommunityRoundEntry> entries = entryRepository
                .findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(communityId);
        for (CommunityRoundEntry entry : entries) {
            if (entry.getCommunityPlayer().getId().equals(playerId)) {
                historicalRoundIds.add(entry.getRound().getId());
            }
        }
        Map<Long, Long> confirmedLinks = new HashMap<>();
        for (CommunityHistoricalPlayerLink other : linkRepository.findByCommunityIdAndStatus(communityId, "ACCEPTED")) {
            confirmedLinks.put(other.getPlayer().getId(), other.getProfile().getId());
        }
        for (CommunityRoundEntry entry : entries) {
            if (entry.getCommunityPlayer().getId().equals(playerId) ||
                    !historicalRoundIds.contains(entry.getRound().getId())) continue;
            Profile existing = entry.getProfile() != null ? entry.getProfile()
                    : entry.getCommunityPlayer().getProfile();
            Long otherProfile = existing == null ? confirmedLinks.get(entry.getCommunityPlayer().getId())
                    : existing.getId();
            if (profileId.equals(otherProfile)) {
                throw new IllegalStateException(
                        "Este perfil já tem uma participação na mesma jornada; resolve o conflito antes de associar");
            }
        }
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


}
