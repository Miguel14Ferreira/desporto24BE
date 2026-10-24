package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityInlineHeatProgression;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.dto.community.ConfirmInlineHeatProgressionRequest;
import com.example.desporto24.model.dto.community.CommunityInlineHeatProgressionResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityInlineHeatProgressionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.*;

/**
 * Manual and auditable associations between already-recorded sprint heats.
 * The organizer confirms the target heat's participants using upstream heats;
 * this does not automatically select, promote or certify any athlete.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityInlineHeatProgressionService {
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityRoundRepository rounds;
    private final CommunityRoundEntryRepository entries;
    private final CommunityInlineHeatProgressionRepository progressions;

    @Transactional(readOnly = true)
    public CommunityInlineHeatProgressionResponse get(Long communityId, Long targetId,
            String username) {
        requireMember(communityId, username, false);
        target(communityId, targetId);
        return response(targetId);
    }

    @Transactional(readOnly = true)
    public List<CommunityInlineHeatProgressionResponse> list(Long communityId, String username) {
        requireMember(communityId, username, false);
        var all = progressions.findByCommunityId(communityId);
        return all.stream().map(x -> x.getTargetRound().getId()).distinct()
                .map(this::response).toList();
    }

    public CommunityInlineHeatProgressionResponse confirm(Long communityId, Long targetId,
            String username, ConfirmInlineHeatProgressionRequest request) {
        Profile actor = requireMember(communityId, username, true);
        CommunityRound target = target(communityId, targetId);
        if (target.getSeason() != null && !target.getSeason().isActive())
            throw new IllegalArgumentException("Não alteres uma época já encerrada");
        if (request == null || request.sourceRoundIds() == null || request.sourceRoundIds().isEmpty()
                || request.sourceRoundIds().size() > 20
                || request.reason() == null || request.reason().trim().length() < 10
                || request.reason().trim().length() > 255)
            throw new IllegalArgumentException("Indica as mangas anteriores e uma justificação (10 a 255 caracteres)");

        List<CommunityRound> origins = new ArrayList<>();
        Map<Long, List<CommunityRoundEntry>> sourceEntries = new HashMap<>();
        for (Long id : request.sourceRoundIds()) {
            if (id == null) throw new IllegalArgumentException("Manga de origem inválida");
            CommunityRound source = target(communityId, id);
            origins.add(source);
            sourceEntries.put(id, entries.findByRoundIdOrderByIdAsc(id));
        }
        CommunityInlineProgressionRules.validate(target, origins,
                entries.findByRoundIdOrderByIdAsc(targetId), sourceEntries);
        var oldLinks = progressions.findByTargetRoundIdOrderBySourceRoundId(targetId);
        List<Long> requested = origins.stream().map(CommunityRound::getId).sorted().toList();
        if (oldLinks.stream().map(x -> x.getSourceRound().getId()).toList().equals(requested)
                && oldLinks.stream().allMatch(x -> x.getConfirmationReason().equals(request.reason().trim())))
            return response(targetId);

        // Replacement remains atomic, and all links are removed if the parent round is deleted.
        progressions.deleteByTargetRoundId(targetId);
        progressions.flush();
        Instant now = Instant.now();
        for (CommunityRound source : origins) {
            progressions.save(CommunityInlineHeatProgression.builder()
                    .sourceRound(source)
                    .targetRound(target)
                    .confirmedAt(now)
                    .confirmedByUsername(actor.getUsername())
                    .confirmationReason(request.reason().trim())
                    .build());
        }
        progressions.flush();
        return response(targetId);
    }

    private CommunityInlineHeatProgressionResponse response(Long targetId) {
        var links = progressions.findByTargetRoundIdOrderBySourceRoundId(targetId);
        if (links.isEmpty())
            return new CommunityInlineHeatProgressionResponse(targetId, List.of(), null, null, null);
        var latest = links.get(0);
        return new CommunityInlineHeatProgressionResponse(targetId,
                links.stream().map(x -> x.getSourceRound().getId()).toList(),
                latest.getConfirmedByUsername(),
                latest.getConfirmationReason(),
                latest.getConfirmedAt());
    }

    private CommunityRound target(Long communityId, Long roundId) {
        CommunityRound round = rounds.findById(roundId)
                .orElseThrow(() -> new IllegalArgumentException("Manga não encontrada"));
        if (!round.getCommunity().getId().equals(communityId))
            throw new AccessDeniedException("Manga não pertence a esta comunidade");
        return round;
    }

    private Profile requireMember(Long communityId, String username, boolean admin) {
        Profile actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador não encontrado"));
        var member = members.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (admin && member.getRole() != CommunityRole.OWNER
                && member.getRole() != CommunityRole.ADMIN)
            throw new AccessDeniedException("Apenas administradores podem confirmar o apuramento");
        return actor;
    }
}
