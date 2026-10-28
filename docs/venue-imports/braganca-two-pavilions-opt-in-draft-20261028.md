# Lote de Bragança — DRAFT opt-in (28/10/2026)

**NÃO executado em produção.** O changelog mestre não inclui o YAML
desta pasta. Só poderá tornar-se executável depois de aprovação separada
e verificação num Postgres autorizado.

BE #502 comparou os dois pavilhões com 3614 recintos LIVE:
- Arnaldo Pereira: 3 recintos vizinhos — piscinas municipais (ID 1141),
  piscinas do Clube Académico (ID 1140), estádio municipal (ID 1139).
- Coxa: estádio municipal (ID 1139), a cerca de 194 metros.
- Nenhum dos dois pavilhões tinha nome exato no catálogo público do distrito,
  mas isso **não prova ausência de aliases ou recintos sem GPS**.

Identidade distinta confirmada no Diário da República, concurso
19668/2024: https://diariodarepublica.pt/dr/detalhe/anuncio-procedimento/19668-2024-888585905

Ponto da construção do Pavilhão da Coxa:
https://www.alnconstrucoes.com/obras/2019/pavilhao-polidesportivo-da-coxa-braganca

Ponto do **conjunto** Arnaldo Pereira e piscina (não da entrada):
https://arquitecturaaqui.eu/pt/obras/4824/pavilhao-municipal-arnaldo-pereira-braganca

Contexto de utilização: https://estrelasbrigantinas.com/contactos

Os pontos GPS no SQL são **localizações documentais de sítio**, ainda
não certificados como entradas ou locais exatos de chegada dos jogadores.
Há um valor de latitude contraditório no índice de um plano municipal
de emergência para Arnaldo Pereira; NÃO foi usado. Modos de acesso,
aluguer, disponibilidade, horários, direitos de reutilização e
modalidades definitivas não foram verificados.

Segurança do DRAFT:
- Apenas dois INSERT idempotentes, sem alterar registos existentes.
- Guardas por referência, nome distrital e possíveis aliases de pavilhão
  com GPS próximo. Recintos distintos (piscinas/estádio) não são fundidos.
- Telemóveis e websites NULL; não inventa contactos.
- Preflight PostgreSQL de leitura sob
  `venue-imports/preflight-braganca-two-pavilions-READONLY.sql`.
- Workflow de integração testa SQL num PostgreSQL 16 descartável e verifica
  que o master nunca inclui este DRAFT.
- Antes de qualquer execução: identidade final, GPS de acesso, revisão
  das fontes/licenças, read-only preflight autorizado e backup/restore
  validado. Autorizar especificamente a escrita e publicação depois.

## Correção de identidade histórica (28/10/2026)

A Câmara Municipal deliberou **em 9 de fevereiro de 2015** dar o nome
Arnaldo Pereira ao antigo **Pavilhão Municipal**, pelo que o nome
antigo não pode ser tratado como um recinto novo. Fonte oficial:
https://www.cm-braganca.pt/uploads/writer_file/document/3277/Diploma_de_homenagem_Arnaldo_Pereira.pdf

O pré-voo SQL e a minuta agora verificam os aliases de nome mais comuns
(`Pavilhão Municipal`, `Pavilhão Municipal de Bragança` e
`Pavilhão Gimnodesportivo`), circunscritos à cidade, freguesias
correspondentes ou rua/zona desportiva, **mesmo sem GPS**. Este bloqueio
não se aplica ao `Pavilhão da Bancada`, ao estádio nem às piscinas,
que são outras instalações municipais.

O **Guia das Acessibilidades dos Espaços Desportivos do Distrito de
Bragança**, disponível no site municipal de Vila Flor, publica para
Arnaldo Pereira os algarismos `41805258, -6769586`, que poderiam
corresponder a `41.805258, -6.769586` se decimalizados. A interpretação
é **não validada** e fica apenas como evidência secundária para revisão.
O ponto do estudo Arquitectura Aqui fica a ~66,6 metros deste valor
interpretado. Não alterar automaticamente o GPS do rascunho nem
apresentar qualquer desses valores como **entrada verificada**.
Fonte: https://www.cm-vilaflor.pt/uploads/writer_file/document/1736/GUIA_BRAGAN_A.pdf

O Pavilhão Polidesportivo da Coxa acolheu o Torneio Regional da
Castanha de Hóquei DI em **15 de outubro de 2026**, confirmando uso
recente mas **não livre disponibilidade** para reserva. Fontes:
https://jornalnordeste.com/noticias/braganca-recebe-7-torneio-regional-da-castanha-de-hoque-di
https://santosmartires.pt/desporto-inclusivo-hoquei-di/

As fontes da identidade, GPS de sítio e da atividade são diferentes;
nenhuma autoriza lançar uma entrada GPS individual sem revisão.

## Regulamento municipal definitivo 977/2026 — confirmação oficial

**Nova fonte prioritária e em vigor:** [Regulamento n.º 977/2026,
de 23 de julho](https://diariodarepublica.pt/dr/detalhe/regulamento/977-2026-1151247864),
Capítulo H-1, anexo H-1/I e tabela de taxas. A identificação, as moradas,
os desportos e os requisitos de acesso **substituem** as afirmações
provisórias dos editais de consulta pública de setembro/outubro de 2025.

| Dado | Arnaldo Pereira | Coxa |
| --- | --- | --- |
| Registo municipal | 14242 | 13288 |
| Morada | Avenida D. Sancho I, Zona Desportiva, 5300-125 Bragança | Rua Padre Benjamim Videira Pires, s/n, 5300-464 Bragança |
| Modalidades | Futsal, andebol, voleibol, basquetebol, hóquei em patins | Futsal, andebol, voleibol, basquetebol, ténis |
| Dias úteis (horário regulamentar habitual) | 08h30–12h30; 14h30–23h30 | 08h30–12h30; 14h30–23h30 |

- **Uso condicionado:** marcação prévia, autorização do Município de
  Bragança e taxa de utilização, salvo isenções/reduções aplicáveis
  (artigos H-1/27.º e H-1/32.º). O horário **não é disponibilidade**.
- Tabela regulamentar, artigo 18.º: 23 €/hora ou fração em dias úteis,
  35 €/hora ou fração em fins de semana e feriados, para cedência da
  nave central; estes são valores-base regulamentares, não um preço
  de reserva Jynlo nem confirmação de um horário livre.
- **Acesso de participantes no Arnaldo:** o Anexo H-1/I indica
  receção/entrada de participantes no rés do chão da lateral norte.
  Continua por confirmar o ponto de entrada geográfico exato.
- O regulamento identifica piscina, estádio e pavilhão da Bancada
  como instalações distintas e o pavilhão da Bancada como parte do
  estádio, evitando confundir recintos próximos.

O SQL DRAFT já contém **as moradas e modalidades desta fonte final**.
As coordenadas continuam a ser **pontos documentais de local**, não
portas certificadas; não ativar importação até rever a precisão e fazer
o pré-voo LIVE PostgreSQL autorizado.

**Requisito funcional para o produto Jynlo:** na futura ficha destes
recintos e no fluxo de criação de sessões, apresentar uma indicação
clara de que é necessária autorização/reserva prévia pelo Município
e de que uma sessão criada na Jynlo **não reserva o espaço**. Não
publicar a tabela como preço garantido, nem uma disponibilidade fictícia.

## Verificação de retificações e aviso na Jynlo — 28/10/2026

A fonte legal principal, o [Regulamento n.º 977/2026](https://diariodarepublica.pt/dr/detalhe/regulamento/977-2026-1151247864), **foi retificada duas vezes**:
- [Declaração de Retificação n.º 715/2026/2 (21 agosto)](https://diariodarepublica.pt/dr/detalhe/declaracao-retificacao/715-2026-1160677051): ajusta os artigos C-2/10.º e K-2/11.º, sem alterar os requisitos de reserva dos pavilhões citados acima.
- [Declaração de Retificação n.º 756/2026/2 (1 setembro)](https://diariodarepublica.pt/dr/detalhe/declaracao-retificacao/756-2026-1164280738): entre outros, altera as regras de ingressos de **museus e piscinas** (art. K-1/9.º) e o número de utilizadores para cedência de uma pista de piscina (tabela, art. 17.º). **Não aplicar esta alteração aos preços do pavilhão**, e não mostrar desconto ou preço de reserva como garantido.

No frontend atual, `src/app/pages/venue-details/venue-details.component.html`
já apresenta, para todos os recintos, a tradução
`venue.detail.accessNotice`: «Antes de organizar uma sessão, confirma
diretamente com a entidade gestora os horários e as condições de utilização.
A Jynlo não faz reservas.» A versão inglesa também existe. **Não duplicar**
o aviso na ficha dos recintos por este lote. Uma possível comunicação futura
específica de autorização municipal deve recorrer a dados de acesso verificáveis
e a um modelo genérico, não a uma exceção codificada pelo nome do pavilhão.

Estas verificações não eliminam a necessidade de confirmar localização exata
de acesso, aliases e documentação de reutilização antes de publicar os dois
recintos. Os dois `INSERT` continuam desativados no master e não autorizados.
