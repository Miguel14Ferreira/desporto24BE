# Jynlo — piloto municipal: triagem pública LIVE, 21/10/2026

Execução factual: [GitHub Actions #39683968426](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39683968426), concluída com sucesso às 22:06 UTC, commit `0ce2c762ba7ca6164c668c556a68e42d85aa07d1`. Esta é uma observação pontual, não uma garantia de não duplicação futura.

## API pública e resultados

O script `scripts/live_public_venue_pilot_preflight.py` fez **seis GET HTTP anónimos** a `https://api.d24sports.com`: três `/api/venues/suggest?name=...&city=...&page=0` e três `/api/venues?latitude=...&longitude=...&radiusKm=0.15`. HTTP e JSON responderam corretamente, os testes offline passaram e nenhum pedido alterou dados.

| OSM | Recinto | Correspondências públicas |
|---|---|---|
| WAY:145943204 | Pavilhão Marialvas, Cantanhede | 0 |
| WAY:122685516 | Piscinas Municipais Rui Abreu, Coimbra | 0 |
| WAY:1056855283 | Centro Olímpico de Piscinas Municipais, Coimbra | 1: **ID 262**, Pavilhão Municipal Multidesportos Mário Mexia, Coimbra, **108 m** |

**Mário Mexia é identificado como pavilhão multidesportos, não piscina.** Distância <=150 m é alerta de instalações vizinhas, não autorização para juntar ou eliminar recintos. O preflight SQL anterior tinha limiar 100 m, pelo que não é contraditório ter indicado zero proximidades nesse limiar.

## O que os resultados NÃO demonstram

- A API pública `VenueResponse` **não expõe `sourceReference`**; a triagem não compara referências OSM e não acede a duplicados por alias complexo.
- Um candidato sem correspondências públicas **não está aprovado** nem garante que a BD LIVE ainda tenha os mesmos 265 registos da última consulta read-only.
- Horários, uso por terceiros, disponibilidade para criar sessões e preços não foram verificados por estes GET. Nem a presença no catálogo constitui reserva de recinto.
- A aprovação individual das três propostas permanece `PENDING` e `approved=false`, tal como definida no repositório.
- Nenhum script executou INSERT/UPDATE/DELETE, não houve conexão PostgreSQL, nem deploy Render.

## Próximo gate antes de importar

Executar preflight SQL read-only atualizado na BD com referências exatas, nomes/aliases e 100–150 m por candidato; analisar especificamente se Mário Mexia e Centro Olímpico são subinstalações independentes. Confirmar fontes municipais atuais e políticas de acesso/cedência, registar aprovação real por recinto, efetuar backup+restauro testado e só então gerar/ativar um changeset Liquibase isolado.

Fontes municipais de referência: [Pavilhão Marialvas](https://cm-cantanhede.pt/mcsite/entidade/1680/pavilhao-marialvas), [Piscinas Rui Abreu](https://www.cm-coimbra.pt/areas/viver/desporto/equipamentos-desportivos/piscinas-municipais/piscina-municipal-rui-abreu), [Centro Olímpico](https://www.cm-coimbra.pt/areas/viver/desporto/equipamentos-desportivos/piscinas-municipais/c-olimpico-de-piscinas). O URL municipal de Cantanhede descreve instalação e GPS, mas a ficha é datada de 2022; confirmações operacionais de 2026 são assunto separado.
