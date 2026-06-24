package com.qa.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading and writing JSON test data
 * 
 * Features:
 * - Parse JSON arrays and objects
 * - Convert JSON to Maps and Lists
 * - Pretty print JSON
 * - JSON validation
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Thread-safe (ObjectMapper is thread-safe)
 */
public class JsonUtils {
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Read JSON array from file
     * Returns list of maps where each map represents a JSON object
     * 
     * @param filePath Path to JSON file
     * @return List of maps containing JSON data
     */
    public static List<Map<String, Object>> readJsonArray(String filePath) {
        try {
            List<Map<String, Object>> dataList = objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<List<Map<String, Object>>>() {}
            );
            logger.info("Successfully read {} objects from JSON file: {}", dataList.size(), filePath);
            return dataList;
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new FrameworkException("Failed to read JSON file", e);
        }
    }

    /**
     * Read JSON object from file
     * 
     * @param filePath Path to JSON file
     * @return Map containing JSON object data
     */
    public static Map<String, Object> readJsonObject(String filePath) {
        try {
            Map<String, Object> jsonData = objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<Map<String, Object>>() {}
            );
            logger.info("Successfully read JSON object from file: {}", filePath);
            return jsonData;
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new FrameworkException("Failed to read JSON file", e);
        }
    }

    /**
     * Read specific JSON object from array by index
     * 
     * @param filePath Path to JSON file
     * @param index Object index in array
     * @return Map containing JSON object data
     */
    public static Map<String, Object> readJsonObjectByIndex(String filePath, int index) {
        List<Map<String, Object>> dataList = readJsonArray(filePath);
        if (index >= 0 && index < dataList.size()) {
            return dataList.get(index);
        }
        logger.warn("Index {} out of bounds for JSON array", index);
        return new HashMap<>();
    }

    /**
     * Read JSON value by key path (supports nested keys)
     * Example: "user.name" to access nested object
     * 
     * @param filePath Path to JSON file
     * @param keyPath Key path (dot-separated for nested keys)
     * @return Value at key path
     */
    public static Object getValueByKeyPath(String filePath, String keyPath) {
        try {
            Map<String, Object> jsonData = readJsonObject(filePath);
            String[] keys = keyPath.split("\\.");
            Object value = jsonData;
            
            for (String key : keys) {
                if (value instanceof Map) {
                    value = ((Map<?, ?>) value).get(key);
                } else {
                    logger.warn("Cannot navigate key path: {}", keyPath);
                    return null;
                }
            }
            
            logger.debug("Retrieved value for key path '{}': {}", keyPath, value);
            return value;
        } catch (Exception e) {
            logger.error("Error retrieving value by key path: {}", keyPath, e);
            return null;
        }
    }

    /**
     * Write data to JSON file
     * 
     * @param filePath Path to JSON file
     * @param data Data to write (List or Map)
     */
    public static void writeJsonData(String filePath, Object data) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(filePath), data);
            logger.info("Successfully wrote data to JSON file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error writing to JSON file: {}", filePath, e);
            throw new FrameworkException("Failed to write to JSON file", e);
        }
    }

    /**
     * Convert JSON string to Map
     * 
     * @param jsonString JSON string
     * @return Map containing JSON data
     */
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        try {
            return objectMapper.readValue(
                    jsonString,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (IOException e) {
            logger.error("Error parsing JSON string", e);
            throw new FrameworkException("Failed to parse JSON string", e);
        }
    }

    /**
     * Convert JSON string to List
     * 
     * @param jsonString JSON string
     * @return List containing JSON data
     */
    public static List<Map<String, Object>> jsonStringToList(String jsonString) {
        try {
            return objectMapper.readValue(
                    jsonString,
                    new TypeReference<List<Map<String, Object>>>() {}
            );
        } catch (IOException e) {
            logger.error("Error parsing JSON string", e);
            throw new FrameworkException("Failed to parse JSON string", e);
        }
    }

    /**
     * Convert Object to JSON string
     * 
     * @param object Object to convert
     * @return JSON string representation
     */
    public static String objectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Error converting object to JSON", e);
            throw new FrameworkException("Failed to convert object to JSON", e);
        }
    }

    /**
     * Pretty print JSON string
     * 
     * @param jsonString JSON string
     * @return Formatted JSON string
     */
    public static String prettyPrintJson(String jsonString) {
        try {
            Object json = objectMapper.readValue(jsonString, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(json);
        } catch (IOException e) {
            logger.error("Error formatting JSON", e);
            return jsonString;
        }
    }
}
