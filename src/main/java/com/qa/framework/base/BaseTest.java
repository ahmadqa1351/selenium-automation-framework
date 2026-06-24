package com.qa.framework.base;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.driver.DriverFactory;
import com.qa.framework.driver.DriverManager;
import com.qa.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

/**
 * Abstract Base Test class for all test cases
 * 
 * Features:
 * - WebDriver lifecycle management
 * - Configuration management
 * - Screenshot capture on failure
 * - Test execution logging
 * - Retry mechanism support
 * 
 * Design Pattern: Template Method Pattern
 * Thread Safety: Thread-safe for parallel execution
 * 
 * Usage:
 * public class LoginTests extends BaseTest {
 *     @Test
 *     public void testLogin() {
 *         WebDriver driver = getDriver();
 *         // Test code
 *     }
 * }
 */
public abstract class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected ConfigReader configReader;
    private DriverFactory driverFactory;

    /**
     * Setup method executed before each test
     * 
     * @param browser Browser type (optional, from parameter)
     * @param method Test method being executed
     */
    @BeforeMethod
    @Parameters({"browser", "environment"})
    public void setUp(String browser, String environment, Method method) {
        logger.info("================================");
        logger.info("Starting Test: {}", method.getName());
        logger.info("================================");

        try {
            // Initialize configuration reader
            configReader = ConfigReader.getInstance();
            logger.debug("ConfigReader initialized");

            // Override environment if provided as parameter
            if (environment != null && !environment.isEmpty()) {
                logger.debug("Environment parameter provided: {}", environment);
            }

            // Create and set WebDriver
            driverFactory = new DriverFactory();
            WebDriver driver = driverFactory.createDriver();
            DriverManager.setDriver(driver);

            // Navigate to base URL
            String baseUrl = configReader.getProperty("base.url");
            driver.navigate().to(baseUrl);
            logger.info("Navigated to: {}", baseUrl);

        } catch (Exception e) {
            logger.error("Failed to initialize test setup", e);
            throw new RuntimeException("Test setup failed: " + e.getMessage(), e);
        }
    }

    /**
     * Teardown method executed after each test
     * 
     * @param result Test execution result
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            // Capture screenshot on failure
            if (!result.isSuccess()) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(result.getName());
                logger.error("Test failed. Screenshot captured: {}", screenshotPath);
            }

        } catch (Exception e) {
            logger.error("Error in teardown screenshot capture", e);
        } finally {
            // Quit WebDriver
            try {
                DriverManager.quitDriver();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.error("Error while closing WebDriver", e);
            }

            logger.info("Test Execution Status: {}", result.isSuccess() ? "PASSED" : "FAILED");
            logger.info("================================\n");
        }
    }

    /**
     * Get WebDriver instance for current thread
     * 
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Get configuration reader
     * 
     * @return ConfigReader instance
     */
    protected ConfigReader getConfig() {
        return configReader;
    }
}
