package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentMeasuredOverviewResponse;
import com.example.desporto24.service.CommunityTournamentMeasuredService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/marks")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentMeasuredController {
    private final CommunityTournamentMeasuredService measured;

    @GetMapping
    public CommunityTournamentMeasuredOverviewResponse overview(
            @PathVariable Long communityId, @PathVariable Long tournamentId, Authentication authentication) {
        return measured.overview(communityId,tournamentId,authentication.getName());
    }

    @PutMapping("/{roundId}")
    public CommunityTournamentMeasuredOverviewResponse link(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long roundId,Authentication authentication) {
        return measured.link(communityId,tournamentId,roundId,authentication.getName());
    }

    @DeleteMapping("/{roundId}")
    public CommunityTournamentMeasuredOverviewResponse unlink(
            @PathVariable Long communityId,@PathVariable Long tournamentId,
            @PathVariable Long roundId,Authentication authentication) {
        return measured.unlink(communityId,tournamentId,roundId,authentication.getName());
    }
}
