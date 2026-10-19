# Importar recintos validados através do Liquibase (sem acesso direto à BD)

**Sim, é possível:** gerar scripts SQL versionados por distrito e deixá-los
executar no arranque do backend via Liquibase, em vez de escrever diretamente
através da API. Não exige acesso direto à PostgreSQL do Render para preparar os ficheiros.

## O que não devemos fazer

Nunca incluir automaticamente os cerca de 2.000 candidatos de Portalegre no
`db.changelog-master.yaml`: o primeiro teste encontrou apenas 13 recintos
preliminarmente aptos, **ainda não confirmados por revisão humana**. As
restantes instalações incluem piscinas privadas e elementos sem nome/concelho.
Liquibase não determina se uma fonte é correta: apenas executa SQL.

## Como preparar os scripts por distrito

1. Confirmar recintos através de fonte permitida, coordenadas, nome,
   concelho, modalidade e acesso. Evitar copiar dados de Google Maps em
   violação dos seus termos. Respeitar atribuição OSM/ODbL e fontes oficiais.
2. Criar um `*.jsonl` com apenas recintos aprovados. Cada linha contém os
   campos OSM da importação nacional e ainda:
   `"approved": true, "approvedBy": "revisor", "verificationNotes": "..." `.
   A confirmação é individual, **não um valor colocado em massa**.
3. Gerar o par SQL + changelog, fora da BD:

```bash
python3 -m unittest discover -s scripts -p 'test_generate_verified_venue_liquibase.py' -v
python3 scripts/generate_verified_venue_liquibase.py \
  --approved-jsonl /caminho/portalegre-approved.jsonl \
  --output-dir src/main/resources/db/changelog/venue-imports \
  --migration-id portalegre-reviewed-01
```

O ficheiro `*.sql` gera INSERTs idempotentes, com fonte
`OPENSTREETMAP` e `source_reference=OSM:WAY:...`, e evita recintos
com o mesmo nome a menos de aproximadamente 30 metros dos já existentes.
O SQL **nunca** faz UPDATE ou DELETE aos dados dos utilizadores.
Os novos campos seguem o esquema atual da tabela `desporto24.venue`.

4. **Por defeito, a migração de dados não é executada:** o changelog gerado
   não é referenciado no `db.changelog-master.yaml`. Isto é propositado,
   pois o Liquibase executaria dados incluídos no master no próximo deploy.
5. Após revisão do SQL e backup da BD, incluir uma referência no master:

```yaml
  - include:
      file: db/changelog/venue-imports/portalegre-reviewed-01-<hash>.yaml
```

Usar o **nome real** do ficheiro gerado; não copiar o texto `<hash>` literalmente.
Abrir PR, executar testes e publicar numa única janela controlada.

## Proteções e limites

- No máximo 500 recintos validados por changelog para limitar transações.
- A geração falha se o nome for sintético, a localidade/distrito estiverem
  vazios, ou faltarem revisões humanas.
- O SQL usa strings escapadas, `NOT EXISTS` por fonte e nome+localização e
  `ON CONFLICT DO NOTHING`. Não altera registos antigos.
- Cada revisão gera ficheiros imutáveis com hash: para novos recintos,
  gerar um changeset novo em vez de alterar uma migração executada.
- **Não se deve fazer rollback automático por DELETE**: poderia apagar
  recintos preexistentes ou entretanto associados a sessões.
- Os relatórios por distrito continuam necessários: quantos candidatos
  obtidos, quantos aprovados, quantos ignorados e quantos efetivamente
  inseridos após aplicar a migração.

Para atualização contínua e volume muito elevado, um processo de importação
em lote continua a ser mais flexível do que gerar milhares de instruções no
arranque da aplicação. Liquibase é adequado para cargas iniciais **curadas,
versionadas e relativamente pequenas**.
