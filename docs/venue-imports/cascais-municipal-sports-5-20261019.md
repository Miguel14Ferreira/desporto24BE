# Municipal-source review of formerly ambiguous Cascais venue sports — 19 Oct 2026

Source dataset: owner-provided 169 venue rows with no sports on production. This review is limited to **five of the 143 ambiguous-name rows**. All five have independent Câmara Municipal de Cascais evidence connecting the venue (or its facilities) to the suggested activity. The other **138 ambiguously named rows remain unverified**, including schools and headquarters; do not infer sports from entity names.

| Venue ID | Original DB name | Candidate sports (existing SportsCatalog) | Official source | Validation note |
|---|---|---|---|---|
| 43 | Complexo Desportivo de Alcabideche | Jiu-Jitsu;Karaté;Natação;Ténis | [CMC](https://www.cascais.pt/contactos/complexo-desportivo-de-alcabideche-0) | Explicit municipality modality list; Rua do Pombal and postcode 2645-074 match. |
| 111 | Complexo Desportivo de São Domingos deRana | Musculação;Ténis | [CMC](https://www.cascais.pt/complexos-e-pavilhoes-desportivos) | Current municipality facilities list explicitly mentions tennis courts and weight-training gym; Rua das Travessas matches; postal code differs in source and export, review before release. |
| 134 | CERCICA -  Cooperativa para a Educação e Reabilitação de Cidadãos Inadaptados de Cascais | Natação | [CMC](https://www.cascais.pt/piscinas) | CERCICA pool in municipality's pools directory at Rua Principal 320 and 320 A (2765-383). |
| 132 | Clube Naval de Cascais | Mergulho;Vela | [CMC](https://www.cascais.pt/contactos/clube-naval-de-cascais-0) | Municipality club page explicitly lists Vela and Mergulho; Esplanada Príncipe D. Luís Filipe matches. |
| 128 | Clube de Petanca de S. Pedro do Estoril | Petanca | [CMC](https://www.cascais.pt/contactos/clube-de-petanca-de-sao-pedro-do-estoril) | Municipality lists Petanca and a dedicated Petanca park at Rua Parque da Petanca. |

## Quality/availability caveats

- **111 São Domingos de Rana**: official facilities list says Rua das Travessas 57 / postcode 2785-210; the owner export says Rua das Travessas / 2785-285. Match is likely, but postal code conflict requires manual review before approval. Only directly supported Ténis and Musculação are proposed, not all activities allegedly possible in a generic pavilion.
- **134 CERCICA**: Natação is listed in official pool data. It is a rehabilitation/education organization; do **not** imply users can book its pool freely. Hydroginástica/Natação Adaptada were not added blindly as new catalog activities.
- **43 Alcabideche**: choose only sports with exact approved catalogue labels in the municipality's official programme; it additionally lists other classes not represented in this selection.
- **132 Clube Naval** and **128 Clube de Petanca**: association and venue are identified by name and municipality address; do not infer public availability or booking/payment rights.
- The municipal listing itself warns information can change; the URLs are research evidence, **not a current availability feed**.

## Staging (no DB writes)

- `preflight-cascais-municipal-sports-5-READONLY.sql` previews exact identity, source and blank-sports guards.
- `draft-cascais-municipal-sports-5-NOT-ACTIVE.sql` is a standalone **draft UPDATE**, NOT included in master. Never run it directly in production.
- The earlier 14 name-based proposals are different records and remain subject to their own review.
- As at 19 Oct, preflight for these five has **not yet been run** on production. Counts are potential only: in a future approved release, the number of updates may range from 0 to 5.
- Need evidence of backup/restore, a fresh review of original source records, PostgreSQL 16 Liquibase tests and explicit owner authorization before any production change.

Reference: [Cascais official sports venues](https://www.cascais.pt/complexos-e-pavilhoes-desportivos) and linked individual source pages above.
