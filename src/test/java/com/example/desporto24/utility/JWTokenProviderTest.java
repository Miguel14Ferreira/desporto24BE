package com.example.desporto24.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Date;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.example.desporto24.constant.SecurityConstant.TOKEN_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JWTokenProviderTest {

    @Test
    void generatedJwtContainsCurrentTokenVersion() {
        JWTokenProvider provider = new JWTokenProvider();
        ReflectionTestUtils.setField(
                provider,
                "secret",
                "fixture-secret-that-is-deliberately-long-enough-for-hmac512-tests-1234567890"
        );

        ReflectionTestUtils.setField(provider, "expirationTimeMs", 1_800_000L);

        Profile profile = new Profile();
        profile.setUsername("miguel");
        profile.setAuthorities(new String[]{"user:read"});
        profile.setRole("ROLE_USER");
        profile.setTokenVersion(7L);

        String token = provider.generateJwtToken(
                new ProfilePrincipal(profile)
        );

        assertEquals(
                7L,
                JWT.decode(token)
                        .getClaim(TOKEN_VERSION)
                        .asLong()
        );
        assertEquals(7L, provider.getTokenVersion(token));
        assertTrue(provider.isTokenValid("miguel", token));
    }
    @Test
    void rejectsAccessTokenWithoutExplicitTokenType() {
        JWTokenProvider provider = provider();
        String noTypeToken = JWT.create()
                .withIssuer("Desporto 24")
                .withAudience("Desporto 24 APP")
                .withSubject("miguel")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60_000))
                .sign(Algorithm.HMAC512("test-hmac-signing-secret-which-is-not-used-outside-unit-tests"));
        assertFalse(provider.isTokenValid("miguel", noTypeToken));
    }

    @Test
    void rejectsJwtSignedForAnotherAudience() {
        JWTokenProvider provider = provider();
        String foreignAudienceToken = JWT.create()
                .withIssuer("Desporto 24")
                .withAudience("some-other-app")
                .withSubject("miguel")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60_000))
                .sign(Algorithm.HMAC512("test-hmac-signing-secret-which-is-not-used-outside-unit-tests"));
        assertThrows(JWTVerificationException.class, () -> provider.getSubject(foreignAudienceToken));
    }

    @Test
    void consecutiveRefreshTokensHaveDistinctSignedJwtIds() {
        JWTokenProvider provider = provider();
        ReflectionTestUtils.setField(provider, "refreshExpirationTimeMs", 604_800_000L);
        Profile profile = new Profile();
        profile.setUsername("fixture");
        profile.setTokenVersion(12L);
        String first = provider.generateRefreshToken(new ProfilePrincipal(profile));
        String second = provider.generateRefreshToken(new ProfilePrincipal(profile));

        assertNotEquals(first, second);
        assertNotNull(provider.getRefreshTokenId(first));
        assertNotEquals(provider.getRefreshTokenId(first), provider.getRefreshTokenId(second));
        assertTrue(provider.getRefreshTokenExpiry(first).isAfter(java.time.Instant.now()));
    }

    @Test
    void deviceBoundJwtPreservesPermissionsAndExpiryWithSignedFamily() {
        JWTokenProvider provider = provider();
        ReflectionTestUtils.setField(provider, "expirationTimeMs", 1_800_000L);
        Profile profile = new Profile();
        profile.setUsername("fixture");
        profile.setTokenVersion(12L);
        profile.setAuthorities(new String[] {"user:read"});
        String original = provider.generateJwtToken(new ProfilePrincipal(profile));
        String family = "bb2e9f25-6119-4802-8496-5d1206ae9c0d";

        String bound = provider.bindAccessTokenToFamily(original, family);

        assertEquals(family, provider.getAccessTokenFamilyId(bound));
        assertTrue(provider.isTokenValid("fixture", bound));
        assertEquals(JWT.decode(original).getExpiresAt(), JWT.decode(bound).getExpiresAt());
        assertEquals(JWT.decode(original).getIssuedAt(), JWT.decode(bound).getIssuedAt());
        assertEquals(provider.getAuthorities(original), provider.getAuthorities(bound));
        assertEquals(12L, provider.getTokenVersion(bound));
        assertNull(provider.getAccessTokenFamilyId(original),
                "Existing pre-rollout access JWTs remain valid until normal expiry");
        assertThrows(JWTVerificationException.class,
                () -> provider.bindAccessTokenToFamily(
                        provider.generateRefreshToken(new ProfilePrincipal(profile)), family));
    }

    private JWTokenProvider provider() {
        JWTokenProvider provider = new JWTokenProvider();
        ReflectionTestUtils.setField(provider, "secret",
                "test-hmac-signing-secret-which-is-not-used-outside-unit-tests");
        return provider;
    }
}
