#!/usr/bin/env bash
# Run all venue import and Liquibase draft checks without GitHub Actions minutes.
# No network access, database, Render credentials or optional osmium dependency.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

PYTHON="${PYTHON:-python3}"
echo "== Compiling national venue importer, SQL generator and tests =="
"$PYTHON" -m py_compile \
  scripts/import_osm_venues_national.py \
  scripts/generate_verified_venue_liquibase.py \
  scripts/test_import_osm_venues_national.py \
  scripts/test_generate_verified_venue_liquibase.py \
  scripts/test_portalegre_liquibase_draft.py \
  scripts/test_venue_district_backfill_draft.py

echo "== Running all venue-only Python smoke tests locally =="
"$PYTHON" -m unittest discover -s scripts -p 'test_*.py' -v

echo "== Checking that draft migrations are not enabled =="
"$PYTHON" - <<'PY'
from pathlib import Path
master = Path('src/main/resources/db/changelog/db.changelog-master.yaml').read_text(encoding='utf-8')
drafts = Path('src/main/resources/db/changelog/venue-imports/drafts')
assert drafts.is_dir(), 'Expected draft migrations folder'
for p in drafts.iterdir():
    if p.suffix in ('.sql', '.yaml'):
        assert p.name not in master, f'Draft migration unexpectedly activated: {p.name}'
print('Draft migrations remain outside the Liquibase master changelog.')
PY

echo "OK: local offline checks passed. PostgreSQL integration still required."
