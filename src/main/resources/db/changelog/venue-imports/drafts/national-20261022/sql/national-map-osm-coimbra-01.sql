-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:145943204 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Marialvas', 'Cantanhede', 'Coimbra', NULL,
       'Futsal;Basquetebol;Voleibol', 'Complexo desportivo', NULL,
       40.3440287, -8.5876799, 'OPENSTREETMAP', 'OSM:WAY:145943204',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145943204')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Marialvas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3440287)) <= 0.00070
               AND ABS(v.longitude - (-8.5876799)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:629287153 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Largo de São João', 'Cantanhede', 'Coimbra', NULL,
       'Futsal;Andebol;Basquetebol', 'Campo desportivo', NULL,
       40.3519276, -8.5912986, 'OPENSTREETMAP', 'OSM:WAY:629287153',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:629287153')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Largo de São João'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3519276)) <= 0.00070
               AND ABS(v.longitude - (-8.5912986)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12403716933 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '2 Scorpions', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2134876, -8.4321961, 'OPENSTREETMAP', 'OSM:NODE:12403716933',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12403716933')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('2 Scorpions'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2134876)) <= 0.00070
               AND ABS(v.longitude - (-8.4321961)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1056855283 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Olímpico de Piscinas Municipais', 'Coimbra', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', 'INDOOR',
       40.2053924, -8.4068928, 'OPENSTREETMAP', 'OSM:WAY:1056855283',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1056855283')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Olímpico de Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2053924)) <= 0.00070
               AND ABS(v.longitude - (-8.4068928)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8706422917 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Chintamani - Centro Yoga Coimbra', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2193546, -8.4043542, 'OPENSTREETMAP', 'OSM:NODE:8706422917',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8706422917')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Chintamani - Centro Yoga Coimbra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2193546)) <= 0.00070
               AND ABS(v.longitude - (-8.4043542)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9946021388 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Coimbra Funcional Fitness', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Complexo desportivo', NULL,
       40.1951219, -8.4019949, 'OPENSTREETMAP', 'OSM:NODE:9946021388',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9946021388')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Coimbra Funcional Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1951219)) <= 0.00070
               AND ABS(v.longitude - (-8.4019949)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12887720297 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Dojo Fit', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2022989, -8.4096104, 'OPENSTREETMAP', 'OSM:NODE:12887720297',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12887720297')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Dojo Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2022989)) <= 0.00070
               AND ABS(v.longitude - (-8.4096104)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14214369245 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Eleven', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1853374, -8.4053086, 'OPENSTREETMAP', 'OSM:NODE:14214369245',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14214369245')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Eleven'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1853374)) <= 0.00070
               AND ABS(v.longitude - (-8.4053086)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11050147142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Faculdades do Corpo', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2067473, -8.4167585, 'OPENSTREETMAP', 'OSM:NODE:11050147142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11050147142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Faculdades do Corpo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2067473)) <= 0.00070
               AND ABS(v.longitude - (-8.4167585)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:840599316 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fisicamente', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1937535, -8.4653468, 'OPENSTREETMAP', 'OSM:WAY:840599316',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:840599316')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fisicamente'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1937535)) <= 0.00070
               AND ABS(v.longitude - (-8.4653468)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9783837556 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Gym XXL', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2219395, -8.4364996, 'OPENSTREETMAP', 'OSM:NODE:9783837556',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9783837556')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Gym XXL'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2219395)) <= 0.00070
               AND ABS(v.longitude - (-8.4364996)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1059553344 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Level Up Artes Marciais', 'Coimbra', 'Coimbra', NULL,
       'Taekwondo', 'Complexo desportivo', NULL,
       40.1829852, -8.4074602, 'OPENSTREETMAP', 'OSM:WAY:1059553344',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1059553344')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Level Up Artes Marciais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1829852)) <= 0.00070
               AND ABS(v.longitude - (-8.4074602)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10668067527 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Moisés Lourenço Premium Studio', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1841913, -8.4061838, 'OPENSTREETMAP', 'OSM:NODE:10668067527',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10668067527')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Moisés Lourenço Premium Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1841913)) <= 0.00070
               AND ABS(v.longitude - (-8.4061838)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9915547792 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'my fit', 'Coimbra', 'Coimbra', NULL,
       'Ginásio;Ginástica', 'Complexo desportivo', NULL,
       40.2137496, -8.4121217, 'OPENSTREETMAP', 'OSM:NODE:9915547792',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9915547792')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('my fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2137496)) <= 0.00070
               AND ABS(v.longitude - (-8.4121217)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12885873092 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'On Move', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2120504, -8.3973648, 'OPENSTREETMAP', 'OSM:NODE:12885873092',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12885873092')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('On Move'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2120504)) <= 0.00070
               AND ABS(v.longitude - (-8.3973648)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:206969817 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão dos Olivais', 'Coimbra', 'Coimbra', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.2171034, -8.4071435, 'OPENSTREETMAP', 'OSM:WAY:206969817',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:206969817')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão dos Olivais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2171034)) <= 0.00070
               AND ABS(v.longitude - (-8.4071435)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221555141 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Phive Celas', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2121639, -8.4177825, 'OPENSTREETMAP', 'OSM:WAY:221555141',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221555141')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Phive Celas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2121639)) <= 0.00070
               AND ABS(v.longitude - (-8.4177825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122685516 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Rui Abreu', 'Coimbra', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', 'INDOOR',
       40.2391033, -8.4384818, 'OPENSTREETMAP', 'OSM:WAY:122685516',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122685516')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Rui Abreu'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2391033)) <= 0.00070
               AND ABS(v.longitude - (-8.4384818)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12761542839 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sky Studio', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2166066, -8.4062822, 'OPENSTREETMAP', 'OSM:NODE:12761542839',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12761542839')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sky Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2166066)) <= 0.00070
               AND ABS(v.longitude - (-8.4062822)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13660506485 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Elephant Studio - Centro Pilates', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2128339, -8.4074355, 'OPENSTREETMAP', 'OSM:NODE:13660506485',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13660506485')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Elephant Studio - Centro Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2128339)) <= 0.00070
               AND ABS(v.longitude - (-8.4074355)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10926434173 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The First', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2098458, -8.3992423, 'OPENSTREETMAP', 'OSM:NODE:10926434173',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10926434173')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The First'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2098458)) <= 0.00070
               AND ABS(v.longitude - (-8.3992423)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14214369246 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The First', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1851425, -8.4039955, 'OPENSTREETMAP', 'OSM:NODE:14214369246',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14214369246')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The First'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1851425)) <= 0.00070
               AND ABS(v.longitude - (-8.4039955)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12578631498 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'upa!', 'Coimbra', 'Coimbra', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       40.2107804, -8.4336092, 'OPENSTREETMAP', 'OSM:NODE:12578631498',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12578631498')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('upa!'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2107804)) <= 0.00070
               AND ABS(v.longitude - (-8.4336092)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12885873091 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2121730, -8.3972905, 'OPENSTREETMAP', 'OSM:NODE:12885873091',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12885873091')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2121730)) <= 0.00070
               AND ABS(v.longitude - (-8.3972905)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11951983332 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Arena Fitness', 'Figueira da Foz', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1628003, -8.8601867, 'OPENSTREETMAP', 'OSM:NODE:11951983332',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11951983332')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Arena Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1628003)) <= 0.00070
               AND ABS(v.longitude - (-8.8601867)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11945184723 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BioCorpo', 'Figueira da Foz', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1630170, -8.8638950, 'OPENSTREETMAP', 'OSM:NODE:11945184723',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11945184723')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BioCorpo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1630170)) <= 0.00070
               AND ABS(v.longitude - (-8.8638950)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12078195768 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitcoach', 'Figueira da Foz', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1643181, -8.8673093, 'OPENSTREETMAP', 'OSM:NODE:12078195768',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12078195768')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitcoach'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1643181)) <= 0.00070
               AND ABS(v.longitude - (-8.8673093)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:571068702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Paião', 'Figueira da Foz', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.0683879, -8.8109469, 'OPENSTREETMAP', 'OSM:WAY:571068702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:571068702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Paião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0683879)) <= 0.00070
               AND ABS(v.longitude - (-8.8109469)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155333876 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Académico das Gândaras', 'Lousã', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1288861, -8.2725570, 'OPENSTREETMAP', 'OSM:WAY:1155333876',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155333876')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Académico das Gândaras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1288861)) <= 0.00070
               AND ABS(v.longitude - (-8.2725570)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14154368552 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Is´Fit', 'Miranda do Corvo', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.0957228, -8.3320112, 'OPENSTREETMAP', 'OSM:NODE:14154368552',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14154368552')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Miranda do Corvo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Is´Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0957228)) <= 0.00070
               AND ABS(v.longitude - (-8.3320112)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1418706110 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Área de Lazer de S. Martinho da Cortiça', 'Arganil', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.2729687, -8.1476677, 'OPENSTREETMAP', 'OSM:WAY:1418706110',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1418706110')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Área de Lazer de S. Martinho da Cortiça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2729687)) <= 0.00070
               AND ABS(v.longitude - (-8.1476677)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:486646810 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do C.O.J.A.', 'Arganil', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2698420, -7.9806879, 'OPENSTREETMAP', 'OSM:WAY:486646810',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:486646810')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do C.O.J.A.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2698420)) <= 0.00070
               AND ABS(v.longitude - (-7.9806879)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:162696843 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Doutor Eduardo Ralha', 'Arganil', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2179757, -8.0571535, 'OPENSTREETMAP', 'OSM:WAY:162696843',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:162696843')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Doutor Eduardo Ralha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2179757)) <= 0.00070
               AND ABS(v.longitude - (-8.0571535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1214572655 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Porto Castanheiro', 'Arganil', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1947081, -7.9243190, 'OPENSTREETMAP', 'OSM:WAY:1214572655',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1214572655')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Porto Castanheiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1947081)) <= 0.00070
               AND ABS(v.longitude - (-7.9243190)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:569472492 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Sarzedo', 'Arganil', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.2395855, -8.0675343, 'OPENSTREETMAP', 'OSM:WAY:569472492',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:569472492')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Sarzedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2395855)) <= 0.00070
               AND ABS(v.longitude - (-8.0675343)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:746808782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina dos Cepos', 'Arganil', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1616610, -7.9774380, 'OPENSTREETMAP', 'OSM:WAY:746808782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:746808782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina dos Cepos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1616610)) <= 0.00070
               AND ABS(v.longitude - (-7.9774380)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:833521149 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Fluvial de Sobral Magro', 'Arganil', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.2478569, -7.8584477, 'OPENSTREETMAP', 'OSM:WAY:833521149',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:833521149')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Fluvial de Sobral Magro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2478569)) <= 0.00070
               AND ABS(v.longitude - (-7.8584477)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:685709216 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Arganil', 'Arganil', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.2193072, -8.0635711, 'OPENSTREETMAP', 'OSM:WAY:685709216',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:685709216')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Arganil'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Arganil'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2193072)) <= 0.00070
               AND ABS(v.longitude - (-8.0635711)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:246065445 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Desembargador Costa Soares', 'Cantanhede', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4066015, -8.6363775, 'OPENSTREETMAP', 'OSM:WAY:246065445',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:246065445')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Desembargador Costa Soares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4066015)) <= 0.00070
               AND ABS(v.longitude - (-8.6363775)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:245870166 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Corticeiro de Cima', 'Cantanhede', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4247725, -8.6776181, 'OPENSTREETMAP', 'OSM:WAY:245870166',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:245870166')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Corticeiro de Cima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4247725)) <= 0.00070
               AND ABS(v.longitude - (-8.6776181)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:533891983 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo da Malhada', 'Cantanhede', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4674869, -8.5973288, 'OPENSTREETMAP', 'OSM:WAY:533891983',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:533891983')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo da Malhada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4674869)) <= 0.00070
               AND ABS(v.longitude - (-8.5973288)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1293941480 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Golfe de Cantanhede', 'Cantanhede', 'Coimbra', NULL,
       'Golfe', 'Campo de golfe', NULL,
       40.3619248, -8.6047046, 'OPENSTREETMAP', 'OSM:WAY:1293941480',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1293941480')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Golfe de Cantanhede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3619248)) <= 0.00070
               AND ABS(v.longitude - (-8.6047046)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:259205263 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Febres Sport Club', 'Cantanhede', 'Coimbra', NULL,
       'Futebol', 'Estádio', NULL,
       40.3958993, -8.6347981, 'OPENSTREETMAP', 'OSM:WAY:259205263',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:259205263')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Febres Sport Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3958993)) <= 0.00070
               AND ABS(v.longitude - (-8.6347981)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:629293525 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Cantanhede', 'Cantanhede', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3631351, -8.6074011, 'OPENSTREETMAP', 'OSM:WAY:629293525',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:629293525')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Cantanhede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3631351)) <= 0.00070
               AND ABS(v.longitude - (-8.6074011)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1058222789 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ex. campo de futebol (inoperacional)', 'Cantanhede', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3623532, -8.6442596, 'OPENSTREETMAP', 'OSM:WAY:1058222789',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1058222789')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ex. campo de futebol (inoperacional)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3623532)) <= 0.00070
               AND ABS(v.longitude - (-8.6442596)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:145943195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Cantanhede', 'Cantanhede', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.3444041, -8.5863591, 'OPENSTREETMAP', 'OSM:WAY:145943195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145943195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Cantanhede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3444041)) <= 0.00070
               AND ABS(v.longitude - (-8.5863591)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:824376723 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Febres', 'Cantanhede', 'Coimbra', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.3956569, -8.6347358, 'OPENSTREETMAP', 'OSM:WAY:824376723',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:824376723')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Febres'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3956569)) <= 0.00070
               AND ABS(v.longitude - (-8.6347358)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:790754717 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sombras Negras', 'Cantanhede', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3130242, -8.6078659, 'OPENSTREETMAP', 'OSM:WAY:790754717',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:790754717')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sombras Negras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3130242)) <= 0.00070
               AND ABS(v.longitude - (-8.6078659)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1505422635 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'XTraining', 'Cantanhede', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.3564577, -8.5883923, 'OPENSTREETMAP', 'OSM:WAY:1505422635',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1505422635')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cantanhede'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('XTraining'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3564577)) <= 0.00070
               AND ABS(v.longitude - (-8.5883923)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1145022947 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Adhoc Life', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1950544, -8.3952471, 'OPENSTREETMAP', 'OSM:WAY:1145022947',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1145022947')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Adhoc Life'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1950544)) <= 0.00070
               AND ABS(v.longitude - (-8.3952471)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:935452027 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Calvário do Inferno', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3163568, -8.4282813, 'OPENSTREETMAP', 'OSM:WAY:935452027',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:935452027')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Calvário do Inferno'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3163568)) <= 0.00070
               AND ABS(v.longitude - (-8.4282813)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:251024537 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 2', 'Coimbra', 'Coimbra', NULL,
       'Rugby', 'Campo desportivo', NULL,
       40.2053361, -8.4353935, 'OPENSTREETMAP', 'OSM:WAY:251024537',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:251024537')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2053361)) <= 0.00070
               AND ABS(v.longitude - (-8.4353935)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:280043950 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 3', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2042873, -8.4346664, 'OPENSTREETMAP', 'OSM:WAY:280043950',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:280043950')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2042873)) <= 0.00070
               AND ABS(v.longitude - (-8.4346664)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1671852396 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Ceres - JDV', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.2661139, -8.4355346, 'OPENSTREETMAP', 'OSM:NODE:1671852396',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1671852396')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Ceres - JDV'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2661139)) <= 0.00070
               AND ABS(v.longitude - (-8.4355346)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127950159 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Moita Santa', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1331736, -8.4644864, 'OPENSTREETMAP', 'OSM:WAY:127950159',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127950159')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Moita Santa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1331736)) <= 0.00070
               AND ABS(v.longitude - (-8.4644864)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:486774013 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquetebol', 'Coimbra', 'Coimbra', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.2026263, -8.4092540, 'OPENSTREETMAP', 'OSM:WAY:486774013',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:486774013')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2026263)) <= 0.00070
               AND ABS(v.longitude - (-8.4092540)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:935450390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol 7 Cabeço dos Moinhos', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3101780, -8.4223252, 'OPENSTREETMAP', 'OSM:WAY:935450390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:935450390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol 7 Cabeço dos Moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3101780)) <= 0.00070
               AND ABS(v.longitude - (-8.4223252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:128055594 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Clube Real da Conchada', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2177479, -8.4311931, 'OPENSTREETMAP', 'OSM:WAY:128055594',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:128055594')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Clube Real da Conchada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2177479)) <= 0.00070
               AND ABS(v.longitude - (-8.4311931)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:269740451 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Santa Cruz', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.2103558, -8.4164535, 'OPENSTREETMAP', 'OSM:WAY:269740451',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:269740451')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Santa Cruz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2103558)) <= 0.00070
               AND ABS(v.longitude - (-8.4164535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:248563660 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Mortal', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3335157, -8.3938712, 'OPENSTREETMAP', 'OSM:WAY:248563660',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:248563660')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Mortal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3335157)) <= 0.00070
               AND ABS(v.longitude - (-8.3938712)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:116268967 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol Bombeiros', 'Coimbra', 'Coimbra', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       40.1936783, -8.4139116, 'OPENSTREETMAP', 'OSM:WAY:116268967',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:116268967')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol Bombeiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1936783)) <= 0.00070
               AND ABS(v.longitude - (-8.4139116)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1671850121 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol Mualdes - RCB', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.2727828, -8.4095129, 'OPENSTREETMAP', 'OSM:NODE:1671850121',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1671850121')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol Mualdes - RCB'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2727828)) <= 0.00070
               AND ABS(v.longitude - (-8.4095129)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:290276662 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Norton de Matos Futsal', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1994513, -8.4103946, 'OPENSTREETMAP', 'OSM:WAY:290276662',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:290276662')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Norton de Matos Futsal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1994513)) <= 0.00070
               AND ABS(v.longitude - (-8.4103946)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:60974347 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Coimbra', 'Coimbra', 'Coimbra', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.1968795, -8.4193708, 'OPENSTREETMAP', 'OSM:WAY:60974347',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:60974347')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Coimbra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1968795)) <= 0.00070
               AND ABS(v.longitude - (-8.4193708)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122997655 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis do Choupal', 'Coimbra', 'Coimbra', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.2216129, -8.4472194, 'OPENSTREETMAP', 'OSM:WAY:122997655',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122997655')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis do Choupal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2216129)) <= 0.00070
               AND ABS(v.longitude - (-8.4472194)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:186781799 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas Luís Lopes da Conceição', 'Coimbra', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', 'INDOOR',
       40.2094031, -8.4539021, 'OPENSTREETMAP', 'OSM:WAY:186781799',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:186781799')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas Luís Lopes da Conceição'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2094031)) <= 0.00070
               AND ABS(v.longitude - (-8.4539021)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1194231307 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Integrado e Centro Olímpico de Ginástica de Coimbra', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1935017, -8.4143643, 'OPENSTREETMAP', 'OSM:WAY:1194231307',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1194231307')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Integrado e Centro Olímpico de Ginástica de Coimbra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1935017)) <= 0.00070
               AND ABS(v.longitude - (-8.4143643)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10668067522 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'E-Fit Electrofitness', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1843679, -8.4054225, 'OPENSTREETMAP', 'OSM:NODE:10668067522',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10668067522')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('E-Fit Electrofitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1843679)) <= 0.00070
               AND ABS(v.longitude - (-8.4054225)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:58257174 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Cidade de Coimbra', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Estádio', NULL,
       40.2032099, -8.4078114, 'OPENSTREETMAP', 'OSM:WAY:58257174',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:58257174')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Cidade de Coimbra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2032099)) <= 0.00070
               AND ABS(v.longitude - (-8.4078114)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:926023613 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio da Arregaça', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1953904, -8.4188492, 'OPENSTREETMAP', 'OSM:WAY:926023613',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:926023613')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio da Arregaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1953904)) <= 0.00070
               AND ABS(v.longitude - (-8.4188492)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10240971171 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Krush It by Kalorias', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2117198, -8.4431080, 'OPENSTREETMAP', 'OSM:NODE:10240971171',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10240971171')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Krush It by Kalorias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2117198)) <= 0.00070
               AND ABS(v.longitude - (-8.4431080)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7105625049 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Nelson Gym', 'Coimbra', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2055817, -8.4018374, 'OPENSTREETMAP', 'OSM:NODE:7105625049',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7105625049')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Nelson Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2055817)) <= 0.00070
               AND ABS(v.longitude - (-8.4018374)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9761597903 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Municipal de Skate', 'Coimbra', 'Coimbra', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.1927785, -8.4216287, 'OPENSTREETMAP', 'OSM:NODE:9761597903',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9761597903')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Municipal de Skate'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1927785)) <= 0.00070
               AND ABS(v.longitude - (-8.4216287)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:384376528 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pátio Desportivo do Monte Formoso', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2232137, -8.4367321, 'OPENSTREETMAP', 'OSM:WAY:384376528',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:384376528')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pátio Desportivo do Monte Formoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2232137)) <= 0.00070
               AND ABS(v.longitude - (-8.4367321)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1214695052 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão da Palheira', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.1635216, -8.4477269, 'OPENSTREETMAP', 'OSM:WAY:1214695052',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1214695052')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão da Palheira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1635216)) <= 0.00070
               AND ABS(v.longitude - (-8.4477269)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127949464 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão da Regibós', 'Coimbra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1370442, -8.4668410, 'OPENSTREETMAP', 'OSM:WAY:127949464',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127949464')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão da Regibós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1370442)) <= 0.00070
               AND ABS(v.longitude - (-8.4668410)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1220420918 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Infantil', 'Coimbra', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1981155, -8.4274555, 'OPENSTREETMAP', 'OSM:WAY:1220420918',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1220420918')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Infantil'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1981155)) <= 0.00070
               AND ABS(v.longitude - (-8.4274555)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1189198299 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Termal', 'Coimbra', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1372666, -8.4562992, 'OPENSTREETMAP', 'OSM:WAY:1189198299',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1189198299')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Termal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1372666)) <= 0.00070
               AND ABS(v.longitude - (-8.4562992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1046335192 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Municipal de BMX', 'Coimbra', 'Coimbra', NULL,
       'BMX', 'Campo desportivo', NULL,
       40.2262702, -8.4425574, 'OPENSTREETMAP', 'OSM:WAY:1046335192',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1046335192')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Municipal de BMX'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2262702)) <= 0.00070
               AND ABS(v.longitude - (-8.4425574)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:174739511 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park', 'Coimbra', 'Coimbra', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.1988709, -8.4288594, 'OPENSTREETMAP', 'OSM:WAY:174739511',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:174739511')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1988709)) <= 0.00070
               AND ABS(v.longitude - (-8.4288594)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1151656295 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Star Padel Coimbra', 'Coimbra', 'Coimbra', NULL,
       'Padel', 'Complexo desportivo', NULL,
       40.1942362, -8.4317592, 'OPENSTREETMAP', 'OSM:WAY:1151656295',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1151656295')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Star Padel Coimbra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1942362)) <= 0.00070
               AND ABS(v.longitude - (-8.4317592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:486785853 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ténis de Mesa', 'Coimbra', 'Coimbra', NULL,
       'Ténis de mesa', 'Campo desportivo', NULL,
       40.2194970, -8.4370113, 'OPENSTREETMAP', 'OSM:WAY:486785853',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:486785853')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coimbra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ténis de Mesa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2194970)) <= 0.00070
               AND ABS(v.longitude - (-8.4370113)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1134153369 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '77'' Academy', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Padel', 'Complexo desportivo', NULL,
       40.1201101, -8.4983150, 'OPENSTREETMAP', 'OSM:WAY:1134153369',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1134153369')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('77'' Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1201101)) <= 0.00070
               AND ABS(v.longitude - (-8.4983150)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127950889 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Condeixa-a-Nova', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1188533, -8.4940047, 'OPENSTREETMAP', 'OSM:WAY:127950889',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127950889')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Condeixa-a-Nova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1188533)) <= 0.00070
               AND ABS(v.longitude - (-8.4940047)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:146268825 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Ténis - Urbanização Nova Conímbriga', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.1074361, -8.5046013, 'OPENSTREETMAP', 'OSM:WAY:146268825',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:146268825')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Ténis - Urbanização Nova Conímbriga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1074361)) <= 0.00070
               AND ABS(v.longitude - (-8.5046013)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157714371 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Condeixa', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1179331, -8.4918137, 'OPENSTREETMAP', 'OSM:WAY:157714371',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157714371')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Condeixa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1179331)) <= 0.00070
               AND ABS(v.longitude - (-8.4918137)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7012636557 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gimfit', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1064022, -8.4896475, 'OPENSTREETMAP', 'OSM:NODE:7012636557',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7012636557')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gimfit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1064022)) <= 0.00070
               AND ABS(v.longitude - (-8.4896475)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1134076416 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mini Golfe', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Golfe', 'Campo de golfe', NULL,
       40.1118131, -8.4906652, 'OPENSTREETMAP', 'OSM:WAY:1134076416',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1134076416')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mini Golfe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1118131)) <= 0.00070
               AND ABS(v.longitude - (-8.4906652)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:524496831 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Ega', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.0954678, -8.5396820, 'OPENSTREETMAP', 'OSM:WAY:524496831',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:524496831')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Ega'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0954678)) <= 0.00070
               AND ABS(v.longitude - (-8.5396820)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169295645 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Condeixa', 'Condeixa-a-Nova', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1086287, -8.4967535, 'OPENSTREETMAP', 'OSM:WAY:1169295645',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169295645')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Condeixa-a-Nova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Condeixa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1086287)) <= 0.00070
               AND ABS(v.longitude - (-8.4967535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:212875644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Basketball', 'Figueira da Foz', 'Coimbra', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.1619178, -8.8631357, 'OPENSTREETMAP', 'OSM:WAY:212875644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:212875644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Basketball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1619178)) <= 0.00070
               AND ABS(v.longitude - (-8.8631357)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:285740038 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Misericórdia', 'Figueira da Foz', 'Coimbra', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.1524809, -8.8551865, 'OPENSTREETMAP', 'OSM:WAY:285740038',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:285740038')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Misericórdia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1524809)) <= 0.00070
               AND ABS(v.longitude - (-8.8551865)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:285738736 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Abadias', 'Figueira da Foz', 'Coimbra', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.1534101, -8.8627798, 'OPENSTREETMAP', 'OSM:WAY:285738736',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:285738736')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Abadias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1534101)) <= 0.00070
               AND ABS(v.longitude - (-8.8627798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:746819252 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Acácias Francisco da Silva Jordão', 'Figueira da Foz', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0918274, -8.8482540, 'OPENSTREETMAP', 'OSM:WAY:746819252',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:746819252')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Acácias Francisco da Silva Jordão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0918274)) <= 0.00070
               AND ABS(v.longitude - (-8.8482540)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142678795 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquete', 'Figueira da Foz', 'Coimbra', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.1530201, -8.8674963, 'OPENSTREETMAP', 'OSM:WAY:142678795',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142678795')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquete'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1530201)) <= 0.00070
               AND ABS(v.longitude - (-8.8674963)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142682257 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquetebol', 'Figueira da Foz', 'Coimbra', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.1615574, -8.8724985, 'OPENSTREETMAP', 'OSM:WAY:142682257',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142682257')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1615574)) <= 0.00070
               AND ABS(v.longitude - (-8.8724985)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122113545 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol', 'Figueira da Foz', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1502081, -8.8672075, 'OPENSTREETMAP', 'OSM:WAY:122113545',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122113545')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1502081)) <= 0.00070
               AND ABS(v.longitude - (-8.8672075)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:212875645 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol', 'Figueira da Foz', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1621757, -8.8628973, 'OPENSTREETMAP', 'OSM:WAY:212875645',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:212875645')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1621757)) <= 0.00070
               AND ABS(v.longitude - (-8.8628973)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221176926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Nautico da Figueira da Foz', 'Figueira da Foz', 'Coimbra', NULL,
       'Vela', 'Complexo desportivo', NULL,
       40.1466903, -8.8637218, 'OPENSTREETMAP', 'OSM:WAY:221176926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221176926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Nautico da Figueira da Foz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1466903)) <= 0.00070
               AND ABS(v.longitude - (-8.8637218)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:113537095 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Piscina Praia-Mar', 'Figueira da Foz', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1516125, -8.8663150, 'OPENSTREETMAP', 'OSM:WAY:113537095',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:113537095')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Piscina Praia-Mar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1516125)) <= 0.00070
               AND ABS(v.longitude - (-8.8663150)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:266069235 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal José Bento Pessoa', 'Figueira da Foz', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1628140, -8.8592924, 'OPENSTREETMAP', 'OSM:WAY:266069235',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:266069235')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal José Bento Pessoa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1628140)) <= 0.00070
               AND ABS(v.longitude - (-8.8592924)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1288151707 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitting Point', 'Figueira da Foz', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1646474, -8.8577948, 'OPENSTREETMAP', 'OSM:WAY:1288151707',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1288151707')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitting Point'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1646474)) <= 0.00070
               AND ABS(v.longitude - (-8.8577948)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11946160409 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kubu', 'Figueira da Foz', 'Coimbra', NULL,
       'Ginásio;Ciclismo;Corrida', 'Ginásio', NULL,
       40.1660528, -8.8515938, 'OPENSTREETMAP', 'OSM:NODE:11946160409',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11946160409')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kubu'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1660528)) <= 0.00070
               AND ABS(v.longitude - (-8.8515938)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1291810326 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de água aquecida', 'Figueira da Foz', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1513203, -8.8682909, 'OPENSTREETMAP', 'OSM:WAY:1291810326',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1291810326')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de água aquecida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1513203)) <= 0.00070
               AND ABS(v.longitude - (-8.8682909)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169706948 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Marinha das Ondas', 'Figueira da Foz', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.0268721, -8.8198768, 'OPENSTREETMAP', 'OSM:WAY:1169706948',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169706948')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Marinha das Ondas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0268721)) <= 0.00070
               AND ABS(v.longitude - (-8.8198768)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:485352274 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Quiaios', 'Figueira da Foz', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.2182853, -8.8921543, 'OPENSTREETMAP', 'OSM:WAY:485352274',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:485352274')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Quiaios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2182853)) <= 0.00070
               AND ABS(v.longitude - (-8.8921543)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:284532172 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Ginásio Clube Figueirense', 'Figueira da Foz', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1633074, -8.8603592, 'OPENSTREETMAP', 'OSM:WAY:284532172',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:284532172')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Ginásio Clube Figueirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1633074)) <= 0.00070
               AND ABS(v.longitude - (-8.8603592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221177028 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Posto Nautico da Naval 1º de Maio', 'Figueira da Foz', 'Coimbra', NULL,
       'Remo', 'Complexo desportivo', NULL,
       40.1468956, -8.8637459, 'OPENSTREETMAP', 'OSM:WAY:221177028',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221177028')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Posto Nautico da Naval 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1468956)) <= 0.00070
               AND ABS(v.longitude - (-8.8637459)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142682312 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park', 'Figueira da Foz', 'Coimbra', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.1628985, -8.8738637, 'OPENSTREETMAP', 'OSM:WAY:142682312',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142682312')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1628985)) <= 0.00070
               AND ABS(v.longitude - (-8.8738637)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:117445632 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tennis Club da Figueira da Foz', 'Figueira da Foz', 'Coimbra', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.1482129, -8.8656581, 'OPENSTREETMAP', 'OSM:WAY:117445632',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:117445632')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira da Foz'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tennis Club da Figueira da Foz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1482129)) <= 0.00070
               AND ABS(v.longitude - (-8.8656581)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13442764245 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '12.79 Trainig Center', 'Lousã', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.1320596, -8.2538467, 'OPENSTREETMAP', 'OSM:NODE:13442764245',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13442764245')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('12.79 Trainig Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1320596)) <= 0.00070
               AND ABS(v.longitude - (-8.2538467)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:277708565 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de 5 da EDP', 'Lousã', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1211615, -8.2444436, 'OPENSTREETMAP', 'OSM:NODE:277708565',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:277708565')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de 5 da EDP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1211615)) <= 0.00070
               AND ABS(v.longitude - (-8.2444436)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:217843305 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Doutor José Pinto de Aguiar', 'Lousã', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1181018, -8.2353799, 'OPENSTREETMAP', 'OSM:WAY:217843305',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:217843305')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Doutor José Pinto de Aguiar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1181018)) <= 0.00070
               AND ABS(v.longitude - (-8.2353799)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:260961575 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio José Redondo', 'Lousã', 'Coimbra', NULL,
       'Rugby', 'Campo desportivo', NULL,
       40.1100824, -8.2530917, 'OPENSTREETMAP', 'OSM:WAY:260961575',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:260961575')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio José Redondo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1100824)) <= 0.00070
               AND ABS(v.longitude - (-8.2530917)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:405016586 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Parque Carlos Reis', 'Lousã', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1115402, -8.2428532, 'OPENSTREETMAP', 'OSM:WAY:405016586',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:405016586')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Parque Carlos Reis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1115402)) <= 0.00070
               AND ABS(v.longitude - (-8.2428532)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1476958846 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo De Futebol De Carromeu', 'Mira', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4145664, -8.7387169, 'OPENSTREETMAP', 'OSM:WAY:1476958846',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1476958846')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo De Futebol De Carromeu'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4145664)) <= 0.00070
               AND ABS(v.longitude - (-8.7387169)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:245781895 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Mira', 'Mira', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.4291911, -8.7413707, 'OPENSTREETMAP', 'OSM:WAY:245781895',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:245781895')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Mira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4291911)) <= 0.00070
               AND ABS(v.longitude - (-8.7413707)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152463409 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Miranda do Corvo', 'Miranda do Corvo', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.0910695, -8.3318006, 'OPENSTREETMAP', 'OSM:WAY:1152463409',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152463409')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Miranda do Corvo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Miranda do Corvo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0910695)) <= 0.00070
               AND ABS(v.longitude - (-8.3318006)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:175108287 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Semide', 'Miranda do Corvo', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1556183, -8.3345122, 'OPENSTREETMAP', 'OSM:WAY:175108287',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:175108287')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Miranda do Corvo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Semide'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1556183)) <= 0.00070
               AND ABS(v.longitude - (-8.3345122)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:538797874 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Amigos da Freguesia de Arazede', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Hóquei', 'Complexo desportivo', NULL,
       40.2873374, -8.6764741, 'OPENSTREETMAP', 'OSM:WAY:538797874',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:538797874')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Amigos da Freguesia de Arazede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2873374)) <= 0.00070
               AND ABS(v.longitude - (-8.6764741)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:176012227 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Montemor-o-Velho', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1797966, -8.6736105, 'OPENSTREETMAP', 'OSM:WAY:176012227',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:176012227')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Montemor-o-Velho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1797966)) <= 0.00070
               AND ABS(v.longitude - (-8.6736105)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:124424059 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de São Pedro', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Futebol', 'Estádio', NULL,
       40.2032077, -8.6460972, 'OPENSTREETMAP', 'OSM:WAY:124424059',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:124424059')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de São Pedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2032077)) <= 0.00070
               AND ABS(v.longitude - (-8.6460972)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:124425683 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de São Pedro 2', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Futebol;Rugby', 'Campo desportivo', NULL,
       40.2023520, -8.6453086, 'OPENSTREETMAP', 'OSM:WAY:124425683',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:124425683')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de São Pedro 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2023520)) <= 0.00070
               AND ABS(v.longitude - (-8.6453086)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:694146670 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Casa do povo', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.2196617, -8.5830733, 'OPENSTREETMAP', 'OSM:WAY:694146670',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:694146670')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Casa do povo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2196617)) <= 0.00070
               AND ABS(v.longitude - (-8.5830733)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:703395309 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Montemor-o-Velho', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Remo', 'Complexo desportivo', NULL,
       40.1791739, -8.6399918, 'OPENSTREETMAP', 'OSM:WAY:703395309',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:703395309')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Montemor-o-Velho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1791739)) <= 0.00070
               AND ABS(v.longitude - (-8.6399918)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:375811651 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Santo Estevão', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.1777558, -8.5742433, 'OPENSTREETMAP', 'OSM:WAY:375811651',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:375811651')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Santo Estevão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1777558)) <= 0.00070
               AND ABS(v.longitude - (-8.5742433)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:125496099 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão CDC', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Basquetebol;Futsal', 'Complexo desportivo', NULL,
       40.2038037, -8.6463639, 'OPENSTREETMAP', 'OSM:WAY:125496099',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:125496099')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão CDC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2038037)) <= 0.00070
               AND ABS(v.longitude - (-8.6463639)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169297358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Montemor-o-Velho', 'Montemor-o-Velho', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.1807675, -8.6734598, 'OPENSTREETMAP', 'OSM:WAY:1169297358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169297358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Montemor-o-Velho'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Montemor-o-Velho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1807675)) <= 0.00070
               AND ABS(v.longitude - (-8.6734598)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:682782020 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Cabral Metelo', 'Oliveira do Hospital', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3581055, -7.8641715, 'OPENSTREETMAP', 'OSM:WAY:682782020',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:682782020')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Hospital'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Cabral Metelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3581055)) <= 0.00070
               AND ABS(v.longitude - (-7.8641715)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1240803392 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Luso Brasileiro', 'Oliveira do Hospital', 'Coimbra', NULL,
       'Futebol', 'Estádio', NULL,
       40.4585634, -7.8828081, 'OPENSTREETMAP', 'OSM:WAY:1240803392',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1240803392')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Hospital'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Luso Brasileiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4585634)) <= 0.00070
               AND ABS(v.longitude - (-7.8828081)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:605263051 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Santo António', 'Oliveira do Hospital', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3382908, -7.8756014, 'OPENSTREETMAP', 'OSM:WAY:605263051',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:605263051')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Hospital'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Santo António'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3382908)) <= 0.00070
               AND ABS(v.longitude - (-7.8756014)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1002434618 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Oliveira do Hospital', 'Oliveira do Hospital', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3630490, -7.8557083, 'OPENSTREETMAP', 'OSM:WAY:1002434618',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1002434618')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Hospital'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Oliveira do Hospital'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3630490)) <= 0.00070
               AND ABS(v.longitude - (-7.8557083)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:286351607 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Serafim Marques', 'Oliveira do Hospital', 'Coimbra', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.3512427, -7.8408177, 'OPENSTREETMAP', 'OSM:WAY:286351607',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:286351607')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Hospital'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Serafim Marques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3512427)) <= 0.00070
               AND ABS(v.longitude - (-7.8408177)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10854043087 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'RDSports', 'Oliveira do Hospital', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.3578801, -7.8577515, 'OPENSTREETMAP', 'OSM:NODE:10854043087',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10854043087')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Hospital'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('RDSports'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3578801)) <= 0.00070
               AND ABS(v.longitude - (-7.8577515)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10854043088 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Total FIT', 'Oliveira do Hospital', 'Coimbra', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.3581218, -7.8576742, 'OPENSTREETMAP', 'OSM:NODE:10854043088',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10854043088')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Hospital'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Total FIT'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3581218)) <= 0.00070
               AND ABS(v.longitude - (-7.8576742)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1536853355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Carqueja', 'Pampilhosa da Serra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1295420, -7.8391057, 'OPENSTREETMAP', 'OSM:WAY:1536853355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1536853355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pampilhosa da Serra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Carqueja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1295420)) <= 0.00070
               AND ABS(v.longitude - (-7.8391057)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:314264069 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo', 'Pampilhosa da Serra', 'Coimbra', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       40.0460509, -7.9481854, 'OPENSTREETMAP', 'OSM:WAY:314264069',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:314264069')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pampilhosa da Serra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0460509)) <= 0.00070
               AND ABS(v.longitude - (-7.9481854)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:310767486 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo dos Lobatinhos', 'Pampilhosa da Serra', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0219813, -7.9258015, 'OPENSTREETMAP', 'OSM:WAY:310767486',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:310767486')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pampilhosa da Serra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo dos Lobatinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0219813)) <= 0.00070
               AND ABS(v.longitude - (-7.9258015)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:208913046 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Popular e Cultural de Chelo', 'Penacova', 'Coimbra', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.2542965, -8.2892607, 'OPENSTREETMAP', 'OSM:WAY:208913046',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:208913046')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penacova'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Popular e Cultural de Chelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2542965)) <= 0.00070
               AND ABS(v.longitude - (-8.2892607)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:406829355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Penela', 'Penela', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.0253342, -8.3855089, 'OPENSTREETMAP', 'OSM:WAY:406829355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:406829355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Penela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0253342)) <= 0.00070
               AND ABS(v.longitude - (-8.3855089)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7704585789 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Trek Fun TRail', 'Penela', 'Coimbra', NULL,
       'Ciclismo', 'Pista desportiva', NULL,
       39.9740029, -8.3279320, 'OPENSTREETMAP', 'OSM:NODE:7704585789',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7704585789')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Trek Fun TRail'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9740029)) <= 0.00070
               AND ABS(v.longitude - (-8.3279320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:242050598 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Carvalheira de Baixo', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0740986, -8.6072653, 'OPENSTREETMAP', 'OSM:WAY:242050598',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:242050598')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Carvalheira de Baixo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0740986)) <= 0.00070
               AND ABS(v.longitude - (-8.6072653)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:439146987 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Vinha da Rainha', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0561263, -8.7011801, 'OPENSTREETMAP', 'OSM:WAY:439146987',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:439146987')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Vinha da Rainha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0561263)) <= 0.00070
               AND ABS(v.longitude - (-8.7011801)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1010353044 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Sintético', 'Soure', 'Coimbra', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.1078789, -8.6352461, 'OPENSTREETMAP', 'OSM:WAY:1010353044',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1010353044')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Sintético'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1078789)) <= 0.00070
               AND ABS(v.longitude - (-8.6352461)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:438860385 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Samuel', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1155806, -8.7598363, 'OPENSTREETMAP', 'OSM:WAY:438860385',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:438860385')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Samuel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1155806)) <= 0.00070
               AND ABS(v.longitude - (-8.7598363)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:375811645 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Pinheiro Manso', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1430703, -8.5688982, 'OPENSTREETMAP', 'OSM:WAY:375811645',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:375811645')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Pinheiro Manso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1430703)) <= 0.00070
               AND ABS(v.longitude - (-8.5688982)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:438852918 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Doutor António Coelho Rodrigues', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0617346, -8.6287472, 'OPENSTREETMAP', 'OSM:WAY:438852918',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:438852918')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Doutor António Coelho Rodrigues'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0617346)) <= 0.00070
               AND ABS(v.longitude - (-8.6287472)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1428963627 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CF Ulmeirense', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1557995, -8.6370190, 'OPENSTREETMAP', 'OSM:WAY:1428963627',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1428963627')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CF Ulmeirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1557995)) <= 0.00070
               AND ABS(v.longitude - (-8.6370190)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1010105207 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo Casa do Povo de Vila Nova de Anços', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.1136986, -8.6359381, 'OPENSTREETMAP', 'OSM:WAY:1010105207',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1010105207')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo Casa do Povo de Vila Nova de Anços'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1136986)) <= 0.00070
               AND ABS(v.longitude - (-8.6359381)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:873231577 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vila Nova de Anços', 'Soure', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.1067942, -8.6350000, 'OPENSTREETMAP', 'OSM:WAY:873231577',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:873231577')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vila Nova de Anços'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.1067942)) <= 0.00070
               AND ABS(v.longitude - (-8.6350000)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:386957840 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Futebol', 'Soure', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0354083, -8.6443713, 'OPENSTREETMAP', 'OSM:WAY:386957840',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:386957840')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Soure'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0354083)) <= 0.00070
               AND ABS(v.longitude - (-8.6443713)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1324210903 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Meda de Mouros', 'Tábua', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2877928, -8.0166281, 'OPENSTREETMAP', 'OSM:WAY:1324210903',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1324210903')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tábua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Meda de Mouros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2877928)) <= 0.00070
               AND ABS(v.longitude - (-8.0166281)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3513199824 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro de Candosa', 'Tábua', 'Coimbra', NULL,
       'Tiro', 'Campo desportivo', NULL,
       40.3463006, -7.9821101, 'OPENSTREETMAP', 'OSM:NODE:3513199824',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3513199824')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tábua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro de Candosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3463006)) <= 0.00070
               AND ABS(v.longitude - (-7.9821101)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:340302905 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Tábua', 'Tábua', 'Coimbra', NULL,
       'Futebol', 'Estádio', NULL,
       40.3692160, -8.0231417, 'OPENSTREETMAP', 'OSM:WAY:340302905',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:340302905')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tábua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Tábua'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3692160)) <= 0.00070
               AND ABS(v.longitude - (-8.0231417)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:340302899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Tabuense', 'Tábua', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3631583, -8.0307246, 'OPENSTREETMAP', 'OSM:WAY:340302899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:340302899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tábua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Tabuense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3631583)) <= 0.00070
               AND ABS(v.longitude - (-8.0307246)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8085449507 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Tábua', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.3632840, -8.0293381, 'OPENSTREETMAP', 'OSM:NODE:8085449507',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8085449507')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tábua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3632840)) <= 0.00070
               AND ABS(v.longitude - (-8.0293381)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1314411110 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Futebol da Liga dos Amigos de Loureiro', 'Tábua', 'Coimbra', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.3515491, -7.9061576, 'OPENSTREETMAP', 'OSM:WAY:1314411110',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1314411110')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tábua'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Futebol da Liga dos Amigos de Loureiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3515491)) <= 0.00070
               AND ABS(v.longitude - (-7.9061576)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:829273178 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol, do Skate park', 'Vila Nova de Poiares', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2107654, -8.2524243, 'OPENSTREETMAP', 'OSM:WAY:829273178',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:829273178')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Poiares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol, do Skate park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2107654)) <= 0.00070
               AND ABS(v.longitude - (-8.2524243)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:844991350 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Ercasol', 'Vila Nova de Poiares', 'Coimbra', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2400357, -8.2566151, 'OPENSTREETMAP', 'OSM:WAY:844991350',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:844991350')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Poiares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Ercasol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2400357)) <= 0.00070
               AND ABS(v.longitude - (-8.2566151)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:469224585 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Piscinas da Fraga', 'Vila Nova de Poiares', 'Coimbra', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.2182900, -8.2204671, 'OPENSTREETMAP', 'OSM:WAY:469224585',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:469224585')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Poiares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Piscinas da Fraga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2182900)) <= 0.00070
               AND ABS(v.longitude - (-8.2204671)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:569472317 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo de Vila Nova Poiares', 'Vila Nova de Poiares', 'Coimbra', NULL,
       'Karting', 'Estádio', NULL,
       40.2121040, -8.2398477, 'OPENSTREETMAP', 'OSM:WAY:569472317',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:569472317')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Poiares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo de Vila Nova Poiares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2121040)) <= 0.00070
               AND ABS(v.longitude - (-8.2398477)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6022776085 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Radical', 'Vila Nova de Poiares', 'Coimbra', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.2109813, -8.2525335, 'OPENSTREETMAP', 'OSM:NODE:6022776085',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6022776085')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Poiares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2109813)) <= 0.00070
               AND ABS(v.longitude - (-8.2525335)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:534191970 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Vila Nova de Poiares', 'Coimbra', NULL,
       'Natação', 'Piscina', NULL,
       40.2117411, -8.2560505, 'OPENSTREETMAP', 'OSM:WAY:534191970',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:534191970')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Poiares'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2117411)) <= 0.00070
               AND ABS(v.longitude - (-8.2560505)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
