# Primeiro piloto nacional da Jynlo: 3 propostas municipais (sem publicação)

Fontes verificadas a 21/10/2026:
1. **Pavilhão Marialvas**, Cantanhede, OSM:WAY:145943204: https://cm-cantanhede.pt/mcsite/entidade/1680/pavilhao-marialvas — Rua Luís de Camões 19, 3060-183, futsal/andebol/basquetebol/voleibol (equipamento coberto).
2. **Piscinas Municipais Rui Abreu**, Coimbra, OSM:WAY:122685516: https://www.cm-coimbra.pt/areas/viver/desporto/equipamentos-desportivos/piscinas-municipais/piscina-municipal-rui-abreu — Rua António Sérgio, 3020-305; natação, horários municipais de 2026 sujeitos a alterações.
3. **Centro Olímpico de Piscinas Municipais**, Coimbra, OSM:WAY:1056855283: https://www.cm-coimbra.pt/areas/viver/desporto/equipamentos-desportivos/piscinas-municipais/c-olimpico-de-piscinas — Praça Heróis do Ultramar, 3030-327; natação e horário municipal de 2026.

### Revisão pontual do GPS do Pavilhão Marialvas (21/10/2026)

A [ficha oficial do Município de Cantanhede](https://cm-cantanhede.pt/mcsite/entidade/1680/pavilhao-marialvas) indica GPS **40.344028, -8.587633**. Esta coordenada substitui, **apenas no rascunho não aprovado**, o GPS OSM anterior (40.3440287, -8.5876799). A referência de deduplicação continua `OSM:WAY:145943204`; a coordenada foi confirmada independentemente pelo município, não é uma localização de entrada garantida.

A [página municipal de contactos](https://cm-cantanhede.pt/mcsite/pagina/844/Os-nossos-Contactos) publica atendimento e horário do pavilhão. As [normas municipais 2022/2023](https://www.cm-cantanhede.pt/mcsite/Media/upload/2022/202281212942_Manualderetomapmarialvas2021revisaojul2022.pdf) referiam utilizadores inscritos/cedência de espaços; **são históricas e não garantem regras de acesso da época atual**. A aprovação depende de confirmação atual junto do responsável pela instalação. A impressão digital do candidato em `first-three-decisions-pending-20261021.json` foi recalculada; a decisão permanece `PENDING`.

Os três têm fonte independente para **identidade e morada**, mas não provam utilização livre para sessões organizadas por terceiros. A Jynlo divulga sessões, não vende nem reserva instalações. Todos os registos são `approved=false`. **Não existe SQL de INSERT nesta alteração**.

Não incluir já o **Casal Vistoso**: a fonte municipal lista piscina, pavilhão, rocódromo e salas dentro de um único complexo, sendo necessária reconciliação de possíveis subinstalações para evitar duplicados. Os outros 46 da fila inicial continuam sujeitos a confirmação.

### Complemento de evidência operacional (21/10/2026; sem aprovação)

- **Pavilhão Marialvas, Cantanhede:** a [ata municipal de março de 2026](https://www.cm-cantanhede.pt/mcsite/media/upload/2026/2026318143051_Ata52026.pdf) documenta pedido de utilização para treinos de basquetebol e decisão de isenção de taxas. Isto corrobora atividade em 2026 e demonstra que a utilização organizada pode estar sujeita a solicitação/autorização e taxas; **não implica aluguer gratuito nem autorização genérica** para sessões anunciadas por terceiros.
- **Piscinas Municipais Rui Abreu:** a [página municipal](https://www.cm-coimbra.pt/areas/viver/desporto/equipamentos-desportivos/piscinas-municipais/piscina-municipal-rui-abreu) informa horário de funcionamento, mapa de eventos de 2026 e encerramento anual de 5 a 22 de agosto de 2026. Atividades, disponibilidade de pistas e utilização por terceiros permanecem condicionadas.
- **Centro Olímpico de Piscinas Municipais:** a [página municipal](https://www.cm-coimbra.pt/areas/viver/desporto/equipamentos-desportivos/piscinas-municipais/c-olimpico-de-piscinas) documenta horários e encerramento de 22 de agosto a 12 de setembro de 2026. O horário da instalação **não é reserva de plano de água**.
- **Localização Marialvas:** a [ficha do equipamento](https://www.cm-cantanhede.pt/mcsite/Entidade/1680/pavilhao-marialvas) indica GPS 40.344028,-8.587633; a [página de contactos](https://www.cm-cantanhede.pt/mcsite/pagina/844/Os-nossos-Contactos) indica 40.344047,-8.5879722, possivelmente outro ponto do mesmo complexo. Manter o ponto da ficha no candidato; **nenhum dos dois está confirmado como entrada**.

As três decisões individuais continuam `PENDING`. Esta pesquisa não atesta abertura no dia da sessão, disponibilidade para utilização de terceiros, nem substitui preflight atual à BD LIVE ou aprovação independente. Não inserir já.

## Execução offline, sem recolha nacional nova

Descarregar o artifact existente de [#39616305935](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39616305935): `national-venue-details-3514.jsonl.gz`. O SHA256 comprimido exigido pelo script é `e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a`, que impede reconstruções acidentais a partir de outro snapshot.

```bash
python3 -m unittest discover -s scripts -p 'test_prepare_official_venue_pilot.py' -v
python3 scripts/prepare_official_venue_pilot.py \
  --national-full-details /caminho/national-venue-details-3514.jsonl.gz \
  --municipal-evidence docs/venue-imports/first-three-official-pilot-20261021.json \
  --output-dir /tmp/jynlo-first-three
```

Produz `pilot-proposals-unapproved.jsonl`, `pilot-readonly-preflight.sql` e `pilot-summary.json`. A consulta SQL usa `BEGIN TRANSACTION READ ONLY`, deteta identidade por referência OSM, nome/concelho e instalações até 100 metros. O resultado das proximidades exige apreciação manual (pode ser outra subinstalação). O `generate_verified_venue_liquibase.py` **rejeita por design** os três rascunhos porque nenhum possui aprovação individual independente (`approved=true` com `approvedBy` e `verificationNotes`).

## Gates antes de disponibilizar os recintos

- Reexecutar a consulta read-only na BD LIVE no momento da publicação (os 265 confirmados podem ter mudado).
- Confirmar modalidade, morada e se o local está operacional no momento de utilização, incluindo eventuais taxas, programas, horários e necessidade de autorização.
- Confirmar se existe duplicado por alias, subinstalação ou outro identificador OSM; não basta ausência de referência exata.
- Aprovação explícita de cada registo por um revisor, export de backup consistente com teste de restauro, testes de PostgreSQL16 da migration idempotente; **só depois incluir changeset no master e fazer deploy**, por operação autorizada.
- Respeitar OpenStreetMap **ODbL** e DGT CAOP2025 **CC BY 4.0**, com atribuição apropriada.

Este PR tem somente scripts, testes e documentação, sem alteração no esquema, nos recintos da BD, no Render ou nas contas. Os 265 LIVE permanecem intactos.
