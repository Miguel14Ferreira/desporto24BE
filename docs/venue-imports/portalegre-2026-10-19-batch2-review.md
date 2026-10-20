# Segundo lote potencial — 13 recintos com fontes independentes

**Portalegre / 2026-10-19 — RASCUNHO DOCUMENTAL. Não importar.**

A primeira migração DRAFT já contém **9** recintos. Este lote contém **13 outros identificadores OSM**, sem repetição das nove referências, cuja existência ou designação foi encontrada em fonte externa (câmara municipal ou associação de futebol).

**Não são 13 recintos prontos para produção.** Os dados de rua, localização OSM precisa, acesso real, estado de funcionamento e duplicados no catálogo Jynlo **não foram validados**. Para piscinas municipais, confirmar época e condições de acesso; para complexos com vários campos/piscinas, decidir se a unidade deve ser representada como complexo, espaço individual ou ambos, sem duplicar a experiência de pesquisa.

| OSM | Recinto | Concelho CAOP | Fonte externa |
|---|---|---|---|
| OSM:NODE:5351786347 | Ginásio Municipal de Avis | Avis | [Consulta](https://cm-avis.pt/turismo/descobrir/o-que-fazer/) |
| OSM:WAY:102867362 | Campo de Jogos João Manuel Nabeiro | Campo Maior | [Consulta](https://afportalegre.fpf.pt/Portals/10/Ficheiros/CAAFP/2025-2026/Nom-11-08122025-14122025.pdf) |
| OSM:WAY:157939265 | Piscinas Cobertas | Ponte de Sor | [Consulta](https://www.cm-pontedesor.pt/infraestruturas-desporto/) |
| OSM:WAY:158032722 | Piscinas Descobertas | Ponte de Sor | [Consulta](https://www.cm-pontedesor.pt/infraestruturas-desporto/) |
| OSM:WAY:190953965 | Estádio Municipal Cândido de Oliveira | Fronteira | [Consulta](https://cm-fronteira.pt/wp-content/uploads/2025/10/Diagnostico_Social_Fronteira_maio25.pdf) |
| OSM:WAY:236910375 | Piscina Municipal de Avis | Avis | [Consulta](https://cm-avis.pt/turismo/descobrir/o-que-fazer/) |
| OSM:WAY:300977684 | Piscina Municipal dos Assentos | Portalegre | [Consulta](https://www.cm-portalegre.pt/wp-content/uploads/2022/09/prestao-de-contas-2019-do-municipio-portalegre.pdf) |
| OSM:WAY:332418159 | Estádio Municipal de Portalegre | Portalegre | [Consulta](https://www.cm-portalegre.pt/wp-content/uploads/2022/09/regulamento_estadios.pdf) |
| OSM:WAY:643431913 | Estádio Municipal de Atletismo de Elvas | Elvas | [Consulta](https://cm-elvas.pt/reuniao-de-camara-na-quarta-feira-dia-26-6/) |
| OSM:WAY:810055267 | Campo de Jogos Dona Berta Courinha | Ponte de Sor | [Consulta](https://www.cm-pontedesor.pt/infraestruturas-desporto/) |
| OSM:WAY:1117681904 | Campo Pedro Barrena | Elvas | [Consulta](https://cm-elvas.pt/reuniao-de-camara-na-quarta-feira-dia-26-6/) |
| OSM:WAY:1117681923 | Campo António Semedo "O Tigre Alentejano" | Elvas | [Consulta](https://cm-elvas.pt/reuniao-de-camara-na-quarta-feira-dia-26-6/) |
| OSM:WAY:1133956450 | Piscinas Municipais de Elvas | Elvas | [Consulta](https://cm-elvas.pt/reuniao-de-camara-na-quarta-feira-dia-26-6/) |

### Anomalias verificadas no lote de 97

- **Nove entradas** têm outro elemento OSM a menos de 50 m: proximidade indica revisão, **não duplicação automática**. Múltiplas piscinas de Ponte de Sor são subinstalações de um complexo.
- **Go Fitness Healthclub** surge com duas referências em Elvas a cerca de 1,68 km. Podem ser unidades diferentes; não eliminar sem verificar.
- Estádio Patalino, Campo Pedro Barrena e Campo António Semedo são próximos, mas representam campos desportivos diferentes; a Jynlo deve permitir distinguir a instalação principal dos seus campos.
- **75 entradas** dos 97 ainda não receberam uma fonte independente específica nesta revisão. Nenhum destes foi aprovado.

### Bloqueios antes da migração Liquibase

1. Completar morada real, caso exista, e confirmar coordenadas e acesso com fontes independentes/autorizadas.
2. Decidir tratamento de espaços filhos em complexos (sem fusão automática).
3. Executar a consulta read-only de duplicados na BD, incluindo aliases.
4. Aprovar individualmente, gerar um novo changeset versionado e testar PostgreSQL.
5. Só ativar após backup e autorização explícita, no deploy conjunto e com atribuição OSM/CAOP.

Manifesto de proveniência legível por máquina: `docs/venue-imports/portalegre-2026-10-19-batch2-corroborated.json`. Todos os registos contêm `approvedForLiquibase: false`.

O relatório de 97 candidatos da conversa tem também um CSV e XLSX com filtros para revisão.
