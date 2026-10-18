package com.example.desporto24.repository.venue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Method;
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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
 * The Maven CI skips it unless the opt-in environment is present.
 */
class VenueSuggestPostgresIntegrationTest {
    private static final Pattern PARAMETER = Pattern.compile(
            ":(namePattern|cityPattern|sport|offset)");

    @Test
    @EnabledIfEnvironmentVariable(named = "JYNLO_ALLOW_PG_SQL_SMOKE",
                                  matches = "YES_TEST_DATABASE_ONLY")
    void realPostgresFiltersTokensAndPaginatesWithStableIds() throws Exception {
        String url = System.getenv("JYNLO_VENUE_TEST_PG_URL");
        assumeTrue(url != null && url.startsWith("jdbc:postgresql:")
                && !url.isBlank(), "Explicit isolated PostgreSQL test URL required");
        try (Connection connection = DriverManager.getConnection(
                url,
                System.getenv("JYNLO_VENUE_TEST_PG_USER"),
                System.getenv("JYNLO_VENUE_TEST_PG_PASSWORD"))) {
            connection.setAutoCommit(false);
            try {
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
