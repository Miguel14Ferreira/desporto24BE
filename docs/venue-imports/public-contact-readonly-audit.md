# Auditoria read-only de telefones públicos dos recintos

O PR FE #419 já apresenta os contactos existentes e aceita sugestões sujeitas
a aprovação administrativa. Este script **mede a cobertura** e gera propostas
**apenas para revisão humana**. Nunca faz POST, altera PostgreSQL, recolhe
dados do Google Maps nem importa recintos.

## Inventário LIVE através do GitHub Actions

O workflow "Venue public contact read-only audit" executa testes offline e
tenta um único GET anónimo a https://api.d24sports.com/api/venues. Se a API
falhar, a medição não é considerada confirmada. São publicados apenas
contact-summary.json e contact-districts.csv, com contagens agregadas durante
7 dias. Também suporta workflow_dispatch, sem deploy do Render.

Exemplo de execução:

    python3 scripts/audit_venue_public_contacts.py \
      --live-public-api \
      --summary-json /tmp/contact-summary.json \
      --districts-csv /tmp/contact-districts.csv

Modo offline, a partir de um JSON obtido previamente na API pública:

    python3 scripts/audit_venue_public_contacts.py \
      --venues-json /tmp/public-venues.json \
      --summary-json /tmp/contact-summary.json \
      --districts-csv /tmp/contact-districts.csv

Os relatórios incluem 18 distritos e "Por confirmar". Medem apenas os
recintos devolvidos pela API naquele instante, não a cobertura de Portugal.
"phone" só significa formato plausível português, não telefone confirmado.

## Sugestões licenciadas, sempre privadas

Após confirmar a licença de reutilização e um contacto específico de recinto,
preparar um JSON de evidência com campos venueId, venueName, city, district,
phone, sourceType, sourceUrl, sourceLicense, reusePermissionChecked e observedAt.
NÃO assinalar reusePermissionChecked como true sem verificar os direitos de
reutilização. Nomes, IDs e telefones fictícios de testes não são evidência real.

    python3 scripts/audit_venue_public_contacts.py \
      --venues-json /tmp/public-venues.json \
      --proposals-json /tmp/licensed-source-evidence.json \
      --review-csv /tmp/PRIVATE-contact-review.csv \
      --summary-json /tmp/contact-summary.json \
      --districts-csv /tmp/contact-districts.csv

É exigida correspondência exata de ID, nome, cidade e distrito. Conflitos,
contactos atuais, telefones inválidos e fontes não verificáveis ficam
assinalados. Mesmo correspondências exatas ficam com REVIEW_REQUIRED,
**nunca APPROVED**. Não existe qualquer comando de escrita na BD.

O CSV privado contém contactos candidatos e evidências. Nunca publicar o CSV
no Git, PR, logs ou artifacts. A exportação neutraliza fórmulas perigosas
para folhas de cálculo. Só relatórios agregados podem ser públicos.

## Fontes permitidas

Priorizar IPDJ/SNID se a licença e a existência de telefones por recinto forem
confirmadas, ou páginas oficiais das autarquias com reutilização autorizada.
Para campos phone/contact:phone do OpenStreetMap, verificar ODbL, atribuição e
efeitos de base derivada antes de combinar dados. Nunca copiar Google Maps em
massa nem imputar o telefone geral de uma câmara a um recinto.

SNID: https://ipdj.gov.pt/sistema-nacional-de-informa%C3%A7%C3%A3o-desportiva-snid-

OSM: https://osmfoundation.org/wiki/Attribution_Guidelines

Issue: https://github.com/Miguel14Ferreira/desporto24BE/issues/359

Testes offline:

    python3 -m unittest discover -s scripts -p 'test_audit_venue_public_contacts.py' -v

## Piloto Porto — referências oficiais de instalações (25/10/2026)

O catálogo piloto está em
`docs/venue-imports/porto-municipal-contact-sources-20261025.json`.
Contém apenas **três URLs de páginas oficiais da Ágora**, nome de cada piscina,
localidade, indicação de rua e data da consulta. **Não contém números de
telefone, Jynlo IDs deduzidos, credenciais ou autorização de reutilização**.

O comando abaixo consulta apenas uma API pública da Jynlo e compara os nomes
da lista com os recintos LIVE, rejeitando duplicados, cidades/distritos
divergentes, moradas por confirmar e contactos já preenchidos:

    python3 scripts/pilot_porto_public_contact_sources.py \
      --live-public-api \
      --sources-json docs/venue-imports/porto-municipal-contact-sources-20261025.json \
      --output-json /tmp/porto-source-matches.json

O JSON só identifica IDs correspondentes e ligações **às fontes**. As linhas
com `POSSIVEL_MATCH_REVISAO_HUMANA` **não estão aprovadas**. Nunca
inserir o telefone na BD ou na API sem verificar, para aquele recinto exato,
o contacto público, a morada e a licitude da reutilização. Moradas
`MORADA_DIVERGENTE_REVER` ou `MORADA_NAO_CONFIRMADA` exigem revisão adicional.
Não generalizar o telefone da entidade gestora para outras instalações.

A execução do GitHub Actions reutiliza o workflow de auditoria já existente,
sem instalar serviços de pagamento ou aceder à base de dados. O arquivo da CI
pode conter apenas os três links oficiais e os IDs resultantes; não publica
o CSV privado de propostas nem números telefónicos individuais.

    python3 -m unittest discover -s scripts -p 'test_pilot_porto_public_contact_sources.py' -v

Páginas oficiais do piloto:
- https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-da-constituicao
- https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-de-cartes
- https://www.agoraporto.pt/desporto/piscina-municipal-eng-o-armando-pimentel

Foi encontrado ainda um dataset municipal de instalações identificado como
CCZero no portal de dados abertos do Porto, mas o ficheiro de dados não
estava acessível durante a verificação. **Não assumir que contém telefones**:
https://opendata.porto.digital/en/dataset/desporto-atlas-desportivo-designacao-da-instalacao-desportiva/resource/bf5a9220-febd-4bd4-925c-afd4d7c10737

## Segunda auditoria Porto: nomes e códigos postais oficiais divergentes

O piloto também permite **pistas para revisão manual** quando a Jynlo não tem
o nome exato de uma das três instalações. Só é devolvida uma lista de, no máximo,
três recintos da própria cidade e distrito do Porto que contenham **duas
palavras distintivas completas** do nome oficial. Termos genéricos como
"PISCINA", "MUNICIPAL" ou "ENGENHEIRO" não valem como identificação.

Essas pistas ficam em `manual_name_hints` e **nunca são promovidas a
`jynlo_venue_id`, não acionam sugestões de contactos nem importações**.
Mais de três homónimos significam lista vazia, não uma escolha arbitrária.
Um nome exato com localidade divergente continua bloqueado.

Foram acrescentadas à lista de fontes as variantes da piscina Eng. Armando
Pimentel usadas no [Portal do Desporto da Ágora](https://desporto.agoraporto.pt/Desporto/Centers).
Para Cartes, a [ficha da Ágora](https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-de-cartes)
indica **4300-008** e o Portal do Desporto indica **4300-088**.
Não resolver essa discrepância por palpite, nem preencher a morada em produção
sem confirmação administrativa. Cartes continua pendente mesmo que o ID Jynlo
seja conhecido.

O script continua read-only, limitado ao endpoint público e sem emitir
contactos pessoais ou números individuais; o mesmo workflow CI verifica
os cenários de nomes/aliases, distrito/cidade, duplicados e ambiguidades.

## Auditoria adicional: testemunhos da morada para nomes semelhantes

Para pistas `manual_name_hints`, o relatório passa também a publicar
`street_review` (compatível / divergente / por confirmar) e
`has_existing_contact` (booleano). São atributos de uma ficha **já pública**;
o algoritmo não exporta nenhum telefone, email ou contacto individual, não
escolhe o ID automaticamente e não altera a ficha.

A comparação de ruas considera apenas cadeias explícitas. Mesmo se um
candidato tem duas palavras distintivas e rua compatível, permanece
`SEM_NOME_EXATO_NA_JYNLO` e requer confirmação manual de identidade.

Fontes oficiais Ágora pertinentes para distinguir moradas:
- Constituição: Rua Almirante Leote do Rêgo
- Cartes: Alameda de Cartes; discrepância de código postal nas duas fichas
- Armando Pimentel: Rua de Diogo Botelho (4150-262 Porto)
