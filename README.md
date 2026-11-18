
# Satellite Telemetry and Data-Priority Dashboard

## 1. Project Overview

The Satellite Telemetry and Data-Priority Dashboard is a full-stack prototype developed for the KJS-SRS-01 use case.

It provides a dashboard to monitor satellite telemetry, manage data awaiting transmission, recommend transmission priorities, and maintain transmission history.

The project uses a rule-based intelligent priority engine to recommend which data items should be transmitted first.

## 2. Objectives

- Monitor satellite battery level and link quality.
- Store and manage satellite telemetry records.
- Maintain a queue of data awaiting transmission.
- Calculate priority scores for queued data.
- Identify data items that should be placed on HOLD.
- Simulate data transmission.
- Maintain transmission and decision history.
- Visualize telemetry and priority information.

## 3. Technologies Used

### Frontend
- React
- Vite
- JavaScript
- CSS
- Recharts

### Backend
- Java
- Spring Boot
- Spring Web
- Spring Data MongoDB
- Jakarta Validation
- Maven

### Database
- MongoDB Community Server
- MongoDB Compass

## 4. Main Features

1. Satellite telemetry management
2. Telemetry CRUD operations
3. Data queue management
4. Intelligent priority calculation
5. Low-battery and poor-link HOLD condition
6. Simulated transmission
7. Transmission history
8. Dashboard analytics and charts
9. Input validation and error handling

## 5. Priority Calculation

The prototype calculates a weighted priority score using:

Priority Score =
(0.40 × Urgency Score) +
(0.30 × Data Type Score) +
(0.15 × Battery Score) +
(0.15 × Link Quality Score)

The priority engine uses the following urgency scores:

- CRITICAL: 100
- HIGH: 75
- MEDIUM: 50
- LOW: 25

Data type scores:

- EMERGENCY: 100
- SENSOR: 80
- IMAGE: 60
- HOUSEKEEPING: 30

If battery level is below 15 or link quality is below 20, the item is assigned HOLD status.

Otherwise, the score determines the priority category.

This is a prototype heuristic and is not a validated satellite communication policy.

## 6. Project Structure

satellite-telemetry-dashboard/
├── backend/
├── frontend/
├── .gitignore
└── README.md

## 7. Database Configuration

The application uses a local MongoDB database.

Database name:

satellite_telemetry_db

Configure the MongoDB connection in:

backend/src/main/resources/application.properties

Example:

spring.data.mongodb.uri=mongodb://localhost:27017/satellite_telemetry_db

Ensure MongoDB Community Server is running before starting the backend.

## 8. Running the Backend

Open a terminal:

cd backend

Run:

.\mvnw.cmd spring-boot:run

Backend URL:

http://localhost:8080

## 9. Running the Frontend

Open another terminal:

cd frontend

Install dependencies:

npm install

Start the development server:

npm run dev

Frontend URL:

http://localhost:5173

## 10. API Endpoints

### Health
- GET /api/health
- GET /api/health/database

### Telemetry
- POST /api/telemetry
- GET /api/telemetry
- GET /api/telemetry/{id}
- GET /api/telemetry/satellite/{satelliteId}
- PUT /api/telemetry/{id}
- DELETE /api/telemetry/{id}

### Queue
- POST /api/queue
- GET /api/queue
- GET /api/queue/{id}

### Transmission
- POST /api/transmission/{id}
- GET /api/transmission/history

## 11. Limitations

- The project simulates satellite telemetry and transmission.
- It does not communicate with actual satellites.
- The priority engine uses a predefined weighted scoring heuristic.
- Ground-pass scheduling and real radio communication are outside the current prototype scope.

## 12. Future Enhancements

- Real satellite telemetry integration.
- More advanced machine-learning-based priority prediction.
- Ground-station pass simulation.
- Communication-mode selection.
- Live telemetry streaming.
- User authentication and role-based access.

## 13. Project Status

The prototype includes telemetry CRUD, priority calculation, queue management, simulated transmission, history tracking, and dashboard visualizations.


