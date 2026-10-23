package com.example.desporto24.model.dto.chat;

import java.util.List;

public record ChatSearchResponse(List<ChatSearchHit> results, boolean hasMore, int page) {
}
