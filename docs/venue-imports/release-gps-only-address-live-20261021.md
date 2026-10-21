# Jynlo controlled release: GPS-only venue address (2026-10-21)

**Backend Render LIVE** service `desporto24-api` follows `feature/desporto24-preview-deploy`, not `develop`, with **autoDeploy=NO**. The backend LIVE Git SHA checked for this release plan was `12cd8260c21ad79844aecb24e84cd6a614de13e4`.

**Frontend Render LIVE** `desporto24-web` follows `preview/render-fullstack`, with **autoDeploy=NO**. FE had **exactly 2 newer commits** after the LIVE SHA `3460f1fd13c7427330b72ec89876009b4de27b01`, implementing GPS-only display and address-optional creation. The FE will be deployed **only after** backend success.

The BE `develop` was ahead of the backend LIVE branch by **12 commits (61 files)**, mostly nation-wide OSM/CAOP audit tools. **Do not merge all 12 commits into the production branch just to ship the GPS-only functionality.** This scoped PR contains only necessary venue model, DTO, service, migration and tests.

## Release gates (NO deployment in this PR)

1. Take an authorized backup of Render Postgres, verify it can be restored safely in a separate environment and ensure available rollback instructions. Do not share credentials in logs or GitHub PR comments.
2. Run `src/main/resources/db/changelog/venue-imports/preflight-gps-only-address-READONLY.sql` in the real DB via a secure connection. The query uses `BEGIN TRANSACTION READ ONLY` and ends `ROLLBACK`. **Require `rows_that_would_block_migration = 0`**; note the current venue count, actual column types and existing CHECK status. The last confirmed catalogue count was 265 but it may have changed.
3. Check migration files are packaged and Liquibase is configured for the same PostgreSQL DB/schema. Only the schema migration `2026-10-21-venue-gps-only-address-nullable.yaml` should apply: `address DROP NOT NULL` + `ck_venue_address_or_mainland_gps`. It has no INSERT/UPDATE/DELETE.
4. Merge this PR after required checks pass, then **manually trigger only the backend LIVE service deployment** and monitor migration + health. Validate `GET /api/venues`, sample venue details with and without addresses, existing sessions and venue autocomplete. Verify CHECK constraint and no unintended venue changes.
5. **After backend success**, manually deploy the frontend `preview/render-fullstack` service and check both ordinary postal-address creation and GPS-only creation with valid coordinates and visible “Morada por confirmar” in the desktop UI. Avoid creating test rows in production unless the owner authorizes that action; use a disposable test DB instead.
6. Observe the running apps; on app rollback, note that `address=NULL` rows created after rollout prevent simply re-adding NOT NULL. Revert code first, evaluate data in backup/test, and never erase live user locations merely to roll back.

This release does **NOT** approve, import or publish 2 959 OSM candidates with unconfirmed postal addresses. Keep OSM source audit and attribution separate from this schema capability. No payment or reservation workflows are introduced.

**The PR code itself does not deploy services or modify the database.**
