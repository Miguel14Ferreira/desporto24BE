-- DRAFT, NOT ACTIVE, NOT INCLUDED IN LIQUIBASE MASTER. DO NOT RUN DIRECTLY.
-- Source-backed proposal: 5 Cascais venues only. No authorization to mutate DB.
-- Prerequisites: backup/restore validated, source recency/manual review, PostgreSQL
-- integration tests, explicit user approval of production release.
-- Idempotent guarded update: source reference, exact id/name, OFFICIAL and empty sports.
WITH proposed (id, source_reference, name, sport) AS (
  VALUES
  (43, 'CASCAIS:SPORTS:equipamento_desportivo.692628', 'Complexo Desportivo de Alcabideche', 'Jiu-Jitsu;Karaté;Natação;Ténis'),
  (111, 'CASCAIS:SPORTS:equipamento_desportivo.693264', 'Complexo Desportivo de São Domingos deRana', 'Musculação;Ténis'),
  (134, 'CASCAIS:SPORTS:equipamento_desportivo.30031558', 'CERCICA -  Cooperativa para a Educação e Reabilitação de Cidadãos Inadaptados de Cascais', 'Natação'),
  (132, 'CASCAIS:SPORTS:equipamento_desportivo.30311614', 'Clube Naval de Cascais', 'Mergulho;Vela'),
  (128, 'CASCAIS:SPORTS:equipamento_desportivo.30274118', 'Clube de Petanca de S. Pedro do Estoril', 'Petanca')
)
UPDATE desporto24.venue AS v
SET sports = p.sport
FROM proposed p
WHERE v.id = p.id
  AND v.source_reference = p.source_reference
  AND v.name = p.name
  AND v.information_source = 'OFFICIAL'
  AND (v.sports IS NULL OR BTRIM(v.sports) = '');
