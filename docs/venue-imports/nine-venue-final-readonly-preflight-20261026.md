# Preflight conjunto dos nove recintos — 26/10/2026

Após os PRs de preparação **#401 (1 Santarém), #402 (2 Castelo Branco/Alcains), #408 (6 Açores/Madeira)**, existe um único relatório SQL, `preflight-nine-source-backed-READONLY-20261026.sql`, para ser executado com acesso autorizado ao PostgreSQL real, **antes de ativar qualquer migração**.

## Garantias e âmbito

- Apenas uma transação `BEGIN ... READ ONLY`, uma query SELECT, `ROLLBACK`. Sem funções externas, criação de tabelas, INSERT ou UPDATE.
- Compara sourceReference estável, nome + cidade, morada + cidade, e correspondências alternativas por texto distintivo em nomes/moradas; para nomes iguais em localidades diferentes, restringe ao mesmo distrito.
- Reporta somente os identificadores, nomes, localidades e moradas dos recintos existentes, já considerados informação de catálogo público. Não lê nem apresenta email, telefone, utilizadores, senhas ou tokens.
- Categorias possíveis: `SOURCE_REF_ALREADY_EXISTS`, `SAME_NAME_CITY`, `SAME_ADDRESS_CITY`, `POSSIBLE_ALIAS_REVIEW`, `NO_POTENTIAL_MATCH_REVIEW`. A última **não é** prova de inexistência: aliases e complexos podem ter nomes distintos.
- Até 12 matches por candidato. Se houver mais de 12, investigar o distrito/concelho adicionalmente antes de aprovar.

O workflow [`venue-nine-readonly-preflight-ci.yml`](../../.github/workflows/venue-nine-readonly-preflight-ci.yml) usa exclusivamente PostgreSQL16 descartável, cria três registos fictícios para testar deteção de duplicados exatos, moradas iguais e aliases, e executa a query com um **utilizador apenas de leitura**. Verifica os nove resultados e ausência de alterações.

## Estado de produção

Esta consulta foi **testada em dados fictícios**, não aplicada à BD real. O conector PostgreSQL Render atual falha por IP allowlist/TLS; Shell web Render é acessível mas a automação ainda não consegue enviar comandos ao terminal xterm. O repositório GitHub não possui secret de autenticação Render nem environment protegido, portanto não inventar tokens ou criar pipelines de acesso à produção sem autorização e revisão.

Depois de obter um canal SQL autorizado: executar este SQL; registar a decisão humana sobre os aliases e acessos (incluindo piscina Alcains); comprovar a restauração de **uma exportação real da Jynlo** (script `verify_render_venue_export_restore.sh` criado no PR #414) sem expor dados; só então promover um lote pequeno de inserts e medir o total.

Nunca incluir este preflight como changeset Liquibase; não há writes nem deploy. Telefones são opcionais.
