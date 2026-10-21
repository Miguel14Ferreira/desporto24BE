-- JYNLO STAGED MUNICIPAL FACILITY BATCH, 21 Oct 2026. NOT activated in master.
-- © Município de Setúbal (2026) / Município de Leiria (2022) / Município de Bragança.
-- Operational status, exact Google Maps pin and public booking rights are NOT guaranteed.
-- Several buildings share municipal sites; do not treat same address as proof of duplicates.
-- Insert-only and conditional on same reference OR same normalized name in same city.
-- In particular, the 2022 Leiria inventory is HISTORICAL, not an open-now claim.

-- Pista Municipal de BMX de Setúbal | Morada e BMX na ficha municipal de 2026
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/pista-municipal-de-bmx-de-setubal/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pista Municipal de BMX de Setúbal', 'Setúbal', 'Setúbal', 'Avenida da Bela Vista, 2900 Setúbal', 'BMX', 'Pista de BMX', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:BMX', 'https://www.mun-setubal.pt/equipamentos_desport/pista-municipal-de-bmx-de-setubal/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:BMX'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pista Municipal de BMX de Setúbal'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Complexo Municipal de Atletismo de Setúbal | Modalidades e estrada na ficha municipal de 2026
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/complexo-municipal-de-atletismo-de-setubal/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Atletismo de Setúbal', 'Setúbal', 'Setúbal', 'Estrada Vale da Rosa, 2910 Setúbal', 'Atletismo;Futebol;Râguebi;Basquetebol;Escalada', 'Complexo desportivo', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:ATLETISMO', 'https://www.mun-setubal.pt/equipamentos_desport/complexo-municipal-de-atletismo-de-setubal/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:ATLETISMO'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo Municipal de Atletismo de Setúbal'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Arrábida Padel | Academia e campos em instalação municipal concessionada; morada exata a confirmar
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/arrabida-padel/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Arrábida Padel', 'Azeitão', 'Setúbal', 'Azeitão, Setúbal', 'Padel', 'Campos de padel', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:ARRABIDA_PADEL', 'https://www.mun-setubal.pt/equipamentos_desport/arrabida-padel/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:ARRABIDA_PADEL'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Arrábida Padel'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Azeitão')))
)
ON CONFLICT DO NOTHING;

-- Piscina Municipal de Azeitão | Morada e modalidade na ficha municipal de 2026
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/piscina-municipal-de-azeitao/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Azeitão', 'Azeitão', 'Setúbal', 'Rua Dr. Agostinho Machado Faria, 2925-499 Vila Nogueira de Azeitão', 'Natação', 'Piscina', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:PISCINA_AZEITAO', 'https://www.mun-setubal.pt/equipamentos_desport/piscina-municipal-de-azeitao/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:PISCINA_AZEITAO'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscina Municipal de Azeitão'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Azeitão')))
)
ON CONFLICT DO NOTHING;

-- Piscina Municipal das Palmeiras | Existência e piscina 25m em catálogo municipal, morada exata não transcrita
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/piscina-municipal-das-palmeiras/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal das Palmeiras', 'Setúbal', 'Setúbal', 'Setúbal (morada específica por confirmar)', 'Natação', 'Piscina', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:PISCINA_PALMEIRAS', 'https://www.mun-setubal.pt/equipamentos_desport/piscina-municipal-das-palmeiras/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:PISCINA_PALMEIRAS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscina Municipal das Palmeiras'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Complexo Municipal de Piscinas das Manteigadas | Ficha municipal de 2026; funcionamento sazonal; não anunciar aberto em outubro
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/complexo-municipal-de-piscinas-das-manteigadas/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Piscinas das Manteigadas', 'Setúbal', 'Setúbal', 'Avenida Emídio Graça, 2900 Setúbal', 'Natação;Canoagem', 'Complexo de piscinas', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:PISCINAS_MANTEIGADAS', 'https://www.mun-setubal.pt/equipamentos_desport/complexo-municipal-de-piscinas-das-manteigadas/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:PISCINAS_MANTEIGADAS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo Municipal de Piscinas das Manteigadas'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo Municipal de Aranguez | Morada ao nível da escola; modalidades exatas não confirmadas
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/pavilhao-desportivo-municipal-de-aranguez/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo Municipal de Aranguez', 'Setúbal', 'Setúbal', 'Escola Básica de Aranguez, Setúbal', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:PAV_ARANGUEZ', 'https://www.mun-setubal.pt/equipamentos_desport/pavilhao-desportivo-municipal-de-aranguez/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:PAV_ARANGUEZ'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo Municipal de Aranguez'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo Municipal das Manteigadas | Instalação municipal; morada exata e modalidades a confirmar
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/pavilhao-desportivo-municipal-das-manteigadas/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo Municipal das Manteigadas', 'Setúbal', 'Setúbal', 'Manteigadas, Setúbal', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:PAV_MANTEIGADAS', 'https://www.mun-setubal.pt/equipamentos_desport/pavilhao-desportivo-municipal-das-manteigadas/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:PAV_MANTEIGADAS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo Municipal das Manteigadas'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Escolar Municipal João dos Santos | Instalação escolar, acesso sujeito a autorização
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/pavilhao-escolar-municipal-joao-dos-santos/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Escolar Municipal João dos Santos', 'Setúbal', 'Setúbal', 'Escola Secundária Lima de Freitas, Setúbal', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:PAV_JOAO_SANTOS', 'https://www.mun-setubal.pt/equipamentos_desport/pavilhao-escolar-municipal-joao-dos-santos/',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:PAV_JOAO_SANTOS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Escolar Municipal João dos Santos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Complexo Desportivo Municipal do Parque Verde da Bela Vista | Modalidades confirmadas no catálogo municipal
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/complexo-desportivo-municipal-do-parque-verde-da-bela-vista/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal do Parque Verde da Bela Vista', 'Setúbal', 'Setúbal', 'Parque Verde da Bela Vista, Setúbal', 'Ténis;BMX;Basquetebol;Andebol;Futsal', 'Complexo desportivo', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:PARQUE_VERDE_BELA_VISTA', 'https://www.mun-setubal.pt/equipamentos_desport/complexo-desportivo-municipal-do-parque-verde-da-bela-vista/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:PARQUE_VERDE_BELA_VISTA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo Desportivo Municipal do Parque Verde da Bela Vista'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Campo Municipal Vítor Batista | Campo municipal no Bairro da Liberdade
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/campo-municipal-vitor-batista/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal Vítor Batista', 'Setúbal', 'Setúbal', 'Bairro da Liberdade, Setúbal', 'Futebol', 'Campo de futebol', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:VITOR_BATISTA', 'https://www.mun-setubal.pt/equipamentos_desport/campo-municipal-vitor-batista/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:VITOR_BATISTA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo Municipal Vítor Batista'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Campo Municipal de Jogos Forte da Bela Vista | Campo de terra batida; não confundir com Parque Verde Bela Vista
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/campo-municipal-de-jogos-forte-da-bela-vista/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de Jogos Forte da Bela Vista', 'Setúbal', 'Setúbal', 'Bairro da Bela Vista, Setúbal', 'Futebol', 'Campo de futebol', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:FORTE_BELA_VISTA', 'https://www.mun-setubal.pt/equipamentos_desport/campo-municipal-de-jogos-forte-da-bela-vista/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:FORTE_BELA_VISTA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo Municipal de Jogos Forte da Bela Vista'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Campo Municipal de Futebol Herculano Santos | Antigo Campo das Pedreiras do Viso, nome atualizado em 2026
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/campo-municipal-de-jogos-das-pedreiras-do-viso/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Campo Municipal de Futebol Herculano Santos', 'Setúbal', 'Setúbal', 'Pedreiras do Viso, Setúbal', 'Futebol', 'Campo de futebol', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:HERCULANO_SANTOS', 'https://www.mun-setubal.pt/equipamentos_desport/campo-municipal-de-jogos-das-pedreiras-do-viso/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:HERCULANO_SANTOS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Campo Municipal de Futebol Herculano Santos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Complexo Desportivo Municipal da Várzea | Dois campos de futebol 11; inserir o complexo uma só vez
-- Provenance: https://www.mun-setubal.pt/equipamentos_desport/complexo-desportivo-municipal-da-varzea/
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Desportivo Municipal da Várzea', 'Setúbal', 'Setúbal', 'Azinhaga de São Joaquim, Setúbal', 'Futebol', 'Complexo desportivo', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:SETUBAL:VARZEA', 'https://www.mun-setubal.pt/equipamentos_desport/complexo-desportivo-municipal-da-varzea/',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:SETUBAL:VARZEA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo Desportivo Municipal da Várzea'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Setúbal')))
)
ON CONFLICT DO NOTHING;

-- Complexo Municipal de Piscinas de Leiria | Inventário municipal 2022; localização explícita; funcionamento atual por confirmar
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Piscinas de Leiria', 'Leiria', 'Leiria', 'Largo da Feira, Arrabalde d''Aquém, Leiria', 'Natação', 'Complexo de piscinas', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PISCINAS_CIDADE', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PISCINAS_CIDADE'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo Municipal de Piscinas de Leiria'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Leiria')))
)
ON CONFLICT DO NOTHING;

-- Piscina Municipal de Caranguejeira | Inventário municipal 2022; funcionamento atual por confirmar
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Caranguejeira', 'Caranguejeira', 'Leiria', 'Rua de São Vicente, Caranguejeira', 'Natação', 'Piscina', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PISCINA_CARANGUEJEIRA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PISCINA_CARANGUEJEIRA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscina Municipal de Caranguejeira'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Caranguejeira')))
)
ON CONFLICT DO NOTHING;

-- Piscina Municipal de Maceira | Inventário municipal 2022; funcionamento atual por confirmar
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscina Municipal de Maceira', 'Maceira', 'Leiria', 'Rua Dr. Carlos Ponte Leça, Maceira', 'Natação', 'Piscina', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PISCINA_MACEIRA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PISCINA_MACEIRA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscina Municipal de Maceira'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Maceira')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo de Arrabal | Inventário municipal 2022; modalidades e acessos atuais não confirmados
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Arrabal', 'Arrabal', 'Leiria', 'Rua José Bernardino Crespo, n.º 25, Arrabal', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_ARRABAL', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_ARRABAL'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo de Arrabal'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Arrabal')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo de Bajouca | Inventário municipal 2022; modalidades e acessos atuais não confirmados
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Bajouca', 'Bajouca', 'Leiria', 'Largo dos Treze, Rua da Mãe Preciosa, Bajouca', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_BAJOUCA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_BAJOUCA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo de Bajouca'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Bajouca')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo de Caranguejeira | Instalação separada da piscina na mesma rua
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Caranguejeira', 'Caranguejeira', 'Leiria', 'Rua de São Vicente, Caranguejeira', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_CARANGUEJEIRA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_CARANGUEJEIRA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo de Caranguejeira'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Caranguejeira')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo de Carreira | Inventário municipal 2022
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Carreira', 'Carreira', 'Leiria', 'Rua da Quinta, Carreira', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_CARREIRA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_CARREIRA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo de Carreira'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Carreira')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo de Colmeias | Inventário municipal 2022
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Colmeias', 'Colmeias', 'Leiria', 'Rua da Escola da Eira Velha, Colmeias', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_COLMEIAS', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_COLMEIAS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo de Colmeias'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Colmeias')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo Dr. Correia Mateus | Inventário municipal 2022
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo Dr. Correia Mateus', 'Leiria', 'Leiria', 'Rua Paulo VI, Leiria', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_CORREIA_MATEUS', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_CORREIA_MATEUS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo Dr. Correia Mateus'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Leiria')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo de Maceira | Instalação distinta da piscina na mesma rua
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Maceira', 'Maceira', 'Leiria', 'Rua Dr. Carlos Ponte Leça, Maceira', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_MACEIRA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_MACEIRA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo de Maceira'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Maceira')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Desportivo de Pousos | Inventário municipal 2022
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Desportivo de Pousos', 'Pousos', 'Leiria', 'Rua Virgílio Monteiro, Charneca do Bailadouro, Pousos', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_POUSOS', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_POUSOS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Desportivo de Pousos'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Pousos')))
)
ON CONFLICT DO NOTHING;

-- Complexo Municipal de Ténis Dr. Rui Garcia da Fonseca | Inventário municipal 2022, freguesia confirmada; morada exata por verificar
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Complexo Municipal de Ténis Dr. Rui Garcia da Fonseca', 'Pousos', 'Leiria', 'Pousos, Leiria', 'Ténis', 'Campos de ténis', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:TENIS_RUI_FONSECA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:TENIS_RUI_FONSECA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Complexo Municipal de Ténis Dr. Rui Garcia da Fonseca'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Pousos')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Municipal do Lis | Inventário patrimonial 2022; acesso e modalidades a confirmar
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal do Lis', 'Cortes', 'Leiria', 'Cortes, Leiria', NULL, 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:PAV_LIS', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:PAV_LIS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Municipal do Lis'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Cortes')))
)
ON CONFLICT DO NOTHING;

-- Centro Nacional de Lançamentos de Leiria | Inventário municipal 2022; disciplina de lançamentos de atletismo
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Centro Nacional de Lançamentos de Leiria', 'Leiria', 'Leiria', 'Rotunda Almoínha Grande, Leiria', 'Atletismo', 'Centro de atletismo', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:CENTRO_LANCAMENTOS', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:CENTRO_LANCAMENTOS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Centro Nacional de Lançamentos de Leiria'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Leiria')))
)
ON CONFLICT DO NOTHING;

-- Estádio Municipal de Leiria Dr. Magalhães Pessoa | Inventário municipal 2022 confirma estádio; alias Dr. Magalhães Pessoa
-- Provenance: https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal de Leiria Dr. Magalhães Pessoa', 'Leiria', 'Leiria', 'Leiria (morada específica por confirmar)', 'Futebol', 'Estádio', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:LEIRIA:ESTADIO_MAGALHAES_PESSOA', 'https://www.cm-leiria.pt/cmleiria-migrate/uploads/document/file/44947/anexo_991_22.pdf',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:LEIRIA:ESTADIO_MAGALHAES_PESSOA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Estádio Municipal de Leiria Dr. Magalhães Pessoa'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Leiria')))
)
ON CONFLICT DO NOTHING;

-- Pavilhão Municipal Arnaldo Pereira | Instalação municipal e provas desportivas; verificar morada precisa
-- Provenance: https://www.cm-braganca.pt/transparencia/comunicacao/noticias/noticia/municipio-de-braganca-investe-na-melhoria-de-infraestruturas-desportivas
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Pavilhão Municipal Arnaldo Pereira', 'Bragança', 'Bragança', 'Parque Desportivo de Bragança, Bragança', 'Basquetebol;Futsal;Hóquei em Patins', 'Pavilhão desportivo', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGANCA:PAV_ARANLDO_PEREIRA', 'https://www.cm-braganca.pt/transparencia/comunicacao/noticias/noticia/municipio-de-braganca-investe-na-melhoria-de-infraestruturas-desportivas',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:BRAGANCA:PAV_ARANLDO_PEREIRA'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Municipal Arnaldo Pereira'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Bragança')))
)
ON CONFLICT DO NOTHING;

-- Estádio Municipal Eng.º José Luís Pinheiro | Nome do estádio documentado em notícia municipal; localização de complexo a confirmar
-- Provenance: https://www.cm-braganca.pt/transparencia/comunicacao/noticias/noticia/municipio-de-braganca-investe-na-melhoria-de-infraestruturas-desportivas
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Estádio Municipal Eng.º José Luís Pinheiro', 'Bragança', 'Bragança', 'Parque Desportivo de Bragança, Bragança', 'Futebol', 'Estádio', 'OUTDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGANCA:ESTADIO_JOSE_LUIS_PINHEIRO', 'https://www.cm-braganca.pt/transparencia/comunicacao/noticias/noticia/municipio-de-braganca-investe-na-melhoria-de-infraestruturas-desportivas',
       FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:BRAGANCA:ESTADIO_JOSE_LUIS_PINHEIRO'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Estádio Municipal Eng.º José Luís Pinheiro'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Bragança')))
)
ON CONFLICT DO NOTHING;

-- Piscinas Municipais de Bragança | Existência e modalidade comprovadas; piscina específica e horário por confirmar
-- Provenance: https://www.cm-braganca.pt/transparencia/comunicacao/noticias/noticia/municipio-de-braganca-investe-na-melhoria-de-infraestruturas-desportivas
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT 'Piscinas Municipais de Bragança', 'Bragança', 'Bragança', 'Parque Desportivo de Bragança, Bragança', 'Natação', 'Piscina', 'INDOOR',
       NULL, NULL, 'OFFICIAL', 'JYNLO:MUNICIPAL:BRAGANCA:PISCINAS_MUNICIPAIS', 'https://www.cm-braganca.pt/transparencia/comunicacao/noticias/noticia/municipio-de-braganca-investe-na-melhoria-de-infraestruturas-desportivas',
       TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:MUNICIPAL:BRAGANCA:PISCINAS_MUNICIPAIS'
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Piscinas Municipais de Bragança'))
         AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Bragança')))
)
ON CONFLICT DO NOTHING;
