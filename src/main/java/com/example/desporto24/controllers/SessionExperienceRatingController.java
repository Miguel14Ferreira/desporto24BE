package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.session.SessionExperienceRatingRequest;
import com.example.desporto24.model.dto.session.SessionExperienceRatingResponse;
import com.example.desporto24.service.SessionExperienceRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions/{sessionId}/ratings")
public class SessionExperienceRatingController {

    private final SessionExperienceRatingService service;

    @PutMapping("/me")
    public SessionExperienceRatingResponse rate(
            @PathVariable Long sessionId,
            @RequestBody @Valid SessionExperienceRatingRequest request,
            Authentication authentication) {
        return service.rate(sessionId, authentication.getName(), request);
    }

    @GetMapping("/me")
    public ResponseEntity<SessionExperienceRatingResponse> mine(
            @PathVariable Long sessionId, Authentication authentication) {
        return service.mine(sessionId, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
