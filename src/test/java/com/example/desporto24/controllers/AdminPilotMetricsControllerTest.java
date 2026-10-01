package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.analytics.PilotEventCountsResponse;
import com.example.desporto24.service.PilotEventService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminPilotMetricsControllerTest {

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
