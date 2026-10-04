package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.analytics.PilotAcquisitionResponse;
import com.example.desporto24.model.dto.analytics.PilotActivation7dResponse;
import com.example.desporto24.model.dto.analytics.PilotAutonomyResponse;
import com.example.desporto24.model.dto.analytics.PilotEventCountsResponse;
import com.example.desporto24.model.dto.analytics.PilotParticipationResponse;
import com.example.desporto24.model.dto.analytics.PilotRealizationResponse;
import com.example.desporto24.model.dto.analytics.PilotRegistrationResponse;
import com.example.desporto24.model.dto.analytics.PilotRecurringSupplyResponse;
import com.example.desporto24.model.dto.analytics.PilotRepetition30dResponse;
import com.example.desporto24.service.PilotEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/pilot")
public class AdminPilotMetricsController {

    private final PilotEventService pilotEventService;

    @GetMapping("/acquisition")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotAcquisitionResponse acquisition(@RequestParam String cohort) {
        return pilotEventService.acquisition(cohort);
    }

    @GetMapping("/registration")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotRegistrationResponse registration(@RequestParam String cohort) {
        return pilotEventService.registration(cohort);
    }

    @GetMapping("/activation-7d")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotActivation7dResponse activation7d() {
        return pilotEventService.activation7d();
    }

    @GetMapping("/repetition-30d")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotRepetition30dResponse repetition30d() {
        return pilotEventService.repetition30d();
    }

    @GetMapping("/participation")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotParticipationResponse participation() {
        return pilotEventService.participation();
    }

    @GetMapping("/realization")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotRealizationResponse realization() {
        return pilotEventService.realization();
    }

    @GetMapping("/recurring-supply")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotRecurringSupplyResponse recurringSupply() {
        return pilotEventService.recurringSupply();
    }

    @PostMapping("/sessions/{sessionId}/operational-intervention")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void recordOperationalIntervention(@PathVariable Long sessionId) {
        pilotEventService.recordOperationalIntervention(sessionId);
    }

    @GetMapping("/autonomy")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotAutonomyResponse autonomy() {
        return pilotEventService.autonomy();
    }

    @GetMapping("/event-counts")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public PilotEventCountsResponse eventCounts() {
        return pilotEventService.eventCounts();
    }
}
