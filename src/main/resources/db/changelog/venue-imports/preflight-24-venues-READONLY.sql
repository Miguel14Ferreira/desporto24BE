-- READ-ONLY candidate-to-existing-venue inventory for the 24 proposed Jynlo inserts.
-- Run using a read-only PostgreSQL role against the actual production database BEFORE merge.
-- Zero writes. The results are a triage, NOT a guarantee of no aliases or duplicates.
-- Spatial proximity is a warning (roughly 200-300m box), not proof that two courts are the same.
-- Rows with match_reason='NO_MATCH_FOUND' still require a human approval and backup.
-- The candidate coordinates and source refs come verbatim from the release SQL.
--
WITH proposed(source_reference, information_source, name, city, district, latitude, longitude) AS (
  VALUES
  ('OSM:WAY:157099522', 'OPENSTREETMAP', 'Recinto Multiusos de Ponte de Sor', 'Ponte de Sor', 'Portalegre', 39.2550475, -8.0119079),
  ('OSM:WAY:426295154', 'OPENSTREETMAP', 'Campo de Futebol Sintético de Sousel', 'Sousel', 'Portalegre', 38.9544786, -7.6714955),
  ('OSM:WAY:426296888', 'OPENSTREETMAP', 'Pavilhão Gimnodesportivo de Sousel', 'Sousel', 'Portalegre', 38.9553066, -7.6708754),
  ('OSM:WAY:429474882', 'OPENSTREETMAP', 'Campo de Futebol de Cano', 'Cano', 'Portalegre', 38.9627718, -7.7639828),
  ('OSM:WAY:429475622', 'OPENSTREETMAP', 'Polidesportivo de Cano', 'Cano', 'Portalegre', 38.9632269, -7.7628395),
  ('OSM:WAY:429476238', 'OPENSTREETMAP', 'Campo de Futebol de Casa Branca', 'Casa Branca', 'Portalegre', 38.9503679, -7.8053164),
  ('OSM:WAY:429477908', 'OPENSTREETMAP', 'Campo de Futebol de Santo Amaro', 'Santo Amaro', 'Portalegre', 38.9705205, -7.5836824),
  ('OSM:WAY:1275651401', 'OPENSTREETMAP', 'Estádio Municipal de Ponte de Sor', 'Ponte de Sor', 'Portalegre', 39.2539045, -8.0132653),
  ('OSM:WAY:1348549266', 'OPENSTREETMAP', 'Estádio Patalino', 'Elvas', 'Portalegre', 38.8772886, -7.1577876),
  ('JYNLO:DOCUMENTED:EVORA:COMPLEXO_DESPORTIVO_DE_EVORA', 'OFFICIAL', 'Complexo Desportivo de Évora', 'Évora', 'Évora', NULL::double precision, NULL::double precision),
  ('JYNLO:DOCUMENTED:EVORA:PISCINAS_MUNICIPAIS_DE_EVORA', 'OFFICIAL', 'Piscinas Municipais de Évora', 'Évora', 'Évora', NULL::double precision, NULL::double precision),
  ('JYNLO:DOCUMENTED:CASTELO_BRANCO:PISTA_DE_ATLETISMO_DE_CASTELO_BRANCO', 'OFFICIAL', 'Pista de Atletismo de Castelo Branco', 'Castelo Branco', 'Castelo Branco', 39.818785611212455, -7.515117240291148),
  ('JYNLO:DOCUMENTED:SANTAREM:COMPLEXO_AQUATICO_MUNICIPAL_DE_SANTAREM', 'OFFICIAL', 'Complexo Aquático Municipal de Santarém', 'Santarém', 'Santarém', 39.264102742942576, -8.669450010048992),
  ('JYNLO:DOCUMENTED:SANTAREM:PISCINAS_MUNICIPAIS_DO_SACAPEITO', 'OFFICIAL', 'Piscinas Municipais do Sacapeito', 'Santarém', 'Santarém', NULL::double precision, NULL::double precision),
  ('JYNLO:DOCUMENTED:SANTAREM:PAVILHAO_DESPORTIVO_MUNICIPAL', 'OFFICIAL', 'Pavilhão Desportivo Municipal', 'Santarém', 'Santarém', NULL::double precision, NULL::double precision),
  ('JYNLO:DOCUMENTED:SANTAREM:NAVE_DESPORTIVA_MUNICIPAL', 'OFFICIAL', 'Nave Desportiva Municipal', 'Santarém', 'Santarém', NULL::double precision, NULL::double precision),
  ('JYNLO:DOCUMENTED:SANTAREM:PAVILHAO_DESPORTIVO_EX_EPC', 'OFFICIAL', 'Pavilhão Desportivo EX-EPC', 'Santarém', 'Santarém', NULL::double precision, NULL::double precision),
  ('JYNLO:CATALOG:PT-FUT-0007', 'OFFICIAL', 'Campo Municipal do Viso', 'Porto', 'Porto', NULL::double precision, NULL::double precision),
  ('JYNLO:CATALOG:PT-FUT-0008', 'OFFICIAL', 'Campo Municipal dos Choupos', 'Porto', 'Porto', NULL::double precision, NULL::double precision),
  ('JYNLO:CATALOG:PT-FUT-0009', 'OFFICIAL', 'Campo Municipal do Parque da Cidade', 'Porto', 'Porto', NULL::double precision, NULL::double precision),
  ('JYNLO:CATALOG:PT-FUT-0011', 'OFFICIAL', 'Campo Municipal do Outeiro', 'Porto', 'Porto', NULL::double precision, NULL::double precision),
  ('JYNLO:CATALOG:PT-FUT-0019', 'OFFICIAL', 'Polidesportivo de Arroios — Joaquim Campos', 'Lisboa', 'Lisboa', NULL::double precision, NULL::double precision),
  ('JYNLO:CATALOG:PT-FUT-0013', 'OFFICIAL', 'Polidesportivo Municipal do Paço', 'Maia', 'Porto', NULL::double precision, NULL::double precision),
  ('JYNLO:CATALOG:PT-PAD-0013', 'OFFICIAL', 'Campo de Padel do Fojo', 'Mira', 'Coimbra', NULL::double precision, NULL::double precision)
)
SELECT p.district AS proposed_district,
       p.city AS proposed_city,
       p.name AS proposed_name,
       p.information_source AS proposed_source,
       p.source_reference AS proposed_reference,
       p.latitude AS proposed_latitude,
       p.longitude AS proposed_longitude,
       COALESCE(existing.match_reason, 'NO_MATCH_FOUND') AS match_reason,
       existing.id AS existing_id,
       existing.name AS existing_name,
       existing.city AS existing_city,
       existing.district AS existing_district,
       existing.information_source AS existing_source,
       existing.source_reference AS existing_reference,
       existing.latitude AS existing_latitude,
       existing.longitude AS existing_longitude
FROM proposed AS p
LEFT JOIN LATERAL (
    SELECT v.id, v.name, v.city, v.district, v.information_source,
           v.source_reference, v.latitude, v.longitude,
           CASE
             WHEN v.source_reference = p.source_reference
               THEN 'SAME_SOURCE_REFERENCE_REVIEW'
             WHEN LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
               AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city))
               THEN 'SAME_NAME_AND_CITY_REVIEW'
             WHEN p.latitude IS NOT NULL AND p.longitude IS NOT NULL
               AND v.latitude BETWEEN p.latitude - 0.0025 AND p.latitude + 0.0025
               AND v.longitude BETWEEN p.longitude - 0.0025 AND p.longitude + 0.0025
               THEN 'NEARBY_LOCATION_REVIEW'
             ELSE 'OTHER_MATCH_REVIEW'
           END AS match_reason
    FROM desporto24.venue AS v
    WHERE v.source_reference = p.source_reference
       OR (LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
           AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city)))
       OR (p.latitude IS NOT NULL AND p.longitude IS NOT NULL
           AND v.latitude BETWEEN p.latitude - 0.0025 AND p.latitude + 0.0025
           AND v.longitude BETWEEN p.longitude - 0.0025 AND p.longitude + 0.0025)
    ORDER BY v.id
    LIMIT 20
) AS existing ON TRUE
ORDER BY p.district, p.city, p.name, existing.match_reason NULLS LAST, existing.id;
