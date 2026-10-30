# Braga — Federação Portuguesa de Atletismo source-map review (27 October 2026)

**Review-only; do not update GPS for Jynlo ID 287.** Existing public Jynlo venue `/api/venues/287`: **Pista de Atletismo do Estádio 1.º de Maio**, Parque S. João da Ponte, Braga; missing GPS.

## Official references
- Municipality names the *pista* separately as a sports facility, but only provides an address: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6214
- Cultural heritage inventory https://imovel.patrimoniocultural.gov.pt/detalhes.php?code=72715 lists the **whole stadium** at `41.538819,-8.420613`. This is the older unapproved source already in the GPS evidence manifest.
- **New independent map evidence** from Federação Portuguesa de Atletismo: https://fpaportalonline.blob.core.windows.net/portalfpa-public/2025/04/RTecnica_AL_2025_CPor-10000.pdf, *page 3 (zero-index 2)*, technical brief for 3 May 2025 competition; printed `41.53889462246087,-8.420265861123468` (N101 12, Braga).
- Inspection of that PDF page screenshot shows the map marker **inside the football pitch**, enclosed by the red athletics track. It is a stadium wayfinding pin and **not a mapped track edge/entrance**.
- OpenStreetMap https://www.openstreetmap.org/way/22660087 similarly names the **whole stadium**, not a separate point certified as the running track.

The government heritage and federation event points are about **30 m apart**: good corroboration of the venue complex, **not** proof of a precise piste coordinate. No need to fabricate or average a track GPS position. If there is a future official track-specific location, submit through the existing moderated correction flow requiring HTTPS source and explicit administrator sign-off.

## Audit
Added a second FPA point to the nonapproved Braga/Évora manifest. Its precision flag `FEDERATION_STADIUM_FOOTBALL_PITCH_PIN_NOT_TRACK_OR_ENTRANCE` ensures the report preserves this caveat. Automated regression checks that both documented points lead to existing ID 287 without GPS, the source gap does not produce a fake >75m conflict, and mismatched ID is rejected. This audit performs only public GETs/offline fixtures, produces no GPS update proposals, uses no Liquibase or production database writes, and needs no Render deploy.
