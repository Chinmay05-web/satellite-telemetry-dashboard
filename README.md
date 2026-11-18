# OrbitLink – Satellite Telemetry and Data-Priority Dashboard

## 1. Project Overview

OrbitLink is a full-stack web application developed as a prototype for the **KJS-SRS-01: AI-Based Autonomous Inter-Satellite Data Routing and Multi-Mode Amateur Radio Communication for PocketQube Missions** use case.

The application provides a centralized dashboard to monitor satellite telemetry, manage data awaiting transmission, calculate transmission priorities, simulate transmission decisions, and maintain historical records.

It uses a rule-based intelligent priority engine to recommend which data items should be transmitted first based on urgency, data type, battery level, and communication link quality.

The project also includes user authentication using JSON Web Tokens (JWT), protected REST APIs, input validation, centralized error handling, and interactive API documentation.

## 2. Project Objectives

- Monitor satellite battery levels and communication link quality.
- Store and manage satellite telemetry records.
- Maintain a queue of data items awaiting transmission.
- Calculate priority scores for queued data.
- Identify data items that should be placed on HOLD.
- Simulate data transmission decisions.
- Maintain transmission and priority evaluation history.
- Visualize telemetry and priority information through dashboard charts.
- Secure application endpoints using JWT authentication.
- Provide API documentation through Swagger UI.

## 3. Technologies Used

### Frontend
- React
- Vite
- JavaScript
- CSS
- Recharts

### Backend
- Java 25
- Spring Boot
- Spring Web
- Spring Data MongoDB
- Spring Security
- Spring Security OAuth2 Resource Server
- JWT (JSON Web Token)
- Jakarta Validation
- Maven
- Springdoc OpenAPI

### Database
- MongoDB Community Server
- MongoDB Compass

### Development Tools
- Visual Studio Code
- Git and GitHub
- Postman / Swagger UI

## 4. Main Features

1. User registration and login.
2. JWT-based authentication.
3. Protected REST API endpoints.
4. Satellite telemetry management.
5. Telemetry CRUD operations.
6. Data queue management.
7. Rule-based intelligent priority calculation.
8. Low-battery and poor-link HOLD conditions.
9. Simulated data transmission.
10. Transmission history.
11. Priority evaluation and evaluation history.
12. Dashboard analytics and charts.
13. Input validation and centralized exception handling.
14. Swagger/OpenAPI documentation with JWT authorization.

## 5. Intelligent Priority Calculation

The prototype calculates a weighted priority score using the following formula:

**Priority Score =**

(0.40 × Urgency Score) +  
(0.30 × Data Type Score) +  
(0.15 × Battery Score) +  
(0.15 × Link Quality Score)

### Urgency Scores

| Urgency | Score |
|---|---:|
| CRITICAL | 100 |
| HIGH | 75 |
| MEDIUM | 50 |
| LOW | 25 |

### Data Type Scores

| Data Type | Score |
|---|---:|
| EMERGENCY | 100 |
| SENSOR | 80 |
| IMAGE | 60 |
| HOUSEKEEPING | 30 |

### Priority Categories

| Priority Score | Priority |
|---|---|
| 80 or above | CRITICAL |
| 60 to below 80 | HIGH |
| 40 to below 60 | MEDIUM |
| Below 40 | LOW |

### HOLD Conditions

An item is assigned HOLD status if:

- Battery level is below 15%.
- Link quality is below 20%.

If neither condition is met, the weighted priority score determines the priority category.

**Note:** This is a prototype heuristic and is not a validated satellite communication policy.

## 6. Project Structure

```text
satellite-telemetry-dashboard/
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/satellite/dashboard/
│   │       │   ├── config/
│   │       │   ├── controller/
│   │       │   ├── dto/
│   │       │   ├── exception/
│   │       │   ├── model/
│   │       │   ├── repository/
│   │       │   └── service/
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
├── .gitignore
└── README.md
```

## 7. Prerequisites

Install the following software before running the project:

- Java Development Kit (JDK) 25
- Apache Maven
- Node.js 20 or later
- npm
- MongoDB Community Server
- Visual Studio Code (recommended)

Make sure Java, Maven, Node.js, and npm are available in your system PATH.

Verify the installations:

```powershell
java -version
mvn -version
node -v
npm -v
```

## 8. Database Configuration

The application uses a local MongoDB database.

**Database name:**

```text
satellite_telemetry_db
```

**MongoDB connection URI:**

```text
mongodb://localhost:27017/satellite_telemetry_db
```

The connection is configured in:

```text
backend/src/main/resources/application.properties
```

Example:

```properties
spring.application.name=satellite-dashboard
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/satellite_telemetry_db

app.jwt.secret=${JWT_SECRET}
app.jwt.expiration-minutes=60
```

Ensure MongoDB Community Server is running before starting the backend.

## 9. Running the Backend

### Step 1: Open the backend directory

```powershell
cd backend
```

### Step 2: Configure the JWT secret

The application requires a Base64-encoded secret containing at least 32 random bytes.

Generate and configure it in the same PowerShell terminal that will run Spring Boot:

```powershell
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
$rng.Dispose()
```

Do not share or commit this secret.

### Step 3: Start the Spring Boot application

```powershell
mvn spring-boot:run
```

Wait until the console displays:

```text
Started SatelliteDashboardApplication
```

**Backend URL:**

http://localhost:8080

## 10. Running the Frontend

Open a second terminal.

### Step 1: Open the frontend directory

```powershell
cd frontend
```

### Step 2: Install dependencies

```powershell
npm install
```

### Step 3: Start the development server

```powershell
npm run dev
```

**Frontend URL:**

http://localhost:5173

Open the URL in your browser. Register an account or log in to access the dashboard.

## 11. Authentication and Security

The application implements JWT-based authentication using Spring Security.

### Registration

Users can register using their name, email address, and password.

Passwords are securely hashed using BCrypt before being stored in MongoDB.

### Login

Users log in using their registered email and password.

After successful authentication, the backend generates a JWT token.

### Protected Endpoints

The frontend stores the authentication token in session storage and attaches it to protected API requests using the HTTP Authorization header:

```text
Authorization: Bearer <JWT_TOKEN>
```

The backend validates the JWT before allowing access to protected endpoints.

### Security Configuration

- Registration and login endpoints are public.
- The basic health endpoint is public.
- Swagger UI and OpenAPI documentation are publicly accessible.
- Other API endpoints require valid JWT authentication.
- Invalid or missing tokens are rejected.
- Passwords are stored as BCrypt hashes.
- The JWT secret is supplied through an environment variable.

JWT expiration is configured to 60 minutes.

## 12. API Endpoints

Base URL:

```text
http://localhost:8080
```

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/register` | Register a user | Public |
| POST | `/api/auth/login` | Authenticate a user | Public |

### Health

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/health` | Check backend health | Public |
| GET | `/api/health/database` | Check database connectivity | JWT |

### Telemetry

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/telemetry` | Create telemetry |
| GET | `/api/telemetry` | Retrieve all telemetry |
| GET | `/api/telemetry/{id}` | Retrieve telemetry by ID |
| GET | `/api/telemetry/satellite/{satellite