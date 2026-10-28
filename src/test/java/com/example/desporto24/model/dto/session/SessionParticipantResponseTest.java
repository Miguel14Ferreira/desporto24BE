package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.AttendanceStatus;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SessionParticipantResponseTest {
    private final ObjectMapper json = new ObjectMapper().findAndRegisterModules();

    @Test
    void serializesOnlyPublicAvatarAlongsideAuthorisedRosterData() throws Exception {
        SessionParticipantResponse row = new SessionParticipantResponse(
                12L, "player-one", AttendanceStatus.PENDING,
                Instant.parse("2026-10-28T18:00:00Z"), "A",
                "https://cdn.example.test/profiles/player.jpg");
        String payload = json.writeValueAsString(row);
        assertTrue(payload.contains("\"profilePicUrl\":\"https://cdn.example.test/profiles/player.jpg\""));
        assertTrue(payload.contains("\"username\":\"player-one\""));
        assertFalse(payload.contains("email"));
        assertFalse(payload.contains("phone"));
    }

    @Test
    void existingCallersKeepNullAvatarWithoutBreakingRecordContract() {
        SessionParticipantResponse row = new SessionParticipantResponse(
                12L, "player-one", AttendanceStatus.PENDING,
                Instant.parse("2026-10-28T18:00:00Z"), null);
        assertNull(row.profilePicUrl());
        assertNull(row.teamCode());
    }
}
