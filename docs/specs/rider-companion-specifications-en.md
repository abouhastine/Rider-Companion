# Rider Companion — Functional and Technical Specifications

## 1. Project Overview

**Rider Companion** is an application designed for motorcycle riders. Its purpose is to centralize essential information related to the rider profile, motorcycles, maintenance records, and ride preparation.

The first version must provide a reusable technical and functional foundation that can be extended after the internship.

The scope is limited to:

- a **Web App**;
- an **Android App**;
- a shared **REST API**;
- a centralized database;
- a **Dashboard**;
- a **Virtual Garage**;
- a **Maintenance Logbook**;
- a simplified **Ride Planner**.

Advanced features such as GPS navigation, weather services, community features, and Artificial Intelligence are outside the scope of this first version.

---

## 2. Internship Objectives

The internship lasts approximately one month.

The objectives are:

1. Design a clean and scalable technical architecture.
2. Develop a shared **REST API** for the **Web App** and the **Android App**.
3. Allow a user to create an account and manage a rider profile.
4. Allow a user to register one or more motorcycles.
5. Allow a user to track motorcycle maintenance.
6. Display a concise **Dashboard**.
7. Allow a user to prepare a simple motorcycle ride.
8. Produce documentation that makes the project easy to continue.

---

## 3. MVP Scope

The **Minimum Viable Product (MVP)** includes:

- **Authentication & Rider Profile**;
- **Dashboard**;
- **Virtual Garage**;
- **Maintenance Logbook**;
- **Ride Planner**.

---

## 4. Target Users

The first version mainly targets:

- beginner motorcycle riders;
- A1, A2, or A license holders;
- users who own one or more motorcycles;
- riders who want to track maintenance and prepare rides.

The MVP requires only one role:

- `USER`.

An `ADMIN` role may be added later.

---

## 5. Platforms

### 5.1 Web App

The **Web App** must be responsive and usable on:

- desktop;
- tablet;
- mobile browsers.

It must provide all MVP features.

### 5.2 Android App

The **Android App** must provide at least:

- sign up;
- sign in;
- the **Dashboard**;
- motorcycle management;
- maintenance record management;
- simple ride preparation.

The **Android App** must consume the same **REST API** as the **Web App**.

---

## 6. Authentication & Rider Profile Module

### 6.1 Sign Up

The user must be able to create an account with:

- first name;
- last name;
- email address;
- password;
- password confirmation.

### 6.2 Sign In

The user must be able to sign in with:

- email address;
- password.

### 6.3 Sign Out

The user must be able to sign out from both the **Web App** and the **Android App**.

### 6.4 Rider Profile

The user must be able to provide:

- first name;
- last name;
- license type;
- license year;
- experience level;
- primary usage;
- estimated annual distance.

Allowed values:

#### License Type

- `A1`
- `A2`
- `A`

#### Experience Level

- `BEGINNER`
- `INTERMEDIATE`
- `EXPERIENCED`

#### Primary Usage

- `COMMUTING`
- `LEISURE`
- `TRAVEL`
- `MIXED`

### 6.5 Business Rules

- An email address can only be linked to one account.
- The password must contain at least eight characters.
- An authenticated user can only access their own data.
- An expired session must redirect the user to the sign-in screen.

---

## 7. Dashboard Module

The **Dashboard** is the main screen displayed after sign-in.

### 7.1 Displayed Information

#### User Summary

- first name;
- experience level;
- total number of motorcycles.

#### Primary Motorcycle

- brand;
- model;
- year;
- current mileage;
- image when available.

#### Maintenance Summary

- latest completed maintenance record;
- next planned maintenance;
- number of upcoming maintenance records;
- number of overdue maintenance records.

#### Statistics

- total number of maintenance records;
- total maintenance cost;
- number of planned rides;
- total estimated ride distance.

#### Quick Actions

- `Add Motorcycle`
- `Add Maintenance Record`
- `Plan Ride`
- `Open Garage`

### 7.2 Empty States

When no motorcycle is registered:

> You have not added a motorcycle yet. Add your first motorcycle to get started.

When no maintenance record exists:

> No maintenance record has been added yet.

### 7.3 Android Layout

On Android, the **Dashboard** may use vertically stacked cards.

The **Bottom Navigation** may contain:

- `Home`
- `Garage`
- `Maintenance`
- `Rides`

---

## 8. Virtual Garage Module

### 8.1 Motorcycle List

The user must be able to view all registered motorcycles.

Each list item must display:

- brand;
- model;
- year;
- mileage;
- image;
- primary motorcycle status.

### 8.2 Add Motorcycle

Required and optional fields:

- brand;
- model;
- year;
- engine capacity;
- power;
- fuel type;
- optional registration number;
- optional purchase date;
- current mileage;
- optional average fuel consumption;
- optional image;
- primary motorcycle status.

### 8.3 Edit Motorcycle

The user must be able to edit motorcycle data.

### 8.4 Delete Motorcycle

Deletion must require confirmation.

> Are you sure you want to delete this motorcycle? Related data may also be deleted.

A future version may replace deletion with an `Archive Motorcycle` feature.

### 8.5 Primary Motorcycle

- Only one primary motorcycle is allowed per user.
- Selecting a new primary motorcycle automatically removes the status from the previous one.

### 8.6 Update Mileage

Mileage must be updatable from:

- the motorcycle details page;
- the **Dashboard**.

### 8.7 Business Rules

- Mileage must be greater than or equal to zero.
- The manufacturing year must be valid.
- Engine capacity must be greater than zero.
- A motorcycle must belong to a user.
- A user can only access and edit their own motorcycles.

---

## 9. Maintenance Logbook Module

### 9.1 Maintenance Record List

Each record must display:

- type;
- date;
- mileage;
- cost;
- status;
- service provider.

### 9.2 Maintenance Types

- `OIL_CHANGE`
- `OIL_FILTER`
- `AIR_FILTER`
- `CHAIN_KIT`
- `CHAIN_LUBRICATION`
- `CHAIN_TENSION`
- `TIRES`
- `BRAKE_PADS`
- `BRAKE_FLUID`
- `COOLANT`
- `SPARK_PLUGS`
- `BATTERY`
- `GENERAL_SERVICE`
- `OTHER`

### 9.3 Add Completed Maintenance Record

Fields:

- motorcycle;
- maintenance type;
- completion date;
- mileage;
- cost;
- service provider;
- notes;
- next due date;
- next due mileage.

### 9.4 Schedule Maintenance

Fields:

- maintenance type;
- planned date;
- planned mileage;
- comment.

### 9.5 Maintenance Status

- `COMPLETED`
- `PLANNED`
- `OVERDUE`

A maintenance record becomes `OVERDUE` when:

- the planned date has passed;
- or the planned mileage has been reached or exceeded.

### 9.6 Alerts

The **Dashboard** must display:

- maintenance planned within the next 30 days;
- maintenance due within the next 1,000 kilometers;
- overdue maintenance.

**Push Notifications** are outside the MVP scope.

### 9.7 Maintenance Costs

The application must calculate:

- total cost for one motorcycle;
- total cost for the full garage;
- average cost per maintenance record.

### 9.8 Business Rules

- A maintenance record must belong to a motorcycle.
- The motorcycle must belong to the authenticated user.
- Cost must be greater than or equal to zero.
- A completion date must not be in the future.
- An inconsistent mileage value must trigger a warning.

---

## 10. Ride Planner Module

This module does not provide GPS navigation.

### 10.1 Create Ride

Fields:

- title;
- motorcycle;
- date;
- departure time;
- departure location;
- destination;
- estimated distance;
- estimated duration;
- ride type;
- highway allowed;
- toll roads allowed;
- planned breaks;
- notes.

### 10.2 Ride Types

- `LEISURE_RIDE`
- `COMMUTE`
- `ROAD_TRIP`
- `BUSINESS_TRIP`
- `GROUP_RIDE`
- `OTHER`

### 10.3 Ride Checklist

A standard checklist must be generated:

- check tire pressure;
- check fuel level;
- check the chain;
- check lights;
- check the weather;
- take vehicle documents;
- take the phone;
- bring water;
- check riding gear.

Each checklist item can be checked or unchecked.

### 10.4 Ride Status

- `DRAFT`
- `PLANNED`
- `COMPLETED`
- `CANCELLED`

### 10.5 Complete Ride

Optional fields after completing a ride:

- actual distance;
- actual duration;
- fuel cost;
- comment;
- rating from one to five.

### 10.6 Ride Views

The user must be able to view:

- upcoming rides;
- past rides;
- cancelled rides.

### 10.7 Business Rules

- A ride must be associated with a motorcycle.
- The motorcycle must belong to the user.
- Distance must be positive.
- A date is required for a `PLANNED` ride.
- Deleting a `COMPLETED` ride must require confirmation.

---

## 11. Navigation

### 11.1 Web App Navigation

- `Dashboard`
- `Garage`
- `Maintenance`
- `Rides`
- `Profile`
- `Sign Out`

### 11.2 Android Navigation

**Bottom Navigation**:

- `Home`
- `Garage`
- `Maintenance`
- `Rides`

The rider profile may be accessed from the **App Bar**.

---

## 12. Screens to Implement

### 12.1 Web App

- `Sign In Page`
- `Sign Up Page`
- `Dashboard Page`
- `Motorcycle List Page`
- `Add Motorcycle Page`
- `Motorcycle Details Page`
- `Edit Motorcycle Page`
- `Maintenance List Page`
- `Add Maintenance Record Page`
- `Maintenance Details Page`
- `Ride List Page`
- `Create Ride Page`
- `Ride Details Page`
- `Profile Page`

### 12.2 Android App

- `Sign In Screen`
- `Sign Up Screen`
- `Dashboard Screen`
- `Motorcycle List Screen`
- `Motorcycle Details Screen`
- `Add/Edit Motorcycle Screen`
- `Maintenance List Screen`
- `Add Maintenance Record Screen`
- `Ride List Screen`
- `Create Ride Screen`
- `Ride Details Screen`
- `Profile Screen`

---

## 13. Proposed Technical Architecture

### 13.1 Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Liquibase
- Maven
- Swagger / OpenAPI

### 13.2 Web App

- React
- TypeScript
- Vite
- React Router
- Axios or Fetch API
- React Hook Form
- Material UI

### 13.3 Android App

Recommended option:

- Kotlin
- Jetpack Compose
- Retrofit
- ViewModel
- Kotlin Coroutines
- DataStore

A local cache with Room may be added later.

### 13.4 Repository Strategy

Two approaches are possible.

#### Monorepo

```text
rider-companion/
├── backend/
├── web-app/
├── android-app/
└── docs/
```

#### Multi-repository

```text
rider-companion-backend
rider-companion-web
rider-companion-android
rider-companion-docs
```

For a short internship, the **monorepo** approach is recommended.

---

## 14. Simplified Data Model

### User

```text
id
firstName
lastName
email
passwordHash
createdAt
updatedAt
```

### RiderProfile

```text
id
userId
licenseType
licenseYear
experienceLevel
primaryUsage
estimatedAnnualDistance
```

### Motorcycle

```text
id
userId
brand
model
year
engineCapacity
power
fuelType
registrationNumber
purchaseDate
currentMileage
averageConsumption
imageUrl
primaryMotorcycle
createdAt
updatedAt
```

### MaintenanceRecord

```text
id
motorcycleId
maintenanceType
status
completionDate
plannedDate
mileage
plannedMileage
cost
serviceProvider
notes
createdAt
updatedAt
```

### Ride

```text
id
userId
motorcycleId
title
plannedDate
departureTime
departureLocation
destination
estimatedDistance
actualDistance
estimatedDuration
actualDuration
rideType
useHighway
useTolls
plannedBreaks
fuelCost
status
rating
notes
createdAt
updatedAt
```

### RideChecklistItem

```text
id
rideId
label
checked
```

---

## 15. Suggested REST API

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
GET  /api/users/me
PUT  /api/users/me
```

### Motorcycles

```http
GET    /api/motorcycles
GET    /api/motorcycles/{id}
POST   /api/motorcycles
PUT    /api/motorcycles/{id}
DELETE /api/motorcycles/{id}
PATCH  /api/motorcycles/{id}/mileage
PATCH  /api/motorcycles/{id}/primary
```

### Maintenance

```http
GET    /api/maintenance
GET    /api/motorcycles/{motorcycleId}/maintenance
GET    /api/maintenance/{id}
POST   /api/maintenance
PUT    /api/maintenance/{id}
DELETE /api/maintenance/{id}
```

### Rides

```http
GET    /api/rides
GET    /api/rides/{id}
POST   /api/rides
PUT    /api/rides/{id}
DELETE /api/rides/{id}
PATCH  /api/rides/{id}/status
PATCH  /api/rides/{rideId}/checklist/{itemId}
```

### Dashboard

```http
GET /api/dashboard
```

The `/api/dashboard` endpoint should return an aggregated view.

---

## 16. Dashboard Response Example

```json
{
  "user": {
    "firstName": "Aymen",
    "experienceLevel": "BEGINNER"
  },
  "motorcyclesCount": 1,
  "primaryMotorcycle": {
    "id": 1,
    "brand": "Triumph",
    "model": "Tiger 900 GT Pro",
    "year": 2025,
    "currentMileage": 3500,
    "imageUrl": null
  },
  "maintenance": {
    "lastMaintenance": {
      "type": "GENERAL_SERVICE",
      "date": "2026-02-01",
      "mileage": 1000
    },
    "upcomingCount": 2,
    "overdueCount": 0
  },
  "statistics": {
    "maintenanceTotalCost": 320.50,
    "maintenanceCount": 4,
    "plannedRidesCount": 2,
    "estimatedRideDistance": 420
  }
}
```

---

## 17. Non-functional Requirements

### 17.1 Security

- Passwords must never be stored in plain text.
- BCrypt must be used for password hashing.
- All business endpoints must be protected.
- Data ownership must be checked on the backend.
- Inputs must be validated on both client and server.
- Internal errors must not be exposed.
- Secrets must be stored in environment variables.

### 17.2 Code Quality

- Use Git.
- Define a branch naming convention.
- Use explicit commit messages.
- Separate `Controller`, `Service`, `Repository`, `DTO`, and `Entity`.
- Centralize error handling.
- Add `README` files.
- Configure a formatter and a linter.

### 17.3 Responsive Design

The **Web App** must work on desktop, tablet, and mobile devices.

### 17.4 Performance

- Minimize the number of **Dashboard** requests.
- Paginate lists when necessary.
- Limit image size.
- Avoid redundant network calls.

### 17.5 Documentation

The project must include:

- a main `README.md`;
- installation instructions;
- startup instructions;
- an architecture overview;
- **REST API** documentation;
- environment variable documentation;
- demonstration data.

---

## 18. Expected Tests

### Backend

Minimum tests:

- user registration;
- authentication;
- motorcycle creation;
- primary motorcycle update;
- maintenance status calculation;
- data ownership validation;
- ride creation;
- checklist generation;
- **Dashboard** retrieval.

### Web App

Priority tests:

- sign-in form;
- add motorcycle form;
- **Dashboard** rendering;
- form validation.

### Android App

Priority tests:

- sign in;
- **Dashboard** loading;
- motorcycle creation;
- maintenance record creation.

---

## 19. Internship Roadmap

### Week 1 — Project Setup & Authentication

Objectives:

- define user journeys;
- create simple wireframes;
- create the monorepo;
- initialize the backend;
- initialize PostgreSQL;
- configure Liquibase;
- implement sign up and sign in;
- configure Swagger / OpenAPI.

Deliverables:

- initial architecture;
- data model;
- working authentication;
- setup documentation.

### Week 2 — Virtual Garage & Dashboard

Objectives:

- implement motorcycle CRUD;
- implement the primary motorcycle rule;
- build the **Virtual Garage** in the **Web App**;
- implement the first **Dashboard**;
- connect the **Web App** to the backend;
- handle validation and errors.

Deliverables:

- working **Virtual Garage**;
- web **Dashboard**;
- tested motorcycle API.

### Week 3 — Maintenance Logbook & Android Foundation

Objectives:

- implement maintenance CRUD;
- calculate `PLANNED` and `OVERDUE` statuses;
- display maintenance alerts;
- integrate the module into the **Web App**;
- initialize the **Android App**;
- connect Android to the **REST API**;
- display the Android **Dashboard**.

Deliverables:

- working **Maintenance Logbook**;
- maintenance alerts;
- Android authentication;
- Android **Dashboard**.

### Week 4 — Ride Planner, Testing & Documentation

Objectives:

- implement the **Ride Planner**;
- generate the checklist;
- integrate rides into the **Web App**;
- implement priority Android screens;
- fix bugs;
- complete tests;
- finalize documentation;
- prepare the final demonstration.

Deliverables:

- working **Ride Planner**;
- demonstration Android App;
- final documentation;
- demonstration data;
- final project demonstration.

---

## 20. Prioritization

### Must Have

- Authentication
- Rider Profile
- Motorcycle CRUD
- Primary Motorcycle
- Dashboard
- Maintenance CRUD
- Maintenance Alerts
- Ride Planner
- Ride Checklist
- Responsive Web App
- Android App connected to the REST API
- Documentation

### Should Have

- motorcycle image;
- filters;
- search;
- simple charts;
- Dark Mode;
- Android local cache;
- Local Notifications.

### Out of Scope

- GPS navigation;
- automatic route generation;
- weather services;
- AI assistant;
- image-based mechanical diagnostics;
- crash detection;
- live GPS tracking;
- social network;
- chat;
- marketplace;
- OBD integration;
- payment;
- booking services.

---

## 21. Acceptance Criteria

The internship is considered successful when:

- a user can sign up and sign in;
- a user can register a motorcycle;
- a user can define a primary motorcycle;
- a user can view a Dashboard;
- a user can create a maintenance record;
- a user can view upcoming and overdue maintenance;
- a user can prepare a ride;
- a user can use a ride checklist;
- data is accessible from both the Web App and the Android App;
- data is properly secured;
- the project is documented;
- another developer can install and continue the project.

---

## 22. Future Enhancements

- Motorcycle Weather Score
- Smart Ride Recommendations
- GPS Route Generation
- Ride Tracking
- Push Notifications
- Expense Tracking
- Equipment Management
- Smart Maintenance Assistant
- AI Riding Assistant
- Road Trip Planner
- Community Features
- Connected Device Integrations


---


## Appendix — Target Technical Architecture (Post-Internship Vision)

This section describes the long-term target architecture. The internship is **not expected** to implement every component, but the project should be designed to support them in the future.

### Backend

- Java 21
- Spring Boot 3
- Maven
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Liquibase
- MapStruct
- Bean Validation
- OpenAPI / Swagger
- JUnit 5
- Mockito
- Testcontainers

### Web App

- React
- TypeScript
- Vite
- Material UI
- React Router
- TanStack Query
- React Hook Form
- Zod

### Android App

- Kotlin
- Jetpack Compose
- MVVM
- Hilt
- Retrofit
- Kotlin Coroutines
- DataStore

### DevOps

- Docker
- Docker Compose
- GitHub Actions
- Kubernetes
- SonarQube
- Checkstyle
- Spotless

### Architecture

- REST API
- Layered Architecture
- DTO Pattern
- Repository Pattern
- Service Layer
- Global Exception Handling
- Environment-based Configuration


### Continuous Delivery

- GitHub Actions for CI/CD pipelines;
- Docker image build and publication;
- automated deployment to Kubernetes;
- separate `development`, `staging`, and `production` environments;
- Helm may be added later for application packaging and deployment.

> **Note:** only the core technologies (Java, Spring Boot, Maven, Liquibase, PostgreSQL, React, Kotlin and Jetpack Compose) are expected during the internship. The remaining technologies belong to the post-internship roadmap.
