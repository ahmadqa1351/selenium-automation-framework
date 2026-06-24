package com.qa.framework.enums;

/**
 * Enum for supported browser types
 * Supports Chrome, Firefox, Edge, and Safari
 */
public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari");

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    /**
     * Get BrowserType from string value
     * @param value Browser name
     * @return BrowserType enum
     */
    public static BrowserType fromString(String value) {
        for (BrowserType browser : BrowserType.values()) {
            if (browser.browserName.equalsIgnoreCase(value)) {
                return browser;
            }
        }
        return CHROME; // Default to Chrome
    }
}
