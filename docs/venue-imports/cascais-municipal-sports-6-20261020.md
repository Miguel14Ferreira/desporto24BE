# Cascais owner catalogue — second municipality/association corroboration batch

Date of source review: 20 October 2026. Based on the **169** owner-exported Render database records whose `sports` values were blank. These six are **additional** and disjoint from the previously staged fourteen explicit-name and five municipal-source proposals. All values are *proposals*, not updates to production.

| Production venue id | Exact exported name | Proposed sport labels | Reference | Caveats |
|---|---|---|---|---|
| 50 | Pavilhão Desportivo dos Lombos | Futsal;Basquetebol;Corfebol;Andebol;Voleibol | [Fonte](https://www.cascais.pt/sites/default/files/anexos/gerais/new/cp_centro_recreativo_e_cultural_quinta_dos_lombos_p212-2020.pdf) | Matches Rua da Feitoria / 2775-568, documented pavilion use; sports are documented for 2020 and may need refreshed operating information. |
| 174 | Parque Decathlon | Futebol;Petanca;Escalada;Skate;Patinagem | [Fonte](https://www.cascais.pt/contactos/decathlon-de-cascais-parque-desportivo) | Exact avenue match. Municipality lists a football field, pétanque, climbing wall, skatepark and skating rink. Postal code conflict: owner CSV 2645-019 vs municipality 2755-019. |
| 109 | União Recreativa e Desportiva de Tires | Futebol | [Fonte](https://www.cascais.pt/contactos/uniao-recreativa-e-desportiva-de-tires) | Owner CSV Rua do Campo de Futebol / 2785-629; municipality documents football pitches at Parque de Jogos Dr A. F. Santos Neves. Does not equate club-wide BTT or darts with football-field amenities. |
| 145 | Parque Desportivo Abel Viegas Lopes (Grupo Desportivo e Recreativo das Fontainhas, Cascais) | Futebol | [Fonte](https://afl.pt/2025/05/08/6o-encontro-de-walking-football/) | 2025 AFL event specifically hosted at Parque Desportivo Abel Viegas, Fontaínhas (football), supporting venue match; club municipality page calls its pitch 'Campo de Futebol Abel Lopes'. Identity/name and address should be double-checked. |
| 237 | Desportivo Monte Real | Karaté;Ténis de mesa | [Fonte](https://www.cascais.pt/contactos/desportivo-monte-real) | Same 2785-141 postcode and named club; municipality lists Karaté and Ténis de mesa alongside polidesportivo, ginásio and sala polivalente. Address given as Rua João de Deus 121 vs owner Parque de Jogos; manual site verification required. |
| 142 | Grupo Recreativo e Dramático 1º de Maio de Tires - Pavilhão Desportivo Serafim Tomé dos Santos | Ginástica;Trampolim | [Fonte](https://www.cascais.pt/noticia/inauguracao-das-obras-de-requalificacao-da-sede-do-1o-de-maio-e-do-pavilhao-serafim-tome-dos) | Municipality documents acrobatic gymnastics and trampolines for the association's pavilion AND headquarters collectively, not specifically which room; generic catalogue 'Ginástica' stands in for acrobatic gymnastics. CSV Rua do Mercado 156 vs municipal Rua Domingos dos Mártires 222: needs location confirmation. |

### Review and quality rules
- `50`: Futsal, Basquetebol, Corfebol, Andebol and Voleibol are documented in an official municipal pavilion contract from 2020; venue address matches, but check current sport availability with the managing club.
- `174`: Municipality facility components support five sports, not an assertion that each activity is currently offered for public booking; **postcode differs from export**.
- `109`: Match site by *football field* and postcode; do not add all team sports of the club to one field.
- `145`: The football association confirms a 2025 event on the named site, but 'Viegas Lopes' vs 'Abel Lopes' and the CSV street need further entity alignment. Do not over-classify 'Walking Football' as a new catalogue sport without a product decision.
- `237`: The municipal club modalities are not proof that each is practiced in the exact site represented by the export. Source address differs; confirm before approval.
- `142`: Generic 'Ginástica' is an **approximate** catalogue mapping from 'Ginástica Acrobática'; activities are attributed collectively to both the group headquarters and the pavilion. Address differs significantly; this proposal is **strictly manual-review-only** until venue-specific evidence is obtained.

### Guardrails
`preflight-cascais-municipal-sports-6-READONLY.sql` is a SELECT-only exact-match query. `draft-cascais-municipal-sports-6-NOT-ACTIVE.sql` is a *standalone inactive* draft guarded by ID, exact name, exact source reference, OFFICIAL and empty sports. Neither is registered with Liquibase or was executed against production. The six proposals are subject to verified backup/restore, current-source/identity checks, PostgreSQL 16 tests and explicit owner approval.

After this research, the 169 missing-sport rows are partitioned for **review purposes only**: 14 name-explicit, 5 previously municipal-sourced, 6 in this batch, 12 awaiting a precise catalogue/activity mapping or source verification, and **132 remaining ambiguous**. **The actual production count remains 169** until an approved deployment changes it. Some already-staged proposals also require further verification: do not claim all 25 are ready to apply.

The owner CSV and any personal, connection or API secrets must not be committed. Public sports/source metadata only.
