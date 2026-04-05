# Twitch Recommendation App

A full-stack Twitch content discovery app built with Spring Boot, MySQL, and the Twitch Helix API.

The backend serves a prebuilt frontend, supports user registration and session login, stores favorites in MySQL, and returns recommendations based on either Twitch top games or a user's saved items.

## Features

- Browse top Twitch games
- Search games by name
- Fetch streams, videos, and clips for a selected game
- Register and log in with Spring Security session auth
- Save and remove favorites
- Generate personalized recommendations from favorite games

## Stack

- Java 21
- Spring Boot 4
- Spring MVC
- Spring Security
- Spring Data JDBC
- Spring Cloud OpenFeign
- OAuth2 client credentials for Twitch API access
- MySQL
- Caffeine cache
- Docker Compose

## Quick Start

### 1. Start MySQL

```bash
docker compose up -d
```

### 2. Run the application

Windows:

```bash
gradlew.bat bootRun
```

macOS or Linux:

```bash
./gradlew bootRun
```

### 3. Open the app

```text
http://localhost:8080
```

## Configuration

The application reads database settings from environment variables.

| Variable | Default | Description |
| --- | --- | --- |
| `DATABASE_URL` | `localhost` | MySQL host |
| `DATABASE_PORT` | `3306` | MySQL port |
| `DATABASE_USERNAME` | `root` | MySQL username |
| `DATABASE_PASSWORD` | `secret` | MySQL password |
| `DATABASE_INIT` | `always` | Controls schema initialization on startup |

Important notes:

- The JDBC URL is `jdbc:mysql://${DATABASE_URL}:${DATABASE_PORT}/twitch?createDatabaseIfNotExist=true`.
- `DATABASE_INIT=always` runs `database-init.sql` on startup, which drops and recreates the tables.
- JSON payloads and responses use `snake_case`.
- Twitch client credentials are currently stored in `src/main/resources/application.yml`. Those values should be overridden outside source control for shared or production environments.

## Authentication

The app uses Spring Security form login with a session cookie.

| Endpoint | Method | Auth Required | Notes |
| --- | --- | --- | --- |
| `/register` | `POST` | No | Accepts JSON |
| `/login` | `POST` | No | Expects form data |
| `/logout` | `POST` | No | Clears session |
| `/favorite` | `GET` | Yes | Returns saved items |
| `/favorite` | `POST` | Yes | Adds a favorite |
| `/favorite` | `DELETE` | Yes | Removes a favorite |

Example register request:

```bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"demo\",\"password\":\"demo123\",\"first_name\":\"Demo\",\"last_name\":\"User\"}"
```

Example login request:

```bash
curl -X POST http://localhost:8080/login -H "Content-Type: application/x-www-form-urlencoded" -d "username=demo&password=demo123" -c cookies.txt
```

## API Overview

| Endpoint | Method | Description |
| --- | --- | --- |
| `/game` | `GET` | Returns top games |
| `/game?game_name=<name>` | `GET` | Searches games by name |
| `/search?game_id=<gameId>` | `GET` | Returns grouped streams, videos, and clips for a game |
| `/recommendation` | `GET` | Returns recommendations |
| `/favorite` | `GET` | Returns grouped favorite items for the logged-in user |
| `/favorite` | `POST` | Saves an item as favorite |
| `/favorite` | `DELETE` | Removes a saved favorite |

Favorite request body:

```json
{
  "favorite": {
    "twitch_id": "123",
    "title": "Example title",
    "url": "https://example.com",
    "thumbnail_url": "https://example.com/thumb.jpg",
    "broadcaster_name": "streamer",
    "game_id": "456",
    "item_type": "VIDEO"
  }
}
```

## Recommendation Behavior

- Anonymous users get recommendations seeded from Twitch top games.
- Logged-in users without favorites get the same default recommendation flow.
- Logged-in users with favorites use up to 3 unique favorite game IDs as recommendation seeds.
- Already-favorited items are excluded from recommendation results.
- Results are grouped into `streams`, `videos`, and `clips`.

## Database

The schema is initialized from `src/main/resources/database-init.sql`.

Tables:

- `users`
- `authorities`
- `items`
- `favorite_records`

The `favorite_records` table prevents duplicate favorites for the same user and item pair.

## Repository Layout

| Path | Purpose |
| --- | --- |
| `src/main/java/com/laioffer/twitch` | Backend source code |
| `src/main/resources/application.yml` | Application configuration |
| `src/main/resources/database-init.sql` | Database schema initialization |
| `src/main/resources/public` | Prebuilt frontend assets |
| `src/test/java/com/laioffer/twitch` | Tests |
| `docker-compose.yml` | Local MySQL setup |
| `Dockerfile` | Runtime image definition |

## Build and Test

Run tests:

```bash
gradlew.bat test
```

Build the project:

```bash
gradlew.bat build
```

## Docker Image

Build the runtime image after creating the jar:

```bash
docker build -t twitch-app .
```

The `Dockerfile` expects:

```text
build/libs/twitch-0.0.1-SNAPSHOT.jar
```
