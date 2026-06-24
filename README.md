# Banking App

A full-stack banking application built as a portfolio project — covering database design, a REST API backend, authentication/authorization, and a React frontend.

## Overview

This project simulates core banking functionality: customer accounts, money transfers, transaction history, and account management — built end-to-end from the database up.

**Live features:**
- Customer signup and login (JWT-based authentication)
- View account balances and details
- Open new accounts
- Transfer funds between accounts (atomic, via a SQL Server stored procedure)
- View transaction history per account
- Authorization enforced — customers can only access their own accounts/transactions

## Tech Stack

| Layer | Technology |
|---|---|
| Database | SQL Server (stored procedures, triggers, constraints) |
| Backend | Java, Spring Boot, JDBC (no ORM — hand-written DAOs) |
| Auth | Spring Security, JWT (jjwt), BCrypt password hashing |
| Frontend | React (Vite), plain CSS |

## Architecture

```
React (localhost:5173)
        │  HTTP + JWT
        ▼
Spring Boot REST API (localhost:8080)
   Controller → Repository (DAO) → JDBC
        │
        ▼
SQL Server (stored procedures, triggers)
```

The backend deliberately uses **plain JDBC with hand-written DAOs** rather than an ORM (e.g. Hibernate/JPA) — this was a learning choice, to understand exactly what's happening at the SQL level rather than relying on framework magic.

## Database Schema

7 tables: `Customers`, `Accounts`, `Branches`, `AccountTypes`, `Transactions`, `Cards`, `AuditLog`.

Key design points:
- `TransferFunds` stored procedure handles balance updates and transaction logging atomically (with row-level locking to prevent race conditions on concurrent transfers)
- A trigger prevents account balances from ever going negative, as a safety net independent of application-level checks
- `AuditLog` table logs key customer actions

## Authentication & Authorization

- Passwords are hashed with BCrypt before storage — never stored or returned in plaintext
- Login returns a signed JWT (1-hour expiry) containing the customer's ID
- Every protected endpoint requires a valid token (`Authorization: Bearer <token>`)
- **Ownership checks**: every account/transaction endpoint verifies the requesting customer actually owns the resource before returning data — a customer cannot view or transfer from another customer's account, even with a valid token

## API Endpoints (selected)

| Method | Endpoint | Auth required |
|---|---|---|
| POST | `/api/customers` | No (signup) |
| POST | `/api/auth/login` | No |
| GET | `/api/accounts/{id}` | Yes (must own account) |
| POST | `/api/accounts/transfer` | Yes (must own source account) |
| GET | `/api/transactions/account/{id}` | Yes (must own account) |

## Running Locally

### Prerequisites
- Java 21+, Maven
- SQL Server (Express edition works)
- Node.js 18+

### Backend
1. Run the schema script (`schema.sql`) and seed script (`seed.sql`) against your SQL Server instance
2. Configure `src/main/resources/application.properties` with your connection details
3. `mvn spring-boot:run`

### Frontend
```
cd banking-frontend
npm install
npm run dev
```

Visit `http://localhost:5173`.

## Known Limitations

This is a portfolio project, not a production system. Notable simplifications:
- JWT secret is hardcoded (should be an environment variable in production)
- No refresh tokens — sessions expire after 1 hour with no renewal flow
- No automated test suite yet
- Account numbers are generated client-side with basic randomness, not a guaranteed-unique server-side scheme

## What I Learned

This project was built incrementally, with a deliberate focus on understanding each layer rather than scaffolding it automatically:
- Writing raw JDBC instead of an ORM, to understand connection pooling, prepared statements, and result mapping directly
- Diagnosing real environment issues (JDBC driver/auth DLL version mismatches, CORS, stale builds) rather than working around them
- Implementing authorization (not just authentication) as a distinct, necessary layer — verified by attempting to break it
