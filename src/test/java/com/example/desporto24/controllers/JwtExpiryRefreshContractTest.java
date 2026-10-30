package com.example.desporto24.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.desporto24.configuration.SecurityConfiguration;
import com.example.desporto24.constant.SecurityConstant;
import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.filter.JwtAccessDeniedHandler;
import com.example.desporto24.filter.JwtAuthenticationEntryPoint;
import com.example.desporto24.filter.JwtAuthorizationFilter;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.RefreshTokenSessionRepository;
import com.example.desporto24.security.SensitiveActionRateLimiter;
import com.example.desporto24.security.RefreshOriginValidator;
import com.example.desporto24.security.RefreshTokenRotationService;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityHistoricalMatchArchiveService;
import com.example.desporto24.service.CommunityHistoricalMatchPreviewService;
import com.example.desporto24.service.CommunityHistoricalPlayerLinkService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunitySessionService;
import com.example.desporto24.service.CommunityStandingsArchiveService;
import com.example.desporto24.service.GoogleAuthenticationService;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.utility.JWTokenProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The browser-facing 403 -> refresh -> retry contract against real HMAC512 JWT
 * verification, the real SecurityFilterChain, controllers, and cookie writer.
 *
 * AuthenticationService is mocked here: real refresh-service token validation
 * is covered separately; this test does not use an external DB or real accounts.
 */
@WebMvcTest(controllers = {CommunityController.class, AuthenticationController.class})
@AutoConfigureMockMvc(addFilters = false)
@Import({
        SecurityConfiguration.class,
        JwtAuthorizationFilter.class,
        JWTokenProvider.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class,
        ExceptionHandling.class,
        RefreshOriginValidator.class
})
@TestPropertySource(properties = {
        "desporto.cors.allowed-origins=http://localhost:4200",
        "desporto.app.jwtSecret=fixture-hmac512-key-long-enough-for-isolated-jwt-expiry-test-12345678",
        "desporto.app.jwtExpirationMs=1800000",
        "desporto.app.refreshExpirationMs=604800000",
        "springdoc.api-docs.enabled=false",
        "desporto.media.provider=s3"
})
class JwtExpiryRefreshContractTest {

    private static final String USERNAME = "fixture-user";
    private static final String SECRET =
            "fixture-hmac512-key-long-enough-for-isolated-jwt-expiry-test-12345678";

    @Autowired private WebApplicationContext context;
    @Autowired private FilterChainProxy chain;
    @Autowired private JWTokenProvider jwt;

    @MockitoBean private ProfileRepository profiles;
    @MockitoBean private RefreshTokenSessionRepository refreshSessionRepository;
    @MockitoBean private AuthenticationService auth;
    @MockitoBean private SensitiveActionRateLimiter rateLimiter;
    @MockitoBean private RefreshTokenRotationService refreshTokenRotationService;
    @MockitoBean private GoogleAuthenticationService google;
    @MockitoBean private CommunityManagementService communities;
    @MockitoBean private CommunityCompetitionService competitions;
    @MockitoBean private CommunityHistoricalPlayerLinkService historicalPlayers;
    @MockitoBean private CommunityImportService imports;
    @MockitoBean private CommunityStandingsArchiveService archives;
    @MockitoBean private CommunityHistoricalMatchPreviewService previews;
    @MockitoBean private CommunityHistoricalMatchArchiveService historicalMatches;
    @MockitoBean private CommunitySessionService sessions;
    @MockitoBean private com.example.desporto24.service.CommunitySessionResultService sessionResults;

    private MockMvc mvc;
    private Profile profile;

    @BeforeEach
    void fixture() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(chain)
                .build();
        profile = new Profile();
        profile.setUsername(USERNAME);
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setTokenVersion(7L);
        profile.setAuthorities(new String[0]);
        profile.setRole("ROLE_USER");
        when(profiles.findUserByUsername(USERNAME)).thenReturn(Optional.of(profile));
    }

    @Test
    void expiredSignedAccessTokenCanRecoverThroughValidHttpOnlyRefreshCookie() throws Exception {
        String expired = JWT.create()
                .withIssuer(SecurityConstant.DESPORTO_24)
                .withAudience(SecurityConstant.DESPORTO_24_ADMINISTRATION)
                .withSubject(USERNAME)
                .withIssuedAt(Date.from(Instant.now().minusSeconds(3600)))
                .withExpiresAt(Date.from(Instant.now().minusSeconds(60)))
                .withClaim(SecurityConstant.TOKEN_VERSION, 7L)
                .withClaim(SecurityConstant.TOKEN_TYPE, SecurityConstant.ACCESS_TOKEN_TYPE)
                .sign(Algorithm.HMAC512(SECRET));

        String refresh = jwt.generateRefreshToken(new ProfilePrincipal(profile));
        String recovered = jwt.generateJwtToken(new ProfilePrincipal(profile));

        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(USERNAME);
        when(auth.refresh(refresh))
                .thenReturn(new LoginResponse(false, null, null, recovered, dto));
        when(auth.createRefreshToken(USERNAME)).thenReturn(refresh);
        when(communities.mine(USERNAME)).thenReturn(List.of());

        // A real expired, signed access JWT is denied at the filter, not in a mock.
        mvc.perform(get("/api/communities/mine")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + expired))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message").value(
                        SecurityConstant.FORBIDDEN_MESSAGE));
        verifyNoInteractions(communities);

        // The browser's HttpOnly refresh cookie can obtain a fresh signed JWT.
        var response = mvc.perform(post("/api/auth/refresh")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                        .cookie(new Cookie(SecurityConstant.REFRESH_TOKEN_COOKIE, refresh)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(recovered))
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andReturn().getResponse();
        String cookie = response.getHeader(HttpHeaders.SET_COOKIE);
        assertTrue(cookie.contains("HttpOnly"));
        assertTrue(cookie.contains("Secure"));
        assertTrue(cookie.contains("SameSite=None"));
        assertTrue(cookie.contains("Path=/api/auth"));

        mvc.perform(get("/api/communities/mine")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + recovered))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(auth).refresh(refresh);
        verify(communities).mine(USERNAME);
    }

    @Test
    void signedRefreshTokenCannotBeUsedAsAccessBearer() throws Exception {
        String refresh = jwt.generateRefreshToken(new ProfilePrincipal(profile));

        mvc.perform(get("/api/communities/mine")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refresh))
                .andExpect(status().isForbidden());

        verifyNoInteractions(communities);
    }

    @Test
    void refreshWithNoCookieIsRejectedWithoutAttemptingTheRefreshService() throws Exception {
        mvc.perform(post("/api/auth/refresh")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200"))
                .andExpect(status().isBadRequest());
        verify(auth, never()).refresh(anyString());
    }

    @Test
    void rejectedRefreshCookieDoesNotReturnAnAccessToken() throws Exception {
        when(auth.refresh("invalid-cookie")).thenThrow(
                new BadCredentialsException("Invalid test credential"));
        mvc.perform(post("/api/auth/refresh")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                        .cookie(new Cookie(SecurityConstant.REFRESH_TOKEN_COOKIE, "invalid-cookie")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatusCode").value(400));
        verify(auth).refresh("invalid-cookie");
    }
    @Test
    void refreshRejectsMissingOriginBeforeAuthenticationServiceIsCalled() throws Exception {
        String refresh = jwt.generateRefreshToken(new ProfilePrincipal(profile));
        mvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie(SecurityConstant.REFRESH_TOKEN_COOKIE, refresh)))
                .andExpect(status().isForbidden());
        verify(auth, never()).refresh(anyString());
    }

    @Test
    void refreshRejectsCrossSiteOriginBeforeAuthenticationServiceIsCalled() throws Exception {
        String refresh = jwt.generateRefreshToken(new ProfilePrincipal(profile));
        mvc.perform(post("/api/auth/refresh")
                        .header(HttpHeaders.ORIGIN, "https://untrusted.example")
                        .cookie(new Cookie(SecurityConstant.REFRESH_TOKEN_COOKIE, refresh)))
                .andExpect(status().isForbidden());
        verify(auth, never()).refresh(anyString());
    }

}
