package com.qa.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qa.framework.config.ConfigReader;
import com.qa.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestNG Listener for test execution tracking and Extent reporting
 * 
 * Features:
 * - Test execution tracking
 * - Extent Reports integration
 * - Screenshot capture on failure
 * - Automatic report generation
 * - Test status logging
 * 
 * Design Pattern: Listener Pattern
 * Thread Safety: Thread-safe for parallel execution
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static ExtentReports extentReports;
    private static final ConfigReader configReader = ConfigReader.getInstance();
    
    // ThreadLocal for storing test reports per thread (for parallel execution)
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    /**
     * Initialize Extent Reports before suite execution
     * 
     * @param context ITestContext
     */
    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite Started: {}", context.getName());
        
        if (extentReports == null) {
            initializeExtentReports();
        }
    }

    /**
     * Called before each test method execution
     * 
     * @param result ITestResult
     */
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test Started: {}", result.getName());
        
        ExtentTest test = extentReports.createTest(
                result.getName(),
                result.getMethod().getDescription()
        );
        
        // Add test class and method information
        test.assignCategory(result.getTestClass().getName());
        test.assignAuthor("Automation Team");
        
        extentTestThreadLocal.set(test);
    }

    /**
     * Called when test passes
     * 
     * @param result ITestResult
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: {}", result.getName());
        
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
            
            // Capture screenshot on success if configured
            if (configReader.getPropertyAsBoolean("capture.screenshot.on.success", false)) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(result.getName());
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Success Screenshot");
                }
            }
        }
    }

    /**
     * Called when test fails
     * 
     * @param result ITestResult
     */
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test Failed: {}", result.getName());
        logger.error("Failure Exception: {}", result.getThrowable());
        
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.FAIL, "Test failed");
            test.log(Status.FAIL, "Failure: " + result.getThrowable().getMessage());
            
            // Capture screenshot on failure
            if (configReader.getPropertyAsBoolean("capture.screenshot.on.failure", true)) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(result.getName() + "_FAILURE");
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                }
            }
        }
    }

    /**
     * Called when test is skipped
     * 
     * @param result ITestResult
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Skipped: {}", result.getName());
        
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getSkipCausesByTestClass());
        }
    }

    /**
     * Called when test fails but within success percentage
     * 
     * @param result ITestResult
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("Test failed but within success percentage: {}", result.getName());
    }

    /**
     * Finalize extent reports after suite execution
     * 
     * @param context ITestContext
     */
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite Finished: {}", context.getName());
        
        if (extentReports != null) {
            extentReports.flush();
            logger.info("Extent Reports flushed successfully");
        }
        
        extentTestThreadLocal.remove();
    }

    /**
     * Initialize Extent Reports
     */
    private synchronized void initializeExtentReports() {
        try {
            String reportPath = configReader.getProperty(
                    "extent.report.path",
                    "reports/extent-reports"
            );
            
            String reportName = configReader.getProperty(
                    "extent.report.name",
                    "TestExecutionReport"
            );
            
            // Create report directory if not exists
            File reportDir = new File(reportPath);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }
            
            // Generate report with timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportFile = reportPath + File.separator + reportName + "_" + timestamp + ".html";
            
            // Create Extent Spark Reporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Test Automation Report");
            sparkReporter.config().setReportName(reportName);
            
            // Create ExtentReports and attach reporter
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            
            // Set system information
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Environment", configReader.getEnvironment().getEnvironmentName());
            extentReports.setSystemInfo("Browser", configReader.getProperty("browser", "Chrome"));
            
            logger.info("Extent Reports initialized successfully");
            logger.info("Report path: {}", reportFile);
            
        } catch (Exception e) {
            logger.error("Failed to initialize Extent Reports", e);
        }
    }
}
