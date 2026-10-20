package com.example.desporto24.service;

import com.example.desporto24.controllers.OnlinePresenceController;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OnlinePresenceServiceTest {
    @Mock ProfileRepository profiles;
    @Mock JdbcTemplate jdbc;
    @InjectMocks OnlinePresenceService service;

    @Test
    void authenticatedActiveAccountUpdatesOneRowNotOnePerBrowserTab() {
        var account = new Profile("member", "password");
        account.setId(42L);
        account.setEnabled(true);
        account.setNotLocked(true);
        when(profiles.findUserByUsername("member")).thenReturn(Optional.of(account));

        service.heartbeat("member");
        service.heartbeat("member");

        verify(jdbc, times(2)).update(
                contains("ON CONFLICT (profile_id) DO UPDATE"),
                eq(42L), any(Timestamp.class));
    }

    @Test
    void inactiveOrAnonymizedAccountsCannotBecomeOnline() {
        var user = new Profile("disabled", "password");
        user.setId(5L);
        user.setEnabled(false);
        user.setNotLocked(true);
        when(profiles.findUserByUsername("disabled")).thenReturn(Optional.of(user));
        assertEquals(HttpStatus.UNAUTHORIZED,
                assertThrows(ResponseStatusException.class,
                        () -> service.heartbeat("disabled")).getStatusCode());
        var deleted = new Profile("deleted", "password");
        deleted.setId(6L);
        deleted.setEnabled(true);
        deleted.setNotLocked(true);
        deleted.setEmail("gone@deleted.invalid");
        when(profiles.findUserByUsername("deleted")).thenReturn(Optional.of(deleted));
        assertEquals(HttpStatus.UNAUTHORIZED,
                assertThrows(ResponseStatusException.class,
                        () -> service.heartbeat("deleted")).getStatusCode());
        verifyNoInteractions(jdbc);
    }

    @Test
    void onlineCountUsesRecentHeartbeatWindowNotLastLogin() {
        Instant at = Instant.parse("2026-10-17T15:00:00Z");
        when(jdbc.queryForObject(anyString(), eq(Long.class), any(Timestamp.class)))
                .thenReturn(9L);
        assertEquals(9L, service.countOnlineAt(at));
        verify(jdbc).queryForObject(
                contains("last_seen_at >= ?"), eq(Long.class),
                eq(Timestamp.from(at.minusSeconds(OnlinePresenceService.WINDOW_SECONDS))));
    }

    @Test
    void presenceEndpointIsAuthenticatedOnly() {
        var policy = OnlinePresenceController.class.getAnnotation(PreAuthorize.class);
        assertNotNull(policy);
        assertEquals("isAuthenticated()", policy.value());
    }
}
