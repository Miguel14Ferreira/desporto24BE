package com.example.desporto24.model.dto.reliability;

import com.example.desporto24.enumeration.DisputeStatus;
import jakarta.validation.constraints.NotNull;

public record DisputeResolutionRequest(@NotNull DisputeStatus status) {
}
