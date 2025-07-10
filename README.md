# Hotel Bao - Sistema de Autenticação

Este é um projeto Spring Boot com autenticação JWT, cadastro de usuários com roles, gerenciamento de quartos (Room) e estadias (Stay).

## Tecnologias Utilizadas

- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **JWT (JSON Web Tokens)**
- **Maven**

## Funcionalidades

- ✅ Sistema de login e cadastro de usuários
- ✅ Autenticação JWT
- ✅ Controle de acesso baseado em roles (ADMIN, USER)
- ✅ Cadastro e listagem de quartos (Room)
- ✅ Apenas ADMIN pode cadastrar quartos

## Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- MySQL rodando e banco hotelbao existente

### Configurando o Banco de Dados

1. Certifique-se de que o MySQL está rodando e o banco hotelbao existe:
   ```sql
   CREATE DATABASE hotelbao;
   ```
2. Configure o usuário e senha do banco no arquivo `src/main/resources/application.properties` (padrão: root/root).

### Executando o Projeto

1. Clone o repositório
2. Navegue até o diretório do projeto
3. Execute o comando:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## Endpoints da API

### Usuários

#### Cadastro de Usuário
```
POST /api/auth/signup
Content-Type: application/json

{
  "name": "João da Silva",
  "email": "joao@email.com",
  "password": "123456",
  "username": "joaosilva",
  "phone": "31999999999",
  "roles": [
    { "name": "ROLE_USER" }
  ]
}
```

#### Login
```
POST /api/auth/signin
Content-Type: application/json

{
  "username": "joaosilva",
  "password": "123456"
}
```

#### Listar todos os usuários
```
GET /api/auth/users
```
Retorna:
```json
[
  {
    "name": "João da Silva",
    "email": "joao@email.com",
    "username": "joaosilva",
    "phone": "31999999999",
    "roleName": "ROLE_USER"
  }
]
```

### Quartos (Room)

#### Listar todos os quartos
```
GET /api/rooms
```

#### Buscar quarto por id
```
GET /api/rooms/{id}
```

#### Cadastrar quarto (apenas ADMIN)
```
POST /api/rooms
Authorization: Bearer {token_do_admin}
Content-Type: application/json

{
  "descricao": "Quarto Duplo com Vista para o Mar",
  "valor": 350.0,
  "urlImagem": "https://exemplo.com/imagens/quarto1.jpg"
}
```

#### Deletar quarto
```
DELETE /api/rooms/{id}
```

### Estadia (Stay)

#### Listar todas as estadias
```
GET /api/stays
```

#### Cadastrar estadia
```
POST /api/stays
Content-Type: application/json
{
  "userId": 1,
  "roomId": 2,
  "checkIn": "2025-05-22"
}
```
- O campo `checkOut` é calculado automaticamente para 1 dia após o `checkIn`.
- O campo `userId` é o id do cliente (usuário).
- O campo `roomId` é o id do quarto.

## Observações

- Para cadastrar um quarto, o usuário deve estar autenticado como ADMIN (enviar o token JWT no header Authorization).
- O projeto está pronto para uso com MySQL, mas pode ser facilmente adaptado para outros bancos.

## Segurança

- Senhas são criptografadas com BCrypt
- Tokens JWT com expiração
- Controle de acesso baseado em roles

---
Se precisar de mais exemplos ou quiser adicionar novas funcionalidades, só avisar!

## Banco de Dados

O projeto utiliza H2 Database em memória. Para acessar o console H2:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:hoteldb`
- Username: `sa`
- Password: `password`

## Estrutura do Projeto

```
src/main/java/com/hotelbao/
├── HotelBaoApplication.java
├── config/
│   └── DataInitializer.java
├── controller/
│   ├── AuthController.java
│   └── TestController.java
├── dto/
│   ├── JwtResponse.java
│   ├── LoginRequest.java
│   ├── MessageResponse.java
│   └── SignupRequest.java
├── entity/
│   ├── Role.java
│   └── User.java
├── repository/
│   ├── RoleRepository.java
│   └── UserRepository.java
└── security/
    ├── WebSecurityConfig.java
    ├── jwt/
    │   ├── AuthEntryPointJwt.java
    │   ├── AuthTokenFilter.java
    │   └── JwtUtils.java
    └── services/
        ├── UserDetailsImpl.java
        └── UserDetailsServiceImpl.java
```

## Exemplo de Uso

### 1. Cadastrar um usuário admin

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@hotel.com",
    "password": "123456",
    "fullName": "Administrador",
    "roles": ["admin"]
  }'
```

### 2. Fazer login

```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

### 3. Usar o token retornado para acessar recursos protegidos

```bash
curl -X GET http://localhost:8080/api/test/admin \
  -H "Authorization: Bearer {token_aqui}"
```

## Configurações

As configurações principais estão em `src/main/resources/application.properties`:

- **Porta**: 8080
- **Banco**: H2 em memória
- **JWT Secret**: Configurado no properties
- **JWT Expiration**: 24 horas

## Segurança

- Senhas são criptografadas com BCrypt
- Tokens JWT com expiração
- Validação de dados com Bean Validation
- Controle de acesso baseado em roles
- CORS configurado para desenvolvimento 