# Verificações de recintos sem consumir minutos do GitHub

Execução única, em qualquer Linux/WSL com Python 3.12:

```bash
bash scripts/check_venues_offline.sh
```

O comando compila o importador, gerador e testes; executa a suite Python
com `unittest`, que inclui o gerador Liquibase, o piloto de Portalegre, a
classificação dos distritos antigos e o conversor nacional. Verifica também
que os changelogs DRAFT permanecem fora do ficheiro master.

**Não precisa** de acesso à BD, credenciais, internet, GitHub Actions,
Render nem descarregar os 18 distritos. Não importa recintos.

## Diagnóstico local adicional (19 outubro 2026)

Foi executada uma simulação SQLite independente, a partir do ZIP do piloto
real de Portalegre (13 recintos recolhidos, dos quais 9 no draft).
**Seis cenários passaram:** primeira execução e reaplicação, ausência de
duplicados para recintos com a mesma fonte, coordenadas iguais, ou nome e
cidade iguais sem GPS, preservação de recintos comunitários, e permissão de
homónimos noutra cidade. O teste simulou a guarda SQL corrigida; **não é
a execução literal do changelog pelo PostgreSQL**.

Os testes novos no repositório ainda não foram executados pelo GitHub:
o PR permanece DRAFT e o orçamento Actions está esgotado. Após recuperação
do orçamento, fazer um único teste PostgreSQL 16 em ambiente temporário e
rever a consulta de duplicados na BD real em modo read-only.

Não ligar os scripts de inserção ao master automaticamente.
