# Bragança — recintos separados Arnaldo Pereira e Coxa, auditoria read-only

Data: 28 de outubro de 2026.

## Identidade oficial

O Município de Bragança identificou por nome próprio os pavilhões Arnaldo
Pereira, Coxa e Bancada como **instalações distintas** num concurso público
para manutenção/limpeza. A piscina municipal também consta separadamente:

https://diariodarepublica.pt/dr/detalhe/anuncio-procedimento/19668-2024-888585905

O sítio do clube Estrelas Brigantinas confirma a utilização do Arnaldo
Pereira para jogos e da Coxa para treinos:

https://estrelasbrigantinas.com/contactos

Os registos de atividades não permitem inferir abertura geral, aluguer ou
disponibilidade de sessões; estes dados continuam por verificar.

## Pontos de investigação, não GPS autorizado

- **Arnaldo Pereira:** estudo académico Arquitectura Aqui, ponto do
  **conjunto histórico** de pavilhão e piscina; não é a entrada nem uma
  posição inequivocamente dentro da área do pavilhão.
  Fonte: https://arquitecturaaqui.eu/pt/obras/4824/pavilhao-municipal-arnaldo-pereira-braganca
- **Pavilhão Polidesportivo da Coxa:** a página da construtora, promotora
  Câmara Municipal de Bragança, publica o ponto de sítio da obra concluída
  em 2019. Não comprova posição da entrada ou acesso.
  Fonte: https://www.alnconstrucoes.com/obras/2019/pavilhao-polidesportivo-da-coxa-braganca
- **Divergência importante:** resultados indexados do plano de emergência
  municipal de 2021 mostram para o Arnaldo Pereira latitude 41.569643,
  muito distante do conjunto documentado no estudo e na cartografia
  da cidade. Esse valor **não deve ser copiado** nem tratado como válido
  para navegação; é necessária revisão manual do original.
- O sítio Arquitetura Aqui também documenta um pavilhão do **Clube
  Académico de Bragança** separado do pavilhão municipal:
  https://arquitecturaaqui.eu/obras/4827/pavilhao-do-clube-academico-de-braganca

## Consulta e anti-duplicação

`scripts/audit_braganca_two_pavilions_20261028.py` faz apenas um
GET ao catálogo público fixo `/api/venues`. Para cada pavilhão:

1. Procura nomes exatamente iguais em **todo o distrito**, incluindo
   recintos com cidade indicada como freguesia ou GPS ausente.
2. Lista até doze recintos com GPS a 250m do ponto documentado,
   com distância aproximada e nome/ID **já públicos**.
3. Nunca deduz identidade da mera proximidade: piscinas, estádio,
   pavilhões e outros recintos do complexo podem ser **distintos**.
4. Não exporta contacto, morada ou coordenadas individuais do catálogo
   e não gera quaisquer SQL, POST, importações ou aprovações.

A baseline de 28/10/2026 continha 3614 registos públicos (39 no
distrito Bragança). Nos 250m em torno do Arnaldo já existiam as
Piscinas Municipais (ID 1141), Piscinas do Clube Académico (ID 1140)
e Estádio Municipal de Bragança (ID 1139). O mesmo nome completo do
pavilhão Arnaldo não apareceu. Isto **não prova** a sua ausência.

Os dados a publicar só poderão ser aprovados após verificação de
identidade de cada edifício, precisão de GPS, modalidade, direitos de
reutilização da fonte, deduplicação no PostgreSQL autorizado,
garantia de backup/restore e avaliação do acesso ao recinto.

Esta etapa guarda apenas o relatório de revisão durante três dias
em GitHub Actions. Não precisa de deploy Render.

## Pré-voo público: deteção de aliases sem GPS (28/10/2026)

A auditoria `scripts/audit_braganca_two_pavilions_20261028.py` passou a
identificar potenciais aliases com distrito Bragança (ou distrito ausente)
e cidade/freguesia compatível, **mesmo quando o GPS está vazio**:

- Arnaldo Pereira: `Pavilhão Municipal`, variantes de Bragança,
  `Pavilhão Gimnodesportivo` e nomes que incluam `Arnaldo Pereira`.
- Coxa: variantes contendo `Coxa` e pavilhão/polidesportivo/gimnodesportivo.
- `Pavilhão da Bancada`, estádio e piscinas não são assimilados aos pavilhões.

O resultado `POSSIBLE_ALIAS_REVIEW` exige revisão humana; não confirma
duplicado e nunca autoriza importação. O relatório limita os candidatos a
12 por recinto, preserva a contagem total e não publica contactos, moradas
nem coordenadas individuais. O catálogo público não substitui o pré-voo
SQL autorizado à base de dados. A alteração é só auditoria, sem endpoint de
escrita ou execução da migração opt-in.

## Como chegar — pistas oficiais, mas não entradas certificadas (28/10/2026)

O portal municipal [Bragança Bus — Linhas e Horários](https://bus.cm-braganca.pt/routes)
apresenta uma paragem chamada **«Pavilhão Arnaldo Pereira»** e lista as
correspondências R1, R3, R5 e R7. Isto é uma pista prática de
transporte público para o equipamento e uma fonte oficial autónoma,
sem depender do ponto arquitetónico do complexo.

**Não** existem neste lote as coordenadas oficiais exatas da paragem,
a posição da porta de entrada, horários em tempo real, frequência
confirmada para a data da sessão ou garantias de funcionamento.
O Regulamento n.º 977/2026 descreve uma receção/entrada para os
praticantes na lateral norte do rés do chão, mas a ligação entre essa
receção e uma coordenada cartográfica ainda não foi certificada.
Uma futura experiência de navegação deverá consultar a fonte de
transportes e distinguir **paragem de autocarro**, **referência de
complexo** e **entrada de jogadores**.

No **Pavilhão da Coxa**, a empresa construtora publica fotografias
da fachada durante a obra de 2019:
https://www.alnconstrucoes.com/obras/2019/pavilhao-polidesportivo-da-coxa-braganca .
Portas visíveis durante uma obra **não demonstram** qual o acesso
público atualmente autorizado, nem o percurso seguro para jogadores.
Não atribuir ponto de chegada a partir da fotografia.

O manifesto distingue explicitamente estes dois níveis de evidência.
O teste de contrato impede promover a paragem, a fotografia ou a
referência de sítio a entrada certificada e impede sugerir reservas
de recinto efetuadas pela Jynlo. **Sem alteração de base de dados,
recintos públicos, Liquibase ou deploy.**
