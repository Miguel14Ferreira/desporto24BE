package com.example.desporto24.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Explicitly opt-in, one-shot, read-only DB preflight for two unapproved
 * Braganca pavilion candidates. Never inserts venue rows or verifies an entrance.
 *
 * Uses the application's existing internal DataSource, not external DB access.
 * All output is private service-log metadata (existing public venue IDs only).
 */
@Component
@ConditionalOnProperty(prefix = "jynlo.venue.braganca-preflight",
        name = "enabled", havingValue = "true")
public class BragancaPavilionPreflightStartupProbe {
    private static final Logger LOG =
            LoggerFactory.getLogger(BragancaPavilionPreflightStartupProbe.class);
    private static final String RESOURCE =
            "db/changelog/venue-imports/preflight-braganca-two-pavilions-READONLY.sql";
    private static final String ARN_REF = "JYNLO:OFFICIAL:BRAGANCA:ARNALDO-PEREIRA";
    private static final String COXA_REF = "JYNLO:OFFICIAL:BRAGANCA:COXA";
    private static final int MAX_REVIEW_ROWS = 100;
    private static final Pattern UNSAFE_SQL = Pattern.compile(
            "\\b(?:INSERT|UPDATE|DELETE|TRUNCATE|DROP|ALTER|CREATE|MERGE|"
                    + "COPY|GRANT|REVOKE|CALL|EXECUTE|DO|FOR\\s+UPDATE|"
                    + "FOR\\s+SHARE|PG_SLEEP)\\b", Pattern.CASE_INSENSITIVE);

    private final DataSource dataSource;

    public BragancaPavilionPreflightStartupProbe(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            executeReadOnly();
        } catch (Exception failure) {
            // No SQL, names, coordinates, credentials or JDBC messages in logs.
            LOG.warn("JYNLO_BRAGANCA_PREFLIGHT_FAILED error_type={} no_writes=true",
                    failure.getClass().getSimpleName());
        }
    }

    void executeReadOnly() throws IOException, SQLException {
        String query = extractSingleSelect(loadResource());
        int matches = 0;
        int referenceMatches = 0;
        try (Connection connection = dataSource.getConnection()) {
            boolean wasAutoCommit = connection.getAutoCommit();
            boolean wasReadOnly = connection.isReadOnly();
            try {
                connection.setReadOnly(true);
                connection.setAutoCommit(false);
                try (Statement transaction = connection.createStatement()) {
                    transaction.execute("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY");
                }
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setQueryTimeout(12);
                    statement.setMaxRows(MAX_REVIEW_ROWS + 1);
                    try (ResultSet rows = statement.executeQuery()) {
                        while (rows.next()) {
                            if (++matches > MAX_REVIEW_ROWS) {
                                throw new IllegalStateException("Preflight result limit exceeded");
                            }
                            Long id = rows.getObject("id", Long.class);
                            if (id == null || id <= 0) {
                                throw new IllegalStateException("Unexpected venue identifier");
                            }
                            String sourceRef = rows.getString("source_reference");
                            String category = ARN_REF.equals(sourceRef) ? "ARNALDO_SOURCE_REFERENCE"
                                    : COXA_REF.equals(sourceRef) ? "COXA_SOURCE_REFERENCE"
                                    : "IDENTITY_OR_NEIGHBOUR_REVIEW";
                            if (!"IDENTITY_OR_NEIGHBOUR_REVIEW".equals(category)) {
                                referenceMatches++;
                            }
                            // A matched ID is a REVIEW lead, NOT evidence of identity
                            // or entrance approval. Never log addresses/contact/GPS.
                            LOG.info("JYNLO_BRAGANCA_PREFLIGHT_ROW existing_id={} category={} review_only=true",
                                    id, category);
                        }
                    }
                }
            } finally {
                try {
                    connection.rollback();
                } finally {
                    connection.setAutoCommit(wasAutoCommit);
                    connection.setReadOnly(wasReadOnly);
                }
            }
        }
        // Success only after read-only transaction and rollback have finished.
        LOG.info("JYNLO_BRAGANCA_PREFLIGHT_SUMMARY candidates=2 matches={} reference_matches={} "
                        + "review_required=true entries_approved=0 no_writes=true",
                matches, referenceMatches);
    }

    private static String loadResource() throws IOException {
        ClassPathResource resource = new ClassPathResource(RESOURCE);
        try (var stream = resource.getInputStream()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    static String extractSingleSelect(String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Missing preflight SQL");
        }
        // SQL comes only from our checked-in read-only resource.
        StringBuilder builder = new StringBuilder();
        for (String line : source.split("\\R")) {
            if (!line.stripLeading().startsWith("--")) {
                builder.append(line).append('\n');
            }
        }
        String value = builder.toString().strip();
        if (!value.startsWith("SELECT v.id,v.name,v.city,v.district,")
                || !value.endsWith(";")
                || value.indexOf(';') != value.length() - 1
                || !value.contains("FROM desporto24.venue v")
                || !value.contains("ORDER BY v.city,v.name,v.id")
                || !value.contains(ARN_REF)
                || !value.contains(COXA_REF)
                || UNSAFE_SQL.matcher(value).find()
                || value.contains("/*") || value.contains("*/")) {
            throw new IllegalArgumentException("Expected single review-only SELECT");
        }
        return value.substring(0, value.length() - 1);
    }
}
