# Recuperação integral dos dados OSM nacionais — Jynlo

Esta etapa corrige a perda dos campos `address`, `venueType` e `environmentType` no artifact resumido do primeiro levantamento. Reexecuta **uma vez** o importador OSM/CAOP2025 dos 18 distritos (aproximadamente 464 MB de PBF no levantamento original) e junta os resultados aos 3 514 candidatos auditados pela referência exata, não por nome ou coordenadas.

Entradas anteriores: GitHub Actions #39597754588 (18/18 distritos) e #39602965416 (comparação com 265 recintos LIVE). Os 10 `sourceReference` do preflight PostgreSQL read-only de 21/10/2026 são bloqueados explicitamente. Qualquer diferença no snapshot OSM fica em `source-drift.json`.

Saída: `national-venue-details-3514.jsonl.gz`, `quality-summary.json`, `source-drift.json`, `district-quality.csv`. **Todos** têm `approvedForImport=false`, `accessAndCurrentOperationVerified=false`, `addressVerifiedIndependently=false`. Moradas OSM e coordenadas não provam acesso público. As 32 instalações municipais staged no PR #142 ainda precisam de reconciliação própria.

Workflow de leitura apenas: sem tokens da Jynlo, sem SQL de escrita, sem alteração a Liquibase master, sem deploy Render. Só armazena ficheiros compactados com retenção de 3 dias, não armazena PBF completos. Licenças OSM ODbL e DGT CAOP2025 CC BY 4.0.

Depois: rever endereços, aliases, acesso/funcionamento; obter backup atualizado e executar testes isolados antes de qualquer importação aprovada.
