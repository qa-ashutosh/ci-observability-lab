package com.example.lab.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage extends BasePage {
    private static final By TITLE = By.cssSelector(".title");
    private static final By NAMES = By.cssSelector(".inventory_item_name");
    private static final By PRICES = By.cssSelector(".inventory_item_price");
    private static final By SORT = By.cssSelector("[data-test='product-sort-container']");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage waitUntilLoaded() {
        visible(TITLE);
        return this;
    }

    public String title() {
        return text(TITLE);
    }

    public List<String> productNames() {
        return driver.findElements(NAMES).stream().map(WebElement::getText).toList();
    }

    public List<Double> productPrices() {
        return driver.findElements(PRICES).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    /** @param value one of: az, za, lohi, hilo */
    public InventoryPage sortBy(String value) {
        new Select(visible(SORT)).selectByValue(value);
        return this;
    }

    /** @param slug e.g. "sauce-labs-backpack" */
    public InventoryPage addToCart(String slug) {
        click(By.id("add-to-cart-" + slug));
        visible(By.id("remove-" + slug)); // button flips to "Remove" once the item is in the cart
        return this;
    }

    public int cartCount() {
        List<WebElement> badge = driver.findElements(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText());
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver).waitUntilLoaded();
    }
}
