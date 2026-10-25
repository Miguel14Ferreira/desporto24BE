package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.*;
import com.example.desporto24.service.VenueEditSuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venues/{venueId}/edit-suggestions")
public class VenueEditSuggestionController {
    private final VenueEditSuggestionService suggestions;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public VenueEditSuggestionResponse submit(@PathVariable Long venueId,
            @Valid @RequestBody VenueEditSuggestionRequest request, Authentication authentication) {
        return suggestions.submit(venueId, request, authentication.getName());
    }

    /** Only the authenticated submitter sees their own previous proposal. */
    @GetMapping("/mine")
    public VenueEditSuggestionResponse mine(@PathVariable Long venueId, Authentication authentication) {
        return suggestions.mine(venueId, authentication.getName());
    }
}
