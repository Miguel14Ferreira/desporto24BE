#!/usr/bin/env bash
# Restore a local custom-format Jynlo pg_dump into disposable PostgreSQL 16.
# No connection or write to Render/production. No database port or network.
set -Eeuo pipefail

usage() {
  cat <<'HELP'
Usage: bash scripts/verify_venue_backup_restore.sh /absolute/path/to/backup.dump

Requires: pg_restore and Docker (or Podman), with access to postgres:16.
The backup must be a local, nonempty, custom-format pg_dump (-Fc).
The container is isolated (--network none) and deleted even on failure.
Optional: JYNLO_RESTORE_EXPECTED_VENUES=228 (minimum row count; default 228).
Never upload the backup to GitHub or put connection secrets in the PR.
HELP
}

if [[ "${1:-}" == '--help' || "${1:-}" == '-h' ]]; then
  usage
  exit 0
fi
if [[ "$#" -ne 1 ]]; then
  usage >&2
  exit 64
fi

backup="$(realpath -- "$1")"
if [[ ! -s "$backup" || ! -f "$backup" ]]; then
  echo 'ERROR: backup file not found or empty' >&2
  exit 66
fi
if ! command -v pg_restore >/dev/null 2>&1; then
  echo 'ERROR: local pg_restore is required to inspect the archive' >&2
  exit 69
fi
if ! pg_restore --list "$backup" >/dev/null; then
  echo 'ERROR: backup is not a readable pg_restore archive' >&2
  exit 65
fi

expected="${JYNLO_RESTORE_EXPECTED_VENUES:-228}"
if [[ ! "$expected" =~ ^[0-9]+$ ]] || [[ "$expected" -lt 1 ]]; then
  echo 'ERROR: JYNLO_RESTORE_EXPECTED_VENUES must be a positive integer' >&2
  exit 64
fi

if command -v docker >/dev/null 2>&1; then
  engine=docker
elif command -v podman >/dev/null 2>&1; then
  engine=podman
else
  echo 'ERROR: Docker or Podman is needed for an isolated PostgreSQL 16 restore' >&2
  exit 69
fi
if ! "$engine" info >/dev/null 2>&1; then
  echo 'ERROR: container engine is not running or is not accessible' >&2
  exit 69
fi

name="jynlo-restore-check-$(id -u)-$$"
cleanup() {
  "$engine" rm -f "$name" >/dev/null 2>&1 || true
}
trap cleanup EXIT

echo 'Starting isolated PostgreSQL 16 container (network disabled; no published ports).'
"$engine" run --detach --rm --name "$name" --network none \
  --env POSTGRES_PASSWORD=temporary_restore_only \
  --env POSTGRES_DB=jynlo_restore_check \
  --volume "$backup:/readonly-backup.dump:ro" \
  postgres:16 >/dev/null

ready=false
for _ in $(seq 1 60); do
  if "$engine" exec "$name" pg_isready -U postgres -d jynlo_restore_check >/dev/null 2>&1; then
    ready=true
    break
  fi
  sleep 1
done
if [[ "$ready" != true ]]; then
  echo 'ERROR: disposable PostgreSQL 16 failed to start' >&2
  exit 1
fi

echo 'Restoring all archived schema objects and data, with failure stopping the transaction.'
"$engine" exec "$name" pg_restore --exit-on-error --single-transaction \
  --no-owner --no-acl --username postgres --dbname jynlo_restore_check \
  /readonly-backup.dump

rows="$("$engine" exec "$name" psql -X -A -t -q -v ON_ERROR_STOP=1 \
  -U postgres -d jynlo_restore_check \
  -c 'SELECT COUNT(*) FROM desporto24.venue;')"
if [[ ! "$rows" =~ ^[0-9]+$ ]] || (( rows < expected )); then
  echo "ERROR: restored desporto24.venue count ($rows) below expected minimum ($expected)" >&2
  exit 1
fi

# Verify both catalogue data and migration metadata survived the restore.
"$engine" exec "$name" psql -X -v ON_ERROR_STOP=1 -U postgres \
  -d jynlo_restore_check -q -c \
  "DO \$\$ BEGIN IF to_regclass('desporto24.databasechangelog') IS NULL THEN RAISE EXCEPTION 'Missing restored Liquibase history'; END IF; END \$\$;"

echo "PASS: custom-format backup restored into isolated PostgreSQL 16; $rows venues and Liquibase history present."
echo 'Disposable container will now be destroyed. Production remains untouched.'
