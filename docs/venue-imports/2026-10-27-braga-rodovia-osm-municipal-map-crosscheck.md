# Braga / Rodovia: divergência entre Parque Desportivo e Complexo Desportivo

**Data de revisão:** 27-10-2026. **Âmbito:** Jynlo 288–294, apenas auditoria administrativa e contexto cartográfico, nenhuma aprovação GPS.

## Novo documento municipal (2020)
O **Roteiro Desportivo e Lazer 2020** da Câmara de Braga (ficheiro disponível no portal da juventude municipal desde pelo menos 2023) distingue explicitamente:

- **Parque Desportivo da Rodovia:** futebol 11 (1), futebol 7 (1), futebol 5 (3), futebol de praia (1), voleibol de praia (1), dois campos de basquetebol e outras atividades de lazer.
- **Complexo Desportivo da Rodovia**, descrito como espaço **contíguo** ao Parque: piscina exterior (1), piscina climatizada (1), ténis (3), padel (2).
- O roteiro localiza o parque na **Av. João Paulo II, 4715-034 Braga**. O catálogo municipal atual usa **Complexo Desportivo da Rodovia, 4710-294 Braga** em várias fichas dos campos numerados.

Fonte primária: https://juventude.cm-braga.pt/wp-content/uploads/2023/06/Roteiro-Desportivo-e-Lazer-2020.pdf — página 3 do PDF (índice zero 2).

**Não concluir que as fichas atuais estão incorretas.** O documento é histórico, e os nomes/contagens dos campos e as moradas podem representar épocas ou recortes do complexo diferentes. O objetivo da comparação é **não confundir espaço contíguo com campo numerado**.

## Cartografia OSM, não GPS individual dos campos

- https://mapcarta.com/pt/W106454979 — **Parque Desportivo da Rodovia**, OSM way `106454979`, `landuse=recreation_ground` (área recreativa), centro aproximado 41.55354,-8.40250. **Não é um campo numerado nem entrada**.
- https://mapcarta.com/pt/W884706818 — **Complexo Desportivo da Rodovia**, OSM way `884706818`, `building=sports_centre` (edifício), centro aproximado 41.55212,-8.40553. **Não é um campo numerado nem entrada**.

Não usar os centroides acima para preencher `latitude` / `longitude` de Jynlo 288–294. Nem uma área recreativa, nem o edifício adjacente identificam qual o campo n.º 1, 2, 3, 4, 5 ou 6.

O inventário municipal atual de equipamentos continua a distinguir separadamente os campos numerados: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas.

## Integração admin read-only

O backend acrescenta `facilityContextReview.cartographyReview` exclusivamente a fichas **288–294**, e só depois de passar os mesmos critérios rigorosos existentes para cada ficha: ID, nome exato, cidade, distrito, morada, website municipal oficial, fonte OFFICIAL e **ambas as coordenadas ainda ausentes**. O payload inclui apenas um `issueCode` restrito e três URLs de evidência para investigar. **Não inclui coordenadas candidatas nem inferência de acesso**.

O frontend mostra um cartão PT/EN diferenciado, com aviso de que os polígonos representam apenas o Parque e um edifício desportivo, não superfícies numeradas, e três ligações HTTPS validadas. Os testes cobrem ausência de GPS, não divulgação em outras fichas, obsolescência da identidade guardada, filtros e zero escritas.

**Sem liquibase, importações, novos recintos, alterações de morada, fusões ou aprovação automática.**

## Próximo requisito de precisão
Só avançar para uma proposta GPS por campo com uma planta atual e georreferenciada **cuja legenda associe um identificador exato do campo à superfície representada**, ou uma ficha cartográfica oficial equivalente. Um satélite sem numeração e um centroide de parque são insuficientes.
