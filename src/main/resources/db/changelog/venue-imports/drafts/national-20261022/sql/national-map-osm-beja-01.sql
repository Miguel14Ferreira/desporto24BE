-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:12412677102 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'R Private Training Space', 'Beja', 'Beja', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.0117304, -7.8594963, 'OPENSTREETMAP', 'OSM:NODE:12412677102',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12412677102')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('R Private Training Space'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0117304)) <= 0.00070
               AND ABS(v.longitude - (-7.8594963)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:116417841 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Odemira', 'Beja', NULL,
       'Natação', 'Piscina', NULL,
       37.5967643, -8.6306285, 'OPENSTREETMAP', 'OSM:WAY:116417841',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:116417841')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5967643)) <= 0.00070
               AND ABS(v.longitude - (-8.6306285)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:442512394 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 25 de Abril', 'Aljustrel', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9541283, -8.1851462, 'OPENSTREETMAP', 'OSM:WAY:442512394',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:442512394')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljustrel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9541283)) <= 0.00070
               AND ABS(v.longitude - (-8.1851462)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:839620469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Messejana', 'Aljustrel', 'Beja', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       37.8312334, -8.2340058, 'OPENSTREETMAP', 'OSM:WAY:839620469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:839620469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljustrel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Messejana'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8312334)) <= 0.00070
               AND ABS(v.longitude - (-8.2340058)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:487096047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Rio de Moinhos', 'Aljustrel', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.8980567, -8.2139142, 'OPENSTREETMAP', 'OSM:WAY:487096047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:487096047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljustrel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Rio de Moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8980567)) <= 0.00070
               AND ABS(v.longitude - (-8.2139142)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:336064380 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo do Alvorada de Ervidel', 'Aljustrel', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9621956, -8.0854882, 'OPENSTREETMAP', 'OSM:WAY:336064380',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:336064380')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljustrel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo do Alvorada de Ervidel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9621956)) <= 0.00070
               AND ABS(v.longitude - (-8.0854882)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:113623669 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Aljustrel-2', 'Aljustrel', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.8842576, -8.1622682, 'OPENSTREETMAP', 'OSM:WAY:113623669',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:113623669')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljustrel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Aljustrel-2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8842576)) <= 0.00070
               AND ABS(v.longitude - (-8.1622682)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:383315137 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas', 'Aljustrel', 'Beja', NULL,
       'Natação', 'Piscina', NULL,
       37.8318554, -8.2486090, 'OPENSTREETMAP', 'OSM:WAY:383315137',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:383315137')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljustrel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8318554)) <= 0.00070
               AND ABS(v.longitude - (-8.2486090)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1551585649 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol 1º de Maio', 'Almodôvar', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6100113, -8.0782090, 'OPENSTREETMAP', 'OSM:WAY:1551585649',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1551585649')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almodôvar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6100113)) <= 0.00070
               AND ABS(v.longitude - (-8.0782090)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:858192690 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Aldeia dos Fernandes', 'Almodôvar', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5721360, -8.1726725, 'OPENSTREETMAP', 'OSM:WAY:858192690',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:858192690')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almodôvar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Aldeia dos Fernandes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5721360)) <= 0.00070
               AND ABS(v.longitude - (-8.1726725)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:993317203 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Santa Clara-a-Nova', 'Almodôvar', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.4868569, -8.1406300, 'OPENSTREETMAP', 'OSM:WAY:993317203',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:993317203')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almodôvar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Santa Clara-a-Nova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.4868569)) <= 0.00070
               AND ABS(v.longitude - (-8.1406300)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119155702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Almodôvar', 'Almodôvar', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5261828, -8.0621710, 'OPENSTREETMAP', 'OSM:WAY:119155702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119155702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almodôvar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Almodôvar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5261828)) <= 0.00070
               AND ABS(v.longitude - (-8.0621710)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:159066377 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal José Joaquim Branquinho', 'Alvito', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.2607818, -7.9937657, 'OPENSTREETMAP', 'OSM:WAY:159066377',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:159066377')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alvito'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal José Joaquim Branquinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2607818)) <= 0.00070
               AND ABS(v.longitude - (-7.9937657)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1172768238 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Alvito', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.2600123, -7.9931023, 'OPENSTREETMAP', 'OSM:WAY:1172768238',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1172768238')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alvito'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2600123)) <= 0.00070
               AND ABS(v.longitude - (-7.9931023)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8309645680 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BLive health & fitness', 'Beja', 'Beja', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.0088735, -7.8595238, 'OPENSTREETMAP', 'OSM:NODE:8309645680',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8309645680')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BLive health & fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0088735)) <= 0.00070
               AND ABS(v.longitude - (-7.8595238)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:374748599 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 25 de Abril', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0248593, -7.7254214, 'OPENSTREETMAP', 'OSM:WAY:374748599',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:374748599')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0248593)) <= 0.00070
               AND ABS(v.longitude - (-7.7254214)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:315919253 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol António Francisco Felizardo', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9654663, -7.6999259, 'OPENSTREETMAP', 'OSM:WAY:315919253',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:315919253')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol António Francisco Felizardo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9654663)) <= 0.00070
               AND ABS(v.longitude - (-7.6999259)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:190600076 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Mombeja', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0262161, -8.0377473, 'OPENSTREETMAP', 'OSM:WAY:190600076',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:190600076')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Mombeja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0262161)) <= 0.00070
               AND ABS(v.longitude - (-8.0377473)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:442510110 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Matias', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1067094, -7.8595000, 'OPENSTREETMAP', 'OSM:WAY:442510110',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:442510110')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Matias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1067094)) <= 0.00070
               AND ABS(v.longitude - (-7.8595000)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1551796355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Trigaches', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0961892, -7.9711974, 'OPENSTREETMAP', 'OSM:WAY:1551796355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1551796355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Trigaches'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0961892)) <= 0.00070
               AND ABS(v.longitude - (-7.9711974)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:115028725 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Municipal de Beja', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0128208, -7.8772411, 'OPENSTREETMAP', 'OSM:WAY:115028725',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:115028725')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Municipal de Beja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0128208)) <= 0.00070
               AND ABS(v.longitude - (-7.8772411)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:110797081 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Carolina Almodôvar Fernandes', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9819411, -7.9179381, 'OPENSTREETMAP', 'OSM:WAY:110797081',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:110797081')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Carolina Almodôvar Fernandes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9819411)) <= 0.00070
               AND ABS(v.longitude - (-7.9179381)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:315549513 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Nossa Senhora das Neves', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0179376, -7.8065600, 'OPENSTREETMAP', 'OSM:WAY:315549513',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:315549513')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Nossa Senhora das Neves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0179376)) <= 0.00070
               AND ABS(v.longitude - (-7.8065600)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:377693976 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Dona Maria Luiza da Costa Menezes Blanco Gomes', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.8858832, -7.8920942, 'OPENSTREETMAP', 'OSM:WAY:377693976',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:377693976')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Dona Maria Luiza da Costa Menezes Blanco Gomes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8858832)) <= 0.00070
               AND ABS(v.longitude - (-7.8920942)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:190598340 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Jorge do Ó', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9653686, -8.0221645, 'OPENSTREETMAP', 'OSM:WAY:190598340',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:190598340')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Jorge do Ó'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9653686)) <= 0.00070
               AND ABS(v.longitude - (-8.0221645)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:357548526 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos José Cardador', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9677186, -7.8722016, 'OPENSTREETMAP', 'OSM:WAY:357548526',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:357548526')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos José Cardador'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9677186)) <= 0.00070
               AND ABS(v.longitude - (-7.8722016)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309590205 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Manuel António Saramago Peste', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.8627722, -7.9585693, 'OPENSTREETMAP', 'OSM:WAY:309590205',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309590205')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Manuel António Saramago Peste'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8627722)) <= 0.00070
               AND ABS(v.longitude - (-7.9585693)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:314423032 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Terras de Pão', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9373065, -7.7718454, 'OPENSTREETMAP', 'OSM:WAY:314423032',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:314423032')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Terras de Pão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9373065)) <= 0.00070
               AND ABS(v.longitude - (-7.7718454)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:639838950 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos Unidos', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0515016, -7.9772303, 'OPENSTREETMAP', 'OSM:WAY:639838950',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:639838950')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos Unidos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0515016)) <= 0.00070
               AND ABS(v.longitude - (-7.9772303)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1189819331 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Circuito de Manutenção', 'Beja', 'Beja', NULL,
       'Atletismo', 'Pista desportiva', NULL,
       38.0171150, -7.8758041, 'OPENSTREETMAP', 'OSM:WAY:1189819331',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1189819331')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Circuito de Manutenção'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0171150)) <= 0.00070
               AND ABS(v.longitude - (-7.8758041)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:69392297 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Circuito de Manutenção', 'Beja', 'Beja', NULL,
       'Atletismo', 'Pista desportiva', NULL,
       38.0185897, -7.8760788, 'OPENSTREETMAP', 'OSM:WAY:69392297',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:69392297')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Circuito de Manutenção'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0185897)) <= 0.00070
               AND ABS(v.longitude - (-7.8760788)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102399256 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio José Agostinho de Matos', 'Beja', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9167616, -7.8023191, 'OPENSTREETMAP', 'OSM:WAY:102399256',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102399256')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio José Agostinho de Matos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9167616)) <= 0.00070
               AND ABS(v.longitude - (-7.8023191)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:64946845 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Flávio dos Santos', 'Beja', 'Beja', NULL,
       'Futebol', 'Estádio', NULL,
       38.0077615, -7.8611611, 'OPENSTREETMAP', 'OSM:WAY:64946845',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:64946845')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Flávio dos Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0077615)) <= 0.00070
               AND ABS(v.longitude - (-7.8611611)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12412676901 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kettleclub', 'Beja', 'Beja', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.0062017, -7.8656769, 'OPENSTREETMAP', 'OSM:NODE:12412676901',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12412676901')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kettleclub'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0062017)) <= 0.00070
               AND ABS(v.longitude - (-7.8656769)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:68995080 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta', 'Beja', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.0107792, -7.8756996, 'OPENSTREETMAP', 'OSM:WAY:68995080',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:68995080')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0107792)) <= 0.00070
               AND ABS(v.longitude - (-7.8756996)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105872842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Beja', 'Beja', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.0089314, -7.8609680, 'OPENSTREETMAP', 'OSM:WAY:105872842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105872842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Beja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0089314)) <= 0.00070
               AND ABS(v.longitude - (-7.8609680)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:547991326 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo Fernando Mamede', 'Beja', 'Beja', NULL,
       'Corrida', 'Pista desportiva', NULL,
       38.0154163, -7.8767301, 'OPENSTREETMAP', 'OSM:WAY:547991326',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:547991326')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Beja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo Fernando Mamede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0154163)) <= 0.00070
               AND ABS(v.longitude - (-7.8767301)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182780587 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 2', 'Castro Verde', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7043650, -8.0930058, 'OPENSTREETMAP', 'OSM:WAY:182780587',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182780587')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7043650)) <= 0.00070
               AND ABS(v.longitude - (-8.0930058)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309590201 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol António José Marques', 'Castro Verde', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7765452, -8.0151204, 'OPENSTREETMAP', 'OSM:WAY:309590201',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309590201')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol António José Marques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7765452)) <= 0.00070
               AND ABS(v.longitude - (-8.0151204)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:217703448 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos 25 de Abril', 'Castro Verde', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6158673, -7.9555573, 'OPENSTREETMAP', 'OSM:WAY:217703448',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:217703448')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6158673)) <= 0.00070
               AND ABS(v.longitude - (-7.9555573)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:210305094 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo João Celorico Drago', 'Castro Verde', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7059503, -7.9375977, 'OPENSTREETMAP', 'OSM:WAY:210305094',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:210305094')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo João Celorico Drago'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7059503)) <= 0.00070
               AND ABS(v.longitude - (-7.9375977)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:828596435 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Simplificada de Castro Verde', 'Castro Verde', 'Beja', NULL,
       'Corrida', 'Pista desportiva', NULL,
       37.7034073, -8.0912096, 'OPENSTREETMAP', 'OSM:WAY:828596435',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:828596435')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Verde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Simplificada de Castro Verde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7034073)) <= 0.00070
               AND ABS(v.longitude - (-8.0912096)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:554674326 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol António Joaquim Pestana Baltazar', 'Cuba', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1500372, -7.9440637, 'OPENSTREETMAP', 'OSM:WAY:554674326',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:554674326')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cuba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol António Joaquim Pestana Baltazar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1500372)) <= 0.00070
               AND ABS(v.longitude - (-7.9440637)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:368617971 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Amado Aguilar', 'Cuba', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1671391, -7.8964934, 'OPENSTREETMAP', 'OSM:WAY:368617971',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:368617971')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cuba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Amado Aguilar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1671391)) <= 0.00070
               AND ABS(v.longitude - (-7.8964934)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:115028100 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Multi-desportos de Cuba', 'Cuba', 'Beja', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       38.1726582, -7.8918342, 'OPENSTREETMAP', 'OSM:WAY:115028100',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:115028100')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cuba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Multi-desportos de Cuba'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1726582)) <= 0.00070
               AND ABS(v.longitude - (-7.8918342)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:488161488 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta Municipal de Cuba', 'Cuba', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.1679480, -7.8960184, 'OPENSTREETMAP', 'OSM:WAY:488161488',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:488161488')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cuba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta Municipal de Cuba'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1679480)) <= 0.00070
               AND ABS(v.longitude - (-7.8960184)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1033327747 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Cuba', 'Cuba', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.1676149, -7.8956108, 'OPENSTREETMAP', 'OSM:NODE:1033327747',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1033327747')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cuba'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Cuba'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1676149)) <= 0.00070
               AND ABS(v.longitude - (-7.8956108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:542672708 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol das Amarelas', 'Ferreira do Alentejo', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0911450, -8.2033161, 'OPENSTREETMAP', 'OSM:WAY:542672708',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:542672708')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ferreira do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol das Amarelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0911450)) <= 0.00070
               AND ABS(v.longitude - (-8.2033161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:554933571 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Peroguarda', 'Ferreira do Alentejo', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0913818, -8.0443584, 'OPENSTREETMAP', 'OSM:WAY:554933571',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:554933571')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ferreira do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Peroguarda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0913818)) <= 0.00070
               AND ABS(v.longitude - (-8.0443584)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1443451327 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo José António Chalaça', 'Ferreira do Alentejo', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1167088, -8.0649861, 'OPENSTREETMAP', 'OSM:WAY:1443451327',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1443451327')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ferreira do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo José António Chalaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1167088)) <= 0.00070
               AND ABS(v.longitude - (-8.0649861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135805285 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Ferreira do Alentejo', 'Ferreira do Alentejo', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0534998, -8.1160799, 'OPENSTREETMAP', 'OSM:WAY:135805285',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135805285')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ferreira do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Ferreira do Alentejo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0534998)) <= 0.00070
               AND ABS(v.longitude - (-8.1160799)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1154028242 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Ferreira do Alentejo', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.0563143, -8.1187162, 'OPENSTREETMAP', 'OSM:WAY:1154028242',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1154028242')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ferreira do Alentejo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0563143)) <= 0.00070
               AND ABS(v.longitude - (-8.1187162)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:891992191 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Algodor', 'Mértola', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7438577, -7.7910562, 'OPENSTREETMAP', 'OSM:WAY:891992191',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:891992191')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mértola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Algodor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7438577)) <= 0.00070
               AND ABS(v.longitude - (-7.7910562)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:858192694 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Corte Sines', 'Mértola', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7054989, -7.6084832, 'OPENSTREETMAP', 'OSM:WAY:858192694',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:858192694')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mértola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Corte Sines'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7054989)) <= 0.00070
               AND ABS(v.longitude - (-7.6084832)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:184499560 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Cross Brown', 'Mértola', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6714777, -7.4995720, 'OPENSTREETMAP', 'OSM:WAY:184499560',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:184499560')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mértola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Cross Brown'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6714777)) <= 0.00070
               AND ABS(v.longitude - (-7.4995720)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:393391134 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Alcaria Ruiva', 'Mértola', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7071641, -7.7797029, 'OPENSTREETMAP', 'OSM:WAY:393391134',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:393391134')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mértola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Alcaria Ruiva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7071641)) <= 0.00070
               AND ABS(v.longitude - (-7.7797029)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:858192692 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Francisco Celorico Palma', 'Mértola', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6110866, -7.7890864, 'OPENSTREETMAP', 'OSM:WAY:858192692',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:858192692')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mértola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Francisco Celorico Palma'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6110866)) <= 0.00070
               AND ABS(v.longitude - (-7.7890864)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166899151 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Mértola', 'Mértola', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6461230, -7.6625420, 'OPENSTREETMAP', 'OSM:WAY:166899151',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166899151')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mértola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Mértola'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6461230)) <= 0.00070
               AND ABS(v.longitude - (-7.6625420)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1536641154 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Canoagem e Remo da Albufeira da Tapada Grande', 'Mértola', 'Beja', NULL,
       'Canoagem', 'Campo desportivo', NULL,
       37.6741808, -7.5109271, 'OPENSTREETMAP', 'OSM:WAY:1536641154',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1536641154')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mértola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Canoagem e Remo da Albufeira da Tapada Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6741808)) <= 0.00070
               AND ABS(v.longitude - (-7.5109271)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136891368 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Cancelinhas', 'Moura', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.2062528, -7.2301918, 'OPENSTREETMAP', 'OSM:WAY:136891368',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136891368')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Cancelinhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2062528)) <= 0.00070
               AND ABS(v.longitude - (-7.2301918)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:210433701 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Tapadinha', 'Moura', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1124851, -7.2195468, 'OPENSTREETMAP', 'OSM:WAY:210433701',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:210433701')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Tapadinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1124851)) <= 0.00070
               AND ABS(v.longitude - (-7.2195468)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:345715517 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Santo Aleixo da Restauração', 'Moura', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0716396, -7.1561848, 'OPENSTREETMAP', 'OSM:WAY:345715517',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:345715517')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Santo Aleixo da Restauração'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0716396)) <= 0.00070
               AND ABS(v.longitude - (-7.1561848)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:507311698 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos 25 de Abril', 'Moura', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.2257356, -7.3415798, 'OPENSTREETMAP', 'OSM:WAY:507311698',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:507311698')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2257356)) <= 0.00070
               AND ABS(v.longitude - (-7.3415798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102392858 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Sobral da Adiça', 'Moura', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0209175, -7.2580622, 'OPENSTREETMAP', 'OSM:WAY:102392858',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102392858')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Sobral da Adiça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0209175)) <= 0.00070
               AND ABS(v.longitude - (-7.2580622)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:211721731 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Moura Atlético Clube', 'Moura', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1405923, -7.4415740, 'OPENSTREETMAP', 'OSM:WAY:211721731',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:211721731')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Moura Atlético Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1405923)) <= 0.00070
               AND ABS(v.longitude - (-7.4415740)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1296750065 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina adultos', 'Moura', 'Beja', NULL,
       'Natação', 'Piscina', NULL,
       38.2021144, -7.4865595, 'OPENSTREETMAP', 'OSM:WAY:1296750065',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1296750065')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina adultos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2021144)) <= 0.00070
               AND ABS(v.longitude - (-7.4865595)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1296750066 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina crianças', 'Moura', 'Beja', NULL,
       'Natação', 'Piscina', NULL,
       38.2019863, -7.4865766, 'OPENSTREETMAP', 'OSM:WAY:1296750066',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1296750066')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moura'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina crianças'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2019863)) <= 0.00070
               AND ABS(v.longitude - (-7.4865766)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:541243524 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Casa do Povo de São Luís', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7214346, -8.6600820, 'OPENSTREETMAP', 'OSM:WAY:541243524',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:541243524')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Casa do Povo de São Luís'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7214346)) <= 0.00070
               AND ABS(v.longitude - (-8.6600820)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:677992849 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol 1º de Maio', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7796067, -8.5624344, 'OPENSTREETMAP', 'OSM:WAY:677992849',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:677992849')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7796067)) <= 0.00070
               AND ABS(v.longitude - (-8.5624344)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:677992847 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Junta de Freguesia de Sabóia', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.4994629, -8.4973888, 'OPENSTREETMAP', 'OSM:WAY:677992847',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:677992847')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Junta de Freguesia de Sabóia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.4994629)) <= 0.00070
               AND ABS(v.longitude - (-8.4973888)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:416063266 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Longueira', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6641838, -8.7741553, 'OPENSTREETMAP', 'OSM:WAY:416063266',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:416063266')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Longueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6641838)) <= 0.00070
               AND ABS(v.longitude - (-8.7741553)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:430340635 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Malavado', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5773443, -8.7272636, 'OPENSTREETMAP', 'OSM:WAY:430340635',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:430340635')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Malavado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5773443)) <= 0.00070
               AND ABS(v.longitude - (-8.7272636)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:847344389 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Vale de Santiago', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7563106, -8.4196324, 'OPENSTREETMAP', 'OSM:WAY:847344389',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:847344389')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Vale de Santiago'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7563106)) <= 0.00070
               AND ABS(v.longitude - (-8.4196324)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:244312118 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos das Figueiras', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5131263, -8.7075150, 'OPENSTREETMAP', 'OSM:WAY:244312118',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244312118')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos das Figueiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5131263)) <= 0.00070
               AND ABS(v.longitude - (-8.7075150)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:847344390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Bicos', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.8182456, -8.5066766, 'OPENSTREETMAP', 'OSM:WAY:847344390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:847344390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Bicos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8182456)) <= 0.00070
               AND ABS(v.longitude - (-8.5066766)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:831290412 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Colos', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7351299, -8.4671300, 'OPENSTREETMAP', 'OSM:WAY:831290412',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:831290412')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Colos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7351299)) <= 0.00070
               AND ABS(v.longitude - (-8.4671300)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:428933613 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Relíquias', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7025862, -8.4840869, 'OPENSTREETMAP', 'OSM:WAY:428933613',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:428933613')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Relíquias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7025862)) <= 0.00070
               AND ABS(v.longitude - (-8.4840869)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:847344391 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Castelão', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6736688, -8.6511667, 'OPENSTREETMAP', 'OSM:WAY:847344391',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:847344391')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Castelão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6736688)) <= 0.00070
               AND ABS(v.longitude - (-8.6511667)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:141160207 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Foz do Mira', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7283658, -8.7827051, 'OPENSTREETMAP', 'OSM:WAY:141160207',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:141160207')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Foz do Mira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7283658)) <= 0.00070
               AND ABS(v.longitude - (-8.7827051)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:319388621 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo Doutor Sidónio Rito', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6908529, -8.4130737, 'OPENSTREETMAP', 'OSM:WAY:319388621',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:319388621')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo Doutor Sidónio Rito'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6908529)) <= 0.00070
               AND ABS(v.longitude - (-8.4130737)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:627636077 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Futebol Clube Pereirense', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.4345695, -8.4368090, 'OPENSTREETMAP', 'OSM:WAY:627636077',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:627636077')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Futebol Clube Pereirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.4345695)) <= 0.00070
               AND ABS(v.longitude - (-8.4368090)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:379903284 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal da Boa Esperança', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5726378, -8.6638044, 'OPENSTREETMAP', 'OSM:WAY:379903284',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:379903284')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal da Boa Esperança'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5726378)) <= 0.00070
               AND ABS(v.longitude - (-8.6638044)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:677992848 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal da Nave Redonda', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.4052256, -8.4830013, 'OPENSTREETMAP', 'OSM:WAY:677992848',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:677992848')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal da Nave Redonda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.4052256)) <= 0.00070
               AND ABS(v.longitude - (-8.4830013)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:677992846 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de Luzianes-Gare', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5967309, -8.4831922, 'OPENSTREETMAP', 'OSM:WAY:677992846',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:677992846')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal de Luzianes-Gare'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5967309)) <= 0.00070
               AND ABS(v.longitude - (-8.4831922)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:156529318 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Mar', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5979268, -8.8167217, 'OPENSTREETMAP', 'OSM:WAY:156529318',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:156529318')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Mar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5979268)) <= 0.00070
               AND ABS(v.longitude - (-8.8167217)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119906095 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Santa Clara-a-Velha', 'Odemira', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.5108302, -8.4728146, 'OPENSTREETMAP', 'OSM:WAY:119906095',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119906095')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Santa Clara-a-Velha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5108302)) <= 0.00070
               AND ABS(v.longitude - (-8.4728146)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196929681 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Doutor Justino dos Santos', 'Odemira', 'Beja', NULL,
       'Futebol', 'Estádio', NULL,
       37.5972337, -8.6327065, 'OPENSTREETMAP', 'OSM:WAY:196929681',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196929681')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Doutor Justino dos Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5972337)) <= 0.00070
               AND ABS(v.longitude - (-8.6327065)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:828596438 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Odemira', 'Odemira', 'Beja', NULL,
       'Corrida', 'Pista desportiva', NULL,
       37.5972366, -8.6324863, 'OPENSTREETMAP', 'OSM:WAY:828596438',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:828596438')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Odemira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5972366)) <= 0.00070
               AND ABS(v.longitude - (-8.6324863)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:436017733 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Praia Fluvial de Santa Clara-a-Velha', 'Odemira', 'Beja', NULL,
       'Natação', 'Piscina', NULL,
       37.5168316, -8.4438007, 'OPENSTREETMAP', 'OSM:WAY:436017733',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:436017733')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odemira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Praia Fluvial de Santa Clara-a-Velha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.5168316)) <= 0.00070
               AND ABS(v.longitude - (-8.4438007)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:368627595 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Eiras', 'Ourique', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7578949, -8.3033025, 'OPENSTREETMAP', 'OSM:WAY:368627595',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:368627595')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourique'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Eiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7578949)) <= 0.00070
               AND ABS(v.longitude - (-8.3033025)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:115843000 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Garvão', 'Ourique', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7111772, -8.3312805, 'OPENSTREETMAP', 'OSM:WAY:115843000',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:115843000')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourique'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Garvão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7111772)) <= 0.00070
               AND ABS(v.longitude - (-8.3312805)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:831290413 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Dr. Albertino de Oliveira', 'Ourique', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7302075, -8.4030016, 'OPENSTREETMAP', 'OSM:WAY:831290413',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:831290413')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourique'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Dr. Albertino de Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7302075)) <= 0.00070
               AND ABS(v.longitude - (-8.4030016)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166709954 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Dom Afonso Henriques', 'Ourique', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.6526476, -8.2295789, 'OPENSTREETMAP', 'OSM:WAY:166709954',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166709954')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourique'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Dom Afonso Henriques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6526476)) <= 0.00070
               AND ABS(v.longitude - (-8.2295789)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:163725047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Ourique', 'Ourique', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.6526725, -8.2286153, 'OPENSTREETMAP', 'OSM:WAY:163725047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:163725047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourique'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Ourique'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.6526725)) <= 0.00070
               AND ABS(v.longitude - (-8.2286153)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:553353590 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 25 de Abril', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9852819, -7.4078350, 'OPENSTREETMAP', 'OSM:WAY:553353590',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:553353590')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9852819)) <= 0.00070
               AND ABS(v.longitude - (-7.4078350)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182778457 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Aldeia do Pinto', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9198572, -7.4811397, 'OPENSTREETMAP', 'OSM:WAY:182778457',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182778457')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Aldeia do Pinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9198572)) <= 0.00070
               AND ABS(v.longitude - (-7.4811397)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:346934008 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Brinches', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0318076, -7.6075897, 'OPENSTREETMAP', 'OSM:WAY:346934008',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:346934008')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Brinches'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0318076)) <= 0.00070
               AND ABS(v.longitude - (-7.6075897)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:338959576 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Juventude Ficalhense', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9445030, -7.3027914, 'OPENSTREETMAP', 'OSM:WAY:338959576',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:338959576')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Juventude Ficalhense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9445030)) <= 0.00070
               AND ABS(v.longitude - (-7.3027914)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:100975251 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo 1º de Maio', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0193471, -7.4807900, 'OPENSTREETMAP', 'OSM:WAY:100975251',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:100975251')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0193471)) <= 0.00070
               AND ABS(v.longitude - (-7.4807900)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:346942358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo 1º de Maio - Campo 2', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0176983, -7.4800627, 'OPENSTREETMAP', 'OSM:WAY:346942358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:346942358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo 1º de Maio - Campo 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0176983)) <= 0.00070
               AND ABS(v.longitude - (-7.4800627)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102392844 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Vila Nova de São Bento', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9291925, -7.4110825, 'OPENSTREETMAP', 'OSM:WAY:102392844',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102392844')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Vila Nova de São Bento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9291925)) <= 0.00070
               AND ABS(v.longitude - (-7.4110825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1549445020 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Vila Nova de São Bento - Campo n.º 2', 'Serpa', 'Beja', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9300739, -7.4109472, 'OPENSTREETMAP', 'OSM:WAY:1549445020',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1549445020')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Vila Nova de São Bento - Campo n.º 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9300739)) <= 0.00070
               AND ABS(v.longitude - (-7.4109472)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:215062342 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Pias', 'Serpa', 'Beja', NULL,
       'Natação', 'Piscina', NULL,
       38.0253804, -7.4764758, 'OPENSTREETMAP', 'OSM:WAY:215062342',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:215062342')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Pias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0253804)) <= 0.00070
               AND ABS(v.longitude - (-7.4764758)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:289937836 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cobertas', 'Serpa', 'Beja', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.9391735, -7.6047314, 'OPENSTREETMAP', 'OSM:WAY:289937836',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:289937836')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Serpa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9391735)) <= 0.00070
               AND ABS(v.longitude - (-7.6047314)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1238130909 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal da Vidigueira', 'Vidigueira', 'Beja', NULL,
       'Futebol', 'Estádio', NULL,
       38.2108704, -7.7949907, 'OPENSTREETMAP', 'OSM:WAY:1238130909',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1238130909')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vidigueira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal da Vidigueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2108704)) <= 0.00070
               AND ABS(v.longitude - (-7.7949907)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
