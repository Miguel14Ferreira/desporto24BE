#!/usr/bin/env bash
# Applies the exact group-stage Liquibase YAML ONLY in a disposable PostgreSQL16.
# Requires an existing, recent, custom-format LIVE backup. Never connects to LIVE.
set -Eeuo pipefail
if [[ $# -ne 1 || ! -s "${1:-}" ]]; then
  echo "Usage: bash scripts/preflight_team_group_stage_pg16.sh /absolute/path/to/recent-live.dump" >&2
  exit 64
fi
backup="$(realpath -- "$1")"
command -v pg_restore >/dev/null || { echo 'pg_restore is required' >&2; exit 69; }
pg_restore --list "$backup" >/dev/null
command -v gh >/dev/null || { echo 'GitHub CLI is required' >&2; exit 69; }
if command -v docker >/dev/null 2>&1; then engine=docker
elif command -v podman >/dev/null 2>&1; then engine=podman
else echo 'Docker or Podman required' >&2; exit 69; fi
"$engine" info >/dev/null

tmp="$(mktemp -d)"
chmod 755 "$tmp"
net="jynlo-groups-preflight-$(id -u)-$$"
db="jynlo-groups-pg-$(id -u)-$$"
cleanup() {
  "$engine" rm -f "$db" >/dev/null 2>&1 || true
  "$engine" network rm "$net" >/dev/null 2>&1 || true
  rm -rf "$tmp"
}
trap cleanup EXIT
yaml=2026-10-22-team-group-stage.yaml
gh api "repos/Miguel14Ferreira/desporto24BE/contents/src/main/resources/db/changelog/changes/$yaml?ref=feature/team-group-stage-20261022" \
  -H 'Accept: application/vnd.github.raw+json' > "$tmp/$yaml"
grep -q 'id: 2026-10-22-team-group-stage' "$tmp/$yaml" || { echo 'Incorrect YAML' >&2; exit 1; }
chmod 644 "$tmp/$yaml"

"$engine" image inspect postgres:16 >/dev/null 2>&1 || "$engine" pull postgres:16 >/dev/null
"$engine" image inspect liquibase/liquibase:4.30.0 >/dev/null 2>&1 || "$engine" pull liquibase/liquibase:4.30.0 >/dev/null
"$engine" run --rm --entrypoint /bin/sh -v "$tmp:/liquibase/changelog:ro" \
  liquibase/liquibase:4.30.0 \
  -c "test -r /liquibase/changelog/$yaml && test -s /liquibase/changelog/$yaml" \
  || { echo 'Changelog not readable by Liquibase' >&2; exit 1; }
echo 'PASS: exact group-stage YAML readable by Liquibase.'

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
[[ "$ready" == true ]] || { echo 'Temporary PostgreSQL did not start' >&2; exit 1; }

echo 'Restoring LIVE snapshot into disposable database (no LIVE writes).'
"$engine" exec "$db" pg_restore --exit-on-error --single-transaction --no-owner --no-acl \
  -U postgres -d jynlo_restore_check /readonly-backup.dump >/dev/null

"$engine" exec -i "$db" psql -X -v ON_ERROR_STOP=1 -U postgres -d jynlo_restore_check <<'SQL'
DO $$
BEGIN
  IF (SELECT count(*) FROM desporto24.venue) < 3595 THEN
    RAISE EXCEPTION 'Snapshot has fewer than 3595 venues';
  END IF;
  IF to_regclass('desporto24.community_tournament_team_match') IS NULL
     OR to_regclass('desporto24.community_tournament') IS NULL THEN
    RAISE EXCEPTION 'Old tournament tables missing';
  END IF;
  IF EXISTS (SELECT 1 FROM information_schema.columns
      WHERE table_schema='desporto24' AND table_name='community_tournament_team_match'
        AND column_name='group_number') THEN
    RAISE EXCEPTION 'group_number already exists: this is not a clean pre-release snapshot';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM desporto24.databasechangelog
      WHERE id='2026-10-22-community-timeline') THEN
    RAISE EXCEPTION 'Backup lacks previously published feed/chat migration';
  END IF;
  RAISE NOTICE 'PASS: existing venues/tournaments and previous migration present';
END $$;
SQL

run_liquibase() {
  "$engine" run --rm --network "$net" -v "$tmp:/liquibase/changelog:ro" \
    liquibase/liquibase:4.30.0 \
    --url='jdbc:postgresql://db:5432/jynlo_restore_check?currentSchema=desporto24' \
    --username=postgres --password=temporary_restore_only \
    --default-schema-name=desporto24 --liquibase-schema-name=desporto24 \
    --search-path=/liquibase/changelog \
    --changelog-file="$yaml" update
}
echo 'Applying exact groups migration.'
run_liquibase | tee "$tmp/first.log"
grep -Eq 'Run: +1' "$tmp/first.log" || { echo 'Expected one changeset' >&2; exit 1; }

"$engine" exec -i "$db" psql -X -v ON_ERROR_STOP=1 -U postgres -d jynlo_restore_check <<'SQL'
DO $$
BEGIN
  IF (SELECT count(*) FROM desporto24.venue) < 3595 THEN
    RAISE EXCEPTION 'Venues altered';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns
     WHERE table_schema='desporto24' AND table_name='community_tournament_team_match'
       AND column_name='group_number' AND is_nullable='YES') THEN
    RAISE EXCEPTION 'Nullable group_number missing';
  END IF;
  IF to_regclass('desporto24.idx_team_match_tournament_group') IS NULL THEN
    RAISE EXCEPTION 'Group match index missing';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE
     conrelid='desporto24.community_tournament_team_match'::regclass
       AND conname='ck_team_match_group_number') THEN
    RAISE EXCEPTION 'Group check constraint missing';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint
      WHERE conrelid='desporto24.community_tournament'::regclass
        AND conname='ck_tournament_format'
        AND pg_get_constraintdef(oid) LIKE '%GROUP_KNOCKOUT%') THEN
    RAISE EXCEPTION 'Group tournament format missing';
  END IF;
  IF (SELECT count(*) FROM desporto24.databasechangelog
      WHERE id='2026-10-22-team-group-stage') <> 1 THEN
    RAISE EXCEPTION 'New changeset history missing or duplicated';
  END IF;
  RAISE NOTICE 'PASS: 3595+ venues, group column/index, format/checks, Liquibase history';
END $$;
SQL

echo 'Checking Liquibase idempotency (0 changesets).'
run_liquibase | tee "$tmp/second.log"
grep -Eq 'Run: +0' "$tmp/second.log" || { echo 'Migration is not idempotent' >&2; exit 1; }
echo "PASS: team groups migration validated on isolated PG16 against $backup"
echo 'Disposable containers/network deleted. Production untouched.'
