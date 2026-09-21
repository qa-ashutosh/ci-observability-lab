package com.example.lab.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /** Submit the form and stay on this page (use for negative tests). */
    public LoginPage submit(String user, String pass) {
        type(USERNAME, user);
        type(PASSWORD, pass);
        click(LOGIN_BUTTON);
        return this;
    }

    /** Submit the form and wait for the products page (use for happy path). */
    public InventoryPage loginAs(String user, String pass) {
        submit(user, pass);
        return new InventoryPage(driver).waitUntilLoaded();
    }

    public String errorText() {
        return text(ERROR);
    }
}
