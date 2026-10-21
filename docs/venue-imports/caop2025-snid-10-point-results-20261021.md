# Jynlo — validação administrativa CAOP2025 dos pontos SNID (21/10/2026)

**AUDITORIA READ-ONLY · 0 GPS aprovados · 0 escritas à BD · sem deploy.**

## Evidência reprodutível
- [Execução GitHub Actions #39580670851](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39580670851): quatro testes PASS, descarregamento DGT CAOP2025 oficial, `cont_municipios` (278) e `cont_distritos` (18) no Geopackage, verificação `point-in-polygon`.
- Entradas: `docs/venue-imports/snid-10-points-for-caop2025.json` (10 pontos SNID para 8 referências da migração), extraídos do resultado de correspondência de nomes SNID.
- Saídas: artefacto GitHub `caop2025-snid-10-point-audit` (JSON + CSV), retenção 7 dias. Não descarregar/publicar o GPKG integral.
- **9/10** sugestões localizam-se no município e distrito da instalação proposta. **1/10** localiza-se num município diferente. **0/10** coordenadas aprovadas para SQL automático. Estes números são de **pontos**, não de recintos distintos.

## Resultados

| Recinto candidato | Sugestão SNID | Município CAOP2025 | Estado |
|---|---|---|---|
| Complexo Desportivo de Évora | Nome exato | Évora | Município confirmado; ponto do recinto por confirmar |
| Piscinas Municipais de Évora | Nome exato | Évora | Município confirmado; ponto do recinto por confirmar |
| Pista de Atletismo de Castelo Branco | Nome semelhante | Castelo Branco | Município confirmado; GPS municipal mais promissor abaixo |
| Piscinas Municipais do Sacapeito | Nome exato | Santarém | Município confirmado; variantes da mesma instalação |
| Piscinas Municipais do Sacapeito | Nome semelhante | Santarém | Município confirmado; alternativa ao ponto anterior |
| Pavilhão Desportivo Municipal (Santarém) | Nome exato | **Entroncamento** | **REJEITAR este GPS** para o recinto em Santarém |
| Campo Municipal do Parque da Cidade (Porto) | Nome semelhante | Porto | Município confirmado; identidade do campo por confirmar |
| Campo Municipal do Outeiro (Porto) | Nome exato | Porto | Município confirmado; morada/freguesia por reconciliar |
| Polidesportivo Municipal do Paço (Maia) | Nome semelhante (Pedrouços) | Maia | Município confirmado; identidade do recinto por confirmar |
| Polidesportivo Municipal do Paço (Maia) | Nome semelhante (Sobreiro) | Maia | Município confirmado; possível homónimo |

## Novas fontes para confirmação do recinto físico

1. **Pista de Atletismo de Castelo Branco**: documento oficial da Câmara Municipal publica GPS **39.818785611212455, -7.515117240291148**, em https://www.cm-castelobranco.pt/media/8123/manual-de-acolhimento-ao-novo-albicastrense.pdf . O ponto SNID com nome semelhante mede ~**76 m** de distância do GPS municipal. **Preferir o GPS fornecido pelo município**, sujeito a validação CAOP2025 desse ponto específico e confirmação operacional antes da atualização da migração.
2. **Complexo Desportivo de Évora**: site oficial municipal confirma Estrada das Alcáçovas (N380), componentes do complexo e **regras de utilização restritivas**: pista de atletismo e campo grande de jogos reservados a clubes/associações; acesso público anunciado ao circuito de manutenção com condições. https://www.cm-evora.pt/visitante/lazer-visitar/complexo-desportivo-de-evora/ . O ponto SNID fica a cerca de **138 m** do centro OSM correspondente a `way/1266229401` (https://mapcarta.com/W1266229401). **Não anunciar os campos como abertos ao uso livre.**
3. **Piscinas Municipais de Évora**: ponto GeoNames/Mapcarta em https://mapcarta.com/35984330 situa-se a aproximadamente **57 m** do ponto SNID. Município confirmado, ponto de entrada/acesso ainda a confirmar.
4. **Polidesportivo Municipal do Paço (Maia)**: município confirma o recinto em **Rua do Paço, Pedrouços**, com gestão exclusiva pela escola e Câmara Municipal: https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco . **Não inferir que os organizadores da Jynlo o podem usar sem autorização.**
5. **Campo Municipal do Outeiro (Porto)**: operador municipal Ágora confirma a morada **Rua D. Afonso Henriques, Paranhos**: https://www.agoraporto.pt/grandes-campos-municipais/campo-municipal-do-outeiro . Um resultado OSM/Mapcarta de nome semelhante referencia uma área diferente; conferir que é o campo municipal de Paranhos antes de escolher GPS.

## Próximos critérios de decisão
- Para cada coordenada, exigir **(a)** município CAOP correto, **(b)** identidade e morada do recinto por fonte independente (preferir sítio oficial), **(c)** ponto adequado para navegação, **(d)** restrições de utilização conhecidas e **(e)** preflight contra a BD.
- O CAOP comprova apenas a localização administrativa; **não confirma** campo, pavilhão, horário, acesso ou possibilidade de criar sessões sem autorização.
- Os outros **19/27** recintos sem match SNID continuam pendentes de fontes municipais, OSM/Google Maps e, quando apropriado, recolha pelos utilizadores.
- A publicação do lote #136 continua pendente; **não** atribuir localização a todos os campos de um complexo pela coordenada central.
- Atribuição: © Direção-Geral do Território, CAOP2025 CC BY 4.0; SNID/IPDJ, verificar condições de reutilização; dados © OpenStreetMap contributors (ODbL).
