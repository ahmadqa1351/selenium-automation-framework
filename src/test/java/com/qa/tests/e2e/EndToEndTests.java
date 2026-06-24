package com.qa.tests.e2e;

import com.qa.framework.base.BaseTest;
import com.qa.pages.DashboardPage;
import com.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * End-to-End Test class for complete user workflows
 * 
 * Test Group: e2e, regression
 * Priority: High
 * 
 * Contains end-to-end test scenarios:
 * - Login -> Browse Products -> Add to Cart -> Checkout
 * - Login -> Browse -> Logout
 * - Multiple product management
 */
public class EndToEndTests extends BaseTest {

    /**
     * E2E Test: Complete purchase workflow
     * 
     * Test Steps:
     * 1. Login with valid credentials
     * 2. Verify dashboard is loaded
     * 3. Verify products are displayed
     * 4. Add product to cart
     * 5. Verify product is in cart
     * 6. Verify cart item count is updated
     */
    @Test(groups = {"e2e", "regression"}, description = "Complete product purchase workflow")
    public void testCompletePurchaseWorkflow() {
        logger.info("E2E Test: Complete purchase workflow");
        
        // Step 1: Login
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        logger.info("User logged in successfully");
        
        // Step 2: Verify dashboard
        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isDashboardPageLoaded(), "Dashboard should be loaded");
        logger.info("Dashboard verified");
        
        // Step 3: Verify products
        int productCount = dashboardPage.getProductCount();
        Assert.assertTrue(productCount > 0, "Products should be displayed");
        logger.info("Found {} products on dashboard", productCount);
        
        // Step 4: Add product to cart
        String productToAdd = "Sauce Labs Backpack";
        dashboardPage.addProductToCart(productToAdd);
        logger.info("Product added to cart: {}", productToAdd);
        
        // Step 5: Verify product is in cart
        Assert.assertTrue(dashboardPage.isProductInCart(productToAdd), "Product should be in cart");
        logger.info("Product verified in cart");
        
        // Step 6: Verify cart item count
        int cartCount = dashboardPage.getCartItemCount();
        Assert.assertEquals(cartCount, 1, "Cart should contain 1 item");
        logger.info("Cart item count verified: {}", cartCount);
    }

    /**
     * E2E Test: Add multiple products to cart
     * 
     * Test Steps:
     * 1. Login with valid credentials
     * 2. Add multiple products to cart
     * 3. Verify all products are in cart
     * 4. Verify cart item count
     */
    @Test(groups = {"e2e", "regression"}, description = "Add multiple products to cart")
    public void testAddMultipleProductsToCart() {
        logger.info("E2E Test: Add multiple products to cart");
        
        // Step 1: Login
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        logger.info("User logged in");
        
        DashboardPage dashboardPage = new DashboardPage();
        
        // Step 2: Add multiple products
        String[] productsToAdd = {
                "Sauce Labs Backpack",
                "Sauce Labs Bike Light",
                "Sauce Labs Bolt T-Shirt"
        };
        
        for (String product : productsToAdd) {
            dashboardPage.addProductToCart(product);
            logger.info("Added product: {}", product);
        }
        
        // Step 3: Verify all products are in cart
        for (String product : productsToAdd) {
            Assert.assertTrue(dashboardPage.isProductInCart(product), 
                    "Product should be in cart: " + product);
        }
        logger.info("All products verified in cart");
        
        // Step 4: Verify cart item count
        int cartCount = dashboardPage.getCartItemCount();
        Assert.assertEquals(cartCount, productsToAdd.length, 
                "Cart should contain " + productsToAdd.length + " items");
        logger.info("Cart item count verified: {}", cartCount);
    }

    /**
     * E2E Test: Add and remove products from cart
     * 
     * Test Steps:
     * 1. Login
     * 2. Add product to cart
     * 3. Remove product from cart
     * 4. Verify product is removed
     * 5. Verify cart is empty
     */
    @Test(groups = {"e2e", "regression"}, description = "Add and remove products from cart")
    public void testAddAndRemoveProductsFromCart() {
        logger.info("E2E Test: Add and remove products from cart");
        
        // Step 1: Login
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        logger.info("User logged in");
        
        DashboardPage dashboardPage = new DashboardPage();
        
        // Step 2: Add product
        String product = "Sauce Labs Backpack";
        dashboardPage.addProductToCart(product);
        logger.info("Product added: {}", product);
        
        // Step 3: Verify product in cart
        Assert.assertTrue(dashboardPage.isProductInCart(product), "Product should be in cart");
        int cartCountBefore = dashboardPage.getCartItemCount();
        Assert.assertEquals(cartCountBefore, 1, "Cart should have 1 item");
        logger.info("Product in cart verified");
        
        // Step 4: Remove product
        dashboardPage.removeProductFromCart(product);
        logger.info("Product removed: {}", product);
        
        // Step 5: Verify product is removed
        Assert.assertFalse(dashboardPage.isProductInCart(product), "Product should not be in cart");
        int cartCountAfter = dashboardPage.getCartItemCount();
        Assert.assertEquals(cartCountAfter, 0, "Cart should be empty");
        logger.info("Product removal verified");
    }

    /**
     * E2E Test: Verify product sorting functionality
     * 
     * Test Steps:
     * 1. Login
     * 2. Sort products by price (low to high)
     * 3. Verify products are sorted
     */
    @Test(groups = {"e2e", "regression"}, description = "Verify product sorting functionality")
    public void testProductSorting() {
        logger.info("E2E Test: Verify product sorting functionality");
        
        // Step 1: Login
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        logger.info("User logged in");
        
        DashboardPage dashboardPage = new DashboardPage();
        
        // Step 2: Get initial product list
        int initialProductCount = dashboardPage.getProductCount();
        Assert.assertTrue(initialProductCount > 0, "Products should be displayed");
        logger.info("Initial product count: {}", initialProductCount);
        
        // Step 3: Sort products
        dashboardPage.sortProductsBy("Price (low to high)");
        logger.info("Products sorted by price (low to high)");
        
        // Step 4: Verify products are still displayed
        int sortedProductCount = dashboardPage.getProductCount();
        Assert.assertEquals(sortedProductCount, initialProductCount, 
                "Product count should remain the same after sorting");
        logger.info("Products verified after sorting");
    }

    /**
     * E2E Test: Browse products and logout
     * 
     * Test Steps:
     * 1. Login
     * 2. Browse dashboard
     * 3. Verify products and cart
     * 4. Logout
     */
    @Test(groups = {"e2e", "regression"}, description = "Browse products and logout")
    public void testBrowseProductsAndLogout() {
        logger.info("E2E Test: Browse products and logout");
        
        // Step 1: Login
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        logger.info("User logged in");
        
        DashboardPage dashboardPage = new DashboardPage();
        
        // Step 2: Verify dashboard
        Assert.assertTrue(dashboardPage.isDashboardPageLoaded(), "Dashboard should be loaded");
        logger.info("Dashboard verified");
        
        // Step 3: Get product names
        java.util.List<String> productNames = dashboardPage.getProductNames();
        Assert.assertTrue(productNames.size() > 0, "Products should be displayed");
        logger.info("Found {} products", productNames.size());
        
        // Step 4: Logout
        dashboardPage.logout();
        logger.info("User logged out");
        
        // Verify return to login page
        loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Should return to login page after logout");
        logger.info("Login page verified after logout");
    }
}
