# Porto — Campo Municipal do Outeiro / GPS source review (27 October 2026)

**Decision: research-only evidence; NO GPS edits, imports or approvals.** The full Jynlo public venue record at `GET /api/venues/283` has latitude and longitude missing and the saved Ágora address **Rua D. Afonso Henriques, Paranhos, Porto**. The bounded name search `/api/venues/suggest?name=Campo%20Municipal%20do%20Outeiro&page=0` returned the existing **ID 283** and no exact duplicate; a broader Outeiro query returned other unrelated cities. This is not proof against all aliases.

## Primary evidence and geometries
- Municipal operator: https://www.agoraporto.pt/grandes-campos-municipais/campo-municipal-do-outeiro explicitly names the Campo Municipal do Outeiro and identifies Sporting Clube da Cruz, with **Rua D. Afonso Henriques, Paranhos** as address. The recorded Jynlo website is an older general Ágora directory URL, not a separate facility.
- Operator: https://www.sportingclubdacruz.pt/contactos separately lists **Campo do Outeiro**, Rua Fonte do Outeiro **521**, Porto. It also lists an administrative club contact address, which must not be conflated with the sports facility.
- Geodata: https://www.openstreetmap.org/way/1298753441 is a **30-node OSM polygon**, tags `name=Campo Municipal do Outeiro`, `operator=Sporting Clube da Cruz`, `leisure=stadium` and `sport=soccer;rugby_union`.
- https://mapcarta.com/pt/W1298753441 renders approximate site reference **41.17589, -8.59259** based on that OSM way. This is a *rounded site-level reference, not a verified entrance or certified GPS point*. Mapcarta classifies the area as Campanhã while the municipal facility says Paranhos. Do not treat that automatically as a distinct venue or a corrected administrative boundary.
- The historic club-address record at https://agc.sg.mai.gov.pt/details?id=650456 uses Rua Fonte do Outeiro 521, but historic office/club address is not evidence of the facility's current entrance.

## Why correction remains blocked
The named polygon likely corresponds to the existing facility, but **no primary official source has supplied entrance coordinates** and there are conflicting/adjacent access street descriptions. Confirm which street provides current spectator entrance, verify that it reaches the OSM polygon and which point is appropriate before making a GPS proposal. OSM geometry alone is not an administrative GPS approval.

## Existing moderation workflow, not an import
If verified, submit an **existing venue** GPS proposal for ID 283 with a suitable precise HTTPS source through the Jynlo moderation workflow. Explicit ADMIN/SUPER_ADMIN review is mandatory. Do not insert a new venue, modify an existing one via Liquibase, automatically geocode, or copy the point from a street centroid.

## Offline audit result
The nonapproved manifest `porto-outeiro-osm-geometry-review-20261027.json` pins **expected_existing_venue_id=283**. The read-only audit reports `EXISTING_VENUE_WITHOUT_GPS`, plus `OSM_SITE_POLYGON_CENTRE_NOT_ENTRANCE`, `MUNICIPAL_AND_CLUB_ADDRESS_REVIEW` and `MAP_NEIGHBORHOOD_DISCREPANCY_REVIEW`. Any matching name+city+district on a different venue ID must be flagged `EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW`. Nothing is generated for updates; contact/user data is never exported.

```bash
python3 -m unittest discover -s scripts -p test_audit_live_venue_gps_evidence.py -v
python3 scripts/audit_live_venue_gps_evidence.py --live-public-api --output /tmp/jynlo-venue-gps-review.json
```
All audit execution uses public GET only or offline fixture. **Zero DB writes, zero deployment required.**

## Revisão administrativa focada — aditamento
A investigação já existente pode agora ser consultada na fila ADMIN de recintos com GPS em falta (`onlyWithEvidence=true`). Só o **ID 283** é elegível, e apenas enquanto o nome, município, distrito, morada, website da Ágora, tipo de fonte OFFICIAL e ausência de ambas as coordenadas coincidirem com a ficha verificada.

O campo `researchCandidate` mostra **41.17589,-8.59259**, referência arredondada do *polígono do estádio*, acompanhada pela página municipal da Ágora e pela página cartográfica Mapcarta/OSM. O aviso genérico visível em PT/EN já esclarece que este **não é um ponto de entrada validado**. A morada Rua D. Afonso Henriques, as referências do clube à Rua Fonte do Outeiro 521 e a atribuição cartográfica a Campanhã são pistas de validação presencial, não erros de morada comprovados nem motivo para atualização automática.

A vista normal sem o parâmetro mantém-se inalterada; os restantes recintos não entram automaticamente na fila. Não se efetuam alterações à base de dados, nem se cria um novo recinto, nem se aprova GPS ou morada. Qualquer edição futura tem de passar por proposta fundamentada e revisão ADMIN/SUPER_ADMIN.
