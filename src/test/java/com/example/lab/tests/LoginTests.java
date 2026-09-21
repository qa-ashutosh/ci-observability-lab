package com.example.lab.tests;

import com.example.lab.pages.InventoryPage;
import com.example.lab.pages.LoginPage;
import com.example.lab.support.BrowserFixture;
import com.example.lab.support.Config;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** Section 1: Login (3 pass, 2 intentional failures) */
public class LoginTests extends BrowserFixture {

    @Test(groups = "smoke", description = "Valid credentials land on the Products page")
    public void validLoginShowsProductsPage() {
        InventoryPage inventory = loginAsStandardUser();
        assertEquals(inventory.title(), "Products");
    }

    @Test(groups = "regression", description = "Wrong password shows a credentials error")
    public void wrongPasswordShowsError() {
        LoginPage login = new LoginPage(driver()).submit(Config.STANDARD_USER, "not_the_password");
        assertTrue(login.errorText().contains("do not match"), "Unexpected error: " + login.errorText());
    }

    @Test(groups = "regression", description = "Locked-out user cannot log in")
    public void lockedOutUserIsRejected() {
        LoginPage login = new LoginPage(driver()).submit(Config.LOCKED_USER, Config.PASSWORD);
        assertTrue(login.errorText().contains("locked out"), "Unexpected error: " + login.errorText());
    }

    // ---- intentional failures (exclude with -DexcludedGroups=intentional-fail) ----

    @Test(groups = "intentional-fail", description = "FAILS ON PURPOSE: wrong expected page title")
    public void loginPageTitleIsWrong() {
        assertEquals(driver().getTitle(), "Swag Labs Login");
    }

    @Test(groups = "intentional-fail", description = "FAILS ON PURPOSE: slow user breaks a 2s latency budget")
    public void performanceGlitchUserLogsInWithinTwoSeconds() {
        long start = System.nanoTime();
        new LoginPage(driver()).loginAs(Config.GLITCH_USER, Config.PASSWORD);
        long ms = (System.nanoTime() - start) / 1_000_000;
        assertTrue(ms < 2000, "Login took " + ms + " ms, budget is 2000 ms");
    }
}
