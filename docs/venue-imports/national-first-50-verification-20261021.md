# Primeira vaga Jynlo — preparação offline (21 outubro 2026)

O primeiro levantamento nacional OSM/CAOP2025 identificou 3 514 candidatos em 18 distritos. A recuperação integral do snapshot foi concluída em [GitHub Actions #39616305935](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39616305935), com 3 514/3 514 referências recuperadas e sem divergências entre recolhas. **Nenhum foi aprovado nem importado.**

O novo `scripts/prepare_first_national_review_batch.py` gera localmente uma fila de **50 candidatos para verificação independente**. É totalmente offline, sem API, Render, PostgreSQL, token, nem novos downloads OSM. Exige o JSONL comprimido `national-venue-details-3514.jsonl.gz` do artifact recuperado, cujo SHA256 é `e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a`.

Execução:

```bash
python3 -m unittest discover -s scripts -p 'test_prepare_first_national_review_batch.py' -v
python3 scripts/prepare_first_national_review_batch.py \
  --full-details /caminho/national-venue-details-3514.jsonl.gz \
  --out-dir /tmp/jynlo-first-50 --limit 50 --district-cap 7
```

Proteções:
- As **10 referências já existentes em LIVE**, confirmadas no preflight do proprietário com 265 recintos, nunca entram.
- **130 possíveis duplicados** ficam fora desta primeira fila.
- Todos os **445** candidatos OSM em Setúbal, Leiria e Bragança ficam pendentes até a revisão das **32 instalações municipais** desse lote (não confundir 445 objetos OSM com 32 fichas municipais).
- Exclui nesta primeira vaga recintos sem morada postal indicada, tipos/modalidades fora do objetivo, nomes genéricos e nomes que indiciem encerramento, hotel ou instalação especializada. **Não significa que esses recintos não existam ou não possam entrar futuramente.**
- Gera `review-queue.csv` com ligação a OSM, campos de evidência independente, estado de funcionamento, acesso e decisão; `unapproved-venue-data.jsonl` contém `approved=false` e não pode ser importado via `generate_verified_venue_liquibase.py`.
- **0 aprovações automáticas, 0 alterações de BD, 0 migrations incluídas no master, 0 deploy.** Morada OSM não é verificação independente de morada nem de acesso.
- A seleção inicial testada originou **128 candidatos elegíveis para esta fila e 50 escolhidos em 11 distritos**, no máximo 7 por distrito.

A etapa seguinte é obter evidência oficial/operacional por recinto, confirmar modalidade e acesso, rever moradas e aliases, e só então aprovar individualmente e construir um changelog Liquibase idempotente de importação com backup e teste de restauro. Atribuição OSM ODbL e CAOP2025 DGT CC BY 4.0 permanecem obrigatórias.

**Não adicionar os ficheiros de output deste script a migrações de produção.**
