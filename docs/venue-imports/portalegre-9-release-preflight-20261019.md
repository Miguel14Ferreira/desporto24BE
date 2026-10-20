# Portalegre — proposta de publicação de nove recintos (19 outubro 2026)

**Este branch/PR não modifica a BD e não está LIVE.** A inclusão no master deste branch só corre após merge e deploy manual do backend.

## Objetivo
Promover, em alteração atómica, **apenas 9** recintos do draft `2026-10-19-portalegre-source-checked-DRAFT.sql`, confirmados documentalmente. Não publica os restantes 97 objetos do piloto, os 13 candidatos do segundo lote ou os 32 de outros distritos.

## Evidência reforçada
- OpenStreetMap: as 9 referências OSM são conservadas no SQL.
- Fontes municipais/externas independentes: `docs/venue-imports/portalegre-2026-10-19-source-review.json`.
- CAOP2025 oficial (Continente, camadas `cont_municipios` e `cont_distritos`, EPSG:4326): teste ponto-em-polígono confirmou 9/9 nos concelhos Portalegre corretos: Ponte de Sor (2), Sousel (6), Elvas (1). Fonte: DGT CAOP2025.
- A proximidade de Sousel e Cano é compatível com espaços distintos (campo vs pavilhão, campo vs polidesportivo); não fundir por proximidade.

## Guardas
- Cada INSERT só acrescenta dados se não houver correspondência pela mesma fonte OSM nem pelo mesmo nome com localização próxima/cidade sem GPS; `ON CONFLICT DO NOTHING`.
- **Nenhum UPDATE e nenhum DELETE**; todos os registos preexistentes, inclusive COMMUNITY, ficam inalterados.
- O changelog está limitado a PostgreSQL e à existência de `desporto24.venue`. Atribuição OSM/ODbL obrigatória no frontend.
- A migração descreve recintos existentes, não confirma disponibilidade pública ou possibilidade de reservar.

## Pré-voo antes da publicação
1. Executar `src/main/resources/db/changelog/venue-imports/drafts/2026-10-19-portalegre-preflight-READONLY.sql` na BD de produção com acesso exclusivamente de leitura. **Parar para rever aliases e vizinhos a menos de 150 m.**
2. Confirmar backup da base PostgreSQL Render e ausência de importação simultânea.
3. Executar uma vez o teste de compatibilidade PostgreSQL16 e Liquibase; Actions sem orçamento não contam como testes aprovados.
4. Rever alterações do master, confirmar FE atribuição OSM e preparar um único deploy manual backend.
5. Depois do deploy, comparar contagens/distritos e referências `OSM:WAY` via endpoint protegido `GET /api/admin/venues/coverage` e por consulta read-only.

## Limitações
Não houve acesso direto à base de dados Render (restrições SSL/TLS/allowlist). O PR permanece DRAFT e **não deve ser integrado antes do pré-voo**.

Attribution: © OpenStreetMap contributors (ODbL); DGT CAOP2025 (CC BY 4.0).
