-- Controlled municipal-source facility catalogue. DRAFT PR, not published.
-- Eight documented public/municipal facilities from Évora, Castelo Branco and Santarém.
-- No assertion of public booking, guaranteed free access, current hours or prices.
-- Coordinates remain NULL unless independently verified by official source + CAOP2025.
-- Only INSERT, no UPDATE/DELETE of existing community venue rows.
-- Manual preflight against production aliases/duplicates and backup required before merge.

-- Complexo Desportivo de Évora / Évora
-- Evidence: https://www.cm-evora.pt/visitante/lazer-visitar/complexo-desportivo-de-evora/
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo de Évora', 'Évora', 'Évora', 'Estrada das Alcáçovas (N380), Évora', 'Atletismo;Futebol', 'Complexo desportivo', NULL, NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:EVORA:COMPLEXO_DESPORTIVO_DE_EVORA', 'https://www.cm-evora.pt/visitante/lazer-visitar/complexo-desportivo-de-evora/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:EVORA:COMPLEXO_DESPORTIVO_DE_EVORA')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Complexo Desportivo de Évora'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Évora')))
)
ON CONFLICT DO NOTHING;

-- Piscinas Municipais de Évora / Évora
-- Evidence: https://www.cm-evora.pt/visitante/lazer-visitar/piscinas-municipais/
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Évora', 'Évora', 'Évora', 'Avenida Engenheiro Arantes de Oliveira, 7000-758 Évora', 'Natação', 'Piscina', NULL, NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:EVORA:PISCINAS_MUNICIPAIS_DE_EVORA', 'https://www.cm-evora.pt/visitante/lazer-visitar/piscinas-municipais/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:EVORA:PISCINAS_MUNICIPAIS_DE_EVORA')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Piscinas Municipais de Évora'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Évora')))
)
ON CONFLICT DO NOTHING;

-- Pista de Atletismo de Castelo Branco / Castelo Branco
-- Evidence: https://www.cm-castelobranco.pt/municipe/areas-de-acao/desporto-lazer/pista-de-atletismo/
-- GPS específico da pista publicado pela Câmara Municipal de Castelo Branco:
-- https://www.cm-castelobranco.pt/media/8123/manual-de-acolhimento-ao-novo-albicastrense.pdf
-- Validação CAOP2025 em 21/10/2026: município e distrito Castelo Branco (CI 39582351980).
-- Acesso: inscrição junto do funcionário da Associação de Atletismo, segundo página municipal.
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo de Castelo Branco', 'Castelo Branco', 'Castelo Branco', 'Parque Urbano, Castelo Branco', 'Atletismo', 'Pista desportiva', 'OUTDOOR', 39.818785611212455, -7.515117240291148, 'OFFICIAL', 'JYNLO:DOCUMENTED:CASTELO_BRANCO:PISTA_DE_ATLETISMO_DE_CASTELO_BRANCO', 'https://www.cm-castelobranco.pt/municipe/areas-de-acao/desporto-lazer/pista-de-atletismo/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:CASTELO_BRANCO:PISTA_DE_ATLETISMO_DE_CASTELO_BRANCO')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Pista de Atletismo de Castelo Branco'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Castelo Branco')))
)
ON CONFLICT DO NOTHING;

-- Complexo Aquático Municipal de Santarém / Santarém
-- Evidence: https://viversantarem.pt/instalacoes-desportivas/
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Aquático Municipal de Santarém', 'Santarém', 'Santarém', 'Jardim de Cima, 2005-444 Santarém', 'Natação;Polo Aquático;Natação Artística;Canoagem', 'Complexo desportivo', NULL, 39.264102742942576, -8.669450010048992, 'OFFICIAL', 'JYNLO:DOCUMENTED:SANTAREM:COMPLEXO_AQUATICO_MUNICIPAL_DE_SANTAREM', 'https://viversantarem.pt/instalacoes-desportivas/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:SANTAREM:COMPLEXO_AQUATICO_MUNICIPAL_DE_SANTAREM')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Complexo Aquático Municipal de Santarém'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Santarém')))
)
ON CONFLICT DO NOTHING;

-- Piscinas Municipais do Sacapeito / Santarém
-- Evidence: https://viversantarem.pt/instalacoes-desportivas/
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais do Sacapeito', 'Santarém', 'Santarém', 'Praceta Cidade de Badajoz, Urbanização do Sacapeito, 2005-213 Santarém', 'Natação', 'Piscina', NULL, NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:SANTAREM:PISCINAS_MUNICIPAIS_DO_SACAPEITO', 'https://viversantarem.pt/instalacoes-desportivas/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:SANTAREM:PISCINAS_MUNICIPAIS_DO_SACAPEITO')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Piscinas Municipais do Sacapeito'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Santarém')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo Municipal / Santarém
-- Evidence: https://viversantarem.pt/pavilhoes-desportivos/
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo Municipal', 'Santarém', 'Santarém', 'Largo Comendador Paulino da Cunha e Silva, 2005-134 Santarém', 'Futsal;Andebol;Hóquei em Patins;Patinagem;Voleibol;Corfebol;Basquetebol', 'Pavilhão desportivo', 'INDOOR', NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:SANTAREM:PAVILHAO_DESPORTIVO_MUNICIPAL', 'https://viversantarem.pt/pavilhoes-desportivos/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:SANTAREM:PAVILHAO_DESPORTIVO_MUNICIPAL')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Pavilhão Desportivo Municipal'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Santarém')))
)
ON CONFLICT DO NOTHING;

-- Nave Desportiva Municipal / Santarém
-- Evidence: https://viversantarem.pt/pavilhoes-desportivos/
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Nave Desportiva Municipal', 'Santarém', 'Santarém', 'Largo Comendador Paulino da Cunha e Silva, 2005-134 Santarém', 'Futsal;Andebol;Voleibol;Basquetebol', 'Pavilhão desportivo', 'INDOOR', NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:SANTAREM:NAVE_DESPORTIVA_MUNICIPAL', 'https://viversantarem.pt/pavilhoes-desportivos/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:SANTAREM:NAVE_DESPORTIVA_MUNICIPAL')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Nave Desportiva Municipal'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Santarém')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo EX-EPC / Santarém
-- Evidence: https://viversantarem.pt/pavilhoes-desportivos/
-- JYNLO:DOCUMENTED is an internal deterministic evidence key, NOT an external authority ID.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo EX-EPC', 'Santarém', 'Santarém', 'Rua dos Ex-Combatentes do Ultramar, 2005-246 Santarém', 'Basquetebol;Boxe;Esgrima;Voleibol;Tiro', 'Pavilhão desportivo', 'INDOOR', NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:SANTAREM:PAVILHAO_DESPORTIVO_EX_EPC', 'https://viversantarem.pt/pavilhoes-desportivos/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source='OFFICIAL' AND existing.source_reference='JYNLO:DOCUMENTED:SANTAREM:PAVILHAO_DESPORTIVO_EX_EPC')
     OR (LOWER(BTRIM(existing.name))=LOWER(BTRIM('Pavilhão Desportivo EX-EPC'))
         AND LOWER(BTRIM(existing.city))=LOWER(BTRIM('Santarém')))
)
ON CONFLICT DO NOTHING;
