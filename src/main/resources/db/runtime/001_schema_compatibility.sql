-- Runtime schema compatibility fixes.
-- These statements are intentionally idempotent because Hibernate currently owns base schema creation via ddl-auto=update.

ALTER TABLE IF EXISTS community_round_entry
    ALTER COLUMN profile_id DROP NOT NULL;

ALTER TABLE IF EXISTS venue
    ALTER COLUMN created_by_profile_id DROP NOT NULL;

ALTER TABLE IF EXISTS venue_photo
    ALTER COLUMN uploaded_by_profile_id DROP NOT NULL;

ALTER TABLE IF EXISTS notifications
    DROP CONSTRAINT IF EXISTS notifications_type_check;

ALTER TABLE IF EXISTS notifications
    ADD CONSTRAINT notifications_type_check
    CHECK (
        type IS NULL OR type IN (
            'WAITLIST_PROMOTED',
            'SESSION_REMOVED',
            'SESSION_CANCELLED',
            'ATTENDANCE_DISPUTE_ACCEPTED',
            'ATTENDANCE_DISPUTE_REJECTED',
            'CHAT_MESSAGE',
            'COMMUNITY_INVITE'
        )
    );

ALTER TABLE IF EXISTS pilot_event
    DROP CONSTRAINT IF EXISTS pilot_event_event_type_check;

ALTER TABLE IF EXISTS pilot_event
    ADD CONSTRAINT pilot_event_event_type_check
    CHECK (
        event_type IN (
            'ACCESS',
            'REGISTRATION_COMPLETED',
            'SESSION_SEARCHED',
            'SESSION_CREATED',
            'SESSION_JOINED',
            'ATTENDANCE_CONFIRMED',
            'SESSION_COMPLETED',
            'OPERATIONAL_INTERVENTION'
        )
    );


-- Performance indexes for growing venue and session datasets.
CREATE INDEX IF NOT EXISTS idx_venue_city_lower
    ON venue (LOWER(city) text_pattern_ops);

CREATE INDEX IF NOT EXISTS idx_venue_name_lower
    ON venue (LOWER(name) text_pattern_ops);

CREATE INDEX IF NOT EXISTS idx_venue_coordinates
    ON venue (latitude, longitude);
