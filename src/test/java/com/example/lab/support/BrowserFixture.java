package com.example.lab.support;

import com.example.lab.pages.InventoryPage;
import com.example.lab.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

/** Base class: fresh browser per test, thread-safe so classes can run in parallel. */
public abstract class BrowserFixture {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver driver() {
        return DRIVER.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        DRIVER.set(createDriver());
        driver().get(Config.BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        WebDriver d = DRIVER.get();
        if (d != null) {
            d.quit();
            DRIVER.remove();
        }
    }

    /** Shortcut used by tests that need a logged-in session. */
    protected InventoryPage loginAsStandardUser() {
        return new LoginPage(driver()).loginAs(Config.STANDARD_USER, Config.PASSWORD);
    }

    private static WebDriver createDriver() {
        // Selenium Manager (built into Selenium) downloads the matching driver automatically.
        switch (Config.BROWSER) {
            case "firefox": {
                FirefoxOptions o = new FirefoxOptions();
                if (Config.HEADLESS) o.addArguments("-headless");
                return new FirefoxDriver(o);
            }
            case "edge": {
                EdgeOptions o = new EdgeOptions();
                if (Config.HEADLESS) o.addArguments("--headless=new");
                o.addArguments("--window-size=1440,900");
                return new EdgeDriver(o);
            }
            default: {
                ChromeOptions o = new ChromeOptions();
                if (Config.HEADLESS) o.addArguments("--headless=new");
                o.addArguments("--window-size=1440,900", "--no-sandbox", "--disable-dev-shm-usage");
                // silence Chrome's password-manager / breach popups that block clicks in headed mode
                o.setExperimentalOption("prefs", Map.of(
                        "credentials_enable_service", false,
                        "profile.password_manager_enabled", false,
                        "profile.password_manager_leak_detection", false));
                return new ChromeDriver(o);
            }
        }
    }
}
