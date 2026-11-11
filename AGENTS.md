# AGENTS.md

Este arquivo fornece instruções e contexto para agentes de IA que trabalham no projeto **SignBridge AI**.

## Visão Geral do Projeto
Plataforma de acessibilidade para tradução de Libras em tempo real e alfabetização visual, convertendo sinais capturados por webcam em texto e voz.

## Tecnologias Utilizadas
- **Backend:** Spring Boot (Java)
- **AI Engine:** Python/FastAPI, MediaPipe, LSTM/Transformers.
- **Comunicação:** WebSockets para tempo real.
- **UX:** Foco em Educação Inclusiva.

## Estrutura de Pastas
- `backend-api/`: API em Spring Boot.
- `ai-engine/`: Motor de IA em Python.

## Comandos Principais
- Build/Instalação: `pip install -r requirements.txt` (no ai-engine)
- Execução AI Engine: `uvicorn main:app --port 8003`
- Execução Backend: `./mvnw spring-boot:run`
- Docker: `docker-compose up --build`

## Diretrizes de Desenvolvimento
- **Privacidade:** Processe apenas coordenadas (keypoints), nunca armazene imagens de vídeo.
- **Tempo Real:** Priorize baixa latência via WebSockets.
- **Multimodalidade:** Analise simultaneamente mãos, rosto e postura corporal.
- **Segurança:** Proteção contra Cross-Site WebSocket Hijacking (CSWSH).
