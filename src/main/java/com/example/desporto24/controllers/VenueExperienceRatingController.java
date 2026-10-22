package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.VenueExperienceRatingRequest;
import com.example.desporto24.model.dto.venue.VenueExperienceRatingResponse;
import com.example.desporto24.model.dto.venue.VenueRatingSummaryResponse;
import com.example.desporto24.service.VenueExperienceRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Readable public aggregate; each authenticated author's own vote stays private. */
@RestController
@RequiredArgsConstructor
public class VenueExperienceRatingController {
    private final VenueExperienceRatingService service;

    @PutMapping("/api/sessions/{sessionId}/venue-rating/me")
    public VenueExperienceRatingResponse rate(
            @PathVariable Long sessionId,
            @RequestBody @Valid VenueExperienceRatingRequest request,
            Authentication authentication) {
        return service.rate(sessionId, authentication.getName(), request);
    }

    @GetMapping("/api/sessions/{sessionId}/venue-rating/me")
    public ResponseEntity<VenueExperienceRatingResponse> mine(
            @PathVariable Long sessionId, Authentication authentication) {
        return service.mine(sessionId, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/api/venues/{venueId}/ratings/summary")
    public VenueRatingSummaryResponse summary(@PathVariable Long venueId) {
        return service.summary(venueId);
    }
}
