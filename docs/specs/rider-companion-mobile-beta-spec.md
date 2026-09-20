# Rider Companion mobile beta

## Scope

The private beta is one English-only React Native/Expo application for iOS TestFlight and Android Play internal testing. It delivers rider sign-up/sign-in, biometric or device-passcode-gated session restoration, dashboard, garage, maintenance records, ride planning and checklists, profile editing, and sign-out. Riders may select an existing gallery image for a motorcycle; camera capture is excluded.

The bottom tabs are Home, Garage, Maintenance, and Rides. Profile and sign-out are reached from Home settings. The client remains online-only and stores only its mobile session in SecureStore.

## Backend contract and security

Existing web endpoints remain compatible. Mobile authentication is isolated at `/api/mobile/auth`: `POST /register`, `/login`, `/refresh`, and `/logout`. Registration/login accept credentials and non-sensitive device metadata; successful registration/login/refresh return a 15-minute access token, a rotated opaque refresh token, expiry timestamps, and a user summary.

A refresh token is `session-id.secret`; only the SHA-256 secret hash is kept in `mobile_sessions`. Refresh locks the session row, verifies the secret in constant time, rotates it, and revokes the session on mismatch, expiry, or prior revocation. Sessions last at most 30 days. The app asks the OS to authenticate the rider with biometrics and device-passcode fallback before refreshing a stored session.

Domain operations use the existing authenticated `/api/me/**` endpoints, which enforce resource ownership. Motorcycle gallery uploads use the existing private multipart endpoint and allow JPEG, PNG, or WebP below 5 MB.

## Beta environment and policy

The one API setting is `EXPO_PUBLIC_API_BASE_URL`. `APP_ENV=local` permits HTTP only in Expo Web,
emulator/simulator, and EAS `development` builds; use the development machine's LAN IP instead of
`localhost` on a physical device. `APP_ENV=preview` and `APP_ENV=production` require an HTTPS demo
hostname and contain no native cleartext exception. Use only synthetic or anonymized rider,
motorcycle, maintenance, ride, and image data. Bundle IDs, signing, EAS credentials, store access,
and the demo hostname are release prerequisites.

Excluded: account deletion, notifications, offline persistence, camera upload, localization, GPS/navigation, weather, community features, and AI.

## Acceptance matrix

Backend coverage validates registration/login, rotation, logout, invalid/replayed tokens, and ownership isolation. Mobile coverage validates forms, secure-session transitions, network/API errors, gallery validation, resource lifecycles, and checklist/status actions. Release validation on physical iOS and Android devices covers sign-in/unlock, expired sessions, interruption, accessibility, navigation, data CRUD, image access, network loss, and internal-store installation.
