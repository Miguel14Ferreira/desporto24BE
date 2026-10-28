-- DRAFT OPT-IN ONLY: not in db.changelog-master.yaml, never production auto-run.
-- DRAFT source: Regulamento Municipal n.º 977/2026 (23 July 2026).
-- Annex H-1/I gives official IDs (Arnaldo 14242, Coxa 13288),
-- exact addresses, officially supported sports and access conditions.
-- https://diariodarepublica.pt/dr/detalhe/regulamento/977-2026-1151247864
-- Pavilion venue booking requires municipal PRE-AUTHORIZATION and fee.
-- A Jynlo session must NEVER imply the pavilion is booked/available.
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
 'Avenida D. Sancho I, Zona Desportiva, 5300-125 Bragança',
 'Futsal;Andebol;Voleibol;Basquetebol;Hóquei em patins',
 'Pavilhão desportivo','INDOOR',
 41.804841855561804,-6.770163826983646,
 'OFFICIAL','JYNLO:OFFICIAL:BRAGANCA:ARNALDO-PEREIRA',NULL,NULL,
 TRUE,FALSE,FALSE,FALSE,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
 SELECT 1 FROM desporto24.venue v
 WHERE v.source_reference='JYNLO:OFFICIAL:BRAGANCA:ARNALDO-PEREIRA'
 OR (LOWER(BTRIM(COALESCE(v.district,'')))=LOWER('Bragança')
   AND (LOWER(COALESCE(v.name,'')) LIKE '%arnaldo pereira%'
   -- The municipality RENAMED the old "Pavilhão Municipal" on 2015-02-09.
   -- Without this check, a legacy COMMUNITY row without GPS would be duplicated.
   OR (LOWER(BTRIM(COALESCE(v.name,''))) IN
         ('pavilhão municipal', 'pavilhao municipal',
          'pavilhão municipal de bragança', 'pavilhao municipal de braganca',
          'pavilhão gimnodesportivo', 'pavilhao gimnodesportivo',
          'pavilhão gimnodesportivo de bragança',
          'pavilhao gimnodesportivo de braganca')
       AND (LOWER(BTRIM(COALESCE(v.city,''))) IN
          ('bragança', 'braganca', 'sé', 'se', 'santa maria', 'meixedo',
           'união das freguesias de sé, santa maria e meixedo')
         OR LOWER(COALESCE(v.address,'')) LIKE '%sancho i%'
         OR LOWER(COALESCE(v.address,'')) LIKE '%zona desportiva%'))
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
 'Rua Padre Benjamim Videira Pires, s/n, 5300-464 Bragança',
 'Futsal;Andebol;Voleibol;Basquetebol;Ténis','Pavilhão desportivo','INDOOR',
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
