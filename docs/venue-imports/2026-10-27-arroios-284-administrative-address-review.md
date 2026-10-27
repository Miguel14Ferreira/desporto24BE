# Jynlo venue 284 — Polidesportivo de Arroios / Complexo Joaquim Campos (27 October 2026)

**Review-only address discrepancy; no GPS inferred or updated.**

## Verified public catalogue
- Existing `GET https://api.d24sports.com/api/venues/284`: `Polidesportivo de Arroios — Joaquim Campos`; city/district `Lisboa`; saved address `Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa`, source OFFICIAL, no GPS, official saved website `https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/`.
- Bounded name search returns ID **284**. A separate existing Jynlo venue `Piscina de Arroios`, ID **1840**, has its own GPS and must not be merged with the polidesportivo or copied as an exact pitch point.

## Primary sources, and why the current saved address is misleading
1. The parish's **equipment-specific** page https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/ explicitly states that the **Polidesportivo de Arroios** lies in **Complexo Desportivo Joaquim Campos**, at **Mercado do Forno do Tijolo**. The **`Rua do Saco 1A`** in its common page footer identifies **Polo da Pena**, an administrative office, not the sports equipment's street.
2. The parish's dedicated market page https://jfarroios.pt/equipamentos/mercados/mercado-do-forno-do-tijolo/ locates **Mercado do Forno do Tijolo at Rua Maria da Fonte, 1170-221 Lisboa**. The market's listed address is **site-level geographic context**, not a municipal guarantee that the polidesportivo shares its individual house number or the main entrance.
3. The Câmara Municipal de Lisboa independently confirms the renovated pitch **next to the market**, and identifies it as part of Joaquim Campos: https://informacao.lisboa.pt/noticias/arquivo/detalhe/arroios-requalifica-polidesportivo-junto-ao-mercado-forno-do-tijolo .
4. The parish additionally confirms a **pool, polidesportivo, and children's playground** coexist in the same complex: https://jfarroios.pt/a-freguesia/ . The existing Jynlo **pool ID 1840** is **not** the pitch.

## Review action, not data mutation
The existing paginated ADMIN/SUPER_ADMIN `GET /api/admin/overview/venue-gps-review` now includes nullable `addressReview`, **only** when the saved record exactly matches ID 284, name, city/district, source type OFFICIAL, saved HTTPS website, original conflated address and GPS still absent.

Response fields:
```json
{
  "issueCode": "ADMIN_OFFICE_ADDRESS_VS_VENUE_LOCATION_REVIEW",
  "venueLocationToCheck": "Mercado do Forno do Tijolo, Rua Maria da Fonte, Lisboa — entrada por confirmar",
  "equipmentSourceUrl": "https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/",
  "marketSourceUrl": "https://jfarroios.pt/equipamentos/mercados/mercado-do-forno-do-tijolo/"
}
```

This is **not a proposed exact postal address or GPS point**. Moderator should visually verify the actual court's access from the market, then use the existing authenticated venue correction suggestion for the address; GPS remains optional and must have its own exact HTTPS evidence. The alert disappears if any relevant saved identity, source, address or GPS changes. No postal correction is applied automatically, no geocoding, no SQL/Liquibase/venue creation. This code path is read-only. Public venue response is deliberately unchanged.
