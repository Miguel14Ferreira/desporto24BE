package com.example.desporto24.controllers;

import com.example.desporto24.service.AdminRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/admin/players")
public class AdminRoleController {
    private final AdminRoleService service;

    public AdminRoleController(AdminRoleService service) {
        this.service = service;
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public AdminRoleService.RoleResult changeRole(
            @PathVariable Long id, @RequestBody RoleChange request, Principal principal) {
        return service.changeRole(principal.getName(), id, request.role(), request.expectedRole());
    }

    public record RoleChange(String role, String expectedRole) {}
}
