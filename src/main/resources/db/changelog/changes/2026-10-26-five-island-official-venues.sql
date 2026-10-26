-- JYNLO official-source island expansion (2026-10-26): five physical venues.
-- NO updates, deletes or modifications to existing venues. Geography source-checked;
-- contact and GPS intentionally unknown (NULL); listing never guarantees access.
-- Idempotent one-off Liquibase migration; matches against source ref, identity, and local aliases.

-- 1/5 — Governo dos Açores, Direção Regional do Desporto, endereço oficial Via Rápida; o complexo contém estádio e pavilhão
-- Official source: https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Complexo Desportivo João Paulo II', 'Angra do Heroísmo', 'Açores',
  'Via Rápida, s/n, 9700-135 Angra do Heroísmo', 'Futebol;Atletismo;Basquetebol;Voleibol', 'Complexo desportivo', 'BOTH',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:AZ-TERCEIRA:JOAO-PAULO-II',
  'https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE v.source_reference = 'JYNLO:OFFICIAL:AZ-TERCEIRA:JOAO-PAULO-II'
     OR (LOWER(BTRIM(v.city)) = LOWER(BTRIM('Angra do Heroísmo'))
         AND (LOWER(BTRIM(v.name)) = LOWER(BTRIM('Complexo Desportivo João Paulo II'))
              OR LOWER(BTRIM(COALESCE(v.address, ''))) = LOWER(BTRIM('Via Rápida, s/n, 9700-135 Angra do Heroísmo'))
              OR LOWER(COALESCE(v.name, '')) LIKE '%joão paulo ii%'
       OR LOWER(COALESCE(v.name, '')) LIKE '%joao paulo ii%'))
)
ON CONFLICT DO NOTHING;

-- 2/5 — Câmara de Angra do Heroísmo, ficha atualizada março de 2026; utilização mediante reserva paga
-- Official source: https://angradoheroismo.pt/pavilhao-municipal-de-desportos-de-angra-do-heroismo/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Municipal de Desportos de Angra do Heroísmo', 'Angra do Heroísmo', 'Açores',
  'Cerrado do Bailão, 9700-017 Angra do Heroísmo', 'Basquetebol;Futsal;Voleibol;Andebol', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:AZ-ANGRA:PAV-MUNICIPAL-BAILAO',
  'https://angradoheroismo.pt/pavilhao-municipal-de-desportos-de-angra-do-heroismo/', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE v.source_reference = 'JYNLO:OFFICIAL:AZ-ANGRA:PAV-MUNICIPAL-BAILAO'
     OR (LOWER(BTRIM(v.city)) = LOWER(BTRIM('Angra do Heroísmo'))
         AND (LOWER(BTRIM(v.name)) = LOWER(BTRIM('Pavilhão Municipal de Desportos de Angra do Heroísmo'))
              OR LOWER(BTRIM(COALESCE(v.address, ''))) = LOWER(BTRIM('Cerrado do Bailão, 9700-017 Angra do Heroísmo'))
              OR LOWER(COALESCE(v.name, '')) LIKE '%pavilhão municipal%angra%'
       OR LOWER(COALESCE(v.name, '')) LIKE '%pavilhao municipal%angra%'
       OR LOWER(COALESCE(v.name, '')) LIKE '%cerrado do bailão%'))
)
ON CONFLICT DO NOTHING;

-- 3/5 — Governo dos Açores, Diretório desportivo da Terceira; operadora de fitness ativa em 2026 confirma morada
-- Official source: https://portal.azores.gov.pt/documents/37017/1838049/terceira.pdf/eeb62d3f-6801-5844-ddcc-a444316a3846?t=1602085352656
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Desportivo do Juncal', 'Praia da Vitória', 'Açores',
  'Canada da Água, n.º 1, Juncal, 9760-408 Praia da Vitória', 'Ténis de mesa', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:AZ-TERCEIRA:JUNCAL-PAVILHAO',
  'https://portal.azores.gov.pt/documents/37017/1838049/terceira.pdf/eeb62d3f-6801-5844-ddcc-a444316a3846?t=1602085352656', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE v.source_reference = 'JYNLO:OFFICIAL:AZ-TERCEIRA:JUNCAL-PAVILHAO'
     OR (LOWER(BTRIM(v.city)) = LOWER(BTRIM('Praia da Vitória'))
         AND (LOWER(BTRIM(v.name)) = LOWER(BTRIM('Pavilhão Desportivo do Juncal'))
              OR LOWER(BTRIM(COALESCE(v.address, ''))) = LOWER(BTRIM('Canada da Água, n.º 1, Juncal, 9760-408 Praia da Vitória'))
              OR LOWER(COALESCE(v.name, '')) LIKE '%pavilhão%juncal%'
       OR LOWER(COALESCE(v.name, '')) LIKE '%pavilhao%juncal%'))
)
ON CONFLICT DO NOTHING;

-- 4/5 — Governo dos Açores, Serviço Desporto Pico no complexo; jornal oficial 2026; Junta de Freguesia da Madalena nomeia a rua
-- Official source: https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Complexo Desportivo Municipal da Madalena', 'Madalena', 'Açores',
  'Rua do Complexo Desportivo da Madalena, 9950-302 Madalena', 'Futebol;Atletismo', 'Complexo desportivo', 'OUTDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:AZ-PICO:MADALENA-COMPLEXO',
  'https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha', NULL,
  FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE v.source_reference = 'JYNLO:OFFICIAL:AZ-PICO:MADALENA-COMPLEXO'
     OR (LOWER(BTRIM(v.city)) = LOWER(BTRIM('Madalena'))
         AND (LOWER(BTRIM(v.name)) = LOWER(BTRIM('Complexo Desportivo Municipal da Madalena'))
              OR LOWER(BTRIM(COALESCE(v.address, ''))) = LOWER(BTRIM('Rua do Complexo Desportivo da Madalena, 9950-302 Madalena'))
              OR LOWER(COALESCE(v.name, '')) LIKE '%complexo desportivo%madalena%'
       OR LOWER(COALESCE(v.name, '')) LIKE '%estádio municipal%madalena%'
       OR LOWER(COALESCE(v.name, '')) LIKE '%estadio municipal%madalena%'))
)
ON CONFLICT DO NOTHING;

-- 5/5 — Governo da Madeira, ficha Pavilhão Multiusos, morada e modalidades; uso escolar e clubes, não inferir disponibilidade
-- Official source: https://www.madeira.gov.pt/gvps/Estrutura/GAPS/ctl/Read/mid/10703/InformacaoId/13693/UnidadeOrganicaId/23/CatalogoId/0
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Multiusos do Porto Santo', 'Porto Santo', 'Madeira',
  'Sítio das Matas, 9400-035 Porto Santo', 'Basquetebol;Ténis de mesa;Futsal;Voleibol', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:MD-PORTO-SANTO:PAV-MULTIUSOS',
  'https://www.madeira.gov.pt/gvps/Estrutura/GAPS/ctl/Read/mid/10703/InformacaoId/13693/UnidadeOrganicaId/23/CatalogoId/0', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE v.source_reference = 'JYNLO:OFFICIAL:MD-PORTO-SANTO:PAV-MULTIUSOS'
     OR (LOWER(BTRIM(v.city)) = LOWER(BTRIM('Porto Santo'))
         AND (LOWER(BTRIM(v.name)) = LOWER(BTRIM('Pavilhão Multiusos do Porto Santo'))
              OR LOWER(BTRIM(COALESCE(v.address, ''))) = LOWER(BTRIM('Sítio das Matas, 9400-035 Porto Santo'))
              OR LOWER(COALESCE(v.name, '')) LIKE '%multiusos%porto santo%'))
)
ON CONFLICT DO NOTHING;
