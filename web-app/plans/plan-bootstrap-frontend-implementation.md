# Bootstrap Rider Companion Web App

## Summary

Create a Vite-based React 18 + TypeScript web application in `web-app/` using npm. It will deliver a responsive, Stitch-reference-driven UI foundation for authentication, dashboard, garage, maintenance, rides, profile, and sign-out pages, using read-only mocked data and validated forms—without backend integration or persisted business behavior.

## Implementation

- Initialize Vite with strict TypeScript and add Material UI, React Router, TanStack Query, React Hook Form, Zod, Axios, ESLint, Prettier, Vitest, and React Testing Library.
- Establish the feature-based structure from the draft: `app`, `components`, `features`, `layouts`, `routes`, `services`, `hooks`, `types`, and `utils`. Keep each module’s pages, form schemas, mocks, UI components, and types co-located under its feature.
- Configure:
  - `AppProviders` for MUI theme, query client, global snackbar/error presentation, and router integration.
  - Axios API client with `VITE_API_BASE_URL`, timeout/interceptor hooks, normalized API error type, and no active endpoint calls yet.
  - `.env.example` with `VITE_API_BASE_URL=http://localhost:8081`.
  - React Hook Form’s Zod resolver for all displayed forms.
  - route-level error boundary, not-found page, and a shared error/empty/loading state component set.
- Implement app routes for `/sign-in`, `/sign-up`, `/dashboard`, `/garage`, `/garage/new`, `/garage/:motorcycleId/edit`, `/maintenance`, `/maintenance/new`, `/rides`, `/rides/new`, `/profile`, and `/sign-out`; unknown URLs render the not-found template.
- Use a temporary client-side mock navigation rule: valid sign-in/sign-up submissions navigate to Dashboard; valid add/edit form submissions navigate back to their corresponding listing pages without changing mock data; sign-out returns to Sign In. No mock authentication state or CRUD persistence is introduced.

## UI and Design System

- Audit every supplied file in `web-app/mokcups/` before implementation and encode its repeated visual rules as MUI tokens: typography scale, colors, spacing, radii, shadows, button/form variants, breakpoint behavior, and icon treatment.
- Build a shared responsive authenticated shell with desktop sidebar navigation and compact mobile/tablet navigation, matching the Stitch information hierarchy. Authentication screens use their own centered/minimal layout.
- Build reusable presentation components rather than page-specific duplicates: app shell/navigation, page header, action buttons, metric cards, motorcycle cards, status chips, section cards, empty states, tables/list rows, form fields, select/date inputs, toggle controls, confirmation dialog, and toast feedback.
- Create visually faithful page templates from the mockups:
  - Sign-in, sign-up, profile, and sign-out confirmation.
  - Dashboard with user greeting, primary motorcycle, maintenance/rides summaries, statistics, and quick actions.
  - Garage overview plus add/edit motorcycle form template.
  - Maintenance overview plus add/schedule maintenance form template.
  - Ride overview plus ride-planning form and checklist template.
- Use domain-shaped TypeScript mock data based on the specification (motorcycle, maintenance record, ride, checklist item, rider profile, dashboard summary). Include the specified statuses and select-option values so future API replacement is straightforward.
- Support desktop, tablet, and mobile layouts: multi-column dashboard/list grids collapse progressively; forms become single-column; navigation and page actions remain accessible; tables switch to card/list presentations where necessary.

## Validation, Tooling, and Documentation

- Add Zod schemas for sign-in, sign-up, profile, motorcycle, maintenance, and ride forms using the required fields and baseline client constraints in the specification. Surface errors with accessible MUI field helpers.
- Configure scripts for development, production build, linting, formatting/checking, tests, and coverage. Use ESLint flat config with TypeScript/React rules and Prettier compatibility.
- Add a `README.md` covering prerequisites, npm install, environment setup, development, tests, build, project architecture, mock-data limitation, and the expected backend base URL.
- Keep all visual source assets external to the runtime bundle unless a referenced image is explicitly needed; document `mokcups/` as the design-reference directory.

## Test Plan

- Verify application providers and each major route render without crashing.
- Test navigation shell behavior and unknown-route fallback.
- Test sign-in, motorcycle, maintenance, and ride schemas: required fields, invalid email/password, numeric bounds, invalid dates, and valid submit navigation.
- Test Dashboard rendering from mocked summaries and empty-state variants.
- Test responsive/shared components through behavior and accessible labels, not brittle pixel snapshots.
- Run `npm run lint`, `npm run test`, and `npm run build` as acceptance checks.

## Assumptions

- The existing `web-app` directory is intentionally empty; the implementation will create the frontend project there.
- The provided `mokcups` directory name is retained as-is and is the authoritative visual reference.
- This milestone implements navigable, responsive UI templates and client-side validation only; it does not call Spring endpoints, manage authentication tokens, persist mocked changes, upload images, or implement domain business rules.
- The exact MUI token values will be derived from the supplied Stitch screens during the visual audit, with accessibility and responsive behavior taking precedence where the desktop references do not specify them.
