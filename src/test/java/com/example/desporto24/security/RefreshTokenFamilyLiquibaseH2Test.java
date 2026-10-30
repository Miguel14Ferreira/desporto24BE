package com.example.desporto24.security;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenFamilyLiquibaseH2Test {

    @Test
    void familyMigrationUpgradesExistingRowsAndRunsTwice() throws Exception {
        JdbcDataSource db = new JdbcDataSource();
        db.setURL("jdbc:h2:mem:refresh-family-" + UUID.randomUUID()
                + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
        try (var connection = db.getConnection();
             var createProfile = connection.createStatement()) {
            createProfile.execute("create table profile (id bigint primary key)");
            createProfile.execute("insert into profile(id) values (1)");
        }
        apply(db, "db/changelog/changes/2026-10-24-refresh-token-once-v1.yaml");

        try (var connection = db.getConnection();
             var insert = connection.prepareStatement(
                     "insert into refresh_token_session " +
                     "(token_hash, profile_id, expires_at, consumed_at) values (?, 1, ?, null)")) {
            insert.setString(1, "a".repeat(64));
            insert.setTimestamp(2, java.sql.Timestamp.from(
                    java.time.Instant.now().plusSeconds(86400)));
            assertEquals(1, insert.executeUpdate());
        }

        String change = "db/changelog/changes/2026-10-24-refresh-family-replay-v1.yaml";
        apply(db, change);
        apply(db, change);

        try (var connection = db.getConnection();
             var rows = connection.createStatement().executeQuery(
                     "select token_hash, family_id, revoked_at from refresh_token_session")) {
            assertTrue(rows.next());
            assertEquals("a".repeat(64), rows.getString("token_hash"));
            assertNull(rows.getString("family_id"),
                    "Preexisting cookies must be migrated lazily on rotation");
            assertNull(rows.getTimestamp("revoked_at"));
            assertFalse(rows.next());
        }
    }

    private void apply(JdbcDataSource db, String changeLog) throws Exception {
        try (var connection = db.getConnection()) {
            var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try (var liquibase = new Liquibase(changeLog,
                    new ClassLoaderResourceAccessor(), database)) {
                liquibase.update(new Contexts(), new LabelExpression());
            }
        }
    }
}
