-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.
-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.
-- OSM:WAY:1164691740 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Testeira', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3523302, -7.7479565, 'OPENSTREETMAP', 'OSM:WAY:1164691740',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164691740')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Testeira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3523302)) <= 0.00070
               AND ABS(v.longitude - (-7.7479565)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297364 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Minicampo de Tuizendes', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2630564, -7.8049698, 'OPENSTREETMAP', 'OSM:WAY:1168297364',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297364')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Minicampo de Tuizendes'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2630564)) <= 0.00070
               AND ABS(v.longitude - (-7.8049698)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:152802672 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Nave dos Desportos', 'Vila Real', 'Vila Real', NULL,
       'Futsal;Basquetebol;Andebol;Voleibol;Ginástica', 'Complexo desportivo', NULL,
       41.2831038, -7.7448867, 'OPENSTREETMAP', 'OSM:WAY:152802672',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:152802672')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Nave dos Desportos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2831038)) <= 0.00070
               AND ABS(v.longitude - (-7.7448867)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11985991220 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Optimum', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3041541, -7.7280469, 'OPENSTREETMAP', 'OSM:NODE:11985991220',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11985991220')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Optimum'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3041541)) <= 0.00070
               AND ABS(v.longitude - (-7.7280469)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164031257 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo Paroquial de Mateus', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Pavilhão desportivo', 'INDOOR',
       41.2993356, -7.7196856, 'OPENSTREETMAP', 'OSM:WAY:1164031257',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164031257')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Desportivo Paroquial de Mateus'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2993356)) <= 0.00070
               AND ABS(v.longitude - (-7.7196856)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182794274 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Fausto Carvalhais', 'Vila Real', 'Vila Real', NULL,
       'Andebol;Futsal;Basquetebol;Voleibol', 'Pavilhão desportivo', 'INDOOR',
       41.3010047, -7.7388666, 'OPENSTREETMAP', 'OSM:WAY:182794274',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182794274')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pavilhão Fausto Carvalhais'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3010047)) <= 0.00070
               AND ABS(v.longitude - (-7.7388666)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182284920 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina do Boque', 'Vila Real', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.3076181, -7.7214775, 'OPENSTREETMAP', 'OSM:WAY:182284920',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182284920')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina do Boque'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3076181)) <= 0.00070
               AND ABS(v.longitude - (-7.7214775)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164481872 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Leoparda', 'Vila Real', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.2985100, -7.8917956, 'OPENSTREETMAP', 'OSM:WAY:1164481872',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164481872')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscina Leoparda'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2985100)) <= 0.00070
               AND ABS(v.longitude - (-7.8917956)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182794268 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas do GCVR', 'Vila Real', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.3029916, -7.7387958, 'OPENSTREETMAP', 'OSM:WAY:182794268',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182794268')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas do GCVR'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3029916)) <= 0.00070
               AND ABS(v.longitude - (-7.7387958)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182794286 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais Cobertas', 'Vila Real', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.3011331, -7.7395319, 'OPENSTREETMAP', 'OSM:WAY:182794286',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182794286')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Piscinas Municipais Cobertas'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3011331)) <= 0.00070
               AND ABS(v.longitude - (-7.7395319)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164691747 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Andrães', 'Vila Real', 'Vila Real', NULL,
       'Corrida', 'Pista desportiva', NULL,
       41.2650489, -7.6862053, 'OPENSTREETMAP', 'OSM:WAY:1164691747',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164691747')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Pista de Atletismo de Andrães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2650489)) <= 0.00070
               AND ABS(v.longitude - (-7.6862053)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:183024880 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo da Araucária', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2940477, -7.7316806, 'OPENSTREETMAP', 'OSM:WAY:183024880',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:183024880')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo da Araucária'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2940477)) <= 0.00070
               AND ABS(v.longitude - (-7.7316806)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182214762 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Almodena', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2962902, -7.7562520, 'OPENSTREETMAP', 'OSM:WAY:182214762',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182214762')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Almodena'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2962902)) <= 0.00070
               AND ABS(v.longitude - (-7.7562520)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802520978 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Montezelos', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3114831, -7.7446437, 'OPENSTREETMAP', 'OSM:WAY:802520978',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802520978')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Montezelos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3114831)) <= 0.00070
               AND ABS(v.longitude - (-7.7446437)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802530569 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de São Mamede', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3079300, -7.7457213, 'OPENSTREETMAP', 'OSM:WAY:802530569',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802530569')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de São Mamede'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3079300)) <= 0.00070
               AND ABS(v.longitude - (-7.7457213)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:901706493 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Vila Marim', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3093037, -7.7758622, 'OPENSTREETMAP', 'OSM:WAY:901706493',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:901706493')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo de Vila Marim'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3093037)) <= 0.00070
               AND ABS(v.longitude - (-7.7758622)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:802311259 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo do Jardim da Carreira', 'Vila Real', 'Vila Real', NULL,
       'Basquetebol;Futsal', 'Campo desportivo', NULL,
       41.3013223, -7.7415161, 'OPENSTREETMAP', 'OSM:WAY:802311259',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:802311259')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo do Jardim da Carreira'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3013223)) <= 0.00070
               AND ABS(v.longitude - (-7.7415161)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:180192577 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo dos Vicentinos', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3080833, -7.7411897, 'OPENSTREETMAP', 'OSM:WAY:180192577',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:180192577')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo dos Vicentinos'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3080833)) <= 0.00070
               AND ABS(v.longitude - (-7.7411897)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:180934372 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Velho da Lage', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.3078431, -7.6967525, 'OPENSTREETMAP', 'OSM:WAY:180934372',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:180934372')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polidesportivo Velho da Lage'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3078431)) <= 0.00070
               AND ABS(v.longitude - (-7.6967525)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:182285707 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polivalente da Estação', 'Vila Real', 'Vila Real', NULL,
       'Futebol;Futsal', 'Campo desportivo', NULL,
       41.2949585, -7.7388927, 'OPENSTREETMAP', 'OSM:WAY:182285707',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:182285707')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polivalente da Estação'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2949585)) <= 0.00070
               AND ABS(v.longitude - (-7.7388927)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:814168478 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polivalente do Parque Florestal', 'Vila Real', 'Vila Real', NULL,
       'Futebol', 'Campo desportivo', NULL,
       41.2994892, -7.7399594, 'OPENSTREETMAP', 'OSM:WAY:814168478',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:814168478')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Polivalente do Parque Florestal'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2994892)) <= 0.00070
               AND ABS(v.longitude - (-7.7399594)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:11496002459 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Powerful - Premium Fitness', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2967944, -7.7484671, 'OPENSTREETMAP', 'OSM:NODE:11496002459',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:11496002459')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Powerful - Premium Fitness'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2967944)) <= 0.00070
               AND ABS(v.longitude - (-7.7484671)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:7667080469 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PT Academy', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2948182, -7.7438409, 'OPENSTREETMAP', 'OSM:NODE:7667080469',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:7667080469')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PT Academy'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2948182)) <= 0.00070
               AND ABS(v.longitude - (-7.7438409)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:9035423351 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'PT+ | Estúdio de Treino', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3103729, -7.7435783, 'OPENSTREETMAP', 'OSM:NODE:9035423351',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:9035423351')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('PT+ | Estúdio de Treino'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3103729)) <= 0.00070
               AND ABS(v.longitude - (-7.7435783)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:12051535842 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Rise - Personal Training', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.2985866, -7.7329880, 'OPENSTREETMAP', 'OSM:NODE:12051535842',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:12051535842')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Rise - Personal Training'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2985866)) <= 0.00070
               AND ABS(v.longitude - (-7.7329880)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:14018967676 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Rocódromo de Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Escalada', 'Complexo desportivo', NULL,
       41.2991717, -7.7374157, 'OPENSTREETMAP', 'OSM:NODE:14018967676',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:14018967676')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Rocódromo de Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2991717)) <= 0.00070
               AND ABS(v.longitude - (-7.7374157)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1168297357 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Sala de Ténis de Mesa de Arrabães', 'Vila Real', 'Vila Real', NULL,
       'Ténis', 'Campo desportivo', 'INDOOR',
       41.2843409, -7.8101428, 'OPENSTREETMAP', 'OSM:WAY:1168297357',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1168297357')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Sala de Ténis de Mesa de Arrabães'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2843409)) <= 0.00070
               AND ABS(v.longitude - (-7.8101428)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1318829047 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Skatepark de Vila Real', 'Vila Real', 'Vila Real', NULL,
       'Skate', 'Campo desportivo', NULL,
       41.2988723, -7.7354510, 'OPENSTREETMAP', 'OSM:WAY:1318829047',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1318829047')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Skatepark de Vila Real'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2988723)) <= 0.00070
               AND ABS(v.longitude - (-7.7354510)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:WAY:1164481873 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Tanque Evaristo', 'Vila Real', 'Vila Real', NULL,
       'Natação', 'Piscina', NULL,
       41.2985183, -7.8909703, 'OPENSTREETMAP', 'OSM:WAY:1164481873',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:WAY:1164481873')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('Tanque Evaristo'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.2985183)) <= 0.00070
               AND ABS(v.longitude - (-7.8909703)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;

-- OSM:NODE:13037299643 (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'We Wonder', 'Vila Real', 'Vila Real', NULL,
       'Ginásio', 'Ginásio', NULL,
       41.3060839, -7.7462685, 'OPENSTREETMAP', 'OSM:NODE:13037299643',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference='OSM:NODE:13037299643')
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM('Vila Real'))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM('We Wonder'))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude - (41.3060839)) <= 0.00070
               AND ABS(v.longitude - (-7.7462685)) <= 0.00100)))
)
ON CONFLICT DO NOTHING;
