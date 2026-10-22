#!/usr/bin/env bash
# Read-only LIVE backup input -> isolated PostgreSQL16 -> exact community feed/chat Liquibase.
# No LIVE PostgreSQL connection, no shared Docker ports, no backup uploads.
set -Eeuo pipefail

if [[ $# -ne 1 || ! -s "${1:-}" ]]; then
  echo "Usage: bash scripts/preflight_community_timeline_pg16.sh /absolute/path/to/recent-live.dump" >&2
  exit 64
fi
backup="$(realpath -- "$1")"
command -v pg_restore >/dev/null || { echo 'pg_restore is required' >&2; exit 69; }
pg_restore --list "$backup" >/dev/null
command -v gh >/dev/null || { echo 'GitHub CLI (gh) is required' >&2; exit 69; }
if command -v docker >/dev/null 2>&1; then engine=docker
elif command -v podman >/dev/null 2>&1; then engine=podman
else echo 'Docker or Podman is required' >&2; exit 69; fi
"$engine" info >/dev/null

tmp="$(mktemp -d)"
net="jynlo-community-check-$(id -u)-$$"
db="jynlo-community-pg-$(id -u)-$$"
cleanup() {
  "$engine" rm -f "$db" >/dev/null 2>&1 || true
  "$engine" network rm "$net" >/dev/null 2>&1 || true
  rm -rf "$tmp"
}
trap cleanup EXIT

echo 'Obtaining EXACT migration YAML from GitHub release candidate (no production access).'
gh api 'repos/Miguel14Ferreira/desporto24BE/contents/src/main/resources/db/changelog/changes/2026-10-22-community-timeline.yaml?ref=feature/community-feed-chat-20261022' \
  -H 'Accept: application/vnd.github.raw+json' > "$tmp/2026-10-22-community-timeline.yaml"
if ! grep -q 'id: 2026-10-22-community-timeline' "$tmp/2026-10-22-community-timeline.yaml"; then
  echo 'Migration YAML is missing or incorrect; stopping.' >&2; exit 1
fi

echo 'Preparing isolated PostgreSQL 16 and Liquibase 4.30.0 images.'
"$engine" image inspect postgres:16 >/dev/null 2>&1 || "$engine" pull postgres:16 >/dev/null
"$engine" image inspect liquibase/liquibase:4.30.0 >/dev/null 2>&1 || "$engine" pull liquibase/liquibase:4.30.0 >/dev/null
"$engine" network create --internal "$net" >/dev/null
"$engine" run -d --rm --name "$db" --network "$net" --network-alias db \
  -e POSTGRES_PASSWORD=temporary_restore_only -e POSTGRES_DB=jynlo_restore_check \
  -v "$backup:/readonly-backup.dump:ro" postgres:16 >/dev/null

ready=false
for _ in $(seq 1 90); do
  if "$engine" exec "$db" psql -X -A -t -q -v ON_ERROR_STOP=1 \
      -U postgres -d jynlo_restore_check -c 'SELECT 1' >/dev/null 2>&1; then
    ready=true; break
  fi
  sleep 1
done
if [[ "$ready" != true ]]; then echo 'Temporary PG16 did not start' >&2; exit 1; fi

echo 'Restoring recent LIVE backup in disposable database.'
"$engine" exec "$db" pg_restore --exit-on-error --single-transaction --no-owner --no-acl \
  -U postgres -d jynlo_restore_check /readonly-backup.dump >/dev/null

echo 'Checking 3595 venues, previously deployed migrations and nonexistence of new table.'
"$engine" exec -i "$db" psql -X -v ON_ERROR_STOP=1 -U postgres -d jynlo_restore_check <<'SQL'
DO $$
BEGIN
  IF (SELECT count(*) FROM desporto24.venue) < 3595 THEN
    RAISE EXCEPTION 'Pre-release venue total below 3595';
  END IF;
  IF to_regclass('desporto24.community_timeline_entry') IS NOT NULL THEN
    RAISE EXCEPTION 'New timeline already exists; use a clean LIVE snapshot';
  END IF;
  IF to_regclass('desporto24.venue_experience_rating') IS NULL OR
     to_regclass('desporto24.session_player_compliment') IS NULL THEN
    RAISE EXCEPTION 'Backup is too old: ratings or compliments missing';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns
      WHERE table_schema = 'desporto24' AND table_name = 'session_organizer_exclusion'
        AND column_name = 'reason') THEN
    RAISE EXCEPTION 'Backup is too old: fair-play migration missing';
  END IF;
END $$;
SQL

run_liquibase() {
  "$engine" run --rm --network "$net" \
    -v "$tmp:/liquibase/changelog:ro" liquibase/liquibase:4.30.0 \
    --url='jdbc:postgresql://db:5432/jynlo_restore_check?currentSchema=desporto24' \
    --username=postgres --password=temporary_restore_only \
    --default-schema-name=desporto24 --liquibase-schema-name=desporto24 \
    --changelog-file=2026-10-22-community-timeline.yaml update
}

echo 'Applying one changeset using its actual GitHub YAML.'
run_liquibase | tee "$tmp/first.log"
grep -Eq 'Run: +1' "$tmp/first.log" || { echo 'Expected exactly one changeset.' >&2; exit 1; }

echo 'Checking schema/constraints/indexes/history without modifying existing data.'
"$engine" exec -i "$db" psql -X -v ON_ERROR_STOP=1 -U postgres -d jynlo_restore_check <<'SQL'
DO $$
DECLARE
  fk_count integer;
BEGIN
  IF (SELECT count(*) FROM desporto24.venue) < 3595 THEN
    RAISE EXCEPTION 'Venue count changed';
  END IF;
  IF to_regclass('desporto24.community_timeline_entry') IS NULL THEN
    RAISE EXCEPTION 'Timeline table missing';
  END IF;
  IF to_regclass('desporto24.idx_community_timeline_kind_recent') IS NULL THEN
    RAISE EXCEPTION 'Timeline index missing';
  END IF;
  SELECT count(*) INTO fk_count FROM pg_constraint WHERE conrelid =
      'desporto24.community_timeline_entry'::regclass AND contype = 'f';
  IF fk_count <> 2 THEN
    RAISE EXCEPTION 'Expected two foreign keys, found %', fk_count;
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conrelid =
      'desporto24.community_timeline_entry'::regclass
      AND conname = 'ck_community_timeline_kind') THEN
    RAISE EXCEPTION 'Kind CHECK missing';
  END IF;
  IF (SELECT count(*) FROM desporto24.databasechangelog
    WHERE id='2026-10-22-community-timeline') <> 1 THEN
    RAISE EXCEPTION 'Exactly one timeline changelog entry expected';
  END IF;
  RAISE NOTICE 'PASS: 3595+ venues, community timeline, 2 FKs, CHECK, index, migration history';
END $$;
SQL

echo 'Re-running Liquibase, expecting no changes.'
run_liquibase | tee "$tmp/second.log"
grep -Eq 'Run: +0' "$tmp/second.log" || { echo 'Liquibase not idempotent' >&2; exit 1; }
echo "PASS: Community feed/chat migration validated against $backup in isolated PostgreSQL16."
echo 'Disposable database and network are being deleted. Production untouched.'
