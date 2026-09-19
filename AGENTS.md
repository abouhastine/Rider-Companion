# Repository Guidelines

## Project Structure & Module Organization

This repository contains a Spring Boot backend in `backend/rider-companion`, plus planned `web-app/` and `android-app/` clients. Backend production code is under `src/main/java/com/rider/companion`, organized by `controller`, `service`, `repository`, `entity`, `dto`, `config`, and `exception`. Tests live in `src/test/java`; configuration and Liquibase YAML changelogs live in `src/main/resources` and `src/main/resources/db/changelog`. Product specifications are in `docs/specs/`.

## Build, Test, and Development Commands

Run backend commands from `backend/rider-companion`:

- `./mvnw clean install` — compile, test, and package the application.
- `./mvnw test` — run the JUnit/Spring Boot test suite.
- `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` — start with persistent file-based H2.

Local services run on port `8081`: Swagger is `/swagger-ui.html`; H2 Console is `/h2-console`. The local H2 files are stored under `backend/rider-companion/data/` and are ignored by Git.

## Coding Style & Naming Conventions

Use Java 17 and four-space indentation. Follow the existing layered pattern: controllers handle HTTP, services contain business logic, and repositories expose Spring Data queries. Name entities `*Entity`, request payloads `*Request`, and exceptions `*NotFoundException`. Use `camelCase` fields and methods; REST paths are lowercase plural nouns, such as `/api/maintenance-records`.

Use Lombok only where established. Do not return cyclic JPA object graphs: use Jackson relationship annotations or DTOs. Keep API changes documented with the existing OpenAPI annotations.

## Database & Migration Guidelines

Liquibase owns schema changes. Add a new, forward-only YAML changeset under `src/main/resources/db/changelog/changes/` and include it from the master changelog. Keep `spring.jpa.hibernate.ddl-auto=validate`; never rely on Hibernate to create or alter production schema. Do not commit H2 database files or credentials.

## Testing Guidelines

Tests use JUnit 5, Spring Boot, and MockMvc. Name test classes `*Test` and test methods for observable behavior, e.g. `createsAndListsRidesWithoutCircularJsonReferences`. Add regression coverage for endpoints, validation, and relationship queries. Ensure cleanup deletes dependent records before parents to satisfy foreign keys.

## Commit & Pull Request Guidelines

Use short imperative commit subjects consistent with history, e.g. `implement ride lookup endpoints` or `fix H2 console proxy access`. Keep commits focused. Pull requests should describe behavior changes, list verification commands, link relevant issues, and include Swagger screenshots for API/UI-visible changes when useful.
