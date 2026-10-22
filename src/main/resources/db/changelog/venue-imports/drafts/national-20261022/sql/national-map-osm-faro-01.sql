-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:106911686 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Algarve Golf Acadamy (Driving Range)', 'Albufeira', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.1043536, -8.2386196, 'OPENSTREETMAP', 'OSM:WAY:106911686',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106911686')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Algarve Golf Acadamy (Driving Range)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1043536)) <= 0.00070
               AND ABS(v.longitude - (-8.2386196)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:643319814 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CerroMar Garden ****', 'Albufeira', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.0905877, -8.2525456, 'OPENSTREETMAP', 'OSM:WAY:643319814',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:643319814')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CerroMar Garden ****'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0905877)) <= 0.00070
               AND ABS(v.longitude - (-8.2525456)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9817088053 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro do Ferro', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0142928, -7.9238115, 'OPENSTREETMAP', 'OSM:NODE:9817088053',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9817088053')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro do Ferro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0142928)) <= 0.00070
               AND ABS(v.longitude - (-7.9238115)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13405318101 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Dynamic Fitness', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0272036, -7.9290219, 'OPENSTREETMAP', 'OSM:NODE:13405318101',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13405318101')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Dynamic Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0272036)) <= 0.00070
               AND ABS(v.longitude - (-7.9290219)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11318434052 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0274777, -7.9469594, 'OPENSTREETMAP', 'OSM:NODE:11318434052',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11318434052')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0274777)) <= 0.00070
               AND ABS(v.longitude - (-7.9469594)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13441158201 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Expira - Pilates, Yoga, Saúde', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0145959, -7.9198965, 'OPENSTREETMAP', 'OSM:NODE:13441158201',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13441158201')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Expira - Pilates, Yoga, Saúde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0145959)) <= 0.00070
               AND ABS(v.longitude - (-7.9198965)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12797353901 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0162457, -7.9259481, 'OPENSTREETMAP', 'OSM:NODE:12797353901',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12797353901')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0162457)) <= 0.00070
               AND ABS(v.longitude - (-7.9259481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8832515104 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gym Féminis Fit', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0252582, -7.9393635, 'OPENSTREETMAP', 'OSM:NODE:8832515104',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8832515104')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gym Féminis Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0252582)) <= 0.00070
               AND ABS(v.longitude - (-7.9393635)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8590125365 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'South boutique gym', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0278065, -7.9267345, 'OPENSTREETMAP', 'OSM:NODE:8590125365',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8590125365')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('South boutique gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0278065)) <= 0.00070
               AND ABS(v.longitude - (-7.9267345)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11013959439 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'All life is Yoga', 'Lagoa', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1463208, -8.5068552, 'OPENSTREETMAP', 'OSM:NODE:11013959439',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11013959439')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('All life is Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1463208)) <= 0.00070
               AND ABS(v.longitude - (-8.5068552)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:491957967 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vale de Milho Golf', 'Lagoa', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0957729, -8.4467952, 'OPENSTREETMAP', 'OSM:WAY:491957967',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:491957967')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vale de Milho Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0957729)) <= 0.00070
               AND ABS(v.longitude - (-8.4467952)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1123666029 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vertical Escalada', 'Lagoa', 'Faro', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       37.1470194, -8.4813022, 'OPENSTREETMAP', 'OSM:WAY:1123666029',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1123666029')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vertical Escalada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1470194)) <= 0.00070
               AND ABS(v.longitude - (-8.4813022)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107214912 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Espiche Golf', 'Lagos', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.1220398, -8.7539477, 'OPENSTREETMAP', 'OSM:WAY:107214912',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107214912')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Espiche Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1220398)) <= 0.00070
               AND ABS(v.longitude - (-8.7539477)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107196466 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pestana Vila Sol Golf Course', 'Loulé', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0869180, -8.0934300, 'OPENSTREETMAP', 'OSM:WAY:107196466',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107196466')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pestana Vila Sol Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0869180)) <= 0.00070
               AND ABS(v.longitude - (-8.0934300)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107208466 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pinheiros Altos Golf Course', 'Loulé', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0465136, -8.0075481, 'OPENSTREETMAP', 'OSM:WAY:107208466',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107208466')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pinheiros Altos Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0465136)) <= 0.00070
               AND ABS(v.longitude - (-8.0075481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240113589 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quinta do Lago South Golf Course', 'Loulé', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0466020, -8.0218901, 'OPENSTREETMAP', 'OSM:WAY:240113589',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240113589')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quinta do Lago South Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0466020)) <= 0.00070
               AND ABS(v.longitude - (-8.0218901)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6929699285 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Campus', 'Loulé', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0469513, -8.0304684, 'OPENSTREETMAP', 'OSM:NODE:6929699285',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6929699285')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Campus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0469513)) <= 0.00070
               AND ABS(v.longitude - (-8.0304684)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222265565 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vilamoura Tennis Academy', 'Loulé', 'Faro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       37.0845600, -8.1172726, 'OPENSTREETMAP', 'OSM:WAY:222265565',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222265565')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vilamoura Tennis Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0845600)) <= 0.00070
               AND ABS(v.longitude - (-8.1172726)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107218749 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Villamoura Driving Range & Golf Academy', 'Loulé', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.1085682, -8.1186696, 'OPENSTREETMAP', 'OSM:WAY:107218749',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107218749')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Villamoura Driving Range & Golf Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1085682)) <= 0.00070
               AND ABS(v.longitude - (-8.1186696)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222567474 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vila Monte Golf Course', 'Olhão', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0982695, -7.7946349, 'OPENSTREETMAP', 'OSM:WAY:222567474',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222567474')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vila Monte Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0982695)) <= 0.00070
               AND ABS(v.longitude - (-7.7946349)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:948376543 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo Internacional do Algarve', 'Portimão', 'Faro', NULL,
       'Karting', 'Complexo desportivo', NULL,
       37.2332740, -8.6357887, 'OPENSTREETMAP', 'OSM:NODE:948376543',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:948376543')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo Internacional do Algarve'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.2332740)) <= 0.00070
               AND ABS(v.longitude - (-8.6357887)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:435219010 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Benamor Golf', 'Tavira', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.1512679, -7.6073265, 'OPENSTREETMAP', 'OSM:WAY:435219010',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:435219010')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tavira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Benamor Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1512679)) <= 0.00070
               AND ABS(v.longitude - (-7.6073265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1316810821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Golf Santo António / Parque da Floresta', 'Vila do Bispo', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0846020, -8.8451311, 'OPENSTREETMAP', 'OSM:WAY:1316810821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1316810821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Bispo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Golf Santo António / Parque da Floresta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0846020)) <= 0.00070
               AND ABS(v.longitude - (-8.8451311)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:664019473 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Monte Rei Golf Course', 'Vila Real de Santo António', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.2134466, -7.5483723, 'OPENSTREETMAP', 'OSM:WAY:664019473',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:664019473')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real de Santo António'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Monte Rei Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.2134466)) <= 0.00070
               AND ABS(v.longitude - (-7.5483723)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4744791124 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pilates Studio', 'Vila Real de Santo António', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1927963, -7.4181515, 'OPENSTREETMAP', 'OSM:NODE:4744791124',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4744791124')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real de Santo António'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pilates Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1927963)) <= 0.00070
               AND ABS(v.longitude - (-7.4181515)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1153489941 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'AP Victoria Sports & Beach', 'Albufeira', 'Faro', NULL,
       'Futebol;Corrida;Atletismo;Ginástica;Rugby;Padel;Ténis', 'Complexo desportivo', NULL,
       37.0902260, -8.1663035, 'OPENSTREETMAP', 'OSM:WAY:1153489941',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1153489941')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('AP Victoria Sports & Beach'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0902260)) <= 0.00070
               AND ABS(v.longitude - (-8.1663035)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:129585165 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bellavista Tenis Club', 'Albufeira', 'Faro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       37.0922427, -8.2413145, 'OPENSTREETMAP', 'OSM:WAY:129585165',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:129585165')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bellavista Tenis Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0922427)) <= 0.00070
               AND ABS(v.longitude - (-8.2413145)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:92442242 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos do Imortal DC', 'Albufeira', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.0977661, -8.2422995, 'OPENSTREETMAP', 'OSM:WAY:92442242',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:92442242')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos do Imortal DC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0977661)) <= 0.00070
               AND ABS(v.longitude - (-8.2422995)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:564844182 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Tiro de Paderne', 'Albufeira', 'Faro', NULL,
       'Tiro', 'Campo desportivo', NULL,
       37.1624489, -8.1861203, 'OPENSTREETMAP', 'OSM:WAY:564844182',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:564844182')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Tiro de Paderne'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1624489)) <= 0.00070
               AND ABS(v.longitude - (-8.1861203)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106911695 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Club Med Da Balaia Academy Pitch & Putt', 'Albufeira', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0910155, -8.2006790, 'OPENSTREETMAP', 'OSM:WAY:106911695',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106911695')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Club Med Da Balaia Academy Pitch & Putt'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0910155)) <= 0.00070
               AND ABS(v.longitude - (-8.2006790)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3655387900 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'hotwheels karting', 'Albufeira', 'Faro', NULL,
       'Karting', 'Pista desportiva', NULL,
       37.0858103, -8.2664467, 'OPENSTREETMAP', 'OSM:NODE:3655387900',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3655387900')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('hotwheels karting'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0858103)) <= 0.00070
               AND ABS(v.longitude - (-8.2664467)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:696646966 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Jogos de agua', 'Albufeira', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1251514, -8.3161490, 'OPENSTREETMAP', 'OSM:WAY:696646966',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:696646966')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Jogos de agua'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1251514)) <= 0.00070
               AND ABS(v.longitude - (-8.3161490)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:638602198 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Paintball', 'Albufeira', 'Faro', NULL,
       'Tiro', 'Campo desportivo', NULL,
       37.1033748, -8.2598178, 'OPENSTREETMAP', 'OSM:WAY:638602198',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:638602198')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Paintball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1033748)) <= 0.00070
               AND ABS(v.longitude - (-8.2598178)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:170187398 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Imortal DC', 'Albufeira', 'Faro', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       37.0867980, -8.2569468, 'OPENSTREETMAP', 'OSM:WAY:170187398',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:170187398')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Imortal DC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0867980)) <= 0.00070
               AND ABS(v.longitude - (-8.2569468)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:90190129 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Criancas', 'Albufeira', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1059187, -8.2513822, 'OPENSTREETMAP', 'OSM:WAY:90190129',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:90190129')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Criancas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1059187)) <= 0.00070
               AND ABS(v.longitude - (-8.2513822)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:92442626 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Albufeira', 'Albufeira', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.0969876, -8.2410173, 'OPENSTREETMAP', 'OSM:WAY:92442626',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:92442626')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Albufeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0969876)) <= 0.00070
               AND ABS(v.longitude - (-8.2410173)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1153609367 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista das Açoteias', 'Albufeira', 'Faro', NULL,
       'Corrida', 'Pista desportiva', NULL,
       37.0920387, -8.1629194, 'OPENSTREETMAP', 'OSM:WAY:1153609367',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1153609367')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista das Açoteias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0920387)) <= 0.00070
               AND ABS(v.longitude - (-8.1629194)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:118876183 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Cross das Açoteias', 'Albufeira', 'Faro', NULL,
       'Atletismo', 'Campo desportivo', NULL,
       37.0915835, -8.1630876, 'OPENSTREETMAP', 'OSM:WAY:118876183',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:118876183')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Cross das Açoteias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0915835)) <= 0.00070
               AND ABS(v.longitude - (-8.1630876)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:607677390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Praia de ondas', 'Albufeira', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1241209, -8.3154461, 'OPENSTREETMAP', 'OSM:WAY:607677390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:607677390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Praia de ondas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1241209)) <= 0.00070
               AND ABS(v.longitude - (-8.3154461)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181338931 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park de Albufeira', 'Albufeira', 'Faro', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.0996295, -8.2386269, 'OPENSTREETMAP', 'OSM:WAY:181338931',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181338931')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park de Albufeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0996295)) <= 0.00070
               AND ABS(v.longitude - (-8.2386269)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1311846742 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Training Camps Albufeira', 'Albufeira', 'Faro', NULL,
       'Atletismo', 'Campo desportivo', NULL,
       37.0911343, -8.1604303, 'OPENSTREETMAP', 'OSM:WAY:1311846742',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1311846742')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Training Camps Albufeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0911343)) <= 0.00070
               AND ABS(v.longitude - (-8.1604303)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10575307229 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'UNIK Fitness Club', 'Albufeira', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0900178, -8.2411721, 'OPENSTREETMAP', 'OSM:NODE:10575307229',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10575307229')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('UNIK Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0900178)) <= 0.00070
               AND ABS(v.longitude - (-8.2411721)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:47841943 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Volley Ball', 'Albufeira', 'Faro', NULL,
       'Voleibol', 'Campo desportivo', NULL,
       37.0755409, -8.2948759, 'OPENSTREETMAP', 'OSM:WAY:47841943',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:47841943')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Albufeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Volley Ball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0755409)) <= 0.00070
               AND ABS(v.longitude - (-8.2948759)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1241619267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Amado Surfcamp Camp', 'Aljezur', 'Faro', NULL,
       'Surf', 'Complexo desportivo', NULL,
       37.1834057, -8.8800638, 'OPENSTREETMAP', 'OSM:WAY:1241619267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1241619267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljezur'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Amado Surfcamp Camp'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1834057)) <= 0.00070
               AND ABS(v.longitude - (-8.8800638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14175779601 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bear Bones Jiujitsu', 'Aljezur', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.3168130, -8.7943636, 'OPENSTREETMAP', 'OSM:NODE:14175779601',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14175779601')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljezur'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bear Bones Jiujitsu'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.3168130)) <= 0.00070
               AND ABS(v.longitude - (-8.7943636)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:155878842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de Futebol - Aljezur', 'Aljezur', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.3106182, -8.8044706, 'OPENSTREETMAP', 'OSM:WAY:155878842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:155878842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljezur'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal de Futebol - Aljezur'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.3106182)) <= 0.00070
               AND ABS(v.longitude - (-8.8044706)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:486364165 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Aljezur', 'Aljezur', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.3262436, -8.7950963, 'OPENSTREETMAP', 'OSM:WAY:486364165',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:486364165')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aljezur'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Aljezur'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.3262436)) <= 0.00070
               AND ABS(v.longitude - (-8.7950963)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107214899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Castro Marim Golf & Country Club', 'Castro Marim', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.2382324, -7.4680140, 'OPENSTREETMAP', 'OSM:WAY:107214899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107214899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Marim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Castro Marim Golf & Country Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.2382324)) <= 0.00070
               AND ABS(v.longitude - (-7.4680140)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5566859024 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Health Gang', 'Castro Marim', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1723579, -7.5011687, 'OPENSTREETMAP', 'OSM:NODE:5566859024',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5566859024')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Marim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Health Gang'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1723579)) <= 0.00070
               AND ABS(v.longitude - (-7.5011687)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5801521696 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Box360', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0247228, -7.9365470, 'OPENSTREETMAP', 'OSM:NODE:5801521696',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5801521696')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Box360'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0247228)) <= 0.00070
               AND ABS(v.longitude - (-7.9365470)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:93491760 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal da Penha', 'Faro', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.0280841, -7.9204714, 'OPENSTREETMAP', 'OSM:WAY:93491760',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:93491760')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal da Penha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0280841)) <= 0.00070
               AND ABS(v.longitude - (-7.9204714)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:372345784 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Náutico da Praia de Faro', 'Faro', 'Faro', NULL,
       'Canoagem', 'Complexo desportivo', NULL,
       37.0039693, -7.9881700, 'OPENSTREETMAP', 'OSM:WAY:372345784',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:372345784')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Náutico da Praia de Faro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0039693)) <= 0.00070
               AND ABS(v.longitude - (-7.9881700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:660859167 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo da Penha', 'Faro', 'Faro', NULL,
       'Futebol;Natação', 'Complexo desportivo', NULL,
       37.0271937, -7.9201694, 'OPENSTREETMAP', 'OSM:WAY:660859167',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:660859167')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo da Penha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0271937)) <= 0.00070
               AND ABS(v.longitude - (-7.9201694)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9995574564 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Contrology Pilates', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0152499, -7.9319470, 'OPENSTREETMAP', 'OSM:NODE:9995574564',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9995574564')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Contrology Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0152499)) <= 0.00070
               AND ABS(v.longitude - (-7.9319470)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:38080909 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Algarve', 'Faro', 'Faro', NULL,
       'Futebol', 'Estádio', NULL,
       37.0882584, -7.9746058, 'OPENSTREETMAP', 'OSM:WAY:38080909',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:38080909')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Algarve'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0882584)) <= 0.00070
               AND ABS(v.longitude - (-7.9746058)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9820290997 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Evox', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0166103, -7.9368760, 'OPENSTREETMAP', 'OSM:NODE:9820290997',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9820290997')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Evox'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0166103)) <= 0.00070
               AND ABS(v.longitude - (-7.9368760)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9563452959 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gymnasium', 'Faro', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0311531, -7.9472525, 'OPENSTREETMAP', 'OSM:NODE:9563452959',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9563452959')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gymnasium'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0311531)) <= 0.00070
               AND ABS(v.longitude - (-7.9472525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12172584965 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Panoramic Pool Faro', 'Faro', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.0302416, -7.9389919, 'OPENSTREETMAP', 'OSM:NODE:12172584965',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12172584965')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Panoramic Pool Faro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0302416)) <= 0.00070
               AND ABS(v.longitude - (-7.9389919)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:660859164 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Faro', 'Faro', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.0268689, -7.9201598, 'OPENSTREETMAP', 'OSM:WAY:660859164',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:660859164')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Faro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0268689)) <= 0.00070
               AND ABS(v.longitude - (-7.9201598)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182817080 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Faro', 'Faro', 'Faro', NULL,
       'Corrida', 'Pista desportiva', NULL,
       37.0283451, -7.9165483, 'OPENSTREETMAP', 'OSM:WAY:182817080',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182817080')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Faro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0283451)) <= 0.00070
               AND ABS(v.longitude - (-7.9165483)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182832431 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube Farense', 'Faro', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.0229199, -7.9284138, 'OPENSTREETMAP', 'OSM:WAY:182832431',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182832431')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Faro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube Farense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0229199)) <= 0.00070
               AND ABS(v.longitude - (-7.9284138)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151109185 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Banzai', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1397916, -8.4744007, 'OPENSTREETMAP', 'OSM:WAY:151109185',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151109185')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Banzai'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1397916)) <= 0.00070
               AND ABS(v.longitude - (-8.4744007)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1501212360 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Big Fall', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1393985, -8.4766089, 'OPENSTREETMAP', 'OSM:WAY:1501212360',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1501212360')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Big Fall'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1393985)) <= 0.00070
               AND ABS(v.longitude - (-8.4766089)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151109195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Big Slides Tower', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1394793, -8.4743456, 'OPENSTREETMAP', 'OSM:WAY:151109195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151109195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Big Slides Tower'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1394793)) <= 0.00070
               AND ABS(v.longitude - (-8.4743456)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151114142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Black Hole', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1393981, -8.4755477, 'OPENSTREETMAP', 'OSM:WAY:151114142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151114142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Black Hole'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1393981)) <= 0.00070
               AND ABS(v.longitude - (-8.4755477)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1501212362 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Boomerang', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1392427, -8.4764976, 'OPENSTREETMAP', 'OSM:WAY:1501212362',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1501212362')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Boomerang'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1392427)) <= 0.00070
               AND ABS(v.longitude - (-8.4764976)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:223409707 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Carvoeiro Clube de Ténis', 'Lagoa', 'Faro', NULL,
       'Ginásio', 'Complexo desportivo', NULL,
       37.1083354, -8.4836242, 'OPENSTREETMAP', 'OSM:WAY:223409707',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:223409707')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Carvoeiro Clube de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1083354)) <= 0.00070
               AND ABS(v.longitude - (-8.4836242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151111527 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Children''s Foam Slides', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1387756, -8.4750952, 'OPENSTREETMAP', 'OSM:WAY:151111527',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151111527')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Children''s Foam Slides'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1387756)) <= 0.00070
               AND ABS(v.longitude - (-8.4750952)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151109193 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Disco River', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1400404, -8.4751160, 'OPENSTREETMAP', 'OSM:WAY:151109193',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151109193')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Disco River'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1400404)) <= 0.00070
               AND ABS(v.longitude - (-8.4751160)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:608184515 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Drops', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1389863, -8.4745220, 'OPENSTREETMAP', 'OSM:WAY:608184515',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:608184515')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Drops'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1389863)) <= 0.00070
               AND ABS(v.longitude - (-8.4745220)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106954264 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Capitão Josino da Costa', 'Lagoa', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1311245, -8.4501371, 'OPENSTREETMAP', 'OSM:WAY:106954264',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106954264')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Capitão Josino da Costa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1311245)) <= 0.00070
               AND ABS(v.longitude - (-8.4501371)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14174385568 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Belavista', 'Lagoa', 'Faro', NULL,
       'Futebol;Atletismo', 'Estádio', NULL,
       37.1339201, -8.5080037, 'OPENSTREETMAP', 'OSM:NODE:14174385568',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14174385568')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Belavista'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1339201)) <= 0.00070
               AND ABS(v.longitude - (-8.5080037)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151111502 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Foam Slides', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1387800, -8.4748662, 'OPENSTREETMAP', 'OSM:WAY:151111502',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151111502')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Foam Slides'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1387800)) <= 0.00070
               AND ABS(v.longitude - (-8.4748662)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151109187 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Laguna', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1403554, -8.4754235, 'OPENSTREETMAP', 'OSM:WAY:151109187',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151109187')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Laguna'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1403554)) <= 0.00070
               AND ABS(v.longitude - (-8.4754235)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151523077 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Lagoa', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1331260, -8.4550078, 'OPENSTREETMAP', 'OSM:WAY:151523077',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151523077')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Lagoa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1331260)) <= 0.00070
               AND ABS(v.longitude - (-8.4550078)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1501212361 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Racers', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1394231, -8.4763094, 'OPENSTREETMAP', 'OSM:WAY:1501212361',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1501212361')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Racers'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1394231)) <= 0.00070
               AND ABS(v.longitude - (-8.4763094)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151109191 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'River Ride', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1398510, -8.4746033, 'OPENSTREETMAP', 'OSM:WAY:151109191',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151109191')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('River Ride'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1398510)) <= 0.00070
               AND ABS(v.longitude - (-8.4746033)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:608169356 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Big Wave', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1409147, -8.4755320, 'OPENSTREETMAP', 'OSM:WAY:608169356',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:608169356')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Big Wave'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1409147)) <= 0.00070
               AND ABS(v.longitude - (-8.4755320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:608150516 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tornado', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1399518, -8.4756027, 'OPENSTREETMAP', 'OSM:WAY:608150516',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:608150516')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tornado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1399518)) <= 0.00070
               AND ABS(v.longitude - (-8.4756027)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151109196 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tropical Paradise', 'Lagoa', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1401520, -8.4747820, 'OPENSTREETMAP', 'OSM:WAY:151109196',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151109196')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagoa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tropical Paradise'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1401520)) <= 0.00070
               AND ABS(v.longitude - (-8.4747820)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13703923103 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Assisted Stretching', 'Lagos', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1100731, -8.6687942, 'OPENSTREETMAP', 'OSM:NODE:13703923103',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13703923103')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Assisted Stretching'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1100731)) <= 0.00070
               AND ABS(v.longitude - (-8.6687942)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158416598 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Clube Desportivo de Odiáxere', 'Lagos', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1508903, -8.6497148, 'OPENSTREETMAP', 'OSM:WAY:158416598',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158416598')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Clube Desportivo de Odiáxere'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1508903)) <= 0.00070
               AND ABS(v.longitude - (-8.6497148)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:93326459 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Futebol Esperança de Lagos', 'Lagos', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1164963, -8.6779179, 'OPENSTREETMAP', 'OSM:WAY:93326459',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:93326459')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Futebol Esperança de Lagos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1164963)) <= 0.00070
               AND ABS(v.longitude - (-8.6779179)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13432448480 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Elite Wellness Ginásio', 'Lagos', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0960011, -8.6760574, 'OPENSTREETMAP', 'OSM:NODE:13432448480',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13432448480')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Elite Wellness Ginásio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0960011)) <= 0.00070
               AND ABS(v.longitude - (-8.6760574)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7539396724 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lagos em Forma', 'Lagos', 'Faro', NULL,
       'Natação;Ginásio;Ginástica', 'Complexo desportivo', NULL,
       37.1130433, -8.6803165, 'OPENSTREETMAP', 'OSM:NODE:7539396724',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7539396724')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lagos em Forma'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1130433)) <= 0.00070
               AND ABS(v.longitude - (-8.6803165)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13754493351 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'luum. Pilates Studio', 'Lagos', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1062111, -8.6796440, 'OPENSTREETMAP', 'OSM:NODE:13754493351',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13754493351')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('luum. Pilates Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1062111)) <= 0.00070
               AND ABS(v.longitude - (-8.6796440)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:93119690 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo e Piscinas', 'Lagos', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.1133918, -8.6800779, 'OPENSTREETMAP', 'OSM:WAY:93119690',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:93119690')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Desportivo e Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1133918)) <= 0.00070
               AND ABS(v.longitude - (-8.6800779)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13703923102 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pilates Room', 'Lagos', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1099316, -8.6674099, 'OPENSTREETMAP', 'OSM:NODE:13703923102',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13703923102')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pilates Room'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1099316)) <= 0.00070
               AND ABS(v.longitude - (-8.6674099)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:189700028 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spa', 'Lagos', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1026329, -8.7068899, 'OPENSTREETMAP', 'OSM:WAY:189700028',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:189700028')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1026329)) <= 0.00070
               AND ABS(v.longitude - (-8.7068899)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10945847451 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Your Move', 'Lagos', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0868316, -8.7345651, 'OPENSTREETMAP', 'OSM:NODE:10945847451',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10945847451')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Your Move'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0868316)) <= 0.00070
               AND ABS(v.longitude - (-8.7345651)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:525163931 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '3G AstroTurf 5-a-side Pitch', 'Loulé', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1053530, -8.1152475, 'OPENSTREETMAP', 'OSM:WAY:525163931',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:525163931')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('3G AstroTurf 5-a-side Pitch'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1053530)) <= 0.00070
               AND ABS(v.longitude - (-8.1152475)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:658647400 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Boliqueime', 'Loulé', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1322452, -8.1468421, 'OPENSTREETMAP', 'OSM:WAY:658647400',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:658647400')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Boliqueime'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1322452)) <= 0.00070
               AND ABS(v.longitude - (-8.1468421)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1187363721 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Ria Park', 'Loulé', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.0444595, -8.0398363, 'OPENSTREETMAP', 'OSM:WAY:1187363721',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1187363721')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Ria Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0444595)) <= 0.00070
               AND ABS(v.longitude - (-8.0398363)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:370297514 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo João Adelino Gonçalves', 'Loulé', 'Faro', NULL,
       'Rugby', 'Campo desportivo', NULL,
       37.1429853, -8.0190024, 'OPENSTREETMAP', 'OSM:WAY:370297514',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:370297514')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo João Adelino Gonçalves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1429853)) <= 0.00070
               AND ABS(v.longitude - (-8.0190024)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:642048719 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal Nº2', 'Loulé', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1393172, -8.0144757, 'OPENSTREETMAP', 'OSM:WAY:642048719',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:642048719')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal Nº2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1393172)) <= 0.00070
               AND ABS(v.longitude - (-8.0144757)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:664535728 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Quarteira', 'Loulé', 'Faro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       37.0656606, -8.0915030, 'OPENSTREETMAP', 'OSM:WAY:664535728',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:664535728')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Quarteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0656606)) <= 0.00070
               AND ABS(v.longitude - (-8.0915030)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107196460 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Dom Pedro Old Course', 'Loulé', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.0992737, -8.1152861, 'OPENSTREETMAP', 'OSM:WAY:107196460',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107196460')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Dom Pedro Old Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0992737)) <= 0.00070
               AND ABS(v.longitude - (-8.1152861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104105167 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Quarteira n.º 2', 'Loulé', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.0781529, -8.1098527, 'OPENSTREETMAP', 'OSM:WAY:104105167',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104105167')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Quarteira n.º 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0781529)) <= 0.00070
               AND ABS(v.longitude - (-8.1098527)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:686121223 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Louletano Desportos Clube', 'Loulé', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1343538, -8.0162053, 'OPENSTREETMAP', 'OSM:WAY:686121223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:686121223')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Louletano Desportos Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1343538)) <= 0.00070
               AND ABS(v.longitude - (-8.0162053)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:588440026 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Ondas', 'Loulé', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.0935919, -8.0724577, 'OPENSTREETMAP', 'OSM:WAY:588440026',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:588440026')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Ondas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0935919)) <= 0.00070
               AND ABS(v.longitude - (-8.0724577)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104104817 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Quarteira', 'Loulé', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.0783017, -8.1109248, 'OPENSTREETMAP', 'OSM:WAY:104104817',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104104817')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Quarteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0783017)) <= 0.00070
               AND ABS(v.longitude - (-8.1109248)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:657501098 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Loulé', 'Loulé', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.1415420, -8.0182374, 'OPENSTREETMAP', 'OSM:WAY:657501098',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:657501098')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Loulé'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1415420)) <= 0.00070
               AND ABS(v.longitude - (-8.0182374)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:608122334 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Tropical', 'Loulé', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.0953373, -8.0719661, 'OPENSTREETMAP', 'OSM:WAY:608122334',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:608122334')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Tropical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0953373)) <= 0.00070
               AND ABS(v.longitude - (-8.0719661)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1122144862 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de BMX de Quarteira', 'Loulé', 'Faro', NULL,
       'BMX', 'Pista desportiva', NULL,
       37.0718319, -8.0914798, 'OPENSTREETMAP', 'OSM:WAY:1122144862',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1122144862')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de BMX de Quarteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0718319)) <= 0.00070
               AND ABS(v.longitude - (-8.0914798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7252618076 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Selva Aventura', 'Loulé', 'Faro', NULL,
       'Escalada;Tiro com Arco', 'Complexo desportivo', NULL,
       37.2181855, -8.1804416, 'OPENSTREETMAP', 'OSM:NODE:7252618076',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7252618076')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Selva Aventura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.2181855)) <= 0.00070
               AND ABS(v.longitude - (-8.1804416)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:660235088 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark de Loulé', 'Loulé', 'Faro', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.1453579, -8.0175452, 'OPENSTREETMAP', 'OSM:WAY:660235088',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:660235088')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark de Loulé'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1453579)) <= 0.00070
               AND ABS(v.longitude - (-8.0175452)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:549950566 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park Quarteira', 'Loulé', 'Faro', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.0766651, -8.1119156, 'OPENSTREETMAP', 'OSM:WAY:549950566',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:549950566')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loulé'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park Quarteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0766651)) <= 0.00070
               AND ABS(v.longitude - (-8.1119156)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12766973163 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Karuna Meditation Center', 'Monchique', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.3007182, -8.5211851, 'OPENSTREETMAP', 'OSM:NODE:12766973163',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12766973163')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Monchique'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Karuna Meditation Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.3007182)) <= 0.00070
               AND ABS(v.longitude - (-8.5211851)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5801518086 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Box360', 'Olhão', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0391264, -7.8345387, 'OPENSTREETMAP', 'OSM:NODE:5801518086',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5801518086')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Box360'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0391264)) <= 0.00070
               AND ABS(v.longitude - (-7.8345387)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:141937198 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio José Arcanjo', 'Olhão', 'Faro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       37.0292232, -7.8485306, 'OPENSTREETMAP', 'OSM:WAY:141937198',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:141937198')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio José Arcanjo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0292232)) <= 0.00070
               AND ABS(v.longitude - (-7.8485306)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:145592926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal', 'Olhão', 'Faro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       37.0384346, -7.8519928, 'OPENSTREETMAP', 'OSM:WAY:145592926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145592926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0384346)) <= 0.00070
               AND ABS(v.longitude - (-7.8519928)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10234952899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP', 'Olhão', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0352666, -7.8407564, 'OPENSTREETMAP', 'OSM:NODE:10234952899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10234952899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0352666)) <= 0.00070
               AND ABS(v.longitude - (-7.8407564)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5426443758 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio ao ar livre', 'Olhão', 'Faro', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       37.0234013, -7.8463667, 'OPENSTREETMAP', 'OSM:NODE:5426443758',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5426443758')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio ao ar livre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0234013)) <= 0.00070
               AND ABS(v.longitude - (-7.8463667)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:563006157 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque com ginásio ao ar livre', 'Olhão', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.0234007, -7.8467317, 'OPENSTREETMAP', 'OSM:WAY:563006157',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:563006157')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque com ginásio ao ar livre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0234007)) <= 0.00070
               AND ABS(v.longitude - (-7.8467317)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:145583799 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Olhão', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.0289527, -7.8504940, 'OPENSTREETMAP', 'OSM:WAY:145583799',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145583799')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0289527)) <= 0.00070
               AND ABS(v.longitude - (-7.8504940)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:706825775 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Olhão', 'Olhão', 'Faro', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.0310639, -7.8336108, 'OPENSTREETMAP', 'OSM:WAY:706825775',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:706825775')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Olhão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Olhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0310639)) <= 0.00070
               AND ABS(v.longitude - (-7.8336108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:884611945 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Adults Pool', 'Portimão', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1253309, -8.5756202, 'OPENSTREETMAP', 'OSM:WAY:884611945',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:884611945')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Adults Pool'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1253309)) <= 0.00070
               AND ABS(v.longitude - (-8.5756202)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2796845672 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BMX park', 'Portimão', 'Faro', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.1287767, -8.5452250, 'OPENSTREETMAP', 'OSM:NODE:2796845672',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2796845672')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BMX park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1287767)) <= 0.00070
               AND ABS(v.longitude - (-8.5452250)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99819641 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Ténis de Portimão', 'Portimão', 'Faro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       37.1348571, -8.5402597, 'OPENSTREETMAP', 'OSM:WAY:99819641',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99819641')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Ténis de Portimão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1348571)) <= 0.00070
               AND ABS(v.longitude - (-8.5402597)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:218208445 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Portimonense Sporting Clube', 'Portimão', 'Faro', NULL,
       'Futebol', 'Estádio', NULL,
       37.1358149, -8.5399367, 'OPENSTREETMAP', 'OSM:WAY:218208445',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:218208445')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Portimonense Sporting Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1358149)) <= 0.00070
               AND ABS(v.longitude - (-8.5399367)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:370731397 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio dos 2 Irmãos', 'Portimão', 'Faro', NULL,
       'Futebol', 'Estádio', NULL,
       37.1397325, -8.5570514, 'OPENSTREETMAP', 'OSM:WAY:370731397',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:370731397')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio dos 2 Irmãos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1397325)) <= 0.00070
               AND ABS(v.longitude - (-8.5570514)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99819632 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Portimão', 'Portimão', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1357771, -8.5399298, 'OPENSTREETMAP', 'OSM:WAY:99819632',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99819632')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Portimão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1357771)) <= 0.00070
               AND ABS(v.longitude - (-8.5399298)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1436965426 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oasis Parque Leisure Centre', 'Portimão', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1563076, -8.5569051, 'OPENSTREETMAP', 'OSM:WAY:1436965426',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1436965426')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oasis Parque Leisure Centre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1563076)) <= 0.00070
               AND ABS(v.longitude - (-8.5569051)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:884611892 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas De Crianças', 'Portimão', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1238842, -8.5769074, 'OPENSTREETMAP', 'OSM:WAY:884611892',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:884611892')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas De Crianças'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1238842)) <= 0.00070
               AND ABS(v.longitude - (-8.5769074)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:404203268 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Portimão Surf Clube', 'Portimão', 'Faro', NULL,
       'Surf', 'Complexo desportivo', NULL,
       37.1145279, -8.5295367, 'OPENSTREETMAP', 'OSM:WAY:404203268',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:404203268')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Portimão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Portimão Surf Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1145279)) <= 0.00070
               AND ABS(v.longitude - (-8.5295367)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:180418872 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo de Ténis Municipal', 'São Brás de Alportel', 'Faro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       37.1578801, -7.8827158, 'OPENSTREETMAP', 'OSM:WAY:180418872',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:180418872')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Brás de Alportel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo de Ténis Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1578801)) <= 0.00070
               AND ABS(v.longitude - (-7.8827158)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:538559358 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fabrica do Fitness', 'São Brás de Alportel', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1587142, -7.8946192, 'OPENSTREETMAP', 'OSM:WAY:538559358',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:538559358')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Brás de Alportel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fabrica do Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1587142)) <= 0.00070
               AND ABS(v.longitude - (-7.8946192)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:538572569 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Cobertas', 'São Brás de Alportel', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.1575058, -7.8828592, 'OPENSTREETMAP', 'OSM:WAY:538572569',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:538572569')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Brás de Alportel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1575058)) <= 0.00070
               AND ABS(v.longitude - (-7.8828592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:664733062 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'São Brás de Alportel', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.1500418, -7.8867614, 'OPENSTREETMAP', 'OSM:WAY:664733062',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:664733062')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Brás de Alportel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1500418)) <= 0.00070
               AND ABS(v.longitude - (-7.8867614)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:696689154 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park', 'São Brás de Alportel', 'Faro', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.1569799, -7.8850316, 'OPENSTREETMAP', 'OSM:WAY:696689154',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:696689154')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Brás de Alportel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1569799)) <= 0.00070
               AND ABS(v.longitude - (-7.8850316)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6656003319 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The wellness place', 'São Brás de Alportel', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1556308, -7.8862256, 'OPENSTREETMAP', 'OSM:NODE:6656003319',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6656003319')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Brás de Alportel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The wellness place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1556308)) <= 0.00070
               AND ABS(v.longitude - (-7.8862256)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:765804082 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis Municipal', 'Silves', 'Faro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       37.1891999, -8.4463187, 'OPENSTREETMAP', 'OSM:NODE:765804082',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:765804082')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1891999)) <= 0.00070
               AND ABS(v.longitude - (-8.4463187)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:607506319 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro do Pinhal', 'Silves', 'Faro', NULL,
       'Tiro', 'Campo desportivo', NULL,
       37.1557598, -8.3221319, 'OPENSTREETMAP', 'OSM:WAY:607506319',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:607506319')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro do Pinhal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1557598)) <= 0.00070
               AND ABS(v.longitude - (-8.3221319)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152445533 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Messinense', 'Silves', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.2573970, -8.2894813, 'OPENSTREETMAP', 'OSM:WAY:152445533',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152445533')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Messinense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.2573970)) <= 0.00070
               AND ABS(v.longitude - (-8.2894813)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:588647212 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Armação de Pêra', 'Silves', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1083685, -8.3653146, 'OPENSTREETMAP', 'OSM:WAY:588647212',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:588647212')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Armação de Pêra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1083685)) <= 0.00070
               AND ABS(v.longitude - (-8.3653146)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:92741627 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Doutor Francisco Vieira', 'Silves', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1864018, -8.4424656, 'OPENSTREETMAP', 'OSM:WAY:92741627',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:92741627')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Doutor Francisco Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1864018)) <= 0.00070
               AND ABS(v.longitude - (-8.4424656)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:942559845 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ilha da Fantasia', 'Silves', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1248444, -8.3170473, 'OPENSTREETMAP', 'OSM:WAY:942559845',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:942559845')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ilha da Fantasia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1248444)) <= 0.00070
               AND ABS(v.longitude - (-8.3170473)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:712346384 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ilha do Tesouro', 'Silves', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1245018, -8.3173280, 'OPENSTREETMAP', 'OSM:WAY:712346384',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:712346384')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ilha do Tesouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1245018)) <= 0.00070
               AND ABS(v.longitude - (-8.3173280)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222778602 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Skate', 'Silves', 'Faro', NULL,
       'Skate', 'Campo desportivo', NULL,
       37.1853614, -8.4441777, 'OPENSTREETMAP', 'OSM:WAY:222778602',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222778602')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Skate'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1853614)) <= 0.00070
               AND ABS(v.longitude - (-8.4441777)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222781876 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Bombeiros', 'Silves', 'Faro', NULL,
       'Ginástica', 'Campo desportivo', NULL,
       37.1865382, -8.4437911, 'OPENSTREETMAP', 'OSM:WAY:222781876',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222781876')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Bombeiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1865382)) <= 0.00070
               AND ABS(v.longitude - (-8.4437911)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:215321730 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas com Escorrega', 'Silves', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1824209, -8.2806588, 'OPENSTREETMAP', 'OSM:WAY:215321730',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:215321730')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas com Escorrega'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1824209)) <= 0.00070
               AND ABS(v.longitude - (-8.2806588)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:92741585 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas municipais', 'Silves', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.1848319, -8.4435302, 'OPENSTREETMAP', 'OSM:WAY:92741585',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:92741585')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1848319)) <= 0.00070
               AND ABS(v.longitude - (-8.4435302)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:408079904 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Pêra', 'Silves', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1231778, -8.3367444, 'OPENSTREETMAP', 'OSM:WAY:408079904',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:408079904')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Pêra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1231778)) <= 0.00070
               AND ABS(v.longitude - (-8.3367444)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:585938926 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Tunes', 'Silves', 'Faro', NULL,
       'Futebol;Basquetebol;Futsal;Corrida', 'Campo desportivo', NULL,
       37.1686699, -8.2569595, 'OPENSTREETMAP', 'OSM:WAY:585938926',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:585938926')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Tunes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1686699)) <= 0.00070
               AND ABS(v.longitude - (-8.2569595)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1355652740 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Rio dos Côcos', 'Silves', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1234025, -8.3174525, 'OPENSTREETMAP', 'OSM:WAY:1355652740',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1355652740')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Silves'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Rio dos Côcos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1234025)) <= 0.00070
               AND ABS(v.longitude - (-8.3174525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:78546923 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Ataíde de Oliveira', 'Tavira', 'Faro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       37.0889013, -7.7088821, 'OPENSTREETMAP', 'OSM:WAY:78546923',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:78546923')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tavira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Ataíde de Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0889013)) <= 0.00070
               AND ABS(v.longitude - (-7.7088821)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:111753104 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Aldeamento Pedras da Rainha', 'Tavira', 'Faro', NULL,
       'Natação', 'Piscina', NULL,
       37.1386403, -7.6023232, 'OPENSTREETMAP', 'OSM:WAY:111753104',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:111753104')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tavira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Aldeamento Pedras da Rainha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1386403)) <= 0.00070
               AND ABS(v.longitude - (-7.6023232)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:110517819 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Tavira', 'Tavira', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.1313098, -7.6408879, 'OPENSTREETMAP', 'OSM:WAY:110517819',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:110517819')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tavira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Tavira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1313098)) <= 0.00070
               AND ABS(v.longitude - (-7.6408879)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:111752969 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Cabanas de Tavira', 'Tavira', 'Faro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       37.1391048, -7.6010881, 'OPENSTREETMAP', 'OSM:WAY:111752969',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:111752969')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tavira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Cabanas de Tavira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1391048)) <= 0.00070
               AND ABS(v.longitude - (-7.6010881)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1222792478 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quinta de Cima', 'Tavira', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.1596017, -7.5735821, 'OPENSTREETMAP', 'OSM:WAY:1222792478',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1222792478')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tavira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quinta de Cima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1596017)) <= 0.00070
               AND ABS(v.longitude - (-7.5735821)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:236346447 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Rockets Netball', 'Tavira', 'Faro', NULL,
       'Andebol', 'Campo desportivo', NULL,
       37.1038918, -7.6651387, 'OPENSTREETMAP', 'OSM:WAY:236346447',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:236346447')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tavira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Rockets Netball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1038918)) <= 0.00070
               AND ABS(v.longitude - (-7.6651387)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:329774872 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo 1º de Maio', 'Vila do Bispo', 'Faro', NULL,
       'Futebol', 'Estádio', NULL,
       37.0849751, -8.9117756, 'OPENSTREETMAP', 'OSM:WAY:329774872',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:329774872')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Bispo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.0849751)) <= 0.00070
               AND ABS(v.longitude - (-8.9117756)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:492085024 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '5A', 'Vila Real de Santo António', 'Faro', NULL,
       'Golfe', 'Campo desportivo', NULL,
       37.1540182, -7.5625081, 'OPENSTREETMAP', 'OSM:WAY:492085024',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:492085024')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real de Santo António'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('5A'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1540182)) <= 0.00070
               AND ABS(v.longitude - (-7.5625081)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:80653798 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Área Municipal de Lançamentos - Vila Real de Santo António', 'Vila Real de Santo António', 'Faro', NULL,
       'Atletismo', 'Campo desportivo', NULL,
       37.1889459, -7.4217863, 'OPENSTREETMAP', 'OSM:WAY:80653798',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:80653798')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real de Santo António'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Área Municipal de Lançamentos - Vila Real de Santo António'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1889459)) <= 0.00070
               AND ABS(v.longitude - (-7.4217863)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6631722139 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Hortense', 'Vila Real de Santo António', 'Faro', NULL,
       'Ginásio', 'Ginásio', NULL,
       37.1952006, -7.4286055, 'OPENSTREETMAP', 'OSM:NODE:6631722139',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6631722139')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real de Santo António'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Hortense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1952006)) <= 0.00070
               AND ABS(v.longitude - (-7.4286055)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:80653785 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Vila Real de Santo António', 'Vila Real de Santo António', 'Faro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       37.1902681, -7.4203442, 'OPENSTREETMAP', 'OSM:WAY:80653785',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:80653785')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real de Santo António'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Vila Real de Santo António'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1902681)) <= 0.00070
               AND ABS(v.longitude - (-7.4203442)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:663038258 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Robinson Club Quinta da Ria', 'Vila Real de Santo António', 'Faro', NULL,
       'Golfe', 'Campo de golfe', NULL,
       37.1541418, -7.5658049, 'OPENSTREETMAP', 'OSM:WAY:663038258',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:663038258')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real de Santo António'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Robinson Club Quinta da Ria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (37.1541418)) <= 0.00070
               AND ABS(v.longitude - (-7.5658049)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
