package com.example.desporto24.controller;

import com.example.desporto24.service.SessionInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/session-invites")
@RequiredArgsConstructor
public class SessionInviteInboxController {
    private final SessionInviteService service;

    @GetMapping("/pending")
    public List<SessionInviteService.PendingSessionInvite> pending(Authentication authentication) {
        return service.pendingFor(authentication.getName());
    }
}
