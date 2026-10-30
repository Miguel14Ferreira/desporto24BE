-- Jynlo official source-backed interior venues, 26 October 2026.
-- No changes to existing COMMUNITY/OSM records, no fabricated phone/GPS.
-- Listings don't certify availability, pricing or open-now.
-- All writes are INSERT-only, idempotent, atomic in the parent Liquibase changeset.

-- 1/5 — Câmara Municipal de Seia: recinto indoor com jogos oficiais de voleibol e horário municipal (confirmar disponibilidade)
-- Authority: https://cm-seia.pt/viver/desporto/equipamentos/complexo-desportivo-municipal-1/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Complexo Desportivo Municipal 1 de Seia', 'Seia', 'Guarda',
  'Rua Dra. Ester Barata, 6270-652 Seia', 'Voleibol', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:SEIA:COMPLEXO-DESPORTIVO-1',
  'https://cm-seia.pt/viver/desporto/equipamentos/complexo-desportivo-municipal-1/', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue e
  WHERE e.source_reference = 'JYNLO:OFFICIAL:SEIA:COMPLEXO-DESPORTIVO-1'
     OR (
       (LOWER(BTRIM(e.city)) = LOWER(BTRIM('Seia'))
        OR (LOWER(BTRIM(e.district)) = LOWER(BTRIM('Guarda'))
            AND ('Seia' = 'Seia' AND LOWER(BTRIM(e.city)) = 'são romão')))
       AND (
           LOWER(BTRIM(e.name)) = LOWER(BTRIM('Complexo Desportivo Municipal 1 de Seia'))
           OR LOWER(BTRIM(COALESCE(e.address,''))) = LOWER(BTRIM('Rua Dra. Ester Barata, 6270-652 Seia'))
           OR LOWER(COALESCE(e.name,'')) LIKE '%complexo desportivo municipal 1%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%complexo desportivo municipal n.º 1%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%ester barata%'
       )
     )
)
ON CONFLICT DO NOTHING;

-- 2/5 — Câmara Municipal de Seia: pavilhão São Romão, jogo 40x20, vários desportos, acesso mediante disponibilidade
-- Authority: https://cm-seia.pt/viver/desporto/equipamentos/gimnodesportivo/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Gimnodesportivo Municipal Padre Martinho', 'Seia', 'Guarda',
  'Largo Outeiro da Forca, 6270-284 São Romão', 'Futsal;Basquetebol;Voleibol;Andebol;Badminton;Ginástica', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:SEIA:PADRE-MARTINHO',
  'https://cm-seia.pt/viver/desporto/equipamentos/gimnodesportivo/', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue e
  WHERE e.source_reference = 'JYNLO:OFFICIAL:SEIA:PADRE-MARTINHO'
     OR (
       (LOWER(BTRIM(e.city)) = LOWER(BTRIM('Seia'))
        OR (LOWER(BTRIM(e.district)) = LOWER(BTRIM('Guarda'))
            AND ('Seia' = 'Seia' AND LOWER(BTRIM(e.city)) = 'são romão')))
       AND (
           LOWER(BTRIM(e.name)) = LOWER(BTRIM('Gimnodesportivo Municipal Padre Martinho'))
           OR LOWER(BTRIM(COALESCE(e.address,''))) = LOWER(BTRIM('Largo Outeiro da Forca, 6270-284 São Romão'))
           OR LOWER(COALESCE(e.name,'')) LIKE '%padre martinho%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%outeiro da forca%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%pavilhão de são romão%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%pavilhao de sao romao%'
       )
     )
)
ON CONFLICT DO NOTHING;

-- 3/5 — Ata municipal de 18-06-2025 confirma propriedade INATEL e gestão pelo Município; endereço da Rua da Força Aérea 185; equipamento usado por clubes
-- Authority: https://www.cm-mirandela.pt/cmmirandela/uploads/document/file/7405/15___ata_18_de_junho.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Gimnodesportivo José Pina – INATEL', 'Mirandela', 'Bragança',
  'Rua da Força Aérea, n.º 185, 5370-265 Mirandela', 'Futsal', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:MIRANDELA:INATEL-JOSE-PINA',
  'https://www.cm-mirandela.pt/cmmirandela/uploads/document/file/7405/15___ata_18_de_junho.pdf', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue e
  WHERE e.source_reference = 'JYNLO:OFFICIAL:MIRANDELA:INATEL-JOSE-PINA'
     OR (
       (LOWER(BTRIM(e.city)) = LOWER(BTRIM('Mirandela'))
        OR (LOWER(BTRIM(e.district)) = LOWER(BTRIM('Bragança'))
            AND ('Mirandela' = 'Seia' AND LOWER(BTRIM(e.city)) = 'são romão')))
       AND (
           LOWER(BTRIM(e.name)) = LOWER(BTRIM('Pavilhão Gimnodesportivo José Pina – INATEL'))
           OR LOWER(BTRIM(COALESCE(e.address,''))) = LOWER(BTRIM('Rua da Força Aérea, n.º 185, 5370-265 Mirandela'))
           OR LOWER(COALESCE(e.name,'')) LIKE '%inatel%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%josé pina%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%jose pina%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%força aérea%'
       )
     )
)
ON CONFLICT DO NOTHING;

-- 4/5 — Município de Belmonte confirma morada em contactos e atividade desportiva oficial de futsal em outubro 2026
-- Authority: https://www.cm-belmonte.pt/contactos-uteis/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Gimnodesportivo de Belmonte', 'Belmonte', 'Castelo Branco',
  'Rua Chafariz do Areal, 50, 6250-049 Belmonte', 'Futsal', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:BELMONTE:GIMNODESPORTIVO',
  'https://www.cm-belmonte.pt/contactos-uteis/', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue e
  WHERE e.source_reference = 'JYNLO:OFFICIAL:BELMONTE:GIMNODESPORTIVO'
     OR (
       (LOWER(BTRIM(e.city)) = LOWER(BTRIM('Belmonte'))
        OR (LOWER(BTRIM(e.district)) = LOWER(BTRIM('Castelo Branco'))
            AND ('Belmonte' = 'Seia' AND LOWER(BTRIM(e.city)) = 'são romão')))
       AND (
           LOWER(BTRIM(e.name)) = LOWER(BTRIM('Pavilhão Gimnodesportivo de Belmonte'))
           OR LOWER(BTRIM(COALESCE(e.address,''))) = LOWER(BTRIM('Rua Chafariz do Areal, 50, 6250-049 Belmonte'))
           OR LOWER(COALESCE(e.name,'')) LIKE '%pavilhão municipal de belmonte%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%pavilhao municipal de belmonte%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%gimnodesportivo%belmonte%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%chafariz do areal%'
       )
     )
)
ON CONFLICT DO NOTHING;

-- 5/5 — Município de Belmonte: inaugurado em 2011, pavilhão 40x20 e morada completa
-- Authority: https://www.cm-belmonte.pt/diretorio/pavilhao-gimnodesportivo-de-caria/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Gimnodesportivo de Caria', 'Caria', 'Castelo Branco',
  'Rua de São Domingos, 18, 6250-111 Caria', 'Andebol;Basquetebol;Voleibol', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:BELMONTE:CARIA-GIMNODESPORTIVO',
  'https://www.cm-belmonte.pt/diretorio/pavilhao-gimnodesportivo-de-caria/', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue e
  WHERE e.source_reference = 'JYNLO:OFFICIAL:BELMONTE:CARIA-GIMNODESPORTIVO'
     OR (
       (LOWER(BTRIM(e.city)) = LOWER(BTRIM('Caria'))
        OR (LOWER(BTRIM(e.district)) = LOWER(BTRIM('Castelo Branco'))
            AND ('Caria' = 'Seia' AND LOWER(BTRIM(e.city)) = 'são romão')))
       AND (
           LOWER(BTRIM(e.name)) = LOWER(BTRIM('Pavilhão Gimnodesportivo de Caria'))
           OR LOWER(BTRIM(COALESCE(e.address,''))) = LOWER(BTRIM('Rua de São Domingos, 18, 6250-111 Caria'))
           OR LOWER(COALESCE(e.name,'')) LIKE '%pavilhão municipal de caria%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%pavilhao municipal de caria%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%gimnodesportivo%de caria%'
             OR LOWER(COALESCE(e.name,'')) LIKE '%são domingos%'
       )
     )
)
ON CONFLICT DO NOTHING;
