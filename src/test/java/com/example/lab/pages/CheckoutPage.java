package com.example.lab.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/** Covers the three checkout screens: your information, overview, complete. */
public class CheckoutPage extends BasePage {
    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By FINISH = By.id("finish");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By COMPLETE_HEADER = By.cssSelector(".complete-header");
    private static final By TRACKING_NUMBER = By.id("tracking-number"); // does not exist in the app

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillInformation(String first, String last, String zip) {
        type(FIRST_NAME, first);
        type(LAST_NAME, last);
        type(POSTAL_CODE, zip);
        return this;
    }

    public CheckoutPage continueToOverview() {
        click(CONTINUE);
        return this;
    }

    public CheckoutPage finish() {
        click(FINISH);
        return this;
    }

    public String errorText() {
        return text(ERROR);
    }

    public String confirmationHeader() {
        return text(COMPLETE_HEADER);
    }

    /** Short wait on purpose: used by the intentionally failing timeout test. */
    public String trackingNumber() {
        return visible(TRACKING_NUMBER, Duration.ofSeconds(3)).getText();
    }
}
