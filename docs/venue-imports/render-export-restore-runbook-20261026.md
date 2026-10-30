# Verificação offline dos exports Render PostgreSQL — 26/10/2026

O Render disponibiliza um export lógico da base Jynlo `desporto24-db` no formato comprimido **`.dir.tar.gz`**. A ferramenta anterior `verify_venue_backup_restore.sh` só aceitava dumps `-Fc`; o novo script não a substitui.

## O que já é comprovável sem aceder à produção

`scripts/verify_render_venue_export_restore.sh` utiliza **PostgreSQL 16 num contentor efémero com `--network none`**, sem portas publicadas e sem credenciais da Jynlo. Aceita um único `.dir.tar.gz` que esteja presente localmente no ambiente seguro de execução; monta o ficheiro **somente de leitura**, extrai o arquivo apenas dentro do contentor, confirma que existe exatamente um `toc.dat` válido, faz `pg_restore --exit-on-error --single-transaction` e verifica:

- a presença de `desporto24.venue` com o número mínimo de linhas (`JYNLO_RESTORE_EXPECTED_VENUES`, predefinição 3595);
- a presença de `desporto24.databasechangelog` (histórico do Liquibase);
- destruição automática do contentor após sucesso **ou** erro.

A CI [`render-venue-export-restore-isolated-ci.yml`](../../.github/workflows/render-venue-export-restore-isolated-ci.yml) cria um backup **sintético, sem utilizadores**, com `pg_dump --format=directory`, compacta-o como o Render, executa o restauro isolado e testa arquivo corrompido. Nenhum backup real é descarregado ou publicado no GitHub.

## Gate que continua por cumprir

**A aprovação da CI prova que o procedimento funciona para o ficheiro de teste, não que o export REAL da Jynlo tenha sido restaurado.** No Render > Database > Recovery já foi identificado um link `.dir.tar.gz`, mas não é um backup validado por restauro.

O export real é privado e não deve ir para o repositório GitHub, Actions artifacts, logs, anexos públicos ou páginas com URL temporária assinada. Para provar a recuperação real é necessário disponibilizá-lo através de um canal autorizado **apenas numa sandbox isolada** (ou iniciar, mediante aprovação de custos, uma instância PITR isolada). O acesso GitHub+Render atual **não disponibiliza transferência binária segura entre o navegador Render e a sandbox executora**. Não solicitar nem divulgar passwords, tokens ou URL de ligação.

O preflight read-only na BD LIVE também continua pendente: o conector PostgreSQL alojado do Render falha por limites de rede e TLS, e a Shell web foi acessível mas a automação não conseguiu enviar comandos ao terminal xterm. Nunca abrir a allowlist da BD para 0.0.0.0/0.

Até ambos os gates estarem satisfeitos, manter os PRs de nove recintos #401, #402, #408 em DRAFT, sem inclusão no master Liquibase nem deploy de INSERTs. O catálogo LIVE permanece no último total confirmado de 3595.

Fontes: [Render — Recovery and Backups](https://render.com/docs/postgresql-backups), [Render — Shell](https://render.com/docs/ssh).
