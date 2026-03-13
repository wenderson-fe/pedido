# Pedidos API
API responsável pelo gerenciamento de pedidos no ecossistema Food. Desenvolvida com **Spring Boot 3.4** e integrada a uma arquitetura de microsserviços.

## Funcionalidades
- **Ciclo de Vida:** Criação, consulta, atualização e cancelamento de pedidos.
- **Pagamentos:** Endpoint dedicado para confirmação de pagamento.
- **Status:** Endpoint para alteração de status para atualização do progresso do pedido.
- **Service Discovery:** Registro automático no **Netflix Eureka**.

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- MySQL
- Flyway
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign
- Resilience4j
- Bean Validation
- Lombok
- SpringDoc OpenApi (Swagger)
- Maven
- Docker

## Documentação da API
Com a aplicação rodando, acesse a interface do Swagger para testar os endpoints:  
http://localhost:8080/swagger-ui/index.html

## Como executar a aplicação
Clone o repositório com os submódulos:
````text
git clone --recursive https://github.com/wenderson-fe/ms-orchestrator
````
Suba tudo com um comando:
````text
docker-compose up --build
````

Para verificar as instâncias cadastradas no Service Discovery acesse:  
http://localhost:8081