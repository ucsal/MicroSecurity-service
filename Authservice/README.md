# Authservice

Serviço de autenticação em Spring Boot com:

- cadastro de usuários
- login com senha criptografada
- geração de JWT
- proteção de rotas com validação de token
- banco H2 em memória para desenvolvimento
- integração opcional com Eureka

## Tecnologias

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (`jjwt`)
- Eureka Client

## Estrutura da API

### `POST /auth/register`

Cria um novo usuário.

Exemplo de corpo:

```json
{
  "username": "joao",
  "email": "joao@email.com",
  "password": "123456"
}
```

Resposta:

- `201 Created` com o usuário salvo

### `POST /auth/login`

Autentica o usuário e retorna um token JWT.

Exemplo de corpo:

```json
{
  "username": "joao",
  "password": "123456"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Autorização

Para acessar rotas protegidas, envie o header:

```http
Authorization: Bearer <token>
```

## Configuração

O arquivo principal é `src/main/resources/application.properties`.

Parâmetros importantes:

- `server.port=8081`
- `spring.datasource.url=jdbc:h2:mem:authdb`
- `jwt.secret=...`
- `eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka`

Importante:

- troque `jwt.secret` por uma chave forte em ambiente real
- o H2 está configurado apenas para desenvolvimento

## Como executar

### Com Gradle Wrapper

```bash
./gradlew bootRun
```

No Windows:

```powershell
.\gradlew.bat bootRun
```

### Rodar os testes

```bash
./gradlew test
```

## Console H2

Se o console estiver habilitado, ele pode ser acessado durante o desenvolvimento.
Confira a URL no log da aplicação e use a mesma datasource configurada no `application.properties`.

## Notas de implementação

- As senhas são salvas com `BCryptPasswordEncoder`
- O login é validado pelo `AuthenticationManager`
- O JWT é lido por um filtro que popula o contexto de segurança antes das requisições protegidas
- Usuários novos recebem a role `ROLE_USER`

## Testes

O projeto já contém um teste básico de contexto. Recomenda-se adicionar testes para:

- cadastro com sucesso
- login com credenciais válidas e inválidas
- acesso a rotas protegidas com e sem token

