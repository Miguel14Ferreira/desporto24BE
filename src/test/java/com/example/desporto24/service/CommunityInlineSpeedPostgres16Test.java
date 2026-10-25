package com.example.desporto24.service;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Contract of the real 2026 Liquibase speed-skating tables and constraints in a
 * disposable PostgreSQL 16 instance. The only allowed host is loopback.
 *
 * The parent community_round / community_tournament tables are deliberately
 * minimal local fixtures; this does not replace a full application+HTTP test.
 * No Render/production DB, real accounts or existing records are accessed.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommunityInlineSpeedPostgres16Test {
    private JdbcTemplate jdbc;
    private static final String MIGRATION =
            "db/changelog/changes/2026-10-25-inline-speed-progression-v1.yaml";

    @BeforeAll
    void setUpDisposableDatabase() throws Exception {
        String url = System.getenv("JYNLO_TEST_PG_JDBC_URL");
        assumeTrue(url != null &&
                url.matches("^jdbc:postgresql://(127\\.0\\.0\\.1|localhost):5432/inline_speed_qa(?:\\?.*)?$"),
                "The PG16 fixture is opt-in and restricted to a loopback-only database");
        var source = new DriverManagerDataSource();
        source.setDriverClassName("org.postgresql.Driver");
        source.setUrl(url);
        source.setUsername(System.getenv("JYNLO_TEST_PG_USERNAME"));
        source.setPassword(System.getenv("JYNLO_TEST_PG_PASSWORD"));
        jdbc = new JdbcTemplate(source);

        // Minimal pre-existing contract ONLY, never an application migration.
        jdbc.execute("create table if not exists community_tournament (" +
                "id bigint primary key, name varchar(120) not null, " +
                "sport varchar(100) not null, rankings_enabled boolean not null)");
        jdbc.execute("create table if not exists community_round (" +
                "id bigint primary key, name varchar(120) not null, " +
                "event_type varchar(64), elapsed_seconds numeric(11,3))");

        try (var connection = source.getConnection()) {
            var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection));
            try (var migration = new Liquibase(MIGRATION, new ClassLoaderResourceAccessor(), database)) {
                migration.update(new Contexts(), new LabelExpression());
                // A repeat deploy must not run the same DDL twice.
                migration.update(new Contexts(), new LabelExpression());
            }
        }
    }

    @BeforeEach
    void newFictionalCompetition() {
        jdbc.execute("truncate table community_inline_speed_decision, " +
                "community_inline_speed_heat, community_round, community_tournament " +
                "restart identity cascade");
        jdbc.update("insert into community_tournament(id,name,sport,rankings_enabled) " +
                "values (9, 'QA Campeonato 2026', 'Patinagem de Velocidade', false)," +
                "(10, 'QA Outra comunidade', 'Patinagem de Velocidade', false)");
        jdbc.update("insert into community_round(id,name,event_type,elapsed_seconds) values " +
                "(100,'Ana Exemplo - 500m +D','INLINE_TRACK_500D_FIRST_ROUND',30.010)," +
                "(101,'Bruno Exemplo - 500m +D','INLINE_TRACK_500D_FIRST_ROUND',30.020)," +
                "(201,'Carla Exemplo - meia-final','INLINE_TRACK_500D_SEMI_HEAT',31.000)");
    }

    private int count(String table) {
        return jdbc.queryForObject("select count(*) from " + table, Integer.class);
    }

    @Test
    void migrationAppliesOnceAndFictionalCommunityKeepsStandingsDisabled() {
        assertEquals(1, jdbc.queryForObject(
                "select count(*) from databasechangelog " +
                "where id='2026-10-25-inline-speed-progression-v1'", Integer.class));
        assertFalse(Boolean.TRUE.equals(jdbc.queryForObject(
                "select rankings_enabled from community_tournament where id=9", Boolean.class)));
        assertEquals(0, count("community_inline_speed_heat"));
        assertEquals(0, count("community_inline_speed_decision"));
    }

    @Test
    void oneRoundCanOnlyBeLinkedOnceAndStageNumbersAreUniquePerCompetition() {
        jdbc.update("insert into community_inline_speed_heat " +
                "(tournament_id,round_id,stage,heat_number) values (9,100,'FIRST',1)");
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("insert into community_inline_speed_heat " +
                    "(tournament_id,round_id,stage,heat_number) values (10,100,'FIRST',1)"));
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("insert into community_inline_speed_heat " +
                    "(tournament_id,round_id,stage,heat_number) values (9,101,'FIRST',1)"));
        jdbc.update("insert into community_inline_speed_heat " +
                "(tournament_id,round_id,stage,heat_number) values (9,101,'FIRST',2)");
        jdbc.update("insert into community_inline_speed_heat " +
                "(tournament_id,round_id,stage,heat_number) values (9,201,'SEMI',1)");
        assertEquals(3, count("community_inline_speed_heat"));
        assertEquals(2, count("community_round"));
    }

    @Test
    void aJudgeDecisionIsUniquePerPhaseAndDoesNotOverwriteTimes() {
        jdbc.update("insert into community_inline_speed_heat " +
                "(tournament_id,round_id,stage,heat_number) values (9,100,'FIRST',1)");
        jdbc.update("insert into community_inline_speed_decision " +
                "(tournament_id,source_stage,target_stage,qualified_player_ids,source_fingerprint," +
                "judge_reason,confirmed_by,confirmed_at) " +
                "values (9,'FIRST','SEMI','1,2,3,4,5,6,7,8',repeat('a',64)," +
                "'Juízes confirmam a primeira fase','qa_owner',now())");
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("insert into community_inline_speed_decision " +
                    "(tournament_id,source_stage,target_stage,qualified_player_ids," +
                    "source_fingerprint,confirmed_by,confirmed_at) " +
                    "values (9,'FIRST','SEMI','1,2',repeat('b',64),'qa_owner',now())"));
        assertEquals("1,2,3,4,5,6,7,8", jdbc.queryForObject(
                "select qualified_player_ids from community_inline_speed_decision where tournament_id=9",
                String.class));
        assertEquals(0, new java.math.BigDecimal("30.010").compareTo(
                jdbc.queryForObject("select elapsed_seconds from community_round where id=100",
                        java.math.BigDecimal.class)));
        assertFalse(Boolean.TRUE.equals(jdbc.queryForObject(
                "select rankings_enabled from community_tournament where id=9", Boolean.class)));
    }

    @Test
    void foreignKeysPreserveOriginalRoundsAndCascadeOnlyWithDeletedTournament() {
        jdbc.update("insert into community_inline_speed_heat " +
                "(tournament_id,round_id,stage,heat_number) values (9,100,'FIRST',1)");
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("delete from community_round where id=100"));
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("insert into community_inline_speed_heat " +
                    "(tournament_id,round_id,stage,heat_number) values (999,101,'FIRST',3)"));
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("insert into community_inline_speed_heat " +
                    "(tournament_id,round_id,stage,heat_number) values (9,999,'FIRST',3)"));
        jdbc.update("delete from community_tournament where id=9");
        assertEquals(0, count("community_inline_speed_heat"));
        assertEquals(3, count("community_round"));
        assertEquals(1, count("community_tournament"));
    }
}
