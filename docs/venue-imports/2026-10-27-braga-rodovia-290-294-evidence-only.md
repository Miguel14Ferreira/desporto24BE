# Braga — Rodovia 290–294: campos distintos, basquetebol em ficha conjunta

**Data:** 2026-10-27. **Resultado:** auditoria e alerta documental para ADMIN; `approved_for_import=false`. Nenhuma proposta GPS, migração, escrita, fusão nem importação.

## Fichas LIVE previamente existentes, ainda sem GPS

| Jynlo ID | Ficha guardada | Página municipal guardada |
|---|---|---|
| 290 | Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia | https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6225 |
| 291 | Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia | https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6226 |
| 292 | Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia | https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6229 |
| 293 | Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia | https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6230 |
| 294 | Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia | https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6231 |

Em todas: `city=Braga`, `district=Braga`, `informationSource=OFFICIAL`, `address=Complexo Desportivo da Rodovia, 4710-294 Braga` e latitude/longitude nulas. O website exato é diferente por ficha.

## Evidência e interpretação

As páginas municipais designam separadamente os campos de futebol 7 n.º 2 e n.º 4, os campos de futebol n.º 5 e n.º 6. Essas páginas **não dão coordenadas geográficas específicas da superfície**. A própria morada é genérica do complexo.

A Câmara chama ao equipamento de basquetebol **"Campo de Basquetebol N.º 1 e 2"** (singular) na página de equipamento 6231, enquanto o registo Jynlo usa **"Campos de Basquetebol N.º 1 e 2"** (plural). A página municipal do **Parque da Rodovia** confirma que existem **dois campos de basquetebol**:
https://www.cm-braga.pt/pt/1201/viver/ambiente/espacos-verdes/item/item-1-722

**Decisão prudente:** uma única ficha Jynlo (294) representa conjuntamente os dois campos. Não criar automaticamente dois novos IDs; não copiar GPS de outro campo ou do parque; não interpretar a página como coordenada ou entrada.

## Implementação de revisão administrativa

O registo deliberadamente limitado `GPS_FACILITY_CONTEXT` agora reconhece estes 5 IDs apenas na fila ADMIN/SUPER_ADMIN `GET /api/admin/overview/venue-gps-review?onlyWithEvidence=true`. Cada registo mostra duas fontes HTTPS oficiais (campo e campo complementar, ou parque para basquetebol), e um código de aviso: `SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT` (290–293) ou `GROUPED_BASKETBALL_COURTS_NO_POINT` (294).

Todos os avisos são **opcionais e anuláveis** em tempo de leitura: só aparecem quando ID, nome completo incluindo número, cidade, distrito, morada, origem OFFICIAL, URL municipal guardado e *ambas* coordenadas ausentes ainda coincidem. O mecanismo não recebe nem escreve coordenadas para estes casos. A fila normal, paginação e pesquisa por distrito permanecem intactas.

O frontend usa o cartão de contexto já existente, com legenda PT/EN específica para a ficha conjunta de basquetebol. Não abre marcador de mapa e não apresenta proposta automática de divisão ou fusão.

## Próxima investigação técnica, sem ação automática

Confrontar ortofoto recente e eventuais polígonos cartográficos **identificados individualmente** pelos números de campo; confirmar se o agrupamento da ficha 294 é a representação desejada do produto. A aprovação de qualquer GPS exige fonte precisa e revisão humana através do circuito administrativo normal.
