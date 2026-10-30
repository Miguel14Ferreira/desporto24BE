# Validação do primeiro lote de 50 — evidências oficiais (21/10/2026)

Um CSV com **50 candidatos**, escolhidos do catálogo nacional OSM/CAOP2025, foi produzido por `scripts/prepare_first_national_review_batch.py`. O ficheiro é um conjunto de **candidatos**, não um catálogo publicado, com modalidades/moradas ainda provenientes de OSM.

`docs/venue-imports/first-50-municipal-evidence-20261021.json` contém **11 referências OSM** com páginas municipais consultadas: 4 identidades e moradas corroboradas em ficha municipal (Marialvas, Rui Abreu, Centro Olímpico Coimbra, Piscina Casal Vistoso), 3 provas parciais de identidade, 2 divergências ou questões de morada, e 2 referências históricas/sazonais. Estas distinções não significam aprovação ou acesso livre, nem substituem revisão de duplicados do complexo.

Novo script `scripts/check_national_review_evidence.py` faz correspondência exata por referência OSM ao primeiro lote, verifica URL HTTPS do domínio português, data, identidade/nome/concelho e coerência de conflitos, produzindo **apenas**:
- `official-review-summary.json`
- `official-review-queue.csv` com `independently_approved_for_import=NO` em todas as 50 linhas e `opening_or_booking_claim=NOT_VERIFIED`.

Executar offline, num checkout do backend e com o CSV da primeira seleção:

```bash
python3 -m unittest discover -s scripts -p 'test_check_national_review_evidence.py' -v
python3 scripts/check_national_review_evidence.py \
  --first-50-csv /caminho/first-venue-review-batch.csv \
  --evidence-json docs/venue-imports/first-50-municipal-evidence-20261021.json \
  --output-dir /tmp/jynlo-review-50
```

O script **não faz pedidos de rede**, não consulta a BD e nunca cria SQL/migrations. A fonte `lisboa.pt` confirma a piscina do Casal Vistoso como subinstalação de um complexo; não concluir que instalações com nomes distintos representam recintos desportivos distintos até revisão manual. Em Loures, a utilização de pavilhões requer autorização escrita; os horários de Coimbra são condicionais e não representam reserva livre.

Não atribuir `approved=true` sem validação de identidade, modalidade, morada, acesso efetivo e pré-condições de importação. **Não fazer deploy** ou incluir mudanças no Liquibase master neste PR. Manter atribuição OSM ODbL/DGT CC BY 4.0.