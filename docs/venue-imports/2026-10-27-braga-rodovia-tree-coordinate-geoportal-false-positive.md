# Braga Rodovia — coordenadas municipais da árvore NÃO são GPS de campo

**Data:** 27-10-2026. **Âmbito:** Jynlo IDs 288–294, apenas investigação administrativa read-only. Sem aprovação de coordenadas.

## Descoberta de risco de precisão

A Câmara Municipal de Braga disponibiliza um relatório técnico fitossanitário, **Registo 3567/2025 (23/01/2025)**, sobre a inspeção de um **choupo (*Populus* sp.)** no Parque Desportivo da Rodovia. O cabeçalho refere coordenadas **41.55296, -8.40345** e fotos de uma árvore junto de um campo desportivo e ciclovia.

- Documento oficial: https://www.cm-braga.pt/archive/doc/Parque_Desportivo_da_Rodovia_0.pdf (página 1, figura 1 e texto da caracterização)
- **Sujeito geográfico do relatório = exemplar arbóreo**, não campo numerado, entrada nem centro de recinto. A existência de um campo no enquadramento visual não transfere as coordenadas para o campo.
- **Proibido** importar, aprovar ou apresentar `41.55296, -8.40345` como candidato GPS para qualquer dos IDs 288–294. No admin, mostrar a URL como aviso contextual sem expor essas coordenadas como ponto candidato.
- A data da fotografia e condições do local podem variar; o documento é uma referência de risco de geolocalização indevida, não uma planta de campos.

## Fonte oficial para aprofundar a investigação

A Câmara disponibiliza o **GeoPortal WebSIG Público**:
https://sigportal.cm-braga.pt/WebSIGBraga/

- Página municipal do PDM em vigor: https://www.cm-braga.pt/pt/0101/viver/urbanismo/planeamento-urbano/apresentacao
- Manual municipal de uso: https://www.cm-braga.pt/archive/doc/Manual_GeoPortal.pdf
- Manual explica pesquisa por rua, visualização de temas, medições de pontos/áreas e exportação de plantas. Indica que na escala 1:10000 o fundo por defeito era composto por ortofotos de **2012**, ou seja, não se deve supor ortofoto recente só por o portal ser oficial.
- O GeoPortal responde, mas na extração automática apenas apresentou o ecrã de carregamento Intergraph/Hexagon. **Não foi confirmada uma camada de campos individuais nem número-to-polígono.** A presença de capacidade genérica para medições não fornece evidência de um campo específico.

## Decisão de arquitetura / revisão

Ampliado o registo de documentação administrativa `facilityContextReview.cartographyReview` com:
- `nonPitchTreeReportUrl` — relatório oficial que documenta uma árvore e não um campo.
- `officialGeoportalUrl` — portal de pesquisa municipal, não fonte de localização precisa comprovada.

Os campos só existem em resposta a **IDs 288–294 preexistentes** que ainda passem nas validações exatas de identidade/source/website e ausência de ambas coordenadas. A UI mostra aviso PT/EN específico e ligações HTTPS, não um marcador, latitude, longitude, fusão, aprovação ou ação de importação. Sem migrações nem mudanças às fichas públicas.

## Evidência em falta para fechar GPS de cada campo

1. Extrair uma camada desportiva oficial ou levantamento técnico contemporâneo em que cada polígono tenha o número exato do campo.
2. Confirmar que o número referido nas páginas municipais (1,2,3,4,5,6) corresponde à nomenclatura da camada e da sinalética.
3. Identificar por separado os dois campos de basquetebol cobertos por **uma única** ficha 294, sem a dividir automaticamente.
4. Submeter cada localização para revisão humana e só depois associar GPS ao campo correspondente.

Até cumprir estes critérios, manter todos os GPS em falta. Não transformar uma medição arbitrária no ortofotomapa numa proposta oficial.
