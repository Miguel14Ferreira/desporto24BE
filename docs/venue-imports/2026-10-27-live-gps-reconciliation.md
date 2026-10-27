# Revisão GPS do catálogo Jynlo — reconciliação com a API LIVE (27/10/2026)

A auditoria cruzou a informação **read-only** da API pública da Jynlo com os cinco candidatos já documentados em dois manifestos oficiais. Os manifestos foram produzidos anteriormente; **não são ordens de atualização**.

## Confirmação na API pública
- ID **296**, Piscinas Municipais de Viseu: GPS existente `40.65985,-7.903158333333334`, igual ao documento municipal.
- ID **297**, Campos de Ténis do Parque do Fontelo: GPS existente `40.660330555555554,-7.9012472222222225`, correspondente ao primeiro dos **três** pontos GPS indicados na ficha municipal. Não atribuir automaticamente este ponto a cada campo de ténis individual.
- ID **298**, Piscinas Municipais da Guarda: GPS existente `40.531141,-7.253715`. A morada da API é `6300-320`; os manifestos mais antigos mencionavam uma divergência com `6300-509`. Não reabrir conflito sem prova atual.
- ID **287**, Pista de Atletismo do Estádio 1.º de Maio (Braga): **sem GPS**. A coordenada documental é do estádio e não demonstra a posição exata da pista.
- Complexo Desportivo de Évora: a pesquisa exata `/api/venues/suggest?name=...` não devolveu correspondência; não prova ausência de alias ou do recinto, pelo que é necessário confirmar no catálogo completo. O documento indica waypoint de prova (2022), não entrada atual.
- Fora do lote de cinco: ID **283** Campo Municipal do Outeiro (Porto) e ID **277** Pavilhão Desportivo Municipal (Santarém) não apresentaram GPS nas pesquisas públicas. As sugestões SNID anteriores tinham conflitos de identidade/localização, e não devem ser aplicadas.

## Executar a auditoria novamente (sem alterações à BD)

```bash
python3 -m unittest discover -s scripts -p 'test_audit_live_venue_gps_evidence.py'
python3 scripts/audit_live_venue_gps_evidence.py --live-public-api --output /tmp/jynlo-venue-gps-review.json
```

Ou passar um snapshot público via `--venues-json` para uma execução totalmente offline. O script usa exclusivamente `GET /api/venues`, com origem HTTPS fixa, sem autenticação e com limite de leitura, aproveitando `load_public_catalogue()`. A saída inclui ID, nomes, municípios, URLs de fonte, avisos e distância entre pontos existentes; **não exporta contactos, utilizadores, nem gera payloads de escrita**.

## Critérios para uma correção eventual

Uma correspondência exata entre nome, cidade e distrito não dispensa a validação da instalação específica, do município no CAOP, dos aliases e da morada. Confrontar GPS oficial com o registo público; se já coincide, **não repetir**. Se falta GPS, confirmar com um humano que a fonte representa o recinto (não o estádio, uma pista distinta ou o centro de um complexo), depois utilizar o circuito de aprovação moderada. Não executar Liquibase nem alterar a produção apenas com base neste relatório.
