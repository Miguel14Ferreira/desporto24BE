# Reconciliação nacional offline: só CSV de recintos, sem dados pessoais

Depois de descarregar o artifact da recolha nacional, guardar o ficheiro manual-review-candidates.jsonl.gz.
Usar exportação READ ONLY de desporto24.venue, SEM dados de perfis, utilizadores ou tokens.

Com PGSSLMODE=require e PGOPTIONS=-c default_transaction_read_only=on, abrir psql com as credenciais usuais. Executar dentro de psql:

    \copy (SELECT id, name, city, district, address, latitude, longitude, source_reference FROM desporto24.venue ORDER BY id) TO '/tmp/jynlo-existing-venues.csv' WITH (FORMAT csv, HEADER true)

Executar localmente no checkout do backend:

    python3 scripts/reconcile_national_stage.py --candidates /caminho/manual-review-candidates.jsonl.gz --existing-csv /tmp/jynlo-existing-venues.csv --report /tmp/jynlo-reconcile-summary.json --review-csv /tmp/jynlo-reconcile.csv

Os estados são EXISTING_REFERENCE, POSSIBLE_DUPLICATE e UNMATCHED_REQUIRES_REVIEW.
Moradas partilhadas ou posições a menos de 100m são suspeitas, não prova de duplicado. Não faz uploads para GitHub, não conecta à BD, não aprova nem importa recintos.
Apagar a exportação local depois da revisão. Respeitar OSM ODbL e DGT CC BY 4.0.
