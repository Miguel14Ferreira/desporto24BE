-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:14255698578 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio GymnoB', 'Portalegre', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.2622866, -7.4311988, 'OPENSTREETMAP', 'OSM:NODE:14255698578',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14255698578')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio GymnoB'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2622866)) <= 0.00070
               AND ABS(v.longitude - (-7.4311988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:352171633 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Seda', 'Alter do Chão', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1879597, -7.7919908, 'OPENSTREETMAP', 'OSM:WAY:352171633',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:352171633')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alter do Chão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Seda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1879597)) <= 0.00070
               AND ABS(v.longitude - (-7.7919908)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:101179435 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Ferragial d''El Rei', 'Alter do Chão', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1982865, -7.6630899, 'OPENSTREETMAP', 'OSM:WAY:101179435',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:101179435')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alter do Chão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Ferragial d''El Rei'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1982865)) <= 0.00070
               AND ABS(v.longitude - (-7.6630899)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:848954572 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Seda', 'Alter do Chão', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.1888704, -7.7857101, 'OPENSTREETMAP', 'OSM:WAY:848954572',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:848954572')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alter do Chão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Seda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1888704)) <= 0.00070
               AND ABS(v.longitude - (-7.7857101)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1172871633 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Alter do Chão', 'Alter do Chão', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.1978302, -7.6616691, 'OPENSTREETMAP', 'OSM:WAY:1172871633',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1172871633')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alter do Chão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Alter do Chão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1978302)) <= 0.00070
               AND ABS(v.longitude - (-7.6616691)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:842164433 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Seda', 'Alter do Chão', 'Portalegre', NULL,
       'Futsal', 'Campo desportivo', NULL,
       39.1885399, -7.7912670, 'OPENSTREETMAP', 'OSM:WAY:842164433',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:842164433')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alter do Chão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Seda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1885399)) <= 0.00070
               AND ABS(v.longitude - (-7.7912670)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:333580360 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo De Jogos Dos Mosteiros', 'Arronches', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1863713, -7.2852745, 'OPENSTREETMAP', 'OSM:WAY:333580360',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:333580360')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arronches'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo De Jogos Dos Mosteiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1863713)) <= 0.00070
               AND ABS(v.longitude - (-7.2852745)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:92162438 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue Municipal', 'Arronches', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1203589, -7.2875428, 'OPENSTREETMAP', 'OSM:WAY:92162438',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:92162438')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arronches'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1203589)) <= 0.00070
               AND ABS(v.longitude - (-7.2875428)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:473170156 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark', 'Arronches', 'Portalegre', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.1216306, -7.2795463, 'OPENSTREETMAP', 'OSM:WAY:473170156',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:473170156')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arronches'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1216306)) <= 0.00070
               AND ABS(v.longitude - (-7.2795463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:239020190 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 1º de Maio', 'Avis', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1057122, -7.8732548, 'OPENSTREETMAP', 'OSM:WAY:239020190',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:239020190')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1057122)) <= 0.00070
               AND ABS(v.longitude - (-7.8732548)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:495707744 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 25 de Abril', 'Avis', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0554842, -7.9014466, 'OPENSTREETMAP', 'OSM:WAY:495707744',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:495707744')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0554842)) <= 0.00070
               AND ABS(v.longitude - (-7.9014466)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5351786347 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Municipal de Avis', 'Avis', 'Portalegre', NULL,
       'Ginásio;Ginástica', 'Ginásio', NULL,
       39.0519351, -7.8916535, 'OPENSTREETMAP', 'OSM:NODE:5351786347',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5351786347')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Municipal de Avis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0519351)) <= 0.00070
               AND ABS(v.longitude - (-7.8916535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544548644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Exercícios', 'Avis', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0567524, -7.8935728, 'OPENSTREETMAP', 'OSM:WAY:544548644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544548644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Exercícios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0567524)) <= 0.00070
               AND ABS(v.longitude - (-7.8935728)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:236910375 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Avis', 'Avis', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.0555881, -7.9124638, 'OPENSTREETMAP', 'OSM:WAY:236910375',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:236910375')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Avis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0555881)) <= 0.00070
               AND ABS(v.longitude - (-7.9124638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:552605840 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas da Junta de Freguesia', 'Avis', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.0540135, -7.8882407, 'OPENSTREETMAP', 'OSM:WAY:552605840',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:552605840')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas da Junta de Freguesia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0540135)) <= 0.00070
               AND ABS(v.longitude - (-7.8882407)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1104940750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Públicas', 'Avis', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.0184085, -7.9107047, 'OPENSTREETMAP', 'OSM:WAY:1104940750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1104940750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Públicas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0184085)) <= 0.00070
               AND ABS(v.longitude - (-7.9107047)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7418898995 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pump Track - XCO Avis', 'Avis', 'Portalegre', NULL,
       'Ciclismo;BMX', 'Pista desportiva', NULL,
       39.0543086, -7.8859658, 'OPENSTREETMAP', 'OSM:NODE:7418898995',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7418898995')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Avis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pump Track - XCO Avis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0543086)) <= 0.00070
               AND ABS(v.longitude - (-7.8859658)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102867362 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos João Manuel Nabeiro', 'Campo Maior', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0138996, -7.0606433, 'OPENSTREETMAP', 'OSM:WAY:102867362',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102867362')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Campo Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos João Manuel Nabeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0138996)) <= 0.00070
               AND ABS(v.longitude - (-7.0606433)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157032691 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Padel da J.F. da Expectação', 'Campo Maior', 'Portalegre', NULL,
       'Padel', 'Campo desportivo', NULL,
       39.0127527, -7.0612085, 'OPENSTREETMAP', 'OSM:WAY:157032691',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157032691')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Campo Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Padel da J.F. da Expectação'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0127527)) <= 0.00070
               AND ABS(v.longitude - (-7.0612085)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157023129 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estadio Capitão César Correia', 'Campo Maior', 'Portalegre', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.0138530, -7.0619145, 'OPENSTREETMAP', 'OSM:WAY:157023129',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157023129')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Campo Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estadio Capitão César Correia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0138530)) <= 0.00070
               AND ABS(v.longitude - (-7.0619145)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10574657898 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio OC Fitness', 'Campo Maior', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0143664, -7.0611458, 'OPENSTREETMAP', 'OSM:NODE:10574657898',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10574657898')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Campo Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio OC Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0143664)) <= 0.00070
               AND ABS(v.longitude - (-7.0611458)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157187771 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta', 'Campo Maior', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.0172746, -7.0759722, 'OPENSTREETMAP', 'OSM:WAY:157187771',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157187771')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Campo Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0172746)) <= 0.00070
               AND ABS(v.longitude - (-7.0759722)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1134270015 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Campo Maior', 'Campo Maior', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.0123799, -7.0593930, 'OPENSTREETMAP', 'OSM:WAY:1134270015',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1134270015')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Campo Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Campo Maior'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0123799)) <= 0.00070
               AND ABS(v.longitude - (-7.0593930)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:370098915 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Skate', 'Castelo de Vide', 'Portalegre', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.4092334, -7.4474050, 'OPENSTREETMAP', 'OSM:WAY:370098915',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:370098915')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Vide'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Skate'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4092334)) <= 0.00070
               AND ABS(v.longitude - (-7.4474050)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1414603537 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Castelo de Vide', 'Castelo de Vide', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.4115723, -7.4503939, 'OPENSTREETMAP', 'OSM:WAY:1414603537',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1414603537')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Vide'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Castelo de Vide'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4115723)) <= 0.00070
               AND ABS(v.longitude - (-7.4503939)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:296197594 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Gáfete', 'Crato', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4159453, -7.6897506, 'OPENSTREETMAP', 'OSM:WAY:296197594',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:296197594')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Crato'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Gáfete'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4159453)) <= 0.00070
               AND ABS(v.longitude - (-7.6897506)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:892496686 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Aquático do Crato', 'Crato', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2930796, -7.6516273, 'OPENSTREETMAP', 'OSM:WAY:892496686',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:892496686')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Crato'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Aquático do Crato'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2930796)) <= 0.00070
               AND ABS(v.longitude - (-7.6516273)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:892496702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Cobertas Municipais', 'Crato', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2920826, -7.6512112, 'OPENSTREETMAP', 'OSM:WAY:892496702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:892496702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Crato'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Cobertas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2920826)) <= 0.00070
               AND ABS(v.longitude - (-7.6512112)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:842410627 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Gáfete', 'Crato', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.4116376, -7.6851099, 'OPENSTREETMAP', 'OSM:WAY:842410627',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:842410627')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Crato'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Gáfete'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4116376)) <= 0.00070
               AND ABS(v.longitude - (-7.6851099)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:96495436 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo António Picão Caldeira', 'Elvas', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0033009, -7.2483488, 'OPENSTREETMAP', 'OSM:WAY:96495436',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:96495436')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo António Picão Caldeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0033009)) <= 0.00070
               AND ABS(v.longitude - (-7.2483488)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1117681923 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo António Semedo "O Tigre Alentejano"', 'Elvas', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8776358, -7.1567001, 'OPENSTREETMAP', 'OSM:WAY:1117681923',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1117681923')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo António Semedo "O Tigre Alentejano"'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8776358)) <= 0.00070
               AND ABS(v.longitude - (-7.1567001)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:125962063 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vila Boim', 'Elvas', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8588813, -7.2849865, 'OPENSTREETMAP', 'OSM:WAY:125962063',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:125962063')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vila Boim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8588813)) <= 0.00070
               AND ABS(v.longitude - (-7.2849865)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:144557535 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Terrugem', 'Elvas', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8480104, -7.3477539, 'OPENSTREETMAP', 'OSM:WAY:144557535',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:144557535')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Terrugem'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8480104)) <= 0.00070
               AND ABS(v.longitude - (-7.3477539)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1117681904 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Pedro Barrena', 'Elvas', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8767049, -7.1588656, 'OPENSTREETMAP', 'OSM:WAY:1117681904',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1117681904')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Pedro Barrena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8767049)) <= 0.00070
               AND ABS(v.longitude - (-7.1588656)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:189422368 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Tiro e Caça de Elvas', 'Elvas', 'Portalegre', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       38.8780269, -7.1555179, 'OPENSTREETMAP', 'OSM:WAY:189422368',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:189422368')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Tiro e Caça de Elvas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8780269)) <= 0.00070
               AND ABS(v.longitude - (-7.1555179)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1278557449 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CUBO', 'Elvas', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8760676, -7.1597311, 'OPENSTREETMAP', 'OSM:WAY:1278557449',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1278557449')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CUBO'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8760676)) <= 0.00070
               AND ABS(v.longitude - (-7.1597311)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:643431913 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Atletismo de Elvas', 'Elvas', 'Portalegre', NULL,
       'Rugby;Atletismo', 'Estádio', NULL,
       38.8799571, -7.1538072, 'OPENSTREETMAP', 'OSM:WAY:643431913',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:643431913')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Atletismo de Elvas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8799571)) <= 0.00070
               AND ABS(v.longitude - (-7.1538072)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:599939685 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginasio Fica fit', 'Elvas', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8764216, -7.1599110, 'OPENSTREETMAP', 'OSM:WAY:599939685',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:599939685')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginasio Fica fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8764216)) <= 0.00070
               AND ABS(v.longitude - (-7.1599110)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10725646629 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Go Fitness healthclub', 'Elvas', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8823554, -7.1711410, 'OPENSTREETMAP', 'OSM:NODE:10725646629',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10725646629')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Go Fitness healthclub'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8823554)) <= 0.00070
               AND ABS(v.longitude - (-7.1711410)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12656769067 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Go Fitness Healthclub', 'Elvas', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8680717, -7.1647861, 'OPENSTREETMAP', 'OSM:NODE:12656769067',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12656769067')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Go Fitness Healthclub'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8680717)) <= 0.00070
               AND ABS(v.longitude - (-7.1647861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:631107233 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Santa Eulália', 'Elvas', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.0107672, -7.2537562, 'OPENSTREETMAP', 'OSM:WAY:631107233',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:631107233')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Santa Eulália'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0107672)) <= 0.00070
               AND ABS(v.longitude - (-7.2537562)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1117194292 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Terrugem Elvas', 'Elvas', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8463903, -7.3480313, 'OPENSTREETMAP', 'OSM:WAY:1117194292',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1117194292')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Terrugem Elvas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8463903)) <= 0.00070
               AND ABS(v.longitude - (-7.3480313)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1133956450 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Elvas', 'Elvas', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8729522, -7.1925315, 'OPENSTREETMAP', 'OSM:WAY:1133956450',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1133956450')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Elvas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8729522)) <= 0.00070
               AND ABS(v.longitude - (-7.1925315)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:554545433 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Poilidesportivo de Vila Fernando', 'Elvas', 'Portalegre', NULL,
       'Futsal', 'Campo desportivo', NULL,
       38.9200985, -7.3221897, 'OPENSTREETMAP', 'OSM:WAY:554545433',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:554545433')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Poilidesportivo de Vila Fernando'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9200985)) <= 0.00070
               AND ABS(v.longitude - (-7.3221897)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:453804814 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Bernardino Canário', 'Elvas', 'Portalegre', NULL,
       'Futsal', 'Campo desportivo', NULL,
       38.8745164, -7.1696500, 'OPENSTREETMAP', 'OSM:WAY:453804814',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:453804814')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Bernardino Canário'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8745164)) <= 0.00070
               AND ABS(v.longitude - (-7.1696500)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:219428894 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Terrugem', 'Elvas', 'Portalegre', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       38.8454474, -7.3483319, 'OPENSTREETMAP', 'OSM:WAY:219428894',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:219428894')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Terrugem'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8454474)) <= 0.00070
               AND ABS(v.longitude - (-7.3483319)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:228666038 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Vila Boim', 'Elvas', 'Portalegre', NULL,
       'Futsal', 'Campo desportivo', NULL,
       38.8629712, -7.2755541, 'OPENSTREETMAP', 'OSM:WAY:228666038',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:228666038')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Vila Boim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8629712)) <= 0.00070
               AND ABS(v.longitude - (-7.2755541)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1150459105 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Bairro de São Pedro', 'Elvas', 'Portalegre', NULL,
       'Futsal', 'Campo desportivo', NULL,
       38.8701058, -7.1626062, 'OPENSTREETMAP', 'OSM:WAY:1150459105',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1150459105')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Elvas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Bairro de São Pedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8701058)) <= 0.00070
               AND ABS(v.longitude - (-7.1626062)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:190953965 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Cândido de Oliveira', 'Fronteira', 'Portalegre', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.0512532, -7.6464161, 'OPENSTREETMAP', 'OSM:WAY:190953965',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:190953965')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fronteira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Cândido de Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0512532)) <= 0.00070
               AND ABS(v.longitude - (-7.6464161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:192141278 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Coberta', 'Fronteira', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.0519674, -7.6456215, 'OPENSTREETMAP', 'OSM:WAY:192141278',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:192141278')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fronteira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Coberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0519674)) <= 0.00070
               AND ABS(v.longitude - (-7.6456215)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:295694395 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Salgueirinho', 'Gavião', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4618631, -7.9455971, 'OPENSTREETMAP', 'OSM:WAY:295694395',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:295694395')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gavião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Salgueirinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4618631)) <= 0.00070
               AND ABS(v.longitude - (-7.9455971)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1202328371 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Eco-Laguna do Gavião', 'Gavião', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.4642091, -7.9367362, 'OPENSTREETMAP', 'OSM:WAY:1202328371',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1202328371')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gavião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Eco-Laguna do Gavião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4642091)) <= 0.00070
               AND ABS(v.longitude - (-7.9367362)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:396576418 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ammaia Clube Golfe Marvâo', 'Marvão', 'Portalegre', NULL,
       'Golfe', 'Campo de golfe', NULL,
       39.3843734, -7.3926525, 'OPENSTREETMAP', 'OSM:WAY:396576418',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:396576418')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marvão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ammaia Clube Golfe Marvâo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3843734)) <= 0.00070
               AND ABS(v.longitude - (-7.3926525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1411873257 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Praia fluvial de Portagem', 'Marvão', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.3824819, -7.3825164, 'OPENSTREETMAP', 'OSM:WAY:1411873257',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1411873257')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marvão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Praia fluvial de Portagem'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3824819)) <= 0.00070
               AND ABS(v.longitude - (-7.3825164)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:552160580 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Dona Maria Gabriela Vieira', 'Nisa', 'Portalegre', NULL,
       'Futebol', 'Estádio', NULL,
       39.5065081, -7.6419231, 'OPENSTREETMAP', 'OSM:WAY:552160580',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:552160580')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Nisa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Dona Maria Gabriela Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5065081)) <= 0.00070
               AND ABS(v.longitude - (-7.6419231)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:554843031 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Fonte Nova', 'Nisa', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5155280, -7.6414681, 'OPENSTREETMAP', 'OSM:WAY:554843031',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:554843031')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Nisa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Fonte Nova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5155280)) <= 0.00070
               AND ABS(v.longitude - (-7.6414681)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:648821567 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Farinha Branca', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1050008, -8.1986471, 'OPENSTREETMAP', 'OSM:WAY:648821567',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:648821567')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Farinha Branca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1050008)) <= 0.00070
               AND ABS(v.longitude - (-8.1986471)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:656813965 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Terra Preta', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1029270, -8.1884340, 'OPENSTREETMAP', 'OSM:WAY:656813965',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:656813965')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Terra Preta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1029270)) <= 0.00070
               AND ABS(v.longitude - (-8.1884340)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544166646 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Galveias', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1611652, -7.9792032, 'OPENSTREETMAP', 'OSM:WAY:544166646',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544166646')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Galveias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1611652)) <= 0.00070
               AND ABS(v.longitude - (-7.9792032)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:358174076 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futsal de Vale de Arco', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3563652, -7.9575862, 'OPENSTREETMAP', 'OSM:WAY:358174076',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:358174076')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futsal de Vale de Arco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3563652)) <= 0.00070
               AND ABS(v.longitude - (-7.9575862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:911882541 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Tramaga', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2273961, -8.0346653, 'OPENSTREETMAP', 'OSM:WAY:911882541',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:911882541')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Tramaga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2273961)) <= 0.00070
               AND ABS(v.longitude - (-8.0346653)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:810055267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Dona Berta Courinha', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Estádio', NULL,
       39.0811930, -8.1743085, 'OPENSTREETMAP', 'OSM:WAY:810055267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:810055267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Dona Berta Courinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0811930)) <= 0.00070
               AND ABS(v.longitude - (-8.1743085)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1544330742 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Matuzarense', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2434390, -8.0112395, 'OPENSTREETMAP', 'OSM:WAY:1544330742',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1544330742')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Matuzarense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2434390)) <= 0.00070
               AND ABS(v.longitude - (-8.0112395)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157550364 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis', 'Ponte de Sor', 'Portalegre', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       39.2479813, -8.0060955, 'OPENSTREETMAP', 'OSM:WAY:157550364',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157550364')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2479813)) <= 0.00070
               AND ABS(v.longitude - (-8.0060955)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1544330884 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Eurico Pais de Carvalho', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2558452, -7.9299066, 'OPENSTREETMAP', 'OSM:WAY:1544330884',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1544330884')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Eurico Pais de Carvalho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2558452)) <= 0.00070
               AND ABS(v.longitude - (-7.9299066)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1275727984 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Ténis de Ponte de Sor', 'Ponte de Sor', 'Portalegre', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       39.2521151, -8.0122958, 'OPENSTREETMAP', 'OSM:WAY:1275727984',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1275727984')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de Ténis de Ponte de Sor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2521151)) <= 0.00070
               AND ABS(v.longitude - (-8.0122958)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157693568 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas Oásis Parque', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.1580504, -7.9934212, 'OPENSTREETMAP', 'OSM:WAY:157693568',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157693568')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas Oásis Parque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1580504)) <= 0.00070
               AND ABS(v.longitude - (-7.9934212)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544166653 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Escorrega Aquatico - Oasis Parque', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.1577960, -7.9938445, 'OPENSTREETMAP', 'OSM:WAY:544166653',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544166653')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Escorrega Aquatico - Oasis Parque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1577960)) <= 0.00070
               AND ABS(v.longitude - (-7.9938445)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544650841 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Grande', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.2490045, -8.0060726, 'OPENSTREETMAP', 'OSM:WAY:544650841',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544650841')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2490045)) <= 0.00070
               AND ABS(v.longitude - (-8.0060726)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544166654 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Lago - Oasis Parque', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.1583221, -7.9937815, 'OPENSTREETMAP', 'OSM:WAY:544166654',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544166654')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Lago - Oasis Parque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1583221)) <= 0.00070
               AND ABS(v.longitude - (-7.9937815)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1474392996 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Media', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.2494009, -8.0059721, 'OPENSTREETMAP', 'OSM:WAY:1474392996',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1474392996')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Media'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2494009)) <= 0.00070
               AND ABS(v.longitude - (-8.0059721)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544650839 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Pequena', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.2492731, -8.0060600, 'OPENSTREETMAP', 'OSM:WAY:544650839',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544650839')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Pequena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2492731)) <= 0.00070
               AND ABS(v.longitude - (-8.0060600)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157939265 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Cobertas', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2523213, -8.0128209, 'OPENSTREETMAP', 'OSM:WAY:157939265',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157939265')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2523213)) <= 0.00070
               AND ABS(v.longitude - (-8.0128209)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158032722 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Descobertas', 'Ponte de Sor', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2494239, -8.0061242, 'OPENSTREETMAP', 'OSM:WAY:158032722',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158032722')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Descobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2494239)) <= 0.00070
               AND ABS(v.longitude - (-8.0061242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:660211174 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Futebol de Montargil', 'Ponte de Sor', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0802849, -8.1739717, 'OPENSTREETMAP', 'OSM:WAY:660211174',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:660211174')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Futebol de Montargil'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0802849)) <= 0.00070
               AND ABS(v.longitude - (-8.1739717)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157552173 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park', 'Ponte de Sor', 'Portalegre', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.2564717, -8.0124849, 'OPENSTREETMAP', 'OSM:WAY:157552173',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157552173')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Sor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2564717)) <= 0.00070
               AND ABS(v.longitude - (-8.0124849)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:306667790 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Granja', 'Portalegre', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2437882, -7.3226852, 'OPENSTREETMAP', 'OSM:WAY:306667790',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:306667790')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Granja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2437882)) <= 0.00070
               AND ABS(v.longitude - (-7.3226852)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:332418159 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Portalegre', 'Portalegre', 'Portalegre', NULL,
       'Futebol', 'Estádio', NULL,
       39.2980655, -7.4336774, 'OPENSTREETMAP', 'OSM:WAY:332418159',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:332418159')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Portalegre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2980655)) <= 0.00070
               AND ABS(v.longitude - (-7.4336774)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:300953860 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory', 'Portalegre', 'Portalegre', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.2759381, -7.4292846, 'OPENSTREETMAP', 'OSM:WAY:300953860',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:300953860')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2759381)) <= 0.00070
               AND ABS(v.longitude - (-7.4292846)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:605340427 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina da Fonte do Açude do Reguengo', 'Portalegre', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.2977123, -7.3962316, 'OPENSTREETMAP', 'OSM:WAY:605340427',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:605340427')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina da Fonte do Açude do Reguengo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2977123)) <= 0.00070
               AND ABS(v.longitude - (-7.3962316)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1078296966 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina da Quinta da Saúde', 'Portalegre', 'Portalegre', NULL,
       'Natação', 'Piscina', NULL,
       39.3092581, -7.4208396, 'OPENSTREETMAP', 'OSM:WAY:1078296966',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1078296966')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina da Quinta da Saúde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3092581)) <= 0.00070
               AND ABS(v.longitude - (-7.4208396)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1150776557 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Alegrete', 'Portalegre', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2413752, -7.3185652, 'OPENSTREETMAP', 'OSM:WAY:1150776557',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1150776557')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Alegrete'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2413752)) <= 0.00070
               AND ABS(v.longitude - (-7.3185652)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:300977684 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal dos Assentos', 'Portalegre', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2795036, -7.4297576, 'OPENSTREETMAP', 'OSM:WAY:300977684',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:300977684')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal dos Assentos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2795036)) <= 0.00070
               AND ABS(v.longitude - (-7.4297576)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155320958 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Portalegre', 'Portalegre', 'Portalegre', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2976199, -7.4347987, 'OPENSTREETMAP', 'OSM:WAY:1155320958',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155320958')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Portalegre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2976199)) <= 0.00070
               AND ABS(v.longitude - (-7.4347987)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:303502038 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Portalegre', 'Portalegre', 'Portalegre', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.2756422, -7.4253747, 'OPENSTREETMAP', 'OSM:WAY:303502038',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303502038')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Portalegre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2756422)) <= 0.00070
               AND ABS(v.longitude - (-7.4253747)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:332484136 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Atalaião', 'Portalegre', 'Portalegre', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2913186, -7.4240077, 'OPENSTREETMAP', 'OSM:WAY:332484136',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:332484136')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Atalaião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2913186)) <= 0.00070
               AND ABS(v.longitude - (-7.4240077)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:303503658 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Municipal', 'Portalegre', 'Portalegre', NULL,
       'Andebol', 'Campo desportivo', NULL,
       39.2987115, -7.4328336, 'OPENSTREETMAP', 'OSM:WAY:303503658',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303503658')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2987115)) <= 0.00070
               AND ABS(v.longitude - (-7.4328336)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
