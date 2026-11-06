package com.example.desporto24.controllers;

import com.example.desporto24.service.CommunityTournamentEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Private pre-match report for the tournament's community owner and admins. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/eligibility")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentEligibilityController {
    private final CommunityTournamentEligibilityService service;

    @GetMapping
    public CommunityTournamentEligibilityService.EligibilityOverview overview(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return service.overview(communityId, tournamentId, authentication.getName());
    }
}
