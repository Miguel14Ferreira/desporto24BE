# Jynlo: Liquibase do piloto de 3 — aprovação obrigatória

Este PR disponibiliza um **gerador do changeset opt-in**, mas **NÃO** inclui um changeset de importação ativado no `db.changelog-master.yaml`.

## Propostas de recintos

- Pavilhão Marialvas (Cantanhede) — OSM:WAY:145943204
- Piscinas Municipais Rui Abreu (Coimbra) — OSM:WAY:122685516
- Centro Olímpico de Piscinas Municipais (Coimbra) — OSM:WAY:1056855283

As três fontes municipais sustentam a identificação e a morada; os horários/programas e a autorização de uso por organizadores de sessões continuam sujeitos à entidade gestora. A consulta **LIVE PostgreSQL read-only** executada pelo proprietário em **21/10/2026** confirmou 265 registos atuais e **zero correspondências** pelos critérios referência exata, nome/concelho, ou distância até 100 metros. O SQL terminou com `ROLLBACK`. Esse resultado **não é autorização permanente**: repetir um preflight imediatamente antes de qualquer importação futura.

## Fluxo de aprovação

`docs/venue-imports/first-three-proposals-unapproved-20261021.jsonl` fixa a proposta de 3 recintos. `docs/venue-imports/first-three-decisions-pending-20261021.json` contém decisões `PENDING` e `approvedBy` vazio para todos.

O gerador `scripts/finalize_official_venue_pilot.py` compara os hashes SHA-256 dos dados essenciais de cada recinto, recusa referências inesperadas, exige uma decisão para os três, e só gera SQL/YAML quando todos tiverem uma decisão **APPROVED ou EXCLUDED** e pelo menos um estiver aprovado. Para os aprovados, exige:

- identidade, morada, modalidades e funcionamento atual validados;
- regras de acesso identificadas (incluindo se precisa autorização ou integração num programa, **não precisa acesso livre**), e que essas restrições serão visíveis antes de qualquer divulgação;
- novo preflight READ-ONLY na BD LIVE, até um dia antes da aprovação e com zero correspondências;
- aprovação nominal do responsável, data recente, URL da fonte municipal e notas substanciais;
- verificação do hash do candidato, para impedir edição silenciosa após a revisão.

Exemplo (em checkout/backend, **falhará por desenho com o ficheiro atual**):

```bash
python3 scripts/finalize_official_venue_pilot.py \
  --pilot-proposals docs/venue-imports/first-three-proposals-unapproved-20261021.jsonl \
  --decisions docs/venue-imports/first-three-decisions-pending-20261021.json \
  --out-dir /tmp/jynlo-three-review-migration
```

Quando devidamente autorizado, o script reutiliza `scripts/generate_verified_venue_liquibase.py` e gera ficheiros SQL/YAML idempotentes e **desligados do master**. Um revisor deve então auditar o SQL, conferir o catálogo LIVE contemporâneo, testar backup+restauro, e só integrar o YAML no master após aprovação específica de publicação.

## CI / segurança

`.github/workflows/venue-three-liquibase-approval-gate.yml` cobre rejeição das decisões reais PENDING, testes de revisão e duas execuções de SQL com **aprovações simuladas exclusivamente em PostgreSQL 16 descartável**. Os aprovadores sintéticos não aparecem nos artefactos e não são uma aprovação verdadeira. Testa também a preservação de um recinto de fonte COMMUNITY com nome idêntico.

**0 mudanças LIVE, 0 novos recintos publicados, 0 deployment Render, 0 alteração do master.** A Jynlo divulga sessões, não gere pagamentos ou reservas de recintos. Respeitar atribuição OSM ODbL / DGT CAOP2025 CC BY 4.0.
