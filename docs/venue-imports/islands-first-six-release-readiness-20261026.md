# Primeiros seis recintos insulares — migração SQL DRAFT

**26/10/2026 — sem publicação nem inserção LIVE.**

Lote: dois Açores e quatro Madeira. As instalações, modalidades existentes em SportsCatalog, localizações e fontes oficiais estão no manifesto `islands-first-six-official-sources-20261026.json`. Auditoria pública CI [#40253886688](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/40253886688): 3595 recintos LIVE, nenhum destes seis com correspondência exata de nome/cidade. Não equivale a preflight da base de dados.

O SQL `2026-10-26-island-six-official-DRAFT.sql` contém seis INSERTs aditivos idempotentes, com sourceReference próprio, sem UPDATE/DELETE, sem telefone e sem GPS inventado. Todas as moradas foram documentadas, permitindo respeitar a constraint existente `ck_venue_address_or_mainland_gps`. O tipo de ambiente fica desconhecido quando a fonte não sustenta interior/exterior. As modalidades vêm das fontes e foram validadas no catálogo. Sidónio Serpa não tem modalidade específica verificada: sports NULL.

O SQL permanece exclusivamente na pasta `venue-imports/drafts`, **fora de db.changelog-master.yaml**.

## Verificações obrigatórias antes de publicar

1. BE com Açores/Madeira em pesquisa por distrito já LIVE; FE com filtro respetivo LIVE após BE (PR BE #407 e FE #496).
2. Confirmar nomes alternativos, diferentes instalações dentro de complexos e condições reais de utilização; não afirmar acesso livre ou disponibilidade de reserva.
3. Correr `2026-10-26-island-six-official-PREFLIGHT-READONLY.sql` **na BD LIVE** com credenciais autorizadas e acesso seguro.
4. Comprovar backup PostgreSQL 16 recente e restauração real isolada, registar totais antes/depois e preparar rollback.
5. Só após aprovação de release específica: incluir um changeset versionado separadamente e fazer deploy BE manual, verificando o catálogo público.
6. Posteriormente, suportar GPS insular em validação API/UI/BD. Os importadores OSM/CAOP atuais só aceitam Portugal continental; não os utilizar nas regiões autónomas.

O PR não modifica a BD, o frontend, nem o backend runtime; testes usam unicamente Postgres descartável. Não foram importados contactos telefónicos.
