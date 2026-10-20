# National venues — independent source corroboration, 19 October 2026

**Read-only and not approved to import.** This review adds sources for 11 entries already in the 97 Portalegre OSM candidate file, and discovers 13 further named installations across Évora, Castelo Branco and Santarém.

- `portalegre-11-municipal-sources-20261019.json`: 11 OSM identity/existence corroborations (11 are **inside the existing 97**, not 11 new OSM objects). OSM coordinates were already matched to CAOP. Most have unverified address/opening.
- `other-districts-13-official-facilities-20261019.json`: 13 independently documented venues from 3 additional districts, **not** OSM extracted. Official municipal names, addresses and sports (where explicitly listed) are preserved; missing sports and GPS stay missing. The Santarém aquatic complex's official GPS location was checked against CAOP2025 official polygons and falls in Santarém municipality/district.
- Do not infer access or booking availability: Évora's pitch/athletics may be reserved for clubs, Castelo Branco outdoor pools are seasonal, the school pavilion in Santarém is not generally open.
- Do not convert these review manifests to API requests or seed SQL automatically. A real importer needs stable OSM IDs or defensible source IDs, spatial duplicates checked against the real production catalogue, and a controlled approval process.
- **No extra OSM PBF district extracts were processed.** The public mirror offers all 18 district PBFs but this environment cannot download binary extracts. `prepare_national_venues.py` is the local resumable staging workflow.

Attribution: OpenStreetMap contributors (ODbL); DGT CAOP2025 (CC BY 4.0). Municipal content is cited using source URLs per record; no copied text.
