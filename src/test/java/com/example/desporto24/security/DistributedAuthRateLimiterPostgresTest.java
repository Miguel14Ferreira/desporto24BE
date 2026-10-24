package com.example.desporto24.security;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Runs against disposable PostgreSQL 16 in the dedicated GitHub Actions job.
 * Never connects to Render production. Normal Maven unit tests skip this
 * suite unless JYNLO_TEST_PG_JDBC_URL is explicitly provided.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DistributedAuthRateLimiterPostgresTest {

    private JdbcTemplate jdbc;
    private DistributedAuthRateLimiter serverA;
    private DistributedAuthRateLimiter serverB;

    @BeforeAll
    void setup() throws Exception {
        String url = System.getenv("JYNLO_TEST_PG_JDBC_URL");
        assumeTrue(url != null && url.startsWith("jdbc:postgresql://"),
                "PostgreSQL integration is opt-in");
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName("org.postgresql.Driver");
        source.setUrl(url);
        source.setUsername(System.getenv("JYNLO_TEST_PG_USERNAME"));
        source.setPassword(System.getenv("JYNLO_TEST_PG_PASSWORD"));

        try (var connection = source.getConnection()) {
            var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try (var liquibase = new Liquibase(
                    "db/changelog/changes/2026-10-24-auth-rate-limit-v1.yaml",
                    new ClassLoaderResourceAccessor(), database)) {
                liquibase.update(new Contexts(), new LabelExpression());
                liquibase.update(new Contexts(), new LabelExpression());
            }
        }
        jdbc = new JdbcTemplate(source);
        serverA = new DistributedAuthRateLimiter(jdbc, "CI-only-signing-key");
        serverB = new DistributedAuthRateLimiter(jdbc, "CI-only-signing-key");
    }

    @BeforeEach
    void reset() {
        jdbc.update("delete from auth_rate_limit_bucket");
    }

    @Test
    void separateServersShareFiveAttemptLimitAndNeverPersistRawUsernames() {
        for (int i = 0; i < 5; i++) {
            (i % 2 == 0 ? serverA : serverB)
                    .reserveAttempt("mfa-confirm", "  TARGET-user  ", 5, Duration.ofMinutes(15));
        }
        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> serverB.reserveAttempt("mfa-confirm", "target-USER", 5, Duration.ofMinutes(15)));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, blocked.getStatusCode());
        assertThrows(ResponseStatusException.class,
                () -> serverA.requireAllowed("mfa-confirm", "target-user", 5));

        String key = jdbc.queryForObject("select bucket_key from auth_rate_limit_bucket",
                String.class);
        assertNotNull(key);
        assertEquals(64, key.length());
        assertFalse(key.contains("target"));
        serverB.clearAfterSuccess("mfa-confirm", "target-user");
        assertDoesNotThrow(() -> serverA.requireAllowed("mfa-confirm", "target-user", 5));
    }

    @Test
    void simultaneousAttemptsCannotExceedSharedBudget() throws Exception {
        final int concurrency = 12;
        CountDownLatch start = new CountDownLatch(1);
        try (var pool = Executors.newFixedThreadPool(concurrency)) {
            List<java.util.concurrent.Future<Boolean>> results = new ArrayList<>();
            for (int i = 0; i < concurrency; i++) {
                final DistributedAuthRateLimiter limiter = i % 2 == 0 ? serverA : serverB;
                results.add(pool.submit((Callable<Boolean>) () -> {
                    if (!start.await(10, TimeUnit.SECONDS)) throw new AssertionError("start timeout");
                    try {
                        limiter.reserveAttempt("login", "same-account", 5, Duration.ofMinutes(15));
                        return true;
                    } catch (ResponseStatusException rejected) {
                        assertEquals(HttpStatus.TOO_MANY_REQUESTS, rejected.getStatusCode());
                        return false;
                    }
                }));
            }
            start.countDown();
            int allowed = 0;
            for (var future : results) allowed += future.get(20, TimeUnit.SECONDS) ? 1 : 0;
            assertEquals(5, allowed);
            assertThrows(ResponseStatusException.class,
                    () -> serverA.requireAllowed("login", "same-account", 5));
        }
    }

    @Test
    void fixedWindowExpiresAndCooldownsAreIndependentByAction() throws Exception {
        serverA.reserveAttempt("cooldown:reset", "account", 1, Duration.ofSeconds(1));
        assertThrows(ResponseStatusException.class,
                () -> serverB.reserveAttempt("cooldown:reset", "account", 1,
                        Duration.ofSeconds(1)));
        assertDoesNotThrow(() -> serverB.reserveAttempt(
                "cooldown:registration", "account", 1, Duration.ofSeconds(1)));
        Thread.sleep(1300);
        assertEquals(1, serverB.reserveAttempt(
                "cooldown:reset", "account", 1, Duration.ofSeconds(1)));
    }
}
