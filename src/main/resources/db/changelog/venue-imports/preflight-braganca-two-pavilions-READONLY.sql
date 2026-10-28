-- READ-ONLY PostgreSQL preflight for an authorized DB connection.
-- Output is existing venue identities / documented public coordinates only.
SELECT v.id,v.name,v.city,v.district,v.latitude,v.longitude,
       v.information_source,v.source_reference
FROM desporto24.venue v
WHERE LOWER(BTRIM(COALESCE(v.district,'')))=LOWER('Bragança')
AND (LOWER(COALESCE(v.name,'')) LIKE '%arnaldo%'
     OR LOWER(COALESCE(v.name,'')) LIKE '%coxa%'
     OR (LOWER(BTRIM(COALESCE(v.name,''))) IN
            ('pavilhão municipal','pavilhao municipal',
             'pavilhão municipal de bragança','pavilhao municipal de braganca',
             'pavilhão gimnodesportivo','pavilhao gimnodesportivo',
             'pavilhão gimnodesportivo de bragança',
             'pavilhao gimnodesportivo de braganca')
         AND (LOWER(BTRIM(COALESCE(v.city,''))) IN
            ('bragança','braganca','sé','se','santa maria','meixedo',
             'união das freguesias de sé, santa maria e meixedo')
              OR LOWER(COALESCE(v.address,'')) LIKE '%sancho i%'
              OR LOWER(COALESCE(v.address,'')) LIKE '%zona desportiva%'))
     OR (v.latitude IS NOT NULL AND v.longitude IS NOT NULL
       AND ((ABS(v.latitude-41.804841855561804)<0.003
             AND ABS(v.longitude-(-6.770163826983646))<0.004)
         OR (ABS(v.latitude-41.801816342807)<0.003
             AND ABS(v.longitude-(-6.7712549306452))<0.004))))
ORDER BY v.city,v.name,v.id;
