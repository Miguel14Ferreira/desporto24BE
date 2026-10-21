-- Jynlo: pilot of 3 — safe read-only live DB duplicate preflight.
-- Distance <=100 m or OSM reference/name alias collision: BLOCK AND REVIEW.
-- Distance >100 m and <=150 m: informational neighbor for human review.
-- This report alone never approves any candidate for public import.
BEGIN TRANSACTION READ ONLY;
SET LOCAL statement_timeout = '15000ms';
WITH proposed (ref, name, city, lat, lon, alias_token) AS (
  VALUES
    ('OSM:WAY:1056855283', 'Centro Olímpico de Piscinas Municipais',
     'Coimbra', 40.2053924::float8, -8.4068928::float8, 'centroolimpico'),
    ('OSM:WAY:122685516', 'Piscinas Municipais Rui Abreu',
     'Coimbra', 40.2391033::float8, -8.4384818::float8, 'ruiabreu'),
    ('OSM:WAY:145943204', 'Pavilhão Marialvas',
     'Cantanhede', 40.3440280::float8, -8.5876330::float8, 'marialvas')
),
matched AS (
  SELECT p.ref candidate_ref, p.name candidate_name, p.city candidate_city,
         v.id, v.name existing_name, v.city existing_city,
         v.information_source existing_source, v.source_reference existing_ref,
         (v.information_source='OPENSTREETMAP' AND v.source_reference=p.ref) exact_ref,
         (LOWER(BTRIM(v.city))=LOWER(BTRIM(p.city))
          AND (
             REGEXP_REPLACE(TRANSLATE(LOWER(BTRIM(v.name)),
               'áàâãäéèêëíìîïóòôõöúùûüç','aaaaaeeeeiiiiooooouuuuc'),
               '[^a-z0-9]+','','g')
             = REGEXP_REPLACE(TRANSLATE(LOWER(BTRIM(p.name)),
               'áàâãäéèêëíìîïóòôõöúùûüç','aaaaaeeeeiiiiooooouuuuc'),
               '[^a-z0-9]+','','g')
             OR POSITION(p.alias_token IN
                 REGEXP_REPLACE(TRANSLATE(LOWER(BTRIM(v.name)),
                  'áàâãäéèêëíìîïóòôõöúùûüç','aaaaaeeeeiiiiooooouuuuc'),
                  '[^a-z0-9]+','','g'))>0
          )) name_or_alias_city,
         CASE WHEN v.latitude IS NOT NULL AND v.longitude IS NOT NULL
              THEN 111320.0 * SQRT(POWER(v.latitude-p.lat,2)
                     +POWER((v.longitude-p.lon)*COS(RADIANS(p.lat)),2))
              ELSE NULL END distance_metres
  FROM proposed p LEFT JOIN desporto24.venue v ON TRUE
),
flags AS (
 SELECT *,
        (exact_ref OR name_or_alias_city OR distance_metres<=100) blocker,
        (distance_metres>100 AND distance_metres<=150
          AND NOT (exact_ref OR name_or_alias_city)) neighbor_100_150m
 FROM matched
)
SELECT candidate_ref, candidate_name, candidate_city,
       (SELECT COUNT(*) FROM desporto24.venue) catalogue_rows,
       COUNT(id) FILTER (WHERE blocker) potential_collision_count,
       COUNT(id) FILTER (WHERE neighbor_100_150m) nearby_100_150m_count,
       COALESCE(JSONB_AGG(JSONB_BUILD_OBJECT(
           'id',id,'name',existing_name,'city',existing_city,
           'source',existing_source,'ref',existing_ref,
           'distanceMetres',ROUND(distance_metres::numeric,1),
           'sameRef',COALESCE(exact_ref,FALSE),
           'sameNameOrAliasCity',COALESCE(name_or_alias_city,FALSE),
           'within100m',COALESCE(distance_metres<=100,FALSE),
           'neighbor100to150m',COALESCE(neighbor_100_150m,FALSE)
         ) ORDER BY distance_metres NULLS LAST, id)
         FILTER (WHERE id IS NOT NULL AND (blocker OR neighbor_100_150m)),
         '[]'::jsonb) AS potential_matches
FROM flags
GROUP BY candidate_ref,candidate_name,candidate_city
ORDER BY candidate_ref;
ROLLBACK;
