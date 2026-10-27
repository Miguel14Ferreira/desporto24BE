package com.example.desporto24.model.dto.community;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/** Explicit post-game report; no final score is required or inferred. */
public record CommunitySessionResultRequest(
        @NotNull Outcome outcome,
        @NotEmpty List<@Valid Player> players
) {
    public enum Outcome { A, B, DRAW }
    public record Player(@NotBlank String username, @NotNull Team team) {}
    public enum Team { A, B }
}
