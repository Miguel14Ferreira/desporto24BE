package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.moderation.UserBlockStatusResponse;
import com.example.desporto24.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocks/users")
public class UserBlockController {

    private final UserBlockService userBlockService;

    @PostMapping("/{username}")
    public UserBlockStatusResponse block(
            @PathVariable String username,
            Authentication authentication) {
        return userBlockService.blockUser(authentication.getName(), username);
    }

    @DeleteMapping("/{username}")
    public UserBlockStatusResponse unblock(
            @PathVariable String username,
            Authentication authentication) {
        return userBlockService.unblockUser(authentication.getName(), username);
    }

    @GetMapping("/{username}/status")
    public UserBlockStatusResponse status(
            @PathVariable String username,
            Authentication authentication) {
        return userBlockService.status(authentication.getName(), username);
    }
}
