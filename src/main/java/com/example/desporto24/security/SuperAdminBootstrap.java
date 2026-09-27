package com.example.desporto24.security;

import com.example.desporto24.enumeration.Role;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuperAdminBootstrap {

    private final ProfileRepository profileRepository;

    @Value("${desporto.bootstrap.super-admin-username:}")
    private String bootstrapUsername;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void promoteConfiguredSuperAdmin() {
        String username = bootstrapUsername == null ? "" : bootstrapUsername.trim();
        if (username.isBlank()) {
            return;
        }

        Profile profile = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElse(null);
        if (profile == null) {
            log.warn("Configured super-admin user was not found: {}", username);
            return;
        }

        String expectedRole = Role.ROLE_SUPER_ADMIN.name();
        String[] expectedAuthorities = Role.ROLE_SUPER_ADMIN.getAuthorities();

        boolean roleChanged = !expectedRole.equals(profile.getRole());
        boolean authoritiesChanged = !Arrays.equals(expectedAuthorities, profile.getAuthorities());

        if (!roleChanged && !authoritiesChanged) {
            return;
        }

        profile.setRole(expectedRole);
        profile.setAuthorities(expectedAuthorities);
        profile.setTokenVersion(profile.getTokenVersion() + 1);
        profileRepository.save(profile);

        log.info("Configured super-admin role applied to {}", username);
    }
}
