package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommunityTournamentRequest(
        @NotBlank @Size(max = 120) String name,
        @Min(0) Integer winPoints,
        @Min(0) Integer drawPoints,
        @Min(0) Integer lossPoints
) {
}
