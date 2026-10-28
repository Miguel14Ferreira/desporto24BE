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
