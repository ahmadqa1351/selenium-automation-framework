package com.qa.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class for date and time operations
 * 
 * Features:
 * - Date formatting and parsing
 * - Date calculations
 * - Date comparisons
 * - Various date formats support
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Thread-safe (LocalDateTime is immutable)
 */
public class DateUtils {
    private static final Logger logger = LogManager.getLogger(DateUtils.class);
    
    // Common date formats
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT_DD_MMM_YYYY = "dd MMM yyyy";

    /**
     * Get current date as string
     * 
     * @param format Date format
     * @return Current date as string
     */
    public static String getCurrentDate(String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String currentDate = LocalDate.now().format(formatter);
            logger.debug("Current date: {}", currentDate);
            return currentDate;
        } catch (Exception e) {
            logger.error("Error formatting current date", e);
            return null;
        }
    }

    /**
     * Get current date and time as string
     * 
     * @param format DateTime format
     * @return Current date and time as string
     */
    public static String getCurrentDateTime(String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String currentDateTime = LocalDateTime.now().format(formatter);
            logger.debug("Current date time: {}", currentDateTime);
            return currentDateTime;
        } catch (Exception e) {
            logger.error("Error formatting current date time", e);
            return null;
        }
    }

    /**
     * Get date after n days from today
     * 
     * @param days Number of days (positive or negative)
     * @param format Date format
     * @return Date after n days as string
     */
    public static String getDateAfterDays(int days, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String futureDate = LocalDate.now().plusDays(days).format(formatter);
            logger.debug("Date after {} days: {}", days, futureDate);
            return futureDate;
        } catch (Exception e) {
            logger.error("Error calculating date after days", e);
            return null;
        }
    }

    /**
     * Get date before n days from today
     * 
     * @param days Number of days
     * @param format Date format
     * @return Date before n days as string
     */
    public static String getDateBeforeDays(int days, String format) {
        return getDateAfterDays(-days, format);
    }

    /**
     * Parse string to LocalDate
     * 
     * @param dateString Date string
     * @param format Date format
     * @return LocalDate object
     */
    public static LocalDate parseDate(String dateString, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate date = LocalDate.parse(dateString, formatter);
            logger.debug("Parsed date: {}", date);
            return date;
        } catch (Exception e) {
            logger.error("Error parsing date: {}", dateString, e);
            return null;
        }
    }

    /**
     * Format LocalDate to string
     * 
     * @param date LocalDate object
     * @param format Date format
     * @return Formatted date string
     */
    public static String formatDate(LocalDate date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String formattedDate = date.format(formatter);
            logger.debug("Formatted date: {}", formattedDate);
            return formattedDate;
        } catch (Exception e) {
            logger.error("Error formatting date", e);
            return null;
        }
    }

    /**
     * Get number of days between two dates
     * 
     * @param startDate Start date string
     * @param endDate End date string
     * @param format Date format
     * @return Number of days between dates
     */
    public static long daysBetween(String startDate, String endDate, String format) {
        try {
            LocalDate start = parseDate(startDate, format);
            LocalDate end = parseDate(endDate, format);
            long days = ChronoUnit.DAYS.between(start, end);
            logger.debug("Days between {} and {}: {}", startDate, endDate, days);
            return days;
        } catch (Exception e) {
            logger.error("Error calculating days between dates", e);
            return 0;
        }
    }

    /**
     * Check if date1 is before date2
     * 
     * @param date1 First date string
     * @param date2 Second date string
     * @param format Date format
     * @return true if date1 is before date2
     */
    public static boolean isDateBefore(String date1, String date2, String format) {
        try {
            LocalDate d1 = parseDate(date1, format);
            LocalDate d2 = parseDate(date2, format);
            return d1.isBefore(d2);
        } catch (Exception e) {
            logger.error("Error comparing dates", e);
            return false;
        }
    }

    /**
     * Check if date1 is after date2
     * 
     * @param date1 First date string
     * @param date2 Second date string
     * @param format Date format
     * @return true if date1 is after date2
     */
    public static boolean isDateAfter(String date1, String date2, String format) {
        try {
            LocalDate d1 = parseDate(date1, format);
            LocalDate d2 = parseDate(date2, format);
            return d1.isAfter(d2);
        } catch (Exception e) {
            logger.error("Error comparing dates", e);
            return false;
        }
    }

    /**
     * Check if date1 equals date2
     * 
     * @param date1 First date string
     * @param date2 Second date string
     * @param format Date format
     * @return true if dates are equal
     */
    public static boolean isDateEqual(String date1, String date2, String format) {
        try {
            LocalDate d1 = parseDate(date1, format);
            LocalDate d2 = parseDate(date2, format);
            return d1.isEqual(d2);
        } catch (Exception e) {
            logger.error("Error comparing dates", e);
            return false;
        }
    }
}
