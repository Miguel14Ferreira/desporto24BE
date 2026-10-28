# Jynlo — plano offline de revisão por distrito (28/10/2026)

Este procedimento usa **apenas contagens do catálogo público existente**,
não o total de instalações desportivas reais de Portugal. Não mede a
percentagem de cobertura, não confirma a disponibilidade para reservas e
não confunde candidatos OSM com recintos aprovados.

## Gerar uma fotografia de leitura apenas

A auditoria de catálogo já existente pode usar um JSON guardado, sem rede:

~~~bash
python3 scripts/audit_live_venue_district_coverage.py \
  --venues-json /dados/venues-publicos.json \
  --output-json /dados/cobertura-por-distrito.json

python3 scripts/plan_national_venue_coverage.py \
  --coverage /dados/cobertura-por-distrito.json \
  --out /dados/plano-revisao.json
~~~

A ferramenta de auditoria original também permite --live-public-api para
uma leitura do catálogo público, mas o planeador **nunca comunica com a API**.

Para definir explicitamente uma área a examinar (não é uma importação):

~~~bash
python3 scripts/plan_national_venue_coverage.py \
  --coverage /dados/cobertura-por-distrito.json \
  --out /dados/plano-lisboa.json --pilot Lisboa
~~~

O plano lista os 18 distritos continentais, Açores, Madeira e a categoria
«por confirmar», mantendo-a separada. Indica a contagem catalogada e as
contagens sem modalidade, GPS e cidade — que podem sobrepor-se no mesmo
recinto, pelo que **não podem ser somadas como recintos únicos**.

A ordem dos distritos com menos registos é apenas uma sugestão de **onde
investigar o catálogo**, não um ranking da oferta desportiva real. Zero
registos não é prova de ausência de recintos. O relatório de saída não inclui
nomes de cidades ou recintos, contactos, moradas nem coordenadas.

O piloto selecionado tem bbox=null, approvalForImport=false e
authorization=READ_ONLY_NO_IMPORT. Nunca faz download OSM, deduplicação
contra PostgreSQL, aprovação, POST ou alterações ao Render.

## Critérios antes de importar qualquer lote futuro

Validar proveniência/licença (OSM, SNID ou fonte municipal), modalidade
verificada, GPS oficial, existência física, acessibilidade real, falsos
duplicados e nomes alternativos. Confirmar pré-voo read-only na base de
dados autorizada e backup/recuperação; só uma implementação separada,
revista e explicitamente aprovada poderá importar dados.

O GitHub Actions nacional offline executa os testes Python da auditoria
e do planeador, sem tokens, serviços externos ou Maven. Este PR adiciona
apenas scripts, testes, documentação e CI. Não requer deploy do Render,
porque nenhum código executável do backend é alterado.
