# Recolha offline de recintos nos 18 distritos

O processo nacional **não escreve na BD** e só lê ficheiros OSM `*.osm.pbf`
já descarregados. Não requer CI, Render ou chaves externas.

1. Obter localmente o GeoPackage oficial CAOP2025 e converter as duas
   camadas de municípios e distritos para GeoJSON EPSG:4326, conforme
   `docs/national-venues-import.md`.
2. Guardar o PBF de cada distrito em `.cache/jynlo-venues/<slug>-latest.osm.pbf`
   (fonte: https://download.openstreetmap.fr/extracts/europe/portugal/).
   O script **nunca** descarrega ficheiros, por isso é seguro executá-lo
   mesmo num ambiente sem acesso à Internet.
3. Processar um distrito ou todo o continente:

```bash
python3 scripts/national_venue_batch_runner.py --district evora \
  --cache-dir .cache/jynlo-venues \
  --municipalities-geojson /caminho/municipios.geojson \
  --districts-geojson /caminho/distritos.geojson

python3 scripts/national_venue_batch_runner.py --district all \
  --cache-dir .cache/jynlo-venues \
  --municipalities-geojson /caminho/municipios.geojson \
  --districts-geojson /caminho/distritos.geojson
```

O relatório em `.cache/jynlo-venues/national-report.json` é atualizado
**após cada distrito**, de forma atómica, e preserva resultados anteriormente
processados. Distritos com PBF em falta ficam `status: failed`, não zero
recintos. A execução devolve código 1 quando algum dos distritos pedidos
falha. Todos os valores são **candidatos**, não entradas aprovadas.

Testar localmente sem pyosmium/PBF reais (faz mock do stage):

```bash
python3 -m unittest discover -s scripts -p 'test_national_venue_batch_runner.py' -v
```

Este runner serve apenas para pré-visualização. Inserções futuras requerem
verificação de identidade/acesso, comparação de duplicados na BD,
aprovação individual e Liquibase de lote curado. Não promover ficheiros
`*-venues.jsonl` a SQL de produção automaticamente.
