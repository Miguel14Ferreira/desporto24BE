package com.example.desporto24.service;

import com.example.desporto24.controllers.AuthenticationController;
import com.example.desporto24.controllers.NotificationController;
import com.example.desporto24.exception.AccountDisabledException;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.Status;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.ConfirmationTokenRepository;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.security.RefreshOriginValidator;
import com.example.desporto24.security.RefreshTokenRotationService;
import com.example.desporto24.security.SensitiveActionRateLimiter;
import com.example.desporto24.security.TotpService;
import com.example.desporto24.service.impl.AuthenticationServiceImpl;
import com.example.desporto24.service.impl.NotificationServiceImpl;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.utility.JWTokenProvider;
import com.example.desporto24.filter.JwtAuthorizationFilter;
import com.example.desporto24.service.GoogleAuthenticationService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Real HTTP controllers + real account/recovery services + persisted H2 rows
 * + actual signed JWT verification. Only outbound email and unrelated services
 * are mocks. Never connects to production, Resend or real user accounts.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:account-lock-recovery-contract;MODE=LEGACY;NON_KEYWORDS=SESSION;INIT=CREATE SCHEMA IF NOT EXISTS desporto24;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.hikari.schema=PUBLIC",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
class AccountLockRecoveryHttpJpaTest {
    private static final String USER = "qa-account-recovery-fixture";
    private static final String EMAIL = "recovery-fixture@example.test";
    private static final String ORIGINAL_PASSWORD = "old-fixture-password-2026";
    private static final String NEW_PASSWORD = "new-fixture-password-2026";
    private static final String UI = "https://example.test";
    private static final String JWT_SECRET =
            "isolated-account-lock-recovery-test-secret-key-long-enough-2026";

    @Autowired private ProfileRepository profiles;
    @Autowired private ConfirmationTokenRepository tokens;
    @Autowired private NotificationsRepository notifications;
    @Autowired private EntityManager entityManager;

    private final EmailService outboundEmail = mock(EmailService.class);
    private MockMvc http;
    private AuthenticationServiceImpl authentication;
    private JWTokenProvider jwt;
    private PasswordHashService passwordHashes;

    @BeforeEach
    void setup() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        passwordHashes = new PasswordHashService(profiles, encoder);
        jwt = new JWTokenProvider();
        ReflectionTestUtils.setField(jwt, "secret", JWT_SECRET);
        ReflectionTestUtils.setField(jwt, "expirationTimeMs", 1_800_000L);
        ReflectionTestUtils.setField(jwt, "refreshExpirationTimeMs", 604_800_000L);

        ConfirmationTokenService confirmationTokens = new ConfirmationTokenService(tokens);
        NotificationServiceImpl notificationService = new NotificationServiceImpl(
                notifications, outboundEmail, mock(ProfileService.class),
                confirmationTokens, mock(FriendRequestService.class),
                mock(FriendRepository.class), profiles, mock(UserBlockService.class),
                mock(PostGameReminderService.class));
        ReflectionTestUtils.setField(notificationService, "uiAppUrl", UI);

        authentication = new AuthenticationServiceImpl(
                mock(LoginAttemptService.class), mock(ProfileService.class),
                profiles, confirmationTokens, outboundEmail, notifications,
                mock(MFAVerificationService.class), passwordHashes, encoder, jwt,
                mock(ProfileMapper.class), mock(TotpService.class),
                mock(PilotEventService.class));
        ReflectionTestUtils.setField(authentication, "uiAppUrl", UI);

        AuthenticationController authController = new AuthenticationController(
                authentication, mock(SensitiveActionRateLimiter.class),
                mock(RefreshOriginValidator.class), mock(RefreshTokenRotationService.class),
                jwt, mock(GoogleAuthenticationService.class));
        http = MockMvcBuilders.standaloneSetup(
                new NotificationController(notificationService, mock(NotificationPreferencesService.class),
                        mock(ConversationNotificationMuteService.class), mock(InboxSummaryService.class),
                        mock(PrivateGroupService.class)),
                authController).build();
    }

    @AfterEach
    void cleanupSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void blockEmailRecoveryAndNewPasswordPreserveRevocationAcrossStoredRows()
            throws Exception {
        Profile active = fixtureAccount();
        String oldAccessJwt = jwt.generateJwtToken(new ProfilePrincipal(active));
        String oldRefreshJwt = jwt.generateRefreshToken(new ProfilePrincipal(active));
        assertTrue(authenticatesWithBearer(oldAccessJwt), "Original signed JWT must work");
        assertNotNull(authentication.refresh(oldRefreshJwt).token());

        // Actual authenticated controller -> service -> H2 transaction.
        http.perform(post("/api/notifications/me/block")
                        .principal(new UsernamePasswordAuthenticationToken(USER, null)))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();
        Profile blocked = profiles.findUserByUsername(USER).orElseThrow();
        assertEquals(Boolean.FALSE, blocked.getNotLocked());
        assertEquals(Status.OFFLINE, blocked.getStatusId());
        assertEquals(8L, blocked.getTokenVersion());
        assertFalse(authenticatesWithBearer(oldAccessJwt),
                "Old access token must stop authenticating immediately");
        assertThrows(AccountDisabledException.class,
                () -> authentication.refresh(oldRefreshJwt),
                "No refresh session may be renewed while locked");

        // Email must have been composed, not actually delivered.
        var emailBody = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(outboundEmail, times(1)).send(eq(EMAIL), emailBody.capture());
        var saved = tokens.findAll();
        assertEquals(1, saved.size());
        ConfirmationToken recovery = saved.get(0);
        assertEquals(USER, recovery.getProfile());
        assertNull(recovery.getConfirmedAt());
        assertTrue(recovery.getExpiredAt().isAfter(LocalDateTime.now().plusMinutes(14)));
        assertTrue(recovery.getExpiredAt().isBefore(LocalDateTime.now().plusMinutes(16)));
        String recoveryLink = UI + "/login/resetPassword/" + recovery.getToken();
        assertTrue(emailBody.getValue().contains(recoveryLink));
        assertTrue(emailBody.getValue().contains("15 minutos"));
        assertTrue(emailBody.getValue().contains("Recuperar e desbloquear conta"));

        // Public reset controller accepts an independently authorised,
        // single-use recovery token without any logged-in browser session.
        http.perform(put("/api/auth/reset-password/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"" + recovery.getToken()
                                + "\",\"newPassword\":\"" + NEW_PASSWORD + "\"}"))
                .andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();
        Profile recovered = profiles.findUserByUsername(USER).orElseThrow();
        assertEquals(Boolean.TRUE, recovered.getNotLocked());
        assertEquals(0, recovered.getLoginAttempts());
        assertEquals(9L, recovered.getTokenVersion());
        assertTrue(passwordHashes.matchesAndUpgrade(recovered, NEW_PASSWORD));
        assertFalse(passwordHashes.matchesAndUpgrade(recovered, ORIGINAL_PASSWORD));
        assertNotNull(tokens.findByToken(recovery.getToken()).orElseThrow().getConfirmedAt(),
                "Recovery link must become unusable after successful redemption");

        assertFalse(authenticatesWithBearer(oldAccessJwt),
                "Unblocking must NOT resurrect previous access sessions");
        assertThrows(BadCredentialsException.class,
                () -> authentication.refresh(oldRefreshJwt),
                "Unblocking must NOT resurrect previous refresh sessions");
        assertTrue(authenticatesWithBearer(
                jwt.generateJwtToken(new ProfilePrincipal(recovered))),
                "Only newly issued access tokens should authenticate");
        String newRefresh = jwt.generateRefreshToken(new ProfilePrincipal(recovered));
        assertNotNull(authentication.refresh(newRefresh).token());
        assertThrows(IllegalStateException.class,
                () -> authentication.resetPasswordStepTwo("replay-password", recovery.getToken()),
                "A used recovery token must not reset another password");
    }

    @Test
    void expiredRecoveryTokenNeverUnlocksTheAccount() {
        Profile active = fixtureAccount();
        active.setNotLocked(false);
        active.setTokenVersion(8L);
        profiles.saveAndFlush(active);
        ConfirmationToken expired = tokens.saveAndFlush(new ConfirmationToken(
                "expired-fixture-token", LocalDateTime.now().minusMinutes(20),
                LocalDateTime.now().minusMinutes(5), USER));

        assertThrows(IllegalStateException.class,
                () -> authentication.resetPasswordStepTwo(NEW_PASSWORD, expired.getToken()));
        entityManager.flush();
        entityManager.clear();
        Profile stillLocked = profiles.findUserByUsername(USER).orElseThrow();
        assertFalse(stillLocked.getNotLocked());
        assertEquals(8L, stillLocked.getTokenVersion());
        assertTrue(passwordHashes.matchesAndUpgrade(stillLocked, ORIGINAL_PASSWORD));
        verifyNoInteractions(outboundEmail);
    }

    private Profile fixtureAccount() {
        Profile profile = new Profile();
        profile.setUserId("qa-recovery-id");
        profile.setUsername(USER);
        profile.setEmail(EMAIL);
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setRole("ROLE_USER");
        profile.setAuthorities(new String[]{"ROLE_USER"});
        profile.setStatusId(Status.OFFLINE);
        profile.setTokenVersion(7L);
        profile.setPassword(passwordHashes.encode(ORIGINAL_PASSWORD));
        return profiles.saveAndFlush(profile);
    }

    /** The actual production JWT authorization filter checks the persisted
     *  account state and token version on every authenticated request. */
    private boolean authenticatesWithBearer(String signedToken) throws Exception {
        SecurityContextHolder.clearContext();
        try {
            var request = new MockHttpServletRequest("GET", "/api/notifications/me");
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + signedToken);
            AtomicBoolean accepted = new AtomicBoolean(false);
            new JwtAuthorizationFilter(jwt, profiles).doFilter(
                    request, new MockHttpServletResponse(),
                    (req, response) -> accepted.set(
                            SecurityContextHolder.getContext().getAuthentication() != null));
            return accepted.get();
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
