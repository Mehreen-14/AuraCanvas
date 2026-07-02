# AuraCanvas

A full-stack art & stationery e-commerce platform built with Spring Boot and React, featuring separate user and admin panels.

## Tech Stack

**Backend:** Java 25, Spring Boot 3.4.1, Spring Security, Spring Data JPA, PostgreSQL, JWT (0.12.6)

**Frontend:** React 18, React Router 6, Axios, react-icons (Feather)

## Features

### User
- Browse products by category
- Product detail with reviews and star ratings
- Shopping cart with quantity controls
- Checkout and order history
- Write reviews (clickable star icons)

### Admin
- Dashboard with stats (products, orders, revenue, users)
- Product management (add, edit — no delete)
- Order management
- User management
- Image upload for products

### General
- JWT-based authentication
- Role-based access (USER / ADMIN)
- Responsive black/crimson/grey theme
- Vector icons throughout (no emoji)

## Getting Started

### Prerequisites
- JDK 25+
- Node.js 18+
- PostgreSQL

### Database Setup

```sql
CREATE DATABASE auracanvas;
```

The tables are auto-created by Hibernate (`ddl-auto=update`). Categories and an admin user are seeded on first startup.

**Default admin:** `admin@auracanvas.com` / `admin123`

### Backend

```bash
cd auracanvas
.\mvnw.cmd spring-boot:run
```

Runs on `http://localhost:8080`

### Frontend

```bash
cd aura_frontend/auracanvus
npm install
npm start
```

Runs on `http://localhost:3000`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/register` | Register |
| GET | `/api/products` | List products |
| GET | `/api/products/{id}` | Product detail |
| GET | `/api/cart` | Get cart |
| POST | `/api/cart` | Add to cart |
| PUT | `/api/cart/{id}` | Update cart item |
| DELETE | `/api/cart/{id}` | Remove from cart |
| POST | `/api/orders` | Place order |
| GET | `/api/orders` | Order history |
| GET | `/api/reviews/product/{productId}` | Get reviews |
| POST | `/api/reviews/product/{productId}` | Add review |
| GET | `/api/admin/dashboard` | Dashboard stats |
| GET | `/api/admin/orders/recent` | Recent orders |
| POST | `/api/admin/products` | Create product |
| PUT | `/api/admin/products/{id}` | Update product |
| DELETE | `/api/admin/products/{id}` | Delete product |
| POST | `/api/upload` | Upload image |
| GET | `/api/admin/users` | List users |

## Project Structure

```
auracanvas/
├── src/main/java/com/auracanvas/
│   ├── controller/        # REST controllers
│   ├── dto/               # Request/response DTOs
│   ├── entity/            # JPA entities
│   ├── enums/             # Role, OrderStatus
│   ├── repository/        # JPA repositories
│   ├── security/          # JWT & Spring Security config
│   └── service/           # Business logic
├── aura_frontend/auracanvus/src/
│   ├── api/               # Axios API modules
│   ├── components/        # Shared components (Navbar, Footer, etc.)
│   ├── context/           # Auth, Cart contexts
│   └── pages/             # Page components (Shop, Cart, Admin, etc.)
└── uploads/images/        # Uploaded product images
```

## Environment Configuration

Key settings in `application.properties`:

| Property | Value |
|----------|-------|
| `server.port` | `8080` |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/auracanvas` |
| `jwt.secret` | (set in properties) |
| `jwt.expiration` | `86400000` (24h) |
| `upload.dir` | `uploads/images/` |
| `spring.servlet.multipart.max-file-size` | `10MB` |
