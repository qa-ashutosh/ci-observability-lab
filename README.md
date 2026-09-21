# ci-observability-lab

Small Selenium 4 + TestNG + Maven suite against https://www.saucedemo.com (public demo shop by Sauce Labs).
15 tests in 3 sections. **11 are expected to pass, 4 fail on purpose** (group `intentional-fail`).

Purpose: hands-on practice for CI pipelines, then observability, monitoring and telemetry on top of the test results.

| Section   | Class          | Pass | Intentional fail                |
|-----------|----------------|------|---------------------------------|
| Login     | LoginTests     | 3    | 2 (wrong title, latency budget) |
| Inventory | InventoryTests | 4    | 1 (expects 7 products)          |
| Checkout  | CheckoutTests  | 4    | 1 (missing element / timeout)   |

Requirements: JDK 17+, Maven 3.9+, Chrome (or Firefox/Edge). No manual driver setup (Selenium Manager).

## Commands
```
mvn clean test                                   # everything (build goes red: 4 failures)
mvn clean test -DexcludedGroups=intentional-fail # green run
mvn clean test -Dgroups=intentional-fail         # only the failing ones
mvn clean test -Dgroups=smoke                    # quick smoke
mvn clean test -Dbrowser=firefox -Dheadless=false
mvn clean test -Dthreads=1                       # no parallelism
mvn clean test -DrunId=build-42                  # tag log lines with your CI build id
mvn clean test -Dmaven.test.failure.ignore=true  # keep later CI steps running despite failures
```

## Output produced (for CI, reports, telemetry)
- `target/surefire-reports/TEST-*.xml`  JUnit-style XML (results, durations, failure types)
- `target/surefire-reports/*.html` and `testng-results.xml`
- `target/screenshots/*.png`            one screenshot per failed test
- stdout lines starting with `[lab]`    key=value events: TEST_START, TEST_END (status, duration_ms, error_type, groups, run_id, browser), SUITE_END

## Observability ideas to try next
- Parse the `[lab]` log lines and ship them to a log platform; chart `duration_ms` per test and failures by `error_type`.
- Publish the JUnit XML in your CI tool to get pass-rate and duration trends over time.
- Alert on the latency test (`performanceGlitchUserLogsInWithinTwoSeconds`) and on any change in pass rate for the non-intentional tests.
- Keep the failing screenshots as CI build artifacts.
