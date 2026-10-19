-- JYNLO DRAFT ONLY. Not included in Liquibase master, not a production runbook.
-- Never run manually; approval, identity check, official source refresh, verified backup
-- and PostgreSQL 16 Liquibase tests are required before registering an approved changeset.
-- Only conditionally fill sports for a specific existing venue. No INSERTs/DELETEs.
WITH proposed (id, source_reference, name, sport) AS (
  VALUES
  (50, 'CASCAIS:SPORTS:equipamento_desportivo.692721', 'Pavilhão Desportivo dos Lombos', 'Futsal;Basquetebol;Corfebol;Andebol;Voleibol'),
  (174, 'CASCAIS:SPORTS:equipamento_desportivo.51227956', 'Parque Decathlon', 'Futebol;Petanca;Escalada;Skate;Patinagem'),
  (109, 'CASCAIS:SPORTS:equipamento_desportivo.693228', 'União Recreativa e Desportiva de Tires', 'Futebol'),
  (145, 'CASCAIS:SPORTS:equipamento_desportivo.30923165', 'Parque Desportivo Abel Viegas Lopes (Grupo Desportivo e Recreativo das Fontainhas, Cascais)', 'Futebol'),
  (237, 'CASCAIS:SPORTS:equipamento_desportivo.693270', 'Desportivo Monte Real', 'Karaté;Ténis de mesa'),
  (142, 'CASCAIS:SPORTS:equipamento_desportivo.30347766', 'Grupo Recreativo e Dramático 1º de Maio de Tires - Pavilhão Desportivo Serafim Tomé dos Santos', 'Ginástica;Trampolim')
)
UPDATE desporto24.venue AS v
SET sports = p.sport
FROM proposed AS p
WHERE v.id = p.id
  AND v.source_reference = p.source_reference
  AND v.name = p.name
  AND v.information_source = 'OFFICIAL'
  AND (v.sports IS NULL OR BTRIM(v.sports) = '');
