# Revisão administrativa de GPS — casos com investigação documentada

**Data:** 27 de outubro de 2026. **Sem alterações automáticas a recintos.**

## Problema resolvido
A lista ADMIN/SUPER_ADMIN de recintos sem GPS usa paginação de 20 por distrito; provas já investigadas podem surgir em páginas distantes. A nova opção **`onlyWithEvidence=true`** do mesmo endpoint `GET /api/admin/overview/venue-gps-review` devolve só os casos existentes cuja identidade e documentação persistem intactas.

Os IDs deliberadamente investigados são derivados do pequeno registo de fontes já integrado: **273 Évora piscinas, 276 Sacapeito, 281 Choupos**, e avisos especiais para **280 Viso** (possível duplicação com 2623) e **284 Arroios** (morada administrativa distinta do recinto). Esta vista não inclui automaticamente quaisquer outros recintos só por terem um endereço ou um ponto geográfico na mesma localidade.

- Carrega apenas **os IDs explicitamente investigados** com `findAllById`, sem varrimento de todo o catálogo e sem N+1 sobre a listagem nacional; para o Viso, consulta o ID **2623** adicionalmente para reconfirmar a identidade exata do possível duplicado.
- Gera os mesmos objetos de revisão de antes, ordenados por ID para páginas determinísticas; mantém o filtro por distrito, numeração, `hasMore` e limites de página. Se um recinto já tiver GPS, for alterado, tiver fonte diferente ou a duplicação deixar de cumprir as condições, **desaparece da vista focalizada**.
- A consulta padrão, sem `onlyWithEvidence` (ou com `false`), continua a usar a mesma query paginada da base de dados.
- **Apenas GET ADMIN/SUPER_ADMIN**, mesmo esquema de segurança, sem mutações em base de dados nem tabelas novas, aprovação ou publicação automática.

## Contexto: Campo Municipal do Parque da Cidade, Porto — ID 282
Investigação adicional neste ciclo: `https://api.d24sports.com/api/venues/282` tem o **Campo Municipal do Parque da Cidade**, Estrada Interior da Circunvalação 4100-083 Porto, fonte Ágora, sem coordenadas. A página específica oficial https://www.agoraporto.pt/grandes-campos-municipais/campo-do-parque-da-cidade confirma **campo de futebol 11**, homologado e gerido pela Ágora. https://www.goporto.pt/noticias/concluido-novo-edificio-de-apoio-a-area-desportiva-do-parque-da-cidade descreve a área desportiva e respetivo edifício de apoio.

A notícia independente https://www.jpn.up.pt/2024/03/27/campo-de-futebol-do-parque-da-cidade-com-falta-de-condicoes/ confirma **dois campos diferentes lado a lado**: um de futebol 5 à data sob gestão do Sport Club do Porto e outro de futebol 11 da Ágora. A referência geográfica genérica ao Parque da Cidade pode confundir os campos ou a entrada do parque. **Não há coordenadas verificadas para o campo municipal de futebol 11 e este ID 282 fica fora da fila focalizada** até obter ponto de terreno de jogo específico com fontes suficientes.

## Verificações
Testes Java garantem filtro com IDs pesquisados, ordenação determinística, município/distrito, exclusão de pistas obsoletas, comportamento original quando desativado, confirmação dinâmica do registo OSM 2623 e inexistência de métodos de escrita. Testes Angular validam o botão PT/EN, regresso à página 0 e composição do URL com distrito. Endpoint permanece 403 sem sessão ADMIN.
