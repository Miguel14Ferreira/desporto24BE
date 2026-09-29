package com.example.desporto24.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSchemaMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureNotificationTypeConstraint() {
        List<String> definitions = jdbcTemplate.queryForList(
                """
                select pg_get_constraintdef(c.oid)
                from pg_constraint c
                join pg_class t on t.oid = c.conrelid
                join pg_namespace n on n.oid = t.relnamespace
                where n.nspname = 'desporto24'
                  and t.relname = 'notifications'
                  and c.conname = 'notifications_type_check'
                """,
                String.class
        );

        if (!definitions.isEmpty() && definitions.getFirst().contains("CHAT_MESSAGE")) {
            return;
        }

        log.info("Updating notifications_type_check to support CHAT_MESSAGE notifications");

        jdbcTemplate.execute(
                "alter table desporto24.notifications drop constraint if exists notifications_type_check"
        );

        jdbcTemplate.execute(
                """
                alter table desporto24.notifications
                add constraint notifications_type_check
                check (
                    type is null or type in (
                        'WAITLIST_PROMOTED',
                        'SESSION_REMOVED',
                        'SESSION_CANCELLED',
                        'ATTENDANCE_DISPUTE_ACCEPTED',
                        'ATTENDANCE_DISPUTE_REJECTED',
                        'CHAT_MESSAGE'
                    )
                )
                """
        );
    }
}
