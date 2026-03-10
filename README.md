# AGMS - Automated Greenhouse Management System

A microservices-based greenhouse management system built with Spring Boot 4.0.3 and Spring Cloud 2025.1.0.

**Course:** ITS 2018 - Software Architectures & Design Patterns II
**Student:** Vidura Priyadarshana

## Architecture

```
                    +------------------+
                    | Service Registry |
                    |  (Eureka 8761)   |
                    +--------+---------+
                             |
                    +--------+---------+
                    |  Config Server   |
                    |    (8888)        |
                    +--------+---------+
                             |
                    +--------+---------+
                    |   API Gateway    |
                    | (8080 - Netty)   |
                    +--+--+--+--+-----+
                       |  |  |  |
          +------------+  |  |  +------------+
          |               |  |               |
   +------+------+ +-----+------+ +-----+------+ +------+------+
   | Zone Service | | Sensor Svc | | Automation | | Crop Service|
   |   (8081)     | |  (8082)    | |   (8083)   | |   (8084)    |
   +------+------+ +-----+------+ +-----+------+ +------+------+
          |                |              |              |
     +----+----+    +------+------+  +----+----+   +----+----+
     |  MySQL  |    | External    |  |  MySQL  |   |  MySQL  |
     |   gms   |    | IoT API     |  |   gms   |   |   gms   |
     +---------+    +-------------+  +---------+   +---------+
```

## Technology Stack

- **Java 21**, Spring Boot 4.0.3, Spring Cloud 2025.1.0
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring Cloud Config** - Centralized Configuration (native filesystem)
- **Spring Cloud Gateway** - API Gateway with JWT Authentication
- **Spring Data JPA** + MySQL - Persistence (zone, automation, crop)
- **Spring Cloud OpenFeign** - Inter-service communication
- **External IoT API** - Device registration & telemetry (sensor-service)

## Services

| Service | Port | Description |
|---------|------|-------------|
| service-registry | 8761 | Eureka Server - Service Discovery |
| config-server | 8888 | Centralized configuration (native profile) |
| api-gateway | 8080 | API Gateway with JWT filter + routing |
| zone-service | 8081 | Greenhouse zone CRUD + threshold management |
| sensor-service | 8082 | IoT device registration + telemetry polling |
| automation-service | 8083 | Telemetry processing + automated actions |
| crop-service | 8084 | Crop lifecycle management |

## Prerequisites

- **Java 21+**
- **Maven 3.9+**
- **MySQL 8.x** - Remote database at `64.227.176.19:3306/gms`
  - See `MYSQL_SETUP.md` for database setup instructions
- **Internet access** - Required for the external IoT API at `http://104.211.95.241:8080/api`

## Database Setup

Before starting the JPA services, ensure the MySQL database is ready:

```sql
CREATE DATABASE IF NOT EXISTS gms;
GRANT ALL PRIVILEGES ON gms.* TO 'root'@'%';
FLUSH PRIVILEGES;
```

Tables are auto-created by JPA (`ddl-auto=update`). See `MYSQL_SETUP.md` for detailed instructions.

## How to Build

Build all services from the project root:

```bash
# Build each service (run from project root)
cd service-registry && mvn package -DskipTests && cd ..
cd config-server && mvn package -DskipTests && cd ..
cd api-gateway && mvn package -DskipTests && cd ..
cd zone-service && mvn package -DskipTests && cd ..
cd sensor-service && mvn package -DskipTests && cd ..
cd automation-service && mvn package -DskipTests && cd ..
cd crop-service && mvn package -DskipTests && cd ..
```

## How to Start (IMPORTANT: Follow this order)

Services MUST be started in this exact order:

### Step 1: Service Registry (Eureka)

```bash
java -jar service-registry/target/service-registry-0.0.1-SNAPSHOT.jar
```

Wait until you see `Started ServiceRegistryApplication`. Verify at http://localhost:8761

### Step 2: Config Server

```bash
java -jar config-server/target/config-server-0.0.1-SNAPSHOT.jar
```

Wait until started. Verify at http://localhost:8888/zone-service/default

### Step 3: API Gateway

```bash
java -jar api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
```

Wait until `Started ApiGatewayApplication` appears.

### Step 4: Domain Services (can start in any order)

```bash
java -jar zone-service/target/zone-service-0.0.1-SNAPSHOT.jar
java -jar sensor-service/target/sensor-service-0.0.1-SNAPSHOT.jar
java -jar automation-service/target/automation-service-0.0.1-SNAPSHOT.jar
java -jar crop-service/target/crop-service-0.0.1-SNAPSHOT.jar
```

### Step 5: Verify

Open http://localhost:8761 and confirm all 6 services are registered with status **UP**:
- CONFIG-SERVER
- API-GATEWAY
- ZONE-SERVICE
- SENSOR-SERVICE
- AUTOMATION-SERVICE
- CROP-SERVICE

## API Endpoints

All requests go through the API Gateway at `http://localhost:8080`. All routes require a valid JWT token in the `Authorization` header.

### Zone Service (`/api/zones`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/zones` | Create a new zone |
| GET | `/api/zones` | Get all zones |
| GET | `/api/zones/{id}` | Get zone by ID |
| PUT | `/api/zones/{id}` | Update a zone |
| DELETE | `/api/zones/{id}` | Delete a zone |

### Sensor Service (`/api/sensors`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/sensors/devices` | Register a new IoT device |
| GET | `/api/sensors/latest` | Get latest telemetry readings |

### Automation Service (`/api/automation`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/automation/process` | Process telemetry and trigger actions |
| GET | `/api/automation/logs` | Get all automation action logs |

### Crop Service (`/api/crops`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/crops` | Create a new crop |
| GET | `/api/crops` | Get all crops |
| PUT | `/api/crops/{id}/status` | Update crop lifecycle status |

## End-to-End Data Flow

1. **Create Zone** - POST `/api/zones` with temperature/humidity thresholds
2. **Register Device** - POST `/api/sensors/devices` with zone assignment
3. **Telemetry Polling** - Sensor-service automatically polls IoT API every 10 seconds
4. **Automation Processing** - When telemetry arrives, automation-service checks zone thresholds and logs actions (ACTIVATE_COOLING, ACTIVATE_HEATING, etc.)
5. **Crop Management** - Create crops assigned to zones, update lifecycle: SEEDLING -> VEGETATIVE -> FLOWERING -> HARVESTING -> HARVESTED

## Project Structure

```
agms/
├── service-registry/       # Eureka Server
├── config-server/          # Spring Cloud Config (native)
├── config-repo/            # Config properties files
├── api-gateway/            # Spring Cloud Gateway + JWT
├── zone-service/           # Zone Management (JPA/MySQL)
├── sensor-service/         # Sensor Telemetry (IoT API)
├── automation-service/     # Automation & Control (JPA/MySQL)
├── crop-service/           # Crop Inventory (JPA/MySQL)
├── postman/                # Postman API collection
├── docs/                   # Eureka dashboard screenshot
├── MYSQL_SETUP.md          # Database setup guide
└── README.md               # This file
```

## Key Design Patterns

- **Microservices Architecture** - Independent, loosely coupled services
- **API Gateway Pattern** - Single entry point with JWT authentication
- **Service Discovery** - Eureka-based dynamic service registration
- **Centralized Configuration** - Spring Cloud Config for externalized config
- **Circuit Breaker** - Feign clients with fallback handling
- **AOP Cross-Cutting** - Logging aspects on all service/controller layers
- **DTO Pattern** - Request/Response DTOs separate from JPA entities

## External IoT API

The sensor-service integrates with an external IoT platform:

- **Base URL:** `http://104.211.95.241:8080/api`
- **Authentication:** Username/password login returns JWT tokens
- **Device Registration:** `POST /devices`
- **Telemetry Fetch:** `GET /devices/telemetry/{deviceId}`
