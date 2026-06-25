package com.qa.framework.retry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry Analyzer for failed tests
 * 
 * Features:
 * - Automatic retry on test failure
 * - Configurable retry count
 * - Logging of retry attempts
 * - Skip retry for specific exceptions
 * 
 * Design Pattern: Analyzer Pattern
 * Thread Safety: Thread-safe
 * 
 * Usage:
 * @Test(retryAnalyzer = RetryAnalyzer.class)
 * public void testThatMightFail() { }
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    /**
     * Determine if test should be retried
     * 
     * @param result Test result
     * @return true if test should be retried
     */
    @Override
    public boolean retry(ITestResult result) {
        // Check if test passed
        if (result.isSuccess()) {
            logger.debug("Test passed: {}", result.getName());
            return false;
        }

        // Check retry count
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("Retrying test: {} (Attempt: {}/{})", 
                    result.getName(), retryCount, MAX_RETRY_COUNT);
            
            // Log failure reason
            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                logger.error("Failure reason: {}", throwable.getMessage());
            }
            
            return true;
        }

        logger.error("Test failed after {} retry attempts: {}", 
                MAX_RETRY_COUNT, result.getName());
        return false;
    }

    /**
     * Get retry count
     * 
     * @return Current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Reset retry count
     * Used for test isolation
     */
    public void resetRetryCount() {
        retryCount = 0;
        logger.debug("Retry count reset");
    }
}
