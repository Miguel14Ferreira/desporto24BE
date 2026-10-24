package com.example.desporto24.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.desporto24.constant.SecurityConstant;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.RefreshTokenSessionRepository;
import com.example.desporto24.utility.JWTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/** Real H2 JPA transactions. No real accounts or external DB calls. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({RefreshTokenRotationService.class, JWTokenProvider.class})
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:refresh-rotation-qa;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=10000",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.hikari.schema=PUBLIC",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "desporto.app.jwtSecret=qa-only-hmac-secret-not-for-production-1234567890abcdef",
        "desporto.app.refreshExpirationMs=604800000"
})
class RefreshTokenRotationH2IntegrationTest {
    private static final String KEY = "qa-only-hmac-secret-not-for-production-1234567890abcdef";

    @Autowired private ProfileRepository profiles;
    @Autowired private RefreshTokenSessionRepository sessions;
    @Autowired private JWTokenProvider tokens;
    @Autowired private RefreshTokenRotationService rotation;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void registeredRefreshTokenCanBeConsumedOnlyOnce() {
        Profile account = account("once-" + UUID.randomUUID());
        try {
        String first = tokens.generateRefreshToken(new ProfilePrincipal(account));
        String replacement = tokens.generateRefreshToken(new ProfilePrincipal(account));
        rotation.registerIssued(account.getUsername(), first);
        rotation.rotate(first, replacement, account.getUsername());

        assertThrows(BadCredentialsException.class,
                () -> rotation.rotate(first,
                        tokens.generateRefreshToken(new ProfilePrincipal(account)),
                        account.getUsername()));
        assertEquals(2, sessions.count());
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void legacyCookieWithoutJwtIdIsAcceptedOnlyOnce() {
        Profile account = account("legacy-" + UUID.randomUUID());
        try {
        String legacy = JWT.create()
                .withIssuer(SecurityConstant.DESPORTO_24)
                .withAudience(SecurityConstant.DESPORTO_24_ADMINISTRATION)
                .withSubject(account.getUsername())
                .withClaim(SecurityConstant.TOKEN_VERSION, account.getTokenVersion())
                .withClaim(SecurityConstant.TOKEN_TYPE, SecurityConstant.REFRESH_TOKEN_TYPE)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1800)))
                .sign(Algorithm.HMAC512(KEY));
        assertNull(tokens.getRefreshTokenId(legacy));
        String replacement = tokens.generateRefreshToken(new ProfilePrincipal(account));

        rotation.rotate(legacy, replacement, account.getUsername());
        assertThrows(BadCredentialsException.class,
                () -> rotation.rotate(legacy, tokens.generateRefreshToken(new ProfilePrincipal(account)),
                        account.getUsername()));
        assertEquals(2, sessions.count());
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void unregisteredModernCookieIsRejectedEvenWithValidSignature() {
        Profile account = account("unknown-" + UUID.randomUUID());
        try {
        String forgedAsIssued = tokens.generateRefreshToken(new ProfilePrincipal(account));
        assertThrows(BadCredentialsException.class,
                () -> rotation.rotate(forgedAsIssued,
                        tokens.generateRefreshToken(new ProfilePrincipal(account)),
                        account.getUsername()));
        assertEquals(0, sessions.count());
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void mismatchingAccountVersionRevokesAnAlreadyIssuedRefreshCookie() {
        Profile account = account("revoked-" + UUID.randomUUID());
        try {
        String cookie = tokens.generateRefreshToken(new ProfilePrincipal(account));
        rotation.registerIssued(account.getUsername(), cookie);
        account.setTokenVersion(account.getTokenVersion() + 1);
        profiles.saveAndFlush(account);

        assertThrows(BadCredentialsException.class,
                () -> rotation.rotate(cookie, tokens.generateRefreshToken(new ProfilePrincipal(account)),
                        account.getUsername()));
        assertEquals(1, sessions.count());
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void delayedReplayRevokesOnlyThatDeviceAndPersistsDespiteAuthenticationFailure() {
        Profile account = account("replay-device-" + UUID.randomUUID());
        try {
            String original = tokens.generateRefreshToken(new ProfilePrincipal(account));
            String otherDevice = tokens.generateRefreshToken(new ProfilePrincipal(account));
            rotation.registerIssued(account.getUsername(), original);
            rotation.registerIssued(account.getUsername(), otherDevice);

            String activeChild = tokens.generateRefreshToken(new ProfilePrincipal(account));
            rotation.rotate(original, activeChild, account.getUsername());

            var consumedParent = sessions.findAll().stream()
                    .filter(t -> t.getConsumedAt() != null)
                    .findFirst().orElseThrow();
            String compromisedFamily = consumedParent.getFamilyId();
            var sibling = sessions.findAll().stream()
                    .filter(t -> t.getTokenHash().equals(hash(otherDevice)))
                    .findFirst().orElseThrow();
            assertNotEquals(compromisedFamily, sibling.getFamilyId());

            // Simulate a replay observed after the benign retry grace window.
            consumedParent.setConsumedAt(Instant.now().minusSeconds(60));
            sessions.saveAndFlush(consumedParent);

            assertThrows(BadCredentialsException.class,
                    () -> rotation.rotate(original,
                            tokens.generateRefreshToken(new ProfilePrincipal(account)),
                            account.getUsername()));

            // The outer HTTP caller receives 401, but the revoke transaction
            // MUST have committed independently; the active successor is denied.
            var revoked = sessions.findAll().stream()
                    .filter(t -> compromisedFamily.equals(t.getFamilyId()))
                    .toList();
            assertEquals(2, revoked.size());
            assertTrue(revoked.stream().allMatch(t -> t.getRevokedAt() != null));
            assertNull(sessions.findById(hash(otherDevice)).orElseThrow().getRevokedAt());

            assertThrows(BadCredentialsException.class,
                    () -> rotation.rotate(activeChild,
                            tokens.generateRefreshToken(new ProfilePrincipal(account)),
                            account.getUsername()));
            assertDoesNotThrow(() -> rotation.rotate(otherDevice,
                    tokens.generateRefreshToken(new ProfilePrincipal(account)),
                    account.getUsername()));
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void immediateDuplicateRequestIsDeniedWithoutRevokingTheValidSuccessor() {
        Profile account = account("duplicate-tab-" + UUID.randomUUID());
        try {
            String initial = tokens.generateRefreshToken(new ProfilePrincipal(account));
            String successor = tokens.generateRefreshToken(new ProfilePrincipal(account));
            rotation.registerIssued(account.getUsername(), initial);
            rotation.rotate(initial, successor, account.getUsername());

            assertThrows(BadCredentialsException.class,
                    () -> rotation.rotate(initial,
                            tokens.generateRefreshToken(new ProfilePrincipal(account)),
                            account.getUsername()));
            assertTrue(sessions.findAll().stream().allMatch(t -> t.getRevokedAt() == null));
            assertDoesNotThrow(() -> rotation.rotate(successor,
                    tokens.generateRefreshToken(new ProfilePrincipal(account)),
                    account.getUsername()));
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void preexistingModernSessionWithoutFamilyAcquiresOneOnRefresh() {
        Profile account = account("upgrade-family-" + UUID.randomUUID());
        try {
            String initial = tokens.generateRefreshToken(new ProfilePrincipal(account));
            String rotated = tokens.generateRefreshToken(new ProfilePrincipal(account));
            rotation.registerIssued(account.getUsername(), initial);

            var stored = sessions.findById(hash(initial)).orElseThrow();
            stored.setFamilyId(null); // Mimics persisted sessions before migration.
            sessions.saveAndFlush(stored);

            rotation.rotate(initial, rotated, account.getUsername());
            var predecessor = sessions.findById(hash(initial)).orElseThrow();
            var successor = sessions.findById(hash(rotated)).orElseThrow();
            assertNotNull(predecessor.getFamilyId());
            assertEquals(predecessor.getFamilyId(), successor.getFamilyId());
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    private String hash(String jwt) {
        try {
            byte[] digest = java.security.MessageDigest.getInstance("SHA-256")
                    .digest(jwt.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (java.security.NoSuchAlgorithmException impossible) {
            throw new AssertionError(impossible);
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void concurrentRefreshOfSameCookieHasExactlyOneWinner() throws Exception {
        Profile account = account("race-" + UUID.randomUUID());
        try {
            String old = tokens.generateRefreshToken(new ProfilePrincipal(account));
            rotation.registerIssued(account.getUsername(), old);
            String replacement1 = tokens.generateRefreshToken(new ProfilePrincipal(account));
            String replacement2 = tokens.generateRefreshToken(new ProfilePrincipal(account));
            CountDownLatch ready = new CountDownLatch(2);
            CountDownLatch begin = new CountDownLatch(1);
            try (var pool = Executors.newFixedThreadPool(2)) {
                var first = pool.submit(() -> tryRotate(old, replacement1, account.getUsername(), ready, begin));
                var second = pool.submit(() -> tryRotate(old, replacement2, account.getUsername(), ready, begin));
                assertTrue(ready.await(5, TimeUnit.SECONDS));
                begin.countDown();
                int successes = first.get(12, TimeUnit.SECONDS) + second.get(12, TimeUnit.SECONDS);
                assertEquals(1, successes, "Only one concurrent transaction may consume the token");
                assertEquals(2, sessions.count());
            }
        } finally {
            sessions.deleteAll();
            profiles.deleteById(account.getId());
        }
    }

    private int tryRotate(String oldToken, String newToken, String username,
                          CountDownLatch ready, CountDownLatch begin) throws Exception {
        ready.countDown();
        if (!begin.await(5, TimeUnit.SECONDS)) throw new AssertionError("start gate timed out");
        try {
            rotation.rotate(oldToken, newToken, username);
            return 1;
        } catch (BadCredentialsException expected) {
            return 0;
        }
    }

    private Profile account(String username) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setUserId(UUID.randomUUID().toString());
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setTokenVersion(7L);
        profile.setRole("ROLE_USER");
        profile.setAuthorities(new String[0]);
        return profiles.saveAndFlush(profile);
    }
}
