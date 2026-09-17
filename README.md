# QA Automation Test Suite

API (REST Assured + GraphQL) and UI (Playwright) test automation for the Flamingo QA Engineer
home assignment.

- Restful Booker REST API: `https://restful-booker.herokuapp.com`
- Hygraph GraphQL API (Video Streaming example schema): `https://hygraph.com/graphql-playground`
- DemoQA UI: `https://demoqa.com` — Practice Form (`/automation-practice-form`) and Web Tables
  (`/webtables`)

## Prerequisites

- Java 17+ (JDK 17 or newer)
- Maven 3.6+
- Playwright's Chromium browser (one-time install, see below)

## How to Run

```bash
# One-time: install the Playwright browser binary used by the UI tests
mvn org.codehaus.mojo:exec-maven-plugin:3.1.1:java -Dexec.mainClass=com.microsoft.playwright.CLI \
    -Dexec.classpathScope=test -Dexec.args="install chromium"

# Run all tests
mvn clean test

# Run only API tests
mvn test -Dgroups="api"

# Run only UI tests
mvn test -Dgroups="ui"

# Run UI tests headed (visible browser) instead of the default headless
mvn test -Dgroups="ui" -Dplaywright.headless=false

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
  api/                    # API test classes (JUnit 5), grouped by feature
    auth/                 # Authentication tests
    booking/              # Booking CRUD + negative tests
    graphql/              # GraphQL positive + negative tests
    ping/                 # Health check test
  ui/                     # UI test classes (JUnit 5 + Playwright), grouped by feature
    form/                 # DemoQA Practice Form tests
    webtables/            # DemoQA Web Tables tests
    pages/                # Page Object Model classes (one per page/component)
    support/              # Playwright browser lifecycle + screenshot-on-failure extension
  client/                 # REST Assured API clients (Booker, retry, Allure wiring, session reuse)
  config/                 # Environment-configurable endpoints
  factory/                # Test data factories (Booking, StudentDetails, PersonRecord)
  graphql/                # GraphQL client + reusable query constants
  model/                  # Request/response POJOs (Lombok) + enums
    ui/                   # UI test-data POJOs (StudentDetails, PersonRecord) + enums
  util/                   # Random test-data generator
docs/
  API_TEST_CASES.md      # API manual test case catalog
  API_TRACEABILITY.md    # API manual -> automated test traceability matrix
  UI_TEST_CASES.md       # UI manual test case catalog
  UI_TRACEABILITY.md     # UI manual -> automated test traceability matrix
```

## Test Strategy

- **Test design first**: every scenario started as a manual test case in
  [docs/API_TEST_CASES.md](docs/API_TEST_CASES.md) / [docs/UI_TEST_CASES.md](docs/UI_TEST_CASES.md),
  then was automated 1:1 — see [docs/API_TRACEABILITY.md](docs/API_TRACEABILITY.md) /
  [docs/UI_TRACEABILITY.md](docs/UI_TRACEABILITY.md).
- **Layered framework**: test classes never call REST Assured/GraphQL/Playwright directly; API
  tests go through `BookerApiClient` / `GraphQLClient`, UI tests go through Page Object classes
  under `ui/pages/` (DRY, single responsibility).
- **Reused API session**: `AuthSession` authenticates once and caches the token for the whole
  run instead of re-authenticating per test.
- **Test data**: `BookingFactory`/`StudentDetailsFactory`/`PersonRecordFactory` + `RandomDataUtils`
  generate randomized, unique test data per run (builder pattern via Lombok `@Builder`) so tests
  don't collide with each other or with data left behind by previous runs.
- **Respecting the shared public API**: every test creates the booking it needs and deletes it
  in an `@AfterEach`/`@AfterAll` cleanup step, so tests are independent of each other and of
  execution order, and no test data is left behind on the free public instance.
- **UI test independence**: `PlaywrightExtension` gives each test a fresh `BrowserContext`/`Page`,
  so UI tests don't need explicit cleanup — DemoQA's Web Tables data only lives in that page's
  in-memory state and disappears once the context closes.
- **Positive + negative coverage**: every feature has both a happy-path suite and a dedicated
  negative-scenario suite (invalid auth, non-existent ids, malformed payloads, GraphQL errors,
  empty UI form submission).
- **Reporting**: Allure annotations (`@Epic`/`@Feature`/`@Step`) and descriptive `@DisplayName`s
  (carrying the manual TC id) make the Allure report self-explanatory and traceable back to the
  manual test case docs. Failed UI tests automatically attach a full-page screenshot to Allure
  via `PlaywrightExtension` (a JUnit 5 `TestWatcher`).

## Challenges & Solutions

- **Public API anti-bot protection (HTTP 418)**: Restful Booker's edge rejects requests without
  a `User-Agent` header, and separately, deterministically 418s RestAssured's default
  `.accept(ContentType.JSON)` header (`application/json, application/javascript, text/javascript,
  text/json`) as a bot/tool signature. Solved by (1) always sending a `User-Agent` header, (2)
  sending a plain `Accept: application/json` header instead of RestAssured's multi-value default,
  and (3) a small `RequestRetrySupport` helper that still retries with exponential backoff as a
  defensive safety net for genuine transient `418`/`429`s.
- **Inconsistent `Content-Type` on some endpoints**: a few Restful Booker responses report
  `text/plain` even though the body is JSON, which REST Assured refuses to auto-parse. Fixed by
  registering `text/plain` as a JSON parser once (`RestAssured.registerParser`).
- **Undocumented negative-path status codes**: malformed JSON on `POST /booking` isn't documented
  and can return different 4xx codes depending on how the payload fails to parse; the test
  asserts a client-error range (`4xx`) rather than one exact code to stay resilient.
- **Shared, periodically-reset test data**: Restful Booker resets its dataset periodically, so
  tests never assume a specific booking id exists — every test creates (or is handed) the data it
  needs.
- **Restful Booker's `checkin`/`checkout` date filter is unreliable**: querying `GET /booking`
  with a booking's own exact dates can return a completely unrelated booking. The corresponding
  test only asserts the endpoint accepts the params and returns a well-formed list, without
  asserting the created booking is present.
- **DemoQA ad banners intercept clicks**: DemoQA reserves layout space for ad slots that can
  overlap real page elements (`<div ...> intercepts pointer events`), causing Playwright clicks
  to time out. Solved by injecting a small CSS rule (`pointer-events: none`) on the known ad
  slot containers right after navigating to each DemoQA page, instead of resorting to forced
  clicks that would mask real bugs elsewhere.
- **DemoQA's Web Tables has no working column sort**: the current build's `<th>` headers have no
  click handler at all (verified manually). The corresponding test documents this by asserting
  row order is unaffected by a header click, rather than asserting a sort behavior that doesn't
  exist on the live site.

## What I Would Add With More Time

- Data-driven tests via JUnit 5 `@ParameterizedTest` + `@MethodSource`/CSV sources for booking
  and student-form field variations.
- Parallel test execution (JUnit 5 `junit-platform.properties` parallel execution).
- A dedicated `docs/architecture.md` describing the client/factory/model/page-object layering in
  more depth.
- Cross-browser UI runs (Firefox/WebKit via Playwright) in addition to Chromium.
