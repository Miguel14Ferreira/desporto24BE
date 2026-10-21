# Optional venue postal address when valid mainland GPS is supplied

This changes a schema NOT NULL on venue.address to allow NULL, while a DB CHECK constraint requires either nonblank address text OR complete plausible continental Portuguese GPS coordinates.

- Public/user venue creation: name/city/sport rules unchanged; GPS-only create stores address=NULL; invalid/partial/out-of-mainland GPS rejected, even with an address.
- OpenStreetMap admin import: optional postal address but original mandatory GPS validation and review processes retained; do not bulk import unapproved OSM data.
- Name/city/address duplicate checks retained for postal addresses; GPS-only duplicates are rejected for same normalized name within 30m. Nearby different sports courts can remain distinct.
- Master Liquibase includes one schema-only changeset. Does not backfill, import, change, or delete the ~265 existing LIVE rows.
- Dedicated PostgreSQL16 disposable integration fixture executes the actual SQL migration, verifies existing addresses unchanged and enforcement of GPS requirement. Backend CI covers Java and validation.
- Backend migration must be applied before frontend allowing GPS-only address omission is deployed. Frontend itself should not be deployed before this PR is approved.

This PR does not deploy, migrate LIVE, or import any of the 2959 unverified OSM candidates.
