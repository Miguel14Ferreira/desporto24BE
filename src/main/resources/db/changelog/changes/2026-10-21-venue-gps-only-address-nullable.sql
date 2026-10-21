-- The address field becomes optional ONLY when continental Portuguese GPS is present.
-- No data migration, no venue INSERT, UPDATE, DELETE or GPS fabrication.
ALTER TABLE desporto24.venue ALTER COLUMN address DROP NOT NULL;

ALTER TABLE desporto24.venue
    ADD CONSTRAINT ck_venue_address_or_mainland_gps
    CHECK (
        NULLIF(BTRIM(address), '') IS NOT NULL
        OR (
            latitude IS NOT NULL AND longitude IS NOT NULL
            AND latitude BETWEEN 36.8 AND 42.3
            AND longitude BETWEEN -9.6 AND -6.0
        )
    );
