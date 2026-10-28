# Jynlo — revisões de identidade e possível duplicação GPS (27-10-2026)

**Auditoria de leitura apenas. Zero fusões, zero propostas aprovadas, zero geocodificações, zero migrations, zero escritas à API ou DB.** Cada relação resulta dos nomes/IDs efetivamente devolvidos por `GET https://api.d24sports.com/api/venues/suggest?name=...&page=0` e de fontes oficiais municipais, e exige validação humana. Não significa que dois IDs representem automaticamente a mesma instalação.

## Casos novos detetados na API pública LIVE

| ID sem GPS | Outro ID com marcador OSM | Classificação segura |
|---|---|---|
| **299** Piscina Municipal de São Jacinto (cidade registada São Jacinto, distrito Aveiro) | **447** Piscinas de São Jacinto (cidade Aveiro, distrito Aveiro; 40.6668823, -8.7391574) | **POTENTIAL_DUPLICATE_POOL:** nomes semelhantes; origem e acesso físico **não comprovados**; nem copiar coordenadas nem eliminar IDs |
| **295** Piscina Climatizada do Complexo Desportivo de Maximinos (Braga) | **912** Complexo Desportivo de Maximinos (Braga; 41.5415045, -8.4402703) | **COMPLEX_VS_SPECIFIC_FACILITY:** marcador do complexo **não é** marcador certificado da entrada da piscina; manter IDs independentes |
| **286** Campo de Padel do Fojo (Mira, distrito Coimbra) | **2377** Golfe da Quinta do Fojo (Vila Nova de Gaia, distrito Porto; 41.1237003, -8.6383088) | **DIFFERENT_DISTRICT_NAME_COLLISION:** homónimo sem relação geográfica; rejeitar uso do marcador de Gaia para Mira |

**Nota crítica:** A etiqueta `GPS_ONLY_ADDRESS_UNKNOWN` de um registo OpenStreetMap é um ponto de investigação, não evidência oficial da entrada. Não pressupor que a Piscina Municipal de São Jacinto 299 é fisicamente idêntica à 447 só porque ambas contêm «São Jacinto»; as designações, tipologia e cobertura municipal requerem confirmação. O registo 912 pode ser precisamente a envolvente, e não o tanque de Maximinos.

Fontes para inspeção:
- Aveiro — https://www.cm-aveiro.pt/visitantes/piscina-municipal-de-sao-jacinto , requalificação em 2025 e campo de voleibol/parque infantil adjacentes; não georreferencia o tanque de forma certificada.
- Braga — https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6262 , identifica a piscina da Rua da Naia; notícia municipal de 18-08-2026 reforça que é uma instalação específica: https://www.cm-braga.pt/pt/0201/comunicacao/noticias/item/item-1-23085 .
- Mira — https://www.cm-mira.pt/node/4084 , confirma novo campo de padel em 22-11-2025 no Complexo Desportivo do Fojo, Seixo de Mira, não Quinta do Fojo de Vila Nova de Gaia.

## Outro risco de confusão: Pavilhão EX-EPC (ID 279)

A empresa municipal Viver Santarém descreve **três instalações diferentes com a mesma Rua dos Ex-Combatentes do Ultramar, 2005-246**: Pavilhão Desportivo EX-EPC, Campo Relvado Sintético da EX-EPC e Campo de Rugby da EX-EPC. O primeiro consta em https://viversantarem.pt/pavilhoes-desportivos/ ; os outros em https://viversantarem.pt/campos-de-jogos/ .

Um código postal ou um pin de rua/complexo **não identifica o edifício do pavilhão**, nem o rugby, nem o relvado; não se deve inferir GPS para o ID 279 usando qualquer um destes pontos. Esses outros equipamentos não foram considerados como novos IDs Jynlo — não criar duplicações.

## Auditoria automática offline

Manifesto fixo: `docs/venue-imports/2026-10-27-known-venue-gps-collision-review.json`.

```bash
python3 scripts/audit_known_venue_gps_collisions.py \
  --venues-json /tmp/jynlo-public-venues.json \
  --output /tmp/gps-known-collisions-review.json
```

Ou `--live-public-api` para carregar a API **pública** sem credenciais, apenas GET.

A auditoria rejeita manifesto aprovado, IDs repetidos, fonte sem HTTPS e jurisdições incoerentes. Valida **ID + nome + cidade + distrito** em cada registo, não apenas texto semelhante. Se algum registo desaparecer, mudar nome ou ganhar GPS, exige reavaliação e não reutiliza estados antigos. Só reporta **presença** de coordenadas, nunca as exporta como instrução de escrita.

Saída: `writeActions=0`, `mergeActions=0`, `approvedGpsCount=0`, `approvedMergeCount=0` e `manualReviewRequired=true` para todos os casos. O auditor nunca inclui contactos privados ou valores de latitude/longitude na resposta.

**Próximas verificações externas:** fonte georreferenciada específica da piscina 299 / piscina 447, verificar mapa de acessos de Maximinos 295, planta do campo de padel 286, e planta do edifício EX-EPC 279. As correções individuais, se forem obtidas, devem passar por revisão administrativa e testes separados; não enviar pedidos formais ou emails sem autorização.
