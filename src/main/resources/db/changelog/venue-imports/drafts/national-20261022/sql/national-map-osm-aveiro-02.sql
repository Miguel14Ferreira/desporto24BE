-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:244610272 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Arinhos', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4117733, -8.4878421, 'OPENSTREETMAP', 'OSM:WAY:244610272',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244610272')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Arinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4117733)) <= 0.00070
               AND ABS(v.longitude - (-8.4878421)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:247603837 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Jorge Manuel', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3754338, -8.3858495, 'OPENSTREETMAP', 'OSM:WAY:247603837',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:247603837')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Jorge Manuel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3754338)) <= 0.00070
               AND ABS(v.longitude - (-8.3858495)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152620959 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Municipal Carlos Duarte', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3298159, -8.4258724, 'OPENSTREETMAP', 'OSM:WAY:152620959',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152620959')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Municipal Carlos Duarte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3298159)) <= 0.00070
               AND ABS(v.longitude - (-8.4258724)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152620960 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Germano Godinho', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3305005, -8.4264743, 'OPENSTREETMAP', 'OSM:WAY:152620960',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152620960')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Germano Godinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3305005)) <= 0.00070
               AND ABS(v.longitude - (-8.4264743)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:247661596 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'campo de voleibol', 'Mealhada', 'Aveiro', NULL,
       'Voleibol', 'Campo desportivo', NULL,
       40.3396080, -8.4210614, 'OPENSTREETMAP', 'OSM:WAY:247661596',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:247661596')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('campo de voleibol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3396080)) <= 0.00070
               AND ABS(v.longitude - (-8.4210614)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14086425598 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Cross Company', 'Mealhada', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.3764478, -8.4520044, 'OPENSTREETMAP', 'OSM:NODE:14086425598',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14086425598')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Cross Company'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3764478)) <= 0.00070
               AND ABS(v.longitude - (-8.4520044)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:159048424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Doutor Américo Couto', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.3740358, -8.4554424, 'OPENSTREETMAP', 'OSM:WAY:159048424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:159048424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Doutor Américo Couto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3740358)) <= 0.00070
               AND ABS(v.longitude - (-8.4554424)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182178622 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol de Praia', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3857349, -8.4496169, 'OPENSTREETMAP', 'OSM:WAY:182178622',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182178622')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol de Praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3857349)) <= 0.00070
               AND ABS(v.longitude - (-8.4496169)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2541679090 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio korpoActivo', 'Mealhada', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.3869518, -8.4514472, 'OPENSTREETMAP', 'OSM:NODE:2541679090',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2541679090')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio korpoActivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3869518)) <= 0.00070
               AND ABS(v.longitude - (-8.4514472)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003553020 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo da Mealhada', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3741242, -8.4553962, 'OPENSTREETMAP', 'OSM:WAY:1003553020',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003553020')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo da Mealhada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3741242)) <= 0.00070
               AND ABS(v.longitude - (-8.4553962)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1004639003 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque do Povo', 'Mealhada', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.3113174, -8.4768455, 'OPENSTREETMAP', 'OSM:WAY:1004639003',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1004639003')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque do Povo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3113174)) <= 0.00070
               AND ABS(v.longitude - (-8.4768455)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:449880600 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal de Ventosa do Bairro', 'Mealhada', 'Aveiro', NULL,
       'Futsal', 'Complexo desportivo', NULL,
       40.3985613, -8.4824683, 'OPENSTREETMAP', 'OSM:WAY:449880600',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:449880600')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal de Ventosa do Bairro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3985613)) <= 0.00070
               AND ABS(v.longitude - (-8.4824683)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169293290 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas 3 Pinheiros', 'Mealhada', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.3932896, -8.4487160, 'OPENSTREETMAP', 'OSM:WAY:1169293290',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169293290')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas 3 Pinheiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3932896)) <= 0.00070
               AND ABS(v.longitude - (-8.4487160)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:110014998 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Mealhada', 'Mealhada', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.3753898, -8.4554700, 'OPENSTREETMAP', 'OSM:WAY:110014998',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:110014998')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Mealhada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3753898)) <= 0.00070
               AND ABS(v.longitude - (-8.4554700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:824376721 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo do Luso', 'Mealhada', 'Aveiro', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.3763487, -8.3879487, 'OPENSTREETMAP', 'OSM:WAY:824376721',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:824376721')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mealhada'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo do Luso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3763487)) <= 0.00070
               AND ABS(v.longitude - (-8.3879487)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:245002888 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal da Murtosa', 'Murtosa', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7485284, -8.6450665, 'OPENSTREETMAP', 'OSM:WAY:245002888',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:245002888')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murtosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal da Murtosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7485284)) <= 0.00070
               AND ABS(v.longitude - (-8.6450665)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:245102330 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais da Murtosa', 'Murtosa', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.7464944, -8.6454802, 'OPENSTREETMAP', 'OSM:WAY:245102330',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:245102330')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murtosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais da Murtosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7464944)) <= 0.00070
               AND ABS(v.longitude - (-8.6454802)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:823590982 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Simplificada da Murtosa', 'Murtosa', 'Aveiro', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.7485291, -8.6448294, 'OPENSTREETMAP', 'OSM:WAY:823590982',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823590982')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Murtosa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista Simplificada da Murtosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7485291)) <= 0.00070
               AND ABS(v.longitude - (-8.6448294)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:852182144 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Calvário', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8778129, -8.4634068, 'OPENSTREETMAP', 'OSM:WAY:852182144',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:852182144')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Calvário'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8778129)) <= 0.00070
               AND ABS(v.longitude - (-8.4634068)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:550116582 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Barão', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8141020, -8.5257356, 'OPENSTREETMAP', 'OSM:WAY:550116582',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:550116582')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Barão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8141020)) <= 0.00070
               AND ABS(v.longitude - (-8.5257356)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003199573 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Doutor Teixeira da Silva', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8844440, -8.4229693, 'OPENSTREETMAP', 'OSM:WAY:1003199573',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003199573')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Doutor Teixeira da Silva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8844440)) <= 0.00070
               AND ABS(v.longitude - (-8.4229693)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:496114237 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Viso - Futebol Clube Macieirense', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9136943, -8.4586410, 'OPENSTREETMAP', 'OSM:WAY:496114237',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:496114237')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Viso - Futebol Clube Macieirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9136943)) <= 0.00070
               AND ABS(v.longitude - (-8.4586410)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142262185 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Formação Ápio Assunção', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.8317365, -8.4838196, 'OPENSTREETMAP', 'OSM:WAY:142262185',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142262185')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Formação Ápio Assunção'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8317365)) <= 0.00070
               AND ABS(v.longitude - (-8.4838196)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:547064782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Desportivo e Cultural de S.Martinho da Gândara', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8528708, -8.5394837, 'OPENSTREETMAP', 'OSM:WAY:547064782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:547064782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Desportivo e Cultural de S.Martinho da Gândara'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8528708)) <= 0.00070
               AND ABS(v.longitude - (-8.5394837)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142262191 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Azeméis', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.8339489, -8.4840817, 'OPENSTREETMAP', 'OSM:WAY:142262191',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142262191')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Azeméis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8339489)) <= 0.00070
               AND ABS(v.longitude - (-8.4840817)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166358816 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Palmaz', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.7986176, -8.4543886, 'OPENSTREETMAP', 'OSM:WAY:166358816',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166358816')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Palmaz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7986176)) <= 0.00070
               AND ABS(v.longitude - (-8.4543886)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:693590299 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Mergulhão - Futebol Clube Cesarense', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9019187, -8.4394229, 'OPENSTREETMAP', 'OSM:WAY:693590299',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:693590299')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Mergulhão - Futebol Clube Cesarense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9019187)) <= 0.00070
               AND ABS(v.longitude - (-8.4394229)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:213261377 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Jaime Rocha', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.7833267, -8.4974279, 'OPENSTREETMAP', 'OSM:WAY:213261377',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:213261377')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Jaime Rocha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7833267)) <= 0.00070
               AND ABS(v.longitude - (-8.4974279)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142262195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Quinta do Côvo', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.8620560, -8.4608238, 'OPENSTREETMAP', 'OSM:WAY:142262195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142262195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Quinta do Côvo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8620560)) <= 0.00070
               AND ABS(v.longitude - (-8.4608238)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:213261443 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube Pinheirense', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7832270, -8.4978805, 'OPENSTREETMAP', 'OSM:WAY:213261443',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:213261443')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube Pinheirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7832270)) <= 0.00070
               AND ABS(v.longitude - (-8.4978805)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:330111992 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo de Fajões', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9179524, -8.4116426, 'OPENSTREETMAP', 'OSM:WAY:330111992',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:330111992')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo de Fajões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9179524)) <= 0.00070
               AND ABS(v.longitude - (-8.4116426)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:119646353 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de jogos Vila de Cucujães', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.8728402, -8.5016971, 'OPENSTREETMAP', 'OSM:WAY:119646353',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:119646353')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de jogos Vila de Cucujães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8728402)) <= 0.00070
               AND ABS(v.longitude - (-8.5016971)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142262200 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão da União Desportiva Oliveirense', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.8311296, -8.4851939, 'OPENSTREETMAP', 'OSM:WAY:142262200',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142262200')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão da União Desportiva Oliveirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8311296)) <= 0.00070
               AND ABS(v.longitude - (-8.4851939)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142262194 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal Professor António Costeira', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       40.8330667, -8.4845907, 'OPENSTREETMAP', 'OSM:WAY:142262194',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142262194')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal Professor António Costeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8330667)) <= 0.00070
               AND ABS(v.longitude - (-8.4845907)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152508665 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal La Salette', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.8411036, -8.4651210, 'OPENSTREETMAP', 'OSM:WAY:1152508665',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152508665')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal La Salette'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8411036)) <= 0.00070
               AND ABS(v.longitude - (-8.4651210)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142262196 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.8324156, -8.4853452, 'OPENSTREETMAP', 'OSM:WAY:142262196',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142262196')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8324156)) <= 0.00070
               AND ABS(v.longitude - (-8.4853452)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:158681301 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Santiago', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8564364, -8.4902891, 'OPENSTREETMAP', 'OSM:WAY:158681301',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:158681301')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Santiago'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8564364)) <= 0.00070
               AND ABS(v.longitude - (-8.4902891)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:871399383 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Real Clube Nogueirense', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8940384, -8.4584961, 'OPENSTREETMAP', 'OSM:WAY:871399383',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:871399383')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Real Clube Nogueirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8940384)) <= 0.00070
               AND ABS(v.longitude - (-8.4584961)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003279372 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube de Bustelo', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8620422, -8.4607017, 'OPENSTREETMAP', 'OSM:WAY:1003279372',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003279372')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube de Bustelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8620422)) <= 0.00070
               AND ABS(v.longitude - (-8.4607017)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1002433173 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva Oliveirense', 'Oliveira de Azeméis', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8420462, -8.4704405, 'OPENSTREETMAP', 'OSM:WAY:1002433173',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1002433173')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira de Azeméis'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva Oliveirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8420462)) <= 0.00070
               AND ABS(v.longitude - (-8.4704405)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:220641533 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol da Giesta', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5397277, -8.5164338, 'OPENSTREETMAP', 'OSM:WAY:220641533',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:220641533')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol da Giesta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5397277)) <= 0.00070
               AND ABS(v.longitude - (-8.5164338)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:692208075 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Silveira', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5305234, -8.5439036, 'OPENSTREETMAP', 'OSM:WAY:692208075',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:692208075')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Silveira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5305234)) <= 0.00070
               AND ABS(v.longitude - (-8.5439036)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:244077276 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Troviscal', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5056687, -8.5493734, 'OPENSTREETMAP', 'OSM:WAY:244077276',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244077276')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Troviscal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5056687)) <= 0.00070
               AND ABS(v.longitude - (-8.5493734)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:244083896 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Vila Verde', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5118482, -8.5248941, 'OPENSTREETMAP', 'OSM:WAY:244083896',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244083896')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Vila Verde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5118482)) <= 0.00070
               AND ABS(v.longitude - (-8.5248941)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182598920 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Gorgulhão', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4854577, -8.5828626, 'OPENSTREETMAP', 'OSM:WAY:182598920',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182598920')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Gorgulhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4854577)) <= 0.00070
               AND ABS(v.longitude - (-8.5828626)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:239342550 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Santa Margarida', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5357574, -8.5548040, 'OPENSTREETMAP', 'OSM:WAY:239342550',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:239342550')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Santa Margarida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5357574)) <= 0.00070
               AND ABS(v.longitude - (-8.5548040)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003274131 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Oliveira do Bairro Sport Clube', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5060516, -8.4819753, 'OPENSTREETMAP', 'OSM:WAY:1003274131',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003274131')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Oliveira do Bairro Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5060516)) <= 0.00070
               AND ABS(v.longitude - (-8.4819753)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1006011789 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Vieiro', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.5471642, -8.5422627, 'OPENSTREETMAP', 'OSM:WAY:1006011789',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1006011789')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Vieiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5471642)) <= 0.00070
               AND ABS(v.longitude - (-8.5422627)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240273645 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.5068588, -8.4838065, 'OPENSTREETMAP', 'OSM:WAY:240273645',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240273645')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5068588)) <= 0.00070
               AND ABS(v.longitude - (-8.4838065)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:824376719 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Oliveira do Bairro', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.5060869, -8.4818901, 'OPENSTREETMAP', 'OSM:WAY:824376719',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:824376719')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Oliveira do Bairro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5060869)) <= 0.00070
               AND ABS(v.longitude - (-8.4818901)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1430011098 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park de Oiã', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.5450224, -8.5398656, 'OPENSTREETMAP', 'OSM:WAY:1430011098',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1430011098')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park de Oiã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5450224)) <= 0.00070
               AND ABS(v.longitude - (-8.5398656)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003547996 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Desportiva de Bustos', 'Oliveira do Bairro', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5019119, -8.5886574, 'OPENSTREETMAP', 'OSM:WAY:1003547996',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003547996')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oliveira do Bairro'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Desportiva de Bustos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5019119)) <= 0.00070
               AND ABS(v.longitude - (-8.5886574)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:665837484 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Arada Atlético Clube', 'Ovar', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9119451, -8.6036028, 'OPENSTREETMAP', 'OSM:WAY:665837484',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:665837484')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Arada Atlético Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9119451)) <= 0.00070
               AND ABS(v.longitude - (-8.6036028)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10259729069 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Be-Fit', 'Ovar', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.8927838, -8.6162455, 'OPENSTREETMAP', 'OSM:NODE:10259729069',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10259729069')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Be-Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8927838)) <= 0.00070
               AND ABS(v.longitude - (-8.6162455)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:404400508 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Barrinha', 'Ovar', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9592949, -8.6434799, 'OPENSTREETMAP', 'OSM:WAY:404400508',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:404400508')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Barrinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9592949)) <= 0.00070
               AND ABS(v.longitude - (-8.6434799)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:437612130 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de treinos', 'Ovar', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.8655538, -8.6265597, 'OPENSTREETMAP', 'OSM:WAY:437612130',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:437612130')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de treinos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8655538)) <= 0.00070
               AND ABS(v.longitude - (-8.6265597)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:516638022 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural e Recreativo de Válega', 'Ovar', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8309263, -8.6112057, 'OPENSTREETMAP', 'OSM:WAY:516638022',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:516638022')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural e Recreativo de Válega'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8309263)) <= 0.00070
               AND ABS(v.longitude - (-8.6112057)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1317430023 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo do Furadouro', 'Ovar', 'Aveiro', NULL,
       'Futebol;Ténis', 'Complexo desportivo', NULL,
       40.8766001, -8.6721590, 'OPENSTREETMAP', 'OSM:WAY:1317430023',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1317430023')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo do Furadouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8766001)) <= 0.00070
               AND ABS(v.longitude - (-8.6721590)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:81622897 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis de Ovar', 'Ovar', 'Aveiro', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       40.8714244, -8.6434878, 'OPENSTREETMAP', 'OSM:WAY:81622897',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:81622897')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis de Ovar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8714244)) <= 0.00070
               AND ABS(v.longitude - (-8.6434878)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:396121343 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Barrinha', 'Ovar', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.9580465, -8.6427365, 'OPENSTREETMAP', 'OSM:WAY:396121343',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:396121343')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Barrinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9580465)) <= 0.00070
               AND ABS(v.longitude - (-8.6427365)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14049576633 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory', 'Ovar', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9573201, -8.6518788, 'OPENSTREETMAP', 'OSM:NODE:14049576633',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14049576633')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9573201)) <= 0.00070
               AND ABS(v.longitude - (-8.6518788)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:500721260 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up', 'Ovar', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.8759647, -8.6347764, 'OPENSTREETMAP', 'OSM:WAY:500721260',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:500721260')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8759647)) <= 0.00070
               AND ABS(v.longitude - (-8.6347764)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6462700086 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio da Barrinha - Esmoriz', 'Ovar', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9637014, -8.6381218, 'OPENSTREETMAP', 'OSM:NODE:6462700086',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6462700086')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio da Barrinha - Esmoriz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9637014)) <= 0.00070
               AND ABS(v.longitude - (-8.6381218)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:263811851 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Doutor Oliveira Santos', 'Ovar', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8717603, -8.5517481, 'OPENSTREETMAP', 'OSM:WAY:263811851',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:263811851')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Doutor Oliveira Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8717603)) <= 0.00070
               AND ABS(v.longitude - (-8.5517481)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:546786052 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão do Gestal', 'Ovar', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.9422117, -8.6251041, 'OPENSTREETMAP', 'OSM:WAY:546786052',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:546786052')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão do Gestal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9422117)) <= 0.00070
               AND ABS(v.longitude - (-8.6251041)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:437736266 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Doutor Raimundo Rodrigues', 'Ovar', 'Aveiro', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.8652013, -8.6273022, 'OPENSTREETMAP', 'OSM:WAY:437736266',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:437736266')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Doutor Raimundo Rodrigues'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8652013)) <= 0.00070
               AND ABS(v.longitude - (-8.6273022)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157628390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão "João Gonçalves" Ovarense', 'Ovar', 'Aveiro', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.8765906, -8.6334487, 'OPENSTREETMAP', 'OSM:WAY:157628390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157628390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão "João Gonçalves" Ovarense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8765906)) <= 0.00070
               AND ABS(v.longitude - (-8.6334487)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:157632700 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Parque de Ovar', 'Ovar', 'Aveiro', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.8720447, -8.6261060, 'OPENSTREETMAP', 'OSM:WAY:157632700',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:157632700')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Parque de Ovar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8720447)) <= 0.00070
               AND ABS(v.longitude - (-8.6261060)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4029561254 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'X5 Health Club', 'Ovar', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9584487, -8.6321722, 'OPENSTREETMAP', 'OSM:NODE:4029561254',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4029561254')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ovar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('X5 Health Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9584487)) <= 0.00070
               AND ABS(v.longitude - (-8.6321722)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:856733024 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva e Cultural de Lobão', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9951487, -8.4848046, 'OPENSTREETMAP', 'OSM:WAY:856733024',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:856733024')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva e Cultural de Lobão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9951487)) <= 0.00070
               AND ABS(v.longitude - (-8.4848046)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13119658385 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Base 520', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9445184, -8.5333134, 'OPENSTREETMAP', 'OSM:NODE:13119658385',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13119658385')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Base 520'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9445184)) <= 0.00070
               AND ABS(v.longitude - (-8.5333134)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154732414 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Caldas de São Jorge Sport Clube', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9657718, -8.5020610, 'OPENSTREETMAP', 'OSM:WAY:154732414',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154732414')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Caldas de São Jorge Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9657718)) <= 0.00070
               AND ABS(v.longitude - (-8.5020610)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1091663644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'campo da freguesia', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0057618, -8.5885970, 'OPENSTREETMAP', 'OSM:WAY:1091663644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1091663644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('campo da freguesia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0057618)) <= 0.00070
               AND ABS(v.longitude - (-8.5885970)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:936927627 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de desporto de Sanfins', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ténis;Futsal', 'Campo desportivo', NULL,
       40.9323591, -8.5178951, 'OPENSTREETMAP', 'OSM:WAY:936927627',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:936927627')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de desporto de Sanfins'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9323591)) <= 0.00070
               AND ABS(v.longitude - (-8.5178951)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1511768216 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de 7', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9536994, -8.5916226, 'OPENSTREETMAP', 'OSM:WAY:1511768216',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1511768216')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de 7'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9536994)) <= 0.00070
               AND ABS(v.longitude - (-8.5916226)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:690775818 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Pousadela', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0050366, -8.5743586, 'OPENSTREETMAP', 'OSM:WAY:690775818',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:690775818')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Pousadela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0050366)) <= 0.00070
               AND ABS(v.longitude - (-8.5743586)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:700434058 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol do Sporting Clube de São João de Ver', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9582503, -8.5562539, 'OPENSTREETMAP', 'OSM:WAY:700434058',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:700434058')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol do Sporting Clube de São João de Ver'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9582503)) <= 0.00070
               AND ABS(v.longitude - (-8.5562539)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:795643346 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Travanca', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9066434, -8.5614176, 'OPENSTREETMAP', 'OSM:WAY:795643346',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:795643346')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Travanca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9066434)) <= 0.00070
               AND ABS(v.longitude - (-8.5614176)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305359290 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Hoquei em Campo União de Lamas', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Hóquei em Campo', 'Campo desportivo', NULL,
       40.9773949, -8.5679650, 'OPENSTREETMAP', 'OSM:WAY:305359290',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305359290')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Hoquei em Campo União de Lamas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9773949)) <= 0.00070
               AND ABS(v.longitude - (-8.5679650)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:867690681 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Associação Desportiva de Sanguedo', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0122318, -8.5147027, 'OPENSTREETMAP', 'OSM:WAY:867690681',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:867690681')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Associação Desportiva de Sanguedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0122318)) <= 0.00070
               AND ABS(v.longitude - (-8.5147027)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:867690680 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Associação Desportiva e Culturl de Sanguedo', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0123176, -8.5140645, 'OPENSTREETMAP', 'OSM:WAY:867690680',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:867690680')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Associação Desportiva e Culturl de Sanguedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0123176)) <= 0.00070
               AND ABS(v.longitude - (-8.5140645)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:904308443 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos das Caniças', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9495720, -8.5602291, 'OPENSTREETMAP', 'OSM:WAY:904308443',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:904308443')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos das Caniças'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9495720)) <= 0.00070
               AND ABS(v.longitude - (-8.5602291)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182640111 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Fornos', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9115144, -8.5207036, 'OPENSTREETMAP', 'OSM:WAY:182640111',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182640111')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Fornos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9115144)) <= 0.00070
               AND ABS(v.longitude - (-8.5207036)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:753717782 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos de Santo André', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9296342, -8.5534409, 'OPENSTREETMAP', 'OSM:WAY:753717782',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:753717782')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos de Santo André'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9296342)) <= 0.00070
               AND ABS(v.longitude - (-8.5534409)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:959890979 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Clube Desportivo de Tarei', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8916888, -8.5819676, 'OPENSTREETMAP', 'OSM:WAY:959890979',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:959890979')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Clube Desportivo de Tarei'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8916888)) <= 0.00070
               AND ABS(v.longitude - (-8.5819676)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:959890981 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Clube Desportivo Soutense', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8905795, -8.5412794, 'OPENSTREETMAP', 'OSM:WAY:959890981',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:959890981')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Clube Desportivo Soutense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8905795)) <= 0.00070
               AND ABS(v.longitude - (-8.5412794)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:957943234 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Dona Zulmira Sá e Silva', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9777809, -8.5907650, 'OPENSTREETMAP', 'OSM:WAY:957943234',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:957943234')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Dona Zulmira Sá e Silva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9777809)) <= 0.00070
               AND ABS(v.longitude - (-8.5907650)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:943070805 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Relâmpago União Clube Nogueirense', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9991902, -8.5823274, 'OPENSTREETMAP', 'OSM:WAY:943070805',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:943070805')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Relâmpago União Clube Nogueirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9991902)) <= 0.00070
               AND ABS(v.longitude - (-8.5823274)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:753717798 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos dos Passionistas', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9279766, -8.5609591, 'OPENSTREETMAP', 'OSM:WAY:753717798',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:753717798')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos dos Passionistas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9279766)) <= 0.00070
               AND ABS(v.longitude - (-8.5609591)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:327953348 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos do Vale', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9873613, -8.4422495, 'OPENSTREETMAP', 'OSM:WAY:327953348',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:327953348')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos do Vale'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9873613)) <= 0.00070
               AND ABS(v.longitude - (-8.4422495)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:700434059 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos "Ervedal"', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9545102, -8.5548778, 'OPENSTREETMAP', 'OSM:WAY:700434059',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:700434059')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos "Ervedal"'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9545102)) <= 0.00070
               AND ABS(v.longitude - (-8.5548778)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:546635927 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos Floriano Borges', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9111525, -8.4731843, 'OPENSTREETMAP', 'OSM:WAY:546635927',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:546635927')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos Floriano Borges'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9111525)) <= 0.00070
               AND ABS(v.longitude - (-8.4731843)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:673961544 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos "Oliveira e Santos"', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9764514, -8.4672737, 'OPENSTREETMAP', 'OSM:WAY:673961544',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:673961544')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos "Oliveira e Santos"'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9764514)) <= 0.00070
               AND ABS(v.longitude - (-8.4672737)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:957221509 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis do Centro Social de Escapães', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.9221444, -8.5187039, 'OPENSTREETMAP', 'OSM:WAY:957221509',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:957221509')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis do Centro Social de Escapães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9221444)) <= 0.00070
               AND ABS(v.longitude - (-8.5187039)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1096839904 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis do Clube de Ténis de Lourosa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.9802348, -8.5345780, 'OPENSTREETMAP', 'OSM:WAY:1096839904',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1096839904')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis do Clube de Ténis de Lourosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9802348)) <= 0.00070
               AND ABS(v.longitude - (-8.5345780)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:870743921 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9954504, -8.5318258, 'OPENSTREETMAP', 'OSM:WAY:870743921',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:870743921')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9954504)) <= 0.00070
               AND ABS(v.longitude - (-8.5318258)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:449274938 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos do Canedo Futebol Clube', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0102770, -8.4449414, 'OPENSTREETMAP', 'OSM:WAY:449274938',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:449274938')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos do Canedo Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0102770)) <= 0.00070
               AND ABS(v.longitude - (-8.4449414)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:580621993 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos do Clube Desportivo Arrifanense', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9192859, -8.4968596, 'OPENSTREETMAP', 'OSM:WAY:580621993',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:580621993')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos do Clube Desportivo Arrifanense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9192859)) <= 0.00070
               AND ABS(v.longitude - (-8.4968596)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:546635928 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos do Grupo Desportivo Milheiroense', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9102651, -8.4726385, 'OPENSTREETMAP', 'OSM:WAY:546635928',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:546635928')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos do Grupo Desportivo Milheiroense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9102651)) <= 0.00070
               AND ABS(v.longitude - (-8.4726385)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:957386004 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos do Lusitânia Futebol Clube Lourosa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9878855, -8.5573213, 'OPENSTREETMAP', 'OSM:WAY:957386004',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:957386004')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos do Lusitânia Futebol Clube Lourosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9878855)) <= 0.00070
               AND ABS(v.longitude - (-8.5573213)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14070540266 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Voleibol de Praia Monte das Pedreiras', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Voleibol de Praia', 'Campo desportivo', NULL,
       40.9867871, -8.5182479, 'OPENSTREETMAP', 'OSM:NODE:14070540266',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14070540266')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Voleibol de Praia Monte das Pedreiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9867871)) <= 0.00070
               AND ABS(v.longitude - (-8.5182479)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:388964996 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Clube Desportivo Arrifanense', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9182090, -8.4946398, 'OPENSTREETMAP', 'OSM:WAY:388964996',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:388964996')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Clube Desportivo Arrifanense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9182090)) <= 0.00070
               AND ABS(v.longitude - (-8.4946398)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:870454536 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Padrão', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0104771, -8.4820815, 'OPENSTREETMAP', 'OSM:WAY:870454536',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:870454536')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Padrão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0104771)) <= 0.00070
               AND ABS(v.longitude - (-8.4820815)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:331408506 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Januário Monteiro', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9786328, -8.5925129, 'OPENSTREETMAP', 'OSM:WAY:331408506',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:331408506')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Januário Monteiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9786328)) <= 0.00070
               AND ABS(v.longitude - (-8.5925129)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305359289 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo nº2', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9777674, -8.5669488, 'OPENSTREETMAP', 'OSM:WAY:305359289',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305359289')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo nº2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9777674)) <= 0.00070
               AND ABS(v.longitude - (-8.5669488)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:957954088 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Padre Joaquim Sousa Lamas', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9542478, -8.5917665, 'OPENSTREETMAP', 'OSM:WAY:957954088',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:957954088')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Padre Joaquim Sousa Lamas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9542478)) <= 0.00070
               AND ABS(v.longitude - (-8.5917665)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8508397454 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de ténis e padel do Clube Académico da Feira', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ténis;Padel', 'Campo desportivo', NULL,
       40.9303827, -8.5568832, 'OPENSTREETMAP', 'OSM:NODE:8508397454',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8508397454')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campos de ténis e padel do Clube Académico da Feira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9303827)) <= 0.00070
               AND ABS(v.longitude - (-8.5568832)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:449274942 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Canedo Futebol Clube', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0097935, -8.4431633, 'OPENSTREETMAP', 'OSM:WAY:449274942',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:449274942')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Canedo Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0097935)) <= 0.00070
               AND ABS(v.longitude - (-8.4431633)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:903733436 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Académico da Feira', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.9287997, -8.5391320, 'OPENSTREETMAP', 'OSM:WAY:903733436',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:903733436')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Académico da Feira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9287997)) <= 0.00070
               AND ABS(v.longitude - (-8.5391320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:640154907 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo Feirense', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9268338, -8.5458056, 'OPENSTREETMAP', 'OSM:WAY:640154907',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:640154907')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo Feirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9268338)) <= 0.00070
               AND ABS(v.longitude - (-8.5458056)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:700433960 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo do Ervedal', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.9544860, -8.5549490, 'OPENSTREETMAP', 'OSM:WAY:700433960',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:700433960')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo do Ervedal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9544860)) <= 0.00070
               AND ABS(v.longitude - (-8.5549490)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1320084619 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Bolhão', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.9967075, -8.5329816, 'OPENSTREETMAP', 'OSM:WAY:1320084619',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1320084619')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Bolhão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9967075)) <= 0.00070
               AND ABS(v.longitude - (-8.5329816)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:870743919 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Bolhão - Fiães Sport Clube', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9965017, -8.5328108, 'OPENSTREETMAP', 'OSM:WAY:870743919',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:870743919')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Bolhão - Fiães Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9965017)) <= 0.00070
               AND ABS(v.longitude - (-8.5328108)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:870819809 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Lusitânia de Lourosa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9879655, -8.5437023, 'OPENSTREETMAP', 'OSM:WAY:870819809',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:870819809')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Lusitânia de Lourosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9879655)) <= 0.00070
               AND ABS(v.longitude - (-8.5437023)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:700433961 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Sporting Clube de São João de Ver', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.9584607, -8.5566101, 'OPENSTREETMAP', 'OSM:WAY:700433961',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:700433961')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Sporting Clube de São João de Ver'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9584607)) <= 0.00070
               AND ABS(v.longitude - (-8.5566101)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12381971223 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9678335, -8.5375581, 'OPENSTREETMAP', 'OSM:NODE:12381971223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12381971223')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9678335)) <= 0.00070
               AND ABS(v.longitude - (-8.5375581)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7021675700 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginasius', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9735897, -8.5824955, 'OPENSTREETMAP', 'OSM:NODE:7021675700',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7021675700')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginasius'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9735897)) <= 0.00070
               AND ABS(v.longitude - (-8.5824955)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:356678633 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grande Campo de jogos de Argoncilhe', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0212242, -8.5401354, 'OPENSTREETMAP', 'OSM:WAY:356678633',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:356678633')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grande Campo de jogos de Argoncilhe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0212242)) <= 0.00070
               AND ABS(v.longitude - (-8.5401354)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12114362923 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Instituto Kikenkô', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9841075, -8.5382718, 'OPENSTREETMAP', 'OSM:NODE:12114362923',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12114362923')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Instituto Kikenkô'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9841075)) <= 0.00070
               AND ABS(v.longitude - (-8.5382718)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13608393812 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Just Club', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Padel', 'Campo desportivo', NULL,
       40.9408042, -8.5407224, 'OPENSTREETMAP', 'OSM:NODE:13608393812',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13608393812')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Just Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9408042)) <= 0.00070
               AND ABS(v.longitude - (-8.5407224)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1096839905 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Largo da Feira dos Dez', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9803311, -8.5342892, 'OPENSTREETMAP', 'OSM:WAY:1096839905',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1096839905')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Largo da Feira dos Dez'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9803311)) <= 0.00070
               AND ABS(v.longitude - (-8.5342892)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11352612143 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lim Up', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9292925, -8.5550050, 'OPENSTREETMAP', 'OSM:NODE:11352612143',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11352612143')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lim Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9292925)) <= 0.00070
               AND ABS(v.longitude - (-8.5550050)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:538337859 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lusitânia de Lourosa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9786056, -8.5408585, 'OPENSTREETMAP', 'OSM:WAY:538337859',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:538337859')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lusitânia de Lourosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9786056)) <= 0.00070
               AND ABS(v.longitude - (-8.5408585)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:388964989 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Manuel de Oliveira Pé D´Arca', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9531877, -8.4841534, 'OPENSTREETMAP', 'OSM:WAY:388964989',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:388964989')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Manuel de Oliveira Pé D´Arca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9531877)) <= 0.00070
               AND ABS(v.longitude - (-8.4841534)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305200309 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mini-campo do Parque da Cidade', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Basquetebol;Futsal', 'Campo desportivo', NULL,
       40.9775296, -8.5398758, 'OPENSTREETMAP', 'OSM:WAY:305200309',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305200309')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mini-campo do Parque da Cidade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9775296)) <= 0.00070
               AND ABS(v.longitude - (-8.5398758)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11225734398 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Moove', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9260941, -8.5404062, 'OPENSTREETMAP', 'OSM:NODE:11225734398',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11225734398')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Moove'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9260941)) <= 0.00070
               AND ABS(v.longitude - (-8.5404062)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11349673236 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Musa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9278199, -8.5576678, 'OPENSTREETMAP', 'OSM:NODE:11349673236',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11349673236')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Musa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9278199)) <= 0.00070
               AND ABS(v.longitude - (-8.5576678)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:921076713 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Jogos Santo André', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9004383, -8.5255296, 'OPENSTREETMAP', 'OSM:WAY:921076713',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:921076713')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Jogos Santo André'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9004383)) <= 0.00070
               AND ABS(v.longitude - (-8.5255296)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305208083 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno Campo de Jogos da Habitação Social de Fiães', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ténis;Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9756584, -8.5251231, 'OPENSTREETMAP', 'OSM:WAY:305208083',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305208083')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno Campo de Jogos da Habitação Social de Fiães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9756584)) <= 0.00070
               AND ABS(v.longitude - (-8.5251231)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:921068828 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno Campo de Jogos de Cai Água', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.8985588, -8.5181317, 'OPENSTREETMAP', 'OSM:WAY:921068828',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:921068828')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno Campo de Jogos de Cai Água'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8985588)) <= 0.00070
               AND ABS(v.longitude - (-8.5181317)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:104350686 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno campo de jogos de Louredo', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9809523, -8.4576516, 'OPENSTREETMAP', 'OSM:WAY:104350686',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:104350686')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno campo de jogos de Louredo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9809523)) <= 0.00070
               AND ABS(v.longitude - (-8.4576516)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:959886028 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno Campo de Jogos de Mosteirô', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.8945424, -8.5285102, 'OPENSTREETMAP', 'OSM:WAY:959886028',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:959886028')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno Campo de Jogos de Mosteirô'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8945424)) <= 0.00070
               AND ABS(v.longitude - (-8.5285102)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:957384891 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno Campo de Jogos de Ribeiro', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9119961, -8.5268017, 'OPENSTREETMAP', 'OSM:WAY:957384891',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:957384891')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno Campo de Jogos de Ribeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9119961)) <= 0.00070
               AND ABS(v.longitude - (-8.5268017)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:184759889 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno Campo de Jogos de Travanca', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol;Ténis', 'Campo desportivo', NULL,
       40.9054679, -8.5700280, 'OPENSTREETMAP', 'OSM:WAY:184759889',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:184759889')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno Campo de Jogos de Travanca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9054679)) <= 0.00070
               AND ABS(v.longitude - (-8.5700280)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:307559772 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno Campo de Jogos do Centro Social e Paroquial de Espargo', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ténis;Futsal', 'Campo desportivo', NULL,
       40.9229692, -8.5753655, 'OPENSTREETMAP', 'OSM:WAY:307559772',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:307559772')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno Campo de Jogos do Centro Social e Paroquial de Espargo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9229692)) <= 0.00070
               AND ABS(v.longitude - (-8.5753655)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182640113 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pequeno Campo Desportivo de Fornos', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9111849, -8.5205727, 'OPENSTREETMAP', 'OSM:WAY:182640113',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182640113')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pequeno Campo Desportivo de Fornos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9111849)) <= 0.00070
               AND ABS(v.longitude - (-8.5205727)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12456351619 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pilares Studio Feira', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9223835, -8.5605989, 'OPENSTREETMAP', 'OSM:NODE:12456351619',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12456351619')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pilares Studio Feira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9223835)) <= 0.00070
               AND ABS(v.longitude - (-8.5605989)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:700433994 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de São João de Ver', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9538139, -8.5497824, 'OPENSTREETMAP', 'OSM:WAY:700433994',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:700433994')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de São João de Ver'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9538139)) <= 0.00070
               AND ABS(v.longitude - (-8.5497824)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1368679195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Muncipal - Canedo', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       41.0120260, -8.4666608, 'OPENSTREETMAP', 'OSM:WAY:1368679195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1368679195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Muncipal - Canedo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0120260)) <= 0.00070
               AND ABS(v.longitude - (-8.4666608)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305203709 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Fiães', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9865510, -8.5190732, 'OPENSTREETMAP', 'OSM:WAY:305203709',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305203709')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Fiães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9865510)) <= 0.00070
               AND ABS(v.longitude - (-8.5190732)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:435877073 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Santa Maria da Feira', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.9230373, -8.5457098, 'OPENSTREETMAP', 'OSM:WAY:435877073',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:435877073')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Santa Maria da Feira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9230373)) <= 0.00070
               AND ABS(v.longitude - (-8.5457098)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1096840278 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Lourosa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9842087, -8.5451503, 'OPENSTREETMAP', 'OSM:WAY:1096840278',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1096840278')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Lourosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9842087)) <= 0.00070
               AND ABS(v.longitude - (-8.5451503)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1004762423 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do Clube de Futebol União de Lamas', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.9795326, -8.5684958, 'OPENSTREETMAP', 'OSM:WAY:1004762423',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1004762423')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do Clube de Futebol União de Lamas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9795326)) <= 0.00070
               AND ABS(v.longitude - (-8.5684958)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:197982793 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Auditório', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Ténis', 'Campo desportivo', NULL,
       40.9835743, -8.5782537, 'OPENSTREETMAP', 'OSM:WAY:197982793',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:197982793')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Auditório'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9835743)) <= 0.00070
               AND ABS(v.longitude - (-8.5782537)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1214654336 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Bairro Nossa Senhora de Fátima', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9995462, -8.5485448, 'OPENSTREETMAP', 'OSM:WAY:1214654336',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1214654336')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Bairro Nossa Senhora de Fátima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9995462)) <= 0.00070
               AND ABS(v.longitude - (-8.5485448)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:958265200 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Boa Nova', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       41.0100233, -8.5264534, 'OPENSTREETMAP', 'OSM:WAY:958265200',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:958265200')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Boa Nova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0100233)) <= 0.00070
               AND ABS(v.longitude - (-8.5264534)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:355833040 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Ribeira da Venda', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       41.0212700, -8.5542733, 'OPENSTREETMAP', 'OSM:WAY:355833040',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:355833040')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Ribeira da Venda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0212700)) <= 0.00070
               AND ABS(v.longitude - (-8.5542733)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:700434044 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo das Piscinas', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9536363, -8.5493628, 'OPENSTREETMAP', 'OSM:WAY:700434044',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:700434044')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo das Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9536363)) <= 0.00070
               AND ABS(v.longitude - (-8.5493628)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106734062 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Travessa da Escola', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9433844, -8.4549998, 'OPENSTREETMAP', 'OSM:WAY:106734062',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106734062')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Travessa da Escola'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9433844)) <= 0.00070
               AND ABS(v.longitude - (-8.4549998)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:959553682 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Arcozelo', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Basquetebol;Ténis;Futsal', 'Campo desportivo', NULL,
       40.9585946, -8.5070117, 'OPENSTREETMAP', 'OSM:WAY:959553682',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:959553682')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Arcozelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9585946)) <= 0.00070
               AND ABS(v.longitude - (-8.5070117)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:490665979 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Guisande', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9746301, -8.4806107, 'OPENSTREETMAP', 'OSM:WAY:490665979',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:490665979')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Guisande'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9746301)) <= 0.00070
               AND ABS(v.longitude - (-8.4806107)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:853230638 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Barreiro', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9402402, -8.4861907, 'OPENSTREETMAP', 'OSM:WAY:853230638',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:853230638')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Barreiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9402402)) <= 0.00070
               AND ABS(v.longitude - (-8.4861907)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:959793147 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Candal', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9970830, -8.5028042, 'OPENSTREETMAP', 'OSM:WAY:959793147',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:959793147')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Candal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9970830)) <= 0.00070
               AND ABS(v.longitude - (-8.5028042)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:957388594 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Seixal', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9149349, -8.4824876, 'OPENSTREETMAP', 'OSM:WAY:957388594',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:957388594')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Seixal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9149349)) <= 0.00070
               AND ABS(v.longitude - (-8.4824876)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1097021123 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Doutor Feiteira Maia 1', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Basquetebol;Ténis', 'Campo desportivo', NULL,
       40.9924502, -8.5705795, 'OPENSTREETMAP', 'OSM:WAY:1097021123',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1097021123')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Doutor Feiteira Maia 1'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9924502)) <= 0.00070
               AND ABS(v.longitude - (-8.5705795)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1097021121 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Doutor Feiteira Maia 2', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9925392, -8.5703916, 'OPENSTREETMAP', 'OSM:WAY:1097021121',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1097021121')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Doutor Feiteira Maia 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9925392)) <= 0.00070
               AND ABS(v.longitude - (-8.5703916)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14070540267 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Monte das Pedreiras', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9869778, -8.5184161, 'OPENSTREETMAP', 'OSM:NODE:14070540267',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14070540267')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Monte das Pedreiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9869778)) <= 0.00070
               AND ABS(v.longitude - (-8.5184161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:958003678 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Parque da Nossa Senhora da Saúde', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Ténis', 'Campo desportivo', NULL,
       40.9876593, -8.5943144, 'OPENSTREETMAP', 'OSM:WAY:958003678',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:958003678')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Parque da Nossa Senhora da Saúde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9876593)) <= 0.00070
               AND ABS(v.longitude - (-8.5943144)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305211962 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Quinta do Areeiro', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9649274, -8.5188102, 'OPENSTREETMAP', 'OSM:WAY:305211962',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305211962')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Quinta do Areeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9649274)) <= 0.00070
               AND ABS(v.longitude - (-8.5188102)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305200128 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Suil Park', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.9678972, -8.5355779, 'OPENSTREETMAP', 'OSM:WAY:305200128',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305200128')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Suil Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9678972)) <= 0.00070
               AND ABS(v.longitude - (-8.5355779)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:53563636 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Travessa São Paulo da Cruz', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9251229, -8.5627845, 'OPENSTREETMAP', 'OSM:WAY:53563636',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:53563636')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Travessa São Paulo da Cruz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9251229)) <= 0.00070
               AND ABS(v.longitude - (-8.5627845)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1227029086 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Urbanização do Engenho Velho', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Ténis', 'Campo desportivo', NULL,
       40.9819184, -8.6086249, 'OPENSTREETMAP', 'OSM:WAY:1227029086',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1227029086')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Urbanização do Engenho Velho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9819184)) <= 0.00070
               AND ABS(v.longitude - (-8.6086249)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:904784105 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Vila Boa', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futsal;Basquetebol', 'Campo desportivo', NULL,
       40.9154628, -8.5658000, 'OPENSTREETMAP', 'OSM:WAY:904784105',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:904784105')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Vila Boa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9154628)) <= 0.00070
               AND ABS(v.longitude - (-8.5658000)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12934602829 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Prime', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9517757, -8.5179693, 'OPENSTREETMAP', 'OSM:NODE:12934602829',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12934602829')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Prime'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9517757)) <= 0.00070
               AND ABS(v.longitude - (-8.5179693)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106873524 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Romariz Futebol Clube', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9462120, -8.4701883, 'OPENSTREETMAP', 'OSM:WAY:106873524',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106873524')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Romariz Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9462120)) <= 0.00070
               AND ABS(v.longitude - (-8.4701883)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10210415387 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Samkhia', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9239082, -8.5521341, 'OPENSTREETMAP', 'OSM:NODE:10210415387',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10210415387')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Samkhia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9239082)) <= 0.00070
               AND ABS(v.longitude - (-8.5521341)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:305359288 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União de Lamas', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9792401, -8.5676577, 'OPENSTREETMAP', 'OSM:WAY:305359288',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:305359288')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União de Lamas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9792401)) <= 0.00070
               AND ABS(v.longitude - (-8.5676577)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:904787161 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vila Nova', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9442061, -8.5296513, 'OPENSTREETMAP', 'OSM:WAY:904787161',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:904787161')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vila Nova'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9442061)) <= 0.00070
               AND ABS(v.longitude - (-8.5296513)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10884062588 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'War House Gym', 'Santa Maria da Feira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9795200, -8.4861674, 'OPENSTREETMAP', 'OSM:NODE:10884062588',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10884062588')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santa Maria da Feira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('War House Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9795200)) <= 0.00070
               AND ABS(v.longitude - (-8.4861674)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:735357597 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva Sanjoanense', 'São João da Madeira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8983561, -8.4991282, 'OPENSTREETMAP', 'OSM:WAY:735357597',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:735357597')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva Sanjoanense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8983561)) <= 0.00070
               AND ABS(v.longitude - (-8.4991282)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:870835979 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos das Travessas', 'São João da Madeira', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9109958, -8.4836872, 'OPENSTREETMAP', 'OSM:WAY:870835979',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:870835979')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos das Travessas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9109958)) <= 0.00070
               AND ABS(v.longitude - (-8.4836872)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:636388010 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Radical', 'São João da Madeira', 'Aveiro', NULL,
       'Skate', 'Campo desportivo', NULL,
       40.8959168, -8.4990386, 'OPENSTREETMAP', 'OSM:WAY:636388010',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:636388010')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Radical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8959168)) <= 0.00070
               AND ABS(v.longitude - (-8.4990386)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:878130630 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'physical', 'São João da Madeira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.9075397, -8.4980019, 'OPENSTREETMAP', 'OSM:WAY:878130630',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:878130630')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('physical'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9075397)) <= 0.00070
               AND ABS(v.longitude - (-8.4980019)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12908235106 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Scoop Authentic Pilates Studio', 'São João da Madeira', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.8986461, -8.4930969, 'OPENSTREETMAP', 'OSM:NODE:12908235106',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12908235106')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('São João da Madeira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Scoop Authentic Pilates Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8986461)) <= 0.00070
               AND ABS(v.longitude - (-8.4930969)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:167246188 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Clube Recreativo e Cultural de Talhadas', 'Sever do Vouga', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6603011, -8.3318813, 'OPENSTREETMAP', 'OSM:WAY:167246188',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:167246188')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Clube Recreativo e Cultural de Talhadas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6603011)) <= 0.00070
               AND ABS(v.longitude - (-8.3318813)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:926238295 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos Capitão Bernardo Barbosa de Quadros', 'Sever do Vouga', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7587952, -8.3376655, 'OPENSTREETMAP', 'OSM:WAY:926238295',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:926238295')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos Capitão Bernardo Barbosa de Quadros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7587952)) <= 0.00070
               AND ABS(v.longitude - (-8.3376655)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:397562431 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desporvo do Parque Urbano', 'Sever do Vouga', 'Aveiro', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       40.7335834, -8.3694160, 'OPENSTREETMAP', 'OSM:WAY:397562431',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:397562431')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desporvo do Parque Urbano'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7335834)) <= 0.00070
               AND ABS(v.longitude - (-8.3694160)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:242409673 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio da Portela', 'Sever do Vouga', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7113816, -8.3780844, 'OPENSTREETMAP', 'OSM:WAY:242409673',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:242409673')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio da Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7113816)) <= 0.00070
               AND ABS(v.longitude - (-8.3780844)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:241806408 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Sever do Vouga', 'Sever do Vouga', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.7375082, -8.3785927, 'OPENSTREETMAP', 'OSM:WAY:241806408',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:241806408')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Sever do Vouga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7375082)) <= 0.00070
               AND ABS(v.longitude - (-8.3785927)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12382821805 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fusion Gym', 'Sever do Vouga', 'Aveiro', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.7316897, -8.3707652, 'OPENSTREETMAP', 'OSM:NODE:12382821805',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12382821805')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fusion Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7316897)) <= 0.00070
               AND ABS(v.longitude - (-8.3707652)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1373788893 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GRCS Silva Escurense', 'Sever do Vouga', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7567269, -8.3923351, 'OPENSTREETMAP', 'OSM:WAY:1373788893',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1373788893')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GRCS Silva Escurense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7567269)) <= 0.00070
               AND ABS(v.longitude - (-8.3923351)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1308695860 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Sever do Vouga', 'Sever do Vouga', 'Aveiro', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.7258871, -8.3651666, 'OPENSTREETMAP', 'OSM:WAY:1308695860',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1308695860')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sever do Vouga'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Sever do Vouga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7258871)) <= 0.00070
               AND ABS(v.longitude - (-8.3651666)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:238480945 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Salgueiro', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5540438, -8.6293651, 'OPENSTREETMAP', 'OSM:WAY:238480945',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238480945')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Salgueiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5540438)) <= 0.00070
               AND ABS(v.longitude - (-8.6293651)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:238430829 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Sosa', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5436003, -8.6474491, 'OPENSTREETMAP', 'OSM:WAY:238430829',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238430829')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Sosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5436003)) <= 0.00070
               AND ABS(v.longitude - (-8.6474491)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:244933665 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Padre Batista', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4747774, -8.7030529, 'OPENSTREETMAP', 'OSM:WAY:244933665',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244933665')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Padre Batista'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4747774)) <= 0.00070
               AND ABS(v.longitude - (-8.7030529)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:243684729 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Pinheiro Manso', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5101858, -8.6930990, 'OPENSTREETMAP', 'OSM:WAY:243684729',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:243684729')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Pinheiro Manso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5101858)) <= 0.00070
               AND ABS(v.longitude - (-8.6930990)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1464356069 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo futsal exterior', 'Vagos', 'Aveiro', NULL,
       'Futsal', 'Campo desportivo', NULL,
       40.5406008, -8.7552846, 'OPENSTREETMAP', 'OSM:WAY:1464356069',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1464356069')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo futsal exterior'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5406008)) <= 0.00070
               AND ABS(v.longitude - (-8.7552846)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1003547994 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo José Maria Neto', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4783130, -8.6654429, 'OPENSTREETMAP', 'OSM:WAY:1003547994',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1003547994')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo José Maria Neto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4783130)) <= 0.00070
               AND ABS(v.longitude - (-8.6654429)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:245419990 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CRAC - Centro Recreativo e Acção Cultural', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4595738, -8.6688442, 'OPENSTREETMAP', 'OSM:WAY:245419990',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:245419990')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CRAC - Centro Recreativo e Acção Cultural'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4595738)) <= 0.00070
               AND ABS(v.longitude - (-8.6688442)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1004037324 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Futebol Clube Vaguense', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5446520, -8.6860252, 'OPENSTREETMAP', 'OSM:WAY:1004037324',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1004037324')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Futebol Clube Vaguense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5446520)) <= 0.00070
               AND ABS(v.longitude - (-8.6860252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:131355185 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Vagos', 'Vagos', 'Aveiro', NULL,
       'Natação', 'Piscina', NULL,
       40.5470663, -8.6832638, 'OPENSTREETMAP', 'OSM:WAY:131355185',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:131355185')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Vagos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5470663)) <= 0.00070
               AND ABS(v.longitude - (-8.6832638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1464356068 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VA Boa Hora', 'Vagos', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5404203, -8.7548150, 'OPENSTREETMAP', 'OSM:WAY:1464356068',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1464356068')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vagos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VA Boa Hora'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5404203)) <= 0.00070
               AND ABS(v.longitude - (-8.7548150)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105608368 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ACR', 'Vale de Cambra', 'Aveiro', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       40.8446550, -8.3941130, 'OPENSTREETMAP', 'OSM:WAY:105608368',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105608368')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ACR'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8446550)) <= 0.00070
               AND ABS(v.longitude - (-8.3941130)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:862174963 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Assoc. Desportiva de Sandiães', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8636618, -8.3411663, 'OPENSTREETMAP', 'OSM:WAY:862174963',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:862174963')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Assoc. Desportiva de Sandiães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8636618)) <= 0.00070
               AND ABS(v.longitude - (-8.3411663)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105611638 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva Valecambrense', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.8384824, -8.3964911, 'OPENSTREETMAP', 'OSM:WAY:105611638',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105611638')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva Valecambrense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8384824)) <= 0.00070
               AND ABS(v.longitude - (-8.3964911)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221190553 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Basquetebol', 'Vale de Cambra', 'Aveiro', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       40.8541179, -8.3944445, 'OPENSTREETMAP', 'OSM:WAY:221190553',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221190553')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8541179)) <= 0.00070
               AND ABS(v.longitude - (-8.3944445)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222328559 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da U.R.P.', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.8622543, -8.3976011, 'OPENSTREETMAP', 'OSM:WAY:222328559',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222328559')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da U.R.P.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8622543)) <= 0.00070
               AND ABS(v.longitude - (-8.3976011)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105661422 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Fotebol de Vilar', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.8372413, -8.3147525, 'OPENSTREETMAP', 'OSM:WAY:105661422',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105661422')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Fotebol de Vilar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8372413)) <= 0.00070
               AND ABS(v.longitude - (-8.3147525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221578587 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Felgueira', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.8367446, -8.3004736, 'OPENSTREETMAP', 'OSM:WAY:221578587',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221578587')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Felgueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8367446)) <= 0.00070
               AND ABS(v.longitude - (-8.3004736)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:221489072 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Raposeira', 'Vale de Cambra', 'Aveiro', NULL,
       'Futebol', 'Estádio', NULL,
       40.8634033, -8.3742047, 'OPENSTREETMAP', 'OSM:WAY:221489072',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:221489072')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vale de Cambra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Raposeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8634033)) <= 0.00070
               AND ABS(v.longitude - (-8.3742047)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
