# Jynlo — uma única consulta LIVE, final, para o piloto municipal de três

Objetivo: obter **um preflight PostgreSQL read-only** de 3 propostas, a executar pelo operador **na ligação que já utiliza à BD LIVE**, sem permitir ligações novas/alterar a lista de IPs do Render. Isto **não é** autorização de importação, nem pedido para expor a base de dados.

## Query pronta

`src/main/resources/db/changelog/venue-imports/preflight-first-three-municipal-live-READONLY.sql`

Quando este PR estiver integrado em `develop`, descarregar o ficheiro usando o `gh` já autenticado em WSL:

```bash
gh api 'repos/Miguel14Ferreira/desporto24BE/contents/src/main/resources/db/changelog/venue-imports/preflight-first-three-municipal-live-READONLY.sql?ref=develop' \
  -H 'Accept: application/vnd.github.raw+json' \
  > /tmp/jynlo-3-final-preflight.sql
```

No **psql já ligado de forma autorizada à BD Jynlo**, executar:

```text
\i /tmp/jynlo-3-final-preflight.sql
```

Não inserir URL/senha da BD neste repositório ou nesta conversa. O SQL usa `BEGIN TRANSACTION READ ONLY`, prazo de 15 segundos e `ROLLBACK`. Não tem INSERT, UPDATE, DELETE nem DDL. Se a ligação ou consulta falhar, **parar** e não inferir ausência de duplicados.

## O que ler no resultado

Cada referência OSM produz exatamente uma linha, incluindo `catalogue_rows` (contagem atual, em vez dos 265 antigos), `potential_collision_count`, `nearby_100_150m_count` e `potential_matches` (JSON de ids, nomes, referências existentes e distâncias).

- `potential_collision_count > 0`: **bloquear** o candidato e analisar colisão de referência OSM, nome/alias no mesmo município ou proximidade até 100 m. Não importar enquanto houver conflito sem reconciliação formal.
- `nearby_100_150m_count > 0`: examinar o vizinho mesmo quando a contagem de bloqueios é zero. O Centro Olímpico de Piscinas e o Pavilhão Mário Mexia estão documentados como equipamentos distintos; este último foi encontrado a 108 m na API pública, mas não autoriza publicação automática.
- Ambas a zero: não foram encontrados conflitos **por estes critérios**; não demonstra que a instalação está acessível ou operacional para a sessão.
- `catalogue_rows` diferente do último valor de 265 significa que houve movimento no catálogo; rever antes de usar dados antigos.

Esta consulta testa referência exata, nome normalizado (acentos e pontuação), aliases específicos `centroolimpico`, `ruiabreu`, `marialvas` com correspondência de município, raio até 100m e anel de 100–150m. Não executa fuzzy matching geral, pelo que **não substitui** verificação humana de nomes alternativos mais invulgares.

## Pós-consulta / regras de publicação

Conservar saída da consulta e data num relatório de revisão, sem dados pessoais/segredos. Rever os três candidatos individualmente: identidade, modalidades, situação atual de funcionamento, horário, cedência/autorizações, taxas e se as restrições são comunicadas na experiência Jynlo. A aprovação do código da query não aprova equipamentos.

Atualizar `first-three-decisions-pending-20261021.json` **só depois de verdadeira revisão nominal**, incluindo novo preflight, justificação e eventual exclusão. Gerar Liquibase com `scripts/finalize_official_venue_pilot.py` e testar em PostgreSQL16/restauro isolado antes de anexar manualmente um changeset ao master. Nunca criar aprovação sintética fora de fixtures de teste.

A CI usa exclusivamente um **Postgres 16 descartável** para comparar os casos: Pavilhão Mário Mexia a ~108m sem bloquear Centro Olímpico, mesmo nome por alias com GPS nulo bloqueado, referência OSM repetida bloqueada, zero escritas pela query.
