# Portalegre — cruzamento real com CAOP2025 (19/10/2026)

**Estado:** análise geográfica concluída, importação NÃO autorizada. Sem escrita
na BD, Render, GitHub Actions ou alterações ao master Liquibase.

## Fontes e método
- OpenStreetMap: artefacto original do piloto de Portalegre, 1.992 candidatos
  analisados, a partir do run GitHub Actions 39241121093.
- DGT: GeoPackage oficial `CAOP_Continente_2025-gpkg.zip` fornecido pelo
  responsável do projeto, com camadas `cont_distritos` (18 geometrias) e
  `cont_municipios` (278 geometrias), originalmente EPSG:3763.
- Validação local offline com GeoPandas/Shapely: coordenadas OSM EPSG:4326,
  limites CAOP reprojetados para EPSG:4326, `sjoin(predicate='within')`.
- O output CSV (1.992 linhas) e a lista priorizada (97 linhas) foram gerados
  na conversa de análise. Não incluir PBF/GeoPackage grande no Git.

## Resultado oficial por localização

| Correspondência geográfica | Candidatos |
|---|---:|
| Distrito de Portalegre | 1.915 |
| Distrito de Évora | 14 |
| Distrito de Castelo Branco | 12 |
| Distrito de Santarém | 6 |
| Sem correspondência oficial no continente | 45 |
| **Total analisado** | **1.992** |

Os 32 candidatos noutros distritos devem ser analisados no distrito
correspondente. O Polidesportivo de Mora (`OSM:WAY:383543126`) é um
exemplo real de sobreposição de extratos: Mora pertence a Évora, não
a Portalegre. Os 45 sem correspondência são retidos para revisão,
**não** lhes é atribuído um distrito por aproximação.

## Qualidade dos candidatos

| Classificação | Número |
|---|---:|
| Nome + modalidade + município/distrito CAOP + morada OSM declarada | 10 |
| Nome + modalidade + município/distrito CAOP, morada por confirmar | 87 |
| Nome ou modalidade insuficiente | 1.862 |
| Outra localização não confirmada | 1 |
| Outro distrito | 32 |
| **Total** | **1.992** |

Portanto existem **97 candidatos priorizáveis para revisão humana**,
não 97 recintos aprovados. Não inferir acesso público, disponibilidade
para reservas nem validade de morada/identidade apenas pela CAOP.

### Lista dos 97 por município

| Município CAOP | Com morada OSM | Morada por confirmar | Total |
|---|---:|---:|---:|
| Alter do Chão | 0 | 5 | 5 |
| Arronches | 0 | 3 | 3 |
| Avis | 0 | 8 | 8 |
| Campo Maior | 0 | 6 | 6 |
| Castelo de Vide | 0 | 2 | 2 |
| Crato | 0 | 4 | 4 |
| Elvas | 1 | 19 | 20 |
| Fronteira | 0 | 2 | 2 |
| Gavião | 0 | 2 | 2 |
| Marvão | 0 | 2 | 2 |
| Nisa | 0 | 2 | 2 |
| Ponte de Sor | 2 | 21 | 23 |
| Portalegre | 1 | 11 | 12 |
| Sousel | 6 | 0 | 6 |
| **Total** | **10** | **87** | **97** |

## Revisão das nove propostas SQL existentes

Todas as nove referências OSM do draft
`2026-10-19-portalegre-source-checked-DRAFT.sql` foram confirmadas
**dentro dos limites de Portalegre** com os polígonos CAOP. As localidades
de Cano, Casa Branca e Santo Amaro são do município oficial **Sousel**.
O décimo candidato completo é o Ginásio GymnoB; mantém-se fora do draft
por não ter sido corroborado independentemente.

O primeiro piloto considerava 13 candidatos completos. Três deixam de
cumprir o critério após revisão geográfica e de morada: o Polidesportivo
de Mora pertence a Évora; o Campo de Ténis de Ponte de Sor e a Piscina
Municipal de Terrugem/Elvas exigem confirmação de morada.

## Outros alertas

- Um par de objetos OSM muito próximos, ambos «Praça de Touros» em
  Campo Maior: verificar possível geometria duplicada, sem fundir por
  aproximação cega.
- Muitos objetos OSM correspondem a piscinas sem nome; podem ser piscinas
  privadas e não devem ser publicados como recintos públicos.
- A CAOP é uma fonte para localização administrativa, **não** prova
  funcionamento ou acesso ao recinto.

## Próximos passos antes de Liquibase

1. Revisão documental e de disponibilidade dos 10, incluindo comparações
   com nomes alternativos e recintos já existentes na Jynlo.
2. Confirmar endereços e acesso dos 87, por ordem de prioridade, sem copiar
   automaticamente moradas de serviços proprietários.
3. Executar teste PostgreSQL 16 em ambiente separado quando CI disponível.
4. Aprovar explicitamente um lote final, fazer backup, só então adicionar o
   changeset correspondente ao master e coordenar um deploy único.
5. Repetir o cruzamento CAOP nos outros 17 distritos e medir por concelho.

Crédito: **© OpenStreetMap contributors (ODbL)** e **© Direção-Geral do
Território, CAOP2025 (CC BY 4.0)**. Garantir conformidade das licenças na
distribuição pública de dados derivados.
