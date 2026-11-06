package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityDisciplinaryWarning;
import com.example.desporto24.model.community.CommunityWarningAppeal;
import com.example.desporto24.model.community.CommunityWarningAppealDecision;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

/**
 * A warning's original audit entry is never deleted or rewritten on review.
 * No disciplinary suspension is activated by this workflow.
 */
@Service
@RequiredArgsConstructor
public class CommunityWarningAppealReviewService {
    private final ProfileRepository profiles;
    private final CommunityRepository communities;
    private final CommunityMemberRepository members;
    private final CommunityDisciplinaryWarningRepository warnings;
    private final CommunityWarningAppealRepository appeals;
    private final CommunityWarningAppealDecisionRepository decisions;

    public record DecisionResponse(Long id, Long appealId, Long communityId,
                                   Long reviewedByProfileId,
                                   CommunityWarningAppealDecision.Outcome outcome,
                                   String reason, Instant decidedAt) {}

    @Transactional
    public DecisionResponse decide(Long communityId, Long appealId, String username,
                                   CommunityWarningAppealDecision.Outcome outcome, String reason) {
        Profile reviewer = profile(username);
        var membership = member(communityId, reviewer.getId());
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas responsáveis podem rever contestações");
        }
        if (appealId == null || appealId <= 0 || outcome == null
                || reason == null || reason.isBlank() || reason.trim().length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Decisão e fundamentação obrigatórias");
        }
        CommunityWarningAppeal appeal = appeals.findById(appealId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contestação não encontrada"));
        if (!communityId.equals(appeal.getCommunityId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contestação não encontrada");
        }
        CommunityDisciplinaryWarning warning = warnings.findById(appeal.getWarningId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertência não encontrada"));
        if (!communityId.equals(warning.getCommunityId())
                || !warning.getTargetProfileId().equals(appeal.getAuthorProfileId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Contestação inconsistente");
        }
        if (reviewer.getId().equals(warning.getIssuedByProfileId())
                || reviewer.getId().equals(appeal.getAuthorProfileId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "O emissor da advertência ou o jogador não pode decidir a contestação");
        }
        if (decisions.existsByAppealId(appealId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Contestação já decidida");
        }
        try {
            return response(decisions.saveAndFlush(CommunityWarningAppealDecision.builder()
                    .appealId(appealId).communityId(communityId)
                    .reviewedByProfileId(reviewer.getId()).outcome(outcome)
                    .reason(reason.trim()).decidedAt(Instant.now()).build()));
        } catch (DataIntegrityViolationException duplicate) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Contestação já decidida", duplicate);
        }
    }

    @Transactional(readOnly = true)
    public List<DecisionResponse> forManagers(Long communityId, String username) {
        var membership = member(communityId, profile(username).getId());
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas responsáveis da comunidade");
        }
        return decisions.findTop100ByCommunityIdOrderByDecidedAtDescIdDesc(communityId)
                .stream().map(CommunityWarningAppealReviewService::response).toList();
    }

    @Transactional(readOnly = true)
    public List<DecisionResponse> mine(Long communityId, String username) {
        Profile actor = profile(username);
        member(communityId, actor.getId());
        var ownAppealIds = appeals.findTop100ByCommunityIdAndAuthorProfileIdOrderByCreatedAtDescIdDesc(
                communityId, actor.getId()).stream().map(CommunityWarningAppeal::getId).toList();
        if (ownAppealIds.isEmpty()) return List.of();
        return decisions.findTop100ByCommunityIdAndAppealIdInOrderByDecidedAtDescIdDesc(
                communityId, ownAppealIds).stream().map(CommunityWarningAppealReviewService::response).toList();
    }

    private Profile profile(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticação necessária");
        }
        return profiles.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta não encontrada"));
    }

    private com.example.desporto24.model.community.CommunityMember member(Long communityId, Long profileId) {
        if (communityId == null || communityId <= 0 || !communities.existsById(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunidade não encontrada");
        }
        return members.findByCommunityIdAndProfileId(communityId, profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem acesso à comunidade"));
    }

    private static DecisionResponse response(CommunityWarningAppealDecision decision) {
        return new DecisionResponse(decision.getId(), decision.getAppealId(), decision.getCommunityId(),
                decision.getReviewedByProfileId(), decision.getOutcome(),
                decision.getReason(), decision.getDecidedAt());
    }
}
