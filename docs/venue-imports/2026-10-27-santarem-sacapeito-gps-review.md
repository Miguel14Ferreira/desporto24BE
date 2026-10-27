# Santarém — Piscinas Municipais do Sacapeito, existing Jynlo ID 276 (27 Oct 2026)

**Research only. No coordinates inserted, modified or approved.**

Existing public Jynlo `GET https://api.d24sports.com/api/venues/276`: Piscinas Municipais do Sacapeito, Santarém, **Praceta Cidade de Badajoz, Urbanização do Sacapeito, 2005-213 Santarém**, source OFFICIAL, no saved GPS, stored website `https://viversantarem.pt/instalacoes-desportivas/`. Exact facility is not the separate Complexo Aquático Municipal at Jardim de Cima, nor pavilhões 277/278.

### Independently matching facility-site sources
1. Operator Viver Santarém: https://viversantarem.pt/piscinas/ confirms the venue, sport and matching street/address.
2. Official local tourism resource: https://www.visitesantarem.pt/lista/piscinas-municipais-do-sacapeito/ confirms the named facility at Praceta Cidade de Badajóz; its HTTPS page links a **directions waypoint `39.2250178,-8.6886809`**. The HTTPS venue page is the research source, *not* certification of a pedestrian entrance.
3. OSM **way 1172748137**, a named swimming-pool building with `leisure=swimming_pool`, `operator=CMS`: https://www.openstreetmap.org/way/1172748137; independent OSM-derived https://mapcarta.com/pt/W1172748137 reports **rounded** reference `39.225,-8.68865`. The two references differ by approximately **3.3 metres**; do not interpret this as centimetre-level accuracy.

### Safety and admin-only presentation
The `ADMIN/SUPER_ADMIN` read-only missing-GPS review responds with the existing optional `researchCandidate` record for ID **276**, site reference **39.2250178,-8.6886809**, municipal directions HTTPS page, OSM building link, precision `SITE_REFERENCE_NOT_VERIFIED_ENTRANCE`. The list is an immutable, code-reviewed map of eligible venue IDs (now 273 and 276). A candidate requires **exact identity, OFFICIAL status, saved approved HTTPS website, matching street fragment, city/district and *both* GPS components absent**; invalid/changed records get null. No data is inferred from addresses on the fly. No geocoding, DB write, migration or approval. The administrator must independently confirm actual site suitability and manually submit a GPS suggestion requiring source HTTPS and later explicit approval.

Two review evidence points added under `santarem-sacapeito-2-site-gps-review-20261027.json`, both **approved_for_import=false**; offline report reports references and warnings but never coordinates to apply. Source-only audit records are never automatically imported.
