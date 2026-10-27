# Braga Rodovia — planta numerada fotografada e campo n.º 1 federado

**Data:** 27-10-2026. **Âmbito:** leitura documental de recintos LIVE 288–294 (GPS em falta).
**Decisão:** **não criar propostas GPS, não aprovar, não escrever nem fundir recintos**.

## Fonte nova 1: sinalética fotografada no local

Uma fotografia de visitante identificada como **"Informação"** na galeria pública do *Parque Desportivo da Rodovia* mostra uma placa com um mapa esquemático dos espaços e números de campos. A presença de números é verificável na própria imagem, mas **não** há ficha técnica georreferenciada, coordenadas por número, norte e escala cartográfica certificados nem data verificável da placa. A fotografia é uma contribuição de visitante num catálogo turístico, **não** um documento cartográfico publicado pela Câmara.

- Galeria onde se encontra a fotografia **"Informação"**: https://fr.tripadvisor.ch/Attraction_Review-g189171-d19701352-Reviews-Parque_Desportivo_da_Rodovia-Braga_Braga_District_Northern_Portugal.html
- Ligação direta contextual à fotografia, sujeita a disponibilidade do fornecedor: https://dynamic-media-cdn.tripadvisor.com/media/photo-o/26/df/32/fd/informacao.jpg?h=1100&s=1&w=1100

**Não extrapolar** o posicionamento geográfico real dos campos a partir de uma planta ilustrada nem presumir correspondência com vias/centroides OpenStreetMap. Este documento não reproduz a imagem de terceiros.

## Fonte nova 2: AF Braga, temporada 2025/2026

A Associação de Futebol de Braga publica o **comunicado n.º 263**, de **29/12/2025**, relativo ao programa da 1.ª Divisão de futebol 11. O documento regista repetidamente **campo (392) "CAMPO Nº 1 COMPLEXO DESPORTIVO RODOVIA"**, piso **relvado sintético**, dimensões **105,0 × 68,0 metros**. Exemplo: páginas de calendário da Série B.

- Documento oficial: https://afbraga.fpf.pt/DesktopModules/InstitutionalDocuments/DownloadFiles.ashx?IdDoc=184789

Esse documento dá evidência de **identidade, modalidade e dimensões do campo 1**, não de GPS, entrada, portão ou associação cartográfica com um polígono concreto. Não valida os campos 2–6 nem os campos de basquetebol. Não alterar a descrição dos recintos automaticamente.

## Comparação com a Jynlo

- **288:** Campo n.º 1, ficha municipal item 6223. Único que recebe *ambas* as referências: galeria de sinalética e calendário AF Braga.
- **289–293:** Campo n.º 3, futebol 7 n.º 2 e 4, campos n.º 5 e 6, ficha municipal própria para cada. Apenas a galeria de sinalética; **não atribuir documentação da AF Braga para o campo 1 a estes IDs**.
- **294:** Uma ficha da Jynlo representa conjuntamente campos de basquetebol 1 e 2. A galeria pode auxiliar na compreensão da disposição, **não** separar automaticamente a ficha.

A enumeração do painel fotográfico **pode não corresponder diretamente à numeração de cada modalidade** nem estar atualizada em 2026. É necessário confirmar essa correspondência em visita técnica, planta oficial contemporânea ou camada geoespacial numerada.

## Implementação (revisão ADMIN, apenas leitura)

O DTO opcional `facilityContextReview.numberingPlanReview` é acrescentado apenas aos IDs existentes **288–294**, quando todos os mesmos guardas exatos da revisão GPS se mantêm válidos: nome completo, município, distrito, morada, fonte OFFICIAL, link municipal guardado e latitude/longitude ambas vazias. Expõe `NUMBERED_SIGN_SCHEMATIC_WITHOUT_GEOREFERENCE`, URL de galeria (todos) e URL oficial da AF Braga (apenas 288).

A página ADMIN apresenta um bloco PT/EN com aviso explícito de **não georreferenciação** e ligações HTTPS filtradas, sem marcador nem coordenadas. Nenhuma alteração à autenticação, base de dados, XML, Liquibase, migração ou tabela pública. Testes cobrem segurança do âmbito, correspondência exata, obsolescência, zero escritas, e o bloqueio de inferências sobre os outros campos.

## Limitação verificada na pesquisa

Tentativas de obter polígonos individuais por Overpass e OpenStreetMap não devolveram uma geometria etiquetada, verificável e associável inequivocamente à numeração dos campos; algumas consultas públicas falharam por timeout. Uma falha na consulta **não demonstra a inexistência** dos dados OSM. Até cruzamento verificável, **não criar coordenadas por dedução**.
