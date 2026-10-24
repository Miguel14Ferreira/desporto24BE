package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.VenueSuggestionPageResponse;
import com.example.desporto24.model.dto.venue.VenueSuggestionResponse;
import com.example.desporto24.service.VenueSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/venue-suggestions")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminVenueSuggestionController {
    private final VenueSuggestionService suggestions;

    @GetMapping
    public VenueSuggestionPageResponse pending(@RequestParam(defaultValue = "0") int page) {
        return suggestions.pending(page);
    }

    @PostMapping("/{id}/approve")
    public VenueSuggestionResponse approve(@PathVariable long id, Authentication auth) {
        return suggestions.approve(id, auth.getName());
    }

    @PostMapping("/{id}/reject")
    public VenueSuggestionResponse reject(@PathVariable long id, Authentication auth) {
        return suggestions.reject(id, auth.getName());
    }
}
