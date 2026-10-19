-- PROPOSTA — NÃO ATIVAR SEM REVISÃO E APROVAÇÃO DO PROPRIETÁRIO.
-- Portalegre: 9 recintos corroborados por fontes independentes e coordenadas OSM.
-- Estes registos NÃO passaram por comparação exaustiva com a BD em produção.
-- Dados geográficos: OpenStreetMap contributors, ODbL https://www.openstreetmap.org/copyright
-- Changelog NÃO incluído em db.changelog-master.yaml. Nenhum UPDATE ou DELETE.
-- Inserções condicionais por origem e nome+proximidade; não impedem todos os alias.

-- Recinto Multiusos de Ponte de Sor | OSM:WAY:157099522 | https://www.cm-pontedesor.pt/infraestruturas-desporto/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Recinto Multiusos de Ponte de Sor', 'Ponte de Sor', 'Portalegre', 'Zona Desportiva de Ponte de Sor', 'Futebol', 'Campo desportivo', 'OUTDOOR',
       39.2550475, -8.0119079, 'OPENSTREETMAP', 'OSM:WAY:157099522',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:157099522')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Recinto Multiusos de Ponte de Sor'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Ponte de Sor'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 39.2547475 AND 39.2553475
                  AND existing.longitude BETWEEN -8.0123079 AND -8.0115079)))
)
ON CONFLICT DO NOTHING;

-- Campo de Futebol Sintético de Sousel | OSM:WAY:426295154 | https://cm-sousel.pt/sou-municipe/desporto/instalacoes-e-equipamentos/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Sintético de Sousel', 'Sousel', 'Portalegre', 'Estrada da Circunvalação, 7470-210, Sousel', 'Futebol', 'Campo desportivo', 'OUTDOOR',
       38.9544786, -7.6714955, 'OPENSTREETMAP', 'OSM:WAY:426295154',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:426295154')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol Sintético de Sousel'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Sousel'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 38.9541786 AND 38.9547786
                  AND existing.longitude BETWEEN -7.6718955 AND -7.6710955)))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Gimnodesportivo de Sousel | OSM:WAY:426296888 | https://cm-sousel.pt/sou-municipe/desporto/instalacoes-e-equipamentos/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo de Sousel', 'Sousel', 'Portalegre', 'Estrada da Circunvalação, 7470-210, Sousel', 'Futsal', 'Pavilhão desportivo', 'INDOOR',
       38.9553066, -7.6708754, 'OPENSTREETMAP', 'OSM:WAY:426296888',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:426296888')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Gimnodesportivo de Sousel'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Sousel'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 38.9550066 AND 38.9556066
                  AND existing.longitude BETWEEN -7.6712754 AND -7.6704754)))
)
ON CONFLICT DO NOTHING;

-- Campo de Futebol de Cano | OSM:WAY:429474882 | https://www.cm-sousel.pt/wp-content/uploads/2023/05/PMEPC_SOUSEL_PARTE_II_VCP.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Cano', 'Cano', 'Portalegre', 'Largo do Rossio, 7470-022, Cano', 'Futebol', 'Campo desportivo', 'OUTDOOR',
       38.9627718, -7.7639828, 'OPENSTREETMAP', 'OSM:WAY:429474882',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:429474882')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol de Cano'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Cano'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 38.9624718 AND 38.9630718
                  AND existing.longitude BETWEEN -7.7643828 AND -7.7635828)))
)
ON CONFLICT DO NOTHING;

-- Polidesportivo de Cano | OSM:WAY:429475622 | https://www.cm-sousel.pt/wp-content/uploads/2023/05/PMEPC_SOUSEL_PARTE_II_VCP.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Cano', 'Cano', 'Portalegre', 'Largo do Rossio, 7470-022, Cano', 'Futebol', 'Campo desportivo', 'OUTDOOR',
       38.9632269, -7.7628395, 'OPENSTREETMAP', 'OSM:WAY:429475622',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:429475622')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Polidesportivo de Cano'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Cano'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 38.9629269 AND 38.9635269
                  AND existing.longitude BETWEEN -7.7632395 AND -7.7624395)))
)
ON CONFLICT DO NOTHING;

-- Campo de Futebol de Casa Branca | OSM:WAY:429476238 | https://www.cm-sousel.pt/wp-content/uploads/2023/05/PMEPC_SOUSEL_PARTE_II_VCP.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Casa Branca', 'Casa Branca', 'Portalegre', 'Rua D. Rodrigo Mendia de Castro, 7470-999, Casa Branca', 'Futebol', 'Campo desportivo', 'OUTDOOR',
       38.9503679, -7.8053164, 'OPENSTREETMAP', 'OSM:WAY:429476238',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:429476238')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol de Casa Branca'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Casa Branca'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 38.9500679 AND 38.9506679
                  AND existing.longitude BETWEEN -7.8057164 AND -7.8049164)))
)
ON CONFLICT DO NOTHING;

-- Campo de Futebol de Santo Amaro | OSM:WAY:429477908 | https://www.cm-sousel.pt/wp-content/uploads/2023/05/PMEPC_SOUSEL_PARTE_II_VCP.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Santo Amaro', 'Santo Amaro', 'Portalegre', 'Rua Visconde D’Asseca, 7470-155, Santo Amaro', 'Futebol', 'Campo desportivo', 'OUTDOOR',
       38.9705205, -7.5836824, 'OPENSTREETMAP', 'OSM:WAY:429477908',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:429477908')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol de Santo Amaro'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Santo Amaro'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 38.9702205 AND 38.9708205
                  AND existing.longitude BETWEEN -7.5840824 AND -7.5832824)))
)
ON CONFLICT DO NOTHING;

-- Estádio Municipal de Ponte de Sor | OSM:WAY:1275651401 | https://www.cm-pontedesor.pt/torneio-da-amizade-em-ponte-de-sor/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Ponte de Sor', 'Ponte de Sor', 'Portalegre', 'Zona Desportiva de Ponte de Sor', 'Futebol;Atletismo', 'Estádio', 'OUTDOOR',
       39.2539045, -8.0132653, 'OPENSTREETMAP', 'OSM:WAY:1275651401',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:1275651401')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Estádio Municipal de Ponte de Sor'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Ponte de Sor'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 39.2536045 AND 39.2542045
                  AND existing.longitude BETWEEN -8.0136653 AND -8.0128653)))
)
ON CONFLICT DO NOTHING;

-- Estádio Patalino | OSM:WAY:1348549266 | https://arquitecturaaqui.eu/pt/obras/19583/estadio-patalino-elvas
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Patalino', 'Elvas', 'Portalegre', 'Rossio do Meio, 7350-135, Elvas', 'Futebol', 'Estádio', 'OUTDOOR',
       38.8772886, -7.1577876, 'OPENSTREETMAP', 'OSM:WAY:1348549266',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = 'OSM:WAY:1348549266')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Estádio Patalino'))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Elvas'))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN 38.8769886 AND 38.8775886
                  AND existing.longitude BETWEEN -7.1581876 AND -7.1573876)))
)
ON CONFLICT DO NOTHING;

