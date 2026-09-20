# Mobile app setup and test plan

This document records the current mobile beta implementation, how to run it,
and the validation needed before internal distribution.

## Current implementation

The mobile client is an Expo / React Native application in `mobile-app/`. It
uses Expo Router, React Query, SecureStore, Local Authentication, and Image
Picker. The implemented user flows are:

- Registration, sign-in, secure session persistence, token refresh, biometric
  or device-passcode-gated session restoration, and sign-out.
- Dashboard, garage, maintenance, rides, and profile/settings screens.
- Motorcycle, maintenance-record, and ride create, read, update, and delete
  operations.
- Ride status updates and checklist-item toggles.
- Selecting, uploading, and removing motorcycle gallery images.

Mobile authentication uses `/api/mobile/auth/**`. Authenticated domain calls
use `/api/me/**`.

## Prerequisites

- Node.js and npm
- Java 17 to run the Spring Boot backend
- One of:
  - Android Studio and an Android emulator
  - Xcode and an iOS Simulator (macOS only)
  - A physical device with an EAS `development` client. Expo Go is useful for UI smoke checks but
    cannot validate the local native HTTP policy.

## Configure and start locally

Install the mobile dependencies:

```bash
cd mobile-app
npm ci
```

Start the backend in a separate terminal:

```bash
cd backend/rider-companion
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The backend starts on port `8081`.

Create `mobile-app/.env` for local development:

```dotenv
APP_ENV=local
EXPO_PUBLIC_API_BASE_URL=http://localhost:8081
```

Do not commit an environment-specific hostname or credentials. Restart Expo after changing the
value. `APP_ENV=local` is the only mode that permits HTTP. Preview and production require an HTTPS
URL and native builds contain no cleartext exception.

Start Metro:

```bash
cd mobile-app
npm run web
```

### Run on a physical device

1. Connect the device and development computer to the same network.
2. Change the API value to the development machine's LAN address, for example
   `http://192.168.1.42:8081`; a device cannot use `localhost` for the computer's backend.
3. Build/install the local development client, then run Metro and open the app:

```bash
npx eas build --profile development --platform android
npx eas build --profile development --platform ios
npm run start
```

An Expo tunnel serves Metro only; it does not make the HTTP API reachable.

### Run with an emulator or simulator

```bash
cd mobile-app
npm run android  # Android Studio emulator must already be available
npm run ios      # Requires macOS and Xcode
```

### Run in a browser

```bash
cd mobile-app
npm run web
```

Web is appropriate for a quick UI and navigation smoke test, but it is not a
replacement for device testing. Biometrics, SecureStore behavior, image-picker
permissions, and native alert behavior need validation on Android and iOS.

Expo Web uses `http://localhost:19007`; the backend's configurable CORS defaults include this
origin. The API URL comes only from `EXPO_PUBLIC_API_BASE_URL`.

## Automated checks

Run these before every review or beta build:

```bash
cd mobile-app
npm test
npm run typecheck
npm run lint
npm run verify:contract

cd ../backend/rider-companion
./mvnw test
```

The mobile suite covers configuration and API behavior; add user-visible flow coverage when a
screen or session behavior changes.

## Test plan

### Authentication and session

- Register with valid input and confirm the dashboard opens.
- Verify required name fields, malformed email addresses, and passwords under
  eight characters show a clear validation error.
- Sign in with valid and invalid credentials.
- Close and reopen the app; approve and reject the biometric/device-passcode
  prompt and verify the resulting session state.
- Verify access-token refresh works and expired/revoked refresh tokens return
  the rider to sign-in.
- Sign out, restart the app, and verify that the secure session is removed.

### Garage

- Create, edit, and delete a motorcycle.
- Verify required brand/model validation and numeric mileage/year handling.
- Mark and change the primary motorcycle; confirm dashboard data updates.
- Choose JPEG, PNG, and WebP gallery images, then remove an image.
- Attempt an unsupported image and a file above the backend size limit.
- Confirm image rendering works with the authenticated image endpoint.

### Maintenance

- Create planned and completed maintenance records for a motorcycle.
- Test required motorcycle selection, date formats, numeric mileage, and cost.
- Edit and delete records, verifying the list and dashboard counts refresh.
- Validate API errors for records owned by another rider or deleted elsewhere.

### Rides

- Create, edit, and delete rides with valid and incomplete forms.
- Verify list ordering by planned date.
- Change status across DRAFT, PLANNED, COMPLETED, and CANCELLED.
- Toggle each checklist item and confirm the state persists after refresh.

### Profile, resilience, and accessibility

- Update each profile field and verify it persists after restart.
- Test offline mode, backend downtime, 401 responses, 403/404 responses, and
  server validation errors; each should show a useful error state.
- Test portrait layout, keyboard behavior, text scaling, screen-reader labels,
  and tab navigation on Android and iOS.
- Build and test `development` with local HTTP on emulator/simulator and a LAN-addressed physical
  device. Then test HTTPS-only `preview` and `production` artifacts after signing and demo API
  configuration are available.

## Known items to verify or address

- The mileage action calls `Alert.prompt`, which is iOS-only. Confirm the
  Android experience and replace it with a cross-platform input flow if needed.
- The motorcycle image component supplies an empty `Authorization` header.
  Verify protected image rendering on device; a token-aware image request or
  another protected-image strategy may be required.
