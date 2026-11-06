package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityDisciplinaryWarning;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityWarningAppeal;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityDisciplinaryWarningRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityWarningAppealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

/**
 * A private, immutable player statement. No moderator can change or delete it.
 * Appeals are collected for later independent review; no automatic sanctions.
 */
@Service
@RequiredArgsConstructor
public class CommunityWarningAppealService {
    private final ProfileRepository profiles;
    private final CommunityRepository communities;
    private final CommunityMemberRepository members;
    private final CommunityDisciplinaryWarningRepository warnings;
    private final CommunityWarningAppealRepository appeals;

    public record AppealResponse(Long id, Long warningId, Long communityId,
                                 Long authorProfileId, String statement, Instant createdAt) {}

    @Transactional
    public AppealResponse submit(Long communityId, Long warningId, String username, String statement) {
        Profile actor = actor(username);
        membership(communityId, actor.getId());
        if (warningId == null || warningId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Advertência inválida");
        }
        CommunityDisciplinaryWarning warning = warnings.findById(warningId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertência não encontrada"));
        if (!communityId.equals(warning.getCommunityId())
                || !actor.getId().equals(warning.getTargetProfileId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertência não encontrada");
        }
        if (statement == null || statement.isBlank() || statement.trim().length() > 2000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resposta obrigatória (máx. 2000 caracteres)");
        }
        if (appeals.existsByWarningId(warningId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já foi apresentada uma contestação");
        }
        try {
            return response(appeals.saveAndFlush(CommunityWarningAppeal.builder()
                    .warningId(warningId)
                    .communityId(communityId)
                    .authorProfileId(actor.getId())
                    .statement(statement.trim())
                    .createdAt(Instant.now())
                    .build()));
        } catch (DataIntegrityViolationException conflict) {
            // Database uniqueness remains authoritative under concurrent submissions.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já foi apresentada uma contestação", conflict);
        }
    }

    @Transactional(readOnly = true)
    public List<AppealResponse> mine(Long communityId, String username) {
        Profile actor = actor(username);
        membership(communityId, actor.getId());
        return appeals.findTop100ByCommunityIdAndAuthorProfileIdOrderByCreatedAtDescIdDesc(
                communityId, actor.getId()).stream().map(CommunityWarningAppealService::response).toList();
    }

    @Transactional(readOnly = true)
    public List<AppealResponse> forManagers(Long communityId, String username) {
        Profile actor = actor(username);
        CommunityRole role = membership(communityId, actor.getId()).getRole();
        if (role != CommunityRole.OWNER && role != CommunityRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas responsáveis da comunidade");
        }
        return appeals.findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(communityId)
                .stream().map(CommunityWarningAppealService::response).toList();
    }

    private Profile actor(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticação necessária");
        }
        return profiles.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta não encontrada"));
    }

    private CommunityMember membership(Long communityId, Long profileId) {
        if (communityId == null || communityId <= 0 || !communities.existsById(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunidade não encontrada");
        }
        return members.findByCommunityIdAndProfileId(communityId, profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem acesso à comunidade"));
    }

    private static AppealResponse response(CommunityWarningAppeal appeal) {
        return new AppealResponse(appeal.getId(), appeal.getWarningId(), appeal.getCommunityId(),
                appeal.getAuthorProfileId(), appeal.getStatement(), appeal.getCreatedAt());
    }
}
