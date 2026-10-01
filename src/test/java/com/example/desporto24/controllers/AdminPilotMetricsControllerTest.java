package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.analytics.PilotActivation7dResponse;
import com.example.desporto24.model.dto.analytics.PilotEventCountsResponse;
import com.example.desporto24.model.dto.analytics.PilotRepetition30dResponse;
import com.example.desporto24.service.PilotEventService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminPilotMetricsControllerTest {

    @Test
    void returnsPilotActivation7d() {
        PilotEventService service = mock(PilotEventService.class);
        var expected = new PilotActivation7dResponse(
                30L,
                50L,
                new BigDecimal("60.00"),
                50L,
                true
        );
        when(service.activation7d()).thenReturn(expected);

        var controller = new AdminPilotMetricsController(service);

        assertEquals(expected, controller.activation7d());
        verify(service).activation7d();
    }

    @Test
    void returnsPilotRepetition30d() {
        PilotEventService service = mock(PilotEventService.class);
        var expected = new PilotRepetition30dResponse(
                6L,
                20L,
                new BigDecimal("30.00"),
                20L,
                true
        );
        when(service.repetition30d()).thenReturn(expected);

        var controller = new AdminPilotMetricsController(service);

        assertEquals(expected, controller.repetition30d());
        verify(service).repetition30d();
    }

    @Test
    void returnsPilotEventCounts() {
        PilotEventService service = mock(PilotEventService.class);
        var expected = new PilotEventCountsResponse(200L, 100L, 70L, 20L, 60L, 30L, 10L);
        when(service.eventCounts()).thenReturn(expected);

        var controller = new AdminPilotMetricsController(service);

        assertEquals(expected, controller.eventCounts());
        verify(service).eventCounts();
    }
}
