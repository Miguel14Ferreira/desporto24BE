# Jynlo: national OSM 3514 candidate snapshot - immutable review copy

This audit file is ONLY a catalogue of **potential sports venues**, not public venue approval, guaranteed accessibility, opening-hours proof, or bookability. Do not seed production from it.

Source: OpenStreetMap (ODbL) / © OpenStreetMap contributors; municipalities/districts classified using Direção-Geral do Território CAOP2025 (CC BY 4.0) / © Direção-Geral do Território. Respect database licensing and attribution obligations before publication.

Original 18/18 districts: https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39597754588
Live public catalogue comparison: https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39602965416
Full-detail recovery: https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39616305935

Original source SHA256: e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a.
Preserved data: docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz (GZIP 194,922 bytes).

Expected state: 3,514 original candidates; 10 previously live by exact OSM reference; 130 possible duplicates; 2,959 addresses flagged for review; no auto-approvals. The rest still require independent validation.

The one-shot GitHub workflow reads an EXISTING Actions artifact (not PBF), verifies SHA-256, reference uniqueness and all approval flags, then commits solely the compressed immutable review copy to this audit branch. No PostgreSQL credentials, Render API, migration include, or production write. This is a small retained source file and must NOT be used as an approved import set.

Offline read: python3 scripts/prepare_first_national_review_batch.py --full-details docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz --out-dir /tmp/jynlo-first50 --limit 50 --district-cap 7
