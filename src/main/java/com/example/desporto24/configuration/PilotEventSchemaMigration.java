package com.example.desporto24.configuration;

import com.example.desporto24.enumeration.PilotEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PilotEventSchemaMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensurePilotEventTypeConstraint() {
        List<String> allowedValues = Arrays.stream(PilotEventType.values())
                .map(Enum::name)
                .toList();

        List<String> definitions = jdbcTemplate.queryForList(
                """
                select pg_get_constraintdef(c.oid)
                from pg_constraint c
                join pg_class t on t.oid = c.conrelid
                join pg_namespace n on n.oid = t.relnamespace
                where n.nspname = 'desporto24'
                  and t.relname = 'pilot_event'
                  and c.conname = 'pilot_event_event_type_check'
                """,
                String.class
        );

        if (!definitions.isEmpty()
                && allowedValues.stream().allMatch(definitions.getFirst()::contains)) {
            return;
        }

        log.info(
                "Updating pilot_event_event_type_check to support {}",
                allowedValues
        );

        jdbcTemplate.execute(
                "alter table desporto24.pilot_event "
                        + "drop constraint if exists pilot_event_event_type_check"
        );

        String sqlValues = allowedValues.stream()
                .map(value -> "'" + value + "'")
                .collect(Collectors.joining(", "));

        jdbcTemplate.execute(
                "alter table desporto24.pilot_event "
                        + "add constraint pilot_event_event_type_check "
                        + "check (event_type in (" + sqlValues + "))"
        );
    }
}
