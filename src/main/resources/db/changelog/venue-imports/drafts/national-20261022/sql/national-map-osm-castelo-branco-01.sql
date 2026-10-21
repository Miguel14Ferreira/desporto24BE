-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:12633562166 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Multidesportos do Louseiro', 'Covilhã', 'Castelo Branco', NULL,
       'Futebol;Basquetebol;Andebol;Corrida', 'Complexo desportivo', NULL,
       40.2418988, -7.5856118, 'OPENSTREETMAP', 'OSM:NODE:12633562166',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12633562166')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Multidesportos do Louseiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2418988)) <= 0.00070
               AND ABS(v.longitude - (-7.5856118)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11993154205 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'City Gym', 'Covilhã', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2721178, -7.4998496, 'OPENSTREETMAP', 'OSM:NODE:11993154205',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11993154205')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('City Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2721178)) <= 0.00070
               AND ABS(v.longitude - (-7.4998496)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:432098032 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CROSSFUNDÃO', 'Fundão', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1783409, -7.4901148, 'OPENSTREETMAP', 'OSM:WAY:432098032',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:432098032')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CROSSFUNDÃO'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1783409)) <= 0.00070
               AND ABS(v.longitude - (-7.4901148)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:434339585 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais do Fundão', 'Fundão', 'Castelo Branco', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.1430689, -7.5015409, 'OPENSTREETMAP', 'OSM:WAY:434339585',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:434339585')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais do Fundão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1430689)) <= 0.00070
               AND ABS(v.longitude - (-7.5015409)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:577818299 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Radical da Pedreira', 'Belmonte', 'Castelo Branco', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.3646193, -7.3427347, 'OPENSTREETMAP', 'OSM:WAY:577818299',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:577818299')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Belmonte'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Radical da Pedreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3646193)) <= 0.00070
               AND ABS(v.longitude - (-7.3427347)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:487504957 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Belmonte', 'Castelo Branco', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.3612112, -7.3509665, 'OPENSTREETMAP', 'OSM:WAY:487504957',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:487504957')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Belmonte'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3612112)) <= 0.00070
               AND ABS(v.longitude - (-7.3509665)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12330700827 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia', 'Castelo Branco', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.8217894, -7.5014578, 'OPENSTREETMAP', 'OSM:NODE:12330700827',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12330700827')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8217894)) <= 0.00070
               AND ABS(v.longitude - (-7.5014578)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155362585 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Albisport Clube', 'Castelo Branco', 'Castelo Branco', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       39.8231455, -7.5033259, 'OPENSTREETMAP', 'OSM:WAY:155362585',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155362585')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Albisport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8231455)) <= 0.00070
               AND ABS(v.longitude - (-7.5033259)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5341145659 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'A Praça', 'Castelo Branco', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.8217551, -7.4935009, 'OPENSTREETMAP', 'OSM:NODE:5341145659',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5341145659')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('A Praça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8217551)) <= 0.00070
               AND ABS(v.longitude - (-7.4935009)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9112242371 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Box6000 - CrossFit Castelo Branco', 'Castelo Branco', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.8080673, -7.5101205, 'OPENSTREETMAP', 'OSM:NODE:9112242371',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9112242371')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Box6000 - CrossFit Castelo Branco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8080673)) <= 0.00070
               AND ABS(v.longitude - (-7.5101205)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:555929027 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CB Padel Center', 'Castelo Branco', 'Castelo Branco', NULL,
       'Padel', 'Complexo desportivo', NULL,
       39.8127894, -7.5076986, 'OPENSTREETMAP', 'OSM:WAY:555929027',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:555929027')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CB Padel Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8127894)) <= 0.00070
               AND ABS(v.longitude - (-7.5076986)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155331867 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas de Castelo Branco', 'Castelo Branco', 'Castelo Branco', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.8160340, -7.5127744, 'OPENSTREETMAP', 'OSM:WAY:155331867',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155331867')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas de Castelo Branco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8160340)) <= 0.00070
               AND ABS(v.longitude - (-7.5127744)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1295049107 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Vale do Romeiro', 'Castelo Branco', 'Castelo Branco', NULL,
       'Futebol', 'Estádio', NULL,
       39.8354960, -7.4954111, 'OPENSTREETMAP', 'OSM:WAY:1295049107',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1295049107')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Vale do Romeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8354960)) <= 0.00070
               AND ABS(v.longitude - (-7.4954111)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11081967638 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Castelo Branco', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.8158937, -7.5210115, 'OPENSTREETMAP', 'OSM:NODE:11081967638',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11081967638')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8158937)) <= 0.00070
               AND ABS(v.longitude - (-7.5210115)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1266563810 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Padel Castelo', 'Castelo Branco', 'Castelo Branco', NULL,
       'Padel', 'Complexo desportivo', NULL,
       39.8025797, -7.5017318, 'OPENSTREETMAP', 'OSM:WAY:1266563810',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1266563810')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Padel Castelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8025797)) <= 0.00070
               AND ABS(v.longitude - (-7.5017318)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:436180509 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Desportos Motorizados de Castelo Branco', 'Castelo Branco', 'Castelo Branco', NULL,
       'Karting', 'Complexo desportivo', NULL,
       39.8618268, -7.4582815, 'OPENSTREETMAP', 'OSM:WAY:436180509',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:436180509')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Desportos Motorizados de Castelo Branco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8618268)) <= 0.00070
               AND ABS(v.longitude - (-7.4582815)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:552235758 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Desportos Radicais de Castelo Branco', 'Castelo Branco', 'Castelo Branco', NULL,
       'Skate;BMX', 'Campo desportivo', NULL,
       39.8182072, -7.5139537, 'OPENSTREETMAP', 'OSM:WAY:552235758',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:552235758')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Desportos Radicais de Castelo Branco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8182072)) <= 0.00070
               AND ABS(v.longitude - (-7.5139537)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1183482139 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Salgueiro do Campo', 'Castelo Branco', 'Castelo Branco', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.8951406, -7.5784397, 'OPENSTREETMAP', 'OSM:WAY:1183482139',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1183482139')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Salgueiro do Campo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8951406)) <= 0.00070
               AND ABS(v.longitude - (-7.5784397)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1119279644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo São Vicente da Beira', 'Castelo Branco', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0380121, -7.5637318, 'OPENSTREETMAP', 'OSM:WAY:1119279644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1119279644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo São Vicente da Beira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0380121)) <= 0.00070
               AND ABS(v.longitude - (-7.5637318)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:408814353 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Praia Fluvial de Almaceda', 'Castelo Branco', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.0076279, -7.6611993, 'OPENSTREETMAP', 'OSM:WAY:408814353',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:408814353')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Praia Fluvial de Almaceda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0076279)) <= 0.00070
               AND ABS(v.longitude - (-7.6611993)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13847170713 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Zakigym', 'Castelo Branco', 'Castelo Branco', NULL,
       'Ginástica', 'Ginásio', NULL,
       39.8324446, -7.4971975, 'OPENSTREETMAP', 'OSM:NODE:13847170713',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13847170713')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo Branco'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Zakigym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8324446)) <= 0.00070
               AND ABS(v.longitude - (-7.4971975)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12944670414 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Barco', 'Covilhã', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1722806, -7.6137235, 'OPENSTREETMAP', 'OSM:NODE:12944670414',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12944670414')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Barco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1722806)) <= 0.00070
               AND ABS(v.longitude - (-7.6137235)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:295251638 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Jorge da Beira', 'Covilhã', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1722701, -7.7811399, 'OPENSTREETMAP', 'OSM:WAY:295251638',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:295251638')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Jorge da Beira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1722701)) <= 0.00070
               AND ABS(v.longitude - (-7.7811399)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1422394041 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Ferro', 'Covilhã', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2402057, -7.4463804, 'OPENSTREETMAP', 'OSM:WAY:1422394041',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1422394041')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Ferro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2402057)) <= 0.00070
               AND ABS(v.longitude - (-7.4463804)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1468728237 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Turístico da Lameira', 'Covilhã', 'Castelo Branco', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.2217977, -7.6427644, 'OPENSTREETMAP', 'OSM:WAY:1468728237',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1468728237')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Turístico da Lameira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2217977)) <= 0.00070
               AND ABS(v.longitude - (-7.6427644)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1323228814 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Country Club Covilhã', 'Covilhã', 'Castelo Branco', NULL,
       'Ténis;Squash;Ginásio;Natação', 'Complexo desportivo', NULL,
       40.2733767, -7.4775706, 'OPENSTREETMAP', 'OSM:WAY:1323228814',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1323228814')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Country Club Covilhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2733767)) <= 0.00070
               AND ABS(v.longitude - (-7.4775706)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13851538511 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estadio da Freguesia do Dominguiso (Lavajóla)', 'Covilhã', 'Castelo Branco', NULL,
       'Futebol;Corrida', 'Estádio', NULL,
       40.2066354, -7.5230272, 'OPENSTREETMAP', 'OSM:NODE:13851538511',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13851538511')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estadio da Freguesia do Dominguiso (Lavajóla)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2066354)) <= 0.00070
               AND ABS(v.longitude - (-7.5230272)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1330203957 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'In Corpore Sano', 'Covilhã', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2697084, -7.4927252, 'OPENSTREETMAP', 'OSM:WAY:1330203957',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1330203957')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('In Corpore Sano'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2697084)) <= 0.00070
               AND ABS(v.longitude - (-7.4927252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:295661253 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Ondas', 'Covilhã', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.2736163, -7.4955019, 'OPENSTREETMAP', 'OSM:WAY:295661253',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:295661253')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Ondas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2736163)) <= 0.00070
               AND ABS(v.longitude - (-7.4955019)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10997601874 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'piscina Exterior do Belo Zêzere', 'Covilhã', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.2445573, -7.5031833, 'OPENSTREETMAP', 'OSM:NODE:10997601874',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10997601874')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('piscina Exterior do Belo Zêzere'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2445573)) <= 0.00070
               AND ABS(v.longitude - (-7.5031833)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152480244 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo da Covilhã', 'Covilhã', 'Castelo Branco', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.2648432, -7.4976203, 'OPENSTREETMAP', 'OSM:WAY:152480244',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152480244')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo da Covilhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2648432)) <= 0.00070
               AND ABS(v.longitude - (-7.4976203)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1186921044 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quinta do sangrinhal (piscina)', 'Covilhã', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.2576356, -7.4991914, 'OPENSTREETMAP', 'OSM:WAY:1186921044',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1186921044')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quinta do sangrinhal (piscina)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2576356)) <= 0.00070
               AND ABS(v.longitude - (-7.4991914)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13851578560 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Futsal.', 'Covilhã', 'Castelo Branco', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.2093557, -7.5253292, 'OPENSTREETMAP', 'OSM:NODE:13851578560',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13851578560')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Futsal.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2093557)) <= 0.00070
               AND ABS(v.longitude - (-7.5253292)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:172088436 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Unidos Futebol Clube do Tortosendo', 'Covilhã', 'Castelo Branco', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.2392026, -7.5233377, 'OPENSTREETMAP', 'OSM:WAY:172088436',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:172088436')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Unidos Futebol Clube do Tortosendo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2392026)) <= 0.00070
               AND ABS(v.longitude - (-7.5233377)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1328578120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Wellness Health Club', 'Covilhã', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2654089, -7.4943272, 'OPENSTREETMAP', 'OSM:WAY:1328578120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1328578120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Covilhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Wellness Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2654089)) <= 0.00070
               AND ABS(v.longitude - (-7.4943272)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:299687743 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Cruzeiro', 'Fundão', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1434851, -7.6542414, 'OPENSTREETMAP', 'OSM:WAY:299687743',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:299687743')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Cruzeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1434851)) <= 0.00070
               AND ABS(v.longitude - (-7.6542414)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:170562857 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal do Fundão', 'Fundão', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1449857, -7.4833060, 'OPENSTREETMAP', 'OSM:WAY:170562857',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:170562857')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal do Fundão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1449857)) <= 0.00070
               AND ABS(v.longitude - (-7.4833060)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:436259469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Associação Desportiva do Fundão', 'Fundão', 'Castelo Branco', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.1362602, -7.4990081, 'OPENSTREETMAP', 'OSM:WAY:436259469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:436259469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Associação Desportiva do Fundão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1362602)) <= 0.00070
               AND ABS(v.longitude - (-7.4990081)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:436564706 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal do Fundão', 'Fundão', 'Castelo Branco', NULL,
       'Futsal', 'Complexo desportivo', NULL,
       40.1410333, -7.5028222, 'OPENSTREETMAP', 'OSM:WAY:436564706',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:436564706')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal do Fundão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1410333)) <= 0.00070
               AND ABS(v.longitude - (-7.5028222)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:179607718 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina da Junta de Freguesia de Orca', 'Fundão', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.0517174, -7.3638441, 'OPENSTREETMAP', 'OSM:WAY:179607718',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:179607718')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina da Junta de Freguesia de Orca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0517174)) <= 0.00070
               AND ABS(v.longitude - (-7.3638441)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:435683243 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Descobertas', 'Fundão', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.1335710, -7.5025184, 'OPENSTREETMAP', 'OSM:WAY:435683243',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:435683243')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Descobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1335710)) <= 0.00070
               AND ABS(v.longitude - (-7.5025184)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102769371 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Orca', 'Fundão', 'Castelo Branco', NULL,
       'Futebol', 'Estádio', NULL,
       40.0510984, -7.3637377, 'OPENSTREETMAP', 'OSM:WAY:102769371',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102769371')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Orca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0510984)) <= 0.00070
               AND ABS(v.longitude - (-7.3637377)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:93087446 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Relvado Sintético', 'Fundão', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1341464, -7.5020969, 'OPENSTREETMAP', 'OSM:WAY:93087446',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:93087446')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Relvado Sintético'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1341464)) <= 0.00070
               AND ABS(v.longitude - (-7.5020969)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:220509989 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Rio de Janeiro', 'Fundão', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0671422, -7.8044503, 'OPENSTREETMAP', 'OSM:WAY:220509989',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:220509989')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Rio de Janeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0671422)) <= 0.00070
               AND ABS(v.longitude - (-7.8044503)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4228850800 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SH Fit', 'Fundão', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1356946, -7.5068387, 'OPENSTREETMAP', 'OSM:NODE:4228850800',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4228850800')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fundão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SH Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1356946)) <= 0.00070
               AND ABS(v.longitude - (-7.5068387)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:341061384 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Club União Idanhense', 'Idanha-a-Nova', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.9425931, -7.2456464, 'OPENSTREETMAP', 'OSM:WAY:341061384',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:341061384')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Idanha-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Club União Idanhense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9425931)) <= 0.00070
               AND ABS(v.longitude - (-7.2456464)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:229279291 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Monfortinho', 'Idanha-a-Nova', 'Castelo Branco', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.9689698, -6.9039295, 'OPENSTREETMAP', 'OSM:WAY:229279291',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:229279291')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Idanha-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Monfortinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9689698)) <= 0.00070
               AND ABS(v.longitude - (-6.9039295)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:635444504 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Penha Garcia', 'Idanha-a-Nova', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.0335721, -7.0096817, 'OPENSTREETMAP', 'OSM:WAY:635444504',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:635444504')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Idanha-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Penha Garcia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0335721)) <= 0.00070
               AND ABS(v.longitude - (-7.0096817)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:420352746 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo Termas de Monfortinho', 'Idanha-a-Nova', 'Castelo Branco', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.9952710, -6.8882691, 'OPENSTREETMAP', 'OSM:WAY:420352746',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:420352746')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Idanha-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo Termas de Monfortinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9952710)) <= 0.00070
               AND ABS(v.longitude - (-6.8882691)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99206040 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Recreativa e Cultural de Oleiros', 'Oleiros', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.9231314, -7.9124608, 'OPENSTREETMAP', 'OSM:WAY:99206040',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99206040')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oleiros'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Recreativa e Cultural de Oleiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9231314)) <= 0.00070
               AND ABS(v.longitude - (-7.9124608)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:637905536 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo do Mosteiro', 'Oleiros', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.8989109, -7.9639743, 'OPENSTREETMAP', 'OSM:WAY:637905536',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:637905536')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oleiros'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo do Mosteiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8989109)) <= 0.00070
               AND ABS(v.longitude - (-7.9639743)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1079128404 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Oleiros', 'Oleiros', 'Castelo Branco', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.9226118, -7.9088946, 'OPENSTREETMAP', 'OSM:WAY:1079128404',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1079128404')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oleiros'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Oleiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9226118)) <= 0.00070
               AND ABS(v.longitude - (-7.9088946)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:538962674 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo de Penamacor', 'Penamacor', 'Castelo Branco', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.1700577, -7.1751636, 'OPENSTREETMAP', 'OSM:WAY:538962674',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:538962674')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penamacor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo de Penamacor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1700577)) <= 0.00070
               AND ABS(v.longitude - (-7.1751636)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169728966 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Cobertas de Penamacor', 'Penamacor', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.1652068, -7.1734737, 'OPENSTREETMAP', 'OSM:WAY:1169728966',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169728966')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penamacor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Cobertas de Penamacor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1652068)) <= 0.00070
               AND ABS(v.longitude - (-7.1734737)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:538962695 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Penamacor', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       40.1701544, -7.1771203, 'OPENSTREETMAP', 'OSM:WAY:538962695',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:538962695')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penamacor'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1701544)) <= 0.00070
               AND ABS(v.longitude - (-7.1771203)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:705795556 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de São Pedro do Esteval', 'Proença-a-Nova', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       39.6387410, -7.8398762, 'OPENSTREETMAP', 'OSM:WAY:705795556',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:705795556')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Proença-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de São Pedro do Esteval'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6387410)) <= 0.00070
               AND ABS(v.longitude - (-7.8398762)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183713791 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Vitória de Sernache', 'Sertã', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8070189, -8.1932978, 'OPENSTREETMAP', 'OSM:WAY:183713791',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183713791')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sertã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Vitória de Sernache'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8070189)) <= 0.00070
               AND ABS(v.longitude - (-8.1932978)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:468488651 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pelado GDVS', 'Sertã', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8078415, -8.1935693, 'OPENSTREETMAP', 'OSM:WAY:468488651',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:468488651')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sertã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pelado GDVS'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8078415)) <= 0.00070
               AND ABS(v.longitude - (-8.1935693)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:468516792 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sertanense Futebol Clube', 'Sertã', 'Castelo Branco', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7975096, -8.0967063, 'OPENSTREETMAP', 'OSM:WAY:468516792',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:468516792')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sertã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sertanense Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7975096)) <= 0.00070
               AND ABS(v.longitude - (-8.0967063)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:694456160 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Fliuvial', 'Vila de Rei', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       39.7045363, -8.2227754, 'OPENSTREETMAP', 'OSM:WAY:694456160',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:694456160')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila de Rei'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Fliuvial'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7045363)) <= 0.00070
               AND ABS(v.longitude - (-8.2227754)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:704120419 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Fluvial', 'Vila de Rei', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       39.7339914, -8.2092382, 'OPENSTREETMAP', 'OSM:WAY:704120419',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:704120419')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila de Rei'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Fluvial'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7339914)) <= 0.00070
               AND ABS(v.longitude - (-8.2092382)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169934349 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Fratel', 'Vila Velha de Ródão', 'Castelo Branco', NULL,
       'Natação', 'Piscina', NULL,
       39.6316803, -7.7463603, 'OPENSTREETMAP', 'OSM:WAY:1169934349',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169934349')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Velha de Ródão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Fratel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6316803)) <= 0.00070
               AND ABS(v.longitude - (-7.7463603)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1314143122 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'World Gym', 'Vila Velha de Ródão', 'Castelo Branco', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.6520121, -7.6680018, 'OPENSTREETMAP', 'OSM:WAY:1314143122',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1314143122')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Velha de Ródão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('World Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6520121)) <= 0.00070
               AND ABS(v.longitude - (-7.6680018)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
