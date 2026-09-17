# Flamingo QA Automation — Project Guidelines

Maven/Java 17 test-only repo (no `src/main`) automating the Restful Booker REST API, a Hygraph
GraphQL API, and DemoQA UI flows (Playwright). See [README.md](../README.md) for setup/run
commands and [docs/API_TEST_CASES.md](../docs/API_TEST_CASES.md) /
[docs/API_TRACEABILITY.md](../docs/API_TRACEABILITY.md) (API) or
[docs/UI_TEST_CASES.md](../docs/UI_TEST_CASES.md) / [docs/UI_TRACEABILITY.md](../docs/UI_TRACEABILITY.md)
(UI) for the manual-test-first workflow.

## Architecture (`src/test/java/com/flamingo/qa/`)

- `api/<feature>/` — JUnit 5 API test classes, one package per feature (`auth`, `booking`,
  `graphql`, `ping`). Positive suites end in `*CrudTests`/`*PositiveTests`; negative suites end
  in `*NegativeTests`.
- `ui/<feature>/` — JUnit 5 UI test classes (`form`, `webtables`), one package per DemoQA page.
- `ui/pages/` — Page Object Model classes (one per page/modal component). **Tests must never
  call Playwright locators directly** — always go through a page object.
- `ui/support/` — `PlaywrightExtension`: manages the browser/context/page lifecycle per test and
  attaches a screenshot to Allure on failure. Inject a `Page` by adding it as a test parameter.
- `client/` — REST Assured / GraphQL clients (`BookerApiClient`, `GraphQLClient`). **Tests must
  never call REST Assured/GraphQL directly** — always go through a client class.
- `factory/` — test-data factories (`BookingFactory`, `StudentDetailsFactory`,
  `PersonRecordFactory`) using Lombok `@Builder`.
- `model/` — request/response POJOs (Lombok) + enums; `model/ui/` holds UI test-data POJOs.
- `config/` — environment-configurable endpoints.
- `util/` — `RandomDataUtils` for unique/randomized values.

## Conventions

- Every test method needs `@DisplayName("TC-<ID>: <description>")` where `TC-<ID>` matches an
  id from the relevant test case catalog (`API_TEST_CASES.md` or `UI_TEST_CASES.md`). Add the
  manual test case there first, then the traceability row, then the automated test.
- Tag every test class `@Tag("api")` or `@Tag("ui")`, plus Allure `@Epic`/`@Feature`.
- Use `assertj` (`assertThat`), not JUnit's built-in assertions.
- Never assume a specific booking/resource id exists — Restful Booker's dataset resets
  periodically; create the booking data a test needs via `BookingFactory` in the test itself.
- Test independence: each test creates the booking(s)/record(s) it needs and deletes them in an
  `@AfterEach`/`@AfterAll` cleanup step (API) or relies on a fresh Playwright `BrowserContext`
  per test (UI) — don't share mutable state or rely on execution order between tests (no
  `@Order`/`@TestMethodOrder` for data dependencies).
- Restful Booker quirks to respect in clients (already handled in `client/`, don't bypass):
  requires a `User-Agent` header (else 418), some responses report `Content-Type: text/plain`
  for JSON bodies, and RestAssured's `.accept(ContentType.JSON)` sends a multi-value Accept
  header (`application/json, application/javascript, text/javascript, text/json`) that Restful
  Booker's anti-bot layer deterministically 418s — `BookerApiClient` sends a plain
  `Accept: application/json` header instead. `RequestRetrySupport` still retries genuine
  418/429s with backoff as a defensive safety net.
- DemoQA quirks to respect in page objects (already handled in `ui/pages/BasePage`, don't
  bypass): ad banner slots overlap real elements and intercept clicks — call
  `disableAdOverlays()` right after navigating to a DemoQA page instead of resorting to forced
  clicks. The current Web Tables build has no working column sort; don't assert sort behavior
  that doesn't exist on the live site.
- Negative tests asserting undocumented error codes should assert a status *range* (e.g. 4xx)
  rather than one exact code.

## Build and Test

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.1.1:java -Dexec.mainClass=com.microsoft.playwright.CLI \
    -Dexec.classpathScope=test -Dexec.args="install chromium"   # one-time UI browser install
mvn clean test                  # all tests
mvn test -Dgroups="api"         # API tests only
mvn test -Dgroups="ui"          # UI tests only (Playwright)
mvn allure:serve                # build + open Allure report
```
