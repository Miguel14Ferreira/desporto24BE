-- Jynlo 2026-10-26: FIRST SIX ISLAND VENUE CANDIDATES, DRAFT ONLY.
-- Not included in db.changelog-master.yaml; NEVER applied to production by this PR.
-- Names, postal addresses, sport labels: documentary evidence in the reviewed manifest.
-- All contact numbers and coordinates remain NULL (no invented data).
-- Venue listing does not confer free access, operating status or booking.
-- Live DB duplicate preflight + real restore-tested backup + access review REQUIRED.
-- Source: docs/venue-imports/islands-first-six-official-sources-20261026.json

-- AZ-SM-LARANJEIRAS: Complexo Desportivo das Laranjeiras (Ponta Delgada, Açores)
-- Official evidence URL: https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Complexo Desportivo das Laranjeiras',
  'Ponta Delgada',
  'Açores',
  'Rua José Maria Caetano de Matos, s/n, 9500-116 Ponta Delgada',
  'Atletismo',
  'Complexo desportivo',
  NULL,
  NULL,
  NULL,
  'OFFICIAL',
  'JYNLO:OFFICIAL:AZ-SM-LARANJEIRAS',
  'https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha',
  NULL,
  FALSE,
  FALSE,
  FALSE,
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:AZ-SM-LARANJEIRAS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo Desportivo das Laranjeiras'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Ponta Delgada')))
     OR (LOWER(BTRIM(COALESCE(existing.address, ''))) = LOWER(BTRIM('Rua José Maria Caetano de Matos, s/n, 9500-116 Ponta Delgada'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Ponta Delgada')))
)
ON CONFLICT DO NOTHING;

-- AZ-SM-SIDONIO-SERPA: Pavilhão Desportivo Sidónio Serpa (Ponta Delgada, Açores)
-- Official evidence URL: https://www.cm-pontadelgada.pt/cmpontadelgada/uploads/writer_file/document/1434/plano_municipal_de_juventude_2022_2026_final.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Desportivo Sidónio Serpa',
  'Ponta Delgada',
  'Açores',
  'Rua da Juventude, 9500-211 Ponta Delgada',
  NULL,
  'Pavilhão desportivo',
  'INDOOR',
  NULL,
  NULL,
  'OFFICIAL',
  'JYNLO:OFFICIAL:AZ-SM-SIDONIO-SERPA',
  'https://www.cm-pontadelgada.pt/cmpontadelgada/uploads/writer_file/document/1434/plano_municipal_de_juventude_2022_2026_final.pdf',
  NULL,
  TRUE,
  FALSE,
  FALSE,
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:AZ-SM-SIDONIO-SERPA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo Sidónio Serpa'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Ponta Delgada')))
     OR (LOWER(BTRIM(COALESCE(existing.address, ''))) = LOWER(BTRIM('Rua da Juventude, 9500-211 Ponta Delgada'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Ponta Delgada')))
)
ON CONFLICT DO NOTHING;

-- MD-FUNCHAL-PISCINAS-OLIMPICAS: Complexo de Piscinas Olímpicas do Funchal (Funchal, Madeira)
-- Official evidence URL: https://www.madeira.gov.pt/draescolar/pesquisar/ctl/ReadInformcao/mid/2173/InformacaoId/66482/UnidadeOrganicaId/30/LiveSearch/Levada%20Pico%20Castelo
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Complexo de Piscinas Olímpicas do Funchal',
  'Funchal',
  'Madeira',
  'Beco dos Álamos, Santo António, Funchal',
  'Natação;Polo Aquático',
  'Complexo desportivo',
  NULL,
  NULL,
  NULL,
  'OFFICIAL',
  'JYNLO:OFFICIAL:MD-FUNCHAL-PISCINAS-OLIMPICAS',
  'https://www.madeira.gov.pt/draescolar/pesquisar/ctl/ReadInformcao/mid/2173/InformacaoId/66482/UnidadeOrganicaId/30/LiveSearch/Levada%20Pico%20Castelo',
  NULL,
  FALSE,
  FALSE,
  FALSE,
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:MD-FUNCHAL-PISCINAS-OLIMPICAS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo de Piscinas Olímpicas do Funchal'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Funchal')))
     OR (LOWER(BTRIM(COALESCE(existing.address, ''))) = LOWER(BTRIM('Beco dos Álamos, Santo António, Funchal'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Funchal')))
)
ON CONFLICT DO NOTHING;

-- MD-CAMARA-ESTREITO-MARCOS-FREITAS: Pavilhão Gimnodesportivo do Estreito de Câmara de Lobos – Marcos Freitas (Câmara de Lobos, Madeira)
-- Official evidence URL: https://www.madeira.gov.pt/srs/Pesquisar/ctl/ReadInformcao/mid/686/InformacaoId/246084/UnidadeOrganicaId/30/LiveSearch/tenente-coronel%20Marco%20Paulo%20Pereira%20Nunes
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Gimnodesportivo do Estreito de Câmara de Lobos – Marcos Freitas',
  'Câmara de Lobos',
  'Madeira',
  'Rua das Vinhas, 9325-051 Estreito de Câmara de Lobos',
  'Basquetebol;Voleibol;Andebol;Ténis de mesa;Futsal',
  'Pavilhão desportivo',
  'INDOOR',
  NULL,
  NULL,
  'OFFICIAL',
  'JYNLO:OFFICIAL:MD-CAMARA-ESTREITO-MARCOS-FREITAS',
  'https://www.madeira.gov.pt/srs/Pesquisar/ctl/ReadInformcao/mid/686/InformacaoId/246084/UnidadeOrganicaId/30/LiveSearch/tenente-coronel%20Marco%20Paulo%20Pereira%20Nunes',
  NULL,
  TRUE,
  FALSE,
  FALSE,
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:MD-CAMARA-ESTREITO-MARCOS-FREITAS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Gimnodesportivo do Estreito de Câmara de Lobos – Marcos Freitas'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Câmara de Lobos')))
     OR (LOWER(BTRIM(COALESCE(existing.address, ''))) = LOWER(BTRIM('Rua das Vinhas, 9325-051 Estreito de Câmara de Lobos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Câmara de Lobos')))
)
ON CONFLICT DO NOTHING;

-- MD-CAMARA-RIBEIRO-REAL: Campo de Futebol do Ribeiro Real (Câmara de Lobos, Madeira)
-- Official evidence URL: https://www.madeira.gov.pt/srtc/Pesquisar/ctl/ReadInformcao/mid/5346/InformacaoId/246085/UnidadeOrganicaId/30/LiveSearch/Ribeiro%20da%20Corujeira
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Campo de Futebol do Ribeiro Real',
  'Câmara de Lobos',
  'Madeira',
  'Caminho Doutor João Henrique Pereiro, Sítio do Ribeiro Real, Câmara de Lobos',
  'Futebol',
  'Campo de futebol',
  'OUTDOOR',
  NULL,
  NULL,
  'OFFICIAL',
  'JYNLO:OFFICIAL:MD-CAMARA-RIBEIRO-REAL',
  'https://www.madeira.gov.pt/srtc/Pesquisar/ctl/ReadInformcao/mid/5346/InformacaoId/246085/UnidadeOrganicaId/30/LiveSearch/Ribeiro%20da%20Corujeira',
  NULL,
  FALSE,
  FALSE,
  FALSE,
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:MD-CAMARA-RIBEIRO-REAL'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol do Ribeiro Real'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Câmara de Lobos')))
     OR (LOWER(BTRIM(COALESCE(existing.address, ''))) = LOWER(BTRIM('Caminho Doutor João Henrique Pereiro, Sítio do Ribeiro Real, Câmara de Lobos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Câmara de Lobos')))
)
ON CONFLICT DO NOTHING;

-- MD-CAMARA-CURRAL-POOL: Piscina do Curral das Freiras (Câmara de Lobos, Madeira)
-- Official evidence URL: https://www.madeira.gov.pt/gsre/pesquisar/ctl/ReadInformcao/mid/3298/InformacaoId/249661/UnidadeOrganicaId/30/LiveSearch/Curral%20da%20Freiras
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Piscina do Curral das Freiras',
  'Câmara de Lobos',
  'Madeira',
  'Sítio da Achada 56, Curral das Freiras, Câmara de Lobos',
  'Natação',
  'Piscina',
  NULL,
  NULL,
  NULL,
  'OFFICIAL',
  'JYNLO:OFFICIAL:MD-CAMARA-CURRAL-POOL',
  'https://www.madeira.gov.pt/gsre/pesquisar/ctl/ReadInformcao/mid/3298/InformacaoId/249661/UnidadeOrganicaId/30/LiveSearch/Curral%20da%20Freiras',
  NULL,
  FALSE,
  FALSE,
  FALSE,
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:MD-CAMARA-CURRAL-POOL'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscina do Curral das Freiras'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Câmara de Lobos')))
     OR (LOWER(BTRIM(COALESCE(existing.address, ''))) = LOWER(BTRIM('Sítio da Achada 56, Curral das Freiras, Câmara de Lobos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Câmara de Lobos')))
)
ON CONFLICT DO NOTHING;

