---
description: "Review a diff/PR or file(s) in this repo against its test-automation conventions"
agent: agent
argument-hint: "Optional: files/PR to focus the review on"
---
Review ${input:target:the current changes} against this repo's conventions (see
[copilot-instructions.md](../copilot-instructions.md)). Check for:

- **Layering**: tests call only `client/` classes, never REST Assured/GraphQL directly; no HTTP
  logic leaking into `api/` test classes.
- **Traceability**: every new/changed test has a `@DisplayName("TC-<ID>: ...")`, and a matching
  entry exists in [docs/API_TEST_CASES.md](../../docs/API_TEST_CASES.md) and
  [docs/API_TRACEABILITY.md](../../docs/API_TRACEABILITY.md).
- **Annotations**: `@Tag("api")`/`@Tag("ui")` present, Allure `@Epic`/`@Feature` consistent with
  the rest of the class/package.
- **Assertions**: AssertJ (`assertThat`), not raw JUnit assertions.
- **Test data hygiene**: uses `factory/`/`util/RandomDataUtils` for unique data; doesn't hardcode
  or assume a specific booking/resource id exists.
- **Test independence**: each test creates the booking(s) it needs and deletes them in
  `@AfterEach`/`@AfterAll` - no shared mutable state or `@Order`/`@TestMethodOrder` dependencies
  between tests.
- **Negative tests**: asserts status ranges (4xx) instead of a single undocumented exact code
  where the API's behavior isn't guaranteed.
- General code quality: naming, duplication, dead code, missing edge cases.

Report findings as a short list grouped by severity (must-fix vs nit), with file/line
references. Do not modify files unless explicitly asked to fix an issue.
