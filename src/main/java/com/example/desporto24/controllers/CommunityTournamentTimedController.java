package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentTimedOverviewResponse;
import com.example.desporto24.service.CommunityTournamentTimedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/times")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentTimedController {
    private final CommunityTournamentTimedService timed;

    @GetMapping
    public CommunityTournamentTimedOverviewResponse overview(
            @PathVariable Long communityId, @PathVariable Long tournamentId, Authentication authentication) {
        return timed.overview(communityId,tournamentId,authentication.getName());
    }

    @PutMapping("/{roundId}")
    public CommunityTournamentTimedOverviewResponse link(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long roundId,Authentication authentication) {
        return timed.link(communityId,tournamentId,roundId,authentication.getName());
    }

    @DeleteMapping("/{roundId}")
    public CommunityTournamentTimedOverviewResponse unlink(
            @PathVariable Long communityId,@PathVariable Long tournamentId,
            @PathVariable Long roundId,Authentication authentication) {
        return timed.unlink(communityId,tournamentId,roundId,authentication.getName());
    }
}
