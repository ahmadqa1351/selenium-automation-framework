package com.qa.framework.base;

import com.qa.framework.driver.DriverManager;
import com.qa.framework.exceptions.ElementNotFoundException;
import com.qa.framework.exceptions.TimeoutException;
import com.qa.framework.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Abstract Base Page class for all Page Objects
 * 
 * Features:
 * - Centralized element interaction methods
 * - Smart waiting and synchronization
 * - Logging for all actions
 * - Exception handling
 * - Common utilities (scrolling, clicking, typing, etc.)
 * 
 * Design Pattern: Page Object Model + Template Method
 * Thread Safety: Safe for parallel execution
 * 
 * Usage:
 * public class LoginPage extends BasePage {
 *     @FindBy(id = "username")
 *     WebElement usernameField;
 *     
 *     public void enterUsername(String username) {
 *         sendKeys(usernameField, username);
 *     }
 * }
 */
public abstract class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WaitUtils waitUtils;

    /**
     * Constructor to initialize WebDriver and PageFactory
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
        logger.debug("Page initialized: {}", this.getClass().getSimpleName());
    }

    // ============= Element Interaction Methods =============

    /**
     * Wait for element to be visible and click
     * 
     * @param element WebElement to click
     */
    public void click(WebElement element) {
        try {
            waitUtils.waitForElementToBeClickable(element);
            element.click();
            logger.debug("Clicked on element: {}", element);
        } catch (Exception e) {
            logger.error("Failed to click element", e);
            throw new ElementNotFoundException("Unable to click element", e);
        }
    }

    /**
     * Click element by locator
     * 
     * @param locator Element locator
     */
    public void click(By locator) {
        try {
            WebElement element = waitUtils.waitForElementToBeVisible(locator);
            element.click();
            logger.debug("Clicked on element using locator: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element with locator: {}", locator, e);
            throw new ElementNotFoundException("Unable to click element: " + locator, e);
        }
    }

    /**
     * Wait for element to be visible and send keys
     * 
     * @param element WebElement to type into
     * @param text Text to type
     */
    public void sendKeys(WebElement element, String text) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.debug("Typed text '{}' into element", text);
        } catch (Exception e) {
            logger.error("Failed to send keys to element", e);
            throw new ElementNotFoundException("Unable to send keys to element", e);
        }
    }

    /**
     * Send keys by locator
     * 
     * @param locator Element locator
     * @param text Text to type
     */
    public void sendKeys(By locator, String text) {
        try {
            WebElement element = waitUtils.waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
            logger.debug("Typed text '{}' into element using locator: {}", text, locator);
        } catch (Exception e) {
            logger.error("Failed to send keys with locator: {}", locator, e);
            throw new ElementNotFoundException("Unable to send keys to element: " + locator, e);
        }
    }

    /**
     * Get text from element
     * 
     * @param element WebElement to get text from
     * @return Element text
     */
    public String getText(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            String text = element.getText();
            logger.debug("Retrieved text from element: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element", e);
            throw new ElementNotFoundException("Unable to get text from element", e);
        }
    }

    /**
     * Get text from element by locator
     * 
     * @param locator Element locator
     * @return Element text
     */
    public String getText(By locator) {
        try {
            WebElement element = waitUtils.waitForElementToBeVisible(locator);
            String text = element.getText();
            logger.debug("Retrieved text from element using locator: {}", locator);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text with locator: {}", locator, e);
            throw new ElementNotFoundException("Unable to get text from element: " + locator, e);
        }
    }

    /**
     * Get attribute value
     * 
     * @param element WebElement to get attribute from
     * @param attributeName Attribute name
     * @return Attribute value
     */
    public String getAttribute(WebElement element, String attributeName) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            String attributeValue = element.getAttribute(attributeName);
            logger.debug("Retrieved attribute '{}' with value: {}", attributeName, attributeValue);
            return attributeValue;
        } catch (Exception e) {
            logger.error("Failed to get attribute from element", e);
            throw new ElementNotFoundException("Unable to get attribute from element", e);
        }
    }

    /**
     * Check if element is displayed
     * 
     * @param element WebElement to check
     * @return true if element is displayed
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            logger.debug("Element display status: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.debug("Element is not displayed");
            return false;
        }
    }

    /**
     * Check if element is enabled
     * 
     * @param element WebElement to check
     * @return true if element is enabled
     */
    public boolean isElementEnabled(WebElement element) {
        try {
            boolean isEnabled = element.isEnabled();
            logger.debug("Element enabled status: {}", isEnabled);
            return isEnabled;
        } catch (Exception e) {
            logger.debug("Element is not enabled");
            return false;
        }
    }

    /**
     * Wait for element to be visible
     * 
     * @param element WebElement to wait for
     * @return true if element is visible within timeout
     */
    public boolean waitForElementVisible(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            logger.debug("Element became visible");
            return true;
        } catch (TimeoutException e) {
            logger.warn("Element did not become visible within timeout");
            return false;
        }
    }

    /**
     * Wait for element to be visible by locator
     * 
     * @param locator Element locator
     * @return true if element is visible within timeout
     */
    public boolean waitForElementVisible(By locator) {
        try {
            waitUtils.waitForElementToBeVisible(locator);
            logger.debug("Element became visible for locator: {}", locator);
            return true;
        } catch (TimeoutException e) {
            logger.warn("Element did not become visible for locator: {}", locator);
            return false;
        }
    }

    // ============= Dropdown Methods =============

    /**
     * Select dropdown option by visible text
     * 
     * @param element Dropdown element
     * @param optionText Visible text of option
     */
    public void selectByVisibleText(WebElement element, String optionText) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            Select select = new Select(element);
            select.selectByVisibleText(optionText);
            logger.debug("Selected option '{}' from dropdown", optionText);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option by visible text", e);
            throw new ElementNotFoundException("Unable to select option: " + optionText, e);
        }
    }

    /**
     * Select dropdown option by value
     * 
     * @param element Dropdown element
     * @param value Value of option
     */
    public void selectByValue(WebElement element, String value) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.debug("Selected option with value '{}' from dropdown", value);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option by value", e);
            throw new ElementNotFoundException("Unable to select option with value: " + value, e);
        }
    }

    /**
     * Select dropdown option by index
     * 
     * @param element Dropdown element
     * @param index Index of option
     */
    public void selectByIndex(WebElement element, int index) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            Select select = new Select(element);
            select.selectByIndex(index);
            logger.debug("Selected option at index '{}' from dropdown", index);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option by index", e);
            throw new ElementNotFoundException("Unable to select option at index: " + index, e);
        }
    }

    /**
     * Get all dropdown options
     * 
     * @param element Dropdown element
     * @return List of WebElements representing options
     */
    public List<WebElement> getDropdownOptions(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            Select select = new Select(element);
            List<WebElement> options = select.getOptions();
            logger.debug("Retrieved {} options from dropdown", options.size());
            return options;
        } catch (Exception e) {
            logger.error("Failed to get dropdown options", e);
            throw new ElementNotFoundException("Unable to get dropdown options", e);
        }
    }

    // ============= Mouse Actions Methods =============

    /**
     * Hover over element
     * 
     * @param element WebElement to hover over
     */
    public void hoverOverElement(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            logger.debug("Hovered over element");
        } catch (Exception e) {
            logger.error("Failed to hover over element", e);
            throw new ElementNotFoundException("Unable to hover over element", e);
        }
    }

    /**
     * Right-click on element
     * 
     * @param element WebElement to right-click
     */
    public void rightClick(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            Actions actions = new Actions(driver);
            actions.contextClick(element).perform();
            logger.debug("Right-clicked on element");
        } catch (Exception e) {
            logger.error("Failed to right-click element", e);
            throw new ElementNotFoundException("Unable to right-click element", e);
        }
    }

    /**
     * Double-click on element
     * 
     * @param element WebElement to double-click
     */
    public void doubleClick(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            Actions actions = new Actions(driver);
            actions.doubleClick(element).perform();
            logger.debug("Double-clicked on element");
        } catch (Exception e) {
            logger.error("Failed to double-click element", e);
            throw new ElementNotFoundException("Unable to double-click element", e);
        }
    }

    // ============= Scrolling Methods =============

    /**
     * Scroll to element
     * 
     * @param element WebElement to scroll to
     */
    public void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.debug("Scrolled to element");
        } catch (Exception e) {
            logger.error("Failed to scroll to element", e);
            throw new ElementNotFoundException("Unable to scroll to element", e);
        }
    }

    /**
     * Scroll page down by pixels
     * 
     * @param pixels Number of pixels to scroll
     */
    public void scrollDown(int pixels) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, " + pixels + ");");
            logger.debug("Scrolled down by {} pixels", pixels);
        } catch (Exception e) {
            logger.error("Failed to scroll down", e);
            throw new ElementNotFoundException("Unable to scroll", e);
        }
    }

    /**
     * Scroll page up by pixels
     * 
     * @param pixels Number of pixels to scroll
     */
    public void scrollUp(int pixels) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, -" + pixels + ");");
            logger.debug("Scrolled up by {} pixels", pixels);
        } catch (Exception e) {
            logger.error("Failed to scroll up", e);
            throw new ElementNotFoundException("Unable to scroll", e);
        }
    }

    // ============= Alert Methods =============

    /**
     * Accept alert
     */
    public void acceptAlert() {
        try {
            waitUtils.waitForAlertPresent();
            driver.switchTo().alert().accept();
            logger.debug("Alert accepted");
        } catch (Exception e) {
            logger.error("Failed to accept alert", e);
            throw new ElementNotFoundException("Unable to accept alert", e);
        }
    }

    /**
     * Dismiss alert
     */
    public void dismissAlert() {
        try {
            waitUtils.waitForAlertPresent();
            driver.switchTo().alert().dismiss();
            logger.debug("Alert dismissed");
        } catch (Exception e) {
            logger.error("Failed to dismiss alert", e);
            throw new ElementNotFoundException("Unable to dismiss alert", e);
        }
    }

    /**
     * Get alert text
     * 
     * @return Alert text
     */
    public String getAlertText() {
        try {
            waitUtils.waitForAlertPresent();
            String alertText = driver.switchTo().alert().getText();
            logger.debug("Alert text retrieved: {}", alertText);
            return alertText;
        } catch (Exception e) {
            logger.error("Failed to get alert text", e);
            throw new ElementNotFoundException("Unable to get alert text", e);
        }
    }

    // ============= Window/Frame Methods =============

    /**
     * Switch to frame by index
     * 
     * @param index Frame index
     */
    public void switchToFrame(int index) {
        try {
            driver.switchTo().frame(index);
            logger.debug("Switched to frame with index: {}", index);
        } catch (Exception e) {
            logger.error("Failed to switch to frame", e);
            throw new ElementNotFoundException("Unable to switch to frame", e);
        }
    }

    /**
     * Switch to frame by element
     * 
     * @param element Frame element
     */
    public void switchToFrame(WebElement element) {
        try {
            driver.switchTo().frame(element);
            logger.debug("Switched to frame element");
        } catch (Exception e) {
            logger.error("Failed to switch to frame", e);
            throw new ElementNotFoundException("Unable to switch to frame", e);
        }
    }

    /**
     * Switch out of frame to default content
     */
    public void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            logger.debug("Switched to default content");
        } catch (Exception e) {
            logger.error("Failed to switch to default content", e);
        }
    }

    /**
     * Get current page URL
     * 
     * @return Current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get current page title
     * 
     * @return Current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Refresh current page
     */
    public void refreshPage() {
        try {
            driver.navigate().refresh();
            logger.debug("Page refreshed");
        } catch (Exception e) {
            logger.error("Failed to refresh page", e);
        }
    }

    /**
     * Navigate to URL
     * 
     * @param url URL to navigate to
     */
    public void navigateTo(String url) {
        try {
            driver.navigate().to(url);
            logger.debug("Navigated to URL: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}", url, e);
            throw new ElementNotFoundException("Unable to navigate to URL: " + url, e);
        }
    }
}
