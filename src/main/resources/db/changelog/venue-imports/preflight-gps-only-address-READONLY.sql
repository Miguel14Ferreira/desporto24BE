-- Jynlo LIVE verification BEFORE deploying the nullable postal-address migration.
-- PostgreSQL READ ONLY. Does NOT migrate, insert, update, delete or change constraints.
-- Run against the real backend database after taking a backup.
BEGIN TRANSACTION READ ONLY;
SET LOCAL statement_timeout = '30000ms';

-- Baseline: record current row count and identify incomplete locations.
SELECT COUNT(*) AS existing_venues,
       COUNT(*) FILTER (WHERE address IS NULL OR BTRIM(address) = '') AS address_missing,
       COUNT(*) FILTER (WHERE latitude IS NOT NULL AND longitude IS NOT NULL) AS gps_pairs_present,
       COUNT(*) FILTER (
          WHERE (address IS NULL OR BTRIM(address) = '')
            AND (latitude IS NULL OR longitude IS NULL
                 OR latitude NOT BETWEEN 36.8 AND 42.3
                 OR longitude NOT BETWEEN -9.6 AND -6.0)
       ) AS rows_that_would_block_migration
FROM desporto24.venue;

-- Check the live column types and current NOT NULL flags.
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_schema = 'desporto24'
  AND table_name = 'venue'
  AND column_name IN ('address', 'latitude', 'longitude')
ORDER BY column_name;

-- Show the presence or absence of the intended new CHECK constraint.
SELECT conname, pg_get_constraintdef(oid) AS definition
FROM pg_constraint
WHERE conrelid = 'desporto24.venue'::regclass
  AND conname = 'ck_venue_address_or_mainland_gps';

-- Detect Liquibase tracking table without assuming which schema it uses.
SELECT to_regclass('public.databasechangelog') AS public_changelog,
       to_regclass('desporto24.databasechangelog') AS desporto24_changelog;

ROLLBACK;
