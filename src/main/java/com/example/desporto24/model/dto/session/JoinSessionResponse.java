package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.JoinState;

public record JoinSessionResponse(
        JoinState state,
        Integer waitlistPosition
) {
}
