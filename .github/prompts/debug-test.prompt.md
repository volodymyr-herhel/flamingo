---
description: "Diagnose a failing/flaky test in this repo (Restful Booker 418s, Playwright/DemoQA UI flakiness, Allure, assertion failures)"
agent: agent
argument-hint: "Test class/method name or failure description"
---
Debug this failing/flaky test: ${input:failure:test name or failure description}

1. Run the specific test with `mvn test -Dtest=<ClassName>#<methodName>` and capture the
   failure/stack trace.
2. Check `target/surefire-reports/` for the detailed report if the terminal output is truncated.
3. Consider repo-specific known causes before anything else (see
   [copilot-instructions.md](../copilot-instructions.md)):
   - **HTTP 418** from Restful Booker: missing `User-Agent` header, or a non-plain `Accept`
     header (e.g. RestAssured's `.accept(ContentType.JSON)` instead of a plain
     `Accept: application/json`) - this is deterministic, not rate-limiting; log the actual
     outgoing request headers (`.log().all()`) before assuming it's a transient throttle.
   - **JSON parsing errors** on responses reported as `text/plain` — confirm
     `RestAssured.registerParser("text/plain", Parser.JSON)` is still registered.
   - **Stale/missing resource ids** — Restful Booker resets its dataset periodically; the test
     must not assume a fixed booking id survives between runs.
   - **Undocumented negative status codes** — malformed-payload tests should assert a 4xx range,
     not one exact code.
   - **Auth token issues** — check `AuthSession` caching/expiry.
   - **UI: element intercepts pointer events** — a DemoQA ad slot is overlapping the target;
     confirm `disableAdOverlays()` runs after navigation rather than adding a forced click.
   - **UI: flaky/timing issues** — re-run headed to watch it (`mvn test -Dgroups="ui"
     -Dplaywright.headless=false -Dtest=<ClassName>#<methodName>`); check the Allure report for
     the automatic failure screenshot (`PlaywrightExtension` attaches one via `TestWatcher`).
4. If it's a genuine assertion/logic bug, fix the test or the client/page-object code (whichever
   is wrong), re-run the single test, then re-run the full `api`/`ui` group to confirm no
   regression.

Summarize the root cause and the fix applied.
