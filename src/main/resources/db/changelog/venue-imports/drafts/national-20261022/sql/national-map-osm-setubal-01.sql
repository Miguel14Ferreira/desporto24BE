-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:5039626992 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Espaço Cultural Kálí', 'Alcochete', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7546141, -8.9634812, 'OPENSTREETMAP', 'OSM:NODE:5039626992',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5039626992')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Espaço Cultural Kálí'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7546141)) <= 0.00070
               AND ABS(v.longitude - (-8.9634812)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103220649 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio António Almeida Correia (Foni)', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Estádio', NULL,
       38.7545369, -8.9549792, 'OPENSTREETMAP', 'OSM:WAY:103220649',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103220649')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio António Almeida Correia (Foni)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7545369)) <= 0.00070
               AND ABS(v.longitude - (-8.9549792)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14104424821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Soul Shape', 'Alcochete', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7283056, -8.9732072, 'OPENSTREETMAP', 'OSM:NODE:14104424821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14104424821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Soul Shape'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7283056)) <= 0.00070
               AND ABS(v.longitude - (-8.9732072)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6425971142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Alfazema', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6556526, -9.2358243, 'OPENSTREETMAP', 'OSM:NODE:6425971142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6425971142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Alfazema'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6556526)) <= 0.00070
               AND ABS(v.longitude - (-9.2358243)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6427894687 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Align- Body and Mind', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6552975, -9.1775732, 'OPENSTREETMAP', 'OSM:NODE:6427894687',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6427894687')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Align- Body and Mind'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6552975)) <= 0.00070
               AND ABS(v.longitude - (-9.1775732)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5222215101 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Aroeira Fitness Club', 'Almada', 'Setúbal', NULL,
       'Ginástica;Ginásio', 'Complexo desportivo', NULL,
       38.5817332, -9.1764056, 'OPENSTREETMAP', 'OSM:NODE:5222215101',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5222215101')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Aroeira Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5817332)) <= 0.00070
               AND ABS(v.longitude - (-9.1764056)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6441330842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Body Mind Soul Pilates Studio', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6371108, -9.1759346, 'OPENSTREETMAP', 'OSM:NODE:6441330842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6441330842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Body Mind Soul Pilates Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6371108)) <= 0.00070
               AND ABS(v.longitude - (-9.1759346)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:975034909 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Knock Out', 'Almada', 'Setúbal', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.6361606, -9.1754474, 'OPENSTREETMAP', 'OSM:WAY:975034909',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:975034909')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Knock Out'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6361606)) <= 0.00070
               AND ABS(v.longitude - (-9.1754474)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9042106716 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CRAQS', 'Almada', 'Setúbal', NULL,
       'Karaté;Futebol;Ginástica', 'Complexo desportivo', NULL,
       38.6208017, -9.1849779, 'OPENSTREETMAP', 'OSM:NODE:9042106716',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9042106716')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CRAQS'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6208017)) <= 0.00070
               AND ABS(v.longitude - (-9.1849779)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:273356396 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6742394, -9.1541912, 'OPENSTREETMAP', 'OSM:WAY:273356396',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:273356396')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6742394)) <= 0.00070
               AND ABS(v.longitude - (-9.1541912)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6428197182 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Feel Good Fitness Center', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6216134, -9.1994908, 'OPENSTREETMAP', 'OSM:NODE:6428197182',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6428197182')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Feel Good Fitness Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6216134)) <= 0.00070
               AND ABS(v.longitude - (-9.1994908)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6427837754 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Filipe Russo- Personal Training', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6556144, -9.1750505, 'OPENSTREETMAP', 'OSM:NODE:6427837754',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6427837754')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Filipe Russo- Personal Training'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6556144)) <= 0.00070
               AND ABS(v.longitude - (-9.1750505)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6427837757 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'FitCamp', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6556262, -9.1755432, 'OPENSTREETMAP', 'OSM:NODE:6427837757',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6427837757')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('FitCamp'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6556262)) <= 0.00070
               AND ABS(v.longitude - (-9.1755432)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6428882828 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fit Unique Studio', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6578393, -9.1529102, 'OPENSTREETMAP', 'OSM:NODE:6428882828',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6428882828')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fit Unique Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6578393)) <= 0.00070
               AND ABS(v.longitude - (-9.1529102)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6441703394 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gym4Fit', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6842036, -9.1497118, 'OPENSTREETMAP', 'OSM:NODE:6441703394',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6441703394')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gym4Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6842036)) <= 0.00070
               AND ABS(v.longitude - (-9.1497118)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6439711550 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Jmg Academy', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6733034, -9.1671060, 'OPENSTREETMAP', 'OSM:NODE:6439711550',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6439711550')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Jmg Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6733034)) <= 0.00070
               AND ABS(v.longitude - (-9.1671060)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6433890723 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'JUDO', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6506237, -9.1595831, 'OPENSTREETMAP', 'OSM:NODE:6433890723',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6433890723')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('JUDO'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6506237)) <= 0.00070
               AND ABS(v.longitude - (-9.1595831)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3995621321 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Katavento.pt', 'Almada', 'Setúbal', NULL,
       'Surf', 'Complexo desportivo', NULL,
       38.5957763, -9.2072225, 'OPENSTREETMAP', 'OSM:NODE:3995621321',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3995621321')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Katavento.pt'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5957763)) <= 0.00070
               AND ABS(v.longitude - (-9.2072225)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:238142509 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PGA Aroeira 1 Golf Course', 'Almada', 'Setúbal', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.5706931, -9.1786406, 'OPENSTREETMAP', 'OSM:WAY:238142509',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238142509')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PGA Aroeira 1 Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5706931)) <= 0.00070
               AND ABS(v.longitude - (-9.1786406)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6441622812 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Slow Yoga Studio', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6852479, -9.1498984, 'OPENSTREETMAP', 'OSM:NODE:6441622812',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6441622812')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Slow Yoga Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6852479)) <= 0.00070
               AND ABS(v.longitude - (-9.1498984)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237361273 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic Almada', 'Almada', 'Setúbal', NULL,
       'Ginásio;Natação', 'Ginásio', NULL,
       38.6708237, -9.1703185, 'OPENSTREETMAP', 'OSM:WAY:237361273',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237361273')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic Almada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6708237)) <= 0.00070
               AND ABS(v.longitude - (-9.1703185)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:358153286 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Light Almada', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6530391, -9.1679335, 'OPENSTREETMAP', 'OSM:WAY:358153286',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:358153286')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Light Almada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6530391)) <= 0.00070
               AND ABS(v.longitude - (-9.1679335)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6456086689 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sun Fitness', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6122987, -9.1900056, 'OPENSTREETMAP', 'OSM:NODE:6456086689',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6456086689')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sun Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6122987)) <= 0.00070
               AND ABS(v.longitude - (-9.1900056)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6428443605 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sunset Bodybuilding & Fitness', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6429934, -9.2322152, 'OPENSTREETMAP', 'OSM:NODE:6428443605',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6428443605')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sunset Bodybuilding & Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6429934)) <= 0.00070
               AND ABS(v.longitude - (-9.2322152)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:255940907 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Time To Fitness', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6747981, -9.1589898, 'OPENSTREETMAP', 'OSM:WAY:255940907',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:255940907')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Time To Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6747981)) <= 0.00070
               AND ABS(v.longitude - (-9.1589898)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5989525462 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Light', 'Barreiro', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5829109, -9.0427888, 'OPENSTREETMAP', 'OSM:NODE:5989525462',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5989525462')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Light'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5829109)) <= 0.00070
               AND ABS(v.longitude - (-9.0427888)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5079933889 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '2BFit Club', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7072880, -8.9754781, 'OPENSTREETMAP', 'OSM:NODE:5079933889',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5079933889')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('2BFit Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7072880)) <= 0.00070
               AND ABS(v.longitude - (-8.9754781)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5050786324 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bodylimits', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6953816, -8.9445299, 'OPENSTREETMAP', 'OSM:NODE:5050786324',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5050786324')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bodylimits'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6953816)) <= 0.00070
               AND ABS(v.longitude - (-8.9445299)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:101883940 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Liberdade', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7134560, -8.9677054, 'OPENSTREETMAP', 'OSM:WAY:101883940',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:101883940')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Liberdade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7134560)) <= 0.00070
               AND ABS(v.longitude - (-8.9677054)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5060772887 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crossfit', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7069072, -9.0015092, 'OPENSTREETMAP', 'OSM:NODE:5060772887',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5060772887')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crossfit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7069072)) <= 0.00070
               AND ABS(v.longitude - (-9.0015092)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12531470116 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crossfit Tejo', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6977175, -8.9507487, 'OPENSTREETMAP', 'OSM:NODE:12531470116',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12531470116')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crossfit Tejo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6977175)) <= 0.00070
               AND ABS(v.longitude - (-8.9507487)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5028417880 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6954946, -8.9434498, 'OPENSTREETMAP', 'OSM:NODE:5028417880',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5028417880')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6954946)) <= 0.00070
               AND ABS(v.longitude - (-8.9434498)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9384906117 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP Montijo', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7019469, -8.9473807, 'OPENSTREETMAP', 'OSM:NODE:9384906117',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9384906117')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP Montijo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7019469)) <= 0.00070
               AND ABS(v.longitude - (-8.9473807)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1088955984 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Montijo', 'Montijo', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.7099493, -8.9858726, 'OPENSTREETMAP', 'OSM:WAY:1088955984',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1088955984')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Montijo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7099493)) <= 0.00070
               AND ABS(v.longitude - (-8.9858726)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:88117624 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Montijo', 'Montijo', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7098137, -8.9861070, 'OPENSTREETMAP', 'OSM:WAY:88117624',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:88117624')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Montijo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7098137)) <= 0.00070
               AND ABS(v.longitude - (-8.9861070)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5060772886 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PumpAddicted - Elite Coaching', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7002633, -9.0063070, 'OPENSTREETMAP', 'OSM:NODE:5060772886',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5060772886')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PumpAddicted - Elite Coaching'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7002633)) <= 0.00070
               AND ABS(v.longitude - (-9.0063070)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5091531665 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga Clube Om', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7088232, -8.9533657, 'OPENSTREETMAP', 'OSM:NODE:5091531665',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5091531665')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga Clube Om'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7088232)) <= 0.00070
               AND ABS(v.longitude - (-8.9533657)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5041958991 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga na Villa', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7072376, -8.9788758, 'OPENSTREETMAP', 'OSM:NODE:5041958991',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5041958991')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga na Villa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7072376)) <= 0.00070
               AND ABS(v.longitude - (-8.9788758)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:116515588 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Golfe do Montado', 'Palmela', 'Setúbal', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.5678342, -8.8072794, 'OPENSTREETMAP', 'OSM:WAY:116515588',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:116515588')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Golfe do Montado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5678342)) <= 0.00070
               AND ABS(v.longitude - (-8.8072794)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9983487467 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Body Makeover', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6248298, -9.1239241, 'OPENSTREETMAP', 'OSM:NODE:9983487467',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9983487467')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Body Makeover'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6248298)) <= 0.00070
               AND ABS(v.longitude - (-9.1239241)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10247978871 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6123611, -9.1030093, 'OPENSTREETMAP', 'OSM:NODE:10247978871',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10247978871')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6123611)) <= 0.00070
               AND ABS(v.longitude - (-9.1030093)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186632783 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas da Amora', 'Seixal', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6250838, -9.1191488, 'OPENSTREETMAP', 'OSM:WAY:186632783',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186632783')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas da Amora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6250838)) <= 0.00070
               AND ABS(v.longitude - (-9.1191488)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1192696120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Fernão Ferro', 'Seixal', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.5583523, -9.0937869, 'OPENSTREETMAP', 'OSM:WAY:1192696120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1192696120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Fernão Ferro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5583523)) <= 0.00070
               AND ABS(v.longitude - (-9.0937869)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:788931264 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Light', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6251836, -9.1258337, 'OPENSTREETMAP', 'OSM:WAY:788931264',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:788931264')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Light'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6251836)) <= 0.00070
               AND ABS(v.longitude - (-9.1258337)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166477776 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'FitnessUP', 'Sesimbra', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5750698, -9.0375080, 'OPENSTREETMAP', 'OSM:WAY:166477776',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166477776')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('FitnessUP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5750698)) <= 0.00070
               AND ABS(v.longitude - (-9.0375080)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:536120942 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Sesimbra', 'Setúbal', NULL,
       'Squash;Natação', 'Ginásio', NULL,
       38.5739098, -9.0454528, 'OPENSTREETMAP', 'OSM:WAY:536120942',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:536120942')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5739098)) <= 0.00070
               AND ABS(v.longitude - (-9.0454528)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:785477069 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Total Combat', 'Sesimbra', 'Setúbal', NULL,
       'Boxe', 'Ginásio', NULL,
       38.5676411, -9.0471408, 'OPENSTREETMAP', 'OSM:WAY:785477069',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:785477069')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sesimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Total Combat'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5676411)) <= 0.00070
               AND ABS(v.longitude - (-9.0471408)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1016588834 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Arrábida Padel', 'Setúbal', 'Setúbal', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.5214912, -9.0171235, 'OPENSTREETMAP', 'OSM:WAY:1016588834',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1016588834')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Arrábida Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5214912)) <= 0.00070
               AND ABS(v.longitude - (-9.0171235)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105957362 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be-Fit', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5489527, -8.8843638, 'OPENSTREETMAP', 'OSM:WAY:105957362',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105957362')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be-Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5489527)) <= 0.00070
               AND ABS(v.longitude - (-8.8843638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119083764 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP Setúbal', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5343099, -8.8652596, 'OPENSTREETMAP', 'OSM:WAY:119083764',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119083764')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP Setúbal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5343099)) <= 0.00070
               AND ABS(v.longitude - (-8.8652596)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1082833423 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'In Wall Climbing Center', 'Setúbal', 'Setúbal', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.5332980, -8.8728988, 'OPENSTREETMAP', 'OSM:WAY:1082833423',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1082833423')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('In Wall Climbing Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5332980)) <= 0.00070
               AND ABS(v.longitude - (-8.8728988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:948857830 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Supera Setúbal', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5297309, -8.8748286, 'OPENSTREETMAP', 'OSM:WAY:948857830',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:948857830')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Supera Setúbal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5297309)) <= 0.00070
               AND ABS(v.longitude - (-8.8748286)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12553639678 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Time To Fitness', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5311495, -8.8866610, 'OPENSTREETMAP', 'OSM:NODE:12553639678',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12553639678')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Time To Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5311495)) <= 0.00070
               AND ABS(v.longitude - (-8.8866610)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10243293265 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Setúbal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5373465, -8.8786430, 'OPENSTREETMAP', 'OSM:NODE:10243293265',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10243293265')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Setúbal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5373465)) <= 0.00070
               AND ABS(v.longitude - (-8.8786430)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:264542899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Mar', 'Sines', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.8546936, -8.7905566, 'OPENSTREETMAP', 'OSM:WAY:264542899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:264542899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sines'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Mar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8546936)) <= 0.00070
               AND ABS(v.longitude - (-8.7905566)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:198183742 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Barreira Guedes', 'Alcácer do Sal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.2952422, -8.2161033, 'OPENSTREETMAP', 'OSM:WAY:198183742',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:198183742')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcácer do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Barreira Guedes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.2952422)) <= 0.00070
               AND ABS(v.longitude - (-8.2161033)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136004998 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'openair', 'Alcácer do Sal', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.3796046, -8.5161029, 'OPENSTREETMAP', 'OSM:WAY:136004998',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136004998')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcácer do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('openair'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3796046)) <= 0.00070
               AND ABS(v.longitude - (-8.5161029)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135801524 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park', 'Alcácer do Sal', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.3698125, -8.5101200, 'OPENSTREETMAP', 'OSM:WAY:135801524',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135801524')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcácer do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.3698125)) <= 0.00070
               AND ABS(v.longitude - (-8.5101200)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:200685833 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Cristiano Ronaldo', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.7303574, -8.8496194, 'OPENSTREETMAP', 'OSM:WAY:200685833',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:200685833')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Cristiano Ronaldo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7303574)) <= 0.00070
               AND ABS(v.longitude - (-8.8496194)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135459400 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Cinco Violinos', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7307814, -8.8501257, 'OPENSTREETMAP', 'OSM:WAY:135459400',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135459400')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Cinco Violinos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7307814)) <= 0.00070
               AND ABS(v.longitude - (-8.8501257)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1409744349 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol informal do Gaio', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7175745, -9.0080782, 'OPENSTREETMAP', 'OSM:WAY:1409744349',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1409744349')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol informal do Gaio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7175745)) <= 0.00070
               AND ABS(v.longitude - (-9.0080782)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:205278511 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Hilário da Conceição', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7306494, -8.8509031, 'OPENSTREETMAP', 'OSM:WAY:205278511',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:205278511')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Hilário da Conceição'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7306494)) <= 0.00070
               AND ABS(v.longitude - (-8.8509031)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135459404 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Luis Figo', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7314643, -8.8509825, 'OPENSTREETMAP', 'OSM:WAY:135459404',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135459404')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Luis Figo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7314643)) <= 0.00070
               AND ABS(v.longitude - (-8.8509825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135459407 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Manuel Fernandes', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7306593, -8.8519142, 'OPENSTREETMAP', 'OSM:WAY:135459407',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135459407')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Manuel Fernandes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7306593)) <= 0.00070
               AND ABS(v.longitude - (-8.8519142)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1093994218 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Paulo Futre', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7314794, -8.8527663, 'OPENSTREETMAP', 'OSM:WAY:1093994218',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1093994218')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Paulo Futre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7314794)) <= 0.00070
               AND ABS(v.longitude - (-8.8527663)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222519782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Vitor Damas', 'Alcochete', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7303084, -8.8524838, 'OPENSTREETMAP', 'OSM:WAY:222519782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222519782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Vitor Damas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7303084)) <= 0.00070
               AND ABS(v.longitude - (-8.8524838)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:304913834 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Alcochete', 'Alcochete', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7468778, -8.9660820, 'OPENSTREETMAP', 'OSM:WAY:304913834',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:304913834')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcochete'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Alcochete'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7468778)) <= 0.00070
               AND ABS(v.longitude - (-8.9660820)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:144341322 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia de Ténis da Charneca', 'Almada', 'Setúbal', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.6090172, -9.1891529, 'OPENSTREETMAP', 'OSM:WAY:144341322',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:144341322')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia de Ténis da Charneca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6090172)) <= 0.00070
               AND ABS(v.longitude - (-9.1891529)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1275438998 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Almada Padel Academy', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6613332, -9.1583064, 'OPENSTREETMAP', 'OSM:WAY:1275438998',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1275438998')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Almada Padel Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6613332)) <= 0.00070
               AND ABS(v.longitude - (-9.1583064)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:666847506 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Aquafitness Health Club - Quinta do Texugo', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6244625, -9.2027082, 'OPENSTREETMAP', 'OSM:WAY:666847506',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:666847506')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Aquafitness Health Club - Quinta do Texugo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6244625)) <= 0.00070
               AND ABS(v.longitude - (-9.2027082)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13136879148 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BHÓUT Club', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6797443, -9.1574836, 'OPENSTREETMAP', 'OSM:NODE:13136879148',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13136879148')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BHÓUT Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6797443)) <= 0.00070
               AND ABS(v.longitude - (-9.1574836)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152851670 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Clube Desportivo da Cova da Piedade', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6658207, -9.1623256, 'OPENSTREETMAP', 'OSM:WAY:152851670',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152851670')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Clube Desportivo da Cova da Piedade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6658207)) <= 0.00070
               AND ABS(v.longitude - (-9.1623256)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:974444682 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Vale Figueira', 'Almada', 'Setúbal', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.6407353, -9.1780388, 'OPENSTREETMAP', 'OSM:WAY:974444682',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:974444682')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Vale Figueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6407353)) <= 0.00070
               AND ABS(v.longitude - (-9.1780388)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:938352256 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Pelado', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6368406, -9.1897684, 'OPENSTREETMAP', 'OSM:WAY:938352256',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:938352256')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Pelado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6368406)) <= 0.00070
               AND ABS(v.longitude - (-9.1897684)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:139453956 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Pepita', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6630679, -9.2438199, 'OPENSTREETMAP', 'OSM:WAY:139453956',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:139453956')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Pepita'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6630679)) <= 0.00070
               AND ABS(v.longitude - (-9.2438199)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152049355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Ténis 1 Aroeira', 'Almada', 'Setúbal', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.5739372, -9.1693891, 'OPENSTREETMAP', 'OSM:WAY:152049355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152049355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Ténis 1 Aroeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5739372)) <= 0.00070
               AND ABS(v.longitude - (-9.1693891)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152049358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Ténis 2 Aroeira', 'Almada', 'Setúbal', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.5739389, -9.1691672, 'OPENSTREETMAP', 'OSM:WAY:152049358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152049358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Ténis 2 Aroeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5739389)) <= 0.00070
               AND ABS(v.longitude - (-9.1691672)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152049360 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Ténis 3 Aroeira', 'Almada', 'Setúbal', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.5735909, -9.1693809, 'OPENSTREETMAP', 'OSM:WAY:152049360',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152049360')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Ténis 3 Aroeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5735909)) <= 0.00070
               AND ABS(v.longitude - (-9.1693809)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152049368 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Ténis 4 Aroeira', 'Almada', 'Setúbal', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.5735920, -9.1691623, 'OPENSTREETMAP', 'OSM:WAY:152049368',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152049368')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Ténis 4 Aroeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5735920)) <= 0.00070
               AND ABS(v.longitude - (-9.1691623)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6416622067 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Ténis', 'Almada', 'Setúbal', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.6091055, -9.1892379, 'OPENSTREETMAP', 'OSM:NODE:6416622067',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6416622067')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6091055)) <= 0.00070
               AND ABS(v.longitude - (-9.1892379)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:144341317 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Charneca de Caparica Futebol Clube (Canários)', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6231884, -9.2016673, 'OPENSTREETMAP', 'OSM:WAY:144341317',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:144341317')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Charneca de Caparica Futebol Clube (Canários)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6231884)) <= 0.00070
               AND ABS(v.longitude - (-9.2016673)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153920489 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Circuito de Manutenção FCT', 'Almada', 'Setúbal', NULL,
       'Ginásio;Corrida', 'Pista desportiva', NULL,
       38.6680268, -9.2100529, 'OPENSTREETMAP', 'OSM:WAY:153920489',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153920489')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Circuito de Manutenção FCT'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6680268)) <= 0.00070
               AND ABS(v.longitude - (-9.2100529)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:179122248 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo Cova da Piedade', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6652816, -9.1632776, 'OPENSTREETMAP', 'OSM:WAY:179122248',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:179122248')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo Cova da Piedade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6652816)) <= 0.00070
               AND ABS(v.longitude - (-9.1632776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153606943 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Piscinas da Sobreda', 'Almada', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6371411, -9.1808744, 'OPENSTREETMAP', 'OSM:WAY:153606943',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153606943')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Piscinas da Sobreda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6371411)) <= 0.00070
               AND ABS(v.longitude - (-9.1808744)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:445317253 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Piscinas na Caparica', 'Almada', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.6698236, -9.1911321, 'OPENSTREETMAP', 'OSM:WAY:445317253',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:445317253')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Piscinas na Caparica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6698236)) <= 0.00070
               AND ABS(v.longitude - (-9.1911321)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152088208 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Court de Basket Herdade da Aroeira', 'Almada', 'Setúbal', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.5823407, -9.1702682, 'OPENSTREETMAP', 'OSM:WAY:152088208',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152088208')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Court de Basket Herdade da Aroeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5823407)) <= 0.00070
               AND ABS(v.longitude - (-9.1702682)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11433153246 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6785087, -9.1559361, 'OPENSTREETMAP', 'OSM:NODE:11433153246',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11433153246')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6785087)) <= 0.00070
               AND ABS(v.longitude - (-9.1559361)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1968076783 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Radical', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6383292, -9.2320144, 'OPENSTREETMAP', 'OSM:NODE:1968076783',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1968076783')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6383292)) <= 0.00070
               AND ABS(v.longitude - (-9.2320144)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:376386524 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Golfe Aldeia dos Capuchos', 'Almada', 'Setúbal', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.6494561, -9.2256515, 'OPENSTREETMAP', 'OSM:WAY:376386524',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:376386524')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Golfe Aldeia dos Capuchos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6494561)) <= 0.00070
               AND ABS(v.longitude - (-9.2256515)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:188747764 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo dos Pescadores da Costa de Caparica', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Estádio', NULL,
       38.6377401, -9.2305862, 'OPENSTREETMAP', 'OSM:WAY:188747764',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:188747764')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo dos Pescadores da Costa de Caparica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6377401)) <= 0.00070
               AND ABS(v.longitude - (-9.2305862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10271803536 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'JAF Club', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6788066, -9.1577782, 'OPENSTREETMAP', 'OSM:NODE:10271803536',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10271803536')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('JAF Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6788066)) <= 0.00070
               AND ABS(v.longitude - (-9.1577782)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11012730829 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'JAF Club', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6798875, -9.1576252, 'OPENSTREETMAP', 'OSM:NODE:11012730829',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11012730829')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('JAF Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6798875)) <= 0.00070
               AND ABS(v.longitude - (-9.1576252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13392452001 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ocean Yoga', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6397970, -9.2287190, 'OPENSTREETMAP', 'OSM:NODE:13392452001',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13392452001')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ocean Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6397970)) <= 0.00070
               AND ABS(v.longitude - (-9.2287190)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:314842755 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo Ginásio Clube do Sul', 'Almada', 'Setúbal', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       38.6731115, -9.1562135, 'OPENSTREETMAP', 'OSM:WAY:314842755',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:314842755')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Desportivo Ginásio Clube do Sul'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6731115)) <= 0.00070
               AND ABS(v.longitude - (-9.1562135)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:274779771 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão dos Desportos', 'Almada', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.6569786, -9.1598052, 'OPENSTREETMAP', 'OSM:WAY:274779771',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:274779771')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão dos Desportos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6569786)) <= 0.00070
               AND ABS(v.longitude - (-9.1598052)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:256728659 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina 25 de Abril - SFUAP', 'Almada', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6695414, -9.1580065, 'OPENSTREETMAP', 'OSM:WAY:256728659',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:256728659')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina 25 de Abril - SFUAP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6695414)) <= 0.00070
               AND ABS(v.longitude - (-9.1580065)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:112316791 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Inatel', 'Almada', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6566244, -9.2403452, 'OPENSTREETMAP', 'OSM:WAY:112316791',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:112316791')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Inatel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6566244)) <= 0.00070
               AND ABS(v.longitude - (-9.2403452)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107125322 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Clube Lisnave', 'Almada', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6774377, -9.1518161, 'OPENSTREETMAP', 'OSM:WAY:107125322',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107125322')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Clube Lisnave'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6774377)) <= 0.00070
               AND ABS(v.longitude - (-9.1518161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:971780120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Tropical da Herdade da Aroeira', 'Almada', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.5731770, -9.1727413, 'OPENSTREETMAP', 'OSM:WAY:971780120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:971780120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Tropical da Herdade da Aroeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5731770)) <= 0.00070
               AND ABS(v.longitude - (-9.1727413)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440936040 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Municipal de Atletismo Alberto Chaíça', 'Almada', 'Setúbal', NULL,
       'Corrida', 'Pista desportiva', NULL,
       38.6456940, -9.1755393, 'OPENSTREETMAP', 'OSM:WAY:440936040',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440936040')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Municipal de Atletismo Alberto Chaíça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6456940)) <= 0.00070
               AND ABS(v.longitude - (-9.1755393)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107126195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Quinta da Alegria', 'Almada', 'Setúbal', NULL,
       'Basquetebol;Futebol', 'Campo desportivo', NULL,
       38.6801553, -9.1506830, 'OPENSTREETMAP', 'OSM:WAY:107126195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107126195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Quinta da Alegria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6801553)) <= 0.00070
               AND ABS(v.longitude - (-9.1506830)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:321699893 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Romeira', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6690090, -9.1530132, 'OPENSTREETMAP', 'OSM:WAY:321699893',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:321699893')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Romeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6690090)) <= 0.00070
               AND ABS(v.longitude - (-9.1530132)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:321285608 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Rua Dom Dinis', 'Almada', 'Setúbal', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.6645872, -9.1583339, 'OPENSTREETMAP', 'OSM:WAY:321285608',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:321285608')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Rua Dom Dinis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6645872)) <= 0.00070
               AND ABS(v.longitude - (-9.1583339)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11404295931 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PT do Bairro', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6795984, -9.1570095, 'OPENSTREETMAP', 'OSM:NODE:11404295931',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11404295931')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PT do Bairro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6795984)) <= 0.00070
               AND ABS(v.longitude - (-9.1570095)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106624417 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quintal Desportivo de Cacilhas', 'Almada', 'Setúbal', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.6837429, -9.1493136, 'OPENSTREETMAP', 'OSM:WAY:106624417',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106624417')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quintal Desportivo de Cacilhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6837429)) <= 0.00070
               AND ABS(v.longitude - (-9.1493136)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:321124909 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue das Barrocas', 'Almada', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6648320, -9.1573889, 'OPENSTREETMAP', 'OSM:WAY:321124909',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:321124909')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue das Barrocas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6648320)) <= 0.00070
               AND ABS(v.longitude - (-9.1573889)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107135064 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue do Pombal', 'Almada', 'Setúbal', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       38.6743638, -9.1614969, 'OPENSTREETMAP', 'OSM:WAY:107135064',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107135064')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue do Pombal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6743638)) <= 0.00070
               AND ABS(v.longitude - (-9.1614969)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:828596420 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Setor de Lançamentos', 'Almada', 'Setúbal', NULL,
       'Corrida', 'Pista desportiva', NULL,
       38.6447864, -9.1731617, 'OPENSTREETMAP', 'OSM:WAY:828596420',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:828596420')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Setor de Lançamentos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6447864)) <= 0.00070
               AND ABS(v.longitude - (-9.1731617)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:317970808 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Almada', 'Almada', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.6720223, -9.1653656, 'OPENSTREETMAP', 'OSM:WAY:317970808',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:317970808')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Almada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6720223)) <= 0.00070
               AND ABS(v.longitude - (-9.1653656)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:973168637 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark da Sobreda', 'Almada', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.6362720, -9.1819536, 'OPENSTREETMAP', 'OSM:WAY:973168637',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:973168637')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark da Sobreda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6362720)) <= 0.00070
               AND ABS(v.longitude - (-9.1819536)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6430381383 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'yoga', 'Almada', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6493823, -9.1712235, 'OPENSTREETMAP', 'OSM:NODE:6430381383',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6430381383')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6493823)) <= 0.00070
               AND ABS(v.longitude - (-9.1712235)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11905168127 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Boom Training Studio', 'Barreiro', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6451685, -9.0555790, 'OPENSTREETMAP', 'OSM:NODE:11905168127',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11905168127')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Boom Training Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6451685)) <= 0.00070
               AND ABS(v.longitude - (-9.0555790)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:140778801 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Verderena', 'Barreiro', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6511679, -9.0654092, 'OPENSTREETMAP', 'OSM:WAY:140778801',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:140778801')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Verderena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6511679)) <= 0.00070
               AND ABS(v.longitude - (-9.0654092)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:390606800 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Carreira de Tiro', 'Barreiro', 'Setúbal', NULL,
       'Tiro', 'Campo desportivo', NULL,
       38.6169265, -9.0509626, 'OPENSTREETMAP', 'OSM:WAY:390606800',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:390606800')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Carreira de Tiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6169265)) <= 0.00070
               AND ABS(v.longitude - (-9.0509626)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103890537 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Alfredo da Silva', 'Barreiro', 'Setúbal', NULL,
       'Futebol', 'Estádio', NULL,
       38.6578289, -9.0538450, 'OPENSTREETMAP', 'OSM:WAY:103890537',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103890537')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Alfredo da Silva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6578289)) <= 0.00070
               AND ABS(v.longitude - (-9.0538450)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6330034821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Barreiro', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6618816, -9.0730776, 'OPENSTREETMAP', 'OSM:NODE:6330034821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6330034821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6618816)) <= 0.00070
               AND ABS(v.longitude - (-9.0730776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:110254878 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Galitos Futebol Clube', 'Barreiro', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6436041, -9.0497844, 'OPENSTREETMAP', 'OSM:WAY:110254878',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:110254878')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Galitos Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6436041)) <= 0.00070
               AND ABS(v.longitude - (-9.0497844)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6257609175 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Jaf', 'Barreiro', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6560471, -9.0624664, 'OPENSTREETMAP', 'OSM:NODE:6257609175',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6257609175')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Jaf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6560471)) <= 0.00070
               AND ABS(v.longitude - (-9.0624664)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1095057134 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'My Place Barreiro', 'Barreiro', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6538039, -9.0679792, 'OPENSTREETMAP', 'OSM:WAY:1095057134',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1095057134')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('My Place Barreiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6538039)) <= 0.00070
               AND ABS(v.longitude - (-9.0679792)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1540990486 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Barreiro', 'Barreiro', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.6633796, -9.0829994, 'OPENSTREETMAP', 'OSM:WAY:1540990486',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1540990486')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Barreiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6633796)) <= 0.00070
               AND ABS(v.longitude - (-9.0829994)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1106736238 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Lavradio', 'Barreiro', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.6644175, -9.0531402, 'OPENSTREETMAP', 'OSM:WAY:1106736238',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1106736238')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Lavradio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6644175)) <= 0.00070
               AND ABS(v.longitude - (-9.0531402)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1096162451 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polivalente do Barreiro - Bairro 6', 'Barreiro', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.6597558, -9.0799191, 'OPENSTREETMAP', 'OSM:WAY:1096162451',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1096162451')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Barreiro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polivalente do Barreiro - Bairro 6'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6597558)) <= 0.00070
               AND ABS(v.longitude - (-9.0799191)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:729212343 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Azinheira dos Barros', 'Grândola', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0677276, -8.4226179, 'OPENSTREETMAP', 'OSM:WAY:729212343',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:729212343')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Grândola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Azinheira dos Barros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0677276)) <= 0.00070
               AND ABS(v.longitude - (-8.4226179)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:248138141 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Castelo', 'Grândola', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1524713, -8.7311809, 'OPENSTREETMAP', 'OSM:WAY:248138141',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:248138141')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Grândola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Castelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1524713)) <= 0.00070
               AND ABS(v.longitude - (-8.7311809)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157902102 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Estrela do Mar', 'Grândola', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.1224480, -8.7209909, 'OPENSTREETMAP', 'OSM:WAY:157902102',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157902102')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Grândola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Estrela do Mar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1224480)) <= 0.00070
               AND ABS(v.longitude - (-8.7209909)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:135594159 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal José Afonso', 'Grândola', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.1734394, -8.5627351, 'OPENSTREETMAP', 'OSM:WAY:135594159',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:135594159')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Grândola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Municipal José Afonso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1734394)) <= 0.00070
               AND ABS(v.longitude - (-8.5627351)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1148778872 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CostaTerra Golf', 'Grândola', 'Setúbal', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.1922115, -8.7660837, 'OPENSTREETMAP', 'OSM:WAY:1148778872',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1148778872')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Grândola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CostaTerra Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.1922115)) <= 0.00070
               AND ABS(v.longitude - (-8.7660837)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1530769917 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spot Troia - Boutique Suites', 'Grândola', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.4636239, -8.8653129, 'OPENSTREETMAP', 'OSM:WAY:1530769917',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1530769917')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Grândola'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spot Troia - Boutique Suites'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.4636239)) <= 0.00070
               AND ABS(v.longitude - (-8.8653129)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181929143 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '1º de Maio Futebol Clube Sarilhense', 'Moita', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6799517, -8.9795071, 'OPENSTREETMAP', 'OSM:WAY:181929143',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181929143')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('1º de Maio Futebol Clube Sarilhense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6799517)) <= 0.00070
               AND ABS(v.longitude - (-8.9795071)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181671830 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Madalena', 'Moita', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6778030, -9.0089744, 'OPENSTREETMAP', 'OSM:WAY:181671830',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181671830')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Madalena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6778030)) <= 0.00070
               AND ABS(v.longitude - (-9.0089744)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:338461447 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol C.R.I.', 'Moita', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6474412, -9.0237145, 'OPENSTREETMAP', 'OSM:WAY:338461447',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:338461447')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol C.R.I.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6474412)) <= 0.00070
               AND ABS(v.longitude - (-9.0237145)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:313809876 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Sporting Clube Vinhense', 'Moita', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6532485, -9.0360219, 'OPENSTREETMAP', 'OSM:WAY:313809876',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:313809876')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Sporting Clube Vinhense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6532485)) <= 0.00070
               AND ABS(v.longitude - (-9.0360219)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108528819 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de Futebol do Vale da Amoreira', 'Moita', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6442596, -9.0395561, 'OPENSTREETMAP', 'OSM:WAY:108528819',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108528819')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal de Futebol do Vale da Amoreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6442596)) <= 0.00070
               AND ABS(v.longitude - (-9.0395561)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1377211690 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo os Rolos', 'Moita', 'Setúbal', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       38.6214729, -9.0104108, 'OPENSTREETMAP', 'OSM:NODE:1377211690',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1377211690')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo os Rolos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6214729)) <= 0.00070
               AND ABS(v.longitude - (-9.0104108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:340030240 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo os Rolos', 'Moita', 'Setúbal', NULL,
       'Tiro', 'Campo desportivo', NULL,
       38.6218750, -9.0097131, 'OPENSTREETMAP', 'OSM:WAY:340030240',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:340030240')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo os Rolos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6218750)) <= 0.00070
               AND ABS(v.longitude - (-9.0097131)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1286129697 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Moita', 'Moita', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6477651, -8.9851127, 'OPENSTREETMAP', 'OSM:WAY:1286129697',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1286129697')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Moita'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6477651)) <= 0.00070
               AND ABS(v.longitude - (-8.9851127)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1095178483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Alhos Vedros', 'Moita', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.6480079, -9.0271451, 'OPENSTREETMAP', 'OSM:WAY:1095178483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1095178483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moita'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Alhos Vedros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6480079)) <= 0.00070
               AND ABS(v.longitude - (-9.0271451)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1493996836 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Aatma Yoga - Atelier Montijo', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7113761, -8.9513872, 'OPENSTREETMAP', 'OSM:WAY:1493996836',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1493996836')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Aatma Yoga - Atelier Montijo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7113761)) <= 0.00070
               AND ABS(v.longitude - (-8.9513872)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9531226837 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BE-FIT', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6960780, -8.9454080, 'OPENSTREETMAP', 'OSM:NODE:9531226837',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9531226837')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BE-FIT'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6960780)) <= 0.00070
               AND ABS(v.longitude - (-8.9454080)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:932102629 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo António Augusto Cota Marques', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6804099, -8.6147160, 'OPENSTREETMAP', 'OSM:WAY:932102629',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:932102629')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo António Augusto Cota Marques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6804099)) <= 0.00070
               AND ABS(v.longitude - (-8.6147160)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104511132 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Bairro das Areias', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7119503, -8.9484324, 'OPENSTREETMAP', 'OSM:WAY:104511132',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104511132')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Bairro das Areias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7119503)) <= 0.00070
               AND ABS(v.longitude - (-8.9484324)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:304916647 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Águias Negras Futebol Clube', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6787253, -8.9295407, 'OPENSTREETMAP', 'OSM:WAY:304916647',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:304916647')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Águias Negras Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6787253)) <= 0.00070
               AND ABS(v.longitude - (-8.9295407)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:304917660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Estrela Futebol Clube Afonsoeirense', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6987418, -8.9511705, 'OPENSTREETMAP', 'OSM:WAY:304917660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:304917660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Estrela Futebol Clube Afonsoeirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6987418)) <= 0.00070
               AND ABS(v.longitude - (-8.9511705)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:435396869 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do União Futebol Clube Jardiense', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6664474, -8.9257129, 'OPENSTREETMAP', 'OSM:WAY:435396869',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:435396869')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do União Futebol Clube Jardiense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6664474)) <= 0.00070
               AND ABS(v.longitude - (-8.9257129)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:878974487 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Corpus Palace', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7061673, -8.9775965, 'OPENSTREETMAP', 'OSM:WAY:878974487',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:878974487')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Corpus Palace'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7061673)) <= 0.00070
               AND ABS(v.longitude - (-8.9775965)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:304165808 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal do Esteval', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7125861, -8.9643811, 'OPENSTREETMAP', 'OSM:WAY:304165808',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:304165808')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal do Esteval'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7125861)) <= 0.00070
               AND ABS(v.longitude - (-8.9643811)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12879359816 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Flow Studio', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7048518, -8.9615098, 'OPENSTREETMAP', 'OSM:NODE:12879359816',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12879359816')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Flow Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7048518)) <= 0.00070
               AND ABS(v.longitude - (-8.9615098)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13688797946 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Physical', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6935740, -8.9476767, 'OPENSTREETMAP', 'OSM:NODE:13688797946',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13688797946')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Physical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6935740)) <= 0.00070
               AND ABS(v.longitude - (-8.9476767)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:578086636 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Pegões-Gare', 'Montijo', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.6669335, -8.6191989, 'OPENSTREETMAP', 'OSM:WAY:578086636',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:578086636')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Pegões-Gare'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6669335)) <= 0.00070
               AND ABS(v.longitude - (-8.6191989)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:308576729 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Parque do Montijo', 'Montijo', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7092682, -8.9784311, 'OPENSTREETMAP', 'OSM:WAY:308576729',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:308576729')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Parque do Montijo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7092682)) <= 0.00070
               AND ABS(v.longitude - (-8.9784311)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1361997489 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sirius Park', 'Montijo', 'Setúbal', NULL,
       'Natação;Escalada', 'Complexo desportivo', NULL,
       38.6842928, -8.7509539, 'OPENSTREETMAP', 'OSM:WAY:1361997489',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1361997489')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sirius Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6842928)) <= 0.00070
               AND ABS(v.longitude - (-8.7509539)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12876820947 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Wellness Solutions', 'Montijo', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7039464, -8.9631576, 'OPENSTREETMAP', 'OSM:NODE:12876820947',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12876820947')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montijo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Wellness Solutions'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7039464)) <= 0.00070
               AND ABS(v.longitude - (-8.9631576)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1018384721 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo António Henrique de Matos', 'Palmela', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5567820, -8.9688071, 'OPENSTREETMAP', 'OSM:WAY:1018384721',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1018384721')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo António Henrique de Matos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5567820)) <= 0.00070
               AND ABS(v.longitude - (-8.9688071)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11691173604 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Padel da Quinta do Anjo', 'Palmela', 'Setúbal', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.5648700, -8.9458497, 'OPENSTREETMAP', 'OSM:NODE:11691173604',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11691173604')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Padel da Quinta do Anjo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5648700)) <= 0.00070
               AND ABS(v.longitude - (-8.9458497)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:292825022 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo e Recreativo Quinta Padre Nabeto', 'Palmela', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.5632986, -8.8782166, 'OPENSTREETMAP', 'OSM:WAY:292825022',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:292825022')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo e Recreativo Quinta Padre Nabeto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5632986)) <= 0.00070
               AND ABS(v.longitude - (-8.8782166)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2945866388 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Portais da Arrábida', 'Palmela', 'Setúbal', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.5646717, -8.9483673, 'OPENSTREETMAP', 'OSM:NODE:2945866388',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2945866388')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Portais da Arrábida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5646717)) <= 0.00070
               AND ABS(v.longitude - (-8.9483673)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8116862445 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crazy Boulder', 'Palmela', 'Setúbal', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.5704024, -8.9112096, 'OPENSTREETMAP', 'OSM:NODE:8116862445',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8116862445')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crazy Boulder'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5704024)) <= 0.00070
               AND ABS(v.longitude - (-8.9112096)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12875101226 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio R&M', 'Palmela', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5761338, -9.0242953, 'OPENSTREETMAP', 'OSM:NODE:12875101226',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12875101226')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio R&M'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5761338)) <= 0.00070
               AND ABS(v.longitude - (-9.0242953)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:388242809 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Palmelense Futebol Clube', 'Palmela', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5726944, -8.9126002, 'OPENSTREETMAP', 'OSM:WAY:388242809',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:388242809')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Palmelense Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5726944)) <= 0.00070
               AND ABS(v.longitude - (-8.9126002)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119968745 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Palmela', 'Palmela', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.5722059, -8.9009400, 'OPENSTREETMAP', 'OSM:WAY:119968745',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119968745')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Palmela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5722059)) <= 0.00070
               AND ABS(v.longitude - (-8.9009400)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:292825026 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Rua Principal', 'Palmela', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5573621, -8.8986222, 'OPENSTREETMAP', 'OSM:WAY:292825026',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:292825026')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Rua Principal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5573621)) <= 0.00070
               AND ABS(v.longitude - (-8.8986222)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:219683336 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Aires', 'Palmela', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5716748, -8.8856675, 'OPENSTREETMAP', 'OSM:WAY:219683336',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:219683336')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Aires'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5716748)) <= 0.00070
               AND ABS(v.longitude - (-8.8856675)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:288616812 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Palmela', 'Palmela', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.5716261, -8.9005818, 'OPENSTREETMAP', 'OSM:WAY:288616812',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:288616812')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Palmela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5716261)) <= 0.00070
               AND ABS(v.longitude - (-8.9005818)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14070325186 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Samadhi Corpo & Mente', 'Palmela', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.5762119, -8.9460297, 'OPENSTREETMAP', 'OSM:NODE:14070325186',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14070325186')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Palmela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Samadhi Corpo & Mente'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5762119)) <= 0.00070
               AND ABS(v.longitude - (-8.9460297)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5741075769 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Alpha Fitness', 'Santiago do Cacém', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.0632384, -8.7915573, 'OPENSTREETMAP', 'OSM:NODE:5741075769',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5741075769')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Alpha Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0632384)) <= 0.00070
               AND ABS(v.longitude - (-8.7915573)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155179682 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Boa Vontade', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.7973206, -8.6747171, 'OPENSTREETMAP', 'OSM:WAY:155179682',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155179682')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Boa Vontade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7973206)) <= 0.00070
               AND ABS(v.longitude - (-8.6747171)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1190436439 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Ermidas do Sado', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0104048, -8.4207044, 'OPENSTREETMAP', 'OSM:WAY:1190436439',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1190436439')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Ermidas do Sado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0104048)) <= 0.00070
               AND ABS(v.longitude - (-8.4207044)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1084726698 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Deodato Rocha', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0947868, -8.6680052, 'OPENSTREETMAP', 'OSM:WAY:1084726698',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1084726698')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Deodato Rocha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0947868)) <= 0.00070
               AND ABS(v.longitude - (-8.6680052)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1052735141 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vale de Água', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.9027085, -8.5865372, 'OPENSTREETMAP', 'OSM:WAY:1052735141',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1052735141')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vale de Água'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.9027085)) <= 0.00070
               AND ABS(v.longitude - (-8.5865372)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1154390456 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Juventude da Atalaia', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0589700, -8.7944565, 'OPENSTREETMAP', 'OSM:WAY:1154390456',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1154390456')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Juventude da Atalaia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0589700)) <= 0.00070
               AND ABS(v.longitude - (-8.7944565)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:382306950 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis da Courela Grande', 'Santiago do Cacém', 'Setúbal', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.0142622, -8.6941705, 'OPENSTREETMAP', 'OSM:WAY:382306950',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:382306950')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis da Courela Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0142622)) <= 0.00070
               AND ABS(v.longitude - (-8.6941705)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:733332874 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol da Galp', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0640394, -8.7743446, 'OPENSTREETMAP', 'OSM:WAY:733332874',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:733332874')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol da Galp'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0640394)) <= 0.00070
               AND ABS(v.longitude - (-8.7743446)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104429972 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de São Sebastião', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.0203981, -8.6913931, 'OPENSTREETMAP', 'OSM:WAY:104429972',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104429972')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal de São Sebastião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0203981)) <= 0.00070
               AND ABS(v.longitude - (-8.6913931)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222552768 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Serafim João', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.8675045, -8.7202486, 'OPENSTREETMAP', 'OSM:WAY:222552768',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222552768')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Serafim João'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.8675045)) <= 0.00070
               AND ABS(v.longitude - (-8.7202486)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5741075750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo da Repsol', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.0569072, -8.7780447, 'OPENSTREETMAP', 'OSM:NODE:5741075750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5741075750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo da Repsol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0569072)) <= 0.00070
               AND ABS(v.longitude - (-8.7780447)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1560773863 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Miróbriga', 'Santiago do Cacém', 'Setúbal', NULL,
       'Futebol', 'Estádio', NULL,
       38.0199181, -8.6946860, 'OPENSTREETMAP', 'OSM:WAY:1560773863',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1560773863')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Miróbriga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0199181)) <= 0.00070
               AND ABS(v.longitude - (-8.6946860)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:382305795 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Santiago do Cacém', 'Santiago do Cacém', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.0127672, -8.6954253, 'OPENSTREETMAP', 'OSM:WAY:382305795',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:382305795')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Santiago do Cacém'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.0127672)) <= 0.00070
               AND ABS(v.longitude - (-8.6954253)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1543229233 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Cercal', 'Santiago do Cacém', 'Setúbal', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.7993436, -8.6666491, 'OPENSTREETMAP', 'OSM:WAY:1543229233',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1543229233')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santiago do Cacém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Cercal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.7993436)) <= 0.00070
               AND ABS(v.longitude - (-8.6666491)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:160320580 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Amora Futebol Clube', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.6315720, -9.1174992, 'OPENSTREETMAP', 'OSM:WAY:160320580',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:160320580')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Amora Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6315720)) <= 0.00070
               AND ABS(v.longitude - (-9.1174992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:511144429 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Área de Exercício e Ping Pong', 'Seixal', 'Setúbal', NULL,
       'Ginásio;Ténis de mesa', 'Campo desportivo', NULL,
       38.6101131, -9.0918221, 'OPENSTREETMAP', 'OSM:WAY:511144429',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:511144429')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Área de Exercício e Ping Pong'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6101131)) <= 0.00070
               AND ABS(v.longitude - (-9.0918221)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1026697074 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Atlantic Dimension', 'Seixal', 'Setúbal', NULL,
       'Natação', 'Piscina', NULL,
       38.5979714, -9.0963972, 'OPENSTREETMAP', 'OSM:WAY:1026697074',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1026697074')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Atlantic Dimension'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5979714)) <= 0.00070
               AND ABS(v.longitude - (-9.0963972)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10123215881 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bodyvolution', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6327516, -9.1545523, 'OPENSTREETMAP', 'OSM:NODE:10123215881',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10123215881')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bodyvolution'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6327516)) <= 0.00070
               AND ABS(v.longitude - (-9.1545523)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:959898416 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bombastic', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6223476, -9.0943957, 'OPENSTREETMAP', 'OSM:WAY:959898416',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:959898416')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bombastic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6223476)) <= 0.00070
               AND ABS(v.longitude - (-9.0943957)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1007078842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Coberto', 'Seixal', 'Setúbal', NULL,
       'Futebol;Basquetebol;Ténis;Voleibol;Andebol', 'Campo desportivo', NULL,
       38.5861813, -9.1595370, 'OPENSTREETMAP', 'OSM:WAY:1007078842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1007078842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Coberto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5861813)) <= 0.00070
               AND ABS(v.longitude - (-9.1595370)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:510587346 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Casal do Marco', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6131572, -9.0881121, 'OPENSTREETMAP', 'OSM:WAY:510587346',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:510587346')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Casal do Marco'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6131572)) <= 0.00070
               AND ABS(v.longitude - (-9.0881121)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:964405857 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futsal', 'Seixal', 'Setúbal', NULL,
       'Futebol;Futsal', 'Campo desportivo', NULL,
       38.5514446, -9.0714898, 'OPENSTREETMAP', 'OSM:WAY:964405857',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:964405857')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futsal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5514446)) <= 0.00070
               AND ABS(v.longitude - (-9.0714898)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:964405855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Padel 1', 'Seixal', 'Setúbal', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.5492896, -9.0715543, 'OPENSTREETMAP', 'OSM:WAY:964405855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:964405855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Padel 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5492896)) <= 0.00070
               AND ABS(v.longitude - (-9.0715543)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:964405856 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Padel 2', 'Seixal', 'Setúbal', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.5492707, -9.0714262, 'OPENSTREETMAP', 'OSM:WAY:964405856',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:964405856')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Padel 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5492707)) <= 0.00070
               AND ABS(v.longitude - (-9.0714262)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:586659719 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Polidesportivo', 'Seixal', 'Setúbal', NULL,
       'Futebol;Basquetebol;Ténis;Voleibol;Andebol', 'Campo desportivo', NULL,
       38.5864708, -9.1584707, 'OPENSTREETMAP', 'OSM:WAY:586659719',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:586659719')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Polidesportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.5864708)) <= 0.00070
               AND ABS(v.longitude - (-9.1584707)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:491811079 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Cariocas Futebol Clube', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6180094, -9.1202473, 'OPENSTREETMAP', 'OSM:WAY:491811079',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:491811079')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Cariocas Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6180094)) <= 0.00070
               AND ABS(v.longitude - (-9.1202473)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:141138256 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural Desportivo das Paivas', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.6189009, -9.1172971, 'OPENSTREETMAP', 'OSM:WAY:141138256',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:141138256')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural Desportivo das Paivas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6189009)) <= 0.00070
               AND ABS(v.longitude - (-9.1172971)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:977649469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Treinos do Serrado', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6310564, -9.1209374, 'OPENSTREETMAP', 'OSM:WAY:977649469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:977649469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Treinos do Serrado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6310564)) <= 0.00070
               AND ABS(v.longitude - (-9.1209374)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:165439009 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Associativo Santa Marta do Pinhal', 'Seixal', 'Setúbal', NULL,
       'Futsal', 'Complexo desportivo', NULL,
       38.6258007, -9.1546916, 'OPENSTREETMAP', 'OSM:WAY:165439009',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:165439009')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Associativo Santa Marta do Pinhal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6258007)) <= 0.00070
               AND ABS(v.longitude - (-9.1546916)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:977649470 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Cidade de Amora', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.6314054, -9.1213840, 'OPENSTREETMAP', 'OSM:WAY:977649470',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:977649470')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Cidade de Amora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6314054)) <= 0.00070
               AND ABS(v.longitude - (-9.1213840)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1329028926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Vale da Abelha', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6187937, -9.0795606, 'OPENSTREETMAP', 'OSM:WAY:1329028926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1329028926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Vale da Abelha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6187937)) <= 0.00070
               AND ABS(v.longitude - (-9.0795606)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155870335 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Campus', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6414150, -9.0891833, 'OPENSTREETMAP', 'OSM:WAY:155870335',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155870335')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Campus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6414150)) <= 0.00070
               AND ABS(v.longitude - (-9.0891833)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154593252 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Clube de Corroios', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.6425034, -9.1560664, 'OPENSTREETMAP', 'OSM:WAY:154593252',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154593252')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Clube de Corroios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6425034)) <= 0.00070
               AND ABS(v.longitude - (-9.1560664)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:316853191 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Leão Altivo', 'Seixal', 'Setúbal', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.6259927, -9.1628878, 'OPENSTREETMAP', 'OSM:WAY:316853191',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:316853191')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Leão Altivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6259927)) <= 0.00070
               AND ABS(v.longitude - (-9.1628878)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13723243068 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Loiola Yoga Pilates', 'Seixal', 'Setúbal', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6439634, -9.1594393, 'OPENSTREETMAP', 'OSM:NODE:13723243068',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13723243068')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Loiola Yoga Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6439634)) <= 0.00070
               AND ABS(v.longitude - (-9.1594393)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1013617387 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Aldeia de Paio Pires', 'Seixal', 'Setúbal', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.6199352, -9.0883323, 'OPENSTREETMAP', 'OSM:WAY:1013617387',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1013617387')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seixal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Aldeia de Paio Pires'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6199352)) <= 0.00070
               AND ABS(v.longitude - (-9.0883323)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
