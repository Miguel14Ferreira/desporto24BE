# Jynlo — revisão GIS municipal e quatro recintos sem GPS (27/10/2026)

**Estado:** investigação apenas, nenhuma coordenada GPS aprovada ou alterada.

## Registos existentes consultados na API pública

| ID | Recinto | Fonte oficial | Lacuna |
|---|---|---|---|
| 279 | Pavilhão Desportivo EX-EPC, Santarém | https://viversantarem.pt/pavilhoes-desportivos/ | Morada Rua dos Ex-Combatentes do Ultramar, 2005-246 Santarém; falta GPS do próprio pavilhão, distinto da Nave Desportiva |
| 286 | Campo de Padel do Fojo, Mira | https://www.cm-mira.pt/node/4084 e https://www.cm-mira.pt/node/4081 | Novo court inaugurado em novembro de 2025 no Complexo Desportivo do Fojo, Seixo de Mira; não usar centro do complexo |
| 295 | Piscina Climatizada de Maximinos, Braga | https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6262 | Rua da Naia, 4710-057 Braga; falta posição exata da piscina |
| 299 | Piscina Municipal de São Jacinto, Aveiro | https://www.cm-aveiro.pt/visitantes/piscina-municipal-de-sao-jacinto | Piscina municipal requalificada em 2025; não usar GPS da praia ou do novo campo de voleibol |

O serviço público Jynlo em GET /api/venues/{id} mostrava latitude/longitude ausentes para os quatro IDs em 27/10/2026. Estes registos já existem: não criar duplicados, não modificar moradas, não copiar coordenadas de equipamentos vizinhos.

**Falso positivo importante:** a Câmara de Aveiro publica GPS da **Praia** de São Jacinto em https://www.cm-aveiro.pt/visitantes/visitar/praia-de-sao-jacinto. Essas coordenadas NÃO são da Piscina Municipal de São Jacinto.

## Nova fonte cartográfica oficial encontrada

- **Lisboa:** https://services.arcgis.com/1dSrzEWVQn5kHHyK/arcgis/rest/services/Desporto_Instalacoes/FeatureServer/0 . O serviço identifica pontos de instalações desportivas com OBJECTID, DESIGNACAO e MORADA. Feições **2027** e **2028** têm ambas o nome **Campo das Amoreiras**, mas pontos diferentes: correspondência por nome não pode escolher uma delas automaticamente.
- **Loulé:** https://geoloule.cm-loule.pt/server/rest/services/MapasOnline/MO_Desporto/FeatureServer/0 . O serviço municipal disponibiliza DESIG, modalidade, morada e geometria nativa **EPSG:3763**. Exportar explicitamente com outSR=4326 antes da auditoria.

Os serviços Lisboa/Loulé **não** são fontes de GPS para Santarém, Mira, Braga ou Aveiro. Apenas podem contribuir para cruzamentos de recintos nas respetivas jurisdições.

## Ferramenta offline adicionada

~~~bash
python3 scripts/audit_official_municipal_gis.py \
  --venues-json /tmp/venues-publicas.json \
  --gis-geojson /tmp/lisboa-instalacoes.geojson \
  --source lisboa --municipality Lisboa --district Lisboa \
  --output /tmp/jynlo-municipal-gis-review.json
~~~

Usar snapshot aprovado do catálogo público GET https://api.d24sports.com/api/venues. Obter GeoJSON por GET ao serviço GIS oficial em /query com where=1=1, outFields=OBJECTID,DESIGNACAO,MORADA, outSR=4326, f=geojson, paginando quando necessário. Verificar direitos de reutilização antes de qualquer publicação do dataset.

Restrições: correspondência exata de recinto, município e distrito; só recintos **já existentes** e **sem GPS**; só pontos WGS84, nunca centroides; proteção contra feições municipais duplicadas e nomes de recintos Jynlo duplicados. O relatório guarda coordenadas como **evidência a rever**, nunca como instrução de atualização. Todas as entradas têm approvedForGPS=false; resultado global writeActions=0 e approvedCount=0.

Mesmo uma feição oficial com correspondência exata pode ser um marcador genérico da instalação, não a entrada, court ou campo. Validar sempre identidade, atualidade e precisão com um revisor. Não executar Liquibase nem afirmar que GPS está LIVE sem verificação do deploy e do registo público.
