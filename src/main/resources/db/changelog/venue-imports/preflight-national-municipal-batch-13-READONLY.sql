-- JYNLO READ-ONLY preflight: exactly 13 proposed municipal venue records.
-- No writes. Matches on same source, same trimmed name+city and same address+city.
-- Same-address matches are REVIEW warnings: multiple pitches share sports complexes.
-- NO_MATCH_FOUND is not a proof against aliases or nearby existing facilities.
WITH proposed (source_reference, name, city, district, address, sports) AS (
  VALUES
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6214', 'Pista de Atletismo do Estádio 1.º de Maio', 'Braga', 'Braga', 'Parque S. João da Ponte, 4711-909 Braga', 'Atletismo'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6223', 'Campo de Futebol N.º1 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6224', 'Campo de Futebol N.º3 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6225', 'Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6226', 'Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6229', 'Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6230', 'Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Futebol'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6231', 'Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia', 'Braga', 'Braga', 'Complexo Desportivo da Rodovia, 4710-294 Braga', 'Basquetebol'),
  ('JYNLO:MUNICIPAL:BRAGA:ITEM-1-6262', 'Piscina Climatizada do Complexo Desportivo de Maximinos', 'Braga', 'Braga', 'Rua da Naia, 4710-057 Braga', 'Natação'),
  ('JYNLO:DOCUMENTED:VISEU:PISCINAS_MUNICIPAIS_FONTelo', 'Piscinas Municipais de Viseu', 'Viseu', 'Viseu', 'Avenida José Relvas, Parque do Fontelo, 3500-143 Viseu', 'Natação'),
  ('JYNLO:DOCUMENTED:VISEU:TENIS_FONTelo', 'Campos de Ténis do Parque do Fontelo', 'Viseu', 'Viseu', 'Rua Anacleto Pinto, Parque do Fontelo, 3500-143 Viseu', 'Ténis'),
  ('JYNLO:DOCUMENTED:GUARDA:PISCINAS_MUNICIPAIS', 'Piscinas Municipais da Guarda', 'Guarda', 'Guarda', 'Bairro Nossa Senhora dos Remédios, 6300-509 Guarda', 'Natação'),
  ('JYNLO:DOCUMENTED:AVEIRO:PISCINAS_SAO_JACINTO', 'Piscina Municipal de São Jacinto', 'São Jacinto', 'Aveiro', 'São Jacinto, Aveiro', 'Natação')
)
SELECT p.district AS proposed_district, p.city AS proposed_city,
       p.name AS proposed_name, p.sports AS proposed_sports, p.source_reference AS proposed_reference,
       COALESCE(v.match_reason, 'NO_MATCH_FOUND') AS match_reason,
       v.id AS existing_id, v.name AS existing_name, v.address AS existing_address,
       v.source_reference AS existing_source_reference
FROM proposed AS p
LEFT JOIN LATERAL (
  SELECT existing.id, existing.name, existing.address, existing.source_reference,
         CASE WHEN existing.source_reference = p.source_reference
                THEN 'SAME_SOURCE_REFERENCE_REVIEW'
              WHEN LOWER(BTRIM(existing.name)) = LOWER(BTRIM(p.name))
               AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM(p.city))
                THEN 'SAME_NAME_CITY_REVIEW'
              ELSE 'SAME_ADDRESS_CITY_REVIEW'
         END AS match_reason
  FROM desporto24.venue AS existing
  WHERE (existing.source_reference = p.source_reference)
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM(p.name))
       AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM(p.city)))
     OR (LOWER(BTRIM(existing.address)) = LOWER(BTRIM(p.address))
       AND LOWER(BTRIM(existing.city)) = LOWER(BTRIM(p.city)))
) AS v ON TRUE
ORDER BY p.district, p.city, p.name, match_reason, existing_id;
