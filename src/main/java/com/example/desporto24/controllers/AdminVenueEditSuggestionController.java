package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.*;
import com.example.desporto24.service.VenueEditSuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/venue-edit-suggestions")
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class AdminVenueEditSuggestionController {
    private final VenueEditSuggestionService suggestions;

    @GetMapping
    public VenueEditSuggestionPageResponse pending(@RequestParam(defaultValue = "0") int page) {
        return suggestions.pending(page);
    }

    @PostMapping("/{id}/approve")
    public VenueEditSuggestionResponse approve(@PathVariable long id,
            @Valid @RequestBody VenueEditReviewRequest request, Authentication auth) {
        return suggestions.approve(id, request.note(), auth.getName(), Boolean.TRUE.equals(request.sourceReviewed()));
    }

    @PostMapping("/{id}/reject")
    public VenueEditSuggestionResponse reject(@PathVariable long id,
            @Valid @RequestBody VenueEditReviewRequest request, Authentication auth) {
        return suggestions.reject(id, request.note(), auth.getName());
    }
}
