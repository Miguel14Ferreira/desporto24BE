package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.dto.session.SessionSearchCriteria;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.service.interfaces.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public List<SessionSummaryResponse> discover(
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String city,
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
            @RequestParam(required = false) String sort) {
        return sessionService.discover(new SessionSearchCriteria(
                sport, city, from, to, latitude, longitude, radiusKm,
                availability, privateGame, freeOnly, sort
        ));
    }

    @GetMapping("/mine")
    public MySessionsResponse mine(Authentication authentication) {
        return sessionService.mine(authentication.getName());
    }

    @GetMapping("/{id}")
    public SessionDetailResponse detail(@PathVariable Long id, Authentication authentication) {
        return sessionService.getById(id, authentication.getName());
    }

    @PostMapping
    public ResponseEntity<SessionDetailResponse> create(
            @RequestBody @Valid CreateSessionRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.create(request, authentication.getName()));
    }
}
