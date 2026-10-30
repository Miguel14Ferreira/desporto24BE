# Bragança — três pavilhões noutros municípios (28/10/2026)

**Só investigação.** O lote não é uma migração nem foi autorizado para
importação. Não atribuir GPS de 2016 a novas fichas nem permitir que um
jogador confunda uma sessão da Jynlo com uma reserva municipal.

Fonte de localização: [Guia das Acessibilidades dos Espaços Desportivos
do Distrito de Bragança](https://www.cm-vilaflor.pt/uploads/writer_file/document/1736/GUIA_BRAGAN_A.pdf),
levantamento realizado em 2016 (ver introdução), páginas impressas 46,
54 e 88. Os três pontos correspondem a instalações documentadas nesse
guia, **não** a entradas verificadas em 2026.

| Recinto | Município | GPS histórico de sítio | Fonte de atividade independente |
| --- | --- | --- | --- |
| Pavilhão Gimnodesportivo Municipal | Freixo de Espada à Cinta | 41.087873, -6.809408 | Canal N, notícia 18/10/2026 sobre jogo de futsal e homenagem |
| Pavilhão Municipal | Macedo de Cavaleiros | 41.5385759, -6.96886618 | Zerozero, relato de jogo futsal 24/10/2026 |
| Pavilhão Gimnodesportivo Municipal | Vila Flor | 41.307304, -7.152449 | Agrupamento de Escolas de Vila Flor, referência sem data a uso por acordo municipal |

Ligações de corroboração em cada registo do JSON de evidência. A
utilização desportiva recente em Freixo e Macedo não demonstra
disponibilidade para reserva nem a entrada de acesso ao edifício.
O documento do agrupamento de Vila Flor não tem data verificável
para afirmar atividade em outubro de 2026.

O audit `scripts/audit_braganca_three_municipal_pavilions_20261028.py`
compara a shortlist fixa ao catálogo público por:
- Nome exato no distrito, mesmo quando a localidade foi registada como freguesia.
- Possíveis aliases de pavilhões no mesmo município, incluindo os sem GPS.
- Recintos com GPS a até 250 m do ponto documental de 2016, sem os tratar
  como o mesmo equipamento.

O relatório tem limite de 10 correspondências por lista, separa
contagens, omite contactos, moradas e GPS de recintos individuais e
**nunca conclui que ausência de correspondência significa recinto novo**.
A execução GitHub Actions chama somente GET ao URL público fixo, sem
credenciais, Postgres, inserções ou deploys.

Para avançar com cada local: confirmar em fonte oficial recente identidade,
entrada utilizável, disponibilidade e condições municipais, direitos de
reutilização do ponto escolhido, comparação PostgreSQL autorizada,
restauro de backup e aprovação separada para escrita.

## Revisão municipal de Macedo e identidade de concelho

O Município de Macedo de Cavaleiros disponibiliza um
[Regulamento Municipal do Pavilhão Desportivo](https://www.cm-macedodecavaleiros.pt/cmmacedocavaleiros/uploads/document/file/13154/regulamento_municipal_do_pavilhao_desportivo.pdf),
que descreve um equipamento no complexo escolar e estabelece
autorização escrita da entidade gestora para utilização (artigo 17.º).
O texto menciona regulamentos de 1993 e 2001, mas a data da
versão consultada e a sua aplicabilidade em 2026 **não estão confirmadas**.
Não inferir horários livres, reservas, preços nem a sua equivalência
física ao Pavilhão Municipal do guia de 2016 sem evidência adicional.
O manifesto identifica estas incertezas expressamente.

A comparação com o catálogo público passou a distinguir:
- `EXACT_LOCAL_NAME_REVIEW`: nome e município coincidem, mas falta revisão
  humana de identidade.
- `DISTRICT_NAME_COLLISION_REVIEW`: mesmo nome no distrito, mas outra
  cidade ou freguesia; não se deve afirmar automaticamente que é o
  mesmo pavilhão.
- `LOCAL_PAVILION_ALIAS_REVIEW`: variantes de pavilhão no município,
  incluindo linhas com distrito vazio.
- Recintos próximos continuam a ser apenas pistas geográficas,
  sem equivalência de identidade, GPS de entrada ou autorização.

Não há alterações ao Liquibase, ao Render, aos recintos ou a qualquer
registo de produção. O resultado do pré-voo é somente um relatório
para investigação. A consulta PostgreSQL autorizada e verificação
da entrada dos jogadores continuam pendentes.
