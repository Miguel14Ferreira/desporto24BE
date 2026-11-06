package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityTournamentSuspension;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentSuspensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Limited, auditable tournament eligibility restrictions, not an account-wide ban. */
@Service
@RequiredArgsConstructor
public class CommunityTournamentSuspensionService {
    public static final int MAX_DAYS = 30;
    private final ProfileRepository profiles;
    private final CommunityRepository communities;
    private final CommunityMemberRepository members;
    private final CommunityTournamentSuspensionRepository suspensions;

    public record SuspensionResponse(Long id, Long communityId, Long targetProfileId,
            Long issuedByProfileId, String reason, Instant startsAt, Instant endsAt,
            Instant revokedAt, Long revokedByProfileId, String revocationReason) {}

    @Transactional
    public SuspensionResponse issue(Long communityId, String username, Long targetProfileId,
                                    int days, String reason) {
        Profile actor = actor(username);
        requireManager(communityId, actor.getId());
        if (targetProfileId == null || targetProfileId <= 0 || targetProfileId.equals(actor.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jogador inválido");
        }
        if (days < 1 || days > MAX_DAYS || reason == null || reason.isBlank()
                || reason.trim().length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duração ou motivo inválido");
        }
        var membership = members.findByCommunityIdAndProfileId(communityId, targetProfileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado"));
        if (membership.getRole() != CommunityRole.MEMBER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Não podes suspender owners ou administradores");
        }
        Instant now = Instant.now();
        if (suspensions.countActive(communityId, targetProfileId, now) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe uma suspensão ativa; não é possível acumulá-la");
        }
        return response(suspensions.save(CommunityTournamentSuspension.builder()
                .communityId(communityId).targetProfileId(targetProfileId)
                .issuedByProfileId(actor.getId()).reason(reason.trim())
                .startsAt(now).endsAt(now.plus(days, ChronoUnit.DAYS)).build()));
    }

    /**
     * Called server-side in every tournament enrollment path.
     * No suspension of unrelated communities or platform accounts.
     */
    @Transactional(readOnly = true)
    public void requireEligible(Long communityId, Long profileId) {
        if (suspensions.countActive(communityId, profileId, Instant.now()) > 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Estás suspenso dos torneios desta comunidade");
        }
    }

    @Transactional
    public SuspensionResponse revoke(Long communityId, Long suspensionId, String username, String reason) {
        Profile actor = actor(username);
        requireManager(communityId, actor.getId());
        if (reason == null || reason.isBlank() || reason.trim().length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fundamentação obrigatória");
        }
        CommunityTournamentSuspension suspension = suspensions.findById(suspensionId)
                .filter(item -> communityId.equals(item.getCommunityId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Suspensão não encontrada"));
        if (suspension.getRevokedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Suspensão já levantada");
        }
        suspension.setRevokedAt(Instant.now());
        suspension.setRevokedByProfileId(actor.getId());
        suspension.setRevocationReason(reason.trim());
        return response(suspensions.save(suspension));
    }

    @Transactional(readOnly = true)
    public List<SuspensionResponse> forManagers(Long communityId, String username) {
        requireManager(communityId, actor(username).getId());
        return suspensions.findTop100ByCommunityIdOrderByStartsAtDescIdDesc(communityId)
                .stream().map(CommunityTournamentSuspensionService::response).toList();
    }

    @Transactional(readOnly = true)
    public List<SuspensionResponse> mine(Long communityId, String username) {
        Profile current = actor(username);
        requireMember(communityId, current.getId());
        return suspensions.findTop100ByCommunityIdAndTargetProfileIdOrderByStartsAtDescIdDesc(
                communityId, current.getId()).stream().map(CommunityTournamentSuspensionService::response).toList();
    }

    private Profile actor(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticação necessária");
        }
        return profiles.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta não encontrada"));
    }

    private com.example.desporto24.model.community.CommunityMember requireMember(
            Long communityId, Long profileId) {
        if (communityId == null || communityId <= 0 || !communities.existsById(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunidade não encontrada");
        }
        return members.findByCommunityIdAndProfileId(communityId, profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem acesso à comunidade"));
    }

    private void requireManager(Long communityId, Long profileId) {
        var role = requireMember(communityId, profileId).getRole();
        if (role != CommunityRole.OWNER && role != CommunityRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas responsáveis da comunidade");
        }
    }

    private static SuspensionResponse response(CommunityTournamentSuspension entry) {
        return new SuspensionResponse(entry.getId(), entry.getCommunityId(),
                entry.getTargetProfileId(), entry.getIssuedByProfileId(), entry.getReason(),
                entry.getStartsAt(), entry.getEndsAt(), entry.getRevokedAt(),
                entry.getRevokedByProfileId(), entry.getRevocationReason());
    }
}
