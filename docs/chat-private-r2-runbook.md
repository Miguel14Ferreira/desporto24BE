# Jynlo — ativar armazenamento privado dos anexos do chat

## Estado inicial
O FE e o BE já têm envio/receção de fotografias, GIFs, PDFs e áudio Opus para mensagens privadas. Em produção, o botão 📎 / 🎙️ mantém-se oculto enquanto o servidor não comprovar que existe armazenamento privado.

**Não reutilizar o bucket público do feed. Não enviar chaves de API por email, PR ou chat.**

## Configuração Cloudflare R2 (exige acesso do administrador Cloudflare)

1. Em **Cloudflare Dashboard → R2 object storage**, criar um bucket novo, por exemplo `jynlo-chat-private` (confirmar que o nome está disponível).
2. **Não ativar R2.dev nem associar domínio público ao bucket.** Os downloads passam exclusivamente pela API autenticada da Jynlo.
3. Em **R2 → API Tokens**, criar um token *Object Read & Write*, restringido **apenas a este novo bucket**.
4. Guardar o **Access Key ID** e **Secret Access Key** como segredos do Render. Não reutilizar as credenciais do bucket público.

## Configuração do backend no Render

Serviço backend Jynlo (`srv-darsqqgjo6nc739a10v0`), em *Environment*:

| Variável | Valor |
| --- | --- |
| `MEDIA_PRIVATE_BUCKET` | Nome do novo bucket privado |
| `MEDIA_PRIVATE_ACCESS_KEY` | Access Key ID do token R2 privado |
| `MEDIA_PRIVATE_SECRET_KEY` | Secret Access Key do token R2 privado |

Permanecem as configurações existentes `MEDIA_PROVIDER=s3`, `MEDIA_S3_ENDPOINT` e `MEDIA_S3_REGION=auto` (e as credenciais do feed público permanecem sem alterações). Não é necessário adicionar um URL público.

Em cada arranque a configuração tem de estar correta, e em cada verificação de disponibilidade a API executa (com cache de 30 segundos) um `HeadBucket` autenticado. Se o bucket estiver ausente, os segredos estiverem em falta, o token não tiver acesso, ou o endpoint não for Cloudflare R2 via HTTPS, o sistema responde `available: false`; os uploads nunca usam o bucket público como alternativa.

## Verificação controlada

1. Confirmar que o backend voltou a **LIVE** no Render, sem falhas no arranque.
2. Entrar com uma conta QA e abrir conversa privada com uma segunda conta QA já amiga. O 📎 e o 🎙️ só podem aparecer quando a API confirmar o bucket privado.
3. Enviar um ficheiro de teste JPG/GIF/PDF pequeno (até 5 MB) e uma nota de voz de teste até 60 segundos.
4. Verificar a pré-visualização e reprodução em ambos os browsers QA.
5. Confirmar que a API exige autenticação para obter o anexo e impede pedidos de outra conversa ou de contas sem permissão. Não expor a chave R2 na resposta JSON, no HTML nem no histórico WebSocket.
6. Confirmar que o bucket não tem domínio público/R2.dev ativados.

## Limites desta fase

- A interface de anexos multimédia é apenas para **conversas privadas**. Comunidades e sessões ainda exigem implementação dedicada.
- Fotografias JPG/PNG/WebP, GIF, PDF e voz Opus WebM/Ogg; um anexo por mensagem, até **5 MB**.
- Não existe pesquisa integrada de GIFs.
- Quando o bucket está por configurar, o chat de texto e os vistos de leitura continuam operacionais.
- O teste com ficheiros reais QA deve ocorrer **após** ativação do bucket privado.
