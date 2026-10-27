# Évora — Piscinas Municipais (Jynlo 273): link municipal de mapa, não GPS validado da entrada

**Auditoria de 27-10-2026** — melhoria de proveniência na fila ADMIN, sem gravação GPS.

## Novo facto confirmado diretamente no portal municipal

Na página oficial de **Contactos e Localização** das Piscinas Municipais de Évora, o elemento de hiperligação **«ver no mapa»** aponta para `http://www.google.com/maps/place/38.573234, -7.928876`. O URL contém literalmente `38.573234, -7.928876`; a página identifica as **Piscinas Municipais** e a morada **Av. Eng.º Arantes de Oliveira, 7000-758 Évora**.

Fonte oficial HTTPS para rever no browser:
https://www.cm-evora.pt/municipe/equipamentos/equipamentos-desportivos/piscinas-municipais/contactos-e-localizacao-das-piscinas-municipais/

O ponto já existia na investigação read-only da Jynlo em `GPS_RESEARCH_LEADS[273]`. A inspeção detalhada confirma agora **a proveniência municipal explícita das coordenadas**, não se trata apenas de um geocoding baseado na morada ou uma hipótese de terceiros.

### Limites cruciais

- **A fonte municipal não especifica que o marcador seja a entrada, portão principal ou acesso público às piscinas.** Nem o texto publicado nem a âncora distinguem quais as piscinas/portões de um complexo com várias instalações e estacionamento.
- O destino `http://www.google.com/maps/place/...` é **HTTP**. Não o apresentar na UI como link HTTPS fiável. Usar a página oficial HTTPS como fonte da evidência.
- A «Carta das Instalações Desportivas de Évora» está identificada pelo município como ainda em atualização: https://www.cm-evora.pt/municipe/equipamentos/equipamentos-desportivos/carta-das-instalacoes-desportivas-do-concelho-de-evora/
- O Portal Geográfico municipal existe, mas não foi demonstrado que identifique o portão de entrada desta instalação: https://www.cm-evora.pt/servicos/servicos-online/portal-geografico-do-municipio-de-evora/
- A página da piscina em 2026 apresenta várias instalações interiores e exteriores; não transferir automaticamente o marcador do complexo a cada piscina individual.
- **Não alterar coordenadas, morada, estados de abertura, instalação, horários ou dados públicos.**

## Alteração exclusivamente administrativa

O backend mantém exata a identidade guardada de `273` e todas as condições anteriores (nome, município, distrito, substring de endereço, website HTTPS oficial, fonte OFFICIAL, ambas as coordenadas vazias). Apenas troca o código de investigação de `SITE_REFERENCE_NOT_VERIFIED_ENTRANCE` para `MUNICIPAL_MAP_LINK_SITE_NOT_VERIFIED_ENTRANCE` quando este caso rigorosamente identificado ainda é elegível.

O frontend reconhece ambos os códigos, com aviso PT/EN específico para Évora e o mesmo mapa consultivo de investigação, sem qualquer botão de aprovação ou `save`. Valores inesperados são rejeitados; valores numéricos não finitos são rejeitados. Os restantes candidatos 276, 281 e 283 mantêm o código anterior.

**Para fechar o GPS de produção:** confirmar presencialmente ou através de fonte cartográfica/operador a localização exata da entrada do equipamento, submeter alteração moderada e obter aprovação específica. Até aí, **GPS do ID 273 permanece por validar**.
