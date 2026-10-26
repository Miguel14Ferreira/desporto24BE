-- DRAFT ONLY: not included by db.changelog-master.yaml. No deployment or data write.
-- Official facility: https://viversantarem.pt/pavilhoes-desportivos/
-- Current school-facility access may be limited; do not promise public booking.
-- Contact intentionally NULL. No inferred GPS. No changes to existing venues.
-- Production gates: read-only full-catalogue/SQL alias review, current restorable
-- backup, independent approval and isolated PostgreSQL 16 validation.
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website, contact,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT
  'Pavilhão da EB 2/3 Mem Ramires',
  'Santarém',
  'Santarém',
  'Rua Doutor Virgílio Arruda, 2000-217 Santarém',
  'Futsal;Andebol;Basquetebol;Ginástica;Dança Desportiva;Voleibol;Badminton',
  'Pavilhão desportivo',
  'INDOOR',
  NULL, NULL,
  'OFFICIAL',
  'JYNLO:OFFICIAL:SANTAREM:MEM-RAMIRES',
  'https://viversantarem.pt/pavilhoes-desportivos/',
  NULL,
  TRUE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE existing.source_reference = 'JYNLO:OFFICIAL:SANTAREM:MEM-RAMIRES'
     OR (LOWER(BTRIM(existing.city)) = LOWER(BTRIM('Santarém'))
         AND (
           LOWER(BTRIM(existing.name)) = LOWER(BTRIM('Pavilhão da EB 2/3 Mem Ramires'))
           OR LOWER(BTRIM(existing.address)) = LOWER(BTRIM('Rua Doutor Virgílio Arruda, 2000-217 Santarém'))
         ))
)
ON CONFLICT DO NOTHING;
