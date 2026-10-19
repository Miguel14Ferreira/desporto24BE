-- JYNLO: Read-only inventory of venues with missing sports in the current catalogue.
-- Use in PostgreSQL with a READ-ONLY role. No data updates, no external imports.
-- Only venue attributes, no user/profile/contact columns.
SELECT v.id,
       v.name,
       v.city,
       v.district,
       v.address,
       v.venue_type,
       v.information_source,
       v.source_reference,
       v.latitude,
       v.longitude
FROM desporto24.venue AS v
WHERE v.sports IS NULL OR BTRIM(v.sports) = ''
ORDER BY LOWER(v.city) NULLS LAST, LOWER(v.name), v.id;
