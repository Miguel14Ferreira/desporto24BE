# Jynlo national reconciliation against the public venue API (21 Oct 2026)

One-shot read-only GitHub Actions workflow: `.github/workflows/venue-national-public-catalogue-reconcile-20261021.yml`.

## Input
- SHA-verified, 18/18-district OSM/CAOP preview from GitHub Actions run **39597754588**: 3,514 nationwide source candidates (`manual-review-candidates.jsonl.gz`).
- **Unauthenticated public** GET `https://api.d24sports.com/api/venues` (fallback to the confirmed Render service URL). It reads the public venue catalogue in memory and immediately projects **only** `id,name,city,district,address,latitude,longitude`. Usernames, photos, created-by fields and other API response data are **never written to artifacts**.
- Safety gate requires >=265 distinct public venue IDs (the last owner-confirmed production baseline). Detecting fewer or malformed API data fails rather than presenting a partial catalogue as complete.

## Output
- `national-live-reconciliation-summary.json`: counts by `POSSIBLE_DUPLICATE` and `UNMATCHED_REQUIRES_REVIEW`. **No entry is approved automatically**.
- `national-live-reconciliation-review.csv`: 3,514 source references/names, geographic coordinates and existing public venue IDs for flagged matches, **no users**.
- Artifact expires after **one day**, never commits output to GitHub and never triggers Render deployment.
- Works with existing `scripts/reconcile_national_stage.py`; the 70m **candidate-candidate** OSM review remains separate from the 100m **candidate-LIVE** check.

**Known limitation:** `VenueResponse` public API has no `sourceReference`; projected CSV fills `source_reference` with empty string. Thus exact OSM source-reference deduplication is **not performed** by the public API comparison. A separate, authorized production **read-only preflight** must check these references before any import. Name/municipality and 100m geoproximity flags are conservative review hints, not an identity judgment. Also compare the 32 pending municipality-sourced candidates in PR #142 before release.

No SQL, user credentials, token, admin API, `--apply`, migration change, backup manipulation, or database writes are involved. The canonical 265 LIVE venues remain unmodified.
