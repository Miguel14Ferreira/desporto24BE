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
public class CommunityRoundEntrySchemaMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void allowEntriesWithoutProfile() {
        // Hibernate schema update does not remove the legacy NOT NULL constraint.
        // Keep the foreign key: linked profiles must still reference a real account.
        jdbcTemplate.execute(
                "alter table if exists community_round_entry "
                        + "alter column profile_id drop not null"
        );
        log.info("Community round entries now support players without an associated profile");
    }
}
