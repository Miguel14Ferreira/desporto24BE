-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:1116855183 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo A. R. C. Paçô', 'Arcos de Valdevez', 'Viana do Castelo', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.8206755, -8.4058725, 'OPENSTREETMAP', 'OSM:WAY:1116855183',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1116855183')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arcos de Valdevez'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo A. R. C. Paçô'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8206755)) <= 0.00070
               AND ABS(v.longitude - (-8.4058725)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240183834 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Axis Golfe Ponte de Lima', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.7494952, -8.5695021, 'OPENSTREETMAP', 'OSM:WAY:240183834',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240183834')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Axis Golfe Ponte de Lima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7494952)) <= 0.00070
               AND ABS(v.longitude - (-8.5695021)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1062152236 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Freixo', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.6543195, -8.5883174, 'OPENSTREETMAP', 'OSM:WAY:1062152236',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1062152236')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Freixo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6543195)) <= 0.00070
               AND ABS(v.longitude - (-8.5883174)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3157107981 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ping-pong', 'Vila Nova de Cerveira', 'Viana do Castelo', NULL,
       'Ténis de mesa', 'Campo desportivo', NULL,
       41.9431015, -8.7419328, 'OPENSTREETMAP', 'OSM:NODE:3157107981',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3157107981')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Cerveira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ping-pong'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9431015)) <= 0.00070
               AND ABS(v.longitude - (-8.7419328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:400621405 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Vila Nova de Cerveira', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.9447202, -8.7442102, 'OPENSTREETMAP', 'OSM:WAY:400621405',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:400621405')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Cerveira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9447202)) <= 0.00070
               AND ABS(v.longitude - (-8.7442102)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10548492860 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BASE Personal Training Studio', 'Arcos de Valdevez', 'Viana do Castelo', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.8328650, -8.4160182, 'OPENSTREETMAP', 'OSM:NODE:10548492860',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10548492860')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arcos de Valdevez'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BASE Personal Training Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8328650)) <= 0.00070
               AND ABS(v.longitude - (-8.4160182)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:391240063 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Monte Aval', 'Arcos de Valdevez', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8059456, -8.4701287, 'OPENSTREETMAP', 'OSM:WAY:391240063',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:391240063')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arcos de Valdevez'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Monte Aval'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8059456)) <= 0.00070
               AND ABS(v.longitude - (-8.4701287)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134574759 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Rugby', 'Arcos de Valdevez', 'Viana do Castelo', NULL,
       'Rugby', 'Campo desportivo', NULL,
       41.8528997, -8.4156505, 'OPENSTREETMAP', 'OSM:WAY:134574759',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134574759')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arcos de Valdevez'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Rugby'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8528997)) <= 0.00070
               AND ABS(v.longitude - (-8.4156505)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1494153706 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Arcos de Valdevez', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.8501006, -8.4172261, 'OPENSTREETMAP', 'OSM:NODE:1494153706',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1494153706')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arcos de Valdevez'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8501006)) <= 0.00070
               AND ABS(v.longitude - (-8.4172261)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:822689747 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Arcos de Valdevez', 'Arcos de Valdevez', 'Viana do Castelo', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.8545236, -8.4165778, 'OPENSTREETMAP', 'OSM:WAY:822689747',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:822689747')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arcos de Valdevez'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Arcos de Valdevez'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8545236)) <= 0.00070
               AND ABS(v.longitude - (-8.4165778)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:818017977 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Vez', 'Arcos de Valdevez', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8516814, -8.4209688, 'OPENSTREETMAP', 'OSM:WAY:818017977',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:818017977')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arcos de Valdevez'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Vez'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8516814)) <= 0.00070
               AND ABS(v.longitude - (-8.4209688)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106276966 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol - Bogalhão', 'Caminha', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8368063, -8.7710969, 'OPENSTREETMAP', 'OSM:WAY:106276966',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106276966')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol - Bogalhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8368063)) <= 0.00070
               AND ABS(v.longitude - (-8.7710969)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:231686947 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Centro Cultural e Desportivo Ancorense', 'Caminha', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8025630, -8.8547589, 'OPENSTREETMAP', 'OSM:WAY:231686947',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:231686947')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Centro Cultural e Desportivo Ancorense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8025630)) <= 0.00070
               AND ABS(v.longitude - (-8.8547589)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:278584256 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Ilidio Couto', 'Caminha', 'Viana do Castelo', NULL,
       'Futebol', 'Estádio', NULL,
       41.9051241, -8.7876386, 'OPENSTREETMAP', 'OSM:WAY:278584256',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:278584256')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Ilidio Couto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9051241)) <= 0.00070
               AND ABS(v.longitude - (-8.7876386)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:256917393 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Largo do Casal', 'Caminha', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8865319, -8.7886684, 'OPENSTREETMAP', 'OSM:WAY:256917393',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:256917393')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Largo do Casal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8865319)) <= 0.00070
               AND ABS(v.longitude - (-8.7886684)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:542725856 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mesa Ping-Pong', 'Caminha', 'Viana do Castelo', NULL,
       'Ténis de mesa', 'Campo desportivo', NULL,
       41.8770230, -8.8357316, 'OPENSTREETMAP', 'OSM:WAY:542725856',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:542725856')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mesa Ping-Pong'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8770230)) <= 0.00070
               AND ABS(v.longitude - (-8.8357316)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:542734308 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mini campo de desportos', 'Caminha', 'Viana do Castelo', NULL,
       'Basquetebol;Andebol;Futebol', 'Campo desportivo', NULL,
       41.8108441, -8.8598026, 'OPENSTREETMAP', 'OSM:WAY:542734308',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:542734308')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mini campo de desportos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8108441)) <= 0.00070
               AND ABS(v.longitude - (-8.8598026)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154568265 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Vila Praia de Âncora', 'Caminha', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.8105578, -8.8553531, 'OPENSTREETMAP', 'OSM:WAY:154568265',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154568265')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Vila Praia de Âncora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8105578)) <= 0.00070
               AND ABS(v.longitude - (-8.8553531)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1598373223 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Club Caminhense', 'Caminha', 'Viana do Castelo', NULL,
       'Remo', 'Complexo desportivo', NULL,
       41.8784743, -8.8359147, 'OPENSTREETMAP', 'OSM:NODE:1598373223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1598373223')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caminha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Club Caminhense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8784743)) <= 0.00070
               AND ABS(v.longitude - (-8.8359147)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:365354528 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Melgaço', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       42.1123448, -8.2603333, 'OPENSTREETMAP', 'OSM:WAY:365354528',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:365354528')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Melgaço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (42.1123448)) <= 0.00070
               AND ABS(v.longitude - (-8.2603333)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:822689745 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Melgaço', 'Melgaço', 'Viana do Castelo', NULL,
       'Corrida', 'Pista desportiva', NULL,
       42.1192167, -8.2703156, 'OPENSTREETMAP', 'OSM:WAY:822689745',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:822689745')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Melgaço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Melgaço'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (42.1192167)) <= 0.00070
               AND ABS(v.longitude - (-8.2703156)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105531181 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Desportivo de Monção', 'Monção', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       42.0753011, -8.4674390, 'OPENSTREETMAP', 'OSM:WAY:105531181',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105531181')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Monção'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Desportivo de Monção'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (42.0753011)) <= 0.00070
               AND ABS(v.longitude - (-8.4674390)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169427296 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Johnny', 'Monção', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       42.0437474, -8.4392643, 'OPENSTREETMAP', 'OSM:WAY:1169427296',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169427296')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Monção'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Johnny'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (42.0437474)) <= 0.00070
               AND ABS(v.longitude - (-8.4392643)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:365620985 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Pública de Monção', 'Monção', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       42.0799048, -8.4729549, 'OPENSTREETMAP', 'OSM:WAY:365620985',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:365620985')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Monção'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Pública de Monção'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (42.0799048)) <= 0.00070
               AND ABS(v.longitude - (-8.4729549)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237686297 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Monçao', 'Monção', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       42.0786558, -8.4732878, 'OPENSTREETMAP', 'OSM:WAY:237686297',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237686297')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Monção'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Monçao'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (42.0786558)) <= 0.00070
               AND ABS(v.longitude - (-8.4732878)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:239919340 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Courense', 'Paredes de Coura', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.9154351, -8.5660097, 'OPENSTREETMAP', 'OSM:WAY:239919340',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:239919340')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes de Coura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Courense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9154351)) <= 0.00070
               AND ABS(v.longitude - (-8.5660097)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:331081063 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Paredes de Coura', 'Paredes de Coura', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       41.9135094, -8.5646049, 'OPENSTREETMAP', 'OSM:WAY:331081063',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:331081063')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes de Coura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Paredes de Coura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9135094)) <= 0.00070
               AND ABS(v.longitude - (-8.5646049)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:545132052 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Cristelo', 'Paredes de Coura', 'Viana do Castelo', NULL,
       'Futebol;Ténis;Voleibol;Andebol', 'Complexo desportivo', NULL,
       41.9030838, -8.5426249, 'OPENSTREETMAP', 'OSM:WAY:545132052',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:545132052')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes de Coura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Cristelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9030838)) <= 0.00070
               AND ABS(v.longitude - (-8.5426249)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:184447788 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol da Associação Desportiva de Ponte da Barca', 'Ponte da Barca', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8049312, -8.4187156, 'OPENSTREETMAP', 'OSM:WAY:184447788',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:184447788')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte da Barca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol da Associação Desportiva de Ponte da Barca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8049312)) <= 0.00070
               AND ABS(v.longitude - (-8.4187156)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:315079471 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas Municipais Doutor António Cabral de Oliveira', 'Ponte da Barca', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       41.8085253, -8.4160538, 'OPENSTREETMAP', 'OSM:WAY:315079471',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:315079471')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte da Barca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas Municipais Doutor António Cabral de Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8085253)) <= 0.00070
               AND ABS(v.longitude - (-8.4160538)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:713942406 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo da Praia Fluvial', 'Ponte da Barca', 'Viana do Castelo', NULL,
       'Futebol;Ténis;Basquetebol', 'Complexo desportivo', NULL,
       41.8090038, -8.4175516, 'OPENSTREETMAP', 'OSM:WAY:713942406',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:713942406')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte da Barca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo da Praia Fluvial'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8090038)) <= 0.00070
               AND ABS(v.longitude - (-8.4175516)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:666552514 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cabral de Oliveira', 'Ponte da Barca', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       41.8088622, -8.4161432, 'OPENSTREETMAP', 'OSM:WAY:666552514',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:666552514')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte da Barca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cabral de Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8088622)) <= 0.00070
               AND ABS(v.longitude - (-8.4161432)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:966222802 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Calheiros', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8175037, -8.5748314, 'OPENSTREETMAP', 'OSM:WAY:966222802',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:966222802')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Calheiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8175037)) <= 0.00070
               AND ABS(v.longitude - (-8.5748314)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:239982282 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vitorino das Donas', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7346292, -8.6411818, 'OPENSTREETMAP', 'OSM:WAY:239982282',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:239982282')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vitorino das Donas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7346292)) <= 0.00070
               AND ABS(v.longitude - (-8.6411818)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:464551545 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol Arcozelo', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7867528, -8.6039657, 'OPENSTREETMAP', 'OSM:WAY:464551545',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:464551545')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol Arcozelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7867528)) <= 0.00070
               AND ABS(v.longitude - (-8.6039657)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107424858 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Náutico de Ponte de Lima', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Canoagem', 'Complexo desportivo', NULL,
       41.7650882, -8.5915680, 'OPENSTREETMAP', 'OSM:WAY:107424858',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107424858')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Náutico de Ponte de Lima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7650882)) <= 0.00070
               AND ABS(v.longitude - (-8.5915680)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:217532787 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo de Vitorino dos Piães', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6832881, -8.6269610, 'OPENSTREETMAP', 'OSM:WAY:217532787',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:217532787')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo de Vitorino dos Piães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6832881)) <= 0.00070
               AND ABS(v.longitude - (-8.6269610)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107424859 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Natura', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.7661763, -8.5906013, 'OPENSTREETMAP', 'OSM:WAY:107424859',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107424859')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Natura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7661763)) <= 0.00070
               AND ABS(v.longitude - (-8.5906013)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:132578988 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       41.7620679, -8.5768912, 'OPENSTREETMAP', 'OSM:WAY:132578988',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:132578988')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7620679)) <= 0.00070
               AND ABS(v.longitude - (-8.5768912)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1615721101 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Brandara', 'Ponte de Lima', 'Viana do Castelo', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.7928701, -8.5786968, 'OPENSTREETMAP', 'OSM:NODE:1615721101',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1615721101')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ponte de Lima'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Brandara'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7928701)) <= 0.00070
               AND ABS(v.longitude - (-8.5786968)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:233486458 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Verdoejo', 'Valença', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       42.0475830, -8.5864209, 'OPENSTREETMAP', 'OSM:WAY:233486458',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:233486458')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valença'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Verdoejo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (42.0475830)) <= 0.00070
               AND ABS(v.longitude - (-8.5864209)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:529196494 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Amorosa Health Club', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.6446409, -8.8203503, 'OPENSTREETMAP', 'OSM:WAY:529196494',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:529196494')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Amorosa Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6446409)) <= 0.00070
               AND ABS(v.longitude - (-8.8203503)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:328865285 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva de Chafé', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Estádio', NULL,
       41.6456370, -8.7999594, 'OPENSTREETMAP', 'OSM:WAY:328865285',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:328865285')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva de Chafé'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6456370)) <= 0.00070
               AND ABS(v.longitude - (-8.7999594)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:328190117 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Beira-Mar', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6255977, -8.8156089, 'OPENSTREETMAP', 'OSM:WAY:328190117',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:328190117')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Beira-Mar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6255977)) <= 0.00070
               AND ABS(v.longitude - (-8.8156089)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:405762051 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos António Castanho', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6409058, -8.7755289, 'OPENSTREETMAP', 'OSM:WAY:405762051',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:405762051')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos António Castanho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6409058)) <= 0.00070
               AND ABS(v.longitude - (-8.7755289)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:208080580 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Senhora das Oliveiras', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6841414, -8.7927155, 'OPENSTREETMAP', 'OSM:WAY:208080580',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:208080580')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Senhora das Oliveiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6841414)) <= 0.00070
               AND ABS(v.longitude - (-8.7927155)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:328190116 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo de Chafé', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6454558, -8.8002987, 'OPENSTREETMAP', 'OSM:WAY:328190116',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:328190116')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo de Chafé'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6454558)) <= 0.00070
               AND ABS(v.longitude - (-8.8002987)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:264561169 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo dos Monções', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7154853, -8.7262600, 'OPENSTREETMAP', 'OSM:WAY:264561169',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:264561169')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo dos Monções'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7154853)) <= 0.00070
               AND ABS(v.longitude - (-8.7262600)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:310512161 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Surf de Viana do Castelo', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Surf', 'Complexo desportivo', NULL,
       41.6786802, -8.8274452, 'OPENSTREETMAP', 'OSM:WAY:310512161',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:310512161')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Surf de Viana do Castelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6786802)) <= 0.00070
               AND ABS(v.longitude - (-8.8274452)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7944702600 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Desportivo do IPVC', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.6935755, -8.8326007, 'OPENSTREETMAP', 'OSM:NODE:7944702600',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7944702600')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Desportivo do IPVC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6935755)) <= 0.00070
               AND ABS(v.longitude - (-8.8326007)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:509943364 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Vela Clube de Vela de Viana do Castelo', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Vela', 'Complexo desportivo', NULL,
       41.6847909, -8.8378886, 'OPENSTREETMAP', 'OSM:WAY:509943364',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:509943364')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Vela Clube de Vela de Viana do Castelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6847909)) <= 0.00070
               AND ABS(v.longitude - (-8.8378886)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:214678264 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Viana do Castelo', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       41.6957297, -8.8196321, 'OPENSTREETMAP', 'OSM:WAY:214678264',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:214678264')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Viana do Castelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6957297)) <= 0.00070
               AND ABS(v.longitude - (-8.8196321)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:208406940 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo desportivo de Barroselas', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6446965, -8.6856053, 'OPENSTREETMAP', 'OSM:WAY:208406940',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:208406940')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo desportivo de Barroselas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6446965)) <= 0.00070
               AND ABS(v.longitude - (-8.6856053)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:224882902 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Alferes Pinto Ribeiro', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6571382, -8.7195732, 'OPENSTREETMAP', 'OSM:WAY:224882902',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:224882902')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Alferes Pinto Ribeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6571382)) <= 0.00070
               AND ABS(v.longitude - (-8.7195732)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168247054 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Manuela Machado', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.7174703, -8.8175375, 'OPENSTREETMAP', 'OSM:WAY:168247054',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168247054')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Manuela Machado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7174703)) <= 0.00070
               AND ABS(v.longitude - (-8.8175375)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7944702599 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'FitnessUP', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.6930751, -8.8227514, 'OPENSTREETMAP', 'OSM:NODE:7944702599',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7944702599')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('FitnessUP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6930751)) <= 0.00070
               AND ABS(v.longitude - (-8.8227514)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:328865284 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Castelense', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Estádio', NULL,
       41.6255355, -8.8155303, 'OPENSTREETMAP', 'OSM:WAY:328865284',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:328865284')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Castelense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6255355)) <= 0.00070
               AND ABS(v.longitude - (-8.8155303)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2995439020 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo de Viana do Castelo', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Karting', 'Pista desportiva', NULL,
       41.6502510, -8.7995009, 'OPENSTREETMAP', 'OSM:NODE:2995439020',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2995439020')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo de Viana do Castelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6502510)) <= 0.00070
               AND ABS(v.longitude - (-8.7995009)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:533825931 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Neves Futebol Clube', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Futebol', 'Estádio', NULL,
       41.6572515, -8.7194294, 'OPENSTREETMAP', 'OSM:WAY:533825931',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:533825931')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Neves Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6572515)) <= 0.00070
               AND ABS(v.longitude - (-8.7194294)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:217169855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Atlântico', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       41.6929266, -8.8463640, 'OPENSTREETMAP', 'OSM:WAY:217169855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:217169855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Atlântico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6929266)) <= 0.00070
               AND ABS(v.longitude - (-8.8463640)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:528904122 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Centro Social Paroquial de Vila Nova de Anha', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.6648120, -8.7894512, 'OPENSTREETMAP', 'OSM:WAY:528904122',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:528904122')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Centro Social Paroquial de Vila Nova de Anha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6648120)) <= 0.00070
               AND ABS(v.longitude - (-8.7894512)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:242006897 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Frederico Pinheiro', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       41.7000980, -8.8222255, 'OPENSTREETMAP', 'OSM:WAY:242006897',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:242006897')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Frederico Pinheiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7000980)) <= 0.00070
               AND ABS(v.longitude - (-8.8222255)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:223156326 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas', 'Viana do Castelo', 'Viana do Castelo', NULL,
       'Natação', 'Piscina', NULL,
       41.6436131, -8.6852901, 'OPENSTREETMAP', 'OSM:WAY:223156326',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:223156326')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viana do Castelo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6436131)) <= 0.00070
               AND ABS(v.longitude - (-8.6852901)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:822689746 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Lançamentos - Vila Nova de Cerveira', 'Vila Nova de Cerveira', 'Viana do Castelo', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.9559046, -8.7445869, 'OPENSTREETMAP', 'OSM:WAY:822689746',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:822689746')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Cerveira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Lançamentos - Vila Nova de Cerveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9559046)) <= 0.00070
               AND ABS(v.longitude - (-8.7445869)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157413359 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de patinagem e skateboard', 'Vila Nova de Cerveira', 'Viana do Castelo', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.9392207, -8.7469360, 'OPENSTREETMAP', 'OSM:WAY:157413359',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157413359')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Cerveira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de patinagem e skateboard'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.9392207)) <= 0.00070
               AND ABS(v.longitude - (-8.7469360)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
