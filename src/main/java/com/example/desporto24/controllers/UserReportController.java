package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.moderation.CreateUserReportRequest;
import com.example.desporto24.service.UserReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/users")
public class UserReportController {

    private final UserReportService userReportService;

    @PostMapping("/{username}")
    public ResponseEntity<Void> reportUser(
            @PathVariable String username,
            @RequestBody @Valid CreateUserReportRequest request,
            Authentication authentication) {
        userReportService.reportUser(
                authentication.getName(),
                username,
                request.reason()
        );
        return ResponseEntity.accepted().build();
    }
}
