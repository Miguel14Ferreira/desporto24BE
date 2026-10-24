package com.example.desporto24.filter;

import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.RefreshTokenSessionRepository;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Instant;
import com.example.desporto24.utility.JWTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JwtAuthorizationFilterTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void currentTokenVersionAuthenticatesUser() throws Exception {
        JWTokenProvider provider = mock(JWTokenProvider.class);
        ProfileRepository repository = mock(ProfileRepository.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        Profile profile = activeProfile(3L);

        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");
        when(provider.getSubject("token")).thenReturn("miguel");
        when(provider.getTokenVersion("token")).thenReturn(3L);
        when(provider.isTokenValid("miguel", "token"))
                .thenReturn(true);
        when(provider.getAuthorities("token"))
                .thenReturn(List.of());
        when(provider.getAuthentication(
                eq("miguel"),
                anyList(),
                eq(request)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        "miguel",
                        null,
                        List.of()
                ));
        when(repository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));

        JwtAuthorizationFilter filter =
                new JwtAuthorizationFilter(provider, repository);

        filter.doFilterInternal(request, response, chain);

        assertNotNull(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        );
        verify(chain).doFilter(request, response);
    }

    @Test
    void revokedDeviceFamilyCannotUseItsUnexpiredAccessJwt() throws Exception {
        assertBoundFamilyAccepted(false);
    }

    @Test
    void activeDeviceFamilyCanStillUseItsAccessJwt() throws Exception {
        assertBoundFamilyAccepted(true);
    }

    private void assertBoundFamilyAccepted(boolean active) throws Exception {
        JWTokenProvider provider = mock(JWTokenProvider.class);
        ProfileRepository repository = mock(ProfileRepository.class);
        RefreshTokenSessionRepository sessions = mock(RefreshTokenSessionRepository.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        Profile profile = activeProfile(3L);
        profile.setId(42L);

        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(provider.getSubject("token")).thenReturn("miguel");
        when(provider.getTokenVersion("token")).thenReturn(3L);
        when(provider.getAccessTokenFamilyId("token")).thenReturn("device-family");
        when(repository.findUserByUsername("miguel")).thenReturn(Optional.of(profile));
        when(sessions.existsByProfileIdAndFamilyIdAndConsumedAtIsNullAndRevokedAtIsNullAndExpiresAtAfter(
                eq(42L), eq("device-family"), any(Instant.class))).thenReturn(active);
        if (active) {
            when(provider.isTokenValid("miguel", "token")).thenReturn(true);
            when(provider.getAuthorities("token")).thenReturn(List.of());
            when(provider.getAuthentication(eq("miguel"), anyList(), eq(request)))
                    .thenReturn(new UsernamePasswordAuthenticationToken("miguel", null, List.of()));
        }

        var filter = new JwtAuthorizationFilter(provider, repository);
        ReflectionTestUtils.setField(filter, "refreshTokenSessions", sessions);
        filter.doFilterInternal(request, response, chain);

        if (active) assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        else {
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            verify(provider, never()).getAuthentication(anyString(), anyList(), any());
        }
        verify(chain).doFilter(request, response);
    }

    @Test
    void staleTokenVersionIsRejected() throws Exception {
        JWTokenProvider provider = mock(JWTokenProvider.class);
        ProfileRepository repository = mock(ProfileRepository.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");
        when(provider.getSubject("token"))
                .thenReturn("miguel");
        when(provider.getTokenVersion("token"))
                .thenReturn(2L);
        when(repository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(activeProfile(3L)));

        JwtAuthorizationFilter filter =
                new JwtAuthorizationFilter(provider, repository);

        filter.doFilterInternal(request, response, chain);

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        );
        verify(provider, never()).getAuthentication(
                anyString(),
                anyList(),
                any()
        );
        verify(chain).doFilter(request, response);
    }

    @Test
    void lockedAccountTokenIsRejected() throws Exception {
        JWTokenProvider provider = mock(JWTokenProvider.class);
        ProfileRepository repository = mock(ProfileRepository.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        Profile profile = activeProfile(1L);
        profile.setNotLocked(false);

        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");
        when(provider.getSubject("token"))
                .thenReturn("miguel");
        when(provider.getTokenVersion("token"))
                .thenReturn(1L);
        when(repository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));

        JwtAuthorizationFilter filter =
                new JwtAuthorizationFilter(provider, repository);

        filter.doFilterInternal(request, response, chain);

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        );
        verify(chain).doFilter(request, response);
    }

    @Test
    void missingEnabledFlagCannotAuthenticateEvenWithValidSignedToken() throws Exception {
        assertProfileWithoutExplicitActiveFlagsIsDenied(true);
    }

    @Test
    void missingNotLockedFlagCannotAuthenticateEvenWithValidSignedToken() throws Exception {
        assertProfileWithoutExplicitActiveFlagsIsDenied(false);
    }

    private void assertProfileWithoutExplicitActiveFlagsIsDenied(boolean missingEnabled)
            throws Exception {
        JWTokenProvider provider = mock(JWTokenProvider.class);
        ProfileRepository repository = mock(ProfileRepository.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        Profile profile = activeProfile(3L);
        if (missingEnabled) profile.setEnabled(null);
        else profile.setNotLocked(null);

        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(provider.getSubject("token")).thenReturn("miguel");
        when(provider.getTokenVersion("token")).thenReturn(3L);
        when(repository.findUserByUsername("miguel")).thenReturn(Optional.of(profile));

        new JwtAuthorizationFilter(provider, repository)
                .doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(provider, never()).getAuthentication(anyString(), anyList(), any());
        verify(chain).doFilter(request, response);
    }

    private Profile activeProfile(long version) {
        Profile profile = new Profile();
        profile.setUsername("miguel");
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setTokenVersion(version);
        return profile;
    }
}
