# Santarém — conflicting GPS evidence for existing venues (27 October 2026)

**Decision: review only, DO NOT publish coordinates.** No SQL/JSON edit to production venues, no auto-approval, and no new venue creation.

## Existing public Jynlo records (read-only API)
- **277** — Pavilhão Desportivo Municipal; Largo Comendador Paulino da Cunha e Silva, 2005-134 Santarém; GPS absent.
- **278** — Nave Desportiva Municipal; same postal address; GPS absent. Distinct facility, **not** an alias or duplicate of 277.
- The public `/api/venues/suggest?city=Santarém&page=0` shows both distinct records alongside other venues. The municipality confirms the two separate buildings at the same address: https://viversantarem.pt/pavilhoes-desportivos/

## Two non-equivalent location leads
1. Event organiser SCALABISCUP: https://scalabiscup.com/directions/ lists the Pavilhão Municipal de Santarém and coordinates **39º14’02.4″N 8º41’36.6″W**, converted to **39.234000, -8.693500**. This is a competition site/arrival waypoint, **not proven to be the exact footprint/entrance of venue 277**. Its associated Google Maps link uses **39.234317,-8.6933874**, a nearby but different point.
2. Municipality's tourism portal: https://www.visitesantarem.pt/lista/pavilhao-desportivo-municipal/ gives the matching facility name/address but its external Google Directions link has **daddr=39.2350265,-8.6892978**. It is a *directions link*, not an explicit building GPS certificate.

The primary documented points are approximately **379.5 m apart**. The two facilities share an address; do not automatically reuse any candidate for both. The source page https://viversantarem.pt/pavilhoes-desportivos/ verifies identity and address but does not publish an unambiguous pair of precise venue-specific coordinates.

## Release gates
1. Determine from the operator/municipality or a clearly site-specific official map **which physical building is 277** and which is 278; verify current entrances and exact source dates.
2. Compare the candidate with the actual footprint and any OSM existing object IDs. Disambiguate the municipal tourism directions point from competition-wayfinding.
3. If exact evidence is established, use **existing** moderated GPS proposal requiring HTTPS source and explicit administrator review; never bypass it or bulk-import.
4. Until then, the offline audit deliberately returns `SOURCE_COORDINATE_CONFLICT_REVIEW` for both source points of 277, `CONFLICTING_DOCUMENTED_SITE_POINTS_REVIEW` and `RELATED_DISTINCT_VENUE_REVIEW`. `maxSourceSeparationMeters` is distance between sources, **not** recommended movement distance.

## Reproducibility / zero writes
```bash
python3 -m unittest discover -s scripts -p 'test_audit_live_venue_gps_evidence.py' -v
python3 scripts/audit_live_venue_gps_evidence.py --live-public-api --output /tmp/jynlo-venue-gps-review.json
```
This audit only performs public GET reads; tests use synthetic public venue fixtures. No authentication, DB writes, migration or deploy required.
