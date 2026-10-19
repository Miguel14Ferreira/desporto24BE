package com.example.desporto24.service;

import com.example.desporto24.model.AdminPlayerModerationAudit;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.AdminPlayerModerationAuditRepository;
import com.example.desporto24.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

@Service
public class AdminPlayerModerationService {
    private final ProfileRepository profiles;
    private final AdminPlayerModerationAuditRepository audits;
    private final AccountDeletionService deletion;

    public AdminPlayerModerationService(ProfileRepository profiles,
            AdminPlayerModerationAuditRepository audits, AccountDeletionService deletion) {
        this.profiles = profiles;
        this.audits = audits;
        this.deletion = deletion;
    }

    @Transactional
    public ModerationResult moderate(String actorUsername, Long targetId, ModerationRequest request) {
        if (targetId == null || targetId <= 0 || request == null || request.action() == null
                || request.expectedUsername() == null || request.expectedSuspended() == null
                || request.reason() == null || request.reason().trim().length() < 10
                || request.reason().trim().length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action and reason (10-500 characters) required");
        }
        Action action;
        try {
            action = Action.valueOf(request.action());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown moderation action");
        }

        Profile actorLookup = profiles.findUserByUsername(actorUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Administrator required"));
        var locked = new HashMap<Long, Profile>();
        for (Long id : new TreeSet<>(List.of(actorLookup.getId(), targetId))) {
            locked.put(id, profiles.findForRoleChange(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found")));
        }
        Profile actor = locked.get(actorLookup.getId());
        boolean superAdmin = "ROLE_SUPER_ADMIN".equals(actor.getRole());
        if ((!superAdmin && !"ROLE_ADMIN".equals(actor.getRole()))
                || !Boolean.TRUE.equals(actor.getEnabled())
                || !Boolean.TRUE.equals(actor.getNotLocked())
                || actor.getModerationSuspendedAt() != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Administrator required");
        }
        if (actor.getId().equals(targetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot moderate yourself");
        }
        if (action == Action.DELETE && !superAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Super administrator required for deletion");
        }

        Profile target = locked.get(targetId);
        if (target.getEmail() != null
                && target.getEmail().toLowerCase(Locale.ROOT).endsWith("@deleted.invalid")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
        if (!"ROLE_USER".equals(target.getRole())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Administrator accounts are protected");
        }
        if (!request.expectedUsername().equals(target.getUsername())
                || request.expectedSuspended() != (target.getModerationSuspendedAt() != null)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account changed; search again");
        }

        Instant now = Instant.now();
        if (action == Action.SUSPEND) {
            if (!Boolean.TRUE.equals(target.getEnabled()) || !Boolean.TRUE.equals(target.getNotLocked())
                    || target.getModerationSuspendedAt() != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Account is not active");
            }
            target.setModerationSuspendedAt(now);
            target.setNotLocked(false);
            target.setTokenVersion(Math.addExact(target.getTokenVersion(), 1));
            profiles.save(target);
        } else if (action == Action.REINSTATE) {
            if (!Boolean.TRUE.equals(target.getEnabled()) || target.getModerationSuspendedAt() == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Account has no moderation suspension");
            }
            target.setModerationSuspendedAt(null);
            target.setNotLocked(true);
            target.setLoginAttempts(0);
            target.setTokenVersion(Math.addExact(target.getTokenVersion(), 1));
            profiles.save(target);
        } else {
            // Reuse the account privacy and relational cleanup path.
            // Community owners must transfer ownership before deletion.
            deletion.deleteMyAccount(target.getUsername());
        }

        audits.save(new AdminPlayerModerationAudit(actor.getId(), targetId,
                action.name(), request.reason().trim(), now));
        return new ModerationResult(targetId, action.name(),
                action == Action.DELETE ? null : target.getModerationSuspendedAt(),
                action != Action.DELETE && Boolean.TRUE.equals(target.getNotLocked()));
    }

    public enum Action { SUSPEND, REINSTATE, DELETE }
    public record ModerationRequest(String action, String expectedUsername,
                                    Boolean expectedSuspended, String reason) {}
    public record ModerationResult(Long id, String action, Instant moderationSuspendedAt, boolean notLocked) {}
}
