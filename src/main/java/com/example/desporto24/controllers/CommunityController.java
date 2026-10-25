package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityStandingsArchiveService;
import com.example.desporto24.service.CommunityHistoricalMatchPreviewService;
import com.example.desporto24.service.CommunityHistoricalMatchArchiveService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunityHistoricalPlayerLinkService;
import com.example.desporto24.service.CommunitySessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityManagementService service;
    private final CommunityCompetitionService competitionService;
    private final CommunityHistoricalPlayerLinkService historicalPlayerLinkService;
    private final CommunityImportService communityImportService;
    private final CommunityStandingsArchiveService historicalStandingsService;
    private final CommunityHistoricalMatchPreviewService historicalMatchPreviewService;
    private final CommunityHistoricalMatchArchiveService historicalMatchArchiveService;
    private final CommunitySessionService communitySessionService;

    @PostMapping
    public ResponseEntity<CommunityResponse> create(
            @RequestBody @Valid CreateCommunityRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.create(authentication.getName(), request));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CommunityResponse>> mine(Authentication authentication) {
        return ResponseEntity.ok(service.mine(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponse> get(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.get(id, authentication.getName()));
    }

    // Deliberately uses a confirmation body; never delete a group via an unsafe link.
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteCommunity(
            @PathVariable Long id,
            @RequestBody @Valid ConfirmCommunityDeletionRequest confirmation,
            Authentication authentication) {
        service.deleteCommunity(id, authentication.getName(), confirmation.name());
        return ResponseEntity.noContent().build();
    }

    public record ConfirmCommunityDeletionRequest(
            @jakarta.validation.constraints.NotBlank String name) {}

    @PostMapping("/{id}/join")
    public ResponseEntity<CommunityResponse> joinPublic(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.joinPublic(id, authentication.getName()));
    }

    @PostMapping("/{id}/join-requests")
    public ResponseEntity<CommunityJoinRequestResponse> requestJoin(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.requestJoin(id, authentication.getName()));
    }

    @GetMapping("/{id}/join-requests")
    public ResponseEntity<List<CommunityJoinRequestResponse>> pendingJoinRequests(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.pendingJoinRequests(id, authentication.getName()));
    }

    @GetMapping("/{id}/join-requests/me")
    public ResponseEntity<CommunityJoinRequestResponse> myPendingJoinRequest(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.of(service.myPendingJoinRequest(id, authentication.getName()));
    }

    @PostMapping("/{id}/join-requests/{requestId}/accept")
    public ResponseEntity<CommunityResponse> acceptJoinRequest(
            @PathVariable Long id,
            @PathVariable Long requestId,
            Authentication authentication) {
        return ResponseEntity.ok(service.acceptJoinRequest(id, requestId, authentication.getName()));
    }

    @PostMapping("/{id}/join-requests/{requestId}/reject")
    public ResponseEntity<Void> rejectJoinRequest(
            @PathVariable Long id,
            @PathVariable Long requestId,
            Authentication authentication) {
        service.rejectJoinRequest(id, requestId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/features")
    public ResponseEntity<CommunityResponse> updateFeatures(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunityFeaturesRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateFeatures(id, authentication.getName(), request));
    }

    @PutMapping("/{id}/sports")
    public ResponseEntity<CommunityResponse> updateSports(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunitySportsRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateSports(id, authentication.getName(), request));
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<CommunityResponse> updateVisibility(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunityVisibilityRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateVisibility(id, authentication.getName(), request));
    }

    @PutMapping(value = "/{id}/photo", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityResponse> updatePhoto(
            @PathVariable Long id,
            @RequestPart("photo") MultipartFile photo,
            @RequestParam(value = "fitMode", defaultValue = "COVER") String fitMode,
            @RequestParam(value = "zoom", defaultValue = "1") double zoom,
            @RequestParam(value = "focusX", defaultValue = "50") double focusX,
            @RequestParam(value = "focusY", defaultValue = "50") double focusY,
            Authentication authentication) {
        return ResponseEntity.ok(service.updatePhoto(
                id, authentication.getName(), photo, fitMode, zoom, focusX, focusY));
    }

    public record CommunityPhotoFramingRequest(
            double zoom, double focusX, double focusY, String expectedPhotoUrl, String fitMode) {}

    @PutMapping("/{id}/photo/framing")
    public ResponseEntity<CommunityResponse> updatePhotoFraming(
            @PathVariable Long id,
            @RequestBody CommunityPhotoFramingRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updatePhotoFraming(
                id, authentication.getName(), request.zoom(), request.focusX(),
                request.focusY(), request.expectedPhotoUrl(), request.fitMode()));
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<CommunityResponse> removePhoto(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.removePhoto(id, authentication.getName()));
    }

    @PostMapping("/{id}/invites")
    public ResponseEntity<CommunityInviteResponse> invite(
            @PathVariable Long id,
            @RequestBody @Valid InviteCommunityMemberRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.invite(id, authentication.getName(), request));
    }

    @GetMapping("/invites/me")
    public ResponseEntity<List<CommunityInviteResponse>> myInvites(Authentication authentication) {
        return ResponseEntity.ok(service.myInvites(authentication.getName()));
    }

    @PostMapping("/invites/{inviteId}/accept")
    public ResponseEntity<CommunityResponse> acceptInvite(
            @PathVariable Long inviteId,
            Authentication authentication) {
        return ResponseEntity.ok(service.acceptInvite(inviteId, authentication.getName()));
    }

    @PostMapping("/invites/{inviteId}/reject")
    public ResponseEntity<Void> rejectInvite(
            @PathVariable Long inviteId,
            Authentication authentication) {
        service.rejectInvite(inviteId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<SessionSummaryResponse>> sessions(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                communitySessionService.sessions(id, authentication.getName())
        );
    }

    @PostMapping("/{id}/sessions/{sessionId}/open-public")
    public ResponseEntity<SessionSummaryResponse> openSessionToPublic(
            @PathVariable Long id,
            @PathVariable Long sessionId,
            Authentication authentication) {
        return ResponseEntity.ok(
                communitySessionService.openToPublic(id, sessionId, authentication.getName())
        );
    }


    @GetMapping("/{id}/competition")
    public ResponseEntity<CommunityCompetitionOverviewResponse> competitionOverview(
            @PathVariable Long id,
            @RequestParam(required = false) Long seasonId,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.overview(id, authentication.getName(), seasonId)
        );
    }

    @PutMapping("/{id}/seasons/{seasonId}/race-rules")
    public ResponseEntity<CommunityRaceRuleResponse> updateRaceRule(
            @PathVariable Long id, @PathVariable Long seasonId,
            @RequestBody @Valid UpdateCommunityRaceRuleRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(competitionService.updateRaceRule(
                id, seasonId, authentication.getName(), request));
    }

    @PutMapping("/{id}/rounds/{roundId}/entries/{entryId}/race-penalty")
    public ResponseEntity<CommunityRoundEntryResponse> updateRacePenalty(
            @PathVariable Long id, @PathVariable Long roundId, @PathVariable Long entryId,
            @RequestBody @Valid UpdateCommunityRacePenaltyRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(competitionService.updateRacePenalty(
                id, roundId, entryId, authentication.getName(), request));
    }

    @PutMapping("/{id}/rounds/{roundId}/entries/{entryId}/race-decision")
    public ResponseEntity<CommunityRoundEntryResponse> updateRaceDecision(
            @PathVariable Long id, @PathVariable Long roundId, @PathVariable Long entryId,
            @RequestBody @Valid UpdateCommunityRaceDecisionRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(competitionService.updateRaceDecision(
                id, roundId, entryId, authentication.getName(), request));
    }

    @PutMapping("/{id}/seasons/{seasonId}/racket-rules")
    public ResponseEntity<CommunityRacketRuleResponse> updateRacketRule(
            @PathVariable Long id, @PathVariable Long seasonId,
            @RequestBody @Valid UpdateCommunityRacketRuleRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(competitionService.updateRacketRule(
                id, seasonId, authentication.getName(), request));
    }

    @GetMapping("/{id}/seasons/{seasonId}/track-sprint-brackets")
    public ResponseEntity<List<CommunityTrackSprintBracketDtos.Overview>> trackSprintBrackets(
            @PathVariable Long id, @PathVariable Long seasonId, Authentication auth) {
        return ResponseEntity.ok(competitionService.trackSprintBrackets(
                id, seasonId, auth.getName()));
    }

    @DeleteMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}")
    public ResponseEntity<Void> deleteTrackSprintBracket(
            @PathVariable Long id, @PathVariable Long seasonId,
            @PathVariable Long bracketId, Authentication auth) {
        competitionService.deleteTrackSprintBracket(
                id, seasonId, bracketId, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/seasons/{seasonId}/track-sprint-brackets")
    public ResponseEntity<CommunityTrackSprintBracketDtos.Overview> createTrackSprintBracket(
            @PathVariable Long id, @PathVariable Long seasonId,
            @RequestBody @Valid CommunityTrackSprintBracketDtos.Create request,
            Authentication auth) {
        return ResponseEntity.ok(competitionService.createTrackSprintBracket(
                id, seasonId, auth.getName(), request));
    }

    @PostMapping("/{id}/seasons")
    public ResponseEntity<CommunitySeasonResponse> createSeason(
            @PathVariable Long id,
            @RequestBody @Valid CreateCommunitySeasonRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.createSeason(id, authentication.getName(), request)
        );
    }

    @PutMapping("/{id}/seasons/{seasonId}")
    public ResponseEntity<CommunitySeasonResponse> renameSeason(
            @PathVariable Long id,
            @PathVariable Long seasonId,
            @RequestBody @Valid UpdateCommunitySeasonRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.renameSeason(id, seasonId, authentication.getName(), request)
        );
    }

    @DeleteMapping("/{id}/seasons/{seasonId}")
    public ResponseEntity<Void> deleteSeason(
            @PathVariable Long id,
            @PathVariable Long seasonId,
            Authentication authentication) {
        competitionService.deleteSeason(id, seasonId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/seasons/{seasonId}/close")
    public ResponseEntity<CommunitySeasonResponse> closeSeason(
            @PathVariable Long id,
            @PathVariable Long seasonId,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.closeSeason(id, seasonId, authentication.getName())
        );
    }

    @PutMapping("/{id}/competition/settings")
    public ResponseEntity<CommunityCompetitionSettingsResponse> updateCompetitionSettings(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunityCompetitionSettingsRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.updateSettings(id, authentication.getName(), request)
        );
    }

    @GetMapping("/{id}/competition/historical-players")
    public ResponseEntity<List<HistoricalPlayerCandidateResponse>> historicalPlayerCandidates(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.candidates(id, authentication.getName()));
    }


    @PostMapping("/{id}/competition/historical-player-links/claim")
    public ResponseEntity<HistoricalPlayerLinkResponse> claimHistoricalName(
            @PathVariable Long id, @RequestBody @Valid ClaimHistoricalPlayerRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.claimForMe(id, authentication.getName(), request.playerId()));
    }

    @GetMapping("/{id}/competition/historical-player-links/my-claims")
    public ResponseEntity<List<HistoricalPlayerLinkResponse>> myHistoricalClaims(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.myClaims(id, authentication.getName()));
    }

    @DeleteMapping("/{id}/competition/historical-player-links/my-claims/{linkId}")
    public ResponseEntity<Void> removeMyHistoricalClaim(
            @PathVariable Long id, @PathVariable Long linkId, Authentication authentication) {
        historicalPlayerLinkService.releaseMyClaim(id, linkId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/competition/import/context/validate")
    public ResponseEntity<CommunityImportContextValidationResponse> validateCompetitionImportContext(
            @PathVariable Long id,
            @RequestBody @Valid CommunityImportContextRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.validateContext(id, authentication.getName(), request)
        );
    }

    @PostMapping(value = "/{id}/competition/import/preview", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPreviewResponse> previewCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.preview(id, authentication.getName(), file)
        );
    }

    @PostMapping(value = "/{id}/competition/import/validate", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportMappingValidationResponse> validateCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.validateMapping(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/normalized-preview", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportNormalizedPreviewResponse> normalizedCompetitionImportPreview(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.normalizedPreview(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPlanResponse> planCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.importPlan(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/rounds/plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportRoundsPlanResponse> planCompetitionImportRounds(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.roundsPlan(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/rounds/final-plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportRoundsPlanResponse> finalCompetitionImportRoundsPlan(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityImportContextRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.finalRoundsPlan(id, authentication.getName(), file, mapping, context)
        );
    }

    @PostMapping(value = "/{id}/competition/import/rounds/confirm", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportRoundsResultResponse> confirmCompetitionImportRounds(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityImportContextRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.confirmRounds(id, authentication.getName(), file, mapping, context)
        );
    }

    @PostMapping(value = "/{id}/competition/import/players/confirm", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPlayersResultResponse> confirmCompetitionImportPlayers(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.confirmPlayers(id, authentication.getName(), file, mapping)
        );
    }

    /**
     * Safe read-only first step for importing real historical tournament matches.
     * No match, participant, team or tournament is created by this endpoint.
     */
    @PostMapping(value = "/{id}/competition/import/matches/preview",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityHistoricalMatchPreviewResponse> previewHistoricalMatches(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(historicalMatchPreviewService.preview(id, authentication.getName(), file, mapping));
    }

    /** Preserve real old fixtures in a separate archive, never a live tournament. */
    @PostMapping(value = "/{id}/competition/import/matches/confirm",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityHistoricalMatchArchiveResponse> confirmHistoricalMatches(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityHistoricalMatchArchiveConfirmRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(historicalMatchArchiveService.confirm(
                id, authentication.getName(), file, mapping, context));
    }

    @GetMapping("/{id}/competition/match-archives")
    public ResponseEntity<List<CommunityHistoricalMatchArchiveSummaryResponse>> historicalMatchArchives(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalMatchArchiveService.list(id, authentication.getName()));
    }

    @GetMapping("/{id}/competition/match-archives/{archiveId}")
    public ResponseEntity<CommunityHistoricalMatchArchiveResponse> historicalMatchArchive(
            @PathVariable Long id, @PathVariable Long archiveId, Authentication authentication) {
        return ResponseEntity.ok(historicalMatchArchiveService.detail(
                id, archiveId, authentication.getName()));
    }

    /**
     * Archived rankings do not invent matches or alter the live standings.
     * The same workbook and mapping must be sent to preview and confirmation.
     */
    @PostMapping(value = "/{id}/competition/import/standings/preview",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityStandingsArchivePreviewResponse> previewHistoricalStandings(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.preview(id, authentication.getName(), file, mapping));
    }

    @PostMapping(value = "/{id}/competition/import/standings/confirm",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityStandingsArchiveResponse> confirmHistoricalStandings(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityStandingsArchiveConfirmRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.confirm(id, authentication.getName(), file, mapping, context));
    }

    @GetMapping("/{id}/competition/standings-archives")
    public ResponseEntity<List<CommunityStandingsArchiveSummaryResponse>> historicalStandingsArchives(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.list(id, authentication.getName()));
    }

    @GetMapping("/{id}/competition/standings-archives/{archiveId}")
    public ResponseEntity<CommunityStandingsArchiveResponse> historicalStandingsArchive(
            @PathVariable Long id, @PathVariable Long archiveId, Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.detail(id, archiveId, authentication.getName()));
    }

    @PostMapping("/{id}/rounds")
    public ResponseEntity<CommunityRoundResponse> createRound(
            @PathVariable Long id,
            @RequestBody @Valid CreateCommunityRoundRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.createRound(id, authentication.getName(), request)
        );
    }

    @DeleteMapping("/{id}/rounds/{roundId}")
    public ResponseEntity<Void> deleteRound(
            @PathVariable Long id,
            @PathVariable Long roundId,
            Authentication authentication) {
        competitionService.deleteRound(id, roundId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/members/{username}/role")
    public ResponseEntity<CommunityResponse> updateRole(
            @PathVariable Long id,
            @PathVariable String username,
            @RequestBody @Valid UpdateCommunityRoleRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateRole(
                id,
                authentication.getName(),
                username,
                request
        ));
    }

    @DeleteMapping("/{id}/members/{username}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long id,
            @PathVariable String username,
            Authentication authentication) {
        service.removeMember(id, authentication.getName(), username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leave(
            @PathVariable Long id,
            Authentication authentication) {
        service.leave(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/owner")
    public ResponseEntity<CommunityResponse> transferOwnership(
            @PathVariable Long id,
            @RequestBody @Valid TransferOwnershipRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.transferOwnership(
                id,
                authentication.getName(),
                request
        ));
    }
}
