# Seven source-checked venues from Pestana's September inventories

These seven facilities are extracted from two **user-owned email attachments**:
- `DESPORTO24_Inventario_Futebol_Futsal.xlsx` (82 entries);
- `DESPORTO24_Inventario_Nacional_Padel (1).xlsx` (107 entries).

All seven have a government or municipal operator page corroborating identity and sport. Manifest: `docs/venue-imports/pestana-7-source-checked-20261019.json`. Do not copy the whole inventories into the repository.

This is not an authorized production write. It is the next **seven conditional INSERTs** after the 17-record migration in PR #118. The merged changelog is additive: 17 original conditional inserts + 7 inventory candidates (24 potential new rows), plus the legacy-district backfill. The final inserted number may be lower if there are existing venue matches.

- Porto: 4 football facilities in Porto and one municipal polidesportivo in Maia.
- Lisboa: Polidesportivo de Arroios.
- Coimbra: Campo de Padel do Fojo in Mira.

No coordinates were created for any of these seven. All venue GPS fields are NULL. Sports are taken from the municipality's own facility descriptions; catalog tokens match Jynlo's `SportsCatalog`. `website` retains the source URL. Availability, price, reserve rights, opening hours and current state cannot be inferred.

Source reference `JYNLO:CATALOG:PT-FUT-...` / `JYNLO:CATALOG:PT-PAD-...` is a Jynlo-internal stable reference to the workbook, *not* a verified OSM ID, licence or partnership claim.

## Release gate
1. Run `src/main/resources/db/changelog/venue-imports/drafts/2026-10-19-pestana-seven-preflight-READONLY.sql` against PostgreSQL production before merging.
2. Check aliases and existing venues in the same complex. Do not merge nearby pitches automatically.
3. Confirm Render PostgreSQL backup and test full PostgreSQL 16 + Liquibase migration.
4. Confirm FE attribution and UX handling of venue cards without GPS.
5. Run `scripts/test_pestana_seven_venues.py` offline, then deploy the approved combined branch *once*.

Sources: official municipalities and publicly available operator sites (URLs in the record manifest). Incomplete inventory entries stay for manual review and are not added here.
