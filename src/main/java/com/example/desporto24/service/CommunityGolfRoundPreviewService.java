package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunityGolfRoundStandingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

/**
 * Read-only preview for one persisted golf round. The competition overview
 * enforces the existing community access policy before any round is exposed.
 * This does not create, link or finalize a tournament.
 */
@Service
@RequiredArgsConstructor
public class CommunityGolfRoundPreviewService {
    private final CommunityCompetitionService competition;

    @Transactional(readOnly = true)
    public List<CommunityGolfRoundStandingResponse> preview(
            Long communityId, Long roundId, String username) {
        CommunityRoundResponse round = competition.overview(communityId, username, null)
                .rounds().stream().filter(item -> roundId.equals(item.id()))
                .findFirst().orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Jornada não encontrada"));
        try {
            return CommunityGolfTournamentRoundAdapter.calculate(round).stream()
                    .map(s -> new CommunityGolfRoundStandingResponse(
                            s.position(), s.playerId(), s.grossStrokes())).toList();
        } catch (IllegalArgumentException invalid) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A jornada não contém resultados completos de golfe", invalid);
        }
    }
}
