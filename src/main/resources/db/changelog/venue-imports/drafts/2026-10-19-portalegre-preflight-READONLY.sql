-- READ-ONLY: inspect duplicates and nearby venues BEFORE any Portalegre seed.
-- Includes legacy records WITHOUT coordinates when name+city match.
-- No INSERT/UPDATE/DELETE, no secrets, no access to production in CI.
-- This report is not a substitute for manual checking of aliases/OSM position.
WITH candidate (source_reference, candidate_name, candidate_city, latitude, longitude) AS (
 VALUES
  ('OSM:WAY:157099522', 'Recinto Multiusos de Ponte de Sor', 'Ponte de Sor', 39.2550475, -8.0119079),
  ('OSM:WAY:426295154', 'Campo de Futebol Sintético de Sousel', 'Sousel', 38.9544786, -7.6714955),
  ('OSM:WAY:426296888', 'Pavilhão Gimnodesportivo de Sousel', 'Sousel', 38.9553066, -7.6708754),
  ('OSM:WAY:429474882', 'Campo de Futebol de Cano', 'Cano', 38.9627718, -7.7639828),
  ('OSM:WAY:429475622', 'Polidesportivo de Cano', 'Cano', 38.9632269, -7.7628395),
  ('OSM:WAY:429476238', 'Campo de Futebol de Casa Branca', 'Casa Branca', 38.9503679, -7.8053164),
  ('OSM:WAY:429477908', 'Campo de Futebol de Santo Amaro', 'Santo Amaro', 38.9705205, -7.5836824),
  ('OSM:WAY:1275651401', 'Estádio Municipal de Ponte de Sor', 'Ponte de Sor', 39.2539045, -8.0132653),
  ('OSM:WAY:1348549266', 'Estádio Patalino', 'Elvas', 38.8772886, -7.1577876)
),
matches AS (
 SELECT c.source_reference, c.candidate_name, c.candidate_city,
        v.id AS existing_id, v.name AS existing_name,
        v.city AS existing_city, v.district AS existing_district,
        v.information_source AS existing_source, v.source_reference AS existing_reference,
        CASE WHEN v.latitude IS NOT NULL AND v.longitude IS NOT NULL
          THEN ROUND((6371000.0 * 2.0 * ASIN(LEAST(1.0, SQRT(
            POWER(SIN(RADIANS((v.latitude - c.latitude) / 2.0)), 2) +
            COS(RADIANS(c.latitude)) * COS(RADIANS(v.latitude)) *
            POWER(SIN(RADIANS((v.longitude - c.longitude) / 2.0)), 2)
          ))))::numeric, 1)
          ELSE NULL END AS distance_metres,
        (v.information_source = 'OPENSTREETMAP' AND
          v.source_reference = c.source_reference) AS same_source,
        (LOWER(BTRIM(v.name)) = LOWER(BTRIM(c.candidate_name)) AND
          LOWER(BTRIM(v.city)) = LOWER(BTRIM(c.candidate_city))) AS same_name_city
 FROM candidate c
 LEFT JOIN LATERAL (
   SELECT v.*
   FROM desporto24.venue v
   WHERE (v.information_source = 'OPENSTREETMAP'
             AND v.source_reference = c.source_reference)
      OR (v.latitude BETWEEN c.latitude - 0.0015 AND c.latitude + 0.0015
          AND v.longitude BETWEEN c.longitude - 0.0020 AND c.longitude + 0.0020)
      OR (LOWER(BTRIM(v.name)) = LOWER(BTRIM(c.candidate_name))
          AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(c.candidate_city)))
   ORDER BY
      CASE
        WHEN v.information_source = 'OPENSTREETMAP'
             AND v.source_reference = c.source_reference THEN 0
        WHEN LOWER(BTRIM(v.name)) = LOWER(BTRIM(c.candidate_name))
             AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(c.candidate_city)) THEN 1
        ELSE 2
      END,
      ABS(COALESCE(v.latitude, 999) - c.latitude) +
      ABS(COALESCE(v.longitude, 999) - c.longitude)
   LIMIT 80
 ) v ON TRUE
)
SELECT source_reference, candidate_name, candidate_city,
       existing_id, existing_name, existing_city, existing_district,
       existing_source, existing_reference, distance_metres,
       CASE
         WHEN existing_id IS NULL THEN 'NO_MATCH_FOUND_REVIEW_BEFORE_INSERT'
         WHEN same_source THEN 'EXISTING_OSM_REFERENCE_DO_NOT_INSERT'
         WHEN same_name_city AND distance_metres IS NULL
           THEN 'SAME_NAME_CITY_MISSING_GPS_REVIEW'
         WHEN same_name_city AND distance_metres > 150
           THEN 'SAME_NAME_CITY_DIFFERENT_GPS_REVIEW'
         WHEN distance_metres <= 12 THEN 'VERY_CLOSE_POSSIBLE_DUPLICATE_REVIEW'
         WHEN same_name_city AND distance_metres <= 150
           THEN 'SAME_NAME_NEARBY_DO_NOT_INSERT'
         WHEN distance_metres <= 150 THEN 'NEARBY_DIFFERENT_OR_ALIAS_REVIEW'
         ELSE 'REVIEW_MATCH'
       END AS review_status
FROM matches
WHERE existing_id IS NULL OR same_source OR same_name_city
      OR distance_metres <= 150
ORDER BY candidate_name,
         CASE WHEN same_source THEN 0 WHEN same_name_city THEN 1
              WHEN existing_id IS NULL THEN 3 ELSE 2 END,
         distance_metres NULLS LAST, existing_id;
