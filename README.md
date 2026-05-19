# Content Insight Tool

A personal business tool I built to help with content planning for my online business. As a food relationship and body image coach, I collect weekly check-ins from clients. This tool analyses those check-ins, identifies the most common themes, and generates relevant content ideas — so my social posts, podcast topics, and downloadable resources are driven by what clients are actually struggling with that week.

## What it does

1. Accepts a batch of client check-in texts via a REST API
2. Scans each check-in for recurring themes (food guilt, all-or-nothing thinking, binge/restrict cycle, weekend overeating, emotional eating, social eating anxiety, body image struggles, and more)
3. Returns the top 3 themes with pre-mapped content suggestions
4. Calls the OpenAI API to generate additional content ideas, podcast episode ideas, and resource ideas based on those themes
5. Persists each batch, detected themes, and AI ideas to MySQL
6. Tracks trending themes across the last 4 weeks

## Tech stack

- Java 17
- Spring Boot 4
- Spring Security + JWT (jjwt 0.12.6)
- MySQL + Spring Data JPA
- OpenAI API (GPT-4.1 mini via the Responses endpoint)
- Maven

## Running locally

Set the required environment variables:

```bash
export OPENAI_API_KEY=your-openai-key
export DB_USERNAME=root
export DB_PASSWORD=your-db-password
export AUTH_PASSWORD=your-chosen-password
export JWT_SECRET=your-32-plus-character-secret
```

Then run:

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. The database `content_insight` will be created automatically on first run.

## Authentication

All endpoints except `/auth/login` require a JWT token.

**1. Get a token:**

**POST** `/auth/login`

```json
{
  "username": "carla",
  "password": "your-AUTH_PASSWORD"
}
```

Response:
```json
{
  "token": "eyJ..."
}
```

Tokens expire after 24 hours.

**2. Include the token on all subsequent requests:**

```
Authorization: Bearer <token>
```

## API

**POST** `/api/content/generate`

Request body:
```json
{
  "checkins": [
    "I felt really guilty after eating at the weekend",
    "I binged on Sunday and decided to start again on Monday",
    "Felt out of control around food after a stressful week"
  ]
}
```

Response includes a theme summary, per-theme content suggestions, and AI-generated ideas:
```json
{
  "summary": "Most common themes this week: food guilt (2), all-or-nothing thinking (1), binge/restrict cycle (1).",
  "themes": [...],
  "aiIdeas": {
    "contentIdeas": [...],
    "podcastIdeas": [...],
    "resourceIdeas": [...]
  }
}
```

---

**GET** `/themes/trending`

Returns the most frequently detected themes across the last 4 weeks.

```json
[
  { "theme": "food guilt", "count": 5 },
  { "theme": "emotional eating", "count": 3 }
]
```

## Notes

This is a personal tool built for my own use — it is not designed for general use or reuse. The theme detection is keyword-based and tailored specifically to the language my clients use.