# Recintos incompletos: auditoria administrativa por localidade

A avaliação do Pestana (email «Descobrir», 15/10/2026) identificou 169 dos 228 recintos piloto sem modalidade e diversos concelhos guardados como freguesias.

`GET /api/admin/venues/quality?limit=50` devolve contagens agregadas e limitadas por localidade de `city` original: `total`, `withoutSports`, `withoutDistrict` e `withoutCoordinates`; as `cities` são ordenadas por número sem modalidade. O endpoint exige ADMIN ou SUPER_ADMIN, é somente de leitura e não devolve nomes de recintos, IDs de perfis ou GPS.

O exemplo «169/228» é histórico e **não representa uma medição LIVE atual**. Testar com H2, depois num PostgreSQL 16 e navegador/admin autenticado. O objetivo é medir uma baseline real, preparar correções Liquibase validadas e repetir a medição. Não inferir modalidades de recintos sem dados independentes.

Este PR é um diagnóstico separado, sem migração, escrita, merge ou deploy.
