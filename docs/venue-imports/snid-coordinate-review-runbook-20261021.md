# Jynlo — cruzamento GPS read-only: 27 recintos sem coordenadas

Objetivo: cruzar os 27 candidatos sem GPS do PR #136 com o **SNID/IPDJ** e preparar revisão humana das correspondências. Nunca alterar produção, nem atribuir pontos aproximados a campos de jogos distintos.

## Entradas
- `docs/venue-imports/snid-coordinate-candidates-27.json`: extraído dos manifestos dos 8 municipais, 7 Pestana e 13 municipais (menos o Complexo Aquático de Santarém, já com GPS). Este ficheiro descreve locais, *não* decisões de importação.
- Exportação pública do SNID `snid-localizacao-*.json` recolhida por HTTPS de `dados.gov.pt`. Informação cartográfica SNID sujeita à licença indicada na origem (IPDJ CC BY 4.0).

## Execução offline
```bash
python3 -m unittest discover -s scripts -p 'test_match_snid_venue_coordinates.py' -v
python3 scripts/match_snid_venue_coordinates.py \
  --manifest docs/venue-imports/snid-coordinate-candidates-27.json \
  --snid-json /caminho/snid-localizacao.json \
  --output-dir .cache/jynlo-venues/snid-matches
```

Em ambiente GitHub Actions (sem acesso à BD):
```bash
python3 scripts/match_snid_venue_coordinates.py \
  --manifest docs/venue-imports/snid-coordinate-candidates-27.json \
  --fetch-official \
  --output-dir /tmp/jynlo-snid-27
```

O CSV contém uma linha por candidato e, quando existe, **uma proposta de coordenadas para revisão**. O JSON inclui até três alternativas por candidato e a referência da fonte.

## Regras de segurança e qualidade
- **Nunca** utilizar automaticamente estes resultados num changelog Liquibase. Todo match é `REVIEW_...`; não existe estado `APPROVED`.
- A concordância de nome e distrito no SNID é um indício, **não** uma verificação CAOP2025 da geometria. O passo seguinte é validar as coordenadas no município correto e confrontar a posição com o recinto exato, dados municipais e OSM.
- Vários campos do **Complexo Desportivo da Rodovia (Braga)** podem partilhar a morada ou a localização da entrada, mas *não* devem herdar as mesmas coordenadas como se fossem campos individuais.
- Sem correspondência SNID `NO_PLAUSIBLE_SNID_NAME_MATCH` **não** significa que a instalação não exista. Revisar as fontes municipais e OSM; é normal existirem nomes alternativos.
- O catálogo pode conter instalações encerradas, de acesso condicionado ou sazonais. Nunca inferir horários, disponibilidade ou possibilidade de reserva.
- Os relatórios nunca escrevem na BD nem ativam o PR #136; para publicação ainda são obrigatórios validação de duplicados, cópia recuperável e autorização de release.
