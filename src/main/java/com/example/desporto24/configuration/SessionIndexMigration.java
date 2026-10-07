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
public class SessionIndexMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureDiscoveryIndex() {
        jdbcTemplate.execute(
                "create index if not exists idx_session_status_game_date "
                        + "on desporto24.session (status, game_date)"
        );
        log.info("Session discovery index is available");
    }
}
