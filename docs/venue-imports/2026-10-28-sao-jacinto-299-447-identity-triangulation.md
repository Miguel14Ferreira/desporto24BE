# Jynlo — identidade dos registos 299 / 447 em São Jacinto (28/10/2026)

**Conclusão provisória:** é **plausível** que os dois registos Jynlo se refiram à mesma instalação municipal. **Não está comprovada** a identidade de ambos, a geometria atual dos tanques nem a entrada após a obra de 2025. Não corrigir GPS, fundir IDs ou eliminar fichas automaticamente.

## Linhagem de evidências, separando nome, recinto e GPS

| Fonte | O que comprova | O que NÃO comprova |
|---|---|---|
| Câmara Municipal, ficha pública — https://www.cm-aveiro.pt/visitantes/piscina-municipal-de-sao-jacinto | Instalação singular **Piscina Municipal de São Jacinto**, renovada em 2025, com tanques infantis e zona ampliada | Coordenadas atuais da porta/entrada, identidade da ficha OSM |
| Câmara Municipal, notícia de 23/09/2026 — https://cm-aveiro.pt/municipio/comunicacao/noticias/noticia/piscina-municipal-de-sao-jacinto-ultrapassa-as-5-000-utilizacoes-neste-verao | Funcionamento da **Piscina Municipal** no verão 2026, 5.200 utilizações | Que o marcador OSM representa o acesso novo |
| Câmara Municipal, arquivo noticioso de 24/07/2026 — https://www.cm-aveiro.pt/municipio/comunicacao/noticias?news_list_82_page=8 | A notícia indexada usa o plural «As Piscinas Municipais de São Jacinto já abriram»; **atenção:** a paginação da listagem é mutável, conservar o título e a data no manifesto | Uma identidade OSM ou coordenada de acesso |
| Junta de Freguesia de São Jacinto — https://www.jf-saojacinto.pt/ver_conteudo/4 | Indica **piscinas** dentro do **polidesportivo na Avenida Ria-Mar**, junto a pavilhão, ginásio, campo relvado e CAR Surf | Qual destes equipamentos ocupa um determinado ponto |
| OpenStreetMap — https://www.openstreetmap.org/way/152241544 | **way 152241544**, nome **Piscinas de São Jacinto**, etiquetas `leisure=swimming_pool` e `sport=swimming`; `source=Bing`; é um **polígono de piscina** | Um ponto oficial recente da Câmara, a entrada ou um polígono atualizado após a requalificação; a página indica edição há mais de três anos |
| Fichas públicas Jynlo — https://api.d24sports.com/api/venues/299 e https://api.d24sports.com/api/venues/447 | **299** = «Piscina Municipal de São Jacinto», `city=São Jacinto`, `district=Aveiro`, **sem GPS**; **447** = «Piscinas de São Jacinto», `city=Aveiro`, `district=Aveiro`, GPS OSM associado | Que os dois IDs já são fisicamente equivalentes ou que a precisão do GPS de 447 é suficiente |

A Câmara relata em 2025 que o tanque principal foi recuperado e que dois tanques mais pequenos foram substituídos por um único tanque infantil. Houve ampliação da vedação para zona de voleibol e parque infantil. Por isso, a geometria OSM pré-requalificação não deve ser copiada para o registo municipal em 2026.

## Revisão humana necessária antes de uma eventual consolidação

1. Obter planta municipal georreferenciada de 2025/2026 ou feição SIG com referência individual da piscina municipal, coordenadas, CRS, data e licença; certificar localização **e entrada** em separado.
2. Confrontar `way/152241544` com a planta, e verificar se é a área do tanque principal, não o edifício dos balneários, o pavilhão, o campo de voleibol ou o recinto completo.
3. Confirmar formalmente que os nomes e entidades gestoras dos IDs 299 e 447 representam a mesma instalação e que não há outra piscina próxima. **Avenida Ria-Mar e nomes semelhantes, por si só, não bastam.**
4. Antes de eventual fusão, inventariar dependências de ambos os IDs (sessões, fotografias, comentários, favoritos, histórico, URLs e avaliações). Fazer proposta separada com migração reversível e controlo administrativo. Não fundir nesta auditoria.
5. Propor uma alteração individual de localização apenas depois de confirmar a fonte e respetiva reutilização, com testes e revisão do resultado LIVE.

## Proteção em código

O ficheiro `docs/venue-imports/2026-10-27-known-venue-gps-collision-review.json` anexa as fontes e a referência OSM da **área**, não coordenadas de entrada. `scripts/audit_known_venue_gps_collisions.py` aceita esse cruzamento **exclusivamente para os IDs 299/447** e assinala prioridade alta enquanto **ambos os IDs mantiverem as identidades esperadas**, mas devolve `samePhysicalFacilityVerified=false`, `currentEntranceVerified=false`, `approvedForGps=false`, `approvedForMerge=false` e `writeActions=0` em todos os casos.

Mudanças de ID, nome, cidade, distrito ou estado GPS devolvem a prioridade normal e exigem nova revisão. **Nenhum valor latitude/longitude é exportado pela auditoria.** A correspondência de nomes é uma hipótese de mesmo local, não decisão automática.
