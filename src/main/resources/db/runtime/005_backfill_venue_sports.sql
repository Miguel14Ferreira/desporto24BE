-- Backfill normalized venue sports from the legacy semicolon-separated venue.sports column.
-- Only sports present in the active SPORT reference catalog are migrated.
-- The NOT EXISTS guard keeps the script idempotent.

INSERT INTO venue_sport (venue_id, sport_code)
SELECT
    v.id,
    catalog.code
FROM venue v
JOIN reference_catalog_item catalog
  ON LOWER(catalog.category) = 'sport'
 AND catalog.active = TRUE
WHERE v.sports IS NOT NULL
  AND TRIM(v.sports) <> ''
  AND (
        REPLACE(LOWER(v.sports), ' ', '') = REPLACE(LOWER(catalog.code), ' ', '')
        OR REPLACE(LOWER(v.sports), ' ', '') LIKE CONCAT(REPLACE(LOWER(catalog.code), ' ', ''), ';%')
        OR REPLACE(LOWER(v.sports), ' ', '') LIKE CONCAT('%;', REPLACE(LOWER(catalog.code), ' ', ''), ';%')
        OR REPLACE(LOWER(v.sports), ' ', '') LIKE CONCAT('%;', REPLACE(LOWER(catalog.code), ' ', ''))
      )
  AND NOT EXISTS (
        SELECT 1
        FROM venue_sport existing
        WHERE existing.venue_id = v.id
          AND LOWER(existing.sport_code) = LOWER(catalog.code)
  );
