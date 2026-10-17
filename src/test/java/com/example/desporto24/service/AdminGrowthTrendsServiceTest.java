package com.example.desporto24.service;

import com.example.desporto24.controllers.AdminPlatformOverviewController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminGrowthTrendsServiceTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks AdminGrowthTrendsService service;

    @Test
    void providesEightOrderedWeeksWithActualGroupedCountsAndZerosForEmptyWeeks() throws Exception {
        LocalDate monday = LocalDate.now(ZoneOffset.UTC)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        doAnswer(invocation -> {
            String sql = invocation.getArgument(0);
            RowCallbackHandler callback = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getDate("week_start")).thenReturn(Date.valueOf(monday));
            when(rs.getLong("total")).thenReturn(
                    sql.contains("desporto24.profile") ? 8L
                    : sql.contains("desporto24.session") ? 5L : 2L);
            callback.processRow(rs);
            return null;
        }).when(jdbc).query(anyString(), any(RowCallbackHandler.class), any(), any());

        var history = service.trends();

        assertEquals("UTC", history.timezone());
        assertEquals("CREATED_PER_WEEK", history.metric());
        assertEquals(8, history.weeks().size());
        assertEquals(monday.minusWeeks(7), history.weeks().get(0).weekStart());
        assertEquals(monday, history.weeks().get(7).weekStart());
        assertEquals(0, history.weeks().get(0).users());
        assertEquals(0, history.weeks().get(0).sessions());
        assertEquals(0, history.weeks().get(0).communities());
        assertEquals(8, history.weeks().get(7).users());
        assertEquals(5, history.weeks().get(7).sessions());
        assertEquals(2, history.weeks().get(7).communities());
        assertNotNull(history.generatedAt());
        verify(jdbc, times(3)).query(anyString(), any(RowCallbackHandler.class),
                any(), any());
    }

    @Test
    void trendsEndpointInheritsAdminOnlySecurityAndExposesNoPersonalInformation()
            throws Exception {
        var admin = AdminPlatformOverviewController.class.getAnnotation(PreAuthorize.class);
        assertNotNull(admin);
        assertEquals("hasAnyRole('ADMIN', 'SUPER_ADMIN')", admin.value());
        var endpoint = AdminPlatformOverviewController.class.getMethod("trends");
        assertEquals("/trends", endpoint.getAnnotation(GetMapping.class).value()[0]);
        var fields = java.util.Arrays.stream(AdminGrowthTrendsService.WeeklyGrowth.class
                .getRecordComponents()).map(java.lang.reflect.RecordComponent::getName).toList();
        assertEquals(java.util.List.of("weekStart", "users", "sessions", "communities"), fields);
    }
}
