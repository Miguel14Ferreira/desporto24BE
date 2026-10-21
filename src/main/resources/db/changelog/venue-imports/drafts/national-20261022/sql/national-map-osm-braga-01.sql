-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:11117392489 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pro-Energy Amares Fitness Club', 'Amares', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.6282754, -8.3438827, 'OPENSTREETMAP', 'OSM:NODE:11117392489',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11117392489')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pro-Energy Amares Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6282754)) <= 0.00070
               AND ABS(v.longitude - (-8.3438827)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13914798464 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Focus Fitness', 'Barcelos', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5355405, -8.6197289, 'OPENSTREETMAP', 'OSM:NODE:13914798464',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13914798464')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Focus Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5355405)) <= 0.00070
               AND ABS(v.longitude - (-8.6197289)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13914798465 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Focus Fitness', 'Barcelos', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5207515, -8.6235781, 'OPENSTREETMAP', 'OSM:NODE:13914798465',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13914798465')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Focus Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5207515)) <= 0.00070
               AND ABS(v.longitude - (-8.6235781)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2808100548 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Braga Sport Center', 'Braga', 'Braga', NULL,
       'Taekwondo', 'Complexo desportivo', NULL,
       41.5474716, -8.4167561, 'OPENSTREETMAP', 'OSM:NODE:2808100548',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2808100548')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Braga Sport Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5474716)) <= 0.00070
               AND ABS(v.longitude - (-8.4167561)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:22820754 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Caseta', 'Braga', 'Braga', NULL,
       'Futebol;Rugby', 'Campo desportivo', 'OUTDOOR',
       41.5333590, -8.4055283, 'OPENSTREETMAP', 'OSM:WAY:22820754',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:22820754')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Caseta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5333590)) <= 0.00070
               AND ABS(v.longitude - (-8.4055283)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107911243 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo Maximinense', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5395411, -8.4300497, 'OPENSTREETMAP', 'OSM:WAY:107911243',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107911243')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo Maximinense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5395411)) <= 0.00070
               AND ABS(v.longitude - (-8.4300497)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8435127675 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fisiminho', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5661759, -8.4335612, 'OPENSTREETMAP', 'OSM:NODE:8435127675',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8435127675')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fisiminho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5661759)) <= 0.00070
               AND ABS(v.longitude - (-8.4335612)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11776462130 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Funcio Fitness', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5594189, -8.4050024, 'OPENSTREETMAP', 'OSM:NODE:11776462130',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11776462130')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Funcio Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5594189)) <= 0.00070
               AND ABS(v.longitude - (-8.4050024)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119559909 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Golf Club de Braga - Driving Range', 'Braga', 'Braga', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.5846620, -8.4456800, 'OPENSTREETMAP', 'OSM:WAY:119559909',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119559909')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Golf Club de Braga - Driving Range'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5846620)) <= 0.00070
               AND ABS(v.longitude - (-8.4456800)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1458227030 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Ponte', 'Braga', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.5394690, -8.4225052, 'OPENSTREETMAP', 'OSM:WAY:1458227030',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1458227030')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Ponte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5394690)) <= 0.00070
               AND ABS(v.longitude - (-8.4225052)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:436573286 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal das Parretas', 'Braga', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.5548562, -8.4365902, 'OPENSTREETMAP', 'OSM:WAY:436573286',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:436573286')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal das Parretas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5548562)) <= 0.00070
               AND ABS(v.longitude - (-8.4365902)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8597100072 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Prime - Neo | Performance & Rehab', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5619312, -8.4413047, 'OPENSTREETMAP', 'OSM:NODE:8597100072',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8597100072')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Prime - Neo | Performance & Rehab'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5619312)) <= 0.00070
               AND ABS(v.longitude - (-8.4413047)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:687467478 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue Polidesportivo de Santa Lucrécia de Algeriz', 'Braga', 'Braga', NULL,
       'Futebol;Basquetebol', 'Complexo desportivo', NULL,
       41.5921295, -8.3734684, 'OPENSTREETMAP', 'OSM:WAY:687467478',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:687467478')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue Polidesportivo de Santa Lucrécia de Algeriz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5921295)) <= 0.00070
               AND ABS(v.longitude - (-8.3734684)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:570683339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Supremus Crossfit', 'Braga', 'Braga', NULL,
       'Halterofilismo', 'Ginásio', NULL,
       41.5465168, -8.4345831, 'OPENSTREETMAP', 'OSM:WAY:570683339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:570683339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Supremus Crossfit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5465168)) <= 0.00070
               AND ABS(v.longitude - (-8.4345831)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13931276181 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Team Personal Training', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5310414, -8.4102058, 'OPENSTREETMAP', 'OSM:NODE:13931276181',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13931276181')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Team Personal Training'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5310414)) <= 0.00070
               AND ABS(v.longitude - (-8.4102058)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1170731499 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Social João Paulo II', 'Esposende', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.4955143, -8.7838334, 'OPENSTREETMAP', 'OSM:WAY:1170731499',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1170731499')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Social João Paulo II'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4955143)) <= 0.00070
               AND ABS(v.longitude - (-8.7838334)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1382649888 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Golfe da Quinta da Barca', 'Esposende', 'Braga', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.5158643, -8.7467889, 'OPENSTREETMAP', 'OSM:WAY:1382649888',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1382649888')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Golfe da Quinta da Barca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5158643)) <= 0.00070
               AND ABS(v.longitude - (-8.7467889)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1382649889 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Golfe da Quinta da Barca', 'Esposende', 'Braga', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.5162677, -8.7511590, 'OPENSTREETMAP', 'OSM:WAY:1382649889',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1382649889')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Golfe da Quinta da Barca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5162677)) <= 0.00070
               AND ABS(v.longitude - (-8.7511590)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1382649891 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Golfe da Quinta da Barca', 'Esposende', 'Braga', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.5190722, -8.7499219, 'OPENSTREETMAP', 'OSM:WAY:1382649891',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1382649891')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Golfe da Quinta da Barca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5190722)) <= 0.00070
               AND ABS(v.longitude - (-8.7499219)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1382649893 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Golfe da Quinta da Barca', 'Esposende', 'Braga', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.5182186, -8.7471872, 'OPENSTREETMAP', 'OSM:WAY:1382649893',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1382649893')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Golfe da Quinta da Barca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5182186)) <= 0.00070
               AND ABS(v.longitude - (-8.7471872)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1074009821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Esposende Padel', 'Esposende', 'Braga', NULL,
       'Padel', 'Complexo desportivo', NULL,
       41.5319027, -8.7607027, 'OPENSTREETMAP', 'OSM:WAY:1074009821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1074009821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Esposende Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5319027)) <= 0.00070
               AND ABS(v.longitude - (-8.7607027)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11769120444 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Go Fitness Esposende', 'Esposende', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5319250, -8.7797088, 'OPENSTREETMAP', 'OSM:NODE:11769120444',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11769120444')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Go Fitness Esposende'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5319250)) <= 0.00070
               AND ABS(v.longitude - (-8.7797088)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1178311031 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Forjães', 'Esposende', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.6155435, -8.7400140, 'OPENSTREETMAP', 'OSM:WAY:1178311031',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1178311031')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Forjães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6155435)) <= 0.00070
               AND ABS(v.longitude - (-8.7400140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7612173084 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Municipal dos Desportos de Fafe - Campo n.º 2', 'Fafe', 'Braga', NULL,
       'Ginásio;Futebol', 'Campo desportivo', NULL,
       41.4461590, -8.1683638, 'OPENSTREETMAP', 'OSM:NODE:7612173084',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7612173084')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Municipal dos Desportos de Fafe - Campo n.º 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4461590)) <= 0.00070
               AND ABS(v.longitude - (-8.1683638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2967425220 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Nokaute', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4865291, -8.3511658, 'OPENSTREETMAP', 'OSM:NODE:2967425220',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2967425220')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Nokaute'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4865291)) <= 0.00070
               AND ABS(v.longitude - (-8.3511658)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155331364 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Clube Operário de Campelos', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4642470, -8.3522994, 'OPENSTREETMAP', 'OSM:WAY:1155331364',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155331364')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Clube Operário de Campelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4642470)) <= 0.00070
               AND ABS(v.longitude - (-8.3522994)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6203275385 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Cidade berço', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4436477, -8.3005725, 'OPENSTREETMAP', 'OSM:NODE:6203275385',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6203275385')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Cidade berço'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4436477)) <= 0.00070
               AND ABS(v.longitude - (-8.3005725)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12018185216 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4460275, -8.2998983, 'OPENSTREETMAP', 'OSM:NODE:12018185216',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12018185216')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4460275)) <= 0.00070
               AND ABS(v.longitude - (-8.2998983)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8522671067 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oxigenium Fitness', 'Póvoa de Lanhoso', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5809610, -8.2706328, 'OPENSTREETMAP', 'OSM:NODE:8522671067',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8522671067')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Lanhoso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oxigenium Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5809610)) <= 0.00070
               AND ABS(v.longitude - (-8.2706328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:225860169 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis da Urbanização do Vinhal', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.4108786, -8.5240263, 'OPENSTREETMAP', 'OSM:WAY:225860169',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:225860169')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis da Urbanização do Vinhal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4108786)) <= 0.00070
               AND ABS(v.longitude - (-8.5240263)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152786140 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal 22 de Junho', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.4016039, -8.5223396, 'OPENSTREETMAP', 'OSM:WAY:1152786140',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152786140')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal 22 de Junho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4016039)) <= 0.00070
               AND ABS(v.longitude - (-8.5223396)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5176843047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Status', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4098056, -8.5179495, 'OPENSTREETMAP', 'OSM:NODE:5176843047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5176843047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Status'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4098056)) <= 0.00070
               AND ABS(v.longitude - (-8.5179495)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:235938948 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Indoor Karting Famalicão', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Karting', 'Complexo desportivo', 'INDOOR',
       41.3694881, -8.5471796, 'OPENSTREETMAP', 'OSM:WAY:235938948',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:235938948')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Indoor Karting Famalicão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3694881)) <= 0.00070
               AND ABS(v.longitude - (-8.5471796)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13233158074 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Inspira Pilates', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4117762, -8.5231226, 'OPENSTREETMAP', 'OSM:NODE:13233158074',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13233158074')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Inspira Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4117762)) <= 0.00070
               AND ABS(v.longitude - (-8.5231226)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3201704685 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.4027330, -8.5206629, 'OPENSTREETMAP', 'OSM:NODE:3201704685',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3201704685')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4027330)) <= 0.00070
               AND ABS(v.longitude - (-8.5206629)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:292265503 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Riba D''Ave Hóquei Clube', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Hóquei', 'Complexo desportivo', NULL,
       41.3935187, -8.3924137, 'OPENSTREETMAP', 'OSM:WAY:292265503',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:292265503')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Riba D''Ave Hóquei Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3935187)) <= 0.00070
               AND ABS(v.longitude - (-8.3924137)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13173769973 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Twenty Fit', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4145237, -8.5204562, 'OPENSTREETMAP', 'OSM:NODE:13173769973',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13173769973')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Twenty Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4145237)) <= 0.00070
               AND ABS(v.longitude - (-8.5204562)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2793525275 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GymnoPrado', 'Vila Verde', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5969095, -8.4666093, 'OPENSTREETMAP', 'OSM:NODE:2793525275',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2793525275')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GymnoPrado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5969095)) <= 0.00070
               AND ABS(v.longitude - (-8.4666093)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7221367279 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Let´s Go Ginásio', 'Vila Verde', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.6289237, -8.4341414, 'OPENSTREETMAP', 'OSM:NODE:7221367279',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7221367279')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Let´s Go Ginásio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6289237)) <= 0.00070
               AND ABS(v.longitude - (-8.4341414)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:533430099 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Cachadinhas', 'Amares', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6750744, -8.3782469, 'OPENSTREETMAP', 'OSM:WAY:533430099',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:533430099')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Cachadinhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6750744)) <= 0.00070
               AND ABS(v.longitude - (-8.3782469)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:55599001 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Santa Marta', 'Amares', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6523964, -8.2991007, 'OPENSTREETMAP', 'OSM:WAY:55599001',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:55599001')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Santa Marta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6523964)) <= 0.00070
               AND ABS(v.longitude - (-8.2991007)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:55598987 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Goães', 'Amares', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6556928, -8.3159776, 'OPENSTREETMAP', 'OSM:WAY:55598987',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:55598987')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Goães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6556928)) <= 0.00070
               AND ABS(v.longitude - (-8.3159776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:293282535 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Amares', 'Amares', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.6249463, -8.3749012, 'OPENSTREETMAP', 'OSM:WAY:293282535',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:293282535')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Amares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6249463)) <= 0.00070
               AND ABS(v.longitude - (-8.3749012)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1341778868 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Amares', 'Amares', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6249858, -8.3750763, 'OPENSTREETMAP', 'OSM:WAY:1341778868',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1341778868')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Amares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6249858)) <= 0.00070
               AND ABS(v.longitude - (-8.3750763)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:308903513 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Caldelas', 'Amares', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.6689265, -8.3678508, 'OPENSTREETMAP', 'OSM:WAY:308903513',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:308903513')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Caldelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6689265)) <= 0.00070
               AND ABS(v.longitude - (-8.3678508)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4794999866 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Amares', 'Amares', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.6321364, -8.3652088, 'OPENSTREETMAP', 'OSM:NODE:4794999866',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4794999866')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Amares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6321364)) <= 0.00070
               AND ABS(v.longitude - (-8.3652088)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1211617014 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Amares', 'Amares', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.6309561, -8.3436518, 'OPENSTREETMAP', 'OSM:WAY:1211617014',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1211617014')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Amares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6309561)) <= 0.00070
               AND ABS(v.longitude - (-8.3436518)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:251831660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 13 de Maio', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6143621, -8.6991005, 'OPENSTREETMAP', 'OSM:WAY:251831660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:251831660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 13 de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6143621)) <= 0.00070
               AND ABS(v.longitude - (-8.6991005)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1958311000 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'campo de fotebol de São martinho', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5939608, -8.6030284, 'OPENSTREETMAP', 'OSM:NODE:1958311000',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1958311000')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('campo de fotebol de São martinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5939608)) <= 0.00070
               AND ABS(v.longitude - (-8.6030284)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:495506785 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol/Basquete', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5310618, -8.6156407, 'OPENSTREETMAP', 'OSM:WAY:495506785',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:495506785')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol/Basquete'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5310618)) <= 0.00070
               AND ABS(v.longitude - (-8.6156407)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1360362442 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Alcaides de Faria', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4762435, -8.6735764, 'OPENSTREETMAP', 'OSM:WAY:1360362442',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1360362442')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Alcaides de Faria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4762435)) <= 0.00070
               AND ABS(v.longitude - (-8.6735764)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185199797 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Carapeços', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5817161, -8.6292424, 'OPENSTREETMAP', 'OSM:WAY:185199797',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185199797')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Carapeços'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5817161)) <= 0.00070
               AND ABS(v.longitude - (-8.6292424)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:253682010 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Macieira de Rates', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4297191, -8.6513998, 'OPENSTREETMAP', 'OSM:WAY:253682010',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:253682010')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Macieira de Rates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4297191)) <= 0.00070
               AND ABS(v.longitude - (-8.6513998)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1312329135 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Negreiros', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4340999, -8.6185049, 'OPENSTREETMAP', 'OSM:WAY:1312329135',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1312329135')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Negreiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4340999)) <= 0.00070
               AND ABS(v.longitude - (-8.6185049)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:252263376 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Pedra Furada', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4669351, -8.6454368, 'OPENSTREETMAP', 'OSM:WAY:252263376',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:252263376')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Pedra Furada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4669351)) <= 0.00070
               AND ABS(v.longitude - (-8.6454368)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:321571315 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Santa Maria Futebol Clube', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5582439, -8.5786783, 'OPENSTREETMAP', 'OSM:WAY:321571315',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:321571315')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Santa Maria Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5582439)) <= 0.00070
               AND ABS(v.longitude - (-8.5786783)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:788918751 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Sporting Clube da Ucha', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5828189, -8.5102136, 'OPENSTREETMAP', 'OSM:WAY:788918751',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:788918751')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Sporting Clube da Ucha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5828189)) <= 0.00070
               AND ABS(v.longitude - (-8.5102136)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185199807 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol NC Os Andorinhas de Arcozelo', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5371897, -8.6052981, 'OPENSTREETMAP', 'OSM:WAY:185199807',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185199807')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol NC Os Andorinhas de Arcozelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5371897)) <= 0.00070
               AND ABS(v.longitude - (-8.6052981)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:225546426 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campod e Futebol - Palme Futebol Clube', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5859601, -8.7102587, 'OPENSTREETMAP', 'OSM:WAY:225546426',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:225546426')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campod e Futebol - Palme Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5859601)) <= 0.00070
               AND ABS(v.longitude - (-8.7102587)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:276302243 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos 1º de Janeiro', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5624963, -8.5566560, 'OPENSTREETMAP', 'OSM:WAY:276302243',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:276302243')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos 1º de Janeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5624963)) <= 0.00070
               AND ABS(v.longitude - (-8.5566560)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185199795 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos das Fontainhas', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5433828, -8.6834447, 'OPENSTREETMAP', 'OSM:WAY:185199795',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185199795')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos das Fontainhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5433828)) <= 0.00070
               AND ABS(v.longitude - (-8.6834447)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:942689426 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Necessidades Futebol Clube', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4745915, -8.7264128, 'OPENSTREETMAP', 'OSM:WAY:942689426',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:942689426')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Necessidades Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4745915)) <= 0.00070
               AND ABS(v.longitude - (-8.7264128)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802022758 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol de 11 e 5 de Lijó', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5732295, -8.6177832, 'OPENSTREETMAP', 'OSM:WAY:802022758',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802022758')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol de 11 e 5 de Lijó'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5732295)) <= 0.00070
               AND ABS(v.longitude - (-8.6177832)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:923505153 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético de Futebol', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4923912, -8.6536356, 'OPENSTREETMAP', 'OSM:WAY:923505153',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:923505153')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético de Futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4923912)) <= 0.00070
               AND ABS(v.longitude - (-8.6536356)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107680289 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Cidade de Barcelos', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.5514398, -8.6233461, 'OPENSTREETMAP', 'OSM:WAY:107680289',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107680289')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Cidade de Barcelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5514398)) <= 0.00070
               AND ABS(v.longitude - (-8.6233461)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:617582773 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gil Vicente Futebol Clube', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5512807, -8.6231796, 'OPENSTREETMAP', 'OSM:WAY:617582773',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:617582773')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gil Vicente Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5512807)) <= 0.00070
               AND ABS(v.longitude - (-8.6231796)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:495509527 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Radical', 'Barcelos', 'Braga', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.5415480, -8.6151973, 'OPENSTREETMAP', 'OSM:WAY:495509527',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:495509527')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5415480)) <= 0.00070
               AND ABS(v.longitude - (-8.6151973)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1110629374 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Gamil (Pena)', 'Barcelos', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5212923, -8.5947999, 'OPENSTREETMAP', 'OSM:WAY:1110629374',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1110629374')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Gamil (Pena)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5212923)) <= 0.00070
               AND ABS(v.longitude - (-8.5947999)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10885477027 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ZFIT Training', 'Barcelos', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5141112, -8.5839498, 'OPENSTREETMAP', 'OSM:NODE:10885477027',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10885477027')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barcelos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ZFIT Training'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5141112)) <= 0.00070
               AND ABS(v.longitude - (-8.5839498)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2191071432 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Akua fit', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5336766, -8.4659849, 'OPENSTREETMAP', 'OSM:NODE:2191071432',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2191071432')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Akua fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5336766)) <= 0.00070
               AND ABS(v.longitude - (-8.4659849)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1194244446 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be-Fit', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5597146, -8.4224753, 'OPENSTREETMAP', 'OSM:WAY:1194244446',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1194244446')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be-Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5597146)) <= 0.00070
               AND ABS(v.longitude - (-8.4224753)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12714512996 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bracara', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5563306, -8.4040085, 'OPENSTREETMAP', 'OSM:NODE:12714512996',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12714512996')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bracara'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5563306)) <= 0.00070
               AND ABS(v.longitude - (-8.4040085)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:473899743 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Celestino Lobo (Dumiense FC)', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5718935, -8.4325091, 'OPENSTREETMAP', 'OSM:WAY:473899743',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:473899743')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Celestino Lobo (Dumiense FC)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5718935)) <= 0.00070
               AND ABS(v.longitude - (-8.4325091)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166728868 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol-Cunha', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5028922, -8.5063654, 'OPENSTREETMAP', 'OSM:WAY:166728868',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166728868')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol-Cunha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5028922)) <= 0.00070
               AND ABS(v.longitude - (-8.5063654)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:55603064 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol das Verdosas', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5646764, -8.3967283, 'OPENSTREETMAP', 'OSM:WAY:55603064',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:55603064')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol das Verdosas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5646764)) <= 0.00070
               AND ABS(v.longitude - (-8.3967283)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183928125 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Frossos', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5700261, -8.4477014, 'OPENSTREETMAP', 'OSM:WAY:183928125',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183928125')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Frossos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5700261)) <= 0.00070
               AND ABS(v.longitude - (-8.4477014)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:207760824 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Mire de Tibães', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5709618, -8.4749108, 'OPENSTREETMAP', 'OSM:WAY:207760824',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:207760824')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Mire de Tibães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5709618)) <= 0.00070
               AND ABS(v.longitude - (-8.4749108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136104939 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Panóias', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5708204, -8.4680269, 'OPENSTREETMAP', 'OSM:WAY:136104939',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136104939')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Panóias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5708204)) <= 0.00070
               AND ABS(v.longitude - (-8.4680269)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107641106 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Pedro', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5699569, -8.3653874, 'OPENSTREETMAP', 'OSM:WAY:107641106',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107641106')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Pedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5699569)) <= 0.00070
               AND ABS(v.longitude - (-8.3653874)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:169366255 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vilaça', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5174144, -8.4798013, 'OPENSTREETMAP', 'OSM:WAY:169366255',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:169366255')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vilaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5174144)) <= 0.00070
               AND ABS(v.longitude - (-8.4798013)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183927960 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Bairro Bracara Augusta', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5960562, -8.4336363, 'OPENSTREETMAP', 'OSM:WAY:183927960',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183927960')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Bairro Bracara Augusta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5960562)) <= 0.00070
               AND ABS(v.longitude - (-8.4336363)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:149578802 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do GD Bairro da Misericórdia', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5598377, -8.4310367, 'OPENSTREETMAP', 'OSM:WAY:149578802',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:149578802')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do GD Bairro da Misericórdia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5598377)) <= 0.00070
               AND ABS(v.longitude - (-8.4310367)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105878652 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Escudeiros', 'Braga', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4771559, -8.4262166, 'OPENSTREETMAP', 'OSM:WAY:105878652',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105878652')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Escudeiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4771559)) <= 0.00070
               AND ABS(v.longitude - (-8.4262166)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:461630665 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Albino Duarte Pinheiro', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6010559, -8.3865374, 'OPENSTREETMAP', 'OSM:WAY:461630665',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:461630665')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Albino Duarte Pinheiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6010559)) <= 0.00070
               AND ABS(v.longitude - (-8.3865374)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:113850038 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Real', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5520051, -8.4477123, 'OPENSTREETMAP', 'OSM:WAY:113850038',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:113850038')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5520051)) <= 0.00070
               AND ABS(v.longitude - (-8.4477123)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108153342 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo das Camélias I', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5393390, -8.4258352, 'OPENSTREETMAP', 'OSM:WAY:108153342',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108153342')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo das Camélias I'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5393390)) <= 0.00070
               AND ABS(v.longitude - (-8.4258352)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108153349 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo das Camélias II', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5388168, -8.4246852, 'OPENSTREETMAP', 'OSM:WAY:108153349',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108153349')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo das Camélias II'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5388168)) <= 0.00070
               AND ABS(v.longitude - (-8.4246852)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1330402236 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis de Real', 'Braga', 'Braga', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.5559974, -8.4386700, 'OPENSTREETMAP', 'OSM:WAY:1330402236',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1330402236')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis de Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5559974)) <= 0.00070
               AND ABS(v.longitude - (-8.4386700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1187266970 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro de Braga', 'Braga', 'Braga', NULL,
       'Tiro', 'Campo desportivo', NULL,
       41.5641758, -8.3590928, 'OPENSTREETMAP', 'OSM:WAY:1187266970',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1187266970')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro de Braga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5641758)) <= 0.00070
               AND ABS(v.longitude - (-8.3590928)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:328684038 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos Maikes', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5293773, -8.3959722, 'OPENSTREETMAP', 'OSM:WAY:328684038',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:328684038')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos Maikes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5293773)) <= 0.00070
               AND ABS(v.longitude - (-8.3959722)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108315661 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Fontainhas', 'Braga', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5583994, -8.4214620, 'OPENSTREETMAP', 'OSM:WAY:108315661',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108315661')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Fontainhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5583994)) <= 0.00070
               AND ABS(v.longitude - (-8.4214620)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4326858485 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Lançamentos Doutor Braga dos Anjos', 'Braga', 'Braga', NULL,
       'Atletismo', 'Campo desportivo', NULL,
       41.5393276, -8.4243291, 'OPENSTREETMAP', 'OSM:NODE:4326858485',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4326858485')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Lançamentos Doutor Braga dos Anjos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5393276)) <= 0.00070
               AND ABS(v.longitude - (-8.4243291)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155137402 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Braga', 'Braga', 'Braga', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       41.5502051, -8.4070199, 'OPENSTREETMAP', 'OSM:WAY:155137402',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155137402')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Braga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5502051)) <= 0.00070
               AND ABS(v.longitude - (-8.4070199)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:149227787 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Figueiredo', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4962240, -8.4451539, 'OPENSTREETMAP', 'OSM:WAY:149227787',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:149227787')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Figueiredo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4962240)) <= 0.00070
               AND ABS(v.longitude - (-8.4451539)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106525468 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Maximinos', 'Braga', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.5415045, -8.4402703, 'OPENSTREETMAP', 'OSM:WAY:106525468',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106525468')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Maximinos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5415045)) <= 0.00070
               AND ABS(v.longitude - (-8.4402703)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440013766 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Pedralva', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5558857, -8.3096218, 'OPENSTREETMAP', 'OSM:WAY:440013766',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440013766')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Pedralva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5558857)) <= 0.00070
               AND ABS(v.longitude - (-8.3096218)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105880398 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Court de Ténis de Escudeiros', 'Braga', 'Braga', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.4770499, -8.4257614, 'OPENSTREETMAP', 'OSM:WAY:105880398',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105880398')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Court de Ténis de Escudeiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4770499)) <= 0.00070
               AND ABS(v.longitude - (-8.4257614)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:22660087 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio 1º de Maio', 'Braga', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.5389301, -8.4207030, 'OPENSTREETMAP', 'OSM:WAY:22660087',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:22660087')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5389301)) <= 0.00070
               AND ABS(v.longitude - (-8.4207030)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1348578815 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Amélia Morais', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5696293, -8.4254286, 'OPENSTREETMAP', 'OSM:WAY:1348578815',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1348578815')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Amélia Morais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5696293)) <= 0.00070
               AND ABS(v.longitude - (-8.4254286)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:274499267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio João Soares Vieira', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5766342, -8.4581096, 'OPENSTREETMAP', 'OSM:WAY:274499267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:274499267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio João Soares Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5766342)) <= 0.00070
               AND ABS(v.longitude - (-8.4581096)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1472643312 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Este Futebol Clube', 'Braga', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5700269, -8.3653920, 'OPENSTREETMAP', 'OSM:WAY:1472643312',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1472643312')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Este Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5700269)) <= 0.00070
               AND ABS(v.longitude - (-8.3653920)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12982557402 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5193288, -8.4208146, 'OPENSTREETMAP', 'OSM:NODE:12982557402',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12982557402')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5193288)) <= 0.00070
               AND ABS(v.longitude - (-8.4208146)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1415243926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5588049, -8.4068574, 'OPENSTREETMAP', 'OSM:WAY:1415243926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1415243926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5588049)) <= 0.00070
               AND ABS(v.longitude - (-8.4068574)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3839565469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Flexgym', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5446713, -8.4139694, 'OPENSTREETMAP', 'OSM:NODE:3839565469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3839565469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Flexgym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5446713)) <= 0.00070
               AND ABS(v.longitude - (-8.4139694)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:163718955 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo de Adaúfe', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5912611, -8.4111958, 'OPENSTREETMAP', 'OSM:WAY:163718955',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:163718955')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo de Adaúfe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5912611)) <= 0.00070
               AND ABS(v.longitude - (-8.4111958)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:146234170 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo e Recreativo de Trandeiras', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4964419, -8.4166093, 'OPENSTREETMAP', 'OSM:WAY:146234170',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:146234170')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo e Recreativo de Trandeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4964419)) <= 0.00070
               AND ABS(v.longitude - (-8.4166093)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:409837363 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Leões de Santa Lucrécia', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6032597, -8.3777360, 'OPENSTREETMAP', 'OSM:WAY:409837363',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:409837363')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Leões de Santa Lucrécia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6032597)) <= 0.00070
               AND ABS(v.longitude - (-8.3777360)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108315659 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'half pipe', 'Braga', 'Braga', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.5583568, -8.4217418, 'OPENSTREETMAP', 'OSM:WAY:108315659',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108315659')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('half pipe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5583568)) <= 0.00070
               AND ABS(v.longitude - (-8.4217418)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9968223779 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Impulse', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5271014, -8.4482925, 'OPENSTREETMAP', 'OSM:NODE:9968223779',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9968223779')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Impulse'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5271014)) <= 0.00070
               AND ABS(v.longitude - (-8.4482925)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14058353518 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Jaqueline Amorim Pilates com Máquinas', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5303062, -8.4123017, 'OPENSTREETMAP', 'OSM:NODE:14058353518',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14058353518')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Jaqueline Amorim Pilates com Máquinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5303062)) <= 0.00070
               AND ABS(v.longitude - (-8.4123017)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11648477312 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kalorias', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5795634, -8.4285554, 'OPENSTREETMAP', 'OSM:NODE:11648477312',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11648477312')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kalorias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5795634)) <= 0.00070
               AND ABS(v.longitude - (-8.4285554)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14175410703 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Liberty Fitness Center', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5645946, -8.4073111, 'OPENSTREETMAP', 'OSM:NODE:14175410703',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14175410703')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Liberty Fitness Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5645946)) <= 0.00070
               AND ABS(v.longitude - (-8.4073111)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:536380727 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lomarense', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5280628, -8.4226891, 'OPENSTREETMAP', 'OSM:WAY:536380727',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:536380727')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lomarense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5280628)) <= 0.00070
               AND ABS(v.longitude - (-8.4226891)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4455197317 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'No Limits', 'Braga', 'Braga', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       41.5328685, -8.4471400, 'OPENSTREETMAP', 'OSM:NODE:4455197317',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4455197317')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('No Limits'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5328685)) <= 0.00070
               AND ABS(v.longitude - (-8.4471400)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:261068090 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Espinho', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5545057, -8.3569805, 'OPENSTREETMAP', 'OSM:WAY:261068090',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:261068090')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Espinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5545057)) <= 0.00070
               AND ABS(v.longitude - (-8.3569805)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:223133697 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Radical', 'Braga', 'Braga', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.5452899, -8.4286285, 'OPENSTREETMAP', 'OSM:WAY:223133697',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:223133697')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5452899)) <= 0.00070
               AND ABS(v.longitude - (-8.4286285)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:207760832 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Mire de Tibães', 'Braga', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5714640, -8.4752685, 'OPENSTREETMAP', 'OSM:WAY:207760832',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:207760832')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Desportivo de Mire de Tibães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5714640)) <= 0.00070
               AND ABS(v.longitude - (-8.4752685)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1492499804 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Personal 20', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5537568, -8.3954697, 'OPENSTREETMAP', 'OSM:NODE:1492499804',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1492499804')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Personal 20'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5537568)) <= 0.00070
               AND ABS(v.longitude - (-8.3954697)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:170673675 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Ferreiros', 'Braga', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5287715, -8.4528784, 'OPENSTREETMAP', 'OSM:WAY:170673675',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:170673675')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Ferreiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5287715)) <= 0.00070
               AND ABS(v.longitude - (-8.4528784)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:772943453 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Nogueiró', 'Braga', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5487909, -8.3815217, 'OPENSTREETMAP', 'OSM:WAY:772943453',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:772943453')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Nogueiró'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5487909)) <= 0.00070
               AND ABS(v.longitude - (-8.3815217)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:188333790 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Ferreiros para crianças', 'Braga', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5286488, -8.4525566, 'OPENSTREETMAP', 'OSM:WAY:188333790',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:188333790')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Ferreiros para crianças'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5286488)) <= 0.00070
               AND ABS(v.longitude - (-8.4525566)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:149312926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Lamas', 'Braga', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5026001, -8.4319730, 'OPENSTREETMAP', 'OSM:WAY:149312926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:149312926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Lamas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5026001)) <= 0.00070
               AND ABS(v.longitude - (-8.4319730)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:224855166 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Passos S. Julião', 'Braga', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.5307914, -8.4934561, 'OPENSTREETMAP', 'OSM:WAY:224855166',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:224855166')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Passos S. Julião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5307914)) <= 0.00070
               AND ABS(v.longitude - (-8.4934561)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:149811349 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais das Parretas', 'Braga', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5551166, -8.4366045, 'OPENSTREETMAP', 'OSM:WAY:149811349',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:149811349')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais das Parretas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5551166)) <= 0.00070
               AND ABS(v.longitude - (-8.4366045)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107606374 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Dume', 'Braga', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.5729469, -8.4329230, 'OPENSTREETMAP', 'OSM:WAY:107606374',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107606374')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Dume'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5729469)) <= 0.00070
               AND ABS(v.longitude - (-8.4329230)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:162971995 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Play Ball', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5726764, -8.4866171, 'OPENSTREETMAP', 'OSM:WAY:162971995',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:162971995')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Play Ball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5726764)) <= 0.00070
               AND ABS(v.longitude - (-8.4866171)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:161324859 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Agrinha', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5322178, -8.4147498, 'OPENSTREETMAP', 'OSM:WAY:161324859',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:161324859')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Agrinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5322178)) <= 0.00070
               AND ABS(v.longitude - (-8.4147498)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1324390124 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PumpTrack', 'Braga', 'Braga', NULL,
       'BMX;Ciclismo', 'Pista desportiva', NULL,
       41.5514093, -8.4051762, 'OPENSTREETMAP', 'OSM:WAY:1324390124',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1324390124')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PumpTrack'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5514093)) <= 0.00070
               AND ABS(v.longitude - (-8.4051762)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1204039366 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quinta de Monserrate', 'Braga', 'Braga', NULL,
       'Padel', 'Campo desportivo', NULL,
       41.5673559, -8.4225720, 'OPENSTREETMAP', 'OSM:WAY:1204039366',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1204039366')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quinta de Monserrate'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5673559)) <= 0.00070
               AND ABS(v.longitude - (-8.4225720)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1151936023 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Radical', 'Braga', 'Braga', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.5512949, -8.4055250, 'OPENSTREETMAP', 'OSM:WAY:1151936023',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1151936023')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5512949)) <= 0.00070
               AND ABS(v.longitude - (-8.4055250)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:149578909 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Realense Futebol Clube', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5554639, -8.4391736, 'OPENSTREETMAP', 'OSM:WAY:149578909',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:149578909')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Realense Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5554639)) <= 0.00070
               AND ABS(v.longitude - (-8.4391736)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:109253251 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue das Enguardas', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5604386, -8.4096479, 'OPENSTREETMAP', 'OSM:WAY:109253251',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:109253251')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue das Enguardas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5604386)) <= 0.00070
               AND ABS(v.longitude - (-8.4096479)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:132865735 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Figueiredo', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5046125, -8.4360338, 'OPENSTREETMAP', 'OSM:WAY:132865735',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:132865735')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Figueiredo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5046125)) <= 0.00070
               AND ABS(v.longitude - (-8.4360338)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105465936 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de São Mamede', 'Braga', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5748995, -8.3597529, 'OPENSTREETMAP', 'OSM:WAY:105465936',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105465936')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de São Mamede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5748995)) <= 0.00070
               AND ABS(v.longitude - (-8.3597529)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:258309143 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue do Areal', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5652972, -8.4091577, 'OPENSTREETMAP', 'OSM:WAY:258309143',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:258309143')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue do Areal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5652972)) <= 0.00070
               AND ABS(v.longitude - (-8.4091577)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107542843 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue do Lugar do Assento', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5853220, -8.4304206, 'OPENSTREETMAP', 'OSM:WAY:107542843',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107542843')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue do Lugar do Assento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5853220)) <= 0.00070
               AND ABS(v.longitude - (-8.4304206)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136675378 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue panoias', 'Braga', 'Braga', NULL,
       'Futebol;Ténis', 'Campo desportivo', NULL,
       41.5702887, -8.4677960, 'OPENSTREETMAP', 'OSM:WAY:136675378',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136675378')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue panoias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5702887)) <= 0.00070
               AND ABS(v.longitude - (-8.4677960)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107425207 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue Sintético Gualtar', 'Braga', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5701895, -8.3807607, 'OPENSTREETMAP', 'OSM:WAY:107425207',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107425207')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue Sintético Gualtar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5701895)) <= 0.00070
               AND ABS(v.longitude - (-8.3807607)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106759547 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5275703, -8.4489935, 'OPENSTREETMAP', 'OSM:WAY:106759547',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106759547')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5275703)) <= 0.00070
               AND ABS(v.longitude - (-8.4489935)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1266854063 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spot Health Club', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5615967, -8.4440552, 'OPENSTREETMAP', 'OSM:WAY:1266854063',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1266854063')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spot Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5615967)) <= 0.00070
               AND ABS(v.longitude - (-8.4440552)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166630239 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Talegre - Campo de Futebol de Ruílhe', 'Braga', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4982024, -8.5032882, 'OPENSTREETMAP', 'OSM:WAY:166630239',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166630239')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Talegre - Campo de Futebol de Ruílhe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4982024)) <= 0.00070
               AND ABS(v.longitude - (-8.5032882)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13357665846 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Treino Ativo', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5563502, -8.4036954, 'OPENSTREETMAP', 'OSM:NODE:13357665846',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13357665846')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Treino Ativo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5563502)) <= 0.00070
               AND ABS(v.longitude - (-8.4036954)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12754553145 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'True Motion', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5549745, -8.4149453, 'OPENSTREETMAP', 'OSM:NODE:12754553145',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12754553145')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('True Motion'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5549745)) <= 0.00070
               AND ABS(v.longitude - (-8.4149453)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12640877488 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Via Pilates', 'Braga', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5550890, -8.4035135, 'OPENSTREETMAP', 'OSM:NODE:12640877488',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12640877488')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Braga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Via Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5550890)) <= 0.00070
               AND ABS(v.longitude - (-8.4035135)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1133797256 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Outeiro', 'Cabeceiras de Basto', 'Braga', NULL,
       'Futebol;Ténis', 'Campo desportivo', NULL,
       41.5126800, -8.0204888, 'OPENSTREETMAP', 'OSM:WAY:1133797256',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1133797256')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cabeceiras de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Outeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5126800)) <= 0.00070
               AND ABS(v.longitude - (-8.0204888)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:248502664 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Cruz', 'Celorico de Basto', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3858456, -7.9914998, 'OPENSTREETMAP', 'OSM:WAY:248502664',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:248502664')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Cruz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3858456)) <= 0.00070
               AND ABS(v.longitude - (-7.9914998)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243791678 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Rego', 'Celorico de Basto', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4177275, -8.0869989, 'OPENSTREETMAP', 'OSM:WAY:243791678',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243791678')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Rego'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4177275)) <= 0.00070
               AND ABS(v.longitude - (-8.0869989)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:487412008 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo futsal de Arnoia', 'Celorico de Basto', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3731995, -8.0131479, 'OPENSTREETMAP', 'OSM:WAY:487412008',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:487412008')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo futsal de Arnoia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3731995)) <= 0.00070
               AND ABS(v.longitude - (-8.0131479)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:474348738 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mota Futebol Clube', 'Celorico de Basto', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3606915, -8.1057605, 'OPENSTREETMAP', 'OSM:WAY:474348738',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:474348738')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mota Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3606915)) <= 0.00070
               AND ABS(v.longitude - (-8.1057605)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14201762202 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ProForm Strenght and Conditioning', 'Celorico de Basto', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3825031, -8.0005309, 'OPENSTREETMAP', 'OSM:NODE:14201762202',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14201762202')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ProForm Strenght and Conditioning'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3825031)) <= 0.00070
               AND ABS(v.longitude - (-8.0005309)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:176992824 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Antas Futebol Clube', 'Esposende', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.6043136, -8.7939842, 'OPENSTREETMAP', 'OSM:WAY:176992824',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:176992824')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Antas Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6043136)) <= 0.00070
               AND ABS(v.longitude - (-8.7939842)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:468488304 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Águias Serpa Pinto', 'Esposende', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5083007, -8.7569461, 'OPENSTREETMAP', 'OSM:WAY:468488304',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:468488304')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Águias Serpa Pinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5083007)) <= 0.00070
               AND ABS(v.longitude - (-8.7569461)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:156248971 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Fonte', 'Esposende', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5288584, -8.7562707, 'OPENSTREETMAP', 'OSM:WAY:156248971',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:156248971')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Fonte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5288584)) <= 0.00070
               AND ABS(v.longitude - (-8.7562707)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:184829914 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Gemeses', 'Esposende', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5232944, -8.7366549, 'OPENSTREETMAP', 'OSM:WAY:184829914',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:184829914')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Gemeses'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5232944)) <= 0.00070
               AND ABS(v.longitude - (-8.7366549)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166972994 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vila Chã', 'Esposende', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.5684983, -8.7434627, 'OPENSTREETMAP', 'OSM:WAY:166972994',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166972994')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vila Chã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5684983)) <= 0.00070
               AND ABS(v.longitude - (-8.7434627)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155305669 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos de Palmeira de Faro', 'Esposende', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5379587, -8.7520108, 'OPENSTREETMAP', 'OSM:WAY:155305669',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155305669')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos de Palmeira de Faro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5379587)) <= 0.00070
               AND ABS(v.longitude - (-8.7520108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:840293980 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis do Marachão', 'Esposende', 'Braga', NULL,
       'Ténis', 'Campo desportivo', 'OUTDOOR',
       41.5046009, -8.7178992, 'OPENSTREETMAP', 'OSM:WAY:840293980',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:840293980')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis do Marachão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5046009)) <= 0.00070
               AND ABS(v.longitude - (-8.7178992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1550435882 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Cedro', 'Esposende', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5097927, -8.7412112, 'OPENSTREETMAP', 'OSM:WAY:1550435882',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1550435882')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Cedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5097927)) <= 0.00070
               AND ABS(v.longitude - (-8.7412112)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:121482662 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Canoagem Rio Neiva', 'Esposende', 'Braga', NULL,
       'Canoagem', 'Complexo desportivo', NULL,
       41.6062373, -8.8019202, 'OPENSTREETMAP', 'OSM:WAY:121482662',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:121482662')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Canoagem Rio Neiva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6062373)) <= 0.00070
               AND ABS(v.longitude - (-8.8019202)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:531648861 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Náutico de Gemeses', 'Esposende', 'Braga', NULL,
       'Canoagem', 'Complexo desportivo', NULL,
       41.5172194, -8.7397285, 'OPENSTREETMAP', 'OSM:WAY:531648861',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:531648861')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Náutico de Gemeses'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5172194)) <= 0.00070
               AND ABS(v.longitude - (-8.7397285)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122205481 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Padre Avelino Peres Filipe', 'Esposende', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.5591968, -8.7728406, 'OPENSTREETMAP', 'OSM:WAY:122205481',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122205481')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Padre Avelino Peres Filipe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5591968)) <= 0.00070
               AND ABS(v.longitude - (-8.7728406)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:55580642 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Padre Sá Pereira', 'Esposende', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.5378447, -8.7812569, 'OPENSTREETMAP', 'OSM:WAY:55580642',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:55580642')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Padre Sá Pereira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5378447)) <= 0.00070
               AND ABS(v.longitude - (-8.7812569)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2791777423 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Esposende 2000', 'Esposende', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.5314746, -8.7835111, 'OPENSTREETMAP', 'OSM:NODE:2791777423',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2791777423')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Esposende 2000'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5314746)) <= 0.00070
               AND ABS(v.longitude - (-8.7835111)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154778182 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Foz do Cávado', 'Esposende', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5314225, -8.7832384, 'OPENSTREETMAP', 'OSM:WAY:154778182',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154778182')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Foz do Cávado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5314225)) <= 0.00070
               AND ABS(v.longitude - (-8.7832384)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:184829917 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Gemeses de baixo', 'Esposende', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.5228366, -8.7371800, 'OPENSTREETMAP', 'OSM:WAY:184829917',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:184829917')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Gemeses de baixo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5228366)) <= 0.00070
               AND ABS(v.longitude - (-8.7371800)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1005029151 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue Polidesportivo de Rio de Moinhos', 'Esposende', 'Braga', NULL,
       'Ténis;Futebol;Andebol', 'Campo desportivo', NULL,
       41.5712482, -8.7789641, 'OPENSTREETMAP', 'OSM:WAY:1005029151',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1005029151')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue Polidesportivo de Rio de Moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5712482)) <= 0.00070
               AND ABS(v.longitude - (-8.7789641)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1216250555 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park Esposende', 'Esposende', 'Braga', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.5340724, -8.7843297, 'OPENSTREETMAP', 'OSM:WAY:1216250555',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1216250555')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Esposende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park Esposende'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5340724)) <= 0.00070
               AND ABS(v.longitude - (-8.7843297)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108727655 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Ases de São Jorge', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4474791, -8.1682353, 'OPENSTREETMAP', 'OSM:WAY:108727655',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108727655')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Ases de São Jorge'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4474791)) <= 0.00070
               AND ABS(v.longitude - (-8.1682353)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:193795507 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Arões', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4563820, -8.2132161, 'OPENSTREETMAP', 'OSM:WAY:193795507',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:193795507')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Arões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4563820)) <= 0.00070
               AND ABS(v.longitude - (-8.2132161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:132159755 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Fareja', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4170753, -8.2249602, 'OPENSTREETMAP', 'OSM:WAY:132159755',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:132159755')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Fareja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4170753)) <= 0.00070
               AND ABS(v.longitude - (-8.2249602)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:190110940 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Arco Íris', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4694751, -8.1986878, 'OPENSTREETMAP', 'OSM:WAY:190110940',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:190110940')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Arco Íris'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4694751)) <= 0.00070
               AND ABS(v.longitude - (-8.1986878)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:133661525 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Operário Futebol Clube', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4389057, -8.1568240, 'OPENSTREETMAP', 'OSM:WAY:133661525',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:133661525')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Operário Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4389057)) <= 0.00070
               AND ABS(v.longitude - (-8.1568240)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:191703479 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Quinchães', 'Fafe', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4272041, -8.1414114, 'OPENSTREETMAP', 'OSM:WAY:191703479',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:191703479')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Quinchães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4272041)) <= 0.00070
               AND ABS(v.longitude - (-8.1414114)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186658604 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos Carvalhinhos', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4967176, -8.2090928, 'OPENSTREETMAP', 'OSM:WAY:186658604',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186658604')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos Carvalhinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4967176)) <= 0.00070
               AND ABS(v.longitude - (-8.2090928)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14029688008 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Central Gym', 'Fafe', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4495596, -8.1758427, 'OPENSTREETMAP', 'OSM:NODE:14029688008',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14029688008')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Central Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4495596)) <= 0.00070
               AND ABS(v.longitude - (-8.1758427)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186658612 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Fafe', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4470569, -8.1689966, 'OPENSTREETMAP', 'OSM:WAY:186658612',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186658612')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Fafe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4470569)) <= 0.00070
               AND ABS(v.longitude - (-8.1689966)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:90638653 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gimnodesportivo de Fafe', 'Fafe', 'Braga', NULL,
       'Andebol', 'Complexo desportivo', NULL,
       41.4493402, -8.1714438, 'OPENSTREETMAP', 'OSM:WAY:90638653',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:90638653')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gimnodesportivo de Fafe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4493402)) <= 0.00070
               AND ABS(v.longitude - (-8.1714438)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:355534175 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Club Mais', 'Fafe', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4396815, -8.1823679, 'OPENSTREETMAP', 'OSM:WAY:355534175',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:355534175')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Club Mais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4396815)) <= 0.00070
               AND ABS(v.longitude - (-8.1823679)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186658602 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Cultural e Recreativo de Golães', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4529342, -8.1991746, 'OPENSTREETMAP', 'OSM:WAY:186658602',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186658602')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Cultural e Recreativo de Golães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4529342)) <= 0.00070
               AND ABS(v.longitude - (-8.1991746)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:85701961 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Cultural Recreativo de Pardelhas', 'Fafe', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4595315, -8.1540461, 'OPENSTREETMAP', 'OSM:WAY:85701961',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:85701961')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Cultural Recreativo de Pardelhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4595315)) <= 0.00070
               AND ABS(v.longitude - (-8.1540461)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:90638647 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Municipal de Desportos', 'Fafe', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.4472408, -8.1690912, 'OPENSTREETMAP', 'OSM:WAY:90638647',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:90638647')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Municipal de Desportos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4472408)) <= 0.00070
               AND ABS(v.longitude - (-8.1690912)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:90638660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Fafe', 'Fafe', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.4480047, -8.1692067, 'OPENSTREETMAP', 'OSM:WAY:90638660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:90638660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fafe'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Fafe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4480047)) <= 0.00070
               AND ABS(v.longitude - (-8.1692067)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11930893634 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Nokaute', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4518054, -8.3103767, 'OPENSTREETMAP', 'OSM:NODE:11930893634',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11930893634')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Nokaute'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4518054)) <= 0.00070
               AND ABS(v.longitude - (-8.3103767)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12031382412 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Activus', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4684466, -8.2761738, 'OPENSTREETMAP', 'OSM:NODE:12031382412',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12031382412')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Activus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4684466)) <= 0.00070
               AND ABS(v.longitude - (-8.2761738)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:338806654 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Airão Santa Maria', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4542015, -8.4160420, 'OPENSTREETMAP', 'OSM:WAY:338806654',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:338806654')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Airão Santa Maria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4542015)) <= 0.00070
               AND ABS(v.longitude - (-8.4160420)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11930893629 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ALMA', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4513077, -8.3110949, 'OPENSTREETMAP', 'OSM:NODE:11930893629',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11930893629')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ALMA'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4513077)) <= 0.00070
               AND ABS(v.longitude - (-8.3110949)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1483977749 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Berço Academia', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5140433, -8.3428078, 'OPENSTREETMAP', 'OSM:WAY:1483977749',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1483977749')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Berço Academia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5140433)) <= 0.00070
               AND ABS(v.longitude - (-8.3428078)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:133160366 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 1', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4482624, -8.2821623, 'OPENSTREETMAP', 'OSM:WAY:133160366',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:133160366')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4482624)) <= 0.00070
               AND ABS(v.longitude - (-8.2821623)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:342027821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 2', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4486378, -8.2806212, 'OPENSTREETMAP', 'OSM:WAY:342027821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:342027821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4486378)) <= 0.00070
               AND ABS(v.longitude - (-8.2806212)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:130705819 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 3', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4488202, -8.2796958, 'OPENSTREETMAP', 'OSM:WAY:130705819',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:130705819')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4488202)) <= 0.00070
               AND ABS(v.longitude - (-8.2796958)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:334835446 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 4', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4489920, -8.2787570, 'OPENSTREETMAP', 'OSM:WAY:334835446',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:334835446')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 4'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4489920)) <= 0.00070
               AND ABS(v.longitude - (-8.2787570)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:130705820 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 5', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4491718, -8.2779712, 'OPENSTREETMAP', 'OSM:WAY:130705820',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:130705820')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 5'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4491718)) <= 0.00070
               AND ABS(v.longitude - (-8.2779712)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:130705821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 6', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4493985, -8.2768890, 'OPENSTREETMAP', 'OSM:WAY:130705821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:130705821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 6'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4493985)) <= 0.00070
               AND ABS(v.longitude - (-8.2768890)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147432809 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da ARD Calvos', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4071539, -8.2414513, 'OPENSTREETMAP', 'OSM:WAY:147432809',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147432809')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da ARD Calvos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4071539)) <= 0.00070
               AND ABS(v.longitude - (-8.2414513)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147431352 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol das Águias Negras de Tabuadelo', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4059410, -8.2842538, 'OPENSTREETMAP', 'OSM:WAY:147431352',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147431352')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol das Águias Negras de Tabuadelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4059410)) <= 0.00070
               AND ABS(v.longitude - (-8.2842538)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147431358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da União Desportiva de Polvoreira', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4091137, -8.2923707, 'OPENSTREETMAP', 'OSM:WAY:147431358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147431358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da União Desportiva de Polvoreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4091137)) <= 0.00070
               AND ABS(v.longitude - (-8.2923707)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1485149176 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Rendufe', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4775854, -8.2339736, 'OPENSTREETMAP', 'OSM:WAY:1485149176',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1485149176')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Rendufe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4775854)) <= 0.00070
               AND ABS(v.longitude - (-8.2339736)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:987856560 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Santo Estêvão', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5038474, -8.3171068, 'OPENSTREETMAP', 'OSM:WAY:987856560',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:987856560')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Santo Estêvão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5038474)) <= 0.00070
               AND ABS(v.longitude - (-8.3171068)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1073951019 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do GDRC de Aldão', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4609377, -8.2700422, 'OPENSTREETMAP', 'OSM:WAY:1073951019',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1073951019')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do GDRC de Aldão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4609377)) <= 0.00070
               AND ABS(v.longitude - (-8.2700422)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155135370 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Salgueiral', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4315037, -8.3046075, 'OPENSTREETMAP', 'OSM:WAY:155135370',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155135370')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Salgueiral'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4315037)) <= 0.00070
               AND ABS(v.longitude - (-8.3046075)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
