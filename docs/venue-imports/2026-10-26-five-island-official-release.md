# Expansão oficial Jynlo — cinco instalações nas ilhas, 26/10/2026

Lote deliberadamente pequeno, com fontes de organismos públicos, moradas não inventadas e contactos/GPS opcionais (`NULL`). Não altera recintos existentes; publicá-los no catálogo não concede direitos de utilização, horários garantidos ou reservas gratuitas.

| Local | Distrito/região | Morada | Evidência pública |
|---|---|---|---|
| Complexo Desportivo João Paulo II | Açores · Angra do Heroísmo | Via Rápida, s/n, 9700-135 Angra do Heroísmo | [Direção Regional do Desporto dos Açores](https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha), complexo identificado com serviço local |
| Pavilhão Municipal de Desportos de Angra do Heroísmo | Açores · Angra do Heroísmo | Cerrado do Bailão, 9700-017 Angra do Heroísmo | [Município de Angra do Heroísmo](https://angradoheroismo.pt/pavilhao-municipal-de-desportos-de-angra-do-heroismo/), atualizado em 23/03/2026: uso **pago mediante reserva**; código postal corroborado pela Direção Regional |
| Pavilhão Desportivo do Juncal | Açores · Praia da Vitória | Canada da Água, n.º 1, Juncal, 9760-408 Praia da Vitória | [Governo dos Açores, diretório da Terceira](https://portal.azores.gov.pt/documents/37017/1838049/terceira.pdf/eeb62d3f-6801-5844-ddcc-a444316a3846?t=1602085352656); atividade local atual corroborada em 2026 |
| Complexo Desportivo Municipal da Madalena | Açores · Madalena (Pico) | Rua do Complexo Desportivo da Madalena, 9950-302 Madalena | [Direção Regional do Desporto](https://portal.azores.gov.pt/web/drd/servi%C3%A7os-de-desporto-de-ilha), [Jornal Oficial dos Açores 2026](https://jo.azores.gov.pt/pdf/jornais/II-2026-108-S0.pdf), [Junta de Freguesia da Madalena](https://www.jfmadalenapico.pt/freguesia/empresas/173-ilha_dos_sabores) para a designação da rua. A localização exata do edifício na rua, sem número, não foi comprovada |
| Pavilhão Multiusos do Porto Santo | Madeira · Porto Santo | Sítio das Matas, 9400-035 Porto Santo | [Governo Regional da Madeira](https://www.madeira.gov.pt/gvps/Estrutura/GAPS/ctl/Read/mid/10703/InformacaoId/13693/UnidadeOrganicaId/23/CatalogoId/0); utilizações escolares e federadas |

## Estado LIVE antes da inclusão

A API `/api/venues/suggest?district=Açores&page=0` continha **2** recintos (Laranjeiras e Sidónio Serpa), `...district=Madeira&page=0` continha **4** (Piscinas Funchal, Marcos Freitas, Ribeiro Real e Curral). Cada resposta tinha `hasMore=false`. Logo os cinco recintos propostos **não aparecem nesse catálogo regional**, segundo os critérios de busca públicos. Existe ainda proteção no SQL por `source_reference`, nome normalizado, morada normalizada e aliases específicos à localidade.

## Procedimento

- `changes/2026-10-26-five-island-official-venues.sql`: 5 INSERTs `WHERE NOT EXISTS ... ON CONFLICT DO NOTHING`; `contact=NULL`, GPS NULL, `information_source=OFFICIAL`; sem UPDATE, DELETE, preços ou horários inventados.
- `changes/2026-10-26-five-island-official-venues.yaml`: **um changeset atómico** PostgreSQL, registado uma única vez no master.
- `venue-five-islands-release-pg16.yml`: Liquibase 4.33, PostgreSQL16 efémero, alterações 5/5, execução dupla sem duplicação e casos de alias comunitário.
- PR apenas backend; frontend existente reconhece as duas regiões autónomas. Render backend tem `autoDeploy=no`, pelo que, após CI geral aprovado e merge, é preciso deploy manual. Verificar logs Liquibase (`Rows affected`) e APIs públicas de Açores e Madeira.
- Uma exportação real `.dir.tar.gz` foi vista no Render em 26/10/2026 e existe janela PITR de 3 dias. O backup real **não foi restaurado de forma isolada**; manter explícito este risco residual.
