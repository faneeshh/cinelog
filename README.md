# CineLog

A Letterboxd-style film tracker built with Spring Boot. Log movies you want to watch, are currently watching, have watched, or dropped.

---

## Running Locally

**Prerequisites:** Java 21+, MySQL 8

1. Create the database:
   ```sql
   CREATE DATABASE cinelog_db;
   ```

2. Edit `cinelog/src/main/resources/application.properties` with your credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/cinelog_db
   spring.datasource.username=your_user
   spring.datasource.password=your_password
   ```

3. Start the app:
   ```bash
   cd cinelog
   ./mvnw spring-boot:run
   ```

4. Open [http://localhost:8080/movies](http://localhost:8080/movies)

The database is seeded automatically on first run via `src/main/resources/data.sql` (3 genres, 10 movies).

---

## Routes

| Method | Path | Description |
|--------|------|-------------|
| GET | `/movies` | Film list page |
| GET | `/movies/fragment/list` | HTMX fragment — filtered list (`?genreId=`) |
| POST | `/movies` | Add a new film |
| PUT | `/movies/{id}` | Update film status |
| DELETE | `/movies/{id}` | Delete a film |
| GET | `/dashboard` | Stats — totals and genre breakdown |

---

## Business Rules

**Status transitions** (forward only):
```
want_to_watch → watching → watched
any status    → dropped
```
Backwards transitions are rejected by the backend.

**Rating required** — a film cannot be marked as `watched` without a rating (1–10).
