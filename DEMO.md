# Demo - SignBridge AI

Roteiro de demonstracao do SignBridge AI (~10 minutos).

---

## 1. Visao Geral

Esta demonstracao apresenta as principais funcionalidades do SignBridge AI:

| Cenario | Descricao | Duracao |
|---------|-----------|:-------:|
| 1 | Traducao em tempo real | 3 min |
| 2 | Dicionario 3D interativo | 2 min |
| 3 | Learning Loop | 2 min |
| 4 | Overlay em videoconferencia | 2 min |
| 5 | Verificacao de seguranca | 1 min |

---

## 2. Pre-requisitos

- Docker e Docker Compose instalados
- NVIDIA GPU com drivers atualizados
- Webcam funcional
- Browser moderno (Chrome, Firefox, Edge)
- Terminais abertos para comandos

---

## 3. Iniciando o Ambiente

### 3.1 Subir os servicos

```bash
cd signbridge-ai

# Copiar configuracao (se ainda nao feito)
cp .env.example .env

# Iniciar todos os containers
docker compose up -d

# Aguardar todos os servicos ficarem saudaveis
docker compose ps
```

### 3.2 Verificar health

```bash
# Backend API
curl -s http://localhost:8080/actuator/health | python -m json.tool
# Esperado: {"status": "UP"}

# Verificar que todos os containers estao rodando
docker compose ps --format "table {{.Name}}\t{{.Status}}"
# signbridge-ai-engine    Up (healthy)
# signbridge-backend      Up (healthy)
# signbridge-frontend     Up (healthy)
```

### 3.3 Abrir o frontend

Navegue para `http://localhost:4200` no browser.

---

## 4. Cenario 1: Traducao em Tempo Real

**Objetivo:** Demonstrar a traducao de sinais de Libras capturados pela webcam para texto em portugues.

### Passos:

1. Na tela inicial, clique em **"Iniciar Traducao"**
2. Permita o acesso a webcam quando solicitado
3. Posicione-se em frente a camera com boa iluminacao
4. Realize sinais de Libras:
   - **"Ola"** - acene com a mao
   - **"Obrigado"** - mao no queixo movendo para frente
   - **"Bom dia"** - combinacao de "bom" + "dia"
5. Observe a traducao aparecendo em tempo real na area de texto

### O que demonstrar:

- Latencia baixa (< 200ms)
- Confianca da predicao (indicador visual)
- Historico de traducoes na lateral

---

## 5. Cenario 2: Dicionario 3D Interativo

**Objetivo:** Mostrar o dicionario visual de sinais com representacao 3D.

### Passos:

1. Navegue para a secao **"Dicionario"** no menu
2. Pesquise por uma palavra (ex: "escola")
3. Visualize o avatar 3D realizando o sinal
4. Observe:
   - Rotacao 360 graus do modelo 3D
   - Velocidade ajustavel da animacao
   - Detalhes dos keypoints destacados
5. Clique em **"Praticar"** para ir ao modo Learning Loop

---

## 6. Cenario 3: Learning Loop

**Objetivo:** Demonstrar a avaliacao automatica de gestos do usuario.

### Passos:

1. Selecione um sinal para praticar (ex: "Obrigado")
2. O sistema mostra o sinal de referencia (video ou 3D)
3. Clique em **"Gravar Tentativa"**
4. Realize o sinal em frente a webcam
5. Aguarde a avaliacao automatica:
   - **Pontuacao de similaridade** (0-100%)
   - **Feedback visual** com keypoints comparados
   - **Sugestoes de melhoria** (posicao da mao, velocidade, etc.)
6. Repita para melhorar a pontuacao

---

## 7. Cenario 4: Overlay em Videoconferencia

**Objetivo:** Mostrar a traducao de Libras integrada em chamadas de video.

### Passos:

1. Navegue para **"Videoconferencia"** no menu
2. Inicie uma sessao de video
3. Ative o **overlay de traducao**
4. Demonstre:
   - Legendas em tempo real sobrepostas ao video
   - Posicionamento ajustavel das legendas
   - Tamanho e cor da fonte personalizaveis
5. Mostre o modo **picture-in-picture** com traducao

---

## 8. Verificacao de Seguranca

**Objetivo:** Demonstrar as medidas de seguranca implementadas.

### 8.1 Porta do AI Engine bloqueada

```bash
# Tentar acessar o ai-engine de fora do Docker
curl http://localhost:8000/health
# Esperado: Connection refused (porta nao exposta)

# Verificar que o ai-engine so e acessivel internamente
docker compose exec backend-api curl http://ai-engine:8000/health
# Esperado: {"status": "ok"}
```

### 8.2 Filesystem read-only

```bash
# Tentar escrever no filesystem do ai-engine
docker compose exec ai-engine touch /app/test.txt
# Esperado: Read-only file system

# Tentar escrever no frontend
docker compose exec frontend-dashboard touch /usr/share/nginx/html/test.txt
# Esperado: Read-only file system
```

### 8.3 Limites de recursos

```bash
# Verificar limites de memoria e CPU
docker stats --no-stream --format "table {{.Name}}\t{{.MemUsage}}\t{{.MemPerc}}\t{{.CPUPerc}}"

# Esperado:
# signbridge-ai-engine     ~2G/8G     25%    varies
# signbridge-backend       ~500M/2G   25%    varies
# signbridge-frontend      ~50M/512M  10%    varies
```

### 8.4 Rede isolada

```bash
# Verificar que o frontend NAO pode acessar o ai-engine diretamente
docker compose exec frontend-dashboard wget -qO- http://ai-engine:8000/health
# Esperado: bad address ou connection refused (redes diferentes)
```

### 8.5 Perfil de producao

```bash
# Verificar que o perfil ativo e 'prod'
curl -s http://localhost:8080/actuator/env | grep spring.profiles.active
# Esperado: prod
```

---

## 9. Encerrando a Demo

```bash
# Parar todos os servicos
docker compose down

# Remover volumes (opcional, para limpeza completa)
docker compose down -v

# Verificar que tudo foi parado
docker compose ps
# Esperado: nenhum container listado
```

---

## 10. Proximos Passos

Apos a demonstracao, os proximos passos recomendados sao:

1. **Seguranca** - Implementar as vulnerabilidades pendentes listadas no [SECURITY_REVIEW_REPORT.md](./SECURITY_REVIEW_REPORT.md)
2. **Tutorial** - Seguir o [TUTORIAL.md](./TUTORIAL.md) para configurar o ambiente de desenvolvimento
3. **Contribuicao** - Consultar o [CONTRIBUTING.md](./CONTRIBUTING.md) para guidelines de contribuicao
4. **Roadmap** - Funcionalidades planejadas:
   - Suporte a outros idiomas de sinais (ASL, BSL)
   - Traducao bidirecional (texto para avatar 3D)
   - App mobile (React Native)
   - Integracao com plataformas de EAD (Moodle, Google Classroom)

---

*Para detalhes tecnicos, consulte o [TUTORIAL.md](./TUTORIAL.md).*
*Para questoes de seguranca, consulte o [SECURITY_REVIEW_REPORT.md](./SECURITY_REVIEW_REPORT.md).*
