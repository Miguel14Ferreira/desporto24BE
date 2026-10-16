package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantsPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentRequest;
import com.example.desporto24.service.CommunityTournamentService;
import com.example.desporto24.service.CommunityTournamentScheduleService;
import com.example.desporto24.service.CommunityTournamentResultService;
import com.example.desporto24.service.CommunityTournamentStandingsService;
import com.example.desporto24.model.dto.community.CommunityTournamentStandingsResponse;
import com.example.desporto24.model.dto.community.RecordCommunityTournamentMatchResultRequest;
import com.example.desporto24.model.dto.community.CommunityTournamentMatchResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentScheduleSummaryResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentMatchesPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentController {
    private final CommunityTournamentService tournamentService;
    private final CommunityTournamentScheduleService scheduleService;
    private final CommunityTournamentResultService resultService;
    private final CommunityTournamentStandingsService standingsService;

    @PostMapping
    public ResponseEntity<CommunityTournamentResponse> create(
            @PathVariable Long communityId,
            @RequestBody @Valid CreateCommunityTournamentRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.create(communityId, authentication.getName(), request));
    }

    @PutMapping("/{tournamentId}/participants/me")
    public ResponseEntity<CommunityTournamentParticipantResponse> join(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.join(
                communityId, tournamentId, authentication.getName()));
    }

    @PutMapping("/{tournamentId}/participants/{profileId}/confirm")
    public ResponseEntity<CommunityTournamentParticipantResponse> confirmParticipant(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @PathVariable Long profileId,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.confirmParticipant(
                communityId, tournamentId, profileId, authentication.getName()));
    }

    @DeleteMapping("/{tournamentId}/participants/me")
    public ResponseEntity<Void> leave(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            Authentication authentication) {
        tournamentService.leave(communityId, tournamentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tournamentId}/participants/{profileId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @PathVariable Long profileId,
            Authentication authentication) {
        tournamentService.removeParticipant(communityId, tournamentId, profileId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tournamentId}/participants")
    public ResponseEntity<CommunityTournamentParticipantsPageResponse> participants(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.participants(
                communityId, tournamentId, authentication.getName(), page));
    }

    @PostMapping("/{tournamentId}/schedule")
    public ResponseEntity<CommunityTournamentScheduleSummaryResponse> generateSchedule(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            Authentication authentication) {
        return ResponseEntity.ok(scheduleService.generate(communityId, tournamentId, authentication.getName()));
    }

    @PutMapping("/{tournamentId}/matches/{matchId}/result")
    public ResponseEntity<CommunityTournamentMatchResponse> recordMatchResult(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @PathVariable Long matchId,
            @RequestBody @Valid RecordCommunityTournamentMatchResultRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(resultService.recordResult(
                communityId, tournamentId, matchId, authentication.getName(), request));
    }

    @GetMapping("/{tournamentId}/standings")
    public ResponseEntity<CommunityTournamentStandingsResponse> standings(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            Authentication authentication) {
        return ResponseEntity.ok(standingsService.standings(
                communityId, tournamentId, authentication.getName()));
    }

    @GetMapping("/{tournamentId}/matches")
    public ResponseEntity<CommunityTournamentMatchesPageResponse> matches(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return ResponseEntity.ok(scheduleService.matches(communityId, tournamentId, authentication.getName(), page));
    }

    @GetMapping
    public ResponseEntity<CommunityTournamentPageResponse> list(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.list(communityId, authentication.getName(), page));
    }
}
