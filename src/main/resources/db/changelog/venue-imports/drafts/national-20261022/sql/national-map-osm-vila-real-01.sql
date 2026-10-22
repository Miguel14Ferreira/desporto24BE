-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:6352984336 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Megacultus', 'Chaves', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.7435016, -7.4743990, 'OPENSTREETMAP', 'OSM:NODE:6352984336',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6352984336')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Megacultus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7435016)) <= 0.00070
               AND ABS(v.longitude - (-7.4743990)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:438293388 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo de Chaves', 'Chaves', 'Vila Real', NULL,
       'Karting', 'Complexo desportivo', NULL,
       41.7217457, -7.4605948, 'OPENSTREETMAP', 'OSM:WAY:438293388',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:438293388')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo de Chaves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7217457)) <= 0.00070
               AND ABS(v.longitude - (-7.4605948)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4370113678 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Matrix Ginásio', 'Chaves', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.7455066, -7.4712441, 'OPENSTREETMAP', 'OSM:NODE:4370113678',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4370113678')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Matrix Ginásio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7455066)) <= 0.00070
               AND ABS(v.longitude - (-7.4712441)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14094805742 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Evolution+ Fitness & Gym', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2799775, -7.7033346, 'OPENSTREETMAP', 'OSM:NODE:14094805742',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14094805742')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Evolution+ Fitness & Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2799775)) <= 0.00070
               AND ABS(v.longitude - (-7.7033346)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182794271 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Clube de Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3031738, -7.7387509, 'OPENSTREETMAP', 'OSM:WAY:182794271',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182794271')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Clube de Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3031738)) <= 0.00070
               AND ABS(v.longitude - (-7.7387509)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:233325515 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Go Gym Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2925562, -7.7370081, 'OPENSTREETMAP', 'OSM:WAY:233325515',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:233325515')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Go Gym Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2925562)) <= 0.00070
               AND ABS(v.longitude - (-7.7370081)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:810503195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo AMF Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Karting', 'Complexo desportivo', NULL,
       41.2758868, -7.7224860, 'OPENSTREETMAP', 'OSM:WAY:810503195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:810503195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo AMF Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2758868)) <= 0.00070
               AND ABS(v.longitude - (-7.7224860)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183600113 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão dos Desportos de Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Futsal;Basquetebol;Andebol;Voleibol;Ginástica;Karaté;Ténis de mesa', 'Complexo desportivo', NULL,
       41.2999941, -7.7497017, 'OPENSTREETMAP', 'OSM:WAY:183600113',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183600113')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão dos Desportos de Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2999941)) <= 0.00070
               AND ABS(v.longitude - (-7.7497017)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13468687960 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Physical', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2969720, -7.7331321, 'OPENSTREETMAP', 'OSM:NODE:13468687960',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13468687960')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Physical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2969720)) <= 0.00070
               AND ABS(v.longitude - (-7.7331321)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13133130617 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studio Life PT', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3074065, -7.7440943, 'OPENSTREETMAP', 'OSM:NODE:13133130617',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13133130617')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studio Life PT'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3074065)) <= 0.00070
               AND ABS(v.longitude - (-7.7440943)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1088134325 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Bola de Souto de Escarão', 'Alijó', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3774007, -7.5869097, 'OPENSTREETMAP', 'OSM:WAY:1088134325',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1088134325')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Bola de Souto de Escarão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3774007)) <= 0.00070
               AND ABS(v.longitude - (-7.5869097)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1146076626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Pegarinhos', 'Alijó', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3608627, -7.4474693, 'OPENSTREETMAP', 'OSM:WAY:1146076626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1146076626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Pegarinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3608627)) <= 0.00070
               AND ABS(v.longitude - (-7.4474693)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1036488381 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Sanfins do Douro', 'Alijó', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2955390, -7.5177032, 'OPENSTREETMAP', 'OSM:WAY:1036488381',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1036488381')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Sanfins do Douro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2955390)) <= 0.00070
               AND ABS(v.longitude - (-7.5177032)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1344865280 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vilar de Maçada', 'Alijó', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3203769, -7.5554630, 'OPENSTREETMAP', 'OSM:WAY:1344865280',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1344865280')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vilar de Maçada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3203769)) <= 0.00070
               AND ABS(v.longitude - (-7.5554630)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:235543036 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Castedo', 'Alijó', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2227821, -7.4634149, 'OPENSTREETMAP', 'OSM:WAY:235543036',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:235543036')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Castedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2227821)) <= 0.00070
               AND ABS(v.longitude - (-7.4634149)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1270684716 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético do Castedo', 'Alijó', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2290227, -7.4682240, 'OPENSTREETMAP', 'OSM:WAY:1270684716',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1270684716')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético do Castedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2290227)) <= 0.00070
               AND ABS(v.longitude - (-7.4682240)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1211839944 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Engenheiro Delfim Magalhães', 'Alijó', 'Vila Real', NULL,
       'Futebol', 'Estádio', NULL,
       41.2707349, -7.4741590, 'OPENSTREETMAP', 'OSM:WAY:1211839944',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1211839944')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Engenheiro Delfim Magalhães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2707349)) <= 0.00070
               AND ABS(v.longitude - (-7.4741590)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1146076628 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Pegarinhos', 'Alijó', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.3599406, -7.4474538, 'OPENSTREETMAP', 'OSM:WAY:1146076628',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1146076628')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Pegarinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3599406)) <= 0.00070
               AND ABS(v.longitude - (-7.4474538)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:441777812 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Alijó', 'Alijó', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.2745097, -7.4744180, 'OPENSTREETMAP', 'OSM:WAY:441777812',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:441777812')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Alijó'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2745097)) <= 0.00070
               AND ABS(v.longitude - (-7.4744180)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6387640995 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polivalente', 'Alijó', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.2696218, -7.5010451, 'OPENSTREETMAP', 'OSM:NODE:6387640995',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6387640995')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alijó'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polivalente'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2696218)) <= 0.00070
               AND ABS(v.longitude - (-7.5010451)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:660162705 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Curalha', 'Chaves', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7117818, -7.5276465, 'OPENSTREETMAP', 'OSM:WAY:660162705',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:660162705')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Curalha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7117818)) <= 0.00070
               AND ABS(v.longitude - (-7.5276465)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:745377678 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Faiões', 'Chaves', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7499858, -7.4227601, 'OPENSTREETMAP', 'OSM:WAY:745377678',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:745377678')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Faiões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7499858)) <= 0.00070
               AND ABS(v.longitude - (-7.4227601)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1468998107 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Loivos', 'Chaves', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6226467, -7.5200372, 'OPENSTREETMAP', 'OSM:WAY:1468998107',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1468998107')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Loivos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6226467)) <= 0.00070
               AND ABS(v.longitude - (-7.5200372)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155750420 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Oura', 'Chaves', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6205057, -7.5720278, 'OPENSTREETMAP', 'OSM:WAY:1155750420',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155750420')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Oura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6205057)) <= 0.00070
               AND ABS(v.longitude - (-7.5720278)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1312845888 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vale de Anta', 'Chaves', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7419089, -7.5024237, 'OPENSTREETMAP', 'OSM:WAY:1312845888',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1312845888')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vale de Anta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7419089)) <= 0.00070
               AND ABS(v.longitude - (-7.5024237)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:743168740 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Relva Natural', 'Chaves', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7535291, -7.4679392, 'OPENSTREETMAP', 'OSM:WAY:743168740',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:743168740')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Relva Natural'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7535291)) <= 0.00070
               AND ABS(v.longitude - (-7.4679392)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:745375483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Relva Sintética', 'Chaves', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.7533705, -7.4662249, 'OPENSTREETMAP', 'OSM:WAY:745375483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:745375483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Relva Sintética'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7533705)) <= 0.00070
               AND ABS(v.longitude - (-7.4662249)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:962033367 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de ténis de Oura', 'Chaves', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.6208052, -7.5708155, 'OPENSTREETMAP', 'OSM:WAY:962033367',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:962033367')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de ténis de Oura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6208052)) <= 0.00070
               AND ABS(v.longitude - (-7.5708155)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136207893 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Chaves', 'Chaves', 'Vila Real', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.7371101, -7.4709775, 'OPENSTREETMAP', 'OSM:WAY:136207893',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136207893')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Chaves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7371101)) <= 0.00070
               AND ABS(v.longitude - (-7.4709775)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1052130665 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas da Ribeira', 'Chaves', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.6193079, -7.5725427, 'OPENSTREETMAP', 'OSM:WAY:1052130665',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1052130665')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas da Ribeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6193079)) <= 0.00070
               AND ABS(v.longitude - (-7.5725427)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169725369 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Rebentão', 'Chaves', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.7016149, -7.5024098, 'OPENSTREETMAP', 'OSM:WAY:1169725369',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169725369')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Rebentão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7016149)) <= 0.00070
               AND ABS(v.longitude - (-7.5024098)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1420618778 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de BMX', 'Chaves', 'Vila Real', NULL,
       'BMX', 'Pista desportiva', NULL,
       41.7346930, -7.4709995, 'OPENSTREETMAP', 'OSM:WAY:1420618778',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1420618778')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de BMX'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7346930)) <= 0.00070
               AND ABS(v.longitude - (-7.4709995)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12021242717 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Walking Inside Out', 'Chaves', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.7412634, -7.4742941, 'OPENSTREETMAP', 'OSM:NODE:12021242717',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12021242717')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chaves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Walking Inside Out'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.7412634)) <= 0.00070
               AND ABS(v.longitude - (-7.4742941)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:557506208 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Municipal', 'Mesão Frio', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1614699, -7.8894559, 'OPENSTREETMAP', 'OSM:WAY:557506208',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:557506208')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mesão Frio'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1614699)) <= 0.00070
               AND ABS(v.longitude - (-7.8894559)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:981633232 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Mesão Frio', 'Mesão Frio', 'Vila Real', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1620396, -7.8897277, 'OPENSTREETMAP', 'OSM:WAY:981633232',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:981633232')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mesão Frio'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Mesão Frio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1620396)) <= 0.00070
               AND ABS(v.longitude - (-7.8897277)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186947794 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Bormela', 'Mondim de Basto', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4689010, -7.9126455, 'OPENSTREETMAP', 'OSM:WAY:186947794',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186947794')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mondim de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Bormela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4689010)) <= 0.00070
               AND ABS(v.longitude - (-7.9126455)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385298 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vilarinho', 'Mondim de Basto', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4169478, -7.8773242, 'OPENSTREETMAP', 'OSM:WAY:157385298',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385298')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mondim de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vilarinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4169478)) <= 0.00070
               AND ABS(v.longitude - (-7.8773242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385274 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Pombal', 'Mondim de Basto', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4657114, -7.9298663, 'OPENSTREETMAP', 'OSM:WAY:157385274',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385274')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mondim de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Pombal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4657114)) <= 0.00070
               AND ABS(v.longitude - (-7.9298663)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385190 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Mondim de Basto', 'Mondim de Basto', 'Vila Real', NULL,
       'Futebol', 'Estádio', NULL,
       41.4072630, -7.9555801, 'OPENSTREETMAP', 'OSM:WAY:157385190',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385190')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mondim de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Mondim de Basto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4072630)) <= 0.00070
               AND ABS(v.longitude - (-7.9555801)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385141 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Mondim de Basto', 'Mondim de Basto', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.4109305, -7.9575493, 'OPENSTREETMAP', 'OSM:WAY:157385141',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385141')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mondim de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Mondim de Basto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4109305)) <= 0.00070
               AND ABS(v.longitude - (-7.9575493)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186947797 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Atei', 'Mondim de Basto', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4599305, -7.9270537, 'OPENSTREETMAP', 'OSM:WAY:186947797',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186947797')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mondim de Basto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Atei'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4599305)) <= 0.00070
               AND ABS(v.longitude - (-7.9270537)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1526776964 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Codeçoso', 'Montalegre', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8227997, -7.7455529, 'OPENSTREETMAP', 'OSM:WAY:1526776964',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1526776964')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Codeçoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8227997)) <= 0.00070
               AND ABS(v.longitude - (-7.7455529)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:156200850 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Padre Manuel José Jorge', 'Montalegre', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6339071, -7.9494483, 'OPENSTREETMAP', 'OSM:WAY:156200850',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:156200850')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Padre Manuel José Jorge'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6339071)) <= 0.00070
               AND ABS(v.longitude - (-7.9494483)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1002433172 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Desportivo e Cultural de Montalegre', 'Montalegre', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.8243254, -7.8051309, 'OPENSTREETMAP', 'OSM:WAY:1002433172',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1002433172')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Desportivo e Cultural de Montalegre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8243254)) <= 0.00070
               AND ABS(v.longitude - (-7.8051309)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1526776962 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Codeçoso', 'Montalegre', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.8222808, -7.7453593, 'OPENSTREETMAP', 'OSM:WAY:1526776962',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1526776962')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Codeçoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8222808)) <= 0.00070
               AND ABS(v.longitude - (-7.7453593)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:660276480 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Montalegre', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.8268539, -7.7801455, 'OPENSTREETMAP', 'OSM:WAY:660276480',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:660276480')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montalegre'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.8268539)) <= 0.00070
               AND ABS(v.longitude - (-7.7801455)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1136495421 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Candedo', 'Murça', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3619664, -7.3782517, 'OPENSTREETMAP', 'OSM:WAY:1136495421',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1136495421')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Candedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3619664)) <= 0.00070
               AND ABS(v.longitude - (-7.3782517)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:624008782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Porrais', 'Murça', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3435118, -7.3733194, 'OPENSTREETMAP', 'OSM:WAY:624008782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:624008782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Porrais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3435118)) <= 0.00070
               AND ABS(v.longitude - (-7.3733194)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168748515 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro de São Domingos', 'Murça', 'Vila Real', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       41.4199429, -7.4472592, 'OPENSTREETMAP', 'OSM:WAY:168748515',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168748515')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro de São Domingos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4199429)) <= 0.00070
               AND ABS(v.longitude - (-7.4472592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168743374 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Court Tenis e Campos de Basquetebol', 'Murça', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.4045251, -7.4513309, 'OPENSTREETMAP', 'OSM:WAY:168743374',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168743374')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Court Tenis e Campos de Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4045251)) <= 0.00070
               AND ABS(v.longitude - (-7.4513309)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9828496990 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'NP Fitness Studio', 'Murça', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.4016246, -7.4535111, 'OPENSTREETMAP', 'OSM:NODE:9828496990',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9828496990')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('NP Fitness Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4016246)) <= 0.00070
               AND ABS(v.longitude - (-7.4535111)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:202294319 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cobertas', 'Murça', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.4044021, -7.4516950, 'OPENSTREETMAP', 'OSM:WAY:202294319',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:202294319')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4044021)) <= 0.00070
               AND ABS(v.longitude - (-7.4516950)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:170030410 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Murça', 'Murça', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.4037567, -7.4530745, 'OPENSTREETMAP', 'OSM:WAY:170030410',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:170030410')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Murça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4037567)) <= 0.00070
               AND ABS(v.longitude - (-7.4530745)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1107945680 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Porrais', 'Murça', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3453760, -7.3731615, 'OPENSTREETMAP', 'OSM:WAY:1107945680',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1107945680')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Porrais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3453760)) <= 0.00070
               AND ABS(v.longitude - (-7.3731615)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:482251870 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Galafura', 'Peso da Régua', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1865693, -7.6768712, 'OPENSTREETMAP', 'OSM:WAY:482251870',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:482251870')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Galafura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1865693)) <= 0.00070
               AND ABS(v.longitude - (-7.6768712)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1149496652 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Loureiro', 'Peso da Régua', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1935111, -7.8233862, 'OPENSTREETMAP', 'OSM:WAY:1149496652',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1149496652')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Loureiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1935111)) <= 0.00070
               AND ABS(v.longitude - (-7.8233862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:890107548 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Moura Morta', 'Peso da Régua', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1956122, -7.8314273, 'OPENSTREETMAP', 'OSM:WAY:890107548',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:890107548')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Moura Morta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1956122)) <= 0.00070
               AND ABS(v.longitude - (-7.8314273)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:847992125 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Caça e Pesca', 'Peso da Régua', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1548803, -7.7768723, 'OPENSTREETMAP', 'OSM:WAY:847992125',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:847992125')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Caça e Pesca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1548803)) <= 0.00070
               AND ABS(v.longitude - (-7.7768723)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:156657371 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis', 'Peso da Régua', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1555889, -7.7787579, 'OPENSTREETMAP', 'OSM:WAY:156657371',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:156657371')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1555889)) <= 0.00070
               AND ABS(v.longitude - (-7.7787579)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1148914022 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Futebol Clube de Fontelas', 'Peso da Régua', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1580088, -7.8159314, 'OPENSTREETMAP', 'OSM:WAY:1148914022',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1148914022')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Futebol Clube de Fontelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1580088)) <= 0.00070
               AND ABS(v.longitude - (-7.8159314)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:848006641 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Artur Vasques', 'Peso da Régua', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1621842, -7.7859262, 'OPENSTREETMAP', 'OSM:WAY:848006641',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:848006641')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Artur Vasques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1621842)) <= 0.00070
               AND ABS(v.longitude - (-7.7859262)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:848037894 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo das Alagoas', 'Peso da Régua', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1681183, -7.8017416, 'OPENSTREETMAP', 'OSM:WAY:848037894',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:848037894')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo das Alagoas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1681183)) <= 0.00070
               AND ABS(v.longitude - (-7.8017416)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127883602 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Multiusos Municipal António Saraiva', 'Peso da Régua', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1691584, -7.7989103, 'OPENSTREETMAP', 'OSM:WAY:127883602',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127883602')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peso da Régua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Multiusos Municipal António Saraiva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1691584)) <= 0.00070
               AND ABS(v.longitude - (-7.7989103)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105883407 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal do Cavalinho', 'Ribeira de Pena', 'Vila Real', NULL,
       'Futebol', 'Estádio', NULL,
       41.5246007, -7.7919727, 'OPENSTREETMAP', 'OSM:WAY:105883407',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105883407')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ribeira de Pena'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal do Cavalinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5246007)) <= 0.00070
               AND ABS(v.longitude - (-7.7919727)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157379165 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Ribeira de Pena', 'Ribeira de Pena', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.5231407, -7.7918111, 'OPENSTREETMAP', 'OSM:WAY:157379165',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157379165')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ribeira de Pena'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Ribeira de Pena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5231407)) <= 0.00070
               AND ABS(v.longitude - (-7.7918111)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:194798175 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Feira Velha', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2660333, -7.5844559, 'OPENSTREETMAP', 'OSM:WAY:194798175',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:194798175')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Feira Velha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2660333)) <= 0.00070
               AND ABS(v.longitude - (-7.5844559)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1347620323 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Celeirós', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2384712, -7.5567532, 'OPENSTREETMAP', 'OSM:WAY:1347620323',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1347620323')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Celeirós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2384712)) <= 0.00070
               AND ABS(v.longitude - (-7.5567532)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1449417513 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Fermentões', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2451597, -7.5958147, 'OPENSTREETMAP', 'OSM:WAY:1449417513',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1449417513')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Fermentões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2451597)) <= 0.00070
               AND ABS(v.longitude - (-7.5958147)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1370666046 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Garganta', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2950234, -7.6139944, 'OPENSTREETMAP', 'OSM:WAY:1370666046',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1370666046')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Garganta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2950234)) <= 0.00070
               AND ABS(v.longitude - (-7.6139944)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1088139712 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Parada do Pinhão', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3360270, -7.5973685, 'OPENSTREETMAP', 'OSM:WAY:1088139712',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1088139712')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Parada do Pinhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3360270)) <= 0.00070
               AND ABS(v.longitude - (-7.5973685)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1370093839 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Paredes do Pinhão', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3233118, -7.6125772, 'OPENSTREETMAP', 'OSM:WAY:1370093839',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1370093839')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Paredes do Pinhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3233118)) <= 0.00070
               AND ABS(v.longitude - (-7.6125772)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1372811478 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vilarinho de São Romão', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2522648, -7.5782100, 'OPENSTREETMAP', 'OSM:WAY:1372811478',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1372811478')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vilarinho de São Romão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2522648)) <= 0.00070
               AND ABS(v.longitude - (-7.5782100)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:852072158 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Rosa Mota', 'Sabrosa', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.2664433, -7.5814976, 'OPENSTREETMAP', 'OSM:WAY:852072158',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:852072158')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Rosa Mota'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2664433)) <= 0.00070
               AND ABS(v.longitude - (-7.5814976)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1030729466 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Covas do Douro', 'Sabrosa', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1871847, -7.5905616, 'OPENSTREETMAP', 'OSM:WAY:1030729466',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1030729466')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabrosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Covas do Douro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1871847)) <= 0.00070
               AND ABS(v.longitude - (-7.5905616)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158742577 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Cumieira', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2510068, -7.7681110, 'OPENSTREETMAP', 'OSM:WAY:158742577',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158742577')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Cumieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2510068)) <= 0.00070
               AND ABS(v.longitude - (-7.7681110)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1482259639 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Fontes', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2316843, -7.8091629, 'OPENSTREETMAP', 'OSM:WAY:1482259639',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1482259639')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Fontes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2316843)) <= 0.00070
               AND ABS(v.longitude - (-7.8091629)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1482118798 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Fornelos', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2471448, -7.7993392, 'OPENSTREETMAP', 'OSM:WAY:1482118798',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1482118798')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Fornelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2471448)) <= 0.00070
               AND ABS(v.longitude - (-7.7993392)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1486188875 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Medrões', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2123487, -7.8098303, 'OPENSTREETMAP', 'OSM:WAY:1486188875',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1486188875')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Medrões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2123487)) <= 0.00070
               AND ABS(v.longitude - (-7.8098303)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1005910200 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis da Quinta da Cumieira', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.2583113, -7.7619311, 'OPENSTREETMAP', 'OSM:WAY:1005910200',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1005910200')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis da Quinta da Cumieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2583113)) <= 0.00070
               AND ABS(v.longitude - (-7.7619311)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168944704 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Treinos Santa Marta', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2138069, -7.7901431, 'OPENSTREETMAP', 'OSM:WAY:168944704',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168944704')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Treinos Santa Marta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2138069)) <= 0.00070
               AND ABS(v.longitude - (-7.7901431)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168944706 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Santa Marta de Penaguião', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2130914, -7.7893348, 'OPENSTREETMAP', 'OSM:WAY:168944706',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168944706')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Santa Marta de Penaguião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2130914)) <= 0.00070
               AND ABS(v.longitude - (-7.7893348)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:847272113 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Santa Marta de Penaguião', 'Santa Marta de Penaguião', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.2098396, -7.7837096, 'OPENSTREETMAP', 'OSM:WAY:847272113',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:847272113')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Marta de Penaguião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Santa Marta de Penaguião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2098396)) <= 0.00070
               AND ABS(v.longitude - (-7.7837096)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:462833490 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Carrazedo de Montenegro', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5759082, -7.4319140, 'OPENSTREETMAP', 'OSM:WAY:462833490',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:462833490')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Carrazedo de Montenegro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5759082)) <= 0.00070
               AND ABS(v.longitude - (-7.4319140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1436593999 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Esturãos', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6267702, -7.4022685, 'OPENSTREETMAP', 'OSM:WAY:1436593999',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1436593999')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Esturãos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6267702)) <= 0.00070
               AND ABS(v.longitude - (-7.4022685)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1458134054 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Padrela', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5700900, -7.5061193, 'OPENSTREETMAP', 'OSM:WAY:1458134054',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1458134054')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Padrela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5700900)) <= 0.00070
               AND ABS(v.longitude - (-7.5061193)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1034247413 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Rio Bom', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5711339, -7.4788789, 'OPENSTREETMAP', 'OSM:WAY:1034247413',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1034247413')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Rio Bom'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5711339)) <= 0.00070
               AND ABS(v.longitude - (-7.4788789)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1268843262 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São João da Corveira', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5842259, -7.4584073, 'OPENSTREETMAP', 'OSM:WAY:1268843262',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1268843262')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São João da Corveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5842259)) <= 0.00070
               AND ABS(v.longitude - (-7.4584073)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1431368528 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vassal', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.6134409, -7.3501366, 'OPENSTREETMAP', 'OSM:WAY:1431368528',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1431368528')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vassal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6134409)) <= 0.00070
               AND ABS(v.longitude - (-7.3501366)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1268843263 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vilarinho do Monte', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5927903, -7.4588555, 'OPENSTREETMAP', 'OSM:WAY:1268843263',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1268843263')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vilarinho do Monte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5927903)) <= 0.00070
               AND ABS(v.longitude - (-7.4588555)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:950206597 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis', 'Valpaços', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.6117139, -7.3074621, 'OPENSTREETMAP', 'OSM:WAY:950206597',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:950206597')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6117139)) <= 0.00070
               AND ABS(v.longitude - (-7.3074621)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:950206598 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Voleibol de Praia', 'Valpaços', 'Vila Real', NULL,
       'Voleibol de Praia', 'Campo desportivo', NULL,
       41.6118955, -7.3072468, 'OPENSTREETMAP', 'OSM:WAY:950206598',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:950206598')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Voleibol de Praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6118955)) <= 0.00070
               AND ABS(v.longitude - (-7.3072468)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:95988384 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Cruz', 'Valpaços', 'Vila Real', NULL,
       'Futebol', 'Estádio', NULL,
       41.6128215, -7.3071682, 'OPENSTREETMAP', 'OSM:WAY:95988384',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:95988384')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Cruz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6128215)) <= 0.00070
               AND ABS(v.longitude - (-7.3071682)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:942627310 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Valpaços', 'Valpaços', 'Vila Real', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.6124673, -7.3061959, 'OPENSTREETMAP', 'OSM:WAY:942627310',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:942627310')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Valpaços'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6124673)) <= 0.00070
               AND ABS(v.longitude - (-7.3061959)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:823002029 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Valpaços', 'Valpaços', 'Vila Real', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.6129285, -7.3072102, 'OPENSTREETMAP', 'OSM:WAY:823002029',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823002029')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Valpaços'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.6129285)) <= 0.00070
               AND ABS(v.longitude - (-7.3072102)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:810018125 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Rio Bom', 'Valpaços', 'Vila Real', NULL,
       'Futsal;Basquetebol;Ténis', 'Campo desportivo', NULL,
       41.5758680, -7.4830409, 'OPENSTREETMAP', 'OSM:WAY:810018125',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:810018125')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valpaços'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Rio Bom'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5758680)) <= 0.00070
               AND ABS(v.longitude - (-7.4830409)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1284193130 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Balugas', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5329564, -7.5669130, 'OPENSTREETMAP', 'OSM:WAY:1284193130',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1284193130')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Balugas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5329564)) <= 0.00070
               AND ABS(v.longitude - (-7.5669130)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186947805 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Cabanes', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5320012, -7.6676284, 'OPENSTREETMAP', 'OSM:WAY:186947805',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186947805')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Cabanes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5320012)) <= 0.00070
               AND ABS(v.longitude - (-7.6676284)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1284193117 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Lagoa', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5472518, -7.5459642, 'OPENSTREETMAP', 'OSM:WAY:1284193117',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1284193117')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Lagoa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5472518)) <= 0.00070
               AND ABS(v.longitude - (-7.5459642)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1284258210 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Parada de Monteiros', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5562086, -7.7059503, 'OPENSTREETMAP', 'OSM:WAY:1284258210',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1284258210')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Parada de Monteiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5562086)) <= 0.00070
               AND ABS(v.longitude - (-7.7059503)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1136298124 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Tresminas', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4863318, -7.5151888, 'OPENSTREETMAP', 'OSM:WAY:1136298124',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1136298124')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Tresminas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4863318)) <= 0.00070
               AND ABS(v.longitude - (-7.5151888)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1136298118 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Grupo Desportivo e Cultural de Vilarelho', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5044272, -7.5474621, 'OPENSTREETMAP', 'OSM:WAY:1136298118',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1136298118')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Grupo Desportivo e Cultural de Vilarelho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5044272)) <= 0.00070
               AND ABS(v.longitude - (-7.5474621)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:586099076 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.5481211, -7.6063940, 'OPENSTREETMAP', 'OSM:WAY:586099076',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:586099076')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5481211)) <= 0.00070
               AND ABS(v.longitude - (-7.6063940)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385372 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal 1º de Maio', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5033456, -7.6401508, 'OPENSTREETMAP', 'OSM:WAY:157385372',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385372')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5033456)) <= 0.00070
               AND ABS(v.longitude - (-7.6401508)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:843211470 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.4963946, -7.6452919, 'OPENSTREETMAP', 'OSM:WAY:843211470',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:843211470')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4963946)) <= 0.00070
               AND ABS(v.longitude - (-7.6452919)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385158 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Vila Pouca de Aguiar', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.5033525, -7.6401834, 'OPENSTREETMAP', 'OSM:WAY:157385158',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385158')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Vila Pouca de Aguiar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5033525)) <= 0.00070
               AND ABS(v.longitude - (-7.6401834)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:911601709 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Barbadães de Cima', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5528961, -7.5505694, 'OPENSTREETMAP', 'OSM:WAY:911601709',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:911601709')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Barbadães de Cima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5528961)) <= 0.00070
               AND ABS(v.longitude - (-7.5505694)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186947795 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Cabanes', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.5416559, -7.6627116, 'OPENSTREETMAP', 'OSM:WAY:186947795',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186947795')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Cabanes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.5416559)) <= 0.00070
               AND ABS(v.longitude - (-7.6627116)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1039417019 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Tresminas', 'Vila Pouca de Aguiar', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4864764, -7.5157840, 'OPENSTREETMAP', 'OSM:WAY:1039417019',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1039417019')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Pouca de Aguiar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Tresminas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4864764)) <= 0.00070
               AND ABS(v.longitude - (-7.5157840)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9023048863 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Alvão Zen', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3040180, -7.7451461, 'OPENSTREETMAP', 'OSM:NODE:9023048863',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9023048863')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Alvão Zen'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3040180)) <= 0.00070
               AND ABS(v.longitude - (-7.7451461)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12051535789 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Físico-Desportiva e Cultural da Sardoeira', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2819183, -7.8478354, 'OPENSTREETMAP', 'OSM:NODE:12051535789',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12051535789')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Físico-Desportiva e Cultural da Sardoeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2819183)) <= 0.00070
               AND ABS(v.longitude - (-7.8478354)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12108005518 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CalisCross Box', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3145107, -7.7327108, 'OPENSTREETMAP', 'OSM:NODE:12108005518',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12108005518')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CalisCross Box'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3145107)) <= 0.00070
               AND ABS(v.longitude - (-7.7327108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:901693395 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Eira da Pedra', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3063002, -7.7846635, 'OPENSTREETMAP', 'OSM:WAY:901693395',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:901693395')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Eira da Pedra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3063002)) <= 0.00070
               AND ABS(v.longitude - (-7.7846635)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183332145 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquetebol dos Quinchosos', 'Vila Real', 'Vila Real', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       41.2988372, -7.7483596, 'OPENSTREETMAP', 'OSM:WAY:183332145',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183332145')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquetebol dos Quinchosos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2988372)) <= 0.00070
               AND ABS(v.longitude - (-7.7483596)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975421 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Bouça', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3159049, -7.6987446, 'OPENSTREETMAP', 'OSM:WAY:1164975421',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975421')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Bouça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3159049)) <= 0.00070
               AND ABS(v.longitude - (-7.6987446)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164712513 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Campeã', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2855581, -7.8546874, 'OPENSTREETMAP', 'OSM:WAY:1164712513',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164712513')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Campeã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2855581)) <= 0.00070
               AND ABS(v.longitude - (-7.8546874)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1104826896 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Granja', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2735351, -7.7832769, 'OPENSTREETMAP', 'OSM:WAY:1104826896',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1104826896')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Granja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2735351)) <= 0.00070
               AND ABS(v.longitude - (-7.7832769)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:960732699 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Portela', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2668919, -7.7029390, 'OPENSTREETMAP', 'OSM:WAY:960732699',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:960732699')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2668919)) <= 0.00070
               AND ABS(v.longitude - (-7.7029390)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385276 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Samardã', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4095552, -7.7139261, 'OPENSTREETMAP', 'OSM:WAY:157385276',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385276')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Samardã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4095552)) <= 0.00070
               AND ABS(v.longitude - (-7.7139261)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1022558038 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Abaças', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2263243, -7.6885471, 'OPENSTREETMAP', 'OSM:WAY:1022558038',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1022558038')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Abaças'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2263243)) <= 0.00070
               AND ABS(v.longitude - (-7.6885471)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975420 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Abobeleira', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3297005, -7.6889677, 'OPENSTREETMAP', 'OSM:WAY:1164975420',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975420')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Abobeleira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3297005)) <= 0.00070
               AND ABS(v.longitude - (-7.6889677)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297365 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Agarez', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3241884, -7.7843823, 'OPENSTREETMAP', 'OSM:WAY:1168297365',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297365')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Agarez'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3241884)) <= 0.00070
               AND ABS(v.longitude - (-7.7843823)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1035111010 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Águas Santas', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3681689, -7.6552165, 'OPENSTREETMAP', 'OSM:WAY:1035111010',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1035111010')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Águas Santas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3681689)) <= 0.00070
               AND ABS(v.longitude - (-7.6552165)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1039380478 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Alvites', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3211079, -7.6724690, 'OPENSTREETMAP', 'OSM:WAY:1039380478',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1039380478')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Alvites'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3211079)) <= 0.00070
               AND ABS(v.longitude - (-7.6724690)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:942956005 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Andrães', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2694261, -7.6901273, 'OPENSTREETMAP', 'OSM:WAY:942956005',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:942956005')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Andrães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2694261)) <= 0.00070
               AND ABS(v.longitude - (-7.6901273)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297354 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Arnadelo', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2640875, -7.7974677, 'OPENSTREETMAP', 'OSM:WAY:1168297354',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297354')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Arnadelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2640875)) <= 0.00070
               AND ABS(v.longitude - (-7.7974677)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1105937239 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Arrabães', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2791391, -7.8236686, 'OPENSTREETMAP', 'OSM:WAY:1105937239',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1105937239')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Arrabães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2791391)) <= 0.00070
               AND ABS(v.longitude - (-7.8236686)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1132456721 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Arroios', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2905588, -7.7110480, 'OPENSTREETMAP', 'OSM:WAY:1132456721',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1132456721')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Arroios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2905588)) <= 0.00070
               AND ABS(v.longitude - (-7.7110480)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385332 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Benagouro', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3716451, -7.7130334, 'OPENSTREETMAP', 'OSM:WAY:157385332',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385332')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Benagouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3716451)) <= 0.00070
               AND ABS(v.longitude - (-7.7130334)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975415 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Bisalhães', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2890087, -7.7774312, 'OPENSTREETMAP', 'OSM:WAY:1164975415',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975415')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Bisalhães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2890087)) <= 0.00070
               AND ABS(v.longitude - (-7.7774312)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385342 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Borbela', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3302880, -7.7562572, 'OPENSTREETMAP', 'OSM:WAY:157385342',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385342')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Borbela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3302880)) <= 0.00070
               AND ABS(v.longitude - (-7.7562572)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164691737 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Bujões', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2028540, -7.6969892, 'OPENSTREETMAP', 'OSM:WAY:1164691737',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164691737')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Bujões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2028540)) <= 0.00070
               AND ABS(v.longitude - (-7.6969892)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385335 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Escariz', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3623042, -7.7214252, 'OPENSTREETMAP', 'OSM:WAY:157385335',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385335')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Escariz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3623042)) <= 0.00070
               AND ABS(v.longitude - (-7.7214252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297345 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Fortunho', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3648322, -7.6966144, 'OPENSTREETMAP', 'OSM:WAY:1168297345',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297345')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Fortunho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3648322)) <= 0.00070
               AND ABS(v.longitude - (-7.6966144)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1137129510 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Gache', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3168930, -7.6447782, 'OPENSTREETMAP', 'OSM:WAY:1137129510',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1137129510')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Gache'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3168930)) <= 0.00070
               AND ABS(v.longitude - (-7.6447782)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1029015088 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Galegos', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2818359, -7.6682670, 'OPENSTREETMAP', 'OSM:WAY:1029015088',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1029015088')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Galegos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2818359)) <= 0.00070
               AND ABS(v.longitude - (-7.6682670)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Gontães', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2943543, -7.8388427, 'OPENSTREETMAP', 'OSM:WAY:1168297339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Gontães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2943543)) <= 0.00070
               AND ABS(v.longitude - (-7.8388427)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385230 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Gravelos', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3377612, -7.7253635, 'OPENSTREETMAP', 'OSM:WAY:157385230',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385230')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Gravelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3377612)) <= 0.00070
               AND ABS(v.longitude - (-7.7253635)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164691751 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Jorjais', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2326350, -7.6790146, 'OPENSTREETMAP', 'OSM:WAY:1164691751',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164691751')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Jorjais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2326350)) <= 0.00070
               AND ABS(v.longitude - (-7.6790146)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975422 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Jorjais', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3400889, -7.6997310, 'OPENSTREETMAP', 'OSM:WAY:1164975422',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975422')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Jorjais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3400889)) <= 0.00070
               AND ABS(v.longitude - (-7.6997310)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1165278207 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Justes', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3542735, -7.6199296, 'OPENSTREETMAP', 'OSM:WAY:1165278207',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1165278207')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Justes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3542735)) <= 0.00070
               AND ABS(v.longitude - (-7.6199296)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385246 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Lamares', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3343242, -7.6370818, 'OPENSTREETMAP', 'OSM:WAY:157385246',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385246')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Lamares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3343242)) <= 0.00070
               AND ABS(v.longitude - (-7.6370818)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164712510 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Lamas de Olo', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3707717, -7.8061971, 'OPENSTREETMAP', 'OSM:WAY:1164712510',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164712510')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Lamas de Olo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3707717)) <= 0.00070
               AND ABS(v.longitude - (-7.8061971)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297349 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Leirós', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3581379, -7.6398865, 'OPENSTREETMAP', 'OSM:WAY:1168297349',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297349')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Leirós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3581379)) <= 0.00070
               AND ABS(v.longitude - (-7.6398865)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1077773911 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Linhares', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3614740, -7.6547951, 'OPENSTREETMAP', 'OSM:WAY:1077773911',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1077773911')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Linhares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3614740)) <= 0.00070
               AND ABS(v.longitude - (-7.6547951)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385302 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Lordelo', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3235970, -7.7680958, 'OPENSTREETMAP', 'OSM:WAY:157385302',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385302')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Lordelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3235970)) <= 0.00070
               AND ABS(v.longitude - (-7.7680958)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1029015125 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Ludares', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2974408, -7.6530997, 'OPENSTREETMAP', 'OSM:WAY:1029015125',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1029015125')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Ludares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2974408)) <= 0.00070
               AND ABS(v.longitude - (-7.6530997)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1151958343 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Magalhã', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2360727, -7.6585155, 'OPENSTREETMAP', 'OSM:WAY:1151958343',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1151958343')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Magalhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2360727)) <= 0.00070
               AND ABS(v.longitude - (-7.6585155)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Moçães', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2693091, -7.8114435, 'OPENSTREETMAP', 'OSM:WAY:1168297358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Moçães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2693091)) <= 0.00070
               AND ABS(v.longitude - (-7.8114435)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1210786451 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Mosteirô', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2560957, -7.7026674, 'OPENSTREETMAP', 'OSM:WAY:1210786451',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1210786451')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Mosteirô'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2560957)) <= 0.00070
               AND ABS(v.longitude - (-7.7026674)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975425 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Nogueira', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2175611, -7.7378455, 'OPENSTREETMAP', 'OSM:WAY:1164975425',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975425')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Nogueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2175611)) <= 0.00070
               AND ABS(v.longitude - (-7.7378455)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1085873834 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Outeiro', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3404860, -7.7495502, 'OPENSTREETMAP', 'OSM:WAY:1085873834',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1085873834')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Outeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3404860)) <= 0.00070
               AND ABS(v.longitude - (-7.7495502)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802722802 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Parada de Cunhos', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2889148, -7.7549799, 'OPENSTREETMAP', 'OSM:WAY:802722802',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802722802')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Parada de Cunhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2889148)) <= 0.00070
               AND ABS(v.longitude - (-7.7549799)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1080811361 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Paredes', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3625796, -7.7360112, 'OPENSTREETMAP', 'OSM:WAY:1080811361',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1080811361')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Paredes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3625796)) <= 0.00070
               AND ABS(v.longitude - (-7.7360112)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1125189436 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Pomarelhos', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2575752, -7.7828401, 'OPENSTREETMAP', 'OSM:WAY:1125189436',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1125189436')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Pomarelhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2575752)) <= 0.00070
               AND ABS(v.longitude - (-7.7828401)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1094649941 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Ponte', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3172710, -7.7079388, 'OPENSTREETMAP', 'OSM:WAY:1094649941',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1094649941')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Ponte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3172710)) <= 0.00070
               AND ABS(v.longitude - (-7.7079388)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:170241975 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Sabroso', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2628485, -7.7353734, 'OPENSTREETMAP', 'OSM:WAY:170241975',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:170241975')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Sabroso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2628485)) <= 0.00070
               AND ABS(v.longitude - (-7.7353734)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385277 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Sanguinhedo', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3355840, -7.6537379, 'OPENSTREETMAP', 'OSM:WAY:157385277',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385277')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Sanguinhedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3355840)) <= 0.00070
               AND ABS(v.longitude - (-7.6537379)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297351 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Bento', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3768238, -7.6799281, 'OPENSTREETMAP', 'OSM:WAY:1168297351',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297351')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Bento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3768238)) <= 0.00070
               AND ABS(v.longitude - (-7.6799281)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1137673926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Cibrão', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2690918, -7.6651445, 'OPENSTREETMAP', 'OSM:WAY:1137673926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1137673926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Cibrão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2690918)) <= 0.00070
               AND ABS(v.longitude - (-7.6651445)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1036011275 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Cosme', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3494701, -7.6876490, 'OPENSTREETMAP', 'OSM:WAY:1036011275',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1036011275')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Cosme'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3494701)) <= 0.00070
               AND ABS(v.longitude - (-7.6876490)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Sigarrosa', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3289635, -7.7020947, 'OPENSTREETMAP', 'OSM:WAY:1164975424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Sigarrosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3289635)) <= 0.00070
               AND ABS(v.longitude - (-7.7020947)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975426 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Tanha', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2337453, -7.7133806, 'OPENSTREETMAP', 'OSM:WAY:1164975426',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975426')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Tanha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2337453)) <= 0.00070
               AND ABS(v.longitude - (-7.7133806)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297363 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Torgueda', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2739465, -7.8220306, 'OPENSTREETMAP', 'OSM:WAY:1168297363',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297363')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Torgueda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2739465)) <= 0.00070
               AND ABS(v.longitude - (-7.8220306)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1029015070 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vale de Nogueiras', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2959692, -7.6752700, 'OPENSTREETMAP', 'OSM:WAY:1029015070',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1029015070')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vale de Nogueiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2959692)) <= 0.00070
               AND ABS(v.longitude - (-7.6752700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1123180420 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Varge', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3163490, -7.6991984, 'OPENSTREETMAP', 'OSM:WAY:1123180420',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1123180420')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Varge'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3163490)) <= 0.00070
               AND ABS(v.longitude - (-7.6991984)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297344 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vila Cova', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3105905, -7.8506202, 'OPENSTREETMAP', 'OSM:WAY:1168297344',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297344')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vila Cova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3105905)) <= 0.00070
               AND ABS(v.longitude - (-7.8506202)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297352 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vila Meã', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3611878, -7.6624244, 'OPENSTREETMAP', 'OSM:WAY:1168297352',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297352')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vila Meã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3611878)) <= 0.00070
               AND ABS(v.longitude - (-7.6624244)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164691742 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vilarinho', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3866973, -7.7019444, 'OPENSTREETMAP', 'OSM:WAY:1164691742',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164691742')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vilarinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3866973)) <= 0.00070
               AND ABS(v.longitude - (-7.7019444)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385257 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vila Seca', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3363267, -7.7226206, 'OPENSTREETMAP', 'OSM:WAY:157385257',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385257')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vila Seca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3363267)) <= 0.00070
               AND ABS(v.longitude - (-7.7226206)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119333092 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Calvário', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3012172, -7.7420845, 'OPENSTREETMAP', 'OSM:WAY:119333092',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119333092')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Calvário'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3012172)) <= 0.00070
               AND ABS(v.longitude - (-7.7420845)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157385329 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Couto', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3455546, -7.7447982, 'OPENSTREETMAP', 'OSM:WAY:157385329',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157385329')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Couto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3455546)) <= 0.00070
               AND ABS(v.longitude - (-7.7447982)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802716827 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Piaget', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2976344, -7.7538717, 'OPENSTREETMAP', 'OSM:WAY:802716827',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802716827')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Piaget'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2976344)) <= 0.00070
               AND ABS(v.longitude - (-7.7538717)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1530821246 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futsal do NaturWaterPark', 'Vila Real', 'Vila Real', NULL,
       'Futsal', 'Campo desportivo', NULL,
       41.2563431, -7.6510260, 'OPENSTREETMAP', 'OSM:WAY:1530821246',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1530821246')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futsal do NaturWaterPark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2563431)) <= 0.00070
               AND ABS(v.longitude - (-7.6510260)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:818875572 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Areia', 'Vila Real', 'Vila Real', NULL,
       'Futebol;Voleibol de Praia', 'Campo desportivo', NULL,
       41.2832067, -7.7342074, 'OPENSTREETMAP', 'OSM:WAY:818875572',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:818875572')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Areia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2832067)) <= 0.00070
               AND ABS(v.longitude - (-7.7342074)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:402033506 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos (Resinosas)', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2869911, -7.7387363, 'OPENSTREETMAP', 'OSM:WAY:402033506',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:402033506')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos (Resinosas)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2869911)) <= 0.00070
               AND ABS(v.longitude - (-7.7387363)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183008801 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis das Flores', 'Vila Real', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.3148767, -7.7314189, 'OPENSTREETMAP', 'OSM:WAY:183008801',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183008801')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis das Flores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3148767)) <= 0.00070
               AND ABS(v.longitude - (-7.7314189)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1530821252 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro ao Alvo', 'Vila Real', 'Vila Real', NULL,
       'Tiro com Arco', 'Campo desportivo', NULL,
       41.2570226, -7.6497776, 'OPENSTREETMAP', 'OSM:WAY:1530821252',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1530821252')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro ao Alvo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2570226)) <= 0.00070
               AND ABS(v.longitude - (-7.6497776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1092907542 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Voleibol da Estação', 'Vila Real', 'Vila Real', NULL,
       'Voleibol', 'Campo desportivo', NULL,
       41.2949118, -7.7384557, 'OPENSTREETMAP', 'OSM:WAY:1092907542',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1092907542')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Voleibol da Estação'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2949118)) <= 0.00070
               AND ABS(v.longitude - (-7.7384557)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:180192464 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Académico Alves Roçadas', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3038721, -7.7456252, 'OPENSTREETMAP', 'OSM:WAY:180192464',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:180192464')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Académico Alves Roçadas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3038721)) <= 0.00070
               AND ABS(v.longitude - (-7.7456252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158742583 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Dom Francisco de Sousa Albuquerque', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2965441, -7.7167249, 'OPENSTREETMAP', 'OSM:WAY:158742583',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158742583')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Dom Francisco de Sousa Albuquerque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2965441)) <= 0.00070
               AND ABS(v.longitude - (-7.7167249)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802728740 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Dona Maria da Piedade Amaral', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3023916, -7.7282692, 'OPENSTREETMAP', 'OSM:WAY:802728740',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802728740')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Dona Maria da Piedade Amaral'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3023916)) <= 0.00070
               AND ABS(v.longitude - (-7.7282692)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183021339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Dona Maria de Lurdes do Amaral', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3029362, -7.7290008, 'OPENSTREETMAP', 'OSM:WAY:183021339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183021339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Dona Maria de Lurdes do Amaral'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3029362)) <= 0.00070
               AND ABS(v.longitude - (-7.7290008)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1530821247 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Padel do NaturWaterPark', 'Vila Real', 'Vila Real', NULL,
       'Padel', 'Campo desportivo', NULL,
       41.2565533, -7.6510148, 'OPENSTREETMAP', 'OSM:WAY:1530821247',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1530821247')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de Padel do NaturWaterPark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2565533)) <= 0.00070
               AND ABS(v.longitude - (-7.6510148)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158742562 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2832016, -7.7350371, 'OPENSTREETMAP', 'OSM:WAY:158742562',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158742562')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2832016)) <= 0.00070
               AND ABS(v.longitude - (-7.7350371)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164712517 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       41.2768414, -7.7181556, 'OPENSTREETMAP', 'OSM:WAY:1164712517',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164712517')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2768414)) <= 0.00070
               AND ABS(v.longitude - (-7.7181556)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9037857002 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Elite Crossbox', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3027640, -7.7415747, 'OPENSTREETMAP', 'OSM:NODE:9037857002',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9037857002')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Elite Crossbox'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3027640)) <= 0.00070
               AND ABS(v.longitude - (-7.7415747)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164712515 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Cruzeiro', 'Vila Real', 'Vila Real', NULL,
       'Futebol;Atletismo', 'Estádio', NULL,
       41.2797457, -7.7041623, 'OPENSTREETMAP', 'OSM:WAY:1164712515',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164712515')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Cruzeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2797457)) <= 0.00070
               AND ABS(v.longitude - (-7.7041623)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1039389721 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Estádio', NULL,
       41.2875699, -7.7557939, 'OPENSTREETMAP', 'OSM:WAY:1039389721',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1039389721')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2875699)) <= 0.00070
               AND ABS(v.longitude - (-7.7557939)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802999667 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2945794, -7.7359825, 'OPENSTREETMAP', 'OSM:WAY:802999667',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802999667')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2945794)) <= 0.00070
               AND ABS(v.longitude - (-7.7359825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9037867841 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '+Fit Wellness Studio', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3041106, -7.7394319, 'OPENSTREETMAP', 'OSM:NODE:9037867841',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9037867841')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('+Fit Wellness Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3041106)) <= 0.00070
               AND ABS(v.longitude - (-7.7394319)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11496002468 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GimnosCult', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2971119, -7.7489087, 'OPENSTREETMAP', 'OSM:NODE:11496002468',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11496002468')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GimnosCult'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2971119)) <= 0.00070
               AND ABS(v.longitude - (-7.7489087)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9859077977 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio BodyStation', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2995512, -7.7237857, 'OPENSTREETMAP', 'OSM:NODE:9859077977',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9859077977')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio BodyStation'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2995512)) <= 0.00070
               AND ABS(v.longitude - (-7.7237857)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11498940828 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Impulse', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3045160, -7.7424258, 'OPENSTREETMAP', 'OSM:NODE:11498940828',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11498940828')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Impulse'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3045160)) <= 0.00070
               AND ABS(v.longitude - (-7.7424258)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9023048906 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mega Ginásio Montezelos', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3074504, -7.7446140, 'OPENSTREETMAP', 'OSM:NODE:9023048906',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9023048906')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mega Ginásio Montezelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3074504)) <= 0.00070
               AND ABS(v.longitude - (-7.7446140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Arnadelo', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2620324, -7.7892633, 'OPENSTREETMAP', 'OSM:WAY:1168297355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Arnadelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2620324)) <= 0.00070
               AND ABS(v.longitude - (-7.7892633)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297356 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Arrabães', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2842913, -7.8103128, 'OPENSTREETMAP', 'OSM:WAY:1168297356',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297356')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Arrabães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2842913)) <= 0.00070
               AND ABS(v.longitude - (-7.8103128)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1151958335 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Magalhã', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2407602, -7.6564904, 'OPENSTREETMAP', 'OSM:WAY:1151958335',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1151958335')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Magalhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2407602)) <= 0.00070
               AND ABS(v.longitude - (-7.6564904)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1080811355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Paredes', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3618647, -7.7335423, 'OPENSTREETMAP', 'OSM:WAY:1080811355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1080811355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Paredes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3618647)) <= 0.00070
               AND ABS(v.longitude - (-7.7335423)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164975419 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Quintelas', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3160287, -7.7975572, 'OPENSTREETMAP', 'OSM:WAY:1164975419',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164975419')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Quintelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3160287)) <= 0.00070
               AND ABS(v.longitude - (-7.7975572)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297343 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Sirarelhos', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3190465, -7.8043627, 'OPENSTREETMAP', 'OSM:WAY:1168297343',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297343')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Sirarelhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3190465)) <= 0.00070
               AND ABS(v.longitude - (-7.8043627)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
