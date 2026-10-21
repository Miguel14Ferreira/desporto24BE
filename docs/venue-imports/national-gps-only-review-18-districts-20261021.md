# Jynlo: 18-district GPS-only venue review (21/10/2026)

## Why

The national OSM/CAOP2025 dataset preserves **3514 facility candidates**, of which **2852** are under the stricter `ADDRESS_REVIEW` gate (GPS but no postal address). Another 107 of the 2959 address-uncertain candidates fall under separate duplicate/existing-reference gates. **Do not confuse missing postal address with missing GPS coordinates.** All 18 mainland districts contain potential sports facilities that could be reviewed individually, even when no postal street is available.

This offline script does **NOT reverse-geocode** or insert pseudo street names. It marks `postal_address_status=UNKNOWN_GPS_ONLY`, with coordinate-sourced OpenStreetMap links. Existing venue publication storage currently uses a required address field; that must be handled explicitly in a future UI/API/schema change, NOT by inventing an address.

## Guardrails

- Read-only input: `docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz`, pinned SHA256 `e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a`.
- Select only `importGate=ADDRESS_REVIEW` entries with an actual usable sport facility type/sport, non-generic name (excluding ambiguous labels such as Pavilhão 1 and school/private-club indications), plausible Portuguese GPS coordinates; exclude known LIVE references and possible duplicates in other gates.
- Prefer fields and multi-use sporting facilities over gyms, golf courses and closed/private-labelled locations. The score is **only for prioritizing review**, not independent confirmation or access authorization.
- First queue: **5 sports venue candidates from each of Portugal mainland's 18 districts**, diversifying municipalities and avoiding stacking locations in one complex (120 m).
- The 32 municipally sourced staged installations in Bragança, Leiria, Setúbal still require separate cross-source alias reconciliation (the CSV flags those districts).
- Nearby OSM candidates within 150 m are flagged; they might be separate sports courts inside the same facility.
- All records are **review-only**: no official identity verified, no operation or access evidence, `approved_for_import=NO`. This file is NOT valid input for Liquibase.
- No network calls, download, git writes by CI, Render deploy, PostgreSQL connections, migration or approval.
- OSM ODbL and DGT CAOP2025 CC BY 4.0 attribution required.

## Usage

```bash
python3 scripts/prepare_national_gps_only_review.py \
  --full-details docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz \
  --output-dir /tmp/jynlo-gps-review --per-district 5
```

Outputs: `national-gps-only-review.csv` (semi-colon, UTF-8-BOM; GPS links and verification columns) and `national-gps-only-summary.json` (counts). Publication must wait for independent venue verification, duplicate checks against current LIVE, operator/use rules and explicit per-venue approval.

The goal is national discoverability for **sports-session organizers**, not allowing the app to charge or reserve facilities.
