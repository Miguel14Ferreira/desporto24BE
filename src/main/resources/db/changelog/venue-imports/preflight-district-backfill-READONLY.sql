-- 2 SELECT-only previews generated from the EXACT mappings in the proposed Liquibase update.
-- Run these after the baseline queries, before any merge. Do NOT execute the UPDATE migration.
-- Rows per locality represent records whose district would be filled, not new venues.

-- A) Main explicitly mapped municipalities / locality strings.
WITH district_by_city(city_key, district_value) AS (
  VALUES
      ('aveiro', 'Aveiro'),
      ('ílhavo', 'Aveiro'),
      ('ovar', 'Aveiro'),
      ('são joão da madeira', 'Aveiro'),
      ('santa maria da feira', 'Aveiro'),
      ('beja', 'Beja'),
      ('serpa', 'Beja'),
      ('moura', 'Beja'),
      ('castro verde', 'Beja'),
      ('braga', 'Braga'),
      ('guimarães', 'Braga'),
      ('barcelos', 'Braga'),
      ('vila nova de famalicão', 'Braga'),
      ('bragança', 'Bragança'),
      ('mirandela', 'Bragança'),
      ('macedo de cavaleiros', 'Bragança'),
      ('castelo branco', 'Castelo Branco'),
      ('covilhã', 'Castelo Branco'),
      ('fundão', 'Castelo Branco'),
      ('coimbra', 'Coimbra'),
      ('figueira da foz', 'Coimbra'),
      ('cantanhede', 'Coimbra'),
      ('évora', 'Évora'),
      ('mora', 'Évora'),
      ('estremoz', 'Évora'),
      ('montemor-o-novo', 'Évora'),
      ('vendas novas', 'Évora'),
      ('faro', 'Faro'),
      ('portimão', 'Faro'),
      ('loulé', 'Faro'),
      ('albufeira', 'Faro'),
      ('lagos', 'Faro'),
      ('tavira', 'Faro'),
      ('vila real de santo antónio', 'Faro'),
      ('guarda', 'Guarda'),
      ('seia', 'Guarda'),
      ('gouveia', 'Guarda'),
      ('trancoso', 'Guarda'),
      ('leiria', 'Leiria'),
      ('caldas da rainha', 'Leiria'),
      ('marinha grande', 'Leiria'),
      ('peniche', 'Leiria'),
      ('nazaré', 'Leiria'),
      ('alcobaça', 'Leiria'),
      ('lisboa', 'Lisboa'),
      ('cascais', 'Lisboa'),
      ('oeiras', 'Lisboa'),
      ('sintra', 'Lisboa'),
      ('amadora', 'Lisboa'),
      ('loures', 'Lisboa'),
      ('odivelas', 'Lisboa'),
      ('torres vedras', 'Lisboa'),
      ('mafra', 'Lisboa'),
      ('vila franca de xira', 'Lisboa'),
      ('alenquer', 'Lisboa'),
      ('portalegre', 'Portalegre'),
      ('elvas', 'Portalegre'),
      ('ponte de sor', 'Portalegre'),
      ('ponte de sôr', 'Portalegre'),
      ('sousel', 'Portalegre'),
      ('nisa', 'Portalegre'),
      ('campo maior', 'Portalegre'),
      ('porto', 'Porto'),
      ('vila nova de gaia', 'Porto'),
      ('matosinhos', 'Porto'),
      ('maia', 'Porto'),
      ('gondomar', 'Porto'),
      ('valongo', 'Porto'),
      ('póvoa de varzim', 'Porto'),
      ('vila do conde', 'Porto'),
      ('amarante', 'Porto'),
      ('santarém', 'Santarém'),
      ('tomar', 'Santarém'),
      ('torres novas', 'Santarém'),
      ('abrantes', 'Santarém'),
      ('ourém', 'Santarém'),
      ('entroncamento', 'Santarém'),
      ('almeirim', 'Santarém'),
      ('fátima', 'Santarém'),
      ('setúbal', 'Setúbal'),
      ('almada', 'Setúbal'),
      ('seixal', 'Setúbal'),
      ('barreiro', 'Setúbal'),
      ('montijo', 'Setúbal'),
      ('palmela', 'Setúbal'),
      ('sesimbra', 'Setúbal'),
      ('grândola', 'Setúbal'),
      ('sines', 'Setúbal'),
      ('costa da caparica', 'Setúbal'),
      ('pinhal novo', 'Setúbal'),
      ('viana do castelo', 'Viana do Castelo'),
      ('ponte de lima', 'Viana do Castelo'),
      ('valença', 'Viana do Castelo'),
      ('monção', 'Viana do Castelo'),
      ('caminha', 'Viana do Castelo'),
      ('vila real', 'Vila Real'),
      ('chaves', 'Vila Real'),
      ('peso da régua', 'Vila Real'),
      ('montalegre', 'Vila Real'),
      ('viseu', 'Viseu'),
      ('lamego', 'Viseu'),
      ('tondela', 'Viseu'),
      ('mangualde', 'Viseu'),
      ('são pedro do sul', 'Viseu')
)
SELECT 'mapped_municipality' AS mapping_group, v.city AS existing_city,
       m.district_value AS proposed_district, COUNT(*) AS venues_that_would_change
FROM desporto24.venue AS v
JOIN district_by_city AS m ON LOWER(BTRIM(v.city)) = m.city_key
WHERE v.district IS NULL OR BTRIM(v.district) = ''
GROUP BY v.city, m.district_value
ORDER BY venues_that_would_change DESC, existing_city, proposed_district;

-- B) Lisbon sub-municipal localities, strictly with GPS within the source SQL rectangle.
WITH lisboa_localities(city_key, district_value) AS (
  VALUES
      ('queluz', 'Lisboa'),
      ('estoril', 'Lisboa'),
      ('carcavelos', 'Lisboa'),
      ('alcabideche', 'Lisboa'),
      ('parede', 'Lisboa'),
      ('abóboda', 'Lisboa'),
      ('rio de mouro', 'Lisboa'),
      ('agualva-cacém', 'Lisboa'),
      ('cacém', 'Lisboa'),
      ('são domingos de rana', 'Lisboa')
)
SELECT 'lisbon_geo_limited' AS mapping_group, v.city AS existing_city,
       m.district_value AS proposed_district, COUNT(*) AS venues_that_would_change
FROM desporto24.venue AS v
JOIN lisboa_localities AS m ON LOWER(BTRIM(v.city)) = m.city_key
WHERE (v.district IS NULL OR BTRIM(v.district) = '')
  AND v.latitude BETWEEN 38.60 AND 38.91
  AND v.longitude BETWEEN -9.62 AND -9.04
GROUP BY v.city, m.district_value
ORDER BY venues_that_would_change DESC, existing_city, proposed_district;
