package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.session.PlayerComplimentRequest;
import com.example.desporto24.model.dto.session.PlayerComplimentResponse;
import com.example.desporto24.service.SessionPlayerComplimentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Authenticated, session-scoped compliments with no public profiles or rankings. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions/{sessionId}/compliments")
public class SessionPlayerComplimentController {
    private final SessionPlayerComplimentService service;

    @PutMapping("/players/{recipientId}/me")
    public PlayerComplimentResponse compliment(
            @PathVariable Long sessionId,
            @PathVariable Long recipientId,
            @RequestBody @Valid PlayerComplimentRequest request,
            Authentication authentication) {
        return service.give(sessionId, recipientId, authentication.getName(), request);
    }

    @GetMapping("/me")
    public List<PlayerComplimentResponse> sent(
            @PathVariable Long sessionId, Authentication authentication) {
        return service.sent(sessionId, authentication.getName());
    }

    @GetMapping("/received/me")
    public List<PlayerComplimentResponse> received(
            @PathVariable Long sessionId, Authentication authentication) {
        return service.received(sessionId, authentication.getName());
    }
}
