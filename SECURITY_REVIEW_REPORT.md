# Security Review Report - SignBridge AI

**Data:** 2026-03-13 (Revisao 2)
**Revisao anterior:** 2026-03-11
**Branch:** `update_security`
**Projeto:** SignBridge AI - Plataforma de Traducao de Libras em Tempo Real

---

## 1. Resumo Executivo

Este relatorio e a segunda revisao de seguranca do SignBridge AI. A primeira revisao (2026-03-11) identificou 19 vulnerabilidades. Diversas correcoes criticas ja foram aplicadas. Este documento reflete o estado atual do projeto.

### Painel de Vulnerabilidades

| Severidade | Total Identificadas | Corrigidas | Pendentes |
|------------|:-------------------:|:----------:|:---------:|
| Critica    | 5                   | 5          | 0         |
| Alta       | 6                   | 1          | 5         |
| Media      | 5                   | 4          | 1         |
| Baixa      | 3                   | 3          | 0         |
| **Total**  | **19**              | **13**     | **6**     |

---

## 2. Remediacoes Concluidas

As seguintes vulnerabilidades foram corrigidas desde a revisao anterior:

### 2.1 Criticas (todas corrigidas)

| # | Vulnerabilidade | Correcao Aplicada |
|---|----------------|-------------------|
| 1 | Spring Boot 3.2.0 desatualizado | Atualizado para **4.0.3** no `pom.xml` |
| 2 | Spring Security ausente | Adicionado `spring-boot-starter-security` |
| 3 | Dependencias Python sem versao fixada | Todas as 15+ dependencias fixadas no `requirements.txt` |
| 4 | spring-cloud-starter-openfeign 4.1.0 | Atualizado para **5.0.1** |
| 5 | Porta ai-engine exposta externamente | Removido bloco `ports` do ai-engine no `docker-compose.yml` |

### 2.2 Altas (1 corrigida)

| # | Vulnerabilidade | Correcao Aplicada |
|---|----------------|-------------------|
| 1 | Volume mount expoe codigo fonte | Removido `volumes: ./ai-engine:/app` do `docker-compose.yml` |

### 2.3 Medias (4 corrigidas)

| # | Vulnerabilidade | Correcao Aplicada |
|---|----------------|-------------------|
| 1 | Rede Docker sem isolamento | Criadas redes separadas (`backend-network` internal, `frontend-network`) |
| 2 | Ausencia de Health Checks | Healthchecks adicionados nos 3 servicos |
| 3 | Ausencia de Limites de Recursos | Limites de CPU/memoria definidos para todos os servicos |
| 4 | Perfil dev como padrao | Alterado para `${SPRING_PROFILE:-prod}` |

### 2.4 Baixas (3 corrigidas)

| # | Vulnerabilidade | Correcao Aplicada |
|---|----------------|-------------------|
| 1 | Validacao de entrada ausente | Adicionado `spring-boot-starter-validation` |
| 2 | Actuator ausente | Adicionado `spring-boot-starter-actuator` |
| 3 | OWASP Dependency Check ausente | Plugin `dependency-check-maven` 11.1.1 adicionado |

### 2.5 Hardening Docker Aplicado

- `read_only: true` nos containers ai-engine e frontend-dashboard
- `tmpfs` para diretorios temporarios
- Limite de memoria do ai-engine: 8G com reserva de GPU
- Limite de memoria do backend-api: 2G / 2 CPUs
- Limite de memoria do frontend: 512M / 1 CPU

---

## 3. Vulnerabilidades Pendentes

### 3.1 ALTA - Ausencia de HTTPS/TLS

**OWASP:** A02:2021 - Cryptographic Failures

Todos os servicos comunicam via HTTP. Dados trafegam em texto plano, incluindo potenciais tokens de autenticacao.

**Remediacao:**
- Configurar nginx como reverse proxy com TLS termination
- Utilizar certificados Let's Encrypt em producao
- Redirecionar HTTP para HTTPS
- Ver variaveis `TLS_*` no `.env.example`

### 3.2 ALTA - CORS/CSRF nao implementado

**OWASP:** A05:2021 - Security Misconfiguration / A01:2021 - Broken Access Control

Spring Security foi adicionado, mas CORS e CSRF ainda nao foram configurados com regras restritivas.

**Remediacao:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.csrfTokenRepository(
                CookieCsrfTokenRepository.withHttpOnlyFalse()));
        return http.build();
    }
}
```

### 3.3 ALTA - Rate Limiting ausente

**OWASP:** A04:2021 - Insecure Design

Nenhum mecanismo de rate limiting implementado. API vulneravel a forca bruta e abuso.

**Remediacao:**
- Spring Boot: usar `bucket4j-spring-boot-starter` ou Spring Cloud Gateway
- FastAPI: usar `slowapi` com limites por IP
- Ver variavel `RATE_LIMIT_REQUESTS_PER_MINUTE` no `.env.example`

### 3.4 ALTA - Security Headers ausentes

**OWASP:** A05:2021 - Security Misconfiguration

Headers como CSP, HSTS, X-Frame-Options, X-Content-Type-Options nao estao configurados.

**Remediacao:**
```java
http.headers(headers -> headers
    .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
    .httpStrictTransportSecurity(hsts -> hsts.maxAgeInSeconds(31536000))
    .frameOptions(frame -> frame.deny()));
```

### 3.5 ALTA - JWT/OAuth2 nao implementado

**OWASP:** A07:2021 - Identification and Authentication Failures

Spring Security esta adicionado como dependencia, mas nenhuma estrategia de autenticacao (JWT, OAuth2) foi implementada.

**Remediacao:**
- Implementar JWT com `spring-boot-starter-oauth2-resource-server`
- Ou usar `python-jose` (ja incluido no requirements.txt) para tokens no ai-engine
- Ver variaveis `JWT_*` no `.env.example`

### 3.6 MEDIA - CI/CD sem SAST/DAST

**OWASP:** A06:2021 - Vulnerable and Outdated Components

Pipeline CI/CD nao inclui analise estatica (SAST) nem analise dinamica (DAST) de seguranca.

**Remediacao:** Ver secao 5 (Recomendacoes DAST) para workflows prontos.

---

## 4. Mapeamento OWASP Top 10 (2021)

| OWASP ID | Categoria | Status Atual | Detalhes |
|----------|-----------|:------------:|----------|
| A01 | Broken Access Control | PARCIAL | Spring Security adicionado; CORS/CSRF pendente |
| A02 | Cryptographic Failures | PENDENTE | TLS/HTTPS nao configurado |
| A03 | Injection | PARCIAL | Pydantic (Python) + Validation (Java) adicionados; sanitizacao pendente |
| A04 | Insecure Design | PENDENTE | Rate limiting nao implementado |
| A05 | Security Misconfiguration | PARCIAL | Perfil prod como padrao; headers e CORS pendentes |
| A06 | Vulnerable Components | CORRIGIDO | Dependencias atualizadas e fixadas; OWASP check adicionado |
| A07 | Auth Failures | PENDENTE | JWT/OAuth2 nao implementado |
| A08 | Software/Data Integrity | CORRIGIDO | Versoes fixadas com checksums implicitly pelo Maven/pip |
| A09 | Logging/Monitoring | PARCIAL | Actuator adicionado; audit logging pendente |
| A10 | SSRF | MITIGADO | Rede interna isolada; validacao de URLs pendente no OpenFeign |

---

## 5. Recomendacoes DAST e Analise Dinamica

### 5.1 OWASP ZAP

Ferramenta open-source para analise dinamica de aplicacoes web.

**Baseline Scan (passivo):**
```bash
docker run -t ghcr.io/zaproxy/zaproxy:stable zap-baseline.py \
  -t http://localhost:8080 \
  -r zap-report.html
```

**API Scan (OpenAPI/Swagger):**
```bash
docker run -t ghcr.io/zaproxy/zaproxy:stable zap-api-scan.py \
  -t http://localhost:8080/v3/api-docs \
  -f openapi \
  -r zap-api-report.html
```

**WebSocket Testing:**
```bash
docker run -t ghcr.io/zaproxy/zaproxy:stable zap-full-scan.py \
  -t http://localhost:8080 \
  -r zap-full-report.html \
  --hook=websocket_hook.py
```

### 5.2 Nuclei Scanner

Scanner de vulnerabilidades baseado em templates.

```bash
# Instalar
go install -v github.com/projectdiscovery/nuclei/v3/cmd/nuclei@latest

# Scan com templates de CVE
nuclei -u http://localhost:8080 -t cves/ -severity critical,high

# Scan completo
nuclei -u http://localhost:8080 -t cves,misconfiguration,exposures
```

### 5.3 GitHub Actions - DAST Automatizado

```yaml
name: DAST Security Scan
on:
  push:
    branches: [main]
  schedule:
    - cron: '0 2 * * 1'  # Toda segunda-feira as 2h

jobs:
  zap-scan:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Start application
        run: docker compose up -d --wait

      - name: OWASP ZAP Baseline Scan
        uses: zaproxy/action-baseline@v0.14.0
        with:
          target: 'http://localhost:8080'
          rules_file_name: '.zap/rules.tsv'

      - name: OWASP ZAP API Scan
        uses: zaproxy/action-api-scan@v0.9.0
        with:
          target: 'http://localhost:8080/v3/api-docs'
          format: openapi

      - name: Upload Reports
        uses: actions/upload-artifact@v4
        with:
          name: zap-reports
          path: report_*.html

  nuclei-scan:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Start application
        run: docker compose up -d --wait

      - name: Nuclei Scan
        uses: projectdiscovery/nuclei-action@main
        with:
          target: 'http://localhost:8080'
          flags: '-severity critical,high,medium'

      - name: Upload Results
        uses: actions/upload-artifact@v4
        with:
          name: nuclei-results
          path: nuclei.sarif
```

### 5.4 Checklist de Testes Manuais de Penetracao

| # | Teste | Ferramenta | Comando/Acao |
|---|-------|-----------|--------------|
| 1 | SQL Injection | sqlmap | `sqlmap -u "http://localhost:8080/api/translate?text=test"` |
| 2 | XSS Reflected | ZAP/Burp | Injetar `<script>alert(1)</script>` nos inputs |
| 3 | CORS misconfiguration | curl | `curl -H "Origin: http://evil.com" -I http://localhost:8080/api/` |
| 4 | JWT manipulation | jwt_tool | `python3 jwt_tool.py <token> -T` |
| 5 | WebSocket injection | wscat | `wscat -c ws://localhost:8080/ws/translate` + payloads |
| 6 | Rate limit bypass | wfuzz | `wfuzz -z range,1-200 http://localhost:8080/api/translate` |
| 7 | Directory traversal | curl | `curl http://localhost:8080/../../etc/passwd` |
| 8 | SSRF via OpenFeign | curl | Testar redirecionamento para endpoints internos |

---

## 6. Matriz de Prioridade

| Vulnerabilidade | Impacto | Esforco | Prioridade |
|----------------|:-------:|:-------:|:----------:|
| HTTPS/TLS | Alto | Medio | **P1** |
| JWT/OAuth2 | Alto | Alto | **P1** |
| CORS/CSRF config | Alto | Baixo | **P1** |
| Security Headers | Medio | Baixo | **P2** |
| Rate Limiting | Medio | Medio | **P2** |
| SAST/DAST no CI/CD | Medio | Medio | **P3** |

**Legenda:**
- **P1** - Implementar antes do primeiro deploy
- **P2** - Implementar no proximo sprint
- **P3** - Implementar antes do release de producao

---

## 7. Referencias

- [Spring Boot 4.0.3 Release](https://spring.io/blog/2026/02/19/spring-boot-4-0-3-available-now/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [OWASP Top 10 - 2021](https://owasp.org/Top10/)
- [OWASP ZAP](https://www.zaproxy.org/)
- [Nuclei Scanner](https://github.com/projectdiscovery/nuclei)
- [Docker Security Best Practices](https://docs.docker.com/develop/security-best-practices/)
- [FastAPI Security](https://fastapi.tiangolo.com/tutorial/security/)
- [CIS Docker Benchmark](https://www.cisecurity.org/benchmark/docker)

---

*Relatorio revisado em 2026-03-13 por Claude Code Security Review*
*Revisao anterior: 2026-03-11*
