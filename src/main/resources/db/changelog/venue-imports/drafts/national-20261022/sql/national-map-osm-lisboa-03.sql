-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:8699303572 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Spartan', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7540237, -9.2786959, 'OPENSTREETMAP', 'OSM:NODE:8699303572',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8699303572')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Spartan'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7540237)) <= 0.00070
               AND ABS(v.longitude - (-9.2786959)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1182022309 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Zone', 'Sintra', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.8010239, -9.3385847, 'OPENSTREETMAP', 'OSM:WAY:1182022309',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1182022309')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Zone'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8010239)) <= 0.00070
               AND ABS(v.longitude - (-9.3385847)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10247927519 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7764713, -9.3368534, 'OPENSTREETMAP', 'OSM:NODE:10247927519',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10247927519')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7764713)) <= 0.00070
               AND ABS(v.longitude - (-9.3368534)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:791298519 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol de Malgas', 'Sobral de Monte Agraço', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9962837, -9.1866214, 'OPENSTREETMAP', 'OSM:WAY:791298519',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:791298519')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sobral de Monte Agraço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol de Malgas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9962837)) <= 0.00070
               AND ABS(v.longitude - (-9.1866214)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:784864638 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo e Recreativo de Pero Negro', 'Sobral de Monte Agraço', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9942531, -9.2023258, 'OPENSTREETMAP', 'OSM:WAY:784864638',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:784864638')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sobral de Monte Agraço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo e Recreativo de Pero Negro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9942531)) <= 0.00070
               AND ABS(v.longitude - (-9.2023258)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5964788838 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Monte Agraço Futebol Clube', 'Sobral de Monte Agraço', 'Lisboa', NULL,
       'Futebol;Voleibol', 'Complexo desportivo', NULL,
       39.0204759, -9.1556705, 'OPENSTREETMAP', 'OSM:NODE:5964788838',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5964788838')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sobral de Monte Agraço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Monte Agraço Futebol Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0204759)) <= 0.00070
               AND ABS(v.longitude - (-9.1556705)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:161786233 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo O Sobreiro', 'Sobral de Monte Agraço', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9635913, -9.1995447, 'OPENSTREETMAP', 'OSM:WAY:161786233',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:161786233')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sobral de Monte Agraço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo O Sobreiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9635913)) <= 0.00070
               AND ABS(v.longitude - (-9.1995447)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:941410318 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Sobral de Monte Agraço', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       39.0226805, -9.1537373, 'OPENSTREETMAP', 'OSM:WAY:941410318',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:941410318')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sobral de Monte Agraço'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0226805)) <= 0.00070
               AND ABS(v.longitude - (-9.1537373)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4918735455 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'A.C.D.R. Arneiros', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.0587848, -9.3223791, 'OPENSTREETMAP', 'OSM:NODE:4918735455',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4918735455')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('A.C.D.R. Arneiros'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0587848)) <= 0.00070
               AND ABS(v.longitude - (-9.3223791)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1155335767 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Flores', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1935257, -9.2337824, 'OPENSTREETMAP', 'OSM:WAY:1155335767',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1155335767')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Flores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1935257)) <= 0.00070
               AND ABS(v.longitude - (-9.2337824)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1051232637 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Matacães', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0872777, -9.2128736, 'OPENSTREETMAP', 'OSM:WAY:1051232637',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1051232637')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Matacães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0872777)) <= 0.00070
               AND ABS(v.longitude - (-9.2128736)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:280768878 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos moinhos', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0974070, -9.3186494, 'OPENSTREETMAP', 'OSM:WAY:280768878',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:280768878')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0974070)) <= 0.00070
               AND ABS(v.longitude - (-9.3186494)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1355503063 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol da Pedra', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.0597336, -9.3413517, 'OPENSTREETMAP', 'OSM:WAY:1355503063',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1355503063')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol da Pedra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0597336)) <= 0.00070
               AND ABS(v.longitude - (-9.3413517)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1348474656 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis e Padel de Torres Vedras', 'Torres Vedras', 'Lisboa', NULL,
       'Ténis;Padel', 'Complexo desportivo', NULL,
       39.0829750, -9.2630783, 'OPENSTREETMAP', 'OSM:WAY:1348474656',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1348474656')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis e Padel de Torres Vedras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0829750)) <= 0.00070
               AND ABS(v.longitude - (-9.2630783)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1352502705 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal Manuel Marques', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.0969019, -9.2571478, 'OPENSTREETMAP', 'OSM:WAY:1352502705',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1352502705')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Municipal Manuel Marques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0969019)) <= 0.00070
               AND ABS(v.longitude - (-9.2571478)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150032794 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Manuel Marques', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.0949903, -9.2569016, 'OPENSTREETMAP', 'OSM:WAY:150032794',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150032794')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Manuel Marques'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0949903)) <= 0.00070
               AND ABS(v.longitude - (-9.2569016)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:359166075 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Sobreirense', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1539418, -9.3090891, 'OPENSTREETMAP', 'OSM:WAY:359166075',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:359166075')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Sobreirense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1539418)) <= 0.00070
               AND ABS(v.longitude - (-9.3090891)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1359085593 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Higia', 'Torres Vedras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.1113049, -9.2560731, 'OPENSTREETMAP', 'OSM:WAY:1359085593',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1359085593')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Higia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1113049)) <= 0.00070
               AND ABS(v.longitude - (-9.2560731)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4918780238 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'MaisFitness', 'Torres Vedras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0769670, -9.2557840, 'OPENSTREETMAP', 'OSM:NODE:4918780238',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4918780238')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('MaisFitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0769670)) <= 0.00070
               AND ABS(v.longitude - (-9.2557840)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153250164 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Desportivo Maximino Franco dos Santos', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.1541559, -9.2893956, 'OPENSTREETMAP', 'OSM:WAY:153250164',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153250164')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Desportivo Maximino Franco dos Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1541559)) <= 0.00070
               AND ABS(v.longitude - (-9.2893956)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:26271397 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Municipal', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1312803, -9.3810443, 'OPENSTREETMAP', 'OSM:WAY:26271397',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:26271397')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1312803)) <= 0.00070
               AND ABS(v.longitude - (-9.3810443)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1171003679 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de A-dos-Cunhados', 'Torres Vedras', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       39.1509286, -9.2994622, 'OPENSTREETMAP', 'OSM:WAY:1171003679',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1171003679')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de A-dos-Cunhados'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1509286)) <= 0.00070
               AND ABS(v.longitude - (-9.2994622)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:782701541 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Clube União Torreense', 'Torres Vedras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0949627, -9.2567336, 'OPENSTREETMAP', 'OSM:WAY:782701541',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:782701541')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Clube União Torreense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0949627)) <= 0.00070
               AND ABS(v.longitude - (-9.2567336)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1429988449 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo 2', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.9656320, -8.9858556, 'OPENSTREETMAP', 'OSM:WAY:1429988449',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1429988449')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9656320)) <= 0.00070
               AND ABS(v.longitude - (-8.9858556)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:114731369 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo nº 2', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8968690, -9.0361391, 'OPENSTREETMAP', 'OSM:WAY:114731369',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:114731369')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo nº 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8968690)) <= 0.00070
               AND ABS(v.longitude - (-9.0361391)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1148238513 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Formação do FC Alverca', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Futebol;Futsal;Padel', 'Complexo desportivo', NULL,
       38.8931195, -9.0332979, 'OPENSTREETMAP', 'OSM:WAY:1148238513',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1148238513')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Formação do FC Alverca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8931195)) <= 0.00070
               AND ABS(v.longitude - (-9.0332979)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:312557390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo da UDV', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9456499, -8.9945805, 'OPENSTREETMAP', 'OSM:WAY:312557390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:312557390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo da UDV'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9456499)) <= 0.00070
               AND ABS(v.longitude - (-8.9945805)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4314504490 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Quinta Da Piedade', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8600489, -9.0708977, 'OPENSTREETMAP', 'OSM:NODE:4314504490',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4314504490')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Quinta Da Piedade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8600489)) <= 0.00070
               AND ABS(v.longitude - (-9.0708977)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1465647523 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'JC Padel Center', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.9862459, -8.9725732, 'OPENSTREETMAP', 'OSM:WAY:1465647523',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1465647523')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('JC Padel Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9862459)) <= 0.00070
               AND ABS(v.longitude - (-8.9725732)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11071446768 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lemonfit', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8625353, -9.0793358, 'OPENSTREETMAP', 'OSM:NODE:11071446768',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11071446768')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lemonfit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8625353)) <= 0.00070
               AND ABS(v.longitude - (-9.0793358)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:508455493 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Alhandra', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Natação', 'Estádio', NULL,
       38.9317510, -9.0054555, 'OPENSTREETMAP', 'OSM:WAY:508455493',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:508455493')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Alhandra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9317510)) <= 0.00070
               AND ABS(v.longitude - (-9.0054555)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:23819424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vila Franca de Xira', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.9650730, -8.9862203, 'OPENSTREETMAP', 'OSM:WAY:23819424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:23819424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vila Franca de Xira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9650730)) <= 0.00070
               AND ABS(v.longitude - (-8.9862203)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:114788278 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8965984, -9.0404932, 'OPENSTREETMAP', 'OSM:WAY:114788278',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:114788278')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8965984)) <= 0.00070
               AND ABS(v.longitude - (-9.0404932)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:145062382 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8913023, -9.0398323, 'OPENSTREETMAP', 'OSM:WAY:145062382',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145062382')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8913023)) <= 0.00070
               AND ABS(v.longitude - (-9.0398323)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:622449190 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SkatePark Quitninha', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.8609159, -9.0684994, 'OPENSTREETMAP', 'OSM:WAY:622449190',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:622449190')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SkatePark Quitninha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8609159)) <= 0.00070
               AND ABS(v.longitude - (-9.0684994)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11876328019 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8897242, -9.0410949, 'OPENSTREETMAP', 'OSM:NODE:11876328019',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11876328019')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8897242)) <= 0.00070
               AND ABS(v.longitude - (-9.0410949)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
