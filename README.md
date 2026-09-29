# CI Observability Lab

A hands-on **Selenium 4 + TestNG + Maven** lab built around the public [SauceDemo](https://www.saucedemo.com/) application.

The project starts with UI test automation and progressively adds **CI, test reporting, metrics, telemetry, and observability** to demonstrate how QA can evolve from test execution to actionable engineering signals.

---

## 🎯 What This Lab Demonstrates

- Selenium 4 UI automation with TestNG
- Maven-based test execution
- Test groups and selective execution
- Intentional test failures for CI scenarios
- Test result extraction and reporting
- GitHub Actions CI integration
- Prometheus / Pushgateway metrics
- Honeycomb build telemetry
- Test duration and failure analysis
- CI artifacts and failure screenshots
- Correlation of test results with CI build information

---

## 🧪 Test Suite

The suite contains **15 tests** across three functional areas.

| Section | Test Class | Expected Pass | Intentional Fail |
|---|---|---:|---:|
| Login | `LoginTests` | 3 | 2 |
| Inventory | `InventoryTests` | 4 | 1 |
| Checkout | `CheckoutTests` | 4 | 1 |
| **Total** | | **11** | **4** |

The four intentional failures belong to the `intentional-fail` TestNG group and are deliberately introduced to simulate real CI failure scenarios.

### Intentional failures

**Login**
- Incorrect page-title expectation
- Login latency exceeds the defined threshold

**Inventory**
- Expects 7 products instead of the actual count

**Checkout**
- Attempts to interact with a missing element, resulting in a timeout

This provides predictable success and failure signals for CI and observability experiments.

---

## 🛠️ Prerequisites

- JDK 17+
- Maven 3.9+
- Chrome, Firefox, or Edge

No manual WebDriver installation is required.  
**Selenium Manager** handles browser driver management automatically.

---

## 🚀 Running the Tests

### Run the complete suite

```bash
mvn clean test