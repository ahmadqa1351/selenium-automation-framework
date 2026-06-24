package com.qa.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Utility class for string operations and manipulations
 * 
 * Features:
 * - String validation
 * - String generation
 * - String formatting
 * - Pattern matching
 * - Case conversion
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Thread-safe (no mutable state)
 */
public class CommonStringUtils {
    private static final Logger logger = LogManager.getLogger(CommonStringUtils.class);
    private static final Random random = new Random();

    // Common regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{10}$"
    );
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$"
    );

    /**
     * Generate random string of specified length
     * 
     * @param length Length of string
     * @return Random string
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        String randomString = sb.toString();
        logger.debug("Generated random string of length {}: {}", length, randomString);
        return randomString;
    }

    /**
     * Generate random number string of specified length
     * 
     * @param length Length of number
     * @return Random number string
     */
    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        
        String randomNumber = sb.toString();
        logger.debug("Generated random number of length {}: {}", length, randomNumber);
        return randomNumber;
    }

    /**
     * Generate random email
     * 
     * @return Random email
     */
    public static String generateRandomEmail() {
        String email = generateRandomString(10) + "@testmail.com";
        logger.debug("Generated random email: {}", email);
        return email;
    }

    /**
     * Generate random phone number
     * 
     * @return Random 10-digit phone number
     */
    public static String generateRandomPhoneNumber() {
        String phoneNumber = generateRandomNumber(10);
        logger.debug("Generated random phone number: {}", phoneNumber);
        return phoneNumber;
    }

    /**
     * Validate email format
     * 
     * @param email Email to validate
     * @return true if valid email
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        boolean isValid = EMAIL_PATTERN.matcher(email).matches();
        logger.debug("Email validation for '{}': {}", email, isValid);
        return isValid;
    }

    /**
     * Validate phone number format
     * 
     * @param phoneNumber Phone number to validate
     * @return true if valid phone number
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        boolean isValid = PHONE_PATTERN.matcher(phoneNumber).matches();
        logger.debug("Phone number validation for '{}': {}", phoneNumber, isValid);
        return isValid;
    }

    /**
     * Validate URL format
     * 
     * @param url URL to validate
     * @return true if valid URL
     */
    public static boolean isValidUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        boolean isValid = URL_PATTERN.matcher(url).matches();
        logger.debug("URL validation for '{}': {}", url, isValid);
        return isValid;
    }

    /**
     * Check if string contains only numbers
     * 
     * @param str String to check
     * @return true if string contains only numbers
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Check if string contains only alphabets
     * 
     * @param str String to check
     * @return true if string contains only alphabets
     */
    public static boolean isAlphabetic(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    /**
     * Convert string to title case
     * 
     * @param str String to convert
     * @return Title case string
     */
    public static String toTitleCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        return StringUtils.capitalize(str);
    }

    /**
     * Reverse string
     * 
     * @param str String to reverse
     * @return Reversed string
     */
    public static String reverseString(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Remove whitespace from string
     * 
     * @param str String to process
     * @return String without whitespace
     */
    public static String removeWhitespace(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * Replace multiple spaces with single space
     * 
     * @param str String to process
     * @return String with single spaces
     */
    public static String replaceMultipleSpaces(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", " ").trim();
    }

    /**
     * Check if string matches pattern
     * 
     * @param str String to check
     * @param pattern Regex pattern
     * @return true if string matches pattern
     */
    public static boolean matchesPattern(String str, String pattern) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(pattern)) {
            return false;
        }
        return Pattern.matches(pattern, str);
    }

    /**
     * Extract number from string
     * 
     * @param str String containing number
     * @return Extracted number as String, or empty string if not found
     */
    public static String extractNumber(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.replaceAll("[^0-9]", "");
    }
}
