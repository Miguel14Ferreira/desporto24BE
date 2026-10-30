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

    /** POST-only: the private RSVP receipt stays in the JSON body, never a URL. */
    public record GuestChatRead(String receipt) {}
    public record GuestChatSend(String receipt,String body) {}

    @PostMapping("/guest-chat/read")
    public List<SessionMessageResponse> guestRead(
            @PathVariable Long sessionId,@RequestBody GuestChatRead request) {
        return sessionChatService.guestMessages(sessionId,request==null?null:request.receipt());
    }

    @PostMapping("/guest-chat/send")
    public SessionMessageResponse guestSend(
            @PathVariable Long sessionId,@RequestBody GuestChatSend request) {
        return sessionChatService.guestPost(sessionId,request==null?null:request.receipt(),
                request==null?null:request.body());
    }

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
    @PutMapping("/{messageId}")
    public SessionMessageResponse edit(@PathVariable Long sessionId,
            @PathVariable Long messageId,
            @RequestBody @Valid com.example.desporto24.model.dto.chat.ChatMessageMutationRequest request,
            Authentication authentication) {
        return sessionChatService.editMessage(sessionId, messageId, request.text(), authentication.getName());
    }

    @DeleteMapping("/{messageId}")
    public SessionMessageResponse delete(@PathVariable Long sessionId,
            @PathVariable Long messageId, Authentication authentication) {
        return sessionChatService.deleteMessage(sessionId, messageId, authentication.getName());
    }

}
