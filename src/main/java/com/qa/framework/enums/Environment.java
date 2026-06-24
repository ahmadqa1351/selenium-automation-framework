package com.qa.framework.enums;

/**
 * Enum for supported environments
 */
public enum Environment {
    DEV("dev"),
    QA("qa"),
    UAT("uat"),
    PROD("prod");

    private final String environmentName;

    Environment(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * Get Environment from string value
     * @param value Environment name
     * @return Environment enum
     */
    public static Environment fromString(String value) {
        for (Environment env : Environment.values()) {
            if (env.environmentName.equalsIgnoreCase(value)) {
                return env;
            }
        }
        return QA; // Default to QA
    }
}
