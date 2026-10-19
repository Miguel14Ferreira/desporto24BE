# Primeiro lote municipal — Évora, Castelo Branco, Santarém

Apenas **oito** instalações com nome, localidade, morada e modalidades documentadas por fonte municipal/gestora oficial. Não inclui piscinas lúdicas sem modalidade confirmada, escolas com acesso condicionado sem confirmação e instalações de Castelo Branco sem modalidades documentadas.

## Contagens por distrito
- Évora: 2 (complexo desportivo e piscinas municipais)
- Castelo Branco: 1 (pista de atletismo)
- Santarém: 5 (complexo aquático, piscinas Sacapeito, Pavilhão Municipal, Nave Municipal, Pavilhão EX-EPC)

## Regras
- **Não é o lote OSM nacional**; estas referências internas `JYNLO:DOCUMENTED` são identificadores gerados para o catálogo, não IDs publicados pelas câmaras.
- A coluna `website` guarda o URL da fonte oficial, para tornar a proveniência auditável.
- Latitude/longitude ficam `NULL` exceto onde a entidade publica coordenadas explícitas verificadas com CAOP2025 (Complexo Aquático Municipal de Santarém).
- A pesquisa por distrito e modalidade pode usar recintos sem GPS; o mapa não deve inventar um ponto.
- Sem horários, preços, estado de abertura ou reservabilidade inferidos.
- SQL faz apenas INSERT condicional por `information_source/source_reference` e nome + cidade; nunca altera os registos comunitários.

## Pré-voo antes de integração
1. Verificar aliases dos oito recintos e coexistência com os existentes através de consulta read-only à PostgreSQL; não foi possível nesta sessão, devido ao conector Render SSL/allowlist.
2. Validar explicitamente backup PostgreSQL e aplicar em ambiente de teste.
3. Executar testes `scripts/test_first_official_district_venues.py`, validação Liquibase PostgreSQL 16 e revisão da licença/proveniência.
4. Após aprovação, juntar com os restantes changesets num único changelog master (PR BE #118 para 9 Portalegre; PR BE #119 para backfill). As três branches editam master; **não integrar as três sem conciliar as alterações**.
5. Um único deploy backend, após os passos anteriores, permite contabilizar as inserções e testar a pesquisa pelo frontend.

## Fontes
Consultar `docs/venue-imports/first-official-district-venues-20261019.json`. Conteúdo resumido, não copiado de páginas municipais.
