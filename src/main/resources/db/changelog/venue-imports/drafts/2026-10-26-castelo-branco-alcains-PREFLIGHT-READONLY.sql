-- SELECT ONLY - draft/production preflight for two proposed source-backed venues.
-- Intentionally broad aliases; reviewer must adjudicate all records found.
SELECT id, name, city, district, address, information_source, source_reference
FROM desporto24.venue
WHERE source_reference IN (
   'JYNLO:OFFICIAL:CASTELO-BRANCO:PAVILHAO-ROTARY',
   'JYNLO:OFFICIAL:ALBIGEC:ALCAINS-POOLS'
)
OR (
 LOWER(COALESCE(city, '')) = 'castelo branco'
 AND (LOWER(COALESCE(name, '')) LIKE '%pavilh%'
      OR LOWER(COALESCE(address, '')) LIKE '%rotary%')
)
OR (
 LOWER(COALESCE(name, '')) LIKE '%alcains%'
 AND (LOWER(COALESCE(name, '')) LIKE '%piscin%'
      OR LOWER(COALESCE(name, '')) LIKE '%aquatic%')
)
OR (
 LOWER(COALESCE(city, '')) = 'alcains'
 AND (LOWER(COALESCE(name, '')) LIKE '%piscin%'
      OR LOWER(COALESCE(address, '')) LIKE '%estrada de santa%')
)
ORDER BY city, name, id;
