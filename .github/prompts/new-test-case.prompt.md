---
description: "Design and automate a new API/GraphQL test case following this repo's manual-test-first workflow"
agent: agent
argument-hint: "Describe the scenario to cover (e.g. 'PUT /booking with an expired token')"
---
Add a new test case for: ${input:scenario:the scenario to cover}

Follow the repo's test-design-first workflow (see
[copilot-instructions.md](../copilot-instructions.md)):

1. **Design first**: add a new entry to [docs/API_TEST_CASES.md](../../docs/API_TEST_CASES.md)
   under the right feature section, with the next free `TC-<FEATURE>-<NNN>` id (or `-N0x` for a
   negative scenario). Include Pre/Steps/Expected, matching the existing style in that file.
2. **Trace it**: add the matching row to
   [docs/API_TRACEABILITY.md](../../docs/API_TRACEABILITY.md) pointing at the test class#method
   you're about to write.
3. **Automate it**: implement the test in the right `api/<feature>/*Tests` class (create the
   class if the feature/suite doesn't exist yet, following the naming and structure of a
   sibling class such as `BookingCrudTests`):
   - Go through the existing client in `client/` (e.g. `BookerApiClient`) — never call REST
     Assured/GraphQL directly from the test.
   - Use `factory/` + `util/RandomDataUtils` for test data instead of hardcoded values.
   - Add `@DisplayName("TC-<ID>: ...")`, `@Tag("api")`/`@Tag("ui")`, and reuse the class's
     `@Epic`/`@Feature` Allure annotations.
   - Use AssertJ (`assertThat`) assertions.
   - If this is a negative test, assert a status *range* rather than one exact undocumented code.
4. Run `mvn test -Dgroups="api"` (or the relevant group) and confirm the new test passes.

Report back which files you changed and the final `TC-<ID>`.
