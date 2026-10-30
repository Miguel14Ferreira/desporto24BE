package com.example.desporto24.controllers;

import com.example.desporto24.service.DirectCallService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Authenticated call signalling. LiveKit credentials never reach the frontend. */
@RestController
@RequestMapping("/api/calls/direct")
@RequiredArgsConstructor
public class DirectCallController {
    private final DirectCallService service;

    public record StartRequest(@NotBlank String media) {}

    @GetMapping("/availability")
    public java.util.Map<String, Object> availability() {
        String status = service.providerStatus();
        return java.util.Map.of("enabled", "ready".equals(status), "status", status);
    }

    /** Allows the recipient to recover a missed RINGING WebSocket event. */
    @GetMapping("/pending")
    public DirectCallService.PendingInvitation pending(Authentication auth) {
        return service.pendingFor(auth.getName());
    }

    @PostMapping("/{peer}")
    public DirectCallService.CallSession start(@PathVariable String peer,
            @RequestBody @Valid StartRequest request, Authentication auth) {
        return service.start(auth.getName(), peer, request.media());
    }

    @PostMapping("/{callId}/accept")
    public DirectCallService.CallSession accept(@PathVariable UUID callId, Authentication auth) {
        return service.accept(callId, auth.getName());
    }

    /** Additional participant must accept an invitation before joining. */
    @PostMapping("/{callId}/invite/{username}")
    public void invite(@PathVariable UUID callId, @PathVariable String username,
            Authentication auth) {
        service.invite(callId, auth.getName(), username);
    }

    @GetMapping("/{callId}/members")
    public DirectCallService.CallMembers members(@PathVariable UUID callId,
            Authentication auth) {
        return service.members(callId, auth.getName());
    }

    @PostMapping("/{callId}/decline")
    public void decline(@PathVariable UUID callId, Authentication auth) {
        service.decline(callId, auth.getName());
    }

    @PostMapping("/{callId}/end")
    public void end(@PathVariable UUID callId, Authentication auth) {
        service.end(callId, auth.getName());
    }
}
