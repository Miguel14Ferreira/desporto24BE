package com.example.desporto24.service;

import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.dto.community.CreateCommunityRoundRequest;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions.*;

class CommunityInlineHeatEventLengthTest {
    @Test void sprintHeatNamesFitDatabaseAndRequestValidation() throws Exception {
        int dbLimit = CommunityRound.class.getDeclaredField("eventType")
                .getAnnotation(Column.class).length();
        RecordComponent component = Arrays.stream(CreateCommunityRoundRequest.class.getRecordComponents())
                .filter(item -> "eventType".equals(item.getName())).findFirst().orElseThrow();
        int validationLimit = component.getAnnotation(Size.class).max();
        assertTrue(dbLimit >= 32);
        assertTrue(validationLimit >= dbLimit);
        for (String code : new String[]{
                "INLINE_TRACK_500D_QUARTER_HEAT", "INLINE_TRACK_500D_SEMI_HEAT",
                "INLINE_TRACK_500D_FINAL_A", "INLINE_ROAD_LAP_QUARTER_HEAT",
                "INLINE_ROAD_LAP_SEMI_HEAT", "INLINE_ROAD_LAP_FINAL_A"}) {
            assertTrue(code.length() <= dbLimit);
            assertTrue(code.length() <= validationLimit);
            assertEquals(code, CommunityRoundEventType.validate("Patinagem de Velocidade", code));
        }
    }
}
