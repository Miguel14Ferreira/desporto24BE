-- READ-ONLY PostgreSQL preflight for an authorized DB connection.
-- Output is existing venue identities / documented public coordinates only.
SELECT v.id,v.name,v.city,v.district,v.latitude,v.longitude,
       v.information_source,v.source_reference
FROM desporto24.venue v
WHERE LOWER(BTRIM(COALESCE(v.district,'')))=LOWER('Bragança')
AND (LOWER(COALESCE(v.name,'')) LIKE '%arnaldo%'
     OR LOWER(COALESCE(v.name,'')) LIKE '%coxa%'
     OR (v.latitude IS NOT NULL AND v.longitude IS NOT NULL
       AND ((ABS(v.latitude-41.804841855561804)<0.003
             AND ABS(v.longitude-(-6.770163826983646))<0.004)
         OR (ABS(v.latitude-41.801816342807)<0.003
             AND ABS(v.longitude-(-6.7712549306452))<0.004))))
ORDER BY v.city,v.name,v.id;
