package com.example.desporto24.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;
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
    void completeReportIsLoggedOnlyAfterSuccessfulRollbackAndConnectionClose() throws Exception {
        Connection c = mock(Connection.class);
        Statement tx = mock(Statement.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        DataSource data = source(c);
        when(c.createStatement()).thenReturn(tx);
        when(c.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rows);
        when(rows.next()).thenReturn(true, true, false);
        when(rows.getObject("id", Long.class)).thenReturn(1L, 2L);

        Logger logger = (Logger) LoggerFactory.getLogger(BragancaPavilionPreflightStartupProbe.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        try {
            doAnswer(invocation -> {
                assertTrue(appender.list.isEmpty(), "No partial report before rollback");
                return null;
            }).when(c).rollback();
            doAnswer(invocation -> {
                assertTrue(appender.list.isEmpty(), "No report before connection close");
                return null;
            }).when(c).close();

            new BragancaPavilionPreflightStartupProbe(data).executeReadOnly();

            List<String> lines = appender.list.stream().map(ILoggingEvent::getFormattedMessage).toList();
            assertEquals(3, lines.size(), "Two rows and exactly one success summary");
            assertTrue(lines.get(0).contains("existing_id=1"));
            assertTrue(lines.get(1).contains("existing_id=2"));
            assertTrue(lines.get(2).contains("complete=true"));
            assertTrue(lines.get(2).contains("entries_approved=0"));
        } finally {
            logger.detachAppender(appender);
            appender.stop();
        }
    }

    @Test
    void exceedingOneHundredRowsIsFailureWithNoPartialLog() throws Exception {
        Connection c = mock(Connection.class);
        Statement tx = mock(Statement.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        DataSource data = source(c);
        when(c.createStatement()).thenReturn(tx);
        when(c.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rows);
        AtomicInteger rowCounter = new AtomicInteger();
        when(rows.next()).thenAnswer(invocation -> rowCounter.incrementAndGet() <= 101);
        when(rows.getObject("id", Long.class))
                .thenAnswer(invocation -> (long) rowCounter.get());

        Logger logger = (Logger) LoggerFactory.getLogger(BragancaPavilionPreflightStartupProbe.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        try {
            assertThrows(IllegalStateException.class,
                    () -> new BragancaPavilionPreflightStartupProbe(data).executeReadOnly());
            assertEquals(101, rowCounter.get());
            assertTrue(appender.list.isEmpty(),
                    "An incomplete report must never look like successful results");
            verify(c).rollback();
            verify(c).close();
            verify(stmt, never()).executeUpdate();
        } finally {
            logger.detachAppender(appender);
            appender.stop();
        }
    }

    @Test
    void rollbackFailureCannotEmitAuditSuccessOrPartialMatches() throws Exception {
        Connection c = mock(Connection.class);
        Statement tx = mock(Statement.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        DataSource data = source(c);
        when(c.createStatement()).thenReturn(tx);
        when(c.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rows);
        when(rows.next()).thenReturn(true, false);
        when(rows.getObject("id", Long.class)).thenReturn(42L);
        doThrow(new SQLException("Synthetic rollback failure")).when(c).rollback();

        Logger logger = (Logger) LoggerFactory.getLogger(BragancaPavilionPreflightStartupProbe.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        try {
            assertThrows(SQLException.class,
                    () -> new BragancaPavilionPreflightStartupProbe(data).executeReadOnly());
            assertTrue(appender.list.isEmpty());
            verify(c).close();
        } finally {
            logger.detachAppender(appender);
            appender.stop();
        }
    }

    @Test
    void duplicateVenueIdRejectsIncompleteReport() throws Exception {
        Connection c = mock(Connection.class);
        Statement tx = mock(Statement.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rows = mock(ResultSet.class);
        DataSource data = source(c);
        when(c.createStatement()).thenReturn(tx);
        when(c.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rows);
        when(rows.next()).thenReturn(true, true, false);
        when(rows.getObject("id", Long.class)).thenReturn(5L, 5L);
        assertThrows(IllegalStateException.class,
                () -> new BragancaPavilionPreflightStartupProbe(data).executeReadOnly());
        verify(c).rollback();
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
