package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityStandingsArchiveService;
import com.example.desporto24.service.CommunityHistoricalMatchPreviewService;
import com.example.desporto24.service.CommunityHistoricalMatchArchiveService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunityCreationQuotaService;
import com.example.desporto24.service.CommunityHistoricalPlayerLinkService;
import com.example.desporto24.service.CommunitySessionService;
import com.example.desporto24.service.CommunitySessionResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import com.example.desporto24.domain.CommunityImportHttpResponse;

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
    private final CommunitySessionResultService communitySessionResultService;

    @PostMapping
    public ResponseEntity<CommunityResponse> create(
            @RequestBody @Valid CreateCommunityRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.create(authentication.getName(), request));
    }

    @GetMapping("/creation-quota")
    public CommunityCreationQuotaService.Quota creationQuota(Authentication authentication) {
        return service.creationQuota(authentication.getName());
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

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/interruption")
    public ResponseEntity<CommunityTrackSprintBracketDtos.Overview> endTrackSprintCompetition(
            @PathVariable Long id, @PathVariable Long seasonId, @PathVariable Long bracketId,
            @RequestBody @Valid CommunityTrackSprintBracketDtos.EndEvent request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.endTrackSprintCompetition(
                    id,seasonId,bracketId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @DeleteMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}")
    public ResponseEntity<Void> deleteTrackSprintBracket(
            @PathVariable Long id, @PathVariable Long seasonId,
            @PathVariable Long bracketId, Authentication auth) {
        competitionService.deleteTrackSprintBracket(
                id, seasonId, bracketId, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> reportTrackSprintIncident(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.Report request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.reportTrackSprintIncident(
                    id,seasonId,bracketId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/decision")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> decideTrackSprintIncident(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.Decide request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.decideTrackSprintIncident(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/official-positions")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> ratifySprintIncidentPositions(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.RatifyIncidentPositions request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.ratifySprintIncidentPositions(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/unintentional-fall-start")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> confirmUnintentionalFallStart(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.UnintentionalFallRestart request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.confirmUnintentionalFallStart(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/pre-sprint-start")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> confirmPreSprintStart(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.PreSprintRestart request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.confirmPreSprintStart(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/flagrant-ruling")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> ruleFlagrantSprintIncident(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.FlagrantRuling request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.ruleFlagrantSprintIncident(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/flagrant-no-sanction-start")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> confirmFlagrantNoSanctionStart(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.FlagrantNoSanctionStart request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.confirmFlagrantNoSanctionStart(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/neutral-restart")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> authorizeNeutralSprintRestart(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.NeutralRestart request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.authorizeNeutralSprintRestart(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/rerun")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> authorizeTrackSprintRerun(
            @PathVariable Long id, @PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.AuthorizeRerun request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.authorizeTrackSprintRerun(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/official-award")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> confirmTwoUpOfficialAward(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.ConfirmOfficialAward request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.confirmTwoUpOfficialAward(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/official-series-verdict")
    public ResponseEntity<CommunityTrackSprintIncidentDtos.View> confirmBo3DisqualificationVerdict(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintIncidentDtos.ConfirmOfficialAward request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.confirmBo3DisqualificationVerdict(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @GetMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/reviews")
    public ResponseEntity<List<CommunityTrackSprintReviewDtos.View>> sprintVerdictReviews(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,Authentication auth) {
        return ResponseEntity.ok(competitionService.sprintVerdictReviews(
                id,seasonId,bracketId,incidentId,auth.getName()));
    }

    @PostMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/reviews")
    public ResponseEntity<CommunityTrackSprintReviewDtos.View> requestSprintVerdictReview(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintReviewDtos.Request request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.requestSprintVerdictReview(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/reviews/decision")
    public ResponseEntity<CommunityTrackSprintReviewDtos.View> resolveSprintVerdictReview(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintReviewDtos.Resolve request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.resolveSprintVerdictReview(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PutMapping("/{id}/seasons/{seasonId}/track-sprint-brackets/{bracketId}/incidents/{incidentId}/reviews/rectification")
    public ResponseEntity<CommunityTrackSprintReviewDtos.View> rectifyOverturnedSprintVerdict(
            @PathVariable Long id,@PathVariable Long seasonId,@PathVariable Long bracketId,
            @PathVariable Long incidentId,
            @RequestBody @Valid CommunityTrackSprintReviewDtos.Rectify request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.rectifyOverturnedSprintVerdict(
                    id,seasonId,bracketId,incidentId,auth.getName(),request));
        } catch(IllegalArgumentException invalid) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,invalid.getMessage(),invalid);
        }
    }

    @PostMapping("/{id}/seasons/{seasonId}/track-sprint-brackets")
    public ResponseEntity<CommunityTrackSprintBracketDtos.Overview> createTrackSprintBracket(
            @PathVariable Long id, @PathVariable Long seasonId,
            @RequestBody @Valid CommunityTrackSprintBracketDtos.Create request,
            Authentication auth) {
        try {
            return ResponseEntity.ok(competitionService.createTrackSprintBracket(
                    id, seasonId, auth.getName(), request));
        } catch (IllegalArgumentException invalidOfficialSource) {
            // Bad source timing / missing commissaires' evidence is a client
            // validation error, never a generic HTTP 500.
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    invalidOfficialSource.getMessage(), invalidOfficialSource);
        }
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
                withImportHttpError(() -> communityImportService.validateContext(id, authentication.getName(), request))
        );
    }

    @PostMapping(value = "/{id}/competition/import/preview", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPreviewResponse> previewCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) {
        return ResponseEntity.ok(
                withImportHttpError(() -> communityImportService.preview(id, authentication.getName(), file))
        );
    }

    @PostMapping(value = "/{id}/competition/import/validate", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportMappingValidationResponse> validateCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                withImportHttpError(() -> communityImportService.validateMapping(id, authentication.getName(), file, mapping))
        );
    }

    @PostMapping(value = "/{id}/competition/import/normalized-preview", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportNormalizedPreviewResponse> normalizedCompetitionImportPreview(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                withImportHttpError(() -> communityImportService.normalizedPreview(id, authentication.getName(), file, mapping))
        );
    }

    @PostMapping(value = "/{id}/competition/import/plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPlanResponse> planCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                withImportHttpError(() -> communityImportService.importPlan(id, authentication.getName(), file, mapping))
        );
    }

    @PostMapping(value = "/{id}/competition/import/rounds/plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportRoundsPlanResponse> planCompetitionImportRounds(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                withImportHttpError(() -> communityImportService.roundsPlan(id, authentication.getName(), file, mapping))
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
                withImportHttpError(() -> communityImportService.finalRoundsPlan(id, authentication.getName(), file, mapping, context))
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
                withImportHttpError(() -> communityImportService.confirmRounds(id, authentication.getName(), file, mapping, context))
        );
    }

    @PostMapping(value = "/{id}/competition/import/players/confirm", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPlayersResultResponse> confirmCompetitionImportPlayers(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                withImportHttpError(() -> communityImportService.confirmPlayers(id, authentication.getName(), file, mapping))
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
        return ResponseEntity.ok(withImportHttpError(() -> historicalMatchPreviewService.preview(id, authentication.getName(), file, mapping)));
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
        return ResponseEntity.ok(withImportHttpError(() -> historicalMatchArchiveService.confirm(
                id, authentication.getName(), file, mapping, context)));
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
        return ResponseEntity.ok(withImportHttpError(() -> historicalStandingsService.preview(id, authentication.getName(), file, mapping)));
    }

    @PostMapping(value = "/{id}/competition/import/standings/confirm",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityStandingsArchiveResponse> confirmHistoricalStandings(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityStandingsArchiveConfirmRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(withImportHttpError(() -> historicalStandingsService.confirm(id, authentication.getName(), file, mapping, context)));
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

    /**
     * Fast post-game result only for sessions owned by this community and with
     * community standings enabled. Never infer players, winners or score.
     */
    @PostMapping("/{id}/sessions/{sessionId}/competition-result")
    public ResponseEntity<CommunityRoundResponse> recordCommunitySessionResult(
            @PathVariable Long id, @PathVariable Long sessionId,
            @RequestBody @Valid CommunitySessionResultRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(communitySessionResultService.record(
                id, sessionId, authentication.getName(), request));
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

    /**
     * Apply HTTP validation semantics only to historical spreadsheet import
     * operations. Other community endpoints keep their existing handlers.
     * Authorization and unexpected failures still use the normal global flow.
     */
    private <T> T withImportHttpError(Supplier<T> operation) {
        try {
            return operation.get();
        } catch (IllegalArgumentException invalidImport) {
            throw new ImportValidationFailure(invalidImport.getMessage());
        }
    }

    private static final class ImportValidationFailure extends RuntimeException {
        private ImportValidationFailure(String legacyMessage) {
            super(legacyMessage);
        }
    }

    @ExceptionHandler(ImportValidationFailure.class)
    public ResponseEntity<CommunityImportHttpResponse> invalidSpreadsheetImport(ImportValidationFailure failure) {
        String message = failure.getMessage() == null || failure.getMessage().isBlank()
                ? "Não foi possível validar o ficheiro histórico" : failure.getMessage();
        CommunityImportDiagnostic detail = CommunityImportDiagnostic.fromLegacy(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommunityImportHttpResponse(HttpStatus.BAD_REQUEST, message,
                        detail.code(), detail.params()));
    }
}
