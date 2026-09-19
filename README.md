# Rider Companion 🏍️

Rider Companion est une application destinée aux motards permettant de gérer leurs motos, suivre les opérations d’entretien et préparer leurs sorties.

## 📌 Fonctionnalités MVP

- Authentification utilisateur
- Gestion du profil pilote
- Tableau de bord (Dashboard)
- Gestion des motos (Virtual Garage)
- Suivi des entretiens (Maintenance Logbook)
- Planification des sorties (Ride Planner)

## 🏗️ Architecture du projet

```text
rider-companion/
├── backend/
├── web-app/
├── mobile-app/
├── shared/
└── docs/

## Démarrer le backend en local

Le profil Spring `local` utilise une base H2 persistante sur disque, sans PostgreSQL installé :

```bash
cd backend/rider-companion
chmod +x mvnw
./mvnw clean install
bash ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

H2 Console: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)

Use the following JDBC URL in the console: `jdbc:h2:file:./data/rider_companion` (user: `sa`, password: empty).

## API documentation

When the backend is running, OpenAPI documentation is available at:

- Swagger UI: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

## Mobile beta

The iOS and Android private beta lives in `mobile-app/` and uses Expo. It covers the rider MVP only: authentication, dashboard, garage, maintenance, rides, and profile settings.

```bash
cd mobile-app
cp .env.example .env
npm install
npm run typecheck
npm test
npx expo start
```

Set `EXPO_PUBLIC_API_BASE_URL` to the supplied **HTTPS** demo API before starting or building the app. Never use production or identifiable rider data in this beta; all data and motorcycle photos must be synthetic or anonymized.

Create internal builds with `npx eas build --profile ios-beta --platform ios` for TestFlight or `npx eas build --profile android-beta --platform android` for Play internal testing. EAS/store credentials and final bundle identifiers must be configured before submission. The beta is English-only, online-only, and excludes camera upload, notifications, offline mode, localization, GPS, weather, community features, and AI.

See [the mobile-beta specification](docs/specs/rider-companion-mobile-beta-spec.md) and [implementation status](docs/specs/rider-companion-mobile-beta-implementation-status.md).
## diagramme UML
```text
+------------------+
|       User       |
+------------------+
| id               |
| firstName        |
| lastName         |
| email            |
| passwordHash     |
| createdAt        |
| updatedAt        |
+------------------+
        |
        | 1..1
        |
        v
+------------------+
|  RiderProfile    |
+------------------+
| id               |
| licenseType      |
| licenseYear      |
| experienceLevel  |
| primaryUsage     |
| estimatedAnnualDistance |
+------------------+

        |
        | 1..*
        |
        v
+------------------+
|    Motorcycle    |
+------------------+
| id               |
| brand            |
| model            |
| year             |
| engineCapacity   |
| power            |
| fuelType         |
| registrationNumber|
| purchaseDate     |
| currentMileage   |
| averageConsumption|
| imageUrl         |
| primaryMotorcycle|
+------------------+
        |
        | 1..*
        |
        +-------------------+
        |                   |
        v                   v

+------------------+   +------------------+
| MaintenanceRecord|   |       Ride       |
+------------------+   +------------------+
| id               |   | id               |
| maintenanceType  |   | title            |
| status           |   | plannedDate      |
| completionDate   |   | destination      |
| mileage          |   | estimatedDistance|
| cost             |   | rideType         |
| serviceProvider  |   | status           |
+------------------+   +------------------+
                                |
                                | 1..*
                                |
                                v
                    +----------------------+
                    | RideChecklistItem    |
                    +----------------------+
                    | id                   |
                    | label                |
                    | checked              |
                    +----------------------+
```
