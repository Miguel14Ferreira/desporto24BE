-- JYNLO source-backed national district coverage. Draft PR only; NOT deployed.
-- 13 additive conditional inserts from official municipality pages/documents.
-- All GPS deliberately NULL; map links use an explicitly approximate address search.
-- A venue source is not proof of current booking availability/opening hours.
-- Explicit backup/recovery verification, read-only production alias preflight and
-- PostgreSQL 16 Liquibase tests are REQUIRED before merging/deploying this PR.
-- No UPDATE, DELETE, or legacy venue modification.

-- Braga: Pista de Atletismo do Estádio 1.º de Maio
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6214
-- Review: Municipal equipment page details athletics track and postcode.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista de Atletismo do Estádio 1.º de Maio', 'Braga', 'Braga', 'Parque S. João da Ponte, 4711-909 Braga', 'Atletismo', 'Pista de atletismo', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6214', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6214',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6214')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pista de Atletismo do Estádio 1.º de Maio'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Campo de Futebol N.º1 do Complexo Desportivo da Rodovia
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6223
-- Review: Separately numbered pitch according to municipal directory.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol N.º1 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol', 'Campo de futebol', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6223', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6223',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6223')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol N.º1 do Complexo Desportivo da Rodovia'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Campo de Futebol N.º3 do Complexo Desportivo da Rodovia
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6224
-- Review: Separately numbered pitch according to municipal directory.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol N.º3 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol', 'Campo de futebol', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6224', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6224',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6224')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol N.º3 do Complexo Desportivo da Rodovia'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6225
-- Review: Football 7 pitch: name and numbering retained.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol', 'Campo de futebol 7', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6225', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6225',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6225')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6226
-- Review: Football 7 pitch: name and numbering retained.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol', 'Campo de futebol 7', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6226', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6226',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6226')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6229
-- Review: Municipality lists this as a separate numbered pitch.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol', 'Campo de futebol', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6229', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6229',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6229')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6230
-- Review: Municipality lists this as a separate numbered pitch.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol', 'Campo de futebol', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6230', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6230',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6230')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6231
-- Review: Official directory jointly lists two courts, represented as one venue to avoid double counting.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Basquetebol', 'Campo de basquetebol', NULL,
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6231', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6231',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6231')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Braga: Piscina Climatizada do Complexo Desportivo de Maximinos
-- Municipal evidence: https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6262
-- Review: Indoor/heated characteristics not asserted beyond official facility name.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Climatizada do Complexo Desportivo de Maximinos', 'Braga', 'Braga', 'Rua da Naia, 4710-057 Braga', 'Natação', 'Piscina', NULL,
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6262', 'https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6262',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:MUNICIPAL:BRAGA:ITEM-1-6262')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscina Climatizada do Complexo Desportivo de Maximinos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Braga')))
)
ON CONFLICT DO NOTHING;

-- Viseu: Piscinas Municipais de Viseu
-- Coordinates from the official Viseu municipal venue page, 40°39'35.46N / 7°54'11.37W.
-- CAOP2025 check: Viseu municipality, Viseu district (source-only GitHub Action #39584936380).
-- https://www.cm-viseu.pt/pt/areas-servicos/desporto-atividade-fisica/complexo-desportivo-do-fontelo/piscinas-municipais-de-viseu/
-- Municipal evidence: https://www.cm-viseu.pt/fotos/documentos_ficheiros/empresas_aderentes_viseu_jovem_atual_mar2023_09_03_14680162436409b24ac9c5a.pdf
-- Review: Official municipal card and sports dossier name location and swimming; source document older than 2026.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Viseu', 'Viseu', 'Viseu', 'Avenida José Relvas, Parque do Fontelo, 3500-143 Viseu', 'Natação', 'Piscina', 'INDOOR',
       40.659850000, -7.903158333333334, 'OFFICIAL', 'JYNLO:DOCUMENTED:VISEU:PISCINAS_MUNICIPAIS_FONTelo', 'https://www.cm-viseu.pt/fotos/documentos_ficheiros/empresas_aderentes_viseu_jovem_atual_mar2023_09_03_14680162436409b24ac9c5a.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:DOCUMENTED:VISEU:PISCINAS_MUNICIPAIS_FONTelo')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscinas Municipais de Viseu'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Viseu')))
)
ON CONFLICT DO NOTHING;

-- Viseu: Campos de Ténis do Parque do Fontelo
-- Coordinates from the official Viseu municipal court page, 40°39'37.19N / 7°54'4.49W.
-- CAOP2025 check: Viseu municipality, Viseu district (source-only GitHub Action #39584936380).
-- https://www.cm-viseu.pt/pt/areas-servicos/desporto-atividade-fisica/complexo-desportivo-do-fontelo/campos-de-tenis/
-- Municipal evidence: https://www.cm-viseu.pt/fotos/documentos_ficheiros/empresas_aderentes_viseu_jovem_atual_mar2023_09_03_14680162436409b24ac9c5a.pdf
-- Review: Official municipal partner directory names tennis at Parque do Fontelo; 2024 sports calendar supports tennis court #3.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campos de Ténis do Parque do Fontelo', 'Viseu', 'Viseu', 'Rua Anacleto Pinto, Parque do Fontelo, 3500-143 Viseu', 'Ténis', 'Campo de ténis', NULL,
       40.660330555555554, -7.9012472222222225, 'OFFICIAL', 'JYNLO:DOCUMENTED:VISEU:TENIS_FONTelo', 'https://www.cm-viseu.pt/fotos/documentos_ficheiros/empresas_aderentes_viseu_jovem_atual_mar2023_09_03_14680162436409b24ac9c5a.pdf',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:DOCUMENTED:VISEU:TENIS_FONTelo')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campos de Ténis do Parque do Fontelo'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Viseu')))
)
ON CONFLICT DO NOTHING;

-- Guarda: Piscinas Municipais da Guarda
-- Municipal evidence: https://desporto.mun-guarda.pt/Documentos/f0698def2751440189338052fc6a1785
-- Review: Municipal 2025/26 sports document provides address; municipality 2026 official program confirms pool in use.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais da Guarda', 'Guarda', 'Guarda', 'Bairro Nossa Senhora dos Remédios, 6300-509 Guarda', 'Natação', 'Piscina', NULL,
       NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:GUARDA:PISCINAS_MUNICIPAIS', 'https://desporto.mun-guarda.pt/Documentos/f0698def2751440189338052fc6a1785',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:DOCUMENTED:GUARDA:PISCINAS_MUNICIPAIS')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscinas Municipais da Guarda'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Guarda')))
)
ON CONFLICT DO NOTHING;

-- Aveiro: Piscina Municipal de São Jacinto
-- Municipal evidence: https://www.cm-aveiro.pt/municipio/comunicacao/noticias/noticia/piscina-municipal-de-sao-jacinto-ultrapassa-as-5-000-utilizacoes-neste-verao
-- Review: Municipality announced reopening 24 July 2026; no street address confirmed by municipality; seasonality applies; official September 2026 municipal report documents summer use, not current October opening.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de São Jacinto', 'São Jacinto', 'Aveiro', 'São Jacinto, Aveiro', 'Natação', 'Piscina', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:DOCUMENTED:AVEIRO:PISCINAS_SAO_JACINTO', 'https://www.cm-aveiro.pt/municipio/comunicacao/noticias/noticia/piscina-municipal-de-sao-jacinto-ultrapassa-as-5-000-utilizacoes-neste-verao',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue AS existing
  WHERE (existing.information_source = 'OFFICIAL' AND existing.source_reference = 'JYNLO:DOCUMENTED:AVEIRO:PISCINAS_SAO_JACINTO')
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscina Municipal de São Jacinto'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('São Jacinto')))
)
ON CONFLICT DO NOTHING;
