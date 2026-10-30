# Auditoria documental de recintos sem GPS — Maia 285 e Mira 286
Data: 2026-10-27. **Sem proposta GPS, alteração de morada, fusão, importação ou migração de dados.**

## Registos atuais confirmados via API pública
- **285: Polidesportivo Municipal do Paço**, Maia, distrito Porto, `Rua do Paço, Pedrouços, 4425-678 Maia`, OFFICIAL, sem latitude e longitude. Fonte guardada: https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco .
- **286: Campo de Padel do Fojo**, Mira, distrito Coimbra, `Fojo, Seixo de Mira, Mira`, OFFICIAL, sem latitude e longitude. Fonte guardada: https://www.cm-mira.pt/node/4084 .

## ID 285 — distinção de instalações municipais
A Câmara Municipal da Maia confirma o Polidesportivo do Paço na Rua do Paço, junto à Escola Básica de Parada, e nomeia **Polidesportivo Municipal de Pedrouços** noutra morada, na Travessa Gonçalo Mendes da Maia. O inventário municipal distingue ainda o estádio e outro polidesportivo de Pedrouços. Fontes:
- https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco
- https://www.cm-maia.pt/desporto/instalacoes-desportivas?pois_list_14_page=4

Não assumir que estes equipamentos são duplicados, que o GPS da escola serve o polidesportivo ou que a morada já identifica a entrada.

## ID 286 — padel e futebol 11 no mesmo complexo não são o mesmo recinto
A Câmara Municipal de Mira documenta o **novo campo de padel do Fojo**, no Seixo de Mira, inserido no Complexo Desportivo do Fojo; em notícia municipal distinta documenta o **campo de futebol 11 do Seixo** e a requalificação do relvado sintético do Fojo. São modalidades e instalações diferentes.
- https://www.cm-mira.pt/node/4084 — inauguração do campo de padel
- https://www.cm-mira.pt/node/3308 — campo de futebol 11

Não copiar o ponto do futebol, do complexo ou de um Fojo homónimo em Vila Nova de Gaia. Não atribuir coordenadas a partir de um centroide do Seixo de Mira.

## Implementação
Apenas a revisão `GET /api/admin/overview/venue-gps-review` para ADMIN/SUPER_ADMIN recebe o objeto opcional `facilityContextReview` com código fechado e **duas fontes oficiais HTTPS**, sem campo de latitude/longitude. O modo `onlyWithEvidence=true` integra 285/286 num conjunto de IDs explícito, revendo em tempo real nome, localidade, distrito, morada, fonte OFFICIAL, website guardado e ausência total de coordenadas. Se algum destes campos mudar, o aviso desaparece. A listagem normal mantém-se compatível.

O frontend existente da fila apresenta um painel de alerta PT/EN diferente do cartão de candidato GPS e usa o validador HTTPS habitual. É documentação para revisão humana, não confirmação de duplicação nem autorização de correção.
