package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.model.dto.moderation.AdminUserReportResponse;
import com.example.desporto24.model.dto.moderation.UpdateUserReportStatusRequest;
import com.example.desporto24.service.UserReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports/users")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminUserReportController {

    private final UserReportService userReportService;

    @GetMapping
    public ResponseEntity<List<AdminUserReportResponse>> listOpenReports() {
        return ResponseEntity.ok(userReportService.openReports());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdminUserReportResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserReportStatusRequest request) {
        if (request.status() == UserReportStatus.OPEN) {
            return ResponseEntity.badRequest().build();
        }

        return userReportService.updateStatus(id, request.status())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
