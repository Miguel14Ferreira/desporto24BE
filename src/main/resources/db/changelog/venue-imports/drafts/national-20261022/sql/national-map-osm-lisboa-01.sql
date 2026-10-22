-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:139732348 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Alenquer Complexo Municipal Victor Santos', 'Alenquer', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.0523353, -8.9987480, 'OPENSTREETMAP', 'OSM:WAY:139732348',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:139732348')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alenquer'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Alenquer Complexo Municipal Victor Santos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0523353)) <= 0.00070
               AND ABS(v.longitude - (-8.9987480)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:945635538 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Natação da Amadora - Piscina da Venteira', 'Amadora', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7566804, -9.2430726, 'OPENSTREETMAP', 'OSM:WAY:945635538',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:945635538')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Natação da Amadora - Piscina da Venteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7566804)) <= 0.00070
               AND ABS(v.longitude - (-9.2430726)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11491247621 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhao António Ferreira', 'Amadora', 'Lisboa', NULL,
       'Futsal;Bilhar', 'Complexo desportivo', NULL,
       38.7567170, -9.2114116, 'OPENSTREETMAP', 'OSM:NODE:11491247621',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11491247621')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhao António Ferreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7567170)) <= 0.00070
               AND ABS(v.longitude - (-9.2114116)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:872027247 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo 1º de Maio', 'Amadora', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7652408, -9.2115562, 'OPENSTREETMAP', 'OSM:WAY:872027247',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:872027247')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo 1º de Maio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7652408)) <= 0.00070
               AND ABS(v.longitude - (-9.2115562)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8703115272 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Light', 'Amadora', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7613260, -9.2429243, 'OPENSTREETMAP', 'OSM:NODE:8703115272',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8703115272')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Light'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7613260)) <= 0.00070
               AND ABS(v.longitude - (-9.2429243)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4242965537 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Time To Fitness 24', 'Amadora', 'Lisboa', NULL,
       'Ginástica', 'Ginásio', NULL,
       38.7663504, -9.2313928, 'OPENSTREETMAP', 'OSM:NODE:4242965537',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4242965537')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Time To Fitness 24'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7663504)) <= 0.00070
               AND ABS(v.longitude - (-9.2313928)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4555294493 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo de Azambuja', 'Azambuja', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0748459, -8.8733322, 'OPENSTREETMAP', 'OSM:NODE:4555294493',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4555294493')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Azambuja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo de Azambuja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0748459)) <= 0.00070
               AND ABS(v.longitude - (-8.8733322)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5442071067 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ADY Associacão de Yoga do Estoril', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7067457, -9.4085706, 'OPENSTREETMAP', 'OSM:NODE:5442071067',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5442071067')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ADY Associacão de Yoga do Estoril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7067457)) <= 0.00070
               AND ABS(v.longitude - (-9.4085706)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5612241063 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Áshrama Estoril', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7043438, -9.3958074, 'OPENSTREETMAP', 'OSM:NODE:5612241063',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5612241063')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Áshrama Estoril'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7043438)) <= 0.00070
               AND ABS(v.longitude - (-9.3958074)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5614311318 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Feline Studio', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6996108, -9.4273186, 'OPENSTREETMAP', 'OSM:NODE:5614311318',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5614311318')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Feline Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6996108)) <= 0.00070
               AND ABS(v.longitude - (-9.4273186)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5607645954 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fit and Company training center', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6907240, -9.3359115, 'OPENSTREETMAP', 'OSM:NODE:5607645954',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5607645954')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fit and Company training center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6907240)) <= 0.00070
               AND ABS(v.longitude - (-9.3359115)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5616009392 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'house of zen', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7053797, -9.3895832, 'OPENSTREETMAP', 'OSM:NODE:5616009392',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5616009392')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('house of zen'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7053797)) <= 0.00070
               AND ABS(v.longitude - (-9.3895832)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12604751835 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Line Up', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7241828, -9.3313502, 'OPENSTREETMAP', 'OSM:NODE:12604751835',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12604751835')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Line Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7241828)) <= 0.00070
               AND ABS(v.longitude - (-9.3313502)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5615437670 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Obidas Place', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7217792, -9.3432741, 'OPENSTREETMAP', 'OSM:NODE:5615437670',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5615437670')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Obidas Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7217792)) <= 0.00070
               AND ABS(v.longitude - (-9.3432741)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5615910535 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Only B''Yoga', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7132358, -9.3302385, 'OPENSTREETMAP', 'OSM:NODE:5615910535',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5615910535')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Only B''Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7132358)) <= 0.00070
               AND ABS(v.longitude - (-9.3302385)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:564886998 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina D. Irene Nery da Silveira de Freitas Borja', 'Cascais', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.6887051, -9.3343772, 'OPENSTREETMAP', 'OSM:WAY:564886998',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:564886998')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina D. Irene Nery da Silveira de Freitas Borja'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6887051)) <= 0.00070
               AND ABS(v.longitude - (-9.3343772)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11204855163 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Light', 'Cascais', 'Lisboa', NULL,
       'Ginásio;Ciclismo', 'Ginásio', NULL,
       38.7140712, -9.3853496, 'OPENSTREETMAP', 'OSM:NODE:11204855163',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11204855163')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Light'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7140712)) <= 0.00070
               AND ABS(v.longitude - (-9.3853496)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5614343001 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga Family', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7009962, -9.3406569, 'OPENSTREETMAP', 'OSM:NODE:5614343001',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5614343001')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga Family'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7009962)) <= 0.00070
               AND ABS(v.longitude - (-9.3406569)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12316401072 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '110% Studio;', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7314445, -9.1386795, 'OPENSTREETMAP', 'OSM:NODE:12316401072',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12316401072')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('110% Studio;'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7314445)) <= 0.00070
               AND ABS(v.longitude - (-9.1386795)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13542082606 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '5inco Avenidas Novas', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7344648, -9.1520056, 'OPENSTREETMAP', 'OSM:NODE:13542082606',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13542082606')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('5inco Avenidas Novas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7344648)) <= 0.00070
               AND ABS(v.longitude - (-9.1520056)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11866440786 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ABHYASA YOGA LISBON', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7098470, -9.1410592, 'OPENSTREETMAP', 'OSM:NODE:11866440786',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11866440786')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ABHYASA YOGA LISBON'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7098470)) <= 0.00070
               AND ABS(v.longitude - (-9.1410592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6126022594 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia Yoga Be One', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7198553, -9.1529101, 'OPENSTREETMAP', 'OSM:NODE:6126022594',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6126022594')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia Yoga Be One'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7198553)) <= 0.00070
               AND ABS(v.longitude - (-9.1529101)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8557062441 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Adhara Yoga Shala', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7308422, -9.1305503, 'OPENSTREETMAP', 'OSM:NODE:8557062441',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8557062441')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Adhara Yoga Shala'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7308422)) <= 0.00070
               AND ABS(v.longitude - (-9.1305503)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9729945390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Amplify', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7229641, -9.1507786, 'OPENSTREETMAP', 'OSM:NODE:9729945390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9729945390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Amplify'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7229641)) <= 0.00070
               AND ABS(v.longitude - (-9.1507786)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12908506459 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'amplify Studio', 'Lisboa', 'Lisboa', NULL,
       'Ginásio;Ciclismo', 'Ginásio', NULL,
       38.7062440, -9.1568146, 'OPENSTREETMAP', 'OSM:NODE:12908506459',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12908506459')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('amplify Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7062440)) <= 0.00070
               AND ABS(v.longitude - (-9.1568146)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4777914139 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'António Pereira', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7290576, -9.1377263, 'OPENSTREETMAP', 'OSM:NODE:4777914139',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4777914139')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('António Pereira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7290576)) <= 0.00070
               AND ABS(v.longitude - (-9.1377263)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11866128930 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'AravindaYogaShala', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7108026, -9.1412563, 'OPENSTREETMAP', 'OSM:NODE:11866128930',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11866128930')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('AravindaYogaShala'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7108026)) <= 0.00070
               AND ABS(v.longitude - (-9.1412563)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13505550330 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Asha''s Place Lisbon', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7120904, -9.1441773, 'OPENSTREETMAP', 'OSM:NODE:13505550330',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13505550330')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Asha''s Place Lisbon'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7120904)) <= 0.00070
               AND ABS(v.longitude - (-9.1441773)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12314843900 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Atma Yoga Centro', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7607864, -9.1639694, 'OPENSTREETMAP', 'OSM:NODE:12314843900',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12314843900')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Atma Yoga Centro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7607864)) <= 0.00070
               AND ABS(v.longitude - (-9.1639694)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11881549284 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Blive Pilates Areeiro', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7432809, -9.1334516, 'OPENSTREETMAP', 'OSM:NODE:11881549284',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11881549284')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Blive Pilates Areeiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7432809)) <= 0.00070
               AND ABS(v.longitude - (-9.1334516)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11886704387 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Brooklyn Fitboxing Restelo', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7077917, -9.2095967, 'OPENSTREETMAP', 'OSM:NODE:11886704387',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11886704387')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Brooklyn Fitboxing Restelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7077917)) <= 0.00070
               AND ABS(v.longitude - (-9.2095967)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:506216160 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo das Salésias', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7011231, -9.1955851, 'OPENSTREETMAP', 'OSM:WAY:506216160',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:506216160')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo das Salésias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7011231)) <= 0.00070
               AND ABS(v.longitude - (-9.1955851)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1279989763 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de basquetebol Rua Alferes Barrilaro', 'Lisboa', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.7759509, -9.1130062, 'OPENSTREETMAP', 'OSM:WAY:1279989763',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1279989763')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de basquetebol Rua Alferes Barrilaro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7759509)) <= 0.00070
               AND ABS(v.longitude - (-9.1130062)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9712222869 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campus Fitness Studio', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7285601, -9.1629654, 'OPENSTREETMAP', 'OSM:NODE:9712222869',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9712222869')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campus Fitness Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7285601)) <= 0.00070
               AND ABS(v.longitude - (-9.1629654)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11849945842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Yoga Áshrama Olivais', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7678941, -9.1104513, 'OPENSTREETMAP', 'OSM:NODE:11849945842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11849945842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Yoga Áshrama Olivais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7678941)) <= 0.00070
               AND ABS(v.longitude - (-9.1104513)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8703001217 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Yoga Lumiar', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7764545, -9.1607370, 'OPENSTREETMAP', 'OSM:NODE:8703001217',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8703001217')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Yoga Lumiar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7764545)) <= 0.00070
               AND ABS(v.longitude - (-9.1607370)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:238175283 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Futebol Varejense', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7270735, -9.1181586, 'OPENSTREETMAP', 'OSM:WAY:238175283',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238175283')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Futebol Varejense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7270735)) <= 0.00070
               AND ABS(v.longitude - (-9.1181586)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102489808 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Desportivo Dos Olivais e Moscavide', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.7740008, -9.1025924, 'OPENSTREETMAP', 'OSM:WAY:102489808',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102489808')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Desportivo Dos Olivais e Moscavide'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7740008)) <= 0.00070
               AND ABS(v.longitude - (-9.1025924)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:558554923 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Recreativo Os Leões das Furnas', 'Lisboa', 'Lisboa', NULL,
       'Futsal', 'Complexo desportivo', NULL,
       38.7410598, -9.1726366, 'OPENSTREETMAP', 'OSM:WAY:558554923',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:558554923')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Recreativo Os Leões das Furnas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7410598)) <= 0.00070
               AND ABS(v.longitude - (-9.1726366)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:145266988 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo da Ajuda', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7147562, -9.1977336, 'OPENSTREETMAP', 'OSM:WAY:145266988',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145266988')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo da Ajuda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7147562)) <= 0.00070
               AND ABS(v.longitude - (-9.1977336)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12110487855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CPYoga', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7362915, -9.1512332, 'OPENSTREETMAP', 'OSM:NODE:12110487855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12110487855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CPYoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7362915)) <= 0.00070
               AND ABS(v.longitude - (-9.1512332)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12328445560 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CrossFit', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7401738, -9.1028969, 'OPENSTREETMAP', 'OSM:NODE:12328445560',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12328445560')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CrossFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7401738)) <= 0.00070
               AND ABS(v.longitude - (-9.1028969)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11841607370 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CrossFit Caravelas', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7088000, -9.1652927, 'OPENSTREETMAP', 'OSM:NODE:11841607370',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11841607370')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CrossFit Caravelas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7088000)) <= 0.00070
               AND ABS(v.longitude - (-9.1652927)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9740335053 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CYR - Centro de Yoga do Rato', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7204668, -9.1543264, 'OPENSTREETMAP', 'OSM:NODE:9740335053',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9740335053')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CYR - Centro de Yoga do Rato'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7204668)) <= 0.00070
               AND ABS(v.longitude - (-9.1543264)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11898720620 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'eBody', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7569896, -9.0969437, 'OPENSTREETMAP', 'OSM:NODE:11898720620',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11898720620')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('eBody'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7569896)) <= 0.00070
               AND ABS(v.longitude - (-9.0969437)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11882149048 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7651977, -9.0983583, 'OPENSTREETMAP', 'OSM:NODE:11882149048',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11882149048')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7651977)) <= 0.00070
               AND ABS(v.longitude - (-9.0983583)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13147500501 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Embody Lisboa', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7271290, -9.1436935, 'OPENSTREETMAP', 'OSM:NODE:13147500501',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13147500501')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Embody Lisboa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7271290)) <= 0.00070
               AND ABS(v.longitude - (-9.1436935)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4773880679 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Equilibrium Therapy Zen', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7277736, -9.1370091, 'OPENSTREETMAP', 'OSM:NODE:4773880679',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4773880679')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Equilibrium Therapy Zen'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7277736)) <= 0.00070
               AND ABS(v.longitude - (-9.1370091)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13230741433 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Escala25', 'Lisboa', 'Lisboa', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.7003518, -9.1788150, 'OPENSTREETMAP', 'OSM:NODE:13230741433',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13230741433')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Escala25'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7003518)) <= 0.00070
               AND ABS(v.longitude - (-9.1788150)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8685881362 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Espaço Cultural de Yoga', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7032389, -9.1815605, 'OPENSTREETMAP', 'OSM:NODE:8685881362',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8685881362')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Espaço Cultural de Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7032389)) <= 0.00070
               AND ABS(v.longitude - (-9.1815605)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:873053374 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Espassus Piscina', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7605298, -9.1865295, 'OPENSTREETMAP', 'OSM:WAY:873053374',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:873053374')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Espassus Piscina'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7605298)) <= 0.00070
               AND ABS(v.longitude - (-9.1865295)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3684740644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estúdio Premium', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7489941, -9.1878698, 'OPENSTREETMAP', 'OSM:NODE:3684740644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3684740644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estúdio Premium'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7489941)) <= 0.00070
               AND ABS(v.longitude - (-9.1878698)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12309114937 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Feel at Om', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7523480, -9.1972732, 'OPENSTREETMAP', 'OSM:NODE:12309114937',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12309114937')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Feel at Om'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7523480)) <= 0.00070
               AND ABS(v.longitude - (-9.1972732)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12323774408 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7435177, -9.1390568, 'OPENSTREETMAP', 'OSM:NODE:12323774408',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12323774408')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7435177)) <= 0.00070
               AND ABS(v.longitude - (-9.1390568)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5251791200 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7247265, -9.1463257, 'OPENSTREETMAP', 'OSM:NODE:5251791200',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5251791200')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7247265)) <= 0.00070
               AND ABS(v.longitude - (-9.1463257)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11880062371 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Up', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7313944, -9.1382947, 'OPENSTREETMAP', 'OSM:NODE:11880062371',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11880062371')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Up'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7313944)) <= 0.00070
               AND ABS(v.longitude - (-9.1382947)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6124623886 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gaia - Centro de Hatha Yoga', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7290398, -9.1436715, 'OPENSTREETMAP', 'OSM:NODE:6124623886',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6124623886')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gaia - Centro de Hatha Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7290398)) <= 0.00070
               AND ABS(v.longitude - (-9.1436715)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4368484840 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Get Fit', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7316889, -9.1375978, 'OPENSTREETMAP', 'OSM:NODE:4368484840',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4368484840')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Get Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7316889)) <= 0.00070
               AND ABS(v.longitude - (-9.1375978)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11849881907 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Comunitário Bairro Padre Cruz', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7696196, -9.1909367, 'OPENSTREETMAP', 'OSM:NODE:11849881907',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11849881907')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Comunitário Bairro Padre Cruz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7696196)) <= 0.00070
               AND ABS(v.longitude - (-9.1909367)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:378684505 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'GO fit Olivais', 'Lisboa', 'Lisboa', NULL,
       'Ginásio;Natação', 'Ginásio', NULL,
       38.7689543, -9.1124371, 'OPENSTREETMAP', 'OSM:WAY:378684505',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:378684505')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('GO fit Olivais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7689543)) <= 0.00070
               AND ABS(v.longitude - (-9.1124371)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4824115388 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Health and Fun', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7310190, -9.1339379, 'OPENSTREETMAP', 'OSM:NODE:4824115388',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4824115388')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Health and Fun'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7310190)) <= 0.00070
               AND ABS(v.longitude - (-9.1339379)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11893436552 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Hi Well', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7796253, -9.0949128, 'OPENSTREETMAP', 'OSM:NODE:11893436552',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11893436552')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Hi Well'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7796253)) <= 0.00070
               AND ABS(v.longitude - (-9.0949128)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4253791152 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7371331, -9.1471843, 'OPENSTREETMAP', 'OSM:NODE:4253791152',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4253791152')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7371331)) <= 0.00070
               AND ABS(v.longitude - (-9.1471843)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9729970820 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7228008, -9.1606691, 'OPENSTREETMAP', 'OSM:NODE:9729970820',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9729970820')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7228008)) <= 0.00070
               AND ABS(v.longitude - (-9.1606691)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4575923489 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place Parque das Nações', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7743603, -9.0976988, 'OPENSTREETMAP', 'OSM:NODE:4575923489',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4575923489')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place Parque das Nações'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7743603)) <= 0.00070
               AND ABS(v.longitude - (-9.0976988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11876814475 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Jacarandá', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7111261, -9.1657717, 'OPENSTREETMAP', 'OSM:NODE:11876814475',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11876814475')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Jacarandá'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7111261)) <= 0.00070
               AND ABS(v.longitude - (-9.1657717)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12320497468 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Kaizen Art', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7404030, -9.1028814, 'OPENSTREETMAP', 'OSM:NODE:12320497468',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12320497468')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Kaizen Art'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7404030)) <= 0.00070
               AND ABS(v.longitude - (-9.1028814)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6124616087 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lisboa Yoga Loft', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7193492, -9.1511630, 'OPENSTREETMAP', 'OSM:NODE:6124616087',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6124616087')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lisboa Yoga Loft'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7193492)) <= 0.00070
               AND ABS(v.longitude - (-9.1511630)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8382739612 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Little Yoga Space', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7118596, -9.1375320, 'OPENSTREETMAP', 'OSM:NODE:8382739612',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8382739612')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Little Yoga Space'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7118596)) <= 0.00070
               AND ABS(v.longitude - (-9.1375320)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11830299790 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Magic Form', 'Lisboa', 'Lisboa', NULL,
       'Ginásio;Ginástica', 'Ginásio', NULL,
       38.7570170, -9.1416533, 'OPENSTREETMAP', 'OSM:NODE:11830299790',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11830299790')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Magic Form'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7570170)) <= 0.00070
               AND ABS(v.longitude - (-9.1416533)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:238326610 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Maria Pia Sport Clube', 'Lisboa', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.7190641, -9.1302517, 'OPENSTREETMAP', 'OSM:WAY:238326610',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238326610')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Maria Pia Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7190641)) <= 0.00070
               AND ABS(v.longitude - (-9.1302517)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:682160320 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Mega Craque Clube', 'Lisboa', 'Lisboa', NULL,
       'Natação;Corrida;Ginásio', 'Ginásio', NULL,
       38.7651000, -9.1707812, 'OPENSTREETMAP', 'OSM:WAY:682160320',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:682160320')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Mega Craque Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7651000)) <= 0.00070
               AND ABS(v.longitude - (-9.1707812)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6124616286 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ministry of Yoga', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7154098, -9.1396425, 'OPENSTREETMAP', 'OSM:NODE:6124616286',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6124616286')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ministry of Yoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7154098)) <= 0.00070
               AND ABS(v.longitude - (-9.1396425)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9718896510 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'OFF LIMITS crossfit', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7199555, -9.1596855, 'OPENSTREETMAP', 'OSM:NODE:9718896510',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9718896510')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('OFF LIMITS crossfit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7199555)) <= 0.00070
               AND ABS(v.longitude - (-9.1596855)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6126026985 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Organic Flow Studio', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7089211, -9.1544831, 'OPENSTREETMAP', 'OSM:NODE:6126026985',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6126026985')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Organic Flow Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7089211)) <= 0.00070
               AND ABS(v.longitude - (-9.1544831)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:872814348 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Paço do Lumiar Clube de Golfe', 'Lisboa', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7807742, -9.1686052, 'OPENSTREETMAP', 'OSM:WAY:872814348',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:872814348')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Paço do Lumiar Clube de Golfe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7807742)) <= 0.00070
               AND ABS(v.longitude - (-9.1686052)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:228240264 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Arroios', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7209420, -9.1323798, 'OPENSTREETMAP', 'OSM:WAY:228240264',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:228240264')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Arroios'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7209420)) <= 0.00070
               AND ABS(v.longitude - (-9.1323798)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:377321921 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Campo de Ourique', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7180493, -9.1709080, 'OPENSTREETMAP', 'OSM:WAY:377321921',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:377321921')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Campo de Ourique'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7180493)) <= 0.00070
               AND ABS(v.longitude - (-9.1709080)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:596107999 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Santa Clara', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7856704, -9.1602160, 'OPENSTREETMAP', 'OSM:WAY:596107999',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:596107999')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Santa Clara'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7856704)) <= 0.00070
               AND ABS(v.longitude - (-9.1602160)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:244890522 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Alfama', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7142450, -9.1254190, 'OPENSTREETMAP', 'OSM:WAY:244890522',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244890522')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Alfama'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7142450)) <= 0.00070
               AND ABS(v.longitude - (-9.1254190)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:249011689 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Casal Vistoso', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7426798, -9.1290915, 'OPENSTREETMAP', 'OSM:WAY:249011689',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:249011689')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Casal Vistoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7426798)) <= 0.00070
               AND ABS(v.longitude - (-9.1290915)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:85021143 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Oriente', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7711721, -9.0994961, 'OPENSTREETMAP', 'OSM:WAY:85021143',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:85021143')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Oriente'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7711721)) <= 0.00070
               AND ABS(v.longitude - (-9.0994961)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:378474609 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Rego', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7421888, -9.1604412, 'OPENSTREETMAP', 'OSM:WAY:378474609',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:378474609')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Rego'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7421888)) <= 0.00070
               AND ABS(v.longitude - (-9.1604412)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:722556361 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Restelo', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7096002, -9.2121339, 'OPENSTREETMAP', 'OSM:WAY:722556361',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:722556361')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Restelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7096002)) <= 0.00070
               AND ABS(v.longitude - (-9.2121339)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:241528753 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal do Vale Fundão', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7511006, -9.1060018, 'OPENSTREETMAP', 'OSM:WAY:241528753',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:241528753')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal do Vale Fundão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7511006)) <= 0.00070
               AND ABS(v.longitude - (-9.1060018)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:105283575 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo Municipal Professor Moniz Pereira', 'Lisboa', 'Lisboa', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       38.7820318, -9.1538381, 'OPENSTREETMAP', 'OSM:WAY:105283575',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:105283575')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo Municipal Professor Moniz Pereira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7820318)) <= 0.00070
               AND ABS(v.longitude - (-9.1538381)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12829036124 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Powerlab', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7561076, -9.0942202, 'OPENSTREETMAP', 'OSM:NODE:12829036124',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12829036124')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Powerlab'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7561076)) <= 0.00070
               AND ABS(v.longitude - (-9.0942202)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12913895911 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Prescription Pilates', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7043492, -9.1738041, 'OPENSTREETMAP', 'OSM:NODE:12913895911',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12913895911')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Prescription Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7043492)) <= 0.00070
               AND ABS(v.longitude - (-9.1738041)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13755937201 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PT do Bairro Restelo', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7076011, -9.2055851, 'OPENSTREETMAP', 'OSM:NODE:13755937201',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13755937201')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PT do Bairro Restelo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7076011)) <= 0.00070
               AND ABS(v.longitude - (-9.2055851)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196089047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Rackets Pro - EUL', 'Lisboa', 'Lisboa', NULL,
       'Padel;Ténis', 'Ginásio', NULL,
       38.7526853, -9.1639354, 'OPENSTREETMAP', 'OSM:WAY:196089047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196089047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Rackets Pro - EUL'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7526853)) <= 0.00070
               AND ABS(v.longitude - (-9.1639354)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9730267974 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ricardo Oliveira Studio Personal Trainer', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7203857, -9.1616372, 'OPENSTREETMAP', 'OSM:NODE:9730267974',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9730267974')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ricardo Oliveira Studio Personal Trainer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7203857)) <= 0.00070
               AND ABS(v.longitude - (-9.1616372)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4253791151 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Lisboa', 'Lisboa', NULL,
       'Ginásio;Natação', 'Ginásio', NULL,
       38.7369060, -9.1440055, 'OPENSTREETMAP', 'OSM:NODE:4253791151',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4253791151')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7369060)) <= 0.00070
               AND ABS(v.longitude - (-9.1440055)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5261556093 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Lisboa', 'Lisboa', NULL,
       'Natação;Ginásio', 'Ginásio', NULL,
       38.7745853, -9.1558297, 'OPENSTREETMAP', 'OSM:NODE:5261556093',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5261556093')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7745853)) <= 0.00070
               AND ABS(v.longitude - (-9.1558297)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1464456936 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7553424, -9.1892241, 'OPENSTREETMAP', 'OSM:WAY:1464456936',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1464456936')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7553424)) <= 0.00070
               AND ABS(v.longitude - (-9.1892241)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:554315749 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7548324, -9.1669551, 'OPENSTREETMAP', 'OSM:WAY:554315749',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:554315749')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7548324)) <= 0.00070
               AND ABS(v.longitude - (-9.1669551)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14300934352 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Light', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7316816, -9.1451991, 'OPENSTREETMAP', 'OSM:NODE:14300934352',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14300934352')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Light'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7316816)) <= 0.00070
               AND ABS(v.longitude - (-9.1451991)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:49292932 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sport Lisboa e Olivais', 'Lisboa', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7622602, -9.1079035, 'OPENSTREETMAP', 'OSM:WAY:49292932',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:49292932')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sport Lisboa e Olivais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7622602)) <= 0.00070
               AND ABS(v.longitude - (-9.1079035)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11879097732 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Studion In', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7763564, -9.0956202, 'OPENSTREETMAP', 'OSM:NODE:11879097732',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11879097732')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Studion In'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7763564)) <= 0.00070
               AND ABS(v.longitude - (-9.0956202)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14056267902 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Supera', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7626947, -9.1780300, 'OPENSTREETMAP', 'OSM:NODE:14056267902',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14056267902')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Supera'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7626947)) <= 0.00070
               AND ABS(v.longitude - (-9.1780300)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11879097719 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'TechBody', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7754824, -9.0964650, 'OPENSTREETMAP', 'OSM:NODE:11879097719',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11879097719')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('TechBody'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7754824)) <= 0.00070
               AND ABS(v.longitude - (-9.0964650)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12304829071 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Techbody by Symbiont', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7610079, -9.1795642, 'OPENSTREETMAP', 'OSM:NODE:12304829071',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12304829071')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Techbody by Symbiont'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7610079)) <= 0.00070
               AND ABS(v.longitude - (-9.1795642)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6385068142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'The Strength Clinic', 'Lisboa', 'Lisboa', NULL,
       'Atletismo', 'Ginásio', NULL,
       38.7058052, -9.1785115, 'OPENSTREETMAP', 'OSM:NODE:6385068142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6385068142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('The Strength Clinic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7058052)) <= 0.00070
               AND ABS(v.longitude - (-9.1785115)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11065620353 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Time to Fitness 24', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7344121, -9.1412981, 'OPENSTREETMAP', 'OSM:NODE:11065620353',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11065620353')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Time to Fitness 24'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7344121)) <= 0.00070
               AND ABS(v.longitude - (-9.1412981)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12304225840 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Time To Fitness 24', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7612098, -9.1569569, 'OPENSTREETMAP', 'OSM:NODE:12304225840',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12304225840')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Time To Fitness 24'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7612098)) <= 0.00070
               AND ABS(v.longitude - (-9.1569569)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13110210701 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Time to Fitness Alameda', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7354132, -9.1337759, 'OPENSTREETMAP', 'OSM:NODE:13110210701',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13110210701')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Time to Fitness Alameda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7354132)) <= 0.00070
               AND ABS(v.longitude - (-9.1337759)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11868846173 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Timo to Fitness 24 Alvalade', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7538798, -9.1438870, 'OPENSTREETMAP', 'OSM:NODE:11868846173',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11868846173')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Timo to Fitness 24 Alvalade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7538798)) <= 0.00070
               AND ABS(v.longitude - (-9.1438870)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9730267973 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Urban Bliss Lisboa', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7203656, -9.1615975, 'OPENSTREETMAP', 'OSM:NODE:9730267973',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9730267973')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Urban Bliss Lisboa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7203656)) <= 0.00070
               AND ABS(v.longitude - (-9.1615975)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3109770683 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vertigo Climbing Center', 'Lisboa', 'Lisboa', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       38.7399020, -9.1020322, 'OPENSTREETMAP', 'OSM:NODE:3109770683',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3109770683')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vertigo Climbing Center'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7399020)) <= 0.00070
               AND ABS(v.longitude - (-9.1020322)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10004985766 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7725695, -9.1595384, 'OPENSTREETMAP', 'OSM:NODE:10004985766',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10004985766')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7725695)) <= 0.00070
               AND ABS(v.longitude - (-9.1595384)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11198128502 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7208404, -9.1598237, 'OPENSTREETMAP', 'OSM:NODE:11198128502',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11198128502')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7208404)) <= 0.00070
               AND ABS(v.longitude - (-9.1598237)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6126021189 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga Integral de Portugal', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7450145, -9.1377419, 'OPENSTREETMAP', 'OSM:NODE:6126021189',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6126021189')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga Integral de Portugal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7450145)) <= 0.00070
               AND ABS(v.longitude - (-9.1377419)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11895979178 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Zen Fir Pilates', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7174880, -9.1689566, 'OPENSTREETMAP', 'OSM:NODE:11895979178',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11895979178')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Zen Fir Pilates'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7174880)) <= 0.00070
               AND ABS(v.longitude - (-9.1689566)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11891668074 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Zen Fit Lisboa', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7174665, -9.1682959, 'OPENSTREETMAP', 'OSM:NODE:11891668074',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11891668074')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Zen Fit Lisboa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7174665)) <= 0.00070
               AND ABS(v.longitude - (-9.1682959)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8702597939 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CECSSAC GYM', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8122950, -9.1592809, 'OPENSTREETMAP', 'OSM:NODE:8702597939',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8702597939')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CECSSAC GYM'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8122950)) <= 0.00070
               AND ABS(v.longitude - (-9.1592809)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:144950211 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Ténis da Quinta das Flores', 'Loures', 'Lisboa', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.8074960, -9.1599323, 'OPENSTREETMAP', 'OSM:WAY:144950211',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:144950211')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Ténis da Quinta das Flores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8074960)) <= 0.00070
               AND ABS(v.longitude - (-9.1599323)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3162577571 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8290586, -9.1624388, 'OPENSTREETMAP', 'OSM:NODE:3162577571',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3162577571')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8290586)) <= 0.00070
               AND ABS(v.longitude - (-9.1624388)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:796558100 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut Sacavém', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7924950, -9.1010833, 'OPENSTREETMAP', 'OSM:WAY:796558100',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:796558100')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut Sacavém'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7924950)) <= 0.00070
               AND ABS(v.longitude - (-9.1010833)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:140339483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Paz e Amizade', 'Loures', 'Lisboa', NULL,
       'Andebol', 'Complexo desportivo', NULL,
       38.8289272, -9.1646673, 'OPENSTREETMAP', 'OSM:WAY:140339483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:140339483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Paz e Amizade'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8289272)) <= 0.00070
               AND ABS(v.longitude - (-9.1646673)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10235056740 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.8335308, -9.1564195, 'OPENSTREETMAP', 'OSM:NODE:10235056740',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10235056740')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8335308)) <= 0.00070
               AND ABS(v.longitude - (-9.1564195)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12791452401 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tonik Sacavém', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7936101, -9.1058806, 'OPENSTREETMAP', 'OSM:NODE:12791452401',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12791452401')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tonik Sacavém'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7936101)) <= 0.00070
               AND ABS(v.longitude - (-9.1058806)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5614011683 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Urban Fitness', 'Loures', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7893854, -9.1076333, 'OPENSTREETMAP', 'OSM:NODE:5614011683',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5614011683')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Loures'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Urban Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7893854)) <= 0.00070
               AND ABS(v.longitude - (-9.1076333)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6814185483 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ADRA Ginásio', 'Mafra', 'Lisboa', NULL,
       'Karaté', 'Ginásio', NULL,
       38.9695546, -9.3628637, 'OPENSTREETMAP', 'OSM:NODE:6814185483',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6814185483')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ADRA Ginásio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9695546)) <= 0.00070
               AND ABS(v.longitude - (-9.3628637)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:129965482 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Crossfit Boarland', 'Mafra', 'Lisboa', NULL,
       'Halterofilismo', 'Ginásio', NULL,
       38.9331229, -9.3328850, 'OPENSTREETMAP', 'OSM:WAY:129965482',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:129965482')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Crossfit Boarland'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9331229)) <= 0.00070
               AND ABS(v.longitude - (-9.3328850)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11093596005 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ericeira Boulder', 'Mafra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0014002, -9.4182278, 'OPENSTREETMAP', 'OSM:NODE:11093596005',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11093596005')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mafra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ericeira Boulder'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0014002)) <= 0.00070
               AND ABS(v.longitude - (-9.4182278)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:970346126 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Odivelas', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7925758, -9.1692802, 'OPENSTREETMAP', 'OSM:WAY:970346126',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:970346126')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Odivelas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7925758)) <= 0.00070
               AND ABS(v.longitude - (-9.1692802)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6058831159 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Arena Club Oeiras', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6917237, -9.3046707, 'OPENSTREETMAP', 'OSM:NODE:6058831159',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6058831159')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Arena Club Oeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6917237)) <= 0.00070
               AND ABS(v.longitude - (-9.3046707)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:167195086 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva de Oeiras', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.6861287, -9.3140043, 'OPENSTREETMAP', 'OSM:WAY:167195086',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:167195086')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva de Oeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6861287)) <= 0.00070
               AND ABS(v.longitude - (-9.3140043)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:749197690 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Polidesportivo - Parque Urbano de Queijas', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7246227, -9.2617142, 'OPENSTREETMAP', 'OSM:WAY:749197690',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:749197690')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Polidesportivo - Parque Urbano de Queijas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7246227)) <= 0.00070
               AND ABS(v.longitude - (-9.2617142)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13665884623 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CENTRIQ - Premium', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7149788, -9.2621317, 'OPENSTREETMAP', 'OSM:NODE:13665884623',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13665884623')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CENTRIQ - Premium'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7149788)) <= 0.00070
               AND ABS(v.longitude - (-9.2621317)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:179417430 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Oeiras', 'Oeiras', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.7006597, -9.3041340, 'OPENSTREETMAP', 'OSM:WAY:179417430',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:179417430')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Oeiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7006597)) <= 0.00070
               AND ABS(v.longitude - (-9.3041340)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8699492090 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Hut', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7165752, -9.2382869, 'OPENSTREETMAP', 'OSM:NODE:8699492090',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8699492090')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Hut'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7165752)) <= 0.00070
               AND ABS(v.longitude - (-9.2382869)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:3645949294 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Holmes Place', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7032582, -9.3034387, 'OPENSTREETMAP', 'OSM:NODE:3645949294',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:3645949294')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Holmes Place'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7032582)) <= 0.00070
               AND ABS(v.longitude - (-9.3034387)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:143326272 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7084701, -9.2455326, 'OPENSTREETMAP', 'OSM:WAY:143326272',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:143326272')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7084701)) <= 0.00070
               AND ABS(v.longitude - (-9.2455326)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:167177456 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic', 'Oeiras', 'Lisboa', NULL,
       'Natação', 'Ginásio', NULL,
       38.6865955, -9.3235242, 'OPENSTREETMAP', 'OSM:WAY:167177456',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:167177456')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6865955)) <= 0.00070
               AND ABS(v.longitude - (-9.3235242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13752790702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Solinca Classic Alfragide', 'Oeiras', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7244625, -9.2181247, 'OPENSTREETMAP', 'OSM:NODE:13752790702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13752790702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Oeiras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Solinca Classic Alfragide'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7244625)) <= 0.00070
               AND ABS(v.longitude - (-9.2181247)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5663379651 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'AmRita - Estúdio de Yôga', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7678532, -9.2934406, 'OPENSTREETMAP', 'OSM:NODE:5663379651',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5663379651')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('AmRita - Estúdio de Yôga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7678532)) <= 0.00070
               AND ABS(v.longitude - (-9.2934406)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1384458717 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Beloura Pestana Golf', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7647670, -9.3697776, 'OPENSTREETMAP', 'OSM:WAY:1384458717',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1384458717')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Beloura Pestana Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7647670)) <= 0.00070
               AND ABS(v.longitude - (-9.3697776)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:454814666 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Beloura Tennis Academy', 'Sintra', 'Lisboa', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.7639046, -9.3746640, 'OPENSTREETMAP', 'OSM:WAY:454814666',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:454814666')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Beloura Tennis Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7639046)) <= 0.00070
               AND ABS(v.longitude - (-9.3746640)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:166860103 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Afonso Araújo Sommer', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.8333947, -9.3613930, 'OPENSTREETMAP', 'OSM:WAY:166860103',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:166860103')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Afonso Araújo Sommer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8333947)) <= 0.00070
               AND ABS(v.longitude - (-9.3613930)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:534155501 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Doutor Raúl Neves', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8065033, -9.3304901, 'OPENSTREETMAP', 'OSM:WAY:534155501',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:534155501')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Doutor Raúl Neves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8065033)) <= 0.00070
               AND ABS(v.longitude - (-9.3304901)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:534155499 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Rio de Mouro', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7813694, -9.3209762, 'OPENSTREETMAP', 'OSM:WAY:534155499',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:534155499')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo de Rio de Mouro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7813694)) <= 0.00070
               AND ABS(v.longitude - (-9.3209762)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:534155497 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Sargento Arménio', 'Sintra', 'Lisboa', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.8105559, -9.3668510, 'OPENSTREETMAP', 'OSM:WAY:534155497',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:534155497')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Sargento Arménio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8105559)) <= 0.00070
               AND ABS(v.longitude - (-9.3668510)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10837445562 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7557903, -9.2543075, 'OPENSTREETMAP', 'OSM:NODE:10837445562',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10837445562')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7557903)) <= 0.00070
               AND ABS(v.longitude - (-9.2543075)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11878846599 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element', 'Sintra', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7872556, -9.3178995, 'OPENSTREETMAP', 'OSM:NODE:11878846599',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11878846599')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7872556)) <= 0.00070
               AND ABS(v.longitude - (-9.3178995)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4927151503 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo e Recreativo de Manique de Cima', 'Sintra', 'Lisboa', NULL,
       'Ginásio;Futebol;Atletismo', 'Complexo desportivo', NULL,
       38.7569801, -9.3602401, 'OPENSTREETMAP', 'OSM:NODE:4927151503',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4927151503')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo e Recreativo de Manique de Cima'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7569801)) <= 0.00070
               AND ABS(v.longitude - (-9.3602401)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1347181240 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lisbon Sports Club', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.8016075, -9.2770735, 'OPENSTREETMAP', 'OSM:WAY:1347181240',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1347181240')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lisbon Sports Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8016075)) <= 0.00070
               AND ABS(v.longitude - (-9.2770735)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:210138245 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lisbon Sports Club', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7974920, -9.2782391, 'OPENSTREETMAP', 'OSM:WAY:210138245',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:210138245')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lisbon Sports Club'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7974920)) <= 0.00070
               AND ABS(v.longitude - (-9.2782391)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1384458718 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pestana Beloura Golf', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7686802, -9.3733468, 'OPENSTREETMAP', 'OSM:WAY:1384458718',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1384458718')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pestana Beloura Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7686802)) <= 0.00070
               AND ABS(v.longitude - (-9.3733468)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1384458719 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pestana Beloura Golf', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7694625, -9.3752335, 'OPENSTREETMAP', 'OSM:WAY:1384458719',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1384458719')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pestana Beloura Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7694625)) <= 0.00070
               AND ABS(v.longitude - (-9.3752335)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1384458720 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pestana Beloura Golf', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7668200, -9.3765842, 'OPENSTREETMAP', 'OSM:WAY:1384458720',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1384458720')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pestana Beloura Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7668200)) <= 0.00070
               AND ABS(v.longitude - (-9.3765842)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1384458721 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pestana Beloura Golf', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7644948, -9.3762282, 'OPENSTREETMAP', 'OSM:WAY:1384458721',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1384458721')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pestana Beloura Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7644948)) <= 0.00070
               AND ABS(v.longitude - (-9.3762282)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1384458722 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pestana Beloura Golf', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7659853, -9.3654612, 'OPENSTREETMAP', 'OSM:WAY:1384458722',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1384458722')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pestana Beloura Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7659853)) <= 0.00070
               AND ABS(v.longitude - (-9.3654612)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1384458723 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pestana Beloura Golf', 'Sintra', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7605328, -9.3707418, 'OPENSTREETMAP', 'OSM:WAY:1384458723',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1384458723')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sintra'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pestana Beloura Golf'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7605328)) <= 0.00070
               AND ABS(v.longitude - (-9.3707418)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:789241001 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação de Educação Física e Desportiva de Torres Vedras', 'Torres Vedras', 'Lisboa', NULL,
       'Basquetebol;Ginástica;Natação;Ténis de mesa;Voleibol', 'Complexo desportivo', NULL,
       39.0868373, -9.2555814, 'OPENSTREETMAP', 'OSM:WAY:789241001',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:789241001')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Vedras'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação de Educação Física e Desportiva de Torres Vedras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0868373)) <= 0.00070
               AND ABS(v.longitude - (-9.2555814)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:150518477 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais da Póvoa de Santa Iria', 'Vila Franca de Xira', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.8594797, -9.0773383, 'OPENSTREETMAP', 'OSM:WAY:150518477',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:150518477')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Franca de Xira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais da Póvoa de Santa Iria'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8594797)) <= 0.00070
               AND ABS(v.longitude - (-9.0773383)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:365309189 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Atlético Paredes', 'Alenquer', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0448400, -8.9997808, 'OPENSTREETMAP', 'OSM:WAY:365309189',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:365309189')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alenquer'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Atlético Paredes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0448400)) <= 0.00070
               AND ABS(v.longitude - (-8.9997808)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127872903 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do ADC', 'Alenquer', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0223203, -8.9792252, 'OPENSTREETMAP', 'OSM:WAY:127872903',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127872903')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alenquer'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do ADC'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0223203)) <= 0.00070
               AND ABS(v.longitude - (-8.9792252)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1149728993 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Tiro Vale das Pedras', 'Alenquer', 'Lisboa', NULL,
       'Tiro', 'Complexo desportivo', NULL,
       39.1115064, -8.9654933, 'OPENSTREETMAP', 'OSM:WAY:1149728993',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1149728993')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alenquer'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Tiro Vale das Pedras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1115064)) <= 0.00070
               AND ABS(v.longitude - (-8.9654933)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1448173386 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Tiro Vale das Pedras', 'Alenquer', 'Lisboa', NULL,
       'Tiro', 'Campo desportivo', NULL,
       39.1139099, -8.9673165, 'OPENSTREETMAP', 'OSM:WAY:1448173386',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1448173386')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alenquer'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Tiro Vale das Pedras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1139099)) <= 0.00070
               AND ABS(v.longitude - (-8.9673165)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13125229079 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'MyGym', 'Alenquer', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0246553, -8.9776668, 'OPENSTREETMAP', 'OSM:NODE:13125229079',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13125229079')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alenquer'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('MyGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0246553)) <= 0.00070
               AND ABS(v.longitude - (-8.9776668)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1427727970 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Padel Factory', 'Alenquer', 'Lisboa', NULL,
       'Padel', 'Complexo desportivo', NULL,
       39.0247226, -8.9763778, 'OPENSTREETMAP', 'OSM:WAY:1427727970',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1427727970')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alenquer'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Padel Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0247226)) <= 0.00070
               AND ABS(v.longitude - (-8.9763778)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102408999 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academica Fitness', 'Amadora', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7535746, -9.2280902, 'OPENSTREETMAP', 'OSM:WAY:102408999',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102408999')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academica Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7535746)) <= 0.00070
               AND ABS(v.longitude - (-9.2280902)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:710557882 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Nataçao da Amadora - Damaia', 'Amadora', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7436168, -9.2209346, 'OPENSTREETMAP', 'OSM:WAY:710557882',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:710557882')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Nataçao da Amadora - Damaia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7436168)) <= 0.00070
               AND ABS(v.longitude - (-9.2209346)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:102409000 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube de Natação da Amadora-Reboleira', 'Amadora', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7529651, -9.2284334, 'OPENSTREETMAP', 'OSM:WAY:102409000',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:102409000')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube de Natação da Amadora-Reboleira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7529651)) <= 0.00070
               AND ABS(v.longitude - (-9.2284334)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1284088061 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Ténis Alfragide', 'Amadora', 'Lisboa', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       38.7402130, -9.2230988, 'OPENSTREETMAP', 'OSM:NODE:1284088061',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1284088061')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Ténis Alfragide'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7402130)) <= 0.00070
               AND ABS(v.longitude - (-9.2230988)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1558593669 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Element Gyms', 'Amadora', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7504837, -9.2263522, 'OPENSTREETMAP', 'OSM:WAY:1558593669',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1558593669')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Element Gyms'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7504837)) <= 0.00070
               AND ABS(v.longitude - (-9.2263522)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1120602144 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Alfornelos', 'Amadora', 'Lisboa', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.7632344, -9.2082225, 'OPENSTREETMAP', 'OSM:WAY:1120602144',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1120602144')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Alfornelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7632344)) <= 0.00070
               AND ABS(v.longitude - (-9.2082225)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:872044684 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark de Alfornelos/Brandoa', 'Amadora', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7639579, -9.2112744, 'OPENSTREETMAP', 'OSM:WAY:872044684',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:872044684')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark de Alfornelos/Brandoa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7639579)) <= 0.00070
               AND ABS(v.longitude - (-9.2112744)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:746491434 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Ilha Mágica do Lido', 'Amadora', 'Lisboa', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.7577347, -9.2488712, 'OPENSTREETMAP', 'OSM:WAY:746491434',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:746491434')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Ilha Mágica do Lido'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7577347)) <= 0.00070
               AND ABS(v.longitude - (-9.2488712)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12176275738 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Soul Lifting', 'Amadora', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7476358, -9.2362479, 'OPENSTREETMAP', 'OSM:NODE:12176275738',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12176275738')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Soul Lifting'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7476358)) <= 0.00070
               AND ABS(v.longitude - (-9.2362479)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1227534045 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ténis de mesa', 'Amadora', 'Lisboa', NULL,
       'Ténis de mesa', 'Campo desportivo', NULL,
       38.7579585, -9.2488293, 'OPENSTREETMAP', 'OSM:WAY:1227534045',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1227534045')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ténis de mesa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7579585)) <= 0.00070
               AND ABS(v.longitude - (-9.2488293)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8662712640 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Viva Fit', 'Amadora', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7373148, -9.2234299, 'OPENSTREETMAP', 'OSM:NODE:8662712640',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8662712640')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Viva Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7373148)) <= 0.00070
               AND ABS(v.longitude - (-9.2234299)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13945677195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'VivaGym', 'Amadora', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7585083, -9.2186825, 'OPENSTREETMAP', 'OSM:NODE:13945677195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13945677195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Amadora'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('VivaGym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7585083)) <= 0.00070
               AND ABS(v.longitude - (-9.2186825)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:386869652 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Aveiras de Cima Sport Clube', 'Azambuja', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1498525, -8.9006161, 'OPENSTREETMAP', 'OSM:WAY:386869652',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:386869652')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Azambuja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Aveiras de Cima Sport Clube'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1498525)) <= 0.00070
               AND ABS(v.longitude - (-8.9006161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:450910093 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Cultural, Recreativo e Desportivo do Vale do Brejo', 'Azambuja', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1356485, -8.9244445, 'OPENSTREETMAP', 'OSM:WAY:450910093',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:450910093')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Azambuja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Cultural, Recreativo e Desportivo do Vale do Brejo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1356485)) <= 0.00070
               AND ABS(v.longitude - (-8.9244445)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10893744544 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque De Fitness De Vila Nova Da Rainha', 'Azambuja', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0398011, -8.9320592, 'OPENSTREETMAP', 'OSM:NODE:10893744544',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10893744544')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Azambuja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque De Fitness De Vila Nova Da Rainha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0398011)) <= 0.00070
               AND ABS(v.longitude - (-8.9320592)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4555364591 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas', 'Azambuja', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.0807680, -8.8789463, 'OPENSTREETMAP', 'OSM:NODE:4555364591',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4555364591')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Azambuja'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0807680)) <= 0.00070
               AND ABS(v.longitude - (-8.8789463)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:821978830 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Bola', 'Cadaval', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2659828, -9.0712370, 'OPENSTREETMAP', 'OSM:WAY:821978830',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:821978830')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cadaval'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Bola'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2659828)) <= 0.00070
               AND ABS(v.longitude - (-9.0712370)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:821978834 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo Municipal', 'Cadaval', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.2419057, -9.1071857, 'OPENSTREETMAP', 'OSM:WAY:821978834',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:821978834')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cadaval'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2419057)) <= 0.00070
               AND ABS(v.longitude - (-9.1071857)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1087644191 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Carcavelos Bikepark', 'Cascais', 'Lisboa', NULL,
       'Ciclismo', 'Pista desportiva', NULL,
       38.6824588, -9.3324808, 'OPENSTREETMAP', 'OSM:WAY:1087644191',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1087644191')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Carcavelos Bikepark'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6824588)) <= 0.00070
               AND ABS(v.longitude - (-9.3324808)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4033727289 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Circuito de Manutenção da Quinta do Gonçalo', 'Cascais', 'Lisboa', NULL,
       'Corrida', 'Complexo desportivo', NULL,
       38.6807001, -9.3238392, 'OPENSTREETMAP', 'OSM:NODE:4033727289',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4033727289')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Circuito de Manutenção da Quinta do Gonçalo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6807001)) <= 0.00070
               AND ABS(v.longitude - (-9.3238392)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:553641593 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio António Coimbra da Mota', 'Cascais', 'Lisboa', NULL,
       'Futebol', 'Estádio', NULL,
       38.7157451, -9.4064245, 'OPENSTREETMAP', 'OSM:WAY:553641593',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:553641593')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio António Coimbra da Mota'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7157451)) <= 0.00070
               AND ABS(v.longitude - (-9.4064245)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5183385521 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'fmp - fitness', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7067626, -9.4058989, 'OPENSTREETMAP', 'OSM:NODE:5183385521',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5183385521')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('fmp - fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7067626)) <= 0.00070
               AND ABS(v.longitude - (-9.4058989)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5615770760 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gandarinha Health Club- Just Fitness', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6954785, -9.4296987, 'OPENSTREETMAP', 'OSM:NODE:5615770760',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5615770760')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gandarinha Health Club- Just Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6954785)) <= 0.00070
               AND ABS(v.longitude - (-9.4296987)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:195730990 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo Estoril Praia', 'Cascais', 'Lisboa', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.7157292, -9.4064420, 'OPENSTREETMAP', 'OSM:WAY:195730990',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:195730990')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo Estoril Praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7157292)) <= 0.00070
               AND ABS(v.longitude - (-9.4064420)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5529024722 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pilates Feline Studio', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7057897, -9.4280107, 'OPENSTREETMAP', 'OSM:NODE:5529024722',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5529024722')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pilates Feline Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7057897)) <= 0.00070
               AND ABS(v.longitude - (-9.4280107)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10016044556 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Serenity Spa', 'Cascais', 'Lisboa', NULL,
       'Ginásio;Corrida', 'Ginásio', NULL,
       38.7100553, -9.4604791, 'OPENSTREETMAP', 'OSM:NODE:10016044556',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10016044556')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Serenity Spa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7100553)) <= 0.00070
               AND ABS(v.longitude - (-9.4604791)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5609858229 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Seven 4 you', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7385217, -9.3938647, 'OPENSTREETMAP', 'OSM:NODE:5609858229',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5609858229')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Seven 4 you'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7385217)) <= 0.00070
               AND ABS(v.longitude - (-9.3938647)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:145113848 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tennis Rivieira', 'Cascais', 'Lisboa', NULL,
       'Ténis', 'Campo desportivo', NULL,
       38.6856896, -9.3437508, 'OPENSTREETMAP', 'OSM:WAY:145113848',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:145113848')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tennis Rivieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6856896)) <= 0.00070
               AND ABS(v.longitude - (-9.3437508)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5607485883 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'yoga Studio', 'Cascais', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.6995368, -9.3823268, 'OPENSTREETMAP', 'OSM:NODE:5607485883',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5607485883')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cascais'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('yoga Studio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.6995368)) <= 0.00070
               AND ABS(v.longitude - (-9.3823268)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2505094742 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia', 'Lisboa', 'Lisboa', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       38.7060341, -9.1531464, 'OPENSTREETMAP', 'OSM:NODE:2505094742',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2505094742')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7060341)) <= 0.00070
               AND ABS(v.longitude - (-9.1531464)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:54753992 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Academia de Golf de Lisboa', 'Lisboa', 'Lisboa', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.7556220, -9.1654242, 'OPENSTREETMAP', 'OSM:WAY:54753992',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:54753992')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Academia de Golf de Lisboa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7556220)) <= 0.00070
               AND ABS(v.longitude - (-9.1654242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10128140192 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Agora Yoga & Meditation', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7221885, -9.1374168, 'OPENSTREETMAP', 'OSM:NODE:10128140192',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10128140192')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Agora Yoga & Meditation'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7221885)) <= 0.00070
               AND ABS(v.longitude - (-9.1374168)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:242543859 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Arco Club Ar LIvre', 'Lisboa', 'Lisboa', NULL,
       'Tiro com Arco', 'Campo desportivo', NULL,
       38.7376837, -9.1882723, 'OPENSTREETMAP', 'OSM:WAY:242543859',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:242543859')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Arco Club Ar LIvre'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7376837)) <= 0.00070
               AND ABS(v.longitude - (-9.1882723)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12314098154 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Authentic Pilates - Alto dos Moinhos', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7511949, -9.1797220, 'OPENSTREETMAP', 'OSM:NODE:12314098154',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12314098154')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Authentic Pilates - Alto dos Moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7511949)) <= 0.00070
               AND ABS(v.longitude - (-9.1797220)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:75107567 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Baía dos Golfinhos', 'Lisboa', 'Lisboa', NULL,
       'Natação', 'Piscina', NULL,
       38.7425973, -9.1709472, 'OPENSTREETMAP', 'OSM:WAY:75107567',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:75107567')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Baía dos Golfinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7425973)) <= 0.00070
               AND ABS(v.longitude - (-9.1709472)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:196087549 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Basquetebol', 'Lisboa', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.7508162, -9.1648325, 'OPENSTREETMAP', 'OSM:WAY:196087549',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:196087549')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Basquetebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7508162)) <= 0.00070
               AND ABS(v.longitude - (-9.1648325)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:839748586 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Basquetebol Casal dos Machados', 'Lisboa', 'Lisboa', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.7725272, -9.1032514, 'OPENSTREETMAP', 'OSM:WAY:839748586',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:839748586')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Basquetebol Casal dos Machados'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7725272)) <= 0.00070
               AND ABS(v.longitude - (-9.1032514)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9720223741 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'beHAUS', 'Lisboa', 'Lisboa', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.7293985, -9.1584504, 'OPENSTREETMAP', 'OSM:NODE:9720223741',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9720223741')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('beHAUS'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7293985)) <= 0.00070
               AND ABS(v.longitude - (-9.1584504)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:763009762 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Belém Rugby Park', 'Lisboa', 'Lisboa', NULL,
       'Rugby', 'Campo desportivo', NULL,
       38.7150375, -9.2070488, 'OPENSTREETMAP', 'OSM:WAY:763009762',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:763009762')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Lisboa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Belém Rugby Park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.7150375)) <= 0.00070
               AND ABS(v.longitude - (-9.2070488)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
