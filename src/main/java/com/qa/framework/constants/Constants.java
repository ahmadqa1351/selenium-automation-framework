package com.qa.framework.constants;

/**
 * Framework-level constants
 * Centralized configuration values used across the framework
 */
public class Constants {

    // File Paths
    public static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    public static final String LOG4J_FILE_PATH = "src/test/resources/log4j2.xml";
    public static final String TEST_DATA_PATH = "src/test/resources/data";

    // Timeout Constants (in seconds)
    public static final long EXPLICIT_WAIT = 10;
    public static final long IMPLICIT_WAIT = 5;
    public static final long PAGE_LOAD_WAIT = 15;

    // Screenshot Configuration
    public static final String SCREENSHOT_PATH = "reports/screenshots";

    // Report Configuration
    public static final String REPORT_PATH = "reports/extent-reports";
    public static final String REPORT_NAME = "TestExecutionReport";

    // Log Configuration
    public static final String LOG_PATH = "logs";

    // Browser Configuration
    public static final String DEFAULT_BROWSER = "chrome";
    public static final String DEFAULT_ENVIRONMENT = "qa";

    // Assertion Messages
    public static final String ELEMENT_SHOULD_BE_VISIBLE = "Element should be visible but is not";
    public static final String ELEMENT_SHOULD_BE_ENABLED = "Element should be enabled but is not";
    public static final String ELEMENT_SHOULD_CONTAIN_TEXT = "Element should contain text: ";

    // Private constructor to prevent instantiation
    private Constants() {
    }
}
