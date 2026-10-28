# Jynlo — medição agregada do catálogo público

Workflow: Actions → Jynlo live public venue coverage snapshot (read-only).
Permite execução manual; também executa uma vez durante o PR inicial.
Apenas consulta o endpoint público fixo https://api.d24sports.com/api/venues.

Valida 9 testes Python offline e executa o auditor atual de cobertura
distrital, seguido do planeador #498. Só produz dois JSON agregados:
contagem de registos existentes, GPS/modalidades/cidade em falta, por
18 distritos, Açores, Madeira e distrito por confirmar. Não grava
o array de recintos, nomes, GPS individuais, moradas ou contactos.

Os resultados são guardados como artefacto GitHub Actions por 3 dias.
A hora UTC está no resumo do job. Se falhar o GET, parse ou validação,
o job falha (não inventar contagens). Nunca alterar o allowlist do
PostgreSQL para executar este relatório.

**Importante:** a contagem do catálogo público não mede o universo de
recintos de Portugal, nem autoriza importação. Um distrito com zero
registos precisa de investigação, não de uma inserção automática.

Antes de acrescentar qualquer lote: confirmar fontes municipais e
SNID/IPDJ, licenças de reutilização, coordenadas oficiais, modalidades
e acesso público; comparar nomes, aliases e GPS com catálogo LIVE;
validar o preflight no PostgreSQL autorizado e backups.

Referências oficiais:
- https://ipdj.gov.pt/sistema-nacional-de-informa%C3%A7%C3%A3o-desportiva-snid-
- https://ipdj.gov.pt/dados-abertos-desporto
