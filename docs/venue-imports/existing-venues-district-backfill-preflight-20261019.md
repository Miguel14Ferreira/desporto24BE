# Existing Jynlo venues — district visibility: controlled backfill

**DRAFT PR, no production write has been executed.** This is not an OSM import.

## Why
`GET /api/venues/suggest?district=...` only returns venues whose `venue.district` is populated. Legacy venues predating the October district field may therefore appear missing even though they exist in the database.

## What
Promotion of the existing, reviewed draft `2026-10-19-backfill-known-districts-DRAFT.sql` to Liquibase `changes/`. Two carefully scoped updates, affecting only `district IS NULL OR BTRIM(district) = ''`, with 114 mapped Portuguese municipality/locality labels spanning 18 mainland districts. The smaller greater Lisbon locality mapping also requires coordinates within the limited Lisbon latitude/longitude rectangle. Never modifies an existing non-blank district, name, sport, author, or location. No INSERT or DELETE.

## Mandatory read-only preflight
Execute against Render PostgreSQL before approval:

```sql
SELECT city, COUNT(*) AS missing_district
FROM desporto24.venue
WHERE district IS NULL OR BTRIM(district) = ''
GROUP BY city ORDER BY missing_district DESC, city;
```

Review mappings where legacy `city` may be ambiguous; confirm a backup and test the change against PostgreSQL16 + real Liquibase changelog. Avoid changing records whose city is not in the explicit list; do not guess location when city or GPS conflicts. Inspect before/after `GET /api/admin/venues/coverage` from an authenticated administrator and search district results.

## Boundaries / warnings
- This lookup is by explicit placename, not a precise CAOP polygon per existing venue; cannot classify all 278 municipalities, every parish, or names with wrong `city` values.
- The official CAOP2025 municipal polygons (278) and districts (18) were prepared separately offline for higher-confidence future GPS backfills.
- Current Render connector PostgreSQL access fails SSL/TLS/allowlist checks; no real read-only preflight was carried out in this PR.
- Merge + controlled backend deploy only after PostgreSQL test and preflight approval. Do not run concurrently with an OSM import.

## Credit
DGT CAOP2025 (CC BY 4.0). This particular script maps preexisting city strings; it does not copy geometry into the production database.
