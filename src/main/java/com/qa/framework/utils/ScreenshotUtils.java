package com.qa.framework.utils;

import com.qa.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.qa.framework.driver.DriverManager.getDriver;

/**
 * Utility class for taking screenshots during test execution
 * 
 * Features:
 * - Capture full page screenshots
 * - Automatic screenshot naming with timestamp
 * - Configurable screenshot directory
 * - Screenshot encoding support
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Thread-safe for parallel execution
 */
public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final ConfigReader configReader = ConfigReader.getInstance();
    private static final String SCREENSHOT_DIR = configReader.getProperty(
            "screenshot.directory", 
            "reports/screenshots"
    );

    /**
     * Private constructor to prevent instantiation
     */
    private ScreenshotUtils() {
    }

    /**
     * Take screenshot with custom filename
     * 
     * @param screenshotName Name of the screenshot
     * @return Path to saved screenshot
     */
    public static String captureScreenshot(String screenshotName) {
        try {
            WebDriver driver = getDriver();
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            // Create screenshot directory if not exists
            createScreenshotDirectory();
            
            // Generate unique filename with timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
            String fileName = String.format("%s_%s.png", screenshotName, timestamp);
            String filePath = SCREENSHOT_DIR + File.separator + fileName;
            
            // Copy screenshot to destination
            Files.copy(sourceFile.toPath(), Paths.get(filePath));
            logger.info("Screenshot captured successfully: {}", filePath);
            
            return filePath;
        } catch (IOException | IllegalStateException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }

    /**
     * Take screenshot with automatic naming based on test method name and timestamp
     * 
     * @return Path to saved screenshot
     */
    public static String captureScreenshot() {
        try {
            WebDriver driver = getDriver();
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            // Create screenshot directory if not exists
            createScreenshotDirectory();
            
            // Generate automatic filename with timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
            String fileName = String.format("screenshot_%s.png", timestamp);
            String filePath = SCREENSHOT_DIR + File.separator + fileName;
            
            // Copy screenshot to destination
            Files.copy(sourceFile.toPath(), Paths.get(filePath));
            logger.info("Screenshot captured successfully: {}", filePath);
            
            return filePath;
        } catch (IOException | IllegalStateException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }

    /**
     * Create screenshot directory if it doesn't exist
     */
    private static void createScreenshotDirectory() {
        try {
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                boolean created = screenshotDir.mkdirs();
                if (created) {
                    logger.info("Screenshot directory created: {}", SCREENSHOT_DIR);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to create screenshot directory", e);
        }
    }

    /**
     * Get screenshot directory path
     * 
     * @return Screenshot directory path
     */
    public static String getScreenshotDir() {
        return SCREENSHOT_DIR;
    }
}
