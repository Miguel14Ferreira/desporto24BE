package com.example.desporto24.model.dto.community;
import java.math.BigDecimal;
public record CommunityRelayFinalistResponse(Long teamId,String teamName,int seed,
    BigDecimal qualifyingSeconds) {}
