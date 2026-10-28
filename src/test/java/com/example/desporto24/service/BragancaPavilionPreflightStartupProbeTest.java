package com.example.desporto24.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;

class BragancaPavilionPreflightStartupProbeTest {
    private static String sql() throws Exception {
        try (var stream = new ClassPathResource(
                "db/changelog/venue-imports/preflight-braganca-two-pavilions-READONLY.sql")
                .getInputStream()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void disabledByDefaultAndRequiresExplicitOptIn() {
        ConditionalOnProperty c = BragancaPavilionPreflightStartupProbe.class
                .getAnnotation(ConditionalOnProperty.class);
        assertNotNull(c);
        assertEquals("jynlo.venue.braganca-preflight", c.prefix());
        assertArrayEquals(new String[]{"enabled"}, c.name());
        assertEquals("true", c.havingValue());
        assertFalse(c.matchIfMissing());
    }

    @Test
    void checkedInResourceIsOneBoundedSelectWithoutMutation() throws Exception {
        String query = BragancaPavilionPreflightStartupProbe.extractSingleSelect(sql());
        assertTrue(query.startsWith("SELECT v.id,v.name,v.city,v.district,"));
        assertTrue(query.contains("FROM desporto24.venue v"));
        assertTrue(query.contains("JYNLO:OFFICIAL:BRAGANCA:ARNALDO-PEREIRA"));
        assertTrue(query.contains("JYNLO:OFFICIAL:BRAGANCA:COXA"));
        assertFalse(query.contains(";"));
    }

    @Test
    void rejectsExtraSqlAndDangerousModifiers() throws Exception {
        String original = sql();
        for (String malicious : new String[]{
                original + " DELETE FROM desporto24.venue;",
                original.replace("ORDER BY v.city,v.name,v.id;",
                        "FOR UPDATE ORDER BY v.city,v.name,v.id;"),
                original.replace("SELECT v.id", "UPDATE v SET name='bad' RETURNING v.id"),
                original.replace("JYNLO:OFFICIAL:BRAGANCA:COXA", "UNEXPECTED_REFERENCE"),
                "SELECT id FROM venue;"
        }) {
            assertThrows(IllegalArgumentException.class,
                    () -> BragancaPavilionPreflightStartupProbe.extractSingleSelect(malicious));
        }
    }

    private static DataSource source(Connection connection) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getAutoCommit()).thenReturn(true);
        when(connection.isReadOnly()).thenReturn(false);
        return dataSource;
    }

    @Test
    void readsViaInternalDatasourceWithReadOnlyTransactionAndRollback() throws Exception {
        Connection c = mock(Connection.class);
        Statement tx = mock(Statement.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        DataSource source = source(c);
        when(c.createStatement()).thenReturn(tx);
        when(c.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rows);
        when(rows.next()).thenReturn(true, true, false);
        when(rows.getObject("id", Long.class)).thenReturn(1139L, 1141L);
        when(rows.getString("source_reference")).thenReturn(null,
                "JYNLO:OFFICIAL:BRAGANCA:COXA");

        new BragancaPavilionPreflightStartupProbe(source).executeReadOnly();
        verify(c).setReadOnly(true);
        verify(c).setAutoCommit(false);
        verify(tx).execute("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY");
        verify(stmt).setQueryTimeout(12);
        verify(stmt).setMaxRows(101);
        verify(stmt).executeQuery();
        verify(c).rollback();
        verify(c).setAutoCommit(true);
        verify(c).setReadOnly(false);
        verify(c).close();
        verify(source).getConnection();
        verifyNoMoreInteractions(source);
    }

    @Test
    void invalidRecordStillRollsBackAndDoesNotIssueWrites() throws Exception {
        Connection c = mock(Connection.class);
        Statement tx = mock(Statement.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        DataSource source = source(c);
        when(c.createStatement()).thenReturn(tx);
        when(c.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rows);
        when(rows.next()).thenReturn(true);
        when(rows.getObject("id", Long.class)).thenReturn(-1L);

        assertThrows(IllegalStateException.class,
                () -> new BragancaPavilionPreflightStartupProbe(source).executeReadOnly());
        verify(c).rollback();
        verify(c).close();
        verify(stmt, never()).executeUpdate();
    }

    @Test
    void missingDatasourceConnectionDoesNotLeadToAnyOtherConnection() throws Exception {
        DataSource source = mock(DataSource.class);
        when(source.getConnection()).thenThrow(new java.sql.SQLException("Unavailable"));
        assertThrows(java.sql.SQLException.class,
                () -> new BragancaPavilionPreflightStartupProbe(source).executeReadOnly());
        verify(source, times(1)).getConnection();
        verifyNoMoreInteractions(source);
    }
}
