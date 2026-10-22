-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:5147621539 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'AGD Sports Academy', 'Águeda', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.5726205, -8.4516888, 'OPENSTREETMAP', 'OSM:NODE:5147621539',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5147621539')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('AGD Sports Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5726205)) <= 0.00070
               AND ABS(v.longitude - (-8.4516888)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:419523249 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Aguada de Cima', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5211139, -8.4168451, 'OPENSTREETMAP', 'OSM:WAY:419523249',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:419523249')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Aguada de Cima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5211139)) <= 0.00070
               AND ABS(v.longitude - (-8.4168451)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:55314585 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube de Fermentelos', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5647788, -8.5297785, 'OPENSTREETMAP', 'OSM:WAY:55314585',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:55314585')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube de Fermentelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5647788)) <= 0.00070
               AND ABS(v.longitude - (-8.5297785)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7249637582 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '3B FitnessClub', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6867769, -8.4811158, 'OPENSTREETMAP', 'OSM:NODE:7249637582',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7249637582')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('3B FitnessClub'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6867769)) <= 0.00070
               AND ABS(v.longitude - (-8.4811158)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7272795759 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'T-Fit Estúdio de Pilates', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6940478, -8.4820446, 'OPENSTREETMAP', 'OSM:NODE:7272795759',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7272795759')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('T-Fit Estúdio de Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6940478)) <= 0.00070
               AND ABS(v.longitude - (-8.4820446)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1306911303 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Anadia', 'Anadia', 'Aveiro', NULL,
       'BMX;Ginástica;Judo', 'Complexo desportivo', NULL,
       40.4791024, -8.4753451, 'OPENSTREETMAP', 'OSM:WAY:1306911303',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1306911303')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Anadia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4791024)) <= 0.00070
               AND ABS(v.longitude - (-8.4753451)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003684821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Relvas', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.9350634, -8.3531433, 'OPENSTREETMAP', 'OSM:WAY:1003684821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003684821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Relvas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9350634)) <= 0.00070
               AND ABS(v.longitude - (-8.3531433)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1799575707 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clínica Izzifit', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Complexo desportivo', NULL,
       40.6437955, -8.6464746, 'OPENSTREETMAP', 'OSM:NODE:1799575707',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1799575707')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clínica Izzifit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6437955)) <= 0.00070
               AND ABS(v.longitude - (-8.6464746)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1418752573 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Ténis de Mesa de Oliveirinha', 'Aveiro', 'Aveiro', NULL,
       'Ténis de mesa', 'Campo desportivo', NULL,
       40.6018183, -8.6058780, 'OPENSTREETMAP', 'OSM:WAY:1418752573',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1418752573')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Ténis de Mesa de Oliveirinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6018183)) <= 0.00070
               AND ABS(v.longitude - (-8.6058780)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1418736904 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'First Round', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6276725, -8.6398466, 'OPENSTREETMAP', 'OSM:WAY:1418736904',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1418736904')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('First Round'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6276725)) <= 0.00070
               AND ABS(v.longitude - (-8.6398466)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13914798467 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Focus Fitness', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6409654, -8.6377040, 'OPENSTREETMAP', 'OSM:NODE:13914798467',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13914798467')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Focus Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6409654)) <= 0.00070
               AND ABS(v.longitude - (-8.6377040)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:548085580 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Be Fit', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6487076, -8.5974467, 'OPENSTREETMAP', 'OSM:WAY:548085580',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:548085580')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Be Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6487076)) <= 0.00070
               AND ABS(v.longitude - (-8.5974467)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4514446389 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Knock-Out Health Club', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6258163, -8.6473605, 'OPENSTREETMAP', 'OSM:NODE:4514446389',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4514446389')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Knock-Out Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6258163)) <= 0.00070
               AND ABS(v.longitude - (-8.6473605)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1700009108 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Power Center Knock-Out', 'Aveiro', 'Aveiro', NULL,
       'Boxe', 'Ginásio', NULL,
       40.6289894, -8.6423376, 'OPENSTREETMAP', 'OSM:NODE:1700009108',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1700009108')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Power Center Knock-Out'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6289894)) <= 0.00070
               AND ABS(v.longitude - (-8.6423376)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:693930989 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube de Aveiro', 'Aveiro', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.6450342, -8.6503061, 'OPENSTREETMAP', 'OSM:WAY:693930989',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:693930989')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube de Aveiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6450342)) <= 0.00070
               AND ABS(v.longitude - (-8.6503061)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1095650696 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Balneário Marinho', 'Espinho', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0110098, -8.6451667, 'OPENSTREETMAP', 'OSM:WAY:1095650696',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1095650696')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Balneário Marinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0110098)) <= 0.00070
               AND ABS(v.longitude - (-8.6451667)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1461308014 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Caçadores da Costa Verde', 'Espinho', 'Aveiro', NULL,
       'Tiro', 'Campo desportivo', NULL,
       40.9953625, -8.6181410, 'OPENSTREETMAP', 'OSM:WAY:1461308014',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1461308014')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Caçadores da Costa Verde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9953625)) <= 0.00070
               AND ABS(v.longitude - (-8.6181410)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:378235181 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Paramos', 'Espinho', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.9742202, -8.6201350, 'OPENSTREETMAP', 'OSM:WAY:378235181',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:378235181')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Paramos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9742202)) <= 0.00070
               AND ABS(v.longitude - (-8.6201350)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1219066745 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Silvalde', 'Espinho', 'Aveiro', NULL,
       'Futebol;Futsal', 'Complexo desportivo', NULL,
       40.9926268, -8.6235774, 'OPENSTREETMAP', 'OSM:WAY:1219066745',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1219066745')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Silvalde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9926268)) <= 0.00070
               AND ABS(v.longitude - (-8.6235774)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:100668764 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Ténis de Espinho', 'Espinho', 'Aveiro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.9983382, -8.6264853, 'OPENSTREETMAP', 'OSM:WAY:100668764',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:100668764')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Ténis de Espinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9983382)) <= 0.00070
               AND ABS(v.longitude - (-8.6264853)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1095650697 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Solário Atlântico', 'Espinho', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0101619, -8.6453100, 'OPENSTREETMAP', 'OSM:WAY:1095650697',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1095650697')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Solário Atlântico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0101619)) <= 0.00070
               AND ABS(v.longitude - (-8.6453100)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12209452152 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Progresso Fitness Academy', 'Espinho', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0038901, -8.6432083, 'OPENSTREETMAP', 'OSM:NODE:12209452152',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12209452152')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Progresso Fitness Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0038901)) <= 0.00070
               AND ABS(v.longitude - (-8.6432083)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4033921083 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'WowFit', 'Ílhavo', 'Aveiro', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       40.6032946, -8.6753567, 'OPENSTREETMAP', 'OSM:NODE:4033921083',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4033921083')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('WowFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6032946)) <= 0.00070
               AND ABS(v.longitude - (-8.6753567)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4861554435 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.8386398, -8.4798395, 'OPENSTREETMAP', 'OSM:NODE:4861554435',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4861554435')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8386398)) <= 0.00070
               AND ABS(v.longitude - (-8.4798395)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3659546479 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GimnoFange - Health Club', 'Ovar', 'Aveiro', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       40.8619031, -8.6176227, 'OPENSTREETMAP', 'OSM:NODE:3659546479',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3659546479')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GimnoFange - Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8619031)) <= 0.00070
               AND ABS(v.longitude - (-8.6176227)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12668342932 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ovar Padel - Clube & Academia', 'Ovar', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.8676915, -8.6141665, 'OPENSTREETMAP', 'OSM:NODE:12668342932',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12668342932')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ovar Padel - Clube & Academia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8676915)) <= 0.00070
               AND ABS(v.longitude - (-8.6141665)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4029561246 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Bombeiros Voluntários de Esmoriz', 'Ovar', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.9585156, -8.6322031, 'OPENSTREETMAP', 'OSM:NODE:4029561246',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4029561246')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Bombeiros Voluntários de Esmoriz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9585156)) <= 0.00070
               AND ABS(v.longitude - (-8.6322031)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11405683384 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Active', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9264499, -8.5521636, 'OPENSTREETMAP', 'OSM:NODE:11405683384',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11405683384')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Active'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9264499)) <= 0.00070
               AND ABS(v.longitude - (-8.5521636)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:175349181 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo de Fornos', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.9113676, -8.5206877, 'OPENSTREETMAP', 'OSM:WAY:175349181',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:175349181')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo de Fornos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9113676)) <= 0.00070
               AND ABS(v.longitude - (-8.5206877)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1211961522 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crossfit Feira', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9204000, -8.5700375, 'OPENSTREETMAP', 'OSM:WAY:1211961522',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1211961522')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crossfit Feira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9204000)) <= 0.00070
               AND ABS(v.longitude - (-8.5700375)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11025269332 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ems - Ginásio, Electro-Estimulação', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9253489, -8.5479817, 'OPENSTREETMAP', 'OSM:NODE:11025269332',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11025269332')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ems - Ginásio, Electro-Estimulação'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9253489)) <= 0.00070
               AND ABS(v.longitude - (-8.5479817)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:53349207 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Marcolino de Castro', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.9267150, -8.5459420, 'OPENSTREETMAP', 'OSM:WAY:53349207',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:53349207')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Marcolino de Castro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9267150)) <= 0.00070
               AND ABS(v.longitude - (-8.5459420)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10894056442 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estúdio do Movimento', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9256974, -8.5452024, 'OPENSTREETMAP', 'OSM:NODE:10894056442',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10894056442')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estúdio do Movimento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9256974)) <= 0.00070
               AND ABS(v.longitude - (-8.5452024)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11262711870 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Feel Free Health Club', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9237442, -8.5513738, 'OPENSTREETMAP', 'OSM:NODE:11262711870',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11262711870')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Feel Free Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9237442)) <= 0.00070
               AND ABS(v.longitude - (-8.5513738)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13298255951 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Homyoga', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9320686, -8.5365052, 'OPENSTREETMAP', 'OSM:NODE:13298255951',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13298255951')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Homyoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9320686)) <= 0.00070
               AND ABS(v.longitude - (-8.5365052)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9991203665 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'HomYoga', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginástica', 'Ginásio', NULL,
       40.9245696, -8.5421685, 'OPENSTREETMAP', 'OSM:NODE:9991203665',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9991203665')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('HomYoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9245696)) <= 0.00070
               AND ABS(v.longitude - (-8.5421685)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9998287863 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Life is Yoga', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9277860, -8.5461108, 'OPENSTREETMAP', 'OSM:NODE:9998287863',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9998287863')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Life is Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9277860)) <= 0.00070
               AND ABS(v.longitude - (-8.5461108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9580835070 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Limup', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9399624, -8.5385283, 'OPENSTREETMAP', 'OSM:NODE:9580835070',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9580835070')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Limup'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9399624)) <= 0.00070
               AND ABS(v.longitude - (-8.5385283)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10540171509 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lotus club', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9286668, -8.5460343, 'OPENSTREETMAP', 'OSM:NODE:10540171509',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10540171509')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lotus club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9286668)) <= 0.00070
               AND ABS(v.longitude - (-8.5460343)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1279484492 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'M2 Training Studio', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9695617, -8.5663917, 'OPENSTREETMAP', 'OSM:WAY:1279484492',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1279484492')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('M2 Training Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9695617)) <= 0.00070
               AND ABS(v.longitude - (-8.5663917)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1080102365 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Moove - Health and Sports', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Padel', 'Complexo desportivo', NULL,
       40.9261276, -8.5403360, 'OPENSTREETMAP', 'OSM:WAY:1080102365',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1080102365')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Moove - Health and Sports'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9261276)) <= 0.00070
               AND ABS(v.longitude - (-8.5403360)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12155184290 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Padel Plus', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Padel', 'Complexo desportivo', NULL,
       40.9307306, -8.5029334, 'OPENSTREETMAP', 'OSM:NODE:12155184290',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12155184290')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Padel Plus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9307306)) <= 0.00070
               AND ABS(v.longitude - (-8.5029334)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:356678638 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo de Argoncilhe', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Complexo desportivo', NULL,
       41.0226893, -8.5382058, 'OPENSTREETMAP', 'OSM:WAY:356678638',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:356678638')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo de Argoncilhe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0226893)) <= 0.00070
               AND ABS(v.longitude - (-8.5382058)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147933352 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo de Paços de Brandão', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.9725071, -8.5944085, 'OPENSTREETMAP', 'OSM:WAY:147933352',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147933352')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo de Paços de Brandão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9725071)) <= 0.00070
               AND ABS(v.longitude - (-8.5944085)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:474261810 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Physical', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9199687, -8.5715077, 'OPENSTREETMAP', 'OSM:WAY:474261810',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:474261810')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Physical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9199687)) <= 0.00070
               AND ABS(v.longitude - (-8.5715077)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11735346497 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pilates by Susana Pereira', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9231036, -8.5585239, 'OPENSTREETMAP', 'OSM:NODE:11735346497',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11735346497')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pilates by Susana Pereira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9231036)) <= 0.00070
               AND ABS(v.longitude - (-8.5585239)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:686280549 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Santa Maria de Lamas', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9769420, -8.5722260, 'OPENSTREETMAP', 'OSM:WAY:686280549',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:686280549')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Santa Maria de Lamas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9769420)) <= 0.00070
               AND ABS(v.longitude - (-8.5722260)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1327554218 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Lourosa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       40.9893682, -8.5387267, 'OPENSTREETMAP', 'OSM:NODE:1327554218',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1327554218')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Lourosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9893682)) <= 0.00070
               AND ABS(v.longitude - (-8.5387267)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11278131105 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Shape Up', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9218397, -8.5700367, 'OPENSTREETMAP', 'OSM:NODE:11278131105',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11278131105')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Shape Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9218397)) <= 0.00070
               AND ABS(v.longitude - (-8.5700367)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11219479048 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studio 21', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9675622, -8.5363766, 'OPENSTREETMAP', 'OSM:NODE:11219479048',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11219479048')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studio 21'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9675622)) <= 0.00070
               AND ABS(v.longitude - (-8.5363766)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12870147918 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Zenith Padel Club', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Padel;Pickleball', 'Campo desportivo', NULL,
       40.9710125, -8.5219731, 'OPENSTREETMAP', 'OSM:NODE:12870147918',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12870147918')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Zenith Padel Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9710125)) <= 0.00070
               AND ABS(v.longitude - (-8.5219731)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:673156469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Paulo Pinto', 'São João da Madeira', 'Aveiro', NULL,
       'Basquetebol;Natação', 'Complexo desportivo', NULL,
       40.8991115, -8.4960568, 'OPENSTREETMAP', 'OSM:WAY:673156469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:673156469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Paulo Pinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8991115)) <= 0.00070
               AND ABS(v.longitude - (-8.4960568)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12954194068 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gimnofísico', 'São João da Madeira', 'Aveiro', NULL,
       'Ginásio;Ginástica;Natação', 'Ginásio', NULL,
       40.8952914, -8.4908019, 'OPENSTREETMAP', 'OSM:NODE:12954194068',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12954194068')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gimnofísico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8952914)) <= 0.00070
               AND ABS(v.longitude - (-8.4908019)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1191676319 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lux Health Club', 'São João da Madeira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9072562, -8.4798261, 'OPENSTREETMAP', 'OSM:WAY:1191676319',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1191676319')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lux Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9072562)) <= 0.00070
               AND ABS(v.longitude - (-8.4798261)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13159944822 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oásis Training Studio', 'São João da Madeira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9011959, -8.4918153, 'OPENSTREETMAP', 'OSM:NODE:13159944822',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13159944822')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oásis Training Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9011959)) <= 0.00070
               AND ABS(v.longitude - (-8.4918153)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13462209214 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio New Life', 'Vagos', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.5272654, -8.6793577, 'OPENSTREETMAP', 'OSM:NODE:13462209214',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13462209214')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio New Life'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5272654)) <= 0.00070
               AND ABS(v.longitude - (-8.6793577)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154067459 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Atlética Macinhatense', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6536451, -8.4423770, 'OPENSTREETMAP', 'OSM:WAY:154067459',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154067459')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Atlética Macinhatense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6536451)) <= 0.00070
               AND ABS(v.longitude - (-8.4423770)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:329581598 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva de Travassô', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.5943414, -8.4829108, 'OPENSTREETMAP', 'OSM:WAY:329581598',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:329581598')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva de Travassô'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5943414)) <= 0.00070
               AND ABS(v.longitude - (-8.4829108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99245022 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva Valonguense', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6207877, -8.4116700, 'OPENSTREETMAP', 'OSM:WAY:99245022',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99245022')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva Valonguense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6207877)) <= 0.00070
               AND ABS(v.longitude - (-8.4116700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:98459912 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futbol ASM', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.5416063, -8.4130205, 'OPENSTREETMAP', 'OSM:WAY:98459912',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:98459912')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futbol ASM'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5416063)) <= 0.00070
               AND ABS(v.longitude - (-8.4130205)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:189198697 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futsal da Estga', 'Águeda', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.5737169, -8.4450341, 'OPENSTREETMAP', 'OSM:WAY:189198697',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:189198697')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futsal da Estga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5737169)) <= 0.00070
               AND ABS(v.longitude - (-8.4450341)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1396002603 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de treinos RDA', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5620496, -8.4399906, 'OPENSTREETMAP', 'OSM:WAY:1396002603',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1396002603')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de treinos RDA'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5620496)) <= 0.00070
               AND ABS(v.longitude - (-8.4399906)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:330924728 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Projeto Jovem', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5698234, -8.5271006, 'OPENSTREETMAP', 'OSM:WAY:330924728',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:330924728')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Projeto Jovem'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5698234)) <= 0.00070
               AND ABS(v.longitude - (-8.5271006)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:98543937 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Recardães', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5563375, -8.4507734, 'OPENSTREETMAP', 'OSM:WAY:98543937',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:98543937')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Recardães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5563375)) <= 0.00070
               AND ABS(v.longitude - (-8.4507734)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:689525947 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Polidesportivo de Recardães', 'Águeda', 'Aveiro', NULL,
       'Futebol;Ténis;Basquetebol;Andebol', 'Campo desportivo', NULL,
       40.5617444, -8.4658481, 'OPENSTREETMAP', 'OSM:WAY:689525947',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:689525947')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Polidesportivo de Recardães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5617444)) <= 0.00070
               AND ABS(v.longitude - (-8.4658481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6919442676 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Náutico Municipal de Águeda', 'Águeda', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.5698921, -8.4470265, 'OPENSTREETMAP', 'OSM:NODE:6919442676',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6919442676')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Náutico Municipal de Águeda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5698921)) <= 0.00070
               AND ABS(v.longitude - (-8.4470265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:88604374 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Águeda', 'Águeda', 'Aveiro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.5615135, -8.4384025, 'OPENSTREETMAP', 'OSM:WAY:88604374',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:88604374')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Águeda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5615135)) <= 0.00070
               AND ABS(v.longitude - (-8.4384025)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:91863453 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Court n1', 'Águeda', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.5612828, -8.4385724, 'OPENSTREETMAP', 'OSM:WAY:91863453',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:91863453')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Court n1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5612828)) <= 0.00070
               AND ABS(v.longitude - (-8.4385724)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99245023 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Bastos Xavier', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.6205230, -8.4114721, 'OPENSTREETMAP', 'OSM:WAY:99245023',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99245023')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Bastos Xavier'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6205230)) <= 0.00070
               AND ABS(v.longitude - (-8.4114721)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:88604380 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Águeda', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.5623416, -8.4416882, 'OPENSTREETMAP', 'OSM:WAY:88604380',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:88604380')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Águeda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5623416)) <= 0.00070
               AND ABS(v.longitude - (-8.4416882)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:91863339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão CT Águeda', 'Águeda', 'Aveiro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.5612869, -8.4381193, 'OPENSTREETMAP', 'OSM:WAY:91863339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:91863339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão CT Águeda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5612869)) <= 0.00070
               AND ABS(v.longitude - (-8.4381193)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1704539702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão do C.R.P.B.', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.5355331, -8.3671186, 'OPENSTREETMAP', 'OSM:NODE:1704539702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1704539702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão do C.R.P.B.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5355331)) <= 0.00070
               AND ABS(v.longitude - (-8.3671186)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:689532321 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Centro Social e Paroquial de Recardães', 'Águeda', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.5621015, -8.4652552, 'OPENSTREETMAP', 'OSM:WAY:689532321',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:689532321')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Centro Social e Paroquial de Recardães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5621015)) <= 0.00070
               AND ABS(v.longitude - (-8.4652552)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154067461 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Macinhatense', 'Águeda', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.6536704, -8.4434859, 'OPENSTREETMAP', 'OSM:WAY:154067461',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154067461')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Macinhatense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6536704)) <= 0.00070
               AND ABS(v.longitude - (-8.4434859)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:86353754 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Águeda', 'Águeda', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.5771929, -8.4436325, 'OPENSTREETMAP', 'OSM:WAY:86353754',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:86353754')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Águeda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5771929)) <= 0.00070
               AND ABS(v.longitude - (-8.4436325)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:88604363 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Recreio Desportivo de Águeda', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5623450, -8.4416819, 'OPENSTREETMAP', 'OSM:WAY:88604363',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:88604363')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Recreio Desportivo de Águeda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5623450)) <= 0.00070
               AND ABS(v.longitude - (-8.4416819)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103082197 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sintético do Areeiro', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5551170, -8.4331323, 'OPENSTREETMAP', 'OSM:WAY:103082197',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103082197')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sintético do Areeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5551170)) <= 0.00070
               AND ABS(v.longitude - (-8.4331323)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154067463 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sintético Macinhatense', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6537774, -8.4431986, 'OPENSTREETMAP', 'OSM:WAY:154067463',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154067463')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sintético Macinhatense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6537774)) <= 0.00070
               AND ABS(v.longitude - (-8.4431986)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003547995 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva Mourisquense', 'Águeda', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6152625, -8.4495460, 'OPENSTREETMAP', 'OSM:WAY:1003547995',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003547995')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Águeda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva Mourisquense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6152625)) <= 0.00070
               AND ABS(v.longitude - (-8.4495460)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:227946145 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo José Oliveira Santos', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6535920, -8.5447202, 'OPENSTREETMAP', 'OSM:WAY:227946145',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:227946145')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo José Oliveira Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6535920)) <= 0.00070
               AND ABS(v.longitude - (-8.5447202)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119396223 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal da Branca', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7489231, -8.4855838, 'OPENSTREETMAP', 'OSM:WAY:119396223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119396223')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal da Branca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7489231)) <= 0.00070
               AND ABS(v.longitude - (-8.4855838)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:422271163 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory Albergaria-a-Velha', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6902552, -8.4781590, 'OPENSTREETMAP', 'OSM:WAY:422271163',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:422271163')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory Albergaria-a-Velha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6902552)) <= 0.00070
               AND ABS(v.longitude - (-8.4781590)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7267031121 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Rainha', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.7205212, -8.4858105, 'OPENSTREETMAP', 'OSM:NODE:7267031121',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7267031121')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Rainha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7205212)) <= 0.00070
               AND ABS(v.longitude - (-8.4858105)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:191844491 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mini-campo da Vila das Laranjeiras', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6860062, -8.4706535, 'OPENSTREETMAP', 'OSM:WAY:191844491',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:191844491')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mini-campo da Vila das Laranjeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6860062)) <= 0.00070
               AND ABS(v.longitude - (-8.4706535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181573207 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Alquerubim', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6292362, -8.5034042, 'OPENSTREETMAP', 'OSM:WAY:181573207',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181573207')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Alquerubim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6292362)) <= 0.00070
               AND ABS(v.longitude - (-8.5034042)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:227946146 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Jogos Quinta do Agro', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6376538, -8.5482382, 'OPENSTREETMAP', 'OSM:WAY:227946146',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:227946146')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Jogos Quinta do Agro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6376538)) <= 0.00070
               AND ABS(v.longitude - (-8.5482382)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119065797 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Branca', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.7486258, -8.4869001, 'OPENSTREETMAP', 'OSM:WAY:119065797',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119065797')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Branca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7486258)) <= 0.00070
               AND ABS(v.longitude - (-8.4869001)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168741447 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Albergaria-a-Velha', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.6866169, -8.4806488, 'OPENSTREETMAP', 'OSM:WAY:168741447',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168741447')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Albergaria-a-Velha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6866169)) <= 0.00070
               AND ABS(v.longitude - (-8.4806488)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:227946148 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de São João de Loure', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.6385584, -8.5473710, 'OPENSTREETMAP', 'OSM:WAY:227946148',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:227946148')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de São João de Loure'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6385584)) <= 0.00070
               AND ABS(v.longitude - (-8.5473710)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:227941212 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Valmaior', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7023543, -8.4544374, 'OPENSTREETMAP', 'OSM:WAY:227941212',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:227941212')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Valmaior'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7023543)) <= 0.00070
               AND ABS(v.longitude - (-8.4544374)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168742913 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sede Clube de Albergaria', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6860442, -8.4810976, 'OPENSTREETMAP', 'OSM:WAY:168742913',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168742913')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sede Clube de Albergaria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6860442)) <= 0.00070
               AND ABS(v.longitude - (-8.4810976)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7249639827 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva e Cultuiral de Mouquim', 'Albergaria-a-Velha', 'Aveiro', NULL,
       'Ténis de mesa;Futebol', 'Campo desportivo', NULL,
       40.6994912, -8.4381400, 'OPENSTREETMAP', 'OSM:NODE:7249639827',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7249639827')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albergaria-a-Velha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva e Cultuiral de Mouquim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6994912)) <= 0.00070
               AND ABS(v.longitude - (-8.4381400)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1931056589 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Anadia Futebol Clube', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.4356798, -8.4422768, 'OPENSTREETMAP', 'OSM:NODE:1931056589',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1931056589')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Anadia Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4356798)) <= 0.00070
               AND ABS(v.longitude - (-8.4422768)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:579726737 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Anadia Futebol Clube', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4277303, -8.4401243, 'OPENSTREETMAP', 'OSM:WAY:579726737',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:579726737')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Anadia Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4277303)) <= 0.00070
               AND ABS(v.longitude - (-8.4401243)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:247919196 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva e Cultural de Vila Nova de Monsarros', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4023892, -8.3953609, 'OPENSTREETMAP', 'OSM:WAY:247919196',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:247919196')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva e Cultural de Vila Nova de Monsarros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4023892)) <= 0.00070
               AND ABS(v.longitude - (-8.3953609)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003684823 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva Recreativa e Cultural da Ribeira - Azenha', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4487231, -8.5508674, 'OPENSTREETMAP', 'OSM:WAY:1003684823',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003684823')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva Recreativa e Cultural da Ribeira - Azenha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4487231)) <= 0.00070
               AND ABS(v.longitude - (-8.5508674)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243058514 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Afonso Ramos Bandarra', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4101678, -8.4482644, 'OPENSTREETMAP', 'OSM:WAY:243058514',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243058514')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Afonso Ramos Bandarra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4101678)) <= 0.00070
               AND ABS(v.longitude - (-8.4482644)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182197382 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de 11', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4262936, -8.4395157, 'OPENSTREETMAP', 'OSM:WAY:182197382',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182197382')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de 11'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4262936)) <= 0.00070
               AND ABS(v.longitude - (-8.4395157)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243096012 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Mogofores', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4438218, -8.4623535, 'OPENSTREETMAP', 'OSM:WAY:243096012',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243096012')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Mogofores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4438218)) <= 0.00070
               AND ABS(v.longitude - (-8.4623535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243337412 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol dos Olivais, Couvelha', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4404343, -8.5300719, 'OPENSTREETMAP', 'OSM:WAY:243337412',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243337412')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol dos Olivais, Couvelha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4404343)) <= 0.00070
               AND ABS(v.longitude - (-8.5300719)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185289214 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Golfe da Curia', 'Anadia', 'Aveiro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       40.4216804, -8.4592713, 'OPENSTREETMAP', 'OSM:WAY:185289214',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185289214')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Golfe da Curia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4216804)) <= 0.00070
               AND ABS(v.longitude - (-8.4592713)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:207180013 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo José Maria Mariz Silva', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4609931, -8.4535634, 'OPENSTREETMAP', 'OSM:WAY:207180013',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:207180013')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo José Maria Mariz Silva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4609931)) <= 0.00070
               AND ABS(v.longitude - (-8.4535634)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182197405 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Tenis', 'Anadia', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.4291462, -8.4410871, 'OPENSTREETMAP', 'OSM:WAY:182197405',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182197405')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de Tenis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4291462)) <= 0.00070
               AND ABS(v.longitude - (-8.4410871)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:256455007 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Pequito Rebelo', 'Anadia', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.4362575, -8.4328023, 'OPENSTREETMAP', 'OSM:WAY:256455007',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:256455007')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Pequito Rebelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4362575)) <= 0.00070
               AND ABS(v.longitude - (-8.4328023)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182750205 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Moita Rugby Clube da Bairrada', 'Anadia', 'Aveiro', NULL,
       'Rugby', 'Campo desportivo', NULL,
       40.4428836, -8.4026700, 'OPENSTREETMAP', 'OSM:WAY:182750205',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182750205')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Moita Rugby Clube da Bairrada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4428836)) <= 0.00070
               AND ABS(v.longitude - (-8.4026700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:256457781 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão dos Desportos de Anadia', 'Anadia', 'Aveiro', NULL,
       'Squash;Basquetebol', 'Complexo desportivo', NULL,
       40.4261163, -8.4379158, 'OPENSTREETMAP', 'OSM:WAY:256457781',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:256457781')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão dos Desportos de Anadia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4261163)) <= 0.00070
               AND ABS(v.longitude - (-8.4379158)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1306911311 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de BMX de Anadia', 'Anadia', 'Aveiro', NULL,
       'BMX', 'Complexo desportivo', NULL,
       40.4783018, -8.4753569, 'OPENSTREETMAP', 'OSM:WAY:1306911311',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1306911311')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Anadia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de BMX de Anadia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4783018)) <= 0.00070
               AND ABS(v.longitude - (-8.4753569)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12729308066 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ativa-te', 'Arouca', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9395631, -8.3989832, 'OPENSTREETMAP', 'OSM:NODE:12729308066',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12729308066')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ativa-te'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9395631)) <= 0.00070
               AND ABS(v.longitude - (-8.3989832)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:588239812 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Portelada', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9666741, -8.3895834, 'OPENSTREETMAP', 'OSM:WAY:588239812',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:588239812')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Portelada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9666741)) <= 0.00070
               AND ABS(v.longitude - (-8.3895834)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:296071464 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Santa Maria do Monte', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9123396, -8.2787731, 'OPENSTREETMAP', 'OSM:WAY:296071464',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:296071464')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Santa Maria do Monte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9123396)) <= 0.00070
               AND ABS(v.longitude - (-8.2787731)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:117967435 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Tropeço (Sequeiros)', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9401589, -8.3115036, 'OPENSTREETMAP', 'OSM:WAY:117967435',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:117967435')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Tropeço (Sequeiros)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9401589)) <= 0.00070
               AND ABS(v.longitude - (-8.3115036)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:588243701 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Cruzeiro', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9400020, -8.4142232, 'OPENSTREETMAP', 'OSM:WAY:588243701',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:588243701')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Cruzeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9400020)) <= 0.00070
               AND ABS(v.longitude - (-8.4142232)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:588242817 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Resumil - União Desportiva de Fermedo', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9553792, -8.4246755, 'OPENSTREETMAP', 'OSM:WAY:588242817',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:588242817')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Resumil - União Desportiva de Fermedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9553792)) <= 0.00070
               AND ABS(v.longitude - (-8.4246755)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:858869858 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Sinja - Grupo Cult. e Recreativo de Rossas', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9074981, -8.3080719, 'OPENSTREETMAP', 'OSM:WAY:858869858',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:858869858')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Sinja - Grupo Cult. e Recreativo de Rossas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9074981)) <= 0.00070
               AND ABS(v.longitude - (-8.3080719)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1313735632 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Vila Cova', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', 'OUTDOOR',
       40.9816103, -8.2295106, 'OPENSTREETMAP', 'OSM:WAY:1313735632',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1313735632')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Vila Cova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9816103)) <= 0.00070
               AND ABS(v.longitude - (-8.2295106)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003563524 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural e Recreativo de Vila Viçosa', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0036031, -8.2160358, 'OPENSTREETMAP', 'OSM:WAY:1003563524',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003563524')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural e Recreativo de Vila Viçosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0036031)) <= 0.00070
               AND ABS(v.longitude - (-8.2160358)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1001270466 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Reinaldo Noronha', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.9616055, -8.1680244, 'OPENSTREETMAP', 'OSM:WAY:1001270466',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1001270466')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Reinaldo Noronha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9616055)) <= 0.00070
               AND ABS(v.longitude - (-8.1680244)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:445117204 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Arouca', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9329046, -8.2503213, 'OPENSTREETMAP', 'OSM:WAY:445117204',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:445117204')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Arouca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9329046)) <= 0.00070
               AND ABS(v.longitude - (-8.2503213)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:271036582 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Santa Cruz de Alvarenga', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9616408, -8.1682404, 'OPENSTREETMAP', 'OSM:WAY:271036582',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:271036582')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Santa Cruz de Alvarenga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9616408)) <= 0.00070
               AND ABS(v.longitude - (-8.1682404)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:853763873 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Escariz', 'Arouca', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.9441491, -8.4063635, 'OPENSTREETMAP', 'OSM:WAY:853763873',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:853763873')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Escariz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9441491)) <= 0.00070
               AND ABS(v.longitude - (-8.4063635)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:407796193 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva Mansores', 'Arouca', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9354562, -8.3534426, 'OPENSTREETMAP', 'OSM:WAY:407796193',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:407796193')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva Mansores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9354562)) <= 0.00070
               AND ABS(v.longitude - (-8.3534426)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1004057762 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Recreativa e Cultural da Freguesia de Oliverinha', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6010764, -8.6070407, 'OPENSTREETMAP', 'OSM:WAY:1004057762',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1004057762')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Recreativa e Cultural da Freguesia de Oliverinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6010764)) <= 0.00070
               AND ABS(v.longitude - (-8.6070407)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12613073441 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be-Fit', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6286777, -8.6447437, 'OPENSTREETMAP', 'OSM:NODE:12613073441',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12613073441')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be-Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6286777)) <= 0.00070
               AND ABS(v.longitude - (-8.6447437)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1699750211 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Box 3810', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6292306, -8.6420828, 'OPENSTREETMAP', 'OSM:NODE:1699750211',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1699750211')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Box 3810'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6292306)) <= 0.00070
               AND ABS(v.longitude - (-8.6420828)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:146492597 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol da escola', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6451517, -8.6329885, 'OPENSTREETMAP', 'OSM:WAY:146492597',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:146492597')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol da escola'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6451517)) <= 0.00070
               AND ABS(v.longitude - (-8.6329885)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1005374617 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo  de futebol do Barroca', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5748974, -8.5857641, 'OPENSTREETMAP', 'OSM:WAY:1005374617',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1005374617')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo  de futebol do Barroca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5748974)) <= 0.00070
               AND ABS(v.longitude - (-8.5857641)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196294170 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de ténis da urbanização das Azenhas de Baixo', 'Aveiro', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.6297935, -8.5946798, 'OPENSTREETMAP', 'OSM:WAY:196294170',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196294170')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de ténis da urbanização das Azenhas de Baixo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6297935)) <= 0.00070
               AND ABS(v.longitude - (-8.5946798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:246322916 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis - Urbanização Chave', 'Aveiro', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.6289975, -8.6523757, 'OPENSTREETMAP', 'OSM:WAY:246322916',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:246322916')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis - Urbanização Chave'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6289975)) <= 0.00070
               AND ABS(v.longitude - (-8.6523757)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:190504642 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de treinos do Eixense', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6204881, -8.5759598, 'OPENSTREETMAP', 'OSM:WAY:190504642',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:190504642')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de treinos do Eixense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6204881)) <= 0.00070
               AND ABS(v.longitude - (-8.5759598)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3419290959 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol Acrema', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.6656086, -8.6355033, 'OPENSTREETMAP', 'OSM:NODE:3419290959',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3419290959')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol Acrema'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6656086)) <= 0.00070
               AND ABS(v.longitude - (-8.6355033)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181372228 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campus 7', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6334646, -8.6579934, 'OPENSTREETMAP', 'OSM:WAY:181372228',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181372228')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campus 7'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6334646)) <= 0.00070
               AND ABS(v.longitude - (-8.6579934)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1306925831 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Surf de São Jacinto', 'Aveiro', 'Aveiro', NULL,
       'Surf', 'Complexo desportivo', NULL,
       40.6670033, -8.7394600, 'OPENSTREETMAP', 'OSM:WAY:1306925831',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1306925831')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Surf de São Jacinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6670033)) <= 0.00070
               AND ABS(v.longitude - (-8.7394600)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:235482998 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Desportivo de São Bernardo', 'Aveiro', 'Aveiro', NULL,
       'Andebol', 'Campo desportivo', NULL,
       40.6137640, -8.6285794, 'OPENSTREETMAP', 'OSM:WAY:235482998',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:235482998')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Desportivo de São Bernardo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6137640)) <= 0.00070
               AND ABS(v.longitude - (-8.6285794)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:500204841 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Aveiro', 'Aveiro', 'Aveiro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.6385688, -8.6567508, 'OPENSTREETMAP', 'OSM:WAY:500204841',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:500204841')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Aveiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6385688)) <= 0.00070
               AND ABS(v.longitude - (-8.6567508)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:190504643 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Eixense', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.6209211, -8.5753154, 'OPENSTREETMAP', 'OSM:WAY:190504643',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:190504643')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Eixense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6209211)) <= 0.00070
               AND ABS(v.longitude - (-8.5753154)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181346701 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio de São Jacinto', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.6659338, -8.7378790, 'OPENSTREETMAP', 'OSM:WAY:181346701',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181346701')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio de São Jacinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6659338)) <= 0.00070
               AND ABS(v.longitude - (-8.7378790)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:219560122 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Aveiro', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6479070, -8.5937823, 'OPENSTREETMAP', 'OSM:WAY:219560122',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:219560122')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Aveiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6479070)) <= 0.00070
               AND ABS(v.longitude - (-8.5937823)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:175463772 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fidec - Frente Impulsionadora de Desporto e Cultura', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6287999, -8.6111728, 'OPENSTREETMAP', 'OSM:WAY:175463772',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:175463772')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fidec - Frente Impulsionadora de Desporto e Cultura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6287999)) <= 0.00070
               AND ABS(v.longitude - (-8.6111728)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:199989729 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube do Bom Sucesso', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6033005, -8.6449463, 'OPENSTREETMAP', 'OSM:WAY:199989729',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:199989729')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube do Bom Sucesso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6033005)) <= 0.00070
               AND ABS(v.longitude - (-8.6449463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151609463 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GinLine', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6479054, -8.6384200, 'OPENSTREETMAP', 'OSM:WAY:151609463',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151609463')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GinLine'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6479054)) <= 0.00070
               AND ABS(v.longitude - (-8.6384200)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1721651312 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6369915, -8.6363868, 'OPENSTREETMAP', 'OSM:NODE:1721651312',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1721651312')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6369915)) <= 0.00070
               AND ABS(v.longitude - (-8.6363868)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:306840645 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'InsideSports', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6517170, -8.5994324, 'OPENSTREETMAP', 'OSM:WAY:306840645',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:306840645')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('InsideSports'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6517170)) <= 0.00070
               AND ABS(v.longitude - (-8.5994324)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157721959 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão do Clube dos Galitos', 'Aveiro', 'Aveiro', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.6369910, -8.6407798, 'OPENSTREETMAP', 'OSM:WAY:157721959',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157721959')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão do Clube dos Galitos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6369910)) <= 0.00070
               AND ABS(v.longitude - (-8.6407798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1915191343 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Multiusos São Jacinto', 'Aveiro', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.6667910, -8.7387556, 'OPENSTREETMAP', 'OSM:NODE:1915191343',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1915191343')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Multiusos São Jacinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6667910)) <= 0.00070
               AND ABS(v.longitude - (-8.7387556)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7647104582 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pilates Studio', 'Aveiro', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6398469, -8.6473331, 'OPENSTREETMAP', 'OSM:NODE:7647104582',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7647104582')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pilates Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6398469)) <= 0.00070
               AND ABS(v.longitude - (-8.6473331)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237407324 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina dos Galitos', 'Aveiro', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.6382715, -8.6465583, 'OPENSTREETMAP', 'OSM:WAY:237407324',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237407324')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina dos Galitos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6382715)) <= 0.00070
               AND ABS(v.longitude - (-8.6465583)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1418738847 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Oliveirinha', 'Aveiro', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.6013965, -8.6060809, 'OPENSTREETMAP', 'OSM:WAY:1418738847',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1418738847')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Oliveirinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6013965)) <= 0.00070
               AND ABS(v.longitude - (-8.6060809)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152241544 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de São Jacinto', 'Aveiro', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.6668823, -8.7391574, 'OPENSTREETMAP', 'OSM:WAY:152241544',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152241544')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de São Jacinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6668823)) <= 0.00070
               AND ABS(v.longitude - (-8.7391574)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1149180855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de BTT de Requeixo', 'Aveiro', 'Aveiro', NULL,
       'Ciclismo', 'Pista desportiva', NULL,
       40.5737366, -8.5490822, 'OPENSTREETMAP', 'OSM:WAY:1149180855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1149180855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de BTT de Requeixo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5737366)) <= 0.00070
               AND ABS(v.longitude - (-8.5490822)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1299579899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Cacia', 'Aveiro', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.6761656, -8.5899048, 'OPENSTREETMAP', 'OSM:WAY:1299579899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1299579899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Cacia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6761656)) <= 0.00070
               AND ABS(v.longitude - (-8.5899048)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:111660551 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quinta do Casal', 'Aveiro', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.6207184, -8.6488615, 'OPENSTREETMAP', 'OSM:WAY:111660551',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:111660551')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aveiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quinta do Casal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6207184)) <= 0.00070
               AND ABS(v.longitude - (-8.6488615)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1055941944 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Real', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9964005, -8.2696019, 'OPENSTREETMAP', 'OSM:WAY:1055941944',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1055941944')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9964005)) <= 0.00070
               AND ABS(v.longitude - (-8.2696019)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1507927759 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo do Gilde', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9780052, -8.2928369, 'OPENSTREETMAP', 'OSM:WAY:1507927759',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1507927759')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo do Gilde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9780052)) <= 0.00070
               AND ABS(v.longitude - (-8.2928369)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:552147307 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Manuel Emilio dos Santos', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       41.0528565, -8.2874813, 'OPENSTREETMAP', 'OSM:WAY:552147307',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:552147307')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Manuel Emilio dos Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0528565)) <= 0.00070
               AND ABS(v.longitude - (-8.2874813)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1563204556 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal da Boavista', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       41.0402357, -8.2642961, 'OPENSTREETMAP', 'OSM:WAY:1563204556',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1563204556')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal da Boavista'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0402357)) <= 0.00070
               AND ABS(v.longitude - (-8.2642961)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:551524375 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural e Recreativo de São Martinho', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0526828, -8.2875059, 'OPENSTREETMAP', 'OSM:WAY:551524375',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:551524375')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural e Recreativo de São Martinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0526828)) <= 0.00070
               AND ABS(v.longitude - (-8.2875059)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:389448417 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Folgoso', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0174838, -8.3589201, 'OPENSTREETMAP', 'OSM:WAY:389448417',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:389448417')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Folgoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0174838)) <= 0.00070
               AND ABS(v.longitude - (-8.3589201)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3367055771 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Go to Gym', 'Castelo de Paiva', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0410210, -8.2704890, 'OPENSTREETMAP', 'OSM:NODE:3367055771',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3367055771')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Go to Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0410210)) <= 0.00070
               AND ABS(v.longitude - (-8.2704890)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:388236540 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo de Pedorido', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0484972, -8.3708483, 'OPENSTREETMAP', 'OSM:WAY:388236540',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:388236540')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo de Pedorido'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0484972)) <= 0.00070
               AND ABS(v.longitude - (-8.3708483)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13956178450 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Just Begin', 'Castelo de Paiva', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0328431, -8.2596692, 'OPENSTREETMAP', 'OSM:NODE:13956178450',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13956178450')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Just Begin'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0328431)) <= 0.00070
               AND ABS(v.longitude - (-8.2596692)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:386841145 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oliveirense Futebol Clube', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0363206, -8.3602806, 'OPENSTREETMAP', 'OSM:WAY:386841145',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:386841145')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oliveirense Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0363206)) <= 0.00070
               AND ABS(v.longitude - (-8.3602806)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:389804804 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pejão', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0009807, -8.3132509, 'OPENSTREETMAP', 'OSM:WAY:389804804',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:389804804')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pejão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0009807)) <= 0.00070
               AND ABS(v.longitude - (-8.3132509)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12159843918 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'RV Fitness Studio', 'Castelo de Paiva', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0421301, -8.2705600, 'OPENSTREETMAP', 'OSM:NODE:12159843918',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12159843918')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('RV Fitness Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0421301)) <= 0.00070
               AND ABS(v.longitude - (-8.2705600)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:389676626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Serradelo', 'Castelo de Paiva', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0266452, -8.3269354, 'OPENSTREETMAP', 'OSM:WAY:389676626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:389676626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castelo de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Serradelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0266452)) <= 0.00070
               AND ABS(v.longitude - (-8.3269354)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1004154765 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva da Freguesia de Anta', 'Espinho', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0042549, -8.6024820, 'OPENSTREETMAP', 'OSM:WAY:1004154765',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1004154765')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva da Freguesia de Anta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0042549)) <= 0.00070
               AND ABS(v.longitude - (-8.6024820)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1039134402 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva da Quinta de Paramos', 'Espinho', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9742695, -8.6203107, 'OPENSTREETMAP', 'OSM:WAY:1039134402',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1039134402')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva da Quinta de Paramos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9742695)) <= 0.00070
               AND ABS(v.longitude - (-8.6203107)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1004154763 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Seara', 'Espinho', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9926406, -8.6237162, 'OPENSTREETMAP', 'OSM:WAY:1004154763',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1004154763')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Seara'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9926406)) <= 0.00070
               AND ABS(v.longitude - (-8.6237162)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:141393179 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos Sporting Clube Espinho', 'Espinho', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9917624, -8.6404708, 'OPENSTREETMAP', 'OSM:WAY:141393179',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:141393179')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos Sporting Clube Espinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9917624)) <= 0.00070
               AND ABS(v.longitude - (-8.6404708)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:921786447 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo A Ronda', 'Espinho', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0186711, -8.5993379, 'OPENSTREETMAP', 'OSM:WAY:921786447',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:921786447')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo A Ronda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0186711)) <= 0.00070
               AND ABS(v.longitude - (-8.5993379)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1054370658 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo da Idanha', 'Espinho', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0090748, -8.6130471, 'OPENSTREETMAP', 'OSM:WAY:1054370658',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1054370658')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo da Idanha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0090748)) <= 0.00070
               AND ABS(v.longitude - (-8.6130471)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:62048975 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oporto Golf Club', 'Espinho', 'Aveiro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       40.9907264, -8.6444128, 'OPENSTREETMAP', 'OSM:WAY:62048975',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:62048975')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oporto Golf Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9907264)) <= 0.00070
               AND ABS(v.longitude - (-8.6444128)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:653130313 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oporto Golf Club', 'Espinho', 'Aveiro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       40.9873222, -8.6427869, 'OPENSTREETMAP', 'OSM:WAY:653130313',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:653130313')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oporto Golf Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9873222)) <= 0.00070
               AND ABS(v.longitude - (-8.6427869)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:146952396 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Arquiteto Jerónimo Reis', 'Espinho', 'Aveiro', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       41.0139739, -8.6392493, 'OPENSTREETMAP', 'OSM:WAY:146952396',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:146952396')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Arquiteto Jerónimo Reis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0139739)) <= 0.00070
               AND ABS(v.longitude - (-8.6392493)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105879819 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Espinho', 'Espinho', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0061748, -8.6334717, 'OPENSTREETMAP', 'OSM:WAY:105879819',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105879819')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Espinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0061748)) <= 0.00070
               AND ABS(v.longitude - (-8.6334717)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158567685 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Surfjah Surf School', 'Espinho', 'Aveiro', NULL,
       'Surf', 'Complexo desportivo', NULL,
       41.0077726, -8.6460958, 'OPENSTREETMAP', 'OSM:WAY:158567685',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158567685')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Surfjah Surf School'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0077726)) <= 0.00070
               AND ABS(v.longitude - (-8.6460958)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158568081 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Surfjah Surf School', 'Espinho', 'Aveiro', NULL,
       'Surf', 'Complexo desportivo', NULL,
       41.0090352, -8.6460118, 'OPENSTREETMAP', 'OSM:WAY:158568081',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158568081')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Espinho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Surfjah Surf School'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0090352)) <= 0.00070
               AND ABS(v.longitude - (-8.6460118)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:251198793 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro de Avanca', 'Estarreja', 'Aveiro', NULL,
       'Tiro', 'Campo desportivo', NULL,
       40.8072979, -8.5589305, 'OPENSTREETMAP', 'OSM:WAY:251198793',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:251198793')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estarreja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro de Avanca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8072979)) <= 0.00070
               AND ABS(v.longitude - (-8.5589305)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152052471 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo de Estarreja', 'Estarreja', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7598329, -8.5691114, 'OPENSTREETMAP', 'OSM:WAY:152052471',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152052471')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estarreja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo de Estarreja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7598329)) <= 0.00070
               AND ABS(v.longitude - (-8.5691114)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1311171956 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Avanca', 'Estarreja', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.8088844, -8.5764513, 'OPENSTREETMAP', 'OSM:WAY:1311171956',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1311171956')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estarreja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Avanca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8088844)) <= 0.00070
               AND ABS(v.longitude - (-8.5764513)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:160289117 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Estarreja', 'Estarreja', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.7618213, -8.5693814, 'OPENSTREETMAP', 'OSM:WAY:160289117',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:160289117')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estarreja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Estarreja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7618213)) <= 0.00070
               AND ABS(v.longitude - (-8.5693814)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:129387616 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Patinagem', 'Estarreja', 'Aveiro', NULL,
       'Patinagem', 'Pista desportiva', NULL,
       40.7242213, -8.5568792, 'OPENSTREETMAP', 'OSM:WAY:129387616',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:129387616')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estarreja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Patinagem'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7242213)) <= 0.00070
               AND ABS(v.longitude - (-8.5568792)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:823590983 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Simplificada de Estarreja', 'Estarreja', 'Aveiro', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.7535079, -8.5654553, 'OPENSTREETMAP', 'OSM:WAY:823590983',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823590983')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Estarreja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Simplificada de Estarreja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7535079)) <= 0.00070
               AND ABS(v.longitude - (-8.5654553)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:977815697 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva Cultural e Recreativa Senhora dos Campos', 'Ílhavo', 'Aveiro', NULL,
       'Voleibol de Praia', 'Campo desportivo', NULL,
       40.6191155, -8.7063011, 'OPENSTREETMAP', 'OSM:WAY:977815697',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:977815697')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva Cultural e Recreativa Senhora dos Campos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6191155)) <= 0.00070
               AND ABS(v.longitude - (-8.7063011)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135997615 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos Futebol / Basquetebol', 'Ílhavo', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6364758, -8.7129903, 'OPENSTREETMAP', 'OSM:WAY:135997615',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135997615')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos Futebol / Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6364758)) <= 0.00070
               AND ABS(v.longitude - (-8.7129903)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:130874229 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos Ténis', 'Ílhavo', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.6366002, -8.7126231, 'OPENSTREETMAP', 'OSM:WAY:130874229',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:130874229')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6366002)) <= 0.00070
               AND ABS(v.longitude - (-8.7126231)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1256162900 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Vela Costa Nova', 'Ílhavo', 'Aveiro', NULL,
       'Vela', 'Complexo desportivo', NULL,
       40.6195952, -8.7489593, 'OPENSTREETMAP', 'OSM:NODE:1256162900',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1256162900')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Vela Costa Nova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6195952)) <= 0.00070
               AND ABS(v.longitude - (-8.7489593)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:247243598 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'NEGE - Novo Estrela da Gafanha da Encarnação', 'Ílhavo', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6159744, -8.7180318, 'OPENSTREETMAP', 'OSM:WAY:247243598',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:247243598')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('NEGE - Novo Estrela da Gafanha da Encarnação'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6159744)) <= 0.00070
               AND ABS(v.longitude - (-8.7180318)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:255867829 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Os Ílhavos', 'Ílhavo', 'Aveiro', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       40.5981427, -8.6632268, 'OPENSTREETMAP', 'OSM:WAY:255867829',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:255867829')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Os Ílhavos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5981427)) <= 0.00070
               AND ABS(v.longitude - (-8.6632268)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105861668 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Capitão Adriano Nordeste', 'Ílhavo', 'Aveiro', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.6026614, -8.6704302, 'OPENSTREETMAP', 'OSM:WAY:105861668',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105861668')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Capitão Adriano Nordeste'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6026614)) <= 0.00070
               AND ABS(v.longitude - (-8.6704302)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107817843 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Ílhavo', 'Ílhavo', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.6056089, -8.6738466, 'OPENSTREETMAP', 'OSM:WAY:107817843',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107817843')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Ílhavo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6056089)) <= 0.00070
               AND ABS(v.longitude - (-8.6738466)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1482289240 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vale de Ílhavo', 'Ílhavo', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.5771019, -8.6521385, 'OPENSTREETMAP', 'OSM:WAY:1482289240',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1482289240')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vale de Ílhavo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5771019)) <= 0.00070
               AND ABS(v.longitude - (-8.6521385)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:128198472 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Gafanha da Nazaré', 'Ílhavo', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.6266086, -8.7079470, 'OPENSTREETMAP', 'OSM:WAY:128198472',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:128198472')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Gafanha da Nazaré'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6266086)) <= 0.00070
               AND ABS(v.longitude - (-8.7079470)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168807700 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Ílhavo', 'Ílhavo', 'Aveiro', NULL,
       'Futsal;Basebol', 'Campo desportivo', NULL,
       40.6044145, -8.6729111, 'OPENSTREETMAP', 'OSM:WAY:168807700',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168807700')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Ílhavo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6044145)) <= 0.00070
               AND ABS(v.longitude - (-8.6729111)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:977818913 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ria Activa', 'Ílhavo', 'Aveiro', NULL,
       'Voleibol de Praia', 'Campo desportivo', NULL,
       40.6068458, -8.7484997, 'OPENSTREETMAP', 'OSM:WAY:977818913',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:977818913')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ria Activa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6068458)) <= 0.00070
               AND ABS(v.longitude - (-8.7484997)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135997618 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park Gafanha da Nazaré', 'Ílhavo', 'Aveiro', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.6267694, -8.7073314, 'OPENSTREETMAP', 'OSM:WAY:135997618',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135997618')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park Gafanha da Nazaré'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6267694)) <= 0.00070
               AND ABS(v.longitude - (-8.7073314)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:208126289 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube Vista Alegre', 'Ílhavo', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5943330, -8.6847135, 'OPENSTREETMAP', 'OSM:WAY:208126289',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:208126289')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ílhavo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube Vista Alegre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5943330)) <= 0.00070
               AND ABS(v.longitude - (-8.6847135)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:732208904 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Alegre Bar Pool', 'Mealhada', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.3825905, -8.3773751, 'OPENSTREETMAP', 'OSM:WAY:732208904',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:732208904')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Alegre Bar Pool'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3825905)) <= 0.00070
               AND ABS(v.longitude - (-8.3773751)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182178611 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Basquetebol', 'Mealhada', 'Aveiro', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.3847382, -8.4493233, 'OPENSTREETMAP', 'OSM:WAY:182178611',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182178611')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3847382)) <= 0.00070
               AND ABS(v.longitude - (-8.4493233)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:246900762 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Carreta', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3192407, -8.4488255, 'OPENSTREETMAP', 'OSM:WAY:246900762',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:246900762')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Carreta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3192407)) <= 0.00070
               AND ABS(v.longitude - (-8.4488255)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:246685135 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Ferrugens', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3899039, -8.4672542, 'OPENSTREETMAP', 'OSM:WAY:246685135',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:246685135')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Ferrugens'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3899039)) <= 0.00070
               AND ABS(v.longitude - (-8.4672542)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
