Bootstrap the Rider Companion Web App in this empty repository.

Use:

- React
- TypeScript
- Vite
- Material UI
- React Router
- TanStack Query
- React Hook Form
- Zod

Use npm as the package manager.

Create a scalable feature-based architecture for the following future modules:

- Authentication
- Dashboard
- Virtual Garage
- Maintenance Logbook
- Ride Planner

Set up:

- application routing
- Material UI theme
- TanStack Query provider
- API client abstraction
- environment variables
- global error handling
- React Hook Form + Zod integration
- ESLint
- Prettier
- Vitest
- React Testing Library

Use a structure similar to:

src/
├── app/
├── components/
├── features/
│ ├── auth/
│ ├── dashboard/
│ ├── garage/
│ ├── maintenance/
│ └── rides/
├── layouts/
├── routes/
├── services/
├── hooks/
├── types/
└── utils/

Do not implement the business features yet.
Only bootstrap the technical foundation.

Add a README explaining how to install, run, test and build the application.

Tech stack:

- React
- TypeScript
- Vite
- Material UI
- React Router
- TanStack Query
- React Hook Form
- Zod

First analyze all provided Stitch screens under Rider-companion/web-app/mockups and identify:

- typography
- color palette
- spacing system
- border radius
- shadows
- layout patterns
- navigation structure
- reusable UI components

Create a reusable Material UI theme and shared design system matching the Stitch designs as closely as possible.

Then implement the reusable page templates and layouts for:

- Authentication
- Dashboard
- Virtual Garage
- Maintenance Logbook
- Ride Planner

The provided Stitch designs are the visual source of truth. Do not redesign the UI unless necessary for responsive behavior or accessibility.

Build reusable components instead of duplicating markup.

Ensure the pages are responsive for desktop, tablet and mobile.

Use mocked data initially. Do not implement backend integration or business logic yet.

Configure:

- React Router
- TanStack Query provider
- Material UI theme
- API client abstraction
- environment variables
- React Hook Form + Zod
- ESLint
- Prettier
- Vitest
- React Testing Library

Run the application, compare the rendered screens against the provided Stitch references, and iterate on the implementation until the main layouts visually match.

Document the project structure and development commands in README.md.

use the Stitch designs as the visual source of truth

Refer to the mockup and spec doc file to understand the features and the design.

Implement React html/css template first with mocked data.
