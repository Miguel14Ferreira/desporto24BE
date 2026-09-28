package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityRole;
import jakarta.validation.constraints.NotNull;

public record UpdateCommunityRoleRequest(@NotNull CommunityRole role) {
}
