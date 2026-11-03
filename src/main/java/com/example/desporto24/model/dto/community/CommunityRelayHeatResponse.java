package com.example.desporto24.model.dto.community;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
public record CommunityRelayHeatResponse(
        Long id, Long teamId, String teamName, int heatNumber, BigDecimal elapsedSeconds,
        List<Long> runnerProfileIds, Instant recordedAt, String stage, String decisionStatus
) {
    public CommunityRelayHeatResponse(Long id,Long teamId,String teamName,int heatNumber,
            BigDecimal elapsedSeconds,List<Long> runnerProfileIds,Instant recordedAt) {
        this(id,teamId,teamName,heatNumber,elapsedSeconds,runnerProfileIds,recordedAt,
                "QUALIFICATION","VALID");
    }
}
