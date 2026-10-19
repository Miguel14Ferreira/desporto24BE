# Jynlo — Release candidate: venue catalogue & discoverability

**DRAFT — NOT LIVE. Requires PostgreSQL 16 tests, a verified backup and production read-only duplicate checks before any merge.**

This candidate combines the contents of BE #118, #122, #123 and the single-file safety removal in #117 without overwriting the shared VenueRepository.

## Data changes: 4 PostgreSQL Liquibase changesets
1. `2026-10-19-backfill-known-districts.yaml`: only sets district on rows where it is NULL/blank, using explicitly reviewed locality mappings.
2. `2026-10-19-portalegre-source-checked.yaml`: 9 source-backed, CAOP-checked OSM venue candidates.
3. `2026-10-19-first-official-district-venues.yaml`: 8 municipal venue candidates from Évora, Castelo Branco and Santarém.
4. `2026-10-19-pestana-7-source-corroborated.yaml`: 7 from the Pestana sports spreadsheets corroborated by official operator pages (Porto, Maia, Lisboa, Mira).

All 24 INSERTs are guarded with NOT EXISTS + ON CONFLICT DO NOTHING. **0–24 actual rows**, never claim 24 confirmed or assume address/GPS for every row. No automatic sport enrichment for the 169/228 historic facilities without sports in the earlier pilot analysis.

## Existing-venue discovery & diagnostics
- Searching Cascais in `GET /api/venues` and `GET /api/venues/suggest` also searches known Cascais parish names, using bounded queries and preserving original user city/address fields.
- Admin-only `GET /api/admin/venues/quality?limit=50` reports aggregate completeness by original locality and district, with limit 1–100 and ADMIN/SUPER_ADMIN authorization; no venue full-table read, coordinates, or user records.
- `VenueRepository` intentionally includes both the new municipality queries and the new aggregate query from separate PRs.
- Removes the legacy `PilotNationalVenuesImportOnReady` application-ready listener, which otherwise inserts two hard-coded OSM venues at restart if absent. Existing records remain untouched; explicit import/admin controllers are preserved.

## Mandatory gates before release
- [ ] Verify current Render PostgreSQL backup and ability to restore. Do not change the database allowlist, TLS or credentials merely to bypass connector problems.
- [ ] Run the read-only preflight scripts for Portalegre, Pestana and official venues on the *actual* catalogue. Manually reconcile alternative names, GPS adjacency and pre-existing community venues.
- [ ] Run Python offline SQLite smoke tests for every release changeset and Java repository/controller tests. Note: passing SQLite/H2 is not proof that migrations run on production PostgreSQL.
- [ ] Validate Liquibase changelog paths and execute on an isolated PostgreSQL 16 schema with realistic existing venues.
- [ ] Review admin role restrictions, paging, mixed municipality & sport filters and regressions.
- [ ] Only after approval: one squash merge to BE preview and one controlled backend Render deploy; verify logs, source counts, district coverage, and autocomplete, with rollback readiness.

## No production actions executed
This branch has **no merge, no deploy, no DB writes and no workflow runs requested**. GitHub Actions minutes are constrained. Current assistant environment does not support a real PostgreSQL MCP connection (SSL/TLS/allowlist blocked) or a Maven checkout of the private repository.

Source provenance: OpenStreetMap contributors (ODbL); DGT CAOP2025 (CC BY 4.0); municipal source links in venue manifests.

## Unified read-only preflight for all 24 candidates

Before any merge, run `src/main/resources/db/changelog/venue-imports/preflight-24-venues-READONLY.sql` using a **SELECT-only role** against the current Render production catalogue. This query enumerates the 24 proposed source references directly from the three INSERT scripts and returns matching existing records by source reference, exact normalised name/city, or short-distance GPS proximity. A `NO_MATCH_FOUND` result is **not** a confirmation of non-duplication: spellings, shared complexes, geocodes and alternate sources still require manual review. There may be several rows per candidate. The lateral match window is capped at 20 results per candidate.

The test `scripts/test_venue_release_preflight.py` statically checks that the preflight still covers the same 24 source IDs and contains no SQL mutations. Neither the SQL nor this test has been run against PostgreSQL or the user's live data here.


## Render database backup and exact release gate (reviewed 19 October 2026)

The active `desporto24-db` compute plan reported by Render is `0.1c-256mb`, which is a **paid** compute plan, not Render's distinct `free` compute plan. Render documents PITR and on-demand logical exports for paid Postgres, but the Render connector does **not** expose recovery/export status or actions here. **No actual backup was verified or created by this change.**

1. In the Render Dashboard open workspace **Jynlo → desporto24-db → Recovery**. Confirm the exact recovery window and that the intended restore point exists. Do not infer recoverability from the database being reported as `available`.
2. Create and **download a logical export** before the schema/data release if the Recovery panel offers `Create export`. Keep the export independently and check that it contains the expected database/schema. Do not initiate point-in-time restoration here: a recovery spins up an additional billable database.
3. Run the **read-only** query `src/main/resources/db/changelog/venue-imports/preflight-24-venues-READONLY.sql` against the real catalogue, using a SELECT-only DB credential and SSL. It produces potentially more than one row per proposed venue. Record all 24 reviewed references, resolving names/aliases and candidates within the geographic warning rectangle. A zero-row match is *not* a guarantee that no duplicate exists.
4. Inspect `district IS NULL OR BTRIM(district)=''` by city, and review at least the 114 named mappings; the backfill changes district data even when no proposed new venue is inserted.
5. On a separate PostgreSQL 16 test schema, run all four Liquibase changesets twice (or re-run the changelog) to verify one-time execution and guard behavior. Test that existing COMMUNITY rows and already populated districts are unchanged.
6. Only after the evidence and tests pass, take an explicit publish decision and run **one** backend deploy, observing the Liquibase logs. Check the before/after source-reference inventory, the admin `/api/admin/venues/quality` counts and the district-based venue suggestions. If an issue is found, **stop further deploys** and use the verified recovery route; avoid a speculative DELETE rollback.

Render documentation: https://render.com/docs/postgresql-backups ; https://render.com/docs/compute-plans .

### Source quality consistency regression
The existing `coverageByDistrict` summary previously counted `sports = ''` as empty, but omitted `sports = ' '`. The new admin quality query correctly uses `TRIM(sports)`, so the two aggregates could disagree. This release now consistently uses `TRIM(sports) = ''` for both; `VenueQualityByCityRepositoryTest` includes a whitespace-only sports case. Tests still require actual execution.
