-- READ-ONLY: 5 officially corroborated Cascais venue modality proposals.
-- NO writes; review all matches, historical facility access, and status before release.
-- The municipality page is an official source of the reported modality/facility,
-- NOT proof that a user can book the activity today.
WITH proposed (id, source_reference, name, sport) AS (
  VALUES
  (43, 'CASCAIS:SPORTS:equipamento_desportivo.692628', 'Complexo Desportivo de Alcabideche', 'Jiu-Jitsu;Karaté;Natação;Ténis'),
  (111, 'CASCAIS:SPORTS:equipamento_desportivo.693264', 'Complexo Desportivo de São Domingos deRana', 'Musculação;Ténis'),
  (134, 'CASCAIS:SPORTS:equipamento_desportivo.30031558', 'CERCICA -  Cooperativa para a Educação e Reabilitação de Cidadãos Inadaptados de Cascais', 'Natação'),
  (132, 'CASCAIS:SPORTS:equipamento_desportivo.30311614', 'Clube Naval de Cascais', 'Mergulho;Vela'),
  (128, 'CASCAIS:SPORTS:equipamento_desportivo.30274118', 'Clube de Petanca de S. Pedro do Estoril', 'Petanca')
)
SELECT p.id AS proposed_id, p.name AS proposed_name, p.sport AS proposed_sports,
       v.id AS existing_id, v.sports AS existing_sports,
       CASE WHEN v.id IS NULL THEN 'NO_EXACT_MATCH_REVIEW'
            WHEN v.sports IS NOT NULL AND BTRIM(v.sports) <> '' THEN 'ALREADY_CLASSIFIED_SKIP'
            ELSE 'ELIGIBLE_PENDING_APPROVAL' END AS preview_status
FROM proposed p
LEFT JOIN desporto24.venue v
       ON v.id = p.id
      AND v.source_reference = p.source_reference
      AND v.name = p.name
      AND v.information_source = 'OFFICIAL'
ORDER BY p.id;
