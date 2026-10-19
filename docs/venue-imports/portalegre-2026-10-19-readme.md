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


## Diagnóstico de duplicados sem acesso de escrita

A consulta `src/main/resources/db/changelog/venue-imports/drafts/2026-10-19-portalegre-preflight-READONLY.sql` executa apenas um `SELECT` na BD e mostra
qualquer recinto existente a até 150 m dos nove candidatos, inclusive
com nomes alternativos, além de correspondências pela referência OSM.
**Não executar o changeset antes de rever esse relatório**. A consulta
não modifica dados e pode ser executada por um utilizador PostgreSQL com
permissão exclusiva de leitura (quando essa ligação estiver disponível).
A ausência de candidatos no relatório não comprova que não existam
duplicados mais distantes ou com coordenadas desatualizadas.

### Teste de compatibilidade PostgreSQL (sem Render)

O workflow `.github/workflows/venue-liquibase-postgres-manual.yml`
é acionado por alterações aos drafts SQL num PR dirigido à branch
`feature/desporto24-preview-deploy`, e suporta também execução manual
**após o workflow existir na branch padrão do GitHub**. O filtro de paths
e a opção `cancel-in-progress` evitam testes repetidos sem necessidade.
Os runners podem continuar bloqueados pela infraestrutura/billing GitHub; nesse
caso não há qualquer resultado PostgreSQL aprovado.
Cria um PostgreSQL 16 temporário, testa a inserção de 9 recintos,
a reaplicação idempotente e a preservação de registos COMMUNITY.
Depois executa o SQL de preflight sem efetuar alterações adicionais.

É um teste do SQL numa *tabela de teste com as colunas relevantes*,
não substitui testes de Liquibase contra o esquema completo real nem
uma revisão de duplicados na base de produção. Não usa segredos Render.

## Proteção contra duplicados sem coordenadas (19/10)

O preflight também procura recintos históricos cuja latitude/longitude sejam
nulas, comparando **nome e cidade**, além de referências OSM e vizinhos a
150 metros. Um nome coincidente numa cidade com coordenadas diferentes
continua a exigir revisão, em vez de ser inserido silenciosamente. Esta
consulta continua a ser exclusivamente de leitura e **não** foi executada
contra a base de dados Render (ligação SSL/TLS do conector recusada).
Os testes SQLite de simulação registam a função PostgreSQL `BTRIM`
localmente para que a avaliação de SQL não termine com um falso erro.

## Regra adicional — recintos antigos sem coordenadas

Antes de inserir, o SQL verifica também se já existe um recinto com o
**mesmo nome e a mesma localidade**, mas com latitude ou longitude em falta.
O registo existente tem prioridade e não é alterado. Um recinto homónimo
noutra cidade continua a poder ser inserido. Esta proteção foi aplicada ao
lote de nove recintos e ao gerador Liquibase para todos os distritos.

Testes offline adicionais cobrem os dois casos e a reaplicação idempotente.
