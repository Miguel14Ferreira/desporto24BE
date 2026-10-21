-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:504737328 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Caldas Sport Club', 'Caldas da Rainha', 'Leiria', NULL,
       'Futebol', 'Estádio', NULL,
       39.4032322, -9.1263873, 'OPENSTREETMAP', 'OSM:WAY:504737328',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:504737328')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Caldas Sport Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4032322)) <= 0.00070
               AND ABS(v.longitude - (-9.1263873)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1291912464 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio CAR', 'Caldas da Rainha', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.4113373, -9.1504067, 'OPENSTREETMAP', 'OSM:WAY:1291912464',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1291912464')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio CAR'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4113373)) <= 0.00070
               AND ABS(v.longitude - (-9.1504067)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:112105878 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Raul Proença', 'Caldas da Rainha', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.4116324, -9.1462790, 'OPENSTREETMAP', 'OSM:WAY:112105878',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:112105878')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Raul Proença'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4116324)) <= 0.00070
               AND ABS(v.longitude - (-9.1462790)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:277723751 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Caldas da Rainha', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.4116946, -9.1526570, 'OPENSTREETMAP', 'OSM:WAY:277723751',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:277723751')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4116946)) <= 0.00070
               AND ABS(v.longitude - (-9.1526570)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4156528750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Queens 2', 'Caldas da Rainha', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.4093434, -9.1341006, 'OPENSTREETMAP', 'OSM:NODE:4156528750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4156528750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Queens 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4093434)) <= 0.00070
               AND ABS(v.longitude - (-9.1341006)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11162046405 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be-Fit Leiria', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7304646, -8.8273328, 'OPENSTREETMAP', 'OSM:NODE:11162046405',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11162046405')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be-Fit Leiria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7304646)) <= 0.00070
               AND ABS(v.longitude - (-8.8273328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12814612760 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CrossFit', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7486045, -8.8072234, 'OPENSTREETMAP', 'OSM:NODE:12814612760',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12814612760')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CrossFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7486045)) <= 0.00070
               AND ABS(v.longitude - (-8.8072234)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:516503570 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CrossFit', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7294942, -8.8050262, 'OPENSTREETMAP', 'OSM:WAY:516503570',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:516503570')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CrossFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7294942)) <= 0.00070
               AND ABS(v.longitude - (-8.8050262)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2135934353 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Racket Sports Club Leiria', 'Leiria', 'Leiria', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       39.7411094, -8.7527979, 'OPENSTREETMAP', 'OSM:NODE:2135934353',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2135934353')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Racket Sports Club Leiria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7411094)) <= 0.00070
               AND ABS(v.longitude - (-8.7527979)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12968205840 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Xplose Hyrox', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7619902, -8.7822525, 'OPENSTREETMAP', 'OSM:NODE:12968205840',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12968205840')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Xplose Hyrox'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7619902)) <= 0.00070
               AND ABS(v.longitude - (-8.7822525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4984717757 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga de Leiria', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7384539, -8.8077107, 'OPENSTREETMAP', 'OSM:NODE:4984717757',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4984717757')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga de Leiria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7384539)) <= 0.00070
               AND ABS(v.longitude - (-8.8077107)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9813251996 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bpilates Studyo Yoga & Pilates', 'Nazaré', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.5945492, -9.0723074, 'OPENSTREETMAP', 'OSM:NODE:9813251996',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9813251996')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Nazaré'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bpilates Studyo Yoga & Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5945492)) <= 0.00070
               AND ABS(v.longitude - (-9.0723074)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1359325474 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Guardian Bom Susseco Golf', 'Óbidos', 'Leiria', NULL,
       'Golfe', 'Campo de golfe', NULL,
       39.3813999, -9.2410495, 'OPENSTREETMAP', 'OSM:WAY:1359325474',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1359325474')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Guardian Bom Susseco Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3813999)) <= 0.00070
               AND ABS(v.longitude - (-9.2410495)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1378453661 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Padel de Serra D''El Rei', 'Peniche', 'Leiria', NULL,
       'Padel', 'Campo desportivo', NULL,
       39.3398213, -9.2716391, 'OPENSTREETMAP', 'OSM:WAY:1378453661',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1378453661')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Padel de Serra D''El Rei'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3398213)) <= 0.00070
               AND ABS(v.longitude - (-9.2716391)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:625837077 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'A.R.C.D. Mendiga', 'Porto de Mós', 'Leiria', NULL,
       'Futsal', 'Complexo desportivo', NULL,
       39.4956167, -8.8560513, 'OPENSTREETMAP', 'OSM:WAY:625837077',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:625837077')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto de Mós'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('A.R.C.D. Mendiga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4956167)) <= 0.00070
               AND ABS(v.longitude - (-8.8560513)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:373807257 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ARDCCA', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6063901, -8.9830497, 'OPENSTREETMAP', 'OSM:WAY:373807257',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:373807257')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ARDCCA'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6063901)) <= 0.00070
               AND ABS(v.longitude - (-8.9830497)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10042727099 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol 5 do Casal Pinheiro', 'Alcobaça', 'Leiria', NULL,
       'Futsal;Futebol', 'Complexo desportivo', NULL,
       39.5113717, -8.9944811, 'OPENSTREETMAP', 'OSM:NODE:10042727099',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10042727099')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol 5 do Casal Pinheiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5113717)) <= 0.00070
               AND ABS(v.longitude - (-8.9944811)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1192264267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Junta de Freguesia do Bárrio', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5495273, -9.0231211, 'OPENSTREETMAP', 'OSM:WAY:1192264267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1192264267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Junta de Freguesia do Bárrio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5495273)) <= 0.00070
               AND ABS(v.longitude - (-9.0231211)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:835087739 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Alpedriz', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6323228, -8.9475968, 'OPENSTREETMAP', 'OSM:WAY:835087739',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:835087739')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Alpedriz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6323228)) <= 0.00070
               AND ABS(v.longitude - (-8.9475968)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:835087738 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol dos Pisões', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6528114, -8.9379441, 'OPENSTREETMAP', 'OSM:WAY:835087738',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:835087738')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol dos Pisões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6528114)) <= 0.00070
               AND ABS(v.longitude - (-8.9379441)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:861075538 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo do Casal da Madeira', 'Alcobaça', 'Leiria', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       39.4846780, -9.0038224, 'OPENSTREETMAP', 'OSM:WAY:861075538',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:861075538')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo do Casal da Madeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4846780)) <= 0.00070
               AND ABS(v.longitude - (-9.0038224)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:733739900 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis do Algarão', 'Alcobaça', 'Leiria', NULL,
       'Ténis', 'Campo desportivo', NULL,
       39.4153399, -8.9901218, 'OPENSTREETMAP', 'OSM:WAY:733739900',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:733739900')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis do Algarão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4153399)) <= 0.00070
               AND ABS(v.longitude - (-8.9901218)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:202965273 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Floresta', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6667507, -9.0052225, 'OPENSTREETMAP', 'OSM:WAY:202965273',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:202965273')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Floresta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6667507)) <= 0.00070
               AND ABS(v.longitude - (-9.0052225)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:365505425 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol dos Cedros', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4719449, -9.0147003, 'OPENSTREETMAP', 'OSM:WAY:365505425',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:365505425')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol dos Cedros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4719449)) <= 0.00070
               AND ABS(v.longitude - (-9.0147003)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:500092724 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Vale do Amieiro', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5898095, -8.9649673, 'OPENSTREETMAP', 'OSM:WAY:500092724',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:500092724')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Vale do Amieiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5898095)) <= 0.00070
               AND ABS(v.longitude - (-8.9649673)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:883913376 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Azambujeira', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3955312, -8.9802160, 'OPENSTREETMAP', 'OSM:WAY:883913376',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:883913376')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Azambujeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3955312)) <= 0.00070
               AND ABS(v.longitude - (-8.9802160)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4927533662 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'DINO''S', 'Alcobaça', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.5533119, -8.9605983, 'OPENSTREETMAP', 'OSM:NODE:4927533662',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4927533662')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('DINO''S'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5533119)) <= 0.00070
               AND ABS(v.longitude - (-8.9605983)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6874194942 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Dino’s', 'Alcobaça', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.4230221, -8.9752634, 'OPENSTREETMAP', 'OSM:NODE:6874194942',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6874194942')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Dino’s'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4230221)) <= 0.00070
               AND ABS(v.longitude - (-8.9752634)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:281953906 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Eira das Chãs', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5822307, -8.9706047, 'OPENSTREETMAP', 'OSM:WAY:281953906',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:281953906')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Eira das Chãs'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5822307)) <= 0.00070
               AND ABS(v.longitude - (-8.9706047)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:277427532 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio de Futebol do ABCD', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4281538, -8.9841471, 'OPENSTREETMAP', 'OSM:WAY:277427532',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:277427532')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio de Futebol do ABCD'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4281538)) <= 0.00070
               AND ABS(v.longitude - (-8.9841471)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:276477940 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Alcobaça', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Estádio', NULL,
       39.5528647, -8.9729295, 'OPENSTREETMAP', 'OSM:WAY:276477940',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:276477940')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Alcobaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5528647)) <= 0.00070
               AND ABS(v.longitude - (-8.9729295)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:347893031 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory Alcobaça', 'Alcobaça', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.5514795, -8.9683854, 'OPENSTREETMAP', 'OSM:WAY:347893031',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:347893031')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory Alcobaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5514795)) <= 0.00070
               AND ABS(v.longitude - (-8.9683854)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:497484189 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Alcobaça', 'Alcobaça', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.5541674, -8.9754543, 'OPENSTREETMAP', 'OSM:WAY:497484189',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:497484189')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Alcobaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5541674)) <= 0.00070
               AND ABS(v.longitude - (-8.9754543)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:496198000 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Pataias', 'Alcobaça', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.6759324, -9.0088582, 'OPENSTREETMAP', 'OSM:WAY:496198000',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:496198000')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Pataias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6759324)) <= 0.00070
               AND ABS(v.longitude - (-9.0088582)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:302626801 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais da Benedita', 'Alcobaça', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.4264296, -8.9774233, 'OPENSTREETMAP', 'OSM:WAY:302626801',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:302626801')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais da Benedita'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4264296)) <= 0.00070
               AND ABS(v.longitude - (-8.9774233)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825282469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Alcobaça', 'Alcobaça', 'Leiria', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.5527550, -8.9728857, 'OPENSTREETMAP', 'OSM:WAY:825282469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825282469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Alcobaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5527550)) <= 0.00070
               AND ABS(v.longitude - (-8.9728857)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7638673502 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Alcobaça', 'Alcobaça', 'Leiria', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.5467378, -8.9696198, 'OPENSTREETMAP', 'OSM:NODE:7638673502',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7638673502')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Alcobaça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5467378)) <= 0.00070
               AND ABS(v.longitude - (-8.9696198)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305396570 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'TAVE', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4248391, -8.9736724, 'OPENSTREETMAP', 'OSM:WAY:305396570',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305396570')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('TAVE'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4248391)) <= 0.00070
               AND ABS(v.longitude - (-8.9736724)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:277071200 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva de Turquel', 'Alcobaça', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4601387, -8.9690116, 'OPENSTREETMAP', 'OSM:WAY:277071200',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:277071200')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcobaça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva de Turquel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4601387)) <= 0.00070
               AND ABS(v.longitude - (-8.9690116)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:303075273 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis Municipal', 'Alvaiázere', 'Leiria', NULL,
       'Ténis', 'Campo desportivo', NULL,
       39.8175196, -8.3814619, 'OPENSTREETMAP', 'OSM:WAY:303075273',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303075273')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alvaiázere'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8175196)) <= 0.00070
               AND ABS(v.longitude - (-8.3814619)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:303075125 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta Municipal', 'Alvaiázere', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.8180754, -8.3813857, 'OPENSTREETMAP', 'OSM:WAY:303075125',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303075125')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alvaiázere'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8180754)) <= 0.00070
               AND ABS(v.longitude - (-8.3813857)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:303075218 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Alvaiázere', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.8183514, -8.3809802, 'OPENSTREETMAP', 'OSM:WAY:303075218',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:303075218')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alvaiázere'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8183514)) <= 0.00070
               AND ABS(v.longitude - (-8.3809802)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:180711530 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Atlético Clube Avelarense', 'Ansião', 'Leiria', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.9178392, -8.3607714, 'OPENSTREETMAP', 'OSM:WAY:180711530',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:180711530')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ansião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Atlético Clube Avelarense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9178392)) <= 0.00070
               AND ABS(v.longitude - (-8.3607714)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:479578961 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol 5', 'Ansião', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.9134898, -8.4343608, 'OPENSTREETMAP', 'OSM:WAY:479578961',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:479578961')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ansião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol 5'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9134898)) <= 0.00070
               AND ABS(v.longitude - (-8.4343608)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6821102066 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio J', 'Ansião', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.9137583, -8.4423119, 'OPENSTREETMAP', 'OSM:NODE:6821102066',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6821102066')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ansião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio J'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9137583)) <= 0.00070
               AND ABS(v.longitude - (-8.4423119)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:169748320 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo e Recreativo de Pousaflores', 'Ansião', 'Leiria', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.8723205, -8.3902171, 'OPENSTREETMAP', 'OSM:WAY:169748320',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:169748320')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ansião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo e Recreativo de Pousaflores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8723205)) <= 0.00070
               AND ABS(v.longitude - (-8.3902171)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166416366 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lusitano Ginásio de Chão de Couce', 'Ansião', 'Leiria', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.8966445, -8.3697033, 'OPENSTREETMAP', 'OSM:WAY:166416366',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166416366')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ansião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lusitano Ginásio de Chão de Couce'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8966445)) <= 0.00070
               AND ABS(v.longitude - (-8.3697033)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:146508057 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Ansião', 'Ansião', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.9127374, -8.4341899, 'OPENSTREETMAP', 'OSM:WAY:146508057',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:146508057')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ansião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Ansião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9127374)) <= 0.00070
               AND ABS(v.longitude - (-8.4341899)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:824376735 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Simplificada de Santiago da Guarda', 'Ansião', 'Leiria', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.9494273, -8.4795442, 'OPENSTREETMAP', 'OSM:WAY:824376735',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:824376735')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ansião'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Simplificada de Santiago da Guarda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9494273)) <= 0.00070
               AND ABS(v.longitude - (-8.4795442)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:321877862 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kartódromo da Batalha', 'Batalha', 'Leiria', NULL,
       'Karting', 'Complexo desportivo', NULL,
       39.6639168, -8.8497295, 'OPENSTREETMAP', 'OSM:WAY:321877862',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:321877862')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Batalha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kartódromo da Batalha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6639168)) <= 0.00070
               AND ABS(v.longitude - (-8.8497295)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:692498908 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas da Batalha', 'Batalha', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.6633423, -8.8259933, 'OPENSTREETMAP', 'OSM:WAY:692498908',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:692498908')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Batalha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas da Batalha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6633423)) <= 0.00070
               AND ABS(v.longitude - (-8.8259933)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:621342167 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais do Reguengo do Fetal', 'Batalha', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.6443382, -8.7750384, 'OPENSTREETMAP', 'OSM:WAY:621342167',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:621342167')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Batalha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais do Reguengo do Fetal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6443382)) <= 0.00070
               AND ABS(v.longitude - (-8.7750384)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:137726491 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Bombarral', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.2713142, -9.1640553, 'OPENSTREETMAP', 'OSM:WAY:137726491',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:137726491')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Bombarral'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2713142)) <= 0.00070
               AND ABS(v.longitude - (-9.1640553)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:504737327 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Mata', 'Caldas da Rainha', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4033262, -9.1264058, 'OPENSTREETMAP', 'OSM:WAY:504737327',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:504737327')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Mata'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4033262)) <= 0.00070
               AND ABS(v.longitude - (-9.1264058)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103174510 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Quinta da Boneca', 'Caldas da Rainha', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4014022, -9.1266201, 'OPENSTREETMAP', 'OSM:WAY:103174510',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103174510')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Quinta da Boneca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4014022)) <= 0.00070
               AND ABS(v.longitude - (-9.1266201)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:425561594 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do F.C.C.', 'Caldas da Rainha', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4131302, -9.1491379, 'OPENSTREETMAP', 'OSM:WAY:425561594',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:425561594')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do F.C.C.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4131302)) <= 0.00070
               AND ABS(v.longitude - (-9.1491379)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:277724545 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Luís Duarte', 'Caldas da Rainha', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4103672, -9.1527514, 'OPENSTREETMAP', 'OSM:WAY:277724545',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:277724545')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Luís Duarte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4103672)) <= 0.00070
               AND ABS(v.longitude - (-9.1527514)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:301801361 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Badminton', 'Caldas da Rainha', 'Leiria', NULL,
       'Badminton', 'Complexo desportivo', NULL,
       39.4112822, -9.1508137, 'OPENSTREETMAP', 'OSM:WAY:301801361',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:301801361')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Badminton'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4112822)) <= 0.00070
               AND ABS(v.longitude - (-9.1508137)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1828415085 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Vela da Lagoa', 'Caldas da Rainha', 'Leiria', NULL,
       'Canoagem;Vela', 'Complexo desportivo', NULL,
       39.4095484, -9.2020263, 'OPENSTREETMAP', 'OSM:NODE:1828415085',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1828415085')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Vela da Lagoa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4095484)) <= 0.00070
               AND ABS(v.longitude - (-9.2020263)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:487764822 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Balance', 'Caldas da Rainha', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.3920940, -9.1301353, 'OPENSTREETMAP', 'OSM:WAY:487764822',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:487764822')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Balance'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3920940)) <= 0.00070
               AND ABS(v.longitude - (-9.1301353)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:329545843 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Cultural de A-dos-Francos', 'Caldas da Rainha', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3151901, -9.0342596, 'OPENSTREETMAP', 'OSM:WAY:329545843',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:329545843')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Cultural de A-dos-Francos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3151901)) <= 0.00070
               AND ABS(v.longitude - (-9.0342596)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:103174519 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo da Mata', 'Caldas da Rainha', 'Leiria', NULL,
       'Ténis de mesa;Patinagem', 'Complexo desportivo', NULL,
       39.4048489, -9.1267315, 'OPENSTREETMAP', 'OSM:WAY:103174519',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:103174519')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Desportivo da Mata'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4048489)) <= 0.00070
               AND ABS(v.longitude - (-9.1267315)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:414267116 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Salir do Porto', 'Caldas da Rainha', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.5006312, -9.1518103, 'OPENSTREETMAP', 'OSM:WAY:414267116',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:414267116')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Salir do Porto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5006312)) <= 0.00070
               AND ABS(v.longitude - (-9.1518103)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:449852461 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo das Caldas da Rainha', 'Caldas da Rainha', 'Leiria', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.4087360, -9.1521696, 'OPENSTREETMAP', 'OSM:WAY:449852461',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:449852461')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo das Caldas da Rainha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4087360)) <= 0.00070
               AND ABS(v.longitude - (-9.1521696)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:451876821 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark', 'Caldas da Rainha', 'Leiria', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.4028083, -9.1431379, 'OPENSTREETMAP', 'OSM:WAY:451876821',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:451876821')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Caldas da Rainha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4028083)) <= 0.00070
               AND ABS(v.longitude - (-9.1431379)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:815245524 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Radical', 'Figueiró dos Vinhos', 'Leiria', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.8984133, -8.2840274, 'OPENSTREETMAP', 'OSM:WAY:815245524',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:815245524')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueiró dos Vinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8984133)) <= 0.00070
               AND ABS(v.longitude - (-8.2840274)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:900535553 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Figueiró dos Vinhos', 'Figueiró dos Vinhos', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.9002238, -8.2719678, 'OPENSTREETMAP', 'OSM:WAY:900535553',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:900535553')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueiró dos Vinhos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Figueiró dos Vinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9002238)) <= 0.00070
               AND ABS(v.longitude - (-8.2719678)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13823795201 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BeON', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7349730, -8.7924065, 'OPENSTREETMAP', 'OSM:NODE:13823795201',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13823795201')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BeON'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7349730)) <= 0.00070
               AND ABS(v.longitude - (-8.7924065)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6568471511 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Blue Fitness Club', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7302770, -8.8105697, 'OPENSTREETMAP', 'OSM:NODE:6568471511',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6568471511')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Blue Fitness Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7302770)) <= 0.00070
               AND ABS(v.longitude - (-8.8105697)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440502176 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Chainça', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6662805, -8.7091359, 'OPENSTREETMAP', 'OSM:WAY:440502176',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440502176')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Chainça'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6662805)) <= 0.00070
               AND ABS(v.longitude - (-8.7091359)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:264763053 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Caranguejeira', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7473643, -8.7021604, 'OPENSTREETMAP', 'OSM:WAY:264763053',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:264763053')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Caranguejeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7473643)) <= 0.00070
               AND ABS(v.longitude - (-8.7021604)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:693991310 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Chãs', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7902906, -8.8197909, 'OPENSTREETMAP', 'OSM:WAY:693991310',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:693991310')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Chãs'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7902906)) <= 0.00070
               AND ABS(v.longitude - (-8.8197909)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243034654 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de 7 da UDS', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6675747, -8.6825990, 'OPENSTREETMAP', 'OSM:WAY:243034654',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243034654')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de 7 da UDS'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6675747)) <= 0.00070
               AND ABS(v.longitude - (-8.6825990)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:530709564 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de António Moreira Tavares', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7803173, -8.7463081, 'OPENSTREETMAP', 'OSM:WAY:530709564',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:530709564')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de António Moreira Tavares'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7803173)) <= 0.00070
               AND ABS(v.longitude - (-8.7463081)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:850811136 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Praia', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.7335574, -8.7716180, 'OPENSTREETMAP', 'OSM:WAY:850811136',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:850811136')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7335574)) <= 0.00070
               AND ABS(v.longitude - (-8.7716180)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:198692255 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Grupo Alegre e Unido', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8973786, -8.7784675, 'OPENSTREETMAP', 'OSM:WAY:198692255',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:198692255')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Grupo Alegre e Unido'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8973786)) <= 0.00070
               AND ABS(v.longitude - (-8.7784675)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:256090109 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desportivo de Santa Eufémia', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7608364, -8.7347825, 'OPENSTREETMAP', 'OSM:WAY:256090109',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:256090109')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desportivo de Santa Eufémia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7608364)) <= 0.00070
               AND ABS(v.longitude - (-8.7347825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440499716 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Tiro', 'Leiria', 'Leiria', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       39.6780152, -8.7076377, 'OPENSTREETMAP', 'OSM:WAY:440499716',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440499716')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Tiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6780152)) <= 0.00070
               AND ABS(v.longitude - (-8.7076377)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:297390939 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos Bombeiros Voluntários de Leiria', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7671035, -8.8162882, 'OPENSTREETMAP', 'OSM:WAY:297390939',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:297390939')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos Bombeiros Voluntários de Leiria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7671035)) <= 0.00070
               AND ABS(v.longitude - (-8.8162882)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:965224111 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Dona Marquinhas da Silva', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.9000993, -8.8374507, 'OPENSTREETMAP', 'OSM:WAY:965224111',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:965224111')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Dona Marquinhas da Silva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9000993)) <= 0.00070
               AND ABS(v.longitude - (-8.8374507)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:175877098 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Piscinas de Leiria', 'Leiria', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.7501038, -8.8086555, 'OPENSTREETMAP', 'OSM:WAY:175877098',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:175877098')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Piscinas de Leiria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7501038)) <= 0.00070
               AND ABS(v.longitude - (-8.8086555)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:160165474 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Ténis Dr. Rui Garcia da Fonseca', 'Leiria', 'Leiria', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       39.7354864, -8.7947862, 'OPENSTREETMAP', 'OSM:WAY:160165474',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:160165474')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Municipal de Ténis Dr. Rui Garcia da Fonseca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7354864)) <= 0.00070
               AND ABS(v.longitude - (-8.7947862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:203784193 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'C.P.R.P', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6979996, -8.8929707, 'OPENSTREETMAP', 'OSM:WAY:203784193',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:203784193')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('C.P.R.P'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6979996)) <= 0.00070
               AND ABS(v.longitude - (-8.8929707)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:981032355 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Elite Padel Club', 'Leiria', 'Leiria', NULL,
       'Padel', 'Campo desportivo', NULL,
       39.8000259, -8.8373556, 'OPENSTREETMAP', 'OSM:WAY:981032355',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:981032355')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Elite Padel Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8000259)) <= 0.00070
               AND ABS(v.longitude - (-8.8373556)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243033353 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Portela', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Estádio', NULL,
       39.6672634, -8.6833282, 'OPENSTREETMAP', 'OSM:WAY:243033353',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243033353')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6672634)) <= 0.00070
               AND ABS(v.longitude - (-8.6833282)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:200050248 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo e Recreativo do Casal Novo', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8776178, -8.7908563, 'OPENSTREETMAP', 'OSM:WAY:200050248',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:200050248')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo e Recreativo do Casal Novo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8776178)) <= 0.00070
               AND ABS(v.longitude - (-8.7908563)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6316627269 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Maxigym - Ginásio de Cultura Física e Estética Corporal', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7330331, -8.7604987, 'OPENSTREETMAP', 'OSM:NODE:6316627269',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6316627269')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Maxigym - Ginásio de Cultura Física e Estética Corporal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7330331)) <= 0.00070
               AND ABS(v.longitude - (-8.7604987)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12180710593 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'MW STUDIO', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7392009, -8.8243309, 'OPENSTREETMAP', 'OSM:NODE:12180710593',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12180710593')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('MW STUDIO'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7392009)) <= 0.00070
               AND ABS(v.longitude - (-8.8243309)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138359796 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo de Maceira', 'Leiria', 'Leiria', NULL,
       'Futsal;Basquetebol;Badminton', 'Campo desportivo', NULL,
       39.6863023, -8.8983490, 'OPENSTREETMAP', 'OSM:WAY:138359796',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138359796')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo de Maceira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6863023)) <= 0.00070
               AND ABS(v.longitude - (-8.8983490)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:198518816 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina da Bajouca', 'Leiria', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.8924368, -8.7796981, 'OPENSTREETMAP', 'OSM:WAY:198518816',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:198518816')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina da Bajouca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8924368)) <= 0.00070
               AND ABS(v.longitude - (-8.7796981)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:741980548 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Caranguejeira', 'Leiria', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.7467220, -8.7000993, 'OPENSTREETMAP', 'OSM:WAY:741980548',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:741980548')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Caranguejeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7467220)) <= 0.00070
               AND ABS(v.longitude - (-8.7000993)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138960902 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Maceira', 'Leiria', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.6858124, -8.8990436, 'OPENSTREETMAP', 'OSM:WAY:138960902',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138960902')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Maceira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6858124)) <= 0.00070
               AND ABS(v.longitude - (-8.8990436)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:257966666 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Belo Horizonte', 'Leiria', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.7444230, -8.8254412, 'OPENSTREETMAP', 'OSM:WAY:257966666',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:257966666')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Belo Horizonte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7444230)) <= 0.00070
               AND ABS(v.longitude - (-8.8254412)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:321534668 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Quinta de Santo António do Freixo Driving Range', 'Leiria', 'Leiria', NULL,
       'Golfe', 'Campo de golfe', NULL,
       39.6939239, -8.7821203, 'OPENSTREETMAP', 'OSM:WAY:321534668',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:321534668')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Quinta de Santo António do Freixo Driving Range'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6939239)) <= 0.00070
               AND ABS(v.longitude - (-8.7821203)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243089950 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Futebol 5', 'Leiria', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6750448, -8.6857627, 'OPENSTREETMAP', 'OSM:WAY:243089950',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243089950')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Futebol 5'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6750448)) <= 0.00070
               AND ABS(v.longitude - (-8.6857627)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:506284307 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SkatePark do Avião', 'Leiria', 'Leiria', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.7459090, -8.8033293, 'OPENSTREETMAP', 'OSM:WAY:506284307',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:506284307')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SkatePark do Avião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7459090)) <= 0.00070
               AND ABS(v.longitude - (-8.8033293)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243035313 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tanque de aprendizagem da UDS', 'Leiria', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.6680999, -8.6829532, 'OPENSTREETMAP', 'OSM:WAY:243035313',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243035313')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tanque de aprendizagem da UDS'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6680999)) <= 0.00070
               AND ABS(v.longitude - (-8.6829532)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12473856713 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Leiria', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7397976, -8.8177826, 'OPENSTREETMAP', 'OSM:NODE:12473856713',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12473856713')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Leiria'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7397976)) <= 0.00070
               AND ABS(v.longitude - (-8.8177826)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:412495633 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Atlético Clube Marinhense', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7367538, -8.9311839, 'OPENSTREETMAP', 'OSM:WAY:412495633',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:412495633')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Atlético Clube Marinhense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7367538)) <= 0.00070
               AND ABS(v.longitude - (-8.9311839)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:202569751 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Comeira', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7315978, -8.9228548, 'OPENSTREETMAP', 'OSM:WAY:202569751',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:202569751')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Comeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7315978)) <= 0.00070
               AND ABS(v.longitude - (-8.9228548)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:202569245 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Portela (Atlético Clube Marinhense)', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7491982, -8.9373163, 'OPENSTREETMAP', 'OSM:WAY:202569245',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:202569245')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Portela (Atlético Clube Marinhense)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7491982)) <= 0.00070
               AND ABS(v.longitude - (-8.9373163)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:203038409 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Figueiras', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7022999, -8.9521960, 'OPENSTREETMAP', 'OSM:WAY:203038409',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:203038409')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Figueiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7022999)) <= 0.00070
               AND ABS(v.longitude - (-8.9521960)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:208864034 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Garcia', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7802009, -8.9278302, 'OPENSTREETMAP', 'OSM:WAY:208864034',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:208864034')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Garcia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7802009)) <= 0.00070
               AND ABS(v.longitude - (-8.9278302)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:376873323 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Pilado', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8092562, -8.9226016, 'OPENSTREETMAP', 'OSM:WAY:376873323',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:376873323')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Pilado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8092562)) <= 0.00070
               AND ABS(v.longitude - (-8.9226016)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:203482915 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Futebol Clube "Os Belenenses"', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7276894, -8.9300676, 'OPENSTREETMAP', 'OSM:WAY:203482915',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:203482915')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Futebol Clube "Os Belenenses"'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7276894)) <= 0.00070
               AND ABS(v.longitude - (-8.9300676)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:202569557 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos Vidreiros (Tojal)', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7303930, -8.9062215, 'OPENSTREETMAP', 'OSM:WAY:202569557',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:202569557')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos Vidreiros (Tojal)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7303930)) <= 0.00070
               AND ABS(v.longitude - (-8.9062215)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181625043 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis da Marinha Grande', 'Marinha Grande', 'Leiria', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       39.7406332, -8.9335297, 'OPENSTREETMAP', 'OSM:WAY:181625043',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181625043')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis da Marinha Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7406332)) <= 0.00070
               AND ABS(v.longitude - (-8.9335297)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:357515573 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Colónia de Férias da PSP', 'Marinha Grande', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.8787794, -8.9659304, 'OPENSTREETMAP', 'OSM:WAY:357515573',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:357515573')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Colónia de Férias da PSP'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8787794)) <= 0.00070
               AND ABS(v.longitude - (-8.9659304)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:126446817 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Albano Tomé Feteira', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8696593, -8.9147002, 'OPENSTREETMAP', 'OSM:WAY:126446817',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:126446817')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Albano Tomé Feteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8696593)) <= 0.00070
               AND ABS(v.longitude - (-8.9147002)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:203580040 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fisicoloucura', 'Marinha Grande', 'Leiria', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       39.7544840, -8.9199649, 'OPENSTREETMAP', 'OSM:WAY:203580040',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:203580040')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fisicoloucura'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7544840)) <= 0.00070
               AND ABS(v.longitude - (-8.9199649)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2131027884 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Jorkyball', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.7468848, -8.9118380, 'OPENSTREETMAP', 'OSM:NODE:2131027884',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2131027884')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Jorkyball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7468848)) <= 0.00070
               AND ABS(v.longitude - (-8.9118380)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6632318237 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Paddock Squash Club', 'Marinha Grande', 'Leiria', NULL,
       'Squash', 'Campo desportivo', NULL,
       39.7542382, -8.9182698, 'OPENSTREETMAP', 'OSM:NODE:6632318237',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6632318237')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Paddock Squash Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7542382)) <= 0.00070
               AND ABS(v.longitude - (-8.9182698)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:299989822 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vieira de Leiria', 'Marinha Grande', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.8625462, -8.9355997, 'OPENSTREETMAP', 'OSM:WAY:299989822',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:299989822')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vieira de Leiria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8625462)) <= 0.00070
               AND ABS(v.longitude - (-8.9355997)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:181625044 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais da Marinha Grande', 'Marinha Grande', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.7496091, -8.9247711, 'OPENSTREETMAP', 'OSM:WAY:181625044',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:181625044')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais da Marinha Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7496091)) <= 0.00070
               AND ABS(v.longitude - (-8.9247711)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1434143804 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Oceânicas de São Pedro de Moel', 'Marinha Grande', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.7533043, -9.0330216, 'OPENSTREETMAP', 'OSM:WAY:1434143804',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1434143804')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Oceânicas de São Pedro de Moel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7533043)) <= 0.00070
               AND ABS(v.longitude - (-9.0330216)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:126833700 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ribeiro da Tábua', 'Marinha Grande', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8699609, -8.9474984, 'OPENSTREETMAP', 'OSM:WAY:126833700',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:126833700')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ribeiro da Tábua'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8699609)) <= 0.00070
               AND ABS(v.longitude - (-8.9474984)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:300047885 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue da Passagem', 'Marinha Grande', 'Leiria', NULL,
       'Ténis;Futebol;Ginástica', 'Campo desportivo', NULL,
       39.8790220, -8.9152631, 'OPENSTREETMAP', 'OSM:WAY:300047885',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:300047885')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue da Passagem'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8790220)) <= 0.00070
               AND ABS(v.longitude - (-8.9152631)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243114142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Half Pipe', 'Marinha Grande', 'Leiria', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.7443153, -8.9335830, 'OPENSTREETMAP', 'OSM:WAY:243114142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243114142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Half Pipe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7443153)) <= 0.00070
               AND ABS(v.longitude - (-8.9335830)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2129504322 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Wellness Life Health Club', 'Marinha Grande', 'Leiria', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.7516512, -8.9296617, 'OPENSTREETMAP', 'OSM:NODE:2129504322',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2129504322')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Marinha Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Wellness Life Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7516512)) <= 0.00070
               AND ABS(v.longitude - (-8.9296617)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:411292413 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Surf da Nazaré', 'Nazaré', 'Leiria', NULL,
       'Surf', 'Complexo desportivo', NULL,
       39.6123859, -9.0788994, 'OPENSTREETMAP', 'OSM:WAY:411292413',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:411292413')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Nazaré'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Surf da Nazaré'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6123859)) <= 0.00070
               AND ABS(v.longitude - (-9.0788994)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:411170324 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal da Nazaré', 'Nazaré', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6087504, -9.0627458, 'OPENSTREETMAP', 'OSM:WAY:411170324',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:411170324')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Nazaré'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal da Nazaré'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6087504)) <= 0.00070
               AND ABS(v.longitude - (-9.0627458)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:503739412 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '10th Green', 'Óbidos', 'Leiria', NULL,
       'Golfe', 'Campo desportivo', NULL,
       39.3891202, -9.2863123, 'OPENSTREETMAP', 'OSM:WAY:503739412',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:503739412')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('10th Green'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3891202)) <= 0.00070
               AND ABS(v.longitude - (-9.2863123)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:299298753 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Amoreira', 'Óbidos', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3419057, -9.2167594, 'OPENSTREETMAP', 'OSM:WAY:299298753',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:299298753')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Amoreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3419057)) <= 0.00070
               AND ABS(v.longitude - (-9.2167594)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:299299943 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol das Gaeiras', 'Óbidos', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3659211, -9.1238329, 'OPENSTREETMAP', 'OSM:WAY:299299943',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:299299943')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol das Gaeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3659211)) <= 0.00070
               AND ABS(v.longitude - (-9.1238329)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:298282726 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Olho Marinho', 'Óbidos', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3336565, -9.2303994, 'OPENSTREETMAP', 'OSM:WAY:298282726',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:298282726')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Olho Marinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3336565)) <= 0.00070
               AND ABS(v.longitude - (-9.2303994)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:298272723 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Óbidos', 'Óbidos', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.3516570, -9.1588488, 'OPENSTREETMAP', 'OSM:WAY:298272723',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:298272723')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Óbidos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3516570)) <= 0.00070
               AND ABS(v.longitude - (-9.1588488)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4940521525 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spot de Surf', 'Óbidos', 'Leiria', NULL,
       'Surf', 'Campo desportivo', NULL,
       39.4019469, -9.2735388, 'OPENSTREETMAP', 'OSM:NODE:4940521525',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4940521525')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spot de Surf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4019469)) <= 0.00070
               AND ABS(v.longitude - (-9.2735388)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7279327414 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spot de Surf da Estrela', 'Óbidos', 'Leiria', NULL,
       'Surf', 'Campo desportivo', NULL,
       39.4060197, -9.2675924, 'OPENSTREETMAP', 'OSM:NODE:7279327414',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7279327414')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spot de Surf da Estrela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4060197)) <= 0.00070
               AND ABS(v.longitude - (-9.2675924)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5378219556 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spot de Surf Rei Cortiço', 'Óbidos', 'Leiria', NULL,
       'Surf', 'Complexo desportivo', NULL,
       39.4207966, -9.2468649, 'OPENSTREETMAP', 'OSM:NODE:5378219556',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5378219556')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Óbidos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spot de Surf Rei Cortiço'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4207966)) <= 0.00070
               AND ABS(v.longitude - (-9.2468649)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:924939374 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Radical', 'Pedrógão Grande', 'Leiria', NULL,
       'Patinagem;Escalada;Ciclismo', 'Complexo desportivo', NULL,
       39.9188644, -8.1508258, 'OPENSTREETMAP', 'OSM:WAY:924939374',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:924939374')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pedrógão Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9188644)) <= 0.00070
               AND ABS(v.longitude - (-8.1508258)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:771614394 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Flutuante do Cabril', 'Pedrógão Grande', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.9209561, -8.1327701, 'OPENSTREETMAP', 'OSM:WAY:771614394',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:771614394')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pedrógão Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Flutuante do Cabril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9209561)) <= 0.00070
               AND ABS(v.longitude - (-8.1327701)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:924939372 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Pedrogão Grande', 'Pedrógão Grande', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.9192680, -8.1495230, 'OPENSTREETMAP', 'OSM:WAY:924939372',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:924939372')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pedrógão Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Pedrogão Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9192680)) <= 0.00070
               AND ABS(v.longitude - (-8.1495230)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:824376728 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Simplificada de Atletismo de Pedrógão Grande', 'Pedrógão Grande', 'Leiria', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.9187266, -8.1517745, 'OPENSTREETMAP', 'OSM:WAY:824376728',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:824376728')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pedrógão Grande'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Simplificada de Atletismo de Pedrógão Grande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9187266)) <= 0.00070
               AND ABS(v.longitude - (-8.1517745)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:563319945 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Peniche', 'Peniche', 'Leiria', NULL,
       'Surf', 'Complexo desportivo', NULL,
       39.3533692, -9.3575265, 'OPENSTREETMAP', 'OSM:WAY:563319945',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:563319945')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Peniche'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3533692)) <= 0.00070
               AND ABS(v.longitude - (-9.3575265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138704022 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Grupo Desportivo de Peniche', 'Peniche', 'Leiria', NULL,
       'Futebol', 'Estádio', NULL,
       39.3520103, -9.3628193, 'OPENSTREETMAP', 'OSM:WAY:138704022',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138704022')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Grupo Desportivo de Peniche'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3520103)) <= 0.00070
               AND ABS(v.longitude - (-9.3628193)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138704012 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque 25 de Abril', 'Peniche', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3534354, -9.3633229, 'OPENSTREETMAP', 'OSM:WAY:138704012',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138704012')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque 25 de Abril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3534354)) <= 0.00070
               AND ABS(v.longitude - (-9.3633229)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5377823050 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pesqueiro dos Viveiros', 'Peniche', 'Leiria', NULL,
       'Surf', 'Complexo desportivo', NULL,
       39.3843731, -9.3031889, 'OPENSTREETMAP', 'OSM:NODE:5377823050',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5377823050')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pesqueiro dos Viveiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3843731)) <= 0.00070
               AND ABS(v.longitude - (-9.3031889)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138715723 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Adultos', 'Peniche', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.3654089, -9.3362833, 'OPENSTREETMAP', 'OSM:WAY:138715723',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138715723')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Adultos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3654089)) <= 0.00070
               AND ABS(v.longitude - (-9.3362833)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:706732009 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Infantil', 'Peniche', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.3654784, -9.3361050, 'OPENSTREETMAP', 'OSM:WAY:706732009',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:706732009')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Infantil'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3654784)) <= 0.00070
               AND ABS(v.longitude - (-9.3361050)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:233178277 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas AcquaViva', 'Peniche', 'Leiria', NULL,
       'Natação', 'Piscina', NULL,
       39.3261720, -9.3565779, 'OPENSTREETMAP', 'OSM:WAY:233178277',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:233178277')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas AcquaViva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3261720)) <= 0.00070
               AND ABS(v.longitude - (-9.3565779)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:488290396 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Peniche', 'Peniche', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.3634407, -9.3891748, 'OPENSTREETMAP', 'OSM:WAY:488290396',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:488290396')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Peniche'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3634407)) <= 0.00070
               AND ABS(v.longitude - (-9.3891748)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:809128339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Serra D''El Rei', 'Peniche', 'Leiria', NULL,
       'Basquetebol;Futsal', 'Campo desportivo', NULL,
       39.3362957, -9.2698604, 'OPENSTREETMAP', 'OSM:WAY:809128339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:809128339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Serra D''El Rei'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3362957)) <= 0.00070
               AND ABS(v.longitude - (-9.2698604)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4939873193 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spot de Surf', 'Peniche', 'Leiria', NULL,
       'Surf;Natação', 'Campo desportivo', NULL,
       39.3873595, -9.2977194, 'OPENSTREETMAP', 'OSM:NODE:4939873193',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4939873193')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Peniche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spot de Surf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3873595)) <= 0.00070
               AND ABS(v.longitude - (-9.2977194)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:304447780 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Bola da Redinha', 'Pombal', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.0008174, -8.5926594, 'OPENSTREETMAP', 'OSM:WAY:304447780',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:304447780')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Bola da Redinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.0008174)) <= 0.00070
               AND ABS(v.longitude - (-8.5926594)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:357144752 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Meirinhas', 'Pombal', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.8317053, -8.7104385, 'OPENSTREETMAP', 'OSM:WAY:357144752',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:357144752')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Meirinhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8317053)) <= 0.00070
               AND ABS(v.longitude - (-8.7104385)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:615641883 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Grupo Desportivo da Ilha', 'Pombal', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.9080090, -8.7625861, 'OPENSTREETMAP', 'OSM:WAY:615641883',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:615641883')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Grupo Desportivo da Ilha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9080090)) <= 0.00070
               AND ABS(v.longitude - (-8.7625861)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:217864075 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Dinis dos Pinheiros', 'Pombal', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.9675329, -8.6221988, 'OPENSTREETMAP', 'OSM:WAY:217864075',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:217864075')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Dinis dos Pinheiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9675329)) <= 0.00070
               AND ABS(v.longitude - (-8.6221988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153647261 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Nº. 2', 'Pombal', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.9114197, -8.6308448, 'OPENSTREETMAP', 'OSM:WAY:153647261',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153647261')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Nº. 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9114197)) <= 0.00070
               AND ABS(v.longitude - (-8.6308448)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:200375023 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Vale das Éguas', 'Pombal', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7855027, -8.5868635, 'OPENSTREETMAP', 'OSM:WAY:200375023',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:200375023')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Vale das Éguas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7855027)) <= 0.00070
               AND ABS(v.longitude - (-8.5868635)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153646397 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Pombal', 'Pombal', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.9097013, -8.6301113, 'OPENSTREETMAP', 'OSM:WAY:153646397',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153646397')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Pombal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.9097013)) <= 0.00070
               AND ABS(v.longitude - (-8.6301113)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:824376736 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Simplificada de Carnide', 'Pombal', 'Leiria', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.8971899, -8.7370518, 'OPENSTREETMAP', 'OSM:WAY:824376736',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:824376736')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Simplificada de Carnide'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8971899)) <= 0.00070
               AND ABS(v.longitude - (-8.7370518)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:537531891 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de São Simão de Litém', 'Pombal', 'Leiria', NULL,
       'Ténis;Futsal', 'Campo desportivo', NULL,
       39.8234652, -8.6179749, 'OPENSTREETMAP', 'OSM:WAY:537531891',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:537531891')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de São Simão de Litém'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8234652)) <= 0.00070
               AND ABS(v.longitude - (-8.6179749)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:388517081 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pump Track', 'Pombal', 'Leiria', NULL,
       'Ciclismo', 'Pista desportiva', NULL,
       39.8903199, -8.6478638, 'OPENSTREETMAP', 'OSM:WAY:388517081',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:388517081')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pombal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pump Track'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.8903199)) <= 0.00070
               AND ABS(v.longitude - (-8.6478638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:288212906 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Alqueidão da Serra', 'Porto de Mós', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6263558, -8.7911617, 'OPENSTREETMAP', 'OSM:WAY:288212906',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:288212906')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto de Mós'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Alqueidão da Serra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6263558)) <= 0.00070
               AND ABS(v.longitude - (-8.7911617)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:437590330 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Fiandeira', 'Porto de Mós', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5452054, -8.7167045, 'OPENSTREETMAP', 'OSM:WAY:437590330',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:437590330')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto de Mós'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Fiandeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5452054)) <= 0.00070
               AND ABS(v.longitude - (-8.7167045)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:789316037 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da União Recreativa e Desportiva Juncalense', 'Porto de Mós', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6047687, -8.8890438, 'OPENSTREETMAP', 'OSM:WAY:789316037',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:789316037')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto de Mós'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da União Recreativa e Desportiva Juncalense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6047687)) <= 0.00070
               AND ABS(v.longitude - (-8.8890438)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:212712335 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos do União Recreativa Mirense', 'Porto de Mós', 'Leiria', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5436431, -8.7035422, 'OPENSTREETMAP', 'OSM:WAY:212712335',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:212712335')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto de Mós'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos do União Recreativa Mirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5436431)) <= 0.00070
               AND ABS(v.longitude - (-8.7035422)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:463227260 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Porto de Mós', 'Porto de Mós', 'Leiria', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.5999783, -8.8257073, 'OPENSTREETMAP', 'OSM:WAY:463227260',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:463227260')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Porto de Mós'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Porto de Mós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5999783)) <= 0.00070
               AND ABS(v.longitude - (-8.8257073)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
