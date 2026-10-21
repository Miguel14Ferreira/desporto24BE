-- Jynlo controlled venue catalogue: 7 source-corroborated facilities from Pestana inventory.
-- NOT a certified list of venues currently available to book or freely use.
-- GPS deliberately NULL: no independently verified coordinate in the spreadsheets.
-- Only INSERTs; no updates or deletion. Requires read-only duplicate preflight and backup.
-- source_reference uses Jynlo-owned workbook IDs, not government-issued identifiers.

-- PT-FUT-0007 | Campo Municipal do Viso
-- Municipal/operational source: https://www.agoraporto.pt/noticias/desporto/desporto/rmid
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal do Viso', 'Porto', 'Porto', 'Rua Direita do Viso, 4250 Porto', 'Futebol;Hóquei em Campo', 'Campo de jogos', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:CATALOG:PT-FUT-0007', 'https://www.agoraporto.pt/noticias/desporto/desporto/rmid',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:CATALOG:PT-FUT-0007')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo Municipal do Viso'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Porto')))
)
ON CONFLICT DO NOTHING;

-- PT-FUT-0008 | Campo Municipal dos Choupos
-- Municipal/operational source: https://www.agoraporto.pt/noticias/desporto/desporto/rmid
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal dos Choupos', 'Porto', 'Porto', 'Rua dos Plátanos, 4100-414 Porto', 'Futebol', 'Campo de jogos', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:CATALOG:PT-FUT-0008', 'https://www.agoraporto.pt/noticias/desporto/desporto/rmid',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:CATALOG:PT-FUT-0008')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo Municipal dos Choupos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Porto')))
)
ON CONFLICT DO NOTHING;

-- PT-FUT-0009 | Campo Municipal do Parque da Cidade
-- Municipal/operational source: https://www.agoraporto.pt/noticias/desporto/desporto/rmid
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal do Parque da Cidade', 'Porto', 'Porto', 'Estrada Interior da Circunvalação, 4100-083 Porto', 'Futebol', 'Campo de jogos', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:CATALOG:PT-FUT-0009', 'https://www.agoraporto.pt/noticias/desporto/desporto/rmid',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:CATALOG:PT-FUT-0009')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo Municipal do Parque da Cidade'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Porto')))
)
ON CONFLICT DO NOTHING;

-- PT-FUT-0011 | Campo Municipal do Outeiro
-- Municipal/operational source: https://www.agoraporto.pt/noticias/desporto/desporto/rmid
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal do Outeiro', 'Porto', 'Porto', 'Rua D. Afonso Henriques, Paranhos, Porto', 'Futebol', 'Campo de jogos', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:CATALOG:PT-FUT-0011', 'https://www.agoraporto.pt/noticias/desporto/desporto/rmid',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:CATALOG:PT-FUT-0011')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo Municipal do Outeiro'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Porto')))
)
ON CONFLICT DO NOTHING;

-- PT-FUT-0019 | Polidesportivo de Arroios — Joaquim Campos
-- Municipal/operational source: https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo de Arroios — Joaquim Campos', 'Lisboa', 'Lisboa', 'Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa', 'Futsal;Andebol;Basquetebol', 'Polidesportivo', NULL,
       NULL, NULL, 'OFFICIAL', 'JYNLO:CATALOG:PT-FUT-0019', 'https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:CATALOG:PT-FUT-0019')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Polidesportivo de Arroios — Joaquim Campos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Lisboa')))
)
ON CONFLICT DO NOTHING;

-- PT-FUT-0013 | Polidesportivo Municipal do Paço
-- Municipal/operational source: https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Polidesportivo Municipal do Paço', 'Maia', 'Porto', 'Rua do Paço, Pedrouços, 4425-678 Maia', 'Andebol;Futsal;Basquetebol', 'Polidesportivo', NULL,
       NULL, NULL, 'OFFICIAL', 'JYNLO:CATALOG:PT-FUT-0013', 'https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:CATALOG:PT-FUT-0013')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Polidesportivo Municipal do Paço'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Maia')))
)
ON CONFLICT DO NOTHING;

-- PT-PAD-0013 | Campo de Padel do Fojo
-- Municipal/operational source: https://www.cm-mira.pt/node/4084
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Padel do Fojo', 'Mira', 'Coimbra', 'Fojo, Seixo de Mira, Mira', 'Padel', 'Campo de padel', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:CATALOG:PT-PAD-0013', 'https://www.cm-mira.pt/node/4084',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:CATALOG:PT-PAD-0013')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Padel do Fojo'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Mira')))
)
ON CONFLICT DO NOTHING;
