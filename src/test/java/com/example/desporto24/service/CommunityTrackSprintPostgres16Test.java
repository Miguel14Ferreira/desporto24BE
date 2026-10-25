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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Real Sprint Liquibase migrations on disposable PostgreSQL 16 with fictional
 * identities. Relational QA only, not authenticated HTTP/Spring E2E.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommunityTrackSprintPostgres16Test {
    private JdbcTemplate jdbc;
    private static final List<String> MIGRATIONS = List.of(
        "db/changelog/changes/2026-10-24-track-sprint-series-v1.yaml",
        "db/changelog/changes/2026-10-25-community-track-sprint-bracket8.yaml",
        "db/changelog/changes/2026-10-25-track-sprint-qualification16.yaml",
        "db/changelog/changes/2026-10-25-sprint-official-200m-tie-decisions.yaml",
        "db/changelog/changes/2026-10-25-track-sprint-incident-decisions.yaml",
        "db/changelog/changes/2026-10-25-track-sprint-disciplinary-rulings.yaml",
        "db/changelog/changes/2026-10-25-sprint-three-up-rerun-approval.yaml");

    @BeforeAll
    void migrateDisposableLocalDatabaseOnly() throws Exception {
        String url = System.getenv("JYNLO_TEST_PG_JDBC_URL");
        assumeTrue(url != null && url.matches(
            "^jdbc:postgresql://(127\\.0\\.0\\.1|localhost):5432/sprint_bracket_qa(?:\\?.*)?$"),
            "Opt-in local PG16 fixture required; no external databases");
        var source = new DriverManagerDataSource();
        source.setDriverClassName("org.postgresql.Driver");
        source.setUrl(url);
        source.setUsername(System.getenv("JYNLO_TEST_PG_USERNAME"));
        source.setPassword(System.getenv("JYNLO_TEST_PG_PASSWORD"));
        jdbc = new JdbcTemplate(source);
        // Only the prerequisite columns, not an application database.
        jdbc.execute("create table if not exists community (id bigint primary key)");
        jdbc.execute("create table if not exists community_season (" +
            "id bigint primary key, community_id bigint not null references community(id))");
        jdbc.execute("create table if not exists community_round (" +
            "id bigint primary key, community_id bigint not null references community(id)," +
            "season_id bigint references community_season(id), name varchar(120)," +
            "event_type varchar(64))");
        for (var changelog : MIGRATIONS) {
            try (var connection = source.getConnection()) {
                var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection));
                try (var migration = new Liquibase(
                    changelog, new ClassLoaderResourceAccessor(), database)) {
                    migration.update(new Contexts(), new LabelExpression());
                    migration.update(new Contexts(), new LabelExpression());
                }
            }
        }
    }

    @BeforeEach
    void independentCommunitiesAndSeasons() {
        jdbc.execute("truncate table community_track_sprint_qualified_rider, " +
            "community_track_sprint_bracket, community_round, community_season, community " +
            "restart identity cascade");
        jdbc.update("insert into community(id) values (7),(8)");
        jdbc.update("insert into community_season(id,community_id) values (4,7),(6,7),(5,8)");
        jdbc.update("insert into community_round(id,community_id,season_id,name,event_type) " +
            "values (42,7,4,'QA 200m 2026','TRACK_FLYING_200')," +
            "(43,8,5,'QA unrelated','TRACK_FLYING_200')," +
            "(99,7,6,'Historical round','TRACK_SPRINT_HEAT')");
    }

    private void bracket(long id, long community, long season, Long qualifier) {
        jdbc.update("insert into community_track_sprint_bracket " +
            "(id,community_id,season_id,name,format,qualifying_round_id,created_at) " +
            "values (?,?,?,'Fictional Olympic Sprint','OLYMPIC_TWENTY_FOUR_REPECHAGE',?,now())",
            id, community, season, qualifier);
    }
    private int count(String table) {
        return jdbc.queryForObject("select count(*) from " + table, Integer.class);
    }

    @Test
    void migrationsAreIdempotentAndHistoricEightRiderDefaultSurvives() {
        assertEquals(7, jdbc.queryForObject("select count(*) from databasechangelog " +
            "where id in ('2026-10-24-track-sprint-series-v1'," +
            "'2026-10-25-community-track-sprint-bracket8'," +
            "'2026-10-25-track-sprint-qualifying16'," +
            "'2026-10-25-sprint-official-200m-tie-decisions'," +
            "'2026-10-25-track-sprint-incident-decisions'," +
            "'2026-10-25-track-sprint-disciplinary-rulings'," +
            "'2026-10-25-sprint-three-up-rerun-approval')", Integer.class));
        jdbc.update("insert into community_track_sprint_bracket " +
            "(id,community_id,season_id,name,created_at) " +
            "values (81,7,6,'Existing eight riders',now())");
        assertEquals("EIGHT_RIDER_FINALS", jdbc.queryForObject(
            "select format from community_track_sprint_bracket where id=81", String.class));
    }

    @Test
    void nullableOfficialDecisionColumnsPreserveHistoricBracketsAndStoreAuditableEvidence() {
        jdbc.update("insert into community_track_sprint_bracket " +
            "(id,community_id,season_id,name,created_at) " +
            "values (81,7,6,'Older historical bracket',now())");
        assertNull(jdbc.queryForObject(
            "select tie_decisions_json from community_track_sprint_bracket where id=81",
            String.class));
        assertNull(jdbc.queryForObject(
            "select tie_decisions_recorded_by from community_track_sprint_bracket where id=81",
            String.class));
        bracket(41,7,4,42L);
        String evidence = """
                [{"method":"DRAW_LOTS","reference":"Fictional official drawing 41"}]
                """;
        jdbc.update("update community_track_sprint_bracket " +
            "set tie_decisions_json=?, tie_decisions_recorded_by=? where id=41",
            evidence, "fixture_sprint_owner");
        assertEquals(evidence, jdbc.queryForObject(
            "select tie_decisions_json from community_track_sprint_bracket where id=41",
            String.class));
        assertEquals("fixture_sprint_owner", jdbc.queryForObject(
            "select tie_decisions_recorded_by from community_track_sprint_bracket where id=41",
            String.class));
        assertEquals(2,count("community_track_sprint_bracket"));
        assertEquals(3,count("community_round"));
    }

    @Test
    void pendingIncidentIsUniquePerSeriesAndBracketDeleteRetainsAuditHistory() {
        bracket(41,7,4,42L);
        jdbc.update("insert into community_track_sprint_incident " +
            "(bracket_id,series_key,heat_number,incident_type,details,status,reported_by,reported_at) " +
            "values (41,'SP41-A1',1,'PUNCTURE','Synthetic puncture during first heat','PENDING','owner',now())");
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
            "insert into community_track_sprint_incident " +
            "(bracket_id,series_key,heat_number,incident_type,details,status,reported_by,reported_at) " +
            "values (41,'SP41-A1',1,'FALL','Synthetic second report','PENDING','owner',now())"));
        jdbc.update("update community_track_sprint_incident " +
            "set status='DECIDED', decision_type='RESTART_REQUIRED', " +
            "decision_reference='Fictional signed decision #41', " +
            "decision_reason='Fictional documented restart order', " +
            "decided_by='owner', decided_at=now() where bracket_id=41");
        jdbc.update("insert into community_track_sprint_incident " +
            "(bracket_id,series_key,heat_number,incident_type,details,status,reported_by,reported_at) " +
            "values (41,'SP41-A1',1,'PUNCTURE','Another fictional interrupted attempt','PENDING','owner',now())");
        assertEquals(2,count("community_track_sprint_incident"));
        assertThrows(DataIntegrityViolationException.class,
            ()->jdbc.update("delete from community_track_sprint_bracket where id=41"));
        assertEquals(3,count("community_round"));
    }

    @Test
    void aDocumentedDisqualificationRemainsUniquelyBlockedWithoutAnInventedWinner() {
        bracket(41,7,4,42L);
        jdbc.update("insert into community_track_sprint_incident " +
            "(bracket_id,series_key,heat_number,incident_type,details,status," +
            "reported_by,reported_at,decision_type,sanction_type,sanctioned_username," +
            "decision_reference,decision_reason,decided_by,decided_at) " +
            "values (41,'SP41-U1',1,'RULE_INFRINGEMENT','Fictional interference'," +
            "'ADJUDICATION_REQUIRED','owner',now(),'DISCIPLINARY_RULING'," +
            "'DISQUALIFICATION','fixture_rider_01','Witnessed minutes #2026/41'," +
            "'Fictional commission decision awaiting modified heat','owner',now())");
        assertEquals("DISQUALIFICATION", jdbc.queryForObject(
            "select sanction_type from community_track_sprint_incident where bracket_id=41",
            String.class));
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
            "insert into community_track_sprint_incident " +
            "(bracket_id,series_key,heat_number,incident_type,details,status,reported_by,reported_at) " +
            "values (41,'SP41-U1',1,'PUNCTURE','Fictional duplicate','PENDING','owner',now())"));
        assertEquals(1,count("community_track_sprint_incident"));
        assertEquals(3,count("community_round"));
    }

    @Test
    void approvedTwoRiderRerunKeepsSignedOrderAndBlocksASecondOpenIncident() {
        bracket(41,7,4,42L);
        String order = "[\"qa-rider-1\",\"qa-rider-2\"]";
        jdbc.update("insert into community_track_sprint_incident " +
            "(bracket_id,series_key,heat_number,incident_type,details,status," +
            "reported_by,reported_at,decision_type,sanction_type,sanctioned_username," +
            "decision_reference,decision_reason,decided_by,decided_at," +
            "rerun_riders_json,rerun_reference,rerun_reason,rerun_authorized_by,rerun_authorized_at) " +
            "values (41,'SP41-U1',1,'RULE_INFRINGEMENT','Synthetic contact'," +
            "'RESTART_AUTHORIZED','owner',now(),'DISCIPLINARY_RULING'," +
            "'DISQUALIFICATION','qa-rider-3','Initial official minutes #41'," +
            "'Synthetic ruling on fault','owner',now(),?,?,?,'owner',now())",
            order,"Separate rerun authorization #42",
            "Two unchanged starting positions confirmed in new fixture");
        assertEquals(order,jdbc.queryForObject(
            "select rerun_riders_json from community_track_sprint_incident where bracket_id=41",
            String.class));
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
            "insert into community_track_sprint_incident " +
            "(bracket_id,series_key,heat_number,incident_type,details,status,reported_by,reported_at) " +
            "values (41,'SP41-U1',1,'PUNCTURE','Fictional duplicate','PENDING','owner',now())"));
        assertEquals(1,count("community_track_sprint_incident"));
        assertEquals(3,count("community_round"));
    }

    @Test
    void olympicSeedsKeepTwentyFourPositionsWithoutDuplicateNamesOrIndexes() {
        bracket(41,7,4,42L);
        for (int i=0;i<24;i++) jdbc.update(
            "insert into community_track_sprint_qualified_rider " +
            "(bracket_id,qualifying_seed,username) values (?,?,?)",
            41,i,"qa-rider-"+(i+1));
        assertEquals(24,count("community_track_sprint_qualified_rider"));
        assertEquals(List.of("qa-rider-1","qa-rider-2","qa-rider-3"),
            jdbc.queryForList("select username from community_track_sprint_qualified_rider " +
                "where bracket_id=41 order by qualifying_seed limit 3",String.class));
        assertEquals("qa-rider-24",jdbc.queryForObject(
            "select username from community_track_sprint_qualified_rider " +
            "where bracket_id=41 and qualifying_seed=23",String.class));
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
            "insert into community_track_sprint_qualified_rider " +
            "(bracket_id,qualifying_seed,username) values (41,0,'someone-else')"));
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
            "insert into community_track_sprint_qualified_rider " +
            "(bracket_id,qualifying_seed,username) values (41,24,'qa-rider-1')"));
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
            "insert into community_track_sprint_qualified_rider " +
            "(bracket_id,qualifying_seed,username) values (999,0,'orphan')"));
    }

    @Test
    void linkedQualificationIsProtectedAndDeletingBracketKeepsHistoricalRounds() {
        bracket(41,7,4,42L);
        jdbc.update("insert into community_track_sprint_qualified_rider " +
            "(bracket_id,qualifying_seed,username) values (41,0,'qa-rider-1')");
        assertThrows(DataIntegrityViolationException.class,
            ()->jdbc.update("delete from community_round where id=42"));
        jdbc.update("delete from community_track_sprint_bracket where id=41");
        assertEquals(0,count("community_track_sprint_qualified_rider"));
        assertEquals(3,count("community_round"));
        assertEquals("Historical round",jdbc.queryForObject(
            "select name from community_round where id=99",String.class));
    }

    @Test
    void heatSequenceUniquenessIsScopedToCommunityAndSeason() {
        jdbc.update("insert into community_round " +
            "(id,community_id,season_id,name,event_type,sprint_series_key,sprint_heat_number) " +
            "values (100,7,4,'Heat 1','TRACK_SPRINT_REPECHAGE','SP41-U1',1)");
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
            "insert into community_round " +
            "(id,community_id,season_id,name,event_type,sprint_series_key,sprint_heat_number) " +
            "values (101,7,4,'Duplicate','TRACK_SPRINT_REPECHAGE','SP41-U1',1)"));
        jdbc.update("insert into community_round " +
            "(id,community_id,season_id,name,event_type,sprint_series_key,sprint_heat_number) " +
            "values (102,7,6,'Other season','TRACK_SPRINT_REPECHAGE','SP41-U1',1)");
        jdbc.update("insert into community_round " +
            "(id,community_id,season_id,name,event_type,sprint_series_key,sprint_heat_number) " +
            "values (103,8,5,'Other community','TRACK_SPRINT_REPECHAGE','SP41-U1',1)");
        assertEquals(6,count("community_round"));
    }

    @Test
    void missingSeasonAndSourceFailWithoutAlteringHistory() {
        assertThrows(DataIntegrityViolationException.class,()->bracket(41,7,999,42L));
        assertThrows(DataIntegrityViolationException.class,()->bracket(42,7,4,999L));
        assertEquals(0,count("community_track_sprint_bracket"));
        assertEquals(3,count("community_round"));
    }
}
