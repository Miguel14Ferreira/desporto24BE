# Jynlo — Braga / Évora: dois GPS documentais para auditoria CAOP2025

**21/10/2026 — READ-ONLY.** Sem atualizações de Liquibase, base de dados ou Render. O PR de lançamento #136 mantém-se em `draft`, e nenhum destes pontos é aprovado automaticamente.

| Referência já existente no PR #136 | Coordenada WGS84 (lat, lon) | Evidência oficial | Precisão real |
|---|---|---|---|
| `JYNLO:DOCUMENTED:EVORA:COMPLEXO_DESPORTIVO_DE_EVORA` | `38.56136111111111,-7.920722222222222` | [Regulamento municipal de atletismo, pág. 2](https://www.cm-evora.pt/wp-content/uploads/2022/04/Regulamento_XL-GPSJ-Atletismo.pdf), de 2022; indica 38°33'40.9"N 7°55'14.6"W para prova na pista | **Waypoint oficial da pista/prova de 2022**, não entrada de navegação nem garantia de permanência |
| `JYNLO:MUNICIPAL:BRAGA:ITEM-1-6214` | `41.538819,-8.420613` | [Inventário patrimonial público do Estádio 1.º de Maio](https://imovel.patrimoniocultural.gov.pt/detalhes.php?code=72715), corroborado pela [ficha municipal da pista](https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-6214) | **GPS do estádio**, não da pista de atletismo nem da sua entrada |

## Implicações e controlo de qualidade

- **Évora:** o GPS municipal de 2022 fica a cerca de **54 m** do GPS SNID anteriormente sugerido. O [município confirma](https://www.cm-evora.pt/visitante/lazer-visitar/complexo-desportivo-de-evora/) a existência da pista de atletismo e reserva a pista e o campo grande aos clubes/associações. **Não anunciar estas instalações como livremente utilizáveis para sessões públicas.** Acesso geral refere-se ao circuito de manutenção e tem condições.
- **Braga:** a ficha municipal comprova pista no Estádio 1.º de Maio, mas o ponto patrimonial está referenciado ao **estádio**. Não propagá-lo para campos individuais no Complexo da Rodovia, nem afirmar localização exata da pista.
- **Santarém:** o ponto SNID `39.47871,-8.466995` para o Pavilhão Desportivo Municipal foi rejeitado porque o CAOP o situa no Entroncamento. Não reutilizar.
- **Validação:** correr `scripts/check_caop2025_snid_points.py --expected-count 2` com o geopackage oficial DGT. Exigir **2/2 municípios/distritos correspondentes**; o verificador **sempre devolve zero aprovações de importação**. Município correto não equivale a identidade da instalação, entrada ou regras de utilização.
- **Preflight antes de produção:** verificar nomes/aliases, source_reference e proximidade à BD real; revalidar regras e acessos, testar restauro do backup, rever eventual alteração isolada ao PR #136 e pedir aprovação explícita.

© Direção-Geral do Território (CAOP2025, CC BY 4.0); fontes oficiais ligadas acima. OpenStreetMap (© contributors, ODbL), quando consultado, permanece fonte auxiliar, não prova de entrada específica.
