package com.example.desporto24.configuration;

import com.example.desporto24.enumeration.PilotEventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PilotEventSchemaMigrationTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void updatesConstraintWhenNewPilotEventTypeIsMissing() {
        when(jdbcTemplate.queryForList(anyString(), eq(String.class)))
                .thenReturn(List.of(
                        "CHECK (((event_type)::text = ANY "
                                + "(ARRAY['ACCESS'::text, 'REGISTRATION_COMPLETED'::text])))"
                ));

        PilotEventSchemaMigration migration =
                new PilotEventSchemaMigration(jdbcTemplate);

        migration.ensurePilotEventTypeConstraint();

        ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(2)).execute(sql.capture());

        String createConstraint = sql.getAllValues().get(1);
        for (PilotEventType type : PilotEventType.values()) {
            assertTrue(
                    createConstraint.contains("'" + type.name() + "'"),
                    () -> "Missing " + type.name() + " from generated constraint"
            );
        }
    }

    @Test
    void leavesConstraintUntouchedWhenAllTypesArePresent() {
        String definition = java.util.Arrays.stream(PilotEventType.values())
                .map(PilotEventType::name)
                .reduce("", (left, right) -> left + " " + right);

        when(jdbcTemplate.queryForList(anyString(), eq(String.class)))
                .thenReturn(List.of(definition));

        PilotEventSchemaMigration migration =
                new PilotEventSchemaMigration(jdbcTemplate);

        migration.ensurePilotEventTypeConstraint();

        verify(jdbcTemplate, never()).execute(anyString());
    }
}
