package com.example.desporto24.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.desporto24.constant.SecurityConstant;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.security.TotpService;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.PilotEventService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.utility.JWTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Checks AuthenticationServiceImpl.refresh using real HMAC512 token signatures.
 * All collaborators are mocked, no external accounts or database are used.
 */
class JwtRefreshServiceContractTest {
    private static final String USERNAME = "fixture-refresh-user";
    private static final String SECRET =
            "fixture-service-jwt-secret-long-enough-for-local-hmac512-testing-123456";

    private final JWTokenProvider jwt = new JWTokenProvider();
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final ProfileMapper mapper = mock(ProfileMapper.class);
    private Profile profile;
    private AuthenticationServiceImpl service;

    @BeforeEach
    void fixture() {
        ReflectionTestUtils.setField(jwt, "secret", SECRET);
        ReflectionTestUtils.setField(jwt, "expirationTimeMs", 1_800_000L);
        ReflectionTestUtils.setField(jwt, "refreshExpirationTimeMs", 604_800_000L);

        profile = new Profile();
        profile.setUsername(USERNAME);
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setTokenVersion(7L);
        profile.setAuthorities(new String[0]);
        profile.setRole("ROLE_USER");
        when(profiles.findUserByUsername(USERNAME)).thenReturn(Optional.of(profile));

        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(USERNAME);
        when(mapper.toDto(profile)).thenReturn(dto);

        service = new AuthenticationServiceImpl(
                mock(LoginAttemptService.class),
                mock(ProfileService.class),
                profiles,
                mock(ConfirmationTokenService.class),
                mock(EmailService.class),
                mock(NotificationsRepository.class),
                mock(MFAVerificationService.class),
                mock(PasswordHashService.class),
                mock(BCryptPasswordEncoder.class),
                jwt,
                mapper,
                mock(TotpService.class),
                mock(PilotEventService.class));
    }

    @Test
    void validSignedRefreshCreatesARealSignedAccessJwt() {
        String refresh = jwt.generateRefreshToken(new ProfilePrincipal(profile));

        var response = service.refresh(refresh);

        assertFalse(response.mfaRequired());
        assertNotNull(response.token());
        assertEquals(USERNAME, jwt.getSubject(response.token()));
        assertEquals(7L, jwt.getTokenVersion(response.token()));
        assertTrue(jwt.isTokenValid(USERNAME, response.token()));
        assertEquals(SecurityConstant.ACCESS_TOKEN_TYPE,
                JWT.decode(response.token()).getClaim(SecurityConstant.TOKEN_TYPE).asString());
    }

    @Test
    void revokedRefreshTokenVersionIsRejected() {
        String refresh = jwt.generateRefreshToken(new ProfilePrincipal(profile));
        profile.setTokenVersion(8L);

        assertThrows(BadCredentialsException.class, () -> service.refresh(refresh));
    }

    @Test
    void signedAccessJwtCannotBeUsedAsRefreshToken() {
        String access = jwt.generateJwtToken(new ProfilePrincipal(profile));

        assertThrows(BadCredentialsException.class, () -> service.refresh(access));
    }

    @Test
    void cryptographicallySignedButExpiredAccessJwtCannotRefreshTheSession() {
        String expiredAccess = JWT.create()
                .withIssuer(SecurityConstant.DESPORTO_24)
                .withAudience(SecurityConstant.DESPORTO_24_ADMINISTRATION)
                .withSubject(USERNAME)
                .withClaim(SecurityConstant.TOKEN_VERSION, 7L)
                .withClaim(SecurityConstant.TOKEN_TYPE, SecurityConstant.ACCESS_TOKEN_TYPE)
                .withIssuedAt(Date.from(Instant.now().minusSeconds(3600)))
                .withExpiresAt(Date.from(Instant.now().minusSeconds(60)))
                .sign(Algorithm.HMAC512(SECRET));

        assertThrows(JWTVerificationException.class, () -> service.refresh(expiredAccess));
    }
}
