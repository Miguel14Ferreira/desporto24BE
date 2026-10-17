package com.example.desporto24.controllers;

import com.example.desporto24.service.OnlinePresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Only the authenticated subject can update its own presence. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presence")
@PreAuthorize("isAuthenticated()")
public class OnlinePresenceController {
    private final OnlinePresenceService service;

    @PostMapping("/heartbeat")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void heartbeat(Authentication authentication) {
        service.heartbeat(authentication.getName());
    }
}
