package com.example.desporto24.controllers;

import com.example.desporto24.configuration.SecurityConfiguration;
import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.filter.JwtAccessDeniedHandler;
import com.example.desporto24.filter.JwtAuthenticationEntryPoint;
import com.example.desporto24.filter.JwtAuthorizationFilter;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.RefreshTokenSessionRepository;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityHistoricalMatchArchiveService;
import com.example.desporto24.service.CommunityHistoricalMatchPreviewService;
import com.example.desporto24.service.CommunityHistoricalPlayerLinkService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunitySessionService;
import com.example.desporto24.service.CommunityStandingsArchiveService;
import com.example.desporto24.utility.JWTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Exercises the real Spring SecurityFilterChain and JwtAuthorizationFilter,
 * real MVC/controller mapping and ExceptionHandling. Profiles, token provider
 * and community services are mocked: no database, network or actual JWTs.
 *
 * The application uses a custom anonymous authentication entry point returning
 * 403 (rather than 401). Keep expectations aligned with that explicit policy.
 */
@WebMvcTest(controllers = {CommunityController.class, SessionGuestRequestController.class})
@AutoConfigureMockMvc(addFilters = false)
@Import({
        SecurityConfiguration.class,
        JwtAuthorizationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class,
        ExceptionHandling.class
})
@TestPropertySource(properties = {
        "desporto.cors.allowed-origins=http://localhost:4200",
        "springdoc.api-docs.enabled=false",
        "desporto.media.provider=s3"
})
class CommunitySecurityFilterChainTest {

    @Autowired private WebApplicationContext webContext;
    @Autowired private FilterChainProxy springSecurityFilterChain;
    private MockMvc mvc;

    @MockitoBean private JWTokenProvider tokens;
    @MockitoBean private ProfileRepository profiles;
    @MockitoBean private RefreshTokenSessionRepository refreshSessionRepository;
    @MockitoBean private CommunityManagementService communities;
    @MockitoBean private CommunityCompetitionService competitions;
    @MockitoBean private CommunityHistoricalPlayerLinkService historicalPlayers;
    @MockitoBean private CommunityImportService imports;
    @MockitoBean private CommunityStandingsArchiveService archives;
    @MockitoBean private CommunityHistoricalMatchPreviewService previews;
    @MockitoBean private CommunityHistoricalMatchArchiveService historicalMatches;
    @MockitoBean private CommunitySessionService sessions;
    @MockitoBean private com.example.desporto24.service.SessionGuestRequestService guests;
    @MockitoBean private com.example.desporto24.service.interfaces.SessionService publicSessionService;
    @MockitoBean private com.example.desporto24.service.CommunitySessionResultService sessionResults;

    @BeforeEach
    void configureAccounts() {
        // Apply the real Spring Security chain exactly once. Spring Boot's
        // MockMvc filter auto-registration otherwise executes the JWT filter
        // outside and again inside the security chain, losing authentication.
        mvc = MockMvcBuilders.webAppContextSetup(webContext)
                .addFilters(springSecurityFilterChain)
                .build();
        stubToken("fixture-owner-token", "fixture-owner", 7L, true);
        stubToken("fixture-member-token", "fixture-member", 3L, true);
        stubToken("fixture-stale-token", "fixture-stale", 2L, false);
    }

    private void stubToken(String token, String username, long version, boolean current) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setTokenVersion(current ? version : version + 1);

        when(tokens.getSubject(token)).thenReturn(username);
        when(tokens.getTokenVersion(token)).thenReturn(version);
        when(profiles.findUserByUsername(username)).thenReturn(Optional.of(profile));
        when(tokens.isTokenValid(username, token)).thenReturn(true);
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(tokens.getAuthorities(token)).thenReturn(List.copyOf(authorities));
        when(tokens.getAuthentication(eq(username), anyList(), any(HttpServletRequest.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, null, authorities));
    }

    @Test
    void anonymousCommunityRequestsAreBlockedByRealSpringSecurity() throws Exception {
        mvc.perform(get("/api/communities/mine"))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/communities/12/join-requests/99/accept"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(communities);
    }

    @Test
    void signedGuestConfirmedRosterIsPostOnlyAndDoesNotRequireAccount() throws Exception {
        when(guests.confirmedPlayers(42L, "signed-invite")).thenReturn(List.of(
                new com.example.desporto24.service.SessionGuestRequestService.ConfirmedPlayer(
                        "guest:8","Fresco",false)));
        mvc.perform(post("/api/sessions/42/guest-confirmed-players")
                .contentType("application/json")
                .content("{\"token\":\"signed-invite\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fresco"));
        mvc.perform(get("/api/sessions/42/guest-confirmed-players"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/sessions/42/guest-link"))
                .andExpect(status().isForbidden());
    }

    @Test
    void ownGuestCorrectionsAreAnonymousPostOnlyWhileNameOwnershipIsReceiptChecked() throws Exception {
        String receipt="r".repeat(43);
        when(guests.changeGuestIdentity(eq(42L),any())).thenReturn(
                new com.example.desporto24.service.SessionGuestRequestService.GuestReceipt(
                        receipt,"APPROVED","Fresco"));
        when(guests.cancelByReceipt(eq(42L),any())).thenReturn(
                new com.example.desporto24.service.SessionGuestRequestService.GuestReceipt(
                        receipt,"CANCELLED","Fresco"));
        mvc.perform(post("/api/sessions/42/guest-requests/change-player")
                .contentType("application/json")
                .content("{\"receipt\":\""+receipt+"\",\"playerId\":null,\"newName\":\"Fresco\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.playerName").value("Fresco"));
        mvc.perform(post("/api/sessions/42/guest-requests/cancel-self")
                .contentType("application/json")
                .content("{\"receipt\":\""+receipt+"\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED"));
        mvc.perform(get("/api/sessions/42/guest-requests/change-player"))
                .andExpect(status().isForbidden());
    }

    @Test
    void malformedBearerTokenCannotReachTheController() throws Exception {
        when(tokens.getSubject("malformed-jwt")).thenThrow(new IllegalArgumentException("malformed"));

        mvc.perform(get("/api/communities/mine")
                        .header("Authorization", "Bearer malformed-jwt"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(communities);
    }

    @Test
    void revokedTokenVersionCannotReachTheController() throws Exception {
        mvc.perform(get("/api/communities/mine")
                        .header("Authorization", "Bearer fixture-stale-token"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(communities);
    }

    @Test
    void validJwtIdentityIsPassedThroughTheFullFilterChain() throws Exception {
        when(communities.mine("fixture-owner")).thenReturn(List.of());

        mvc.perform(get("/api/communities/mine")
                        .header("Authorization", "Bearer fixture-owner-token"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(communities).mine("fixture-owner");
    }

    @Test
    void validMemberTokenStillCannotReadManagersJoinRequestQueue() throws Exception {
        when(communities.pendingJoinRequests(12L, "fixture-member"))
                .thenThrow(new SecurityException("internal authorization reason"));

        mvc.perform(get("/api/communities/12/join-requests")
                        .header("Authorization", "Bearer fixture-member-token"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));

        verify(communities).pendingJoinRequests(12L, "fixture-member");
    }

    @Test
    void managerCanReadTheQueueThroughTheRealSecurityFilterChain() throws Exception {
        when(communities.pendingJoinRequests(12L, "fixture-owner")).thenReturn(List.of());

        mvc.perform(get("/api/communities/12/join-requests")
                        .header("Authorization", "Bearer fixture-owner-token"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(communities).pendingJoinRequests(12L, "fixture-owner");
    }

    @Test
    void regularUserTokenDoesNotGrantGlobalAdminAccess() throws Exception {
        mvc.perform(get("/api/admin/pilot/activation")
                        .header("Authorization", "Bearer fixture-owner-token"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(communities);
    }
}
