package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.chat.ChatReactionRequest;
import com.example.desporto24.model.dto.chat.ChatReactionSummary;
import com.example.desporto24.service.ChatReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages/reactions")
@RequiredArgsConstructor
public class ChatReactionController {
    private final ChatReactionService service;

    @GetMapping("/{kind}/{conversationId}")
    public List<ChatReactionSummary> summaries(@PathVariable String kind,
            @PathVariable String conversationId, @RequestParam List<Long> ids,
            Authentication auth) {
        return service.summaries(auth.getName(), kind, conversationId, ids);
    }

    @PutMapping("/{kind}/{conversationId}/{messageId}")
    public ChatReactionSummary put(@PathVariable String kind,
            @PathVariable String conversationId, @PathVariable Long messageId,
            @RequestBody @Valid ChatReactionRequest request, Authentication auth) {
        return service.update(auth.getName(), kind, conversationId, messageId, request.emoji());
    }

    @DeleteMapping("/{kind}/{conversationId}/{messageId}")
    public ChatReactionSummary remove(@PathVariable String kind,
            @PathVariable String conversationId, @PathVariable Long messageId,
            Authentication auth) {
        return service.update(auth.getName(), kind, conversationId, messageId, null);
    }
}
