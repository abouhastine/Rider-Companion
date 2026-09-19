# Guide: replace frontend mock data with real CRUD API calls

This guide explains the pattern introduced by commit `b37a93b` (`security and remove mocks/call backend`) and how to apply it to the remaining features.

The goal is not only to replace an imported array with an HTTP call. A complete migration must connect this whole chain:

```text
React page
  -> React Query query/mutation
  -> typed function in services/api.ts
  -> Axios client with Bearer token
  -> authenticated /api/me/... controller
  -> service with current-user ownership checks
  -> repository/database
```

At the time of this commit:

| Feature                  | Real API status                                 |
| ------------------------ | ----------------------------------------------- |
| Sign up, login, logout   | Connected                                       |
| Profile read/update      | Connected                                       |
| Dashboard                | Connected                                       |
| Garage/motorcycles       | Connected, including CRUD and image upload/read |
| Maintenance              | Still uses `mockData.ts`                        |
| Rides and ride checklist | Still use `mockData.ts`                         |

## 1. What the latest commit changed

### Backend

Authentication is now stateless:

- `JwtService` issues and verifies signed JWT access tokens.
- `JwtAuthenticationFilter` reads `Authorization: Bearer <token>` and puts the user ID in the Spring Security context.
- `SecurityConfig` protects `/api/me/**`, allows the Vite origin, and leaves sign-up/login and developer tools public.
- Logout revokes the token by storing its `jti` in `revoked_tokens`.

The authenticated user API was added under `CurrentUserController`:

| Method   | Path                               | Purpose                           |
| -------- | ---------------------------------- | --------------------------------- |
| `GET`    | `/api/me/profile`                  | Read the signed-in user's profile |
| `PUT`    | `/api/me/profile`                  | Update the profile                |
| `GET`    | `/api/me/dashboard`                | Read the user's dashboard data    |
| `GET`    | `/api/me/motorcycles`              | List the user's motorcycles       |
| `POST`   | `/api/me/motorcycles`              | Create a motorcycle for the user  |
| `GET`    | `/api/me/motorcycles/{id}`         | Read one owned motorcycle         |
| `PUT`    | `/api/me/motorcycles/{id}`         | Update one owned motorcycle       |
| `PATCH`  | `/api/me/motorcycles/{id}/primary` | Change the primary flag           |
| `PATCH`  | `/api/me/motorcycles/{id}/mileage` | Change mileage                    |
| `DELETE` | `/api/me/motorcycles/{id}`         | Delete an owned motorcycle        |
| `GET`    | `/api/me/motorcycles/{id}/image`   | Read its image                    |
| `PUT`    | `/api/me/motorcycles/{id}/image`   | Upload/replace its image          |
| `DELETE` | `/api/me/motorcycles/{id}/image`   | Delete its image                  |

`CurrentUserService` is the important security layer. It does not trust a user ID sent by the browser. It takes the authenticated user ID from Spring Security, forces that ID during creation, and calls `owned(userId, id)` before reading, updating, or deleting a motorcycle.

The commit also added response DTOs. `MotorcycleResponse` sends the fields the UI needs and avoids returning a cyclic JPA graph. This is the preferred approach for the remaining features as well.

Liquibase changes create the revoked-token and motorcycle-image tables. Any future schema change must follow the same forward-only Liquibase pattern; do not change `ddl-auto=validate`.

### Frontend

The frontend now has three integration layers:

1. `services/session.ts` stores the session and token in `sessionStorage`.
2. `services/apiClient.ts` defines the backend base URL, adds the Bearer token to every request, and normalizes API errors.
3. `services/api.ts` defines typed, feature-level API functions such as `meApi.motorcycles()` and `meApi.createMotorcycle()`.

React Query is provided once in `App.tsx`. Pages use:

- `useQuery` for reads;
- `useMutation` for create, update, delete, and upload operations;
- `invalidateQueries` after a successful mutation so all affected screens fetch fresh data.

The Garage page is the reference implementation:

```tsx
const bikes = useQuery({
  queryKey: ["motorcycles"],
  queryFn: meApi.motorcycles,
});

const remove = useMutation({
  mutationFn: meApi.deleteMotorcycle,
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ["motorcycles"] });
    queryClient.invalidateQueries({ queryKey: ["dashboard"] });
  },
});
```

The form converts frontend field names to the backend request contract, waits for the create/update request, optionally uploads an image, invalidates cached data, and navigates only after success.

## 2. Rules to follow for every migrated feature

### Never trust identity from the request body

The browser must not decide which user owns a record. The authenticated JWT decides this.

Good:

```java
public MaintenanceResponse create(
    @AuthenticationPrincipal Long userId,
    @Valid @RequestBody MaintenanceRecordRequest request) {
  return service.createForUser(userId, request);
}
```

Bad:

```java
// A browser could send another user's ID.
service.create(request.userId(), request);
```

The current motorcycle frontend sends `user: 0`, but `CurrentUserService.withOwner(...)` safely ignores/replaces it. For new authenticated request DTOs, it is cleaner to remove the user field entirely.

### Check ownership on every operation

Filtering the list is not sufficient. Read-one, update, patch, delete, nested resources, and relationship changes must all verify ownership.

For a maintenance record or ride, ownership is reached through:

```text
record -> motorcycle -> user -> id
```

Useful repository methods are:

```java
List<MaintenanceRecordEntity> findByMotorcycleUserId(Long userId);

Optional<MaintenanceRecordEntity> findByIdAndMotorcycleUserId(Long id, Long userId);

Optional<MotocycleEntity> findByIdAndUserId(Long motorcycleId, Long userId);
```

On create and update, validate that the selected motorcycle belongs to the signed-in user. On read, patch, and delete, load the record through an ownership-aware query. Returning `404` for an unowned resource is often preferable because it does not reveal whether another user's record exists; use one consistent policy.

### Return response DTOs, not JPA entities

The existing maintenance and ride controllers currently return entities. Do not use those entity shapes as the long-term frontend contract. Entities can expose fields accidentally, trigger lazy-loading queries, and create circular JSON relationships.

A response DTO should flatten relations:

```java
public record MaintenanceRecordResponse(
    Long id,
    Long motorcycleId,
    String motorcycleLabel,
    String maintenanceType,
    String status,
    LocalDate completionDate,
    LocalDate plannedDate,
    Integer mileage,
    Integer plannedMileage,
    BigDecimal cost,
    String serviceProvider,
    String notes) {}
```

For rides, use a `RideResponse` containing a list of small `RideChecklistItemResponse` objects. Do not serialize the back-reference from checklist item to ride.

### Use IDs as select values

The mocked forms store a motorcycle label such as `Yamaha Tracer 9 GT`. The API needs the database ID. The form value should therefore be a numeric motorcycle ID; the label is only presentation.

```tsx
// Conceptual shape. Adapt FormSelect if it currently accepts only string labels.
options={motorcycles.data?.map((bike) => ({
  value: bike.id,
  label: `${bike.brand} ${bike.model}`,
}))}
```

Do not try to find a motorcycle by its label. Two motorcycles may have the same brand and model.

### Model all UI states

Each page must visibly handle:

- loading;
- API failure;
- empty data;
- mutation in progress (disable duplicate submissions);
- mutation failure;
- successful refresh/navigation.

Do not silently render an empty page while a query is failing.

### Invalidate every affected query

Examples:

| Mutation                         | Queries to invalidate                                          |
| -------------------------------- | -------------------------------------------------------------- |
| Create/update/delete motorcycle  | `motorcycles`, the individual `motorcycle`, `dashboard`        |
| Create/update/delete maintenance | `maintenance-records`, individual record, `dashboard`          |
| Create/update/delete ride        | `rides`, individual ride, `dashboard`                          |
| Patch ride status/checklist      | individual ride, `rides`, `dashboard` when its counters change |

Query keys should be stable and include IDs:

```tsx
["maintenance-records"][("maintenance-record", maintenanceId)]["rides"][
  ("ride", rideId)
];
```

## 3. Repeatable backend implementation

Create separate authenticated controllers instead of continuing to grow `CurrentUserController`, for example:

```text
CurrentUserMaintenanceRecordController -> /api/me/maintenance-records
CurrentUserRideController              -> /api/me/rides
```

For each feature, implement in this order:

1. Confirm the request fields and validation rules.
2. Add a response DTO that exactly matches the frontend's needs.
3. Add repository methods that filter by `motorcycle.user.id`.
4. Add service methods that accept `userId` and enforce ownership.
5. Add authenticated controller endpoints below `/api/me`.
6. Add Liquibase changes only if the stored model must change.
7. Add security and behavior tests before connecting the UI.

Recommended CRUD shape:

| Method                | Collection path            | Item path                                |
| --------------------- | -------------------------- | ---------------------------------------- |
| List                  | `GET /api/me/{resources}`  | —                                        |
| Read                  | —                          | `GET /api/me/{resources}/{id}`           |
| Create                | `POST /api/me/{resources}` | —                                        |
| Replace editable data | —                          | `PUT /api/me/{resources}/{id}`           |
| Update one state      | —                          | `PATCH /api/me/{resources}/{id}/{state}` |
| Delete                | —                          | `DELETE /api/me/{resources}/{id}`        |

Expected response codes are `200` for reads/updates, `201` for creates, `204` for deletes, `400` for invalid input, `401` for a missing/invalid token, and `404` (or consistently `403`) for an unowned resource.

## 4. Worked next feature: Maintenance

### Backend contract

Add these authenticated endpoints:

```text
GET    /api/me/maintenance-records
POST   /api/me/maintenance-records
GET    /api/me/maintenance-records/{id}
PUT    /api/me/maintenance-records/{id}
DELETE /api/me/maintenance-records/{id}
```

The existing `MaintenanceRecordRepository.findByMotorcycleUserId(userId)` already supports the owned list. Add an owned item lookup and an owned motorcycle lookup.

Before implementation, settle one domain ambiguity: the current form calls `nextDueDate` and `nextDueMileage` fields, while the backend calls them `plannedDate` and `plannedMileage` on the same record. Decide whether those values describe this maintenance event or the next event. Keep the names and meaning consistent across DTO, database, form, and dashboard.

If the current form represents completed work, a minimal request mapping is:

```ts
const body = {
  motorcycle: values.motorcycleId,
  maintenanceType: values.type,
  status: "COMPLETED",
  completionDate: values.date,
  plannedDate: values.nextDueDate || null,
  mileage: values.mileage,
  plannedMileage: values.nextDueMileage === "" ? null : values.nextDueMileage,
  cost: values.cost,
  serviceProvider: values.provider || null,
  notes: values.notes || null,
};
```

If users must also create planned maintenance, add an explicit status/mode to the UI instead of guessing from dates.

### Frontend API layer

Add types and API functions to `web-app/src/services/api.ts`:

```ts
export type MaintenanceRecordApi = {
  id: number;
  motorcycleId: number;
  motorcycleLabel: string;
  maintenanceType: string;
  status: string;
  completionDate: string | null;
  plannedDate: string | null;
  mileage: number | null;
  plannedMileage: number | null;
  cost: number | null;
  serviceProvider: string | null;
  notes: string | null;
};

export const maintenanceApi = {
  list: () =>
    apiClient
      .get<MaintenanceRecordApi[]>("/api/me/maintenance-records")
      .then((r) => r.data),
  one: (id: number) =>
    apiClient
      .get<MaintenanceRecordApi>(`/api/me/maintenance-records/${id}`)
      .then((r) => r.data),
  create: (body: object) =>
    apiClient
      .post<MaintenanceRecordApi>("/api/me/maintenance-records", body)
      .then((r) => r.data),
  update: (id: number, body: object) =>
    apiClient
      .put<MaintenanceRecordApi>(`/api/me/maintenance-records/${id}`, body)
      .then((r) => r.data),
  remove: (id: number) => apiClient.delete(`/api/me/maintenance-records/${id}`),
};
```

Prefer a specific request type instead of `object` once the contract is stable.

### Frontend page

In `MaintenancePages.tsx`:

1. Remove the `maintenanceRecords` and `motorcycles` mock imports.
2. Fetch records with `useQuery({ queryKey: ['maintenance-records'], ... })`.
3. Fetch motorcycles with `meApi.motorcycles` for the form select.
4. Change the form field from a motorcycle label to `motorcycleId`.
5. Submit with `useMutation(maintenanceApi.create)`.
6. Disable Save while pending and display mutation errors.
7. Invalidate `maintenance-records` and `dashboard` after success.
8. Add edit and delete actions if full CRUD is required, including an edit route such as `/maintenance/:maintenanceId/edit`.
9. Add loading, error, and empty states.

Only remove Maintenance entries from `mockData.ts` after the list, create, edit, and delete flows use the API.

## 5. Then migrate Rides

Use the same structure:

```text
GET    /api/me/rides
POST   /api/me/rides
GET    /api/me/rides/{id}
PUT    /api/me/rides/{id}
PATCH  /api/me/rides/{id}/status
PATCH  /api/me/rides/{rideId}/checklist/{itemId}
DELETE /api/me/rides/{id}
```

Important Ride-specific rules:

- Use `RideRepository.findByMotorcycleUserId(userId)` for the list.
- Verify the selected motorcycle on create/update.
- Verify ride ownership before status changes.
- Verify both ride ownership and that `itemId` belongs to that ride before patching a checklist item.
- Return checklist DTOs inside `RideResponse`; do not return cyclic entities.
- Store `motorcycleId`, not the display label, in the form.
- Add real routes for view/edit, for example `/rides/:rideId` and `/rides/:rideId/edit`. The current “View ride” link incorrectly opens the new-ride page.
- Decide whether the default checklist is created by the backend when a ride is created or submitted explicitly by the frontend. Keep that rule in one place.
- Invalidate `rides`, the individual ride, and `dashboard` after relevant mutations.

When this is complete, `RidePages.tsx` should no longer import `rides`, `motorcycles`, or `checklist` from `mockData.ts`.

## 6. Tests required for each feature

### Backend tests

Add MockMvc/integration coverage for observable behavior:

- no token returns `401` for `/api/me/**`;
- a user's list contains only that user's records;
- create automatically belongs to the authenticated user;
- create/update rejects a motorcycle owned by someone else;
- read/update/patch/delete cannot access another user's record;
- invalid payloads return `400`;
- create returns `201`, delete returns `204`;
- response JSON does not contain recursive entity graphs;
- deleting dependent data respects foreign keys;
- Ride checklist updates cannot target an item from another ride.

Use two users in ownership tests. A test with only one user cannot prove isolation.

### Frontend tests

Mock the API module and wrap the page in a fresh `QueryClientProvider`. Cover:

- list data renders from the API response;
- loading, error, and empty states;
- submitted values are mapped to the backend contract;
- Save/Delete is disabled while pending;
- success invalidates the correct query and navigates when expected;
- an API failure stays on the form and displays its message.

Do not import production mock arrays in these tests. Test fixtures can be local to a test file.

## 7. Manual end-to-end check

Run the backend from `backend/rider-companion`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Run the frontend from `web-app`:

```bash
npm run dev
```

Then verify in the browser:

1. Register a new account and confirm the dashboard opens.
2. Create a motorcycle because Maintenance and Rides require one.
3. Create, list, edit, and delete the migrated record.
4. Refresh after every operation; data must remain because it came from H2, not memory.
5. Inspect the Network tab: requests must target `http://localhost:8081/api/me/...` and include a Bearer token.
6. Sign out and confirm protected pages redirect to sign-in.
7. Sign in as a second user and confirm the first user's records are not visible or editable.

Final verification commands:

```bash
cd backend/rider-companion
./mvnw test

cd ../../web-app
npm run build
npm test
npm run lint
```

## 8. Review notes from commit `b37a93b`

The reviewed commit builds and its current automated tests pass. Before extending the pattern, address or keep these points visible:

- `/api/me/**` is protected, but the older `/api/rides`, `/api/maintenance-records`, `/api/motorcycles`, and similar endpoints are still public because `SecurityConfig` permits every other request. Do not connect authenticated pages to those global endpoints. Protect, remove, or clearly reserve them for an admin role.
- The latest commit added `data/rider_companion.mv.db` and `data/rider_companion.trace.db` at repository root. Database and trace files should not be committed. Add the root `/data/` directory to `.gitignore` and remove those files from Git tracking in a dedicated cleanup commit.
- `application.properties` contains a database password. Move credentials to environment variables or an ignored local configuration before sharing or deploying the project.
- The new authenticated flows do not yet have dedicated two-user ownership regression tests. Add those before using the pattern for more resources.
- `AppRoutes.tsx` contains a stray `·` between route groups. It currently compiles, but it should be removed.
- Blob URLs created for motorcycle images should eventually be released with `URL.revokeObjectURL` when no longer used.
- On a `401`, the Axios response interceptor currently reports the error but does not clear the expired session. A later improvement can clear the session and redirect to sign-in.

## Definition of done

A feature has moved from mocks to real data only when all of the following are true:

- no production page imports that feature's data from `mockData.ts`;
- all CRUD operations persist through the backend;
- all endpoints derive the user from the JWT;
- list and item operations enforce ownership;
- frontend and backend use explicit, matching request/response types;
- loading, empty, failure, pending, and success states are handled;
- affected React Query caches are invalidated after mutations;
- backend ownership tests and frontend API-state tests pass;
- refresh and second-user manual checks prove persistence and isolation;
- Swagger/OpenAPI annotations describe the new endpoints.
