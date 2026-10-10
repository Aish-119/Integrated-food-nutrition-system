# Integrated Food Supply & Nutrition Management System

An Enterprise Java Web Application designed to coordinate food procurement, monitor stock shortages, track wastage, and manage beneficiary requirements.

## Architecture
- **Design Pattern:** Model-View-Controller (MVC) & Microservices Architecture
- **Frontend Service (Port 8080):** Spring Boot MVC, Thymeleaf, Bootstrap 5
- **Backend Service (Port 8081):** Spring Boot REST API, Spring Data JPA / JDBC
- **Database:** PostgreSQL (`food_management_db`)
- **Inter-Service Communication:** Synchronous RESTful calls using Spring `RestTemplate`

## Core Modules & API Endpoints
- `GET /api/inventory` — Retrieve all current stock records and shortage status
- `POST /api/inventory` — Register new procurement batches
- `DELETE /api/inventory/{id}` — Log food wastage and remove expired items

## Team Distribution
- **Role 1 (Lead & Integration):** REST client integration (`RestTemplate`), project structure, documentation
- **Role 2 (Frontend UI):** Thymeleaf dashboard, Bootstrap forms, status badges
- **Role 3 (Backend API):** REST controllers, CRUD request mapping, JSON serialization
- **Role 4 (Database & JPA):** PostgreSQL schema, Spring Data JPA entities, database persistence
