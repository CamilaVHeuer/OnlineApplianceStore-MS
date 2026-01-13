# 🏪 Online Appliance Store - Microservices Architecture

<div align="center">
  <img src="Appliances-log.png" alt="Online Appliance Store" width="## 🐳 Docker Execution

### Build and Startup"/>
  
  <## 📡 API Endpoints><em>E-commerce system developed with microservices architecture</em></p>
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
- **📦 DTO Pattern**: Data transfer between layers and services

### Architecture Diagram

![Microservices Architecture](Microservices%20Architecture%20–%20Online%20Appliance%20Store.drawio.png)

_Diagram showing interaction between all microservices, implemented patterns and data flow_

### Implemented Services

| Service                   | Port | Description                              | Database |
| ------------------------- | ---- | ---------------------------------------- | -------- |
| **Config Server**         | 8001 | Centralized configuration management     | -        |
| **Eureka Server**         | 8761 | Service registration and discovery       | -        |
| **API Gateway**           | 8000 | Routing and load balancing               | -        |
| **Products Service**      | 8083 | Product and inventory management         | H2       |
| **Shopping Cart Service** | 8082 | Shopping cart management                 | H2       |
| **Sales Service**         | 8081 | Sales and transaction processing         | H2       |

## 🚀 Main Features

### 📱 Products Service

- ✅ Create, edit and delete products
- ✅ Search products by ID
- ✅ Query products with low stock (< 5 units)
- ✅ Update price, stock, name and brand
- ✅ Automatic inventory management

### 🛒 Shopping Cart Service

- ✅ Add products to cart
- ✅ Manage product quantities
- ✅ Query cart contents
- ✅ Remove products from cart
- ✅ Stock validation before adding products

### 💰 Sales Service

- ✅ Create new sales
- ✅ Query existing sales
- ✅ Edit sales date and status
- ✅ **Cancel sales** (automatically restores stock)
- ✅ **No physical deletion** of sales (auditability)
- ✅ Integration with Products Service for stock deduction
- ✅ Integration with Shopping Cart Service for sales data
- ✅ Distributed transaction handling

## 🛠️ Technologies Used

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

## 🚀 Installation and Execution

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
GET    /api/products/{id}              # Get product by ID
GET    /api/products/low-stock          # Products with stock < 5
POST   /api/products                   # Create new product
PUT    /api/products/{id}              # Update product
DELETE /api/products/{id}              # Delete product

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

## 🔄 Business Flow

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

## 🎯 Future Implementations

- [ ] **🔐 Security** with JWT and OAuth2
- [ ] **🌐 API Versioning** (v1, v2, etc.)
- [ ] **📱 Frontend** with React

## 📚 Implemented Patterns and Best Practices

### Architectural

- **Database per Service**: Each microservice has its own H2 database
- **API Composition**: Data composition from multiple services
- **Bulkhead Pattern**: Failure isolation between services

### Code

- **Separation of Concerns**: Clear separation of responsibilities
- **Dependency Injection**: Use of Spring IoC
- **Exception Handling**: Centralized exception handling
- **Data Transfer Objects**: DTOs for data transfer between layers and services

## Author

**Camila V. Heuer**

- 📧 Email: cbvillalbaheuer@gmail.com
- GitHub: [@CamilaVHeuer](https://github.com/CamilaVHeuer)
- LinkedIn: [Camila Heuer](https://linkedin.com/in/camilavheuer)

_Developed with 💙 using Spring Boot and Spring Cloud_
