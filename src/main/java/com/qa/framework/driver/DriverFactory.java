package com.qa.framework.driver;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.enums.BrowserType;
import com.qa.framework.exceptions.FrameworkException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.util.HashMap;
import java.util.Map;

/**
 * WebDriver Factory for creating and configuring browser instances
 * 
 * Design Pattern: Factory Pattern
 * Supports: Chrome, Firefox, Edge, Safari
 * Features: Automatic driver management, custom options, event listeners
 */
public class DriverFactory {
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private final ConfigReader configReader = ConfigReader.getInstance();

    /**
     * Create WebDriver instance based on configuration
     * @return Configured WebDriver instance
     */
    public WebDriver createDriver() {
        String browserName = configReader.getProperty("browser", "chrome");
        BrowserType browser = BrowserType.fromString(browserName);
        return createDriver(browser);
    }

    /**
     * Create WebDriver instance for specific browser type
     * @param browserType Browser type to create
     * @return Configured WebDriver instance
     */
    public WebDriver createDriver(BrowserType browserType) {
        logger.info("Creating WebDriver for browser: {}", browserType.getBrowserName());
        
        WebDriver driver;
        
        switch (browserType) {
            case CHROME:
                driver = createChromeDriver();
                break;
            case FIREFOX:
                driver = createFirefoxDriver();
                break;
            case EDGE:
                driver = createEdgeDriver();
                break;
            case SAFARI:
                driver = createSafariDriver();
                break;
            default:
                logger.warn("Unsupported browser type: {}. Defaulting to Chrome", browserType);
                driver = createChromeDriver();
        }

        // Apply common configurations
        configureDriver(driver);
        
        logger.info("WebDriver instance created successfully");
        return driver;
    }

    /**
     * Create Chrome WebDriver with optimized options
     * @return Chrome WebDriver instance
     */
    private WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = getChromeOptions();
        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver with optimized options
     * @return Firefox WebDriver instance
     */
    private WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = getFirefoxOptions();
        return new FirefoxDriver(options);
    }

    /**
     * Create Edge WebDriver with optimized options
     * @return Edge WebDriver instance
     */
    private WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = getEdgeOptions();
        return new EdgeDriver(options);
    }

    /**
     * Create Safari WebDriver
     * @return Safari WebDriver instance
     */
    private WebDriver createSafariDriver() {
        return new SafariDriver();
    }

    /**
     * Get Chrome options with common configurations
     * @return Configured ChromeOptions
     */
    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Headless mode
        if (configReader.getPropertyAsBoolean("headless", false)) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
        }

        // Common arguments
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-extensions",
                "--disable-popup-blocking",
                "start-maximized"
        );

        // Window size for headless
        options.addArguments("--window-size=1920,1080");

        // Accept insecure certificates
        if (configReader.getPropertyAsBoolean("accept.insecure.ssl", true)) {
            options.setAcceptInsecureCerts(true);
        }

        // Page load strategy
        String pageLoadStrategy = configReader.getProperty("page.load.strategy", "normal");
        options.setPageLoadStrategy(PageLoadStrategy.valueOf(pageLoadStrategy.toUpperCase()));

        // Disable notifications
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.managed_default_content_settings.notifications", 2);
        options.setExperimentalOption("prefs", prefs);

        return options;
    }

    /**
     * Get Firefox options with common configurations
     * @return Configured FirefoxOptions
     */
    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        // Headless mode
        if (configReader.getPropertyAsBoolean("headless", false)) {
            options.addArguments("--headless");
        }

        // Accept insecure certificates
        if (configReader.getPropertyAsBoolean("accept.insecure.ssl", true)) {
            options.setAcceptInsecureCerts(true);
        }

        // Page load strategy
        String pageLoadStrategy = configReader.getProperty("page.load.strategy", "normal");
        options.setPageLoadStrategy(PageLoadStrategy.valueOf(pageLoadStrategy.toUpperCase()));

        return options;
    }

    /**
     * Get Edge options with common configurations
     * @return Configured EdgeOptions
     */
    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        // Headless mode
        if (configReader.getPropertyAsBoolean("headless", false)) {
            options.addArguments("--headless");
        }

        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "start-maximized"
        );

        // Accept insecure certificates
        if (configReader.getPropertyAsBoolean("accept.insecure.ssl", true)) {
            options.setAcceptInsecureCerts(true);
        }

        // Page load strategy
        String pageLoadStrategy = configReader.getProperty("page.load.strategy", "normal");
        options.setPageLoadStrategy(PageLoadStrategy.valueOf(pageLoadStrategy.toUpperCase()));

        return options;
    }

    /**
     * Configure common driver settings
     * @param driver WebDriver instance to configure
     */
    private void configureDriver(WebDriver driver) {
        try {
            // Maximize window if configured
            if (configReader.getPropertyAsBoolean("window.maximize", true)) {
                driver.manage().window().maximize();
            }
        } catch (Exception e) {
            logger.warn("Could not maximize window", e);
        }
    }
}
