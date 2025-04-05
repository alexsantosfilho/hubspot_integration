# HubSpot Integration API

Esta é uma API Spring Boot para integração com a plataforma HubSpot, oferecendo funcionalidades de criação de contatos, autenticação OAuth 2.0, processamento de webhooks e documentação interativa via Swagger.

## 📋 Pré-requisitos

- Java 21+
- Maven 3.6+
- Conta de desenvolvedor no HubSpot
- Aplicativo registrado no HubSpot Developer Portal

## 🚀 Configuração e Execução

### 1. Configuração do Ambiente

Crie um arquivo `application.properties` ou `application.yml` na pasta `src/main/resources` com as seguintes configurações:

```properties
# Configurações do HubSpot
hubspot.client.id=<seu-client-id>
hubspot.client.secret=<seu-client-secret>
hubspot.redirect.uri=http://localhost:8080/oauth/callback
hubspot.scopes=contacts content
hubspot.auth.url=https://app.hubspot.com/oauth/authorize
hubspot.token.url=https://api.hubapi.com/oauth/v1/token
hubspot.response_type=code

# Configurações da API
hubspot.api.base-url=https://api.hubapi.com
hubspot.api.contacts-endpoint=/crm/v3/objects/contacts
hubspot.api.rate-limit=100
hubspot.api.rate-limit-interval=10000
```

### 2. Build do Projeto

```bash
mvn clean install
```

### 3. Execução da Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## ▶️ Passos para Testar a Aplicação

1. **Configure suas chaves do HubSpot**

   No arquivo `src/main/resources/application.properties`, adicione:

   ```properties
   hubspot.client.id=SUA_CLIENT_ID
   hubspot.client.secret=SEU_CLIENT_SECRET
   ```

2. **Compile e execute o projeto**

   ```bash
   mvn clean compile
   mvn spring-boot:run
   ```

3. **Autenticação no HubSpot**

   - Acesse no navegador: [http://localhost:8080/oauth/authorize](http://localhost:8080/oauth/authorize)
     
     ![image](https://github.com/user-attachments/assets/1b1f9126-8bf5-450d-b5e8-252805461aa3)

   - Você será redirecionado para o HubSpot, onde deve selecionar sua conta de desenvolvedor e autorizar a aplicação.
     ![image](https://github.com/user-attachments/assets/a2e6e76b-a5ec-49fe-8e47-31652c17313f)

   - Após autorizar, será feito o callback com o **access token** necessário para as requisições autenticadas.
     ![image](https://github.com/user-attachments/assets/d33e08cd-ef92-4dad-8928-fb679ac35e89)


4. **Testar criação de contato via Postman**

   - Crie uma requisição **POST** para a URL:  
     `http://localhost:8080/api/contacts`

   - Corpo da requisição (raw JSON):

     ```json
     {
       "properties": {
         "email": "alex@gmail.com",
         "firstname": "alex",
         "lastname": "filho",
         "website": "http://www.example.com",
         "company": "Example Company",
         "phone": "555-555-488484"
       }
     }
     ```

   - Vá na aba **Authorization**, selecione o tipo **Bearer Token**, e cole o token obtido no passo anterior.

   - Envie a requisição. Se tudo estiver correto, o contato será criado com sucesso no HubSpot.

## 📑 Documentação da API com Swagger

Acesse a documentação interativa em:

👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
![image](https://github.com/user-attachments/assets/d4337916-fdee-4831-bda1-974819c7d004)


## 🔧 Endpoints da API

### Autenticação OAuth 2.0

- **GET /oauth/authorize**: Inicia o fluxo OAuth  
- **GET /oauth/callback**: Callback para receber o código de autorização

### Gerenciamento de Contatos

- **POST /api/contacts**: Cria um novo contato no HubSpot  
  - Requer header `Authorization: Bearer <access_token>`  
  - Corpo da requisição deve seguir o formato `ContactRequestDTO`

### Webhooks

- **POST /api/webhooks/contact-creation**: Endpoint para receber webhooks do HubSpot sobre criação de contatos  
  - Valida assinatura usando `X-HubSpot-Signature`

## 📚 Documentação Técnica

### Diagrama do Projeto
![diagram](https://github.com/user-attachments/assets/18c7b9cd-ad89-4eae-9c22-2727c4120682)


### Decisões de Projeto

1. **Arquitetura**:
   - Arquitetura em camadas (controller, service, repository)
   - Uso de DTOs para transferência de dados
   - Tratamento centralizado de exceções

2. **Bibliotecas**:
   - **Lombok**: Redução de código repetitivo
   - **Spring Validation**: Validação de dados de entrada
   - **Spring Web**: API REST
   - **RestTemplate**: Comunicação com a API HubSpot
   - **Springdoc OpenAPI / Swagger**: Documentação da API

3. **Segurança**:
   - Fluxo OAuth 2.0 completo
   - Validação de assinatura de webhooks
   - Rate limiting para chamadas externas

4. **Resiliência**:
   - Tratamento robusto de erros
   - Logging detalhado
   - Rate limiting customizado

### Melhorias Futuras

1. **Cache**:
   - Tokens OAuth
   - Dados frequentemente utilizados

2. **Monitoramento**:
   - Health checks e métricas
   - Integração com APM com DataDog

3. **Testes**:
   - Aumentar testes unitários
   - Incluir testes de integração

4. **Funcionalidades**:
   - Atualização de contatos
   - Filtros e busca de contatos
   - Paginação nas listagens

5. **Segurança Avançada**:
   - Suporte a refresh token
   - Validações adicionais
  
6. Exceptions
   - Ajustar tratamento global e separar por classes

## 🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests.

---
