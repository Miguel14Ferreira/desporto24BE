-- JYNLO READ ONLY. Additional six source-supported sports proposals from owner export.
-- Do NOT confuse source-supported sport with permission for public use or current bookings.
-- Compare exact ID, name, official source reference and missing-sports status before migration.
-- No writes.
WITH proposed (id, source_reference, name, sport) AS (
  VALUES
  (50, 'CASCAIS:SPORTS:equipamento_desportivo.692721', 'Pavilhão Desportivo dos Lombos', 'Futsal;Basquetebol;Corfebol;Andebol;Voleibol'),
  (174, 'CASCAIS:SPORTS:equipamento_desportivo.51227956', 'Parque Decathlon', 'Futebol;Petanca;Escalada;Skate;Patinagem'),
  (109, 'CASCAIS:SPORTS:equipamento_desportivo.693228', 'União Recreativa e Desportiva de Tires', 'Futebol'),
  (145, 'CASCAIS:SPORTS:equipamento_desportivo.30923165', 'Parque Desportivo Abel Viegas Lopes (Grupo Desportivo e Recreativo das Fontainhas, Cascais)', 'Futebol'),
  (237, 'CASCAIS:SPORTS:equipamento_desportivo.693270', 'Desportivo Monte Real', 'Karaté;Ténis de mesa'),
  (142, 'CASCAIS:SPORTS:equipamento_desportivo.30347766', 'Grupo Recreativo e Dramático 1º de Maio de Tires - Pavilhão Desportivo Serafim Tomé dos Santos', 'Ginástica;Trampolim')
)
SELECT p.id AS proposal_id, p.name AS proposal_name, p.sport AS proposed_sports,
       v.id AS existing_id, v.sports AS existing_sports,
       CASE WHEN v.id IS NULL THEN 'NO_EXACT_MATCH_REVIEW'
            WHEN v.sports IS NOT NULL AND BTRIM(v.sports) <> '' THEN 'ALREADY_CLASSIFIED_SKIP'
            ELSE 'ELIGIBLE_PENDING_APPROVAL' END AS preview_status
FROM proposed AS p
LEFT JOIN desporto24.venue AS v
  ON v.id = p.id
  AND v.source_reference = p.source_reference
  AND v.name = p.name
  AND v.information_source = 'OFFICIAL'
ORDER BY p.id;
