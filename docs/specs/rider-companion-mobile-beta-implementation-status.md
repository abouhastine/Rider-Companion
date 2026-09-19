# Rider Companion mobile beta — implementation status

## Completed

- Expo/React Native workspace with Expo Router, React Query, SecureStore, Local Authentication, Image Picker, TypeScript, Jest configuration, and EAS beta profiles.
- Mobile authentication API with 15-minute JWTs, 30-day hashed rotating refresh sessions, replay revocation, non-sensitive device metadata, and Liquibase schema migration.
- Mobile Home, Garage, Maintenance, Rides, authentication, and Profile/Settings flows, including gallery image selection and ride checklist/status actions.
- Shared framework-neutral TypeScript API contracts under `shared/src`.
- Backend regression coverage for registration, refresh rotation/replay, invalid credentials, and logout revocation.

## Executed verification

- `./mvnw test -q` from `backend/rider-companion` starts Spring and applies Liquibase including `003-mobile-sessions`.

## Pending release validation

- Install dependencies and run mobile unit/type checks in `mobile-app`.
- Configure a real HTTPS demo API, EAS signing/store credentials, then complete physical-device and TestFlight/Play internal-distribution acceptance checks.
