# Porto — municipal Choupos GPS research and Viso potential duplicate (27 October 2026)

**Read-only evidence. No automatic venue creation, location update, or duplicate merge.**

## Campo Municipal dos Choupos, existing Jynlo ID 281

The live Jynlo public GET `/api/venues/281` identifies **Campo Municipal dos Choupos**, Rua dos Plátanos 4100-414, Porto, source OFFICIAL, saved Ágora directory `https://www.agoraporto.pt/noticias/desporto/desporto/rmid`, missing GPS. Public name autocomplete `?name=Choupos&page=0` returns this exact venue only (not proof against every alias).

Official Ágora property page https://www.agoraporto.pt/grandes-campos-municipais/polidesportivo-dos-choupos states **Campo Municipal dos Choupos**, municipal 7-a-side turf pitch, Rua dos Plátanos, 4100-414 Porto, in Ramalde. OSM way **449975768** at https://www.openstreetmap.org/way/449975768 is named **Polidesportivo dos Choupos** and classed as a sports-centre polygon in Ramalde. OSM-derived https://mapcarta.com/pt/W449975768 displays site-level reference **41.17345,-8.65816**, a rounded polygon centre, not a mapped stadium gate/precise visitor access. Waze independently lists the polidesportivo at the same street (https://www.waze.com/live-map/directions/pt/porto/porto/polidesportivo-dos-choupos?to=place.ChIJp1no8IplJA0RhfKUMnWktwA), without independently certifying the polygon centroid.

The admin-only, paginated, read-only missing-GPS queue may include **`researchCandidate`** for ID **281** with `latitude=41.17345`, `longitude=-8.65816`, Ágora property HTTPS page and OSM way HTTPS page, and `precision=SITE_REFERENCE_NOT_VERIFIED_ENTRANCE`. This requires the current record to match exact ID/name/city/district/street, OFFICIAL source, saved exact HTTPS directory and missing BOTH coordinates. It is not a GPS update proposal, coordinate certification or public location.

## Campo Municipal do Viso ID 280 — possible duplicated alias of OSM ID 2623

- Public `/api/venues/280`: **Campo Municipal do Viso**, Porto, Rua Direita do Viso 4250 Porto, official Ágora directory, no GPS; football and field hockey.
- Public `/api/venues/2623`: **Campo Sintético do Viso**, Porto, OSM import, no postal address, location **41.1819461,-8.6450944**, football only.
- Ágora directly documents **Campo Municipal do Viso** as **Campo Sintético do Viso** on https://www.agoraporto.pt/grandes-campos-municipais/campo-sintetico-do-viso and describes Rua Direita do Viso, 4250 Porto, plus field hockey+football. Official GO Porto https://goporto.pt/noticias/reformulacao-da-bancada-do-campo-sintetico-do-viso-arranca-ja-no-inicio-de-marco independently confirms field and street.
- An OSM pitch named **Campo Sintético do Viso** way 334849388, https://www.openstreetmap.org/way/334849388, has site centre around **41.18189,-8.64494** per https://mapcarta.com/pt/W334849388. Its vicinity is labelled *Senhora da Hora, Matosinhos* on map even though Ágora describes *Ramalde, Porto*, close to the municipal boundary. This must be manually checked against the field footprint and administrative limits. Its site centre is different from the OSM imported venue point, but near it (~14-15 metres).
- **Potential duplicate only**, not guaranteed same facility: never blindly fill GPS on ID 280 from 2623 or merge records, because facilities could be adjacent/alias, and source boundaries differ. The admin-only list now includes `possibleDuplicateReview` **only if both live records are intact**: OFFICIAL ID280 exact known identity/no GPS and OPENSTREETMAP ID2623 exact name/Porto and GPS still close to the inspected value. The backend performs ONE extra read-only `findById(2623)` solely when 280 appears on a review page.
- The warning links both existing Jynlo venues via admin UI, and official Ágora+OSM pages for human research. Moderator must verify aliases, geometry, sport and entrance and only then decide whether to consolidate, correct or keep distinct records using separately approved moderation steps.

## Safeguards
No new venues, no GPS/database updates, no Liquibase, no regular public API/venue detail change. New Choupos manifest `porto-choupos-site-gps-review-20261027.json` has `approved_for_import=false` and no runnable import; offline audit tests identity, no coordinate update fields and source type. Viso duplicate is explicitly **not** a GPS candidate. Existing frontend GPS research card can already show Choupos without code changes; a small FE change is needed for the **new potential duplicate warning**.
