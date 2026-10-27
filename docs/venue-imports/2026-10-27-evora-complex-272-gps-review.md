# Évora — live venue ID 272: dated event waypoint versus named OSM site polygon (27 October 2026)

**Decision: no coordinate import, submission or admin approval**. Two legitimate sources identify the Complexo Desportivo de Évora complex, but their pins have different *semantics* and differ by more than 75 m.

## Confirmed public Jynlo venue identity
- GET `https://api.d24sports.com/api/venues/272` confirms **ID 272** `Complexo Desportivo de Évora`, city/district `Évora`, `Estrada das Alcáçovas (N380), Évora`, with no GPS.
- GET `https://api.d24sports.com/api/venues/suggest?district=%C3%89vora&page=1` includes that ID. The `name=Évora` and `city=Évora` public queries unexpectedly returned empty, independently tracked in the separate PR #451 fixing uppercase accent handling in the repository. **Do not misclassify the existing venue as NO_EXACT_LIVE_MATCH_REVIEW.**
- Other nearby facilities in the same district include existing Jynlo **3588 Campo Estrela** with GPS, and distinct football training grounds. Do **not** clone any of these venues or overwrite their GPS.

## Two verified but non-equivalent location clues
1. **Municipal 2022 race regulations:** https://www.cm-evora.pt/wp-content/uploads/2022/04/Regulamento_XL-GPSJ-Atletismo.pdf **page 2 (PDF index 1)** explicitly says the 25 June 2022 race starts and finishes on the tartan athletics track of Complexo Desportivo de Évora; event coordination point **38°33'40.9"N 7°55'14.6"W**, or **38.5613611111,-7.9207222222**. This is a **historic event/start waypoint**, not confirmed current entrance; the PDF uses one GPS for distinct race events. Independently inspected page screenshot.
2. **OSM mapped 50-node sports-centre polygon:** https://www.openstreetmap.org/way/1266229401, named **Complexo Desportivo de Évora**, tags `leisure=sports_centre` and `sport=rugby_union;athletics;running;gymnastics`. The OSM-derived https://mapcarta.com/W1266229401 shows approximate area centre **38.56074,-7.91927**. This is a **rounded polygon centre**, not the entrance or an independently municipal-certified track waypoint. It is separate from the mapped rugby pitch (way 606438252) and the nearby Campo Estrela.

These point references are approximately **144 m apart**, indicating different positions within/near the complex or different point semantics. They must **not** be averaged or blindly copied into venue 272.

## Business/access rules
The current municipal facility page https://www.cm-evora.pt/visitante/lazer-visitar/complexo-desportivo-de-evora/ confirms Estrada das Alcáçovas, management by the city and access rules: general public use of the maintenance circuit subject to specified hours; athletics track and large sports field reserved to clubs/associations. Do not imply the competition facilities are freely reservable.

## Approval gate and audit
- Confirm current **entrance to the overall sports complex** or an officially adopted public venue map point, not only an event start position.
- Validate date and identity against the same venue **272**, and distinguish neighbouring **3588 Campo Estrela**.
- Only then, if suitable source is available, submit a venue-272 GPS correction via the Jynlo *existing* HTTPS source + administrative approval process.
- The read-only offline audit now stores both evidence rows with `expected_existing_venue_id=272`. It emits `SOURCE_COORDINATE_CONFLICT_REVIEW`, `CONFLICTING_DOCUMENTED_SITE_POINTS_REVIEW`, `maxSourceSeparationMeters`, and source-specific `point_precision` warnings. It never exports latitude/longitude as proposed update fields.
- This change contains **only manifest/test/docs/CI label**. No DB mutations, Liquibase, admin approval, or Render runtime deploy.
