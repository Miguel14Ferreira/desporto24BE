# Primeiro lote documental — Açores e Madeira (6 recintos)

**26/10/2026 — auditoria de fontes, sem importação ou publicação.** A API pública tinha **3.595** recintos, classificados em 18 distritos continentais, e **zero** como `Açores` ou `Madeira`. Isto não prova cobertura zero de todas as instalações físicas; mede o catálogo.

Os 6 candidatos com referências oficiais são descritos no manifesto `islands-first-six-official-sources-20261026.json`:

- **Açores (2):** Complexo Desportivo das Laranjeiras, Ponta Delgada, Rua José Maria Caetano de Matos, s/n, 9500-116; Pavilhão Desportivo Sidónio Serpa, Rua da Juventude, 9500-211. Fonte: [Governo dos Açores](https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha), [Plano Municipal de Juventude](https://www.cm-pontadelgada.pt/cmpontadelgada/uploads/writer_file/document/1434/plano_municipal_de_juventude_2022_2026_final.pdf) e [Federação Portuguesa de Xadrez 2025](https://clubes.fpx.pt/provas/regulamentos/ACO_2025_CDE.pdf). O pavilhão pode integrar o complexo do Lajedo, ainda por verificar. Sem inferir modalidades quando a ficha não as confirma.
- **Madeira (4):** Complexo de Piscinas Olímpicas do Funchal (Beco dos Álamos); Pavilhão do Estreito de Câmara de Lobos – Marcos Freitas (Rua das Vinhas, 9325-051); Campo de Futebol do Ribeiro Real (Caminho Doutor João Henrique Pereiro); Piscina do Curral das Freiras (Sítio da Achada 56). Fonte: [Direção Regional de Desporto](https://www.madeira.gov.pt/drd/Estrutura/DRD/ctl/ReadMore/mid/10858/CatalogoId/1078/TemaId/2) e fichas oficiais por recinto (URLs no JSON).

**Não copiar contatos:** todos os 6 têm `contact = null`; não existem coordenadas GPS inventadas. Modalidades verificadas contra `SportsCatalog.ALLOWED` atual. Nenhuma instalação é declarada de acesso livre ou reservável pela Jynlo; fontes incluem fichas históricas, a confirmar em 2026 antes da publicação.

## Bloqueios reais no código LIVE

1. `ContinentalDistricts.normalizeFilter` aceita **apenas 18 distritos continentais**; pesquisas com `district=Açores`/`Madeira` falham sem adaptação.
2. `CreateVenueRequest.isValidVenueLocation` valida coordenadas **apenas na BBOX continental**; impediria a criação normal de recinto insular com GPS real.
3. A constraint de BD `ck_venue_address_or_mainland_gps` aceita `address` ou **GPS continental**; um recinto insular sem rua teria de fornecer morada textual até alterar essa regra de forma segura.
4. O importador OSM nacional e os scripts para distritos usam CAOP continente e BBOX continental. Não usá-los para ilhas sem extensão territorial própria.

**Próxima etapa de engenharia (PRs separados, testes BE/FE antes de deploy):** suportar Açores/Madeira nos filtros de distrito, validação GPS com polígonos/intervalos apropriados, geografia e UI; manter registos existentes e não enfraquecer validação. Depois realizar análise de duplicados diretamente na BD, backup restaurável, migração idempotente revista, autorização de publicação e deploy manual.

Este PR é apenas *pesquisa documental e controlo de qualidade*. Não altera serviços, BD, frontend nem master Liquibase.
