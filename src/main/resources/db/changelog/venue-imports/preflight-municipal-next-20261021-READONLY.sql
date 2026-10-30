-- READ ONLY: preflight for exactly 32 staged facilities, not proof of all aliases.
-- Executable with PGOPTIONS='-c default_transaction_read_only=on'.
WITH proposed (reference, name, city, district, address) AS (
 VALUES
 ('JYNLO:MUNICIPAL:SETUBAL:BMX', 'Pista Municipal de BMX de Setúbal', 'Setúbal', 'Setúbal', 'Avenida da Bela Vista, 2900 Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:ATLETISMO', 'Complexo Municipal de Atletismo de Setúbal', 'Setúbal', 'Setúbal', 'Estrada Vale da Rosa, 2910 Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:ARRABIDA_PADEL', 'Arrábida Padel', 'Azeitão', 'Setúbal', 'Azeitão, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:PISCINA_AZEITAO', 'Piscina Municipal de Azeitão', 'Azeitão', 'Setúbal', 'Rua Dr. Agostinho Machado Faria, 2925-499 Vila Nogueira de Azeitão'),
 ('JYNLO:MUNICIPAL:SETUBAL:PISCINA_PALMEIRAS', 'Piscina Municipal das Palmeiras', 'Setúbal', 'Setúbal', 'Setúbal (morada específica por confirmar)'),
 ('JYNLO:MUNICIPAL:SETUBAL:PISCINAS_MANTEIGADAS', 'Complexo Municipal de Piscinas das Manteigadas', 'Setúbal', 'Setúbal', 'Avenida Emídio Graça, 2900 Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:PAV_ARANGUEZ', 'Pavilhão Desportivo Municipal de Aranguez', 'Setúbal', 'Setúbal', 'Escola Básica de Aranguez, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:PAV_MANTEIGADAS', 'Pavilhão Desportivo Municipal das Manteigadas', 'Setúbal', 'Setúbal', 'Manteigadas, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:PAV_JOAO_SANTOS', 'Pavilhão Escolar Municipal João dos Santos', 'Setúbal', 'Setúbal', 'Escola Secundária Lima de Freitas, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:PARQUE_VERDE_BELA_VISTA', 'Complexo Desportivo Municipal do Parque Verde da Bela Vista', 'Setúbal', 'Setúbal', 'Parque Verde da Bela Vista, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:VITOR_BATISTA', 'Campo Municipal Vítor Batista', 'Setúbal', 'Setúbal', 'Bairro da Liberdade, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:FORTE_BELA_VISTA', 'Campo Municipal de Jogos Forte da Bela Vista', 'Setúbal', 'Setúbal', 'Bairro da Bela Vista, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:HERCULANO_SANTOS', 'Campo Municipal de Futebol Herculano Santos', 'Setúbal', 'Setúbal', 'Pedreiras do Viso, Setúbal'),
 ('JYNLO:MUNICIPAL:SETUBAL:VARZEA', 'Complexo Desportivo Municipal da Várzea', 'Setúbal', 'Setúbal', 'Azinhaga de São Joaquim, Setúbal'),
 ('JYNLO:MUNICIPAL:LEIRIA:PISCINAS_CIDADE', 'Complexo Municipal de Piscinas de Leiria', 'Leiria', 'Leiria', 'Largo da Feira, Arrabalde d''Aquém, Leiria'),
 ('JYNLO:MUNICIPAL:LEIRIA:PISCINA_CARANGUEJEIRA', 'Piscina Municipal de Caranguejeira', 'Caranguejeira', 'Leiria', 'Rua de São Vicente, Caranguejeira'),
 ('JYNLO:MUNICIPAL:LEIRIA:PISCINA_MACEIRA', 'Piscina Municipal de Maceira', 'Maceira', 'Leiria', 'Rua Dr. Carlos Ponte Leça, Maceira'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_ARRABAL', 'Pavilhão Desportivo de Arrabal', 'Arrabal', 'Leiria', 'Rua José Bernardino Crespo, n.º 25, Arrabal'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_BAJOUCA', 'Pavilhão Desportivo de Bajouca', 'Bajouca', 'Leiria', 'Largo dos Treze, Rua da Mãe Preciosa, Bajouca'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_CARANGUEJEIRA', 'Pavilhão Desportivo de Caranguejeira', 'Caranguejeira', 'Leiria', 'Rua de São Vicente, Caranguejeira'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_CARREIRA', 'Pavilhão Desportivo de Carreira', 'Carreira', 'Leiria', 'Rua da Quinta, Carreira'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_COLMEIAS', 'Pavilhão Desportivo de Colmeias', 'Colmeias', 'Leiria', 'Rua da Escola da Eira Velha, Colmeias'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_CORREIA_MATEUS', 'Pavilhão Desportivo Dr. Correia Mateus', 'Leiria', 'Leiria', 'Rua Paulo VI, Leiria'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_MACEIRA', 'Pavilhão Desportivo de Maceira', 'Maceira', 'Leiria', 'Rua Dr. Carlos Ponte Leça, Maceira'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_POUSOS', 'Pavilhão Desportivo de Pousos', 'Pousos', 'Leiria', 'Rua Virgílio Monteiro, Charneca do Bailadouro, Pousos'),
 ('JYNLO:MUNICIPAL:LEIRIA:TENIS_RUI_FONSECA', 'Complexo Municipal de Ténis Dr. Rui Garcia da Fonseca', 'Pousos', 'Leiria', 'Pousos, Leiria'),
 ('JYNLO:MUNICIPAL:LEIRIA:PAV_LIS', 'Pavilhão Municipal do Lis', 'Cortes', 'Leiria', 'Cortes, Leiria'),
 ('JYNLO:MUNICIPAL:LEIRIA:CENTRO_LANCAMENTOS', 'Centro Nacional de Lançamentos de Leiria', 'Leiria', 'Leiria', 'Rotunda Almoínha Grande, Leiria'),
 ('JYNLO:MUNICIPAL:LEIRIA:ESTADIO_MAGALHAES_PESSOA', 'Estádio Municipal de Leiria Dr. Magalhães Pessoa', 'Leiria', 'Leiria', 'Leiria (morada específica por confirmar)'),
 ('JYNLO:MUNICIPAL:BRAGANCA:PAV_ARANLDO_PEREIRA', 'Pavilhão Municipal Arnaldo Pereira', 'Bragança', 'Bragança', 'Parque Desportivo de Bragança, Bragança'),
 ('JYNLO:MUNICIPAL:BRAGANCA:ESTADIO_JOSE_LUIS_PINHEIRO', 'Estádio Municipal Eng.º José Luís Pinheiro', 'Bragança', 'Bragança', 'Parque Desportivo de Bragança, Bragança'),
 ('JYNLO:MUNICIPAL:BRAGANCA:PISCINAS_MUNICIPAIS', 'Piscinas Municipais de Bragança', 'Bragança', 'Bragança', 'Parque Desportivo de Bragança, Bragança')
)
SELECT p.district, p.city, p.name, p.reference,
 COALESCE(e.match_reason, 'NO_MATCH_FOUND') AS match_reason,
 e.id AS existing_id, e.name AS existing_name, e.city AS existing_city, e.address AS existing_address, e.source_reference AS existing_reference
FROM proposed p
LEFT JOIN LATERAL (
 SELECT v.id,v.name,v.city,v.address,v.source_reference,
 CASE WHEN v.source_reference=p.reference THEN 'SAME_SOURCE_REFERENCE_REVIEW'
      WHEN LOWER(BTRIM(v.name))=LOWER(BTRIM(p.name)) AND LOWER(BTRIM(v.city))=LOWER(BTRIM(p.city))
      THEN 'SAME_NAME_CITY_REVIEW'
      ELSE 'SAME_ADDRESS_CITY_REVIEW' END AS match_reason
 FROM desporto24.venue v
 WHERE v.source_reference=p.reference
    OR (LOWER(BTRIM(v.name))=LOWER(BTRIM(p.name)) AND LOWER(BTRIM(v.city))=LOWER(BTRIM(p.city)))
    OR (LOWER(BTRIM(v.address))=LOWER(BTRIM(p.address)) AND LOWER(BTRIM(v.city))=LOWER(BTRIM(p.city)))
 ORDER BY v.id LIMIT 15
) e ON TRUE
ORDER BY p.district,p.city,p.name,existing_id;
