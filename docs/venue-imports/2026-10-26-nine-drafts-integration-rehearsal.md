# Ensaio integrado dos 9 recintos Jynlo — 26/10/2026

**Âmbito:** validar o lote futuro de **nove instalações verdadeiras**, nunca importar em produção a partir deste branch. PRs fonte:
- [#401](https://github.com/Miguel14Ferreira/desporto24BE/pull/401): Pavilhão da EB 2/3 Mem Ramires, Santarém (1);
- [#402](https://github.com/Miguel14Ferreira/desporto24BE/pull/402): Pavilhão Municipal de Castelo Branco e Piscinas de Alcains (2);
- [#408](https://github.com/Miguel14Ferreira/desporto24BE/pull/408): Açores 2 e Madeira 4 (6).

O workflow [`venue-nine-drafts-integration-rehearsal.yml`](../../.github/workflows/venue-nine-drafts-integration-rehearsal.yml) busca os **SQL originais nas respetivas branches DRAFT**, sem criar cópias divergentes, e valida os seguintes blobs Git exatos:

| Lote | Blob SHA (Git) | SQL |
|---|---|---|
| Santarém | `4d153500f51b877244a14ef0ac185d52bc308397` | `2026-10-26-santarem-mem-ramires-DRAFT.sql` |
| Castelo Branco / Alcains | `ef02d68ec304651a1caab4f939fbf8d7f8657593` | `2026-10-26-castelo-branco-alcains-DRAFT.sql` |
| Açores / Madeira | `f3a93af4ff874db4e40c072eb54ce70013001185` | `2026-10-26-island-six-official-DRAFT.sql` |

Se uma das branches mudar o SQL, **o teste falha fechado** até uma nova revisão explícita dos blobs. A CI executa todos os INSERTs em conjunto num PostgreSQL 16 de GitHub Actions descartável, com localização obrigatória (morada ou GPS continental), `source_reference` único, campos e comprimentos à semelhança da BD. Não é um clone integral do esquema real com todos os triggers/restrições.

Verifica:

1. 9 registos na primeira execução e os mesmos 9 na segunda, sem duplicados;
2. duas instalações nos Açores, quatro na Madeira e três continentais;
3. `contact=NULL`, GPS não inventado, morada não vazia, fonte `OFFICIAL`;
4. preservação integral de quatro instalações comunitárias de teste que coincidam por **morada**, **nome** ou **nome alternativo**, acrescentando apenas as outras cinco;
5. ausência de alterações ao `db.changelog-master.yaml` e de qualquer ligação ao PostgreSQL LIVE.

O preflight **real** dos nove recintos já correu em 26/10 às 11:48:50 UTC pela ligação interna do backend, em transação READ ONLY, e devolveu `candidates=9 potential_matches=0 no_data_writes=true` — ver [#416](https://github.com/Miguel14Ferreira/desporto24BE/pull/416) e a [issue #99](https://github.com/Miguel14Ferreira/desporto24BE/issues/99). Zero matches pelos critérios testados **não prova inexistência de todos os aliases**.

## Bloqueio de publicação não levantado

O Render disponibiliza no Dashboard um export privado no formato `.dir.tar.gz`. O [PR #414](https://github.com/Miguel14Ferreira/desporto24BE/pull/414) passou num restauro **sintético**, mas **o backup REAL da Jynlo ainda não foi restaurado em PostgreSQL 16 isolado**. Nenhuma migração de nove recintos poderá ser ativada por este PR. O Render PITR cria outra instância faturável; não fazer sem aprovação. Não mover o dump real para GitHub Actions ou logs públicos. Acesso físico/horários, em especial ao complexo ALBIGEC Alcains e pavilhão escolar, continuam por rever.

**Objetivo:** preparar o changeset final com confiança, mas manter todos os recintos em DRAFT até haver prova de restauro real e revisão humana.
