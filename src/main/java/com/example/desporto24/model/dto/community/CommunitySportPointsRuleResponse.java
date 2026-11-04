package com.example.desporto24.model.dto.community;

public record CommunitySportPointsRuleResponse(
        String sport, int winPoints, int drawPoints, int lossPoints
) {}
