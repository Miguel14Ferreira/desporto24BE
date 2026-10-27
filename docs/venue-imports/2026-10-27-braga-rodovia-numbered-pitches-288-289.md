# Braga — Campos n.º 1 e n.º 3 da Rodovia / identidade e GPS por confirmar

**Data:** 27 de outubro de 2026. **Estado:** investigação documental, sem proposta de coordenadas, sem importação e sem aprovação GPS.

## Validação na API pública LIVE
- **Jynlo ID 288:** `Campo de Futebol N.º1 do Complexo Desportivo da Rodovia`; cidade/distrito Braga; `Complexo Desportivo da Rodovia, 4710-294 Braga`; fonte `OFFICIAL`; latitude/longitude ausentes. Website guardado: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6223
- **Jynlo ID 289:** `Campo de Futebol N.º3 do Complexo Desportivo da Rodovia`; mesma cidade/distrito/morada; `OFFICIAL`; latitude/longitude ausentes. Website guardado: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6224

## Evidência municipal de instalações distintas
A Câmara Municipal de Braga publica **páginas separadas** com nomes e números próprios para o campo n.º 1 (item 6223) e para o n.º 3 (item 6224). O índice geral da Câmara lista também campos de futebol 7 n.º 2 e n.º 4, campos n.º 5 e n.º 6 e campos de basquetebol do mesmo complexo.

- Campo n.º 1: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6223
- Campo n.º 3: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6224
- Inventário municipal: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas
- Fonte oficial turística que designa a área como **Campos de Futebol** no plural: https://visitbraga.travel/it/highlights/desporto-e-lazer/campos-de-futebol-do-complexo-desportivo-da-rodovia/

A morada/postal code do complexo **não localiza cada superfície**; o número do campo não é coordenada nem fornece localização cartográfica. Não utilizar o centro do complexo, um edifício/pavilhão OSM, o marcador de uma piscina ou o GPS de um dos outros campos para preencher qualquer destes dois IDs.

## Comportamento administrativo implementado
- Apenas a lista autenticada e read-only `GET /api/admin/overview/venue-gps-review?onlyWithEvidence=true` apresenta uma nota `facilityContextReview` com o código `SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT`, sem latitude/longitude e com duas URLs municipais específicas (ficha própria e ficha do outro campo).
- O aviso aparece **apenas** se o ID exato e identidade guardada (nome incluindo número, cidade, distrito, morada, fonte OFFICIAL, website municipal específico) continuarem corretos e **ambos** os valores GPS continuarem em falta.
- A página normal, filtros por distrito/página, ordenação e segurança ADMIN/SUPER_ADMIN continuam inalterados. O frontend reutiliza o cartão de aviso existente e acrescenta uma tradução contextual PT/EN.
- **Não criar nem fundir recintos.** Sem SQL/Liquibase, escritura de coordenadas, importação ou alteração de ficha. Qualquer futura proposta exige provas geográficas sobre cada superfície e aprovação administrativa explícita.

### Próximos passos na investigação documental
Verificar separadamente se existem plantas técnicas georreferenciadas ou polígonos OSM com etiqueta específica `Campo n.º1` e `Campo n.º3`, e confrontar essas referências com ortofotos/numeração municipal. Até lá, **ambos os casos são apenas avisos de identidade**, não candidatos GPS.
