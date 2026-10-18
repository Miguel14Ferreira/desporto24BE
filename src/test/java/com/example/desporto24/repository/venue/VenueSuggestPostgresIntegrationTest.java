package com.example.desporto24.repository.venue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Method;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Optional real PostgreSQL smoke test, requiring an explicitly designated TEST database.
 * It creates only a TEMP TABLE, never reads or modifies the real venue table.
 *
 * Run with:
 * JYNLO_VENUE_TEST_PG_URL=jdbc:postgresql://localhost:5432/jynlo_test
 * JYNLO_VENUE_TEST_PG_USER=...
 * JYNLO_VENUE_TEST_PG_PASSWORD=...
 * JYNLO_ALLOW_PG_SQL_SMOKE=YES_TEST_DATABASE_ONLY
 *
 * Only loopback hosts (NOT Render/prod), exact /jynlo_test and explicit port
 * are accepted. Never tunnel a live DB into the local test port.
 * The Maven CI skips the integration method unless the opt-in env is present.
 */
class VenueSuggestPostgresIntegrationTest {
    private static final Pattern PARAMETER = Pattern.compile(
            ":(namePattern|cityPattern|sport|offset)");

    @Test
    @EnabledIfEnvironmentVariable(named = "JYNLO_ALLOW_PG_SQL_SMOKE",
                                  matches = "YES_TEST_DATABASE_ONLY")
    void realPostgresFiltersTokensAndPaginatesWithStableIds() throws Exception {
        String url = System.getenv("JYNLO_VENUE_TEST_PG_URL");
        assertTrue(isIsolatedTestDatabaseUrl(url),
                "SQL smoke test requires a loopback PostgreSQL server and exact jynlo_test DB");
        try (Connection connection = DriverManager.getConnection(
                url,
                System.getenv("JYNLO_VENUE_TEST_PG_USER"),
                System.getenv("JYNLO_VENUE_TEST_PG_PASSWORD"))) {
            connection.setAutoCommit(false);
            try {
                // Check server-reported DB name BEFORE even creating a temporary table.
                try (Statement probe = connection.createStatement();
                     ResultSet identity = probe.executeQuery("SELECT current_database()")) {
                    assertTrue(identity.next(), "PostgreSQL identity query returned no rows");
                    assertEquals("jynlo_test", identity.getString(1),
                            "Never execute the fixture against any other database");
                }
                try (Statement statement = connection.createStatement()) {
                    statement.execute("""
                            CREATE TEMP TABLE venue (
                              id BIGINT PRIMARY KEY, name TEXT NOT NULL,
                              city TEXT NOT NULL, address TEXT NOT NULL, sports TEXT
                            ) ON COMMIT DROP
                            """);
                }
                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO venue (id,name,city,address,sports) VALUES (?,?,?,?,?)")) {
                    for (int id = 1; id <= 85; id++) {
                        insert.setLong(1, id);
                        insert.setString(2, "Pavilhão Central");
                        insert.setString(3, "Lisboa");
                        insert.setString(4, "Rua Exemplo " + id);
                        insert.setString(5, "  Padel; Ténis ");
                        insert.addBatch();
                    }
                    insert.setLong(1, 86L);
                    insert.setString(2, "Pavilhão Central");
                    insert.setString(3, "Lisboa");
                    insert.setString(4, "Rua Exemplo 86");
                    insert.setString(5, "Padelball");
                    insert.addBatch();
                    insert.setLong(1, 87L);
                    insert.setString(2, "100% _ ~ Clube");
                    insert.setString(3, "Porto");
                    insert.setString(4, "Rua da Constituição");
                    insert.setString(5, "Futsal, Padel");
                    insert.addBatch();
                    insert.executeBatch();
                }
                List<Long> first = query(connection, "%pavilhão%", "%lisboa%", "padel", 0);
                List<Long> second = query(connection, "%pavilhão%", "%lisboa%", "padel", 40);
                List<Long> third = query(connection, "%pavilhão%", "%lisboa%", "padel", 80);
                assertEquals(41, first.size()); // includes hasMore lookahead
                assertEquals(41, second.size());
                assertEquals(List.of(81L, 82L, 83L, 84L, 85L), third);
                assertEquals(40L, first.get(39));
                assertEquals(41L, second.getFirst());
                assertEquals(80L, second.get(39));
                assertFalse(first.subList(0, 40).contains(41L));
                assertFalse(third.contains(86L)); // not a complete Padel token
                assertEquals(List.of(87L), query(connection,
                        "%100~% ~_ ~~ clube%", "%porto%", "padel", 0));
                assertTrue(query(connection, "%pavilhão%", "%lisboa%", "badminton", 0).isEmpty());
            } finally {
                connection.rollback(); // No changes persist in test database.
            }
        }
    }

    /**
     * A token alone must never enable tests against production.
     * This is a best-effort safeguard: never tunnel production to localhost.
     */
    static boolean isIsolatedTestDatabaseUrl(String url) {
        if (url == null || !url.startsWith("jdbc:postgresql://")) return false;
        try {
            URI uri = URI.create(url.substring("jdbc:".length()));
            String host = uri.getHost();
            return host != null
                    && ("localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host)
                        || "::1".equals(host) || "[::1]".equals(host))
                    && "/jynlo_test".equals(uri.getPath())
                    && uri.getPort() > 0 && uri.getPort() <= 65535
                    && uri.getUserInfo() == null
                    && uri.getRawQuery() == null
                    && uri.getFragment() == null;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    @Test
    void sqlSmokeRejectsRemoteAndProductionDatabaseUrlsEvenWhenOptedIn() {
        assertTrue(isIsolatedTestDatabaseUrl("jdbc:postgresql://localhost:5432/jynlo_test"));
        assertTrue(isIsolatedTestDatabaseUrl("jdbc:postgresql://127.0.0.1:5433/jynlo_test"));
        assertFalse(isIsolatedTestDatabaseUrl("jdbc:postgresql://db.internal:5432/jynlo_test"));
        assertFalse(isIsolatedTestDatabaseUrl("jdbc:postgresql://localhost:5432/desporto24_preview_db"));
        assertFalse(isIsolatedTestDatabaseUrl("jdbc:postgresql://localhost:5432/jynlo_test?currentSchema=public"));
        assertFalse(isIsolatedTestDatabaseUrl("jdbc:postgresql://localhost:5432/postgres"));
        assertFalse(isIsolatedTestDatabaseUrl("jdbc:postgresql://prod.example.com:5432/desporto24"));
        assertFalse(isIsolatedTestDatabaseUrl(null));
    }

    /** Execute the repository annotation itself, not a copy of its SQL. */
    private List<Long> query(Connection connection, String name, String city, String sport,
                             int offset) throws Exception {
        Method method = VenueRepository.class.getMethod(
                "findSuggestions", String.class, String.class, String.class, int.class);
        String sql = method.getAnnotation(Query.class).value();
        Matcher matcher = PARAMETER.matcher(sql);
        var tokens = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            tokens.add(matcher.group(1));
            matcher.appendReplacement(buffer, "?");
        }
        matcher.appendTail(buffer);
        try (PreparedStatement statement = connection.prepareStatement(buffer.toString())) {
            for (int index = 0; index < tokens.size(); index++) {
                switch (tokens.get(index)) {
                    case "namePattern" -> statement.setString(index + 1, name);
                    case "cityPattern" -> statement.setString(index + 1, city);
                    case "sport" -> statement.setString(index + 1, sport);
                    case "offset" -> statement.setInt(index + 1, offset);
                    default -> throw new AssertionError("Unknown query argument");
                }
            }
            List<Long> ids = new ArrayList<>();
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) ids.add(rows.getLong("id"));
            }
            return ids;
        }
    }
}
