# Rider Companion Web App

Responsive React 18 + TypeScript frontend for Rider Companion. It uses mock data and client-side form validation only; it does not yet call the Spring Boot API or persist edits.

## Start

```bash
npm install
cp .env.example .env
npm run dev
```

The app runs on the Vite URL shown in the terminal. Set `VITE_API_BASE_URL` to the backend base URL (default: `http://localhost:8081`) for the future API integration.

## Commands

```bash
npm run build
npm run lint
npm run test
npm run coverage
npm run format:check
```

## Architecture

`src/features` keeps each product area and its forms together; `components` contains reusable UI primitives; `layouts` owns authenticated and authentication shells; `routes` owns route composition; `services/apiClient.ts` provides the future Axios API boundary. The supplied `mokcups/` directory is the visual reference and is not bundled at runtime.
