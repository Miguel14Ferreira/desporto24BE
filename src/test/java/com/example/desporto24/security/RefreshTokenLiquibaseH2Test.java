package com.example.desporto24.security;

import liquibase.Liquibase;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenLiquibaseH2Test {

    @Test
    void migrationCreatesUniqueTokenHashAndCanRunTwice() throws Exception {
        JdbcDataSource db = new JdbcDataSource();
        db.setURL("jdbc:h2:mem:refresh-liquibase-" + UUID.randomUUID()
                + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
        try (var connection = db.getConnection()) {
            try (var setup = connection.createStatement()) {
                setup.execute("create table profile (id bigint primary key)");
            }
            var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try (var liquibase = new Liquibase(
                    "db/changelog/changes/2026-10-24-refresh-token-once-v1.yaml",
                    new ClassLoaderResourceAccessor(), database)) {
                liquibase.update(new Contexts(), new LabelExpression());
                liquibase.update(new Contexts(), new LabelExpression());
            }

            // Liquibase closes its JDBC connection; use a new connection
            // to validate the migrated schema and PK constraint.
            try (var verificationConnection = db.getConnection();
                 var insert = verificationConnection.prepareStatement(
                    "insert into refresh_token_session(token_hash, profile_id, expires_at) values (?, ?, ?)")) {
                verificationConnection.createStatement().execute("insert into profile(id) values (1)");
                insert.setString(1, "a".repeat(64));
                insert.setLong(2, 1);
                insert.setTimestamp(3, java.sql.Timestamp.from(java.time.Instant.now().plusSeconds(3600)));
                assertEquals(1, insert.executeUpdate());
                assertThrows(java.sql.SQLException.class, insert::executeUpdate,
                        "Duplicate token hash must be rejected by the DB");
            }
        }
    }
}
