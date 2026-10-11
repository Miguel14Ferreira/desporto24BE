package com.example.desporto24.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VenueSchemaDiagnosticsOnReady {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void report() {
        try {
            String activeSchema = jdbcTemplate.queryForObject("SELECT current_schema()", String.class);
            log.info("VENUE_SCHEMA_DIAGNOSTIC current_schema={}", activeSchema);
            jdbcTemplate.query(
                    "SELECT n.nspname, pg_get_constraintdef(c.oid) AS definition "
                            + "FROM pg_constraint c "
                            + "JOIN pg_class t ON t.oid = c.conrelid "
                            + "JOIN pg_namespace n ON n.oid = t.relnamespace "
                            + "WHERE t.relname = 'venue' AND c.conname = 'venue_information_source_check'",
                    (org.springframework.jdbc.core.RowCallbackHandler) rs -> log.info("VENUE_SCHEMA_DIAGNOSTIC constraint_schema={} definition={}",
                            rs.getString("nspname"), rs.getString("definition")));
            jdbcTemplate.query(
                    "SELECT table_schema FROM information_schema.tables "
                            + "WHERE lower(table_name) = 'databasechangelog' "
                            + "ORDER BY table_schema",
                    (org.springframework.jdbc.core.RowCallbackHandler) rs -> log.info("VENUE_SCHEMA_DIAGNOSTIC changelog_schema={}",
                            rs.getString("table_schema")));
        } catch (Exception ex) {
            log.warn("VENUE_SCHEMA_DIAGNOSTIC failed: {}", ex.toString());
        }
    }
}
