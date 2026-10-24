package com.example.desporto24.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A ProfileDTO is used both as incoming /login credentials and as an outgoing
 * profile in LoginResponse and /api/profile/me. Never serialize a password
 * (plain text or hash) even if a future mapper mistakenly sets the field.
 */
class ProfileDtoSecretSerializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void passwordIsNeverSerializedInProfileResponses() throws Exception {
        ProfileDTO profile = new ProfileDTO();
        profile.setUsername("fixture");
        profile.setPassword("$2a$12$fake-hash-must-not-leak");

        String json = mapper.writeValueAsString(profile);

        assertTrue(json.contains("\"username\":\"fixture\""));
        assertFalse(json.contains("password"));
        assertFalse(json.contains("fake-hash-must-not-leak"));
    }

    @Test
    void loginStillAcceptsThePasswordAsAnInputOnlyField() throws Exception {
        ProfileDTO input = mapper.readValue(
                "{\"username\":\"fixture\",\"password\":\"test-only-secret\"}",
                ProfileDTO.class);

        assertEquals("fixture", input.getUsername());
        assertEquals("test-only-secret", input.getPassword());
        assertFalse(mapper.writeValueAsString(input).contains("test-only-secret"));
    }
}
