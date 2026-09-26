package com.example.desporto24.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfilePrincipalTest {

    @Test
    void authoritiesIncludeRoleAndConfiguredPermissions() {
        Profile profile = new Profile();
        profile.setUsername("admin");
        profile.setPassword("encoded");
        profile.setRole("ROLE_ADMIN");
        profile.setAuthorities(new String[]{"user:read", "user:update"});

        ProfilePrincipal principal = new ProfilePrincipal(profile);

        Set<String> authorities = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertTrue(authorities.contains("user:read"));
        assertTrue(authorities.contains("user:update"));
    }
}
