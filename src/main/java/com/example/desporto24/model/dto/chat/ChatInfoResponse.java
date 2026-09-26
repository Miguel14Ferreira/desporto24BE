package com.example.desporto24.model.dto.chat;

public record ChatInfoResponse(
        boolean chatBlocked,
        String whoBlocked
) {
}
