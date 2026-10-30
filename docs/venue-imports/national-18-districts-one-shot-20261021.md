# Jynlo — uma execução nacional dos 18 distritos (21 outubro 2026)

O objetivo é substituir ciclos manuais de 30 recintos por uma **única recolha nacional read-only**, seguida de triagem automatizada e uma publicação futura controlada. A aplicação mantém **265 recintos** no último número confirmado pelo proprietário. O PR #142 (32 recintos municipais documentados, ainda não importados) será reconciliado como fonte adicional, nunca somado automaticamente.

## O que está implementado
- Runner `scripts/prepare_national_venues.py --district all`: percorre os 18 extratos oficiais de OSM FR, aplica polígonos DGT CAOP2025 (18 distritos, 278 municípios), mantém checkpoints após cada distrito, separa candidatos completos e casos para revisão.
- Nova auditoria `scripts/audit_national_stage.py`: valida integridade por SHA-256 e por distrito, verifica referência OSM, contagens, coordenadas e riscos de duplicação por nome e proximidade (70 m); gera um JSON de auditoria e lista compacta de candidatos com etiqueta **REQUIRES_LIVE_DB_AND_HUMAN_APPROVAL**.
- Workflow `.github/workflows/venue-national-18-preview-20261021.yml`: lançado **uma única vez** na branch de auditoria, ou manualmente por workflow_dispatch após integração. Não contém `D24_ADMIN_TOKEN`, segredos do Render, `--apply` nem `db.changelog-master.yaml`.
- Relatórios de 18 distritos, CSV e JSONL comprimido descarregáveis como artifact com retenção de **1 dia**; não arquiva os 18 PBF (aproximadamente centenas de MB).
- A contagem de candidatos OSM não corresponde a recintos distintos, operacionais ou aprovados: Way e Node podem representar o mesmo estádio/campo, acesso pode ser condicionado e a posição Way é uma aproximação de centroide. **Nenhum registo é aprovado automaticamente.**

## Depois do relatório nacional
1. Consultar `national-qa.json` e `coverage-18-districts.csv`; rejeitar distritos falhados, duplicados de fontes, instalações sem modalidade, sem nome/concelho ou com acesso vedado.
2. Cruzar os candidatos com as 265 entradas atuais por referência, nome normalizado, concelho, endereço e geoproximidade, usando export read-only de colunas públicas da BD; **não descarregar e expor dados pessoais de utilizadores**.
3. Rever especialmente relações/polígonos OSM, recintos dentro de complexos e instalações fechadas. Confirmar licença OpenStreetMap ODbL e as obrigações de atribuição/redistribuição e DGT CC BY 4.0.
4. Gerar batches SQL imutáveis de até 500 aprovados usando `scripts/generate_verified_venue_liquibase.py` ou fluxo de importação autenticado idempotente; provar restauração de backup atualizado da BD.
5. Um único lançamento controlado, com contagem/distritos antes e depois, sem deploy automático, sem substituir entradas comunitárias.

## Execução gratuita alternativa em WSL
Para evitar minutos de GitHub Actions, o mesmo runner pode trabalhar localmente em WSL Linux com `pip install osmium` e `ogr2ogr`. Os ficheiros oficiais:
- https://download.openstreetmap.fr/extracts/europe/portugal/
- https://geo2.dgterritorio.gov.pt/caop/CAOP_Continente_2025-gpkg.zip

Não afirmar nacionalização concluída até o relatório provar **18/18 processados com CAOP válido**. Não afirmar 100% dos recintos portugueses: nenhuma fonte é exaustiva.
