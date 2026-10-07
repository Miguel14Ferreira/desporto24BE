package com.example.desporto24.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionParticipantIndexMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureSessionStateIndex() {
        jdbcTemplate.execute(
                "create index if not exists idx_session_participant_session_state "
                        + "on desporto24.session_participant (session_id, state)"
        );
        log.info("Session participant session/state index is available");
    }
}
