# Jynlo — GPS municipais específicos: Fontelo (Viseu) e Piscinas da Guarda

**21 outubro 2026.** Auditoria de localização; **não há qualquer importação nem alteração à BD**.

| Recinto candidato | Fonte de coordenadas | Formato original | WGS84 (lat, lon) | Verificações pendentes |
|---|---|---|---|---|
| Piscinas Municipais de Viseu | [Câmara Municipal de Viseu](https://www.cm-viseu.pt/pt/areas-servicos/desporto-atividade-fisica/complexo-desportivo-do-fontelo/piscinas-municipais-de-viseu/) | 40°39'35.46"N, 7°54'11.37"W | 40.659850000, -7.903158333 | CAOP, acesso, duplicados atuais |
| Campos de Ténis do Fontelo | [Câmara Municipal de Viseu](https://www.cm-viseu.pt/pt/areas-servicos/desporto-atividade-fisica/complexo-desportivo-do-fontelo/campos-de-tenis/) | 40°39'37.19"N, 7°54'4.49"W | 40.660330556, -7.901247222 | CAOP, acesso, duplicados atuais |
| Piscinas Municipais da Guarda | [Portal VisitGuarda](https://www.visitguarda.pt/en/ponto_de_interesse/piscina-municipal-da-guarda/) | 40.531141,-7.253715 | 40.531141, -7.253715 | **Diferença de código postal**, CAOP, duplicados atuais |

**Discrepância prioritária na Guarda:** o release #136 usa «Bairro Nossa Senhora dos Remédios, **6300-509** Guarda»; o VisitGuarda apresenta «Rua Nossa Senhora dos Remédios, **6300-320** Guarda». A pista GPS é credível como *referência*, mas a morada/postal deve ser reconciliada antes de qualquer alteração ao changelog.

**Contexto de qualidade:** as Piscinas de Viseu e os Campos de Ténis ficam no mesmo Parque do Fontelo, mas os seus GPS publicados são **distintos**. Não preencher ambos com o centro do parque. A Guarda tem instalações exteriores sazonais e interiores: não anunciar as exteriores como disponíveis durante todo o ano. Não inferir que os utilizadores podem organizar jogos ou reservar instalações sem autorização.

**Execução:** o workflow específico descarrega o GeoPackage DGT CAOP2025 (sem tocar no Render), verifica cada ponto contra municípios/distritos e guarda apenas JSON/CSV de revisão. Nenhum match autoriza importação automaticamente. Antes de alterar Liquibase, confirmar município, identidade da instalação, endereço, acesso e consulta de duplicados na BD.

Attribution: CAOP2025 © Direção-Geral do Território (CC BY 4.0). Fontes municipais nas ligações acima.
