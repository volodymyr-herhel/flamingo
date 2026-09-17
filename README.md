# QA Automation Test Suite

API (REST Assured + GraphQL) and UI (Playwright, coming in Part 2) test automation for the
Flamingo QA Engineer home assignment.

- Restful Booker REST API: `https://restful-booker.herokuapp.com`
- Hygraph GraphQL API (Video Streaming example schema): `https://hygraph.com/graphql-playground`

## Prerequisites

- Java 17+ (JDK 17 or newer)
- Maven 3.6+
- Chrome browser (for the upcoming UI tests)

## How to Run

```bash
# Run all tests
mvn clean test

# Run only API tests
mvn test -Dgroups="api"

# Run only UI tests (Part 2)
mvn test -Dgroups="ui"

# Generate the Allure report
mvn allure:report
mvn allure:serve   # builds the report and opens it in a browser

# Preserve trend history between local runs (Allure "Trends" graph)
# PowerShell:
Copy-Item -Recurse target/allure-report/history target/allure-results/history -Force
mvn allure:report
```

In CI, Allure history is carried across runs automatically via the `gh-pages` branch
(see `.github/workflows/ci.yml`), so the Trends graph builds up run over run.

## Project Structure

```
src/test/java/com/flamingo/qa/
  api/                    # Test classes (JUnit 5), grouped by feature
    auth/                 # Authentication tests
    booking/              # Booking CRUD + negative tests
    graphql/              # GraphQL positive + negative tests
    ping/                 # Health check test
  client/                 # REST Assured API clients (Booker, retry, Allure wiring, session reuse)
  config/                 # Environment-configurable endpoints
  factory/                # Test data factories (Booking)
  graphql/                # GraphQL client + reusable query constants
  model/                  # Request/response POJOs (Lombok) + enums
  util/                   # Random test-data generator
docs/
  API_TEST_CASES.md      # Manual test case catalog
  API_TRACEABILITY.md    # Manual -> automated test traceability matrix
```

## Test Strategy

- **Test design first**: every scenario started as a manual test case in
  [docs/API_TEST_CASES.md](docs/API_TEST_CASES.md), then was automated 1:1 — see
  [docs/API_TRACEABILITY.md](docs/API_TRACEABILITY.md).
- **Layered framework**: test classes never call REST Assured directly; they go through
  `BookerApiClient` / `GraphQLClient`, keeping tests readable and the HTTP details reusable
  (DRY, single responsibility).
- **Reused API session**: `AuthSession` authenticates once and caches the token for the whole
  run instead of re-authenticating per test.
- **Test data**: `BookingFactory` + `RandomDataUtils` generate randomized, unique booking data
  per run (builder pattern via Lombok `@Builder`) so tests don't collide with each other or with
  data left behind by previous runs on the shared public instance.
- **Respecting the shared public API**: `BookingCrudTests` and `BookingNegativeTests` reuse a
  single booking created once per class instead of one booking per test, keeping the number of
  requests against the free public instance to a minimum.
- **Positive + negative coverage**: every feature has both a happy-path suite and a dedicated
  negative-scenario suite (invalid auth, non-existent ids, malformed payloads, GraphQL errors).
- **Reporting**: Allure annotations (`@Epic`/`@Feature`/`@Step`) and descriptive `@DisplayName`s
  (carrying the manual TC id) make the Allure report self-explanatory and traceable back to
  `docs/API_TEST_CASES.md`.

## Challenges & Solutions

- **Public API anti-bot protection (HTTP 418)**: Restful Booker's edge rejects requests without
  a `User-Agent` header and can throttle bursts of requests from the same IP with `418`/`429`.
  Solved by (1) always sending a `User-Agent` header and (2) a small `RequestRetrySupport` helper
  that retries with exponential backoff when a transient `418`/`429` is seen.
- **Inconsistent `Content-Type` on some endpoints**: a few Restful Booker responses report
  `text/plain` even though the body is JSON, which REST Assured refuses to auto-parse. Fixed by
  registering `text/plain` as a JSON parser once (`RestAssured.registerParser`).
- **Undocumented negative-path status codes**: malformed JSON on `POST /booking` isn't documented
  and can return different 4xx codes depending on how the payload fails to parse; the test
  asserts a client-error range (`4xx`) rather than one exact code to stay resilient.
- **Shared, periodically-reset test data**: Restful Booker resets its dataset periodically, so
  tests never assume a specific booking id exists — every test creates (or is handed) the data it
  needs.

## What I Would Add With More Time

- Playwright UI tests with the Page Object Model (Part 2).
- Data-driven tests via JUnit 5 `@ParameterizedTest` + `@MethodSource`/CSV sources for booking
  field variations.
- Parallel test execution (JUnit 5 `junit-platform.properties` parallel execution) once the
  shared-booking-per-class design is revisited for thread safety.
- A dedicated `docs/architecture.md` describing the client/factory/model layering in more depth.
