package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.OrganizerExclusionReason;
import jakarta.validation.constraints.NotNull;

/** Explicit choice for organizer-owned future sessions. */
public record OrganizerExclusionRequest(@NotNull OrganizerExclusionReason reason) {}
