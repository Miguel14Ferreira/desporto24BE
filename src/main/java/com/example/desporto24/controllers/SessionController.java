package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.JoinSessionRequest;
import com.example.desporto24.model.dto.session.JoinSessionResponse;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.dto.session.SessionPublicPreviewResponse;
import com.example.desporto24.model.dto.session.SessionSearchCriteria;
import com.example.desporto24.model.dto.session.SessionParticipantResponse;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.model.dto.session.SessionTeamSuggestionResponse;
import com.example.desporto24.model.dto.session.ReportSessionProblemRequest;
import com.example.desporto24.service.interfaces.SessionService;
import com.example.desporto24.service.SessionTeamSuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final SessionTeamSuggestionService sessionTeamSuggestionService;

    @GetMapping
    public List<SessionSummaryResponse> discover(
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String query,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radiusKm,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) Boolean privateGame,
            @RequestParam(required = false) Boolean freeOnly,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sort,
            Authentication authentication) {
        SessionSearchCriteria criteria = new SessionSearchCriteria(
                sport, city, from, to, latitude, longitude, radiusKm,
                availability, privateGame, freeOnly, minPrice, maxPrice, sort, query
        );

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return sessionService.discover(criteria);
        }

        return sessionService.discover(criteria, authentication.getName());
    }

    @GetMapping("/mine")
    public MySessionsResponse mine(Authentication authentication) {
        return sessionService.mine(authentication.getName());
    }

    @GetMapping("/{id}/public-preview")
    public SessionPublicPreviewResponse publicPreview(@PathVariable Long id) {
        return sessionService.publicPreview(id);
    }

    @GetMapping("/{id}")
    public SessionDetailResponse detail(@PathVariable Long id, Authentication authentication) {
        return sessionService.getById(id, authentication.getName());
    }

    @GetMapping("/{id}/series")
    public List<SessionSummaryResponse> series(
            @PathVariable Long id,
            Authentication authentication) {
        return sessionService.series(id, authentication.getName());
    }

    @GetMapping("/{id}/participants")
    public List<SessionParticipantResponse> participants(
            @PathVariable Long id,
            Authentication authentication) {
        return sessionService.participants(id, authentication.getName());
    }

    @GetMapping("/{id}/teams/suggestion")
    public SessionTeamSuggestionResponse teamSuggestion(
            @PathVariable Long id,
            Authentication authentication) {
        return sessionTeamSuggestionService.suggest(id, authentication.getName());
    }

    @PostMapping("/{id}/teams/confirm")
    public SessionTeamSuggestionResponse confirmTeams(
            @PathVariable Long id,
            Authentication authentication) {
        return sessionTeamSuggestionService.confirm(id, authentication.getName());
    }

    @PostMapping("/{id}/join")
    public JoinSessionResponse join(
            @PathVariable Long id,
            @RequestBody(required = false) JoinSessionRequest request,
            Authentication authentication) {
        JoinSessionRequest effectiveRequest = request == null ? new JoinSessionRequest(null) : request;
        return sessionService.join(id, effectiveRequest, authentication.getName());
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leave(@PathVariable Long id, Authentication authentication) {
        sessionService.leave(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication authentication) {
        sessionService.cancel(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long id, Authentication authentication) {
        sessionService.complete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/problems/not-occurred")
    public ResponseEntity<Void> reportDidNotOccur(
            @PathVariable Long id,
            Authentication authentication) {
        sessionService.reportDidNotOccur(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/problems")
    public ResponseEntity<Void> reportProblem(
            @PathVariable Long id,
            @RequestBody @Valid ReportSessionProblemRequest request,
            Authentication authentication) {
        sessionService.reportProblem(id, request.type(), request.details(), authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/participants/{participantId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable Long id,
            @PathVariable Long participantId,
            Authentication authentication) {
        sessionService.removeParticipant(id, participantId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SessionDetailResponse> create(
            @RequestBody @Valid CreateSessionRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.create(request, authentication.getName()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SessionDetailResponse> createWithPhoto(
            @RequestPart("session") @Valid CreateSessionRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.create(request, photo, authentication.getName()));
    }
}
