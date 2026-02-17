# Relatório de Análise e Melhorias – Ticket System API

Este documento consolida a análise do projeto **ticket-system-api** (Spring Boot 2.7, Java 18) e as melhorias recomendadas, além das alterações já aplicadas.

---

## Correções já aplicadas

1. **`AbstractServiceImpl.getPageable()`**  
   - **Problema:** Com `orderBy` vazio (valor padrão dos controllers), o método lançava exceção e a listagem quebrava.  
   - **Correção:** `orderBy` passou a ser opcional; quando vazio, é retornado `PageRequest.of(page, pageSize)` sem ordenação.

2. **Mensagem de erro em `TicketServiceImpl`**  
   - "Ticket not founded." → "Ticket not found."

3. **Títulos incorretos no `RestExceptionHandler`**  
   - BadRequestException: "Resource not found." → "Bad request."  
   - PropertyReferenceException: "Resource not found." → "Invalid sort parameter."

4. **`TicketController`**  
   - Parâmetro do POST: `TicketDTO TicketDTO` → `@RequestBody TicketDTO ticketDTO`.  
   - Tipos de retorno genéricos: `ResponseEntity` → `ResponseEntity<Object>` e adicionado `@RequestBody` no PUT.

5. **`application.yml`**  
   - Typo: `secrect` → `secret` (devtools).

---

## Segurança (crítico)

### 1. Credenciais em repositório

- **Onde:**  
  - `src/main/resources/application.yml`: `password: Bia8ha77%`  
  - `docker-compose.yaml`: `MYSQL_PASSWORD` e `MYSQL_ROOT_PASSWORD`  
  - Comentário no `pom.xml` (Flyway) com a mesma senha  

- **Recomendação:**  
  - Usar variáveis de ambiente (ex.: `SPRING_DATASOURCE_PASSWORD`, `MYSQL_ROOT_PASSWORD`) e nunca commitar senhas.  
  - Exemplo no `application.yml`:
    ```yaml
    spring:
      datasource:
        password: ${SPRING_DATASOURCE_PASSWORD:}
    ```
  - Rotacionar a senha que já foi exposta no Git.

### 2. API sem autenticação/autorização

- Não há Spring Security (nem dependência). Todos os endpoints estão abertos.  
- **Recomendação:**  
  - Adicionar `spring-boot-starter-security`.  
  - Definir autenticação (ex.: JWT ou sessão) e autorização por recurso/role (ex.: `@PreAuthorize`).

### 3. Uso de SSL no banco

- `useSSL=FALSE` na URL do MySQL reduz segurança em produção.  
- **Recomendação:** Em produção, usar SSL e configurar a URL (e certificados, se necessário) via configuração/ambiente.

---

## Configuração e ambiente

### 4. `docker-compose.yaml`

- Volume com caminho absoluto de outra máquina:
  ```yaml
  - /D:/workspaces/ccb-ticket-system/src/main/resources/scripts/V1_1__insert.sql
  ```
- **Recomendação:** Usar caminho relativo ao projeto, por exemplo:
  ```yaml
  - ./src/main/resources/scripts/V1_1__insert.sql:/data/application/V1_1__insert.sql
  ```
  (ou o caminho que fizer sentido no container.)

### 5. Flyway apenas em teste

- No `pom.xml`, `flyway-core` está com `<scope>test</scope>`. Em produção o schema é controlado por `ddl-auto: update`.  
- **Recomendação:**  
  - Incluir Flyway em runtime também e usar migrações versionadas.  
  - Em produção, usar `ddl-auto: validate` (ou `none`) e não gerar schema via Hibernate.

### 6. Hibernate `ddl-auto: update` em produção

- `ddl-auto: update` pode alterar o schema em produção de forma imprevisível.  
- **Recomendação:** Em produção, usar `ddl-auto: validate` (ou `none`) e evoluir o banco apenas via Flyway (ou scripts controlados).

---

## Dependências e build

### 7. Repositório JCenter

- JCenter está descontinuado.  
- **Recomendação:** Remover o bloco `<repositories>` com JCenter; o Maven Central é suficiente para as dependências atuais.

### 8. Versões e dependências

- **Spring Boot 2.7.3:** Há versões 2.7.x mais recentes (e 3.x LTS). Planejar upgrade para correções de segurança.  
- **mockito-all:** Está deprecado; o projeto já usa `spring-boot-starter-test`, que traz o Mockito moderno.  
- **Recomendação:** Remover `mockito-all` e usar o Mockito do starter; alinhar versões do Spring e do Java conforme política do projeto.

### 9. Flyway no `pom.xml`

- Plugin Flyway comentado contém senha em texto.  
- **Recomendação:** Se for reativar o plugin, usar apenas variáveis de ambiente (ou perfil Maven) e nunca commitar senhas.

---

## Código e modelo de dados

### 10. Cascade em entidades (`Ticket`)

- Em `Ticket`, os `@ManyToOne` (assignedTo, department, category) estão com `CascadeType.REMOVE`.  
- **Risco:** Remover um ticket pode propagar a remoção para User, Department ou Category.  
- **Recomendação:** Remover `CascadeType.REMOVE` desses `@ManyToOne` (manter apenas o que fizer sentido, por exemplo PERSIST/MERGE).

### 11. `RestExceptionHandlerImpl`

- Classe vazia que só estende `RestExceptionHandler`.  
- **Recomendação:** Remover `RestExceptionHandlerImpl` e usar apenas `RestExceptionHandler`, ou dar responsabilidade real à impl (por exemplo, handlers adicionais).

### 12. Validação de atualização de ticket

- No `update` do ticket, o código não verifica se o recurso existe antes de fazer `save` (apenas converte o ID).  
- **Recomendação:** Garantir que o ID exista (por exemplo, usando `checkAndConvertID` ou `findById` e lançando `NotFoundException` se não existir) antes de atualizar.

### 13. `ValidationHelper.requireNonNull` / `requireNonBlank`

- Retornam `false` quando o valor é válido, o que é confuso.  
- **Recomendação:** Alterar para `void` ou retornar `true` em caso de sucesso, para não sugerir que “false = sucesso”.

### 14. Logging e pacote

- `logging.level.com.numericaideas` no `application.yml` não corresponde ao pacote do projeto (`be.congregationchretienne.ticketsystem`).  
- **Recomendação:** Ajustar para o pacote da aplicação, por exemplo:
  ```yaml
  logging:
    level:
      be.congregationchretienne: DEBUG
  ```

---

## API e documentação

### 15. README

- O README é o template genérico do GitLab e não descreve o projeto (como rodar, endpoints, variáveis de ambiente).  
- **Recomendação:** Reescrever com: descrição do sistema, pré-requisitos, como rodar (Maven, Docker), variáveis de ambiente e link para a documentação da API (Swagger/OpenAPI).

### 16. OpenAPI / Swagger

- SpringDoc está presente; trecho de configuração está comentado no `application.yml`.  
- **Recomendação:** Descomentar e ajustar `springdoc.api-docs.path` e, se houver, a URL da UI (ex.: `/swagger-ui.html`), e documentar autenticação quando for adicionada.

---

## Testes e CI

### 17. Testes de integração e `orderBy` nulo

- Em alguns ITs, `orderBy` é passado como `null`. Com a correção em `getPageable`, listagens sem ordenação passam a funcionar; vale garantir que os ITs cubram tanto `orderBy` vazio quanto com valor.

### 18. Cobertura e qualidade

- JaCoCo está configurado.  
- **Recomendação:** Definir meta de cobertura mínima e falhar o build se não for atingida (ex.: `maven-jacoco-plugin` com `check` goal).

---

## Resumo prioritário

| Prioridade | Item |
|-----------|------|
| Crítica   | Remover senhas do código e do repositório; usar variáveis de ambiente. |
| Crítica   | Introduzir autenticação e autorização (ex.: Spring Security + JWT). |
| Alta      | Ajustar cascade em `Ticket` (remover REMOVE dos ManyToOne). |
| Alta      | Usar Flyway em produção e `ddl-auto: validate`/`none` em produção. |
| Média     | Remover JCenter; atualizar/remover dependências obsoletas (mockito-all, etc.). |
| Média     | Corrigir caminho do volume no `docker-compose` e melhorar README e documentação da API. |
| Baixa     | Limpar `RestExceptionHandlerImpl`, melhorar `ValidationHelper` e logging. |

Se quiser, posso detalhar ou implementar algum desses itens (por exemplo: exemplo de `application.yml` com env vars, configuração mínima de segurança ou ajuste do Flyway no `pom.xml`).
