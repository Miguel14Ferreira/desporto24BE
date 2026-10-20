# Jynlo — Release candidate: venue catalogue & discoverability

**DRAFT — NOT LIVE. Isolated PostgreSQL 16 Liquibase integration tests PASSED; a verified production backup, final real-database preflight and explicit publish approval remain mandatory.**

This candidate combines the contents of BE #118, #122, #123 and the single-file safety removal in #117 without overwriting the shared VenueRepository.

## Data changes: 5 PostgreSQL Liquibase changesets (37 conditional candidates)
1. `2026-10-19-backfill-known-districts.yaml`: only sets district on rows where it is NULL/blank, using explicitly reviewed locality mappings.
2. `2026-10-19-portalegre-source-checked.yaml`: 9 source-backed, CAOP-checked OSM venue candidates.
3. `2026-10-19-first-official-district-venues.yaml`: 8 municipal venue candidates from Évora, Castelo Branco and Santarém.
4. `2026-10-19-pestana-7-source-corroborated.yaml`: 7 from the Pestana sports spreadsheets corroborated by official operator pages (Porto, Maia, Lisboa, Mira).
5. `2026-10-20-national-municipal-batch-13.yaml`: 13 additional source-reviewed municipal venues in Braga, Viseu, Guarda and Aveiro; deliberately no GPS, no guarantees of access, booking or opening hours.

The original 24 candidates and the newer 13 candidates total **37 conditional INSERTs**, guarded with NOT EXISTS + ON CONFLICT DO NOTHING. **0–37 actual new rows** after deployment, never claim 37 confirmed or assume precise addresses/GPS. No automatic sport enrichment for the 169/228 historic facilities without sports in the earlier pilot analysis.

## Existing-venue discovery & diagnostics
- Searching Cascais in `GET /api/venues` and `GET /api/venues/suggest` also searches known Cascais parish names, using bounded queries and preserving original user city/address fields.
- Admin-only `GET /api/admin/venues/quality?limit=50` reports aggregate completeness by original locality and district, with limit 1–100 and ADMIN/SUPER_ADMIN authorization; no venue full-table read, coordinates, or user records.
- `VenueRepository` intentionally includes both the new municipality queries and the new aggregate query from separate PRs.
- Removes the legacy `PilotNationalVenuesImportOnReady` application-ready listener, which otherwise inserts two hard-coded OSM venues at restart if absent. Existing records remain untouched; explicit import/admin controllers are preserved.


## Extra 13-candidate municipal preflight (20 October 2026)

The separate `preflight-national-municipal-batch-13-READONLY.sql` covers all thirteen proposed 2026-10-20 rows. `docs/venue-imports/national-municipal-batch-13-live-preflight-20261020.md` records the owner's earlier live **SELECT-only** results (13 rows, `NO_MATCH_FOUND` under source/name/address rules); that is not proof against aliases or facilities within the same sports complex. **The São Jacinto pool entry was corrected after that run and needs a fresh single-candidate preflight.** The bounded SELECT-only query `src/main/resources/db/changelog/venue-imports/preflight-sao-jacinto-READONLY.sql` also flags possible name aliases in São Jacinto/Aveiro, and is paired with contract and disposable PostgreSQL 16 regression tests. The production result remains **unknown** until that query is run on the real catalogue; do not substitute simulated fixture results. Follow the documented backup, PostgreSQL 16 and deduplication gates for **all 37 candidates**; this documentation update has not changed the live database or authorized deployment.


## PostgreSQL 16 Liquibase proof (GitHub Actions, 20 October 2026)

- [**Workflow #39427378090 — success**](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39427378090) on commit `ab45fce`, using `postgres:16` and Liquibase 4.33.0 with the **five actual venue-release YAML changeSets and SQL files**, not the draft scripts. The test used an isolated synthetic venue table and five preexisting community fixtures; it did **not** connect to the Render database.
- First update recorded **5 new changeSets**; the fixture had **36 new proposed venues** after respecting one preexisting same-name/city community venue (out of 37 conditional proposals). Second update recorded **0** new changeSets, preserved the venue count and prevented duplicate inserts.
- Assertions covered preserving community address/source, not overwriting nonempty district, applying known-city backfill, and bounding Cacém district inference by coordinates.
- This closes the **isolated PostgreSQL 16 SQL/Liquibase smoke-test gate**; it does **not** establish equivalence to the full production schema, safe rollout on the live 228-row catalogue, or verified backup/restore. Those remain release blockers.

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

## Original unified read-only preflight for the first 24 candidates

Before any merge, run `src/main/resources/db/changelog/venue-imports/preflight-24-venues-READONLY.sql` using a **SELECT-only role** against the current Render production catalogue. This query enumerates the 24 proposed source references directly from the three INSERT scripts and returns matching existing records by source reference, exact normalised name/city, or short-distance GPS proximity. A `NO_MATCH_FOUND` result is **not** a confirmation of non-duplication: spellings, shared complexes, geocodes and alternate sources still require manual review. There may be several rows per candidate. The lateral match window is capped at 20 results per candidate.

The test `scripts/test_venue_release_preflight.py` statically checks that the preflight covers the first 24 source IDs and contains no SQL mutations. The venue migration integration CI now uses a disposable PostgreSQL 16 test fixture; this is **not** a fresh live production duplicate preflight.


## Render database backup and exact release gate (reviewed 19 October 2026)

The active `desporto24-db` compute plan reported by Render is `0.1c-256mb`, which is a **paid** compute plan, not Render's distinct `free` compute plan. Render documents PITR and on-demand logical exports for paid Postgres, but the Render connector does **not** expose recovery/export status or actions here. **No actual backup was verified or created by this change.**

1. In the Render Dashboard open workspace **Jynlo → desporto24-db → Recovery**. Confirm the exact recovery window and that the intended restore point exists. Do not infer recoverability from the database being reported as `available`.
2. Create and **download a logical export** before the schema/data release if the Recovery panel offers `Create export`. Keep the export independently and check that it contains the expected database/schema. Do not initiate point-in-time restoration here: a recovery spins up an additional billable database.
3. Run the **read-only** query `src/main/resources/db/changelog/venue-imports/preflight-24-venues-READONLY.sql` against the real catalogue, using a SELECT-only DB credential and SSL. It produces potentially more than one row per proposed venue. Record all 24 reviewed references, resolving names/aliases and candidates within the geographic warning rectangle. A zero-row match is *not* a guarantee that no duplicate exists.
4. Inspect `district IS NULL OR BTRIM(district)=''` by city, and review at least the 114 named mappings; the backfill changes district data even when no proposed new venue is inserted.
5. The five release Liquibase changesets have passed two updates on a disposable PostgreSQL 16 schema in GitHub Actions (#39437004073): 5 changeSets on the first run, 0 new changeSets on the second, with the fixture's community records and filled districts preserved. Production-schema compatibility and backup/recovery verification remain mandatory.
6. Only after the evidence and tests pass, take an explicit publish decision and run **one** backend deploy, observing the Liquibase logs. Check the before/after source-reference inventory, the admin `/api/admin/venues/quality` counts and the district-based venue suggestions. If an issue is found, **stop further deploys** and use the verified recovery route; avoid a speculative DELETE rollback.

Render documentation: https://render.com/docs/postgresql-backups ; https://render.com/docs/compute-plans .

### Source quality consistency regression
The existing `coverageByDistrict` summary previously counted `sports = ''` as empty, but omitted `sports = ' '`. The new admin quality query correctly uses `TRIM(sports)`, so the two aggregates could disagree. This release now consistently uses `TRIM(sports) = ''` for both; `VenueQualityByCityRepositoryTest` includes a whitespace-only sports case. Java and isolated PostgreSQL 16 migration suites have executed successfully in GitHub Actions; production preflight/backup are separate gates.

## Manual owner read-only queries: exact release decision inputs

The owner can run `src/main/resources/db/changelog/venue-imports/preflight-venue-baseline-READONLY.sql` in pgAdmin or DBeaver using their own credentials. This file contains **four SELECT queries only**: catalogue counts, per-district counts, top 30 incomplete localities, and discovery of Liquibase tracking table schema. Export **only query-result CSVs or screenshots** (no credentials, connection strings or secrets).

Next run `src/main/resources/db/changelog/venue-imports/preflight-24-venues-READONLY.sql` — also read-only — to compare exactly the 24 proposed venue IDs/names/coordinates with production rows. Results can have multiple matches per proposal. Treat `NO_MATCH_FOUND` as inconclusive for aliases; manually review and decide. Both preflight scripts are outside the active Liquibase master and do not run on startup.

PostgreSQL 16 migration CI has passed on a synthetic fixture, but do not deploy until production backup/restore availability and final live duplicate checks are verified. Database networking does **not** need to be opened.


## Results received from owner — production preflight, 19 October 2026

These are **results of read-only SQL personally run by the owner** against the currently available Render PostgreSQL catalogue; they are not a staging test or authorization for INSERTs.

- Current venue count: **228**. Without sports: **169**; without district: **3**; without coordinates: **0**. `information_source`: **226 OFFICIAL**, **2 OPENSTREETMAP**, **0 COMMUNITY**.
- Unified 24-venue preflight: **24 rows, all `NO_MATCH_FOUND`** by source-reference, matching normalized name + city, or near-location rectangle when coordinates exist. No matched existing id was returned. This result establishes **no match under the query's criteria only**, not a complete deduplication guarantee. Particularly, **14 of the proposed 24 have no coordinates** and therefore were not spatially checked.
- Backfill preview: main mapping **1 Coimbra → Coimbra**, **1 Porto → Porto**; Lisbon geographic mapping **1 Parede → Lisboa**. Exactly **3 historical rows** qualify for district backfill, matching the baseline count. No current district values would be overwritten.
- New venue insert proposals: **10 have coordinates** (nine Portalegre OSM + one Santarém official) and **14 have `NULL` coordinates**. The existing frontend `venue-details.component.html` renders its Google Maps link only if *both* coordinates exist. **Those 14 cards would have no coordinate-based map link** unless data is enriched or a suitable UI fallback is implemented.

### Release interpretation
Read-only duplicate and backfill queries **completed**, but the matches are inconclusive for aliases/compound complexes; manual source/nearby review remains. The existing 169 records without sports must **not** be inferred as suitable for sport filters. To work on them independently, execute the new SELECT-only `src/main/resources/db/changelog/venue-imports/preflight-missing-sports-READONLY.sql`, export its result grid as CSV and cross-check official/OSM descriptions before generating UPDATE changesets. Do not apply blind venue-type-based sport updates.

The following are still **BLOCKING**: verified Render database backup/restore path, final preflight for the corrected São Jacinto entry, manual duplicate/source review especially candidates without GPS, representative production-schema compatibility review, and explicit authorization before a production migration. Java and isolated PostgreSQL 16 Liquibase tests have now passed. No production data was changed.
