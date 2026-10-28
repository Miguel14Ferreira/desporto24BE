-- READ-ONLY PostgreSQL preflight for an authorized DB connection.
-- Includes historic municipal aliases with missing district and GPS.
-- May return independent facilities; a result is a REVIEW lead, not proof.
SELECT v.id,v.name,v.city,v.district,v.latitude,v.longitude,
       v.information_source,v.source_reference
FROM desporto24.venue v
WHERE v.source_reference IN (
   'JYNLO:OFFICIAL:BRAGANCA:ARNALDO-PEREIRA',
   'JYNLO:OFFICIAL:BRAGANCA:COXA'
)
OR (
  (LOWER(BTRIM(COALESCE(v.district,'')))='bragança'
    OR (BTRIM(COALESCE(v.district,''))=''
      AND (LOWER(BTRIM(COALESCE(v.city,''))) IN
             ('bragança','braganca','sé','se','santa maria','meixedo',
              'união das freguesias de sé, santa maria e meixedo')
        OR LOWER(COALESCE(v.address,'')) LIKE '%sancho i%'
        OR LOWER(COALESCE(v.address,'')) LIKE '%zona desportiva%'
        OR LOWER(COALESCE(v.address,'')) LIKE '%padre benjamim videira pires%'
        OR LOWER(COALESCE(v.address,'')) LIKE '%bairro da coxa%')))
  AND (
    LOWER(COALESCE(v.name,'')) LIKE '%arnaldo%'
    OR LOWER(COALESCE(v.name,'')) LIKE '%coxa%'
    OR (LOWER(BTRIM(COALESCE(v.name,''))) IN (
           'pavilhão municipal','pavilhao municipal',
           'pavilhão municipal de bragança','pavilhao municipal de braganca',
           'pavilhão gimnodesportivo','pavilhao gimnodesportivo',
           'pavilhão gimnodesportivo de bragança',
           'pavilhao gimnodesportivo de braganca')
        AND (LOWER(BTRIM(COALESCE(v.city,''))) IN (
             'bragança','braganca','sé','se','santa maria','meixedo',
             'união das freguesias de sé, santa maria e meixedo')
          OR LOWER(COALESCE(v.address,'')) LIKE '%sancho i%'
          OR LOWER(COALESCE(v.address,'')) LIKE '%zona desportiva%'))
    OR (v.latitude IS NOT NULL AND v.longitude IS NOT NULL
      AND ((ABS(v.latitude-41.804841855561804)<0.003
            AND ABS(v.longitude-(-6.770163826983646))<0.004)
        OR (ABS(v.latitude-41.801816342807)<0.003
            AND ABS(v.longitude-(-6.7712549306452))<0.004)))
  )
)
ORDER BY v.city,v.name,v.id;
