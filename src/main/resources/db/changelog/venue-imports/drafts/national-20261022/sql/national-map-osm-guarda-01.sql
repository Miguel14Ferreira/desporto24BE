-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:823590984 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Aguiar da Beira', 'Aguiar da Beira', 'Guarda', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.8129948, -7.5339183, 'OPENSTREETMAP', 'OSM:WAY:823590984',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823590984')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Aguiar da Beira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Aguiar da Beira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8129948)) <= 0.00070
               AND ABS(v.longitude - (-7.5339183)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169728407 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'ADCS Aldeia de São Sebastião', 'Almeida', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.6022083, -6.8861216, 'OPENSTREETMAP', 'OSM:WAY:1169728407',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169728407')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeida'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('ADCS Aldeia de São Sebastião'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6022083)) <= 0.00070
               AND ABS(v.longitude - (-6.8861216)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:414028314 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo José Júlio Balcão', 'Almeida', 'Guarda', NULL,
       'Futebol', 'Estádio', NULL,
       40.6033075, -6.8288880, 'OPENSTREETMAP', 'OSM:WAY:414028314',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:414028314')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeida'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo José Júlio Balcão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6033075)) <= 0.00070
               AND ABS(v.longitude - (-6.8288880)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:330016945 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Almeida', 'Almeida', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.7130084, -6.8982708, 'OPENSTREETMAP', 'OSM:WAY:330016945',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:330016945')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeida'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal de Almeida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7130084)) <= 0.00070
               AND ABS(v.longitude - (-6.8982708)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:414028315 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Vilar Formoso', 'Almeida', 'Guarda', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.6009880, -6.8354175, 'OPENSTREETMAP', 'OSM:WAY:414028315',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:414028315')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeida'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Vilar Formoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6009880)) <= 0.00070
               AND ABS(v.longitude - (-6.8354175)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:414028327 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Almeida', 'Almeida', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.7166676, -6.8991112, 'OPENSTREETMAP', 'OSM:WAY:414028327',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:414028327')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeida'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Almeida'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7166676)) <= 0.00070
               AND ABS(v.longitude - (-6.8991112)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:413970505 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Termas da Fonte Santa', 'Almeida', 'Guarda', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.7458559, -6.9495138, 'OPENSTREETMAP', 'OSM:WAY:413970505',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:413970505')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Almeida'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Termas da Fonte Santa'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7458559)) <= 0.00070
               AND ABS(v.longitude - (-6.9495138)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:987954924 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol de Açores', 'Celorico da Beira', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6419890, -7.3017954, 'OPENSTREETMAP', 'OSM:WAY:987954924',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:987954924')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico da Beira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol de Açores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6419890)) <= 0.00070
               AND ABS(v.longitude - (-7.3017954)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1160867896 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais', 'Celorico da Beira', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.6348050, -7.3919587, 'OPENSTREETMAP', 'OSM:WAY:1160867896',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1160867896')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico da Beira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6348050)) <= 0.00070
               AND ABS(v.longitude - (-7.3919587)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:987955582 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Açores', 'Celorico da Beira', 'Guarda', NULL,
       'Ténis;Futsal', 'Campo desportivo', NULL,
       40.6425266, -7.3023111, 'OPENSTREETMAP', 'OSM:WAY:987955582',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:987955582')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Celorico da Beira'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Açores'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6425266)) <= 0.00070
               AND ABS(v.longitude - (-7.3023111)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:633899211 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Lazer', 'Figueira de Castelo Rodrigo', 'Guarda', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9500705, -7.0537551, 'OPENSTREETMAP', 'OSM:WAY:633899211',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:633899211')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira de Castelo Rodrigo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Lazer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9500705)) <= 0.00070
               AND ABS(v.longitude - (-7.0537551)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:385818750 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas de Castelo Rodrigo', 'Figueira de Castelo Rodrigo', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.8705412, -6.9654850, 'OPENSTREETMAP', 'OSM:WAY:385818750',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:385818750')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Figueira de Castelo Rodrigo'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas de Castelo Rodrigo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.8705412)) <= 0.00070
               AND ABS(v.longitude - (-6.9654850)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:236427203 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo da Cruzinha', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6328002, -7.4972510, 'OPENSTREETMAP', 'OSM:WAY:236427203',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:236427203')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo da Cruzinha'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6328002)) <= 0.00070
               AND ABS(v.longitude - (-7.4972510)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:325166644 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Doutor Moreira da Cruz', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Estádio', NULL,
       40.6215885, -7.5461739, 'OPENSTREETMAP', 'OSM:WAY:325166644',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:325166644')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Doutor Moreira da Cruz'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6215885)) <= 0.00070
               AND ABS(v.longitude - (-7.5461739)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1355929254 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estadio Municipal', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6228584, -7.5468539, 'OPENSTREETMAP', 'OSM:WAY:1355929254',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1355929254')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estadio Municipal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6228584)) <= 0.00070
               AND ABS(v.longitude - (-7.5468539)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:163824339 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Serra da Esgalhada', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Estádio', NULL,
       40.6228352, -7.5471036, 'OPENSTREETMAP', 'OSM:WAY:163824339',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:163824339')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Serra da Esgalhada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6228352)) <= 0.00070
               AND ABS(v.longitude - (-7.5471036)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:273302117 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Infias', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6296320, -7.5405680, 'OPENSTREETMAP', 'OSM:WAY:273302117',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:273302117')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Infias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6296320)) <= 0.00070
               AND ABS(v.longitude - (-7.5405680)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:384084807 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo outdoor', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6411986, -7.5262936, 'OPENSTREETMAP', 'OSM:WAY:384084807',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:384084807')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo outdoor'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6411986)) <= 0.00070
               AND ABS(v.longitude - (-7.5262936)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1355929253 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sintetico', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6142254, -7.5471905, 'OPENSTREETMAP', 'OSM:WAY:1355929253',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1355929253')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sintetico'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6142254)) <= 0.00070
               AND ABS(v.longitude - (-7.5471905)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:211545433 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sobreirada', 'Fornos de Algodres', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.6934271, -7.4556046, 'OPENSTREETMAP', 'OSM:WAY:211545433',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:211545433')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Fornos de Algodres'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sobreirada'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.6934271)) <= 0.00070
               AND ABS(v.longitude - (-7.4556046)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:306090061 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Dias Gonçalves', 'Gouveia', 'Guarda', NULL,
       'Futebol', 'Estádio', NULL,
       40.5220905, -7.6074091, 'OPENSTREETMAP', 'OSM:WAY:306090061',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:306090061')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Dias Gonçalves'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5220905)) <= 0.00070
               AND ABS(v.longitude - (-7.6074091)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1001490404 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal do Farvão', 'Gouveia', 'Guarda', NULL,
       'Futebol', 'Estádio', NULL,
       40.4858150, -7.5945189, 'OPENSTREETMAP', 'OSM:WAY:1001490404',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1001490404')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal do Farvão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4858150)) <= 0.00070
               AND ABS(v.longitude - (-7.5945189)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:142157855 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Farvão', 'Gouveia', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.4859506, -7.5946819, 'OPENSTREETMAP', 'OSM:WAY:142157855',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:142157855')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Farvão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4859506)) <= 0.00070
               AND ABS(v.longitude - (-7.5946819)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:453857770 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Parque de Lazer "O Poção"', 'Gouveia', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.5041552, -7.5186310, 'OPENSTREETMAP', 'OSM:WAY:453857770',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:453857770')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Parque de Lazer "O Poção"'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5041552)) <= 0.00070
               AND ABS(v.longitude - (-7.5186310)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:300422568 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina de Arcozelo da Serra', 'Gouveia', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.5433893, -7.6248455, 'OPENSTREETMAP', 'OSM:WAY:300422568',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:300422568')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina de Arcozelo da Serra'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5433893)) <= 0.00070
               AND ABS(v.longitude - (-7.6248455)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:682501032 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Pública de Aldeias', 'Gouveia', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.4667665, -7.5970721, 'OPENSTREETMAP', 'OSM:WAY:682501032',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:682501032')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Pública de Aldeias'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4667665)) <= 0.00070
               AND ABS(v.longitude - (-7.5970721)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:516100596 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Private Pool', 'Gouveia', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.4932018, -7.6186710, 'OPENSTREETMAP', 'OSM:WAY:516100596',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:516100596')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Private Pool'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4932018)) <= 0.00070
               AND ABS(v.longitude - (-7.6186710)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1187059630 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de Nespereira', 'Gouveia', 'Guarda', NULL,
       'Futebol;Futsal;Ténis', 'Campo desportivo', NULL,
       40.5145511, -7.6074194, 'OPENSTREETMAP', 'OSM:WAY:1187059630',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1187059630')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Gouveia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de Nespereira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5145511)) <= 0.00070
               AND ABS(v.longitude - (-7.6074194)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4419343757 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Bemequer', 'Guarda', 'Guarda', NULL,
       'Ginásio', 'Ginásio', NULL,
       40.5390952, -7.2783580, 'OPENSTREETMAP', 'OSM:NODE:4419343757',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4419343757')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guarda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Bemequer'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5390952)) <= 0.00070
               AND ABS(v.longitude - (-7.2783580)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:120356192 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol do Zambito', 'Guarda', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5439126, -7.2828665, 'OPENSTREETMAP', 'OSM:WAY:120356192',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:120356192')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guarda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Futebol do Zambito'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5439126)) <= 0.00070
               AND ABS(v.longitude - (-7.2828665)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:681407345 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Polidesportivo das Lameirinhas', 'Guarda', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.5339930, -7.2795849, 'OPENSTREETMAP', 'OSM:WAY:681407345',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:681407345')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guarda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo Polidesportivo das Lameirinhas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5339930)) <= 0.00070
               AND ABS(v.longitude - (-7.2795849)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:4232281444 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'FFitness', 'Guarda', 'Guarda', NULL,
       'Ginástica', 'Complexo desportivo', NULL,
       40.5416213, -7.2631833, 'OPENSTREETMAP', 'OSM:NODE:4232281444',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:4232281444')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guarda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('FFitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5416213)) <= 0.00070
               AND ABS(v.longitude - (-7.2631833)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:162959945 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina da Universidade Politécnica da Guarda.', 'Guarda', 'Guarda', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.5406314, -7.2843916, 'OPENSTREETMAP', 'OSM:WAY:162959945',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:162959945')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Guarda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina da Universidade Politécnica da Guarda.'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.5406314)) <= 0.00070
               AND ABS(v.longitude - (-7.2843916)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:281877648 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Manteigas', 'Manteigas', 'Guarda', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.4015379, -7.5406587, 'OPENSTREETMAP', 'OSM:WAY:281877648',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:281877648')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Manteigas'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Municipal de Manteigas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4015379)) <= 0.00070
               AND ABS(v.longitude - (-7.5406587)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:302403746 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo das Pisicinas Municipais', 'Mêda', 'Guarda', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.9689020, -7.2595194, 'OPENSTREETMAP', 'OSM:WAY:302403746',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:302403746')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mêda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Complexo Desportivo das Pisicinas Municipais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9689020)) <= 0.00070
               AND ABS(v.longitude - (-7.2595194)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:302947909 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Doutor Auguto César de Carvalho', 'Mêda', 'Guarda', NULL,
       'Futebol', 'Estádio', NULL,
       40.9670351, -7.2598185, 'OPENSTREETMAP', 'OSM:WAY:302947909',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:302947909')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mêda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio Municipal Doutor Auguto César de Carvalho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9670351)) <= 0.00070
               AND ABS(v.longitude - (-7.2598185)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:419969419 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina descoberta', 'Mêda', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.9690542, -7.2591833, 'OPENSTREETMAP', 'OSM:WAY:419969419',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:419969419')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mêda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina descoberta'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9690542)) <= 0.00070
               AND ABS(v.longitude - (-7.2591833)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169726186 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Cobertas', 'Mêda', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.9685539, -7.2592376, 'OPENSTREETMAP', 'OSM:WAY:1169726186',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169726186')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mêda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9685539)) <= 0.00070
               AND ABS(v.longitude - (-7.2592376)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:419969424 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Ringue de futebol', 'Mêda', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.9727575, -7.2655223, 'OPENSTREETMAP', 'OSM:WAY:419969424',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:419969424')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Mêda'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Ringue de futebol'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.9727575)) <= 0.00070
               AND ABS(v.longitude - (-7.2655223)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:229651969 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Ténis da Casa do Povo de Pinhel', 'Pinhel', 'Guarda', NULL,
       'Ténis', 'Campo desportivo', NULL,
       40.7734609, -7.0688997, 'OPENSTREETMAP', 'OSM:WAY:229651969',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:229651969')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pinhel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Campo de Ténis da Casa do Povo de Pinhel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7734609)) <= 0.00070
               AND ABS(v.longitude - (-7.0688997)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1055054812 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Pinhel', 'Pinhel', 'Guarda', NULL,
       'Natação', 'Complexo desportivo', NULL,
       40.7721035, -7.0776499, 'OPENSTREETMAP', 'OSM:WAY:1055054812',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1055054812')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Pinhel'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Pinhel'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7721035)) <= 0.00070
               AND ABS(v.longitude - (-7.0776499)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14115545667 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas', 'Sabugal', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.3212713, -6.8452771, 'OPENSTREETMAP', 'OSM:NODE:14115545667',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14115545667')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabugal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3212713)) <= 0.00070
               AND ABS(v.longitude - (-6.8452771)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:823981539 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo do Sabugal', 'Sabugal', 'Guarda', NULL,
       'Corrida', 'Pista desportiva', NULL,
       40.3573349, -7.0774432, 'OPENSTREETMAP', 'OSM:WAY:823981539',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:823981539')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Sabugal'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo do Sabugal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.3573349)) <= 0.00070
               AND ABS(v.longitude - (-7.0774432)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:1644275875 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Desportivo de Seia', 'Seia', 'Guarda', NULL,
       'Futebol', 'Complexo desportivo', NULL,
       40.4152471, -7.7020018, 'OPENSTREETMAP', 'OSM:NODE:1644275875',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:1644275875')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro Desportivo de Seia'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.4152471)) <= 0.00070
               AND ABS(v.longitude - (-7.7020018)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:681504278 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio do Ramalheiro', 'Seia', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       40.2699083, -7.7149242, 'OPENSTREETMAP', 'OSM:WAY:681504278',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:681504278')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Seia'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Estádio do Ramalheiro'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.2699083)) <= 0.00070
               AND ABS(v.longitude - (-7.7149242)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1169734657 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pisão', 'Trancoso', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.7206753, -7.4319356, 'OPENSTREETMAP', 'OSM:WAY:1169734657',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1169734657')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trancoso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pisão'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7206753)) <= 0.00070
               AND ABS(v.longitude - (-7.4319356)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:751080823 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Trancoso', 'Trancoso', 'Guarda', NULL,
       'Natação', 'Piscina', NULL,
       40.7711545, -7.3567619, 'OPENSTREETMAP', 'OSM:WAY:751080823',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:751080823')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Trancoso'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais de Trancoso'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (40.7711545)) <= 0.00070
               AND ABS(v.longitude - (-7.3567619)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:273193527 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro de Alto Rendimento de Remo do Pocinho', 'Vila Nova de Foz Côa', 'Guarda', NULL,
       'Remo', 'Complexo desportivo', NULL,
       41.1307356, -7.1181130, 'OPENSTREETMAP', 'OSM:WAY:273193527',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:273193527')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Foz Côa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Centro de Alto Rendimento de Remo do Pocinho'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.1307356)) <= 0.00070
               AND ABS(v.longitude - (-7.1181130)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:106467421 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'futebol de 11', 'Vila Nova de Foz Côa', 'Guarda', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.0914539, -7.3184838, 'OPENSTREETMAP', 'OSM:WAY:106467421',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:106467421')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Nova de Foz Côa'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('futebol de 11'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.0914539)) <= 0.00070
               AND ABS(v.longitude - (-7.3184838)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
