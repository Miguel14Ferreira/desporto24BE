-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:335841607 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva e Cultural Várzea do Douro', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0727745, -8.2750879, 'OPENSTREETMAP', 'OSM:WAY:335841607',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:335841607')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva e Cultural Várzea do Douro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0727745)) <= 0.00070
               AND ABS(v.longitude - (-8.2750879)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1475170403 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Banho', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2557786, -8.1378903, 'OPENSTREETMAP', 'OSM:WAY:1475170403',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1475170403')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Banho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2557786)) <= 0.00070
               AND ABS(v.longitude - (-8.1378903)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:252637049 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Carvalhosa', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2436626, -8.1639333, 'OPENSTREETMAP', 'OSM:WAY:252637049',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:252637049')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Carvalhosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2436626)) <= 0.00070
               AND ABS(v.longitude - (-8.1639333)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127883543 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Paços de Gaiolo', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1045107, -8.1046040, 'OPENSTREETMAP', 'OSM:WAY:127883543',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127883543')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Paços de Gaiolo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1045107)) <= 0.00070
               AND ABS(v.longitude - (-8.1046040)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:120909354 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Lourenço', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1046672, -8.1955245, 'OPENSTREETMAP', 'OSM:WAY:120909354',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:120909354')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Lourenço'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1046672)) <= 0.00070
               AND ABS(v.longitude - (-8.1955245)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1317989615 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos Marco Canaveses', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1932845, -8.1444949, 'OPENSTREETMAP', 'OSM:WAY:1317989615',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1317989615')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos Marco Canaveses'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1932845)) <= 0.00070
               AND ABS(v.longitude - (-8.1444949)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:252636932 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Américo Monteiro', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2235985, -8.1499144, 'OPENSTREETMAP', 'OSM:WAY:252636932',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:252636932')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Américo Monteiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2235985)) <= 0.00070
               AND ABS(v.longitude - (-8.1499144)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122351773 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio municipal de Alpendorada', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0884530, -8.2515469, 'OPENSTREETMAP', 'OSM:WAY:122351773',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122351773')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio municipal de Alpendorada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0884530)) <= 0.00070
               AND ABS(v.longitude - (-8.2515469)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54662917 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal do Marco de Canaveses', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1924065, -8.1449226, 'OPENSTREETMAP', 'OSM:WAY:54662917',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54662917')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal do Marco de Canaveses'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1924065)) <= 0.00070
               AND ABS(v.longitude - (-8.1449226)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12539381724 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kalorias', 'Marco de Canaveses', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1849277, -8.1538777, 'OPENSTREETMAP', 'OSM:NODE:12539381724',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12539381724')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kalorias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1849277)) <= 0.00070
               AND ABS(v.longitude - (-8.1538777)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1170044261 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo D. Maria Carolina Albuquerque Vasconcelos', 'Marco de Canaveses', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2017467, -8.1898353, 'OPENSTREETMAP', 'OSM:WAY:1170044261',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1170044261')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo D. Maria Carolina Albuquerque Vasconcelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2017467)) <= 0.00070
               AND ABS(v.longitude - (-8.1898353)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:778099616 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Marco de Canaveses', 'Marco de Canaveses', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1944772, -8.1455103, 'OPENSTREETMAP', 'OSM:WAY:778099616',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:778099616')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Marco de Canaveses'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1944772)) <= 0.00070
               AND ABS(v.longitude - (-8.1455103)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168883966 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Alpendorada', 'Marco de Canaveses', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0868855, -8.2532902, 'OPENSTREETMAP', 'OSM:WAY:1168883966',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168883966')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marco de Canaveses'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Alpendorada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0868855)) <= 0.00070
               AND ABS(v.longitude - (-8.2532902)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12824099194 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia António Mesquita', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1907866, -8.6090275, 'OPENSTREETMAP', 'OSM:NODE:12824099194',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12824099194')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia António Mesquita'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1907866)) <= 0.00070
               AND ABS(v.longitude - (-8.6090275)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13381822438 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Paulo Magalhães', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2134048, -8.6142757, 'OPENSTREETMAP', 'OSM:NODE:13381822438',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13381822438')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Paulo Magalhães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2134048)) <= 0.00070
               AND ABS(v.longitude - (-8.6142757)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8786883505 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Workout', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1758856, -8.6849638, 'OPENSTREETMAP', 'OSM:NODE:8786883505',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8786883505')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Workout'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1758856)) <= 0.00070
               AND ABS(v.longitude - (-8.6849638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8625406397 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Amazing Gym', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1768128, -8.6762777, 'OPENSTREETMAP', 'OSM:NODE:8625406397',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8625406397')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Amazing Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1768128)) <= 0.00070
               AND ABS(v.longitude - (-8.6762777)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8786727815 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BPS - Balance Pilates Studio', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1745092, -8.6823543, 'OPENSTREETMAP', 'OSM:NODE:8786727815',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8786727815')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BPS - Balance Pilates Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1745092)) <= 0.00070
               AND ABS(v.longitude - (-8.6823543)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:25687861 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Gatões Futebol Clube', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2113742, -8.6622992, 'OPENSTREETMAP', 'OSM:WAY:25687861',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:25687861')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Gatões Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2113742)) <= 0.00070
               AND ABS(v.longitude - (-8.6622992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:100956050 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de tiro de Guifões', 'Matosinhos', 'Porto', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       41.2004602, -8.6768343, 'OPENSTREETMAP', 'OSM:WAY:100956050',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:100956050')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de tiro de Guifões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2004602)) <= 0.00070
               AND ABS(v.longitude - (-8.6768343)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182937243 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Custóias Futebol Clube', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2068196, -8.6414509, 'OPENSTREETMAP', 'OSM:WAY:182937243',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182937243')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Custóias Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2068196)) <= 0.00070
               AND ABS(v.longitude - (-8.6414509)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:561397367 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Padroense FC', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1868903, -8.6350393, 'OPENSTREETMAP', 'OSM:WAY:561397367',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:561397367')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Padroense FC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1868903)) <= 0.00070
               AND ABS(v.longitude - (-8.6350393)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:561397366 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético do Padroense FC', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1876241, -8.6353689, 'OPENSTREETMAP', 'OSM:WAY:561397366',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:561397366')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético do Padroense FC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1876241)) <= 0.00070
               AND ABS(v.longitude - (-8.6353689)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11418686826 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Coach Ach Alice', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1843191, -8.6912394, 'OPENSTREETMAP', 'OSM:NODE:11418686826',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11418686826')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Coach Ach Alice'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1843191)) <= 0.00070
               AND ABS(v.longitude - (-8.6912394)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:446754873 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo desportivo da Bataria', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2043585, -8.6936952, 'OPENSTREETMAP', 'OSM:WAY:446754873',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:446754873')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo desportivo da Bataria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2043585)) <= 0.00070
               AND ABS(v.longitude - (-8.6936952)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:446754875 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo desportivo da Bataria', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2037944, -8.6947339, 'OPENSTREETMAP', 'OSM:WAY:446754875',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:446754875')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo desportivo da Bataria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2037944)) <= 0.00070
               AND ABS(v.longitude - (-8.6947339)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9756670161 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crosspunch', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1823306, -8.6930040, 'OPENSTREETMAP', 'OSM:NODE:9756670161',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9756670161')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crosspunch'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1823306)) <= 0.00070
               AND ABS(v.longitude - (-8.6930040)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11410865399 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Dna Sports Lab', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1825352, -8.6889986, 'OPENSTREETMAP', 'OSM:NODE:11410865399',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11410865399')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Dna Sports Lab'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1825352)) <= 0.00070
               AND ABS(v.longitude - (-8.6889986)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8839964697 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'eBody - Next Generation', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1759774, -8.6897841, 'OPENSTREETMAP', 'OSM:NODE:8839964697',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8839964697')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('eBody - Next Generation'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1759774)) <= 0.00070
               AND ABS(v.longitude - (-8.6897841)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:561397368 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Padroense FC', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1868867, -8.6353076, 'OPENSTREETMAP', 'OSM:WAY:561397368',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:561397368')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Padroense FC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1868867)) <= 0.00070
               AND ABS(v.longitude - (-8.6353076)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:244968804 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Lavrense', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2537370, -8.7195359, 'OPENSTREETMAP', 'OSM:WAY:244968804',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244968804')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Lavrense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2537370)) <= 0.00070
               AND ABS(v.longitude - (-8.7195359)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8846109944 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estúdio Pilates Corine Costa', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1747140, -8.6866636, 'OPENSTREETMAP', 'OSM:NODE:8846109944',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8846109944')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estúdio Pilates Corine Costa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1747140)) <= 0.00070
               AND ABS(v.longitude - (-8.6866636)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4316811664 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Feel Free', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1999559, -8.6991030, 'OPENSTREETMAP', 'OSM:NODE:4316811664',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4316811664')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Feel Free'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1999559)) <= 0.00070
               AND ABS(v.longitude - (-8.6991030)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8833324530 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fit Home', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1776974, -8.6861938, 'OPENSTREETMAP', 'OSM:NODE:8833324530',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8833324530')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fit Home'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1776974)) <= 0.00070
               AND ABS(v.longitude - (-8.6861938)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1117697930 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2081659, -8.6860305, 'OPENSTREETMAP', 'OSM:WAY:1117697930',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1117697930')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2081659)) <= 0.00070
               AND ABS(v.longitude - (-8.6860305)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8675952724 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up Matosinhos', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1784915, -8.6810656, 'OPENSTREETMAP', 'OSM:NODE:8675952724',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8675952724')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up Matosinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1784915)) <= 0.00070
               AND ABS(v.longitude - (-8.6810656)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8867612970 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fusion Fitness', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1800797, -8.6870448, 'OPENSTREETMAP', 'OSM:NODE:8867612970',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8867612970')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fusion Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1800797)) <= 0.00070
               AND ABS(v.longitude - (-8.6870448)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11413691004 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fusion Inboxe', 'Matosinhos', 'Porto', NULL,
       'Boxe', 'Ginásio', NULL,
       41.1811932, -8.6885950, 'OPENSTREETMAP', 'OSM:NODE:11413691004',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11413691004')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fusion Inboxe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1811932)) <= 0.00070
               AND ABS(v.longitude - (-8.6885950)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8786787923 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Hot Yoga', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1758400, -8.6832899, 'OPENSTREETMAP', 'OSM:NODE:8786787923',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8786787923')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Hot Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1758400)) <= 0.00070
               AND ABS(v.longitude - (-8.6832899)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9809585347 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'JUST', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1916880, -8.6526953, 'OPENSTREETMAP', 'OSM:NODE:9809585347',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9809585347')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('JUST'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1916880)) <= 0.00070
               AND ABS(v.longitude - (-8.6526953)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544254293 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'kangaroo', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1829034, -8.6292990, 'OPENSTREETMAP', 'OSM:WAY:544254293',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544254293')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('kangaroo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1829034)) <= 0.00070
               AND ABS(v.longitude - (-8.6292990)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:130533263 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo do Cabo do Mundo', 'Matosinhos', 'Porto', NULL,
       'Karting', 'Pista desportiva', NULL,
       41.2234888, -8.7133356, 'OPENSTREETMAP', 'OSM:WAY:130533263',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:130533263')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo do Cabo do Mundo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2234888)) <= 0.00070
               AND ABS(v.longitude - (-8.7133356)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:372189417 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Leixões Sport Club', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1840724, -8.6667149, 'OPENSTREETMAP', 'OSM:WAY:372189417',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:372189417')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Leixões Sport Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1840724)) <= 0.00070
               AND ABS(v.longitude - (-8.6667149)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1205088679 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lusitanos Futebol Club de Santa Cruz - Parque de Jogos Domingos Soares Lopes', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2164044, -8.6773819, 'OPENSTREETMAP', 'OSM:WAY:1205088679',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1205088679')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lusitanos Futebol Club de Santa Cruz - Parque de Jogos Domingos Soares Lopes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2164044)) <= 0.00070
               AND ABS(v.longitude - (-8.6773819)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9407460383 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Matosinhos Sport', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1850900, -8.6683839, 'OPENSTREETMAP', 'OSM:NODE:9407460383',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9407460383')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Matosinhos Sport'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1850900)) <= 0.00070
               AND ABS(v.longitude - (-8.6683839)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14281719381 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Movimento Zen', 'Matosinhos', 'Porto', NULL,
       'Ginástica', 'Ginásio', NULL,
       41.1806977, -8.6865548, 'OPENSTREETMAP', 'OSM:NODE:14281719381',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14281719381')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Movimento Zen'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1806977)) <= 0.00070
               AND ABS(v.longitude - (-8.6865548)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:88220969 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão de Guifões', 'Matosinhos', 'Porto', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       41.1974270, -8.6705211, 'OPENSTREETMAP', 'OSM:WAY:88220969',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:88220969')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão de Guifões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1974270)) <= 0.00070
               AND ABS(v.longitude - (-8.6705211)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305407182 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal de Leça da Palmeira', 'Matosinhos', 'Porto', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       41.2053745, -8.6937848, 'OPENSTREETMAP', 'OSM:WAY:305407182',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305407182')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal de Leça da Palmeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2053745)) <= 0.00070
               AND ABS(v.longitude - (-8.6937848)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8857016772 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pilates Aviz Loft', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1780432, -8.6912799, 'OPENSTREETMAP', 'OSM:NODE:8857016772',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8857016772')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pilates Aviz Loft'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1780432)) <= 0.00070
               AND ABS(v.longitude - (-8.6912799)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:569716660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Adultos', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1811659, -8.6675775, 'OPENSTREETMAP', 'OSM:WAY:569716660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:569716660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Adultos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1811659)) <= 0.00070
               AND ABS(v.longitude - (-8.6675775)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:569716661 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Crianças', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1810743, -8.6677529, 'OPENSTREETMAP', 'OSM:WAY:569716661',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:569716661')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Crianças'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1810743)) <= 0.00070
               AND ABS(v.longitude - (-8.6677529)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:316314331 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Custóias', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1940033, -8.6436511, 'OPENSTREETMAP', 'OSM:WAY:316314331',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:316314331')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Custóias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1940033)) <= 0.00070
               AND ABS(v.longitude - (-8.6436511)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:88213864 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Guifões', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1991803, -8.6663902, 'OPENSTREETMAP', 'OSM:WAY:88213864',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:88213864')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Guifões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1991803)) <= 0.00070
               AND ABS(v.longitude - (-8.6663902)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1025351627 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Leça do Balio', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2187961, -8.6318025, 'OPENSTREETMAP', 'OSM:WAY:1025351627',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1025351627')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Leça do Balio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2187961)) <= 0.00070
               AND ABS(v.longitude - (-8.6318025)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:179137447 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Perafita', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2220450, -8.7006877, 'OPENSTREETMAP', 'OSM:WAY:179137447',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:179137447')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Perafita'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2220450)) <= 0.00070
               AND ABS(v.longitude - (-8.7006877)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:251861229 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de São Mamede', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1969249, -8.6024990, 'OPENSTREETMAP', 'OSM:WAY:251861229',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:251861229')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de São Mamede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1969249)) <= 0.00070
               AND ABS(v.longitude - (-8.6024990)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:55597554 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Matosinhos', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1833246, -8.6783158, 'OPENSTREETMAP', 'OSM:WAY:55597554',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:55597554')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Matosinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1833246)) <= 0.00070
               AND ABS(v.longitude - (-8.6783158)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8786787918 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PowerSoul', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1748435, -8.6828819, 'OPENSTREETMAP', 'OSM:NODE:8786787918',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8786787918')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PowerSoul'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1748435)) <= 0.00070
               AND ABS(v.longitude - (-8.6828819)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8796445147 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Proa Climbing Center', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1788188, -8.6822036, 'OPENSTREETMAP', 'OSM:NODE:8796445147',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8796445147')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Proa Climbing Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1788188)) <= 0.00070
               AND ABS(v.longitude - (-8.6822036)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544220919 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue do Seixo', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1876222, -8.6315814, 'OPENSTREETMAP', 'OSM:WAY:544220919',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544220919')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue do Seixo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1876222)) <= 0.00070
               AND ABS(v.longitude - (-8.6315814)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1229138293 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Seven Fitness Club', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1850551, -8.6950039, 'OPENSTREETMAP', 'OSM:WAY:1229138293',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1229138293')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Seven Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1850551)) <= 0.00070
               AND ABS(v.longitude - (-8.6950039)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8866357323 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Shape Fitness Club', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1789615, -8.6875872, 'OPENSTREETMAP', 'OSM:NODE:8866357323',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8866357323')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Shape Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1789615)) <= 0.00070
               AND ABS(v.longitude - (-8.6875872)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1739869711 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Shape II', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1777890, -8.6881753, 'OPENSTREETMAP', 'OSM:NODE:1739869711',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1739869711')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Shape II'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1777890)) <= 0.00070
               AND ABS(v.longitude - (-8.6881753)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:552898308 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Parque de Matosinhos', 'Matosinhos', 'Porto', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.1850346, -8.6822342, 'OPENSTREETMAP', 'OSM:WAY:552898308',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:552898308')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Parque de Matosinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1850346)) <= 0.00070
               AND ABS(v.longitude - (-8.6822342)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182519901 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Clube Senhora da Hora', 'Matosinhos', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1868746, -8.6563319, 'OPENSTREETMAP', 'OSM:WAY:182519901',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182519901')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Clube Senhora da Hora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1868746)) <= 0.00070
               AND ABS(v.longitude - (-8.6563319)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8846460123 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studio Wellness', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1738586, -8.6870699, 'OPENSTREETMAP', 'OSM:NODE:8846460123',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8846460123')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studio Wellness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1738586)) <= 0.00070
               AND ABS(v.longitude - (-8.6870699)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:325013303 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ténis - JF Custóias', 'Matosinhos', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.2009527, -8.6377146, 'OPENSTREETMAP', 'OSM:WAY:325013303',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:325013303')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ténis - JF Custóias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2009527)) <= 0.00070
               AND ABS(v.longitude - (-8.6377146)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8769265649 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Pilates Workhouse', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1833033, -8.6848035, 'OPENSTREETMAP', 'OSM:NODE:8769265649',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8769265649')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Pilates Workhouse'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1833033)) <= 0.00070
               AND ABS(v.longitude - (-8.6848035)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8833324453 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ViewsPro', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1781635, -8.6862597, 'OPENSTREETMAP', 'OSM:NODE:8833324453',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8833324453')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ViewsPro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1781635)) <= 0.00070
               AND ABS(v.longitude - (-8.6862597)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:412213314 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Citânia', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3270718, -8.3667297, 'OPENSTREETMAP', 'OSM:WAY:412213314',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:412213314')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Citânia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3270718)) <= 0.00070
               AND ABS(v.longitude - (-8.3667297)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:449457851 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Serôa', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2625879, -8.4480626, 'OPENSTREETMAP', 'OSM:WAY:449457851',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:449457851')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Serôa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2625879)) <= 0.00070
               AND ABS(v.longitude - (-8.4480626)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:535285449 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Fereira', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2697365, -8.3419098, 'OPENSTREETMAP', 'OSM:WAY:535285449',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:535285449')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Fereira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2697365)) <= 0.00070
               AND ABS(v.longitude - (-8.3419098)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:302643702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Codessos', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3303361, -8.3449257, 'OPENSTREETMAP', 'OSM:WAY:302643702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:302643702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Codessos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3303361)) <= 0.00070
               AND ABS(v.longitude - (-8.3449257)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150607024 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Bairro da Cadeia', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol;Futsal', 'Campo desportivo', NULL,
       41.2541022, -8.4421495, 'OPENSTREETMAP', 'OSM:WAY:150607024',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150607024')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Bairro da Cadeia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2541022)) <= 0.00070
               AND ABS(v.longitude - (-8.4421495)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1013107899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo AJM Lamoso', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3245645, -8.3565491, 'OPENSTREETMAP', 'OSM:WAY:1013107899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1013107899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo AJM Lamoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3245645)) <= 0.00070
               AND ABS(v.longitude - (-8.3565491)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:441851849 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Lírio', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3018669, -8.3659463, 'OPENSTREETMAP', 'OSM:WAY:441851849',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:441851849')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Lírio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3018669)) <= 0.00070
               AND ABS(v.longitude - (-8.3659463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150613336 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Engenheiro Edgar Oliveira', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3187356, -8.3368212, 'OPENSTREETMAP', 'OSM:WAY:150613336',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150613336')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Engenheiro Edgar Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3187356)) <= 0.00070
               AND ABS(v.longitude - (-8.3368212)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9456379206 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo do Sport Clube de Freamunde', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol;Atletismo', 'Complexo desportivo', NULL,
       41.2764704, -8.3393731, 'OPENSTREETMAP', 'OSM:NODE:9456379206',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9456379206')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo do Sport Clube de Freamunde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2764704)) <= 0.00070
               AND ABS(v.longitude - (-8.3393731)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1013107381 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Luís Portos', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3097468, -8.3397251, 'OPENSTREETMAP', 'OSM:WAY:1013107381',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1013107381')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Luís Portos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3097468)) <= 0.00070
               AND ABS(v.longitude - (-8.3397251)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135262893 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Capital do Móvel', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2713506, -8.3853821, 'OPENSTREETMAP', 'OSM:WAY:135262893',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135262893')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Capital do Móvel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2713506)) <= 0.00070
               AND ABS(v.longitude - (-8.3853821)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134801005 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Mata Real', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2707627, -8.3845099, 'OPENSTREETMAP', 'OSM:WAY:134801005',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134801005')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Mata Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2707627)) <= 0.00070
               AND ABS(v.longitude - (-8.3845099)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11319732041 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory', 'Paços de Ferreira', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2857733, -8.3611278, 'OPENSTREETMAP', 'OSM:NODE:11319732041',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11319732041')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2857733)) <= 0.00070
               AND ABS(v.longitude - (-8.3611278)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150607012 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube Paços de Ferreira', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2713475, -8.3853556, 'OPENSTREETMAP', 'OSM:WAY:150607012',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150607012')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube Paços de Ferreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2713475)) <= 0.00070
               AND ABS(v.longitude - (-8.3853556)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150607003 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Penamaior', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2826541, -8.4256252, 'OPENSTREETMAP', 'OSM:WAY:150607003',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150607003')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Penamaior'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2826541)) <= 0.00070
               AND ABS(v.longitude - (-8.4256252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150607026 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Vila de Frazão', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2600715, -8.4162893, 'OPENSTREETMAP', 'OSM:WAY:150607026',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150607026')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Vila de Frazão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2600715)) <= 0.00070
               AND ABS(v.longitude - (-8.4162893)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150607023 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pelado 1', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2603897, -8.4154975, 'OPENSTREETMAP', 'OSM:WAY:150607023',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150607023')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pelado 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2603897)) <= 0.00070
               AND ABS(v.longitude - (-8.4154975)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:535285459 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pelado 1', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2757984, -8.3410115, 'OPENSTREETMAP', 'OSM:WAY:535285459',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:535285459')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pelado 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2757984)) <= 0.00070
               AND ABS(v.longitude - (-8.3410115)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150605566 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Freamunde', 'Paços de Ferreira', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2862487, -8.3397235, 'OPENSTREETMAP', 'OSM:WAY:150605566',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150605566')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Freamunde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2862487)) <= 0.00070
               AND ABS(v.longitude - (-8.3397235)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134802095 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Paços de Ferreira', 'Paços de Ferreira', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2731285, -8.3819320, 'OPENSTREETMAP', 'OSM:WAY:134802095',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134802095')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Paços de Ferreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2731285)) <= 0.00070
               AND ABS(v.longitude - (-8.3819320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1017633028 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo do SC Freamunde', 'Paços de Ferreira', 'Porto', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.2763461, -8.3393549, 'OPENSTREETMAP', 'OSM:WAY:1017633028',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1017633028')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo do SC Freamunde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2763461)) <= 0.00070
               AND ABS(v.longitude - (-8.3393549)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1013107900 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sintetico 1', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3271531, -8.3662361, 'OPENSTREETMAP', 'OSM:WAY:1013107900',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1013107900')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sintetico 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3271531)) <= 0.00070
               AND ABS(v.longitude - (-8.3662361)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:366712225 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sintetico 1', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2750520, -8.3370354, 'OPENSTREETMAP', 'OSM:WAY:366712225',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:366712225')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sintetico 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2750520)) <= 0.00070
               AND ABS(v.longitude - (-8.3370354)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:366712226 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sintetico 2', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2752386, -8.3382196, 'OPENSTREETMAP', 'OSM:WAY:366712226',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:366712226')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sintetico 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2752386)) <= 0.00070
               AND ABS(v.longitude - (-8.3382196)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8857374041 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SportStudio Santos', 'Paços de Ferreira', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2906521, -8.3566094, 'OPENSTREETMAP', 'OSM:NODE:8857374041',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8857374041')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SportStudio Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2906521)) <= 0.00070
               AND ABS(v.longitude - (-8.3566094)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:957146501 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'X-Sport', 'Paços de Ferreira', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2929756, -8.3282709, 'OPENSTREETMAP', 'OSM:WAY:957146501',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:957146501')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paços de Ferreira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('X-Sport'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2929756)) <= 0.00070
               AND ABS(v.longitude - (-8.3282709)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:353198021 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Areia Paredes', 'Paredes', 'Porto', NULL,
       'Futebol;Voleibol de Praia;Andebol', 'Campo desportivo', NULL,
       41.2099937, -8.3240959, 'OPENSTREETMAP', 'OSM:WAY:353198021',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:353198021')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Areia Paredes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2099937)) <= 0.00070
               AND ABS(v.longitude - (-8.3240959)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1101958727 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Balmontes', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2253149, -8.3350427, 'OPENSTREETMAP', 'OSM:WAY:1101958727',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1101958727')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Balmontes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2253149)) <= 0.00070
               AND ABS(v.longitude - (-8.3350427)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:969276141 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Alvre', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1292364, -8.4211988, 'OPENSTREETMAP', 'OSM:WAY:969276141',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:969276141')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Alvre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1292364)) <= 0.00070
               AND ABS(v.longitude - (-8.4211988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1225324105 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Futebol Clube de Cristelo', 'Paredes', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2334709, -8.3652112, 'OPENSTREETMAP', 'OSM:WAY:1225324105',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1225324105')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Futebol Clube de Cristelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2334709)) <= 0.00070
               AND ABS(v.longitude - (-8.3652112)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:224690575 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo da Nora', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2058091, -8.3950448, 'OPENSTREETMAP', 'OSM:WAY:224690575',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:224690575')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo da Nora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2058091)) <= 0.00070
               AND ABS(v.longitude - (-8.3950448)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108391456 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Duas Igrejas', 'Paredes', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2297377, -8.3845330, 'OPENSTREETMAP', 'OSM:WAY:108391456',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108391456')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Duas Igrejas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2297377)) <= 0.00070
               AND ABS(v.longitude - (-8.3845330)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:284342014 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Vasco da Gama', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1498180, -8.4224322, 'OPENSTREETMAP', 'OSM:WAY:284342014',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:284342014')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Vasco da Gama'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1498180)) <= 0.00070
               AND ABS(v.longitude - (-8.4224322)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105875008 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Despotivo de Azevido', 'Paredes', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2174123, -8.4080375, 'OPENSTREETMAP', 'OSM:WAY:105875008',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105875008')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Despotivo de Azevido'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2174123)) <= 0.00070
               AND ABS(v.longitude - (-8.4080375)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:125486998 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Cete', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1721246, -8.3596363, 'OPENSTREETMAP', 'OSM:WAY:125486998',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:125486998')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Cete'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1721246)) <= 0.00070
               AND ABS(v.longitude - (-8.3596363)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196226780 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Parada', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1671963, -8.3651089, 'OPENSTREETMAP', 'OSM:WAY:196226780',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196226780')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Parada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1671963)) <= 0.00070
               AND ABS(v.longitude - (-8.3651089)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:195773214 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GD Santa Comba', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1103690, -8.3958075, 'OPENSTREETMAP', 'OSM:WAY:195773214',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:195773214')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GD Santa Comba'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1103690)) <= 0.00070
               AND ABS(v.longitude - (-8.3958075)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10754712855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Padel Clube Idealkorpus', 'Paredes', 'Porto', NULL,
       'Padel', 'Campo desportivo', NULL,
       41.1997793, -8.3471890, 'OPENSTREETMAP', 'OSM:NODE:10754712855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10754712855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Padel Clube Idealkorpus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1997793)) <= 0.00070
               AND ABS(v.longitude - (-8.3471890)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196224723 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Ernesto Silva da Casa do Povo de Sobreira', 'Paredes', 'Porto', NULL,
       'Hóquei', 'Complexo desportivo', NULL,
       41.1451444, -8.3895670, 'OPENSTREETMAP', 'OSM:WAY:196224723',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196224723')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Ernesto Silva da Casa do Povo de Sobreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1451444)) <= 0.00070
               AND ABS(v.longitude - (-8.3895670)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108386142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Paredes', 'Paredes', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2030890, -8.3359286, 'OPENSTREETMAP', 'OSM:WAY:108386142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108386142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Paredes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2030890)) <= 0.00070
               AND ABS(v.longitude - (-8.3359286)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150223053 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Lordelo', 'Paredes', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2364113, -8.4136076, 'OPENSTREETMAP', 'OSM:WAY:150223053',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150223053')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Lordelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2364113)) <= 0.00070
               AND ABS(v.longitude - (-8.4136076)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:124281275 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Rota dos Móveis', 'Paredes', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1532143, -8.4048825, 'OPENSTREETMAP', 'OSM:WAY:124281275',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:124281275')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Rota dos Móveis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1532143)) <= 0.00070
               AND ABS(v.longitude - (-8.4048825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108209629 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pisicina de Rebordosa', 'Paredes', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2208429, -8.4122146, 'OPENSTREETMAP', 'OSM:WAY:108209629',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108209629')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pisicina de Rebordosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2208429)) <= 0.00070
               AND ABS(v.longitude - (-8.4122146)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105613350 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Cête', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1759571, -8.3519137, 'OPENSTREETMAP', 'OSM:WAY:105613350',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105613350')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Cête'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1759571)) <= 0.00070
               AND ABS(v.longitude - (-8.3519137)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:234454198 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do RC', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2240512, -8.4065577, 'OPENSTREETMAP', 'OSM:WAY:234454198',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:234454198')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do RC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2240512)) <= 0.00070
               AND ABS(v.longitude - (-8.4065577)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:284342018 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Clube Nun''Álvares', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1480899, -8.4254053, 'OPENSTREETMAP', 'OSM:WAY:284342018',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:284342018')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Clube Nun''Álvares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1480899)) <= 0.00070
               AND ABS(v.longitude - (-8.4254053)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12069122515 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BSPlace', 'Penafiel', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2121642, -8.2706761, 'OPENSTREETMAP', 'OSM:NODE:12069122515',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12069122515')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BSPlace'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2121642)) <= 0.00070
               AND ABS(v.longitude - (-8.2706761)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:430389994 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Baralha', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0509657, -8.3583931, 'OPENSTREETMAP', 'OSM:WAY:430389994',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:430389994')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Baralha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0509657)) <= 0.00070
               AND ABS(v.longitude - (-8.3583931)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:447180335 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Bustelo', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2320666, -8.2791632, 'OPENSTREETMAP', 'OSM:WAY:447180335',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:447180335')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Bustelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2320666)) <= 0.00070
               AND ABS(v.longitude - (-8.2791632)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:889961218 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Perafita', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1790347, -8.2550764, 'OPENSTREETMAP', 'OSM:WAY:889961218',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:889961218')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Perafita'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1790347)) <= 0.00070
               AND ABS(v.longitude - (-8.2550764)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1170503190 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Vicente', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1240137, -8.3086589, 'OPENSTREETMAP', 'OSM:WAY:1170503190',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1170503190')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Vicente'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1240137)) <= 0.00070
               AND ABS(v.longitude - (-8.3086589)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:291221307 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Lusitano Clube de Retorta', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1960491, -8.3164725, 'OPENSTREETMAP', 'OSM:WAY:291221307',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:291221307')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Lusitano Clube de Retorta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1960491)) <= 0.00070
               AND ABS(v.longitude - (-8.3164725)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:535374748 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo desportivo da Associação Desportiva de Cabroelo', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1004340, -8.3302107, 'OPENSTREETMAP', 'OSM:WAY:535374748',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:535374748')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo desportivo da Associação Desportiva de Cabroelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1004340)) <= 0.00070
               AND ABS(v.longitude - (-8.3302107)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1255254863 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'DLUX Fitness', 'Penafiel', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1159565, -8.2649720, 'OPENSTREETMAP', 'OSM:WAY:1255254863',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1255254863')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('DLUX Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1159565)) <= 0.00070
               AND ABS(v.longitude - (-8.2649720)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:461270736 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Bela Vista', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1007134, -8.2712242, 'OPENSTREETMAP', 'OSM:WAY:461270736',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:461270736')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Bela Vista'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1007134)) <= 0.00070
               AND ABS(v.longitude - (-8.2712242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1170500263 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube Termas São Vicente', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1194677, -8.2963709, 'OPENSTREETMAP', 'OSM:WAY:1170500263',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1170500263')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube Termas São Vicente'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1194677)) <= 0.00070
               AND ABS(v.longitude - (-8.2963709)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:448161695 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GDR Rans / Dragon Force Rans', 'Penafiel', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1656422, -8.2899510, 'OPENSTREETMAP', 'OSM:WAY:448161695',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:448161695')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GDR Rans / Dragon Force Rans'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1656422)) <= 0.00070
               AND ABS(v.longitude - (-8.2899510)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1494671776 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lagoa Artifícial', 'Penafiel', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1991054, -8.2797157, 'OPENSTREETMAP', 'OSM:WAY:1494671776',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1494671776')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lagoa Artifícial'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1991054)) <= 0.00070
               AND ABS(v.longitude - (-8.2797157)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1494671777 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lago do Galeão', 'Penafiel', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1996842, -8.2792098, 'OPENSTREETMAP', 'OSM:WAY:1494671777',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1494671777')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lago do Galeão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1996842)) <= 0.00070
               AND ABS(v.longitude - (-8.2792098)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1485813617 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas municipais das Termas de São Vincente', 'Penafiel', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1185518, -8.2949350, 'OPENSTREETMAP', 'OSM:WAY:1485813617',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1485813617')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas municipais das Termas de São Vincente'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1185518)) <= 0.00070
               AND ABS(v.longitude - (-8.2949350)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:137206729 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Penafiel', 'Penafiel', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1673809, -8.3335318, 'OPENSTREETMAP', 'OSM:WAY:137206729',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:137206729')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Penafiel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1673809)) <= 0.00070
               AND ABS(v.longitude - (-8.3335318)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8586055021 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be In Balance', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1600803, -8.6282331, 'OPENSTREETMAP', 'OSM:NODE:8586055021',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8586055021')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be In Balance'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1600803)) <= 0.00070
               AND ABS(v.longitude - (-8.6282331)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9020366847 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Blueprint Personal Training', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1630902, -8.6614898, 'OPENSTREETMAP', 'OSM:NODE:9020366847',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9020366847')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Blueprint Personal Training'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1630902)) <= 0.00070
               AND ABS(v.longitude - (-8.6614898)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1064355870 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BN Vitória Campanhã', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1451497, -8.5744552, 'OPENSTREETMAP', 'OSM:WAY:1064355870',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1064355870')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BN Vitória Campanhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1451497)) <= 0.00070
               AND ABS(v.longitude - (-8.5744552)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:449426290 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Boavista Futebol Clube', 'Porto', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1626925, -8.6416652, 'OPENSTREETMAP', 'OSM:WAY:449426290',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:449426290')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Boavista Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1626925)) <= 0.00070
               AND ABS(v.longitude - (-8.6416652)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:755451318 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Breathe Sport Fitness', 'Porto', 'Porto', NULL,
       'Ténis;Ginásio', 'Ginásio', NULL,
       41.1716592, -8.6819408, 'OPENSTREETMAP', 'OSM:WAY:755451318',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:755451318')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Breathe Sport Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1716592)) <= 0.00070
               AND ABS(v.longitude - (-8.6819408)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:448746715 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Ervilha', 'Porto', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1599427, -8.6714979, 'OPENSTREETMAP', 'OSM:WAY:448746715',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:448746715')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Ervilha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1599427)) <= 0.00070
               AND ABS(v.longitude - (-8.6714979)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:334849388 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético do Viso', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1819461, -8.6450944, 'OPENSTREETMAP', 'OSM:WAY:334849388',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:334849388')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético do Viso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1819461)) <= 0.00070
               AND ABS(v.longitude - (-8.6450944)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181397779 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Universitário da Fadeup', 'Porto', 'Porto', NULL,
       'Futebol;Atletismo', 'Campo desportivo', NULL,
       41.1788034, -8.6040639, 'OPENSTREETMAP', 'OSM:WAY:181397779',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181397779')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Universitário da Fadeup'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1788034)) <= 0.00070
               AND ABS(v.longitude - (-8.6040639)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:450905752 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Central Court', 'Porto', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1610780, -8.5886198, 'OPENSTREETMAP', 'OSM:WAY:450905752',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:450905752')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Central Court'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1610780)) <= 0.00070
               AND ABS(v.longitude - (-8.5886198)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11429488563 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Desporto da Universidade do Porto', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1521167, -8.6209024, 'OPENSTREETMAP', 'OSM:NODE:11429488563',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11429488563')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Desporto da Universidade do Porto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1521167)) <= 0.00070
               AND ABS(v.longitude - (-8.6209024)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9991170194 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Club One II One', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1636295, -8.5991912, 'OPENSTREETMAP', 'OSM:NODE:9991170194',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9991170194')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Club One II One'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1636295)) <= 0.00070
               AND ABS(v.longitude - (-8.5991912)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305965767 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'court 1', 'Porto', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1609639, -8.6143257, 'OPENSTREETMAP', 'OSM:WAY:305965767',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305965767')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('court 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1609639)) <= 0.00070
               AND ABS(v.longitude - (-8.6143257)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305965765 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'court 2', 'Porto', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1609268, -8.6139709, 'OPENSTREETMAP', 'OSM:WAY:305965765',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305965765')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('court 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1609268)) <= 0.00070
               AND ABS(v.longitude - (-8.6139709)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305965764 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'court 3', 'Porto', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1609022, -8.6137609, 'OPENSTREETMAP', 'OSM:WAY:305965764',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305965764')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('court 3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1609022)) <= 0.00070
               AND ABS(v.longitude - (-8.6137609)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305965762 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'court 4', 'Porto', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1613563, -8.6139152, 'OPENSTREETMAP', 'OSM:WAY:305965762',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305965762')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('court 4'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1613563)) <= 0.00070
               AND ABS(v.longitude - (-8.6139152)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305965763 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'court 5', 'Porto', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1613087, -8.6136794, 'OPENSTREETMAP', 'OSM:WAY:305965763',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305965763')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('court 5'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1613087)) <= 0.00070
               AND ABS(v.longitude - (-8.6136794)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10592487508 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CR7 Fitness', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1691560, -8.6317748, 'OPENSTREETMAP', 'OSM:NODE:10592487508',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10592487508')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CR7 Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1691560)) <= 0.00070
               AND ABS(v.longitude - (-8.6317748)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9035098385 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crossfit Durius', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1577248, -8.6321052, 'OPENSTREETMAP', 'OSM:NODE:9035098385',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9035098385')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crossfit Durius'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1577248)) <= 0.00070
               AND ABS(v.longitude - (-8.6321052)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9008531127 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'E-Fit', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1637190, -8.6676481, 'OPENSTREETMAP', 'OSM:NODE:9008531127',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9008531127')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('E-Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1637190)) <= 0.00070
               AND ABS(v.longitude - (-8.6676481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152784750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Bessa Século XXI', 'Porto', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1622050, -8.6427260, 'OPENSTREETMAP', 'OSM:WAY:1152784750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152784750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Bessa Século XXI'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1622050)) <= 0.00070
               AND ABS(v.longitude - (-8.6427260)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10241095904 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1636506, -8.5846876, 'OPENSTREETMAP', 'OSM:NODE:10241095904',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10241095904')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1636506)) <= 0.00070
               AND ABS(v.longitude - (-8.5846876)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8635469500 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up Praça da Galiza', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1520806, -8.6272646, 'OPENSTREETMAP', 'OSM:NODE:8635469500',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8635469500')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up Praça da Galiza'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1520806)) <= 0.00070
               AND ABS(v.longitude - (-8.6272646)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4703883587 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Footspace Indoor Soccer', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1723027, -8.6465027, 'OPENSTREETMAP', 'OSM:NODE:4703883587',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4703883587')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Footspace Indoor Soccer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1723027)) <= 0.00070
               AND ABS(v.longitude - (-8.6465027)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:40588839 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube da Foz', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1599081, -8.6713644, 'OPENSTREETMAP', 'OSM:WAY:40588839',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:40588839')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube da Foz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1599081)) <= 0.00070
               AND ABS(v.longitude - (-8.6713644)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12453706936 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gimnoporto', 'Porto', 'Porto', NULL,
       'Ginásio', 'Complexo desportivo', NULL,
       41.1518316, -8.6042286, 'OPENSTREETMAP', 'OSM:NODE:12453706936',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12453706936')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gimnoporto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1518316)) <= 0.00070
               AND ABS(v.longitude - (-8.6042286)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10130691539 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásico Fonseca', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1599891, -8.5948686, 'OPENSTREETMAP', 'OSM:NODE:10130691539',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10130691539')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásico Fonseca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1599891)) <= 0.00070
               AND ABS(v.longitude - (-8.5948686)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6647824410 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Torres Vilas', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1473537, -8.6065643, 'OPENSTREETMAP', 'OSM:NODE:6647824410',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6647824410')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Torres Vilas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1473537)) <= 0.00070
               AND ABS(v.longitude - (-8.6065643)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:211323750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Vitória', 'Porto', 'Porto', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       41.1443268, -8.6169028, 'OPENSTREETMAP', 'OSM:WAY:211323750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:211323750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Vitória'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1443268)) <= 0.00070
               AND ABS(v.longitude - (-8.6169028)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11163694605 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Go Gym', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1842955, -8.6007712, 'OPENSTREETMAP', 'OSM:NODE:11163694605',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11163694605')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Go Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1842955)) <= 0.00070
               AND ABS(v.longitude - (-8.6007712)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7056507827 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place-Boavista', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1615091, -8.6427644, 'OPENSTREETMAP', 'OSM:NODE:7056507827',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7056507827')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place-Boavista'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1615091)) <= 0.00070
               AND ABS(v.longitude - (-8.6427644)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:450403980 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Indor Soccer Paranhos', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1706384, -8.6024598, 'OPENSTREETMAP', 'OSM:WAY:450403980',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:450403980')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Indor Soccer Paranhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1706384)) <= 0.00070
               AND ABS(v.longitude - (-8.6024598)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7070727855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Inspire Studio - Fitness Pilates & Yoga Studio', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1650233, -8.6695432, 'OPENSTREETMAP', 'OSM:NODE:7070727855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7070727855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Inspire Studio - Fitness Pilates & Yoga Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1650233)) <= 0.00070
               AND ABS(v.longitude - (-8.6695432)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12430425386 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Intentium', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1856199, -8.6039539, 'OPENSTREETMAP', 'OSM:NODE:12430425386',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12430425386')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Intentium'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1856199)) <= 0.00070
               AND ABS(v.longitude - (-8.6039539)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:490250951 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lawn Tennis Clube da Foz', 'Porto', 'Porto', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       41.1486294, -8.6739061, 'OPENSTREETMAP', 'OSM:WAY:490250951',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:490250951')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lawn Tennis Clube da Foz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1486294)) <= 0.00070
               AND ABS(v.longitude - (-8.6739061)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:753557660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Ramalde / INATEL', 'Porto', 'Porto', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.1670100, -8.6584591, 'OPENSTREETMAP', 'OSM:WAY:753557660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:753557660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Ramalde / INATEL'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1670100)) <= 0.00070
               AND ABS(v.longitude - (-8.6584591)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:308993804 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão de Ténis', 'Porto', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.1603307, -8.6138421, 'OPENSTREETMAP', 'OSM:WAY:308993804',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:308993804')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1603307)) <= 0.00070
               AND ABS(v.longitude - (-8.6138421)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104688241 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Campanhã', 'Porto', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1507653, -8.5894430, 'OPENSTREETMAP', 'OSM:WAY:104688241',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104688241')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Campanhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1507653)) <= 0.00070
               AND ABS(v.longitude - (-8.5894430)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:542445803 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Cartes', 'Porto', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1597988, -8.5733357, 'OPENSTREETMAP', 'OSM:WAY:542445803',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:542445803')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Cartes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1597988)) <= 0.00070
               AND ABS(v.longitude - (-8.5733357)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:484603001 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Engenheiro Armando Pimentel', 'Porto', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1500234, -8.6616746, 'OPENSTREETMAP', 'OSM:WAY:484603001',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:484603001')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Engenheiro Armando Pimentel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1500234)) <= 0.00070
               AND ABS(v.longitude - (-8.6616746)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:446856736 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo da FADEUP', 'Porto', 'Porto', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.1786649, -8.6040514, 'OPENSTREETMAP', 'OSM:WAY:446856736',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:446856736')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo da FADEUP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1786649)) <= 0.00070
               AND ABS(v.longitude - (-8.6040514)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:332479840 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo do CDUP - Porto', 'Porto', 'Porto', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.1510531, -8.6428395, 'OPENSTREETMAP', 'OSM:WAY:332479840',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:332479840')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo do CDUP - Porto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1510531)) <= 0.00070
               AND ABS(v.longitude - (-8.6428395)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:256269882 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Vilar', 'Porto', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1494751, -8.6280962, 'OPENSTREETMAP', 'OSM:WAY:256269882',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:256269882')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Vilar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1494751)) <= 0.00070
               AND ABS(v.longitude - (-8.6280962)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9008531128 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Power Studio - Power Plate', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1633121, -8.6679519, 'OPENSTREETMAP', 'OSM:NODE:9008531128',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9008531128')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Power Studio - Power Plate'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1633121)) <= 0.00070
               AND ABS(v.longitude - (-8.6679519)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13858850218 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'React', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1757283, -8.6336259, 'OPENSTREETMAP', 'OSM:NODE:13858850218',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13858850218')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('React'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1757283)) <= 0.00070
               AND ABS(v.longitude - (-8.6336259)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8426376399 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Room 2fit Group', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1656555, -8.6704134, 'OPENSTREETMAP', 'OSM:NODE:8426376399',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8426376399')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Room 2fit Group'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1656555)) <= 0.00070
               AND ABS(v.longitude - (-8.6704134)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1070590582 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Comércio e Salgueiros', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1589115, -8.5728283, 'OPENSTREETMAP', 'OSM:WAY:1070590582',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1070590582')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Comércio e Salgueiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1589115)) <= 0.00070
               AND ABS(v.longitude - (-8.5728283)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13924026986 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studio4all', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1737838, -8.6373239, 'OPENSTREETMAP', 'OSM:NODE:13924026986',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13924026986')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studio4all'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1737838)) <= 0.00070
               AND ABS(v.longitude - (-8.6373239)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8255793583 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Treino no Parque', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1685140, -8.6711697, 'OPENSTREETMAP', 'OSM:NODE:8255793583',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8255793583')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Treino no Parque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1685140)) <= 0.00070
               AND ABS(v.longitude - (-8.6711697)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:474896543 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Académica António Aroso', 'Porto', 'Porto', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       41.1681064, -8.6689284, 'OPENSTREETMAP', 'OSM:WAY:474896543',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:474896543')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Académica António Aroso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1681064)) <= 0.00070
               AND ABS(v.longitude - (-8.6689284)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9754902517 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaFit', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1622549, -8.6129796, 'OPENSTREETMAP', 'OSM:NODE:9754902517',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9754902517')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1622549)) <= 0.00070
               AND ABS(v.longitude - (-8.6129796)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1420012934 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vivagym', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1546757, -8.6296503, 'OPENSTREETMAP', 'OSM:WAY:1420012934',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1420012934')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vivagym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1546757)) <= 0.00070
               AND ABS(v.longitude - (-8.6296503)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:32111339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Atlético da Póvoa de Varzim', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol;Atletismo', 'Campo desportivo', NULL,
       41.3822540, -8.7510365, 'OPENSTREETMAP', 'OSM:WAY:32111339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:32111339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Atlético da Póvoa de Varzim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3822540)) <= 0.00070
               AND ABS(v.longitude - (-8.7510365)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6726566045 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be Fit', 'Póvoa de Varzim', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3786599, -8.7388555, 'OPENSTREETMAP', 'OSM:NODE:6726566045',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6726566045')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3786599)) <= 0.00070
               AND ABS(v.longitude - (-8.7388555)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134700052 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Averomar Futebol Clube', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3993864, -8.7770409, 'OPENSTREETMAP', 'OSM:WAY:134700052',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134700052')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Averomar Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3993864)) <= 0.00070
               AND ABS(v.longitude - (-8.7770409)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:963417278 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Argivai', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3818972, -8.7395638, 'OPENSTREETMAP', 'OSM:WAY:963417278',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:963417278')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Argivai'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3818972)) <= 0.00070
               AND ABS(v.longitude - (-8.7395638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1042339932 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Aguçadoura F.C.', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.4448146, -8.7770861, 'OPENSTREETMAP', 'OSM:WAY:1042339932',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1042339932')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Aguçadoura F.C.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4448146)) <= 0.00070
               AND ABS(v.longitude - (-8.7770861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:771492875 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol MAPADI', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3881708, -8.7619956, 'OPENSTREETMAP', 'OSM:WAY:771492875',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:771492875')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol MAPADI'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3881708)) <= 0.00070
               AND ABS(v.longitude - (-8.7619956)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:832381174 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Xangai', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3870939, -8.7582332, 'OPENSTREETMAP', 'OSM:WAY:832381174',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:832381174')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Xangai'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3870939)) <= 0.00070
               AND ABS(v.longitude - (-8.7582332)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1309939582 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Limarinho', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4265032, -8.6570018, 'OPENSTREETMAP', 'OSM:WAY:1309939582',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1309939582')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Limarinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4265032)) <= 0.00070
               AND ABS(v.longitude - (-8.6570018)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:296579234 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural e Desportivo de Terroso', 'Póvoa de Varzim', 'Porto', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       41.4090352, -8.7201720, 'OPENSTREETMAP', 'OSM:WAY:296579234',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:296579234')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural e Desportivo de Terroso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4090352)) <= 0.00070
               AND ABS(v.longitude - (-8.7201720)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:300915691 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Social Bonitos de Amorim', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3970314, -8.7390558, 'OPENSTREETMAP', 'OSM:WAY:300915691',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:300915691')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Social Bonitos de Amorim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3970314)) <= 0.00070
               AND ABS(v.longitude - (-8.7390558)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:41843466 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo da Póvoa de Varzim', 'Póvoa de Varzim', 'Porto', NULL,
       'Ténis;Natação', 'Complexo desportivo', NULL,
       41.3869792, -8.7735567, 'OPENSTREETMAP', 'OSM:WAY:41843466',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:41843466')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo da Póvoa de Varzim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3869792)) <= 0.00070
               AND ABS(v.longitude - (-8.7735567)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:832382142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'NF GYM', 'Póvoa de Varzim', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3815099, -8.7544339, 'OPENSTREETMAP', 'OSM:WAY:832382142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:832382142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('NF GYM'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3815099)) <= 0.00070
               AND ABS(v.longitude - (-8.7544339)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185323032 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo da Estela', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4452928, -8.7484095, 'OPENSTREETMAP', 'OSM:WAY:185323032',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185323032')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo da Estela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4452928)) <= 0.00070
               AND ABS(v.longitude - (-8.7484095)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:297770304 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Varzim Lazer', 'Póvoa de Varzim', 'Porto', NULL,
       'Voleibol;Basquetebol;Andebol', 'Complexo desportivo', NULL,
       41.3885792, -8.7588641, 'OPENSTREETMAP', 'OSM:WAY:297770304',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:297770304')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Varzim Lazer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3885792)) <= 0.00070
               AND ABS(v.longitude - (-8.7588641)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:766449195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas CDP', 'Póvoa de Varzim', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.3867205, -8.7721874, 'OPENSTREETMAP', 'OSM:WAY:766449195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:766449195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas CDP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3867205)) <= 0.00070
               AND ABS(v.longitude - (-8.7721874)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:766449197 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Póvoa de Varzim', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.3866196, -8.7732029, 'OPENSTREETMAP', 'OSM:WAY:766449197',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:766449197')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3866196)) <= 0.00070
               AND ABS(v.longitude - (-8.7732029)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:298591954 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo da Póvoa de Varzim', 'Póvoa de Varzim', 'Porto', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.4011091, -8.7561366, 'OPENSTREETMAP', 'OSM:WAY:298591954',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:298591954')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo da Póvoa de Varzim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4011091)) <= 0.00070
               AND ABS(v.longitude - (-8.7561366)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:794489641 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park Póvoa de Varzim', 'Póvoa de Varzim', 'Porto', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.3754797, -8.7632189, 'OPENSTREETMAP', 'OSM:WAY:794489641',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:794489641')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park Póvoa de Varzim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3754797)) <= 0.00070
               AND ABS(v.longitude - (-8.7632189)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13504609424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studiofit Varzim Lazer', 'Póvoa de Varzim', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3885732, -8.7590588, 'OPENSTREETMAP', 'OSM:NODE:13504609424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13504609424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studiofit Varzim Lazer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3885732)) <= 0.00070
               AND ABS(v.longitude - (-8.7590588)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
