package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentRaceOverviewResponse;
import com.example.desporto24.service.CommunityTournamentRaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/races")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentRaceController {
    private final CommunityTournamentRaceService raceService;

    public record RacePointsRequest(@NotBlank String pointsByPlace) {}

    @GetMapping
    public CommunityTournamentRaceOverviewResponse overview(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return raceService.overview(communityId, tournamentId, authentication.getName());
    }

    @PostMapping("/complete")
    public com.example.desporto24.model.dto.community.CommunityTournamentResponse complete(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return raceService.complete(communityId, tournamentId, authentication.getName());
    }

    @PutMapping("/points")
    public CommunityTournamentRaceOverviewResponse updatePoints(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @RequestBody @Valid RacePointsRequest request, Authentication authentication) {
        return raceService.changePoints(
                communityId, tournamentId, authentication.getName(), request.pointsByPlace());
    }

    @PutMapping("/{roundId}")
    public CommunityTournamentRaceOverviewResponse link(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long roundId, Authentication authentication) {
        return raceService.link(communityId, tournamentId, roundId, authentication.getName());
    }

    @DeleteMapping("/{roundId}")
    public CommunityTournamentRaceOverviewResponse unlink(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long roundId, Authentication authentication) {
        return raceService.unlink(communityId, tournamentId, roundId, authentication.getName());
    }
}
