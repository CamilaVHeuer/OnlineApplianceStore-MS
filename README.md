# 🏪 Online Appliance Store - Microservices Architecture

<div align="center">
  <img src="./Appliances-log.png" alt="Online Appliance Store Logo" width="400"/>
  
  <p><em>E-commerce system developed with microservices architecture</em></p>
</div>

## 📋 Description

E-commerce system for appliance sales developed with **microservices architecture** using Spring Boot and Spring Cloud. This project demonstrates the implementation of distributed design patterns and best practices in microservices development.

> **Note**: Project developed as part of learning microservices architecture, based on TodoCode course, adapted for professional portfolio.

## 🏗️ Microservices Architecture

### Implemented Design Patterns

- **🚪 API Gateway Pattern**: Single entry point for all requests
- **🔍 Service Discovery**: Automatic service registration and discovery with Eureka
- **⚙️ Centralized Configuration**: Centralized configuration with Config Server
- **🔄 Circuit Breaker**: Fault tolerance with Resilience4j
- **🔁 Retry Pattern**: Automatic retries in inter-service calls
- **📋 MVC Pattern**: Model-View-Controller architecture in each microservice
- **📦 DTO Pattern**: Data transfer between layers and services (implemented using Java records for immutability, validation, and cleaner code)

### Architecture Diagram

![Microservices Architecture](Microservices%20Architecture%20–%20Online%20Appliance%20Store.drawio.png)

_Diagram showing interaction between all microservices, implemented patterns and data flow_

### Implemented Services

| Service                   | Port | Swagger UI | Description                              | Database |
| ------------------------- | ---- | ---------- | ---------------------------------------- | -------- |
| **Config Server**         | 8001 | -          | Centralized configuration management     | -        |
| **Eureka Server**         | 8761 | -          | Service registration and discovery       | -        |
| **API Gateway**           | 8000 | [📚 Docs](http://localhost:8000/swagger-ui.html) | Routing, load balancing & unified docs   | -        |
| **Products Service**      | 8083 | [📚 Docs](http://localhost:8083/swagger-ui.html) | Product and inventory management         | H2       |
| **Shopping Cart Service** | 8082 | [📚 Docs](http://localhost:8082/swagger-ui.html) | Shopping cart management                 | H2       |
| **Sales Service**         | 8081 | [📚 Docs](http://localhost:8081/swagger-ui.html) | Sales and transaction processing         | H2       |
| **Auth Service**          | 8085 | [📚 Docs](http://localhost:8085/swagger-ui.html) | User authentication, roles, permissions, JWT security | H2       |

## 🚀 Main Features

### 🔐 Auth Service

- ✅ **User Management**: Register, query and manage users
- ✅ **Role and Permission Management**: Assign roles and granular permissions to users
- ✅ **Spring Security Integration**: Full authentication and authorization using Spring Security
- ✅ **JWT Authentication**: Secure login and token-based authentication for all users
- ✅ **Centralized Authentication** responsible for issuing and validationg JWT
- ✅ **DataInitializer** for automatic creation of base roles and permissions at startup
- ✅ **Stateless Authentication** across all microservices

#### Example Entities

- **UserApp**: Stores user credentials and profile information
- **Role**: Defines user roles (e.g., ADMIN, USER)
- **Permission**: Fine-grained access control for specific actions

All other microservices validate JWT tokens locally using a shared public key and Spring Security filters.

### 📱 Products Service

- ✅ Create, edit and update products
- ✅ Search products by ID
- ✅ Query products with low stock (< 5 units)
- ✅ Update price, stock, name and brand
- ✅ Automatic inventory management
- ✅ **Product status management**:
  - Products have states: `ACTIVE` and `DISCONTINUED`
  - Products cannot be physically deleted
  - To discontinue a product, use the PUT method to change its status to `DISCONTINUED`
  - To reactivate a discontinued product, use the PUT method to change its status back to `ACTIVE`
  - When creating a cart, the service checks the product status; products marked as `DISCONTINUED` cannot be added to the cart

### 🛒 Shopping Cart Service

- ✅ Add products to cart
- ✅ Manage product quantities
- ✅ Query cart contents
- ✅ Remove products from cart *(only if the cart is not marked as SOLD)*
- ✅ Stock validation before adding products
- ✅ **Cart status management**:
  - Each cart has a status (`CREATED`, `SOLD`)
  - When a sale is created, the cart status is set to `SOLD`
  - A cart can only be deleted if its status is not `SOLD`
  - If you try to update a cart with status `SOLD`, the service will return a `409 Conflict` error

### 💰 Sales Service

- ✅ Create new sales
- ✅ Query existing sales
- ✅ Edit sales date and status
- ✅ **Cancel sales** (automatically restores stock)
- ✅ **No physical deletion** of sales (auditability)
- ✅ Integration with Products Service for stock deduction
- ✅ Integration with Shopping Cart Service for sales data
- ✅ Distributed transaction handling

> **Design inspiration from nuclear safety engineering:**
>
> This project incorporates lessons learned from my experience working in a nuclear reactor ☢️. In nuclear systems, critical components are governed by explicit state machines (like the Reactor Protection System) to ensure only valid and safe transitions. Inspired by this, both the Products Service and the Shopping Cart Service enforce strict state management:
>
> - **Products Service:** Products have explicit states (`ACTIVE`, `DISCONTINUED`). Products cannot be physically deleted; instead, they are marked as discontinued using a dedicated endpoint. Only products with `ACTIVE` status can be added to carts, ensuring inventory consistency and preventing invalid operations.
> - **Shopping Cart Service:** Carts have states (`CREATED`, `SOLD`). Once a sale is completed, the cart is marked as `SOLD` and cannot be modified or deleted, guaranteeing transactional integrity and auditability.
>
> This approach ensures data consistency and reliability in distributed systems—just as state control is essential for safety in nuclear engineering, it is also key for robust microservices.



## 🔐 Distributed Security Architecture (JWT + Spring Security)

This project implements a **dual-token security model** using JWT and Spring Security, clearly separating **user authentication** from **service-to-service authorization**.

### Authentication Flow (User Requests)

- A client authenticates against **Auth Service**
- **Auth Service** issues a **User JWT** containing:
    - `username`
    - `roles` / `authorities`
- The client sends the token in each request:
    - `Authorization: Bearer <user-token>`
- Requests enter the system through the **API Gateway**
- Each microservice:
    - Validates the JWT locally using a shared public key
    - Builds the `Authentication` object in the `SecurityContext`
    - Applies endpoint- and method-level authorization (`@PreAuthorize`)

This ensures fully **stateless user authentication** across the platform.

### Inter-Service Communication Security (Service Tokens)

Inter-service communication (e.g. **Sales → Shopping Cart → Products**) does **not** propagate the user token.

Instead, the system uses **dedicated Service JWTs** issued by the **Auth Service**.

#### Service Authentication Flow

- Each business microservice authenticates once against **Auth Service** using service credentials
- **Auth Service** issues a **Service JWT** with:
    - `ROLE_SERVICE`
    - Service-level authorities
- The token is cached in memory by the calling service
- All outgoing Feign requests automatically include:
    - `Authorization: Bearer <service-token>`
- Target services expose **internal endpoints** protected by:
    - `@PreAuthorize("hasRole('SERVICE')")`

This approach ensures:
- Clear separation between **user context** and **system context**
- No dependency on user identity for internal operations
- Strong security boundaries between public and internal APIs

### Authorization Model

The system enforces authorization at multiple levels:

- **Public endpoints**
    - Authentication and registration
- **User-protected endpoints**
    - Require authenticated users
    - Enforce ownership and role checks (ADMIN / USER)
- **Internal service endpoints**
    - Accessible only with `ROLE_SERVICE`
    - No user ownership logic
    - Used exclusively for service-to-service operations

### Benefits

- Clear separation of concerns
- Strong security boundaries
- No token leakage between users and services
- Stateless and scalable architecture
- Production-grade microservices security model

## � API Documentation
### 🌐 Centralized Documentation (Recommended)

**📍 Access all services through API Gateway**: http://localhost:8000/swagger-ui.html

- ✅ **Service dropdown** (Products, Shopping Cart, Sales)
- ✅ **Unified testing** with fault tolerance
- ✅ **Production-ready** routing

> **⚠️ Note:**
> When using the centralized Swagger UI at [http://localhost:8000/swagger-ui.html](http://localhost:8000/swagger-ui.html), make sure to manually select the `http://localhost:8000` server in the "Servers" dropdown for each service. By default, Swagger may select the direct service URL (e.g., `http://localhost:8083`), which will not work for API Gateway requests. Always choose the API Gateway server (`:8000`) to test through the gateway.

### 🔧 Individual Service Documentation

| Service | Direct Swagger URL |
|---------|-------------------|
| **Products Service** | http://localhost:8083/swagger-ui.html |
| **Shopping Cart Service** | http://localhost:8082/swagger-ui.html |
| **Sales Service** | http://localhost:8081/swagger-ui.html |
| **Auth Service** | http://localhost:8085/swagger-ui.html |

### 🎯 Testing Recommendations

- **Development**: Use individual Swagger UIs for rapid testing
- **Integration**: Use Gateway Swagger for complete workflow and Circuit Breaker testing

🔐 For secured endpoints, first authenticate via Auth Service and use the returned JWT as:
Authorization: Bearer token

### 🛡️ Fault Tolerance Testing

Test **Circuit Breaker** through Gateway:
1. Stop a service (e.g., Products Service)
2. Try creating a sale → Observe **503 Service Unavailable**
3. Restart service → Automatic recovery

## � API Documentation
### 🌐 Centralized Documentation (Recommended)

**📍 Access all services through API Gateway**: http://localhost:8000/swagger-ui.html

- ✅ **Service dropdown** (Products, Shopping Cart, Sales)
- ✅ **Unified testing** with fault tolerance
- ✅ **Production-ready** routing

> **⚠️ Note:**
> When using the centralized Swagger UI at [http://localhost:8000/swagger-ui.html](http://localhost:8000/swagger-ui.html), make sure to manually select the `http://localhost:8000` server in the "Servers" dropdown for each service. By default, Swagger may select the direct service URL (e.g., `http://localhost:8083`), which will not work for API Gateway requests. Always choose the API Gateway server (`:8000`) to test through the gateway.

### 🔧 Individual Service Documentation

| Service | Direct Swagger URL |
|---------|-------------------|
| **Products Service** | http://localhost:8083/swagger-ui.html |
| **Shopping Cart Service** | http://localhost:8082/swagger-ui.html |
| **Sales Service** | http://localhost:8081/swagger-ui.html |
| **Auth Service** | http://localhost:8085/swagger-ui.html |

### 🎯 Testing Recommendations

- **Development**: Use individual Swagger UIs for rapid testing
- **Integration**: Use Gateway Swagger for complete workflow and Circuit Breaker testing

🔐 For secured endpoints, first authenticate via Auth Service and use the returned JWT as:
Authorization: Bearer token

### 🛡️ Fault Tolerance Testing

Test **Circuit Breaker** through Gateway:
1. Stop a service (e.g., Products Service)
2. Try creating a sale → Observe **503 Service Unavailable**
3. Restart service → Automatic recovery

## �🛠️ Technologies Used

### Backend

- **Java 17**
- **Spring Boot 3.1.3**
- **Spring Cloud 2022.0.4**
- **Spring Data JPA**
- **H2 Database** (In-memory)
- **OpenFeign** (Inter-service communication)
- **Netflix Eureka** (Service Discovery)
- **Spring Cloud Gateway** (API Gateway)
- **Spring Cloud Config** (Centralized configuration)
- **Resilience4j** (Circuit Breaker and Retry)

### Tools

- **Maven** (Dependency management)
- **Postman** (API testing)
- **Postman Collection** (Included in project - `OnlineApplianceStore.postman_collection.json`)

## � Prerequisites

- Java 17 or higher
- Maven 3.6+
- Recommended IDE: IntelliJ IDEA or Eclipse

## �🚀 Installation and Execution

### 1. Clone the repository

```bash
git clone https://github.com/CamilaVHeuer/OnlineApplianceStore-MS.git
cd OnlineApplianceStore-MS
```

### 2. Service startup order

**⚠️ Important**: Services must be started in the following order:

```bash
# 1. Config Server (Port 8001)
cd config-server
mvn spring-boot:run

# 2. Eureka Server (Port 8761)
cd ../eureka-sv
mvn spring-boot:run

# 3. Business services
cd ../products-service
mvn spring-boot:run

cd ../shopping-cart-service
mvn spring-boot:run

cd ../sales-service
mvn spring-boot:run

# 4. API Gateway (Port 8080)
cd ../api-gateway
mvn spring-boot:run
```

### 3. Verify active services

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8000
- **Config Server**: http://localhost:8001
- **H2 Console** (each service):
  - Products: http://localhost:8083/h2-console
  - Shopping Cart: http://localhost:8082/h2-console
  - Sales: http://localhost:8081/h2-console

### 4. Import Postman collection (Optional)

To facilitate testing, import the included collection:

- File: `OnlineApplianceStore.postman_collection.json`
- Contains all endpoints with request examples

## � Ejecución con Docker

### Construcción y Arranque

```bash
# Option 1: Automatic (single command)
docker-compose up --build

# Option 2: Sequential (more stable)
# 1. Infrastructure first:
docker-compose up config-server eureka-sv

# 2. Wait for complete startup, then microservices:
docker-compose up products-service shopping-cart-service sales-service api-gateway
```

### Docker Ports

| Service          | Host Port |
| ---------------- | --------- |
| Config Server    | 8001      |
| Eureka Server    | 8761      |
| API Gateway      | 8000      |
| Products Service | 8083      |
| Shopping Cart    | 8082      |
| Sales Service    | 8081      |

### Docker Verification

- **Containers**: `docker ps` (6 active services)
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: `curl http://localhost:8001/sales-service/docker`

### Dual Configuration

- **Local**: Uses `application.yml` with `localhost`
- **Docker**: Uses `*-docker.yml` with internal hostnames (`eureka-sv`, `config-server`)

### Useful Commands

```bash
# View specific logs
docker-compose logs sales-service

# Restart with changes
docker-compose down && docker-compose up --build

# Clean project images
docker rmi $(docker images "onlineappliancestore-ms-*" -q)
```

> **💡 Tip**: If there are connection issues, use sequential startup. Microservices need Config Server and Eureka to be completely ready.

## �📡 Endpoints de API

### Products Service (via Gateway: `/products`)

```http
GET    /api/products                    # List all products
GET    /api/products/{id}               # Get product by ID
GET    /api/products/low-stock          # Products with stock < 5
POST   /api/products                    # Create new product (default state: ACTIVE)
PUT    /api/products/{id}               # Update product (name, brand, price, stock)
PUT    /api/products/{id}/discontinue   # Discontinue product (sets status to DISCONTINUED)
PUT    /api/products/{id}/activate    # Reactivate product (sets status to ACTIVE)
DELETE /api/products/{id}               # Logical delete (not physical)
```

### Shopping Cart Service (via Gateway: `/cart`)

```http
GET    /api/cart                       # List all carts
GET    /api/cart/{id}                  # Get cart by ID
POST   /api/cart                       # Create new cart
PUT    /api/cart/{id}                  # Update cart
DELETE /api/cart/{id}                  # Delete cart
```

### Sales Service (via Gateway: `/sales`)

```http
GET    /api/sales                      # List all sales
GET    /api/sales/{id}                 # Get sale by ID
GET    /api/sales/date/{date}          # Get sales by date
POST   /api/sales                      # Create new sale
PUT    /api/sales/{id}                 # Update sale
PUT    /api/sales/cancel/{id}          # Cancel sale (restores stock)
```

### Auth Service (via Gateway: `/auth`)

```http
POST   /api/auth/register               # Register new user
POST   /api/auth/login                  # User login
GET    /api/auth/users                  # List all users
GET    /api/auth/users/{id}             # Get user by ID
PUT    /api/auth/users/{id}             # Update user information
DELETE /api/auth/users/{id}             # Delete user (logical)
```

## �️ Data Models

### Product

```json
{
  "productId": 1,
  "name": "Refrigerator Samsung",
  "brand": "Samsung", 
  "unitPrice": 899.99,
  "stock": 10
}
```

### Shopping Cart

```json
{
  "id": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "unitPrice": 899.99
    }
  ],
  "totalPrice": 1799.98
}
```

### Sale

```json
{
  "id": 1,
  "date": "2026-01-12",
  "cartId": 1,
  "totalAmount": 1799.98,
  "status": "CREATED"
}
```

### UserApp

```json
{
  "id": 1,
  "username": "john_doe",
  "password": "securepassword",
  "email": "john.doe@example.com",
  "roles": ["USER"]
}
```

## �🔄 Business Flow

### Complete Sales Process

1. **Create/Query Products** → Products Service
2. **Add to Cart** → Shopping Cart Service
3. **Process Sale** → Sales Service
   - Query cart and products (Shopping Cart Service)
   - Validate stock availability
   - Deduct stock (Products Service with Circuit Breaker)
   - Register the sale
4. **Cancellation** (if necessary)
   - Change sale status to "CANCELLING"
   - **Automatically restore stock** (Products Service)
   - Update status to "CANCELLED"

### Fault Tolerance

- **Circuit Breaker**: Prevents failure cascades between services
- **Retry Pattern**: Automatic retries in communication
- **Fallback Methods**: Default responses in case of failure
- **Transactional Management**: Consistency in critical operations

### Sales State Management

The system implements state handling similar to **Saga Pattern**:

- **CREATED**: Sale created successfully
- **CANCELLING**: Cancellation process initiated
- **STOCK_RESTORED**: Stock restored after cancellation
- **CANCELLED**: Sale cancelled completely

## 🧪 Testing with Postman

### 📁 Included Test Collection

The project includes a **complete Postman collection** (`OnlineApplianceStore.postman_collection.json`) with all endpoints configured and usage examples.

#### Import the Collection

1. Open Postman
2. Click "Import"
3. Select the file `OnlineApplianceStore.postman_collection.json`
4. The collection will appear with all requests organized by service

### Complete Test Flow

1. **Create Products**

```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Refrigerator",
  "brand": "Samsung",
  "unitPrice": 899.99,
  "stock": 10
}
```

2. **Create Cart with Products**

```http
POST http://localhost:8080/api/cart
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

3. **Process Sale**

```http
POST http://localhost:8080/api/sales
Content-Type: application/json

{
  "date": "2026-01-12",
  "cartId": 1
}
```

4. **Cancel Sale (Optional)**

```http
PUT http://localhost:8080/api/sales/cancel/1
```

5. **User Registration and Login**

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepassword",
  "email": "john.doe@example.com"
}

POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepassword"
}
```

## 📚 Implemented Patterns and Best Practices

### Architectural

- **Database per Service**: Each microservice has its own H2 database
- **API Composition**: Data composition from multiple services
- **Bulkhead Pattern**: Failure isolation between services

### Code

- **Separation of Concerns**: Clear separation of responsibilities
- **Dependency Injection**: Use of Spring IoC
- **Exception Handling**: Centralized exception handling
- **Data Transfer Objects**: DTOs for data transfer between layers and services (using Java records)

---

### 🚀 Recent Improvements & Best Practices

- **Bean Validation in DTOs**: Use of `@NotNull`, `@Positive`, etc. directly in records for automatic request validation and less service-layer logic.
- **Global Exception Handler**: Centralized error handling for consistent API responses and cleaner controllers/services.
- **Migration to Java Records**: All DTOs refactored to records, improving immutability, expressiveness, and reducing boilerplate.
- **Operation-Specific Records**: Clear separation of request, response, and update records for each use case.
- **Helper Methods**: Encapsulation of repetitive logic in helpers for better readability and maintainability.
- **Improved Entity-DTO Mapping**: More organized and maintainable mapping between entities and DTOs.
- **Distributed JWT Security**: Authentication centralized in Auth Service, validation decentralized in each microservice.
- **Feign Security Interceptor**: Automatic propagation of Authorization header between microservices.
- **Method-Level Authorization**: Use of @PreAuthorize for role-based and authentication-based access.
- **Stateless Architecture**: No server-side sessions.

These improvements result in a more robust, maintainable, and professional codebase aligned with modern Java and Spring Boot standards.

## Author

**Camila V. Heuer**

- 📧 Email: cbvillalbaheuer@gmail.com
- GitHub: [@CamilaVHeuer](https://github.com/CamilaVHeuer)
- LinkedIn: [Camila Heuer](https://linkedin.com/in/camilavheuer)

_Developed with 💙 using Spring Boot and Spring Cloud_
