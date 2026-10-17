package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.AssignCommunityTournamentTeamPlayerRequest;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentTeamRequest;
import com.example.desporto24.service.CommunityTournamentTeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/teams")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentTeamController {
    private final CommunityTournamentTeamService service;

    @GetMapping
    public List<CommunityTournamentTeamResponse> list(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return service.list(communityId, tournamentId, authentication.getName());
    }

    @PostMapping
    public ResponseEntity<CommunityTournamentTeamResponse> create(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @RequestBody @Valid CreateCommunityTournamentTeamRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTeam(
                communityId, tournamentId, authentication.getName(), request.name()));
    }

    @PostMapping("/{teamId}/players")
    public ResponseEntity<CommunityTournamentTeamResponse> addPlayer(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long teamId,
            @RequestBody @Valid AssignCommunityTournamentTeamPlayerRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.assignPlayer(
                communityId, tournamentId, teamId, authentication.getName(), request.profileId()));
    }

    @DeleteMapping("/{teamId}/players/{profileId}")
    public ResponseEntity<Void> removePlayer(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long teamId, @PathVariable Long profileId,
            Authentication authentication) {
        service.removePlayer(communityId, tournamentId, teamId, authentication.getName(), profileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long teamId, Authentication authentication) {
        service.deleteTeam(communityId, tournamentId, teamId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
