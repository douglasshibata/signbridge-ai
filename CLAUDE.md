# SignBridge AI - CLAUDE.md

Guia de comandos e diretrizes para o SignBridge AI.

## Comandos de Build e Execução

### Docker Compose
```bash
docker-compose up --build
```

### Backend (Spring Boot)
```bash
cd signbridge-ai/backend-api
./mvnw spring-boot:run
```

### AI Engine (Python/FastAPI)
```bash
cd signbridge-ai/ai-engine
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
uvicorn main:app --port 8003
```

## Diretrizes de Desenvolvimento

- **IA:** Utiliza MediaPipe para extração de keypoints e LSTM/Transformers para tradução temporal.
- **Tempo Real:** Comunicação via WebSockets para atualização do dashboard e overlays.
- **Privacidade:** O sistema processa apenas coordenadas (keypoints), não armazenando imagens de vídeo dos usuários.
- **Multimodal:** Analisa simultaneamente mãos, rosto e postura corporal.
- **UX:** Foco em acessibilidade digital e educação inclusiva.
