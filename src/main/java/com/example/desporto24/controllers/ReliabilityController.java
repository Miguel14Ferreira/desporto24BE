package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.reliability.AttendanceDisputeRequest;
import com.example.desporto24.model.dto.reliability.AttendanceUpdateRequest;
import com.example.desporto24.model.dto.reliability.DisputeResolutionRequest;
import com.example.desporto24.model.dto.reliability.ReliabilitySummaryResponse;
import com.example.desporto24.service.ReliabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReliabilityController {

    private final ReliabilityService reliabilityService;

    @PutMapping("/sessions/{sessionId}/participants/{participantId}/attendance")
    public ResponseEntity<Void> updateAttendance(
            @PathVariable Long sessionId,
            @PathVariable Long participantId,
            @RequestBody @Valid AttendanceUpdateRequest request,
            Authentication authentication) {

        reliabilityService.recordAttendance(
                sessionId,
                participantId,
                request.status(),
                authentication.getName());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attendance-disputes")
    public ResponseEntity<Void> createDispute(
            @RequestBody @Valid AttendanceDisputeRequest request,
            Authentication authentication) {

        reliabilityService.createDispute(
                request.participantId(),
                request.reason(),
                authentication.getName());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/attendance-disputes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resolveDispute(
            @PathVariable Long id,
            @RequestBody @Valid DisputeResolutionRequest request) {

        reliabilityService.resolveDispute(id, request.status());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profiles/{username}/reliability")
    public ReliabilitySummaryResponse summary(
            @PathVariable String username) {

        return reliabilityService.summaryByUsername(username);
    }
}
