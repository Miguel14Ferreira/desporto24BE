package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.profile.ProfileActivityResponse;
import com.example.desporto24.service.ProfileActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileActivityController {

    private final ProfileActivityService profileActivityService;

    @GetMapping("/{username}/activity")
    public ProfileActivityResponse activity(@PathVariable String username) {
        return profileActivityService.activity(username);
    }
}
