# 🏪 Online Appliance Store - Microservices Architecture

## 📋 Descripción

Sistema de comercio electrónico para venta de electrodomésticos desarrollado con **arquitectura de microservicios** utilizando Spring Boot y Spring Cloud. Este proyecto demuestra la implementación de patrones de diseño distribuidos y mejores prácticas en desarrollo de microservicios.

> **Nota**: Proyecto desarrollado como parte del aprendizaje en arquitectura de microservicios, basado en el curso de TodoCode, adaptado para portfolio profesional.

## 🏗️ Arquitectura de Microservicios

### Patrones de Diseño Implementados

- **🚪 API Gateway Pattern**: Punto único de entrada para todas las peticiones
- **🔍 Service Discovery**: Registro y descubrimiento automático de servicios con Eureka
- **⚙️ Centralized Configuration**: Configuración centralizada con Config Server
- **🔄 Circuit Breaker**: Tolerancia a fallos con Resilience4j
- **🔁 Retry Pattern**: Reintentos automáticos en llamadas entre servicios
- **📋 MVC Pattern**: Arquitectura Modelo-Vista-Controlador en cada microservicio
- **📦 DTO Pattern**: Transferencia de datos entre capas y servicios

### Diagrama de Arquitectura

![Arquitectura de Microservicios](Microservices%20Architecture%20–%20Online%20Appliance%20Store.drawio.png)

_Diagrama que muestra la interacción entre todos los microservicios, patrones implementados y flujo de datos_

### Servicios Implementados

| Servicio                  | Puerto | Descripción                             | Base de Datos |
| ------------------------- | ------ | --------------------------------------- | ------------- |
| **Config Server**         | 8001   | Gestión centralizada de configuración   | -             |
| **Eureka Server**         | 8761   | Registro y descubrimiento de servicios  | -             |
| **API Gateway**           | 8080   | Enrutamiento y balanceado de carga      | -             |
| **Products Service**      | 8083   | Gestión de productos y stock            | H2            |
| **Shopping Cart Service** | 8082   | Gestión de carritos de compra           | H2            |
| **Sales Service**         | 8081   | Procesamiento de ventas y transacciones | H2            |

## 🚀 Funcionalidades Principales

### 📱 Products Service

- ✅ Crear, editar y eliminar productos
- ✅ Buscar productos por ID
- ✅ Consultar productos con stock bajo (< 5 unidades)
- ✅ Actualizar precio, stock, nombre y marca
- ✅ Gestión automática de inventario

### 🛒 Shopping Cart Service

- ✅ Agregar productos al carrito
- ✅ Gestionar cantidades de productos
- ✅ Consultar contenido del carrito
- ✅ Eliminar productos del carrito
- ✅ Validación de stock antes de agregar productos

### 💰 Sales Service

- ✅ Crear nuevas ventas
- ✅ Consultar ventas existentes
- ✅ Editar fecha y estado de ventas
- ✅ **Cancelar ventas** (restablece stock automáticamente)
- ✅ **No borrado físico** de ventas (auditabilidad)
- ✅ Integración con Products Service para descuento de stock
- ✅ Integración con Shopping Cart Service para datos de venta
- ✅ Manejo de transacciones distribuidas

## 🛠️ Tecnologías Utilizadas

### Backend

- **Java 17**
- **Spring Boot 3.1.3**
- **Spring Cloud 2022.0.4**
- **Spring Data JPA**
- **H2 Database** (En memoria)
- **OpenFeign** (Comunicación entre servicios)
- **Netflix Eureka** (Service Discovery)
- **Spring Cloud Gateway** (API Gateway)
- **Spring Cloud Config** (Configuración centralizada)
- **Resilience4j** (Circuit Breaker y Retry)

### Herramientas

- **Maven** (Gestión de dependencias)
- **Postman** (Testing de APIs)

## 📋 Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- IDE recomendado: IntelliJ IDEA o Eclipse

## 🚀 Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/CamilaVHeuer/OnlineApplianceStore-MS.git
cd OnlineApplianceStore-MS
```

### 2. Orden de inicio de servicios

**⚠️ Importante**: Los servicios deben iniciarse en el siguiente orden:

```bash
# 1. Config Server (Puerto 8001)
cd config-server
mvn spring-boot:run

# 2. Eureka Server (Puerto 8761)
cd ../eureka-sv
mvn spring-boot:run

# 3. Servicios de negocio
cd ../products-service
mvn spring-boot:run

cd ../shopping-cart-service
mvn spring-boot:run

cd ../sales-service
mvn spring-boot:run

# 4. API Gateway (Puerto 8080)
cd ../api-gateway
mvn spring-boot:run
```

### 3. Verificar servicios activos

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Config Server**: http://localhost:8001
- **H2 Console** (cada servicio):
  - Products: http://localhost:8083/h2-console
  - Shopping Cart: http://localhost:8082/h2-console
  - Sales: http://localhost:8081/h2-console

## 📡 Endpoints de API

### Products Service (via Gateway: `/products`)

```http
GET    /api/products                    # Listar todos los productos
GET    /api/products/{id}              # Obtener producto por ID
GET    /api/products/lowstock          # Productos con stock < 5
POST   /api/products                   # Crear nuevo producto
PUT    /api/products/{id}              # Actualizar producto
DELETE /api/products/{id}              # Eliminar producto
PUT    /api/products/{id}/stock/{quantity}   # Actualizar stock específico
```

### Shopping Cart Service (via Gateway: `/cart`)

```http
GET    /api/cart                       # Listar todos los carritos
GET    /api/cart/{id}                  # Obtener carrito por ID
POST   /api/cart                       # Crear nuevo carrito
PUT    /api/cart/{id}                  # Actualizar carrito
DELETE /api/cart/{id}                  # Eliminar carrito
```

### Sales Service (via Gateway: `/sales`)

```http
GET    /api/sales                      # Listar todas las ventas
GET    /api/sales/{id}                 # Obtener venta por ID
GET    /api/sales/date/{date}          # Obtener ventas por fecha
POST   /api/sales                      # Crear nueva venta
PUT    /api/sales/{id}                 # Actualizar venta
PUT    /api/sales/cancel/{id}          # Cancelar venta (restaura stock)
```

## 🗃️ Modelo de Datos

### Product

```json
{
  "productId": 1,
  "name": "Refrigerador Samsung",
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

## 🔄 Flujo de Negocio

### Proceso de Venta Completo

1. **Crear/Consultar Productos** → Products Service
2. **Agregar al Carrito** → Shopping Cart Service
3. **Procesar Venta** → Sales Service
   - Consulta carrito y productos (Shopping Cart Service)
   - Valida disponibilidad de stock
   - Descuenta stock (Products Service con Circuit Breaker)
   - Registra la venta
4. **Cancelación** (si es necesario)
   - Cambia estado de venta a "CANCELLING"
   - **Restaura stock automáticamente** (Products Service)
   - Actualiza estado a "CANCELLED"

### Tolerancia a Fallos

- **Circuit Breaker**: Previene cascadas de fallos entre servicios
- **Retry Pattern**: Reintentos automáticos en comunicación
- **Fallback Methods**: Respuestas por defecto en caso de fallo
- **Transactional Management**: Consistencia en operaciones críticas

### Gestión de Estados de Venta

El sistema implementa un manejo de estados similar al **Saga Pattern**:

- **CREATED**: Venta creada exitosamente
- **CANCELLING**: Proceso de cancelación iniciado
- **STOCK_RESTORED**: Stock restaurado tras cancelación
- **CANCELLED**: Venta cancelada completamente

## 🧪 Testing con Postman

### Flujo de Prueba Completo

1. **Crear Productos**

```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Refrigerador Samsung",
  "brand": "Samsung",
  "unitPrice": 899.99,
  "stock": 10
}
```

2. **Crear Carrito con Productos**

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

3. **Procesar Venta**

```http
POST http://localhost:8080/api/sales
Content-Type: application/json

{
  "date": "2026-01-12",
  "cartId": 1
}
```

4. **Cancelar Venta (Opcional)**

```http
PUT http://localhost:8080/api/sales/cancel/1
```

## 🎯 Próximas Implementaciones

- [ ] **🐳 Dockerización** de todos los servicios
- [ ] **🔐 Seguridad** con JWT y OAuth2
- [ ] **📊 Monitoreo** con Micrometer y Prometheus
- [ ] **📝 Testing** unitario e integración
- [ ] **🚀 CI/CD** pipeline con GitHub Actions
- [ ] **📋 Logging** centralizado con ELK Stack
- [ ] **🌐 Versionado de APIs** (v1, v2, etc.)
- [ ] **📱 Frontend** con React o Angular

## 📚 Patrones y Buenas Prácticas Implementadas

### Arquitecturales

- **Database per Service**: Cada microservicio tiene su propia base de datos H2
- **API Composition**: Composición de datos desde múltiples servicios
- **Bulkhead Pattern**: Aislamiento de fallas entre servicios

### Código

- **Separation of Concerns**: Separación clara de responsabilidades
- **Dependency Injection**: Uso de Spring IoC
- **Exception Handling**: Manejo centralizado de excepciones
- **Data Transfer Objects**: DTOs para transferencia de datos


## 👨‍💻 Autor

**Camila V. Heuer**

- GitHub: [@CamilaVHeuer](https://github.com/CamilaVHeuer)
- LinkedIn: [Camila Heuer](https://linkedin.com/in/camilavheuer)

_Desarrollado con 💙 usando Spring Boot y Spring Cloud_
