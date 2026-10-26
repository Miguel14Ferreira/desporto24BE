#!/usr/bin/env bash
# Test Render .dir.tar.gz logical exports in a disposable, network-isolated
# PostgreSQL 16 container. Never connects to any production database.
set -Eeuo pipefail

usage() {
  cat <<'HELP'
Usage: bash scripts/verify_render_venue_export_restore.sh /path/to/export.dir.tar.gz
Requires local nonempty Render export, tar and Docker or Podman.
Optional: JYNLO_RESTORE_EXPECTED_VENUES=3595 (minimum restored rows).
Does not upload, forward or disclose the archive and never connects to Render.
HELP
}

if [[ "$#" -eq 1 && ( "$1" == "--help" || "$1" == "-h" ) ]]; then
  usage
  exit 0
fi
if [[ "$#" -ne 1 ]]; then
  usage >&2
  exit 64
fi
archive="$(realpath -- "$1")"
if [[ ! -f "$archive" || ! -s "$archive" || "$archive" != *.dir.tar.gz ]]; then
  echo 'ERROR: provide a nonempty .dir.tar.gz Render export file' >&2
  exit 66
fi
if ! tar -tzf "$archive" >/dev/null; then
  echo 'ERROR: backup archive is corrupt/not gzip tar' >&2
  exit 65
fi

expected="$(printenv JYNLO_RESTORE_EXPECTED_VENUES || printf '3595')"
if [[ ! "$expected" =~ ^[0-9]+$ ]] || (( expected < 1 )); then
  echo 'ERROR: expected venue count must be positive' >&2
  exit 64
fi

if command -v docker >/dev/null 2>&1; then
  engine=docker
elif command -v podman >/dev/null 2>&1; then
  engine=podman
else
  echo 'ERROR: Docker/Podman required' >&2
  exit 69
fi
if ! "$engine" info >/dev/null 2>&1; then
  echo 'ERROR: container engine unavailable' >&2
  exit 69
fi

container="jynlo-render-restore-$(id -u)-$$"
cleanup() { "$engine" rm -f "$container" >/dev/null 2>&1 || true; }
trap cleanup EXIT

echo 'Starting isolated PostgreSQL 16 with no network and no exposed ports.'
"$engine" run --detach --rm --name "$container" --network none \
  --env POSTGRES_PASSWORD=only_for_isolated_restore \
  --env POSTGRES_DB=jynlo_render_restore_test \
  --volume "$archive:/input/readonly-export.dir.tar.gz:ro" \
  postgres:16 >/dev/null

ready=false
for _ in $(seq 1 90); do
  if "$engine" exec "$container" psql -X -A -t -q -v ON_ERROR_STOP=1 \
      -U postgres -d jynlo_render_restore_test -c 'SELECT 1;' >/dev/null 2>&1; then
    ready=true
    break
  fi
  sleep 1
done
if [[ "$ready" != true ]]; then
  echo 'ERROR: disposable PostgreSQL did not become available' >&2
  exit 1
fi

# Extract only inside ephemeral, network-disabled container; host archive is
# the ONLY volume and is read-only, even for a malformed or hostile tar file.
"$engine" exec "$container" bash -euc '
  mkdir -p /tmp/render-export
  tar -xzf /input/readonly-export.dir.tar.gz -C /tmp/render-export \
      --no-same-owner --no-same-permissions
  mapfile -d "" -t toc_files < <(find /tmp/render-export -type f -name toc.dat -print0)
  if (( ${#toc_files[@]} != 1 )); then
    echo "ERROR: archive needs exactly one directory-format toc.dat" >&2
    exit 65
  fi
  directory="$(dirname "${toc_files[0]}")"
  pg_restore --list --format=directory "$directory" >/dev/null
  pg_restore --format=directory --exit-on-error --single-transaction \
    --no-owner --no-acl -U postgres -d jynlo_render_restore_test "$directory"
'

rows="$("$engine" exec "$container" psql -X -A -t -q -v ON_ERROR_STOP=1 \
  -U postgres -d jynlo_render_restore_test \
  -c 'SELECT COUNT(*) FROM desporto24.venue;')"
if [[ ! "$rows" =~ ^[0-9]+$ ]] || (( rows < expected )); then
  echo "ERROR: only $rows restored venues, expected at least $expected" >&2
  exit 1
fi
"$engine" exec "$container" psql -X -q -v ON_ERROR_STOP=1 \
  -U postgres -d jynlo_render_restore_test -c \
  "DO \$\$ BEGIN
    IF to_regclass('desporto24.databasechangelog') IS NULL THEN
      RAISE EXCEPTION 'Missing restored Liquibase history';
    END IF;
  END \$\$;"

echo "PASS: restored $rows venues and Liquibase history in a disposable PostgreSQL 16."
echo 'Cleanup deletes the entire restore container. Production was never accessed.'
