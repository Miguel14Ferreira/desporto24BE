# Read-only national venue extraction (18 districts) — practical runbook

**Do not execute on Render. No admin token, API writes or DB connection are used.**

## Official map input
The user-provided **DGT CAOP2025 Continente** was transformed into WGS84
GeoJSON: `municipios.geojson` (278 official municipalities) and
`distritos.geojson` (18 official districts). The ZIP is stored with the
conversation artifacts, **not** in Git or GitHub Actions (25 MB compressed).
Extract it locally into `.cache/jynlo-caop/`. No PostGIS service is needed.

## Procedure
From the backend repo checkout on a machine with network access and disk space:

```bash
python3 -m pip install osmium
python3 -m unittest discover -s scripts -p 'test_prepare_national_venues.py' -v
python3 scripts/prepare_national_venues.py --district all \\
  --municipalities-geojson .cache/jynlo-caop/municipios.geojson \\
  --districts-geojson .cache/jynlo-caop/distritos.geojson
python3 scripts/venue_coverage_report.py \\
  --input .cache/jynlo-venues/national-report.json \\
  --output .cache/jynlo-venues/district-coverage.csv
```

- Downloads missing/corrupt-free PBFs at
  `https://download.openstreetmap.fr/extracts/europe/portugal/` on demand.
  The current public index shows all 18 district extracts (13–53 MB each).
- Keeps source downloads and per-district `venues.jsonl` and `review.jsonl`
  in a local ignored cache. Stage output uses CAOP geometry; no guesswork from
  the district filename. Does not process OSM relations without geometry.
- Writes `national-report.json` **after each district**, including failures.
  Restart the command to skip checksum-verified districts or retry failures.
- `--district portalegre` stages just one district; `--refresh` explicitly
  redownloads and restages (the default is to reuse caches).
- Quarantine missing name, municipality, sport, district and restricted access.
  A named candidate is **not** proof of public reservability.

## Production import is intentionally separate
Never copy the staged files automatically into PostgreSQL or a scheduled Render
cron. Review quality, aliases and preflight existing venue overlaps first.
Only approved, deduplicated venues should enter the Jynlo catalogue via a
bounded, explicit, authenticated import. This script cannot make bookings or
infer sports, prices, opening hours and access.

## Environment limitations
The assistant runtime cannot resolve/download third-party PBF hosts. This PR
adds a resumable runner and offline tests but **has not processed the other 17
districts or modified the live app**. Avoid claiming national completion.

Source rights: OpenStreetMap contributors (ODbL); DGT CAOP2025 (CC BY 4.0).
