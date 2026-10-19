# Primeira proposta de migração Liquibase — Portalegre

**Estado: RASCUNHO — NÃO ATIVAR.** Nove recintos com existência/nome corroborados
por fontes externas (maioritariamente câmaras municipais) e coordenadas OSM.

Não foi realizado qualquer INSERT, nem foi lida a base de dados de produção.
O ficheiro `src/main/resources/db/changelog/venue-imports/drafts/2026-10-19-portalegre-source-checked-DRAFT.sql` é uma proposta real de SQL com
`NOT EXISTS` (origem + nome e proximidade) e `ON CONFLICT DO NOTHING`,
sem UPDATE e DELETE. A migração *não* está incluída em
`db.changelog-master.yaml`. Em caso de aprovação, após revisão de duplicados,
o changeset deve ser promovido para a pasta de alterações aprovadas; os dados
dos utilizadores não podem ser sobrescritos.

## Evidência e limitações

Ver `docs/venue-imports/portalegre-2026-10-19-source-review.json`:
cada um dos nove tem referência OSM e fonte independente. Fontes municipais:
- Ponte de Sor: https://www.cm-pontedesor.pt/infraestruturas-desporto/
- Sousel: https://cm-sousel.pt/sou-municipe/desporto/instalacoes-e-equipamentos/
- Equipamentos de Cano/Casa Branca/Santo Amaro: https://www.cm-sousel.pt/wp-content/uploads/2023/05/PMEPC_SOUSEL_PARTE_II_VCP.pdf
- Estádio Patalino: https://arquitecturaaqui.eu/pt/obras/19583/estadio-patalino-elvas

**Pendentes/excluídos:** Ginásio GymnoB, Campo de Ténis de Ponte de Sor,
Polidesportivo de Mora (Évora), Piscina Municipal Terrugem.

O filtro por distrito não substitui o futuro cruzamento com CAOP2025.
A existência de um equipamento não implica que esteja reservado ou acessível
sem autorização. A duplicação por nome alternativo exige uma verificação
contra a base de dados antes de ativar o seed. A atribuição OSM/ODbL tem
de estar visível na Jynlo (PR do frontend dedicado).

## Checklist para autorizar

1. Confirmar moradas/localização das nove infraestruturas, especialmente
   estádio de Ponte de Sor e obras atuais no pavilhão de Sousel.
2. Auditar 9 origens e equivalentes com designações alternativas na BD.
3. Fazer backup PostgreSQL no Render.
4. Validar SQL e changeset em base de dados de teste usando o esquema atual.
5. Obter aprovação explícita do proprietário e incluir o changelog no master.
6. Publicar FE com crédito OSM e BE de forma coordenada, e medir inserções.

Não há operações automáticas de produção associadas a este commit.
