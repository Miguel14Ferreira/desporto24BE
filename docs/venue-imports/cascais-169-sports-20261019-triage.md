# Source-name venue sports triage — owner export of 19 October 2026

**No production changes have been applied. Do not merge/deploy as permission to run a migration.**

Source: owner-provided CSV of 169 current `desporto24.venue` rows missing sports, read from Render Postgres with a SELECT. Dataset: Câmara Municipal de Cascais [Equipamento Desportivo](https://dadosabertos.cascais.pt/pt_PT/dataset/geocascais-equipamentodesportivo); all 169 have `OFFICIAL` and `CASCAIS:SPORTS:` refs, coordinate and district values. All are in the municipality of Cascais: 61 U.F. Cascais e Estoril, 41 São Domingos de Rana, 37 Alcabideche, 23 U.F. Carcavelos e Parede, 7 Cascais. No personal/user rows involved.

| Category | Count | Next action |
|---|---:|---|
| Explicit modality in venue name, existing SportsCatalog entry | 14 | Manual source verification, approval, then staged guarded SQL |
| Activity explicit but unavailable as exact SportsCatalog entry | 12 | Decide if Hipismo, Teqball, Ténis de praia, Bowling and obstacle-course racing/OCR belong in catalog; then validate individual venues |
| Indeterminate generic/school/association/fitness/club name | 143 | Verify official record, actual space, public access and available sports; **do not infer** football/futsal/basketball etc. for generic 'Polidesportivo' |

Approved-sport candidate distribution: Calistenia **8**, Surf **2**, Yoga **2**, Escalada **1**, Mountain Bike **1**. These are **proposals identified from venue names** only, not proof the site is open to session bookings, that the sport remains active or that a specific court is available.

Potential duplicate worth manual inspection: IDs 40 and 222 both named `Grupo Recreativo e Familiar de Murches`, with different addresses/GPS and different official source references (possibly sede vs venue; do not delete or merge automatically). There may be other genuine brand/association aliases; check before treating venue count as distinct playable facilities.

Read-only verification SQL (safe): `src/main/resources/db/changelog/venue-imports/preflight-cascais-explicit-sports-14-READONLY.sql`.

**Not wired to Liquibase:** draft mutation `src/main/resources/db/changelog/venue-imports/draft-cascais-explicit-sports-14-NOT-ACTIVE.sql`; exactly 14 conditional updates guarded on exact `id`, source reference, name, source type and missing sports. A future release must convert approved proposals into an independently reviewed/registered Liquibase changeset only after verified backup/restore, integration tests and explicit owner authorization. The 14 have not been executed.

The original 169-row CSV is retained by the owner; we intentionally do not commit an entire production snapshot. Use the source references in a fresh read-only export for revalidation before release.
