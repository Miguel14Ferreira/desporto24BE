# EULisboa — correspondência de recintos, 26/10/2026

**Estado:** propostas de curadoria, sem escrita na base de dados e sem aprovação automática.  
**Origem LIVE:** `GET /api/venues/{id}` com GPS e `GPS_ONLY_ADDRESS_UNKNOWN`.  
**Identificação OSM:** importação histórica `national-map-osm-lisboa-02.sql` (22/10/2026).  
**Confirmação institucional:** [Campos oficiais](https://www.estadio.ulisboa.pt/instalacoes/grandes-campos-de-jogos) e [planta de localização (abril 2026)](https://www.estadio.ulisboa.pt/sites/default/files/documents/document/2026-04/a3-mapa-reservas.pdf).

## Propostas de nomes (ainda NÃO aprovadas)

| ID Jynlo | OSM WAY | Nome LIVE | Nome proposto | Etiqueta na planta |
|---|---|---|---|---|
| 1965 | 49290574 | Campo 3 | Estádio Universitário de Lisboa — Campo 3 | 2.2 |
| 1966 | 49290573 | Campo 4 | Estádio Universitário de Lisboa — Campo 4 | 2.3 |
| 1968 | 49290575 | Campo 5 | Estádio Universitário de Lisboa — Campo 5 | 2.4 |
| 1969 | 49290576 | Campo 6 | Estádio Universitário de Lisboa — Campo 6 | 2.5 |
| 1970 | 54754001 | Campo 7 | Estádio Universitário de Lisboa — Campo 7 | 2.6 |

As posições relativas estão de acordo com a planta: 4 e 6 a norte; 3 e 5 (ID 1968) abaixo; 7 mais a sul. Esta comparação é **posicional**, não uma sobreposição topológica exata das geometrias das áreas desportivas. Manter revisão por administrador antes de qualquer alteração, mesmo para os cinco candidatos fortes.

## Duas exceções importantes

- **1967 — Campo 5**, OSM:WAY:196085505, GPS (38.7511714, -9.1648749): é um campo **diferente**, vários centenas de metros a sul da área 2.4 da planta. Não confundir com o ID 1968, nem apagar, fundir ou atribuir o nome do campo 2.4 sem nova prova.
- **1972 — Campo da Mata**, OSM:WAY:569394331, GPS (38.7127475, -9.1995598): pertence a outra zona, próximo do Campus da Ajuda. A referência municipal indica a **Rua Ildefonso Borges, Lisboa**; confirmar o acesso exato antes de aprovar uma morada. [Fonte municipal](https://www.lisboa.pt/fileadmin/informacao/publicacoes/revista_lisboa/Revista_Lisboa_34.pdf), [ficha complementar](https://www.timeout.pt/lisboa/pt/desporto/campo-da-mata).

## Morada versus instalação

A fonte institucional publica `Av. Professor Egas Moniz, 1600-190 Lisboa` como **morada geral do Campus Estádio Universitário**, mas isso **não confirma a entrada postal específica de nenhum dos cinco campos**. Portanto, **não é proposta nenhuma alteração a `venue.address`**. A página Jynlo deve continuar a apresentar «Morada por confirmar» enquanto o acesso exato não estiver documentado.

A fonte confirma ainda a prática de futebol nos campos 3–7 e de rugby no Campo 4. Caso seja necessária a inclusão de rugby no ID 1966, confirmar previamente a correspondência dessa modalidade com o catálogo Jynlo e submeter uma proposta separada de `sports`.

## Fluxo de aprovação

1. Abrir a ficha da instalação original usando o ID Jynlo e OSM WAY correspondente.
2. Reconfirmar GPS com a planta atual e, idealmente, limites do campo em base cartográfica.
3. Submeter **uma proposta por ID** pelo mecanismo de «Sugerir alteração» com fonte institucional HTTPS e nome proposto; nunca alterar diretamente por lote.
4. Rever e aprovar manualmente na administração. Em caso de alteração concorrente, usar o bloqueio de conflito do backend.
5. Confirmar via API LIVE `GET /api/venues/{id}` a alteração aprovada; validar que não surgiram duplicados nem houve mudança de morada, coordenadas ou origem.
6. Conservar casos ambíguos com nome original e revisão pendente.

Os dados estruturados estão no ficheiro adjacente `lisboa-eul-generic-names-review-20261026.json`.
