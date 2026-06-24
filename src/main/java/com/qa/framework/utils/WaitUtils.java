package com.qa.framework.utils;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.exceptions.TimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility class for smart synchronization and explicit waits
 * 
 * Features:
 * - Element visibility wait
 * - Element clickability wait
 * - Element presence wait
 * - Alert wait
 * - Configurable timeouts
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Safe for parallel execution
 */
public class WaitUtils {
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private final WebDriver driver;
    private final long explicitWaitTimeout;
    private WebDriverWait wait;

    /**
     * Constructor initializing wait with configurable timeout
     * 
     * @param driver WebDriver instance
     */
    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.explicitWaitTimeout = ConfigReader.getInstance()
                .getPropertyAsInteger("explicit.wait.timeout", 10);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitTimeout));
    }

    /**
     * Wait for element to be visible
     * 
     * @param element WebElement to wait for
     * @return WebElement when visible
     * @throws TimeoutException If element not visible within timeout
     */
    public WebElement waitForElementToBeVisible(WebElement element) {
        try {
            logger.debug("Waiting for element to be visible with timeout: {} seconds", explicitWaitTimeout);
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Element did not become visible within timeout", e);
            throw new TimeoutException("Element visibility timeout", e);
        }
    }

    /**
     * Wait for element to be visible by locator
     * 
     * @param locator By locator
     * @return WebElement when visible
     * @throws TimeoutException If element not visible within timeout
     */
    public WebElement waitForElementToBeVisible(By locator) {
        try {
            logger.debug("Waiting for element '{}' to be visible with timeout: {} seconds", 
                    locator, explicitWaitTimeout);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Element with locator '{}' did not become visible within timeout", locator, e);
            throw new TimeoutException("Element visibility timeout for: " + locator, e);
        }
    }

    /**
     * Wait for element to be clickable
     * 
     * @param element WebElement to wait for
     * @return WebElement when clickable
     * @throws TimeoutException If element not clickable within timeout
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        try {
            logger.debug("Waiting for element to be clickable with timeout: {} seconds", explicitWaitTimeout);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Element did not become clickable within timeout", e);
            throw new TimeoutException("Element clickability timeout", e);
        }
    }

    /**
     * Wait for element to be clickable by locator
     * 
     * @param locator By locator
     * @return WebElement when clickable
     * @throws TimeoutException If element not clickable within timeout
     */
    public WebElement waitForElementToBeClickable(By locator) {
        try {
            logger.debug("Waiting for element '{}' to be clickable with timeout: {} seconds", 
                    locator, explicitWaitTimeout);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Element with locator '{}' did not become clickable within timeout", locator, e);
            throw new TimeoutException("Element clickability timeout for: " + locator, e);
        }
    }

    /**
     * Wait for element to be present in DOM
     * 
     * @param locator By locator
     * @return WebElement when present
     * @throws TimeoutException If element not present within timeout
     */
    public WebElement waitForElementPresence(By locator) {
        try {
            logger.debug("Waiting for element '{}' to be present with timeout: {} seconds", 
                    locator, explicitWaitTimeout);
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Element with locator '{}' was not present within timeout", locator, e);
            throw new TimeoutException("Element presence timeout for: " + locator, e);
        }
    }

    /**
     * Wait for all elements to be visible
     * 
     * @param locator By locator
     * @return List of WebElements when all visible
     * @throws TimeoutException If elements not visible within timeout
     */
    public java.util.List<WebElement> waitForAllElementsVisible(By locator) {
        try {
            logger.debug("Waiting for all elements '{}' to be visible with timeout: {} seconds", 
                    locator, explicitWaitTimeout);
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Elements with locator '{}' were not all visible within timeout", locator, e);
            throw new TimeoutException("Elements visibility timeout for: " + locator, e);
        }
    }

    /**
     * Wait for alert to be present
     * 
     * @return Alert when present
     * @throws TimeoutException If alert not present within timeout
     */
    public void waitForAlertPresent() {
        try {
            logger.debug("Waiting for alert to be present with timeout: {} seconds", explicitWaitTimeout);
            wait.until(ExpectedConditions.alertIsPresent());
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Alert was not present within timeout", e);
            throw new TimeoutException("Alert presence timeout", e);
        }
    }

    /**
     * Wait for element to contain text
     * 
     * @param locator By locator
     * @param text Text to wait for
     * @return true when text appears
     * @throws TimeoutException If text not found within timeout
     */
    public boolean waitForElementToContainText(By locator, String text) {
        try {
            logger.debug("Waiting for element '{}' to contain text '{}' with timeout: {} seconds", 
                    locator, text, explicitWaitTimeout);
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Element did not contain expected text within timeout", e);
            throw new TimeoutException("Element text timeout for: " + text, e);
        }
    }

    /**
     * Wait for element to be invisible
     * 
     * @param locator By locator
     * @return true when element becomes invisible
     * @throws TimeoutException If element doesn't become invisible
     */
    public boolean waitForElementToBeInvisible(By locator) {
        try {
            logger.debug("Waiting for element '{}' to be invisible with timeout: {} seconds", 
                    locator, explicitWaitTimeout);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Element did not become invisible within timeout", e);
            throw new TimeoutException("Element invisibility timeout for: " + locator, e);
        }
    }

    /**
     * Wait for URL to contain text
     * 
     * @param urlContains Text that URL should contain
     * @return true when URL contains text
     * @throws TimeoutException If URL doesn't contain text
     */
    public boolean waitForUrlContains(String urlContains) {
        try {
            logger.debug("Waiting for URL to contain '{}' with timeout: {} seconds", 
                    urlContains, explicitWaitTimeout);
            return wait.until(ExpectedConditions.urlContains(urlContains));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("URL did not contain expected text within timeout", e);
            throw new TimeoutException("URL contains timeout for: " + urlContains, e);
        }
    }

    /**
     * Custom wait with custom timeout
     * 
     * @param condition Expected condition
     * @param timeoutSeconds Custom timeout in seconds
     * @param <T> Return type
     * @return Result of condition
     */
    public <T> T waitWithTimeout(org.openqa.selenium.support.ui.ExpectedCondition<T> condition, long timeoutSeconds) {
        try {
            logger.debug("Waiting with custom timeout: {} seconds", timeoutSeconds);
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return customWait.until(condition);
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Custom wait timeout", e);
            throw new TimeoutException("Custom wait timeout after " + timeoutSeconds + " seconds", e);
        }
    }
}
