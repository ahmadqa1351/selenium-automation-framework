package com.qa.framework.utils;

import com.qa.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading and writing CSV test data
 * 
 * Features:
 * - Parse CSV files
 * - Convert CSV rows to maps
 * - Write data to CSV files
 * - Handle quoted values with commas
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Not thread-safe for write operations
 */
public class CsvUtils {
    private static final Logger logger = LogManager.getLogger(CsvUtils.class);
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    /**
     * Read all data from CSV file
     * Returns list of maps where each map represents a row
     * 
     * @param filePath Path to CSV file
     * @return List of maps containing row data
     */
    public static List<Map<String, String>> readCsvData(String filePath) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String> headers = null;
            
            while ((line = br.readLine()) != null) {
                String[] values = parseCsvLine(line);
                
                if (headers == null) {
                    // First line is header
                    headers = new ArrayList<>();
                    for (String header : values) {
                        headers.add(header.trim());
                    }
                } else {
                    // Subsequent lines are data
                    Map<String, String> rowData = new HashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        String value = i < values.length ? values[i].trim() : "";
                        rowData.put(headers.get(i), value);
                    }
                    dataList.add(rowData);
                }
            }
            
            logger.info("Successfully read {} rows from CSV file: {}", dataList.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", filePath, e);
            throw new FrameworkException("Failed to read CSV file", e);
        }
        
        return dataList;
    }

    /**
     * Read specific rows from CSV file
     * 
     * @param filePath Path to CSV file
     * @param rowIndexes Row indexes to read (0-based, excluding header)
     * @return List of maps containing selected row data
     */
    public static List<Map<String, String>> readCsvDataByRows(String filePath, int... rowIndexes) {
        List<Map<String, String>> allData = readCsvData(filePath);
        List<Map<String, String>> selectedData = new ArrayList<>();
        
        for (int index : rowIndexes) {
            if (index >= 0 && index < allData.size()) {
                selectedData.add(allData.get(index));
            }
        }
        
        logger.debug("Retrieved {} selected rows from CSV", selectedData.size());
        return selectedData;
    }

    /**
     * Read single row from CSV
     * 
     * @param filePath Path to CSV file
     * @param rowIndex Row index to read (0-based, excluding header)
     * @return Map containing row data
     */
    public static Map<String, String> readCsvRow(String filePath, int rowIndex) {
        List<Map<String, String>> data = readCsvDataByRows(filePath, rowIndex);
        return data.isEmpty() ? new HashMap<>() : data.get(0);
    }

    /**
     * Parse CSV line handling quoted values
     * 
     * @param line CSV line
     * @return Array of values
     */
    private static String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            char nextChar = i < line.length() - 1 ? line.charAt(i + 1) : ' ';
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        values.add(current.toString());
        return values.toArray(new String[0]);
    }

    /**
     * Get number of rows in CSV file
     * 
     * @param filePath Path to CSV file
     * @return Number of rows (excluding header)
     */
    public static int getRowCount(String filePath) {
        int rowCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.readLine() != null) {
                rowCount++;
            }
            rowCount--; // Exclude header
            logger.debug("Row count in CSV file: {}", rowCount);
        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", filePath, e);
            return 0;
        }
        
        return rowCount;
    }

    /**
     * Write data to CSV file
     * 
     * @param filePath Path to CSV file
     * @param dataList List of maps containing data to write
     */
    public static void writeCsvData(String filePath, List<Map<String, String>> dataList) {
        if (dataList.isEmpty()) {
            logger.warn("No data to write");
            return;
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            List<String> headers = new ArrayList<>(dataList.get(0).keySet());
            bw.write(String.join(COMMA_DELIMITER, headers));
            bw.write(NEW_LINE_SEPARATOR);
            
            // Write data
            for (Map<String, String> rowData : dataList) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    String value = rowData.getOrDefault(header, "");
                    // Quote value if it contains comma or quote
                    if (value.contains(",") || value.contains("\"")) {
                        value = "\"" + value.replace("\"", "\\\"") + "\"";
                    }
                    values.add(value);
                }
                bw.write(String.join(COMMA_DELIMITER, values));
                bw.write(NEW_LINE_SEPARATOR);
            }
            
            logger.info("Successfully wrote {} rows to CSV file: {}", dataList.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error writing to CSV file: {}", filePath, e);
            throw new FrameworkException("Failed to write to CSV file", e);
        }
    }
}
