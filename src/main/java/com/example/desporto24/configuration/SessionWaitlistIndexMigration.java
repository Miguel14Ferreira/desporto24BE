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
public class SessionWaitlistIndexMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureQueueOrderIndex() {
        jdbcTemplate.execute(
                "create index if not exists idx_session_waitlist_queue_order "
                        + "on desporto24.session_waitlist (session_id, priority_tier, created_at)"
        );
        log.info("Session waitlist queue order index is available");
    }
}
