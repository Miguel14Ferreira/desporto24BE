# Bragança — primeiro pré-voo de proximidade (28/10/2026)

A base de 28/10/2026, 10:09 UTC, tinha **3614 recintos** no catálogo público,
dos quais 39 marcados com distrito Bragança. Estes são apenas registos
Jynlo, não o inventário real de todas as instalações.

## Evidência geográfica

- Pavilhão Municipal Arnaldo Pereira: designação e localização na
  **Avenida D. Sancho I, Zona Desportiva, 5300-125 Bragança** no
  documento municipal de 2025, ID de instalação 14242:
  https://www.cm-braganca.pt/cmbraganca2020/uploads/document/file/5548/edital_n_o_292_2025.pdf
- Ficha de investigação arquitetónica pública com ponto geográfico
  **do conjunto** pavilhão/piscina, NÃO da entrada:
  https://arquitecturaaqui.eu/pt/obras/4824/pavilhao-municipal-arnaldo-pereira-braganca
- O **Guia das Acessibilidades dos Espaços Desportivos do Distrito de
  Bragança**, publicado no sítio da Câmara de Vila Flor, também inclui
  o pavilhão (página 34) e a piscina (página 32); os valores GPS têm
  pontuação pouco convencional, pelo que não os converteremos para
  coordenadas Jynlo sem validação da origem:
  https://www.cm-vilaflor.pt/cmvilaflor/uploads/writer_file/document/1736/GUIA_BRAGAN_A.pdf
- A piscina e o pavilhão constam como instalações separadas e podem
  ter **pontos GPS próximos**, sem serem duplicados.

## Critério de comparação

O script `scripts/audit_braganca_arnaldo_nearby_20261028.py`
usa **o ponto do estudo arquitetónico apenas como centro de pesquisa**
e procura recintos já presentes num raio de 250 metros. Mostra
IDs/nome/localidade públicos, distância aproximada e coincidências de
nome no distrito mesmo que estejam sem GPS ou com freguesia no campo
`city`. O limite de 250m é uma ferramenta de triagem, não uma distância
oficial de identidade. A listagem é limitada a oito vizinhos, mas
mantém a contagem completa de resultados próximos.

**Nunca** é apresentado um payload de inserção nem coordenadas para
copiar para produção. Um resultado vazio não significa que não exista
um recinto (pode estar sem GPS ou com nome distinto). O relatório
não copia dados de contacto, endereço ou GPS de registos Jynlo.

## Outros candidatos de Bragança

- Pavilhão da Coxa: Rua Padre Benjamim Videira Pires, conforme
  regulamento da Federação Portuguesa de Xadrez:
  https://fpx.pt/site/public/uploads/regulamentos/REG-PT-Tras-os-Montes.pdf
- Pavilhão da Bancada: integrado no Estádio Municipal, confirmado
  em atas da Câmara de Bragança:
  https://www.cm-braganca.pt/cmbraganca2020/uploads/document/file/3944/ata_da_reuniao_ordinaria_de_12_de_agosto_de_2019.pdf
- Pista de Atletismo: o nome na lista municipal não basta para afirmar
  que é uma instalação separada da pista/complexo do Estádio Municipal.

Continuam por confirmar localização exata, identidade e licença de
reutilização de dados para estes três candidatos. Fontes de cartografia
municipal podem impor regras específicas de reutilização.
Nenhum recinto está aprovado para inserção ou atualização.
