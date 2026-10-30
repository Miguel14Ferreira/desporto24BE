# Jynlo GPS site evidence: Piscinas Municipais de Évora, ID 273

**Status: research lead only; not an approved GPS, not verified pedestrian entrance.**

- Confirmed LIVE existing ID **273**, exact name **Piscinas Municipais de Évora**, district and city **Évora**, official saved website, **without latitude and longitude**. Bounded public autocomplete exact-name query returns ID 273.
- Municipality's current facility location page (HTTPS): https://www.cm-evora.pt/municipe/equipamentos/equipamentos-desportivos/piscinas-municipais/contactos-e-localizacao-das-piscinas-municipais/ verifies **Av. Engenheiro Arantes de Oliveira, 7000-758 Évora** and links from **«ver no mapa»** to `http://www.google.com/maps/place/38.573234, -7.928876`. The **HTTPS municipal page** is the citable source; **do not use the HTTP maps link** as submission source URL.
- Independently, Turismo do Alentejo ERT as a verified publisher has a POI with **38.573425, -7.928949**: https://www.outdooractive.pt/pt/poi/evora/piscina-municipal/802361541/. The municipal map pin is approximately **22m** from the regional tourism marker, coherent for the **same multibuilding property**.
- Tertiary architectural archive with same address and owner also places the compound at **38.5735004374,-7.9290987902**: https://arquitecturaaqui.eu/pt/obras/18489/piscinas-municipais-de-evora .
- Both curated source leads in `evora-piscinas-municipais-site-gps-review-20261027.json` have `approved_for_import=false`; the offline audit reports `EXISTING_VENUE_WITHOUT_GPS`, distinct precision warnings, **no suggested DB write**.

## Admin queue usability
The existing **ADMIN/SUPER_ADMIN-only** paginated `GET /api/admin/overview/venue-gps-review` may now supply a nullable nested `researchCandidate` for ID **273** only when saved id, name, city, district, street, official HTTPS website and BOTH missing GPS axes are still consistent. It contains:
```json
{
  "latitude": 38.573234,
  "longitude": -7.928876,
  "officialSourceUrl": "https://www.cm-evora.pt/municipe/equipamentos/equipamentos-desportivos/piscinas-municipais/contactos-e-localizacao-das-piscinas-municipais/",
  "secondarySourceUrl": "https://www.outdooractive.pt/pt/poi/evora/piscina-municipal/802361541/",
  "precision": "SITE_REFERENCE_NOT_VERIFIED_ENTRANCE"
}
```
It is a **research clue only**. The UI must label it as unverified and require manual research and existing `venue/{id}/suggest-edit` authenticated submit/approval. The backend does NOT send, submit, save or approve a change to any venue. Once coordinates are saved or identity/source mismatch occurs, the hint disappears. No Liquibase, no postcode updates or new venues.

## Checks
Java regressions prove the exact ID and official source guard, missing-coordinates-only exposure and no venueRepository.save. Python offline tests prove the two points agree at site scale without becoming approved. Any future additional candidate must have an official HTTPS site-specific source, a known existing venue ID, and separate tests.
