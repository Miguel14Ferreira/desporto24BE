# Jynlo next municipal batch — 32 proposed venues (21 October 2026)

**Not LIVE, not approved, not included in the Liquibase master.** The already-published catalogue has **265 venues** according to the owner. The 32 entries are staged and will not execute on startup until a separate explicit activation change.

- 14 Setúbal district: current official municipal [sports facilities list](https://www.mun-setubal.pt/equipamentos_desport/) (2026); each entry has a source-specific municipal URL in the machine-readable manifest. May include restricted school sports facilities and seasonal pools.
- 15 Leiria district: [municipal 2022 patrimonial inventory PDF](https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf), specifically Annex B pages 101–102 of the PDF (0-index 100, 101). These entries **prove historical registration only**; recheck operational status before publication. Sports are NULL for halls unless the primary source explicitly establishes a discipline.
- 3 Bragança district: [municipal sports facilities refurbishment](https://www.cm-braganca.pt/transparencia/comunicacao/noticias/noticia/municipio-de-braganca-investe-na-melhoria-de-infraestruturas-desportivas) and public municipal sporting competitions; exact streets and room-specific use are **not verified**.

### Data quality and policy
- Database schema requires a nonempty address: where street not verified, the stored value states explicitly **morada por confirmar**, or provides a source-backed local area or institutional site. **This is NOT an exact geocoding address.** All GPS fields remain NULL; do not invent a map marker. Municipal URLs retained in `website` for users to get reliable details.
- No inferred opening hours, prices, free access, available bookings, parking, accessibility or changing rooms. Neither a sports venue listing nor a school building proves the general public may reserve it.
- Same-street complex constituents (e.g., pool/pavilion in Maceira or Caranguejeira) can be genuinely separate; alias preflight flags possible matches for human review but does not delete or collapse them.
- References are **internal Jynlo deterministic IDs**, not official government-issued identifiers. Same source ref or same normalized name + same city protects exact duplicates. No UPDATE/DELETE, and no automatic bulk import.

### Approval steps
1. Back up the **current** live catalogue, not only the older 228-venue snapshot, then prove restore in isolated PostgreSQL 16 using `scripts/verify_venue_backup_restore.sh` with `JYNLO_RESTORE_EXPECTED_VENUES=265` (or actual current total).
2. Run `src/main/resources/db/changelog/venue-imports/preflight-municipal-next-20261021-READONLY.sql` on production with a read-only user. Review rows including aliases (old names) and collisions at shared addresses. Expect one row per candidate only when nothing matches.
3. Verify operational availability, exact position or acceptable location text for each venue, especially 2022 Leiria entries. Decide whether partial-location records are good enough for public listing or should stay staged. Validate sport selections against session sport catalogue.
4. Only after a live preflight and explicit approval, add the above YAML into `db.changelog-master.yaml` through a reviewed release update and deploy manually in Render (autoDeploy currently off).

Existing application was not deployed by this PR. Official links were checked by browsing on 21 October 2026. No current Google Maps pin is claimed.
