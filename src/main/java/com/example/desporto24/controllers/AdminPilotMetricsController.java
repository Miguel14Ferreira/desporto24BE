package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.analytics.PilotEventCountsResponse;
import com.example.desporto24.service.PilotEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/pilot")
public class AdminPilotMetricsController {

    private final PilotEventService pilotEventService;

    @GetMapping("/event-counts")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotEventCountsResponse eventCounts() {
        return pilotEventService.eventCounts();
    }
}
