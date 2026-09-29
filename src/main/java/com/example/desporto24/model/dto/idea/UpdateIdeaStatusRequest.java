package com.example.desporto24.model.dto.idea;

import com.example.desporto24.enumeration.IdeaStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateIdeaStatusRequest(@NotNull IdeaStatus status) {
}
