package com.example.desporto24.service;

import com.example.desporto24.enumeration.Role;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.AdminRoleAudit;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.AdminRoleAuditRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeSet;
import java.util.List;

@Service
public class AdminRoleService {
    private final ProfileRepository profiles;
    private final AdminRoleAuditRepository audits;

    public AdminRoleService(ProfileRepository profiles, AdminRoleAuditRepository audits) {
        this.profiles = profiles;
        this.audits = audits;
    }

    @Transactional
    public RoleResult changeRole(String actorUsername, Long targetId, String requestedRole, String expectedRole) {
        if (targetId == null || targetId <= 0
                || (!"ROLE_USER".equals(requestedRole) && !"ROLE_ADMIN".equals(requestedRole))
                || expectedRole == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role change");
        }
        Profile lookup = profiles.findUserByUsername(actorUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Super administrator required"));
        // Lock in stable order, including the actor, so concurrent role changes cannot bypass authorization.
        var locked = new HashMap<Long, Profile>();
        for (Long id : new TreeSet<>(List.of(lookup.getId(), targetId))) {
            locked.put(id, profiles.findForRoleChange(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found")));
        }
        Profile actor = locked.get(lookup.getId());
        if (!"ROLE_SUPER_ADMIN".equals(actor.getRole())
                || !Boolean.TRUE.equals(actor.getEnabled())
                || !Boolean.TRUE.equals(actor.getNotLocked())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Super administrator required");
        }
        Profile target = locked.get(targetId);
        if (target.getEmail() != null && target.getEmail().toLowerCase(Locale.ROOT).endsWith("@deleted.invalid")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
        String previousRole = target.getRole();
        if (!"ROLE_USER".equals(previousRole) && !"ROLE_ADMIN".equals(previousRole)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This role is protected");
        }
        if (!expectedRole.equals(previousRole)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role changed; refresh the player");
        }
        if ("ROLE_ADMIN".equals(requestedRole)
                && (!Boolean.TRUE.equals(target.getEnabled()) || !Boolean.TRUE.equals(target.getNotLocked()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only active players can become administrators");
        }
        if (previousRole.equals(requestedRole)) {
            return new RoleResult(target.getId(), previousRole);
        }
        Role role = Role.valueOf(requestedRole);
        target.setRole(role.name());
        target.setAuthorities(role.getAuthorities().clone());
        target.setTokenVersion(Math.addExact(target.getTokenVersion(), 1));
        profiles.save(target);
        audits.save(new AdminRoleAudit(actor.getId(), target.getId(), previousRole, requestedRole, Instant.now()));
        return new RoleResult(target.getId(), requestedRole);
    }

    public record RoleResult(Long id, String role) {}
}
