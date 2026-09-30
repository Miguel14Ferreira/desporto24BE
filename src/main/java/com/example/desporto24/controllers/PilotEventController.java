package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.analytics.PilotAccessEventRequest;
import com.example.desporto24.service.PilotEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pilot/events")
public class PilotEventController {

    private final PilotEventService pilotEventService;

    @PostMapping("/access")
    public ResponseEntity<Void> recordAccess(
            @RequestBody @Valid PilotAccessEventRequest request) {
        pilotEventService.recordFirstAccess(request.deviceId());
        return ResponseEntity.accepted().build();
    }
}
