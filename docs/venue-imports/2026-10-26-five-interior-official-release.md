# Lote Jynlo — 5 recintos do interior: Guarda, Bragança e Castelo Branco (26/10/2026)

O catálogo da Jynlo contém recintos registados via fontes externas com diferentes graus de certeza; esta expansão utiliza **fontes oficiais municipais com morada explícita**, sem inventar GPS ou telefone. O site Jynlo não efetua reservas nem recebe pagamentos. A existência na lista **não implica gratuidade ou livre utilização**.

| Instalação | Município e distrito | Morada confirmada | Fonte |
|---|---|---|---|
| Complexo Desportivo Municipal 1 de Seia | Seia, Guarda | Rua Dra. Ester Barata, 6270-652 Seia | [Câmara de Seia, equipamentos](https://cm-seia.pt/viver/desporto/equipamentos/complexo-desportivo-municipal-1/) |
| Gimnodesportivo Municipal Padre Martinho | São Romão (município de Seia), Guarda | Largo Outeiro da Forca, 6270-284 São Romão | [Câmara de Seia, ginásio e modalidades](https://cm-seia.pt/viver/desporto/equipamentos/gimnodesportivo/) |
| Pavilhão Gimnodesportivo José Pina – INATEL | Mirandela, Bragança | Rua da Força Aérea, n.º 185, 5370-265 Mirandela | [Câmara de Mirandela, ata de 18/06/2025](https://www.cm-mirandela.pt/cmmirandela/uploads/document/file/7405/15___ata_18_de_junho.pdf), a confirmar gestão municipal e proprietário INATEL; [Plano municipal de emergência](https://www.cm-mirandela.pt/cmmirandela/uploads/document/file/4577/planomunicipalcontingenciacoronavirus_mirandela_v13.pdf) para o código postal |
| Pavilhão Gimnodesportivo de Belmonte | Belmonte, Castelo Branco | Rua Chafariz do Areal, 50, 6250-049 Belmonte | [Município de Belmonte, contactos úteis](https://www.cm-belmonte.pt/contactos-uteis/); [evento municipal de futsal, outubro 2026](https://www.cm-belmonte.pt/agenda-de-eventos/cm-belmonte-vs-cm-oeiras/) |
| Pavilhão Gimnodesportivo de Caria | Caria (município de Belmonte), Castelo Branco | Rua de São Domingos, 18, 6250-111 Caria | [Câmara de Belmonte, diretório](https://www.cm-belmonte.pt/diretorio/pavilhao-gimnodesportivo-de-caria/) |

**Critérios de exclusão:** o Estádio Municipal de Seia não foi incluído porque a API Jynlo já devolve `Centro Desportivo de Seia` com geolocalização potencialmente correspondente. O `Complexo Desportivo Municipal 2` não foi incluído por existir um aviso municipal de encerramento temporário em julho 2026. Pavilhão Municipal e Gimnodesportivo de Belmonte são uma única instalação, representada por só um registo. Não atribuir acesso livre ao pavilhão INATEL sem confirmação das condições de uso.

## Preflight e release

- A API LIVE `GET /api/venues/suggest?district=Guarda&page=0/1`, `...district=Bragança&page=0/1`, `...district=Castelo Branco&page=0/1` foi verificada antes da migração; nenhum dos cinco nomes completos aparece entre as entradas destas páginas. Esta pesquisa não é garantia de ausência de aliases desconhecidos.
- O SQL `2026-10-26-five-interior-official-venues.sql` inclui cinco `INSERT .. SELECT .. WHERE NOT EXISTS .. ON CONFLICT DO NOTHING`, cruzando `source_reference`, nomes e moradas nas localidades, com aliases comuns. Não altera os registos anteriores. Campos `contact`, `latitude` e `longitude` continuam NULL. Os campos de instalações não documentados (balneários, parque, luz) não são presumidos como confirmados.
- Uma migração transacional `2026-10-26-five-interior-official-venues-v1` registada uma vez no master do Liquibase.
- CI `.github/workflows/venue-five-interior-release-pg16.yml`: PostgreSQL16 efémero, Liquibase 4.33 verdadeiro em duas execuções, integridade de 2 Guarda / 1 Bragança / 2 Castelo Branco, unicidade, moradas, NULL de GPS/telefone, preservação de três possíveis aliases comunitários.
- Após checks CI gerais e específicos, merge para `feature/desporto24-preview-deploy` e deploy manual Render `srv-darsqqgjo6nc739a10v0`; `autoDeploy=no`. Confirmar no log a execução do changeset e `Rows affected` e consultar as fichas na API pública. Não afirmar total global sem contagem real.

**Recuperação:** O Render PostgreSQL dispõe de recuperação PITR com retenção de três dias e existia export lógico `.dir.tar.gz` de 26/10 antes do lote anterior. Foi validado procedimento de recuperação com dump **sintético**, mas não foi restaurado o dump real da Jynlo. Este risco residual já foi discutido para lotes idempotentes limitados. Nunca abrir a base de dados ao exterior ou criar recurso pago sem autorização.
