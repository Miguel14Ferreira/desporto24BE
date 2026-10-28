# Primeiro pré-voo de Bragança e Guarda (28/10/2026)

A baseline pública da Jynlo de 28/10/2026, às 10:09 UTC, tem 3.614
registos. O distrito de Bragança tem 39, Guarda tem 52. Isto **não**
indica quantas instalações reais existem nessas regiões.

A shortlist JSON guarda 10 referências a instalações mencionadas em
fontes **municipais**:
- Bragança (5): Pavilhão Municipal Arnaldo Pereira; Piscina Municipal;
  Pavilhão da Bancada; Pavilhão da Coxa; Pista de Atletismo.
- Guarda (5): Estádio Municipal da Guarda; Campo de Jogos do Zambito;
  Pavilhão Desportivo Municipal de São Miguel; Pavilhão Gimnodesportivo
  do INATEL; Piscinas Municipais da Guarda.

Fontes: Câmara Municipal de Bragança (requerimento municipal de
instalações e regulamento do Pavilhão Municipal Arnaldo Pereira),
Município da Guarda (página Desporto e programação municipal de 2026).
As ligações individuais estão na shortlist.

**Não é um lote pronto a importar.** Ainda não há confirmação de
identidade dos espaços face aos já catalogados, GPS, modalidade exata,
morada, horário, acesso público nem licença de reutilização de dados.
Os nomes nas fontes municipais não garantem que estejam em falta.

A execução Github Actions para esta shortlist consulta o catálogo
público atual em GET e mostra:
- EXACT_EXISTING: o nome e a cidade coincidem exatamente;
- POSSIBLE_ALIAS_REVIEW: possíveis outros nomes/instalações do mesmo tipo;
- NO_LOCAL_MATCH_REVIEW: nenhum candidato local óbvio, ainda sem
  aprovação para importação.

É um **filtro de investigação**, não uma decisão de duplicação.
O algoritmo nunca prova que um candidato sem correspondência é
realmente novo. A comparação só usa nomes, IDs públicos, município
e distrito. Não grava contactos, moradas ou GPS de recintos.

Só depois da revisão manual das fontes, identidade e geolocalização
e de um preflight autorizado na BD se poderá propor alterações, em
separado, com rollback e backup testado. Não inserir nem alterar os
10 recintos por causa deste relatório.
