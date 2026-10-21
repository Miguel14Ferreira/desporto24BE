# GPS sem morada postal — importação municipal/nacional (gate opt-in)

A migration LIVE `2026-10-21-venue-gps-only-address-nullable-v1` permite `venue.address = NULL` **apenas com GPS completo em Portugal continental**. O gerador offline `scripts/generate_verified_venue_liquibase.py` passa a suportar esta situação sem fabricar moradas.

## Contrato obrigatório para um candidato GPS-only

O revisor deve fornecer **explicitamente**:
- `address: null` (campo presente, não string vazia ou «Morada por confirmar»);
- `postalAddressStatus: "UNKNOWN_GPS_ONLY"`;
- `gpsVerified: true` **após** confirmar a coordenada independentemente;
- `gpsEvidenceUrl`: ligação HTTPS à fonte independente; uma ligação apenas a OSM não chega;
- `gpsVerificationNotes`: notas concretas com pelo menos 40 caracteres;
- aprovação **individual** pré-existente `approved: true`, `approvedBy` nominal e `verificationNotes` substantivas;
- referência OSM permitida, nome verdadeiro, município, distrito, modalidades, tipo, latitude/longitude no território continental.

Moradas postais efetivamente verificadas continuam a funcionar sem estes novos campos e não podem ser marcadas como GPS-only.

A máquina verifica **presença, formato e coerência mínima**, não consegue certificar que o GPS aponta à instalação certa ou que o recinto funciona. A revisão humana tem de conferir identidade, GPS, operação, acesso/necessidade de autorização, modalidades, aliases e duplicados. Instalações privadas, escolares ou com condições desconhecidas não se tornam automaticamente disponíveis para sessões apenas por terem coordenadas.

## Passos antes de qualquer inserção

1. Confirmar fonte independente por recinto, manter informação de acesso condicional visível aos utilizadores da Jynlo, e recolher aprovação individual verdadeira.
2. Executar preflight read-only **atual** de referências, nomes/aliases e distância na BD LIVE, e registar correspondências; o preflight anterior de 265 recintos não é uma autorização permanente.
3. Fazer backup consistente e **restauro comprovado** em PostgreSQL 16 isolado. Validar a constraint de endereço/GPS e executar o SQL sintético duas vezes para garantir idempotência.
4. Gerar changeset com `scripts/generate_verified_venue_liquibase.py` apenas a partir de ficheiro verificado. Auditar SQL, confirmar zero colisões e incluir deliberadamente o YAML no master em PR próprio de release.
5. Publicar só num deploy backend controlado e confirmar Liquibase, endpoint público e nova contagem.

Este ajuste **não altera master**, não executa SQL, não cria recintos, não abre a rede da BD e não aciona o Render. O catálogo nacional de 3514 candidatos é apenas matéria-prima de revisão, não um ficheiro de importação. Atribuição OSM (ODbL) e DGT CAOP2025 (CC BY 4.0) mantêm-se.
