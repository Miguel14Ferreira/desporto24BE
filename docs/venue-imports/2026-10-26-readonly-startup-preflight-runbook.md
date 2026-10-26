# Preflight real dos nove recintos via conexão interna do backend

**26 de outubro de 2026 — nova capacidade de diagnóstico, desligada por defeito.**

O conector PostgreSQL externo no Render não consegue atravessar a allowlist/TLS da BD. Em vez de abrir portas ou introduzir credenciais no GitHub, esta funcionalidade usa o **DataSource PostgreSQL interno já autenticado da aplicação Jynlo**. A classe `VenuePreflightStartupProbe` é carregada **apenas** quando a variável de ambiente `JYNLO_VENUE_PREFLIGHT_ENABLED=true` estiver explicitamente definida.

- Ao receber `ApplicationReadyEvent`, lê **o SQL já revisto no PR #415**, extrai apenas o `WITH ... SELECT`, abre uma conexão existente do pool, força `SET TRANSACTION ... READ ONLY` em PostgreSQL e executa uma só consulta com timeout de 12 segundos.
- Nos logs **privados do serviço Render** apresenta apenas `source_ref`, classe de possível duplicado e `existing_id` para os 9 candidatos, mais o resumo final. Não imprime moradas, contactos, emails, strings de ligação, JWT, nomes de utilizador ou SQL completo.
- Executa obrigatoriamente `ROLLBACK` e fecha a conexão. Em erro faz apenas `WARN` com o tipo de exceção, sem expor a mensagem/stack trace nem impedir o arranque da aplicação.
- Não cria nenhum endpoint HTTP, não requer credenciais novas nem abre a lista IP da BD.
- Com a flag omitida ou `false`, o bean nem sequer é criado e o comportamento da aplicação é igual ao anterior.

## Procedimento de ativação controlada

1. Integrar após a suite BE passar. Confirmar ausência da flag (modo desligado) e deploy BE normal.
2. Opcionalmente, ativar `JYNLO_VENUE_PREFLIGHT_ENABLED=true` **temporariamente** no serviço Render `desporto24-api` (somente mediante aprovação explícita de operação de manutenção) e fazer deploy. Confirmar logs com o prefixo `JYNLO_VENUE_PREFLIGHT`.
3. Remover a flag / repor `false` e fazer deploy para desativar. A consulta nunca insere dados.
4. Rever as nove correspondências com fontes oficiais, **mas não aprovar dados automaticamente** só por ter resultado `NO_POTENTIAL_MATCH_REVIEW`.
5. Exigir também uma **restauração efetiva do export real da Jynlo em PostgreSQL16 isolado** para autorizar qualquer importação. O script de restauro é o PR #414. Não ligar migrações de recintos sem esta validação.

**Nota:** a funcionalidade de diagnóstico não demonstra a integridade de backups, nem altera a necessidade de decisão humana sobre Alcains e os recintos escolares. Não será usado `UPDATE`, `INSERT`, `DELETE` nem dump de dados pessoais.

## Testes

`VenuePreflightStartupProbeTest` comprova a flag `matchIfMissing=false`, extração segura do SQL de fonte controlada, transação PostgreSQL READ ONLY, reversão em sucesso/falha e rejeição de SQL extra ou status inválidos.
