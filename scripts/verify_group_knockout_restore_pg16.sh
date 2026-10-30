#!/usr/bin/env bash
# Validate the exact group+knockout production changeSet on a local restored backup.
# Never connects to Render, never publishes ports, never modifies the backup.
set -Eeuo pipefail

if [[ "${1:-}" == "--help" || "${1:-}" == "-h" ]]; then
  echo 'Usage: JYNLO_RESTORE_EXPECTED_VENUES=3595 bash scripts/verify_group_knockout_restore_pg16.sh /path/to/backup.dump'
  exit 0
fi
if [[ "$#" -ne 1 ]]; then
  echo 'ERROR: provide one local custom-format PostgreSQL archive (-Fc)' >&2
  exit 64
fi
backup="$(realpath -- "$1")"
if [[ ! -f "$backup" || ! -s "$backup" ]]; then
  echo 'ERROR: backup file not found or empty' >&2
  exit 66
fi
if ! command -v pg_restore >/dev/null 2>&1 || ! pg_restore --list "$backup" >/dev/null 2>&1; then
  echo 'ERROR: a readable custom-format pg_dump and local pg_restore are required' >&2
  exit 65
fi
expected="${JYNLO_RESTORE_EXPECTED_VENUES:-3595}"
if [[ ! "$expected" =~ ^[0-9]+$ ]] || (( expected < 3595 )); then
  echo 'ERROR: expected venue count must be at least 3595' >&2
  exit 64
fi
if command -v docker >/dev/null 2>&1; then
  engine=docker
elif command -v podman >/dev/null 2>&1; then
  engine=podman
else
  echo 'ERROR: Docker or Podman is needed to restore into isolated PostgreSQL 16' >&2
  exit 69
fi
if ! "$engine" info >/dev/null 2>&1; then
  echo 'ERROR: local container engine is not available' >&2
  exit 69
fi
changelog="src/main/resources/db/changelog/changes/2026-10-22-community-team-group-knockout.yaml"
if [[ ! -f "$changelog" ]]; then
  echo 'ERROR: run from the repository root; group migration YAML is missing' >&2
  exit 66
fi

name="jynlo-group-check-$(id -u)-$$"
cleanup() {
  "$engine" rm -f "$name" >/dev/null 2>&1 || true
}
trap cleanup EXIT

echo 'Starting disposable, network-isolated PostgreSQL 16; no published ports.'
"$engine" run --detach --rm --name "$name" --network none \
  --env POSTGRES_HOST_AUTH_METHOD=trust \
  --env POSTGRES_DB=jynlo_group_check \
  --volume "$backup:/readonly-backup.dump:ro" postgres:16 >/dev/null

sql() {
  "$engine" exec "$name" psql -X -A -t -q -v ON_ERROR_STOP=1 \
    -U postgres -d jynlo_group_check -c "$1"
}
ready=false
for _ in $(seq 1 90); do
  if sql 'SELECT 1' >/dev/null 2>&1; then ready=true; break; fi
  sleep 1
done
if [[ "$ready" != true ]]; then
  echo 'ERROR: disposable database failed to start' >&2
  exit 1
fi
echo 'Restoring archived schema and records, without modifying the source archive.'
"$engine" exec "$name" pg_restore --exit-on-error --single-transaction \
  --no-owner --no-acl -U postgres -d jynlo_group_check /readonly-backup.dump

venue_count="$(sql 'SELECT COUNT(*) FROM desporto24.venue')"
if [[ ! "$venue_count" =~ ^[0-9]+$ ]] || (( venue_count < expected )); then
  echo "ERROR: only $venue_count venues restored (expected at least $expected)" >&2
  exit 1
fi
test "$(sql "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema='desporto24' AND table_name='community_tournament' AND column_name='group_count'")" = 0 || {
  echo 'ERROR: this backup already has the group migration; cannot test first application' >&2
  exit 1
}
test "$(sql "SELECT COUNT(*) FROM pg_constraint WHERE conname='ck_tournament_format' AND connamespace='desporto24'::regnamespace")" = 1 || {
  echo 'ERROR: original tournament format constraint not found' >&2
  exit 1
}
test "$(sql "SELECT COUNT(*) FROM desporto24.databasechangelog")" -ge 1 || {
  echo 'ERROR: restored Liquibase history missing' >&2
  exit 1
}

before_tournaments="$(sql 'SELECT COUNT(*) FROM desporto24.community_tournament')"
before_teams="$(sql 'SELECT COUNT(*) FROM desporto24.community_tournament_team')"
before_matches="$(sql 'SELECT COUNT(*) FROM desporto24.community_tournament_team_match')"
before_venue_count="$venue_count"
original_format_counts="$(sql "SELECT COALESCE(string_agg(format || ':' || n::text, ',' ORDER BY format),'none') FROM (SELECT format, COUNT(*) AS n FROM desporto24.community_tournament GROUP BY format) grouped")"
before_migration_count="$(sql "SELECT COUNT(*) FROM desporto24.databasechangelog WHERE id='2026-10-22-community-team-group-knockout'")"
if [[ "$before_migration_count" != 0 ]]; then
  echo 'ERROR: group migration is already present in the restored Liquibase history' >&2
  exit 1
fi

apply_once() {
  "$engine" run --rm --network "container:$name" \
    -v "$PWD/src/main/resources:/liquibase/resources:ro" \
    liquibase:4.33.0 \
    --url=jdbc:postgresql://127.0.0.1:5432/jynlo_group_check?currentSchema=desporto24 \
    --username=postgres \
    --default-schema-name=desporto24 \
    --liquibase-schema-name=desporto24 \
    --search-path=/liquibase/resources \
    --changelog-file=db/changelog/changes/2026-10-22-community-team-group-knockout.yaml \
    update
}

echo 'Applying exact production group migration YAML to the restored copy.'
apply_once
test "$(sql "SELECT COUNT(*) FROM desporto24.databasechangelog WHERE id='2026-10-22-community-team-group-knockout'")" = 1
test "$(sql "SELECT COUNT(*) FROM desporto24.community_tournament WHERE group_count IS NOT NULL")" = 0
test "$(sql "SELECT COUNT(*) FROM desporto24.community_tournament_team WHERE group_number IS NOT NULL OR group_draw_order IS NOT NULL")" = 0
test "$(sql "SELECT COUNT(*) FROM desporto24.community_tournament_team_match WHERE group_number IS NOT NULL")" = 0
test "$(sql "SELECT COUNT(*) FROM pg_constraint WHERE conname='ck_tournament_format' AND pg_get_constraintdef(oid) LIKE '%GROUP_KNOCKOUT%'")" = 1

echo 'Applying the exact same Liquibase migration a second time to prove idempotency.'
apply_once
test "$(sql "SELECT COUNT(*) FROM desporto24.databasechangelog WHERE id='2026-10-22-community-team-group-knockout'")" = 1
test "$(sql 'SELECT COUNT(*) FROM desporto24.community_tournament')" = "$before_tournaments"
test "$(sql 'SELECT COUNT(*) FROM desporto24.community_tournament_team')" = "$before_teams"
test "$(sql 'SELECT COUNT(*) FROM desporto24.community_tournament_team_match')" = "$before_matches"
test "$(sql 'SELECT COUNT(*) FROM desporto24.venue')" = "$before_venue_count"
test "$(sql "SELECT COALESCE(string_agg(format || ':' || n::text, ',' ORDER BY format),'none') FROM (SELECT format, COUNT(*) AS n FROM desporto24.community_tournament GROUP BY format) grouped")" = "$original_format_counts"

echo "PASS: restored $venue_count venues, existing competition data unchanged, Liquibase applied twice and idempotent."
echo 'PASS: all changes were confined to a temporary PostgreSQL 16 without network access; source archive unchanged.'
