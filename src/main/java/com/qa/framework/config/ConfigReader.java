package com.qa.framework.config;

import com.qa.framework.enums.Environment;
import com.qa.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Reader for managing environment-specific properties
 * Supports configuration inheritance with environment-specific overrides
 *
 * Design Pattern: Singleton
 * Thread Safety: Thread-safe implementation
 */
public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;
    private Environment environment;

    private ConfigReader() {
        properties = new Properties();
        loadConfiguration();
    }

    /**
     * Get singleton instance of ConfigReader
     * @return ConfigReader instance
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    /**
     * Load configuration from properties files
     * Base config + environment-specific overrides
     */
    private void loadConfiguration() {
        try {
            // Load base configuration
            String baseConfigPath = "src/test/resources/config/config.properties";
            loadPropertiesFile(baseConfigPath);
            logger.info("Base configuration loaded from: {}", baseConfigPath);

            // Determine environment from system property or default
            String env = System.getProperty("environment", 
                    properties.getProperty("environment", "qa"));
            this.environment = Environment.fromString(env);

            // Load environment-specific configuration
            String envConfigPath = String.format(
                    "src/test/resources/config/config-%s.properties",
                    environment.getEnvironmentName()
            );
            loadPropertiesFile(envConfigPath);
            logger.info("Environment-specific configuration loaded for: {}", environment);

        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
            throw new FrameworkException("Configuration loading failed", e);
        }
    }

    /**
     * Load properties from file
     * @param filePath Path to properties file
     * @throws IOException If file cannot be read
     */
    private void loadPropertiesFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            logger.warn("Properties file not found: {}", filePath);
            // Don't throw exception for environment-specific files that might not exist
        }
    }

    /**
     * Get property value as String
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: {}", key);
            return null;
        }
        return value;
    }

    /**
     * Get property value with default
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get property value as Integer
     * @param key Property key
     * @return Property value as Integer
     */
    public Integer getPropertyAsInteger(String key) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Failed to parse property as integer: {}", key, e);
            throw new FrameworkException("Invalid integer property: " + key);
        }
    }

    /**
     * Get property value as Integer with default
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public Integer getPropertyAsInteger(String key, Integer defaultValue) {
        Integer value = getPropertyAsInteger(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get property value as Boolean
     * @param key Property key
     * @return Property value as Boolean
     */
    public Boolean getPropertyAsBoolean(String key) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Get property value as Boolean with default
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public Boolean getPropertyAsBoolean(String key, Boolean defaultValue) {
        Boolean value = getPropertyAsBoolean(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get current environment
     * @return Current environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Reset singleton instance (useful for testing)
     */
    public static synchronized void reset() {
        instance = null;
    }
}
