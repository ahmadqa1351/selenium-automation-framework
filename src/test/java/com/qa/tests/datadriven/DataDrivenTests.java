package com.qa.tests.datadriven;

import com.qa.framework.base.BaseTest;
import com.qa.framework.utils.TestDataProvider;
import com.qa.pages.DashboardPage;
import com.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Data-Driven Test class for parameterized testing
 * 
 * Test Group: smoke, regression, datadriven
 * 
 * Features:
 * - TestNG DataProvider for external data
 * - Excel, JSON, and CSV data sources
 * - Parameterized test execution
 * 
 * Test Data File: src/test/resources/data/loginTestData.xlsx
 * Sheet Name: LoginCredentials
 */
public class DataDrivenTests extends BaseTest {

    /**
     * Data-Driven Test: Login with multiple credentials from Excel
     * 
     * Test Data Format:
     * Column 1: username
     * Column 2: password
     * Column 3: expectedResult (success/failure)
     * 
     * @param testData Map containing username and password
     */
    @Test(groups = {"smoke", "regression", "datadriven"}, 
            dataProvider = "excelDataProvider", 
            dataProviderClass = TestDataProvider.class,
            description = "Login with multiple credentials from Excel")
    public void testLoginWithMultipleCredentialsFromExcel(Map<String, String> testData) {
        logger.info("Data-Driven Test: Testing login with credentials from Excel");
        
        // Extract test data
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedResult = testData.get("expectedResult");
        
        logger.info("Test data - Username: {}, Expected Result: {}", username, expectedResult);
        
        // Perform login
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        
        // Verify result based on expected outcome
        if ("success".equalsIgnoreCase(expectedResult)) {
            DashboardPage dashboardPage = new DashboardPage();
            Assert.assertTrue(dashboardPage.isDashboardPageLoaded(), 
                    "Dashboard should be loaded for successful login");
            logger.info("Login successful for user: {}", username);
        } else {
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                    "Error message should be displayed for failed login");
            logger.info("Login failed as expected for user: {}", username);
        }
    }

    /**
     * Data-Driven Test: Product addition with multiple products from Excel
     * 
     * Test Data Format:
     * Column 1: productName
     * Column 2: quantity
     * 
     * @param testData Map containing product information
     */
    @Test(groups = {"regression", "datadriven"}, 
            dataProvider = "excelDataProvider", 
            dataProviderClass = TestDataProvider.class,
            description = "Add products to cart from Excel data")
    public void testAddProductsToCartFromExcel(Map<String, String> testData) {
        logger.info("Data-Driven Test: Adding products from Excel data");
        
        // Login first
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        
        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isDashboardPageLoaded(), "Dashboard should be loaded");
        
        // Extract product data
        String productName = testData.get("productName");
        String quantity = testData.get("quantity");
        
        logger.info("Test data - Product: {}, Quantity: {}", productName, quantity);
        
        // Add product to cart
        int quantityInt = Integer.parseInt(quantity);
        for (int i = 0; i < quantityInt; i++) {
            dashboardPage.addProductToCart(productName);
        }
        
        // Verify product is in cart
        Assert.assertTrue(dashboardPage.isProductInCart(productName), 
                "Product should be in cart: " + productName);
        logger.info("Product added successfully: {}", productName);
    }

    /**
     * Data-Driven Test: Verify product information from test data
     * 
     * @param testData Map containing product information
     */
    @Test(groups = {"regression", "datadriven"}, 
            dataProvider = "excelDataProvider", 
            dataProviderClass = TestDataProvider.class,
            description = "Verify product information")
    public void testVerifyProductInformation(Map<String, String> testData) {
        logger.info("Data-Driven Test: Verifying product information");
        
        // Login
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        
        DashboardPage dashboardPage = new DashboardPage();
        
        // Extract test data
        String productName = testData.get("productName");
        String expectedPrice = testData.get("expectedPrice");
        
        logger.info("Test data - Product: {}, Expected Price: {}", productName, expectedPrice);
        
        // Get actual price
        String actualPrice = dashboardPage.getProductPrice(productName);
        
        // Verify price
        Assert.assertEquals(actualPrice, expectedPrice, 
                "Product price should match expected price");
        logger.info("Product price verified: {}", actualPrice);
    }
}
