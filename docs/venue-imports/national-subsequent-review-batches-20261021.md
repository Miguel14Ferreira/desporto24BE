# Jynlo: Review batches after the first 50 (without new OSM collection)

Input is the SHA256-pinned and committed 3514-candidate national dataset at docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz.

The first-50 selector remains unchanged. The new tool deterministically replays that selection to exclude the same 50 OSM reference IDs, then prepares subsequent review batches. It prefers up to seven candidates per district, but when too few districts have eligible facilities it relaxes the quota rather than returning an incomplete second batch.

Page 2 (50 candidates) uses only Aveiro 14, Braga 6, Lisboa 15, Porto 15, because the 128 candidates passing the **strictest** postal-address and venue-type filters have already been concentrated there after the first batch. This does not mean other districts lack sports venues: there are **2,959 candidates with unconfirmed postal addresses** and 130 possible duplicates that require separate independent validation. Page 3 has 28 under the current strict filters.

Command:

    python3 scripts/prepare_subsequent_venue_review_batches.py --full-details docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz --out-dir /tmp/jynlo-page2 --page 2 --limit 50

Outputs are CSV and summary JSON. All records have decision UNDECIDED and are for manual independent source review only. The code has no network or database access, does not set approved=true, cannot produce Liquibase migrations, and does not touch Render or production. OSM contributors (ODbL) and DGT CAOP2025 (CC BY 4.0) attribution as documented with the underlying data.
