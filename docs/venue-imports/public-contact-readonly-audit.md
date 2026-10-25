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
