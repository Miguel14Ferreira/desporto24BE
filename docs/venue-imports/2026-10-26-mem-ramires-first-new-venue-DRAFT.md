# Draft de um novo recinto — Pavilhão EB 2/3 Mem Ramires

**Não está em produção, nem aprovado para importação automática.**

- **Nome:** Pavilhão da EB 2/3 Mem Ramires
- **Cidade e distrito:** Santarém
- **Morada:** Rua Doutor Virgílio Arruda, 2000-217 Santarém
- **Modalidades declaradas pela operadora:** Futsal, Andebol, Basquetebol, Ginástica, Dança Desportiva, Voleibol e Badminton
- **Telefone:** intencionalmente omitido; **contact = NULL**
- **Coordenadas:** desconhecidas, não inventar
- **Origem:** [Viver Santarém, pavilhões desportivos](https://viversantarem.pt/pavilhoes-desportivos/), ver secção específica «Pavilhão da EB 2/3 Mem Ramires»
- **Acesso:** utilização de instalação escolar; não afirmar acesso livre ou reservas pela Jynlo

## Evidência e travões

O catálogo de 3.595 recintos já contém Pavilhão Desportivo Municipal #277, Nave Desportiva #278 e EX-EPC #279, mas as suas moradas documentadas são diferentes. A pesquisa por nome exato não encontrou Mem Ramires. O nome e morada devem voltar a ser verificados imediatamente antes de qualquer release, incluindo variantes e ruas no SQL preflight read-only.

SQL **só em `venue-imports/drafts`**, não incluído no master Liquibase. Testes PostgreSQL 16 descartáveis cobrem idempotência, campos nulos, não modificar um homónimo já existente e execução read-only da query preflight. A consulta pública LIVE é apenas uma proteção adicional: não equivale a ligação direta à base de dados.

**Antes de autorizar publicação:** (1) backup PostgreSQL 16 real, recente e restaurável verificado num ambiente isolado; (2) preflight do catálogo real com aliases/morada sem colisões; (3) aprovação final da identidade, modalidade e condições de acesso; (4) PR separado para ativação de changelog e deploy manual BE, validando o total antes/depois. Não usar a ligação Render PostgreSQL hospedada enquanto as regras de rede a bloquearem.

Issue: https://github.com/Miguel14Ferreira/desporto24BE/issues/99
