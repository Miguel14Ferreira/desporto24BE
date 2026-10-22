-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:823002027 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Alfândega da Fé', 'Alfândega da Fé', 'Bragança', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.3357540, -6.9594585, 'OPENSTREETMAP', 'OSM:WAY:823002027',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823002027')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alfândega da Fé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Alfândega da Fé'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3357540)) <= 0.00070
               AND ABS(v.longitude - (-6.9594585)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155752748 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Cima', 'Bragança', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7005413, -6.7223458, 'OPENSTREETMAP', 'OSM:WAY:1155752748',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155752748')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Bragança'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Cima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7005413)) <= 0.00070
               AND ABS(v.longitude - (-6.7223458)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:643164728 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Rebordainhos', 'Bragança', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6671346, -6.8626429, 'OPENSTREETMAP', 'OSM:WAY:643164728',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:643164728')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Bragança'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Rebordainhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6671346)) <= 0.00070
               AND ABS(v.longitude - (-6.8626429)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104044165 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Bragança', 'Bragança', 'Bragança', NULL,
       'Futebol', 'Estádio', NULL,
       41.8034202, -6.7703439, 'OPENSTREETMAP', 'OSM:WAY:104044165',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104044165')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Bragança'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Bragança'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8034202)) <= 0.00070
               AND ABS(v.longitude - (-6.7703439)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825473684 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Clube Académico', 'Bragança', 'Bragança', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.8055930, -6.7717401, 'OPENSTREETMAP', 'OSM:WAY:825473684',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825473684')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Bragança'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Clube Académico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8055930)) <= 0.00070
               AND ABS(v.longitude - (-6.7717401)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134121277 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Bragança', 'Bragança', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.8046409, -6.7702977, 'OPENSTREETMAP', 'OSM:WAY:134121277',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134121277')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Bragança'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8046409)) <= 0.00070
               AND ABS(v.longitude - (-6.7702977)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1128002439 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SkateParque da Braguinha', 'Bragança', 'Bragança', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.8143032, -6.7605063, 'OPENSTREETMAP', 'OSM:WAY:1128002439',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1128002439')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Bragança'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SkateParque da Braguinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8143032)) <= 0.00070
               AND ABS(v.longitude - (-6.7605063)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1132615373 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Carrazeda de Ansiães', 'Carrazeda de Ansiães', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2383579, -7.3060945, 'OPENSTREETMAP', 'OSM:WAY:1132615373',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1132615373')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carrazeda de Ansiães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Carrazeda de Ansiães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2383579)) <= 0.00070
               AND ABS(v.longitude - (-7.3060945)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150947476 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Carrazeda de Ansiães', 'Carrazeda de Ansiães', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.2453319, -7.3055413, 'OPENSTREETMAP', 'OSM:WAY:150947476',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150947476')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carrazeda de Ansiães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Carrazeda de Ansiães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2453319)) <= 0.00070
               AND ABS(v.longitude - (-7.3055413)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1366966527 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas municipais descobertas', 'Carrazeda de Ansiães', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.2304854, -7.2806553, 'OPENSTREETMAP', 'OSM:WAY:1366966527',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1366966527')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carrazeda de Ansiães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas municipais descobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2304854)) <= 0.00070
               AND ABS(v.longitude - (-7.2806553)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:187939465 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Zedes', 'Carrazeda de Ansiães', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2799948, -7.3028151, 'OPENSTREETMAP', 'OSM:WAY:187939465',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:187939465')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carrazeda de Ansiães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Zedes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2799948)) <= 0.00070
               AND ABS(v.longitude - (-7.3028151)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3118419272 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Freixo de Espada à Cinta', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.0946699, -6.8130631, 'OPENSTREETMAP', 'OSM:NODE:3118419272',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3118419272')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Freixo de Espada à Cinta'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0946699)) <= 0.00070
               AND ABS(v.longitude - (-6.8130631)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106554899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol', 'Macedo de Cavaleiros', 'Bragança', NULL,
       'Futebol', 'Pista desportiva', NULL,
       41.5283612, -6.9649269, 'OPENSTREETMAP', 'OSM:WAY:106554899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106554899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Macedo de Cavaleiros'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5283612)) <= 0.00070
               AND ABS(v.longitude - (-6.9649269)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:712521252 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Atlético de Macedo de Cavaleiros', 'Macedo de Cavaleiros', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5283058, -6.9650247, 'OPENSTREETMAP', 'OSM:WAY:712521252',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:712521252')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Macedo de Cavaleiros'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Atlético de Macedo de Cavaleiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5283058)) <= 0.00070
               AND ABS(v.longitude - (-6.9650247)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:187060845 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Calvário', 'Macedo de Cavaleiros', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4885056, -6.6971452, 'OPENSTREETMAP', 'OSM:WAY:187060845',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:187060845')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Macedo de Cavaleiros'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Calvário'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4885056)) <= 0.00070
               AND ABS(v.longitude - (-6.6971452)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:257176620 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Gatunha', 'Miranda do Douro', 'Bragança', NULL,
       'Futebol', 'Estádio', NULL,
       41.4706065, -6.3145761, 'OPENSTREETMAP', 'OSM:WAY:257176620',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:257176620')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Miranda do Douro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Gatunha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4706065)) <= 0.00070
               AND ABS(v.longitude - (-6.3145761)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:187060846 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Santa Luzia', 'Miranda do Douro', 'Bragança', NULL,
       'Futebol', 'Estádio', NULL,
       41.4945363, -6.2823903, 'OPENSTREETMAP', 'OSM:WAY:187060846',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:187060846')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Miranda do Douro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Santa Luzia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4945363)) <= 0.00070
               AND ABS(v.longitude - (-6.2823903)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169731437 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Sendim', 'Miranda do Douro', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.3899937, -6.4334798, 'OPENSTREETMAP', 'OSM:WAY:1169731437',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169731437')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Miranda do Douro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Sendim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3899937)) <= 0.00070
               AND ABS(v.longitude - (-6.4334798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1350773776 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Miranda do Douro', 'Miranda do Douro', 'Bragança', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.4945387, -6.2845984, 'OPENSTREETMAP', 'OSM:WAY:1350773776',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1350773776')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Miranda do Douro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Miranda do Douro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4945387)) <= 0.00070
               AND ABS(v.longitude - (-6.2845984)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:465306748 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol em terra', 'Mirandela', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3990295, -7.1396018, 'OPENSTREETMAP', 'OSM:WAY:465306748',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:465306748')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mirandela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol em terra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3990295)) <= 0.00070
               AND ABS(v.longitude - (-7.1396018)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:126637628 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio de São Sebastião', 'Mirandela', 'Bragança', NULL,
       'Futebol', 'Estádio', NULL,
       41.4901169, -7.1767393, 'OPENSTREETMAP', 'OSM:WAY:126637628',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:126637628')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mirandela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio de São Sebastião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4901169)) <= 0.00070
               AND ABS(v.longitude - (-7.1767393)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:875645526 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo Regional de Mirandela', 'Mirandela', 'Bragança', NULL,
       'Karting', 'Complexo desportivo', NULL,
       41.4717283, -7.2259425, 'OPENSTREETMAP', 'OSM:WAY:875645526',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:875645526')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mirandela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo Regional de Mirandela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4717283)) <= 0.00070
               AND ABS(v.longitude - (-7.2259425)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1091728576 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Maravilha', 'Mirandela', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.5062605, -7.1975073, 'OPENSTREETMAP', 'OSM:WAY:1091728576',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1091728576')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mirandela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Maravilha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5062605)) <= 0.00070
               AND ABS(v.longitude - (-7.1975073)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:353291303 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Mirandela', 'Mirandela', 'Bragança', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.4773257, -7.1792972, 'OPENSTREETMAP', 'OSM:WAY:353291303',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:353291303')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mirandela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Mirandela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4773257)) <= 0.00070
               AND ABS(v.longitude - (-7.1792972)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:184576660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Clube de Mirandela', 'Mirandela', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4900449, -7.1766609, 'OPENSTREETMAP', 'OSM:WAY:184576660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:184576660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mirandela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Clube de Mirandela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4900449)) <= 0.00070
               AND ABS(v.longitude - (-7.1766609)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:439935938 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis', 'Mogadouro', 'Bragança', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.3344850, -6.7176551, 'OPENSTREETMAP', 'OSM:WAY:439935938',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:439935938')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mogadouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3344850)) <= 0.00070
               AND ABS(v.longitude - (-6.7176551)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4539821995 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de tiro', 'Mogadouro', 'Bragança', NULL,
       'Tiro', 'Campo desportivo', NULL,
       41.3261916, -6.7184454, 'OPENSTREETMAP', 'OSM:NODE:4539821995',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4539821995')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mogadouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de tiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3261916)) <= 0.00070
               AND ABS(v.longitude - (-6.7184454)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:117593432 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Mogadouro', 'Mogadouro', 'Bragança', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.3330375, -6.7174318, 'OPENSTREETMAP', 'OSM:WAY:117593432',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:117593432')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mogadouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Mogadouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3330375)) <= 0.00070
               AND ABS(v.longitude - (-6.7174318)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169730379 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta', 'Mogadouro', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.3356668, -6.7184103, 'OPENSTREETMAP', 'OSM:WAY:1169730379',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169730379')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mogadouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3356668)) <= 0.00070
               AND ABS(v.longitude - (-6.7184103)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152505322 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Mogadouro', 'Mogadouro', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.3342232, -6.7182273, 'OPENSTREETMAP', 'OSM:WAY:1152505322',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152505322')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mogadouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Mogadouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3342232)) <= 0.00070
               AND ABS(v.longitude - (-6.7182273)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2229043731 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BodyPlay', 'Torre de Moncorvo', 'Bragança', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       41.1762314, -7.0529008, 'OPENSTREETMAP', 'OSM:NODE:2229043731',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2229043731')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torre de Moncorvo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BodyPlay'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1762314)) <= 0.00070
               AND ABS(v.longitude - (-7.0529008)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:317309772 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo António Sobrinho', 'Torre de Moncorvo', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1810605, -7.0456978, 'OPENSTREETMAP', 'OSM:WAY:317309772',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:317309772')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torre de Moncorvo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo António Sobrinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1810605)) <= 0.00070
               AND ABS(v.longitude - (-7.0456978)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108275415 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Engenheiro José Aires', 'Torre de Moncorvo', 'Bragança', NULL,
       'Futebol', 'Estádio', NULL,
       41.1813467, -7.0436770, 'OPENSTREETMAP', 'OSM:WAY:108275415',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108275415')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torre de Moncorvo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Engenheiro José Aires'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1813467)) <= 0.00070
               AND ABS(v.longitude - (-7.0436770)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108403361 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cobertas de Torre de Moncorvo', 'Torre de Moncorvo', 'Bragança', NULL,
       'Natação', 'Piscina', NULL,
       41.1751324, -7.0578067, 'OPENSTREETMAP', 'OSM:WAY:108403361',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108403361')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torre de Moncorvo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cobertas de Torre de Moncorvo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1751324)) <= 0.00070
               AND ABS(v.longitude - (-7.0578067)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104150884 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vila Flor', 'Vila Flor', 'Bragança', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2949012, -7.1714820, 'OPENSTREETMAP', 'OSM:WAY:104150884',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104150884')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Flor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vila Flor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2949012)) <= 0.00070
               AND ABS(v.longitude - (-7.1714820)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:98666581 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Vila Flor', 'Vila Flor', 'Bragança', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3087719, -7.1502027, 'OPENSTREETMAP', 'OSM:WAY:98666581',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:98666581')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Flor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Vila Flor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3087719)) <= 0.00070
               AND ABS(v.longitude - (-7.1502027)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:823002028 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Vila Flor', 'Vila Flor', 'Bragança', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.2959429, -7.1677776, 'OPENSTREETMAP', 'OSM:WAY:823002028',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823002028')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Flor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Vila Flor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2959429)) <= 0.00070
               AND ABS(v.longitude - (-7.1677776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1495008208 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Santalha', 'Vinhais', 'Bragança', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.9015342, -7.1098412, 'OPENSTREETMAP', 'OSM:WAY:1495008208',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1495008208')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vinhais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Santalha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9015342)) <= 0.00070
               AND ABS(v.longitude - (-7.1098412)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
