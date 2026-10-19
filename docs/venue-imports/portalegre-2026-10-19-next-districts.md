# Próximos distritos — aproveitamento de fronteiras CAOP2025 (2026-10-19)

A análise geográfica do primeiro extrato OSM de Portalegre identificou
**32 elementos pertencentes a outros distritos**, após o cruzamento com
os limites oficiais CAOP2025:

| Distrito real | Candidatos encontrados |
| --- | ---: |
| Évora | 14 |
| Castelo Branco | 12 |
| Santarém | 6 |
| **Total** | **32** |

Este grupo resulta **apenas de sobreposições da área do primeiro extrato**;
não equivale a ter recolhido os três distritos. Dos 32, só **1** apresenta
nome minimamente identificável, mas ainda falta confirmar fonte, identidade,
acesso e duplicados. Não existe autorização para gerar SQL ou importar.

O relatório local da revisão da Jynlo foi exportado na conversa em
`jynlo_3_distritos_32_candidatos_para_revisao.csv`.
Origem: `portalegre_32_outros_distritos.csv` do cruzamento CAOP2025.
Os identificadores OSM originais permitem revisitar cada entrada.

## Portalegre — 13 do segundo lote documental

Há **13 OSM IDs adicionais** cuja designação ou existência é referida
por fontes independentes, sobretudo câmaras municipais, **sem aprovação**
de morada, coordenadas exatas, acesso ou duplicados na BD. Não são recintos
novos ainda publicados, nem um changeset Liquibase.

Ver `docs/venue-imports/portalegre-2026-10-19-batch2-corroborated.json`
e a lista local exportada `jynlo_portalegre_13_validacao_pendente.csv`.
A evidência municipal de Elvas refere cedências de campos para 2026/27,
mas não autoriza atribuir disponibilidade pública ou reservabilidade.
A informação de horários e sazonalidade de Avis e piscinas de Ponte de Sor
também exige confirmação atual.

## Escala nacional com custos controlados

Foi criado `scripts/national_venue_batch_runner.py`. Processa offline os
PBF já disponíveis de cada distrito contra os GeoJSON oficiais CAOP,
sem token/API/BD e sem GitHub Actions. Mantém `national-report.json`
atualizado após cada distrito, incluindo falhas de downloads.

**Segurança da continuidade:** um relatório antigo de Portalegre criado
antes da validação CAOP não conta como «distrito processado» e fica marcado
`legacy_unverified`, para não falsear a cobertura nacional.
O CSV em `venue_coverage_report.py` separa falhas e recolhas antigas
de distritos efetivamente sem recintos.

Executar os testes localmente:
```bash
python3 -m unittest discover -s scripts -p 'test_national_venue_batch_runner.py' -v
python3 -m unittest discover -s scripts -p 'test_venue_coverage_report.py' -v
```

**Limitações:** até ao momento só Portalegre teve o extrato original analisado
com a CAOP real; os restantes 17 extratos distritais ainda precisam de
recolha. Não ativar alterações no changelog master ou efetuar deploy sem
validar os recintos individualmente e testar PostgreSQL.

Atribuição: OpenStreetMap contributors — ODbL; DGT CAOP2025 — CC BY 4.0.
