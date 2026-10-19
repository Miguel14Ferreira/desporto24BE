# Medir cobertura nacional sem pagar CI nem fazer deploy

A recolha OSM já gera `national-report.json` com as estatísticas por distrito.
Para ter uma tabela utilizável no Excel / LibreOffice, sem aceder à BD:

```bash
python3 scripts/import_osm_venues_national.py --district all \
  --municipalities-geojson /caminho/municipios.geojson \
  --districts-geojson /caminho/distritos.geojson

python3 scripts/venue_coverage_report.py \
  --input .cache/jynlo-venues/national-report.json \
  --output .cache/jynlo-venues/estado-distritos.csv
```

O CSV tem uma linha por cada um dos **18 distritos**, incluindo os ainda não
processados. Distingue candidatos OSM, recintos cujo nome/modalidade/concelho
são conhecidos, entradas em revisão, ausência de cartografia, instalações
que pertencem a outro distrito e exclusões por acesso restrito.

Este relatório mede **qualidade da fonte**, não o número de recintos reais
disponíveis ao público, nem registos efetivamente inseridos na Jynlo.
Recintos prontos no relatório continuam a necessitar de aprovação final,
deduplicação e validação legal. Os scripts não inserem dados na BD.

Teste local isolado:

```bash
python3 -m unittest discover -s scripts -p 'test_venue_coverage_report.py'
```
