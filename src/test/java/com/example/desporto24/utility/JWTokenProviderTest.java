package com.example.desporto24.utility;

import com.auth0.jwt.JWT;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.example.desporto24.constant.SecurityConstant.TOKEN_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }
}
