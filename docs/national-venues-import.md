# Cobertura nacional de recintos: execução segura

## Objetivo

O catálogo deve cobrir progressivamente todos os 18 distritos de Portugal continental.
**Cobertura integral de todos os recintos existentes não é garantida por nenhuma
fonte pública.** Diferenciar recintos recolhidos, importados e verificados. Não publicar
percentagens de cobertura sem denominador oficial por concelho.

## Fonte

- OpenStreetMap: extratos distritais do espelho francês
  https://download.openstreetmap.fr/extracts/europe/portugal/
- Extratos de 18 distritos (exclui Açores e Madeira), cada um descarregado apenas
  uma vez e reutilizado localmente. Extratos com data identificável.
- Dados OSM sob **ODbL**. Atribuição visível «Dados de OpenStreetMap» e
  cumprimento das obrigações de disponibilidade de base de dados derivada antes
  de publicar grandes quantidades. https://www.openstreetmap.org/copyright
- Complementar com fonte oficial SNID após validar URL, licença e qualidade.
  **Não misturar fontes com licenças incompatíveis num dataset derivado.**

## Piloto de Portalegre com CAOP2025

O workflow de simulação `.github/workflows/national-venue-portalegre-dryrun.yml` descarrega o
GeoPackage oficial **CAOP2025** da Direção-Geral do Território (CC BY 4.0),
converte as duas camadas `cont_municipios` e `cont_distritos` com GDAL para GeoJSON em EPSG:4326,
e compara **duas execuções com o mesmo extrato OSM**: sem concelhos e com concelhos
oficiais. Não existe qualquer API de escrita ou token para a base de dados.

Origem do ficheiro: https://geo2.dgterritorio.gov.pt/caop/CAOP_Continente_2025-gpkg.zip

Além de contagens globais, o relatório inclui os tipos de instalação para
distinguir piscinas privadas/sem nome de campos, pavilhões e complexos
efetivamente identificados. **Não importar piscinas sem nome ou recintos
com acesso não verificado apenas para aumentar o número de entradas.**

## Completar concelhos sem APIs pagas

A fonte oficial [CAOP2025 da DGT](https://www.dgterritorio.gov.pt/atividades/cartografia/cartografia-tematica/caop),
sob licença CC BY 4.0, permite associar recintos a municípios pelas coordenadas.
Descarregar o GeoPackage oficial continental e converter **apenas a camada de
municípios** para GeoJSON em WGS84 (EPSG:4326), usando ogr2ogr/QGIS.
Identificar a camada no QGIS/`ogrinfo`, pois o seu nome pode variar.

Usar **ambas as fontes CAOP** no importador:
`--municipalities-geojson /caminho/municipios.geojson` e
`--districts-geojson /caminho/distritos.geojson`.
Se a geometria oficial indicar um distrito diferente do extrato PBF, o
elemento é excluído desse distrito e pertence ao extrato do distrito real.
Sem correspondência com os polígonos **de distrito ou de município**, o recinto fica em revisão,
mesmo que o OpenStreetMap apresente uma localidade textual, nome e modalidade.
O concelho oficial prevalece sobre o texto de localidade do OpenStreetMap.

## Preparar e auditar sem escrever no servidor

```bash
python3 -m pip install osmium
python3 -m unittest discover -s scripts -p 'test_import_osm_venues_national.py'
python3 scripts/import_osm_venues_national.py --district all --municipalities-geojson /caminho/municipios.geojson --districts-geojson /caminho/distritos.geojson
```

Pela primeira vez são percorridos **todos os 18 distritos** de forma automática,
sem BBOX manual. Cada distrito produz **dois** ficheiros JSONL: `*-venues.jsonl` com dados aptos a importar e `*-review.jsonl` com os registos incompletos, **nunca submetidos automaticamente**. Contagens
`candidates`, `prepared`, `import_ready`, `review_required`, `name_to_verify`, `municipality_to_verify`,
`address_to_verify`, `sports_to_verify`, `district_to_verify`,
`skipped_other_district`, `skipped_restricted_access`.
O resumo consolidado é `.cache/jynlo-venues/national-report.json`. Não
partilhar o relatório com tokens nem adicionar o cache ao Git.

**Atenção:** não indicar que um recinto aceita reservas ou é público apenas
porque aparece no OpenStreetMap. As modalidades são preservadas apenas quando
estão marcadas; se desconhecidas, não inventar. Relações multipolígono com
apenas geometria de relação são excluídas, devendo ser tratadas num passo posterior.

## Antes de importar em produção

1. Confirmar a escolha do workspace Render e uma cópia de segurança da BD.
2. Rever amostras por distrito, incluindo recintos sem nome ou morada.
3. Confirmar o endpoint protegido (backend desta branch) e validar as métricas
   por distrito e a pesquisa paginada `GET /api/venues/suggest`.
4. A importação deve ser feita fora do arranque do serviço, num computador
   próprio ou runner controlado. Não consumir CPU/RAM de serviço Render.
5. Apenas então, com confirmação explícita do proprietário:

```bash
export D24_API_URL='https://api.d24sports.com'
export D24_ADMIN_TOKEN='TOKEN_DE_ADMIN'  # nunca gravar em ficheiros ou Git
python3 scripts/import_osm_venues_national.py --district aveiro --municipalities-geojson /caminho/municipios.geojson --districts-geojson /caminho/distritos.geojson --apply --confirm IMPORTAR_RECINTOS
# Após validar um distrito: --district all, sem --refresh durante retoma
```

As chamadas enviam lotes de 50, têm progressos por distrito, e retomar utiliza
checksums do JSONL para evitar saltar ou repetir lotes inadvertidamente.
Sem ambos os GeoJSON oficiais, `--apply` é recusado. A ausência de CAOP
permite apenas pré-visualização com os recintos em quarentena, nunca
publicação automática. A extração oficial deve ser efetuada fora do Render,
num computador local e com custos controlados.
ID de fonte OSM é estável. O endpoint é idempotente por fonte, e valida
duplicados próximos com o mesmo nome; nunca apagar ou sobrescrever recintos
com origem diferente. Em caso de erro HTTP parar e inspecionar.

## Critérios para considerar a cobertura aceitável

- Relatório dos 18 distritos processados e sumário por distrito/concelho.
- Sem blocos de dados por importar por causa de RAM/timeout; auditar erros.
- Pesquisa por distrito/localidade e por modalidade com paginação e índices.
- Recintos sem nome/modalidade/concelho em quarentena, não publicados automaticamente. A ausência de rua explícita é apresentada como 'morada por confirmar', com coordenadas OSM verificáveis; podem ser corrigidos por colaboradores.
- Propor fontes municipais/regionais para as lacunas de OSM/SNID, com licença.
- Confirmar número antes e depois na BD, com duplicados e erros discriminados.
- Não declarar «todos os recintos» sem processo de validação territorial.

## Preparar GeoJSON da CAOP localmente (sem GitHub Actions)

No computador local, descarregar o GeoPackage **CAOP2025 — Continente** da
Direção-Geral do Território. Não é necessário enviar a cartografia ao GitHub.
Depois de instalar o GDAL (`ogr2ogr` e `ogrinfo`):

```bash
# O GeoPackage pode ter outro nome; ajustar o caminho depois de extrair o ZIP.
ogrinfo -ro /caminho/CAOP_Continente_2025.gpkg
ogr2ogr -f GeoJSON -t_srs EPSG:4326 -lco RFC7946=YES \
  /caminho/municipios.geojson /caminho/CAOP_Continente_2025.gpkg cont_municipios
ogr2ogr -f GeoJSON -t_srs EPSG:4326 -lco RFC7946=YES \
  /caminho/distritos.geojson /caminho/CAOP_Continente_2025.gpkg cont_distritos
python3 scripts/import_osm_venues_national.py --district portalegre \
  --municipalities-geojson /caminho/municipios.geojson \
  --districts-geojson /caminho/distritos.geojson
```

O importador compara a coordenada de cada recinto com os polígonos dos
18 distritos. Só assim poderá classificar como aptos os candidatos de
Portalegre sem arriscar repetir o erro observado com o município de Mora
(que pertence ao distrito de Évora). Os números do primeiro piloto antigo
não são diretamente comparáveis com o novo critério de validação.
O cruzamento com a CAOP ainda não foi executado com dados reais nesta etapa.


## Triagem de nomes e duplicados a partir do piloto real

A primeira recolha de Portalegre encontrou quatro campos com os nomes
`01`, `02`, `03` e `04`, e dois objetos distintos denominados
«Praça de Touros» separados por cerca de 1,3 metros.
O importador mantém esses elementos na fonte, mas assinala os
nomes numéricos e rótulos genéricos (por exemplo `Piscina` ou
`Campo de Futebol`) com `name_to_verify` para não os publicar
como instalações identificadas.

A proximidade espacial deve ser auditada antes de gerar SQL: um recinto
pode conter diferentes campos lado a lado, pelo que a proximidade não
autoriza apagar ou fundir automaticamente os elementos OSM.

**Limitação:** a cartografia oficial CAOP2025 não foi descarregada neste
ambiente e o piloto enriquecido ainda não foi executado. Os totais de
importação permanecem por confirmar; não divulgar cobertura nacional
como concluída.
