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
public class SocialPostCommentIndexMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensurePostCreatedAtIndex() {
        jdbcTemplate.execute(
                "create index if not exists idx_social_post_comment_post_created_at "
                        + "on desporto24.social_post_comment (post_id, created_at)"
        );
        log.info("Social post comment post/created-at index is available");
    }
}
