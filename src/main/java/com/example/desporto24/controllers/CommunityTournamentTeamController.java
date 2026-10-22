package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.AssignCommunityTournamentTeamPlayerRequest;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentTeamRequest;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamScheduleSummaryResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamMatchesPageResponse;
import com.example.desporto24.service.CommunityTournamentTeamScheduleService;
import com.example.desporto24.service.CommunityTournamentTeamResultService;
import com.example.desporto24.service.CommunityTournamentTeamStandingsService;
import com.example.desporto24.model.dto.community.RecordCommunityTournamentMatchResultRequest;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamMatchResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamStandingsResponse;
import com.example.desporto24.service.CommunityTournamentTeamService;
import com.example.desporto24.service.CommunityTournamentTeamGroupsService;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamGroupsResponse;
import com.example.desporto24.service.CommunityTournamentTeamFinalizationService;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
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
    private final CommunityTournamentTeamScheduleService scheduleService;
    private final CommunityTournamentTeamFinalizationService finalizationService;
    private final CommunityTournamentTeamResultService resultService;
    private final CommunityTournamentTeamStandingsService standingsService;
    private final CommunityTournamentTeamGroupsService groupsService;

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


    @PostMapping("/schedule")
    public CommunityTournamentTeamScheduleSummaryResponse generateSchedule(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return scheduleService.generate(communityId, tournamentId, authentication.getName());
    }

    @GetMapping("/matches")
    public CommunityTournamentTeamMatchesPageResponse matches(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return scheduleService.matches(communityId, tournamentId, authentication.getName(), page);
    }

    @PostMapping("/matches/{matchId}/result")
    public CommunityTournamentTeamMatchResponse recordResult(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long matchId,
            @RequestBody @Valid RecordCommunityTournamentMatchResultRequest request,
            Authentication authentication) {
        return resultService.record(communityId, tournamentId, matchId,
                authentication.getName(), request);
    }

    @PostMapping("/complete")
    public CommunityTournamentResponse complete(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return finalizationService.complete(communityId, tournamentId, authentication.getName());
    }

    @GetMapping("/standings")
    public CommunityTournamentTeamStandingsResponse standings(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return standingsService.standings(communityId, tournamentId, authentication.getName());
    }

    @GetMapping("/groups")
    public CommunityTournamentTeamGroupsResponse groups(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication authentication) {
        return groupsService.standings(communityId, tournamentId, authentication.getName());
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
