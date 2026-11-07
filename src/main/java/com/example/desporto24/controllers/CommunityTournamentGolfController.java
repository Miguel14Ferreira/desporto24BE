package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentGolfOverviewResponse;
import com.example.desporto24.service.CommunityTournamentGolfService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/golf")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentGolfController {
    private final CommunityTournamentGolfService golf;

    @GetMapping
    public CommunityTournamentGolfOverviewResponse overview(@PathVariable Long communityId,
            @PathVariable Long tournamentId, Authentication authentication) {
        return golf.overview(communityId, tournamentId, authentication.getName());
    }

    @PutMapping("/{roundId}")
    public CommunityTournamentGolfOverviewResponse link(@PathVariable Long communityId,
            @PathVariable Long tournamentId, @PathVariable Long roundId, Authentication authentication) {
        return golf.link(communityId, tournamentId, roundId, authentication.getName());
    }

    @DeleteMapping("/{roundId}")
    public CommunityTournamentGolfOverviewResponse unlink(@PathVariable Long communityId,
            @PathVariable Long tournamentId, @PathVariable Long roundId, Authentication authentication) {
        return golf.unlink(communityId, tournamentId, roundId, authentication.getName());
    }
}
