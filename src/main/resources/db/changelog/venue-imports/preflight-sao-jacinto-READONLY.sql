-- JYNLO RECHECK (READ ONLY): corrected São Jacinto pool candidate.
-- Run with SELECT-only credentials against the real venue catalogue before approval.
-- Warnings are NOT automatic proof of a duplicate. No record is changed by this SQL.
-- The source reference, corrected official name and generic address must match the
-- 2026-10-20 batch manifest/INSERT. Do not treat "São Jacinto, Aveiro" as verified GPS.
-- If NO_MATCH_FOUND, inspect other spellings/aliases manually before authorizing any INSERT.
WITH proposed AS (
    SELECT
      'JYNLO:DOCUMENTED:AVEIRO:PISCINAS_SAO_JACINTO'::text AS source_reference,
      'Piscina Municipal de São Jacinto'::text AS name,
      'São Jacinto'::text AS city,
      'Aveiro'::text AS district
),
review_matches AS (
    SELECT v.id, v.name, v.city, v.district, v.address, v.source_reference,
           CASE
             WHEN v.source_reference = p.source_reference THEN 'SAME_SOURCE_REFERENCE_REVIEW'
             WHEN LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
                  AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city))
                THEN 'SAME_NAME_CITY_REVIEW'
             WHEN LOWER(COALESCE(v.name, '')) LIKE '%piscin%'
                  AND (
                    LOWER(COALESCE(v.city, '')) IN ('são jacinto', 'sao jacinto')
                    OR (
                      (LOWER(COALESCE(v.name, '')) LIKE '%jacinto%'
                       OR LOWER(COALESCE(v.address, '')) LIKE '%jacinto%')
                      AND (LOWER(COALESCE(v.city, '')) = 'aveiro'
                           OR LOWER(COALESCE(v.district, '')) = 'aveiro')
                    )
                  )
                THEN 'POTENTIAL_POOL_ALIAS_REVIEW'
             ELSE 'OTHER_SAO_JACINTO_VENUE_REVIEW'
           END AS match_reason
    FROM desporto24.venue AS v
    CROSS JOIN proposed AS p
    WHERE v.source_reference = p.source_reference
       OR (LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
           AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city)))
       OR (
          LOWER(COALESCE(v.name, '')) LIKE '%piscin%'
          AND (
             LOWER(COALESCE(v.city, '')) IN ('são jacinto', 'sao jacinto')
             OR (
               (LOWER(COALESCE(v.name, '')) LIKE '%jacinto%'
                OR LOWER(COALESCE(v.address, '')) LIKE '%jacinto%')
               AND (LOWER(COALESCE(v.city, '')) = 'aveiro'
                    OR LOWER(COALESCE(v.district, '')) = 'aveiro')
             )
          )
       )
       OR (
           LOWER(COALESCE(v.name, '')) LIKE '%jacinto%'
           AND (LOWER(COALESCE(v.city, '')) = 'aveiro'
                OR LOWER(COALESCE(v.district, '')) = 'aveiro')
       )
    ORDER BY
      CASE WHEN v.source_reference = p.source_reference THEN 0
           WHEN LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name)) THEN 1
           ELSE 2 END, v.id
    LIMIT 50
)
SELECT p.name AS proposed_name,
       p.city AS proposed_city,
       p.source_reference AS proposed_reference,
       COALESCE(m.match_reason, 'NO_MATCH_FOUND') AS match_reason,
       m.id AS existing_id,
       m.name AS existing_name,
       m.city AS existing_city,
       m.district AS existing_district,
       m.address AS existing_address,
       m.source_reference AS existing_source_reference
FROM proposed AS p
LEFT JOIN review_matches AS m ON TRUE
ORDER BY match_reason, existing_id;
