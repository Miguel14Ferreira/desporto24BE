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
public class NotificationIndexMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureUnreadCountIndex() {
        jdbcTemplate.execute(
                "create index if not exists idx_notifications_username_unread "
                        + "on desporto24.notifications (username) where read = false"
        );
        log.info("Unread notification count index is available");
    }
}
