-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:154609455 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Corroios', 'Seixal', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6355617, -9.1531890, 'OPENSTREETMAP', 'OSM:WAY:154609455',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154609455')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Corroios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6355617)) <= 0.00070
               AND ABS(v.longitude - (-9.1531890)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:819276877 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Parque Verde', 'Seixal', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.5500734, -9.0713135, 'OPENSTREETMAP', 'OSM:WAY:819276877',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:819276877')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Parque Verde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5500734)) <= 0.00070
               AND ABS(v.longitude - (-9.0713135)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:828596421 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista do Complexo Municipal de Atletismo Carla Sacramento', 'Seixal', 'Setúbal', NULL,
       'Corrida', 'Pista desportiva', NULL,
       38.6278676, -9.1364728, 'OPENSTREETMAP', 'OSM:WAY:828596421',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:828596421')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista do Complexo Municipal de Atletismo Carla Sacramento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6278676)) <= 0.00070
               AND ABS(v.longitude - (-9.1364728)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:676597702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Bacelinho', 'Seixal', 'Setúbal', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.6344406, -9.0964067, 'OPENSTREETMAP', 'OSM:WAY:676597702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:676597702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Bacelinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6344406)) <= 0.00070
               AND ABS(v.longitude - (-9.0964067)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:517973484 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park do Seixal', 'Seixal', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.6390813, -9.1045828, 'OPENSTREETMAP', 'OSM:WAY:517973484',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:517973484')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park do Seixal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6390813)) <= 0.00070
               AND ABS(v.longitude - (-9.1045828)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13723221065 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studio DoFitness', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6445045, -9.1589210, 'OPENSTREETMAP', 'OSM:NODE:13723221065',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13723221065')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studio DoFitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6445045)) <= 0.00070
               AND ABS(v.longitude - (-9.1589210)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1091838853 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Trevo', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6325207, -9.1171737, 'OPENSTREETMAP', 'OSM:WAY:1091838853',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1091838853')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Trevo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6325207)) <= 0.00070
               AND ABS(v.longitude - (-9.1171737)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13966266190 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'WinJoy Fitness Health & Longevity', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6073106, -9.0890044, 'OPENSTREETMAP', 'OSM:NODE:13966266190',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13966266190')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('WinJoy Fitness Health & Longevity'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6073106)) <= 0.00070
               AND ABS(v.longitude - (-9.0890044)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6380040285 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga e terapias holisticas', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6171780, -9.1166522, 'OPENSTREETMAP', 'OSM:NODE:6380040285',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6380040285')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga e terapias holisticas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6171780)) <= 0.00070
               AND ABS(v.longitude - (-9.1166522)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1234855812 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação de Desenvolvimento Quinta do Conde', 'Sesimbra', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.5529934, -9.0381190, 'OPENSTREETMAP', 'OSM:WAY:1234855812',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1234855812')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação de Desenvolvimento Quinta do Conde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5529934)) <= 0.00070
               AND ABS(v.longitude - (-9.0381190)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:365664451 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Maçã', 'Sesimbra', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.4710435, -9.0743608, 'OPENSTREETMAP', 'OSM:WAY:365664451',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:365664451')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Maçã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4710435)) <= 0.00070
               AND ABS(v.longitude - (-9.0743608)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:512413484 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Desportivo de Alfarim', 'Sesimbra', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.4892095, -9.1612148, 'OPENSTREETMAP', 'OSM:WAY:512413484',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:512413484')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Desportivo de Alfarim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4892095)) <= 0.00070
               AND ABS(v.longitude - (-9.1612148)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:303028220 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal da Maçã', 'Sesimbra', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.4715551, -9.0745291, 'OPENSTREETMAP', 'OSM:WAY:303028220',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303028220')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Municipal da Maçã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4715551)) <= 0.00070
               AND ABS(v.longitude - (-9.0745291)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:638541424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Multiusos de Areia da Quinta do Conde', 'Sesimbra', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5535058, -9.0568077, 'OPENSTREETMAP', 'OSM:WAY:638541424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:638541424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Multiusos de Areia da Quinta do Conde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5535058)) <= 0.00070
               AND ABS(v.longitude - (-9.0568077)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12001493056 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Flecte Actividades Desportivas', 'Sesimbra', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.4598233, -9.1036747, 'OPENSTREETMAP', 'OSM:NODE:12001493056',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12001493056')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Flecte Actividades Desportivas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4598233)) <= 0.00070
               AND ABS(v.longitude - (-9.1036747)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12508495928 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'JS Ginásio Clinico', 'Sesimbra', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.4713595, -9.1013757, 'OPENSTREETMAP', 'OSM:NODE:12508495928',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12508495928')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('JS Ginásio Clinico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4713595)) <= 0.00070
               AND ABS(v.longitude - (-9.1013757)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13201831453 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Nuno Gusmão', 'Sesimbra', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.4728375, -9.1010530, 'OPENSTREETMAP', 'OSM:NODE:13201831453',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13201831453')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Nuno Gusmão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4728375)) <= 0.00070
               AND ABS(v.longitude - (-9.1010530)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:442940166 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo Municipal de Sampaio', 'Sesimbra', 'Setúbal', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       38.4750467, -9.0929741, 'OPENSTREETMAP', 'OSM:WAY:442940166',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:442940166')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo Municipal de Sampaio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4750467)) <= 0.00070
               AND ABS(v.longitude - (-9.0929741)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:396627914 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Grupo Desportivo de Sesimbra', 'Sesimbra', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.4472679, -9.1004598, 'OPENSTREETMAP', 'OSM:WAY:396627914',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:396627914')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Grupo Desportivo de Sesimbra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4472679)) <= 0.00070
               AND ABS(v.longitude - (-9.1004598)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:875471978 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Quinta do Conde', 'Sesimbra', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.5680697, -9.0371557, 'OPENSTREETMAP', 'OSM:WAY:875471978',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:875471978')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Quinta do Conde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5680697)) <= 0.00070
               AND ABS(v.longitude - (-9.0371557)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1205641892 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Chushin', 'Setúbal', 'Setúbal', NULL,
       'Judo;Ginástica;Dança Desportiva', 'Ginásio', NULL,
       38.5449860, -9.0309787, 'OPENSTREETMAP', 'OSM:WAY:1205641892',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1205641892')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Chushin'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5449860)) <= 0.00070
               AND ABS(v.longitude - (-9.0309787)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1187351660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Rugby Club de Setúbal', 'Setúbal', 'Setúbal', NULL,
       'Rugby', 'Campo desportivo', NULL,
       38.5320335, -8.8953365, 'OPENSTREETMAP', 'OSM:WAY:1187351660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1187351660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Rugby Club de Setúbal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5320335)) <= 0.00070
               AND ABS(v.longitude - (-8.8953365)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1189125074 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Alto da Guerra Sport Clube', 'Setúbal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5401396, -8.8465481, 'OPENSTREETMAP', 'OSM:WAY:1189125074',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1189125074')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Alto da Guerra Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5401396)) <= 0.00070
               AND ABS(v.longitude - (-8.8465481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:109294014 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Bela Vista (UFCI)', 'Setúbal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5304614, -8.8816287, 'OPENSTREETMAP', 'OSM:WAY:109294014',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:109294014')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Bela Vista (UFCI)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5304614)) <= 0.00070
               AND ABS(v.longitude - (-8.8816287)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:586969334 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Pedreiras (Viso)', 'Setúbal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5241684, -8.9099894, 'OPENSTREETMAP', 'OSM:WAY:586969334',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:586969334')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Pedreiras (Viso)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5241684)) <= 0.00070
               AND ABS(v.longitude - (-8.9099894)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103062832 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal Vítor Batista', 'Setúbal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5296630, -8.8650872, 'OPENSTREETMAP', 'OSM:WAY:103062832',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103062832')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal Vítor Batista'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5296630)) <= 0.00070
               AND ABS(v.longitude - (-8.8650872)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:288568858 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo nº 2 do Bonfim', 'Setúbal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5322207, -8.8901733, 'OPENSTREETMAP', 'OSM:WAY:288568858',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:288568858')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo nº 2 do Bonfim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5322207)) <= 0.00070
               AND ABS(v.longitude - (-8.8901733)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1234650032 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural e Desportivo de Brejos de Azeitão', 'Setúbal', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.5468992, -9.0259646, 'OPENSTREETMAP', 'OSM:WAY:1234650032',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1234650032')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural e Desportivo de Brejos de Azeitão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5468992)) <= 0.00070
               AND ABS(v.longitude - (-9.0259646)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11022386572 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory Setúbal', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5210136, -8.8991068, 'OPENSTREETMAP', 'OSM:NODE:11022386572',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11022386572')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory Setúbal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5210136)) <= 0.00070
               AND ABS(v.longitude - (-8.8991068)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11208075706 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'LV Personal Trainer', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5319747, -8.8742936, 'OPENSTREETMAP', 'OSM:NODE:11208075706',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11208075706')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('LV Personal Trainer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5319747)) <= 0.00070
               AND ABS(v.longitude - (-8.8742936)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11032215365 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oásis Fitness', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5178038, -8.9060307, 'OPENSTREETMAP', 'OSM:NODE:11032215365',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11032215365')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oásis Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5178038)) <= 0.00070
               AND ABS(v.longitude - (-8.9060307)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:42793973 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Azeitão', 'Setúbal', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.5224751, -9.0179776, 'OPENSTREETMAP', 'OSM:WAY:42793973',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:42793973')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Azeitão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5224751)) <= 0.00070
               AND ABS(v.longitude - (-9.0179776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:288567437 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Setubal', 'Setúbal', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.5340387, -8.8893669, 'OPENSTREETMAP', 'OSM:WAY:288567437',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:288567437')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Setubal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5340387)) <= 0.00070
               AND ABS(v.longitude - (-8.8893669)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:629272883 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de BMX', 'Setúbal', 'Setúbal', NULL,
       'Ciclismo;BMX', 'Pista desportiva', NULL,
       38.5199769, -8.8663575, 'OPENSTREETMAP', 'OSM:WAY:629272883',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:629272883')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de BMX'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5199769)) <= 0.00070
               AND ABS(v.longitude - (-8.8663575)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1187351661 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Setúbal', 'Setúbal', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.5317467, -8.8961766, 'OPENSTREETMAP', 'OSM:WAY:1187351661',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1187351661')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Setúbal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5317467)) <= 0.00070
               AND ABS(v.longitude - (-8.8961766)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11099525652 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Taekwondo - Dojang Armando Silva', 'Setúbal', 'Setúbal', NULL,
       'Taekwondo', 'Complexo desportivo', NULL,
       38.5257428, -8.8739395, 'OPENSTREETMAP', 'OSM:NODE:11099525652',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11099525652')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Taekwondo - Dojang Armando Silva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5257428)) <= 0.00070
               AND ABS(v.longitude - (-8.8739395)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104443384 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vitória Futebol Clube', 'Setúbal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5312536, -8.8912056, 'OPENSTREETMAP', 'OSM:WAY:104443384',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104443384')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vitória Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5312536)) <= 0.00070
               AND ABS(v.longitude - (-8.8912056)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1538926106 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Bragada', 'Sines', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9733273, -8.7707494, 'OPENSTREETMAP', 'OSM:WAY:1538926106',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1538926106')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sines'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Bragada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9733273)) <= 0.00070
               AND ABS(v.longitude - (-8.7707494)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544412930 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal dos Desportos', 'Sines', 'Setúbal', NULL,
       'Hóquei', 'Complexo desportivo', NULL,
       37.9581239, -8.8697927, 'OPENSTREETMAP', 'OSM:WAY:544412930',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544412930')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sines'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal dos Desportos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9581239)) <= 0.00070
               AND ABS(v.longitude - (-8.8697927)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:291943407 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Sines', 'Sines', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.9554505, -8.8529387, 'OPENSTREETMAP', 'OSM:WAY:291943407',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:291943407')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sines'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Sines'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9554505)) <= 0.00070
               AND ABS(v.longitude - (-8.8529387)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5039958321 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Safira', 'Sines', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.9587410, -8.8635093, 'OPENSTREETMAP', 'OSM:NODE:5039958321',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5039958321')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sines'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Safira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9587410)) <= 0.00070
               AND ABS(v.longitude - (-8.8635093)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1386636080 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park de Sines', 'Sines', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.9556097, -8.8634444, 'OPENSTREETMAP', 'OSM:WAY:1386636080',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1386636080')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sines'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park de Sines'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9556097)) <= 0.00070
               AND ABS(v.longitude - (-8.8634444)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
