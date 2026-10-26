package com.example.desporto24.controllers;

import com.example.desporto24.service.DirectCallService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Authenticated, one-to-one call signalling. LiveKit credentials never reach the frontend. */
@RestController
@RequestMapping("/api/calls/direct")
@RequiredArgsConstructor
public class DirectCallController {
    private final DirectCallService service;

    public record StartRequest(@NotBlank String media) {}

    @GetMapping("/availability")
    public java.util.Map<String, Boolean> availability() {
        return java.util.Map.of("enabled", service.available());
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

    @PostMapping("/{callId}/decline")
    public void decline(@PathVariable UUID callId, Authentication auth) {
        service.decline(callId, auth.getName());
    }

    @PostMapping("/{callId}/end")
    public void end(@PathVariable UUID callId, Authentication auth) {
        service.end(callId, auth.getName());
    }
}
