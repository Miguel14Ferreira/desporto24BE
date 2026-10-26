package com.example.desporto24.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Opt-in, single-run audit of the 9 DRAFT venue candidates using the backend's
 * already authenticated INTERNAL PostgreSQL connection.
 *
 * Disabled unless JYNLO_VENUE_PREFLIGHT_ENABLED=true. No controller, API route,
 * credentials, persisted output, external connections or venue modifications.
 * Matches are emitted to PRIVATE Render service logs by source ref + public ID.
 */
@Component
@ConditionalOnProperty(prefix = "jynlo.venue.preflight", name = "enabled", havingValue = "true")
public class VenuePreflightStartupProbe {
    private static final Logger LOG = LoggerFactory.getLogger(VenuePreflightStartupProbe.class);
    private static final String RESOURCE =
            "db/changelog/venue-imports/preflight-nine-source-backed-READONLY-20261026.sql";
    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "SOURCE_REF_ALREADY_EXISTS", "SAME_NAME_CITY", "SAME_ADDRESS_CITY",
            "POSSIBLE_ALIAS_REVIEW", "NO_POTENTIAL_MATCH_REVIEW");

    private final DataSource dataSource;

    public VenuePreflightStartupProbe(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            executeReadOnly();
        } catch (Exception failure) {
            // A diagnostic must never disrupt application startup or leak JDBC
            // URLs, SQL errors, passwords, individual names or addresses.
            LOG.warn("JYNLO_VENUE_PREFLIGHT_FAILED error_type={} (no writes)",
                    failure.getClass().getSimpleName());
        }
    }

    void executeReadOnly() throws SQLException, IOException {
        String query = extractSelect(loadResource());
        Set<String> reviewed = new HashSet<>();
        int possibleMatches = 0;

        try (Connection connection = dataSource.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();
            boolean originalReadOnly = connection.isReadOnly();
            try {
                connection.setReadOnly(true);
                connection.setAutoCommit(false);
                // Explicit PostgreSQL enforcement, beyond the JDBC read-only hint.
                try (Statement transaction = connection.createStatement()) {
                    transaction.execute("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY");
                }
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setQueryTimeout(12);
                    try (ResultSet rows = statement.executeQuery()) {
                        while (rows.next()) {
                            String sourceRef = rows.getString("source_ref");
                            String classification = rows.getString("review_status");
                            Long existingId = rows.getObject("existing_id", Long.class);
                            if (sourceRef == null || !sourceRef.startsWith("JYNLO:OFFICIAL:")
                                    || !ALLOWED_STATUSES.contains(classification)) {
                                throw new IllegalStateException("Unexpected preflight output");
                            }
                            reviewed.add(sourceRef);
                            if (existingId != null) {
                                possibleMatches++;
                            }
                            LOG.info("JYNLO_VENUE_PREFLIGHT candidate={} status={} existing_id={}",
                                    sourceRef, classification, existingId);
                        }
                    }
                }
                if (reviewed.size() != 9) {
                    throw new IllegalStateException("Expected exactly nine candidates");
                }
                LOG.info("JYNLO_VENUE_PREFLIGHT_SUMMARY candidates={} potential_matches={} no_data_writes=true",
                        reviewed.size(), possibleMatches);
            } finally {
                try {
                    connection.rollback();
                } finally {
                    connection.setAutoCommit(originalAutoCommit);
                    connection.setReadOnly(originalReadOnly);
                }
            }
        }
    }

    private static String loadResource() throws IOException {
        ClassPathResource resource = new ClassPathResource(RESOURCE);
        try (var input = resource.getInputStream()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    static String extractSelect(String fileContent) {
        final String begin = "BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY;";
        if (fileContent == null || !fileContent.contains(begin)
                || !fileContent.stripTrailing().endsWith("ROLLBACK;")) {
            throw new IllegalArgumentException("Preflight resource is not read-only");
        }
        int start = fileContent.indexOf("\nWITH candidates(");
        int end = fileContent.lastIndexOf("\nROLLBACK;");
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("No preflight SELECT in resource");
        }
        String query = fileContent.substring(start + 1, end).trim();
        if (!query.startsWith("WITH candidates(") || !query.endsWith(";")) {
            throw new IllegalArgumentException("Unexpected preflight SELECT shape");
        }
        query = query.substring(0, query.length() - 1);
        if (query.contains(";")
                || !query.toUpperCase(Locale.ROOT).contains("\nSELECT C.SOURCE_REF")
                || query.contains("--;")) {
            throw new IllegalArgumentException("Unexpected additional SQL statements");
        }
        return query;
    }
}
