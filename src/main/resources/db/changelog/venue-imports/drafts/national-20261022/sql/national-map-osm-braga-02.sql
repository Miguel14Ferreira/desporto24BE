-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:303410781 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Valinha', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.4138489, -8.3063467, 'OPENSTREETMAP', 'OSM:WAY:303410781',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303410781')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Valinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4138489)) <= 0.00070
               AND ABS(v.longitude - (-8.3063467)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147431354 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Gémeos', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4066595, -8.2593760, 'OPENSTREETMAP', 'OSM:WAY:147431354',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147431354')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Gémeos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4066595)) <= 0.00070
               AND ABS(v.longitude - (-8.2593760)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:551673778 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Prazins', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4845201, -8.3150056, 'OPENSTREETMAP', 'OSM:WAY:551673778',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:551673778')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Prazins'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4845201)) <= 0.00070
               AND ABS(v.longitude - (-8.3150056)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:188336279 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Souto São Salvador', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5015295, -8.3154278, 'OPENSTREETMAP', 'OSM:WAY:188336279',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:188336279')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Souto São Salvador'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5015295)) <= 0.00070
               AND ABS(v.longitude - (-8.3154278)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:189376352 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Centro Desportivo de Pinheiro', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4153565, -8.2928083, 'OPENSTREETMAP', 'OSM:WAY:189376352',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:189376352')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Centro Desportivo de Pinheiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4153565)) <= 0.00070
               AND ABS(v.longitude - (-8.2928083)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:327590363 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do GRCD Candoso Santiago/Mascotelos', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4191393, -8.3185797, 'OPENSTREETMAP', 'OSM:WAY:327590363',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:327590363')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do GRCD Candoso Santiago/Mascotelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4191393)) <= 0.00070
               AND ABS(v.longitude - (-8.3185797)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:343929311 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Práticas de Golfe (Driving Range)', 'Guimarães', 'Braga', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.4514672, -8.2891765, 'OPENSTREETMAP', 'OSM:WAY:343929311',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:343929311')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Práticas de Golfe (Driving Range)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4514672)) <= 0.00070
               AND ABS(v.longitude - (-8.2891765)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:191744868 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de São Claúdio de Barco', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4968359, -8.3217654, 'OPENSTREETMAP', 'OSM:WAY:191744868',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:191744868')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de São Claúdio de Barco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4968359)) <= 0.00070
               AND ABS(v.longitude - (-8.3217654)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:340731047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro Clube Industrial de Pevidém', 'Guimarães', 'Braga', NULL,
       'Tiro', 'Campo desportivo', NULL,
       41.4362860, -8.3569894, 'OPENSTREETMAP', 'OSM:WAY:340731047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:340731047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro Clube Industrial de Pevidém'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4362860)) <= 0.00070
               AND ABS(v.longitude - (-8.3569894)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:541008136 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Monte do Bouro', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4703989, -8.2809165, 'OPENSTREETMAP', 'OSM:WAY:541008136',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:541008136')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Monte do Bouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4703989)) <= 0.00070
               AND ABS(v.longitude - (-8.2809165)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:188333930 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Desportivo dos Amigos de Urgezes', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4261652, -8.2925257, 'OPENSTREETMAP', 'OSM:WAY:188333930',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:188333930')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Desportivo dos Amigos de Urgezes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4261652)) <= 0.00070
               AND ABS(v.longitude - (-8.2925257)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1292389605 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Ténis Guimarães', 'Guimarães', 'Braga', NULL,
       'Ténis;Padel;Pickleball', 'Complexo desportivo', NULL,
       41.4458747, -8.3035525, 'OPENSTREETMAP', 'OSM:WAY:1292389605',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1292389605')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Ténis Guimarães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4458747)) <= 0.00070
               AND ABS(v.longitude - (-8.3035525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:255814790 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas de Guimarães', 'Guimarães', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.4306301, -8.3189149, 'OPENSTREETMAP', 'OSM:WAY:255814790',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:255814790')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas de Guimarães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4306301)) <= 0.00070
               AND ABS(v.longitude - (-8.3189149)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:191725882 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas Taipas Termal', 'Guimarães', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.4830557, -8.3435305, 'OPENSTREETMAP', 'OSM:WAY:191725882',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:191725882')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas Taipas Termal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4830557)) <= 0.00070
               AND ABS(v.longitude - (-8.3435305)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:191725883 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo do Clube Caçadores das Taipas', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4906720, -8.3369446, 'OPENSTREETMAP', 'OSM:WAY:191725883',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:191725883')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo do Clube Caçadores das Taipas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4906720)) <= 0.00070
               AND ABS(v.longitude - (-8.3369446)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:133160697 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo do Vitória Sport Clube', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4492989, -8.2784864, 'OPENSTREETMAP', 'OSM:WAY:133160697',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:133160697')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo do Vitória Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4492989)) <= 0.00070
               AND ABS(v.longitude - (-8.2784864)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1289681046 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crescer Mais', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4531652, -8.3073852, 'OPENSTREETMAP', 'OSM:WAY:1289681046',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1289681046')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crescer Mais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4531652)) <= 0.00070
               AND ABS(v.longitude - (-8.3073852)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:40700203 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Comendador Joaquim de Almeida Freitas', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.3782248, -8.3546226, 'OPENSTREETMAP', 'OSM:WAY:40700203',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:40700203')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Comendador Joaquim de Almeida Freitas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3782248)) <= 0.00070
               AND ABS(v.longitude - (-8.3546226)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:211693383 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio D. Afonso Henriques', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.4458734, -8.3010328, 'OPENSTREETMAP', 'OSM:WAY:211693383',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:211693383')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio D. Afonso Henriques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4458734)) <= 0.00070
               AND ABS(v.longitude - (-8.3010328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:148516225 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Desportivo de Ronfe', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4409177, -8.3876242, 'OPENSTREETMAP', 'OSM:WAY:148516225',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:148516225')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Desportivo de Ronfe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4409177)) <= 0.00070
               AND ABS(v.longitude - (-8.3876242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1482371005 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Montinho', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4906069, -8.3376631, 'OPENSTREETMAP', 'OSM:WAY:1482371005',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1482371005')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Montinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4906069)) <= 0.00070
               AND ABS(v.longitude - (-8.3376631)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11318409640 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4483834, -8.3416866, 'OPENSTREETMAP', 'OSM:NODE:11318409640',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11318409640')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4483834)) <= 0.00070
               AND ABS(v.longitude - (-8.3416866)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155331313 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'forum Airão são joão', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4647893, -8.4291862, 'OPENSTREETMAP', 'OSM:WAY:1155331313',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155331313')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('forum Airão são joão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4647893)) <= 0.00070
               AND ABS(v.longitude - (-8.4291862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1837444034 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Corpo Activo', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4497123, -8.2967441, 'OPENSTREETMAP', 'OSM:NODE:1837444034',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1837444034')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Corpo Activo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4497123)) <= 0.00070
               AND ABS(v.longitude - (-8.2967441)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:130796528 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo " Os Unidos do Cano"', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4489852, -8.2875181, 'OPENSTREETMAP', 'OSM:WAY:130796528',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:130796528')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo " Os Unidos do Cano"'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4489852)) <= 0.00070
               AND ABS(v.longitude - (-8.2875181)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147436137 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo São Faustino', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3915283, -8.2800983, 'OPENSTREETMAP', 'OSM:WAY:147436137',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147436137')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo São Faustino'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3915283)) <= 0.00070
               AND ABS(v.longitude - (-8.2800983)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183991754 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos do Brito SC', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4508246, -8.3618315, 'OPENSTREETMAP', 'OSM:WAY:183991754',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183991754')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos do Brito SC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4508246)) <= 0.00070
               AND ABS(v.longitude - (-8.3618315)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1091199244 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos Dr. João Afonso Almeida', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4674506, -8.3428572, 'OPENSTREETMAP', 'OSM:WAY:1091199244',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1091199244')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos Dr. João Afonso Almeida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4674506)) <= 0.00070
               AND ABS(v.longitude - (-8.3428572)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1351436412 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Ases Santa Eufémia', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4844441, -8.3275259, 'OPENSTREETMAP', 'OSM:WAY:1351436412',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1351436412')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Ases Santa Eufémia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4844441)) <= 0.00070
               AND ABS(v.longitude - (-8.3275259)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:226966142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Trial Bike', 'Guimarães', 'Braga', NULL,
       'Ciclismo', 'Pista desportiva', NULL,
       41.4328769, -8.3225536, 'OPENSTREETMAP', 'OSM:WAY:226966142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:226966142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Trial Bike'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4328769)) <= 0.00070
               AND ABS(v.longitude - (-8.3225536)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1552306408 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Polidesportivo das Taipas', 'Guimarães', 'Braga', NULL,
       'Futebol;Ténis', 'Complexo desportivo', NULL,
       41.4831247, -8.3418554, 'OPENSTREETMAP', 'OSM:WAY:1552306408',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1552306408')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Polidesportivo das Taipas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4831247)) <= 0.00070
               AND ABS(v.longitude - (-8.3418554)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11958657060 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Physical', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4420278, -8.2859710, 'OPENSTREETMAP', 'OSM:NODE:11958657060',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11958657060')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Physical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4420278)) <= 0.00070
               AND ABS(v.longitude - (-8.2859710)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169425451 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Moreira de Cónegos', 'Guimarães', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.3868520, -8.3406015, 'OPENSTREETMAP', 'OSM:WAY:1169425451',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169425451')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Moreira de Cónegos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3868520)) <= 0.00070
               AND ABS(v.longitude - (-8.3406015)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134922334 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina dos Bombeiros Voluntários de Guimarães', 'Guimarães', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.4474705, -8.2985853, 'OPENSTREETMAP', 'OSM:WAY:134922334',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134922334')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina dos Bombeiros Voluntários de Guimarães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4474705)) <= 0.00070
               AND ABS(v.longitude - (-8.2985853)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155331314 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'piscinas Airão são joão', 'Guimarães', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.4651320, -8.4284950, 'OPENSTREETMAP', 'OSM:WAY:1155331314',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155331314')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('piscinas Airão são joão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4651320)) <= 0.00070
               AND ABS(v.longitude - (-8.4284950)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:200335339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas dos Bombeiros', 'Guimarães', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.4870934, -8.3478766, 'OPENSTREETMAP', 'OSM:WAY:200335339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:200335339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas dos Bombeiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4870934)) <= 0.00070
               AND ABS(v.longitude - (-8.3478766)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1483977750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Vitória Sport Clube', 'Guimarães', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.4477140, -8.2818720, 'OPENSTREETMAP', 'OSM:WAY:1483977750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1483977750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Vitória Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4477140)) <= 0.00070
               AND ABS(v.longitude - (-8.2818720)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1290228025 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Play Indoor', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4302125, -8.3078015, 'OPENSTREETMAP', 'OSM:WAY:1290228025',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1290228025')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Play Indoor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4302125)) <= 0.00070
               AND ABS(v.longitude - (-8.3078015)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1179975146 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Atães', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4584770, -8.2500764, 'OPENSTREETMAP', 'OSM:WAY:1179975146',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1179975146')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Atães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4584770)) <= 0.00070
               AND ABS(v.longitude - (-8.2500764)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:187177775 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SCORE Health Club', 'Guimarães', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.4208057, -8.2989755, 'OPENSTREETMAP', 'OSM:WAY:187177775',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:187177775')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SCORE Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4208057)) <= 0.00070
               AND ABS(v.longitude - (-8.2989755)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1289681057 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park Guimarães', 'Guimarães', 'Braga', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.4475878, -8.2784608, 'OPENSTREETMAP', 'OSM:WAY:1289681057',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1289681057')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park Guimarães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4475878)) <= 0.00070
               AND ABS(v.longitude - (-8.2784608)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243005089 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Top Padel - Guimarães', 'Guimarães', 'Braga', NULL,
       'Padel', 'Campo desportivo', NULL,
       41.4368025, -8.3029237, 'OPENSTREETMAP', 'OSM:WAY:243005089',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243005089')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Top Padel - Guimarães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4368025)) <= 0.00070
               AND ABS(v.longitude - (-8.3029237)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11939750424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Train4You', 'Guimarães', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4545094, -8.2811217, 'OPENSTREETMAP', 'OSM:NODE:11939750424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11939750424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Train4You'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4545094)) <= 0.00070
               AND ABS(v.longitude - (-8.2811217)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:189966626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vitória Sport Clube', 'Guimarães', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4459826, -8.3008709, 'OPENSTREETMAP', 'OSM:WAY:189966626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:189966626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guimarães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vitória Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4459826)) <= 0.00070
               AND ABS(v.longitude - (-8.3008709)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:283631766 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Rendufinho', 'Póvoa de Lanhoso', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6014469, -8.2779100, 'OPENSTREETMAP', 'OSM:WAY:283631766',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:283631766')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Lanhoso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Rendufinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6014469)) <= 0.00070
               AND ABS(v.longitude - (-8.2779100)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:460560921 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio dos Moinhos Novos', 'Póvoa de Lanhoso', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.5697702, -8.2641304, 'OPENSTREETMAP', 'OSM:WAY:460560921',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:460560921')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Lanhoso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio dos Moinhos Novos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5697702)) <= 0.00070
               AND ABS(v.longitude - (-8.2641304)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:187741960 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas municipais', 'Póvoa de Lanhoso', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5733039, -8.2711137, 'OPENSTREETMAP', 'OSM:WAY:187741960',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:187741960')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Lanhoso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5733039)) <= 0.00070
               AND ABS(v.longitude - (-8.2711137)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:187741969 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas municipais', 'Póvoa de Lanhoso', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.5740464, -8.2724863, 'OPENSTREETMAP', 'OSM:WAY:187741969',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:187741969')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Lanhoso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5740464)) <= 0.00070
               AND ABS(v.longitude - (-8.2724863)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5981467377 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Canyoning', 'Terras de Bouro', 'Braga', NULL,
       'Canoagem', 'Complexo desportivo', NULL,
       41.7237055, -8.1297564, 'OPENSTREETMAP', 'OSM:NODE:5981467377',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5981467377')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Terras de Bouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Canyoning'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7237055)) <= 0.00070
               AND ABS(v.longitude - (-8.1297564)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:59838304 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Terras de Bouro', 'Terras de Bouro', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7171322, -8.3123527, 'OPENSTREETMAP', 'OSM:WAY:59838304',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:59838304')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Terras de Bouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Terras de Bouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7171322)) <= 0.00070
               AND ABS(v.longitude - (-8.3123527)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7272941924 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Municipal', 'Terras de Bouro', 'Braga', NULL,
       'Natação;Ginásio', 'Ginásio', NULL,
       41.7183924, -8.3123755, 'OPENSTREETMAP', 'OSM:NODE:7272941924',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7272941924')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Terras de Bouro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7183924)) <= 0.00070
               AND ABS(v.longitude - (-8.3123755)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:165547041 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol Vieira Sport Club', 'Vieira do Minho', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6333819, -8.1352570, 'OPENSTREETMAP', 'OSM:WAY:165547041',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:165547041')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vieira do Minho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol Vieira Sport Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6333819)) <= 0.00070
               AND ABS(v.longitude - (-8.1352570)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:377225008 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Coberta', 'Vieira do Minho', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.6326478, -8.1348844, 'OPENSTREETMAP', 'OSM:WAY:377225008',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:377225008')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vieira do Minho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Coberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6326478)) <= 0.00070
               AND ABS(v.longitude - (-8.1348844)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169426132 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vieira do Minho', 'Vieira do Minho', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.6326245, -8.1355713, 'OPENSTREETMAP', 'OSM:WAY:1169426132',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169426132')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vieira do Minho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vieira do Minho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6326245)) <= 0.00070
               AND ABS(v.longitude - (-8.1355713)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169426131 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Vieira do Minho', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.6326309, -8.1353427, 'OPENSTREETMAP', 'OSM:WAY:1169426131',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169426131')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vieira do Minho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6326309)) <= 0.00070
               AND ABS(v.longitude - (-8.1353427)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1201850294 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia FC Famalicão', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.3762019, -8.5337373, 'OPENSTREETMAP', 'OSM:WAY:1201850294',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1201850294')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia FC Famalicão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3762019)) <= 0.00070
               AND ABS(v.longitude - (-8.5337373)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107425399 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Ribeirão Futebol Club', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3660902, -8.5575276, 'OPENSTREETMAP', 'OSM:WAY:107425399',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107425399')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Ribeirão Futebol Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3660902)) <= 0.00070
               AND ABS(v.longitude - (-8.5575276)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1243811837 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ADN', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4678533, -8.5485533, 'OPENSTREETMAP', 'OSM:WAY:1243811837',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1243811837')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ADN'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4678533)) <= 0.00070
               AND ABS(v.longitude - (-8.5485533)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:307553427 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'AD Oliveirense', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4044518, -8.4162817, 'OPENSTREETMAP', 'OSM:WAY:307553427',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:307553427')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('AD Oliveirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4044518)) <= 0.00070
               AND ABS(v.longitude - (-8.4162817)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237111233 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva de Castelões', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4109825, -8.4300196, 'OPENSTREETMAP', 'OSM:WAY:237111233',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237111233')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva de Castelões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4109825)) <= 0.00070
               AND ABS(v.longitude - (-8.4300196)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:133659775 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva de Pedome', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4125103, -8.3934264, 'OPENSTREETMAP', 'OSM:WAY:133659775',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:133659775')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva de Pedome'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4125103)) <= 0.00070
               AND ABS(v.longitude - (-8.3934264)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:536138814 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bairro Futebol Clube', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.3753197, -8.4161478, 'OPENSTREETMAP', 'OSM:WAY:536138814',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:536138814')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bairro Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3753197)) <= 0.00070
               AND ABS(v.longitude - (-8.4161478)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13228096630 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Barras. Ac', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3906680, -8.3968946, 'OPENSTREETMAP', 'OSM:NODE:13228096630',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13228096630')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Barras. Ac'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3906680)) <= 0.00070
               AND ABS(v.longitude - (-8.3968946)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:282575251 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Associação Cultural de Vermoim', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4201191, -8.4605003, 'OPENSTREETMAP', 'OSM:WAY:282575251',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:282575251')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Associação Cultural de Vermoim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4201191)) <= 0.00070
               AND ABS(v.longitude - (-8.4605003)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1309159069 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Portela', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3830917, -8.4111486, 'OPENSTREETMAP', 'OSM:WAY:1309159069',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1309159069')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3830917)) <= 0.00070
               AND ABS(v.longitude - (-8.4111486)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:224680536 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Barreiros - Grupo Desportivo de Joane', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4384645, -8.4119439, 'OPENSTREETMAP', 'OSM:WAY:224680536',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:224680536')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Barreiros - Grupo Desportivo de Joane'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4384645)) <= 0.00070
               AND ABS(v.longitude - (-8.4119439)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:942279380 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol do Sardoal', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3516607, -8.5225526, 'OPENSTREETMAP', 'OSM:WAY:942279380',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:942279380')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol do Sardoal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3516607)) <= 0.00070
               AND ABS(v.longitude - (-8.5225526)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:692560680 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol : GRAC', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4427805, -8.4981204, 'OPENSTREETMAP', 'OSM:WAY:692560680',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:692560680')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol : GRAC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4427805)) <= 0.00070
               AND ABS(v.longitude - (-8.4981204)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1252555403 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Breia', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol;Andebol;Futsal', 'Campo desportivo', NULL,
       41.4175951, -8.4517673, 'OPENSTREETMAP', 'OSM:WAY:1252555403',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1252555403')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Breia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4175951)) <= 0.00070
               AND ABS(v.longitude - (-8.4517673)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:469665421 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Juncal', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3689125, -8.5011861, 'OPENSTREETMAP', 'OSM:WAY:469665421',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:469665421')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Juncal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3689125)) <= 0.00070
               AND ABS(v.longitude - (-8.5011861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:459197720 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos José Dias Oliveira', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4280119, -8.4375363, 'OPENSTREETMAP', 'OSM:WAY:459197720',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:459197720')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos José Dias Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4280119)) <= 0.00070
               AND ABS(v.longitude - (-8.4375363)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:469665409 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos S. C. Cabeçudense', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3642152, -8.5134230, 'OPENSTREETMAP', 'OSM:WAY:469665409',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:469665409')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos S. C. Cabeçudense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3642152)) <= 0.00070
               AND ABS(v.longitude - (-8.5134230)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:236954480 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Lagarinhos', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4211397, -8.5311573, 'OPENSTREETMAP', 'OSM:WAY:236954480',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:236954480')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Lagarinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4211397)) <= 0.00070
               AND ABS(v.longitude - (-8.5311573)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:235822609 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo São Cosme', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4363869, -8.4678502, 'OPENSTREETMAP', 'OSM:WAY:235822609',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:235822609')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo São Cosme'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4363869)) <= 0.00070
               AND ABS(v.longitude - (-8.4678502)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1034207602 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Carreira de Tiro de Esmeriz', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Tiro', 'Campo desportivo', NULL,
       41.3732692, -8.5272314, 'OPENSTREETMAP', 'OSM:WAY:1034207602',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1034207602')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Carreira de Tiro de Esmeriz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3732692)) <= 0.00070
               AND ABS(v.longitude - (-8.5272314)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1348677369 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Cessn Piscinas', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3925701, -8.4279847, 'OPENSTREETMAP', 'OSM:WAY:1348677369',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1348677369')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Cessn Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3925701)) <= 0.00070
               AND ABS(v.longitude - (-8.4279847)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:622910270 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo de Lousado - CDL', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3567932, -8.5394255, 'OPENSTREETMAP', 'OSM:WAY:622910270',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:622910270')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo de Lousado - CDL'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3567932)) <= 0.00070
               AND ABS(v.longitude - (-8.5394255)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1252887370 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Corpo Perfeito Privé', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4176488, -8.4434988, 'OPENSTREETMAP', 'OSM:WAY:1252887370',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1252887370')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Corpo Perfeito Privé'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4176488)) <= 0.00070
               AND ABS(v.longitude - (-8.4434988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:232079192 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'DragonForce Famalicão', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3736549, -8.5408277, 'OPENSTREETMAP', 'OSM:WAY:232079192',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:232079192')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('DragonForce Famalicão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3736549)) <= 0.00070
               AND ABS(v.longitude - (-8.5408277)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106500441 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Passal', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.3546816, -8.5565518, 'OPENSTREETMAP', 'OSM:WAY:106500441',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106500441')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Passal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3546816)) <= 0.00070
               AND ABS(v.longitude - (-8.5565518)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:225861164 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       41.4159879, -8.5195106, 'OPENSTREETMAP', 'OSM:WAY:225861164',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:225861164')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4159879)) <= 0.00070
               AND ABS(v.longitude - (-8.5195106)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:277935139 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube Famalicão', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4014443, -8.5224679, 'OPENSTREETMAP', 'OSM:WAY:277935139',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:277935139')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube Famalicão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4014443)) <= 0.00070
               AND ABS(v.longitude - (-8.5224679)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:315152991 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'G.D.U. Louredo', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4052676, -8.5356402, 'OPENSTREETMAP', 'OSM:WAY:315152991',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:315152991')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('G.D.U. Louredo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4052676)) <= 0.00070
               AND ABS(v.longitude - (-8.5356402)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:296957785 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Geração Benfica', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4171399, -8.5143615, 'OPENSTREETMAP', 'OSM:WAY:296957785',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:296957785')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Geração Benfica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4171399)) <= 0.00070
               AND ABS(v.longitude - (-8.5143615)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107492439 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Salgueirinhos', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       41.3553673, -8.5684199, 'OPENSTREETMAP', 'OSM:WAY:107492439',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107492439')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Salgueirinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3553673)) <= 0.00070
               AND ABS(v.longitude - (-8.5684199)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1083054146 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo de Fradelos', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3635420, -8.5904575, 'OPENSTREETMAP', 'OSM:WAY:1083054146',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1083054146')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo de Fradelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3635420)) <= 0.00070
               AND ABS(v.longitude - (-8.5904575)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1251254504 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Recreativo Covense', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.4022432, -8.4461165, 'OPENSTREETMAP', 'OSM:WAY:1251254504',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1251254504')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Recreativo Covense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4022432)) <= 0.00070
               AND ABS(v.longitude - (-8.4461165)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:374559286 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Hospital de Dia da Trofa', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.4002809, -8.5175515, 'OPENSTREETMAP', 'OSM:WAY:374559286',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:374559286')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Hospital de Dia da Trofa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4002809)) <= 0.00070
               AND ABS(v.longitude - (-8.5175515)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1250420481 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'J.A.S.P.', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3985701, -8.4809812, 'OPENSTREETMAP', 'OSM:WAY:1250420481',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1250420481')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('J.A.S.P.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3985701)) <= 0.00070
               AND ABS(v.longitude - (-8.4809812)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185558744 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Operário Futebol Clube', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4197436, -8.5207112, 'OPENSTREETMAP', 'OSM:WAY:185558744',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185558744')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Operário Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4197436)) <= 0.00070
               AND ABS(v.longitude - (-8.5207112)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:253214510 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Amândio Carvalho', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4132862, -8.5768160, 'OPENSTREETMAP', 'OSM:WAY:253214510',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:253214510')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Amândio Carvalho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4132862)) <= 0.00070
               AND ABS(v.longitude - (-8.5768160)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:321396596 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Lagense', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3864674, -8.5397026, 'OPENSTREETMAP', 'OSM:WAY:321396596',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:321396596')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Lagense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3864674)) <= 0.00070
               AND ABS(v.longitude - (-8.5397026)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1544506252 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Bairro', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3714857, -8.4360190, 'OPENSTREETMAP', 'OSM:WAY:1544506252',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1544506252')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Bairro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3714857)) <= 0.00070
               AND ABS(v.longitude - (-8.4360190)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:866784800 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Joane', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.4394216, -8.4118592, 'OPENSTREETMAP', 'OSM:WAY:866784800',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:866784800')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Joane'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4394216)) <= 0.00070
               AND ABS(v.longitude - (-8.4118592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:665971373 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Oliveira São Mateus', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.3918294, -8.4044048, 'OPENSTREETMAP', 'OSM:WAY:665971373',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:665971373')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Oliveira São Mateus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3918294)) <= 0.00070
               AND ABS(v.longitude - (-8.4044048)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440966951 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Brufe', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4093588, -8.5398730, 'OPENSTREETMAP', 'OSM:WAY:440966951',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440966951')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Brufe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4093588)) <= 0.00070
               AND ABS(v.longitude - (-8.5398730)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:296957782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva Bairrense', 'Vila Nova de Famalicão', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4165791, -8.5160578, 'OPENSTREETMAP', 'OSM:WAY:296957782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:296957782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Famalicão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva Bairrense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4165791)) <= 0.00070
               AND ABS(v.longitude - (-8.5160578)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:262773295 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BragaFut', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6181196, -8.4411301, 'OPENSTREETMAP', 'OSM:WAY:262773295',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:262773295')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BragaFut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6181196)) <= 0.00070
               AND ABS(v.longitude - (-8.4411301)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:358349656 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Fonte Perdiz', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7472953, -8.4063512, 'OPENSTREETMAP', 'OSM:WAY:358349656',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:358349656')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Fonte Perdiz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7472953)) <= 0.00070
               AND ABS(v.longitude - (-8.4063512)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1219288568 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Valdreu', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7263193, -8.3167661, 'OPENSTREETMAP', 'OSM:WAY:1219288568',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1219288568')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Valdreu'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7263193)) <= 0.00070
               AND ABS(v.longitude - (-8.3167661)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:159385446 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Pico', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6870701, -8.4257926, 'OPENSTREETMAP', 'OSM:WAY:159385446',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:159385446')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Pico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6870701)) <= 0.00070
               AND ABS(v.longitude - (-8.4257926)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1529346256 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Lage', 'Vila Verde', 'Braga', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', 'OUTDOOR',
       41.6199678, -8.4689303, 'OPENSTREETMAP', 'OSM:WAY:1529346256',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1529346256')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Lage'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6199678)) <= 0.00070
               AND ABS(v.longitude - (-8.4689303)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134659437 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Praia Fluvial do Faial', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5984520, -8.4581370, 'OPENSTREETMAP', 'OSM:WAY:134659437',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134659437')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Praia Fluvial do Faial'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5984520)) <= 0.00070
               AND ABS(v.longitude - (-8.4581370)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:235610095 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos de Marrancos', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6685152, -8.5144715, 'OPENSTREETMAP', 'OSM:WAY:235610095',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:235610095')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos de Marrancos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6685152)) <= 0.00070
               AND ABS(v.longitude - (-8.5144715)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1101229070 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Oriz', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6943703, -8.3706168, 'OPENSTREETMAP', 'OSM:WAY:1101229070',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1101229070')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Oriz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6943703)) <= 0.00070
               AND ABS(v.longitude - (-8.3706168)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1539015751 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Great Padel', 'Vila Verde', 'Braga', NULL,
       'Padel', 'Campo desportivo', 'INDOOR',
       41.6489713, -8.4208124, 'OPENSTREETMAP', 'OSM:WAY:1539015751',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1539015751')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Great Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6489713)) <= 0.00070
               AND ABS(v.longitude - (-8.4208124)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134659441 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo de Prado', 'Vila Verde', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6006842, -8.4582210, 'OPENSTREETMAP', 'OSM:WAY:134659441',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134659441')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo de Prado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6006842)) <= 0.00070
               AND ABS(v.longitude - (-8.4582210)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169423758 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vila de Prado', 'Vila Verde', 'Braga', NULL,
       'Natação', 'Piscina', NULL,
       41.6049669, -8.4598189, 'OPENSTREETMAP', 'OSM:WAY:1169423758',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169423758')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vila de Prado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6049669)) <= 0.00070
               AND ABS(v.longitude - (-8.4598189)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:310358926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Associação Desportiva São Paio Sport Clube', 'Vizela', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3887865, -8.2597950, 'OPENSTREETMAP', 'OSM:WAY:310358926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:310358926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Associação Desportiva São Paio Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3887865)) <= 0.00070
               AND ABS(v.longitude - (-8.2597950)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:310493404 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Tagilde', 'Vizela', 'Braga', NULL,
       'Futebol;Ginásio', 'Campo desportivo', NULL,
       41.3792718, -8.2817831, 'OPENSTREETMAP', 'OSM:WAY:310493404',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:310493404')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Tagilde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3792718)) <= 0.00070
               AND ABS(v.longitude - (-8.2817831)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:303243267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Montesinhos', 'Vizela', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.3785152, -8.2934030, 'OPENSTREETMAP', 'OSM:WAY:303243267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303243267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Montesinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3785152)) <= 0.00070
               AND ABS(v.longitude - (-8.2934030)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122548720 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro do Clube Turístico e Desportivo de Vizela', 'Vizela', 'Braga', NULL,
       'Tiro', 'Campo desportivo', NULL,
       41.3875494, -8.2944637, 'OPENSTREETMAP', 'OSM:WAY:122548720',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122548720')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro do Clube Turístico e Desportivo de Vizela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3875494)) <= 0.00070
               AND ABS(v.longitude - (-8.2944637)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108314647 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Futebol Clube de Vizela', 'Vizela', 'Braga', NULL,
       'Futebol', 'Estádio', NULL,
       41.3889321, -8.3075230, 'OPENSTREETMAP', 'OSM:WAY:108314647',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108314647')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Futebol Clube de Vizela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3889321)) <= 0.00070
               AND ABS(v.longitude - (-8.3075230)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122103341 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Infias', 'Vizela', 'Braga', NULL,
       'Futebol', 'Pista desportiva', NULL,
       41.3996548, -8.3093826, 'OPENSTREETMAP', 'OSM:WAY:122103341',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122103341')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Infias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3996548)) <= 0.00070
               AND ABS(v.longitude - (-8.3093826)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108314646 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Treino do Futebol Clube de Vizela', 'Vizela', 'Braga', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.3879052, -8.3088477, 'OPENSTREETMAP', 'OSM:WAY:108314646',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108314646')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de Treino do Futebol Clube de Vizela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3879052)) <= 0.00070
               AND ABS(v.longitude - (-8.3088477)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003186146 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Vizela', 'Vizela', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3888315, -8.3074944, 'OPENSTREETMAP', 'OSM:WAY:1003186146',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003186146')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Vizela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3888315)) <= 0.00070
               AND ABS(v.longitude - (-8.3074944)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:133666665 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Santa Eulália', 'Vizela', 'Braga', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3550600, -8.3032798, 'OPENSTREETMAP', 'OSM:WAY:133666665',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:133666665')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Desportivo de Santa Eulália'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3550600)) <= 0.00070
               AND ABS(v.longitude - (-8.3032798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:863637065 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Vilar', 'Vizela', 'Braga', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3731451, -8.3187222, 'OPENSTREETMAP', 'OSM:WAY:863637065',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:863637065')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vizela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Vilar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3731451)) <= 0.00070
               AND ABS(v.longitude - (-8.3187222)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
