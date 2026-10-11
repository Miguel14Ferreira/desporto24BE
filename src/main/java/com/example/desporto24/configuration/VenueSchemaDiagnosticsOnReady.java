package com.example.desporto24.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VenueSchemaDiagnosticsOnReady {

    private final JdbcTemplate jdbcTemplate;
    private final Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void report() {
        try {
            log.info("VENUE_SCHEMA_DIAGNOSTIC liquibase_enabled={} liquibase_schema={} default_schema={}",
                    environment.getProperty("spring.liquibase.enabled", "true"),
                    environment.getProperty("spring.liquibase.liquibase-schema", "(unset)"),
                    environment.getProperty("spring.liquibase.default-schema", "(unset)"));
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
            java.util.List<String> changelogSchemas = jdbcTemplate.queryForList(
                    "SELECT table_schema FROM information_schema.tables "
                            + "WHERE lower(table_name) = 'databasechangelog' "
                            + "ORDER BY table_schema",
                    String.class);
            log.info("VENUE_SCHEMA_DIAGNOSTIC changelog_schemas={}", changelogSchemas);
            for (String schema : changelogSchemas) {
                if (!schema.matches("[A-Za-z_][A-Za-z0-9_]*")) continue;
                jdbcTemplate.query(
                        "SELECT id, exectype FROM \\"" + schema + "\\".databasechangelog WHERE id = ?",
                        (org.springframework.jdbc.core.RowCallbackHandler) rs ->
                                log.info("VENUE_SCHEMA_DIAGNOSTIC changeset_schema={} id={} exectype={}",
                                        schema, rs.getString("id"), rs.getString("exectype")),
                        "2026-10-11-venue-information-source-constraint");
            }
        } catch (Exception ex) {
            log.warn("VENUE_SCHEMA_DIAGNOSTIC failed: {}", ex.toString());
        }
    }
}
