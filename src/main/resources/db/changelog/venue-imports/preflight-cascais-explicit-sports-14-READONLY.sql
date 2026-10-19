-- JYNLO Cascais sports: READ-ONLY PREFLIGHT.
-- Owner-provided read-only production export, 19-Oct-2026: 169 missing sports.
-- 14 explicitly-named venue/sport suggestions, not independently verified.
-- This standalone file is NOT included in db.changelog-master.yaml.
-- Safe read-only preview of exact matches; does not apply any change.
-- Guards: id + exact original source_reference + exact name + OFFICIAL + blank sports.
-- No other venue attributes are modified.
WITH proposed (id, source_reference, name, sport) AS (
  VALUES
  (108, 'CASCAIS:SPORTS:equipamento_desportivo.51226702', 'Street Workout Bairro Cruz Vermelha Norte', 'Calistenia'),
  (155, 'CASCAIS:SPORTS:equipamento_desportivo.51226337', 'Street Workout Bairro de Alcoitão', 'Calistenia'),
  (208, 'CASCAIS:SPORTS:equipamento_desportivo.52606497', 'Street Workout Bicesse', 'Calistenia'),
  (51, 'CASCAIS:SPORTS:equipamento_desportivo.51226731', 'Street Workout Zambujeiro', 'Calistenia'),
  (175, 'CASCAIS:SPORTS:equipamento_desportivo.51228025', 'Surf Cascais School', 'Surf'),
  (231, 'CASCAIS:SPORTS:equipamento_desportivo.100000004', 'Escola de Escalada da Guia', 'Escalada'),
  (206, 'CASCAIS:SPORTS:equipamento_desportivo.52606415', 'Street workout Cabeço de Mouro', 'Calistenia'),
  (170, 'CASCAIS:SPORTS:equipamento_desportivo.51227724', 'Street Workout Outeiro de Polima', 'Calistenia'),
  (207, 'CASCAIS:SPORTS:equipamento_desportivo.52606467', 'Street Workout Jardins da Parede', 'Calistenia'),
  (123, 'CASCAIS:SPORTS:equipamento_desportivo.30035635', 'Centro de Surf de São Pedro do Estoril', 'Surf'),
  (195, 'CASCAIS:SPORTS:equipamento_desportivo.51229308', 'Centro do Yoga - Cascais', 'Yoga'),
  (218, 'CASCAIS:SPORTS:equipamento_desportivo.53729159', 'Mountain Bike Outeiro da Vela', 'Mountain Bike'),
  (187, 'CASCAIS:SPORTS:equipamento_desportivo.51229011', 'Street Workout Bairro da Assunção', 'Calistenia'),
  (193, 'CASCAIS:SPORTS:equipamento_desportivo.51229272', 'Yoga Estoril | Áshrama Estoril Centro do Yoga', 'Yoga')
)
SELECT p.id AS proposal_id, p.name AS proposal_name, p.sport AS sport_proposed,
       v.id AS existing_id, v.sports AS existing_sports,
       CASE WHEN v.id IS NULL THEN 'NO_EXACT_MATCH_REVIEW'
            WHEN v.sports IS NOT NULL AND BTRIM(v.sports) <> '' THEN 'ALREADY_HAS_SPORT_SKIP'
            ELSE 'ELIGIBLE_PENDING_VERIFICATION' END AS preview_status
FROM proposed p
LEFT JOIN desporto24.venue v
       ON v.id = p.id AND v.source_reference = p.source_reference
      AND v.name = p.name AND v.information_source = 'OFFICIAL'
ORDER BY p.id;
