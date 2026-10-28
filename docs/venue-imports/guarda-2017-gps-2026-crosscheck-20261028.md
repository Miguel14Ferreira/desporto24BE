# Guarda — cruzamento de coordenadas municipais de 2017 (28/10/2026)

**Investigação apenas. Não importar, alterar GPS ou publicar novas fichas.**

Foi identificada uma ata oficial da Câmara Municipal da Guarda, **10 de
julho de 2017**, que contém uma tabela de localizações usadas em rondas de
vigilância (página impressa 26, página PDF 26). É uma fonte municipal, mas
a data e a finalidade de vigilância **não permitem certificar a porta
atual de entrada dos jogadores**.

Fonte: https://cdn.mun-guarda.pt/WS/03-154889474611829

| Equipamento | Latitude | Longitude | Avaliação |
|---|---:|---:|---|
| Piscinas Municipais da Guarda | 40.531324 | -7.254329 | Ponto municipal de ronda de 2017 |
| Estádio Municipal da Guarda | 40.536435 | -7.266712 | Ponto municipal de ronda de 2017 |
| Pavilhão Desportivo S. Miguel | 40.548919 | -7.252073 | Ponto municipal de ronda de 2017 |

O Município continua a identificar **Estádio, Zambito, INATEL e São
Miguel** como equipamentos desportivos sob gestão autárquica:
https://www.mun-guarda.pt/intervencao/desporto/

## Cruzamento com a informação já existente

- **Piscinas Municipais:** já têm nome exato no catálogo público
  (pré-voo BE #500). O ponto do portal VisitGuarda guardado anteriormente
  (40.531141, -7.253715) fica a **cerca de 55,7 metros** do ponto de
  vigilância de 2017. A diferença não resolve qual a entrada correta.
  Fonte independente: https://www.visitguarda.pt/en/ponto_de_interesse/piscina-municipal-da-guarda/
- **Estádio Municipal da Guarda:** sem coincidência local evidente no
  relatório público de 28/10/2026; isto não prova ausência na BD.
- **Pavilhão S. Miguel:** igualmente sem correspondência local evidente
  nessa auditoria pública. Antes de qualquer inclusão, investigar aliases
  e coordenadas de acesso.
- **Campo do Zambito:** a auditoria anterior devolveu possível alias.
  Esta ata não fornece coordenada para o Zambito; não inferir uma.
- **INATEL:** não há nesta tabela uma localização a atribuir ao recinto.

O ficheiro `guarda-municipal-historical-gps-evidence-20261028.json`
documenta estes três pontos históricos, sem os aprovar para importação.
O teste offline correspondente rejeita alterações às coordenadas
reproduzidas, ausência das ressalvas de entrada e qualquer sinal de
aprovação. Este trabalho não modifica o catálogo, o Liquibase nem o
Render. Para eventual publicação continuam obrigatórios GPS de acesso
atual verificado, duplicados no PostgreSQL autorizado, proveniência,
direitos de reutilização, regras de utilização e rollback validado.
