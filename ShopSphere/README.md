# ShopSphere

Full-stack microservices e-commerce platform built per `ShopSphere_System_Design.docx`.

- **Backend**: Spring Boot 3.3.5, Java 17, Spring Cloud 2023.0.4 (Eureka, Config, Gateway, OpenFeign), Spring Security + JWT, JPA + MySQL, Kafka, JavaMail.
- **Frontend**: React 18 + Vite, React Router, Axios.
- **Observability**: Swagger (springdoc-openapi), Zipkin tracing (Micrometer Brave), Prometheus + Grafana, SonarQube + JaCoCo.
- **Async**: Apache Kafka with a dedicated `notification-service` consumer that sends order-confirmation emails.
- **Deploy**: Docker Compose for the full stack (infra + services).

## Architecture

```
         React (5173)
              │
              ▼
    Spring Cloud Gateway (8080)  ── JWT filter, CORS, route rules
              │
   ┌──────────┼──────────────┬──────────────┐
   ▼          ▼              ▼              ▼
 Auth (8081) Catalog (8082) Order (8083)  Admin (8084)
   │          │              │              │
   ▼          ▼              ▼ Kafka       (Feign reads)
 auth_db   catalog_db     order_db ───▶ notification-service (8085) ──▶ MailHog
                          + Kafka
```

Infrastructure: **Eureka** (8761) registry · **Config Server** (8888, native, reads `config-repo/`) · **Zipkin** (9411) · **Prometheus** (9090) · **Grafana** (3000) · **SonarQube** (9000) · **MailHog** (1025 SMTP / 8025 UI).

## Module layout

```
ShopSphere/
├── pom.xml                 # parent (packaging=pom)
├── docker-compose.yml
├── config-repo/            # native Config Server source
├── eureka-server/          :8761
├── config-server/          :8888
├── api-gateway/            :8080
├── auth-service/           :8081  (full impl)
├── catalog-service/        :8082  (full impl + 15 seeded products)
├── order-service/          :8083  (scaffold + Kafka publisher)
├── admin-service/          :8084  (sample dashboard JSON)
├── notification-service/   :8085  (Kafka → JavaMail → MailHog)
└── shopsphere-frontend/    :5173  (Vite + React)
```

## Prerequisites

- JDK 17, Maven (the wrapper `mvnw` works fine)
- Node 18+ and npm
- Docker Desktop (with Compose v2)

## Local startup

### 1) Infrastructure

```powershell
docker compose --profile infra up -d
```

Brings up MySQL, Kafka, Zookeeper, Zipkin, Prometheus, Grafana, SonarQube, MailHog. SonarQube takes ~1 minute to become ready the first time.

### 2) Build all modules

```powershell
.\mvnw -DskipTests package
```

### 3) Backend services

Open **5 separate terminals** (or use `scripts/start-all.ps1`):

```powershell
.\mvnw -pl eureka-server  spring-boot:run    # wait until /actuator/health is UP
.\mvnw -pl config-server  spring-boot:run
.\mvnw -pl api-gateway    spring-boot:run
.\mvnw -pl auth-service   spring-boot:run
.\mvnw -pl catalog-service spring-boot:run
# Optional:
.\mvnw -pl order-service  spring-boot:run
.\mvnw -pl admin-service  spring-boot:run
.\mvnw -pl notification-service spring-boot:run
```

### 4) Frontend

```powershell
cd shopsphere-frontend
npm install
npm run dev
```

Open <http://localhost:5173>.

## Useful URLs

| What | URL |
|---|---|
| Frontend | <http://localhost:5173> |
| Gateway | <http://localhost:8080> |
| Eureka | <http://localhost:8761> |
| Config Server | <http://localhost:8888/actuator/health> |
| Auth Swagger | <http://localhost:8081/swagger-ui.html> |
| Catalog Swagger | <http://localhost:8082/swagger-ui.html> |
| Order Swagger | <http://localhost:8083/swagger-ui.html> |
| Admin Swagger | <http://localhost:8084/swagger-ui.html> |
| Notification Swagger | <http://localhost:8085/swagger-ui.html> |
| Zipkin | <http://localhost:9411> |
| Prometheus | <http://localhost:9090> |
| Grafana (admin/admin) | <http://localhost:3000> |
| SonarQube (admin/admin) | <http://localhost:9000> |
| MailHog UI | <http://localhost:8025> |

## Smoke-test

```powershell
# 1. Sign up
curl -X POST http://localhost:8080/gateway/auth/signup -H "Content-Type: application/json" -d "{\"name\":\"Alice\",\"email\":\"alice@test.com\",\"password\":\"pass1234\"}"

# 2. Save the returned token, then call /me
$token = "<paste>"
curl -H "Authorization: Bearer $token" http://localhost:8080/gateway/auth/me

# 3. List products (public)
curl http://localhost:8080/gateway/catalog/products
```

## Tests + coverage

```powershell
.\mvnw -pl auth-service,catalog-service test
.\mvnw -pl auth-service,catalog-service verify     # also runs JaCoCo report
```

## SonarQube one-time setup

1. Open <http://localhost:9000>, log in `admin/admin`, set a new password.
2. **My Account → Security → Generate Token**, name it `shopsphere`, copy the token.
3. Run a scan from the repo root:

```powershell
.\mvnw -DskipTests verify
.\mvnw sonar:sonar -Dsonar.token=<your-token> -Dsonar.host.url=http://localhost:9000 -Dsonar.projectKey=shopsphere -Dsonar.projectName=ShopSphere
```

Open the project in SonarQube to view bugs, code smells, and JaCoCo-driven coverage.

## Verify Kafka → email loop

```powershell
# Send a fake order.placed event
docker exec -it shopsphere-kafka kafka-console-producer --broker-list localhost:9092 --topic order.placed
# Paste a single line of JSON, then Ctrl+C:
{"orderId":1,"userId":1,"userEmail":"alice@test.com","totalAmount":2999.00,"items":[{"productId":1,"productName":"Wireless Headphones","quantity":1,"unitPrice":2999.00}],"timestamp":"2025-01-01T00:00:00"}
```

Open MailHog at <http://localhost:8025> — the email should appear.

## What's done in Phase 1

- [x] Maven multi-module restructure (Spring Boot 3.3.5 / Spring Cloud 2023.0.4)
- [x] Eureka, Config Server, API Gateway with JWT global filter and CORS
- [x] **Auth Service**: signup, login, refresh, me (GET/PUT), logout, admin/users, JWT with JJWT 0.12.6, BCrypt, Swagger, unit tests
- [x] **Catalog Service**: Product/Category CRUD, dynamic Specification filters, paginated search, featured list, 15 seeded products, Swagger, unit tests
- [x] Order, Admin, Notification scaffolded (entities, Feign clients, Kafka publisher/consumer, JavaMail, sample dashboard)
- [x] Docker Compose with full observability stack (MySQL, Kafka, Zipkin, Prometheus, Grafana, SonarQube, MailHog)
- [x] React frontend: Login, Signup, Home (featured), Product List (search/pagination), Product Detail, Admin Dashboard tiles
- [x] JaCoCo coverage + Sonar Maven plugin wired in parent POM

## Phase 2 (planned)

- Full Order Service: cart → 4-step checkout → place → Kafka publish → stock decrement
- Full Admin reports + dashboard aggregation via Feign
- Customer cart/checkout/orders pages, admin product/order/report pages
- Token blacklist persistence on logout
- `@SpringBootTest` integration tests
- GitHub Actions CI/CD pipeline
