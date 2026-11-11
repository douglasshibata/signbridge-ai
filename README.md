# 🤟 SignBridge AI: Tradutor de Libras em Tempo Real e Alfabetização Visual

O **SignBridge AI** é uma plataforma de acessibilidade projetada para eliminar barreiras de comunicação entre alunos surdos e ouvintes no ambiente educacional. Utilizando Visão Computacional avançada e Processamento de Sequências Temporais, o sistema atua como um intérprete digital onipresente.

## 📋 Tabela de Conteúdos
- [Funcionalidades](#funcionalidades-principais)
- [Arquitetura Técnica](#arquitetura-técnica)
- [Tecnologias](#tecnologias)
- [Instalação](#instalação)
- [Segurança](#segurança)
- [Licença](#licença)

## 🚀 Funcionalidades Principais
- **Tradução em Tempo Real:** Converte sinais de Libras capturados pela webcam em texto e voz instantaneamente.
- **Dicionário 3D Interativo:** Permite que professores consultem a execução correta de sinais através de avatares ou vídeos indexados.
- **Learning Loop (Avaliação de Gestos):** Propõe desafios práticos onde o aluno replica sinais e recebe feedback imediato.
- **Interface Flutuante (Overlay):** Pode ser utilizada sobre ferramentas de videoconferência (Google Meet/Zoom).

## 🛠️ Arquitetura Técnica
- **Backend (Spring Boot):** Gerencia o fluxo de dados e orquestra a comunicação via WebSockets.
- **AI Engine (Python/GPU):** Utiliza LSTM ou Transformers Temporais para predição de frases complexas.
- **Análise Multimodal:** Processa simultaneamente mãos, face e postura via MediaPipe.

## Tecnologias
- **Frontend**: Angular 19, Tailwind CSS.
- **Backend**: Java 21, Spring Boot 4.0.6 (Spring AI).
- **AI Engine**: Python 3.12, PyTorch 2.6.0, MediaPipe.
- **Infra**: Docker, WebSockets.

## Instalação

### AI Engine (Python)
```bash
cd ai-engine
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python main.py
```

### Backend (Java)
```bash
cd backend
mvn spring-boot:run
```

## Segurança
- **Privacidade por Design**: O sistema processa apenas coordenadas (keypoints), nunca armazenando imagens de vídeo brutas.
- **Comunicação Segura**: WebSockets protegidos e validação de tokens JWT.
- **PHI/PII**: Anonimização automática de dados de alunos em relatórios pedagógicos.

## Licença
Licença Educacional / Open Source

---

## 🛠️ Project Deep Dive & Technical Guide

### 🎯 Main Purpose
Real-time Gesture-to-Speech translation platform using computer vision and multimodal transformers.

### 🧰 Tecnologias Utilizadas (Tech Stack)
- **Primary Technologies:** Docker / Docker Compose, FastAPI, Java 21, Maven, Python 3.12, Spring Boot 3.4.3
- **Deployment Strategy:** Container-ready with standard Docker/Docker Compose integrations where applicable.

### 🚀 Como Implantar e Executar (How to Deploy & Run)
#### Backend (Java / Spring Boot)
1. Navigate to the backend directory.
2. Compile and run: `./mvnw spring-boot:run` or `mvn spring-boot:run`.

#### Python Engine / FastAPI
1. Install dependencies: `pip install -r requirements.txt`.
2. Run the server/script: `uvicorn main:app --reload` or `python main.py`.

---
