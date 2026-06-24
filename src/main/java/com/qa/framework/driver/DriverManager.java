package com.qa.framework.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Thread-safe WebDriver Manager for lifecycle management
 * 
 * Design Pattern: Singleton + ThreadLocal
 * Thread Safety: Complete thread-safe implementation for parallel execution
 * 
 * Usage:
 * - DriverManager.setDriver(driver)    // In @Before method
 * - DriverManager.getDriver()            // Access driver in test
 * - DriverManager.quitDriver()           // In @After method
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Set WebDriver instance for current thread
     * @param driver WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        if (driver != null) {
            driverThreadLocal.set(driver);
            logger.debug("WebDriver set for thread: {}", Thread.currentThread().getId());
        }
    }

    /**
     * Get WebDriver instance for current thread
     * @return WebDriver instance for current thread
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.error("WebDriver not initialized for thread: {}", Thread.currentThread().getId());
            throw new IllegalStateException("WebDriver is not initialized for current thread");
        }
        return driver;
    }

    /**
     * Quit WebDriver and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully for thread: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Check if driver is initialized for current thread
     * @return true if driver is initialized, false otherwise
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

    /**
     * Remove driver from ThreadLocal without quitting
     * Use with caution - prefer quitDriver()
     */
    public static void removeDriver() {
        driverThreadLocal.remove();
        logger.debug("WebDriver removed from ThreadLocal for thread: {}", Thread.currentThread().getId());
    }
}
