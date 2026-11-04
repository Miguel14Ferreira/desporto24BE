package com.example.desporto24.model.dto.community;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CreateCommunityRoundSeasonRequestTest {
    @Test
    void legacyRoundCreationRemainsUnassignedForBackwardCompatibility() {
        var request = new CreateCommunityRoundRequest(
                "Jornada 1", Instant.parse("2026-11-04T12:00:00Z"), List.of());
        assertNull(request.seasonId());
    }

    @Test
    void newRoundCanCarryAnExplicitSeasonIdWithoutChangingSportOrEvent() {
        var request = new CreateCommunityRoundRequest(
                "Jornada 2", Instant.parse("2026-11-04T12:00:00Z"), List.of(),
                "Futsal", null, null, null, null, null, null, null,
                null, null, null, 44L);
        assertEquals(44L, request.seasonId());
        assertEquals("Futsal", request.sport());
        assertEquals("Jornada 2", request.name());
    }
}
