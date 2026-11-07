package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityGolfRoundPreviewServiceTest {
    private final CommunityCompetitionService competition = mock(CommunityCompetitionService.class);
    private final CommunityGolfRoundPreviewService service = new CommunityGolfRoundPreviewService(competition);

    @Test void accessFailureFromCommunityOverviewIsNotSwallowed() {
        when(competition.overview(5L, "visitor", null))
                .thenThrow(new org.springframework.security.access.AccessDeniedException("Sem acesso"));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.preview(5L, 10L, "visitor"));
    }

    @Test void invalidIdentifiersAreRejectedWithoutAccessingCommunity() {
        for (Long id : new Long[]{null, 0L, -1L}) {
            var invalidCommunity = assertThrows(ResponseStatusException.class,
                    () -> service.preview(id, 10L, "member"));
            assertEquals(HttpStatus.BAD_REQUEST, invalidCommunity.getStatusCode());
            var invalidRound = assertThrows(ResponseStatusException.class,
                    () -> service.preview(5L, id, "member"));
            assertEquals(HttpStatus.BAD_REQUEST, invalidRound.getStatusCode());
        }
        verifyNoInteractions(competition);
    }

    @Test void missingRoundReturnsNotFound() {
        var overview = mock(CommunityCompetitionOverviewResponse.class);
        when(overview.rounds()).thenReturn(List.of());
        when(competition.overview(5L, "member", null)).thenReturn(overview);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.preview(5L, 10L, "member"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test void validRoundReturnsRankedPublicDto() {
        var entry = new CommunityRoundEntryResponse(null, "golfer", "Golfer", null,
                null, CommunityMatchResult.DRAW, 0, 0, false, false,
                null, null, null, null, null, 9L, 0, null,
                null, null, false, null, new BigDecimal("40"));
        var round = new CommunityRoundResponse(10L, "Round", null,
                List.of(entry), "Golfe", null, List.of(), "GOLF_STROKE_9");
        var overview = mock(CommunityCompetitionOverviewResponse.class);
        when(overview.rounds()).thenReturn(List.of(round));
        when(competition.overview(5L, "member", null)).thenReturn(overview);
        assertEquals(List.of(new CommunityGolfRoundStandingResponse(1, 9L, 40L)),
                service.preview(5L, 10L, "member"));
    }
}
