# 🤝 Guia de Contribuição: SignBridge AI (Dicionário de Libras)

Obrigado pelo seu interesse em contribuir para o **SignBridge AI**! Sua ajuda é fundamental para expandir o dicionário de sinais da plataforma e tornar a educação mais inclusiva para todos.

---

## 🛠️ Como Contribuir para o Dicionário de Sinais

A expansão do dicionário consiste na coleta de novos dados de keypoints (coordenadas X, Y, Z) para o treinamento do nosso modelo de Deep Learning.

### 1. Preparação do Ambiente de Captura

Você precisará da nossa ferramenta interna de captura (`SignCollector`):

```bash
# Clone o repositório e instale as dependências da AI Engine
pip install -r signbridge-ai/ai-engine/requirements.txt
```

### 2. Fluxo de Gravação de Novos Sinais

1. **Escolha um Sinal:** Consulte o arquivo `needs_data.csv` no diretório de dados para ver quais sinais precisamos coletar.
2. **Execute o Capturador:**
   ```bash
   python signbridge-ai/ai-engine/collect_data.py --sign "NOME_DO_SINAL"
   ```
3. **Padrão de Execução:**
   - Realize o sinal em frente à webcam por **10 a 20 repetições**.
   - Mantenha uma distância de ~1.5m da câmera.
   - Certifique-se de que o rosto e as mãos estejam bem iluminados.
   - Varie levemente o ângulo e a velocidade entre as repetições.

### 3. Estrutura dos Dados Coletados

O `SignCollector` gera arquivos `.npy` contendo sequências de frames com keypoints do MediaPipe:
- `[seq_length, 1662]` (coordenadas normalizadas de mãos, rosto e corpo).

### 4. Submissão (Pull Request)

- Crie uma branch para o novo sinal: `feat/add-sign-NAME`.
- Adicione os arquivos `.npy` na pasta `signbridge-ai/ai-engine/data/raw_keypoints/NAME/`.
- Atualize o arquivo `sign_labels.json` com o novo identificador.
- Abra um **Pull Request** descrevendo o sinal adicionado e anexando um GIF curto da execução (para validação visual dos gestos).

---

## 💻 Contribuição em Código

Se você é um desenvolvedor e quer melhorar o sistema:

1. **Bug Fixes:** Abra uma Issue descrevendo o erro e vincule ao seu PR.
2. **Melhorias na IA:** Sugestões de novos modelos (Transformers, Graph Neural Networks) são bem-vindas.
3. **Frontend:** Melhorias na UX do dashboard de inclusão.

### Diretrizes de Código:
- Siga as convenções de código do projeto (Python PEP8, Java Google Style).
- Adicione testes unitários para novas funcionalidades.
- Garanta que a documentação foi atualizada.

---

## 🌍 Código de Conduta

Este projeto preza pelo respeito e inclusão. Ao participar, você concorda em manter um ambiente acolhedor e livre de assédio para todos os colaboradores.

---

*Juntos, estamos construindo a ponte para uma educação sem barreiras!* 🤟
