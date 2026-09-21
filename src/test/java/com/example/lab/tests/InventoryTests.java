package com.example.lab.tests;

import com.example.lab.pages.InventoryPage;
import com.example.lab.support.BrowserFixture;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

/** Section 2: Inventory (4 pass, 1 intentional failure) */
public class InventoryTests extends BrowserFixture {

    @Test(groups = "smoke", description = "Catalog lists six products")
    public void sixProductsAreListed() {
        InventoryPage page = loginAsStandardUser();
        assertEquals(page.productNames().size(), 6);
    }

    @Test(groups = "regression", description = "Sorting Z to A reverses the name order")
    public void productsSortByNameDescending() {
        InventoryPage page = loginAsStandardUser().sortBy("za");
        List<String> expected = new ArrayList<>(page.productNames());
        expected.sort(Collections.reverseOrder());
        assertEquals(page.productNames(), expected);
    }

    @Test(groups = "regression", description = "Sorting by price low to high is ascending")
    public void productsSortByPriceAscending() {
        InventoryPage page = loginAsStandardUser().sortBy("lohi");
        List<Double> expected = new ArrayList<>(page.productPrices());
        Collections.sort(expected);
        assertEquals(page.productPrices(), expected);
    }

    @Test(groups = "regression", description = "Cart badge counts added items")
    public void cartBadgeReflectsAddedItems() {
        InventoryPage page = loginAsStandardUser();
        page.addToCart("sauce-labs-backpack");
        assertEquals(page.cartCount(), 1);
        page.addToCart("sauce-labs-bike-light");
        assertEquals(page.cartCount(), 2);
    }

    // ---- intentional failure ----

    @Test(groups = "intentional-fail", description = "FAILS ON PURPOSE: expects 7 products but the app has 6")
    public void sevenProductsAreListed() {
        assertEquals(loginAsStandardUser().productNames().size(), 7, "Catalog size mismatch");
    }
}
