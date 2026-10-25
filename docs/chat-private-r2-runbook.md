# Jynlo — ativar e validar o R2 privado dos anexos dos chats

## Estado e limites

O backend e o frontend suportam os seguintes anexos, sujeitos a autorização:
- **Chat individual:** apenas a conversa entre duas contas com acesso autorizado; bloqueios e amizade verificados no servidor.
- **Chat de comunidade:** apenas membros atuais da comunidade. Acesso à mensagem e à comunidade é verificado em cada download. Os **posts** mantêm o seu armazenamento de multimédia separado e público, conforme a funcionalidade do feed.
- **Chat de sessão:** apenas organizador e participantes ativos **antes do início do jogo**. Depois do kickoff, cancelamento, conclusão ou declaração de que não ocorreu, os endpoints de chat ativo deixam de servir anexos e novas mensagens. O frontend também fecha o chat, limpa previews e descarta downloads pendentes.

São permitidos JPG, PNG, WebP, GIF, PDF e mensagens de voz Opus WebM/Ogg. Limites: **um anexo por mensagem, até 5 MB**, gravação de voz até 60 segundos. Não existe pesquisa integrada de GIFs.

**Importante:** o código está publicado, mas a existência/configuração do bucket privado e a validação real em produção **não estão confirmadas**. Os botões 📎 e 🎙️ só aparecem quando o backend da conversa específica confirma `available: true`.

## Cloudflare R2 — intervenção autorizada

Esta etapa exige acesso administrativo à conta Cloudflare. Não fornecer tokens, credenciais ou screenshots com segredos no chat, GitHub, email ou PR.

1. Em **Cloudflare Dashboard → R2 Object Storage**, criar um bucket privado dedicado (nome sugerido: `jynlo-chat-private`, sujeito a disponibilidade).
2. **Não habilitar R2.dev nem associar domínio público** ao bucket. A API Jynlo entrega ficheiros exclusivamente após autenticação e autorização.
3. Em **R2 → API Tokens**, gerar credenciais **Object Read & Write**, limitadas ao bucket privado criado.
4. O Access Key ID e Secret Access Key privados devem ser **ambos diferentes** dos usados no feed público. O servidor rejeita credenciais reutilizadas.

## Render — variáveis de ambiente

No backend **desporto24-api** (`srv-darsqqgjo6nc739a10v0`), em **Environment**, configurar:

| Variável | Valor |
| --- | --- |
| `MEDIA_PRIVATE_BUCKET` | Nome exato do bucket privado |
| `MEDIA_PRIVATE_ACCESS_KEY` | Access Key ID do token do bucket privado |
| `MEDIA_PRIVATE_SECRET_KEY` | Secret Access Key do token do bucket privado |

Manter intactos `MEDIA_PROVIDER=s3`, `MEDIA_S3_ENDPOINT` (HTTPS para a API Cloudflare R2), `MEDIA_S3_REGION=auto` e a configuração do bucket público do feed. Não adicionar URL público para os anexos dos chats.

O armazenamento privado executa `HeadBucket` autenticado na verificação de disponibilidade, com cache de aproximadamente 30 segundos. Nome ou credenciais em falta, bucket inexistente/inacessível ou endpoint inválido resultam em `available: false`. O backend **não utiliza o bucket público como fallback**.

Depois da configuração, confirmar que o serviço regressou a **LIVE**, sem erros de inicialização nem de migrações Liquibase.

## QA isolada com duas contas

Usar apenas contas e sessões QA, ficheiros inofensivos e sem dados pessoais. Não modificar sessões reais dos utilizadores.

1. **Individual:** com as duas contas amigas, confirmar disponibilidade, enviar ficheiros JPG/GIF/PDF e uma nota de voz; visualizar em ambos os browsers. Confirmar que outra conta/conversa, utilizador bloqueado e acesso anónimo não conseguem descarregar.
2. **Comunidade:** criar ou usar uma comunidade QA com duas contas membros. Enviar e reproduzir anexos, confirmar receção via WebSocket, rejeitar downloads por não membros ou com IDs de outra comunidade e confirmar que retirar um membro revoga acesso imediatamente. Os posts de feed continuam independentes.
3. **Sessão:** criar sessão QA futura com organizador e participante ACTIVE. Confirmar envio/descarga por ambas as contas e rejeição de não participante/expulso. Ao iniciar ou cancelar a sessão, confirmar que o chat deixa de aceitar envio/download e que o browser liberta as pré-visualizações. O backend deve rejeitar upload que termine após kickoff com HTTP 409, removendo o objeto enviado sem gravar mensagem.
4. **Isolamento:** em todos os chats, verificar que o JSON, o WebSocket e o HTML não expõem `attachmentKey`, credenciais, URL pública R2 ou links S3 pré-assinados. Um GET sem autenticação tem de falhar.
5. **Conta/sessão:** repetir depois de alterar o login noutra aba e de alternar rapidamente de conversa: ficheiros da identidade anterior não devem surgir na nova vista.
6. **Cloudflare:** confirmar que o bucket privado **não** tem `r2.dev` nem domínio público associado e que o token só alcança esse bucket.

## Endpoints disponíveis

- Direto: `/api/chat/me/{peer}/attachments`
- Comunidade: `/api/communities/{communityId}/messages/attachments`
- Sessão: `/api/sessions/{sessionId}/messages/attachments`

Cada prefixo expõe `GET /availability`, `POST` multipart e `GET /{messageId}`. A disponibilidade não substitui a autorização no upload e no download. Todos os downloads são autenticados, `Cache-Control: no-store`, `Content-Disposition: attachment`, `X-Content-Type-Options: nosniff`.

## Operação e rollback

- A ausência de R2 privado desativa apenas anexos, **não** o chat de texto nem os vistos de leitura.
- Se houver um problema, não associar domínio público ao bucket nem copiar chaves do feed. Desativar os controlos removendo a configuração privada do backend, com intervenção autorizada; preservar os metadados já gravados.
- Não usar `npm audit fix --force` indiscriminadamente. Os avisos de dependências devem ser revistos separadamente, distinguindo runtime e desenvolvimento.

Este guia documenta o procedimento; **não confirma que o R2 ou os fluxos QA já foram ativados/testados**.
