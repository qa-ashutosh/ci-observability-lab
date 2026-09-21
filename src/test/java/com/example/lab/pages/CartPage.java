package com.example.lab.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {
    private static final By TITLE = By.cssSelector(".title");
    private static final By ITEM = By.cssSelector(".cart_item");
    private static final By ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    private static final By CHECKOUT = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage waitUntilLoaded() {
        visible(TITLE);
        return this;
    }

    public List<String> itemNames() {
        return driver.findElements(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public CartPage remove(String slug) {
        click(By.id("remove-" + slug));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(ITEM));
        return this;
    }

    public CheckoutPage startCheckout() {
        click(CHECKOUT);
        return new CheckoutPage(driver);
    }
}
