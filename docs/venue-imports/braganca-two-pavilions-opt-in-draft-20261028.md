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
