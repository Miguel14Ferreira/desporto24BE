package com.example.desporto24.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;

class VenuePreflightStartupProbeTest {

    private static String fileContent() throws Exception {
        try (var input = new ClassPathResource(
                "db/changelog/venue-imports/preflight-nine-source-backed-READONLY-20261026.sql")
                .getInputStream()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void isExplicitlyOptInAndDisabledWhenConfigurationMissing() {
        var annotation = VenuePreflightStartupProbe.class.getAnnotation(ConditionalOnProperty.class);
        assertNotNull(annotation);
        assertEquals("jynlo.venue.preflight", annotation.prefix());
        assertArrayEquals(new String[]{"enabled"}, annotation.name());
        assertEquals("true", annotation.havingValue());
        assertFalse(annotation.matchIfMissing());
    }

    @Test
    void extractsSingleSelectFromCheckedInReadOnlySqlWithoutTransactionCommands() throws Exception {
        String extracted = VenuePreflightStartupProbe.extractSelect(fileContent());
        assertTrue(extracted.startsWith("WITH candidates("));
        assertTrue(extracted.contains("SELECT c.source_ref"));
        assertFalse(extracted.contains("BEGIN TRANSACTION"));
        assertFalse(extracted.contains("ROLLBACK"));
        assertFalse(extracted.contains(";"));
        Matcher matcher = Pattern.compile("'(JYNLO:OFFICIAL:[A-Z0-9:-]+)'").matcher(extracted);
        int candidates = 0;
        while (matcher.find()) candidates++;
        assertEquals(9, candidates, "Probe source manifest must contain exactly 9 candidates");
    }

    @Test
    void refusesSqlResourcesWithAdditionalStatementsOrMissingReadOnlyMarker() throws Exception {
        String original = fileContent();
        assertThrows(IllegalArgumentException.class,
                () -> VenuePreflightStartupProbe.extractSelect(original.replace(
                        "BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY;",
                        "BEGIN;")));
        assertThrows(IllegalArgumentException.class,
                () -> VenuePreflightStartupProbe.extractSelect(original.replace(
                        "\nROLLBACK;", "\nDELETE FROM desporto24.venue;\nROLLBACK;")));
        assertThrows(IllegalArgumentException.class,
                () -> VenuePreflightStartupProbe.extractSelect(original + "\nCOMMIT;"));
    }

    @Test
    void usesExistingDatasourceWithDatabaseReadOnlyTransactionAndRollsBack() throws Exception {
        DataSource source = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Statement readonly = mock(Statement.class);
        PreparedStatement query = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        when(source.getConnection()).thenReturn(connection);
        when(connection.getAutoCommit()).thenReturn(true);
        when(connection.isReadOnly()).thenReturn(false);
        when(connection.createStatement()).thenReturn(readonly);
        when(connection.prepareStatement(anyString())).thenReturn(query);
        when(query.executeQuery()).thenReturn(rows);

        Matcher matcher = Pattern.compile("'(JYNLO:OFFICIAL:[A-Z0-9:-]+)'")
                .matcher(VenuePreflightStartupProbe.extractSelect(fileContent()));
        List<String> refs = new ArrayList<>();
        while (matcher.find()) refs.add(matcher.group(1));
        assertEquals(9, refs.size());
        final int[] index = {0};
        when(rows.next()).thenAnswer(x -> index[0]++ < refs.size());
        when(rows.getString("source_ref")).thenAnswer(x -> refs.get(index[0] - 1));
        when(rows.getString("review_status")).thenReturn("NO_POTENTIAL_MATCH_REVIEW");

        new VenuePreflightStartupProbe(source).executeReadOnly();

        verify(connection).setReadOnly(true);
        verify(connection).setAutoCommit(false);
        verify(readonly).execute("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY");
        verify(query).setQueryTimeout(12);
        verify(query).executeQuery();
        verify(connection).rollback();
        verify(connection).setAutoCommit(true);
        verify(connection).setReadOnly(false);
        verify(connection).close();
        verifyNoMoreInteractions(source);
    }

    @Test
    void unknownStatusIsRejectedAndTransactionStillRolledBack() throws Exception {
        DataSource source = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Statement readonly = mock(Statement.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        when(source.getConnection()).thenReturn(connection);
        when(connection.getAutoCommit()).thenReturn(true);
        when(connection.createStatement()).thenReturn(readonly);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rows);
        when(rows.next()).thenReturn(true);
        when(rows.getString("source_ref")).thenReturn("JYNLO:OFFICIAL:TEST");
        when(rows.getString("review_status")).thenReturn("DROP EVERYTHING");

        assertThrows(IllegalStateException.class,
                () -> new VenuePreflightStartupProbe(source).executeReadOnly());
        verify(connection).rollback();
        verify(connection).close();
    }
}
