-- Fixed official venues belong in database seed/reference data, not Java startup code.
-- Idempotent through portable INSERT ... SELECT ... WHERE NOT EXISTS statements.

INSERT INTO venue (
    name,
    city,
    district,
    address,
    sports,
    venue_type,
    ownership_type,
    environment_type,
    indoor,
    changing_rooms,
    toilets,
    showers,
    stands,
    cafe,
    parking,
    lighting,
    latitude,
    longitude,
    website,
    contact,
    opening_hours,
    information_source,
    source_reference,
    created_at
)
SELECT
    'Clube Nacional de Ginástica - Parque Desportivo',
    'Parede',
    'Lisboa',
    'Rua Machado dos Santos, 112, 2775-236 Parede',
    'Futebol;Futsal;Ginásio;Ginástica;Ginástica Rítmica;Judo;Karaté;Natação;Pilates;Taekwondo;Ténis;Voleibol;Yoga',
    'Complexo desportivo',
    'PRIVATE',
    'BOTH',
    TRUE,
    TRUE,
    NULL,
    NULL,
    NULL,
    NULL,
    FALSE,
    TRUE,
    38.6895611,
    -9.3474667,
    'https://cng.pt/',
    '214 571 010',
    'Segunda a sábado 08:00-23:00; domingo e feriados 08:00-21:00',
    'OFFICIAL',
    'OFFICIAL:CNG:PAREDE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM venue
    WHERE information_source = 'OFFICIAL'
      AND source_reference = 'OFFICIAL:CNG:PAREDE'
);

INSERT INTO venue (
    name,
    city,
    district,
    address,
    sports,
    venue_type,
    ownership_type,
    environment_type,
    indoor,
    changing_rooms,
    toilets,
    showers,
    stands,
    cafe,
    parking,
    lighting,
    latitude,
    longitude,
    website,
    contact,
    opening_hours,
    information_source,
    source_reference,
    created_at
)
SELECT
    'Complexo Desportivo Municipal da Abóboda',
    'São Domingos de Rana',
    'Lisboa',
    'Rua Mouzinho de Albuquerque, 186, 2785-032 São Domingos de Rana',
    'Natação;Pólo Aquático;Hidroginástica;Ginásio;Pilates;Yoga;Taekwondo;Jiu-Jitsu;Boxe;Kickboxing',
    'Complexo desportivo',
    'PUBLIC',
    'INDOOR',
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    38.7263,
    -9.3307,
    'https://complexodesportivoaboboda.com/',
    '+351 214 489 720',
    'Segunda a sexta 07:00-22:00; sábado 08:00-20:00; domingo piscina 08:00-14:00',
    'OFFICIAL',
    'OFFICIAL:CASCAIS:CDM-ABOBODA',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM venue
    WHERE information_source = 'OFFICIAL'
      AND source_reference = 'OFFICIAL:CASCAIS:CDM-ABOBODA'
);
