-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:8694280940 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Brahma Kumaris', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7069671, -9.1948815, 'OPENSTREETMAP', 'OSM:NODE:8694280940',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8694280940')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Brahma Kumaris'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7069671)) <= 0.00070
               AND ABS(v.longitude - (-9.1948815)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:49290574 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 3', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7555360, -9.1625579, 'OPENSTREETMAP', 'OSM:WAY:49290574',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:49290574')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7555360)) <= 0.00070
               AND ABS(v.longitude - (-9.1625579)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:49290573 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 4', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7566487, -9.1631396, 'OPENSTREETMAP', 'OSM:WAY:49290573',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:49290573')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 4'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7566487)) <= 0.00070
               AND ABS(v.longitude - (-9.1631396)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196085505 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 5', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7511714, -9.1648749, 'OPENSTREETMAP', 'OSM:WAY:196085505',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196085505')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 5'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7511714)) <= 0.00070
               AND ABS(v.longitude - (-9.1648749)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:49290575 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 5', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7554230, -9.1636678, 'OPENSTREETMAP', 'OSM:WAY:49290575',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:49290575')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 5'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7554230)) <= 0.00070
               AND ABS(v.longitude - (-9.1636678)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:49290576 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 6', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7565213, -9.1642356, 'OPENSTREETMAP', 'OSM:WAY:49290576',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:49290576')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 6'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7565213)) <= 0.00070
               AND ABS(v.longitude - (-9.1642356)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54754001 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 7', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7545563, -9.1634015, 'OPENSTREETMAP', 'OSM:WAY:54754001',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54754001')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 7'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7545563)) <= 0.00070
               AND ABS(v.longitude - (-9.1634015)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:862791766 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Cristiano Ronaldo', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7632719, -9.1574864, 'OPENSTREETMAP', 'OSM:WAY:862791766',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:862791766')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Cristiano Ronaldo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7632719)) <= 0.00070
               AND ABS(v.longitude - (-9.1574864)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:569394331 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Mata', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7127475, -9.1995598, 'OPENSTREETMAP', 'OSM:WAY:569394331',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:569394331')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Mata'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7127475)) <= 0.00070
               AND ABS(v.longitude - (-9.1995598)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154273608 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquetebol do Moinho do Penedo', 'Lisboa', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.7205279, -9.1965720, 'OPENSTREETMAP', 'OSM:WAY:154273608',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154273608')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquetebol do Moinho do Penedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7205279)) <= 0.00070
               AND ABS(v.longitude - (-9.1965720)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:672784503 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquetebol José Calheiros', 'Lisboa', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.7163954, -9.2179747, 'OPENSTREETMAP', 'OSM:WAY:672784503',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:672784503')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquetebol José Calheiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7163954)) <= 0.00070
               AND ABS(v.longitude - (-9.2179747)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003287062 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da EPAL', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7671342, -9.1035772, 'OPENSTREETMAP', 'OSM:WAY:1003287062',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003287062')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da EPAL'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7671342)) <= 0.00070
               AND ABS(v.longitude - (-9.1035772)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:162867341 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Fernando Perfeito', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7784298, -9.1213031, 'OPENSTREETMAP', 'OSM:WAY:162867341',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:162867341')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Fernando Perfeito'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7784298)) <= 0.00070
               AND ABS(v.longitude - (-9.1213031)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11841306175 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campolide Skatepark', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7346551, -9.1693878, 'OPENSTREETMAP', 'OSM:NODE:11841306175',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11841306175')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campolide Skatepark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7346551)) <= 0.00070
               AND ABS(v.longitude - (-9.1693878)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:614659894 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Luís Figo', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7629077, -9.1573953, 'OPENSTREETMAP', 'OSM:WAY:614659894',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:614659894')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Luís Figo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7629077)) <= 0.00070
               AND ABS(v.longitude - (-9.1573953)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:98385210 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Polidesportivo do IPL - Benfica', 'Lisboa', 'Lisboa', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.7469574, -9.1946071, 'OPENSTREETMAP', 'OSM:WAY:98385210',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:98385210')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Polidesportivo do IPL - Benfica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7469574)) <= 0.00070
               AND ABS(v.longitude - (-9.1946071)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:336011779 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Padel', 'Lisboa', 'Lisboa', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.7622358, -9.1542050, 'OPENSTREETMAP', 'OSM:WAY:336011779',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:336011779')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7622358)) <= 0.00070
               AND ABS(v.longitude - (-9.1542050)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:975810720 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos e Atletismo', 'Lisboa', 'Lisboa', NULL,
       'Atletismo;Basquetebol;Voleibol;Futebol', 'Campo desportivo', NULL,
       38.7669186, -9.1140867, 'OPENSTREETMAP', 'OSM:WAY:975810720',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:975810720')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos e Atletismo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7669186)) <= 0.00070
               AND ABS(v.longitude - (-9.1140867)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12323405926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CAPLX - Centro de Alta Performance', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7539623, -9.1408781, 'OPENSTREETMAP', 'OSM:NODE:12323405926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12323405926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CAPLX - Centro de Alta Performance'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7539623)) <= 0.00070
               AND ABS(v.longitude - (-9.1408781)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:860053413 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Casa Pia Atlético Clube', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7376235, -9.2038870, 'OPENSTREETMAP', 'OSM:WAY:860053413',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:860053413')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Casa Pia Atlético Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7376235)) <= 0.00070
               AND ABS(v.longitude - (-9.2038870)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:962642233 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Ténis de Monsanto', 'Lisboa', 'Lisboa', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.7175440, -9.1826225, 'OPENSTREETMAP', 'OSM:WAY:962642233',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:962642233')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Ténis de Monsanto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7175440)) <= 0.00070
               AND ABS(v.longitude - (-9.1826225)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:681914786 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis Alto da Faia', 'Lisboa', 'Lisboa', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.7658818, -9.1708023, 'OPENSTREETMAP', 'OSM:WAY:681914786',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:681914786')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis Alto da Faia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7658818)) <= 0.00070
               AND ABS(v.longitude - (-9.1708023)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:909202700 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Club Internacional de Foot-Ball', 'Lisboa', 'Lisboa', NULL,
       'Ténis;Futebol', 'Complexo desportivo', NULL,
       38.7138212, -9.2105713, 'OPENSTREETMAP', 'OSM:WAY:909202700',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:909202700')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Club Internacional de Foot-Ball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7138212)) <= 0.00070
               AND ABS(v.longitude - (-9.2105713)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54754000 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7558389, -9.1617140, 'OPENSTREETMAP', 'OSM:WAY:54754000',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54754000')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7558389)) <= 0.00070
               AND ABS(v.longitude - (-9.1617140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:131903597 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal São João de Brito', 'Lisboa', 'Lisboa', NULL,
       'Rugby;Futebol', 'Campo desportivo', NULL,
       38.7610187, -9.1350894, 'OPENSTREETMAP', 'OSM:WAY:131903597',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:131903597')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Municipal São João de Brito'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7610187)) <= 0.00070
               AND ABS(v.longitude - (-9.1350894)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12315963983 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'E-Fit Benfica', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7535906, -9.1802861, 'OPENSTREETMAP', 'OSM:NODE:12315963983',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12315963983')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('E-Fit Benfica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7535906)) <= 0.00070
               AND ABS(v.longitude - (-9.1802861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:96857177 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'EletroFitness Estúdio Oficial', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7246878, -9.1479008, 'OPENSTREETMAP', 'OSM:WAY:96857177',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:96857177')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('EletroFitness Estúdio Oficial'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7246878)) <= 0.00070
               AND ABS(v.longitude - (-9.1479008)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:71229178 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Luz', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.7526957, -9.1846666, 'OPENSTREETMAP', 'OSM:WAY:71229178',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:71229178')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Luz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7526957)) <= 0.00070
               AND ABS(v.longitude - (-9.1846666)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:199304519 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Tapadinha', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7096410, -9.1806909, 'OPENSTREETMAP', 'OSM:WAY:199304519',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:199304519')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Tapadinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7096410)) <= 0.00070
               AND ABS(v.longitude - (-9.1806909)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:28355917 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Restelo', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.7027146, -9.2079810, 'OPENSTREETMAP', 'OSM:WAY:28355917',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:28355917')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Restelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7027146)) <= 0.00070
               AND ABS(v.longitude - (-9.2079810)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240279389 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Engenheiro Carlos Salema', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.7436267, -9.1098727, 'OPENSTREETMAP', 'OSM:WAY:240279389',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240279389')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Engenheiro Carlos Salema'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7436267)) <= 0.00070
               AND ABS(v.longitude - (-9.1098727)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:144845355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Francisco Lázaro', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.7494971, -9.2063128, 'OPENSTREETMAP', 'OSM:WAY:144845355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:144845355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Francisco Lázaro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7494971)) <= 0.00070
               AND ABS(v.longitude - (-9.2063128)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:374890120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Pina Manique', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.7369396, -9.2032264, 'OPENSTREETMAP', 'OSM:WAY:374890120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:374890120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Pina Manique'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7369396)) <= 0.00070
               AND ABS(v.longitude - (-9.2032264)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:203540596 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Pinto Basto', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7149264, -9.2092814, 'OPENSTREETMAP', 'OSM:WAY:203540596',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:203540596')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Pinto Basto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7149264)) <= 0.00070
               AND ABS(v.longitude - (-9.2092814)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3112500139 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fábrica do Físico', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7240993, -9.1287770, 'OPENSTREETMAP', 'OSM:NODE:3112500139',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3112500139')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fábrica do Físico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7240993)) <= 0.00070
               AND ABS(v.longitude - (-9.1287770)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10248335433 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7482249, -9.1908311, 'OPENSTREETMAP', 'OSM:NODE:10248335433',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10248335433')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7482249)) <= 0.00070
               AND ABS(v.longitude - (-9.1908311)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10248383233 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7624031, -9.1149250, 'OPENSTREETMAP', 'OSM:NODE:10248383233',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10248383233')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7624031)) <= 0.00070
               AND ABS(v.longitude - (-9.1149250)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5195747924 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7310642, -9.1479639, 'OPENSTREETMAP', 'OSM:NODE:5195747924',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5195747924')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7310642)) <= 0.00070
               AND ABS(v.longitude - (-9.1479639)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12354212134 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP São Sebastião', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7355026, -9.1543707, 'OPENSTREETMAP', 'OSM:NODE:12354212134',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12354212134')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP São Sebastião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7355026)) <= 0.00070
               AND ABS(v.longitude - (-9.1543707)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12314098146 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Flow Studio Pilates&Wellness', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7524616, -9.1806410, 'OPENSTREETMAP', 'OSM:NODE:12314098146',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12314098146')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Flow Studio Pilates&Wellness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7524616)) <= 0.00070
               AND ABS(v.longitude - (-9.1806410)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9729909304 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GCP-PADEL Amoreiras', 'Lisboa', 'Lisboa', NULL,
       'Ténis;Padel', 'Campo desportivo', NULL,
       38.7215933, -9.1573540, 'OPENSTREETMAP', 'OSM:NODE:9729909304',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9729909304')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GCP-PADEL Amoreiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7215933)) <= 0.00070
               AND ABS(v.longitude - (-9.1573540)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12351992704 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio ART', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7649316, -9.1811270, 'OPENSTREETMAP', 'OSM:NODE:12351992704',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12351992704')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio ART'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7649316)) <= 0.00070
               AND ABS(v.longitude - (-9.1811270)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:829977093 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Pista Nº2', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7524207, -9.1638711, 'OPENSTREETMAP', 'OSM:WAY:829977093',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:829977093')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Pista Nº2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7524207)) <= 0.00070
               AND ABS(v.longitude - (-9.1638711)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243800855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GO Fit Campo Grande', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Ginásio', NULL,
       38.7527721, -9.1508939, 'OPENSTREETMAP', 'OSM:WAY:243800855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243800855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GO Fit Campo Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7527721)) <= 0.00070
               AND ABS(v.longitude - (-9.1508939)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:37010007 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Os Unidos', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7702230, -9.1876523, 'OPENSTREETMAP', 'OSM:WAY:37010007',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:37010007')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Os Unidos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7702230)) <= 0.00070
               AND ABS(v.longitude - (-9.1876523)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5331790487 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7182933, -9.1427447, 'OPENSTREETMAP', 'OSM:NODE:5331790487',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5331790487')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7182933)) <= 0.00070
               AND ABS(v.longitude - (-9.1427447)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:336762856 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Ginásio', NULL,
       38.7622239, -9.1612077, 'OPENSTREETMAP', 'OSM:WAY:336762856',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:336762856')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7622239)) <= 0.00070
               AND ABS(v.longitude - (-9.1612077)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103708104 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lemonfit Premium', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7826208, -9.0958897, 'OPENSTREETMAP', 'OSM:WAY:103708104',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103708104')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lemonfit Premium'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7826208)) <= 0.00070
               AND ABS(v.longitude - (-9.0958897)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153604088 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lisboa Racket Center', 'Lisboa', 'Lisboa', NULL,
       'Ténis;Padel;Squash;Ginásio', 'Complexo desportivo', NULL,
       38.7582982, -9.1340239, 'OPENSTREETMAP', 'OSM:WAY:153604088',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153604088')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lisboa Racket Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7582982)) <= 0.00070
               AND ABS(v.longitude - (-9.1340239)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13389046801 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'MVMT Studio', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7049111, -9.1619320, 'OPENSTREETMAP', 'OSM:NODE:13389046801',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13389046801')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('MVMT Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7049111)) <= 0.00070
               AND ABS(v.longitude - (-9.1619320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:504240322 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Padel Factory Expo', 'Lisboa', 'Lisboa', NULL,
       'Padel', 'Complexo desportivo', NULL,
       38.7860171, -9.0933328, 'OPENSTREETMAP', 'OSM:WAY:504240322',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:504240322')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Padel Factory Expo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7860171)) <= 0.00070
               AND ABS(v.longitude - (-9.0933328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1543229234 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque das Naçoes Pump track', 'Lisboa', 'Lisboa', NULL,
       'Skate;BMX', 'Pista desportiva', NULL,
       38.7883142, -9.0918565, 'OPENSTREETMAP', 'OSM:WAY:1543229234',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1543229234')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque das Naçoes Pump track'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7883142)) <= 0.00070
               AND ABS(v.longitude - (-9.0918565)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309147945 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão 1', 'Lisboa', 'Lisboa', NULL,
       'Basquetebol;Futsal', 'Complexo desportivo', NULL,
       38.7511492, -9.1835342, 'OPENSTREETMAP', 'OSM:WAY:309147945',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309147945')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7511492)) <= 0.00070
               AND ABS(v.longitude - (-9.1835342)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309147944 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão 2', 'Lisboa', 'Lisboa', NULL,
       'Andebol;Voleibol', 'Complexo desportivo', NULL,
       38.7517212, -9.1833895, 'OPENSTREETMAP', 'OSM:WAY:309147944',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309147944')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7517212)) <= 0.00070
               AND ABS(v.longitude - (-9.1833895)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:319608828 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pedrouços Skatepark', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.6952564, -9.2195992, 'OPENSTREETMAP', 'OSM:WAY:319608828',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:319608828')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pedrouços Skatepark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6952564)) <= 0.00070
               AND ABS(v.longitude - (-9.2195992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:722635632 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Infantil Villa Restelo', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7142517, -9.2153460, 'OPENSTREETMAP', 'OSM:WAY:722635632',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:722635632')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Infantil Villa Restelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7142517)) <= 0.00070
               AND ABS(v.longitude - (-9.2153460)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12307074430 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'piscina publica da freguesia', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7302567, -9.1305892, 'OPENSTREETMAP', 'OSM:NODE:12307074430',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12307074430')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('piscina publica da freguesia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7302567)) <= 0.00070
               AND ABS(v.longitude - (-9.1305892)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309147947 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Sport Lisboa e Benfica', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7512477, -9.1828049, 'OPENSTREETMAP', 'OSM:WAY:309147947',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309147947')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Sport Lisboa e Benfica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7512477)) <= 0.00070
               AND ABS(v.longitude - (-9.1828049)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:722635631 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Semiolímpica Villa Restelo', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7141520, -9.2155366, 'OPENSTREETMAP', 'OSM:WAY:722635631',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:722635631')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Semiolímpica Villa Restelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7141520)) <= 0.00070
               AND ABS(v.longitude - (-9.2155366)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196084358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo 3', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7520160, -9.1608464, 'OPENSTREETMAP', 'OSM:WAY:196084358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196084358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo 3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7520160)) <= 0.00070
               AND ABS(v.longitude - (-9.1608464)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1015842369 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Passadiço', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7227121, -9.1436823, 'OPENSTREETMAP', 'OSM:WAY:1015842369',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1015842369')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Passadiço'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7227121)) <= 0.00070
               AND ABS(v.longitude - (-9.1436823)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12320436240 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Prescription - Pilates Studio - Sao Bento', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7155106, -9.1545812, 'OPENSTREETMAP', 'OSM:NODE:12320436240',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12320436240')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Prescription - Pilates Studio - Sao Bento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7155106)) <= 0.00070
               AND ABS(v.longitude - (-9.1545812)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:844432159 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pump Track Skate', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7747781, -9.1551811, 'OPENSTREETMAP', 'OSM:WAY:844432159',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:844432159')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pump Track Skate'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7747781)) <= 0.00070
               AND ABS(v.longitude - (-9.1551811)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11879897760 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quimera Fitness', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7194648, -9.1663862, 'OPENSTREETMAP', 'OSM:NODE:11879897760',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11879897760')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quimera Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7194648)) <= 0.00070
               AND ABS(v.longitude - (-9.1663862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:216266878 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Recinto da AE-FFUL', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7477137, -9.1552807, 'OPENSTREETMAP', 'OSM:WAY:216266878',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:216266878')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Recinto da AE-FFUL'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7477137)) <= 0.00070
               AND ABS(v.longitude - (-9.1552807)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155352870 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue António Livramento', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7486347, -9.1972389, 'OPENSTREETMAP', 'OSM:WAY:155352870',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155352870')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue António Livramento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7486347)) <= 0.00070
               AND ABS(v.longitude - (-9.1972389)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12354242279 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'shankara yoga - Lisbon', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7329652, -9.1520140, 'OPENSTREETMAP', 'OSM:NODE:12354242279',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12354242279')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('shankara yoga - Lisbon'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7329652)) <= 0.00070
               AND ABS(v.longitude - (-9.1520140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1058390242 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Carlos Pinhão', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7530375, -9.1348276, 'OPENSTREETMAP', 'OSM:WAY:1058390242',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1058390242')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Carlos Pinhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7530375)) <= 0.00070
               AND ABS(v.longitude - (-9.1348276)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:174997053 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park Expo - Parque das nações', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7872585, -9.0915838, 'OPENSTREETMAP', 'OSM:WAY:174997053',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:174997053')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park Expo - Parque das nações'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7872585)) <= 0.00070
               AND ABS(v.longitude - (-9.0915838)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:577818297 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark São Sebastião', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7340255, -9.1588766, 'OPENSTREETMAP', 'OSM:WAY:577818297',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:577818297')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark São Sebastião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7340255)) <= 0.00070
               AND ABS(v.longitude - (-9.1588766)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:567594674 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Parque de Marvila', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7532137, -9.1195834, 'OPENSTREETMAP', 'OSM:WAY:567594674',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:567594674')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Parque de Marvila'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7532137)) <= 0.00070
               AND ABS(v.longitude - (-9.1195834)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:834792267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Spot Bairro do Horizonte', 'Lisboa', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7347602, -9.1258419, 'OPENSTREETMAP', 'OSM:WAY:834792267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:834792267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Spot Bairro do Horizonte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7347602)) <= 0.00070
               AND ABS(v.longitude - (-9.1258419)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240441626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Futebol Palmense', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7490350, -9.1704868, 'OPENSTREETMAP', 'OSM:WAY:240441626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240441626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Futebol Palmense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7490350)) <= 0.00070
               AND ABS(v.longitude - (-9.1704868)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54756293 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube de Portugal', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7613074, -9.1608766, 'OPENSTREETMAP', 'OSM:WAY:54756293',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54756293')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube de Portugal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7613074)) <= 0.00070
               AND ABS(v.longitude - (-9.1608766)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12314545108 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sthenos Benfica - Calistenia Portugal', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7471484, -9.1976837, 'OPENSTREETMAP', 'OSM:NODE:12314545108',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12314545108')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sthenos Benfica - Calistenia Portugal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7471484)) <= 0.00070
               AND ABS(v.longitude - (-9.1976837)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11844674468 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studio Pilates Filipa Mayer', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7157855, -9.1567892, 'OPENSTREETMAP', 'OSM:NODE:11844674468',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11844674468')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studio Pilates Filipa Mayer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7157855)) <= 0.00070
               AND ABS(v.longitude - (-9.1567892)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11880053469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studiorise Cycling', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7194032, -9.1649802, 'OPENSTREETMAP', 'OSM:NODE:11880053469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11880053469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studiorise Cycling'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7194032)) <= 0.00070
               AND ABS(v.longitude - (-9.1649802)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13119799901 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Supera', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7448848, -9.1388735, 'OPENSTREETMAP', 'OSM:NODE:13119799901',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13119799901')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Supera'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7448848)) <= 0.00070
               AND ABS(v.longitude - (-9.1388735)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12314505555 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'TechBidy Campo Pequeno', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7442378, -9.1418328, 'OPENSTREETMAP', 'OSM:NODE:12314505555',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12314505555')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('TechBidy Campo Pequeno'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7442378)) <= 0.00070
               AND ABS(v.longitude - (-9.1418328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9969548047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Code', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7310763, -9.1442510, 'OPENSTREETMAP', 'OSM:NODE:9969548047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9969548047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Code'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7310763)) <= 0.00070
               AND ABS(v.longitude - (-9.1442510)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11880901426 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Yoga Hostel', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7186708, -9.1327988, 'OPENSTREETMAP', 'OSM:NODE:11880901426',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11880901426')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Yoga Hostel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7186708)) <= 0.00070
               AND ABS(v.longitude - (-9.1327988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11883951700 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tonus Gym Campo de Ourique', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7189146, -9.1655375, 'OPENSTREETMAP', 'OSM:NODE:11883951700',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11883951700')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tonus Gym Campo de Ourique'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7189146)) <= 0.00070
               AND ABS(v.longitude - (-9.1655375)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:484611121 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Torre de escalada de Monsanto', 'Lisboa', 'Lisboa', NULL,
       'Escalada', 'Campo desportivo', NULL,
       38.7439014, -9.1879286, 'OPENSTREETMAP', 'OSM:WAY:484611121',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:484611121')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Torre de escalada de Monsanto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7439014)) <= 0.00070
               AND ABS(v.longitude - (-9.1879286)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13195891730 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7080491, -9.1507249, 'OPENSTREETMAP', 'OSM:NODE:13195891730',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13195891730')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7080491)) <= 0.00070
               AND ABS(v.longitude - (-9.1507249)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9970131483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7346905, -9.1395065, 'OPENSTREETMAP', 'OSM:NODE:9970131483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9970131483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7346905)) <= 0.00070
               AND ABS(v.longitude - (-9.1395065)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:98824236 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7258405, -9.1356948, 'OPENSTREETMAP', 'OSM:WAY:98824236',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:98824236')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7258405)) <= 0.00070
               AND ABS(v.longitude - (-9.1356948)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12319254170 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga Shala', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7551113, -9.1430058, 'OPENSTREETMAP', 'OSM:NODE:12319254170',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12319254170')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga Shala'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7551113)) <= 0.00070
               AND ABS(v.longitude - (-9.1430058)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12177125032 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '9.8 Gravity', 'Loures', 'Lisboa', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.7842913, -9.1255514, 'OPENSTREETMAP', 'OSM:NODE:12177125032',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12177125032')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('9.8 Gravity'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7842913)) <= 0.00070
               AND ABS(v.longitude - (-9.1255514)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5416412842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Boost Fit', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8179746, -9.1752323, 'OPENSTREETMAP', 'OSM:NODE:5416412842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5416412842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Boost Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8179746)) <= 0.00070
               AND ABS(v.longitude - (-9.1752323)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:139266646 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Atlético Reynolds', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8453205, -9.0893180, 'OPENSTREETMAP', 'OSM:WAY:139266646',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:139266646')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Atlético Reynolds'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8453205)) <= 0.00070
               AND ABS(v.longitude - (-9.0893180)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:736371687 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Meia Laranja', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8570336, -9.1406021, 'OPENSTREETMAP', 'OSM:WAY:736371687',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:736371687')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Meia Laranja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8570336)) <= 0.00070
               AND ABS(v.longitude - (-9.1406021)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:121667826 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Bonjardim', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8168276, -9.1529607, 'OPENSTREETMAP', 'OSM:WAY:121667826',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:121667826')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Bonjardim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8168276)) <= 0.00070
               AND ABS(v.longitude - (-9.1529607)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158599663 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Júlio Camilo Alves', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.9000653, -9.1194675, 'OPENSTREETMAP', 'OSM:WAY:158599663',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158599663')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Júlio Camilo Alves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9000653)) <= 0.00070
               AND ABS(v.longitude - (-9.1194675)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:115601946 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo ABD', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8046165, -9.1010208, 'OPENSTREETMAP', 'OSM:WAY:115601946',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:115601946')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo ABD'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8046165)) <= 0.00070
               AND ABS(v.longitude - (-9.1010208)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:868902309 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo "In Omnibus" do F.C. Prior Velho', 'Loures', 'Lisboa', NULL,
       'Futebol;Futsal', 'Campo desportivo', NULL,
       38.7931208, -9.1228236, 'OPENSTREETMAP', 'OSM:WAY:868902309',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:868902309')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo "In Omnibus" do F.C. Prior Velho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7931208)) <= 0.00070
               AND ABS(v.longitude - (-9.1228236)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152030312 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro', 'Loures', 'Lisboa', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       38.8572580, -9.2036426, 'OPENSTREETMAP', 'OSM:WAY:152030312',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152030312')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8572580)) <= 0.00070
               AND ABS(v.longitude - (-9.2036426)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151806995 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Sport Clube Sanjoanense', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8218127, -9.0948975, 'OPENSTREETMAP', 'OSM:WAY:151806995',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151806995')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Sport Clube Sanjoanense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8218127)) <= 0.00070
               AND ABS(v.longitude - (-9.0948975)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:195774857 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Nº.2', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7897287, -9.1021340, 'OPENSTREETMAP', 'OSM:WAY:195774857',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:195774857')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Nº.2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7897287)) <= 0.00070
               AND ABS(v.longitude - (-9.1021340)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:229056423 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Nº.3', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7892056, -9.1030738, 'OPENSTREETMAP', 'OSM:WAY:229056423',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:229056423')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Nº.3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7892056)) <= 0.00070
               AND ABS(v.longitude - (-9.1030738)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:545869060 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Quinta Barros', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.8036376, -9.1398389, 'OPENSTREETMAP', 'OSM:WAY:545869060',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:545869060')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Quinta Barros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8036376)) <= 0.00070
               AND ABS(v.longitude - (-9.1398389)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152779307 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Futebol de Santa Iria de Azóia', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.8450439, -9.0898836, 'OPENSTREETMAP', 'OSM:WAY:1152779307',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152779307')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Futebol de Santa Iria de Azóia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8450439)) <= 0.00070
               AND ABS(v.longitude - (-9.0898836)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1575607357 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas da Portela', 'Loures', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7834895, -9.1107550, 'OPENSTREETMAP', 'OSM:NODE:1575607357',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1575607357')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas da Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7834895)) <= 0.00070
               AND ABS(v.longitude - (-9.1107550)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:782661189 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Court Tennis ABD', 'Loures', 'Lisboa', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.8054273, -9.1006302, 'OPENSTREETMAP', 'OSM:WAY:782661189',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:782661189')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Court Tennis ABD'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8054273)) <= 0.00070
               AND ABS(v.longitude - (-9.1006302)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9732138080 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crossfit CSAC 2660', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8138216, -9.1569047, 'OPENSTREETMAP', 'OSM:NODE:9732138080',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9732138080')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crossfit CSAC 2660'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8138216)) <= 0.00070
               AND ABS(v.longitude - (-9.1569047)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9447780820 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7820828, -9.1038313, 'OPENSTREETMAP', 'OSM:NODE:9447780820',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9447780820')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7820828)) <= 0.00070
               AND ABS(v.longitude - (-9.1038313)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152030307 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Cultural e Recreativo Murteirense', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8596286, -9.1947259, 'OPENSTREETMAP', 'OSM:WAY:152030307',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152030307')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Cultural e Recreativo Murteirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8596286)) <= 0.00070
               AND ABS(v.longitude - (-9.1947259)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10155643886 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'MOVE', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8410029, -9.1609557, 'OPENSTREETMAP', 'OSM:NODE:10155643886',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10155643886')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('MOVE'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8410029)) <= 0.00070
               AND ABS(v.longitude - (-9.1609557)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:782679344 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Multidesporivo ABD', 'Loures', 'Lisboa', NULL,
       'Futebol;Basquetebol;Atletismo;Voleibol', 'Complexo desportivo', NULL,
       38.8093935, -9.1022793, 'OPENSTREETMAP', 'OSM:WAY:782679344',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:782679344')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Multidesporivo ABD'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8093935)) <= 0.00070
               AND ABS(v.longitude - (-9.1022793)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:147263059 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Portela', 'Loures', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7835173, -9.1107907, 'OPENSTREETMAP', 'OSM:WAY:147263059',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:147263059')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7835173)) <= 0.00070
               AND ABS(v.longitude - (-9.1107907)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:140349626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Loures', 'Loures', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8301548, -9.1639892, 'OPENSTREETMAP', 'OSM:WAY:140349626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:140349626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Loures'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8301548)) <= 0.00070
               AND ABS(v.longitude - (-9.1639892)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:144950206 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Muncipais Santo António dos Cavaleiros', 'Loures', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8097137, -9.1631042, 'OPENSTREETMAP', 'OSM:WAY:144950206',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:144950206')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Muncipais Santo António dos Cavaleiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8097137)) <= 0.00070
               AND ABS(v.longitude - (-9.1631042)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:264932426 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Santa Iria de Azóia', 'Loures', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.8490582, -9.0827794, 'OPENSTREETMAP', 'OSM:WAY:264932426',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:264932426')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Santa Iria de Azóia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8490582)) <= 0.00070
               AND ABS(v.longitude - (-9.0827794)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:864811451 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo José Jorge', 'Loures', 'Lisboa', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.9034673, -9.1753241, 'OPENSTREETMAP', 'OSM:WAY:864811451',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:864811451')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo José Jorge'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9034673)) <= 0.00070
               AND ABS(v.longitude - (-9.1753241)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:192735976 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue ABD', 'Loures', 'Lisboa', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.8042869, -9.1004944, 'OPENSTREETMAP', 'OSM:WAY:192735976',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:192735976')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue ABD'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8042869)) <= 0.00070
               AND ABS(v.longitude - (-9.1004944)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:782661169 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue Desportivo Bento Jesus Carraça', 'Loures', 'Lisboa', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.8064653, -9.1026570, 'OPENSTREETMAP', 'OSM:WAY:782661169',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:782661169')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue Desportivo Bento Jesus Carraça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8064653)) <= 0.00070
               AND ABS(v.longitude - (-9.1026570)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1108366502 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Parque do Parque Urbano do Infantado', 'Loures', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.8412558, -9.1602022, 'OPENSTREETMAP', 'OSM:WAY:1108366502',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1108366502')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Parque do Parque Urbano do Infantado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8412558)) <= 0.00070
               AND ABS(v.longitude - (-9.1602022)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:161060654 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube Pinheiro de Loures', 'Loures', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8386641, -9.1945048, 'OPENSTREETMAP', 'OSM:WAY:161060654',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:161060654')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube Pinheiro de Loures'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8386641)) <= 0.00070
               AND ABS(v.longitude - (-9.1945048)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13105642804 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaFit', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8157534, -9.1573151, 'OPENSTREETMAP', 'OSM:NODE:13105642804',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13105642804')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8157534)) <= 0.00070
               AND ABS(v.longitude - (-9.1573151)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:843657501 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8441834, -9.1619950, 'OPENSTREETMAP', 'OSM:WAY:843657501',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:843657501')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8441834)) <= 0.00070
               AND ABS(v.longitude - (-9.1619950)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1050970120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol - Grupo Desportivo de Ribamar', 'Lourinhã', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1974973, -9.3285433, 'OPENSTREETMAP', 'OSM:WAY:1050970120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1050970120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lourinhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol - Grupo Desportivo de Ribamar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1974973)) <= 0.00070
               AND ABS(v.longitude - (-9.3285433)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:712325120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Nova Era', 'Lourinhã', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.2410887, -9.3048314, 'OPENSTREETMAP', 'OSM:WAY:712325120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:712325120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lourinhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Nova Era'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2410887)) <= 0.00070
               AND ABS(v.longitude - (-9.3048314)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544614351 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Hóquei Clube da Lourinhã', 'Lourinhã', 'Lisboa', NULL,
       'Hóquei', 'Estádio', NULL,
       39.2443428, -9.3113490, 'OPENSTREETMAP', 'OSM:WAY:544614351',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544614351')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lourinhã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Hóquei Clube da Lourinhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2443428)) <= 0.00070
               AND ABS(v.longitude - (-9.3113490)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3727421009 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Blue Ocean Ericeira Surf School & Rentals', 'Mafra', 'Lisboa', NULL,
       'Surf', 'Complexo desportivo', NULL,
       38.9876697, -9.4179266, 'OPENSTREETMAP', 'OSM:NODE:3727421009',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3727421009')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Blue Ocean Ericeira Surf School & Rentals'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9876697)) <= 0.00070
               AND ABS(v.longitude - (-9.4179266)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:577818298 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Boardriders', 'Mafra', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.9762648, -9.4183964, 'OPENSTREETMAP', 'OSM:WAY:577818298',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:577818298')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Boardriders'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9762648)) <= 0.00070
               AND ABS(v.longitude - (-9.4183964)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:290974358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Bica', 'Mafra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9233276, -9.2183649, 'OPENSTREETMAP', 'OSM:WAY:290974358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:290974358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Bica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9233276)) <= 0.00070
               AND ABS(v.longitude - (-9.2183649)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:124866085 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Doutor Mário Silveira', 'Mafra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9363047, -9.3324894, 'OPENSTREETMAP', 'OSM:WAY:124866085',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:124866085')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Doutor Mário Silveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9363047)) <= 0.00070
               AND ABS(v.longitude - (-9.3324894)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:431233076 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Henrique Tomás Frade', 'Mafra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9662021, -9.4113265, 'OPENSTREETMAP', 'OSM:WAY:431233076',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:431233076')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Henrique Tomás Frade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9662021)) <= 0.00070
               AND ABS(v.longitude - (-9.4113265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152027460 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo José dos Santos Machado Alegre', 'Mafra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9353039, -9.2600414, 'OPENSTREETMAP', 'OSM:WAY:152027460',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152027460')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo José dos Santos Machado Alegre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9353039)) <= 0.00070
               AND ABS(v.longitude - (-9.2600414)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1342232737 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CoolGym CoolPadel Ericeira', 'Mafra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.9652448, -9.4007020, 'OPENSTREETMAP', 'OSM:WAY:1342232737',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1342232737')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CoolGym CoolPadel Ericeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9652448)) <= 0.00070
               AND ABS(v.longitude - (-9.4007020)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:631359105 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Desportivo União Gradilense', 'Mafra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9822570, -9.2825685, 'OPENSTREETMAP', 'OSM:WAY:631359105',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:631359105')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Desportivo União Gradilense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9822570)) <= 0.00070
               AND ABS(v.longitude - (-9.2825685)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12833201656 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mega Campo', 'Mafra', 'Lisboa', NULL,
       'Tiro', 'Campo desportivo', NULL,
       39.0077076, -9.3238430, 'OPENSTREETMAP', 'OSM:NODE:12833201656',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12833201656')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mega Campo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0077076)) <= 0.00070
               AND ABS(v.longitude - (-9.3238430)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1050568564 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'My Padel Center Mafra', 'Mafra', 'Lisboa', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.9434957, -9.3403343, 'OPENSTREETMAP', 'OSM:WAY:1050568564',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1050568564')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('My Padel Center Mafra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9434957)) <= 0.00070
               AND ABS(v.longitude - (-9.3403343)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:124866082 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Cobertas', 'Mafra', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.9408275, -9.3390479, 'OPENSTREETMAP', 'OSM:WAY:124866082',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:124866082')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9408275)) <= 0.00070
               AND ABS(v.longitude - (-9.3390479)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3067600052 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Mafra', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.9213950, -9.2351962, 'OPENSTREETMAP', 'OSM:NODE:3067600052',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3067600052')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9213950)) <= 0.00070
               AND ABS(v.longitude - (-9.2351962)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1239825815 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais da Encarnação', 'Mafra', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.0221122, -9.3709030, 'OPENSTREETMAP', 'OSM:WAY:1239825815',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1239825815')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais da Encarnação'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0221122)) <= 0.00070
               AND ABS(v.longitude - (-9.3709030)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11653189000 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Unlimited Crossbox', 'Mafra', 'Lisboa', NULL,
       'Ginásio;Halterofilismo', 'Ginásio', NULL,
       38.9430728, -9.3399234, 'OPENSTREETMAP', 'OSM:NODE:11653189000',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11653189000')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Unlimited Crossbox'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9430728)) <= 0.00070
               AND ABS(v.longitude - (-9.3399234)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138226942 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Tenente Valdez', 'Odivelas', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7782616, -9.2049275, 'OPENSTREETMAP', 'OSM:WAY:138226942',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138226942')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Tenente Valdez'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7782616)) <= 0.00070
               AND ABS(v.longitude - (-9.2049275)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:868443750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo desportivo da UDR Santa Maria', 'Odivelas', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7758948, -9.1953469, 'OPENSTREETMAP', 'OSM:WAY:868443750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:868443750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo desportivo da UDR Santa Maria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7758948)) <= 0.00070
               AND ABS(v.longitude - (-9.1953469)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:510218836 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Castelinho', 'Odivelas', 'Lisboa', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.7943036, -9.1815388, 'OPENSTREETMAP', 'OSM:WAY:510218836',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:510218836')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Castelinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7943036)) <= 0.00070
               AND ABS(v.longitude - (-9.1815388)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4745205228 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parede de Escalada', 'Odivelas', 'Lisboa', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.7775139, -9.2068623, 'OPENSTREETMAP', 'OSM:NODE:4745205228',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4745205228')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parede de Escalada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7775139)) <= 0.00070
               AND ABS(v.longitude - (-9.2068623)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:238360065 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Multiusos de Odivelas', 'Odivelas', 'Lisboa', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       38.7901755, -9.1871488, 'OPENSTREETMAP', 'OSM:WAY:238360065',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238360065')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Multiusos de Odivelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7901755)) <= 0.00070
               AND ABS(v.longitude - (-9.1871488)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:399030866 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Odivelas', 'Odivelas', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7942428, -9.1809774, 'OPENSTREETMAP', 'OSM:WAY:399030866',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:399030866')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Odivelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7942428)) <= 0.00070
               AND ABS(v.longitude - (-9.1809774)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:481607021 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Saltos BTT', 'Odivelas', 'Lisboa', NULL,
       'Ciclismo', 'Complexo desportivo', NULL,
       38.7739714, -9.2097023, 'OPENSTREETMAP', 'OSM:WAY:481607021',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:481607021')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Saltos BTT'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7739714)) <= 0.00070
               AND ABS(v.longitude - (-9.2097023)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:481540988 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Stage Fitness Club', 'Odivelas', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7837525, -9.1842757, 'OPENSTREETMAP', 'OSM:WAY:481540988',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:481540988')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Stage Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7837525)) <= 0.00070
               AND ABS(v.longitude - (-9.1842757)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:364239029 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vertical Wall Escalada', 'Odivelas', 'Lisboa', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.7907529, -9.1679851, 'OPENSTREETMAP', 'OSM:WAY:364239029',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:364239029')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vertical Wall Escalada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7907529)) <= 0.00070
               AND ABS(v.longitude - (-9.1679851)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1528273056 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Odivelas', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7824166, -9.1919808, 'OPENSTREETMAP', 'OSM:WAY:1528273056',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1528273056')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7824166)) <= 0.00070
               AND ABS(v.longitude - (-9.1919808)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:194752883 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos IST', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7377591, -9.3021782, 'OPENSTREETMAP', 'OSM:WAY:194752883',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:194752883')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos IST'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7377591)) <= 0.00070
               AND ABS(v.longitude - (-9.3021782)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:239523949 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Jardim dos Plátanos', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7145421, -9.2339763, 'OPENSTREETMAP', 'OSM:WAY:239523949',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:239523949')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Jardim dos Plátanos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7145421)) <= 0.00070
               AND ABS(v.longitude - (-9.2339763)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:468039340 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Carreira de Tiro do Jamor', 'Oeiras', 'Lisboa', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       38.7089402, -9.2479966, 'OPENSTREETMAP', 'OSM:WAY:468039340',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:468039340')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Carreira de Tiro do Jamor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7089402)) <= 0.00070
               AND ABS(v.longitude - (-9.2479966)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:938359390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Caxias Skate Park', 'Oeiras', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.6991712, -9.2754787, 'OPENSTREETMAP', 'OSM:WAY:938359390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:938359390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Caxias Skate Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6991712)) <= 0.00070
               AND ABS(v.longitude - (-9.2754787)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:49167468 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Nacional de Formação do Jamor', 'Oeiras', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7123218, -9.2544404, 'OPENSTREETMAP', 'OSM:WAY:49167468',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:49167468')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Nacional de Formação do Jamor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7123218)) <= 0.00070
               AND ABS(v.longitude - (-9.2544404)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243583388 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas do Jamor', 'Oeiras', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7032263, -9.2572084, 'OPENSTREETMAP', 'OSM:WAY:243583388',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243583388')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas do Jamor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7032263)) <= 0.00070
               AND ABS(v.longitude - (-9.2572084)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:218059399 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Carlos Queiroz', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.7233839, -9.2263252, 'OPENSTREETMAP', 'OSM:WAY:218059399',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:218059399')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Carlos Queiroz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7233839)) <= 0.00070
               AND ABS(v.longitude - (-9.2263252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185863366 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CVO', 'Oeiras', 'Lisboa', NULL,
       'Voleibol', 'Campo desportivo', NULL,
       38.6844384, -9.3197004, 'OPENSTREETMAP', 'OSM:WAY:185863366',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185863366')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CVO'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6844384)) <= 0.00070
               AND ABS(v.longitude - (-9.3197004)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:265399351 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio ADO', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7006891, -9.3039943, 'OPENSTREETMAP', 'OSM:WAY:265399351',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:265399351')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio ADO'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7006891)) <= 0.00070
               AND ABS(v.longitude - (-9.3039943)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:191581196 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio ENIDH', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6904531, -9.2999636, 'OPENSTREETMAP', 'OSM:WAY:191581196',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:191581196')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio ENIDH'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6904531)) <= 0.00070
               AND ABS(v.longitude - (-9.2999636)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:167009546 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo "Os Fixes"', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7453227, -9.2694696, 'OPENSTREETMAP', 'OSM:WAY:167009546',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:167009546')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo "Os Fixes"'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7453227)) <= 0.00070
               AND ABS(v.longitude - (-9.2694696)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:771321701 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Miraflores', 'Oeiras', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7117633, -9.2268942, 'OPENSTREETMAP', 'OSM:WAY:771321701',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:771321701')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Miraflores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7117633)) <= 0.00070
               AND ABS(v.longitude - (-9.2268942)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155320253 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Inatel de Oeiras', 'Oeiras', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.6790863, -9.3164288, 'OPENSTREETMAP', 'OSM:WAY:155320253',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155320253')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Inatel de Oeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6790863)) <= 0.00070
               AND ABS(v.longitude - (-9.3164288)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:116697781 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina ENIDH', 'Oeiras', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.6911387, -9.3003632, 'OPENSTREETMAP', 'OSM:WAY:116697781',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:116697781')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina ENIDH'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6911387)) <= 0.00070
               AND ABS(v.longitude - (-9.3003632)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:376389537 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Linda-a-Velha', 'Oeiras', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7142392, -9.2330953, 'OPENSTREETMAP', 'OSM:WAY:376389537',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:376389537')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Linda-a-Velha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7142392)) <= 0.00070
               AND ABS(v.longitude - (-9.2330953)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:320148362 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Outurela/Portela', 'Oeiras', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7230674, -9.2242477, 'OPENSTREETMAP', 'OSM:WAY:320148362',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:320148362')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Outurela/Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7230674)) <= 0.00070
               AND ABS(v.longitude - (-9.2242477)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:589670012 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Estádio Nacional', 'Oeiras', 'Lisboa', NULL,
       'Corrida', 'Pista desportiva', NULL,
       38.7089141, -9.2608898, 'OPENSTREETMAP', 'OSM:WAY:589670012',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:589670012')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Estádio Nacional'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7089141)) <= 0.00070
               AND ABS(v.longitude - (-9.2608898)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1899934140 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quinta da Moura Tenis', 'Oeiras', 'Lisboa', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.7183074, -9.2785592, 'OPENSTREETMAP', 'OSM:NODE:1899934140',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1899934140')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quinta da Moura Tenis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7183074)) <= 0.00070
               AND ABS(v.longitude - (-9.2785592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2305454683 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate park Oeiras', 'Oeiras', 'Lisboa', NULL,
       'Skate', 'Complexo desportivo', NULL,
       38.6932355, -9.3234364, 'OPENSTREETMAP', 'OSM:NODE:2305454683',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2305454683')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate park Oeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6932355)) <= 0.00070
               AND ABS(v.longitude - (-9.3234364)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:145697336 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva e Recreativa de Algés', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7067725, -9.2281245, 'OPENSTREETMAP', 'OSM:WAY:145697336',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145697336')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva e Recreativa de Algés'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7067725)) <= 0.00070
               AND ABS(v.longitude - (-9.2281245)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:167009612 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Valejas Atlético Clube', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7367963, -9.2692248, 'OPENSTREETMAP', 'OSM:WAY:167009612',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:167009612')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Valejas Atlético Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7367963)) <= 0.00070
               AND ABS(v.longitude - (-9.2692248)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8732467740 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaFit', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6927098, -9.3282685, 'OPENSTREETMAP', 'OSM:NODE:8732467740',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8732467740')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6927098)) <= 0.00070
               AND ABS(v.longitude - (-9.3282685)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13532678002 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7151054, -9.2989840, 'OPENSTREETMAP', 'OSM:NODE:13532678002',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13532678002')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7151054)) <= 0.00070
               AND ABS(v.longitude - (-9.2989840)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14040208985 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7235286, -9.2142946, 'OPENSTREETMAP', 'OSM:NODE:14040208985',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14040208985')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7235286)) <= 0.00070
               AND ABS(v.longitude - (-9.2142946)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12211730587 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VK Studio', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6971106, -9.2999693, 'OPENSTREETMAP', 'OSM:NODE:12211730587',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12211730587')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VK Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6971106)) <= 0.00070
               AND ABS(v.longitude - (-9.2999693)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11284759292 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Altissimo Escalada', 'Sintra', 'Lisboa', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.7646861, -9.3040637, 'OPENSTREETMAP', 'OSM:NODE:11284759292',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11284759292')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Altissimo Escalada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7646861)) <= 0.00070
               AND ABS(v.longitude - (-9.3040637)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240146249 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Belas Clube Campo Golf Course', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.8092886, -9.2776115, 'OPENSTREETMAP', 'OSM:WAY:240146249',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240146249')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Belas Clube Campo Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8092886)) <= 0.00070
               AND ABS(v.longitude - (-9.2776115)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6380645502 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Alternativo do Bairro', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7685165, -9.2896486, 'OPENSTREETMAP', 'OSM:NODE:6380645502',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6380645502')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Alternativo do Bairro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7685165)) <= 0.00070
               AND ABS(v.longitude - (-9.2896486)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:308826483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo António Pinheiro Pinto Basto', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7789891, -9.2569628, 'OPENSTREETMAP', 'OSM:WAY:308826483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:308826483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo António Pinheiro Pinto Basto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7789891)) <= 0.00070
               AND ABS(v.longitude - (-9.2569628)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:456259690 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Conde de Sucena', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7882565, -9.3748223, 'OPENSTREETMAP', 'OSM:WAY:456259690',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:456259690')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Conde de Sucena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7882565)) <= 0.00070
               AND ABS(v.longitude - (-9.3748223)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1467797819 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de homenagem ao T-REX', 'Sintra', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.7615008, -9.2604949, 'OPENSTREETMAP', 'OSM:WAY:1467797819',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1467797819')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de homenagem ao T-REX'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7615008)) <= 0.00070
               AND ABS(v.longitude - (-9.2604949)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:395766265 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Engenheiro Álvaro Garcia Carvalho', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8132704, -9.4417004, 'OPENSTREETMAP', 'OSM:WAY:395766265',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:395766265')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Engenheiro Álvaro Garcia Carvalho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8132704)) <= 0.00070
               AND ABS(v.longitude - (-9.4417004)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:162579197 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Joaquim Vieira', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7751609, -9.3195778, 'OPENSTREETMAP', 'OSM:WAY:162579197',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:162579197')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Joaquim Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7751609)) <= 0.00070
               AND ABS(v.longitude - (-9.3195778)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11341409241 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Central Park Massamá', 'Sintra', 'Lisboa', NULL,
       'Ginásio;Padel', 'Ginásio', NULL,
       38.7585940, -9.2793014, 'OPENSTREETMAP', 'OSM:NODE:11341409241',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11341409241')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Central Park Massamá'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7585940)) <= 0.00070
               AND ABS(v.longitude - (-9.2793014)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:558553463 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Espaço 1 - Ginásio', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7539728, -9.2708581, 'OPENSTREETMAP', 'OSM:WAY:558553463',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:558553463')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Espaço 1 - Ginásio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7539728)) <= 0.00070
               AND ABS(v.longitude - (-9.2708581)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:558551345 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Espaço 3 - Pavilhão', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7539621, -9.2706686, 'OPENSTREETMAP', 'OSM:WAY:558551345',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:558551345')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Espaço 3 - Pavilhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7539621)) <= 0.00070
               AND ABS(v.longitude - (-9.2706686)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:561905618 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Espaço 4 - Campo Sintético', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7549540, -9.2704833, 'OPENSTREETMAP', 'OSM:WAY:561905618',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:561905618')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Espaço 4 - Campo Sintético'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7549540)) <= 0.00070
               AND ABS(v.longitude - (-9.2704833)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7945381699 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut Massamá', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7508819, -9.2792250, 'OPENSTREETMAP', 'OSM:NODE:7945381699',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7945381699')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut Massamá'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7508819)) <= 0.00070
               AND ABS(v.longitude - (-9.2792250)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4624938690 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place Quinta da Beloura', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7624734, -9.3742200, 'OPENSTREETMAP', 'OSM:NODE:4624938690',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4624938690')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place Quinta da Beloura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7624734)) <= 0.00070
               AND ABS(v.longitude - (-9.3742200)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10831989411 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Life Gymnasium', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7565981, -9.2521481, 'OPENSTREETMAP', 'OSM:NODE:10831989411',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10831989411')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Life Gymnasium'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7565981)) <= 0.00070
               AND ABS(v.longitude - (-9.2521481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10292268693 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Liga dos Amigos de Covas de Ferro', 'Sintra', 'Lisboa', NULL,
       'Futsal', 'Campo desportivo', 'INDOOR',
       38.8655961, -9.2502534, 'OPENSTREETMAP', 'OSM:NODE:10292268693',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10292268693')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Liga dos Amigos de Covas de Ferro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8655961)) <= 0.00070
               AND ABS(v.longitude - (-9.2502534)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5151586555 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque calistécnico de ouressa/mem martins', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7968521, -9.3559526, 'OPENSTREETMAP', 'OSM:NODE:5151586555',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5151586555')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque calistécnico de ouressa/mem martins'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7968521)) <= 0.00070
               AND ABS(v.longitude - (-9.3559526)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1002142870 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos Pardal Monteiro', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.8577766, -9.3182291, 'OPENSTREETMAP', 'OSM:WAY:1002142870',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1002142870')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos Pardal Monteiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8577766)) <= 0.00070
               AND ABS(v.longitude - (-9.3182291)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:263514190 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo do Casal de São José', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7915567, -9.3552367, 'OPENSTREETMAP', 'OSM:WAY:263514190',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:263514190')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo do Casal de São José'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7915567)) <= 0.00070
               AND ABS(v.longitude - (-9.3552367)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138643177 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Patamar do Ténis', 'Sintra', 'Lisboa', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.7947304, -9.3955859, 'OPENSTREETMAP', 'OSM:WAY:138643177',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138643177')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Patamar do Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7947304)) <= 0.00070
               AND ABS(v.longitude - (-9.3955859)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1172752753 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina da Praia das Maçãs', 'Sintra', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.8239365, -9.4679327, 'OPENSTREETMAP', 'OSM:WAY:1172752753',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1172752753')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina da Praia das Maçãs'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8239365)) <= 0.00070
               AND ABS(v.longitude - (-9.4679327)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:403151755 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Complexo Desportivo Municipal de Ouressa', 'Sintra', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7987748, -9.3533623, 'OPENSTREETMAP', 'OSM:WAY:403151755',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:403151755')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Complexo Desportivo Municipal de Ouressa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7987748)) <= 0.00070
               AND ABS(v.longitude - (-9.3533623)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:508502137 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina dos Bombeiros de Colares', 'Sintra', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.8008336, -9.4457081, 'OPENSTREETMAP', 'OSM:WAY:508502137',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:508502137')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina dos Bombeiros de Colares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8008336)) <= 0.00070
               AND ABS(v.longitude - (-9.4457081)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:502618974 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Oceânica das Azenhas do Mar', 'Sintra', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.8405267, -9.4624399, 'OPENSTREETMAP', 'OSM:WAY:502618974',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:502618974')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Oceânica das Azenhas do Mar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8405267)) <= 0.00070
               AND ABS(v.longitude - (-9.4624399)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11793112535 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Real de Massamá', 'Sintra', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7556095, -9.2770702, 'OPENSTREETMAP', 'OSM:NODE:11793112535',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11793112535')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Real de Massamá'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7556095)) <= 0.00070
               AND ABS(v.longitude - (-9.2770702)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:200224244 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Oceânicas da Praia das Maçãs', 'Sintra', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8239529, -9.4672649, 'OPENSTREETMAP', 'OSM:WAY:200224244',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:200224244')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Oceânicas da Praia das Maçãs'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8239529)) <= 0.00070
               AND ABS(v.longitude - (-9.4672649)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825465361 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo Real Sport Clube', 'Sintra', 'Lisboa', NULL,
       'Corrida', 'Pista desportiva', NULL,
       38.7521161, -9.2696923, 'OPENSTREETMAP', 'OSM:WAY:825465361',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825465361')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo Real Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7521161)) <= 0.00070
               AND ABS(v.longitude - (-9.2696923)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5074605532 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Progresso Sport Club', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8013895, -9.3436467, 'OPENSTREETMAP', 'OSM:NODE:5074605532',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5074605532')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Progresso Sport Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8013895)) <= 0.00070
               AND ABS(v.longitude - (-9.3436467)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:163243153 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sociedade União 1º Dezembro', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.7882676, -9.3750796, 'OPENSTREETMAP', 'OSM:WAY:163243153',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:163243153')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sociedade União 1º Dezembro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7882676)) <= 0.00070
               AND ABS(v.longitude - (-9.3750796)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
