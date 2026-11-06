package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityDisciplinaryWarning;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityDisciplinaryWarningRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

/**
 * First discipline milestone: auditable warnings only. A warning never revokes
 * participation, changes match results, or suspends a platform account.
 */
@Service
@RequiredArgsConstructor
public class CommunityDisciplineService {
    private final ProfileRepository profiles;
    private final CommunityRepository communities;
    private final CommunityMemberRepository members;
    private final CommunityDisciplinaryWarningRepository warnings;

    public record WarningResponse(Long id, Long communityId, Long targetProfileId,
                                  Long issuedByProfileId, String reason, Instant createdAt) {}

    @Transactional
    public WarningResponse warn(Long communityId, String username, Long targetProfileId, String reason) {
        Profile actor = actor(username);
        requireManager(communityId, actor.getId());
        if (targetProfileId == null || targetProfileId <= 0 || targetProfileId.equals(actor.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destinatário inválido");
        }
        if (reason == null || reason.isBlank() || reason.trim().length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Motivo obrigatório (máx. 1000 caracteres)");
        }
        CommunityMember target = members.findByCommunityIdAndProfileId(communityId, targetProfileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado"));
        if (target.getRole() != CommunityRole.MEMBER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "A gestão disciplinar de owners e administradores exige revisão pela plataforma");
        }
        return response(warnings.save(CommunityDisciplinaryWarning.builder()
                .communityId(communityId)
                .targetProfileId(targetProfileId)
                .issuedByProfileId(actor.getId())
                .reason(reason.trim())
                .createdAt(Instant.now())
                .build()));
    }

    @Transactional(readOnly = true)
    public List<WarningResponse> listForManagers(Long communityId, String username) {
        requireManager(communityId, actor(username).getId());
        return warnings.findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(communityId)
                .stream().map(CommunityDisciplineService::response).toList();
    }

    @Transactional(readOnly = true)
    public List<WarningResponse> mine(Long communityId, String username) {
        Profile current = actor(username);
        requireMembership(communityId, current.getId());
        return warnings.findTop100ByCommunityIdAndTargetProfileIdOrderByCreatedAtDescIdDesc(
                communityId, current.getId()).stream().map(CommunityDisciplineService::response).toList();
    }

    private Profile actor(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticação necessária");
        }
        return profiles.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta não encontrada"));
    }

    private CommunityMember requireMembership(Long communityId, Long profileId) {
        if (communityId == null || communityId <= 0 || !communities.existsById(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunidade não encontrada");
        }
        return members.findByCommunityIdAndProfileId(communityId, profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem acesso à comunidade"));
    }

    private void requireManager(Long communityId, Long profileId) {
        CommunityRole role = requireMembership(communityId, profileId).getRole();
        if (role != CommunityRole.OWNER && role != CommunityRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas responsáveis da comunidade");
        }
    }

    private static WarningResponse response(CommunityDisciplinaryWarning warning) {
        return new WarningResponse(warning.getId(), warning.getCommunityId(),
                warning.getTargetProfileId(), warning.getIssuedByProfileId(),
                warning.getReason(), warning.getCreatedAt());
    }
}
