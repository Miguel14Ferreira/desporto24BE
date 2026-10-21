# Jynlo — SNID vs geografia pública: primeira revisão dos 27 recintos sem GPS

**21 outubro 2026 · Só investigação / não publicar coordenadas ou novos recintos.**

### Execução reproduzível

- [CI — extração SNID e seis testes offline, sucesso](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39578713864).
- SNID/IPDJ exportação `snid-localizacao-de-instalacoes-desportivas-20260901.json`: **27.894 registos**.
- Ao deduplicar pares **nome normalizado + coordenadas arredondadas a 6 decimais** no mesmo distrito declarado: **8.840 pontos distintos** no conjunto filtrado; **17.722 repetições de ponto/nome** excluídas desse conjunto.
- **27 candidatos** sem GPS verificado no PR #136: **5 nome exato com um único ponto distinto SNID**, **3 só nome semelhante**, **19 sem correspondência plausível**. Nenhuma localização atribuída automaticamente à migração.
- Os rótulos `municipalityInfo` da exportação SNID mostraram valores incongruentes com nomes e posições (por exemplo, "Vilarinho Do Bairro" para Évora); **não são prova do município**. Esta auditoria usa apenas distrito declarado e bounding box, não polígonos CAOP2025.

### Primeiros confrontos independentes — NÃO APROVADOS

| Candidato | Coordenadas SNID propostas | Prova adicional | Estado |
|---|---|---|---|
| Complexo Desportivo de Évora | 38.56169636, -7.92027891 | [OSM via Mapcarta](https://mapcarta.com/W1266229401): 38.56074, -7.91927, diferença aprox. **138 m**; [município](https://www.cm-evora.pt/visitante/lazer-visitar/complexo-desportivo-de-evora/) confirma instalação | **REVIEW** — coordenada SNID poderá representar complexo, não campo exato. Falta confirmar limite CAOP e ponto apropriado. |
| Piscinas Municipais de Évora | 38.5732268, -7.9288771 | [GeoNames via Mapcarta](https://mapcarta.com/35984330): 38.57373, -7.92873, diferença aprox. **57 m** | **REVIEW** — indício coerente, falta confirmar ponto exato. |
| Piscinas Municipais do Sacapeito (Santarém) | 39.2258699, -8.6887934 | SNID contém também a grafia "Piscinas Municipais de Sacapeito", posição próxima mas distinta; [Viver Santarém](https://viversantarem.pt/piscinas/) confirma a morada do recinto | **REVIEW** — decidir a coordenada depois de conferir a instalação em mapa/CAOP. |
| Pavilhão Desportivo Municipal (Santarém) | 39.47871, -8.466995 | **Conflito**: a localização sugerida está a aprox. **29,5 km** do Complexo Aquático de Santarém (localização já verificada noutra entrada) e perto do [pavilhão homónimo em Entroncamento](https://mapcarta.com/W164125503). A [página do operador](https://viversantarem.pt/pavilhoes-desportivos/) indica *Largo Comendador Paulino da Cunha e Silva, Santarém*. | **REJEITAR ESTA PROPOSTA SNID** para o recinto de Santarém; procurar GPS específico da morada, não excluir o recinto. |
| Campo Municipal do Outeiro (Porto) | 41.17668225, -8.59226933 | [OSM via Mapcarta](https://mapcarta.com/W1298753441): 41.17589, -8.59259, diferença aprox. **92 m**. Porém, o manifesto candidato menciona *Paranhos* e OSM classifica a zona como *Campanhã*. | **REVIEW / MORADA A CONFIRMAR** — não atribuir GPS até reconciliar identidade e morada. |

Outros três candidatos com apenas semelhança nominal: Pista de Atletismo de Castelo Branco, Campo Municipal do Parque da Cidade (Porto), Polidesportivo Municipal do Paço (Maia). A identificação da instalação exata é pendente, e nomes semelhantes nunca recebem GPS automático.

### Decisão de segurança

Não há dados suficientes para preencher automaticamente os 27 GPS do PR #136. O relatório é um **conjunto de provas e suspeitas**, não uma atualização do catálogo. Não alterar SQL Liquibase; não converter o centro de um complexo na posição de cada recinto filho; não criar duplicados; não afirmar que existe possibilidade de reserva ou acesso público.

**Próxima passagem:** conferir localizações no CAOP2025 por município e validar os recintos com as fichas municipais e OSM. Os 19 sem correspondência SNID devem prosseguir por essas fontes. Manter PR #136 em rascunho até ter um lote publicável e backup testado.
