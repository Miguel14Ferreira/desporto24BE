-- DRAFT: opt-in one-time backfill for EXISTING venue rows with missing district.
-- No insert or delete, never overwrite nonempty district values.
-- Source: known Portuguese municipality names and limited high-confidence localities.
-- Ambiguous settlement names not in mapping are intentionally untouched.
-- This is a partial fix, not a substitute for geometric CAOP2025 validation.
--
-- Rows affected can be inspected safely before activation using:
-- SELECT city, COUNT(*) FROM desporto24.venue
-- WHERE district IS NULL OR BTRIM(district) = ''
-- GROUP BY city ORDER BY COUNT(*) DESC;
--
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
UPDATE desporto24.venue AS v
SET district = m.district_value
FROM district_by_city AS m
WHERE (v.district IS NULL OR BTRIM(v.district) = '')
  AND LOWER(BTRIM(v.city)) = m.city_key;

-- Smaller settlement names require coordinates within greater Lisbon region,
-- preventing obvious same-name locations elsewhere from being misclassified.
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
UPDATE desporto24.venue AS v
SET district = m.district_value
FROM lisboa_localities AS m
WHERE (v.district IS NULL OR BTRIM(v.district) = '')
  AND LOWER(BTRIM(v.city)) = m.city_key
  AND v.latitude BETWEEN 38.60 AND 38.91
  AND v.longitude BETWEEN -9.62 AND -9.04;
