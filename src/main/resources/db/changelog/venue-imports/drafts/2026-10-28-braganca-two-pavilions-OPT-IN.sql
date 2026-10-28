-- DRAFT OPT-IN ONLY: not in db.changelog-master.yaml, never production auto-run.
-- Independent municipal identities: https://diariodarepublica.pt/dr/detalhe/anuncio-procedimento/19668-2024-888585905
-- Arnaldo historic complex site point, NOT entrance: https://arquitecturaaqui.eu/pt/obras/4824/pavilhao-municipal-arnaldo-pereira-braganca
-- Coxa contractor 2019 building site point, NOT entrance: https://www.alnconstrucoes.com/obras/2019/pavilhao-polidesportivo-da-coxa-braganca
-- Sports used by clubs: https://estrelasbrigantinas.com/contactos
-- No contacts, prices, opening hours, reservations or exact-entrance claims.

INSERT INTO desporto24.venue
(name,city,district,address,sports,venue_type,environment_type,
 latitude,longitude,information_source,source_reference,website,contact,
 indoor,changing_rooms,parking,lighting,created_at)
SELECT
 'Pavilhão Municipal Arnaldo Pereira','Bragança','Bragança',
 'Avenida Dom Sancho I, Zona Desportiva, Bragança','Basquetebol;Futsal',
 'Pavilhão desportivo','INDOOR',
 41.804841855561804,-6.770163826983646,
 'OFFICIAL','JYNLO:OFFICIAL:BRAGANCA:ARNALDO-PEREIRA',NULL,NULL,
 TRUE,FALSE,FALSE,FALSE,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
 SELECT 1 FROM desporto24.venue v
 WHERE v.source_reference='JYNLO:OFFICIAL:BRAGANCA:ARNALDO-PEREIRA'
 OR (LOWER(BTRIM(COALESCE(v.district,'')))=LOWER('Bragança')
   AND (LOWER(COALESCE(v.name,'')) LIKE '%arnaldo pereira%'
   OR (v.latitude IS NOT NULL AND v.longitude IS NOT NULL
     AND ABS(v.latitude-41.804841855561804)<=0.0006
     AND ABS(v.longitude-(-6.770163826983646))<=0.00075
     AND (LOWER(COALESCE(v.name,'')) LIKE '%pavilh%'
       OR LOWER(COALESCE(v.name,'')) LIKE '%gimnodesport%'))))
)
ON CONFLICT DO NOTHING;

INSERT INTO desporto24.venue
(name,city,district,address,sports,venue_type,environment_type,
 latitude,longitude,information_source,source_reference,website,contact,
 indoor,changing_rooms,parking,lighting,created_at)
SELECT
 'Pavilhão Polidesportivo da Coxa','Bragança','Bragança',
 'Rua Padre Benjamim Videira Pires, Bairro da Coxa, Bragança',
 'Basquetebol;Futsal','Pavilhão desportivo','INDOOR',
 41.801816342807,-6.7712549306452,
 'OFFICIAL','JYNLO:OFFICIAL:BRAGANCA:COXA',NULL,NULL,
 TRUE,FALSE,FALSE,FALSE,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
 SELECT 1 FROM desporto24.venue v
 WHERE v.source_reference='JYNLO:OFFICIAL:BRAGANCA:COXA'
 OR (LOWER(BTRIM(COALESCE(v.district,'')))=LOWER('Bragança')
   AND ((LOWER(COALESCE(v.name,'')) LIKE '%coxa%'
        AND (LOWER(COALESCE(v.name,'')) LIKE '%pavilh%'
          OR LOWER(COALESCE(v.name,'')) LIKE '%gimnodesport%'))
   OR (v.latitude IS NOT NULL AND v.longitude IS NOT NULL
     AND ABS(v.latitude-41.801816342807)<=0.0006
     AND ABS(v.longitude-(-6.7712549306452))<=0.00075
     AND (LOWER(COALESCE(v.name,'')) LIKE '%pavilh%'
       OR LOWER(COALESCE(v.name,'')) LIKE '%gimnodesport%'))))
)
ON CONFLICT DO NOTHING;
