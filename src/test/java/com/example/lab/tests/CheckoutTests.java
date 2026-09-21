package com.example.lab.tests;

import com.example.lab.pages.CartPage;
import com.example.lab.pages.CheckoutPage;
import com.example.lab.support.BrowserFixture;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** Section 3: Cart and checkout (4 pass, 1 intentional failure) */
public class CheckoutTests extends BrowserFixture {

    private static final String BACKPACK = "sauce-labs-backpack";

    private CartPage cartWithBackpack() {
        return loginAsStandardUser().addToCart(BACKPACK).openCart();
    }

    private CheckoutPage completeOrder() {
        return cartWithBackpack()
                .startCheckout()
                .fillInformation("Ada", "Lovelace", "12345")
                .continueToOverview()
                .finish();
    }

    @Test(groups = "regression", description = "Cart shows the item that was added")
    public void cartShowsAddedItem() {
        assertEquals(cartWithBackpack().itemNames(), List.of("Sauce Labs Backpack"));
    }

    @Test(groups = "regression", description = "Removing the only item empties the cart")
    public void removingItemEmptiesCart() {
        assertTrue(cartWithBackpack().remove(BACKPACK).itemNames().isEmpty());
    }

    @Test(groups = "regression", description = "First name is required on the information step")
    public void checkoutRequiresFirstName() {
        CheckoutPage checkout = cartWithBackpack().startCheckout().continueToOverview();
        assertTrue(checkout.errorText().contains("First Name is required"), "Got: " + checkout.errorText());
    }

    @Test(groups = "smoke", description = "Full happy-path order shows the thank-you message")
    public void fullCheckoutCompletesOrder() {
        assertEquals(completeOrder().confirmationHeader(), "Thank you for your order!");
    }

    // ---- intentional failure ----

    @Test(groups = "intentional-fail", description = "FAILS ON PURPOSE: element does not exist -> TimeoutException")
    public void orderConfirmationShowsTrackingNumber() {
        assertTrue(completeOrder().trackingNumber().startsWith("TRK-"));
    }
}
