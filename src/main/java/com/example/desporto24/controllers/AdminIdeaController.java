package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.idea.AdminIdeaResponse;
import com.example.desporto24.model.dto.idea.UpdateIdeaStatusRequest;
import com.example.desporto24.service.interfaces.IdeaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/ideas")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminIdeaController {

    private final IdeaService ideaService;

    @GetMapping
    public ResponseEntity<List<AdminIdeaResponse>> list() {
        return ResponseEntity.ok(ideaService.getAllIdeas());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdminIdeaResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateIdeaStatusRequest request) {
        return ResponseEntity.ok(ideaService.updateStatus(id, request.status()));
    }
}
