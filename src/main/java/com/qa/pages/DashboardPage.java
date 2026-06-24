package com.qa.pages;

import com.qa.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

/**
 * Page Object for Dashboard/Inventory Page
 * 
 * Contains all elements and actions related to dashboard/inventory functionality
 * Extends BasePage to inherit all utility methods
 * 
 * Design Pattern: Page Object Model
 * 
 * Example Usage:
 * DashboardPage dashboardPage = new DashboardPage();
 * dashboardPage.verifyDashboardLoaded();
 * dashboardPage.getProductCount();
 * dashboardPage.addProductToCart("Sauce Labs Backpack");
 */
public class DashboardPage extends BasePage {

    // ============= Web Elements =============

    @FindBy(xpath = "//div[@class='app_logo']")
    private WebElement dashboardLogo;

    @FindBy(xpath = "//button[contains(text(), 'Open Menu')]")
    private WebElement menuButton;

    @FindBy(xpath = "//a[contains(text(), 'Logout')]")
    private WebElement logoutButton;

    @FindBy(xpath = "//span[@class='shopping_cart_badge']")
    private WebElement cartBadge;

    @FindBy(xpath = "//div[@class='inventory_item']")
    private List<WebElement> productList;

    @FindBy(xpath = "//select[@class='product_sort_container']")
    private WebElement sortDropdown;

    @FindBy(xpath = "//div[@class='inventory_item_name ']")
    private List<WebElement> productNames;

    // By locators
    private final By dashboardLogoBy = By.xpath("//div[@class='app_logo']");
    private final By cartBadgeBy = By.xpath("//span[@class='shopping_cart_badge']");
    private final By addToCartButtonBy = By.xpath("//button[contains(text(), 'Add to cart')]");

    // ============= Page Actions =============

    /**
     * Verify dashboard page is loaded
     * 
     * @return true if dashboard logo is visible
     */
    public boolean isDashboardPageLoaded() {
        logger.info("Verifying dashboard page is loaded");
        return waitForElementVisible(dashboardLogo);
    }

    /**
     * Get number of products displayed on dashboard
     * 
     * @return Number of products
     */
    public int getProductCount() {
        logger.info("Getting product count");
        return productList.size();
    }

    /**
     * Get product names displayed on dashboard
     * 
     * @return List of product names
     */
    public List<String> getProductNames() {
        logger.info("Getting product names");
        List<String> names = new java.util.ArrayList<>();
        for (WebElement productName : productNames) {
            names.add(getText(productName));
        }
        return names;
    }

    /**
     * Add product to cart by product name
     * 
     * @param productName Name of product to add
     */
    public void addProductToCart(String productName) {
        logger.info("Adding product to cart: {}", productName);
        String xpathExpression = String.format(
                "//div[contains(text(), '%s')]/ancestor::div[@class='inventory_item']//button[contains(text(), 'Add to cart')]",
                productName
        );
        click(By.xpath(xpathExpression));
    }

    /**
     * Remove product from cart by product name
     * 
     * @param productName Name of product to remove
     */
    public void removeProductFromCart(String productName) {
        logger.info("Removing product from cart: {}", productName);
        String xpathExpression = String.format(
                "//div[contains(text(), '%s')]/ancestor::div[@class='inventory_item']//button[contains(text(), 'Remove')]",
                productName
        );
        click(By.xpath(xpathExpression));
    }

    /**
     * Get cart item count
     * 
     * @return Number of items in cart
     */
    public int getCartItemCount() {
        logger.info("Getting cart item count");
        if (isElementDisplayed(cartBadge)) {
            String count = getText(cartBadge);
            return Integer.parseInt(count);
        }
        return 0;
    }

    /**
     * Click on shopping cart
     */
    public void clickShoppingCart() {
        logger.info("Clicking on shopping cart");
        click(By.xpath("//a[@class='shopping_cart_link']"));
    }

    /**
     * Logout from dashboard
     */
    public void logout() {
        logger.info("Logging out from dashboard");
        click(menuButton);
        click(logoutButton);
    }

    /**
     * Sort products by option
     * 
     * @param sortOption Sort option (e.g., "Price (low to high)")
     */
    public void sortProductsBy(String sortOption) {
        logger.info("Sorting products by: {}", sortOption);
        selectByVisibleText(sortDropdown, sortOption);
    }

    /**
     * Click on product by name
     * 
     * @param productName Product name to click
     */
    public void clickProduct(String productName) {
        logger.info("Clicking on product: {}", productName);
        String xpathExpression = String.format(
                "//div[contains(text(), '%s')]",
                productName
        );
        click(By.xpath(xpathExpression));
    }

    /**
     * Check if product is in cart
     * 
     * @param productName Product name to check
     * @return true if product has "Remove" button (meaning it's in cart)
     */
    public boolean isProductInCart(String productName) {
        logger.info("Checking if product is in cart: {}", productName);
        String xpathExpression = String.format(
                "//div[contains(text(), '%s')]/ancestor::div[@class='inventory_item']//button[contains(text(), 'Remove')]",
                productName
        );
        try {
            WebElement removeButton = driver.findElement(By.xpath(xpathExpression));
            return removeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get product price by product name
     * 
     * @param productName Product name
     * @return Product price as string
     */
    public String getProductPrice(String productName) {
        logger.info("Getting product price: {}", productName);
        String xpathExpression = String.format(
                "//div[contains(text(), '%s')]/ancestor::div[@class='inventory_item']//div[@class='inventory_item_price']",
                productName
        );
        return getText(By.xpath(xpathExpression));
    }

    /**
     * Get product description by product name
     * 
     * @param productName Product name
     * @return Product description
     */
    public String getProductDescription(String productName) {
        logger.info("Getting product description: {}", productName);
        String xpathExpression = String.format(
                "//div[contains(text(), '%s')]/ancestor::div[@class='inventory_item']//div[@class='inventory_item_desc']",
                productName
        );
        return getText(By.xpath(xpathExpression));
    }
}
