package com.qa.tests.login;

import com.qa.framework.base.BaseTest;
import com.qa.pages.DashboardPage;
import com.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for Login functionality
 * 
 * Test Group: smoke, regression
 * Priority: High
 * 
 * Contains test cases for:
 * - Valid login
 * - Invalid login
 * - Empty credentials
 * - UI element validation
 */
public class LoginTests extends BaseTest {

    /**
     * Test: Verify login page elements are displayed
     * 
     * Test Steps:
     * 1. Open login page
     * 2. Verify username field is displayed
     * 3. Verify password field is displayed
     * 4. Verify login button is displayed
     */
    @Test(groups = {"smoke", "regression"}, description = "Verify login page elements are displayed")
    public void testLoginPageElementsAreDisplayed() {
        logger.info("Test: Verify login page elements are displayed");
        
        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field should be displayed");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field should be displayed");
        Assert.assertTrue(loginPage.isLoginButtonEnabled(), "Login button should be enabled");
    }

    /**
     * Test: Successful login with valid credentials
     * 
     * Test Steps:
     * 1. Enter valid username
     * 2. Enter valid password
     * 3. Click login button
     * 4. Verify dashboard page is loaded
     */
    @Test(groups = {"smoke", "regression"}, description = "Successful login with valid credentials")
    public void testSuccessfulLoginWithValidCredentials() {
        logger.info("Test: Successful login with valid credentials");
        
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage;
        
        // Login with valid credentials
        loginPage.login("standard_user", "secret_sauce");
        
        // Verify dashboard is loaded
        dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isDashboardPageLoaded(), "Dashboard should be loaded after successful login");
    }

    /**
     * Test: Login fails with invalid username
     * 
     * Test Steps:
     * 1. Enter invalid username
     * 2. Enter valid password
     * 3. Click login button
     * 4. Verify error message is displayed
     */
    @Test(groups = {"regression"}, description = "Login fails with invalid username")
    public void testLoginFailsWithInvalidUsername() {
        logger.info("Test: Login fails with invalid username");
        
        LoginPage loginPage = new LoginPage();
        
        // Login with invalid username
        loginPage.login("invalid_user", "secret_sauce");
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid username");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"), 
                "Error message should indicate username and password mismatch");
    }

    /**
     * Test: Login fails with invalid password
     * 
     * Test Steps:
     * 1. Enter valid username
     * 2. Enter invalid password
     * 3. Click login button
     * 4. Verify error message is displayed
     */
    @Test(groups = {"regression"}, description = "Login fails with invalid password")
    public void testLoginFailsWithInvalidPassword() {
        logger.info("Test: Login fails with invalid password");
        
        LoginPage loginPage = new LoginPage();
        
        // Login with invalid password
        loginPage.login("standard_user", "wrong_password");
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid password");
    }

    /**
     * Test: Login fails with empty username
     * 
     * Test Steps:
     * 1. Leave username field empty
     * 2. Enter password
     * 3. Click login button
     * 4. Verify error message is displayed
     */
    @Test(groups = {"regression"}, description = "Login fails with empty username")
    public void testLoginFailsWithEmptyUsername() {
        logger.info("Test: Login fails with empty username");
        
        LoginPage loginPage = new LoginPage();
        
        // Login with empty username
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty username");
    }

    /**
     * Test: Login fails with empty password
     * 
     * Test Steps:
     * 1. Enter username
     * 2. Leave password field empty
     * 3. Click login button
     * 4. Verify error message is displayed
     */
    @Test(groups = {"regression"}, description = "Login fails with empty password")
    public void testLoginFailsWithEmptyPassword() {
        logger.info("Test: Login fails with empty password");
        
        LoginPage loginPage = new LoginPage();
        
        // Login with empty password
        loginPage.enterUsername("standard_user");
        loginPage.clickLoginButton();
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty password");
    }

    /**
     * Test: Username and password fields can be cleared
     * 
     * Test Steps:
     * 1. Enter username and password
     * 2. Clear both fields
     * 3. Verify fields are empty
     */
    @Test(groups = {"regression"}, description = "Username and password fields can be cleared")
    public void testCredentialFieldsCanBeCleared() {
        logger.info("Test: Username and password fields can be cleared");
        
        LoginPage loginPage = new LoginPage();
        
        // Enter credentials
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        
        // Clear fields
        loginPage.clearUsernameField();
        loginPage.clearPasswordField();
        
        // Verify fields are empty
        Assert.assertEquals(loginPage.getUsernameFieldValue(), "", "Username field should be empty");
        Assert.assertEquals(loginPage.getPasswordFieldValue(), "", "Password field should be empty");
    }
}
