package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommunityTournamentTeamRequest(
        @NotBlank @Size(max = 100) String name
) {}
