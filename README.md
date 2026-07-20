# Just Catering SuperAdmin

Enterprise Spring Boot backend for the Just Catering SuperAdmin platform.

## Stack

- Java 21 · Spring Boot 3.3 · Spring Security + JWT · JPA/Hibernate · SQLite · Flyway · MapStruct · SpringDoc

## Quick Start

```bash
mkdir -p data
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
mvn spring-boot:run
```

| Service | URL |
|---------|-----|
| API | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui.html |
| SQLite DB | `./data/just_catering_superadmin.db` |

**Super Admin:** `admin@justcatering.com` / `Admin@12345`

### Optional: PostgreSQL

To use PostgreSQL instead of SQLite:

```bash
docker compose up -d
export SPRING_PROFILES_ACTIVE=postgres,dev
mvn spring-boot:run
```

## Complete Module Map (Figma → API)

| # | Module | Base Path | Status |
|---|--------|-----------|--------|
| 1 | Auth | `/api/auth` | Done |
| 2 | Users / Roles / Permissions | `/api/users` `/api/roles` `/api/permissions` | Done |
| 3 | Departments / Members | `/api/departments` | Done |
| 4 | Products | `/api/products` | Done |
| 5 | Clients | `/api/clients` | Done |
| 6 | Meeting Leads | `/api/leads` | Done |
| 7 | Follow-ups | `/api/follow-ups` | Done |
| 8 | Queries / Requirements | `/api/queries` | Done |
| 9 | Payments / Receipts | `/api/payments` | Done |
| 10 | Expenses | `/api/expenses` | Done |
| 11 | Project Ops (Manager, Deadline, P&L) | `/api/project-ops` | Done |
| 12 | Dashboard | `/api/dashboard` | Done |
| 13 | File Upload | `/api/files` | Done |

## Business Flow

```
Login → Dashboard Overview
  → Meeting Leads → (qualify) → Clients
  → Client Detail
       → Follow-ups
       → Queries / Requirements
       → Payments / Receipts / Overview
       → Expenses
       → Manager Assignment / Deadlines / P&L
  → Team Departments
  → Products
  → Settings (Roles & Permissions via Users/Roles APIs)
```

## Key Aggregates (verified)

Dashboard overview returns meeting leads, clients, revenue collected, receivable/pending, and query counts.

Payment overview: `productCost`, `amountPaid`, `remainingBalance`.

Expense summary: travel / food / office / other / total.

Project P&L: income − expenses with margin %.

## Postman Collections

- `postman/Just-Catering-SuperAdmin-Auth.postman_collection.json`
- `postman/Just-Catering-SuperAdmin-Users-Roles-Permissions.postman_collection.json`
- `postman/Just-Catering-SuperAdmin-Departments.postman_collection.json`
- `postman/Just-Catering-SuperAdmin-Products.postman_collection.json`
- `postman/Just-Catering-SuperAdmin-Clients.postman_collection.json`

## Flyway

Migrations `V1`–`V21` cover auth, RBAC seed, departments, products, clients, leads, follow-ups, queries, payments, expenses, and project ops.
# Just-Catering
