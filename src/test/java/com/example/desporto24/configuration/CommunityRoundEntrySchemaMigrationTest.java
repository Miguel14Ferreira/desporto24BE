package com.example.desporto24.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.jupiter.api.Assertions.*;

class CommunityRoundEntrySchemaMigrationTest {

    @Test
    void permitsHistoricalPlayersWithoutLosingExistingProfileLinks() {
        JdbcTemplate jdbc = new JdbcTemplate(new DriverManagerDataSource(
                "jdbc:h2:mem:round-entry-migration;DB_CLOSE_DELAY=-1", "sa", ""));
        jdbc.execute("create table profile (id bigint primary key)");
        jdbc.execute("create table community_round_entry (id bigint primary key, "
                + "profile_id bigint not null references profile(id), "
                + "community_player_id bigint not null)");
        jdbc.execute("insert into profile values (7)");
        jdbc.execute("insert into community_round_entry values (1, 7, 10)");
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> jdbc.execute("insert into community_round_entry values (2, null, 11)"));

        CommunityRoundEntrySchemaMigration migration = new CommunityRoundEntrySchemaMigration(jdbc);
        migration.allowEntriesWithoutProfile();
        migration.allowEntriesWithoutProfile();

        jdbc.execute("insert into community_round_entry values (2, null, 11)");
        assertEquals(7L, jdbc.queryForObject(
                "select profile_id from community_round_entry where id=1", Long.class));
        assertNull(jdbc.queryForObject(
                "select profile_id from community_round_entry where id=2", Long.class));
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> jdbc.execute("insert into community_round_entry values (3, 99, 12)"));
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> jdbc.execute("insert into community_round_entry values (4, null, null)"));
    }
}
