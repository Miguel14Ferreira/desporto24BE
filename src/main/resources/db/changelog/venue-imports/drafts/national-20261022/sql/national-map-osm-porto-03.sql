-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:2367527052 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ténis', 'Póvoa de Varzim', 'Porto', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       41.3871088, -8.7737738, 'OPENSTREETMAP', 'OSM:NODE:2367527052',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2367527052')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3871088)) <= 0.00070
               AND ABS(v.longitude - (-8.7737738)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:771484912 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Varzim Sport Club', 'Póvoa de Varzim', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3877070, -8.7725380, 'OPENSTREETMAP', 'OSM:WAY:771484912',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:771484912')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Póvoa de Varzim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Varzim Sport Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3877070)) <= 0.00070
               AND ABS(v.longitude - (-8.7725380)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150613273 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva Carreira', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3088445, -8.4626544, 'OPENSTREETMAP', 'OSM:WAY:150613273',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150613273')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva Carreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3088445)) <= 0.00070
               AND ABS(v.longitude - (-8.4626544)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:38331465 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Abílio Ferreira de Oliveira', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.3605301, -8.3684334, 'OPENSTREETMAP', 'OSM:WAY:38331465',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:38331465')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Abílio Ferreira de Oliveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3605301)) <= 0.00070
               AND ABS(v.longitude - (-8.3684334)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108052372 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de São Mamede de Negrelos', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3454192, -8.3564830, 'OPENSTREETMAP', 'OSM:WAY:108052372',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108052372')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de São Mamede de Negrelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3454192)) <= 0.00070
               AND ABS(v.longitude - (-8.3564830)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:686208390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Lazer do Lar da Tranquilidade', 'Santo Tirso', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.3700914, -8.4106183, 'OPENSTREETMAP', 'OSM:WAY:686208390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:686208390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Lazer do Lar da Tranquilidade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3700914)) <= 0.00070
               AND ABS(v.longitude - (-8.4106183)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:304162881 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube do Rio', 'Santo Tirso', 'Porto', NULL,
       'Ginásio;Natação;Halterofilismo', 'Ginásio', NULL,
       41.3622768, -8.3949983, 'OPENSTREETMAP', 'OSM:WAY:304162881',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:304162881')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube do Rio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3622768)) <= 0.00070
               AND ABS(v.longitude - (-8.3949983)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:681684438 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Clube Desportivo das Aves', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.3618393, -8.4105230, 'OPENSTREETMAP', 'OSM:WAY:681684438',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:681684438')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Clube Desportivo das Aves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3618393)) <= 0.00070
               AND ABS(v.longitude - (-8.4105230)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:845441281 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crossfit Vale do Ave', 'Santo Tirso', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3650543, -8.3992378, 'OPENSTREETMAP', 'OSM:WAY:845441281',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:845441281')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crossfit Vale do Ave'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3650543)) <= 0.00070
               AND ABS(v.longitude - (-8.3992378)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1149107383 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio de Areias', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3649706, -8.4749902, 'OPENSTREETMAP', 'OSM:WAY:1149107383',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1149107383')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio de Areias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3649706)) <= 0.00070
               AND ABS(v.longitude - (-8.4749902)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1206431139 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Clube Desportivo das Aves', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.3671853, -8.4115752, 'OPENSTREETMAP', 'OSM:WAY:1206431139',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1206431139')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Clube Desportivo das Aves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3671853)) <= 0.00070
               AND ABS(v.longitude - (-8.4115752)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:40403404 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Vilarinho', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3732759, -8.3394365, 'OPENSTREETMAP', 'OSM:WAY:40403404',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:40403404')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Vilarinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3732759)) <= 0.00070
               AND ABS(v.longitude - (-8.3394365)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:171274423 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Vilarinho', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3092276, -8.4926050, 'OPENSTREETMAP', 'OSM:WAY:171274423',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:171274423')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Vilarinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3092276)) <= 0.00070
               AND ABS(v.longitude - (-8.4926050)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:602939523 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo da Mouteira', 'Santo Tirso', 'Porto', NULL,
       'Futebol;Skate', 'Complexo desportivo', NULL,
       41.2697395, -8.4589043, 'OPENSTREETMAP', 'OSM:WAY:602939523',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:602939523')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo da Mouteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2697395)) <= 0.00070
               AND ABS(v.longitude - (-8.4589043)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:40550576 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Clube Desportivo das Aves', 'Santo Tirso', 'Porto', NULL,
       'Futebol;Voleibol;Futsal', 'Complexo desportivo', NULL,
       41.3670351, -8.4102756, 'OPENSTREETMAP', 'OSM:WAY:40550576',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:40550576')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Clube Desportivo das Aves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3670351)) <= 0.00070
               AND ABS(v.longitude - (-8.4102756)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:814653604 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Água Longa', 'Santo Tirso', 'Porto', NULL,
       'Futebol;Ténis;Andebol;Basquetebol', 'Campo desportivo', NULL,
       41.2621172, -8.4895592, 'OPENSTREETMAP', 'OSM:WAY:814653604',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:814653604')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Água Longa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2621172)) <= 0.00070
               AND ABS(v.longitude - (-8.4895592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108132107 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Espinho', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3651631, -8.3789582, 'OPENSTREETMAP', 'OSM:WAY:108132107',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108132107')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Espinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3651631)) <= 0.00070
               AND ABS(v.longitude - (-8.3789582)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108329639 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de São Mamede', 'Santo Tirso', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3477862, -8.3482092, 'OPENSTREETMAP', 'OSM:WAY:108329639',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108329639')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santo Tirso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de São Mamede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3477862)) <= 0.00070
               AND ABS(v.longitude - (-8.3482092)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152785522 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Atlético Clube Bougadense', 'Trofa', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.3265197, -8.5719486, 'OPENSTREETMAP', 'OSM:WAY:1152785522',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152785522')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Atlético Clube Bougadense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3265197)) <= 0.00070
               AND ABS(v.longitude - (-8.5719486)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:301852500 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Alvarelhos', 'Trofa', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3120652, -8.6137955, 'OPENSTREETMAP', 'OSM:WAY:301852500',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:301852500')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Alvarelhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3120652)) <= 0.00070
               AND ABS(v.longitude - (-8.6137955)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:301850667 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Guidões F.C.', 'Trofa', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3243915, -8.6165601, 'OPENSTREETMAP', 'OSM:WAY:301850667',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:301850667')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Guidões F.C.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3243915)) <= 0.00070
               AND ABS(v.longitude - (-8.6165601)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182214662 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Jogos São Mamede Coronado', 'Trofa', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2715121, -8.5797905, 'OPENSTREETMAP', 'OSM:WAY:182214662',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182214662')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Jogos São Mamede Coronado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2715121)) <= 0.00070
               AND ABS(v.longitude - (-8.5797905)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:749600956 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo CD Trofense', 'Trofa', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3218041, -8.5402136, 'OPENSTREETMAP', 'OSM:WAY:749600956',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:749600956')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo CD Trofense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3218041)) <= 0.00070
               AND ABS(v.longitude - (-8.5402136)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8866573223 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up', 'Trofa', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3409215, -8.5524629, 'OPENSTREETMAP', 'OSM:NODE:8866573223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8866573223')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3409215)) <= 0.00070
               AND ABS(v.longitude - (-8.5524629)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:209439838 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Health Club Complexo Tropical', 'Trofa', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.3418144, -8.5579938, 'OPENSTREETMAP', 'OSM:WAY:209439838',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:209439838')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trofa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Health Club Complexo Tropical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3418144)) <= 0.00070
               AND ABS(v.longitude - (-8.5579938)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151268922 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Atlético Clube Alfenense', 'Valongo', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2314248, -8.5390301, 'OPENSTREETMAP', 'OSM:WAY:151268922',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151268922')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Atlético Clube Alfenense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2314248)) <= 0.00070
               AND ABS(v.longitude - (-8.5390301)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4775158826 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Befit', 'Valongo', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2050346, -8.5509984, 'OPENSTREETMAP', 'OSM:NODE:4775158826',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4775158826')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Befit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2050346)) <= 0.00070
               AND ABS(v.longitude - (-8.5509984)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150639348 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos Fijós', 'Valongo', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2124428, -8.4626519, 'OPENSTREETMAP', 'OSM:WAY:150639348',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150639348')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos Fijós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2124428)) <= 0.00070
               AND ABS(v.longitude - (-8.4626519)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:100897454 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Dragon Force Ermesinde', 'Valongo', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2064969, -8.5445383, 'OPENSTREETMAP', 'OSM:WAY:100897454',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:100897454')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Dragon Force Ermesinde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2064969)) <= 0.00070
               AND ABS(v.longitude - (-8.5445383)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54664513 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Calvário', 'Valongo', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1960409, -8.5025459, 'OPENSTREETMAP', 'OSM:WAY:54664513',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54664513')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Calvário'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1960409)) <= 0.00070
               AND ABS(v.longitude - (-8.5025459)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108271399 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Campo Futebol Clube', 'Valongo', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1748283, -8.4599751, 'OPENSTREETMAP', 'OSM:WAY:108271399',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108271399')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Campo Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1748283)) <= 0.00070
               AND ABS(v.longitude - (-8.4599751)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:864734824 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio S. Martinho de Campo', 'Valongo', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1751685, -8.4602642, 'OPENSTREETMAP', 'OSM:WAY:864734824',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:864734824')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio S. Martinho de Campo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1751685)) <= 0.00070
               AND ABS(v.longitude - (-8.4602642)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54664531 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Ginmodesportivo Municipal de Valongo', 'Valongo', 'Porto', NULL,
       'Hóquei', 'Complexo desportivo', NULL,
       41.1949162, -8.5010134, 'OPENSTREETMAP', 'OSM:WAY:54664531',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54664531')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Ginmodesportivo Municipal de Valongo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1949162)) <= 0.00070
               AND ABS(v.longitude - (-8.5010134)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1274475995 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Alfena', 'Valongo', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2244454, -8.5275940, 'OPENSTREETMAP', 'OSM:WAY:1274475995',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1274475995')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Alfena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2244454)) <= 0.00070
               AND ABS(v.longitude - (-8.5275940)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151430907 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Ermesinde', 'Valongo', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.2110558, -8.5495894, 'OPENSTREETMAP', 'OSM:WAY:151430907',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151430907')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Ermesinde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2110558)) <= 0.00070
               AND ABS(v.longitude - (-8.5495894)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54664529 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Valongo', 'Valongo', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1944095, -8.5008657, 'OPENSTREETMAP', 'OSM:WAY:54664529',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54664529')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Valongo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Valongo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1944095)) <= 0.00070
               AND ABS(v.longitude - (-8.5008657)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:814899470 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquetebol B.D.P', 'Vila do Conde', 'Porto', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       41.3578102, -8.7527348, 'OPENSTREETMAP', 'OSM:WAY:814899470',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:814899470')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquetebol B.D.P'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3578102)) <= 0.00070
               AND ABS(v.longitude - (-8.7527348)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:696154463 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basquetebol das Dálias', 'Vila do Conde', 'Porto', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       41.3718051, -8.7578959, 'OPENSTREETMAP', 'OSM:WAY:696154463',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:696154463')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basquetebol das Dálias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3718051)) <= 0.00070
               AND ABS(v.longitude - (-8.7578959)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:814900169 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol B.D.P', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3580073, -8.7547174, 'OPENSTREETMAP', 'OSM:WAY:814900169',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:814900169')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol B.D.P'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3580073)) <= 0.00070
               AND ABS(v.longitude - (-8.7547174)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825384192 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Touguinha', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3710556, -8.7210798, 'OPENSTREETMAP', 'OSM:WAY:825384192',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825384192')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Touguinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3710556)) <= 0.00070
               AND ABS(v.longitude - (-8.7210798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:255677652 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Eng. Mário Almeida', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3976816, -8.6507962, 'OPENSTREETMAP', 'OSM:WAY:255677652',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:255677652')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Eng. Mário Almeida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3976816)) <= 0.00070
               AND ABS(v.longitude - (-8.6507962)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:914846900 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Malta', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2972003, -8.6607365, 'OPENSTREETMAP', 'OSM:WAY:914846900',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:914846900')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Malta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2972003)) <= 0.00070
               AND ABS(v.longitude - (-8.6607365)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:191828449 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos do Castelo', 'Vila do Conde', 'Porto', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       41.3419739, -8.7491841, 'OPENSTREETMAP', 'OSM:WAY:191828449',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:191828449')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos do Castelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3419739)) <= 0.00070
               AND ABS(v.longitude - (-8.7491841)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:407493294 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Arões', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.2839199, -8.6520098, 'OPENSTREETMAP', 'OSM:WAY:407493294',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:407493294')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Arões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2839199)) <= 0.00070
               AND ABS(v.longitude - (-8.6520098)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:331965606 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Minicipais de Vila do Conde - Pólo II (Mindelo)', 'Vila do Conde', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.3090095, -8.7208544, 'OPENSTREETMAP', 'OSM:WAY:331965606',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:331965606')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Minicipais de Vila do Conde - Pólo II (Mindelo)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3090095)) <= 0.00070
               AND ABS(v.longitude - (-8.7208544)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:75701117 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Vila do Conde', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.3458585, -8.7460949, 'OPENSTREETMAP', 'OSM:WAY:75701117',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:75701117')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3458585)) <= 0.00070
               AND ABS(v.longitude - (-8.7460949)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:795834553 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Árvore', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3317161, -8.7226475, 'OPENSTREETMAP', 'OSM:WAY:795834553',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:795834553')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Árvore'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3317161)) <= 0.00070
               AND ABS(v.longitude - (-8.7226475)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:266454577 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Macieira da Maia', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3407526, -8.6776592, 'OPENSTREETMAP', 'OSM:WAY:266454577',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:266454577')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Macieira da Maia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3407526)) <= 0.00070
               AND ABS(v.longitude - (-8.6776592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:397645146 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Rio Ave Futebol Clube', 'Vila do Conde', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3627922, -8.7401936, 'OPENSTREETMAP', 'OSM:WAY:397645146',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:397645146')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Rio Ave Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3627922)) <= 0.00070
               AND ABS(v.longitude - (-8.7401936)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:250330311 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SPUMP - Health Club & Spa', 'Vila do Conde', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3303983, -8.7188054, 'OPENSTREETMAP', 'OSM:WAY:250330311',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:250330311')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SPUMP - Health Club & Spa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3303983)) <= 0.00070
               AND ABS(v.longitude - (-8.7188054)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2279603323 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Stress Out', 'Vila do Conde', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3482039, -8.7468342, 'OPENSTREETMAP', 'OSM:NODE:2279603323',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2279603323')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Stress Out'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3482039)) <= 0.00070
               AND ABS(v.longitude - (-8.7468342)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13069434222 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vilafit', 'Vila do Conde', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3509474, -8.7469658, 'OPENSTREETMAP', 'OSM:NODE:13069434222',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13069434222')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila do Conde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vilafit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3509474)) <= 0.00070
               AND ABS(v.longitude - (-8.7469658)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5084234338 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia 111', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1182976, -8.5999145, 'OPENSTREETMAP', 'OSM:NODE:5084234338',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5084234338')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia 111'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1182976)) <= 0.00070
               AND ABS(v.longitude - (-8.5999145)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182878190 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Serzedo', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0546629, -8.6174014, 'OPENSTREETMAP', 'OSM:WAY:182878190',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182878190')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Serzedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0546629)) <= 0.00070
               AND ABS(v.longitude - (-8.6174014)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:161129411 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos do Futebol Clube de Crestuma', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.0586465, -8.4985398, 'OPENSTREETMAP', 'OSM:WAY:161129411',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:161129411')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos do Futebol Clube de Crestuma'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0586465)) <= 0.00070
               AND ABS(v.longitude - (-8.4985398)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:865458423 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos dos Dragões Sandinenses', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0419096, -8.5167335, 'OPENSTREETMAP', 'OSM:WAY:865458423',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:865458423')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos dos Dragões Sandinenses'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0419096)) <= 0.00070
               AND ABS(v.longitude - (-8.5167335)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:469686801 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Cedro', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1092668, -8.6093385, 'OPENSTREETMAP', 'OSM:WAY:469686801',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:469686801')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Cedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1092668)) <= 0.00070
               AND ABS(v.longitude - (-8.6093385)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:469722465 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Cedro', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1131380, -8.6100597, 'OPENSTREETMAP', 'OSM:WAY:469722465',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:469722465')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Cedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1131380)) <= 0.00070
               AND ABS(v.longitude - (-8.6100597)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:193298877 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Sintético de Canelas', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0826800, -8.6055975, 'OPENSTREETMAP', 'OSM:WAY:193298877',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:193298877')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Sintético de Canelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0826800)) <= 0.00070
               AND ABS(v.longitude - (-8.6055975)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1306925839 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Vila Nova de Gaia', 'Vila Nova de Gaia', 'Porto', NULL,
       'Taekwondo;Ténis de mesa', 'Complexo desportivo', NULL,
       41.1181071, -8.5937727, 'OPENSTREETMAP', 'OSM:WAY:1306925839',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1306925839')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Vila Nova de Gaia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1181071)) <= 0.00070
               AND ABS(v.longitude - (-8.5937727)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440386696 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Municipal de Formação Desportiva Rui Jorge', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1130223, -8.5877077, 'OPENSTREETMAP', 'OSM:WAY:440386696',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440386696')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Municipal de Formação Desportiva Rui Jorge'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1130223)) <= 0.00070
               AND ABS(v.longitude - (-8.5877077)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:117217754 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Futebol Canelas 2010', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0818498, -8.6057354, 'OPENSTREETMAP', 'OSM:WAY:117217754',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:117217754')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Futebol Canelas 2010'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0818498)) <= 0.00070
               AND ABS(v.longitude - (-8.6057354)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54934693 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Hóquei dos Carvalhos', 'Vila Nova de Gaia', 'Porto', NULL,
       'Hóquei em Campo', 'Complexo desportivo', NULL,
       41.0643534, -8.5689775, 'OPENSTREETMAP', 'OSM:WAY:54934693',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54934693')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Hóquei dos Carvalhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0643534)) <= 0.00070
               AND ABS(v.longitude - (-8.5689775)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:459348869 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube União Desportiva Leverense', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0681983, -8.4772282, 'OPENSTREETMAP', 'OSM:WAY:459348869',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:459348869')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube União Desportiva Leverense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0681983)) <= 0.00070
               AND ABS(v.longitude - (-8.4772282)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237743022 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo São Félix da Marinha', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0424096, -8.6249296, 'OPENSTREETMAP', 'OSM:WAY:237743022',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237743022')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo São Félix da Marinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0424096)) <= 0.00070
               AND ABS(v.longitude - (-8.6249296)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108444947 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Futebol Clube de Pedroso', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0676848, -8.5477705, 'OPENSTREETMAP', 'OSM:WAY:108444947',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108444947')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Futebol Clube de Pedroso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0676848)) <= 0.00070
               AND ABS(v.longitude - (-8.5477705)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:192225194 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Gulpilhares', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.0751472, -8.6186279, 'OPENSTREETMAP', 'OSM:WAY:192225194',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:192225194')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Gulpilhares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0751472)) <= 0.00070
               AND ABS(v.longitude - (-8.6186279)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:865458422 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Sport Clube Dragões Sandinenses', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0404873, -8.5172480, 'OPENSTREETMAP', 'OSM:WAY:865458422',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:865458422')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Sport Clube Dragões Sandinenses'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0404873)) <= 0.00070
               AND ABS(v.longitude - (-8.5172480)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:128432325 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Futebol Clube de Avintes', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1028783, -8.5551858, 'OPENSTREETMAP', 'OSM:WAY:128432325',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:128432325')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Futebol Clube de Avintes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1028783)) <= 0.00070
               AND ABS(v.longitude - (-8.5551858)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108444948 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Jorge Sampaio', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.0672148, -8.5448786, 'OPENSTREETMAP', 'OSM:WAY:108444948',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108444948')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Jorge Sampaio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0672148)) <= 0.00070
               AND ABS(v.longitude - (-8.5448786)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:209604213 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Arcozelo', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.0499813, -8.6365151, 'OPENSTREETMAP', 'OSM:WAY:209604213',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:209604213')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Arcozelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0499813)) <= 0.00070
               AND ABS(v.longitude - (-8.6365151)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:99016657 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Rei Ramiro', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Estádio', NULL,
       41.1405338, -8.6306209, 'OPENSTREETMAP', 'OSM:WAY:99016657',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:99016657')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Rei Ramiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1405338)) <= 0.00070
               AND ABS(v.longitude - (-8.6306209)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4754579194 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitnessgim', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1195660, -8.6093251, 'OPENSTREETMAP', 'OSM:NODE:4754579194',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4754579194')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitnessgim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1195660)) <= 0.00070
               AND ABS(v.longitude - (-8.6093251)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5317229938 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitstudio', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1155465, -8.6076943, 'OPENSTREETMAP', 'OSM:NODE:5317229938',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5317229938')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitstudio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1155465)) <= 0.00070
               AND ABS(v.longitude - (-8.6076943)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1425035500 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Crestuma', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0550553, -8.5020067, 'OPENSTREETMAP', 'OSM:WAY:1425035500',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1425035500')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Crestuma'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0550553)) <= 0.00070
               AND ABS(v.longitude - (-8.5020067)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:197101629 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube de Gaia', 'Vila Nova de Gaia', 'Porto', NULL,
       'Andebol', 'Complexo desportivo', NULL,
       41.1277737, -8.6089110, 'OPENSTREETMAP', 'OSM:WAY:197101629',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:197101629')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube de Gaia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1277737)) <= 0.00070
               AND ABS(v.longitude - (-8.6089110)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4799323277 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gimnocedro', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1154790, -8.6082548, 'OPENSTREETMAP', 'OSM:NODE:4799323277',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4799323277')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gimnocedro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1154790)) <= 0.00070
               AND ABS(v.longitude - (-8.6082548)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1466864775 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GymStar', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1375606, -8.6390702, 'OPENSTREETMAP', 'OSM:WAY:1466864775',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1466864775')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GymStar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1375606)) <= 0.00070
               AND ABS(v.longitude - (-8.6390702)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:264439768 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1327475, -8.6264615, 'OPENSTREETMAP', 'OSM:WAY:264439768',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:264439768')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1327475)) <= 0.00070
               AND ABS(v.longitude - (-8.6264615)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:489435560 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Life Club', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1210045, -8.6011527, 'OPENSTREETMAP', 'OSM:WAY:489435560',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:489435560')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Life Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1210045)) <= 0.00070
               AND ABS(v.longitude - (-8.6011527)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:781951061 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mais Fit Premium Low Cost', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1312928, -8.6466442, 'OPENSTREETMAP', 'OSM:WAY:781951061',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:781951061')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mais Fit Premium Low Cost'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1312928)) <= 0.00070
               AND ABS(v.longitude - (-8.6466442)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9985968310 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Paddling Center', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1412140, -8.6509077, 'OPENSTREETMAP', 'OSM:NODE:9985968310',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9985968310')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Paddling Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1412140)) <= 0.00070
               AND ABS(v.longitude - (-8.6509077)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196511095 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos Francisco Vieira', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0867956, -8.5925966, 'OPENSTREETMAP', 'OSM:WAY:196511095',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196511095')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos Francisco Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0867956)) <= 0.00070
               AND ABS(v.longitude - (-8.5925966)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1414911881 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Visconde Pereira Machado', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol;Ténis;Corrida;Voleibol', 'Complexo desportivo', NULL,
       41.0742052, -8.6019054, 'OPENSTREETMAP', 'OSM:WAY:1414911881',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1414911881')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Visconde Pereira Machado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0742052)) <= 0.00070
               AND ABS(v.longitude - (-8.6019054)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1201707034 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina e Ginásio VP', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Ginásio', NULL,
       41.1074409, -8.6219184, 'OPENSTREETMAP', 'OSM:WAY:1201707034',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1201707034')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina e Ginásio VP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1074409)) <= 0.00070
               AND ABS(v.longitude - (-8.6219184)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:865453818 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Aurora Cunha', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.0678049, -8.5487748, 'OPENSTREETMAP', 'OSM:WAY:865453818',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:865453818')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Aurora Cunha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0678049)) <= 0.00070
               AND ABS(v.longitude - (-8.5487748)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151729655 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Granja Descoberta', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.0412023, -8.6504848, 'OPENSTREETMAP', 'OSM:WAY:151729655',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151729655')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Granja Descoberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0412023)) <= 0.00070
               AND ABS(v.longitude - (-8.6504848)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:151729225 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Granja Semiaberta', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Piscina', NULL,
       41.0418975, -8.6504655, 'OPENSTREETMAP', 'OSM:WAY:151729225',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:151729225')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Granja Semiaberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0418975)) <= 0.00070
               AND ABS(v.longitude - (-8.6504655)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:283426759 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vila D''Este', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0972676, -8.5907273, 'OPENSTREETMAP', 'OSM:WAY:283426759',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:283426759')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vila D''Este'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0972676)) <= 0.00070
               AND ABS(v.longitude - (-8.5907273)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1138266800 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Lever', 'Vila Nova de Gaia', 'Porto', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0652385, -8.4731205, 'OPENSTREETMAP', 'OSM:WAY:1138266800',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1138266800')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Lever'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0652385)) <= 0.00070
               AND ABS(v.longitude - (-8.4731205)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:823590980 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Pedroso', 'Vila Nova de Gaia', 'Porto', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.0671998, -8.5448148, 'OPENSTREETMAP', 'OSM:WAY:823590980',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823590980')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Pedroso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0671998)) <= 0.00070
               AND ABS(v.longitude - (-8.5448148)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:482653956 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Quinta das Rosas', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.1085742, -8.6161697, 'OPENSTREETMAP', 'OSM:WAY:482653956',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:482653956')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Quinta das Rosas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1085742)) <= 0.00070
               AND ABS(v.longitude - (-8.6161697)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:202566281 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de São Caetano', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0950523, -8.6122159, 'OPENSTREETMAP', 'OSM:WAY:202566281',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:202566281')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de São Caetano'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0950523)) <= 0.00070
               AND ABS(v.longitude - (-8.6122159)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11406499418 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1212810, -8.6188911, 'OPENSTREETMAP', 'OSM:NODE:11406499418',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11406499418')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1212810)) <= 0.00070
               AND ABS(v.longitude - (-8.6188911)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1049450101 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube de Arcozelo', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0499406, -8.6362844, 'OPENSTREETMAP', 'OSM:WAY:1049450101',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1049450101')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube de Arcozelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0499406)) <= 0.00070
               AND ABS(v.longitude - (-8.6362844)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153688051 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Valadares Gaia Futebol Clube', 'Vila Nova de Gaia', 'Porto', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0977846, -8.6399870, 'OPENSTREETMAP', 'OSM:WAY:153688051',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153688051')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Valadares Gaia Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0977846)) <= 0.00070
               AND ABS(v.longitude - (-8.6399870)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4754579192 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Villa Fitness', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1033823, -8.6186247, 'OPENSTREETMAP', 'OSM:NODE:4754579192',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4754579192')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Villa Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1033823)) <= 0.00070
               AND ABS(v.longitude - (-8.6186247)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14111920801 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Vila Nova de Gaia', 'Porto', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.1211964, -8.6259668, 'OPENSTREETMAP', 'OSM:NODE:14111920801',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14111920801')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Gaia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1211964)) <= 0.00070
               AND ABS(v.longitude - (-8.6259668)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
