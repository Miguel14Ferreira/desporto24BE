# Preparar pesquisa por distrito para recintos existentes (draft)

A pesquisa por distrito do PR de backend #114 devolve apenas recintos que já
têm `district`. Os cerca de 228 recintos anteriores incluem entradas ainda
sem esse campo, pelo que podem desaparecer da pesquisa por distrito caso se
publique o filtro sem migrar os metadados.

Esta migração **opcional** preenche `venue.district` apenas quando está
NULL/vazio e `venue.city` é uma cidade/município com associação de distrito
conhecida. Para nomes de localidades mais genéricos da área de Lisboa
(por exemplo, Parede), exige também coordenadas na região de Lisboa.
**Nunca modifica** um distrito preenchido e nunca apaga ou cria recintos.

Nomes ambíguos, incluindo `Terrugem`, ficam sem classificação e podem
continuar a ser encontrados pela pesquisa nacional. Recomendamos completar
esses casos depois usando limites oficiais CAOP2025 em EPSG:4326, com
verificação geométrica correta por concelho (não por distrito do extrato OSM).

Ficheiros: `2026-10-19-backfill-known-districts-DRAFT.sql` e
`2026-10-19-backfill-known-districts-DRAFT.yaml`.
Ambos **fora do changelog principal**. Só incluir após verificar em staging
`SELECT city, COUNT(*) ... GROUP BY city`, confirmar eventuais homónimos,
testar PostgreSQL e aprovar o deploy com backup.

Smoke test sem BD:
`python3 -m unittest discover -s scripts -p 'test_venue_district_backfill_draft.py' -v`.

As correspondências cobrem 18 distritos e 114 nomes de
municípios/localidades, não toda a cartografia de Portugal. Evitar alegar
cobertura total ou atribuição automática de todos os recintos.
