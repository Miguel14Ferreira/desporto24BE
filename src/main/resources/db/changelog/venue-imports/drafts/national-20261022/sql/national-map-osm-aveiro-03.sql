-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:222008392 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Rôge', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8536518, -8.3563914, 'OPENSTREETMAP', 'OSM:WAY:222008392',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222008392')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Rôge'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8536518)) <= 0.00070
               AND ABS(v.longitude - (-8.3563914)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136397866 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal Nº 2 de Vale de Cambra', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8411618, -8.3997131, 'OPENSTREETMAP', 'OSM:WAY:136397866',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136397866')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal Nº 2 de Vale de Cambra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8411618)) <= 0.00070
               AND ABS(v.longitude - (-8.3997131)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:739568742 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal das Dairas - Associação Desportiva Valecambrense', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8387205, -8.3963083, 'OPENSTREETMAP', 'OSM:WAY:739568742',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:739568742')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal das Dairas - Associação Desportiva Valecambrense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8387205)) <= 0.00070
               AND ABS(v.longitude - (-8.3963083)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221190779 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8534447, -8.3951837, 'OPENSTREETMAP', 'OSM:WAY:221190779',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221190779')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8534447)) <= 0.00070
               AND ABS(v.longitude - (-8.3951837)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221190431 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futsal', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8537774, -8.3955198, 'OPENSTREETMAP', 'OSM:WAY:221190431',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221190431')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futsal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8537774)) <= 0.00070
               AND ABS(v.longitude - (-8.3955198)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:134795831 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo e Cultural de Codal', 'Vale de Cambra', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.8599505, -8.4128806, 'OPENSTREETMAP', 'OSM:WAY:134795831',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:134795831')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo e Cultural de Codal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8599505)) <= 0.00070
               AND ABS(v.longitude - (-8.4128806)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2012384141 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Ilídio Pedro', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.8457653, -8.4099530, 'OPENSTREETMAP', 'OSM:NODE:2012384141',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2012384141')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Ilídio Pedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8457653)) <= 0.00070
               AND ABS(v.longitude - (-8.4099530)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105605641 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'piscina de 5 metros de profundidade', 'Vale de Cambra', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.8394808, -8.3991213, 'OPENSTREETMAP', 'OSM:WAY:105605641',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105605641')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('piscina de 5 metros de profundidade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8394808)) <= 0.00070
               AND ABS(v.longitude - (-8.3991213)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105613227 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'piscina infantil', 'Vale de Cambra', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.8394903, -8.3988320, 'OPENSTREETMAP', 'OSM:WAY:105613227',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105613227')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('piscina infantil'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8394903)) <= 0.00070
               AND ABS(v.longitude - (-8.3988320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105613223 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Coberta', 'Vale de Cambra', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.8397257, -8.3985704, 'OPENSTREETMAP', 'OSM:WAY:105613223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105613223')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Coberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8397257)) <= 0.00070
               AND ABS(v.longitude - (-8.3985704)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:830788459 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate park', 'Vale de Cambra', 'Aveiro', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.8527025, -8.3973793, 'OPENSTREETMAP', 'OSM:WAY:830788459',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:830788459')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8527025)) <= 0.00070
               AND ABS(v.longitude - (-8.3973793)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:958822828 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Cultural e Recreativa de Fuste e Função', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8581807, -8.3322293, 'OPENSTREETMAP', 'OSM:WAY:958822828',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:958822828')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Cultural e Recreativa de Fuste e Função'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8581807)) <= 0.00070
               AND ABS(v.longitude - (-8.3322293)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221190454 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Voleibol de Praia', 'Vale de Cambra', 'Aveiro', NULL,
       'Voleibol de Praia', 'Campo desportivo', NULL,
       40.8538143, -8.3945929, 'OPENSTREETMAP', 'OSM:WAY:221190454',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221190454')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Voleibol de Praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8538143)) <= 0.00070
               AND ABS(v.longitude - (-8.3945929)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
