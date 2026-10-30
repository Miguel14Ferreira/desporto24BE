package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.chat.ChatSearchResponse;
import com.example.desporto24.service.ChatMessageSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages/inbox")
public class ChatMessageSearchController {
    private final ChatMessageSearchService search;


    @GetMapping("/context")
    public java.util.List<com.example.desporto24.model.dto.chat.ChatSearchHit> context(
            @RequestParam String kind, @RequestParam String id,
            @RequestParam Long messageId, Authentication authentication) {
        return search.context(authentication.getName(), kind, id, messageId);
    }

    @GetMapping("/search")
    public ChatSearchResponse search(@RequestParam String kind,
                                     @RequestParam String id,
                                     @RequestParam("q") String query,
                                     @RequestParam(defaultValue = "0") int page,
                                     Authentication authentication) {
        return search.search(authentication.getName(), kind, id, query, page);
    }
}
