package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


/** Creation limits count ownership, not group membership or admin membership. */
@Service
@RequiredArgsConstructor
public class CommunityCreationQuotaService {
    public static final int DEFAULT_ACTIVE_LIMIT = 5;

    private final ProfileRepository profiles;
    private final CommunityRepository communities;
    private final JdbcTemplate jdbc;

    public record Quota(long owned, int limit, boolean canCreate) {}

    @Transactional(readOnly = true)
    public Quota quota(Profile actor) {
        return snapshot(actor);
    }

    /**
     * Locks the profile row in the caller's transaction. Two simultaneous
     * creations or transfers for the same owner cannot both claim the last
     * available slot. Only the number of currently owned groups matters.
     */
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.MANDATORY)
    public void reserveCreate(Profile owner) {
        Profile locked = profiles.lockForCommunityQuota(owner.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        Quota current = snapshot(locked);
        if (current.owned() >= current.limit()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Atingiste o limite de " + current.limit() + " comunidades próprias. Contacta o suporte para pedir um aumento.");
        }
    }

    /** A transfer must not bypass the five-owned-community threshold. */
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.MANDATORY)
    public void requireTransferCapacity(Profile nextOwner) {
        Profile locked = profiles.lockForCommunityQuota(nextOwner.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        Quota current = snapshot(locked);
        if (current.owned() >= current.limit()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O novo proprietário já atingiu o limite de comunidades próprias.");
        }
    }

    @Transactional
    public Quota updateOverride(Long targetId, Long adminId,
                                Integer limit, String reason) {
        if (limit != null && (limit < 1 || limit > 100)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O limite deve estar entre 1 e 100");
        }
        if (reason == null || reason.isBlank() || reason.length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Indica o motivo da alteração (até 500 caracteres)");
        }
        Profile target = profiles.lockForCommunityQuota(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        if (!java.util.Objects.equals(target.getCommunityCreationLimit(), limit)) {
            Integer previous = target.getCommunityCreationLimit();
            target.setCommunityCreationLimit(limit);
            profiles.save(target);
            jdbc.update("""
                    INSERT INTO desporto24.community_creation_limit_audit
                    (actor_profile_id, target_profile_id, previous_limit, new_limit, reason, changed_at)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """, adminId, targetId, previous, limit, reason.trim(), java.time.OffsetDateTime.now(java.time.ZoneOffset.UTC));
        }
        return snapshot(target);
    }

    private Quota snapshot(Profile owner) {
        int effective = owner.getCommunityCreationLimit() == null
                ? DEFAULT_ACTIVE_LIMIT : owner.getCommunityCreationLimit();
        long active = communities.countByOwnerId(owner.getId());
        return new Quota(active, effective, active < effective);
    }
}
