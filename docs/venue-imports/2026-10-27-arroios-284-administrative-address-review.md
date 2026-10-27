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

## Nova evidência cartográfica municipal tipificada (27/10/2026, sem aprovação de GPS)

Camada oficial **Câmara Municipal de Lisboa — Instalações Desportivas**:
https://services.arcgis.com/1dSrzEWVQn5kHHyK/arcgis/rest/services/Desporto_Instalacoes/FeatureServer/0

Verificação GET read-only de `OBJECTID IN (2954,2955)` com `returnGeometry=true&outSR=4326&f=geojson`, sem geocodificação, centróides ou extrapolação de moradas:

| OBJECTID | Designação municipal | Tipologia oficial | Ponto municipal WGS84 | DATA_INF |
|---|---|---|---|---|
| **2954** | Forno do Tijolo - Arroios | **Pequeno Campo de Jogos** | **38.7210189039736, -9.13265738281149** | **2016-07-06 UTC** |
| **2955** | Forno do Tijolo - Arroios | **Piscina** | **38.7209885110846, -9.13240589722577** | **2016-07-06 UTC** |

Separação dos pontos ~22,1 metros: **não são intercambiáveis**. A ficha específica da Junta de Freguesia `https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/` identifica um **campo de jogos de 27×15m**, para futsal, andebol, basquetebol e minivoleibol, no Complexo Joaquim Campos / Mercado do Forno do Tijolo; o registo existente Jynlo **284** é o polidesportivo, enquanto a **Piscina de Arroios (ID 1840)** tem GPS próprio OSM e não pode herdar este ponto do campo.

O dataset Lisboa Aberta https://dados.cm-lisboa.pt/dataset/instalacoes-desportivas-municipais-e-concessoes-dados-2015 indica licença **CCZero**, mas a data de observação desta feição é **2016**, e a ficha da freguesia não contém coordenadas explícitas do campo ou sua entrada. Portanto **é uma correspondência histórica, tipificada, de recinto e não uma localização atual aprovada da superfície ou do acesso**. A data de atualização de catálogo ou de serviço ArcGIS não prova observação do terreno nesse dia.

Novo manifesto apenas de revisão: `docs/venue-imports/lisboa-arroios-284-municipal-gis-pitch-review-20261027.json`, aponta estritamente para ID 284, OBJECTID 2954, indica OBJECTID 2955 como **outro equipamento**, `approved_for_import=false`. Integra o auditor GPS read-only com aviso de antiguidade, alias entre nomes e morada administrativa incorreta. Não deve produzir SQL, Liquibase, alteração de morada, fusão de recintos, ou autoaprovação.

**Próxima ação para GPS aprovado:** confrontar o ponto CML 2954 com ortofotomapa/levantamento recente que mostre o campo após a requalificação, confirmar a entrada do campo por fonte oficial ou visita e só depois submeter uma proposta administrativa individual. A morada municipal do mercado não é um número de porta certificado do campo.
