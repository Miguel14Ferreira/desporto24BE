package com.example.desporto24.model.dto.community;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Prevent the old 20-character DTO cap from blocking real 2026 sprint heat codes. */
class CommunityRoundEventTypeRequestValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void acceptsCanonicalTrackAndRoadHeatCodes() {
        for (String code : List.of("INLINE_TRACK_500D_FIRST_ROUND",
                "INLINE_ROAD_LAP_QUARTER_HEAT",
                "INLINE_TRACK_500D_SEMI_HEAT",
                "INLINE_ROAD_LAP_FINAL_A")) {
            var input = new CreateCommunityRoundRequest("QA", Instant.now(),
                    List.of(), "Patinagem de Velocidade", null, null, code);
            assertTrue(validator.validateProperty(input, "eventType").isEmpty(), code);
        }
    }

    @Test
    void rejectsOversizedEventCodesWithoutChangingStoredLegacyData() {
        var invalid = new CreateCommunityRoundRequest("QA", Instant.now(),
                List.of(), "Patinagem de Velocidade", null, null, "X".repeat(65));
        assertFalse(validator.validateProperty(invalid, "eventType").isEmpty());
    }
}
