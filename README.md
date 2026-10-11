# Integrated Food Supply & Nutrition Management System

An enterprise Java web application designed to manage food inventory, procurement, distribution, beneficiary nutrition needs, quality inspections, and food wastage through an integrated microservices architecture.

## 1. Project Overview

The Integrated Food Supply & Nutrition Management System helps manage food resources and beneficiary requirements through a web-based portal connected to REST APIs and a PostgreSQL database.

The application uses three Spring Boot services that communicate through RESTful APIs.

## 2. Technology Stack

- **Programming Language:** Java
- **Backend Framework:** Spring Boot
- **Frontend:** Spring Boot MVC, Thymeleaf, HTML
- **Database:** PostgreSQL
- **Data Access:** Spring Data JPA and Hibernate
- **Inter-Service Communication:** REST APIs using RestTemplate
- **Build Tool:** Apache Maven
- **Architecture:** Model-View-Controller (MVC) and Microservices

## 3. System Architecture

### Frontend Service — Port 8080

Provides the web interface for accessing the system's management functions.

Responsibilities:
- Inventory management interface
- Beneficiary management interface
- Procurement management interface
- Food distribution interface
- Quality inspection and wastage interface
- Nutrition programme interface

### Food Backend Service — Port 8081

Provides REST APIs for food and inventory operations.

Responsibilities:
- Food item management
- Inventory batch management
- Procurement records
- Supplier information
- Nutrition value records
- Stock movement records
- Quality inspections
- Wastage records

### Nutrition Service — Port 8082

Provides REST APIs for beneficiary and nutrition programme operations.

Responsibilities:
- Beneficiary management
- Food distribution planning and tracking
- Distribution item records
- Distribution status management
- Inventory availability checks for distributions
- Nutrition programme creation and listing

### Database

PostgreSQL stores the application's persistent data.

- Database name: `food_nutrition_db`
- Database port: `5433`
- Database user: configured through environment variables

The database password must be configured locally and must not be committed to GitHub.

## 4. Core Functional Modules

### 1. Beneficiary Management

Maintains beneficiary profiles, including names, ages, household sizes, addresses, and nutrition needs.

### 2. Food Item and Nutrition Management

Provides food item records and nutrition value APIs for managing food-related information.

### 3. Procurement Management

Records procurement details, including food items, quantities, suppliers, and procurement dates.

### 4. Inventory and Stock Management

Manages inventory batches, quantities, units, received dates, and expiry dates. Inventory quantities can be updated through REST APIs.

### 5. Food Distribution Management

Records distributions for beneficiaries, checks inventory availability, tracks distribution items, and supports distribution status transitions. Successful distribution creation deducts the requested quantity from eligible inventory batches.

### 6. Nutrition Programme Management

Provides API support and a web interface for creating and listing nutrition programmes.

### 7. Food Quality and Wastage Management

Records food quality inspections and wastage details, including food items, batches, quantities, reasons, dates, and remarks. Wastage stock deduction is being integrated and must be verified against the running application.

### 8. Dashboard and Reporting

The web portal provides navigation to the management modules. Comprehensive live reporting and dashboard summaries remain areas for further development.

## 5. Main API Endpoints

### Food Backend — `http://localhost:8081`

| Endpoint | Purpose |
|---|---|
| `/api/food-items` | Retrieve food items |
| `/api/inventory-batches` | Manage inventory batches |
| `/api/nutrition-values` | Access nutrition values |
| `/api/procurements` | Manage procurement records |
| `/api/suppliers` | Access supplier records |
| `/api/stock-movements` | Access stock movement records |
| `/api/quality-inspections` | Manage quality inspections |
| `/api/wastage-records` | Manage wastage records |

### Nutrition Service — `http://localhost:8082`

| Endpoint | Purpose |
|---|---|
| `/api/beneficiaries` | Manage beneficiaries |
| `/api/distributions` | List and create distributions |
| `/api/distributions/check` | Check inventory availability |
| `/api/distributions/{id}/items` | Retrieve distribution items |
| `/api/distributions/{id}/status` | Update distribution status using PATCH |
| `/api/programmes` | List and create nutrition programmes |

The supported HTTP methods and request fields are defined in the corresponding REST controllers and DTOs.

## 6. Project Structure

```text
Integrated-food-nutrition-system/
|
|-- food-frontend-service/
|   |-- food-frontend-service/
|       |-- src/main/java/
|       |-- src/main/resources/templates/
|       |-- pom.xml
|
|-- food-backend-service/
|   |-- food-backend-service/
|       |-- src/main/java/
|       |-- src/main/resources/
|       |-- pom.xml
|
|-- nutrition-service/
|   |-- nutrition-service/
|       |-- src/main/java/
|       |-- src/main/resources/
|       |-- pom.xml
|
|-- README.md
```

## 7. Prerequisites

Install the following before running the project:

- JDK 21 or compatible Java version
- Apache Maven
- PostgreSQL
- Git

Make sure PostgreSQL is running and the `food_nutrition_db` database exists on port `5433`.

## 8. Database Configuration

The backend services use environment variables for database configuration.

For each backend terminal, configure the database credentials before starting the service.

Example using PowerShell:

```powershell
$env:DB_USER = "postgres"
$env:DB_PORT = "5433"
$secure = Read-Host "PostgreSQL password" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $secure).Password
```

Enter the password locally when prompted. Do not put the password in source code, configuration files committed to GitHub, or this README.

If the services are configured to use environment variable defaults, verify the actual database URL in each service's `application.properties` before running.

## 9. Running the Application

Start each service in a separate terminal from the project root.

### Terminal 1: Food Backend

```powershell
cd food-backend-service/food-backend-service
```

Configure the database environment variables as described above, then run:

```powershell
mvn spring-boot:run
```

The service should run on port `8081`.

### Terminal 2: Nutrition Service

```powershell
cd nutrition-service/nutrition-service
```

Configure the database environment variables in this terminal as well, then run:

```powershell
mvn spring-boot:run
```

The service should run on port `8082`.

### Terminal 3: Food Frontend

```powershell
cd food-frontend-service/food-frontend-service
mvn spring-boot:run
```

The service should run on port `8080`.

Keep all three service terminals running while using the application.

Open the portal at:

**http://localhost:8080**

## 10. Testing the Services

The following URLs can be used to check the running services:

- Food items: `http://localhost:8081/api/food-items`
- Inventory batches: `http://localhost:8081/api/inventory-batches`
- Beneficiaries: `http://localhost:8082/api/beneficiaries`
- Distributions: `http://localhost:8082/api/distributions`
- Nutrition programmes: `http://localhost:8082/api/programmes`
- Inventory page: `http://localhost:8080/inventory`
- Procurement page: `http://localhost:8080/procurement`
- Distribution page: `http://localhost:8080/distribution`
- Quality and wastage page: `http://localhost:8080/quality-wastage`
- Nutrition programmes page: `http://localhost:8080/programmes`

## 11. Building the Project

Run the following command inside each individual service directory:

```powershell
mvn -DskipTests compile
```

Run this command for the Food Backend, Nutrition Service, and Food Frontend separately.

A successful compilation confirms that the source code compiles; it does not by itself confirm that database connectivity and all workflows work at runtime.

## 12. Future Improvements

- Complete nutrition value and dietary requirement management.
- Add procurement approval and purchase order workflows.
- Extend nutrition programmes with meal plans and beneficiary participation.
- Verify and strengthen inventory deduction for all wastage operations.
- Add comprehensive dashboard metrics and reports.
- Add automated integration tests, validation, and concurrency protection.
- Improve deployment configuration and service discovery.

## 13. Team Responsibilities

- **Role 1 — Lead and Integration:** Project structure, REST client integration, service integration, and documentation.
- **Role 2 — Frontend UI:** Thymeleaf pages, forms, navigation, and status display.
- **Role 3 — Backend API:** REST controllers, request mapping, and business logic.
- **Role 4 — Database and JPA:** PostgreSQL schema, entities, repositories, and persistence.

---

**Project Title:** Integrated Food Supply & Nutrition Management System

**Architecture:** Spring Boot MVC + REST Microservices + PostgreSQL
