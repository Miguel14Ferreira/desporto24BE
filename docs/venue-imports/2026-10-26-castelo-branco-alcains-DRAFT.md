# Dois novos recintos propostos — Castelo Branco e Alcains

Data: 26/10/2026. Estado: **DRAFT sem escrita ou publicação LIVE**.

## Pavilhão Municipal de Castelo Branco

O [Município de Castelo Branco](https://www.cm-castelobranco.pt/visitante/contactos-uteis/) confirma a instalação e morada na Avenida Rotary, Castelo Branco. O [programa da DGE de 2024](https://desportoescolar.dge.medu.pt/sites/default/files/RepositorioDocumentos/2024/programasnf2024.pdf) identifica Avenida Rotary, 6000-087 e utilização de **Futsal** nesse pavilhão. Não inferir outras modalidades ou disponibilidade de reserva.

Morada: Avenida Rotary, 6000-087 Castelo Branco. Modalidade: Futsal. Contacto: **NULL**; GPS: **NULL**.

## Complexo de Piscinas Municipais de Alcains

A [ALBIGEC descreve o complexo](https://www.albigec.pt/equipamentos/complexo-de-piscinas-de-alcains/), a [Escola de Natação 2026/27 começou em 28/09](https://www.albigec.pt/noticias/2026/escola-de-natacao-da-albigec-inicia-nova-temporada-a-28-de-setembro/) e a [época da piscina exterior acabou em 06/09/2026](https://www.albigec.pt/noticias/2026/encerramento-da-epoca-balnear-2026/). A ficha da ALBIGEC contém também a indicação «Piscinas Cobertas (Encerradas)»; há **informação aparentemente contraditória** sobre piscina interior. Não apresentar como aberta sem clarificação.

**Uma só ficha** para o complexo, sem duplicar piscinas internas/exteriores. Cidade Alcains, distrito Castelo Branco; modalidade Natação; ambiente BOTH. Rua e GPS desconhecidos, por isso address/latitude/longitude = NULL; telefone NULL.

## Controlos para importar

A auditoria do PR #400 classificou os nomes como sem correspondência, mas **isso não é uma prova definitiva de ausência**. Este PR tem SQL idempotente, preflight SELECT amplo por aliases e testes PostgreSQL 16 descartáveis + GET público LIVE para detetar homónimos. O SQL **não aparece no master Liquibase**, logo não executa em produção.

Antes da promoção para produção: revisão humana de aliases, preflight no PostgreSQL real, **backup real atual testado por restauração**, confirmação de identidade e funcionamento, aprovação de release e deploy manual com contagem antes/depois. Não ativar comunicações para recintos ou contactos não comprovados.

Issue: https://github.com/Miguel14Ferreira/desporto24BE/issues/99
