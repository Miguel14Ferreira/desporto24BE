# Portalegre — CAOP2025 validation of 97 identified OSM candidates (2026-10-19)

## Reproducible inputs
- Original 97-row `portalegre_97_para_verificacao.csv` from the Jynlo user pilot (not scraped from Google Maps).
- Official 2025 DGT **municipal and district** polygons, converted to EPSG:4326: 278 municipalities and 18 districts (provided separately by the owner, not committed to Git).
- A Shapely point-in-polygon match with every candidate's original OSM coordinates: exactly one matched municipality and exactly one district for each candidate.

## Measured results
| Measure | Result |
|---|---:|
| OSM objects under review | **97** |
| Inside expected CAOP2025 district/municipality | **97/97** |
| Mainland municipalities represented | **14** |
| Previously sourced first-batch draft | **9** |
| Other candidates with independent existence/name evidence | **13** |
| Remaining to review (not additional count) | **75** |
| Original addresses still marked «por confirmar» | **87** |
| Non-specific/generic OSM names requiring review | **13** |
| Objects with at least one other OSM point within 150 m | **39** |
| Exact normalized-name collisions within 150 m | **0** |
| Approved for production | **0** |

The **97** include both the nine source-checked draft entries and the thirteen separately corroborated identities. Do **not** sum those groups to 119. The 39 nearby elements are **not** proven duplicates: e.g. `Piscinas Descobertas` in Ponte de Sor includes individual pools within 15–50 m, and `Estádio Patalino` is ~102–114 m from nearby separate named pitches.

## Distribution by official municipality
| Municipality | OSM candidates |
|---|---:|
| Ponte de Sor | 23 |
| Elvas | 20 |
| Portalegre | 12 |
| Avis | 8 |
| Campo Maior | 6 |
| Sousel | 6 |
| Alter do Chão | 5 |
| Crato | 4 |
| Arronches | 3 |
| Fronteira | 2 |
| Gavião | 2 |
| Castelo de Vide | 2 |
| Marvão | 2 |
| Nisa | 2 |

## Release blockers
1. Geographic validation proves only that the coordinates lie inside the expected municipality; it does **not** prove operation, public availability, booking rights or exact address.
2. The 87 uncertain addresses must remain unknown rather than be fabricated.
3. The 39 spatial neighbours need review by facility/complex identity; no automatic merger/deletion.
4. The PostgreSQL production duplicate/alias preflight has **not** been executed due to Render-hosted MCP connection requiring SSL/TLS and unsupported source-IP allowlisting.
5. Until read-only duplicate checks and backup are done, no automatic SQL insertion or API import.

## Snapshot artifacts
In the Jynlo conversation, produced `jynlo_portalegre_lote_97_geovalidado_para_revisao.zip` with
- `jynlo_portalegre_97_staging_CAOP2025_20261019.jsonl` (full source references, source URL, normalized CAOP municipality, geocoordinates, quality flags, nearby candidates);
- `jynlo_portalegre_97_triagem_CAOP2025_20261019.csv`;
- `jynlo_portalegre_97_resumo_CAOP2025_20261019.md`.

They deliberately carry `approvedForImport=false` for every one of the 97 records. Copy reviewed approved batches into the separate bounded import workflow; do not turn all 97 into seeds by default.

## Provenance
© OpenStreetMap contributors (ODbL) and DGT CAOP2025 (CC BY 4.0).
