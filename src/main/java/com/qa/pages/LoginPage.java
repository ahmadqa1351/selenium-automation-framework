package com.qa.pages;

import com.qa.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Login Page
 * 
 * Contains all elements and actions related to login functionality
 * Extends BasePage to inherit all utility methods
 * 
 * Design Pattern: Page Object Model
 * 
 * Example Usage:
 * LoginPage loginPage = new LoginPage();
 * loginPage.enterUsername("testuser");
 * loginPage.enterPassword("password123");
 * loginPage.clickLoginButton();
 */
public class LoginPage extends BasePage {

    // ============= Web Elements =============
    
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(className = "error-message-container")
    private WebElement errorMessage;

    @FindBy(xpath = "//div[@class='login_logo']")
    private WebElement loginLogo;

    // By locators for alternative element access
    private final By usernameFieldBy = By.id("user-name");
    private final By passwordFieldBy = By.id("password");
    private final By loginButtonBy = By.id("login-button");
    private final By errorMessageBy = By.className("error-message-container");

    // ============= Page Actions =============

    /**
     * Enter username in username field
     * 
     * @param username Username to enter
     */
    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        sendKeys(usernameField, username);
    }

    /**
     * Enter password in password field
     * 
     * @param password Password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        sendKeys(passwordField, password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        click(loginButton);
    }

    /**
     * Perform login with username and password
     * 
     * @param username Username
     * @param password Password
     */
    public void login(String username, String password) {
        logger.info("Performing login for user: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get error message text
     * 
     * @return Error message text
     */
    public String getErrorMessage() {
        logger.info("Retrieving error message");
        if (isElementDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }

    /**
     * Check if error message is displayed
     * 
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
        logger.info("Checking if error message is displayed");
        return isElementDisplayed(errorMessage);
    }

    /**
     * Check if login page is loaded
     * 
     * @return true if login logo is visible
     */
    public boolean isLoginPageLoaded() {
        logger.info("Checking if login page is loaded");
        return waitForElementVisible(loginLogo);
    }

    /**
     * Check if username field is displayed
     * 
     * @return true if username field is visible
     */
    public boolean isUsernameFieldDisplayed() {
        logger.info("Checking if username field is displayed");
        return isElementDisplayed(usernameField);
    }

    /**
     * Check if password field is displayed
     * 
     * @return true if password field is visible
     */
    public boolean isPasswordFieldDisplayed() {
        logger.info("Checking if password field is displayed");
        return isElementDisplayed(passwordField);
    }

    /**
     * Check if login button is enabled
     * 
     * @return true if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        logger.info("Checking if login button is enabled");
        return isElementEnabled(loginButton);
    }

    /**
     * Clear username field
     */
    public void clearUsernameField() {
        logger.info("Clearing username field");
        usernameField.clear();
    }

    /**
     * Clear password field
     */
    public void clearPasswordField() {
        logger.info("Clearing password field");
        passwordField.clear();
    }

    /**
     * Get username field value
     * 
     * @return Username field value
     */
    public String getUsernameFieldValue() {
        logger.info("Getting username field value");
        return getAttribute(usernameField, "value");
    }

    /**
     * Get password field value
     * 
     * @return Password field value
     */
    public String getPasswordFieldValue() {
        logger.info("Getting password field value");
        return getAttribute(passwordField, "value");
    }
}
