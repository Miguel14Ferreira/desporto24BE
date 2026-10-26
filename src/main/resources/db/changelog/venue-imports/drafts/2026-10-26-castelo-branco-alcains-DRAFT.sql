-- JYNLO DRAFT ONLY. No automatic Liquibase include, no LIVE writes.
-- Two independently documented facilities; no phone/contact and no inferred GPS.
-- Safety gate: compare full LIVE catalogue for aliases + current restorable backup +
-- read-only real database preflight before manually approved release.
-- Pavilion: https://www.cm-castelobranco.pt/visitante/contactos-uteis/
-- Futsal at the pavilion in 2024 official school sport program:
-- https://desportoescolar.dge.medu.pt/sites/default/files/RepositorioDocumentos/2024/programasnf2024.pdf
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão Municipal de Castelo Branco', 'Castelo Branco', 'Castelo Branco',
  'Avenida Rotary, 6000-087 Castelo Branco',
  'Futsal', 'Pavilhão desportivo', 'INDOOR',
  NULL, NULL, 'OFFICIAL',
  'JYNLO:OFFICIAL:CASTELO-BRANCO:PAVILHAO-ROTARY',
  'https://www.cm-castelobranco.pt/visitante/contactos-uteis/', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:CASTELO-BRANCO:PAVILHAO-ROTARY'
     OR (
       LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Castelo Branco'))
       AND (
         LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Municipal de Castelo Branco'))
         OR LOWER(COALESCE(existing.address, '')) LIKE '%rotary%'
         OR LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão Municipal'))
       )
     )
)
ON CONFLICT DO NOTHING;

-- ALBIGEC confirms 2026/27 school-of-swimming and the Alcains complex:
-- https://www.albigec.pt/noticias/2026/escola-de-natacao-da-albigec-inicia-nova-temporada-a-28-de-setembro/
-- https://www.albigec.pt/equipamentos/complexo-de-piscinas-de-alcains/
-- Indoor and outdoor pools belong to SAME facility: one row.
-- Outdoor pool closed for the season 2026-09-06. Do not claim year-round access.
-- No verified street, so address NULL.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Complexo de Piscinas Municipais de Alcains', 'Alcains', 'Castelo Branco',
  NULL, 'Natação', 'Complexo desportivo', 'BOTH',
  NULL, NULL, 'OFFICIAL', 'JYNLO:OFFICIAL:ALBIGEC:ALCAINS-POOLS',
  'https://www.albigec.pt/equipamentos/complexo-de-piscinas-de-alcains/', NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:ALBIGEC:ALCAINS-POOLS'
     OR (
       LOWER(COALESCE(existing.name, '')) LIKE '%alcains%'
       AND (
         LOWER(COALESCE(existing.name, '')) LIKE '%piscin%'
         OR LOWER(COALESCE(existing.name, '')) LIKE '%aquatic%'
       )
     )
     OR (
       LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Alcains'))
       AND LOWER(COALESCE(existing.name, '')) LIKE '%piscin%'
     )
)
ON CONFLICT DO NOTHING;
