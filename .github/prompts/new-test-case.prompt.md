---
description: "Design and automate a new API/GraphQL/UI test case following this repo's manual-test-first workflow"
agent: agent
argument-hint: "Describe the scenario to cover (e.g. 'PUT /booking with an expired token')"
---
Add a new test case for: ${input:scenario:the scenario to cover}

Follow the repo's test-design-first workflow (see
[copilot-instructions.md](../copilot-instructions.md)). Determine whether this is an API/GraphQL
scenario or a UI (DemoQA) scenario, then:

1. **Design first**: add a new entry to [docs/API_TEST_CASES.md](../../docs/API_TEST_CASES.md)
   (API/GraphQL) or [docs/UI_TEST_CASES.md](../../docs/UI_TEST_CASES.md) (UI) under the right
   feature section, with the next free `TC-<FEATURE>-<NNN>` id (or `-N0x` for a negative
   scenario). Include Pre/Steps/Expected, matching the existing style in that file.
2. **Trace it**: add the matching row to
   [docs/API_TRACEABILITY.md](../../docs/API_TRACEABILITY.md) or
   [docs/UI_TRACEABILITY.md](../../docs/UI_TRACEABILITY.md) pointing at the test class#method
   you're about to write.
3. **Automate it**:
   - API/GraphQL: implement the test in the right `api/<feature>/*Tests` class (create the class
     if the feature/suite doesn't exist yet, following the naming and structure of a sibling
     class such as `BookingCrudTests`). Go through the existing client in `client/` (e.g.
     `BookerApiClient`) — never call REST Assured/GraphQL directly from the test.
   - UI: implement the test in the right `ui/<feature>/*Tests` class, going through a Page
     Object under `ui/pages/` — never call Playwright locators directly from the test. Add
     `@ExtendWith(PlaywrightExtension.class)` and inject `Page` as a test parameter.
   - Use `factory/` + `util/RandomDataUtils` for test data instead of hardcoded values.
   - Add `@DisplayName("TC-<ID>: ...")`, `@Tag("api")`/`@Tag("ui")`, and reuse the class's
     `@Epic`/`@Feature` Allure annotations.
   - Use AssertJ (`assertThat`) assertions.
   - If this is a negative test, assert a status *range* rather than one exact undocumented code.
4. Run `mvn test -Dgroups="api"` or `mvn test -Dgroups="ui"` (whichever applies) and confirm the
   new test passes.


Report back which files you changed and the final `TC-<ID>`.
