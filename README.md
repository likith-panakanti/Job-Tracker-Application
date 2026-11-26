# JobTrackr

A job application tracker with a Kanban board. Move applications through Wishlist, Applied, Interview, Offer and Rejected.

**Stack:** Java 21, Spring Boot 3.3 (REST, Spring Data JPA, validation, H2) and Angular 18 (standalone components, signals, new control flow).

## Structure

```
backend/    Spring Boot REST API (Maven)
  src/main/java/com/jobtrackr/
    model/        JobApplication entity, Status enum
    repository/   Spring Data JPA repository
    controller/   REST endpoints under /api/applications
    config/       CORS and validation error handling
  src/test/       MockMvc tests
frontend/   Angular app
  src/app/        app component, job service, models
```

## Run it

You need JDK 21, Maven and Node 18.19+ (Node 20 or 22 recommended).

**1. Backend** (http://localhost:8080)

```bash
cd backend
mvn spring-boot:run
mvn test          # run the API tests
```

**2. Frontend** (http://localhost:4200), in a second terminal

```bash
cd frontend
npm install
npm start
```

Open http://localhost:4200. Three sample applications are added on first start. Data lives in memory (H2), so it resets when the backend restarts. The H2 console is at http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:jobtrackr`, user `sa`, empty password).

## API

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/applications?status=APPLIED` | List applications, optionally by status |
| GET | `/api/applications/stats` | Count per status |
| GET | `/api/applications/{id}` | One application |
| POST | `/api/applications` | Create (company and role required) |
| PUT | `/api/applications/{id}` | Replace an application |
| PATCH | `/api/applications/{id}/status` | Move to another column: `{ "status": "INTERVIEW" }` |
| DELETE | `/api/applications/{id}` | Delete |

## Ideas for next steps

- Switch H2 to PostgreSQL and add Flyway migrations
- Drag and drop between columns (Angular CDK)
- Login with Spring Security and JWT, so each user has their own board
- Reminders for follow-ups, notes per application, Dockerfile and docker-compose
