-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:1510246375 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Juventude Sport Clube', 'Évora', 'Évora', NULL,
       'Futebol', 'Estádio', NULL,
       38.5498279, -7.9033164, 'OPENSTREETMAP', 'OSM:WAY:1510246375',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1510246375')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Juventude Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5498279)) <= 0.00070
               AND ABS(v.longitude - (-7.9033164)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1374698251 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo e Recreativo de Canaviais', 'Évora', 'Évora', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.6092324, -7.9046937, 'OPENSTREETMAP', 'OSM:NODE:1374698251',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1374698251')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo e Recreativo de Canaviais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6092324)) <= 0.00070
               AND ABS(v.longitude - (-7.9046937)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1288804733 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mov4fit', 'Évora', 'Évora', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5466984, -7.9094696, 'OPENSTREETMAP', 'OSM:WAY:1288804733',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1288804733')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mov4fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5466984)) <= 0.00070
               AND ABS(v.longitude - (-7.9094696)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:308853320 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Aminata Évora Clube de Natação', 'Évora', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.5632478, -7.9152474, 'OPENSTREETMAP', 'OSM:WAY:308853320',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:308853320')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Aminata Évora Clube de Natação'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5632478)) <= 0.00070
               AND ABS(v.longitude - (-7.9152474)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6137227783 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga Sámkhya', 'Évora', 'Évora', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5709042, -7.9111815, 'OPENSTREETMAP', 'OSM:NODE:6137227783',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6137227783')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga Sámkhya'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5709042)) <= 0.00070
               AND ABS(v.longitude - (-7.9111815)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4279099894 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'AlfGym', 'Vendas Novas', 'Évora', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6766074, -8.4638511, 'OPENSTREETMAP', 'OSM:NODE:4279099894',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4279099894')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vendas Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('AlfGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6766074)) <= 0.00070
               AND ABS(v.longitude - (-8.4638511)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1509575932 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Padel e Ténis de Vendas Novas', 'Vendas Novas', 'Évora', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.6735962, -8.4610646, 'OPENSTREETMAP', 'OSM:WAY:1509575932',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1509575932')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vendas Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de Padel e Ténis de Vendas Novas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6735962)) <= 0.00070
               AND ABS(v.longitude - (-8.4610646)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1144645440 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético Municipal de Vendas Novas', 'Vendas Novas', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6723656, -8.4646371, 'OPENSTREETMAP', 'OSM:WAY:1144645440',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1144645440')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vendas Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético Municipal de Vendas Novas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6723656)) <= 0.00070
               AND ABS(v.longitude - (-8.4646371)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1509561577 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark de Vendas Novas', 'Vendas Novas', 'Évora', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.6734220, -8.4658794, 'OPENSTREETMAP', 'OSM:WAY:1509561577',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1509561577')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vendas Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark de Vendas Novas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6734220)) <= 0.00070
               AND ABS(v.longitude - (-8.4658794)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:349368198 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Pardais', 'Vila Viçosa', 'Évora', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       38.7223038, -7.4019247, 'OPENSTREETMAP', 'OSM:WAY:349368198',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:349368198')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Viçosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Pardais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7223038)) <= 0.00070
               AND ABS(v.longitude - (-7.4019247)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:606359093 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Biológica', 'Alandroal', 'Évora', NULL,
       'Natação', 'Piscina', NULL,
       38.6772473, -7.4028824, 'OPENSTREETMAP', 'OSM:WAY:606359093',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:606359093')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alandroal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Biológica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6772473)) <= 0.00070
               AND ABS(v.longitude - (-7.4028824)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:803460613 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipaís de Alandroal', 'Alandroal', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7053686, -7.4067042, 'OPENSTREETMAP', 'OSM:WAY:803460613',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:803460613')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alandroal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipaís de Alandroal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7053686)) <= 0.00070
               AND ABS(v.longitude - (-7.4067042)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:284543049 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Aliança do Povo - Heróis do 25 de Abril', 'Arraiolos', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7680676, -8.0360647, 'OPENSTREETMAP', 'OSM:WAY:284543049',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:284543049')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arraiolos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Aliança do Povo - Heróis do 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7680676)) <= 0.00070
               AND ABS(v.longitude - (-8.0360647)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:546359986 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Júlio Verne Soares Arimatea', 'Arraiolos', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8283642, -7.8437930, 'OPENSTREETMAP', 'OSM:WAY:546359986',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:546359986')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arraiolos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Júlio Verne Soares Arimatea'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8283642)) <= 0.00070
               AND ABS(v.longitude - (-7.8437930)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:570076878 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Doutor Cosme de Campos Callado', 'Arraiolos', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7373861, -8.0697543, 'OPENSTREETMAP', 'OSM:WAY:570076878',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:570076878')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arraiolos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Doutor Cosme de Campos Callado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7373861)) <= 0.00070
               AND ABS(v.longitude - (-8.0697543)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:161749937 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Montes Claros', 'Borba', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7717397, -7.4984184, 'OPENSTREETMAP', 'OSM:WAY:161749937',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:161749937')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Borba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Montes Claros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7717397)) <= 0.00070
               AND ABS(v.longitude - (-7.4984184)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:970661435 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Borba', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8070332, -7.4578104, 'OPENSTREETMAP', 'OSM:WAY:970661435',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:970661435')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Borba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8070332)) <= 0.00070
               AND ABS(v.longitude - (-7.4578104)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:612047941 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cobertas', 'Borba', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8019652, -7.4512304, 'OPENSTREETMAP', 'OSM:WAY:612047941',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:612047941')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Borba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8019652)) <= 0.00070
               AND ABS(v.longitude - (-7.4512304)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:660758996 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Artur Gomes Jorge', 'Estremoz', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8300831, -7.5077024, 'OPENSTREETMAP', 'OSM:WAY:660758996',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:660758996')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estremoz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Artur Gomes Jorge'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8300831)) <= 0.00070
               AND ABS(v.longitude - (-7.5077024)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1134529815 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de Estremoz', 'Estremoz', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8442206, -7.5680861, 'OPENSTREETMAP', 'OSM:WAY:1134529815',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1134529815')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estremoz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal de Estremoz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8442206)) <= 0.00070
               AND ABS(v.longitude - (-7.5680861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1134529816 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Nº2 de Estremoz', 'Estremoz', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8447614, -7.5683476, 'OPENSTREETMAP', 'OSM:WAY:1134529816',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1134529816')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estremoz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Nº2 de Estremoz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8447614)) <= 0.00070
               AND ABS(v.longitude - (-7.5683476)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150089747 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Esplanada Parque', 'Estremoz', 'Évora', NULL,
       'Hóquei em Campo', 'Complexo desportivo', NULL,
       38.8393966, -7.5854428, 'OPENSTREETMAP', 'OSM:WAY:150089747',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150089747')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estremoz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Esplanada Parque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8393966)) <= 0.00070
               AND ABS(v.longitude - (-7.5854428)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1183429561 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Estremoz', 'Estremoz', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8462221, -7.5917021, 'OPENSTREETMAP', 'OSM:WAY:1183429561',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1183429561')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estremoz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Estremoz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8462221)) <= 0.00070
               AND ABS(v.longitude - (-7.5917021)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:539495924 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia - Lusitano Ginásio Clube', 'Évora', 'Évora', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.5490268, -7.9444502, 'OPENSTREETMAP', 'OSM:WAY:539495924',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:539495924')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia - Lusitano Ginásio Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5490268)) <= 0.00070
               AND ABS(v.longitude - (-7.9444502)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12217445437 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be-Fit', 'Évora', 'Évora', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5668886, -7.9419454, 'OPENSTREETMAP', 'OSM:NODE:12217445437',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12217445437')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be-Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5668886)) <= 0.00070
               AND ABS(v.longitude - (-7.9419454)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9765313625 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BeInShape', 'Évora', 'Évora', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5581634, -7.9115140, 'OPENSTREETMAP', 'OSM:NODE:9765313625',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9765313625')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BeInShape'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5581634)) <= 0.00070
               AND ABS(v.longitude - (-7.9115140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:540281713 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 25 de Junho', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6567963, -7.9872733, 'OPENSTREETMAP', 'OSM:WAY:540281713',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:540281713')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 25 de Junho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6567963)) <= 0.00070
               AND ABS(v.longitude - (-7.9872733)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:378741120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol José Eduardo Abreu', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6093547, -7.9047401, 'OPENSTREETMAP', 'OSM:WAY:378741120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:378741120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol José Eduardo Abreu'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6093547)) <= 0.00070
               AND ABS(v.longitude - (-7.9047401)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:909576478 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Maria do Anjo Barahona', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7048192, -7.7885429, 'OPENSTREETMAP', 'OSM:WAY:909576478',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:909576478')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Maria do Anjo Barahona'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7048192)) <= 0.00070
               AND ABS(v.longitude - (-7.7885429)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147056442 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de treinos - Juventude Sport Clube', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5617842, -7.9151172, 'OPENSTREETMAP', 'OSM:WAY:147056442',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147056442')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de treinos - Juventude Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5617842)) <= 0.00070
               AND ABS(v.longitude - (-7.9151172)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147056412 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos - Lusitano Ginásio Clube', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5616343, -7.9169412, 'OPENSTREETMAP', 'OSM:WAY:147056412',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147056412')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos - Lusitano Ginásio Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5616343)) <= 0.00070
               AND ABS(v.longitude - (-7.9169412)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:963299880 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Engenheiro Vasco Maria Eugénio Almeida', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.4619964, -7.7541191, 'OPENSTREETMAP', 'OSM:WAY:963299880',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:963299880')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Engenheiro Vasco Maria Eugénio Almeida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4619964)) <= 0.00070
               AND ABS(v.longitude - (-7.7541191)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1265922627 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Estrela', 'Évora', 'Évora', NULL,
       'Futebol;Futsal', 'Estádio', NULL,
       38.5622320, -7.9178504, 'OPENSTREETMAP', 'OSM:WAY:1265922627',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1265922627')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Estrela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5622320)) <= 0.00070
               AND ABS(v.longitude - (-7.9178504)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:499208841 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo N.º 1', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5479372, -7.9454835, 'OPENSTREETMAP', 'OSM:WAY:499208841',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:499208841')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo N.º 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5479372)) <= 0.00070
               AND ABS(v.longitude - (-7.9454835)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1332578188 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo N.º 2', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5479352, -7.9445776, 'OPENSTREETMAP', 'OSM:WAY:1332578188',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1332578188')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo N.º 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5479352)) <= 0.00070
               AND ABS(v.longitude - (-7.9445776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1332578187 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo N.º 3', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5491152, -7.9445628, 'OPENSTREETMAP', 'OSM:WAY:1332578187',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1332578187')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo N.º 3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5491152)) <= 0.00070
               AND ABS(v.longitude - (-7.9445628)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4795033982 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Everybody', 'Évora', 'Évora', NULL,
       'Ginásio;Natação', 'Ginásio', NULL,
       38.5494674, -7.9096749, 'OPENSTREETMAP', 'OSM:NODE:4795033982',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4795033982')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Everybody'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5494674)) <= 0.00070
               AND ABS(v.longitude - (-7.9096749)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:569135956 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Évora Sports Center - ESC PADEL', 'Évora', 'Évora', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.5610925, -7.9060014, 'OPENSTREETMAP', 'OSM:WAY:569135956',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:569135956')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Évora Sports Center - ESC PADEL'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5610925)) <= 0.00070
               AND ABS(v.longitude - (-7.9060014)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:499209244 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo e Cultural da Cruz da Picada', 'Évora', 'Évora', NULL,
       'Atletismo;Ténis de mesa', 'Campo desportivo', NULL,
       38.5658248, -7.9258067, 'OPENSTREETMAP', 'OSM:WAY:499209244',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:499209244')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo e Cultural da Cruz da Picada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5658248)) <= 0.00070
               AND ABS(v.longitude - (-7.9258067)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9765313626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'IP7 Évora Padel', 'Évora', 'Évora', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.5579096, -7.9116144, 'OPENSTREETMAP', 'OSM:NODE:9765313626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9765313626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('IP7 Évora Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5579096)) <= 0.00070
               AND ABS(v.longitude - (-7.9116144)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1255804308 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Chartres', 'Évora', 'Évora', NULL,
       'Ginásio', 'Campo desportivo', NULL,
       38.5745953, -7.9163776, 'OPENSTREETMAP', 'OSM:WAY:1255804308',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1255804308')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Chartres'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5745953)) <= 0.00070
               AND ABS(v.longitude - (-7.9163776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:650798746 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Recinto Desportivo Engenheiro Jaime António Morais Figo', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5331720, -8.0247292, 'OPENSTREETMAP', 'OSM:WAY:650798746',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:650798746')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Recinto Desportivo Engenheiro Jaime António Morais Figo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5331720)) <= 0.00070
               AND ABS(v.longitude - (-8.0247292)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309577320 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Lisboa e Évora', 'Évora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5686467, -7.8909442, 'OPENSTREETMAP', 'OSM:WAY:309577320',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309577320')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Évora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Lisboa e Évora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5686467)) <= 0.00070
               AND ABS(v.longitude - (-7.8909442)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:264287572 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Casa do Povo de Cabrela', 'Montemor-o-Novo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5999373, -8.4651010, 'OPENSTREETMAP', 'OSM:WAY:264287572',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:264287572')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Novo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Casa do Povo de Cabrela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5999373)) <= 0.00070
               AND ABS(v.longitude - (-8.4651010)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:235483284 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Municipal de Montemor-o-Novo', 'Montemor-o-Novo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6500912, -8.2099235, 'OPENSTREETMAP', 'OSM:WAY:235483284',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:235483284')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Novo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Municipal de Montemor-o-Novo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6500912)) <= 0.00070
               AND ABS(v.longitude - (-8.2099235)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:570076932 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Dom Manuel I', 'Montemor-o-Novo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7958963, -8.2304384, 'OPENSTREETMAP', 'OSM:WAY:570076932',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:570076932')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Novo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Dom Manuel I'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7958963)) <= 0.00070
               AND ABS(v.longitude - (-8.2304384)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99410530 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo União Sport de Montemor-o-Novo', 'Montemor-o-Novo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6473963, -8.2105511, 'OPENSTREETMAP', 'OSM:WAY:99410530',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99410530')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Novo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo União Sport de Montemor-o-Novo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6473963)) <= 0.00070
               AND ABS(v.longitude - (-8.2105511)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:252671891 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo 25 de Abril', 'Montemor-o-Novo', 'Évora', NULL,
       'Futebol;Ténis', 'Complexo desportivo', NULL,
       38.5382321, -8.1706206, 'OPENSTREETMAP', 'OSM:WAY:252671891',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:252671891')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Novo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5382321)) <= 0.00070
               AND ABS(v.longitude - (-8.1706206)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:728907604 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Municipal de Montemor-o-Novo', 'Montemor-o-Novo', 'Évora', NULL,
       'Atletismo;Futebol', 'Complexo desportivo', NULL,
       38.6443028, -8.1989226, 'OPENSTREETMAP', 'OSM:WAY:728907604',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:728907604')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Novo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Municipal de Montemor-o-Novo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6443028)) <= 0.00070
               AND ABS(v.longitude - (-8.1989226)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:956323112 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos António Joaquim', 'Mora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8743842, -8.1573009, 'OPENSTREETMAP', 'OSM:WAY:956323112',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:956323112')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos António Joaquim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8743842)) <= 0.00070
               AND ABS(v.longitude - (-8.1573009)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:110285511 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Pavia', 'Mora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8941157, -8.0223928, 'OPENSTREETMAP', 'OSM:WAY:110285511',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:110285511')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Pavia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8941157)) <= 0.00070
               AND ABS(v.longitude - (-8.0223928)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:332066105 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de Mora', 'Mora', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9506450, -8.1525261, 'OPENSTREETMAP', 'OSM:WAY:332066105',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:332066105')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal de Mora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9506450)) <= 0.00070
               AND ABS(v.longitude - (-8.1525261)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:762103300 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo', 'Mora', 'Évora', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.9556504, -8.0730038, 'OPENSTREETMAP', 'OSM:WAY:762103300',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:762103300')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9556504)) <= 0.00070
               AND ABS(v.longitude - (-8.0730038)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:344518113 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Polidesportivo da Luz', 'Mourão', 'Évora', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.3436275, -7.3741693, 'OPENSTREETMAP', 'OSM:WAY:344518113',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:344518113')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mourão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Polidesportivo da Luz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3436275)) <= 0.00070
               AND ABS(v.longitude - (-7.3741693)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:339570416 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Descoberta', 'Mourão', 'Évora', NULL,
       'Natação', 'Piscina', NULL,
       38.3856807, -7.3358172, 'OPENSTREETMAP', 'OSM:WAY:339570416',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:339570416')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mourão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Descoberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3856807)) <= 0.00070
               AND ABS(v.longitude - (-7.3358172)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:247073414 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Vera Cruz', 'Portel', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.2302604, -7.6820689, 'OPENSTREETMAP', 'OSM:WAY:247073414',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:247073414')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Vera Cruz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2302604)) <= 0.00070
               AND ABS(v.longitude - (-7.6820689)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154761148 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Dom Manuel II', 'Portel', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.3929058, -7.7102729, 'OPENSTREETMAP', 'OSM:WAY:154761148',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154761148')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Dom Manuel II'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3929058)) <= 0.00070
               AND ABS(v.longitude - (-7.7102729)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183842165 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Dom Nuno Álvares Pereira', 'Portel', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.3029916, -7.6972462, 'OPENSTREETMAP', 'OSM:WAY:183842165',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183842165')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Dom Nuno Álvares Pereira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3029916)) <= 0.00070
               AND ABS(v.longitude - (-7.6972462)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:488050240 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de São Bartolomeu do Outeiro', 'Portel', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.3580080, -7.9107691, 'OPENSTREETMAP', 'OSM:WAY:488050240',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:488050240')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de São Bartolomeu do Outeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3580080)) <= 0.00070
               AND ABS(v.longitude - (-7.9107691)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:488060527 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Manuel Mendes Vieira', 'Redondo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5120086, -7.5967748, 'OPENSTREETMAP', 'OSM:WAY:488060527',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:488060527')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Redondo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Manuel Mendes Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5120086)) <= 0.00070
               AND ABS(v.longitude - (-7.5967748)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:951526334 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Redondo', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.6446837, -7.5469373, 'OPENSTREETMAP', 'OSM:WAY:951526334',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:951526334')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Redondo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6446837)) <= 0.00070
               AND ABS(v.longitude - (-7.5469373)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:176285509 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Redondense FC', 'Redondo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6470541, -7.5504322, 'OPENSTREETMAP', 'OSM:WAY:176285509',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:176285509')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Redondo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Redondense FC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6470541)) <= 0.00070
               AND ABS(v.longitude - (-7.5504322)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1518761862 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'WeFit', 'Redondo', 'Évora', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6514739, -7.5487322, 'OPENSTREETMAP', 'OSM:WAY:1518761862',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1518761862')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Redondo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('WeFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6514739)) <= 0.00070
               AND ABS(v.longitude - (-7.5487322)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:670691182 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Dâmaso Ramalho', 'Reguengos de Monsaraz', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.4811331, -7.4722265, 'OPENSTREETMAP', 'OSM:WAY:670691182',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:670691182')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Reguengos de Monsaraz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Dâmaso Ramalho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4811331)) <= 0.00070
               AND ABS(v.longitude - (-7.4722265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:113676897 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Virgílio Durão', 'Reguengos de Monsaraz', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.4316582, -7.5347634, 'OPENSTREETMAP', 'OSM:WAY:113676897',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:113676897')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Reguengos de Monsaraz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Virgílio Durão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4316582)) <= 0.00070
               AND ABS(v.longitude - (-7.5347634)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:677940676 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Corval', 'Reguengos de Monsaraz', 'Évora', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.4440528, -7.4747031, 'OPENSTREETMAP', 'OSM:WAY:677940676',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:677940676')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Reguengos de Monsaraz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Corval'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4440528)) <= 0.00070
               AND ABS(v.longitude - (-7.4747031)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1036984401 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo Arquiteto Rosado Correia', 'Reguengos de Monsaraz', 'Évora', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       38.4302606, -7.5351582, 'OPENSTREETMAP', 'OSM:WAY:1036984401',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1036984401')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Reguengos de Monsaraz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo Arquiteto Rosado Correia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4302606)) <= 0.00070
               AND ABS(v.longitude - (-7.5351582)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:248486151 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Piso Modular Sportcourt', 'Vendas Novas', 'Évora', NULL,
       'Futebol;Basquetebol;Ténis', 'Campo desportivo', NULL,
       38.6736326, -8.4607825, 'OPENSTREETMAP', 'OSM:WAY:248486151',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:248486151')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vendas Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Piso Modular Sportcourt'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6736326)) <= 0.00070
               AND ABS(v.longitude - (-8.4607825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309577280 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo João Branco Núncio', 'Viana do Alentejo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.3932728, -8.1559912, 'OPENSTREETMAP', 'OSM:WAY:309577280',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309577280')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo João Branco Núncio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3932728)) <= 0.00070
               AND ABS(v.longitude - (-8.1559912)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183831158 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo João Faria e Melo', 'Viana do Alentejo', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.3333508, -7.9959132, 'OPENSTREETMAP', 'OSM:WAY:183831158',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183831158')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo João Faria e Melo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3333508)) <= 0.00070
               AND ABS(v.longitude - (-7.9959132)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309577276 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Alcaçovas', 'Viana do Alentejo', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.3906665, -8.1490903, 'OPENSTREETMAP', 'OSM:WAY:309577276',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309577276')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Alcaçovas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3906665)) <= 0.00070
               AND ABS(v.longitude - (-8.1490903)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182613601 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol Eng. Gonçalves', 'Vila Viçosa', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7781030, -7.3153556, 'OPENSTREETMAP', 'OSM:WAY:182613601',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182613601')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Viçosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol Eng. Gonçalves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7781030)) <= 0.00070
               AND ABS(v.longitude - (-7.3153556)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:88277857 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos João Figueiredo', 'Vila Viçosa', 'Évora', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7788044, -7.4219909, 'OPENSTREETMAP', 'OSM:WAY:88277857',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:88277857')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Viçosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos João Figueiredo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7788044)) <= 0.00070
               AND ABS(v.longitude - (-7.4219909)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802045779 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Vila Viçosa', 'Vila Viçosa', 'Évora', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7773295, -7.4225455, 'OPENSTREETMAP', 'OSM:WAY:802045779',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802045779')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Viçosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Vila Viçosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7773295)) <= 0.00070
               AND ABS(v.longitude - (-7.4225455)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
