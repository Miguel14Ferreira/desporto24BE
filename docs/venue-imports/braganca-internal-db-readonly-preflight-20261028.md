# Bragança — pré-voo PostgreSQL interno opcional (28/10/2026)

**Não ativado em produção.** A migração dos pavilhões Arnaldo Pereira e
Polidesportivo da Coxa continua fora do changelog mestre. Este trabalho
cria apenas uma forma de obter evidência de possíveis duplicados reais
sem abrir a allowlist externa da BD do Render.

## Como funciona

`BragancaPavilionPreflightStartupProbe` só é criado mediante ativação
**explícita** de `JYNLO_VENUE_BRAGANCA_PREFLIGHT_ENABLED=true`.
Sem a variável, o comportamento do backend não muda.

- Ao arranque, lê a consulta SQL já existente e versionada
  `preflight-braganca-two-pavilions-READONLY.sql`; exige um único
  `SELECT` e rejeita SQL adicional ou comandos de mutação.
- Usa exclusivamente a conexão interna da aplicação com PostgreSQL,
  força transação `REPEATABLE READ READ ONLY` e `ROLLBACK`.
- Tempo máximo da consulta: 12 segundos. Limite de 100 registos de
  revisão; se ultrapassado, aborta sem sugerir que a lista está completa.
- Nos logs privados escreve apenas IDs públicos dos recintos
  eventualmente correspondentes, se a referência oficial coincide
  ou se o resultado é uma pista de nome/proximidade, e o total.
  Não regista moradas, contactos, coordenadas, dados pessoais,
  credenciais ou conteúdo da consulta SQL.
- Não cria endpoints, não autoriza importações e não impede o arranque
  da API em caso de erro. Cada arranque com a flag ativa executa
  uma consulta (não é um agendamento nem um serviço permanente).

## Como utilizar numa manutenção futura

1. Validar que o estado de produção e os backups continuam saudáveis
   e que há aprovação para uma breve consulta interna de leitura.
2. Configurar temporariamente a flag acima no serviço Render
   `desporto24-api` e executar uma publicação controlada.
   **Não executar este passo automaticamente neste PR.**
3. Ler os logs `JYNLO_BRAGANCA_PREFLIGHT_ROW` e
   `JYNLO_BRAGANCA_PREFLIGHT_SUMMARY`; revisar manualmente
   todos os IDs e identidades, incluindo falsos positivos.
4. Retirar a flag e fazer uma publicação controlada de reposição.
   Os resultados não dispensam GPS de acesso verificado,
   prova de direitos de reutilização e backup/restauro testado.
5. Manter o DRAFT de Bragança desativado até decisão separada
   e autorização explícita para efetuar escrita.

## Limites

A consulta por nome, distrito e proximidade é conservadora, podendo
devolver piscina, estádio, Pavilhão da Bancada ou pistas sem identidade
confirmada. Uma lista sem resultados **não prova a ausência de
duplicados**. O GPS disponível continua a ser de sítio/complexo e não
o ponto de entrada dos jogadores. Criar uma sessão na Jynlo não reserva
o pavilhão municipal.

Testes Java:
`BragancaPavilionPreflightStartupProbeTest` valida a flag,
rejeição de SQL de escrita, controlo de tempo e registos,
transação PostgreSQL read-only, rollback e tratamento de falhas.
