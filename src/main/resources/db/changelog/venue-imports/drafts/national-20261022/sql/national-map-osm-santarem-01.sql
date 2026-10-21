-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:NODE:9413559884 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Gynásio Always Fitness', 'Abrantes', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.4688856, -8.2078476, 'OPENSTREETMAP', 'OSM:NODE:9413559884',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9413559884')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Gynásio Always Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4688856)) <= 0.00070
               AND ABS(v.longitude - (-8.2078476)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13366827129 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Lezíria Indoor Padel', 'Benavente', 'Santarém', NULL,
       'Padel', 'Campo desportivo', NULL,
       38.9187704, -8.8622238, 'OPENSTREETMAP', 'OSM:NODE:13366827129',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13366827129')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Lezíria Indoor Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9187704)) <= 0.00070
               AND ABS(v.longitude - (-8.8622238)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:217562754 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ribagolfe (Course I + II)', 'Benavente', 'Santarém', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.8194015, -8.7532579, 'OPENSTREETMAP', 'OSM:WAY:217562754',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:217562754')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ribagolfe (Course I + II)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8194015)) <= 0.00070
               AND ABS(v.longitude - (-8.7532579)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:8268112603 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bless Fitness', 'Entroncamento', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.4773870, -8.4660332, 'OPENSTREETMAP', 'OSM:NODE:8268112603',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:8268112603')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Entroncamento'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bless Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4773870)) <= 0.00070
               AND ABS(v.longitude - (-8.4660332)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1309325020 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Clube Entre-Linhas', 'Entroncamento', 'Santarém', NULL,
       'Ginásio;Corrida', 'Ginásio', NULL,
       39.4685361, -8.4674265, 'OPENSTREETMAP', 'OSM:WAY:1309325020',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1309325020')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Entroncamento'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Clube Entre-Linhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4685361)) <= 0.00070
               AND ABS(v.longitude - (-8.4674265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11738493669 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'MaviYoga', 'Ourém', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.6175822, -8.6615020, 'OPENSTREETMAP', 'OSM:NODE:11738493669',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11738493669')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('MaviYoga'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6175822)) <= 0.00070
               AND ABS(v.longitude - (-8.6615020)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13679674840 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Cartódromo De Alcanede', 'Santarém', 'Santarém', NULL,
       'Karting', 'Complexo desportivo', NULL,
       39.4231066, -8.8049217, 'OPENSTREETMAP', 'OSM:NODE:13679674840',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13679674840')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Cartódromo De Alcanede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4231066)) <= 0.00070
               AND ABS(v.longitude - (-8.8049217)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9413671622 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Clube de Tomar', 'Tomar', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.6030421, -8.4015330, 'OPENSTREETMAP', 'OSM:NODE:9413671622',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9413671622')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Clube de Tomar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6030421)) <= 0.00070
               AND ABS(v.longitude - (-8.4015330)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11275469566 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'LR Fitness - Ginásio', 'Tomar', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.6048338, -8.4106753, 'OPENSTREETMAP', 'OSM:NODE:11275469566',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11275469566')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('LR Fitness - Ginásio'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6048338)) <= 0.00070
               AND ABS(v.longitude - (-8.4106753)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11738446569 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Yoga with Viki', 'Tomar', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.6056559, -8.4101355, 'OPENSTREETMAP', 'OSM:NODE:11738446569',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11738446569')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Yoga with Viki'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6056559)) <= 0.00070
               AND ABS(v.longitude - (-8.4101355)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1377196891 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'CrossFit', 'Torres Novas', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.4666168, -8.5315813, 'OPENSTREETMAP', 'OSM:WAY:1377196891',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1377196891')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('CrossFit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4666168)) <= 0.00070
               AND ABS(v.longitude - (-8.5315813)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:114834834 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Basebol', 'Abrantes', 'Santarém', NULL,
       'Basebol', 'Campo desportivo', NULL,
       39.4595804, -8.2170070, 'OPENSTREETMAP', 'OSM:WAY:114834834',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:114834834')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Basebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4595804)) <= 0.00070
               AND ABS(v.longitude - (-8.2170070)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:299539332 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Armando Matafome', 'Abrantes', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4583666, -8.0510745, 'OPENSTREETMAP', 'OSM:WAY:299539332',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:299539332')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Armando Matafome'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4583666)) <= 0.00070
               AND ABS(v.longitude - (-8.0510745)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:976900189 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Rio de Moinhos', 'Abrantes', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4807244, -8.2466038, 'OPENSTREETMAP', 'OSM:WAY:976900189',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:976900189')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Rio de Moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4807244)) <= 0.00070
               AND ABS(v.longitude - (-8.2466038)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:244138331 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de futebol Os Dragões', 'Abrantes', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4730350, -8.1856407, 'OPENSTREETMAP', 'OSM:WAY:244138331',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:244138331')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de futebol Os Dragões'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4730350)) <= 0.00070
               AND ABS(v.longitude - (-8.1856407)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:299631213 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futsal Concavadda', 'Abrantes', 'Santarém', NULL,
       'Futsal', 'Campo desportivo', NULL,
       39.4516620, -8.0654157, 'OPENSTREETMAP', 'OSM:WAY:299631213',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:299631213')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futsal Concavadda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4516620)) <= 0.00070
               AND ABS(v.longitude - (-8.0654157)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:299528740 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos José dos Santos Ruivo', 'Abrantes', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4522611, -8.0653992, 'OPENSTREETMAP', 'OSM:WAY:299528740',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:299528740')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos José dos Santos Ruivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4522611)) <= 0.00070
               AND ABS(v.longitude - (-8.0653992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:375592154 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de ténis', 'Abrantes', 'Santarém', NULL,
       'Ténis', 'Campo desportivo', NULL,
       39.4629143, -8.2045747, 'OPENSTREETMAP', 'OSM:WAY:375592154',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:375592154')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4629143)) <= 0.00070
               AND ABS(v.longitude - (-8.2045747)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:976912897 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Voleibol de Rio de Moinhos', 'Abrantes', 'Santarém', NULL,
       'Voleibol de Praia', 'Campo desportivo', NULL,
       39.4733099, -8.2472263, 'OPENSTREETMAP', 'OSM:WAY:976912897',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:976912897')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Voleibol de Rio de Moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4733099)) <= 0.00070
               AND ABS(v.longitude - (-8.2472263)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:114834830 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Abrantes', 'Abrantes', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.4585284, -8.2176385, 'OPENSTREETMAP', 'OSM:WAY:114834830',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:114834830')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Abrantes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4585284)) <= 0.00070
               AND ABS(v.longitude - (-8.2176385)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1202429264 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Vale das Mós', 'Abrantes', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.3429530, -8.0634047, 'OPENSTREETMAP', 'OSM:WAY:1202429264',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1202429264')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Vale das Mós'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3429530)) <= 0.00070
               AND ABS(v.longitude - (-8.0634047)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825282473 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Abrantes', 'Abrantes', 'Santarém', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.4589244, -8.2153437, 'OPENSTREETMAP', 'OSM:WAY:825282473',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825282473')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Abrantes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4589244)) <= 0.00070
               AND ABS(v.longitude - (-8.2153437)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:734263496 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue Casais de Revelhos', 'Abrantes', 'Santarém', NULL,
       'Futsal;Futebol', 'Campo desportivo', NULL,
       39.5042416, -8.1797575, 'OPENSTREETMAP', 'OSM:WAY:734263496',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:734263496')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue Casais de Revelhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5042416)) <= 0.00070
               AND ABS(v.longitude - (-8.1797575)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:976893828 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Futebol de Rio de Moinhos', 'Abrantes', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4760172, -8.2446205, 'OPENSTREETMAP', 'OSM:WAY:976893828',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:976893828')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Futebol de Rio de Moinhos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4760172)) <= 0.00070
               AND ABS(v.longitude - (-8.2446205)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:114834846 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sintético', 'Abrantes', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4603577, -8.2159774, 'OPENSTREETMAP', 'OSM:WAY:114834846',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:114834846')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Abrantes'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sintético'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4603577)) <= 0.00070
               AND ABS(v.longitude - (-8.2159774)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1172748636 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas municipais', 'Alcanena', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.4565974, -8.6666476, 'OPENSTREETMAP', 'OSM:WAY:1172748636',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1172748636')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcanena'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4565974)) <= 0.00070
               AND ABS(v.longitude - (-8.6666476)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825282481 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Alcanena', 'Alcanena', 'Santarém', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.4563193, -8.6647632, 'OPENSTREETMAP', 'OSM:WAY:825282481',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825282481')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alcanena'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Alcanena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4563193)) <= 0.00070
               AND ABS(v.longitude - (-8.6647632)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:506051452 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Street Futsal UEFA', 'Almeirim', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2117444, -8.6193962, 'OPENSTREETMAP', 'OSM:WAY:506051452',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:506051452')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeirim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Street Futsal UEFA'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2117444)) <= 0.00070
               AND ABS(v.longitude - (-8.6193962)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:441960423 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Hóquei Club Os Tigres', 'Almeirim', 'Santarém', NULL,
       'Hóquei;Futebol;Basquetebol;Atletismo', 'Complexo desportivo', NULL,
       39.2061801, -8.6243521, 'OPENSTREETMAP', 'OSM:WAY:441960423',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:441960423')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeirim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Hóquei Club Os Tigres'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2061801)) <= 0.00070
               AND ABS(v.longitude - (-8.6243521)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1567601611 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Prof. José Sousa Gomes', 'Almeirim', 'Santarém', NULL,
       'Futebol', 'Estádio', NULL,
       39.1795623, -8.5780677, 'OPENSTREETMAP', 'OSM:WAY:1567601611',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1567601611')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeirim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Prof. José Sousa Gomes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1795623)) <= 0.00070
               AND ABS(v.longitude - (-8.5780677)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:618561929 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Dona Eugénia Manuel', 'Almeirim', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.1435735, -8.6933960, 'OPENSTREETMAP', 'OSM:WAY:618561929',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:618561929')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeirim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Dona Eugénia Manuel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1435735)) <= 0.00070
               AND ABS(v.longitude - (-8.6933960)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:547028062 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque Polidesportivo', 'Almeirim', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2284423, -8.6571522, 'OPENSTREETMAP', 'OSM:WAY:547028062',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:547028062')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeirim'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque Polidesportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2284423)) <= 0.00070
               AND ABS(v.longitude - (-8.6571522)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1306901369 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Doutor Raul José das Neves', 'Alpiarça', 'Santarém', NULL,
       'Ciclismo;Futebol', 'Complexo desportivo', NULL,
       39.2524043, -8.5898768, 'OPENSTREETMAP', 'OSM:WAY:1306901369',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1306901369')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Alpiarça'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Doutor Raul José das Neves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2524043)) <= 0.00070
               AND ABS(v.longitude - (-8.5898768)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:579440944 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Arcílio Rocha', 'Benavente', 'Santarém', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.9090314, -8.8789638, 'OPENSTREETMAP', 'OSM:WAY:579440944',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:579440944')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Arcílio Rocha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9090314)) <= 0.00070
               AND ABS(v.longitude - (-8.8789638)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:667980461 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de basquetebol de Samora Correia', 'Benavente', 'Santarém', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       38.9316833, -8.8645568, 'OPENSTREETMAP', 'OSM:WAY:667980461',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:667980461')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de basquetebol de Samora Correia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9316833)) <= 0.00070
               AND ABS(v.longitude - (-8.8645568)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:843664147 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de terra do Belo Jardim', 'Benavente', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9401866, -8.8588620, 'OPENSTREETMAP', 'OSM:WAY:843664147',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:843664147')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de terra do Belo Jardim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9401866)) <= 0.00070
               AND ABS(v.longitude - (-8.8588620)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:558600142 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Polivalente do Sapal Entre Águas', 'Benavente', 'Santarém', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.9270159, -8.8857987, 'OPENSTREETMAP', 'OSM:WAY:558600142',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:558600142')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Polivalente do Sapal Entre Águas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9270159)) <= 0.00070
               AND ABS(v.longitude - (-8.8857987)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1528800348 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Santa Isabel', 'Benavente', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8587271, -8.7383307, 'OPENSTREETMAP', 'OSM:WAY:1528800348',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1528800348')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Santa Isabel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8587271)) <= 0.00070
               AND ABS(v.longitude - (-8.7383307)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:570652678 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Treinos AREPA', 'Benavente', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9098155, -8.8789195, 'OPENSTREETMAP', 'OSM:WAY:570652678',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:570652678')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Treinos AREPA'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9098155)) <= 0.00070
               AND ABS(v.longitude - (-8.8789195)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13520727231 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Dream Gym', 'Benavente', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.9251232, -8.8855684, 'OPENSTREETMAP', 'OSM:NODE:13520727231',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13520727231')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Dream Gym'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9251232)) <= 0.00070
               AND ABS(v.longitude - (-8.8855684)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:579440646 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estadio da Murteira', 'Benavente', 'Santarém', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       38.9287796, -8.8526700, 'OPENSTREETMAP', 'OSM:WAY:579440646',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:579440646')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estadio da Murteira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9287796)) <= 0.00070
               AND ABS(v.longitude - (-8.8526700)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1196904837 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory Samora Correia', 'Benavente', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       38.9160422, -8.8631409, 'OPENSTREETMAP', 'OSM:WAY:1196904837',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1196904837')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory Samora Correia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9160422)) <= 0.00070
               AND ABS(v.longitude - (-8.8631409)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:579449106 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginasio Physic', 'Benavente', 'Santarém', NULL,
       'Atletismo', 'Complexo desportivo', NULL,
       38.9230819, -8.8757313, 'OPENSTREETMAP', 'OSM:WAY:579449106',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:579449106')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginasio Physic'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9230819)) <= 0.00070
               AND ABS(v.longitude - (-8.8757313)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:570460188 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Samora Correia', 'Benavente', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       38.9352882, -8.8673035, 'OPENSTREETMAP', 'OSM:WAY:570460188',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:570460188')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Samora Correia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9352882)) <= 0.00070
               AND ABS(v.longitude - (-8.8673035)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825282494 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista do Complexo Desportivo dos Camarinhais', 'Benavente', 'Santarém', NULL,
       'Corrida', 'Pista desportiva', NULL,
       38.9788396, -8.8363044, 'OPENSTREETMAP', 'OSM:WAY:825282494',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825282494')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista do Complexo Desportivo dos Camarinhais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9788396)) <= 0.00070
               AND ABS(v.longitude - (-8.8363044)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222460702 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Santo Estevão Golfe', 'Benavente', 'Santarém', NULL,
       'Golfe', 'Campo de golfe', NULL,
       38.9061425, -8.7324215, 'OPENSTREETMAP', 'OSM:WAY:222460702',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222460702')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Santo Estevão Golfe'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9061425)) <= 0.00070
               AND ABS(v.longitude - (-8.7324215)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:667980460 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate Park Samora Correia', 'Benavente', 'Santarém', NULL,
       'Skate', 'Campo desportivo', NULL,
       38.9317390, -8.8643350, 'OPENSTREETMAP', 'OSM:WAY:667980460',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:667980460')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Benavente'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate Park Samora Correia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9317390)) <= 0.00070
               AND ABS(v.longitude - (-8.8643350)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:306751579 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Salgueiral', 'Cartaxo', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0789471, -8.7614230, 'OPENSTREETMAP', 'OSM:WAY:306751579',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:306751579')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cartaxo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Salgueiral'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0789471)) <= 0.00070
               AND ABS(v.longitude - (-8.7614230)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1624296329 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal', 'Cartaxo', 'Santarém', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.1714371, -8.7932876, 'OPENSTREETMAP', 'OSM:NODE:1624296329',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1624296329')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cartaxo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1714371)) <= 0.00070
               AND ABS(v.longitude - (-8.7932876)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:160103252 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skate park', 'Cartaxo', 'Santarém', NULL,
       'Skate', 'Complexo desportivo', NULL,
       39.1585432, -8.7882188, 'OPENSTREETMAP', 'OSM:WAY:160103252',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:160103252')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cartaxo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skate park'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1585432)) <= 0.00070
               AND ABS(v.longitude - (-8.7882188)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1004732793 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'tanque', 'Cartaxo', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.1820030, -8.8665038, 'OPENSTREETMAP', 'OSM:WAY:1004732793',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1004732793')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Cartaxo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('tanque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.1820030)) <= 0.00070
               AND ABS(v.longitude - (-8.8665038)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:468404793 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol da Canineira/Vale de Cavalos', 'Chamusca', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2838445, -8.5126204, 'OPENSTREETMAP', 'OSM:WAY:468404793',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:468404793')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chamusca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol da Canineira/Vale de Cavalos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2838445)) <= 0.00070
               AND ABS(v.longitude - (-8.5126204)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1046486286 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Chamusca', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.3614768, -8.4786905, 'OPENSTREETMAP', 'OSM:WAY:1046486286',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1046486286')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chamusca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3614768)) <= 0.00070
               AND ABS(v.longitude - (-8.4786905)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:655941319 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Carregueira', 'Chamusca', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4199087, -8.4154545, 'OPENSTREETMAP', 'OSM:WAY:655941319',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:655941319')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chamusca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Carregueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4199087)) <= 0.00070
               AND ABS(v.longitude - (-8.4154545)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:655756378 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'SkateParque Chamusca', 'Chamusca', 'Santarém', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.3593552, -8.4731903, 'OPENSTREETMAP', 'OSM:WAY:655756378',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:655756378')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Chamusca'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('SkateParque Chamusca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3593552)) <= 0.00070
               AND ABS(v.longitude - (-8.4731903)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:297547544 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Casa de Povo de Montalvo', 'Constância', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4830877, -8.3042526, 'OPENSTREETMAP', 'OSM:WAY:297547544',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:297547544')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Constância'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Casa de Povo de Montalvo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4830877)) <= 0.00070
               AND ABS(v.longitude - (-8.3042526)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:297547545 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da Portela', 'Constância', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4549874, -8.3230292, 'OPENSTREETMAP', 'OSM:WAY:297547545',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:297547545')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Constância'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da Portela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4549874)) <= 0.00070
               AND ABS(v.longitude - (-8.3230292)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:985691067 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Desporto', 'Constância', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4787424, -8.3379907, 'OPENSTREETMAP', 'OSM:WAY:985691067',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:985691067')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Constância'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Desporto'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4787424)) <= 0.00070
               AND ABS(v.longitude - (-8.3379907)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:389860803 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis', 'Constância', 'Santarém', NULL,
       'Ténis', 'Campo desportivo', NULL,
       39.4775513, -8.3414800, 'OPENSTREETMAP', 'OSM:WAY:389860803',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:389860803')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Constância'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4775513)) <= 0.00070
               AND ABS(v.longitude - (-8.3414800)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:734443183 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futsal', 'Constância', 'Santarém', NULL,
       'Futebol;Basquetebol', 'Campo desportivo', NULL,
       39.4823181, -8.3377769, 'OPENSTREETMAP', 'OSM:WAY:734443183',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:734443183')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Constância'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futsal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4823181)) <= 0.00070
               AND ABS(v.longitude - (-8.3377769)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:297388056 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Constância', 'Constância', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.4819085, -8.3369850, 'OPENSTREETMAP', 'OSM:WAY:297388056',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:297388056')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Constância'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Constância'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4819085)) <= 0.00070
               AND ABS(v.longitude - (-8.3369850)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:389860727 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Volei de Praia', 'Constância', 'Santarém', NULL,
       'Voleibol', 'Campo desportivo', NULL,
       39.4758794, -8.3411300, 'OPENSTREETMAP', 'OSM:WAY:389860727',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:389860727')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Constância'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Volei de Praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4758794)) <= 0.00070
               AND ABS(v.longitude - (-8.3411300)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:865766958 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Águias da Lamarosa', 'Coruche', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.0702456, -8.4646687, 'OPENSTREETMAP', 'OSM:WAY:865766958',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:865766958')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coruche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Águias da Lamarosa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0702456)) <= 0.00070
               AND ABS(v.longitude - (-8.4646687)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:127650417 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Barroca', 'Coruche', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9864103, -8.2814014, 'OPENSTREETMAP', 'OSM:WAY:127650417',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:127650417')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coruche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Barroca'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9864103)) <= 0.00070
               AND ABS(v.longitude - (-8.2814014)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:728912458 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Professor José Peseiro', 'Coruche', 'Santarém', NULL,
       'Futebol', 'Estádio', NULL,
       38.9621787, -8.5316155, 'OPENSTREETMAP', 'OSM:WAY:728912458',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:728912458')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coruche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Professor José Peseiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9621787)) <= 0.00070
               AND ABS(v.longitude - (-8.5316155)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:290211359 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fajardense', 'Coruche', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9573467, -8.5927815, 'OPENSTREETMAP', 'OSM:WAY:290211359',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:290211359')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coruche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fajardense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9573467)) <= 0.00070
               AND ABS(v.longitude - (-8.5927815)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136929848 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Grupo Desportivo O Coruchense', 'Coruche', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.9619382, -8.5316806, 'OPENSTREETMAP', 'OSM:WAY:136929848',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136929848')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coruche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Grupo Desportivo O Coruchense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.9619382)) <= 0.00070
               AND ABS(v.longitude - (-8.5316806)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:251386872 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sporting Clube Santanense', 'Coruche', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       38.8772149, -8.4104092, 'OPENSTREETMAP', 'OSM:WAY:251386872',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:251386872')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Coruche'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sporting Clube Santanense'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (38.8772149)) <= 0.00070
               AND ABS(v.longitude - (-8.4104092)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1492916842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo do Bonito', 'Entroncamento', 'Santarém', NULL,
       'Futebol;Ténis;Basquetebol;Atletismo', 'Complexo desportivo', NULL,
       39.4741156, -8.4669265, 'OPENSTREETMAP', 'OSM:WAY:1492916842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1492916842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Entroncamento'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo do Bonito'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4741156)) <= 0.00070
               AND ABS(v.longitude - (-8.4669265)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1550390200 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Padel Entre-linhas', 'Entroncamento', 'Santarém', NULL,
       'Padel', 'Campo desportivo', NULL,
       39.4689156, -8.4673824, 'OPENSTREETMAP', 'OSM:WAY:1550390200',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1550390200')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Entroncamento'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Padel Entre-linhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4689156)) <= 0.00070
               AND ABS(v.longitude - (-8.4673824)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:164125555 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais do Entroncamento', 'Entroncamento', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.4771456, -8.4658987, 'OPENSTREETMAP', 'OSM:WAY:164125555',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:164125555')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Entroncamento'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais do Entroncamento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4771456)) <= 0.00070
               AND ABS(v.longitude - (-8.4658987)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825282471 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista simplificada - CM Entroncamento', 'Entroncamento', 'Santarém', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.4751986, -8.4676216, 'OPENSTREETMAP', 'OSM:WAY:825282471',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825282471')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Entroncamento'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista simplificada - CM Entroncamento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4751986)) <= 0.00070
               AND ABS(v.longitude - (-8.4676216)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:164124935 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'União Futebol Entroncamento', 'Entroncamento', 'Santarém', NULL,
       'Hóquei', 'Complexo desportivo', NULL,
       39.4595496, -8.4732803, 'OPENSTREETMAP', 'OSM:WAY:164124935',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:164124935')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Entroncamento'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('União Futebol Entroncamento'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4595496)) <= 0.00070
               AND ABS(v.longitude - (-8.4732803)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169932416 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas', 'Ferreira do Zêzere', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.6951576, -8.2870287, 'OPENSTREETMAP', 'OSM:WAY:1169932416',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169932416')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ferreira do Zêzere'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6951576)) <= 0.00070
               AND ABS(v.longitude - (-8.2870287)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:477277959 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal da Golegã', 'Golegã', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.4022150, -8.4901862, 'OPENSTREETMAP', 'OSM:WAY:477277959',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:477277959')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Golegã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal da Golegã'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4022150)) <= 0.00070
               AND ABS(v.longitude - (-8.4901862)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:137256976 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cobertas', 'Golegã', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.4024931, -8.4907808, 'OPENSTREETMAP', 'OSM:WAY:137256976',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:137256976')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Golegã'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4024931)) <= 0.00070
               AND ABS(v.longitude - (-8.4907808)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:747165558 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Basket e futebol', 'Mação', 'Santarém', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       39.5545557, -7.9930844, 'OPENSTREETMAP', 'OSM:WAY:747165558',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:747165558')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mação'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Basket e futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5545557)) <= 0.00070
               AND ABS(v.longitude - (-7.9930844)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153890277 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal', 'Mação', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5498340, -8.0024393, 'OPENSTREETMAP', 'OSM:WAY:153890277',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153890277')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mação'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5498340)) <= 0.00070
               AND ABS(v.longitude - (-8.0024393)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:369274043 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Fluvial', 'Mação', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.7088483, -8.0139188, 'OPENSTREETMAP', 'OSM:WAY:369274043',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:369274043')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mação'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Fluvial'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7088483)) <= 0.00070
               AND ABS(v.longitude - (-8.0139188)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:153890352 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Cobertas', 'Mação', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.5629342, -8.0001546, 'OPENSTREETMAP', 'OSM:WAY:153890352',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:153890352')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mação'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5629342)) <= 0.00070
               AND ABS(v.longitude - (-8.0001546)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:259241295 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Associação Desportiva Recreativa Cultural Vasco da Gama', 'Ourém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5809961, -8.6376091, 'OPENSTREETMAP', 'OSM:WAY:259241295',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:259241295')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Associação Desportiva Recreativa Cultural Vasco da Gama'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5809961)) <= 0.00070
               AND ABS(v.longitude - (-8.6376091)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:292968280 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Urqueira', 'Ourém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7307691, -8.6013104, 'OPENSTREETMAP', 'OSM:WAY:292968280',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:292968280')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Urqueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7307691)) <= 0.00070
               AND ABS(v.longitude - (-8.6013104)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1539512707 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo do Operário', 'Ourém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.6358851, -8.5713944, 'OPENSTREETMAP', 'OSM:WAY:1539512707',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1539512707')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo do Operário'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6358851)) <= 0.00070
               AND ABS(v.longitude - (-8.5713944)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:122858552 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo dos Cepos', 'Ourém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7623461, -8.5677929, 'OPENSTREETMAP', 'OSM:WAY:122858552',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:122858552')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo dos Cepos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7623461)) <= 0.00070
               AND ABS(v.longitude - (-8.5677929)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:237369929 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo das Piscinas Municipais de Ourém', 'Ourém', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.6536279, -8.5766548, 'OPENSTREETMAP', 'OSM:WAY:237369929',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:237369929')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo das Piscinas Municipais de Ourém'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6536279)) <= 0.00070
               AND ABS(v.longitude - (-8.5766548)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1088028018 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio das Barreiras', 'Ourém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.7203515, -8.6441427, 'OPENSTREETMAP', 'OSM:WAY:1088028018',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1088028018')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio das Barreiras'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.7203515)) <= 0.00070
               AND ABS(v.longitude - (-8.6441427)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1293855862 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio João Paulo II', 'Ourém', 'Santarém', NULL,
       'Futebol', 'Estádio', NULL,
       39.6260533, -8.6630684, 'OPENSTREETMAP', 'OSM:WAY:1293855862',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1293855862')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio João Paulo II'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6260533)) <= 0.00070
               AND ABS(v.longitude - (-8.6630684)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:168717886 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Fátima (Papa Francisco)', 'Ourém', 'Santarém', NULL,
       'Futebol;Atletismo', 'Estádio', NULL,
       39.6033510, -8.6634672, 'OPENSTREETMAP', 'OSM:WAY:168717886',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:168717886')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Fátima (Papa Francisco)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6033510)) <= 0.00070
               AND ABS(v.longitude - (-8.6634672)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:10243492074 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Vila Fit', 'Ourém', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.6536197, -8.5909975, 'OPENSTREETMAP', 'OSM:NODE:10243492074',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:10243492074')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Ourém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Vila Fit'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6536197)) <= 0.00070
               AND ABS(v.longitude - (-8.5909975)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:246775612 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol Comendador António Cardoso dos Santos Loureiro', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2498899, -8.9020372, 'OPENSTREETMAP', 'OSM:WAY:246775612',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:246775612')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol Comendador António Cardoso dos Santos Loureiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2498899)) <= 0.00070
               AND ABS(v.longitude - (-8.9020372)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:246122765 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Ribeira de São João', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2876550, -8.9007087, 'OPENSTREETMAP', 'OSM:WAY:246122765',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:246122765')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Ribeira de São João'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2876550)) <= 0.00070
               AND ABS(v.longitude - (-8.9007087)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142892428 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos nº 2', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3431409, -8.9358328, 'OPENSTREETMAP', 'OSM:WAY:142892428',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142892428')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos nº 2'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3431409)) <= 0.00070
               AND ABS(v.longitude - (-8.9358328)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142892555 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Treinos nº 3', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3432147, -8.9350148, 'OPENSTREETMAP', 'OSM:WAY:142892555',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142892555')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Treinos nº 3'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3432147)) <= 0.00070
               AND ABS(v.longitude - (-8.9350148)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142896889 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol 7 - Relvado Sintético', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3423116, -8.9334218, 'OPENSTREETMAP', 'OSM:WAY:142896889',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142896889')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol 7 - Relvado Sintético'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3423116)) <= 0.00070
               AND ABS(v.longitude - (-8.9334218)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:455315644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Futebol de Praia', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3417101, -8.9341881, 'OPENSTREETMAP', 'OSM:WAY:455315644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:455315644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Futebol de Praia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3417101)) <= 0.00070
               AND ABS(v.longitude - (-8.9341881)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:455482768 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Treino', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3422436, -8.9342533, 'OPENSTREETMAP', 'OSM:WAY:455482768',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:455482768')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Treino'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3422436)) <= 0.00070
               AND ABS(v.longitude - (-8.9342533)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142899428 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Treinos Basketball', 'Rio Maior', 'Santarém', NULL,
       'Basquetebol', 'Campo desportivo', NULL,
       39.3426288, -8.9335432, 'OPENSTREETMAP', 'OSM:WAY:142899428',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142899428')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Treinos Basketball'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3426288)) <= 0.00070
               AND ABS(v.longitude - (-8.9335432)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142899424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Treinos Futebol 5', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3418060, -8.9338299, 'OPENSTREETMAP', 'OSM:WAY:142899424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142899424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Treinos Futebol 5'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3418060)) <= 0.00070
               AND ABS(v.longitude - (-8.9338299)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142891173 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Estágios e Formação Desportiva', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.3418769, -8.9346654, 'OPENSTREETMAP', 'OSM:WAY:142891173',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142891173')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Estágios e Formação Desportiva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3418769)) <= 0.00070
               AND ABS(v.longitude - (-8.9346654)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240175899 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Golden Eagle Golf Course', 'Rio Maior', 'Santarém', NULL,
       'Golfe', 'Campo de golfe', NULL,
       39.2598275, -8.9230403, 'OPENSTREETMAP', 'OSM:WAY:240175899',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240175899')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Golden Eagle Golf Course'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2598275)) <= 0.00070
               AND ABS(v.longitude - (-8.9230403)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142891172 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Gimnodesportivo', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.3424694, -8.9364840, 'OPENSTREETMAP', 'OSM:WAY:142891172',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142891172')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Gimnodesportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3424694)) <= 0.00070
               AND ABS(v.longitude - (-8.9364840)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142896890 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Polidesportivo', 'Rio Maior', 'Santarém', NULL,
       'Basquetebol', 'Complexo desportivo', NULL,
       39.3431822, -8.9342299, 'OPENSTREETMAP', 'OSM:WAY:142896890',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142896890')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Polidesportivo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3431822)) <= 0.00070
               AND ABS(v.longitude - (-8.9342299)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:156453367 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Rio Maior', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.3439919, -8.9371992, 'OPENSTREETMAP', 'OSM:WAY:156453367',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:156453367')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3439919)) <= 0.00070
               AND ABS(v.longitude - (-8.9371992)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:825282484 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista simplificada Rio Maior', 'Rio Maior', 'Santarém', NULL,
       'Corrida', 'Pista desportiva', NULL,
       39.3422845, -8.9333427, 'OPENSTREETMAP', 'OSM:WAY:825282484',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:825282484')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista simplificada Rio Maior'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3422845)) <= 0.00070
               AND ABS(v.longitude - (-8.9333427)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:885596162 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'pool', 'Rio Maior', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.3139116, -8.8917545, 'OPENSTREETMAP', 'OSM:WAY:885596162',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:885596162')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('pool'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3139116)) <= 0.00070
               AND ABS(v.longitude - (-8.8917545)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:240488414 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'U.D. de Malaqueijo', 'Rio Maior', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.2919221, -8.8290742, 'OPENSTREETMAP', 'OSM:WAY:240488414',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:240488414')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Rio Maior'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('U.D. de Malaqueijo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2919221)) <= 0.00070
               AND ABS(v.longitude - (-8.8290742)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:5807324797 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ginásio Scorpus', 'Salvaterra de Magos', 'Santarém', NULL,
       'Halterofilismo;Ginásio', 'Ginásio', NULL,
       39.0223484, -8.7927725, 'OPENSTREETMAP', 'OSM:NODE:5807324797',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:5807324797')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Salvaterra de Magos'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ginásio Scorpus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.0223484)) <= 0.00070
               AND ABS(v.longitude - (-8.7927725)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1496680425 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT '10 por 20 Padel', 'Santarém', 'Santarém', NULL,
       'Padel', 'Campo desportivo', NULL,
       39.2425054, -8.6966464, 'OPENSTREETMAP', 'OSM:WAY:1496680425',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1496680425')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('10 por 20 Padel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2425054)) <= 0.00070
               AND ABS(v.longitude - (-8.6966464)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:187385004 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Azenha', 'Santarém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4443439, -8.7445421, 'OPENSTREETMAP', 'OSM:WAY:187385004',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:187385004')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Azenha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4443439)) <= 0.00070
               AND ABS(v.longitude - (-8.7445421)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:154708727 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Vale de Figueira', 'Santarém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.3094477, -8.6313217, 'OPENSTREETMAP', 'OSM:WAY:154708727',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:154708727')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Vale de Figueira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.3094477)) <= 0.00070
               AND ABS(v.longitude - (-8.6313217)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:288386710 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de jogos de Valverde', 'Santarém', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4556521, -8.8470521, 'OPENSTREETMAP', 'OSM:WAY:288386710',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:288386710')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de jogos de Valverde'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4556521)) <= 0.00070
               AND ABS(v.longitude - (-8.8470521)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1079862390 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Fitness Factory', 'Santarém', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.2423290, -8.6987187, 'OPENSTREETMAP', 'OSM:WAY:1079862390',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1079862390')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Fitness Factory'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2423290)) <= 0.00070
               AND ABS(v.longitude - (-8.6987187)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1172748137 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Munícipais Sacapeito', 'Santarém', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.2250618, -8.6885599, 'OPENSTREETMAP', 'OSM:WAY:1172748137',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1172748137')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Munícipais Sacapeito'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2250618)) <= 0.00070
               AND ABS(v.longitude - (-8.6885599)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1533887058 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Santarém Cricket Ground (SCG)', 'Santarém', 'Santarém', NULL,
       'Cricket', 'Campo desportivo', NULL,
       39.2335441, -8.8486810, 'OPENSTREETMAP', 'OSM:WAY:1533887058',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1533887058')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Santarém'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Santarém Cricket Ground (SCG)'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.2335441)) <= 0.00070
               AND ABS(v.longitude - (-8.8486810)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:238671537 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Alcaravela', 'Sardoal', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5634490, -8.1215649, 'OPENSTREETMAP', 'OSM:WAY:238671537',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:238671537')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sardoal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Alcaravela'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5634490)) <= 0.00070
               AND ABS(v.longitude - (-8.1215649)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1126193674 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos da A.C.R.Linhaceira', 'Tomar', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.5237512, -8.3863847, 'OPENSTREETMAP', 'OSM:WAY:1126193674',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1126193674')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos da A.C.R.Linhaceira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5237512)) <= 0.00070
               AND ABS(v.longitude - (-8.3863847)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:172453319 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal de Tomar', 'Tomar', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.6064047, -8.3991736, 'OPENSTREETMAP', 'OSM:WAY:172453319',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:172453319')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo Municipal de Tomar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6064047)) <= 0.00070
               AND ABS(v.longitude - (-8.3991736)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:289863541 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal Vasco Jacob', 'Tomar', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.6074038, -8.4113291, 'OPENSTREETMAP', 'OSM:WAY:289863541',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:289863541')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal Vasco Jacob'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.6074038)) <= 0.00070
               AND ABS(v.longitude - (-8.4113291)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:289521195 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark Tomar', 'Tomar', 'Santarém', NULL,
       'Skate', 'Campo desportivo', NULL,
       39.5987768, -8.4126630, 'OPENSTREETMAP', 'OSM:WAY:289521195',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:289521195')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Tomar'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark Tomar'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.5987768)) <= 0.00070
               AND ABS(v.longitude - (-8.4126630)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:222531666 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Meia Via', 'Torres Novas', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4783019, -8.4952661, 'OPENSTREETMAP', 'OSM:WAY:222531666',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:222531666')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Meia Via'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4783019)) <= 0.00070
               AND ABS(v.longitude - (-8.4952661)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1152782008 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos dos Casais Castelos', 'Torres Novas', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4570943, -8.4982851, 'OPENSTREETMAP', 'OSM:WAY:1152782008',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1152782008')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos dos Casais Castelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4570943)) <= 0.00070
               AND ABS(v.longitude - (-8.4982851)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:185195701 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Coronel Mário Cunha', 'Torres Novas', 'Santarém', NULL,
       'Futebol', 'Estádio', NULL,
       39.4388997, -8.5134353, 'OPENSTREETMAP', 'OSM:WAY:185195701',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:185195701')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Coronel Mário Cunha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4388997)) <= 0.00070
               AND ABS(v.longitude - (-8.5134353)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:136775572 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Doutor Alves Vieira', 'Torres Novas', 'Santarém', NULL,
       'Futebol', 'Estádio', NULL,
       39.4882354, -8.5446593, 'OPENSTREETMAP', 'OSM:WAY:136775572',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:136775572')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Doutor Alves Vieira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4882354)) <= 0.00070
               AND ABS(v.longitude - (-8.5446593)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:163806725 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Ténis Municipal', 'Torres Novas', 'Santarém', NULL,
       'Ténis', 'Complexo desportivo', NULL,
       39.4907617, -8.5477823, 'OPENSTREETMAP', 'OSM:WAY:163806725',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:163806725')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Ténis Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4907617)) <= 0.00070
               AND ABS(v.longitude - (-8.5477823)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:143401183 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal de Ginástica', 'Torres Novas', 'Santarém', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       39.4881241, -8.5461723, 'OPENSTREETMAP', 'OSM:WAY:143401183',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:143401183')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Municipal de Ginástica'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4881241)) <= 0.00070
               AND ABS(v.longitude - (-8.5461723)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1172508619 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Fernando Cunha', 'Torres Novas', 'Santarém', NULL,
       'Natação', 'Piscina', NULL,
       39.4815137, -8.5389417, 'OPENSTREETMAP', 'OSM:WAY:1172508619',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1172508619')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Fernando Cunha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4815137)) <= 0.00070
               AND ABS(v.longitude - (-8.5389417)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:675729074 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Recinto desportivo do 2º Bairro da Sopovo', 'Torres Novas', 'Santarém', NULL,
       'Ténis', 'Campo desportivo', NULL,
       39.4330850, -8.5101189, 'OPENSTREETMAP', 'OSM:WAY:675729074',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:675729074')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Recinto desportivo do 2º Bairro da Sopovo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4330850)) <= 0.00070
               AND ABS(v.longitude - (-8.5101189)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:6816410543 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Wiva', 'Torres Novas', 'Santarém', NULL,
       'Ginásio', 'Ginásio', NULL,
       39.4640306, -8.5318040, 'OPENSTREETMAP', 'OSM:NODE:6816410543',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:6816410543')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Torres Novas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Wiva'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4640306)) <= 0.00070
               AND ABS(v.longitude - (-8.5318040)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:164210440 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Atalaia', 'Vila Nova da Barquinha', 'Santarém', NULL,
       'Futebol', 'Campo desportivo', NULL,
       39.4735510, -8.4566769, 'OPENSTREETMAP', 'OSM:WAY:164210440',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:164210440')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova da Barquinha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Atalaia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4735510)) <= 0.00070
               AND ABS(v.longitude - (-8.4566769)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:2504756635 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Jogos de Moita do Norte', 'Vila Nova da Barquinha', 'Santarém', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       39.4714924, -8.4396397, 'OPENSTREETMAP', 'OSM:NODE:2504756635',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:2504756635')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova da Barquinha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Jogos de Moita do Norte'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4714924)) <= 0.00070
               AND ABS(v.longitude - (-8.4396397)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1195943200 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Infantil', 'Vila Nova da Barquinha', 'Santarém', NULL,
       'Natação', 'Complexo desportivo', NULL,
       39.4709213, -8.4388827, 'OPENSTREETMAP', 'OSM:WAY:1195943200',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1195943200')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova da Barquinha'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Infantil'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (39.4709213)) <= 0.00070
               AND ABS(v.longitude - (-8.4388827)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
