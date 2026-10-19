-- JYNLO: PRE-DEPLOY DIAGNOSIS. SELECT ONLY. No production writes.
-- Run using your own PostgreSQL client and export only the result grids.
-- Never share DB passwords, full connection strings, or access tokens.

-- 1: Overall venue counts
SELECT COUNT(*) AS total_recintos,
 COUNT(*) FILTER (WHERE sports IS NULL OR BTRIM(sports)='') AS sem_modalidade,
 COUNT(*) FILTER (WHERE district IS NULL OR BTRIM(district)='') AS sem_distrito,
 COUNT(*) FILTER (WHERE latitude IS NULL OR longitude IS NULL) AS sem_coordenadas,
 COUNT(*) FILTER (WHERE information_source='OPENSTREETMAP') AS origem_osm,
 COUNT(*) FILTER (WHERE information_source='OFFICIAL') AS origem_oficial,
 COUNT(*) FILTER (WHERE information_source='COMMUNITY') AS origem_comunidade
FROM desporto24.venue;

-- 2: Coverage by district
SELECT COALESCE(NULLIF(BTRIM(district),''),'(sem distrito)') AS distrito,
 COUNT(*) AS total_recintos,
 COUNT(*) FILTER (WHERE sports IS NULL OR BTRIM(sports)='') AS sem_modalidade,
 COUNT(*) FILTER (WHERE latitude IS NULL OR longitude IS NULL) AS sem_coordenadas
FROM desporto24.venue
GROUP BY COALESCE(NULLIF(BTRIM(district),''),'(sem distrito)')
ORDER BY total_recintos DESC, distrito LIMIT 30;

-- 3: Most incomplete legacy city/parish strings
SELECT COALESCE(NULLIF(BTRIM(city),''),'(sem localidade)') AS localidade,
 COUNT(*) AS total_recintos,
 COUNT(*) FILTER (WHERE sports IS NULL OR BTRIM(sports)='') AS sem_modalidade,
 COUNT(*) FILTER (WHERE district IS NULL OR BTRIM(district)='') AS sem_distrito
FROM desporto24.venue
GROUP BY COALESCE(NULLIF(BTRIM(city),''),'(sem localidade)')
ORDER BY sem_modalidade DESC, total_recintos DESC, localidade LIMIT 30;

-- 4: Find Liquibase tracking tables; no changes to migrations
SELECT table_schema, table_name
FROM information_schema.tables
WHERE LOWER(table_name) IN ('databasechangelog','databasechangeloglock')
ORDER BY table_schema,table_name;
