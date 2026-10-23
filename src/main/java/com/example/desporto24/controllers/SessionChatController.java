package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.session.SessionMessageRequest;
import com.example.desporto24.model.dto.session.SessionMessageResponse;
import com.example.desporto24.service.SessionChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions/{sessionId}/messages")
public class SessionChatController {

    private final SessionChatService sessionChatService;

    @GetMapping
    public List<SessionMessageResponse> messages(
            @PathVariable Long sessionId,
            Authentication authentication) {
        return sessionChatService.messages(sessionId, authentication.getName());
    }

    @PostMapping
    public SessionMessageResponse post(
            @PathVariable Long sessionId,
            @RequestBody @Valid SessionMessageRequest request,
            Authentication authentication) {
        return sessionChatService.post(sessionId, request.body(), authentication.getName(), request.replyToMessageId());
    }
}
