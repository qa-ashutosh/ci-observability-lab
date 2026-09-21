package com.example.lab.support;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

/**
 * Emits one key=value log line per event (easy to parse/ship to a log or telemetry backend)
 * and saves a screenshot for every failed test into target/screenshots.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult r) {
        log("TEST_START", r, "");
    }

    @Override
    public void onTestSuccess(ITestResult r) {
        log("TEST_END", r, "status=PASS");
    }

    @Override
    public void onTestFailure(ITestResult r) {
        Throwable t = r.getThrowable();
        String shot = screenshot(r);
        log("TEST_END", r, "status=FAIL error_type=" + t.getClass().getSimpleName()
                + " error=\"" + clean(t.getMessage()) + "\" screenshot=" + shot);
    }

    @Override
    public void onTestSkipped(ITestResult r) {
        log("TEST_END", r, "status=SKIP");
    }

    @Override
    public void onFinish(ITestContext ctx) {
        System.out.printf("[lab] ts=%s event=SUITE_END run_id=%s passed=%d failed=%d skipped=%d%n",
                Instant.now(), Config.RUN_ID,
                ctx.getPassedTests().size(), ctx.getFailedTests().size(), ctx.getSkippedTests().size());
    }

    private static void log(String event, ITestResult r, String extra) {
        long duration = r.getEndMillis() > 0 ? r.getEndMillis() - r.getStartMillis() : 0;
        System.out.printf("[lab] ts=%s event=%s run_id=%s browser=%s test=%s.%s groups=%s duration_ms=%d %s%n",
                Instant.now(), event, Config.RUN_ID, Config.BROWSER,
                r.getTestClass().getRealClass().getSimpleName(), r.getMethod().getMethodName(),
                String.join(",", r.getMethod().getGroups()), duration, extra);
    }

    private static String screenshot(ITestResult r) {
        try {
            WebDriver d = BrowserFixture.driver();
            if (d instanceof TakesScreenshot ts) {
                Path file = Path.of("target", "screenshots",
                        r.getTestClass().getRealClass().getSimpleName() + "_" + r.getMethod().getMethodName() + ".png");
                Files.createDirectories(file.getParent());
                Files.write(file, ts.getScreenshotAs(OutputType.BYTES));
                return file.toString();
            }
        } catch (Exception e) {
            return "failed:" + e.getClass().getSimpleName();
        }
        return "none";
    }

    private static String clean(String msg) {
        if (msg == null) return "";
        String one = msg.replace('\n', ' ').replace('\r', ' ').replace('"', '\'');
        return one.length() > 300 ? one.substring(0, 300) + "..." : one;
    }
}
