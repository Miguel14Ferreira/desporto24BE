package com.example.desporto24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Eight calendar weeks of actual creation events (UTC, Monday through Sunday).
 * Three grouped SQL queries only; missing weeks are explicitly reported as zero.
 * No private user details leave the database.
 */
@Service
@RequiredArgsConstructor
public class AdminGrowthTrendsService {
    public static final int WEEKS = 8;
    private final JdbcTemplate jdbc;

    @Transactional(readOnly = true)
    public GrowthTrends trends() {
        Instant now = Instant.now();
        LocalDate thisMonday = now.atZone(ZoneOffset.UTC).toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate firstMonday = thisMonday.minusWeeks(WEEKS - 1L);
        Instant since = firstMonday.atStartOfDay(ZoneOffset.UTC).toInstant();

        Map<LocalDate, Long> registered = readWeekly("""
                SELECT date_trunc('week', join_date)::date AS week_start, COUNT(*) AS total
                FROM desporto24.profile
                WHERE join_date >= ? AND join_date <= ?
                  AND (email IS NULL OR lower(email) NOT LIKE '%@deleted.invalid')
                GROUP BY 1
                """, since, now);
        Map<LocalDate, Long> sessions = readWeekly("""
                SELECT date_trunc('week', created_at)::date AS week_start, COUNT(*) AS total
                FROM desporto24.session
                WHERE created_at >= ? AND created_at <= ?
                GROUP BY 1
                """, since, now);
        Map<LocalDate, Long> communities = readWeekly("""
                SELECT date_trunc('week', created_at)::date AS week_start, COUNT(*) AS total
                FROM desporto24.community
                WHERE created_at >= ? AND created_at <= ?
                GROUP BY 1
                """, since, now);

        List<WeeklyGrowth> weeks = new ArrayList<>(WEEKS);
        for (int i = 0; i < WEEKS; i++) {
            LocalDate monday = firstMonday.plusWeeks(i);
            weeks.add(new WeeklyGrowth(monday,
                    registered.getOrDefault(monday, 0L),
                    sessions.getOrDefault(monday, 0L),
                    communities.getOrDefault(monday, 0L)));
        }
        return new GrowthTrends(now, "UTC", "CREATED_PER_WEEK", List.copyOf(weeks));
    }

    private Map<LocalDate, Long> readWeekly(String sql, Instant since, Instant now) {
        Map<LocalDate, Long> results = new HashMap<>();
        jdbc.query(sql, rs -> {
            LocalDate week = rs.getDate("week_start").toLocalDate();
            results.put(week, rs.getLong("total"));
        }, Timestamp.from(since), Timestamp.from(now));
        return results;
    }

    public record WeeklyGrowth(LocalDate weekStart, long users, long sessions, long communities) {}

    public record GrowthTrends(Instant generatedAt, String timezone, String metric,
                               List<WeeklyGrowth> weeks) {}
}
