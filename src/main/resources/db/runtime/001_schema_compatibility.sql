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
CREATE INDEX IF NOT EXISTS idx_venue_city
    ON venue (city);

CREATE INDEX IF NOT EXISTS idx_venue_name
    ON venue (name);

CREATE INDEX IF NOT EXISTS idx_venue_coordinates
    ON venue (latitude, longitude);


CREATE INDEX IF NOT EXISTS idx_venue_sport_code
    ON venue_sport (sport_code);

CREATE INDEX IF NOT EXISTS idx_venue_sport_venue
    ON venue_sport (venue_id);


CREATE INDEX IF NOT EXISTS idx_session_status_game_date
    ON session (status, game_date);

CREATE INDEX IF NOT EXISTS idx_session_sport
    ON session (sport);

CREATE INDEX IF NOT EXISTS idx_session_city
    ON session (city);

CREATE INDEX IF NOT EXISTS idx_session_price_amount
    ON session (price_amount);


CREATE INDEX IF NOT EXISTS idx_social_post_like_post
    ON social_post_like (post_id);

CREATE INDEX IF NOT EXISTS idx_social_post_comment_post
    ON social_post_comment (post_id);


CREATE INDEX IF NOT EXISTS idx_session_participant_session_state
    ON session_participant (session_id, state);

CREATE INDEX IF NOT EXISTS idx_session_participant_profile_state
    ON session_participant (profile_id, state);

CREATE INDEX IF NOT EXISTS idx_session_waitlist_profile_created
    ON session_waitlist (profile_id, created_at);

CREATE INDEX IF NOT EXISTS idx_session_waitlist_session_priority
    ON session_waitlist (session_id, priority_tier, created_at);
