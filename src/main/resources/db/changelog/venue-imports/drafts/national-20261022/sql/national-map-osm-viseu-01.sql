-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:119582384 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Parque', 'Armamar', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1502619, -7.6797639, 'OPENSTREETMAP', 'OSM:WAY:119582384',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119582384')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Armamar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Parque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1502619)) <= 0.00070
               AND ABS(v.longitude - (-7.6797639)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106775311 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Armamar', 'Armamar', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1002392, -7.6937795, 'OPENSTREETMAP', 'OSM:WAY:106775311',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106775311')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Armamar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Armamar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1002392)) <= 0.00070
               AND ABS(v.longitude - (-7.6937795)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:440114058 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta Municipal de Armamar', 'Armamar', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1078555, -7.6891149, 'OPENSTREETMAP', 'OSM:WAY:440114058',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:440114058')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Armamar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta Municipal de Armamar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1078555)) <= 0.00070
               AND ABS(v.longitude - (-7.6891149)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104688507 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Descoberta Municipal de Armamar', 'Armamar', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.1083633, -7.6884155, 'OPENSTREETMAP', 'OSM:WAY:104688507',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104688507')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Armamar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Descoberta Municipal de Armamar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1083633)) <= 0.00070
               AND ABS(v.longitude - (-7.6884155)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127883568 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Queimadela', 'Armamar', 'Viseu', NULL,
       'Futsal;Andebol;Ténis;Voleibol;Badminton', 'Campo desportivo', NULL,
       41.0892434, -7.7425785, 'OPENSTREETMAP', 'OSM:WAY:127883568',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127883568')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Armamar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Queimadela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0892434)) <= 0.00070
               AND ABS(v.longitude - (-7.7425785)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1182088913 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ARDFT - Associação Recreativa e Desportiva de Fiais da Telha', 'Carregal do Sal', 'Viseu', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.4549660, -7.9358956, 'OPENSTREETMAP', 'OSM:WAY:1182088913',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1182088913')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ARDFT - Associação Recreativa e Desportiva de Fiais da Telha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4549660)) <= 0.00070
               AND ABS(v.longitude - (-7.9358956)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:840241793 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol das Laceiras - utilizado em jogos de futebol organizado pela Associação Recreativa, Cultural e Desportiva de Laceiras', 'Carregal do Sal', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4870453, -7.9474481, 'OPENSTREETMAP', 'OSM:WAY:840241793',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:840241793')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol das Laceiras - utilizado em jogos de futebol organizado pela Associação Recreativa, Cultural e Desportiva de Laceiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4870453)) <= 0.00070
               AND ABS(v.longitude - (-7.9474481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1163972517 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Beijós', 'Carregal do Sal', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5141980, -7.9583741, 'OPENSTREETMAP', 'OSM:WAY:1163972517',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1163972517')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Beijós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5141980)) <= 0.00070
               AND ABS(v.longitude - (-7.9583741)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1146081345 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Outeiro do Seixo - Sport Cabanas de Viriato e Benfica', 'Carregal do Sal', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4667196, -7.9696343, 'OPENSTREETMAP', 'OSM:WAY:1146081345',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1146081345')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Outeiro do Seixo - Sport Cabanas de Viriato e Benfica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4667196)) <= 0.00070
               AND ABS(v.longitude - (-7.9696343)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:443561187 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol - Travanca de São Tomé', 'Carregal do Sal', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4615076, -7.9888149, 'OPENSTREETMAP', 'OSM:WAY:443561187',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:443561187')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol - Travanca de São Tomé'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4615076)) <= 0.00070
               AND ABS(v.longitude - (-7.9888149)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:749721941 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Casas Do Zagão - Campo de ténis', 'Carregal do Sal', 'Viseu', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.4338077, -7.9970525, 'OPENSTREETMAP', 'OSM:WAY:749721941',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:749721941')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Casas Do Zagão - Campo de ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4338077)) <= 0.00070
               AND ABS(v.longitude - (-7.9970525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1283362317 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Casas Do Zagão - Piscina', 'Carregal do Sal', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.4336171, -7.9973415, 'OPENSTREETMAP', 'OSM:WAY:1283362317',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1283362317')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Casas Do Zagão - Piscina'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4336171)) <= 0.00070
               AND ABS(v.longitude - (-7.9973415)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:279969918 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Nossa Senhora das Febres - Clube de Futebol de Carregal do Sal', 'Carregal do Sal', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4263531, -8.0116811, 'OPENSTREETMAP', 'OSM:WAY:279969918',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:279969918')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Nossa Senhora das Febres - Clube de Futebol de Carregal do Sal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4263531)) <= 0.00070
               AND ABS(v.longitude - (-8.0116811)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1288773886 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina com trampolins olímpicos - Piscinas Municipais de Carregal do Sal', 'Carregal do Sal', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.4301304, -7.9986954, 'OPENSTREETMAP', 'OSM:WAY:1288773886',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1288773886')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina com trampolins olímpicos - Piscinas Municipais de Carregal do Sal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4301304)) <= 0.00070
               AND ABS(v.longitude - (-7.9986954)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1288773889 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina interior - Piscinas Municipais de Carregal do Sal', 'Carregal do Sal', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.4300506, -7.9992413, 'OPENSTREETMAP', 'OSM:WAY:1288773889',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1288773889')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina interior - Piscinas Municipais de Carregal do Sal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4300506)) <= 0.00070
               AND ABS(v.longitude - (-7.9992413)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1288773885 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina olímpica - Piscinas Municipais de Carregal do Sal', 'Carregal do Sal', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.4303836, -7.9989220, 'OPENSTREETMAP', 'OSM:WAY:1288773885',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1288773885')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina olímpica - Piscinas Municipais de Carregal do Sal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4303836)) <= 0.00070
               AND ABS(v.longitude - (-7.9989220)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:815622703 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Carregal do Sal', 'Carregal do Sal', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.4302680, -7.9993362, 'OPENSTREETMAP', 'OSM:WAY:815622703',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:815622703')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Carregal do Sal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4302680)) <= 0.00070
               AND ABS(v.longitude - (-7.9993362)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1137563021 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Cabanas de Viriato', 'Carregal do Sal', 'Viseu', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.4803910, -7.9761536, 'OPENSTREETMAP', 'OSM:WAY:1137563021',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1137563021')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Cabanas de Viriato'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4803910)) <= 0.00070
               AND ABS(v.longitude - (-7.9761536)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1213412834 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Carregal do Sal', 'Carregal do Sal', 'Viseu', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.4309181, -7.9987237, 'OPENSTREETMAP', 'OSM:WAY:1213412834',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1213412834')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Carregal do Sal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4309181)) <= 0.00070
               AND ABS(v.longitude - (-7.9987237)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1302915439 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Sobral', 'Carregal do Sal', 'Viseu', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.4614745, -8.0263463, 'OPENSTREETMAP', 'OSM:WAY:1302915439',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1302915439')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Sobral'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4614745)) <= 0.00070
               AND ABS(v.longitude - (-8.0263463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1392313790 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Oliveira do Conde', 'Carregal do Sal', 'Viseu', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.4397111, -7.9635265, 'OPENSTREETMAP', 'OSM:WAY:1392313790',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1392313790')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Carregal do Sal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Oliveira do Conde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4397111)) <= 0.00070
               AND ABS(v.longitude - (-7.9635265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:48132047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Relva', 'Castro Daire', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       40.9592035, -7.8599918, 'OPENSTREETMAP', 'OSM:WAY:48132047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:48132047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Daire'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Relva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9592035)) <= 0.00070
               AND ABS(v.longitude - (-7.8599918)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:909202536 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal de Castro Daire', 'Castro Daire', 'Viseu', NULL,
       'Atletismo;Futebol', 'Estádio', NULL,
       40.9016813, -7.9283416, 'OPENSTREETMAP', 'OSM:WAY:909202536',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:909202536')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Daire'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Municipal de Castro Daire'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9016813)) <= 0.00070
               AND ABS(v.longitude - (-7.9283416)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:4853829 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Granja', 'Castro Daire', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       40.9548382, -7.8041173, 'OPENSTREETMAP', 'OSM:WAY:4853829',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:4853829')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Daire'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Granja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9548382)) <= 0.00070
               AND ABS(v.longitude - (-7.8041173)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1055936540 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Faifa', 'Castro Daire', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.9503003, -8.0075936, 'OPENSTREETMAP', 'OSM:WAY:1055936540',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1055936540')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Daire'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Faifa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9503003)) <= 0.00070
               AND ABS(v.longitude - (-8.0075936)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:47576040 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Castro Daire', 'Castro Daire', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', 'INDOOR',
       40.9020512, -7.9273180, 'OPENSTREETMAP', 'OSM:WAY:47576040',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:47576040')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Castro Daire'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Castro Daire'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9020512)) <= 0.00070
               AND ABS(v.longitude - (-7.9273180)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107827913 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de treinos', 'Cinfães', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0747135, -8.1017505, 'OPENSTREETMAP', 'OSM:WAY:107827913',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107827913')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cinfães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de treinos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0747135)) <= 0.00070
               AND ABS(v.longitude - (-8.1017505)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:215625037 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol Fornelos', 'Cinfães', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0208739, -8.2079792, 'OPENSTREETMAP', 'OSM:WAY:215625037',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:215625037')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cinfães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol Fornelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0208739)) <= 0.00070
               AND ABS(v.longitude - (-8.2079792)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107827922 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Cinfães', 'Cinfães', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       41.0744503, -8.1003422, 'OPENSTREETMAP', 'OSM:WAY:107827922',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107827922')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cinfães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Cinfães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0744503)) <= 0.00070
               AND ABS(v.longitude - (-8.1003422)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1781612570 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Nespereira Futebol Clube', 'Cinfães', 'Viseu', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.0008247, -8.1800781, 'OPENSTREETMAP', 'OSM:NODE:1781612570',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1781612570')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cinfães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Nespereira Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0008247)) <= 0.00070
               AND ABS(v.longitude - (-8.1800781)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:107827917 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Cinfães', 'Cinfães', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       41.0753040, -8.0992036, 'OPENSTREETMAP', 'OSM:WAY:107827917',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:107827917')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cinfães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Cinfães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0753040)) <= 0.00070
               AND ABS(v.longitude - (-8.0992036)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104691112 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Cinfães (coberta)', 'Cinfães', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       41.0796804, -8.1060162, 'OPENSTREETMAP', 'OSM:WAY:104691112',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104691112')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cinfães'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Cinfães (coberta)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0796804)) <= 0.00070
               AND ABS(v.longitude - (-8.1060162)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:275935498 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Meijinhos', 'Lamego', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0418640, -7.8327639, 'OPENSTREETMAP', 'OSM:WAY:275935498',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:275935498')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Meijinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0418640)) <= 0.00070
               AND ABS(v.longitude - (-7.8327639)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1318129768 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Sande', 'Lamego', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1303428, -7.7841019, 'OPENSTREETMAP', 'OSM:WAY:1318129768',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1318129768')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Sande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1303428)) <= 0.00070
               AND ABS(v.longitude - (-7.7841019)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122279548 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treino', 'Lamego', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0890823, -7.8152290, 'OPENSTREETMAP', 'OSM:WAY:122279548',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122279548')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treino'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0890823)) <= 0.00070
               AND ABS(v.longitude - (-7.8152290)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122278312 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Nossa Senhora dos Remédios', 'Lamego', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       41.0898406, -7.8160019, 'OPENSTREETMAP', 'OSM:WAY:122278312',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122278312')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Nossa Senhora dos Remédios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0898406)) <= 0.00070
               AND ABS(v.longitude - (-7.8160019)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122297718 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Multi Desportivo', 'Lamego', 'Viseu', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.0863208, -7.8142780, 'OPENSTREETMAP', 'OSM:WAY:122297718',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122297718')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Multi Desportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0863208)) <= 0.00070
               AND ABS(v.longitude - (-7.8142780)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122725932 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cobertas de Lamego', 'Lamego', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0946991, -7.8133206, 'OPENSTREETMAP', 'OSM:WAY:122725932',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122725932')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cobertas de Lamego'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0946991)) <= 0.00070
               AND ABS(v.longitude - (-7.8133206)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122297831 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Poli Desportivo', 'Lamego', 'Viseu', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       41.0833717, -7.8058898, 'OPENSTREETMAP', 'OSM:WAY:122297831',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122297831')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Poli Desportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0833717)) <= 0.00070
               AND ABS(v.longitude - (-7.8058898)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1030461699 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ténis Clube de Lamego', 'Lamego', 'Viseu', NULL,
       'Ténis', 'Campo desportivo', NULL,
       41.0888364, -7.8166538, 'OPENSTREETMAP', 'OSM:WAY:1030461699',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1030461699')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lamego'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ténis Clube de Lamego'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0888364)) <= 0.00070
               AND ABS(v.longitude - (-7.8166538)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12452979701 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Best Fit', 'Mangualde', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6182450, -7.7662535, 'OPENSTREETMAP', 'OSM:NODE:12452979701',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12452979701')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mangualde'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Best Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6182450)) <= 0.00070
               AND ABS(v.longitude - (-7.7662535)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:819369038 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Aldeia de Nacomba', 'Moimenta da Beira', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9608158, -7.6141256, 'OPENSTREETMAP', 'OSM:WAY:819369038',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:819369038')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moimenta da Beira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Aldeia de Nacomba'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9608158)) <= 0.00070
               AND ABS(v.longitude - (-7.6141256)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:616347152 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Moimenta da Beira', 'Moimenta da Beira', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       40.9894085, -7.6162607, 'OPENSTREETMAP', 'OSM:WAY:616347152',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:616347152')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moimenta da Beira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Moimenta da Beira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9894085)) <= 0.00070
               AND ABS(v.longitude - (-7.6162607)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1270448681 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Moimenta da Beira', 'Moimenta da Beira', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9840753, -7.6157307, 'OPENSTREETMAP', 'OSM:WAY:1270448681',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1270448681')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Moimenta da Beira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Moimenta da Beira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9840753)) <= 0.00070
               AND ABS(v.longitude - (-7.6157307)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:816841124 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Gatuna (Sport Vale de Madeiros e Benfica)', 'Nelas', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4914629, -7.8749426, 'OPENSTREETMAP', 'OSM:WAY:816841124',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:816841124')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Nelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Gatuna (Sport Vale de Madeiros e Benfica)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4914629)) <= 0.00070
               AND ABS(v.longitude - (-7.8749426)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:213882440 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Publica', 'Nelas', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.5064093, -7.8986187, 'OPENSTREETMAP', 'OSM:WAY:213882440',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:213882440')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Nelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Publica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5064093)) <= 0.00070
               AND ABS(v.longitude - (-7.8986187)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:910981003 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futsal exterior', 'Oliveira de Frades', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7372472, -8.1765976, 'OPENSTREETMAP', 'OSM:WAY:910981003',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:910981003')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Frades'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futsal exterior'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7372472)) <= 0.00070
               AND ABS(v.longitude - (-8.1765976)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:910650644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis do GDOF', 'Oliveira de Frades', 'Viseu', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.7376693, -8.1768706, 'OPENSTREETMAP', 'OSM:WAY:910650644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:910650644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Frades'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis do GDOF'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7376693)) <= 0.00070
               AND ABS(v.longitude - (-8.1768706)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:281163126 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo de Oliveira de Frades', 'Oliveira de Frades', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7372961, -8.1757262, 'OPENSTREETMAP', 'OSM:WAY:281163126',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:281163126')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Frades'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo de Oliveira de Frades'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7372961)) <= 0.00070
               AND ABS(v.longitude - (-8.1757262)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:904785362 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Oliveira de Frades', 'Oliveira de Frades', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.7363629, -8.1752876, 'OPENSTREETMAP', 'OSM:WAY:904785362',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:904785362')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Frades'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Oliveira de Frades'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7363629)) <= 0.00070
               AND ABS(v.longitude - (-8.1752876)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:776871936 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Penedono', 'Penedono', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.9909199, -7.3927441, 'OPENSTREETMAP', 'OSM:WAY:776871936',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:776871936')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Penedono'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Penedono'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9909199)) <= 0.00070
               AND ABS(v.longitude - (-7.3927441)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:895752461 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta de Resende', 'Resende', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       41.1069973, -7.9623130, 'OPENSTREETMAP', 'OSM:WAY:895752461',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:895752461')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Resende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta de Resende'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1069973)) <= 0.00070
               AND ABS(v.longitude - (-7.9623130)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:108166251 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Resende', 'Resende', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       41.1019235, -7.9576449, 'OPENSTREETMAP', 'OSM:WAY:108166251',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:108166251')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Resende'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Resende'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1019235)) <= 0.00070
               AND ABS(v.longitude - (-7.9576449)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1432134617 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Dr. Estevão de Faria', 'Santa Comba Dão', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       40.3957394, -8.1392348, 'OPENSTREETMAP', 'OSM:WAY:1432134617',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1432134617')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Comba Dão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Dr. Estevão de Faria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3957394)) <= 0.00070
               AND ABS(v.longitude - (-8.1392348)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:132546107 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Coberta', 'São João da Pesqueira', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       41.1506262, -7.4110463, 'OPENSTREETMAP', 'OSM:WAY:132546107',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:132546107')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Pesqueira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Coberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1506262)) <= 0.00070
               AND ABS(v.longitude - (-7.4110463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:666718447 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de futebol', 'São João da Pesqueira', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0853520, -7.4312619, 'OPENSTREETMAP', 'OSM:WAY:666718447',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:666718447')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Pesqueira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0853520)) <= 0.00070
               AND ABS(v.longitude - (-7.4312619)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:280840818 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal da Pedreira', 'São Pedro do Sul', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7574731, -8.0540852, 'OPENSTREETMAP', 'OSM:WAY:280840818',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:280840818')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Pedro do Sul'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal da Pedreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7574731)) <= 0.00070
               AND ABS(v.longitude - (-8.0540852)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1054362326 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Gerós', 'São Pedro do Sul', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.7415642, -8.0846706, 'OPENSTREETMAP', 'OSM:WAY:1054362326',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1054362326')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São Pedro do Sul'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Gerós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7415642)) <= 0.00070
               AND ABS(v.longitude - (-8.0846706)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:335263492 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de 11 de Vila Longa', 'Sátão', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7526840, -7.5882462, 'OPENSTREETMAP', 'OSM:WAY:335263492',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:335263492')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sátão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de 11 de Vila Longa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7526840)) <= 0.00070
               AND ABS(v.longitude - (-7.5882462)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:335263494 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futsal de Vila Longa', 'Sátão', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7521960, -7.5877177, 'OPENSTREETMAP', 'OSM:WAY:335263494',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:335263494')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sátão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futsal de Vila Longa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7521960)) <= 0.00070
               AND ABS(v.longitude - (-7.5877177)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:343271464 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Recreativo de Ferreira de Aves', 'Sátão', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7955435, -7.6960767, 'OPENSTREETMAP', 'OSM:WAY:343271464',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:343271464')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sátão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Recreativo de Ferreira de Aves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7955435)) <= 0.00070
               AND ABS(v.longitude - (-7.6960767)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1001267488 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Montragão', 'Sátão', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       40.7956609, -7.6958232, 'OPENSTREETMAP', 'OSM:WAY:1001267488',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1001267488')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sátão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Montragão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7956609)) <= 0.00070
               AND ABS(v.longitude - (-7.6958232)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157280050 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal da Premoreira', 'Sátão', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7484689, -7.7362744, 'OPENSTREETMAP', 'OSM:WAY:157280050',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157280050')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sátão'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal da Premoreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7484689)) <= 0.00070
               AND ABS(v.longitude - (-7.7362744)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:856082150 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Tabuaço', 'Tabuaço', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.1141752, -7.5681271, 'OPENSTREETMAP', 'OSM:WAY:856082150',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:856082150')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tabuaço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Tabuaço'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1141752)) <= 0.00070
               AND ABS(v.longitude - (-7.5681271)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1182951782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol e Basquetebol', 'Tarouca', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0246887, -7.7732952, 'OPENSTREETMAP', 'OSM:WAY:1182951782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1182951782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol e Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0246887)) <= 0.00070
               AND ABS(v.longitude - (-7.7732952)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:87605211 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Tarouca', 'Tarouca', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0160020, -7.7781285, 'OPENSTREETMAP', 'OSM:WAY:87605211',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:87605211')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Tarouca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0160020)) <= 0.00070
               AND ABS(v.longitude - (-7.7781285)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:91057769 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Fluvial de Várzea da Serra', 'Tarouca', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9923454, -7.8238825, 'OPENSTREETMAP', 'OSM:WAY:91057769',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:91057769')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Fluvial de Várzea da Serra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9923454)) <= 0.00070
               AND ABS(v.longitude - (-7.8238825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:87605207 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Interior de Tarouca', 'Tarouca', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       41.0150937, -7.7791988, 'OPENSTREETMAP', 'OSM:WAY:87605207',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:87605207')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Interior de Tarouca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0150937)) <= 0.00070
               AND ABS(v.longitude - (-7.7791988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1182951767 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Parque Ribeirinho de Tarouca', 'Tarouca', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0250787, -7.7717680, 'OPENSTREETMAP', 'OSM:WAY:1182951767',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1182951767')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Parque Ribeirinho de Tarouca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0250787)) <= 0.00070
               AND ABS(v.longitude - (-7.7717680)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:87605210 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Tarouca', 'Tarouca', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       41.0153872, -7.7786913, 'OPENSTREETMAP', 'OSM:WAY:87605210',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:87605210')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Tarouca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0153872)) <= 0.00070
               AND ABS(v.longitude - (-7.7786913)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1812405460 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PowerGym', 'Tarouca', 'Viseu', NULL,
       'Ginástica', 'Ginásio', NULL,
       41.0277952, -7.7721571, 'OPENSTREETMAP', 'OSM:NODE:1812405460',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1812405460')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PowerGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0277952)) <= 0.00070
               AND ABS(v.longitude - (-7.7721571)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1182951780 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark', 'Tarouca', 'Viseu', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.0245053, -7.7726181, 'OPENSTREETMAP', 'OSM:WAY:1182951780',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1182951780')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tarouca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0245053)) <= 0.00070
               AND ABS(v.longitude - (-7.7726181)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:419742503 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Monte de São Marcos', 'Tondela', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5869747, -8.1322275, 'OPENSTREETMAP', 'OSM:WAY:419742503',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:419742503')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Monte de São Marcos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5869747)) <= 0.00070
               AND ABS(v.longitude - (-8.1322275)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:418357639 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Rugby de Molelinhos', 'Tondela', 'Viseu', NULL,
       'Rugby', 'Campo desportivo', NULL,
       40.5207492, -8.1122404, 'OPENSTREETMAP', 'OSM:WAY:418357639',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:418357639')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Rugby de Molelinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5207492)) <= 0.00070
               AND ABS(v.longitude - (-8.1122404)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:424972290 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Sporting Clube De Nandufe', 'Tondela', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5381643, -8.0826626, 'OPENSTREETMAP', 'OSM:WAY:424972290',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:424972290')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Sporting Clube De Nandufe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5381643)) <= 0.00070
               AND ABS(v.longitude - (-8.0826626)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158972574 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Atletico de Molelos', 'Tondela', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5340375, -8.1092709, 'OPENSTREETMAP', 'OSM:WAY:158972574',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158972574')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Atletico de Molelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5340375)) <= 0.00070
               AND ABS(v.longitude - (-8.1092709)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:640773825 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo de Tondela', 'Tondela', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5120464, -8.0830656, 'OPENSTREETMAP', 'OSM:WAY:640773825',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:640773825')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo de Tondela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5120464)) <= 0.00070
               AND ABS(v.longitude - (-8.0830656)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:416581541 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Estádio João Cardoso', 'Tondela', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       40.5120725, -8.0825163, 'OPENSTREETMAP', 'OSM:WAY:416581541',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:416581541')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Estádio João Cardoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5120725)) <= 0.00070
               AND ABS(v.longitude - (-8.0825163)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:416581540 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Estádio João Cardoso - Campo n.º 2', 'Tondela', 'Viseu', NULL,
       'Futebol', 'Estádio', NULL,
       40.5110270, -8.0840919, 'OPENSTREETMAP', 'OSM:WAY:416581540',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:416581540')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Estádio João Cardoso - Campo n.º 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5110270)) <= 0.00070
               AND ABS(v.longitude - (-8.0840919)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:345720223 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Tomaz Ribeiro', 'Tondela', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5765096, -8.0019643, 'OPENSTREETMAP', 'OSM:WAY:345720223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:345720223')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Tomaz Ribeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5765096)) <= 0.00070
               AND ABS(v.longitude - (-8.0019643)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:425020782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal - Campo de Besteiros', 'Tondela', 'Viseu', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.5626386, -8.1230381, 'OPENSTREETMAP', 'OSM:WAY:425020782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:425020782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal - Campo de Besteiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5626386)) <= 0.00070
               AND ABS(v.longitude - (-8.1230381)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6015359899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park de Tondela', 'Tondela', 'Viseu', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.5192541, -8.0815964, 'OPENSTREETMAP', 'OSM:NODE:6015359899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6015359899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tondela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park de Tondela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5192541)) <= 0.00070
               AND ABS(v.longitude - (-8.0815964)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:384606325 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Vila Nova de Paiva', 'Vila Nova de Paiva', 'Viseu', NULL,
       'Natação', 'Piscina', NULL,
       40.8549676, -7.7263947, 'OPENSTREETMAP', 'OSM:WAY:384606325',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:384606325')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Paiva'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Vila Nova de Paiva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8549676)) <= 0.00070
               AND ABS(v.longitude - (-7.7263947)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7141399202 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'BestFit', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6508760, -7.8862638, 'OPENSTREETMAP', 'OSM:NODE:7141399202',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7141399202')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('BestFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6508760)) <= 0.00070
               AND ABS(v.longitude - (-7.8862638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:462826971 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Bola', 'Viseu', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6273148, -7.8672318, 'OPENSTREETMAP', 'OSM:WAY:462826971',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:462826971')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Bola'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6273148)) <= 0.00070
               AND ABS(v.longitude - (-7.8672318)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:836796449 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Fail', 'Viseu', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6119359, -7.9777065, 'OPENSTREETMAP', 'OSM:WAY:836796449',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:836796449')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Fail'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6119359)) <= 0.00070
               AND ABS(v.longitude - (-7.9777065)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:525248172 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Paradinha', 'Viseu', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6411182, -7.9416284, 'OPENSTREETMAP', 'OSM:WAY:525248172',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:525248172')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Paradinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6411182)) <= 0.00070
               AND ABS(v.longitude - (-7.9416284)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:511388124 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo desportos de praia', 'Viseu', 'Viseu', NULL,
       'Voleibol de Praia', 'Campo desportivo', NULL,
       40.6610252, -7.9035724, 'OPENSTREETMAP', 'OSM:WAY:511388124',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:511388124')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo desportos de praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6610252)) <= 0.00070
               AND ABS(v.longitude - (-7.9035724)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:836796448 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Grupo Desportivo de Fail', 'Viseu', 'Viseu', NULL,
       'Futsal;Andebol;Ténis', 'Campo desportivo', NULL,
       40.6124708, -7.9779203, 'OPENSTREETMAP', 'OSM:WAY:836796448',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:836796448')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Grupo Desportivo de Fail'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6124708)) <= 0.00070
               AND ABS(v.longitude - (-7.9779203)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:278808186 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol Leões da Beira', 'Viseu', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6610938, -7.8796546, 'OPENSTREETMAP', 'OSM:WAY:278808186',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:278808186')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol Leões da Beira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6610938)) <= 0.00070
               AND ABS(v.longitude - (-7.8796546)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138228986 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo José Alves Madeira', 'Viseu', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6610775, -7.9045276, 'OPENSTREETMAP', 'OSM:WAY:138228986',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138228986')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo José Alves Madeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6610775)) <= 0.00070
               AND ABS(v.longitude - (-7.9045276)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:138306106 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio dos Trambelos', 'Viseu', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6581314, -7.9256636, 'OPENSTREETMAP', 'OSM:WAY:138306106',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:138306106')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio dos Trambelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6581314)) <= 0.00070
               AND ABS(v.longitude - (-7.9256636)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:504273561 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'FFitness Health Club', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6612490, -7.9284317, 'OPENSTREETMAP', 'OSM:WAY:504273561',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:504273561')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('FFitness Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6612490)) <= 0.00070
               AND ABS(v.longitude - (-7.9284317)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10248150260 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6623108, -7.9135016, 'OPENSTREETMAP', 'OSM:NODE:10248150260',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10248150260')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6623108)) <= 0.00070
               AND ABS(v.longitude - (-7.9135016)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:777968215 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness UP Viseu', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6523178, -7.9133126, 'OPENSTREETMAP', 'OSM:WAY:777968215',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:777968215')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness UP Viseu'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6523178)) <= 0.00070
               AND ABS(v.longitude - (-7.9133126)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5002433944 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Forlife Desporto', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6433320, -7.9117867, 'OPENSTREETMAP', 'OSM:NODE:5002433944',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5002433944')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Forlife Desporto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6433320)) <= 0.00070
               AND ABS(v.longitude - (-7.9117867)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9988608805 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'La Palestra', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6719185, -7.9295351, 'OPENSTREETMAP', 'OSM:NODE:9988608805',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9988608805')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('La Palestra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6719185)) <= 0.00070
               AND ABS(v.longitude - (-7.9295351)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240183075 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Montebelo Golfe', 'Viseu', 'Viseu', NULL,
       'Golfe', 'Campo de golfe', NULL,
       40.6095170, -8.0363486, 'OPENSTREETMAP', 'OSM:WAY:240183075',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240183075')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Montebelo Golfe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6095170)) <= 0.00070
               AND ABS(v.longitude - (-8.0363486)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13347258728 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Padel Viseu Academy', 'Viseu', 'Viseu', NULL,
       'Padel', 'Campo desportivo', NULL,
       40.6352113, -7.9276977, 'OPENSTREETMAP', 'OSM:NODE:13347258728',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13347258728')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Padel Viseu Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6352113)) <= 0.00070
               AND ABS(v.longitude - (-7.9276977)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9035569704 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Primum - Studio de Pilates', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6419956, -7.9133096, 'OPENSTREETMAP', 'OSM:NODE:9035569704',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9035569704')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Primum - Studio de Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6419956)) <= 0.00070
               AND ABS(v.longitude - (-7.9133096)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9968353891 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Reboot', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6495274, -7.9146746, 'OPENSTREETMAP', 'OSM:NODE:9968353891',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9968353891')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Reboot'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6495274)) <= 0.00070
               AND ABS(v.longitude - (-7.9146746)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13250788887 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Templo Dourado', 'Viseu', 'Viseu', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.6625334, -7.8952094, 'OPENSTREETMAP', 'OSM:NODE:13250788887',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13250788887')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Viseu'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Templo Dourado'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6625334)) <= 0.00070
               AND ABS(v.longitude - (-7.8952094)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:521868852 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Pendão', 'Vouzela', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6883186, -8.1526615, 'OPENSTREETMAP', 'OSM:WAY:521868852',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:521868852')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vouzela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Pendão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6883186)) <= 0.00070
               AND ABS(v.longitude - (-8.1526615)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:167013418 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal das Chãs', 'Vouzela', 'Viseu', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7283700, -8.1134963, 'OPENSTREETMAP', 'OSM:WAY:167013418',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:167013418')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vouzela'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Municipal das Chãs'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7283700)) <= 0.00070
               AND ABS(v.longitude - (-8.1134963)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
