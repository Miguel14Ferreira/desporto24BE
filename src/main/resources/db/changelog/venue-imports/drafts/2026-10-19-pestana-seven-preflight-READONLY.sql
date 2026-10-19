-- Read-only. Run against the production DB before activating the changeset.
-- Exact names/references are a first filter; manually review known aliases.
WITH proposed (ref, name, city) AS (
 VALUES
  ('JYNLO:CATALOG:PT-FUT-0007', 'Campo Municipal do Viso', 'Porto'),
  ('JYNLO:CATALOG:PT-FUT-0008', 'Campo Municipal dos Choupos', 'Porto'),
  ('JYNLO:CATALOG:PT-FUT-0009', 'Campo Municipal do Parque da Cidade', 'Porto'),
  ('JYNLO:CATALOG:PT-FUT-0011', 'Campo Municipal do Outeiro', 'Porto'),
  ('JYNLO:CATALOG:PT-FUT-0019', 'Polidesportivo de Arroios — Joaquim Campos', 'Lisboa'),
  ('JYNLO:CATALOG:PT-FUT-0013', 'Polidesportivo Municipal do Paço', 'Maia'),
  ('JYNLO:CATALOG:PT-PAD-0013', 'Campo de Padel do Fojo', 'Mira')
)
SELECT p.ref, p.name AS proposed_name, p.city AS proposed_city,
       v.id AS existing_id, v.name AS existing_name, v.city AS existing_city,
       v.information_source, v.source_reference
FROM proposed p
LEFT JOIN desporto24.venue v
  ON (v.source_reference = p.ref)
  OR (LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
      AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city)))
ORDER BY p.ref, v.id;
