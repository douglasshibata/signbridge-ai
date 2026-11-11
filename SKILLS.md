# SKILLS.md - SignBridge AI

Diretrizes para evolução do tradutor de Libras em tempo real.

## 🛡️ Auditoria de Segurança

1. **WebSocket Security:** Garanta que as conexões WebSocket sejam autenticadas e protegidas contra ataques de Cross-Site WebSocket Hijacking (CSWSH).
2. **Data Privacy:** Valide que o fluxo de vídeo da webcam é processado apenas localmente ou em trânsito seguro, e que apenas os keypoints anônimos chegam ao motor de IA.
3. **Prompt Injection (Tradução):** Se a saída do tradutor for alimentada em um LLM para refinamento gramatical, proteja contra manipulação da saída.

## 📈 Melhorias Sugeridas

1. **Inteligência:**
   - Adicionar suporte a regionalismos de Libras (variações de sinais por estado).
   - Implementar tradução inversa: Texto/Voz -> Avatar 3D realizando o sinal.
2. **Funcionalidades:**
   - Criar uma extensão de navegador para injetar o overlay de tradução diretamente no Google Meet.
   - Adicionar modo offline usando TensorFlow.js para tradução direta no navegador.
3. **UX de Aprendizado:**
   - Gamificar o "Learning Loop" com níveis de dificuldade e conquistas.

## 🤖 Papel do Claude

- **Vision Engineer:** Ajude a otimizar a extração de landmarks do MediaPipe para rodar em dispositivos móveis.
- **Linguista de Sinais:** Colabore na definição da estrutura temporal das sequências LSTM para capturar a gramática da Libras.
- **Frontend Dev:** Desenvolva o componente de Overlay flutuante com suporte a transparência e drag-and-drop.
