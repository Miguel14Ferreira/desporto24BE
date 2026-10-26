-- DRAFT PRE-FLIGHT / READ-ONLY. Must run against LIVE DB before publication.
-- Empty results are necessary but do not prove absence of close aliases.
-- Covers same source, same street, distinctive identity token and exact name.
SELECT id, name, city, district, address, information_source, source_reference
FROM desporto24.venue
WHERE source_reference = 'JYNLO:OFFICIAL:SANTAREM:MEM-RAMIRES'
   OR (
     LOWER(BTRIM(city)) = LOWER(BTRIM('Santarém'))
     AND (
       LOWER(BTRIM(name)) = LOWER(BTRIM('Pavilhão da EB 2/3 Mem Ramires'))
       OR LOWER(COALESCE(name, '')) LIKE '%ramires%'
       OR LOWER(COALESCE(address, '')) LIKE '%virgílio arruda%'
       OR LOWER(BTRIM(COALESCE(address, ''))) =
          LOWER(BTRIM('Rua Doutor Virgílio Arruda, 2000-217 Santarém'))
     )
   )
ORDER BY id;
