# Flamingo QA Automation — Project Guidelines

Maven/Java 17 test-only repo (no `src/main`) automating the Restful Booker REST API and a
Hygraph GraphQL API. See [README.md](../README.md) for setup/run commands and
[docs/API_TEST_CASES.md](../docs/API_TEST_CASES.md) /
[docs/API_TRACEABILITY.md](../docs/API_TRACEABILITY.md) for the manual-test-first workflow.

## Architecture (`src/test/java/com/flamingo/qa/`)

- `api/<feature>/` — JUnit 5 test classes, one package per feature (`auth`, `booking`, `graphql`,
  `ping`). Positive suites end in `*CrudTests`/`*PositiveTests`; negative suites end in
  `*NegativeTests`.
- `client/` — REST Assured / GraphQL clients (`BookerApiClient`, `GraphQLClient`). **Tests must
  never call REST Assured/GraphQL directly** — always go through a client class.
- `factory/` — test-data factories (e.g. `BookingFactory`) using Lombok `@Builder`.
- `model/` — request/response POJOs (Lombok) + enums.
- `config/` — environment-configurable endpoints.
- `util/` — `RandomDataUtils` for unique/randomized values.

## Conventions

- Every test method needs `@DisplayName("TC-<ID>: <description>")` where `TC-<ID>` matches an
  id from [docs/API_TEST_CASES.md](../docs/API_TEST_CASES.md). Add the manual test case there
  first, then the traceability row in
  [docs/API_TRACEABILITY.md](../docs/API_TRACEABILITY.md), then the automated test.
- Tag every test class `@Tag("api")` or `@Tag("ui")`, plus Allure `@Epic`/`@Feature`.
- Use `assertj` (`assertThat`), not JUnit's built-in assertions.
- Never assume a specific booking/resource id exists — Restful Booker's dataset resets
  periodically; create the booking data a test needs via `BookingFactory` in the test itself.
- Test independence: each test creates the booking(s) it needs and deletes them in an
  `@AfterEach`/`@AfterAll` cleanup step — don't share mutable booking state or rely on execution
  order between tests (no `@Order`/`@TestMethodOrder` for booking data dependencies).
- Restful Booker quirks to respect in clients (already handled in `client/`, don't bypass):
  requires a `User-Agent` header (else 418), some responses report `Content-Type: text/plain`
  for JSON bodies, and RestAssured's `.accept(ContentType.JSON)` sends a multi-value Accept
  header (`application/json, application/javascript, text/javascript, text/json`) that Restful
  Booker's anti-bot layer deterministically 418s — `BookerApiClient` sends a plain
  `Accept: application/json` header instead. `RequestRetrySupport` still retries genuine
  418/429s with backoff as a defensive safety net.
- Negative tests asserting undocumented error codes should assert a status *range* (e.g. 4xx)
  rather than one exact code.

## Build and Test

```bash
mvn clean test                  # all tests
mvn test -Dgroups="api"         # API tests only
mvn test -Dgroups="ui"          # UI tests only (Part 2, Playwright)
mvn allure:serve                # build + open Allure report
```
