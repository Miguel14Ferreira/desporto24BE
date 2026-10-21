-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:238142796 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Golfe de Amarante', 'Amarante', 'Porto', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.2568860, -8.1132356, 'OPENSTREETMAP', 'OSM:WAY:238142796',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238142796')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Golfe de Amarante'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2568860)) <= 0.00070
               AND ABS(v.longitude - (-8.1132356)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:567746592 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Felgueiras', 'Felgueiras', 'Porto', NULL,
       'Natação', 'Campo desportivo', NULL,
       41.3594140, -8.2028036, 'OPENSTREETMAP', 'OSM:WAY:567746592',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:567746592')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Felgueiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3594140)) <= 0.00070
               AND ABS(v.longitude - (-8.2028036)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13914798468 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Focus Fitness', 'Gondomar', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1356020, -8.5325346, 'OPENSTREETMAP', 'OSM:NODE:13914798468',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13914798468')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Focus Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1356020)) <= 0.00070
               AND ABS(v.longitude - (-8.5325346)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11048279205 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'OMG Gondomar', 'Gondomar', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1425770, -8.5469217, 'OPENSTREETMAP', 'OSM:NODE:11048279205',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11048279205')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('OMG Gondomar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1425770)) <= 0.00070
               AND ABS(v.longitude - (-8.5469217)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:376667661 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Gens', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1186369, -8.4831337, 'OPENSTREETMAP', 'OSM:WAY:376667661',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:376667661')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Gens'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1186369)) <= 0.00070
               AND ABS(v.longitude - (-8.4831337)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:332348145 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Rio Tinto', 'Gondomar', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1786161, -8.5607026, 'OPENSTREETMAP', 'OSM:WAY:332348145',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:332348145')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Rio Tinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1786161)) <= 0.00070
               AND ABS(v.longitude - (-8.5607026)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13728580402 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic - Rio Tinto', 'Gondomar', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1708049, -8.5529245, 'OPENSTREETMAP', 'OSM:NODE:13728580402',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13728580402')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic - Rio Tinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1708049)) <= 0.00070
               AND ABS(v.longitude - (-8.5529245)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151547991 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Atlética de Águas Santas', 'Maia', 'Porto', NULL,
       'Andebol', 'Complexo desportivo', NULL,
       41.2060483, -8.5658051, 'OPENSTREETMAP', 'OSM:WAY:151547991',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151547991')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Atlética de Águas Santas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2060483)) <= 0.00070
               AND ABS(v.longitude - (-8.5658051)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9484242348 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be Fit', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2477821, -8.6210907, 'OPENSTREETMAP', 'OSM:NODE:9484242348',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9484242348')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2477821)) <= 0.00070
               AND ABS(v.longitude - (-8.6210907)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1218860728 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Body Space', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2671001, -8.6168895, 'OPENSTREETMAP', 'OSM:WAY:1218860728',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1218860728')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Body Space'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2671001)) <= 0.00070
               AND ABS(v.longitude - (-8.6168895)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9089448153 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CrossFit 4475', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2631347, -8.6444595, 'OPENSTREETMAP', 'OSM:NODE:9089448153',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9089448153')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CrossFit 4475'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2631347)) <= 0.00070
               AND ABS(v.longitude - (-8.6444595)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8406342609 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'FitnessUP São João', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1827765, -8.5957869, 'OPENSTREETMAP', 'OSM:NODE:8406342609',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8406342609')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('FitnessUP São João'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1827765)) <= 0.00070
               AND ABS(v.longitude - (-8.5957869)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13914798469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Focus Fitness', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2096451, -8.5716298, 'OPENSTREETMAP', 'OSM:NODE:13914798469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13914798469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Focus Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2096451)) <= 0.00070
               AND ABS(v.longitude - (-8.5716298)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305248356 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lemon Fit', 'Maia', 'Porto', NULL,
       'Ginástica;Natação;Padel', 'Complexo desportivo', NULL,
       41.2331798, -8.6266117, 'OPENSTREETMAP', 'OSM:WAY:305248356',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305248356')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lemon Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2331798)) <= 0.00070
               AND ABS(v.longitude - (-8.6266117)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6590032252 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Maiafit', 'Maia', 'Porto', NULL,
       'Natação;Ginásio', 'Ginásio', NULL,
       41.2244726, -8.6125992, 'OPENSTREETMAP', 'OSM:NODE:6590032252',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6590032252')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Maiafit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2244726)) <= 0.00070
               AND ABS(v.longitude - (-8.6125992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:328471721 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos de Milheirós', 'Maia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2249818, -8.5767603, 'OPENSTREETMAP', 'OSM:WAY:328471721',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:328471721')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos de Milheirós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2249818)) <= 0.00070
               AND ABS(v.longitude - (-8.5767603)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:251851482 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal de Águas Santas III - Formigueiro', 'Maia', 'Porto', NULL,
       'Basquetebol;Voleibol;Andebol', 'Complexo desportivo', NULL,
       41.2040738, -8.5877919, 'OPENSTREETMAP', 'OSM:WAY:251851482',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:251851482')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal de Águas Santas III - Formigueiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2040738)) <= 0.00070
               AND ABS(v.longitude - (-8.5877919)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1460376704 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Maia', 'Porto', NULL,
       'Natação;Ginásio', 'Ginásio', NULL,
       41.2375225, -8.5973932, 'OPENSTREETMAP', 'OSM:WAY:1460376704',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1460376704')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2375225)) <= 0.00070
               AND ABS(v.longitude - (-8.5973932)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1346668265 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Light', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2356751, -8.6261230, 'OPENSTREETMAP', 'OSM:WAY:1346668265',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1346668265')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Light'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2356751)) <= 0.00070
               AND ABS(v.longitude - (-8.6261230)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7275494954 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'TeamZStudio', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2253175, -8.6103480, 'OPENSTREETMAP', 'OSM:NODE:7275494954',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7275494954')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('TeamZStudio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2253175)) <= 0.00070
               AND ABS(v.longitude - (-8.6103480)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13056169924 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Strength Lab Gym', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2570139, -8.6482328, 'OPENSTREETMAP', 'OSM:NODE:13056169924',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13056169924')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Strength Lab Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2570139)) <= 0.00070
               AND ABS(v.longitude - (-8.6482328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11327754541 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga Marga Studio', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2654019, -8.6186865, 'OPENSTREETMAP', 'OSM:NODE:11327754541',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11327754541')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga Marga Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2654019)) <= 0.00070
               AND ABS(v.longitude - (-8.6186865)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:544254417 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'AmazinGym', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1890357, -8.6345719, 'OPENSTREETMAP', 'OSM:WAY:544254417',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:544254417')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('AmazinGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1890357)) <= 0.00070
               AND ABS(v.longitude - (-8.6345719)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:958230266 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Arena Matosinhos', 'Matosinhos', 'Porto', NULL,
       'Boxe', 'Complexo desportivo', NULL,
       41.1789126, -8.6881743, 'OPENSTREETMAP', 'OSM:WAY:958230266',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:958230266')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Arena Matosinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1789126)) <= 0.00070
               AND ABS(v.longitude - (-8.6881743)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:417307079 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Desportos e Congressos', 'Matosinhos', 'Porto', NULL,
       'Futebol;Basquetebol;Voleibol;Ginástica;Ciclismo', 'Complexo desportivo', NULL,
       41.1847538, -8.6687066, 'OPENSTREETMAP', 'OSM:WAY:417307079',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:417307079')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Desportos e Congressos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1847538)) <= 0.00070
               AND ABS(v.longitude - (-8.6687066)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:552226243 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Citygolf', 'Matosinhos', 'Porto', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.1849480, -8.6392862, 'OPENSTREETMAP', 'OSM:WAY:552226243',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:552226243')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Citygolf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1849480)) <= 0.00070
               AND ABS(v.longitude - (-8.6392862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13914798466 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Focus Fitness', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1809168, -8.6918876, 'OPENSTREETMAP', 'OSM:NODE:13914798466',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13914798466')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Focus Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1809168)) <= 0.00070
               AND ABS(v.longitude - (-8.6918876)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13190872198 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Keep Fit', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1871790, -8.6453428, 'OPENSTREETMAP', 'OSM:NODE:13190872198',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13190872198')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Keep Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1871790)) <= 0.00070
               AND ABS(v.longitude - (-8.6453428)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13190872176 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Now Fitness Gym', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1879653, -8.6458776, 'OPENSTREETMAP', 'OSM:NODE:13190872176',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13190872176')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Now Fitness Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1879653)) <= 0.00070
               AND ABS(v.longitude - (-8.6458776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2268170462 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Académica de São Mamede', 'Matosinhos', 'Porto', NULL,
       'Ginástica;Voleibol;Ténis;Andebol', 'Complexo desportivo', NULL,
       41.1978717, -8.6162449, 'OPENSTREETMAP', 'OSM:NODE:2268170462',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2268170462')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Académica de São Mamede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1978717)) <= 0.00070
               AND ABS(v.longitude - (-8.6162449)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182519917 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal da Senhora da Hora', 'Matosinhos', 'Porto', NULL,
       'Futsal;Voleibol;Basquetebol', 'Complexo desportivo', NULL,
       41.1874163, -8.6567770, 'OPENSTREETMAP', 'OSM:WAY:182519917',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182519917')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal da Senhora da Hora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1874163)) <= 0.00070
               AND ABS(v.longitude - (-8.6567770)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182519914 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Senhora da Hora', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1842109, -8.6565802, 'OPENSTREETMAP', 'OSM:WAY:182519914',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182519914')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Senhora da Hora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1842109)) <= 0.00070
               AND ABS(v.longitude - (-8.6565802)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:101311936 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Marés', 'Matosinhos', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1928549, -8.7074645, 'OPENSTREETMAP', 'OSM:WAY:101311936',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:101311936')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Marés'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1928549)) <= 0.00070
               AND ABS(v.longitude - (-8.7074645)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1368481977 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1796909, -8.6562376, 'OPENSTREETMAP', 'OSM:WAY:1368481977',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1368481977')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1796909)) <= 0.00070
               AND ABS(v.longitude - (-8.6562376)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12803945601 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Stat Martial Arts Matosinhos Sul', 'Matosinhos', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1769350, -8.6863161, 'OPENSTREETMAP', 'OSM:NODE:12803945601',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12803945601')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Matosinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Stat Martial Arts Matosinhos Sul'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1769350)) <= 0.00070
               AND ABS(v.longitude - (-8.6863161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1272797036 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Verde', 'Paredes', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2091876, -8.3243972, 'OPENSTREETMAP', 'OSM:WAY:1272797036',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1272797036')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Verde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2091876)) <= 0.00070
               AND ABS(v.longitude - (-8.3243972)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1101759115 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Gondalães', 'Paredes', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2333060, -8.3281492, 'OPENSTREETMAP', 'OSM:WAY:1101759115',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1101759115')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Paredes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Gondalães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2333060)) <= 0.00070
               AND ABS(v.longitude - (-8.3281492)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:224728270 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Penafiel', 'Penafiel', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2054193, -8.2741464, 'OPENSTREETMAP', 'OSM:WAY:224728270',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:224728270')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penafiel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Penafiel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2054193)) <= 0.00070
               AND ABS(v.longitude - (-8.2741464)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1226195375 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva e Recreativa da Pasteleira', 'Porto', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1535700, -8.6639898, 'OPENSTREETMAP', 'OSM:WAY:1226195375',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1226195375')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva e Recreativa da Pasteleira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1535700)) <= 0.00070
               AND ABS(v.longitude - (-8.6639898)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:306725116 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Constituição', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1632207, -8.6076647, 'OPENSTREETMAP', 'OSM:WAY:306725116',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:306725116')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Constituição'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1632207)) <= 0.00070
               AND ABS(v.longitude - (-8.6076647)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:178413105 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural e Desportivo dos Trabalhadores da Câmara Municipal do Porto', 'Porto', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1600801, -8.6151518, 'OPENSTREETMAP', 'OSM:WAY:178413105',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:178413105')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural e Desportivo dos Trabalhadores da Câmara Municipal do Porto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1600801)) <= 0.00070
               AND ABS(v.longitude - (-8.6151518)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:132865132 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Fluvial Portuense - Complexo de Piscinas', 'Porto', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1505458, -8.6548463, 'OPENSTREETMAP', 'OSM:WAY:132865132',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:132865132')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Fluvial Portuense - Complexo de Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1505458)) <= 0.00070
               AND ABS(v.longitude - (-8.6548463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:835324649 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Campanhã', 'Porto', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1587188, -8.5724963, 'OPENSTREETMAP', 'OSM:WAY:835324649',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:835324649')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Campanhã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1587188)) <= 0.00070
               AND ABS(v.longitude - (-8.5724963)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:35744588 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo do Monte Aventino (Federação Portuguesa de Tenis)', 'Porto', 'Porto', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       41.1612867, -8.5896247, 'OPENSTREETMAP', 'OSM:WAY:35744588',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:35744588')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo do Monte Aventino (Federação Portuguesa de Tenis)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1612867)) <= 0.00070
               AND ABS(v.longitude - (-8.5896247)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13030337970 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1668369, -8.6441323, 'OPENSTREETMAP', 'OSM:NODE:13030337970',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13030337970')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1668369)) <= 0.00070
               AND ABS(v.longitude - (-8.6441323)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8289938836 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1510732, -8.6709074, 'OPENSTREETMAP', 'OSM:NODE:8289938836',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8289938836')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1510732)) <= 0.00070
               AND ABS(v.longitude - (-8.6709074)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:547707192 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Dragão', 'Porto', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1618410, -8.5837180, 'OPENSTREETMAP', 'OSM:WAY:547707192',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:547707192')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Dragão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1618410)) <= 0.00070
               AND ABS(v.longitude - (-8.5837180)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2893281180 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1513969, -8.6109289, 'OPENSTREETMAP', 'OSM:NODE:2893281180',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2893281180')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1513969)) <= 0.00070
               AND ABS(v.longitude - (-8.6109289)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10604618627 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1604088, -8.5977376, 'OPENSTREETMAP', 'OSM:NODE:10604618627',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10604618627')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1604088)) <= 0.00070
               AND ABS(v.longitude - (-8.5977376)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10719220229 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness up', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1826080, -8.6082585, 'OPENSTREETMAP', 'OSM:NODE:10719220229',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10719220229')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1826080)) <= 0.00070
               AND ABS(v.longitude - (-8.6082585)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12474468236 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP Antas', 'Porto', 'Porto', NULL,
       'Halterofilismo;Ginásio;Ciclismo;Dança Desportiva', 'Ginásio', NULL,
       41.1639500, -8.5974675, 'OPENSTREETMAP', 'OSM:NODE:12474468236',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12474468236')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP Antas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1639500)) <= 0.00070
               AND ABS(v.longitude - (-8.5974675)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:165546651 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up Foz', 'Porto', 'Porto', NULL,
       'Natação', 'Ginásio', NULL,
       41.1545745, -8.6653222, 'OPENSTREETMAP', 'OSM:WAY:165546651',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:165546651')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up Foz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1545745)) <= 0.00070
               AND ABS(v.longitude - (-8.6653222)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11059019047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fox Gym', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1590695, -8.6004446, 'OPENSTREETMAP', 'OSM:NODE:11059019047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11059019047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fox Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1590695)) <= 0.00070
               AND ABS(v.longitude - (-8.6004446)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13081938886 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Liga Portugal Lab', 'Porto', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1735445, -8.6391009, 'OPENSTREETMAP', 'OSM:NODE:13081938886',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13081938886')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Liga Portugal Lab'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1735445)) <= 0.00070
               AND ABS(v.longitude - (-8.6391009)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4464733890 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ra Sports & Fitness', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1675066, -8.6669561, 'OPENSTREETMAP', 'OSM:NODE:4464733890',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4464733890')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ra Sports & Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1675066)) <= 0.00070
               AND ABS(v.longitude - (-8.6669561)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9814405564 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'São Rock Climbing', 'Porto', 'Porto', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       41.1544425, -8.5872762, 'OPENSTREETMAP', 'OSM:NODE:9814405564',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9814405564')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('São Rock Climbing'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1544425)) <= 0.00070
               AND ABS(v.longitude - (-8.5872762)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12684651264 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Porto', 'Porto', NULL,
       'Natação;Ginásio', 'Ginásio', NULL,
       41.1605829, -8.5839596, 'OPENSTREETMAP', 'OSM:NODE:12684651264',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12684651264')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1605829)) <= 0.00070
               AND ABS(v.longitude - (-8.5839596)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:465437600 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Porto', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1624109, -8.6759527, 'OPENSTREETMAP', 'OSM:WAY:465437600',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:465437600')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1624109)) <= 0.00070
               AND ABS(v.longitude - (-8.6759527)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12435650695 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vital Fitness Studio Antas', 'Porto', 'Porto', NULL,
       'Ginásio;Halterofilismo', 'Ginásio', NULL,
       41.1642542, -8.5883716, 'OPENSTREETMAP', 'OSM:NODE:12435650695',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12435650695')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vital Fitness Studio Antas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1642542)) <= 0.00070
               AND ABS(v.longitude - (-8.5883716)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12435651696 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vital Fitness Studio Aviz', 'Porto', 'Porto', NULL,
       'Ginásio;Halterofilismo;Ginástica', 'Ginásio', NULL,
       41.1636124, -8.6674064, 'OPENSTREETMAP', 'OSM:NODE:12435651696',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12435651696')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vital Fitness Studio Aviz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1636124)) <= 0.00070
               AND ABS(v.longitude - (-8.6674064)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:310891826 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vitalis Park', 'Porto', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1633643, -8.6076407, 'OPENSTREETMAP', 'OSM:WAY:310891826',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:310891826')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vitalis Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1633643)) <= 0.00070
               AND ABS(v.longitude - (-8.6076407)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:296863676 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal da Póvoa de Varzim', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol;Atletismo;Corrida;Ginásio', 'Estádio', NULL,
       41.4009347, -8.7559326, 'OPENSTREETMAP', 'OSM:WAY:296863676',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:296863676')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal da Póvoa de Varzim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4009347)) <= 0.00070
               AND ABS(v.longitude - (-8.7559326)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240180470 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estela Golf Club', 'Póvoa de Varzim', 'Porto', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.4575973, -8.7759387, 'OPENSTREETMAP', 'OSM:WAY:240180470',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240180470')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estela Golf Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4575973)) <= 0.00070
               AND ABS(v.longitude - (-8.7759387)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13817421902 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP Póvoa de Varzim', 'Póvoa de Varzim', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3776835, -8.7573208, 'OPENSTREETMAP', 'OSM:NODE:13817421902',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13817421902')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP Póvoa de Varzim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3776835)) <= 0.00070
               AND ABS(v.longitude - (-8.7573208)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13504609151 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mais Padel', 'Póvoa de Varzim', 'Porto', NULL,
       'Padel', 'Complexo desportivo', NULL,
       41.3873637, -8.7732523, 'OPENSTREETMAP', 'OSM:NODE:13504609151',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13504609151')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mais Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3873637)) <= 0.00070
               AND ABS(v.longitude - (-8.7732523)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13656281274 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'A-Z Padel Santo Tirso', 'Santo Tirso', 'Porto', NULL,
       'Padel', 'Ginásio', NULL,
       41.3378461, -8.4822259, 'OPENSTREETMAP', 'OSM:NODE:13656281274',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13656281274')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('A-Z Padel Santo Tirso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3378461)) <= 0.00070
               AND ABS(v.longitude - (-8.4822259)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13656281275 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BreaK Fitness Club', 'Santo Tirso', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3376528, -8.4821696, 'OPENSTREETMAP', 'OSM:NODE:13656281275',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13656281275')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BreaK Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3376528)) <= 0.00070
               AND ABS(v.longitude - (-8.4821696)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103430790 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Abel Alves de Figueiredo', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.3437567, -8.4822827, 'OPENSTREETMAP', 'OSM:WAY:103430790',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103430790')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Abel Alves de Figueiredo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3437567)) <= 0.00070
               AND ABS(v.longitude - (-8.4822827)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13323782652 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Focus Fitness', 'Santo Tirso', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3362555, -8.4747481, 'OPENSTREETMAP', 'OSM:NODE:13323782652',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13323782652')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Focus Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3362555)) <= 0.00070
               AND ABS(v.longitude - (-8.4747481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:262167095 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Santo Tirso', 'Santo Tirso', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3387177, -8.4744197, 'OPENSTREETMAP', 'OSM:WAY:262167095',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:262167095')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Santo Tirso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3387177)) <= 0.00070
               AND ABS(v.longitude - (-8.4744197)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1515459365 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Peak', 'Santo Tirso', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3412313, -8.4822294, 'OPENSTREETMAP', 'OSM:WAY:1515459365',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1515459365')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Peak'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3412313)) <= 0.00070
               AND ABS(v.longitude - (-8.4822294)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:446515183 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vale Pisão Golfe', 'Santo Tirso', 'Porto', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.2770608, -8.5046358, 'OPENSTREETMAP', 'OSM:WAY:446515183',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:446515183')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vale Pisão Golfe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2770608)) <= 0.00070
               AND ABS(v.longitude - (-8.5046358)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1243847548 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Aquaplace', 'Trofa', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3437381, -8.5507304, 'OPENSTREETMAP', 'OSM:NODE:1243847548',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1243847548')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Aquaplace'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3437381)) <= 0.00070
               AND ABS(v.longitude - (-8.5507304)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12344024267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GYMSTAR Trofa', 'Trofa', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3404337, -8.5574333, 'OPENSTREETMAP', 'OSM:NODE:12344024267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12344024267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GYMSTAR Trofa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3404337)) <= 0.00070
               AND ABS(v.longitude - (-8.5574333)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1738112483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Valongo', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2151917, -8.5515734, 'OPENSTREETMAP', 'OSM:NODE:1738112483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1738112483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2151917)) <= 0.00070
               AND ABS(v.longitude - (-8.5515734)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9655594088 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory', 'Valongo', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1835387, -8.4816274, 'OPENSTREETMAP', 'OSM:NODE:9655594088',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9655594088')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1835387)) <= 0.00070
               AND ABS(v.longitude - (-8.4816274)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1223203649 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Go Gym Valongo', 'Valongo', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1984002, -8.5050238, 'OPENSTREETMAP', 'OSM:WAY:1223203649',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1223203649')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Go Gym Valongo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1984002)) <= 0.00070
               AND ABS(v.longitude - (-8.5050238)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13116184811 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'OneSoul', 'Vila do Conde', 'Porto', NULL,
       'Escalada;Ginásio', 'Ginásio', NULL,
       41.3660438, -8.7518151, 'OPENSTREETMAP', 'OSM:NODE:13116184811',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13116184811')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('OneSoul'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3660438)) <= 0.00070
               AND ABS(v.longitude - (-8.7518151)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:480198161 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Best Club', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0866959, -8.6262140, 'OPENSTREETMAP', 'OSM:WAY:480198161',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:480198161')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Best Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0866959)) <= 0.00070
               AND ABS(v.longitude - (-8.6262140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108565624 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Golfe de Miramar', 'Vila Nova de Gaia', 'Porto', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.0634407, -8.6549679, 'OPENSTREETMAP', 'OSM:WAY:108565624',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108565624')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Golfe de Miramar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0634407)) <= 0.00070
               AND ABS(v.longitude - (-8.6549679)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:322959888 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Treinos e Formação Desportiva Jorge Costa', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.0669792, -8.5168118, 'OPENSTREETMAP', 'OSM:WAY:322959888',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:322959888')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Treinos e Formação Desportiva Jorge Costa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0669792)) <= 0.00070
               AND ABS(v.longitude - (-8.5168118)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2810750807 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Náutico Eng. Edgar Cardoso', 'Vila Nova de Gaia', 'Porto', NULL,
       'Canoagem', 'Complexo desportivo', NULL,
       41.1368077, -8.5957212, 'OPENSTREETMAP', 'OSM:NODE:2810750807',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2810750807')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Náutico Eng. Edgar Cardoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1368077)) <= 0.00070
               AND ABS(v.longitude - (-8.5957212)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1204309299 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Champion Criterion', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1149652, -8.5827181, 'OPENSTREETMAP', 'OSM:WAY:1204309299',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1204309299')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Champion Criterion'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1149652)) <= 0.00070
               AND ABS(v.longitude - (-8.5827181)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152783612 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Grijó', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.0265094, -8.5764786, 'OPENSTREETMAP', 'OSM:WAY:1152783612',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152783612')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Grijó'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0265094)) <= 0.00070
               AND ABS(v.longitude - (-8.5764786)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7645648854 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fit Canidelo', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1302443, -8.6464946, 'OPENSTREETMAP', 'OSM:NODE:7645648854',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7645648854')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fit Canidelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1302443)) <= 0.00070
               AND ABS(v.longitude - (-8.6464946)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12889064072 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0915413, -8.5971373, 'OPENSTREETMAP', 'OSM:NODE:12889064072',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12889064072')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0915413)) <= 0.00070
               AND ABS(v.longitude - (-8.5971373)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8745675586 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fit Planet', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0670373, -8.6273019, 'OPENSTREETMAP', 'OSM:NODE:8745675586',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8745675586')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fit Planet'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0670373)) <= 0.00070
               AND ABS(v.longitude - (-8.6273019)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:486418376 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gaiagym', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1180150, -8.6096638, 'OPENSTREETMAP', 'OSM:WAY:486418376',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:486418376')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gaiagym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1180150)) <= 0.00070
               AND ABS(v.longitude - (-8.6096638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3961386144 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GayaDance Factory', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1184728, -8.5997826, 'OPENSTREETMAP', 'OSM:NODE:3961386144',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3961386144')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GayaDance Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1184728)) <= 0.00070
               AND ABS(v.longitude - (-8.5997826)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11390617156 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio VIV', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0656300, -8.5751650, 'OPENSTREETMAP', 'OSM:NODE:11390617156',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11390617156')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio VIV'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0656300)) <= 0.00070
               AND ABS(v.longitude - (-8.5751650)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99116334 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Golfe da Quinta do Fojo', 'Vila Nova de Gaia', 'Porto', NULL,
       'Golfe', 'Campo de golfe', NULL,
       41.1237003, -8.6383088, 'OPENSTREETMAP', 'OSM:WAY:99116334',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99116334')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Golfe da Quinta do Fojo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1237003)) <= 0.00070
               AND ABS(v.longitude - (-8.6383088)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11406499577 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grifo Gym', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1233465, -8.6146599, 'OPENSTREETMAP', 'OSM:NODE:11406499577',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11406499577')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grifo Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1233465)) <= 0.00070
               AND ABS(v.longitude - (-8.6146599)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1204309300 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Hybrid Training Center', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1147776, -8.5826985, 'OPENSTREETMAP', 'OSM:WAY:1204309300',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1204309300')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Hybrid Training Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1147776)) <= 0.00070
               AND ABS(v.longitude - (-8.5826985)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13944193085 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal Fernando Gomes', 'Vila Nova de Gaia', 'Porto', NULL,
       'Basquetebol;Andebol;Futsal;Voleibol', 'Complexo desportivo', NULL,
       41.1308549, -8.6128285, 'OPENSTREETMAP', 'OSM:NODE:13944193085',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13944193085')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal Fernando Gomes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1308549)) <= 0.00070
               AND ABS(v.longitude - (-8.6128285)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:560200452 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Maravedi', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1272267, -8.6142433, 'OPENSTREETMAP', 'OSM:WAY:560200452',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:560200452')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Maravedi'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1272267)) <= 0.00070
               AND ABS(v.longitude - (-8.6142433)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2014980583 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Arrábida', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1427417, -8.6374688, 'OPENSTREETMAP', 'OSM:NODE:2014980583',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2014980583')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Arrábida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1427417)) <= 0.00070
               AND ABS(v.longitude - (-8.6374688)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1412247282 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Smart Fitness', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1191701, -8.6196818, 'OPENSTREETMAP', 'OSM:WAY:1412247282',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1412247282')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Smart Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1191701)) <= 0.00070
               AND ABS(v.longitude - (-8.6196818)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12973230643 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Zone Box', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1112675, -8.6212291, 'OPENSTREETMAP', 'OSM:NODE:12973230643',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12973230643')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Zone Box'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1112675)) <= 0.00070
               AND ABS(v.longitude - (-8.6212291)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12211876533 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Zone Climb', 'Vila Nova de Gaia', 'Porto', NULL,
       'Escalada', 'Ginásio', NULL,
       41.1112664, -8.6212700, 'OPENSTREETMAP', 'OSM:NODE:12211876533',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12211876533')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Zone Climb'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1112664)) <= 0.00070
               AND ABS(v.longitude - (-8.6212700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12211876534 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Zone Murus', 'Vila Nova de Gaia', 'Porto', NULL,
       'Escalada', 'Ginásio', NULL,
       41.1185469, -8.6000273, 'OPENSTREETMAP', 'OSM:NODE:12211876534',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12211876534')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Zone Murus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1185469)) <= 0.00070
               AND ABS(v.longitude - (-8.6000273)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825803066 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Amarante Futebol Clube', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2821088, -8.0757979, 'OPENSTREETMAP', 'OSM:WAY:825803066',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825803066')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Amarante Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2821088)) <= 0.00070
               AND ABS(v.longitude - (-8.0757979)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:308102254 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bustelo Futebol Clube', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2392730, -7.9948790, 'OPENSTREETMAP', 'OSM:WAY:308102254',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:308102254')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bustelo Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2392730)) <= 0.00070
               AND ABS(v.longitude - (-7.9948790)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:257183630 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Pedreira', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2760975, -8.0397433, 'OPENSTREETMAP', 'OSM:WAY:257183630',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:257183630')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Pedreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2760975)) <= 0.00070
               AND ABS(v.longitude - (-8.0397433)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:170166510 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Associação Cultural e Recriativa de Figueiró (Santiago)', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2917516, -8.1550195, 'OPENSTREETMAP', 'OSM:WAY:170166510',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:170166510')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Associação Cultural e Recriativa de Figueiró (Santiago)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2917516)) <= 0.00070
               AND ABS(v.longitude - (-8.1550195)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:249210497 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Madalena', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2668109, -8.0635165, 'OPENSTREETMAP', 'OSM:WAY:249210497',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:249210497')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Madalena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2668109)) <= 0.00070
               AND ABS(v.longitude - (-8.0635165)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1310884527 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Aboim', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3225900, -8.0592643, 'OPENSTREETMAP', 'OSM:WAY:1310884527',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1310884527')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Aboim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3225900)) <= 0.00070
               AND ABS(v.longitude - (-8.0592643)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:889963928 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Santa Cristina', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2926595, -8.1640464, 'OPENSTREETMAP', 'OSM:WAY:889963928',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:889963928')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Santa Cristina'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2926595)) <= 0.00070
               AND ABS(v.longitude - (-8.1640464)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:409850822 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vila Chã do Marão', 'Amarante', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2961177, -8.0255905, 'OPENSTREETMAP', 'OSM:WAY:409850822',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:409850822')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vila Chã do Marão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2961177)) <= 0.00070
               AND ABS(v.longitude - (-8.0255905)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:253130124 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Freixo de Cima', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3136240, -8.1304605, 'OPENSTREETMAP', 'OSM:WAY:253130124',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:253130124')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Freixo de Cima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3136240)) <= 0.00070
               AND ABS(v.longitude - (-8.1304605)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:309237074 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Dona Amália Mota', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2389291, -8.0922666, 'OPENSTREETMAP', 'OSM:WAY:309237074',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:309237074')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Dona Amália Mota'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2389291)) <= 0.00070
               AND ABS(v.longitude - (-8.0922666)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:887287032 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Ramiro Vieira', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3075313, -8.0358086, 'OPENSTREETMAP', 'OSM:WAY:887287032',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:887287032')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Ramiro Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3075313)) <= 0.00070
               AND ABS(v.longitude - (-8.0358086)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:887286993 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Jogos Padre Joaquim', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3137602, -8.0365988, 'OPENSTREETMAP', 'OSM:WAY:887286993',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:887286993')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de Jogos Padre Joaquim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3137602)) <= 0.00070
               AND ABS(v.longitude - (-8.0365988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:257177836 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Toneca', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2674786, -8.0477978, 'OPENSTREETMAP', 'OSM:WAY:257177836',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:257177836')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Toneca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2674786)) <= 0.00070
               AND ABS(v.longitude - (-8.0477978)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154364787 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Amarante', 'Amarante', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2821351, -8.0758968, 'OPENSTREETMAP', 'OSM:WAY:154364787',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154364787')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Amarante'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2821351)) <= 0.00070
               AND ABS(v.longitude - (-8.0758968)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:228623476 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Vila Meã', 'Amarante', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2524895, -8.1710525, 'OPENSTREETMAP', 'OSM:WAY:228623476',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:228623476')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Vila Meã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2524895)) <= 0.00070
               AND ABS(v.longitude - (-8.1710525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1942500760 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Madalena', 'Amarante', 'Porto', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       41.2678765, -8.0726950, 'OPENSTREETMAP', 'OSM:NODE:1942500760',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1942500760')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Madalena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2678765)) <= 0.00070
               AND ABS(v.longitude - (-8.0726950)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1265686120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Mancelos', 'Amarante', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2808638, -8.1679508, 'OPENSTREETMAP', 'OSM:WAY:1265686120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1265686120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Mancelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2808638)) <= 0.00070
               AND ABS(v.longitude - (-8.1679508)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1284240999 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vila Meã', 'Amarante', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2528818, -8.1687347, 'OPENSTREETMAP', 'OSM:WAY:1284240999',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1284240999')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vila Meã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2528818)) <= 0.00070
               AND ABS(v.longitude - (-8.1687347)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237494626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Colégio', 'Amarante', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2700056, -8.0696737, 'OPENSTREETMAP', 'OSM:WAY:237494626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237494626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Colégio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2700056)) <= 0.00070
               AND ABS(v.longitude - (-8.0696737)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237495102 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Amarante', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2665350, -8.0814131, 'OPENSTREETMAP', 'OSM:WAY:237495102',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237495102')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2665350)) <= 0.00070
               AND ABS(v.longitude - (-8.0814131)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154369020 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Amarante', 'Amarante', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2664973, -8.0816045, 'OPENSTREETMAP', 'OSM:WAY:154369020',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154369020')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amarante'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Amarante'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2664973)) <= 0.00070
               AND ABS(v.longitude - (-8.0816045)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:879308490 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação de Desenvolvimento do Grilo', 'Baião', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1369597, -8.0726405, 'OPENSTREETMAP', 'OSM:WAY:879308490',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:879308490')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Baião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação de Desenvolvimento do Grilo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1369597)) <= 0.00070
               AND ABS(v.longitude - (-8.0726405)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127883557 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Futebol Clube de Frende', 'Baião', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1281643, -7.9234270, 'OPENSTREETMAP', 'OSM:WAY:127883557',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127883557')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Baião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Futebol Clube de Frende'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1281643)) <= 0.00070
               AND ABS(v.longitude - (-7.9234270)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127883551 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Prenhó - AD Baião', 'Baião', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1686801, -8.0267904, 'OPENSTREETMAP', 'OSM:WAY:127883551',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127883551')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Baião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Prenhó - AD Baião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1686801)) <= 0.00070
               AND ABS(v.longitude - (-8.0267904)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:451797876 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Baião', 'Baião', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1668746, -8.0273890, 'OPENSTREETMAP', 'OSM:WAY:451797876',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:451797876')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Baião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Baião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1668746)) <= 0.00070
               AND ABS(v.longitude - (-8.0273890)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14206578705 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be-Fit', 'Felgueiras', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3533909, -8.1938337, 'OPENSTREETMAP', 'OSM:NODE:14206578705',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14206578705')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be-Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3533909)) <= 0.00070
               AND ABS(v.longitude - (-8.1938337)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:141361204 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Associação Cultural e Recreativa de Jugueiros', 'Felgueiras', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.4032510, -8.2217206, 'OPENSTREETMAP', 'OSM:WAY:141361204',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:141361204')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Associação Cultural e Recreativa de Jugueiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.4032510)) <= 0.00070
               AND ABS(v.longitude - (-8.2217206)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:159146239 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos do F.C.Lixa', 'Felgueiras', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.3298522, -8.1437511, 'OPENSTREETMAP', 'OSM:WAY:159146239',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:159146239')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos do F.C.Lixa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3298522)) <= 0.00070
               AND ABS(v.longitude - (-8.1437511)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:159146199 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético da Lixa', 'Felgueiras', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.3297975, -8.1449988, 'OPENSTREETMAP', 'OSM:WAY:159146199',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:159146199')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético da Lixa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3297975)) <= 0.00070
               AND ABS(v.longitude - (-8.1449988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:553154832 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Doutor Machado de Matos', 'Felgueiras', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.3559686, -8.1910097, 'OPENSTREETMAP', 'OSM:WAY:553154832',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:553154832')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Doutor Machado de Matos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3559686)) <= 0.00070
               AND ABS(v.longitude - (-8.1910097)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:159145816 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Senhor do Amparo', 'Felgueiras', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.3306616, -8.1499555, 'OPENSTREETMAP', 'OSM:WAY:159145816',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:159145816')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Senhor do Amparo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3306616)) <= 0.00070
               AND ABS(v.longitude - (-8.1499555)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1002433171 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Felgueiras 1932', 'Felgueiras', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3560549, -8.1906345, 'OPENSTREETMAP', 'OSM:WAY:1002433171',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1002433171')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Felgueiras 1932'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3560549)) <= 0.00070
               AND ABS(v.longitude - (-8.1906345)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:677024375 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Idães', 'Felgueiras', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3273381, -8.2603037, 'OPENSTREETMAP', 'OSM:WAY:677024375',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:677024375')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Idães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3273381)) <= 0.00070
               AND ABS(v.longitude - (-8.2603037)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:170364193 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas da Lixa', 'Felgueiras', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.3271944, -8.1444709, 'OPENSTREETMAP', 'OSM:WAY:170364193',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:170364193')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Felgueiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas da Lixa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3271944)) <= 0.00070
               AND ABS(v.longitude - (-8.1444709)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:262258881 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva de São Pedro da Cova', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1597375, -8.5054046, 'OPENSTREETMAP', 'OSM:WAY:262258881',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:262258881')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva de São Pedro da Cova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1597375)) <= 0.00070
               AND ABS(v.longitude - (-8.5054046)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:128272698 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Medense', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0631510, -8.4268446, 'OPENSTREETMAP', 'OSM:WAY:128272698',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:128272698')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Medense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0631510)) <= 0.00070
               AND ABS(v.longitude - (-8.4268446)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:550879538 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Trás da Serra', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1138878, -8.5069088, 'OPENSTREETMAP', 'OSM:WAY:550879538',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:550879538')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Trás da Serra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1138878)) <= 0.00070
               AND ABS(v.longitude - (-8.5069088)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:331562184 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de São Domingos', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1152588, -8.5300969, 'OPENSTREETMAP', 'OSM:WAY:331562184',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:331562184')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de São Domingos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1152588)) <= 0.00070
               AND ABS(v.longitude - (-8.5300969)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:381421863 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Centro Popular de Trabalhadores Unidos ao São Pedro da Cova', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1420115, -8.4982226, 'OPENSTREETMAP', 'OSM:WAY:381421863',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:381421863')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Centro Popular de Trabalhadores Unidos ao São Pedro da Cova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1420115)) <= 0.00070
               AND ABS(v.longitude - (-8.4982226)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:722262671 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo futebol Durão Barroso', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1478288, -8.5010941, 'OPENSTREETMAP', 'OSM:WAY:722262671',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:722262671')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo futebol Durão Barroso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1478288)) <= 0.00070
               AND ABS(v.longitude - (-8.5010941)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:722262676 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo futebol escola', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1571741, -8.5109411, 'OPENSTREETMAP', 'OSM:WAY:722262676',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:722262676')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo futebol escola'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1571741)) <= 0.00070
               AND ABS(v.longitude - (-8.5109411)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1070586587 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Sport Clube Rio Tinto', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1848408, -8.5460535, 'OPENSTREETMAP', 'OSM:WAY:1070586587',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1070586587')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Sport Clube Rio Tinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1848408)) <= 0.00070
               AND ABS(v.longitude - (-8.5460535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:480183831 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'DouroFit', 'Gondomar', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.0729625, -8.4219126, 'OPENSTREETMAP', 'OSM:WAY:480183831',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:480183831')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('DouroFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0729625)) <= 0.00070
               AND ABS(v.longitude - (-8.4219126)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:232722248 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio 1º de Dezembro', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0923589, -8.4844038, 'OPENSTREETMAP', 'OSM:WAY:232722248',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:232722248')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio 1º de Dezembro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0923589)) <= 0.00070
               AND ABS(v.longitude - (-8.4844038)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:128432653 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio de São Miguel', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1360548, -8.5418951, 'OPENSTREETMAP', 'OSM:WAY:128432653',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:128432653')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio de São Miguel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1360548)) <= 0.00070
               AND ABS(v.longitude - (-8.5418951)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:381421933 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Futebol Clube Estrelas de Fânzeres', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1598710, -8.5368845, 'OPENSTREETMAP', 'OSM:WAY:381421933',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:381421933')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Futebol Clube Estrelas de Fânzeres'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1598710)) <= 0.00070
               AND ABS(v.longitude - (-8.5368845)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:381421799 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Laranjal', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1596672, -8.5062320, 'OPENSTREETMAP', 'OSM:WAY:381421799',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:381421799')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Laranjal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1596672)) <= 0.00070
               AND ABS(v.longitude - (-8.5062320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1415390620 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Gondomar', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1743173, -8.5654169, 'OPENSTREETMAP', 'OSM:WAY:1415390620',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1415390620')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1743173)) <= 0.00070
               AND ABS(v.longitude - (-8.5654169)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:383005930 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo do Covelo', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1093541, -8.4669899, 'OPENSTREETMAP', 'OSM:WAY:383005930',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:383005930')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo do Covelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1093541)) <= 0.00070
               AND ABS(v.longitude - (-8.4669899)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:447493946 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipais das Medas', 'Gondomar', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0712409, -8.4545564, 'OPENSTREETMAP', 'OSM:WAY:447493946',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:447493946')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipais das Medas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0712409)) <= 0.00070
               AND ABS(v.longitude - (-8.4545564)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:342614455 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Baguim do Monte', 'Gondomar', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1932776, -8.5222977, 'OPENSTREETMAP', 'OSM:WAY:342614455',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:342614455')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Baguim do Monte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1932776)) <= 0.00070
               AND ABS(v.longitude - (-8.5222977)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:381423241 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Fânzeres', 'Gondomar', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1667049, -8.5244902, 'OPENSTREETMAP', 'OSM:WAY:381423241',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:381423241')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Fânzeres'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1667049)) <= 0.00070
               AND ABS(v.longitude - (-8.5244902)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:28617991 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de São Cosme Gondomar', 'Gondomar', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1368453, -8.5406972, 'OPENSTREETMAP', 'OSM:WAY:28617991',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:28617991')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de São Cosme Gondomar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1368453)) <= 0.00070
               AND ABS(v.longitude - (-8.5406972)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:291483329 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de São Pedro da Cova', 'Gondomar', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.1600542, -8.5040463, 'OPENSTREETMAP', 'OSM:WAY:291483329',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:291483329')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de São Pedro da Cova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1600542)) <= 0.00070
               AND ABS(v.longitude - (-8.5040463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:381422173 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Valbom', 'Gondomar', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1387154, -8.5647617, 'OPENSTREETMAP', 'OSM:WAY:381422173',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:381422173')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Valbom'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1387154)) <= 0.00070
               AND ABS(v.longitude - (-8.5647617)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:256459236 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Melres', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0671955, -8.4030873, 'OPENSTREETMAP', 'OSM:WAY:256459236',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:256459236')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Melres'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0671955)) <= 0.00070
               AND ABS(v.longitude - (-8.4030873)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:233432248 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ramalde Futebol Clube', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1332289, -8.5163629, 'OPENSTREETMAP', 'OSM:WAY:233432248',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:233432248')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ramalde Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1332289)) <= 0.00070
               AND ABS(v.longitude - (-8.5163629)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:100721739 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Clube de Rio Tinto', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1847635, -8.5473238, 'OPENSTREETMAP', 'OSM:WAY:100721739',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:100721739')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Clube de Rio Tinto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1847635)) <= 0.00070
               AND ABS(v.longitude - (-8.5473238)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:232723837 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Clube Montezelo', 'Gondomar', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1730870, -8.5241419, 'OPENSTREETMAP', 'OSM:WAY:232723837',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:232723837')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gondomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Clube Montezelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1730870)) <= 0.00070
               AND ABS(v.longitude - (-8.5241419)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:604319633 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de UD Lagoas', 'Lousada', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2574586, -8.3086910, 'OPENSTREETMAP', 'OSM:WAY:604319633',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:604319633')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de UD Lagoas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2574586)) <= 0.00070
               AND ABS(v.longitude - (-8.3086910)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1013098313 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Sousela', 'Lousada', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3002129, -8.3239449, 'OPENSTREETMAP', 'OSM:WAY:1013098313',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1013098313')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Sousela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3002129)) <= 0.00070
               AND ABS(v.longitude - (-8.3239449)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:264747209 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do União Cultural e Recreativa de Boim', 'Lousada', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2613616, -8.2730759, 'OPENSTREETMAP', 'OSM:WAY:264747209',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:264747209')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do União Cultural e Recreativa de Boim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2613616)) <= 0.00070
               AND ABS(v.longitude - (-8.2730759)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:889963872 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol Meinedo', 'Lousada', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2507578, -8.2576818, 'OPENSTREETMAP', 'OSM:WAY:889963872',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:889963872')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol Meinedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2507578)) <= 0.00070
               AND ABS(v.longitude - (-8.2576818)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11972256037 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Ténis de Mesa de Lousada', 'Lousada', 'Porto', NULL,
       'Ténis de mesa', 'Campo desportivo', NULL,
       41.2852048, -8.2919481, 'OPENSTREETMAP', 'OSM:NODE:11972256037',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11972256037')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Ténis de Mesa de Lousada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2852048)) <= 0.00070
               AND ABS(v.longitude - (-8.2919481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108166551 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Hóquei em Campo', 'Lousada', 'Porto', NULL,
       'Hóquei em Campo', 'Campo desportivo', NULL,
       41.2830053, -8.2897541, 'OPENSTREETMAP', 'OSM:WAY:108166551',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108166551')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Hóquei em Campo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2830053)) <= 0.00070
               AND ABS(v.longitude - (-8.2897541)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168887849 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Lousada', 'Lousada', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2763103, -8.2783925, 'OPENSTREETMAP', 'OSM:WAY:1168887849',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168887849')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Lousada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2763103)) <= 0.00070
               AND ABS(v.longitude - (-8.2783925)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108122732 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Lousada', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2762239, -8.2781359, 'OPENSTREETMAP', 'OSM:WAY:108122732',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108122732')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2762239)) <= 0.00070
               AND ABS(v.longitude - (-8.2781359)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:823148046 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Lousada', 'Lousada', 'Porto', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.2844791, -8.2927198, 'OPENSTREETMAP', 'OSM:WAY:823148046',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823148046')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Lousada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2844791)) <= 0.00070
               AND ABS(v.longitude - (-8.2927198)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10753435483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Playlife Fitness Center', 'Lousada', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2685472, -8.2879010, 'OPENSTREETMAP', 'OSM:NODE:10753435483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10753435483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Playlife Fitness Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2685472)) <= 0.00070
               AND ABS(v.longitude - (-8.2879010)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:255739012 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ténis Atlântico', 'Lousada', 'Porto', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.2838491, -8.2903896, 'OPENSTREETMAP', 'OSM:WAY:255739012',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:255739012')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lousada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ténis Atlântico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2838491)) <= 0.00070
               AND ABS(v.longitude - (-8.2903896)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151513199 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação da Granja', 'Maia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2019961, -8.5614623, 'OPENSTREETMAP', 'OSM:WAY:151513199',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151513199')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação da Granja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2019961)) <= 0.00070
               AND ABS(v.longitude - (-8.5614623)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182620832 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Municipal de Gondim', 'Maia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2626481, -8.5963472, 'OPENSTREETMAP', 'OSM:WAY:182620832',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182620832')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Municipal de Gondim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2626481)) <= 0.00070
               AND ABS(v.longitude - (-8.5963472)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:160334197 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos do Estádio Municipal do Professor Doutor José Vieira de Carvalho', 'Maia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2345912, -8.6168340, 'OPENSTREETMAP', 'OSM:WAY:160334197',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:160334197')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos do Estádio Municipal do Professor Doutor José Vieira de Carvalho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2345912)) <= 0.00070
               AND ABS(v.longitude - (-8.6168340)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:605089389 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Maria da Fonte', 'Maia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2469720, -8.6678377, 'OPENSTREETMAP', 'OSM:WAY:605089389',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:605089389')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Maria da Fonte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2469720)) <= 0.00070
               AND ABS(v.longitude - (-8.6678377)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:428980507 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Castêlo da Maia Ginásio Clube', 'Maia', 'Porto', NULL,
       'Voleibol', 'Estádio', NULL,
       41.2687143, -8.6100708, 'OPENSTREETMAP', 'OSM:WAY:428980507',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:428980507')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Castêlo da Maia Ginásio Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2687143)) <= 0.00070
               AND ABS(v.longitude - (-8.6100708)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1460562541 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CF Maia', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2338436, -8.6030743, 'OPENSTREETMAP', 'OSM:WAY:1460562541',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1460562541')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CF Maia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2338436)) <= 0.00070
               AND ABS(v.longitude - (-8.6030743)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:605096147 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo desportivo do F. C. Pedras Rubras', 'Maia', 'Porto', NULL,
       'Andebol', 'Complexo desportivo', NULL,
       41.2479718, -8.6593838, 'OPENSTREETMAP', 'OSM:WAY:605096147',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:605096147')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo desportivo do F. C. Pedras Rubras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2479718)) <= 0.00070
               AND ABS(v.longitude - (-8.6593838)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:306717765 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal de Nogueira', 'Maia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2422911, -8.5914399, 'OPENSTREETMAP', 'OSM:WAY:306717765',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:306717765')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Municipal de Nogueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2422911)) <= 0.00070
               AND ABS(v.longitude - (-8.5914399)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:160334185 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Ténis da Maia', 'Maia', 'Porto', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       41.2340796, -8.6160319, 'OPENSTREETMAP', 'OSM:WAY:160334185',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:160334185')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Ténis da Maia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2340796)) <= 0.00070
               AND ABS(v.longitude - (-8.6160319)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1296652916 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Ginástica', 'Maia', 'Porto', NULL,
       'Ginástica', 'Complexo desportivo', 'INDOOR',
       41.2341448, -8.6146961, 'OPENSTREETMAP', 'OSM:WAY:1296652916',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1296652916')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Ginástica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2341448)) <= 0.00070
               AND ABS(v.longitude - (-8.6146961)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440423404 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Pedrouços', 'Maia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1899740, -8.5848128, 'OPENSTREETMAP', 'OSM:WAY:440423404',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440423404')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Pedrouços'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1899740)) <= 0.00070
               AND ABS(v.longitude - (-8.5848128)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1459368517 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Piscinas de Gueifães', 'Maia', 'Porto', NULL,
       'Natação', 'Ginásio', NULL,
       41.2149998, -8.6007631, 'OPENSTREETMAP', 'OSM:WAY:1459368517',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1459368517')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Piscinas de Gueifães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2149998)) <= 0.00070
               AND ABS(v.longitude - (-8.6007631)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:29035120 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio de São Pedro Fins', 'Maia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2452785, -8.5634387, 'OPENSTREETMAP', 'OSM:WAY:29035120',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:29035120')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio de São Pedro Fins'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2452785)) <= 0.00070
               AND ABS(v.longitude - (-8.5634387)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:35939446 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal do Professor Doutor José Vieira de Carvalho', 'Maia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2345802, -8.6183758, 'OPENSTREETMAP', 'OSM:WAY:35939446',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:35939446')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal do Professor Doutor José Vieira de Carvalho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2345802)) <= 0.00070
               AND ABS(v.longitude - (-8.6183758)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:405572232 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Doutor Costa Lima', 'Maia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.2585302, -8.6183949, 'OPENSTREETMAP', 'OSM:WAY:405572232',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:405572232')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Doutor Costa Lima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2585302)) <= 0.00070
               AND ABS(v.longitude - (-8.6183949)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7927531579 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'FitnessHut', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2335819, -8.6216336, 'OPENSTREETMAP', 'OSM:NODE:7927531579',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7927531579')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('FitnessHut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2335819)) <= 0.00070
               AND ABS(v.longitude - (-8.6216336)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1370715149 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Maia', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2552514, -8.6529397, 'OPENSTREETMAP', 'OSM:WAY:1370715149',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1370715149')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Maia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2552514)) <= 0.00070
               AND ABS(v.longitude - (-8.6529397)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10248419051 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2198076, -8.5616924, 'OPENSTREETMAP', 'OSM:NODE:10248419051',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10248419051')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2198076)) <= 0.00070
               AND ABS(v.longitude - (-8.5616924)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1198135727 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2492598, -8.6217180, 'OPENSTREETMAP', 'OSM:WAY:1198135727',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1198135727')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2492598)) <= 0.00070
               AND ABS(v.longitude - (-8.6217180)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8376116916 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Desafios', 'Maia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2401578, -8.6170325, 'OPENSTREETMAP', 'OSM:NODE:8376116916',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8376116916')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Desafios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2401578)) <= 0.00070
               AND ABS(v.longitude - (-8.6170325)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1217767359 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Leões da Guarda', 'Maia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2493788, -8.6373663, 'OPENSTREETMAP', 'OSM:WAY:1217767359',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1217767359')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Leões da Guarda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2493788)) <= 0.00070
               AND ABS(v.longitude - (-8.6373663)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4632081247 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Maximos', 'Maia', 'Porto', NULL,
       'Natação;Ginásio', 'Ginásio', NULL,
       41.2618625, -8.6177600, 'OPENSTREETMAP', 'OSM:NODE:4632081247',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4632081247')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Maximos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2618625)) <= 0.00070
               AND ABS(v.longitude - (-8.6177600)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151707602 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavihão Municipal de Nogueira', 'Maia', 'Porto', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       41.2300888, -8.5942940, 'OPENSTREETMAP', 'OSM:WAY:151707602',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151707602')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavihão Municipal de Nogueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2300888)) <= 0.00070
               AND ABS(v.longitude - (-8.5942940)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:281752446 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal da Nortecoope', 'Maia', 'Porto', NULL,
       'Hóquei em Campo', 'Complexo desportivo', NULL,
       41.2233883, -8.6090513, 'OPENSTREETMAP', 'OSM:WAY:281752446',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:281752446')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal da Nortecoope'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2233883)) <= 0.00070
               AND ABS(v.longitude - (-8.6090513)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:228193787 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal de Crestins', 'Maia', 'Porto', NULL,
       'Basquetebol;Andebol', 'Campo desportivo', NULL,
       41.2337957, -8.6593988, 'OPENSTREETMAP', 'OSM:WAY:228193787',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:228193787')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal de Crestins'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2337957)) <= 0.00070
               AND ABS(v.longitude - (-8.6593988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151216623 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Águas Santas', 'Maia', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2041433, -8.5638501, 'OPENSTREETMAP', 'OSM:WAY:151216623',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151216623')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Águas Santas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2041433)) <= 0.00070
               AND ABS(v.longitude - (-8.5638501)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:224165505 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Folgosa', 'Maia', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.2680762, -8.5456558, 'OPENSTREETMAP', 'OSM:WAY:224165505',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:224165505')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Folgosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2680762)) <= 0.00070
               AND ABS(v.longitude - (-8.5456558)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1211375001 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas da Quinta da Gruta', 'Maia', 'Porto', NULL,
       'Natação;Ténis', 'Complexo desportivo', NULL,
       41.2651454, -8.6113469, 'OPENSTREETMAP', 'OSM:WAY:1211375001',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1211375001')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas da Quinta da Gruta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2651454)) <= 0.00070
               AND ABS(v.longitude - (-8.6113469)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:450601829 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polideportivo de São Pedro de Avioso', 'Maia', 'Porto', NULL,
       'Futsal', 'Campo desportivo', NULL,
       41.2804699, -8.6175793, 'OPENSTREETMAP', 'OSM:WAY:450601829',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:450601829')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polideportivo de São Pedro de Avioso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2804699)) <= 0.00070
               AND ABS(v.longitude - (-8.6175793)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:543186027 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park da Maia', 'Maia', 'Porto', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.2349571, -8.6150000, 'OPENSTREETMAP', 'OSM:WAY:543186027',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:543186027')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Maia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park da Maia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2349571)) <= 0.00070
               AND ABS(v.longitude - (-8.6150000)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
