-- READ ONLY: run against authorized real PostgreSQL before any release.
-- Name or address match needs human identity review; never delete records.
WITH proposed(source_id,name,city,district,address) AS (
 VALUES
  ('AZ-SM-LARANJEIRAS', 'Complexo Desportivo das Laranjeiras', 'Ponta Delgada', 'Açores', 'Rua José Maria Caetano de Matos, s/n, 9500-116 Ponta Delgada'),
  ('AZ-SM-SIDONIO-SERPA', 'Pavilhão Desportivo Sidónio Serpa', 'Ponta Delgada', 'Açores', 'Rua da Juventude, 9500-211 Ponta Delgada'),
  ('MD-FUNCHAL-PISCINAS-OLIMPICAS', 'Complexo de Piscinas Olímpicas do Funchal', 'Funchal', 'Madeira', 'Beco dos Álamos, Santo António, Funchal'),
  ('MD-CAMARA-ESTREITO-MARCOS-FREITAS', 'Pavilhão Gimnodesportivo do Estreito de Câmara de Lobos – Marcos Freitas', 'Câmara de Lobos', 'Madeira', 'Rua das Vinhas, 9325-051 Estreito de Câmara de Lobos'),
  ('MD-CAMARA-RIBEIRO-REAL', 'Campo de Futebol do Ribeiro Real', 'Câmara de Lobos', 'Madeira', 'Caminho Doutor João Henrique Pereiro, Sítio do Ribeiro Real, Câmara de Lobos'),
  ('MD-CAMARA-CURRAL-POOL', 'Piscina do Curral das Freiras', 'Câmara de Lobos', 'Madeira', 'Sítio da Achada 56, Curral das Freiras, Câmara de Lobos')
)
SELECT p.source_id, p.name AS proposed_name, p.city AS proposed_city,
       COALESCE(m.reason, 'NO_EXACT_MATCH_REVIEW') AS match_reason,
       m.id AS existing_id, m.name AS existing_name,
       m.city AS existing_city, m.address AS existing_address
FROM proposed p
LEFT JOIN LATERAL (
 SELECT v.id, v.name, v.city, v.address,
   CASE WHEN v.source_reference = 'JYNLO:OFFICIAL:' || p.source_id
          THEN 'SAME_SOURCE_REFERENCE'
        WHEN LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
         AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city))
          THEN 'SAME_NAME_CITY'
        ELSE 'SAME_ADDRESS_CITY' END AS reason
 FROM desporto24.venue v
 WHERE v.source_reference = 'JYNLO:OFFICIAL:' || p.source_id
    OR (LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
        AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city)))
    OR (LOWER(BTRIM(COALESCE(v.address,''))) = LOWER(BTRIM(p.address))
        AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city)))
 LIMIT 20
) m ON TRUE
ORDER BY p.district, p.city, p.name, m.id;
