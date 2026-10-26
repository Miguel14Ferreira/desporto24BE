-- JYNLO 2026-10-26 — read-only preflight for all 9 DRAFT venue releases.
-- REQUIRES authorized read-only access to the real PostgreSQL database.
-- Sources: #401 (Mem Ramires), #402 (Castelo Branco / Alcains), #408 (islands).
-- No INSERT, UPDATE, DELETE or DDL. Running it DOES NOT authorize deployment.
-- All matched IDs/names are publicly displayable venue data, never contacts.
-- Distinctive-word matches are hints: false positives and aliases require review.
BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY;

WITH candidates(source_ref,name,city,district,address,distinctive_name,distinctive_street) AS (
  VALUES
  ('JYNLO:OFFICIAL:SANTAREM:MEM-RAMIRES', 'Pavilhão da EB 2/3 Mem Ramires', 'Santarém', 'Santarém', 'Rua Doutor Virgílio Arruda, 2000-217 Santarém', 'ramires', 'virgilio arruda'),
  ('JYNLO:OFFICIAL:CASTELO-BRANCO:PAVILHAO-ROTARY', 'Pavilhão Municipal de Castelo Branco', 'Castelo Branco', 'Castelo Branco', 'Avenida Rotary, 6000-087 Castelo Branco', 'pavilhao municipal', 'rotary'),
  ('JYNLO:OFFICIAL:ALBIGEC:ALCAINS-POOLS', 'Complexo de Piscinas Municipais de Alcains', 'Alcains', 'Castelo Branco', 'Estrada de Santa, 6005-000 Alcains', 'piscina', 'estrada de santa'),
  ('JYNLO:OFFICIAL:AZ-SM-LARANJEIRAS', 'Complexo Desportivo das Laranjeiras', 'Ponta Delgada', 'Açores', 'Rua José Maria Caetano de Matos, s/n, 9500-116 Ponta Delgada', 'laranjeiras', ''),
  ('JYNLO:OFFICIAL:AZ-SM-SIDONIO-SERPA', 'Pavilhão Desportivo Sidónio Serpa', 'Ponta Delgada', 'Açores', 'Rua da Juventude, 9500-211 Ponta Delgada', 'sidonio serpa', ''),
  ('JYNLO:OFFICIAL:MD-FUNCHAL-PISCINAS-OLIMPICAS', 'Complexo de Piscinas Olímpicas do Funchal', 'Funchal', 'Madeira', 'Beco dos Álamos, Santo António, Funchal', 'piscinas olimpicas', ''),
  ('JYNLO:OFFICIAL:MD-CAMARA-ESTREITO-MARCOS-FREITAS', 'Pavilhão Gimnodesportivo do Estreito de Câmara de Lobos – Marcos Freitas', 'Câmara de Lobos', 'Madeira', 'Rua das Vinhas, 9325-051 Estreito de Câmara de Lobos', 'marcos freitas', ''),
  ('JYNLO:OFFICIAL:MD-CAMARA-RIBEIRO-REAL', 'Campo de Futebol do Ribeiro Real', 'Câmara de Lobos', 'Madeira', 'Caminho Doutor João Henrique Pereiro, Sítio do Ribeiro Real, Câmara de Lobos', 'ribeiro real', ''),
  ('JYNLO:OFFICIAL:MD-CAMARA-CURRAL-POOL', 'Piscina do Curral das Freiras', 'Câmara de Lobos', 'Madeira', 'Sítio da Achada 56, Curral das Freiras, Câmara de Lobos', 'curral das freiras', '')
)
SELECT c.source_ref, c.name AS candidate_name, c.city AS candidate_city,
       c.district AS candidate_district,
       COALESCE(e.reason, 'NO_POTENTIAL_MATCH_REVIEW') AS review_status,
       e.id AS existing_id, e.name AS existing_name, e.city AS existing_city,
       e.district AS existing_district, e.address AS existing_address
FROM candidates c
LEFT JOIN LATERAL (
  SELECT v.id, v.name, v.city, v.district, v.address,
         CASE
           WHEN v.source_reference = c.source_ref THEN 'SOURCE_REF_ALREADY_EXISTS'
           WHEN lower(btrim(v.name)) = lower(btrim(c.name))
                AND lower(btrim(v.city)) = lower(btrim(c.city))
             THEN 'SAME_NAME_CITY'
           WHEN lower(btrim(COALESCE(v.address, ''))) = lower(btrim(c.address))
                AND lower(btrim(v.city)) = lower(btrim(c.city))
             THEN 'SAME_ADDRESS_CITY'
           ELSE 'POSSIBLE_ALIAS_REVIEW'
         END AS reason
  FROM desporto24.venue v
  WHERE v.source_reference = c.source_ref
     OR (
       -- A name equal across localities of the same district may be an alias.
       lower(btrim(COALESCE(v.district, ''))) = lower(btrim(c.district))
       AND lower(btrim(v.name)) = lower(btrim(c.name))
     )
     OR (
       lower(btrim(v.city)) = lower(btrim(c.city))
       AND (
         lower(btrim(v.name)) = lower(btrim(c.name))
         OR lower(btrim(COALESCE(v.address, ''))) = lower(btrim(c.address))
         OR translate(lower(v.name),
            'áàâãäéêëíìîïóòôõöúùûüç',
            'aaaaaeeeiiiiooooouuuuc') LIKE '%' || c.distinctive_name || '%'
         OR (
           c.distinctive_street <> ''
           AND translate(lower(COALESCE(v.address,'')),
              'áàâãäéêëíìîïóòôõöúùûüç',
              'aaaaaeeeiiiiooooouuuuc') LIKE '%' || c.distinctive_street || '%'
         )
       )
     )
  ORDER BY (v.source_reference = c.source_ref) DESC,
           (lower(btrim(v.name)) = lower(btrim(c.name))) DESC,
           v.id
  LIMIT 12
) e ON TRUE
ORDER BY c.district, c.city, c.name, e.id;

ROLLBACK;
