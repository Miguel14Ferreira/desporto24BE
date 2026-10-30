package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/** Guard against enum/schema drift: inserts must not fail after a session note
 * changes and roll the organizer's edit back with a constraint exception.
 */
class MeetingPointNotificationTypeConstraintTest {
    @Test
    void currentNotificationTypesAreAllowedByLatestPostgresConstraint() throws Exception {
        String path = "db/changelog/changes/2026-10-28-session-meeting-point-notification-type.yaml";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
            assertNotNull(in, "Migration missing from the classpath");
            String changelog = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(changelog.contains("ADD CONSTRAINT notifications_type_check"));
            for (NotificationType type : NotificationType.values()) {
                assertTrue(changelog.contains("'" + type.name() + "'"),
                        () -> "PostgreSQL check is missing notification type " + type.name());
            }
        }
    }

    @Test
    void masterChangelogIncludesConstraintUpgrade() throws Exception {
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("db/changelog/db.changelog-master.yaml")) {
            assertNotNull(in);
            String master = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(master.contains("db/changelog/changes/2026-10-28-session-meeting-point-notification-type.yaml"));
        }
    }
}
